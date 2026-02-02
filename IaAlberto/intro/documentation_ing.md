# THEORETICAL REPORT
## Pt 2.1 - Introduction to LLM Applications with LangChain

**Date:** December 2024

---

## 1. Team Identification

### Team Members
- Serhii
- Martí

---

## 2. Answers to Theoretical Questions

### Exercise 1: Non-Reasoning vs. Reasoning Models

#### Main Distinctions

Large Language Models (LLMs) can be classified into two fundamental categories based on their cognitive processing capabilities:

**Non-Reasoning Models**

These are models that generate responses directly based on statistical patterns learned during training. They process input and produce output without intermediate deliberation steps. They work by predicting the most probable next token given the context, without maintaining a visible or structured internal "thought process."

**Reasoning Models**

These are models designed to break down complex problems into intermediate steps before reaching a conclusion. They use techniques such as Chain-of-Thought (CoT), where the model explicitly generates its reasoning process. Examples include models like OpenAI's o1, which dedicate "thinking time" before responding.

#### Typical Use Cases

**Non-Reasoning Models:**
- Creative text generation (stories, poems, marketing content)
- Language translation
- Text summarization
- Simple conversational chatbots
- Text classification and sentiment analysis
- Autocomplete and writing suggestions

**Reasoning Models:**
- Complex mathematical problem solving
- Programming and code debugging
- Logical and deductive analysis
- Multi-step planning and decision making
- Scientific problems requiring structured reasoning
- Tasks requiring verification and self-correction

#### Implications for LLM Application Design

1. **Model selection:** Choose the model type based on task complexity. For simple, high-speed tasks, non-reasoning models are more efficient and cost-effective.

2. **Prompt design:** Reasoning models benefit from instructions that explicitly request step-by-step processing. Non-reasoning models require more direct and specific prompts.

3. **Latency management:** Reasoning models have higher latency due to additional processing. Real-time applications may prefer non-reasoning models.

4. **Costs:** Reasoning models consume more tokens (due to intermediate steps), impacting operational costs.

5. **Application architecture:** Complex applications can combine both types, using fast models for simple tasks and reasoning models for critical decisions.

---

### Exercise 2: Prompting Techniques for Non-Agentic Applications

#### 1. Basic Prompting Strategies

**a) Instruction Prompts**

Prompts that give direct and clear instructions to the model about what task to perform. They constitute the most basic and common way to interact with an LLM.

*Example: "Translate the following text to French: [text]"*

**b) Zero-Shot Prompting**

A technique where the model is asked to perform a task without providing previous examples. The model must infer what to do based solely on the instruction and its pre-trained knowledge.

*Example: "Classify the sentiment of this review as positive, negative, or neutral: [review]"*

**c) Few-Shot Prompting**

A technique that provides the model with several examples of the desired input-output format before presenting the actual task. This helps the model better understand the expected pattern.

*Example:*
```
Convert to uppercase:
hello → HELLO
world → WORLD
python →
```

**d) Format-Constrained Prompts**

Prompts that explicitly specify the expected output format, such as JSON, XML, numbered lists, tables, etc. This facilitates programmatic processing of responses.

*Example: "Extract the product information and return it in JSON format with the fields: name, price, category."*

#### 2. Purpose of Each Technique

**Instruction Prompts:**
- **Purpose:** Clearly and directly communicate the desired task
- **Achieves:** Focused and relevant responses to the specific request

**Zero-Shot:**
- **Purpose:** Leverage the model's general knowledge without additional configuration
- **Achieves:** Speed in implementation and flexibility for varied tasks

**Few-Shot:**
- **Purpose:** Guide the model through examples to improve accuracy
- **Achieves:** Greater consistency in response format and style, especially useful for domain-specific tasks

**Format-Constrained:**
- **Purpose:** Ensure output is programmatically processable
- **Achieves:** Smooth integration with downstream systems and reliable response parsing

#### 3. Recommendations and Best Practices

1. **Be specific and clear:** Avoid ambiguities in instructions. The more specific the prompt, the more predictable the response.

2. **Provide relevant context:** Include necessary background information for the model to fully understand the task.

3. **Define the model's role:** Use phrases like "Act as an expert in..." to establish the expected tone and level of expertise.

4. **Specify output format:** Explicitly indicate how the response should be structured (list, paragraph, JSON, etc.).

5. **Use delimiters:** Clearly separate different parts of the prompt using markers such as quotes, brackets, or XML tags.

6. **Iterate and refine:** Prompts are rarely perfect on the first attempt. Test, evaluate, and adjust iteratively.

7. **Consider length:** Very long prompts can confuse the model; very short prompts can be ambiguous. Find the right balance.

8. **Include negative constraints:** Indicate what the model should NOT do (e.g., "Do not include additional explanations").

---

## 3. Implementation Documentation

### Exercise 3: Chatbot with Memory

#### Application Description

A console-based chatbot was developed using LangChain that maintains conversation context across multiple turns. The chatbot allows natural interactions in Spanish and remembers what the user has said previously in the session.

#### Design Decisions

1. **Memory structure:** A Python list was used to store messages, following the LangChain recommended pattern with SystemMessage, HumanMessage, and AIMessage objects. This structure is simple, efficient, and allows easy access to the complete history.

2. **System message:** An initial SystemMessage was defined that establishes the assistant's behavior (friendly, responds in Spanish, maintains context).

3. **Model selected:** gpt-4o-mini for its balance between cost, speed, and response quality.

4. **Special commands:** Commands ('exit', 'memory', 'clear') were implemented for session control without interfering with normal conversation.

