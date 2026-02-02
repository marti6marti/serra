#Ejercicio 5: Mini Aplicacion - Workflow Pattern: Parallelization

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
    input_tokens: int = 0
    output_tokens: int = 0
    reasoning_tokens: int = 0
    calls: int = 0
    details: list = field(default_factory=list)
    
    def add_usage(self, response, step_name: str):
        
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
        
        self.input_tokens += other.input_tokens
        self.output_tokens += other.output_tokens
        self.calls += other.calls
        self.details.extend(other.details)
    
    def get_total(self):
       
        return self.input_tokens + self.output_tokens
    
    def display_summary(self):
        
        print("\n" + "=" * 50)
        print("RESUMEN DE USO DE TOKENS")
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
    
    tracker = TokenTracker()
    prompt = f"""Analiza las HABILIDADES TECNICAS de este CV:

{cv_text}

Proporciona:
1. Lista de habilidades tecnicas identificadas
2. Nivel estimado de cada una (basico/intermedio/avanzado)
3. Habilidades tecnicas que podrian faltar segun el perfil
4. Puntuacion general de habilidades tecnicas (1-10)

Se especifico y conciso."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Analisis Tecnico")
    return response.content, tracker


def analyze_experience(cv_text: str) -> tuple:
    
    tracker = TokenTracker()
    prompt = f"""Analiza la EXPERIENCIA LABORAL de este CV:

{cv_text}

Proporciona:
1. Resumen de trayectoria profesional
2. Anos totales de experiencia estimados
3. Progresion de carrera (ascendente/estable/variada)
4. Logros destacables
5. Puntuacion de experiencia (1-10)

Se especifico y conciso."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Analisis Experiencia")
    return response.content, tracker


def analyze_education(cv_text: str) -> tuple:
    
    tracker = TokenTracker()
    prompt = f"""Analiza la FORMACION ACADEMICA de este CV:

{cv_text}

Proporciona:
1. Nivel educativo maximo alcanzado
2. Relevancia de la formacion para su carrera
3. Certificaciones o cursos adicionales
4. Areas de mejora en formacion
5. Puntuacion de formacion (1-10)

Se especifico y conciso."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Analisis Educacion")
    return response.content, tracker


def analyze_soft_skills(cv_text: str) -> tuple:
    
    tracker = TokenTracker()
    prompt = f"""Analiza las SOFT SKILLS (habilidades blandas) de este CV: {cv_text}

    Proporciona:
        1. Soft skills evidenciadas en el CV
        2. Indicadores de liderazgo o trabajo en equipo
        3. Capacidad de comunicacion inferida
        4. Soft skills que podria destacar mejor
        5. Puntuacion de soft skills (1-10)
    
    Se especifico y conciso."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Analisis Soft Skills")
    return response.content, tracker


