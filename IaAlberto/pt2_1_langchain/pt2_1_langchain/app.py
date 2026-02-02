"""
Ejercicio 6: Aplicación Combinada Multi-Patrón (Consola)
=========================================================
Aplicación: Asistente de Viajes Inteligente

Combina dos patrones de workflow:
1. ROUTING: Clasifica consultas (destinos, actividades, presupuesto, clima)
2. PROMPT CHAINING: Genera itinerarios paso a paso

Incluye:
- Memoria de conversación
- Tracking global de tokens
- Interfaz de consola interactiva

Autor: [Tu nombre]
Fecha: Diciembre 2024
"""

from dotenv import load_dotenv
from langchain_openai import ChatOpenAI
from langchain_core.messages import SystemMessage, HumanMessage, AIMessage
from dataclasses import dataclass, field
from typing import List, Dict, Any
import os

load_dotenv()


# ============================================================
# TOKEN TRACKING
# ============================================================

@dataclass
class GlobalTokenTracker:
    """
    Tracker global de tokens para toda la sesión.
    """
    input_tokens: int = 0
    output_tokens: int = 0
    reasoning_tokens: int = 0
    total_calls: int = 0
    session_history: list = field(default_factory=list)
    
    def add_usage(self, response, operation: str):
        """Registra uso de tokens de una operación."""
        if hasattr(response, 'usage_metadata') and response.usage_metadata:
            usage = response.usage_metadata
            input_t = usage.get('input_tokens', 0)
            output_t = usage.get('output_tokens', 0)
            
            self.input_tokens += input_t
            self.output_tokens += output_t
            self.total_calls += 1
            
            self.session_history.append({
                'operation': operation,
                'input': input_t,
                'output': output_t
            })
    
    def get_total(self) -> int:
        return self.input_tokens + self.output_tokens
    
    def display_current(self):
        """Muestra tokens de la última operación."""
        if self.session_history:
            last = self.session_history[-1]
            print(f"  📊 Tokens: {last['input']} in / {last['output']} out")
    
    def display_session_summary(self):
        """Muestra resumen de toda la sesión."""
        print("\n" + "=" * 60)
        print("📊 RESUMEN DE TOKENS DE LA SESIÓN")
        print("=" * 60)
        print(f"  Total Input:    {self.input_tokens} tokens")
        print(f"  Total Output:   {self.output_tokens} tokens")
        print(f"  TOTAL:          {self.get_total()} tokens")
        print(f"  Llamadas API:   {self.total_calls}")
        print("-" * 60)
        print("  Historial por operación:")
        for i, h in enumerate(self.session_history[-10:], 1):  # Últimas 10
            print(f"    {i}. {h['operation']}: {h['input']} in / {h['output']} out")
        print("=" * 60)


# ============================================================
# MEMORY MANAGEMENT
# ============================================================

class ConversationMemory:
    """
    Gestiona la memoria de la conversación.
    Almacena contexto del viaje y historial de mensajes.
    """
    
    def __init__(self):
        self.messages: List = []
        self.travel_context: Dict[str, Any] = {
            "destino": None,
            "fechas": None,
            "presupuesto": None,
            "preferencias": [],
            "itinerario": None
        }
        self._init_system_message()
    
    def _init_system_message(self):
        """Inicializa con mensaje del sistema."""
        self.messages = [
            SystemMessage(content="""Eres un asistente de viajes experto y amigable.
Ayudas a los usuarios a planificar sus viajes perfectos.
Recuerdas toda la información que el usuario comparte sobre su viaje.
Respondes en español de forma clara y útil.""")
        ]
    
    def add_user_message(self, content: str):
        """Añade mensaje del usuario."""
        self.messages.append(HumanMessage(content=content))
    
    def add_assistant_message(self, content: str):
        """Añade respuesta del asistente."""
        self.messages.append(AIMessage(content=content))
    
    def update_context(self, key: str, value: Any):
        """Actualiza contexto del viaje."""
        if key in self.travel_context:
            self.travel_context[key] = value
    
    def get_context_summary(self) -> str:
        """Retorna resumen del contexto actual."""
        ctx = self.travel_context
        parts = []
        if ctx["destino"]:
            parts.append(f"Destino: {ctx['destino']}")
        if ctx["fechas"]:
            parts.append(f"Fechas: {ctx['fechas']}")
        if ctx["presupuesto"]:
            parts.append(f"Presupuesto: {ctx['presupuesto']}")
        if ctx["preferencias"]:
            parts.append(f"Preferencias: {', '.join(ctx['preferencias'])}")
        
        return " | ".join(parts) if parts else "Sin información del viaje aún"
    
    def clear(self):
        """Limpia la memoria."""
        self.travel_context = {
            "destino": None,
            "fechas": None,
            "presupuesto": None,
            "preferencias": [],
            "itinerario": None
        }
        self._init_system_message()
    
    def display_context(self):
        """Muestra el contexto actual."""
        print("\n" + "-" * 40)
        print("📋 CONTEXTO DEL VIAJE:")
        print("-" * 40)
        ctx = self.travel_context
        print(f"  🌍 Destino: {ctx['destino'] or 'No definido'}")
        print(f"  📅 Fechas: {ctx['fechas'] or 'No definidas'}")
        print(f"  💰 Presupuesto: {ctx['presupuesto'] or 'No definido'}")
        print(f"  ⭐ Preferencias: {', '.join(ctx['preferencias']) if ctx['preferencias'] else 'Ninguna'}")
        print(f"  📝 Itinerario: {'Generado' if ctx['itinerario'] else 'No generado'}")
        print("-" * 40)


