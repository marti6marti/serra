
# Ejercicio 5: Mini Aplicación - Workflow Pattern: Prompt Chaining


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
        # Retorna el total de tokens usats
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

#analiza
def step1_analyze_ingredients(ingredients: str, tracker: TokenTracker) -> str:
    prompt = f"""Analiza los siguientes ingredientes separalo por categorias:
    
    Ingredientes: {ingredients}
        Proporciona:
        1. Categorías (proteínas, vegetales, carbohidratos, etc.)
        2. Posibles combinaciones
        3. Tipo de cocina sugerida (italiana, mexicana, asiática, etc.)

    OBLIGATORIO: responde de forma concisa."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Paso 1: Análisis")
    return response.content

#sugiere
def step2_suggest_dish(analysis: str, tracker: TokenTracker) -> str:
    prompt = f"""Basándote en este análisis de ingredientes:

    {analysis}

    Sugiere UN plato específico que se pueda preparar. Indica:
        1. Nombre del plato
        2. Por qué es apropiado para estos ingredientes
        3. Nivel de dificultad (fácil/medio/difícil)
        4. Tiempo estimado de preparación

    OBLIGATORIO: responde de forma concisa."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Paso 2: Sugerencia")
    return response.content

#genera
def step3_generate_recipe(dish_suggestion: str, original_ingredients: str, tracker: TokenTracker) -> str:
    prompt = f"""Genera una receta completa para:

    {dish_suggestion}

    Ingredientes disponibles: {original_ingredients}

    Incluye:
        1. Lista completa de ingredientes con cantidades
        2. Pasos de preparación numerados
        3. Tips o consejos útiles

    Formato claro y fácil de seguir."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Paso 3: Receta")
    return response.content

#lista de compras con ingredientes faltantes
def step4_shopping_list(recipe: str, available_ingredients: str, tracker: TokenTracker) -> str:
    prompt = f"""Compara la receta con los ingredientes disponibles:

    RECETA: {recipe}
    
    INGREDIENTES DISPONIBLES: {available_ingredients}
    
    Genera una lista de compras con:
        1. Ingredientes que FALTAN (obligatorios)
        2. Ingredientes opcionales para mejorar el plato
        3. Cantidades aproximadas
    
    Si no falta nada, indícalo."""

    response = model.invoke([HumanMessage(content=prompt)])
    tracker.add_usage(response, "Paso 4: Lista compras")
    return response.content


def recipe_generator_chain(ingredients: str):
    tracker = TokenTracker()
    
    print("\n" + "=" * 60)
    print(" GENERADOR DE RECETAS - PROMPT CHAINING")
    print("=" * 60)
    print(f"\n Ingredientes: {ingredients}\n")
    
    # Paso 1: Análisis
    print(" Paso 1: Analizando ingredientes...")
    analysis = step1_analyze_ingredients(ingredients, tracker)
    print(f"\n Análisis:\n{analysis}\n")
    
    # Paso 2: Sugerencia de plato
    print(" Paso 2: Sugiriendo plato...")
    dish = step2_suggest_dish(analysis, tracker)
    print(f"\n Plato sugerido:\n{dish}\n")
    
    # Paso 3: Receta completa
    print(" Paso 3: Generando receta...")
    recipe = step3_generate_recipe(dish, ingredients, tracker)
    print(f"\n Receta:\n{recipe}\n")
    
    # Paso 4: Lista de compras
    print(" Paso 4: Creando lista de compras...")
    shopping = step4_shopping_list(recipe, ingredients, tracker)
    print(f"\n Lista de compras:\n{shopping}\n")
    
    # Mostrar resumen de tokens
    tracker.display_summary()
    
    return {
        "analysis": analysis,
        "dish": dish,
        "recipe": recipe,
        "shopping_list": shopping,
        "tokens": tracker
    }


def main():
    print("\n" + "=" * 60)
    print("  EJERCICIO 5: PROMPT CHAINING - GENERADOR DE RECETAS")
    print("=" * 60)

    ingredients1 = "pollo, arroz, pimientos, cebolla, ajo, tomate, aceite de oliva"
    result1 = recipe_generator_chain(ingredients1)
    
    print("\n" + "=" * 60)
    print("¿Quieres probar con otros ingredientes? (s/n)")
    
    while True:
        choice = input("> ").strip().lower()
        if choice == 's':
            ingredients = input("\nIngresa tus ingredientes (separados por coma):\n> ")
            recipe_generator_chain(ingredients)
        elif choice == 'n':
            print("\n¡Hasta luego! \n")
            break
        else:
            print("Escribe 's' para sí o 'n' para no.")


if __name__ == "__main__":
    main()
