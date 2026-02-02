"""
Ejercicio 6: Aplicación Combinada Multi-Patrón (Streamlit)
===========================================================
Aplicación: Asistente de Viajes Inteligente - Interfaz Web

Combina dos patrones de workflow:
1. ROUTING: Clasifica consultas (destinos, actividades, presupuesto, clima)
2. PROMPT CHAINING: Genera itinerarios paso a paso

Incluye:
- Memoria de conversación con session_state
- Tracking global de tokens
- Interfaz gráfica con Streamlit

Ejecutar con: streamlit run gui_app.py

Autor: [Tu nombre]
Fecha: Diciembre 2024
"""

import streamlit as st
from dotenv import load_dotenv
from langchain_openai import ChatOpenAI
from langchain_core.messages import SystemMessage, HumanMessage, AIMessage
from dataclasses import dataclass, field
from typing import List, Dict, Any
import os

load_dotenv()

# ============================================================
# CONFIGURACIÓN DE PÁGINA
# ============================================================

st.set_page_config(
    page_title="✈️ Asistente de Viajes",
    page_icon="✈️",
    layout="wide"
)

# ============================================================
# TOKEN TRACKING
# ============================================================

@dataclass
class TokenTracker:
    """Tracker de tokens para la sesión."""
    input_tokens: int = 0
    output_tokens: int = 0
    total_calls: int = 0
    history: list = field(default_factory=list)
    
    def add_usage(self, response, operation: str):
        if hasattr(response, 'usage_metadata') and response.usage_metadata:
            usage = response.usage_metadata
            input_t = usage.get('input_tokens', 0)
            output_t = usage.get('output_tokens', 0)
            self.input_tokens += input_t
            self.output_tokens += output_t
            self.total_calls += 1
            self.history.append({'op': operation, 'in': input_t, 'out': output_t})
    
    def get_total(self) -> int:
        return self.input_tokens + self.output_tokens


# ============================================================
# INICIALIZACIÓN DE SESSION STATE
# ============================================================

def init_session_state():
    """Inicializa el estado de la sesión."""
    if 'messages' not in st.session_state:
        st.session_state.messages = []
    
    if 'travel_context' not in st.session_state:
        st.session_state.travel_context = {
            "destino": None,
            "fechas": None,
            "presupuesto": None,
            "preferencias": [],
            "itinerario": None
        }
    
    if 'token_tracker' not in st.session_state:
        st.session_state.token_tracker = TokenTracker()
    
    if 'model' not in st.session_state:
        st.session_state.model = ChatOpenAI(
            model="gpt-4o-mini",
            temperature=0.7,
            api_key=os.getenv("OPENAI_API_KEY")
        )


# ============================================================
# ROUTING
# ============================================================

def route_query(query: str) -> str:
    """Clasifica la consulta del usuario."""
    model = st.session_state.model
    tracker = st.session_state.token_tracker
    
    router_prompt = f"""Clasifica esta consulta de viaje en UNA categoría:
- 'destino': lugares, países, ciudades
- 'actividades': qué hacer, atracciones
- 'presupuesto': costos, precios
- 'clima': tiempo, temporadas
- 'itinerario': planificación día a día
- 'general': otras consultas

Consulta: {query}

Responde SOLO con la categoría."""

    response = model.invoke([HumanMessage(content=router_prompt)])
    tracker.add_usage(response, "Router")
    
    category = response.content.strip().lower()
    categories = ['destino', 'actividades', 'presupuesto', 'clima', 'itinerario', 'general']
    for cat in categories:
        if cat in category:
            return cat
    return 'general'


# ============================================================
# HANDLERS
# ============================================================

def get_context_summary() -> str:
    """Retorna resumen del contexto."""
    ctx = st.session_state.travel_context
    parts = []
    if ctx["destino"]:
        parts.append(f"Destino: {ctx['destino']}")
    if ctx["fechas"]:
        parts.append(f"Fechas: {ctx['fechas']}")
    if ctx["presupuesto"]:
        parts.append(f"Presupuesto: {ctx['presupuesto']}")
    return " | ".join(parts) if parts else "Sin información aún"