# ============================================================
# MODEL & ROUTING
# ============================================================

model = ChatOpenAI(
    model="gpt-4o-mini",
    temperature=0.7,
    api_key=os.getenv("OPENAI_API_KEY")
)


def route_query(query: str, tracker: GlobalTokenTracker) -> str:
    """
    PATRÓN ROUTING: Clasifica la consulta del usuario.
    
    Categorías:
    - destino: Preguntas sobre lugares, países, ciudades
    - actividades: Qué hacer, atracciones, tours
    - presupuesto: Costos, precios, ahorro
    - clima: Tiempo, temporadas, qué llevar
    - itinerario: Planificación día a día
    - general: Otras consultas de viaje
    """
    router_prompt = f"""Clasifica esta consulta de viaje en UNA categoría:
- 'destino': preguntas sobre lugares, países, ciudades, recomendaciones de destino
- 'actividades': qué hacer, atracciones, tours, experiencias
- 'presupuesto': costos, precios, ahorro, dinero
- 'clima': tiempo, temperatura, temporadas, qué ropa llevar
- 'itinerario': planificación, días, horarios, rutas
- 'general': otras consultas de viaje

Consulta: {query}

Responde SOLO con la categoría."""

    response = model.invoke([HumanMessage(content=router_prompt)])
    tracker.add_usage(response, "Router")
    
    category = response.content.strip().lower()
    
    # Normalizar
    categories = ['destino', 'actividades', 'presupuesto', 'clima', 'itinerario', 'general']
    for cat in categories:
        if cat in category:
            return cat
    return 'general'


# ============================================================
# SPECIALIZED HANDLERS
# ============================================================

def handle_destination(query: str, memory: ConversationMemory, tracker: GlobalTokenTracker) -> str:
    """Handler para consultas de destino."""
    context = memory.get_context_summary()
    
    prompt = f"""Como experto en viajes, responde sobre destinos.

Contexto del usuario: {context}
Consulta: {query}

Proporciona:
1. Información relevante del destino
2. Mejores épocas para visitar
3. Por qué es recomendable
4. Tip especial

Si el usuario menciona un destino específico, extráelo."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: Destino")
    
    # Intentar extraer destino mencionado
    extract_prompt = f"Del texto '{query}', extrae el nombre del destino/ciudad/país si se menciona. Si no hay ninguno, responde 'NINGUNO'."
    extract_response = model.invoke([HumanMessage(content=extract_prompt)])
    tracker.add_usage(extract_response, "Extracción destino")
    
    destino = extract_response.content.strip()
    if destino != 'NINGUNO' and len(destino) < 50:
        memory.update_context("destino", destino)
    
    return response.content


def handle_activities(query: str, memory: ConversationMemory, tracker: GlobalTokenTracker) -> str:
    """Handler para consultas de actividades."""
    context = memory.get_context_summary()
    
    prompt = f"""Como experto en viajes, recomienda actividades.

Contexto del viaje: {context}
Consulta: {query}

Proporciona:
1. Actividades recomendadas (top 5)
2. Experiencias imperdibles
3. Actividades según tipo de viajero
4. Reservas recomendadas

Personaliza según el destino si está definido."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: Actividades")
    return response.content


