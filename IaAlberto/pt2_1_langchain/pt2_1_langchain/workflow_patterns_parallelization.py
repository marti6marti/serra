"""
Ejercicio 5: Mini Aplicación - Workflow Pattern: Parallelization
=================================================================
Aplicación: Analizador de CV/Currículum Multi-Perspectiva

Este patrón ejecuta múltiples análisis en paralelo y luego
agrega los resultados:
- Análisis de habilidades técnicas
- Análisis de experiencia laboral
- Análisis de formación académica
- Análisis de soft skills
→ Agregación en recomendaciones finales

Incluye tracking de tokens para monitorear el consumo.

Autor: [Tu nombre]
Fecha: Diciembre 2024
"""

from dotenv import load_dotenv
from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage
from dataclasses import dataclass, field
from concurrent.futures import ThreadPoolExecutor, as_completed
import time
import os

load_dotenv()


@dataclass
class TokenTracker:
    """
    Clase para rastrear el uso de tokens a lo largo del pipeline.
    Thread-safe para uso con paralelización.
    """
    input_tokens: int = 0
    output_tokens: int = 0
    reasoning_tokens: int = 0
    calls: int = 0
    details: list = field(default_factory=list)
    
    def add_usage(self, response, step_name: str):
        """Añade el uso de tokens de una respuesta."""
        if hasattr(response, 'usage_metadata') and response.usage_metadata:
            usage = response.usage_metadata
            input_t = usage.get('input_tokens', 0)
            output_t = usage.get('output_tokens', 0)
            
            self.input_tokens += input_t
            self.output_tokens += output_t
            self.calls += 1
            
            self.details.append({
                'step': step_name,
                'input': input_t,
                'output': output_t
            })
    
    def merge(self, other: 'TokenTracker'):
        """Combina otro tracker con este (para paralelización)."""
        self.input_tokens += other.input_tokens
        self.output_tokens += other.output_tokens
        self.calls += other.calls
        self.details.extend(other.details)
    
    def get_total(self):
        """Retorna el total de tokens usados."""
        return self.input_tokens + self.output_tokens
    
    def display_summary(self):
        """Muestra resumen del uso de tokens."""
        print("\n" + "=" * 50)
        print("📊 RESUMEN DE USO DE TOKENS")
        print("=" * 50)
        for detail in self.details:
            print(f"  {detail['step']}: {detail['input']} in / {detail['output']} out")
        print("-" * 50)
        print(f"  TOTAL Input:  {self.input_tokens} tokens")
        print(f"  TOTAL Output: {self.output_tokens} tokens")
        print(f"  TOTAL:        {self.get_total()} tokens")
        print(f"  Llamadas API: {self.calls}")
        print("=" * 50)


# Inicializar modelo
model = ChatOpenAI(
    model="gpt-4o-mini",
    temperature=0.7,
    api_key=os.getenv("OPENAI_API_KEY")
)


def analyze_technical_skills(cv_text: str) -> tuple:
    """
    Tarea paralela 1: Analiza habilidades técnicas.
    """
    tracker = TokenTracker()
    prompt = f"""Analiza las HABILIDADES TÉCNICAS de este CV:

{cv_text}

Proporciona:
1. Lista de habilidades técnicas identificadas
2. Nivel estimado de cada una (básico/intermedio/avanzado)
3. Habilidades técnicas que podrían faltar según el perfil
4. Puntuación general de habilidades técnicas (1-10)

Sé específico y conciso."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Análisis Técnico")
    return response.content, tracker


def analyze_experience(cv_text: str) -> tuple:
    """
    Tarea paralela 2: Analiza experiencia laboral.
    """
    tracker = TokenTracker()
    prompt = f"""Analiza la EXPERIENCIA LABORAL de este CV:

{cv_text}

Proporciona:
1. Resumen de trayectoria profesional
2. Años totales de experiencia estimados
3. Progresión de carrera (ascendente/estable/variada)
4. Logros destacables
5. Puntuación de experiencia (1-10)

Sé específico y conciso."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Análisis Experiencia")
    return response.content, tracker


