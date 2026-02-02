# INFORME TEÓRICO
## Pt 2.1 - Introducción a Aplicaciones LLM con LangChain

**Fecha:** Diciembre 2024

---

## 1. Identificación del Equipo

### Miembros del Equipo
- Serhii
- Martí

---

## 2. Respuestas a las Preguntas Teóricas

### Ejercicio 1: Modelos Sin Razonamiento vs. Modelos con Razonamiento

#### Principales Distinciones

Los modelos de lenguaje grande (LLM) pueden clasificarse en dos categorías fundamentales según su capacidad de procesamiento cognitivo:

**Modelos Sin Razonamiento (Non-Reasoning)**

Son modelos que generan respuestas de manera directa basándose en patrones estadísticos aprendidos durante el entrenamiento. Procesan el input y producen output sin pasos intermedios de deliberación explícita. Funcionan mediante predicción del siguiente token más probable dado el contexto, sin mantener un "proceso de pensamiento" interno visible o estructurado.

**Modelos con Razonamiento (Reasoning)**

Son modelos diseñados para descomponer problemas complejos en pasos intermedios antes de llegar a una conclusión. Utilizan técnicas como Chain-of-Thought (CoT), donde el modelo genera explícitamente su proceso de razonamiento. Ejemplos incluyen modelos como o1 de OpenAI, que dedican "tiempo de pensamiento" antes de responder.

#### Casos de Uso Típicos

**Modelos Sin Razonamiento:**
- Generación de texto creativo (historias, poemas, contenido marketing)
- Traducción de idiomas
- Resúmenes de texto
- Chatbots conversacionales simples
- Clasificación de texto y análisis de sentimiento
- Autocompletado y sugerencias de escritura

**Modelos con Razonamiento:**
- Resolución de problemas matemáticos complejos
- Programación y depuración de código
- Análisis lógico y deductivo
- Planificación y toma de decisiones multi-paso
- Problemas científicos que requieren razonamiento estructurado
- Tareas que requieren verificación y autocorrección

#### Implicaciones para el Diseño de Aplicaciones LLM

1. **Selección del modelo:** Elegir el tipo de modelo según la complejidad de la tarea. Para tareas simples y de alta velocidad, modelos sin razonamiento son más eficientes y económicos.

2. **Diseño de prompts:** Los modelos con razonamiento se benefician de instrucciones que soliciten explícitamente el proceso paso a paso. Los modelos sin razonamiento requieren prompts más directos y específicos.

3. **Gestión de latencia:** Los modelos con razonamiento tienen mayor latencia debido al procesamiento adicional. Las aplicaciones en tiempo real pueden preferir modelos sin razonamiento.

4. **Costos:** Los modelos con razonamiento consumen más tokens (por los pasos intermedios), impactando costos operativos.

5. **Arquitectura de aplicación:** Aplicaciones complejas pueden combinar ambos tipos, usando modelos rápidos para tareas simples y modelos de razonamiento para decisiones críticas.

---

### Ejercicio 2: Técnicas de Prompting para Aplicaciones No Agénticas

#### 1. Estrategias Básicas de Prompting

**a) Instruction Prompts (Prompts de Instrucción)**

Son prompts que dan instrucciones directas y claras al modelo sobre qué tarea realizar. Constituyen la forma más básica y común de interactuar con un LLM.

*Ejemplo: "Traduce el siguiente texto al francés: [texto]"*

**b) Zero-Shot Prompting**

Técnica donde se solicita al modelo realizar una tarea sin proporcionar ejemplos previos. El modelo debe inferir qué hacer basándose únicamente en la instrucción y su conocimiento preentrenado.

*Ejemplo: "Clasifica el sentimiento de esta reseña como positivo, negativo o neutral: [reseña]"*

**c) Few-Shot Prompting**

Técnica que proporciona al modelo varios ejemplos del formato entrada-salida deseado antes de presentar la tarea real. Esto ayuda al modelo a entender mejor el patrón esperado.

*Ejemplo:*
```
Convierte a mayúsculas:
hola → HOLA
mundo → MUNDO
python →
```

**d) Format-Constrained Prompts (Prompts con Formato Restringido)**

Prompts que especifican explícitamente el formato de salida esperado, como JSON, XML, listas numeradas, tablas, etc. Esto facilita el procesamiento programático de las respuestas.

*Ejemplo: "Extrae la información del producto y devuélvela en formato JSON con los campos: nombre, precio, categoría."*

#### 2. Propósito de Cada Técnica

**Instruction Prompts:**
- **Propósito:** Comunicar de forma clara y directa la tarea deseada
- **Logra:** Respuestas enfocadas y relevantes a la solicitud específica

**Zero-Shot:**
- **Propósito:** Aprovechar el conocimiento general del modelo sin configuración adicional
- **Logra:** Rapidez en implementación y flexibilidad para tareas variadas

