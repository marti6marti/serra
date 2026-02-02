# Ejercicio 5: Mini Aplicacion - Workflow Pattern: Routing

from dotenv import load_dotenv
from langchain_openai import ChatOpenAI
from langchain_core.messages import HumanMessage
from dataclasses import dataclass, field
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


def route_question(question: str, tracker: TokenTracker) -> str:
    router_prompt = f"""Clasifica esta pregunta de estudiante en UNA de estas materias:
    - 'matematicas' para calculos, algebra, geometria, estadistica
    - 'ciencias' para fisica, quimica, biologia, astronomia
    - 'historia' para eventos historicos, personajes, fechas, civilizaciones
    - 'idiomas' para gramatica, vocabulario, traduccion, literatura
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
    prompt = f"""Eres un tutor experto en matematicas. Responde a esta pregunta de forma educativa:

    Pregunta: {question}

    Proporciona:
    1. Explicacion clara del concepto
    2. Paso a paso de la solucion (si aplica)
    3. Un ejemplo adicional para practicar
    4. Tip para recordar el concepto

    Usa formato claro y amigable para estudiantes."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: Matematicas")
    return response.content


def handle_science(question: str, tracker: TokenTracker) -> str:
    prompt = f"""Eres un tutor experto en ciencias (fisica, quimica, biologia). Responde educativamente:

    Pregunta: {question}

    Proporciona:
    1. Explicacion del concepto cientifico
    2. Principios o leyes relacionadas
    3. Ejemplo o experimento ilustrativo
    4. Aplicacion en la vida real

    Usa lenguaje accesible para estudiantes."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: Ciencias")
    return response.content


def handle_history(question: str, tracker: TokenTracker) -> str:
    prompt = f"""Eres un tutor experto en historia. Responde de forma educativa y entretenida:

    Pregunta: {question}

    Proporciona:
    1. Contexto historico
    2. Datos y fechas importantes
    3. Personajes relevantes
    4. Consecuencias o impacto historico
    5. Dato curioso relacionado

    Haz la historia interesante y memorable."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: Historia")
    return response.content


def handle_languages(question: str, tracker: TokenTracker) -> str:
    prompt = f"""Eres un tutor experto en idiomas y linguistica. Responde educativamente:

    Pregunta: {question}

    Proporciona:
    1. Explicacion gramatical o linguistica
    2. Ejemplos de uso correcto
    3. Errores comunes a evitar
    4. Ejercicio practico sugerido

    Se claro y proporciona ejemplos practicos."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: Idiomas")
    return response.content


def handle_general(question: str, tracker: TokenTracker) -> str:
    prompt = f"""Eres un tutor amigable y conocedor. Responde esta pregunta de forma clara:

    Pregunta: {question}

    Proporciona una respuesta completa, educativa y facil de entender."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: General")
    return response.content


# Mapeo de categorias a handlers
HANDLERS = {
    'matematicas': handle_math,
    'ciencias': handle_science,
    'historia': handle_history,
    'idiomas': handle_languages,
    'general': handle_general
}


def tutor_router(question: str):
    tracker = TokenTracker()

    print("\n" + "-" * 50)
    print(f"Pregunta: {question}")
    print("-" * 50)

    # Paso 1: Routing - clasificar la pregunta
    print("\nClasificando pregunta...")
    category = route_question(question, tracker)

    print(f"Categoria detectada: {category.upper()}")

    # Paso 2: Ejecutar handler especializado
    print(f"\nProcesando con tutor de {category}...")
    handler = HANDLERS.get(category, handle_general)
    response = handler(question, tracker)

    print(f"\nRESPUESTA DEL TUTOR:")
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
    """Funcion principal para demostrar el patron Routing."""
    print("\n" + "=" * 60)
    print("  EJERCICIO 5: ROUTING - TUTOR VIRTUAL MULTI-MATERIA")
    print("=" * 60)
    print("\nBienvenido al Tutor Virtual!")
    print("Puedo ayudarte con: Matematicas, Ciencias, Historia, Idiomas")
    print("Escribe 'salir' para terminar.\n")

    # Ejemplos de demostracion
    example_questions = [
        "Como resuelvo una ecuacion de segundo grado?",
        "Por que el cielo es azul?",
        "Quien fue Napoleon Bonaparte?",
        "Cuando se usa 'whom' en lugar de 'who' en ingles?"
    ]

    print("Ejemplos de preguntas que puedo responder:")
    for i, q in enumerate(example_questions, 1):
        print(f"   {i}. {q}")

    print("\n" + "-" * 60)

    while True:
        question = input("\nTu pregunta: ").strip()

        if not question:
            continue

        if question.lower() == 'salir':
            print("\nHasta luego! Sigue aprendiendo.\n")
            break

        tutor_router(question)


if __name__ == "__main__":
    main()