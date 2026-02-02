# Pt 2.1 - Introducción a Aplicaciones LLM con LangChain

## Estructura del Proyecto

```
pt2_1_langchain/
├── .env                                    # Variables de entorno (API Key)
├── requirements.txt                        # Dependencias
├── README.md                               # Este archivo
│
├── chatbot.py                              # Ejercicio 3: Chatbot con memoria
│
├── workflow_patterns_chains.py             # Ejercicio 5: Prompt Chaining
├── workflow_patterns_router.py             # Ejercicio 5: Routing
├── workflow_patterns_parallelization.py    # Ejercicio 5: Parallelization
│
├── app.py                                  # Ejercicio 6: App consola combinada
└── gui_app.py                              # Ejercicio 6: App Streamlit
```

## Instalación

1. Crear entorno virtual:
```bash
python -m venv .venv
source .venv/bin/activate  # Linux/Mac
# o
.venv\Scripts\activate     # Windows
```

2. Instalar dependencias:
```bash
pip install -r requirements.txt
```

3. Configurar API Key:
   - Editar el archivo `.env`
   - Añadir tu API key de OpenAI: `OPENAI_API_KEY=sk-...`

## Ejecución

### Ejercicio 3: Chatbot con Memoria
```bash
python chatbot.py
```

### Ejercicio 5: Mini Aplicaciones de Workflow Patterns

**Prompt Chaining - Generador de Recetas:**
```bash
python workflow_patterns_chains.py
```

**Routing - Tutor Virtual Multi-Materia:**
```bash
python workflow_patterns_router.py
```

**Parallelization - Analizador de CV:**
```bash
python workflow_patterns_parallelization.py
```

### Ejercicio 6: Aplicación Combinada

**Versión Consola:**
```bash
python app.py
```

**Versión Streamlit (GUI):**
```bash
streamlit run gui_app.py
```

## Descripción de Aplicaciones

### Ejercicio 3: Chatbot con Memoria
- Chatbot conversacional usando LangChain
- Memoria implementada con lista de Python
- Comandos: `salir`, `memoria`, `limpiar`

### Ejercicio 5: Mini Apps

#### Prompt Chaining: Generador de Recetas
Genera recetas personalizadas en 4 pasos encadenados:
1. Analizar ingredientes
2. Sugerir tipo de plato
3. Generar receta completa
4. Crear lista de compras

#### Routing: Tutor Virtual
Clasifica preguntas y las dirige a tutores especializados:
- Matemáticas
- Ciencias
- Historia
- Idiomas
- General

#### Parallelization: Analizador de CV
Ejecuta 4 análisis en paralelo:
- Habilidades técnicas
- Experiencia laboral
- Formación académica
- Soft skills
→ Agrega resultados en informe ejecutivo

### Ejercicio 6: Asistente de Viajes
Combina **Routing** + **Prompt Chaining**:
- Router clasifica consultas (destino, actividades, presupuesto, clima)
- Handlers especializados responden cada categoría
- Generador de itinerarios usa encadenamiento de prompts
- Memoria de conversación y contexto del viaje
- Tracking global de tokens

## Token Tracking

Todas las aplicaciones (Ejercicio 5 y 6) incluyen seguimiento de tokens:
- `input_tokens`: Tokens de entrada
- `output_tokens`: Tokens de salida
- Total acumulado por sesión
- Desglose por operación

## Autor
[Tu nombre]

## Fecha
Diciembre 2024