def handle_destination(query: str) -> str:
    model = st.session_state.model
    tracker = st.session_state.token_tracker
    context = get_context_summary()
    
    prompt = f"""Como experto en viajes, responde sobre destinos.
Contexto: {context}
Consulta: {query}

Proporciona información útil sobre el destino, mejores épocas, y tips."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Destino")
    
    # Extraer destino
    extract = model.invoke([HumanMessage(content=f"Extrae el destino de: '{query}'. Si no hay, responde NINGUNO.")])
    tracker.add_usage(extract, "Extracción")
    destino = extract.content.strip()
    if destino != 'NINGUNO' and len(destino) < 50:
        st.session_state.travel_context["destino"] = destino
    
    return response.content


def handle_activities(query: str) -> str:
    model = st.session_state.model
    tracker = st.session_state.token_tracker
    context = get_context_summary()
    
    prompt = f"""Recomienda actividades turísticas.
Contexto: {context}
Consulta: {query}

Lista las mejores actividades y experiencias."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Actividades")
    return response.content


def handle_budget(query: str) -> str:
    model = st.session_state.model
    tracker = st.session_state.token_tracker
    context = get_context_summary()
    
    prompt = f"""Asesora sobre presupuesto de viaje.
Contexto: {context}
Consulta: {query}

Da estimaciones de costos y tips de ahorro."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Presupuesto")
    return response.content


def handle_weather(query: str) -> str:
    model = st.session_state.model
    tracker = st.session_state.token_tracker
    context = get_context_summary()
    
    prompt = f"""Informa sobre clima y temporadas.
Contexto: {context}
Consulta: {query}

Indica clima típico, mejor época y qué llevar."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Clima")
    return response.content


def handle_general(query: str) -> str:
    model = st.session_state.model
    tracker = st.session_state.token_tracker
    context = get_context_summary()
    
    prompt = f"""Como asistente de viajes, responde:
Contexto: {context}
Consulta: {query}"""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "General")
    return response.content


# ============================================================
# PROMPT CHAINING: ITINERARIO
# ============================================================

def generate_itinerary() -> str:
    """Genera itinerario usando prompt chaining."""
    model = st.session_state.model
    tracker = st.session_state.token_tracker
    ctx = st.session_state.travel_context
    
    if not ctx["destino"]:
        return "❌ Primero dime a dónde quieres viajar para generar tu itinerario."
    
    progress = st.progress(0)
    status = st.empty()
    
    # Paso 1: Análisis
    status.text("📋 Paso 1/4: Analizando tu viaje...")
    progress.progress(25)
    
    step1 = model.invoke([HumanMessage(content=f"""Analiza este viaje:
- Destino: {ctx['destino']}
- Fechas: {ctx['fechas'] or 'Flexibles'}
- Presupuesto: {ctx['presupuesto'] or 'Medio'}

Determina duración recomendada y enfoque del viaje.""")])
    tracker.add_usage(step1, "Itinerario: Análisis")
    
    # Paso 2: Estructura
    status.text("📅 Paso 2/4: Estructurando días...")
    progress.progress(50)
    
    step2 = model.invoke([HumanMessage(content=f"""Basándote en:
{step1.content}

Crea estructura del itinerario: días, temas por día, zonas a visitar.""")])
    tracker.add_usage(step2, "Itinerario: Estructura")
    
    # Paso 3: Detalles
    status.text("🎯 Paso 3/4: Detallando actividades...")
    progress.progress(75)
    
    step3 = model.invoke([HumanMessage(content=f"""Detalla el itinerario para {ctx['destino']}:

{step2.content}

Para cada día: mañana, tarde, noche con actividades específicas y horarios.""")])
    tracker.add_usage(step3, "Itinerario: Detalles")
    
    # Paso 4: Tips
    status.text("💡 Paso 4/4: Añadiendo recomendaciones...")
    progress.progress(100)
    
    step4 = model.invoke([HumanMessage(content=f"""Para este itinerario:
{step3.content}

Añade: reservas necesarias, apps útiles, errores a evitar, presupuesto total estimado.""")])
    tracker.add_usage(step4, "Itinerario: Tips")
    
    status.empty()
    progress.empty()
    
    itinerary = f"""## 🗺️ Itinerario: {ctx['destino'].upper()}

{step3.content}

---

### 💡 Tips y Recomendaciones

{step4.content}
"""
    
    st.session_state.travel_context["itinerario"] = itinerary
    return itinerary


# ============================================================
# HANDLERS MAPPING
# ============================================================

HANDLERS = {
    'destino': handle_destination,
    'actividades': handle_activities,
    'presupuesto': handle_budget,
    'clima': handle_weather,
    'general': handle_general
}

CATEGORY_ICONS = {
    'destino': '🌍',
    'actividades': '🎯',
    'presupuesto': '💰',
    'clima': '🌤️',
    'itinerario': '🗺️',
    'general': '💬'
}


