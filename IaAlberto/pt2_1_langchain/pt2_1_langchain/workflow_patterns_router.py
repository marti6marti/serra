"""
Ejercicio 5: Mini Aplicación - Workflow Pattern: Routing
=========================================================
Aplicación: Tutor Virtual Multi-Materia

Este patrón utiliza un router que clasifica las consultas y las
dirige a manejadores especializados según la materia detectada:
- Matemáticas
- Ciencias
- Historia
- Idiomas
- General

Incluye tracking de tokens para monitorear el consumo.

Autor: [Tu nombre]
Fecha: Diciembre 2024
"""

from dotenv import load_dotenv
from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage
from dataclasses import dataclass, field
import os

load_dotenv()


@dataclass
class TokenTracker:
    """
    Clase para rastrear el uso de tokens a lo largo del pipeline.
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


def route_question(question: str, tracker: TokenTracker) -> str:
    """
    Router: Clasifica la pregunta en una categoría/materia.
    
    Args:
        question: Pregunta del estudiante
        tracker: Tracker de tokens
        
    Returns:
        str: Categoría detectada
    """
    router_prompt = f"""Clasifica esta pregunta de estudiante en UNA de estas materias:
    - 'matematicas' para cálculos, álgebra, geometría, estadística
    - 'ciencias' para física, química, biología, astronomía
    - 'historia' para eventos históricos, personajes, fechas, civilizaciones
    - 'idiomas' para gramática, vocabulario, traducción, literatura
    - 'general' para otros temas

Pregunta: {question}

Responde SOLO con el nombre de la materia (matematicas, ciencias, historia, idiomas o general)."""

    response = model.invoke([HumanMessage(content=router_prompt)])
    tracker.add_usage(response, "Router")
    
    category = response.content.strip().lower()
    
    # Normalizar respuesta
    if 'matem' in category:
        return 'matematicas'
    elif 'ciencia' in category or 'fisica' in category or 'quimica' in category or 'biolog' in category:
        return 'ciencias'
    elif 'histor' in category:
        return 'historia'
    elif 'idioma' in category or 'lengua' in category or 'ingles' in category:
        return 'idiomas'
    else:
        return 'general'


def handle_math(question: str, tracker: TokenTracker) -> str:
    """Manejador especializado para matemáticas."""
    prompt = f"""Eres un tutor experto en matemáticas. Responde a esta pregunta de forma educativa:

Pregunta: {question}

Proporciona:
1. Explicación clara del concepto
2. Paso a paso de la solución (si aplica)
3. Un ejemplo adicional para practicar
4. Tip para recordar el concepto

Usa formato claro y amigable para estudiantes."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: Matemáticas")
    return response.content


def handle_science(question: str, tracker: TokenTracker) -> str:
    """Manejador especializado para ciencias."""
    prompt = f"""Eres un tutor experto en ciencias (física, química, biología). Responde educativamente:

Pregunta: {question}

Proporciona:
1. Explicación del concepto científico
2. Principios o leyes relacionadas
3. Ejemplo o experimento ilustrativo
4. Aplicación en la vida real

Usa lenguaje accesible para estudiantes."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: Ciencias")
    return response.content


def handle_history(question: str, tracker: TokenTracker) -> str:
    """Manejador especializado para historia."""
    prompt = f"""Eres un tutor experto en historia. Responde de forma educativa y entretenida:

Pregunta: {question}

Proporciona:
1. Contexto histórico
2. Datos y fechas importantes
3. Personajes relevantes
4. Consecuencias o impacto histórico
5. Dato curioso relacionado

Haz la historia interesante y memorable."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: Historia")
    return response.content


def handle_languages(question: str, tracker: TokenTracker) -> str:
    """Manejador especializado para idiomas."""
    prompt = f"""Eres un tutor experto en idiomas y lingüística. Responde educativamente:

Pregunta: {question}

Proporciona:
1. Explicación gramatical o lingüística
2. Ejemplos de uso correcto
3. Errores comunes a evitar
4. Ejercicio práctico sugerido

Sé claro y proporciona ejemplos prácticos."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: Idiomas")
    return response.content


def handle_general(question: str, tracker: TokenTracker) -> str:
    """Manejador para preguntas generales."""
    prompt = f"""Eres un tutor amigable y conocedor. Responde esta pregunta de forma clara:

Pregunta: {question}

Proporciona una respuesta completa, educativa y fácil de entender."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: General")
    return response.content


# Mapeo de categorías a handlers
HANDLERS = {
    'matematicas': handle_math,
    'ciencias': handle_science,
    'historia': handle_history,
    'idiomas': handle_languages,
    'general': handle_general
}

# Emojis por categoría
CATEGORY_EMOJIS = {
    'matematicas': '🔢',
    'ciencias': '🔬',
    'historia': '📜',
    'idiomas': '📚',
    'general': '💡'
}


def tutor_router(question: str):
    """
    Pipeline principal de Routing para el tutor virtual.
    
    Flujo:
    Pregunta → Router (clasificación) → Handler especializado → Respuesta
    
    Args:
        question: Pregunta del estudiante
        
    Returns:
        dict: Resultado con categoría, respuesta y tokens
    """
    tracker = TokenTracker()
    
    print("\n" + "-" * 50)
    print(f"❓ Pregunta: {question}")
    print("-" * 50)
    
    # Paso 1: Routing - clasificar la pregunta
    print("\n⏳ Clasificando pregunta...")
    category = route_question(question, tracker)
    emoji = CATEGORY_EMOJIS.get(category, '📝')
    print(f"📂 Categoría detectada: {emoji} {category.upper()}")
    
    # Paso 2: Ejecutar handler especializado
    print(f"\n⏳ Procesando con tutor de {category}...")
    handler = HANDLERS.get(category, handle_general)
    response = handler(question, tracker)
    
    print(f"\n{emoji} RESPUESTA DEL TUTOR:")
    print("=" * 50)
    print(response)
    print("=" * 50)
    
    # Mostrar resumen de tokens
    tracker.display_summary()
    
    return {
        "category": category,
        "response": response,
        "tokens": tracker
    }


def main():
    """Función principal para demostrar el patrón Routing."""
    print("\n" + "=" * 60)
    print("  EJERCICIO 5: ROUTING - TUTOR VIRTUAL MULTI-MATERIA")
    print("=" * 60)
    print("\n🎓 ¡Bienvenido al Tutor Virtual!")
    print("Puedo ayudarte con: Matemáticas, Ciencias, Historia, Idiomas")
    print("Escribe 'salir' para terminar.\n")
    
    # Ejemplos de demostración
    example_questions = [
        "¿Cómo resuelvo una ecuación de segundo grado?",
        "¿Por qué el cielo es azul?",
        "¿Quién fue Napoleón Bonaparte?",
        "¿Cuándo se usa 'whom' en lugar de 'who' en inglés?"
    ]
    
    print("📝 Ejemplos de preguntas que puedo responder:")
    for i, q in enumerate(example_questions, 1):
        print(f"   {i}. {q}")
    
    print("\n" + "-" * 60)
    
    while True:
        question = input("\n🙋 Tu pregunta: ").strip()
        
        if not question:
            continue
        
        if question.lower() == 'salir':
            print("\n👋 ¡Hasta luego! Sigue aprendiendo.\n")
            break
        
        tutor_router(question)


if __name__ == "__main__":
    main()