#### Difficulties Encountered

- **API error handling:** Resolved by implementing try-catch blocks to handle connection failures without terminating the program.
- **Memory accumulation:** In very long conversations, context may exceed the token limit. For a production version, a sliding window system could be implemented.

---

### Exercise 5: Workflow Patterns Mini Applications

#### Application 1: Recipe Generator (Prompt Chaining)

**Description:**
Application that generates personalized recipes from available ingredients, using prompt chaining in 4 sequential steps.

**Pattern Flow:**
```
Ingredients → Analysis → Dish suggestion → Complete recipe → Shopping list
```

**Pattern Justification:**
Prompt Chaining is ideal for this case because each step requires information from the previous one: you cannot generate a recipe without knowing what dish to prepare, and you cannot suggest a dish without first analyzing the available ingredients.

#### Application 2: Multi-Subject Virtual Tutor (Routing)

**Description:**
Educational application that classifies student questions and directs them to specialized tutors in different subjects (mathematics, science, history, languages).

**Pattern Flow:**
```
Question → Router (classification) → Specialized handler → Educational response
```

**Pattern Justification:**
Routing allows each subject to have an optimized prompt for its specific domain. A math tutor needs to show resolution steps, while a history tutor needs to provide temporal context and relevant characters.

#### Application 3: CV Analyzer (Parallelization)

**Description:**
Application that analyzes resumes from multiple simultaneous perspectives (technical skills, experience, education, soft skills) and generates a consolidated executive report.

**Pattern Flow:**
```
CV → [Technical | Experience | Education | Soft Skills] (parallel) → Aggregation → Final report
```

**Pattern Justification:**
Parallelization significantly reduces analysis time since the 4 evaluations are independent of each other. Using ThreadPoolExecutor allows concurrent API calls, reducing total time from ~12 seconds (sequential) to ~4 seconds (parallel).

---

### Exercise 6: Combined Application - Travel Assistant

#### Application Description

An intelligent travel assistant was developed that combines two workflow patterns (Routing and Prompt Chaining) to help users plan trips. The application is available in two versions: console (app.py) and web with Streamlit (gui_app.py).

#### Pattern Integration

**Pattern 1 - Routing:**
- Classifies user queries into categories: destination, activities, budget, weather, itinerary, general
- Each category has a specialized handler with optimized prompts
- Allows more precise and contextual responses based on question type

**Pattern 2 - Prompt Chaining (Itinerary Generation):**
- Step 1: Travel context analysis (destination, dates, budget, preferences)
- Step 2: Day structuring and themes
- Step 3: Activity details per day (morning, afternoon, evening)
- Step 4: Final tips and practical recommendations

#### Memory Mechanism

Memory was implemented at two levels:
- **Message history:** List of HumanMessage/AIMessage that enables coherent multi-turn conversations
- **Travel context:** Dictionary that stores specific information (destination, dates, budget, preferences) automatically extracted from conversations

#### Design Decisions

1. **Automatic context extraction:** When the user mentions a destination, the system automatically extracts and stores it to personalize future responses.

2. **Dual interface:** The console version allows quick testing; the Streamlit version offers better user experience with sidebar for context and metrics.

3. **Session state in Streamlit:** st.session_state was used to persist memory, context, and tokens between page reloads.

4. **Visual feedback:** In Streamlit, progress is shown during itinerary generation and token usage in real-time.

---

## 4. Token Usage Discussion

### Implemented Tracking Mechanism

A TokenTracker class (dataclass) was implemented that records:

- **input_tokens:** Tokens consumed in prompts sent to the model
- **output_tokens:** Tokens generated in model responses
- **reasoning_tokens:** Field prepared for models that support it (like o1)
- **calls:** Total number of API calls
- **details/history:** List with breakdown by operation

Tracking is performed through the `usage_metadata` attribute of LangChain responses, which exposes token usage information provided by the OpenAI API.

### Results and Insights

**Prompt Chaining Observations (Recipe Generator):**
- Typical total: ~2000-3000 tokens per complete execution
- Distribution: Each step consumes approximately 400-600 input tokens and 200-400 output tokens
- The recipe generation step is the most expensive in output tokens

**Routing Observations (Virtual Tutor):**
- Typical total: ~800-1200 tokens per question
- The router consumes ~100-150 tokens (quick classification)
- Specialized handlers vary depending on question complexity

**Parallelization Observations (CV Analyzer):**
- Typical total: ~4000-5000 tokens per complete analysis
- Each parallel analysis: ~600-800 tokens
- Final aggregation: ~800-1000 tokens
- Parallelization benefit: Time reduced ~60% vs sequential

**Implications for Production:**
- **Costs:** With gpt-4o-mini (~$0.15/1M input, ~$0.60/1M output), a typical travel assistant session costs fractions of a cent
- **Optimization:** Prompts could be compressed to reduce input tokens without losing quality
- **Scalability:** Tracking allows identifying which operations are most expensive to optimize first

---

## 5. References

- LangChain Documentation: https://python.langchain.com/docs/
- OpenAI API Documentation: https://platform.openai.com/docs/
- Learn Prompting - Prompt Engineering: https://learnprompting.org/docs/basics/prompt_engineering
- Agentic Patterns Article (Phil Schmid): https://www.philschmid.de/agentic-pattern
- Streamlit Documentation: https://docs.streamlit.io/
- OpenAI Reasoning Models: https://platform.openai.com/docs/guides/reasoning
- Python concurrent.futures Documentation: https://docs.python.org/3/library/concurrent.futures.html