def aggregate_cv_analysis(technical: str, experience: str, education: str, soft_skills: str, tracker: TokenTracker) -> str:
    
    prompt = f"""Como experto en recursos humanos, crea un INFORME EJECUTIVO basandote en estos analisis:

    HABILIDADES TECNICAS: {technical}
    
    EXPERIENCIA LABORAL: {experience}
    
    FORMACION ACADEMICA: {education}
    
    SOFT SKILLS: {soft_skills}
    
    Genera:
        1. RESUMEN EJECUTIVO (3-4 oraciones)
        2. FORTALEZAS PRINCIPALES (top 3)
        3. AREAS DE MEJORA (top 3)
        4. RECOMENDACIONES para el candidato
        5. ROLES SUGERIDOS donde podria destacar
        6. PUNTUACION GLOBAL del perfil (1-10)
    
    Formato profesional y accionable."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Agregacion Final")
    return response.content


def cv_analyzer_parallel(cv_text: str):
    main_tracker = TokenTracker()
    
    print("\n" + "=" * 60)
    print("ANALIZADOR DE CV - PARALLELIZATION")
    print("=" * 60)
    
    # Paso 1: Ejecutar analisis en paralelo
    print("\nEjecutando 4 analisis en paralelo...")
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
            print(f"  Completado: {key}")
    
    parallel_time = time.time() - start_time
    print(f"\nAnalisis paralelo completado en {parallel_time:.2f} segundos")
    
    # Mostrar resultados individuales
    print("\n" + "-" * 60)
    print("ANALISIS TECNICO:")
    print(results["technical"][:500] + "..." if len(results["technical"]) > 500 else results["technical"])
    
    print("\n" + "-" * 60)
    print("ANALISIS DE EXPERIENCIA:")
    print(results["experience"][:500] + "..." if len(results["experience"]) > 500 else results["experience"])
    
    print("\n" + "-" * 60)
    print("ANALISIS DE EDUCACION:")
    print(results["education"][:500] + "..." if len(results["education"]) > 500 else results["education"])
    
    print("\n" + "-" * 60)
    print("ANALISIS DE SOFT SKILLS:")
    print(results["soft_skills"][:500] + "..." if len(results["soft_skills"]) > 500 else results["soft_skills"])
    
    # Paso 2: Agregar resultados
    print("\n" + "=" * 60)
    print("Agregando analisis en informe final...")
    final_report = aggregate_cv_analysis(
        results["technical"],
        results["experience"],
        results["education"],
        results["soft_skills"],
        main_tracker
    )
    
    print("\n" + "=" * 60)
    print("INFORME EJECUTIVO FINAL")
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
    JUAN GARCIA LOPEZ
    Desarrollador Full Stack | Madrid, Espana
    Email: juan.garcia@email.com | LinkedIn: /in/juangarcia
    
    RESUMEN PROFESIONAL
    Desarrollador con 5 anos de experiencia en desarrollo web y aplicaciones moviles.
    Apasionado por la tecnologia y el aprendizaje continuo. Experiencia liderando equipos pequenos.
    
    EXPERIENCIA LABORAL
    
    Senior Developer - TechCorp Solutions (2021-2024)
    - Lidere equipo de 4 desarrolladores en proyecto de e-commerce
    - Implemente arquitectura microservicios reduciendo tiempos de carga 40%
    - Mentoria a desarrolladores junior
    
    Developer - StartupXYZ (2019-2021)
    - Desarrollo full stack con React y Node.js
    - Integracion de APIs de terceros
    - Metodologias agiles (Scrum)
    
    Junior Developer - WebAgency (2018-2019)
    - Desarrollo frontend con JavaScript y CSS
    - Mantenimiento de sitios WordPress
    
    EDUCACION
    Grado en Ingenieria Informatica - Universidad Politecnica de Madrid (2014-2018)
    Certificacion AWS Cloud Practitioner (2022)
    Curso de Machine Learning - Coursera (2023)
    
    HABILIDADES TECNICAS
    - Frontend: React, Vue.js, TypeScript, HTML5, CSS3
    - Backend: Node.js, Python, Java
    - Bases de datos: PostgreSQL, MongoDB, Redis
    - DevOps: Docker, Kubernetes, AWS, CI/CD
    - Otros: Git, Agile/Scrum
    
    IDIOMAS
    - Espanol (nativo)
    - Ingles (B2)
    """


def main():
    print("\n" + "=" * 60)
    print("  EJERCICIO 5: PARALLELIZATION - ANALIZADOR DE CV")
    print("=" * 60)
    
    print("\nAnalizando CV de ejemplo...\n")
    print("CV a analizar:")
    print("-" * 40)
    print(SAMPLE_CV[:500] + "...")
    print("-" * 40)
    
    # Ejecutar analisis
    result = cv_analyzer_parallel(SAMPLE_CV)
    
    print("\n" + "=" * 60)
    print("Quieres analizar otro CV? Pega el texto y presiona Enter dos veces.")
    print("Escribe 'salir' para terminar.")
    print("=" * 60)
    
    while True:
        print("\nPega tu CV (o 'salir'):")
        lines = []
        while True:
            line = input()
            if line.lower() == 'salir':
                print("\nHasta luego!\n")
                return
            if line == '' and lines and lines[-1] == '':
                break
            lines.append(line)
        
        cv_text = '\n'.join(lines[:-1])
        if cv_text.strip():
            cv_analyzer_parallel(cv_text)


if __name__ == "__main__":
    main()