def analyze_education(cv_text: str) -> tuple:
    """
    Tarea paralela 3: Analiza formación académica.
    """
    tracker = TokenTracker()
    prompt = f"""Analiza la FORMACIÓN ACADÉMICA de este CV:

{cv_text}

Proporciona:
1. Nivel educativo máximo alcanzado
2. Relevancia de la formación para su carrera
3. Certificaciones o cursos adicionales
4. Áreas de mejora en formación
5. Puntuación de formación (1-10)

Sé específico y conciso."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Análisis Educación")
    return response.content, tracker


def analyze_soft_skills(cv_text: str) -> tuple:
    """
    Tarea paralela 4: Analiza soft skills.
    """
    tracker = TokenTracker()
    prompt = f"""Analiza las SOFT SKILLS (habilidades blandas) de este CV:

{cv_text}

Proporciona:
1. Soft skills evidenciadas en el CV
2. Indicadores de liderazgo o trabajo en equipo
3. Capacidad de comunicación inferida
4. Soft skills que podría destacar mejor
5. Puntuación de soft skills (1-10)

Sé específico y conciso."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Análisis Soft Skills")
    return response.content, tracker


def aggregate_cv_analysis(technical: str, experience: str, education: str, soft_skills: str, tracker: TokenTracker) -> str:
    """
    Agrega todos los análisis paralelos en recomendaciones finales.
    """
    prompt = f"""Como experto en recursos humanos, crea un INFORME EJECUTIVO basándote en estos análisis:

HABILIDADES TÉCNICAS:
{technical}

EXPERIENCIA LABORAL:
{experience}

FORMACIÓN ACADÉMICA:
{education}

SOFT SKILLS:
{soft_skills}

Genera:
1. RESUMEN EJECUTIVO (3-4 oraciones)
2. FORTALEZAS PRINCIPALES (top 3)
3. ÁREAS DE MEJORA (top 3)
4. RECOMENDACIONES para el candidato
5. ROLES SUGERIDOS donde podría destacar
6. PUNTUACIÓN GLOBAL del perfil (1-10)

Formato profesional y accionable."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Agregación Final")
    return response.content


def cv_analyzer_parallel(cv_text: str):
    """
    Pipeline principal de Parallelization para análisis de CV.
    
    Flujo:
    CV → [Técnico, Experiencia, Educación, Soft Skills] (paralelo) → Agregación
    
    Args:
        cv_text: Texto del CV a analizar
        
    Returns:
        dict: Resultados de cada análisis y recomendaciones
    """
    main_tracker = TokenTracker()
    
    print("\n" + "=" * 60)
    print("📄 ANALIZADOR DE CV - PARALLELIZATION")
    print("=" * 60)
    
    # Paso 1: Ejecutar análisis en paralelo
    print("\n⏳ Ejecutando 4 análisis en paralelo...")
    start_time = time.time()
    
    results = {}
    with ThreadPoolExecutor(max_workers=4) as executor:
        futures = {
            executor.submit(analyze_technical_skills, cv_text): "technical",
            executor.submit(analyze_experience, cv_text): "experience",
            executor.submit(analyze_education, cv_text): "education",
            executor.submit(analyze_soft_skills, cv_text): "soft_skills"
        }
        
        for future in as_completed(futures):
            key = futures[future]
            content, tracker = future.result()
            results[key] = content
            main_tracker.merge(tracker)
            print(f"  ✓ Completado: {key}")
    
    parallel_time = time.time() - start_time
    print(f"\n⚡ Análisis paralelo completado en {parallel_time:.2f} segundos")
    
    # Mostrar resultados individuales
    print("\n" + "-" * 60)
    print("🔧 ANÁLISIS TÉCNICO:")
    print(results["technical"][:500] + "..." if len(results["technical"]) > 500 else results["technical"])
    
    print("\n" + "-" * 60)
    print("💼 ANÁLISIS DE EXPERIENCIA:")
    print(results["experience"][:500] + "..." if len(results["experience"]) > 500 else results["experience"])
    
    print("\n" + "-" * 60)
    print("🎓 ANÁLISIS DE EDUCACIÓN:")
    print(results["education"][:500] + "..." if len(results["education"]) > 500 else results["education"])
    
    print("\n" + "-" * 60)
    print("🤝 ANÁLISIS DE SOFT SKILLS:")
    print(results["soft_skills"][:500] + "..." if len(results["soft_skills"]) > 500 else results["soft_skills"])
    
    # Paso 2: Agregar resultados
    print("\n" + "=" * 60)
    print("⏳ Agregando análisis en informe final...")
    final_report = aggregate_cv_analysis(
        results["technical"],
        results["experience"],
        results["education"],
        results["soft_skills"],
        main_tracker
    )
    
    print("\n" + "=" * 60)
    print("📋 INFORME EJECUTIVO FINAL")
    print("=" * 60)
    print(final_report)
    
    # Mostrar resumen de tokens
    main_tracker.display_summary()
    
    return {
        "technical": results["technical"],
        "experience": results["experience"],
        "education": results["education"],
        "soft_skills": results["soft_skills"],
        "final_report": final_report,
        "tokens": main_tracker
    }


# CV de ejemplo para pruebas
SAMPLE_CV = """
JUAN GARCÍA LÓPEZ
Desarrollador Full Stack | Madrid, España
Email: juan.garcia@email.com | LinkedIn: /in/juangarcia