def handle_budget(query: str, memory: ConversationMemory, tracker: GlobalTokenTracker) -> str:
    """Handler para consultas de presupuesto."""
    context = memory.get_context_summary()
    
    prompt = f"""Como experto en viajes económicos, asesora sobre presupuesto.

Contexto del viaje: {context}
Consulta: {query}

Proporciona:
1. Estimación de costos (si aplica)
2. Tips de ahorro
3. Opciones por rango de presupuesto
4. Gastos típicos a considerar

Sé específico con números cuando sea posible."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: Presupuesto")
    
    # Intentar extraer presupuesto si se menciona
    if any(char.isdigit() for char in query):
        memory.update_context("presupuesto", query)
    
    return response.content


def handle_weather(query: str, memory: ConversationMemory, tracker: GlobalTokenTracker) -> str:
    """Handler para consultas de clima."""
    context = memory.get_context_summary()
    
    prompt = f"""Como experto en viajes, informa sobre clima y temporadas.

Contexto del viaje: {context}
Consulta: {query}

Proporciona:
1. Clima típico del destino (si conocido)
2. Mejor temporada para visitar
3. Qué ropa/equipaje llevar
4. Consideraciones especiales

Usa el destino del contexto si está disponible."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: Clima")
    return response.content


def handle_general(query: str, memory: ConversationMemory, tracker: GlobalTokenTracker) -> str:
    """Handler para consultas generales."""
    context = memory.get_context_summary()
    
    prompt = f"""Como asistente de viajes amigable, responde esta consulta.

Contexto del viaje: {context}
Historial reciente: {len(memory.messages)} mensajes
Consulta: {query}

Responde de forma útil y personalizada."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Handler: General")
    return response.content


# ============================================================
# PROMPT CHAINING: ITINERARY GENERATOR
# ============================================================

def generate_itinerary_chain(memory: ConversationMemory, tracker: GlobalTokenTracker) -> str:
    """
    PATRÓN PROMPT CHAINING: Genera itinerario en pasos encadenados.
    
    Cadena:
    1. Analizar contexto → 2. Estructura días → 3. Detallar actividades → 4. Tips finales
    """
    ctx = memory.travel_context
    
    if not ctx["destino"]:
        return "❌ Necesito saber el destino primero. ¿A dónde quieres viajar?"
    
    print("\n⏳ Generando itinerario personalizado...")
    
    # Paso 1: Analizar contexto
    print("  📋 Paso 1: Analizando tu viaje...")
    step1_prompt = f"""Analiza este contexto de viaje:
- Destino: {ctx['destino']}
- Fechas: {ctx['fechas'] or 'Flexibles'}
- Presupuesto: {ctx['presupuesto'] or 'Medio'}
- Preferencias: {', '.join(ctx['preferencias']) if ctx['preferencias'] else 'General'}

Determina:
1. Duración recomendada del viaje
2. Tipo de viajero
3. Enfoque del itinerario
4. Consideraciones especiales"""

    response1 = model.invoke([HumanMessage(content=step1_prompt)])
    tracker.add_usage(response1, "Itinerario: Análisis")
    analysis = response1.content
    
    # Paso 2: Estructurar días
    print("  📅 Paso 2: Estructurando días...")
    step2_prompt = f"""Basándote en este análisis:
{analysis}

Crea la ESTRUCTURA del itinerario:
- Número de días recomendados
- Tema/enfoque de cada día
- Distribución general de actividades
- Zonas/áreas a cubrir cada día

Solo estructura, sin detalles aún."""

    response2 = model.invoke([HumanMessage(content=step2_prompt)])
    tracker.add_usage(response2, "Itinerario: Estructura")
    structure = response2.content
    
    # Paso 3: Detallar actividades
    print("  🎯 Paso 3: Detallando actividades...")
    step3_prompt = f"""Ahora detalla el itinerario completo para {ctx['destino']}:

Estructura base:
{structure}

Para cada día incluye:
- Mañana: actividades específicas con horarios
- Tarde: actividades y experiencias
- Noche: cena y entretenimiento
- Transporte entre lugares
- Costos aproximados

Formato claro día por día."""

    response3 = model.invoke([HumanMessage(content=step3_prompt)])
    tracker.add_usage(response3, "Itinerario: Detalles")
    detailed = response3.content
    
    # Paso 4: Tips finales
    print("  💡 Paso 4: Agregando tips finales...")
    step4_prompt = f"""Para este itinerario en {ctx['destino']}:

{detailed}

Añade una sección de TIPS FINALES:
1. Reservas necesarias con anticipación
2. Apps útiles para el viaje
3. Frases básicas si hay otro idioma
4. Qué NO hacer (errores comunes)
5. Presupuesto total estimado