def process_message(user_input: str) -> str:
    """Procesa mensaje del usuario."""
    # Verificar si pide itinerario
    if 'itinerario' in user_input.lower() and any(word in user_input.lower() for word in ['genera', 'crea', 'hazme', 'quiero']):
        return generate_itinerary()
    
    # Routing
    category = route_query(user_input)
    
    # Handler
    if category == 'itinerario':
        return generate_itinerary()
    
    handler = HANDLERS.get(category, handle_general)
    response = handler(user_input)
    
    return f"{CATEGORY_ICONS.get(category, '💬')} **Categoría: {category.title()}**\n\n{response}"


# ============================================================
# INTERFAZ PRINCIPAL
# ============================================================

def main():
    init_session_state()
    
    # Header
    st.title("✈️ Asistente de Viajes Inteligente")
    st.markdown("*Combina Routing + Prompt Chaining para planificar tu viaje perfecto*")
    
    # Sidebar
    with st.sidebar:
        st.header("📋 Tu Viaje")
        
        ctx = st.session_state.travel_context
        st.markdown(f"**🌍 Destino:** {ctx['destino'] or 'No definido'}")
        st.markdown(f"**📅 Fechas:** {ctx['fechas'] or 'No definidas'}")
        st.markdown(f"**💰 Presupuesto:** {ctx['presupuesto'] or 'No definido'}")
        
        st.divider()
        
        # Inputs manuales
        st.subheader("⚙️ Configurar Viaje")
        new_dest = st.text_input("Destino", value=ctx['destino'] or "")
        if new_dest:
            st.session_state.travel_context['destino'] = new_dest
        
        new_dates = st.text_input("Fechas", value=ctx['fechas'] or "")
        if new_dates:
            st.session_state.travel_context['fechas'] = new_dates
        
        new_budget = st.selectbox("Presupuesto", ["", "Bajo", "Medio", "Alto", "Sin límite"])
        if new_budget:
            st.session_state.travel_context['presupuesto'] = new_budget
        
        st.divider()
        
        # Token tracker
        st.subheader("📊 Uso de Tokens")
        tracker = st.session_state.token_tracker
        col1, col2 = st.columns(2)
        col1.metric("Input", tracker.input_tokens)
        col2.metric("Output", tracker.output_tokens)
        st.metric("Total", tracker.get_total())
        st.caption(f"Llamadas API: {tracker.total_calls}")
        
        st.divider()
        
        # Botones
        if st.button("🗺️ Generar Itinerario", use_container_width=True):
            with st.spinner("Generando itinerario..."):
                response = generate_itinerary()
                st.session_state.messages.append({"role": "assistant", "content": response})
            st.rerun()
        
        if st.button("🔄 Nueva Conversación", use_container_width=True):
            st.session_state.messages = []
            st.session_state.travel_context = {
                "destino": None, "fechas": None, "presupuesto": None,
                "preferencias": [], "itinerario": None
            }
            st.session_state.token_tracker = TokenTracker()
            st.rerun()
    
    # Chat principal
    st.divider()
    
    # Mostrar historial
    for message in st.session_state.messages:
        with st.chat_message(message["role"]):
            st.markdown(message["content"])
    
    # Input del usuario
    if prompt := st.chat_input("¿A dónde te gustaría viajar?"):
        # Mostrar mensaje del usuario
        with st.chat_message("user"):
            st.markdown(prompt)
        st.session_state.messages.append({"role": "user", "content": prompt})
        
        # Procesar y mostrar respuesta
        with st.chat_message("assistant"):
            with st.spinner("Pensando..."):
                response = process_message(prompt)
            st.markdown(response)
        st.session_state.messages.append({"role": "assistant", "content": response})
        
        st.rerun()
    
    # Mensaje inicial si no hay conversación
    if not st.session_state.messages:
        st.info("""
        👋 ¡Hola! Soy tu asistente de viajes inteligente.
        
        Puedo ayudarte con:
        - 🌍 **Destinos** - Recomendaciones de lugares
        - 🎯 **Actividades** - Qué hacer y ver
        - 💰 **Presupuesto** - Costos y ahorro
        - 🌤️ **Clima** - Temporadas y qué llevar
        - 🗺️ **Itinerario** - Planificación día a día
        
        ¡Empieza diciéndome a dónde quieres viajar!
        """)


if __name__ == "__main__":
    main()