RESUMEN PROFESIONAL
Desarrollador con 5 años de experiencia en desarrollo web y aplicaciones móviles.
Apasionado por la tecnología y el aprendizaje continuo. Experiencia liderando equipos pequeños.

EXPERIENCIA LABORAL

Senior Developer - TechCorp Solutions (2021-2024)
- Lideré equipo de 4 desarrolladores en proyecto de e-commerce
- Implementé arquitectura microservicios reduciendo tiempos de carga 40%
- Mentoría a desarrolladores junior

Developer - StartupXYZ (2019-2021)
- Desarrollo full stack con React y Node.js
- Integración de APIs de terceros
- Metodologías ágiles (Scrum)

Junior Developer - WebAgency (2018-2019)
- Desarrollo frontend con JavaScript y CSS
- Mantenimiento de sitios WordPress

EDUCACIÓN
Grado en Ingeniería Informática - Universidad Politécnica de Madrid (2014-2018)
Certificación AWS Cloud Practitioner (2022)
Curso de Machine Learning - Coursera (2023)

HABILIDADES TÉCNICAS
- Frontend: React, Vue.js, TypeScript, HTML5, CSS3
- Backend: Node.js, Python, Java
- Bases de datos: PostgreSQL, MongoDB, Redis
- DevOps: Docker, Kubernetes, AWS, CI/CD
- Otros: Git, Agile/Scrum

IDIOMAS
- Español (nativo)
- Inglés (B2)
"""


def main():
    """Función principal para demostrar el patrón Parallelization."""
    print("\n" + "=" * 60)
    print("  EJERCICIO 5: PARALLELIZATION - ANALIZADOR DE CV")
    print("=" * 60)
    
    print("\n📄 Analizando CV de ejemplo...\n")
    print("CV a analizar:")
    print("-" * 40)
    print(SAMPLE_CV[:500] + "...")
    print("-" * 40)
    
    # Ejecutar análisis
    result = cv_analyzer_parallel(SAMPLE_CV)
    
    print("\n" + "=" * 60)
    print("¿Quieres analizar otro CV? Pega el texto y presiona Enter dos veces.")
    print("Escribe 'salir' para terminar.")
    print("=" * 60)
    
    while True:
        print("\n📝 Pega tu CV (o 'salir'):")
        lines = []
        while True:
            line = input()
            if line.lower() == 'salir':
                print("\n👋 ¡Hasta luego!\n")
                return
            if line == '' and lines and lines[-1] == '':
                break
            lines.append(line)
        
        cv_text = '\n'.join(lines[:-1])  # Quitar último enter vacío
        if cv_text.strip():
            cv_analyzer_parallel(cv_text)


if __name__ == "__main__":
    main()