Resumen ejecutivo al inicio."""

    response4 = model.invoke([HumanMessage(content=step4_prompt)])
    tracker.add_usage(response4, "Itinerario: Tips")
    final_tips = response4.content
    
    # Combinar resultado final
    itinerary = f"""
{'=' * 60}
🗺️ ITINERARIO PERSONALIZADO: {ctx['destino'].upper()}
{'=' * 60}

{detailed}

{'=' * 60}
💡 TIPS Y RECOMENDACIONES
{'=' * 60}

{final_tips}
"""
    
    memory.update_context("itinerario", itinerary)
    print("  ✅ ¡Itinerario generado!")
    
    return itinerary


# ============================================================
# MAIN APPLICATION
# ============================================================

CATEGORY_EMOJIS = {
    'destino': '🌍',
    'actividades': '🎯',
    'presupuesto': '💰',
    'clima': '🌤️',
    'itinerario': '🗺️',
    'general': '💬'
}

HANDLERS = {
    'destino': handle_destination,
    'actividades': handle_activities,
    'presupuesto': handle_budget,
    'clima': handle_weather,
    'general': handle_general
}


def process_query(query: str, memory: ConversationMemory, tracker: GlobalTokenTracker) -> str:
    """
    Procesa una consulta del usuario usando routing + handlers.
    """
    # Comando especial para itinerario
    if 'itinerario' in query.lower() and ('genera' in query.lower() or 'crea' in query.lower() or 'hazme' in query.lower()):
        return generate_itinerary_chain(memory, tracker)
    
    # Routing
    category = route_query(query, tracker)
    emoji = CATEGORY_EMOJIS.get(category, '💬')
    print(f"  📂 Categoría: {emoji} {category}")
    
    # Handler apropiado
    if category == 'itinerario':
        return generate_itinerary_chain(memory, tracker)
    
    handler = HANDLERS.get(category, handle_general)
    return handler(query, memory, tracker)


def display_welcome():
    """Muestra mensaje de bienvenida."""
    print("\n" + "=" * 60)
    print("  ✈️  ASISTENTE DE VIAJES INTELIGENTE  ✈️")
    print("=" * 60)
    print("""
  Combina ROUTING + PROMPT CHAINING para ayudarte a planificar.
  
  Puedo ayudarte con:
  🌍 Destinos     - Recomendaciones de lugares
  🎯 Actividades  - Qué hacer y ver
  💰 Presupuesto  - Costos y ahorro
  🌤️ Clima        - Temporadas y qué llevar
  🗺️ Itinerario   - Planificación día a día
  
  Comandos especiales:
  'contexto'  - Ver información guardada de tu viaje
  'itinerario' - Generar itinerario completo
  'tokens'    - Ver uso de tokens
  'limpiar'   - Reiniciar conversación
  'salir'     - Terminar
""")
    print("=" * 60)


def main():
    """Función principal de la aplicación."""
    # Inicializar componentes
    memory = ConversationMemory()
    tracker = GlobalTokenTracker()
    
    display_welcome()
    
    print("\n💬 ¿A dónde te gustaría viajar?\n")
    
    while True:
        try:
            user_input = input("Tú: ").strip()
            
            if not user_input:
                continue
            
            # Comandos especiales
            if user_input.lower() == 'salir':
                tracker.display_session_summary()
                print("\n✈️ ¡Buen viaje! Hasta pronto.\n")
                break
            
            if user_input.lower() == 'contexto':
                memory.display_context()
                continue
            
            if user_input.lower() == 'tokens':
                tracker.display_session_summary()
                continue
            
            if user_input.lower() == 'limpiar':
                memory.clear()
                print("\n🔄 Conversación reiniciada.\n")
                continue
            
            # Añadir a memoria
            memory.add_user_message(user_input)
            
            # Procesar consulta
            print()
            response = process_query(user_input, memory, tracker)
            
            # Añadir respuesta a memoria
            memory.add_assistant_message(response)
            
            # Mostrar respuesta
            print(f"\n🤖 Asistente:\n{response}\n")
            
            # Mostrar tokens usados
            tracker.display_current()
            print()
            
        except KeyboardInterrupt:
            tracker.display_session_summary()
            print("\n\n✈️ ¡Hasta pronto!\n")
            break
        except Exception as e:
            print(f"\n❌ Error: {e}\n")
            continue


if __name__ == "__main__":
    main()