**Few-Shot:**
- **Propósito:** Guiar al modelo mediante ejemplos para mejorar precisión
- **Logra:** Mayor consistencia en el formato y estilo de las respuestas, especialmente útil para tareas específicas de dominio

**Format-Constrained:**
- **Propósito:** Garantizar que la salida sea procesable programáticamente
- **Logra:** Integración fluida con sistemas downstream y parsing confiable de respuestas

#### 3. Recomendaciones y Mejores Prácticas

1. **Ser específico y claro:** Evitar ambigüedades en las instrucciones. Cuanto más específico sea el prompt, más predecible será la respuesta.

2. **Proporcionar contexto relevante:** Incluir información de fondo necesaria para que el modelo entienda la tarea completamente.

3. **Definir el rol del modelo:** Usar frases como "Actúa como un experto en..." para establecer el tono y nivel de expertise esperado.

4. **Especificar el formato de salida:** Indicar explícitamente cómo debe estructurarse la respuesta (lista, párrafo, JSON, etc.).

5. **Usar delimitadores:** Separar claramente las diferentes partes del prompt usando marcadores como comillas, corchetes o etiquetas XML.

6. **Iterar y refinar:** Los prompts rara vez son perfectos en el primer intento. Probar, evaluar y ajustar iterativamente.

7. **Considerar la longitud:** Prompts muy largos pueden confundir al modelo; prompts muy cortos pueden ser ambiguos. Encontrar el balance adecuado.

8. **Incluir restricciones negativas:** Indicar qué NO debe hacer el modelo (ej: "No incluyas explicaciones adicionales").

---

## 3. Documentación de la Implementación

### Ejercicio 3: Chatbot con Memoria

#### Descripción de la Aplicación

Se desarrolló un chatbot basado en consola utilizando LangChain que mantiene el contexto de la conversación a través de múltiples turnos. El chatbot permite interacciones naturales en español y recuerda lo que el usuario ha dicho previamente en la sesión.

#### Decisiones de Diseño

1. **Estructura de memoria:** Se utilizó una lista de Python para almacenar los mensajes, siguiendo el patrón recomendado de LangChain con objetos SystemMessage, HumanMessage y AIMessage. Esta estructura es simple, eficiente y permite fácil acceso al historial completo.

2. **Mensaje del sistema:** Se definió un SystemMessage inicial que establece el comportamiento del asistente (amigable, responde en español, mantiene contexto).

3. **Modelo seleccionado:** gpt-4o-mini por su balance entre costo, velocidad y calidad de respuestas.

4. **Comandos especiales:** Se implementaron comandos ('salir', 'memoria', 'limpiar') para control de la sesión sin interferir con la conversación normal.

#### Dificultades Encontradas

- **Gestión de errores de API:** Se resolvió implementando bloques try-catch para manejar fallos de conexión sin terminar el programa.
- **Acumulación de memoria:** En conversaciones muy largas, el contexto puede exceder el límite de tokens. Para una versión de producción se podría implementar un sistema de ventana deslizante.

---

### Ejercicio 5: Mini Aplicaciones de Workflow Patterns

#### Aplicación 1: Generador de Recetas (Prompt Chaining)

**Descripción:**
Aplicación que genera recetas personalizadas a partir de ingredientes disponibles, utilizando encadenamiento de prompts en 4 pasos secuenciales.

**Flujo del Patrón:**
```
Ingredientes → Análisis → Sugerencia de plato → Receta completa → Lista de compras
```

**Justificación del Patrón:**
El Prompt Chaining es ideal para este caso porque cada paso requiere la información del anterior: no se puede generar una receta sin saber qué plato preparar, y no se puede sugerir un plato sin analizar primero los ingredientes disponibles.

#### Aplicación 2: Tutor Virtual Multi-Materia (Routing)

**Descripción:**
Aplicación educativa que clasifica preguntas de estudiantes y las dirige a tutores especializados en diferentes materias (matemáticas, ciencias, historia, idiomas).

**Flujo del Patrón:**
```
Pregunta → Router (clasificación) → Handler especializado → Respuesta educativa
```

**Justificación del Patrón:**
El Routing permite que cada materia tenga un prompt optimizado para su dominio específico. Un tutor de matemáticas necesita mostrar pasos de resolución, mientras que uno de historia necesita proporcionar contexto temporal y personajes relevantes.

#### Aplicación 3: Analizador de CV (Parallelization)

**Descripción:**
Aplicación que analiza currículums desde múltiples perspectivas simultáneas (habilidades técnicas, experiencia, educación, soft skills) y genera un informe ejecutivo consolidado.

**Flujo del Patrón:**
```
CV → [Técnico | Experiencia | Educación | Soft Skills] (paralelo) → Agregación → Informe final
```

**Justificación del Patrón:**
La paralelización reduce significativamente el tiempo de análisis ya que las 4 evaluaciones son independientes entre sí. Usar ThreadPoolExecutor permite ejecutar las llamadas API concurrentemente, reduciendo el tiempo total de ~12 segundos (secuencial) a ~4 segundos (paralelo).

---

### Ejercicio 6: Aplicación Combinada - Asistente de Viajes

#### Descripción de la Aplicación

Se desarrolló un asistente de viajes inteligente que combina dos patrones de workflow (Routing y Prompt Chaining) para ayudar a los usuarios a planificar viajes. La aplicación está disponible en dos versiones: consola (app.py) y web con Streamlit (gui_app.py).

#### Integración de Patrones

**Patrón 1 - Routing:**
- Clasifica las consultas del usuario en categorías: destino, actividades, presupuesto, clima, itinerario, general
- Cada categoría tiene un handler especializado con prompts optimizados
- Permite respuestas más precisas y contextuales según el tipo de pregunta

**Patrón 2 - Prompt Chaining (Generación de Itinerarios):**
- Paso 1: Análisis del contexto del viaje (destino, fechas, presupuesto, preferencias)
- Paso 2: Estructuración de días y temas
- Paso 3: Detalle de actividades por día (mañana, tarde, noche)
- Paso 4: Tips finales y recomendaciones prácticas

#### Mecanismo de Memoria

La memoria se implementó en dos niveles:
- **Historial de mensajes:** Lista de HumanMessage/AIMessage que permite conversaciones multi-turno coherentes
- **Contexto del viaje:** Diccionario que almacena información específica (destino, fechas, presupuesto, preferencias) extraída automáticamente de las conversaciones

#### Decisiones de Diseño

1. **Extracción automática de contexto:** Cuando el usuario menciona un destino, el sistema lo extrae y almacena automáticamente para personalizar respuestas futuras.

2. **Interfaz dual:** La versión consola permite testing rápido; la versión Streamlit ofrece mejor experiencia de usuario con sidebar para contexto y métricas.

3. **Session state en Streamlit:** Se utilizó st.session_state para persistir memoria, contexto y tokens entre recargas de página.

4. **Feedback visual:** En Streamlit, se muestra progreso durante la generación de itinerarios y uso de tokens en tiempo real.

---

## 4. Discusión sobre el Uso de Tokens

### Mecanismo de Tracking Implementado

Se implementó una clase TokenTracker (dataclass) que registra:

- **input_tokens:** Tokens consumidos en los prompts enviados al modelo
- **output_tokens:** Tokens generados en las respuestas del modelo
- **reasoning_tokens:** Campo preparado para modelos que lo soporten (como o1)
- **calls:** Número total de llamadas a la API
- **details/history:** Lista con desglose por operación

El tracking se realiza mediante el atributo `usage_metadata` de las respuestas de LangChain, que expone la información de uso de tokens proporcionada por la API de OpenAI.

### Resultados e Insights

**Observaciones del Prompt Chaining (Generador de Recetas):**
- Total típico: ~2000-3000 tokens por ejecución completa
- Distribución: Cada paso consume aproximadamente 400-600 tokens de input y 200-400 de output
- El paso de generación de receta es el más costoso en tokens de salida

**Observaciones del Routing (Tutor Virtual):**
- Total típico: ~800-1200 tokens por pregunta
- El router consume ~100-150 tokens (clasificación rápida)
- Los handlers especializados varían según la complejidad de la pregunta

**Observaciones de Parallelization (Analizador CV):**
- Total típico: ~4000-5000 tokens por análisis completo
- Cada análisis paralelo: ~600-800 tokens
- La agregación final: ~800-1000 tokens
- Beneficio de paralelización: Tiempo reducido ~60% vs secuencial

**Implicaciones para Producción:**
- **Costos:** Con gpt-4o-mini (~$0.15/1M input, ~$0.60/1M output), una sesión típica del asistente de viajes cuesta fracciones de centavo
- **Optimización:** Los prompts podrían comprimirse para reducir tokens de input sin perder calidad
- **Escalabilidad:** El tracking permite identificar qué operaciones son más costosas para optimizar primero

---

## 5. Referencias

- LangChain Documentation: https://python.langchain.com/docs/
- OpenAI API Documentation: https://platform.openai.com/docs/
- Learn Prompting - Prompt Engineering: https://learnprompting.org/docs/basics/prompt_engineering
- Agentic Patterns Article (Phil Schmid): https://www.philschmid.de/agentic-pattern
- Streamlit Documentation: https://docs.streamlit.io/
- OpenAI Reasoning Models: https://platform.openai.com/docs/guides/reasoning
- Python concurrent.futures Documentation: https://docs.python.org/3/library/concurrent.futures.html