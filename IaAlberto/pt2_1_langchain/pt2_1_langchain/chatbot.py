"""
Ejercicio 3: Console-Based Chatbot with Memory
==============================================
Chatbot simple basado en consola usando LangChain con memoria implementada
mediante una lista de Python para soportar conversaciones multi-turno.

Autor: [Tu nombre]
Fecha: Diciembre 2024
"""

from dotenv import load_dotenv
from langchain_openai import ChatOpenAI
from langchain_core.messages import SystemMessage, HumanMessage, AIMessage
import os

# Cargar variables de entorno
load_dotenv()


def create_chatbot():
    """
    Crea e inicializa el modelo de chat con configuración básica.
    
    Returns:
        ChatOpenAI: Instancia del modelo configurado
    """
    model = ChatOpenAI(
        model="gpt-4o-mini",
        temperature=0.7,
        max_tokens=500,
        api_key=os.getenv("OPENAI_API_KEY")
    )
    return model


def initialize_memory():
    """
    Inicializa la memoria del chatbot con el mensaje del sistema.
    
    La memoria se implementa como una lista de Python que almacena
    todos los mensajes de la conversación (sistema, usuario, asistente).
    
    Returns:
        list: Lista con el mensaje inicial del sistema
    """
    memory = [
        SystemMessage(content="""Eres un asistente amigable y útil. 
        Responde en español de manera clara y concisa.
        Recuerda el contexto de la conversación para dar respuestas coherentes.""")
    ]
    return memory


def chat(model, memory, user_input):
    """
    Procesa un mensaje del usuario y genera una respuesta.
    
    Args:
        model: Modelo de ChatOpenAI
        memory: Lista con el historial de la conversación
        user_input: Mensaje del usuario
        
    Returns:
        str: Respuesta del asistente
    """
    # Añadir mensaje del usuario a la memoria
    memory.append(HumanMessage(content=user_input))
    
    # Obtener respuesta del modelo
    response = model.invoke(memory)
    
    # Añadir respuesta del asistente a la memoria
    memory.append(AIMessage(content=response.content))
    
    return response.content


def display_welcome():
    """Muestra mensaje de bienvenida."""
    print("\n" + "=" * 60)
    print("       CHATBOT CON MEMORIA - LangChain")
    print("=" * 60)
    print("Escribe 'salir' para terminar la conversación.")
    print("Escribe 'memoria' para ver el historial.")
    print("Escribe 'limpiar' para reiniciar la conversación.")
    print("=" * 60 + "\n")


def display_memory(memory):
    """
    Muestra el contenido actual de la memoria.
    
    Args:
        memory: Lista con el historial de mensajes
    """
    print("\n" + "-" * 40)
    print("HISTORIAL DE CONVERSACIÓN:")
    print("-" * 40)
    for i, msg in enumerate(memory):
        if isinstance(msg, SystemMessage):
            print(f"[Sistema]: {msg.content[:50]}...")
        elif isinstance(msg, HumanMessage):
            print(f"[Tú]: {msg.content}")
        elif isinstance(msg, AIMessage):
            print(f"[Asistente]: {msg.content[:100]}...")
    print("-" * 40 + "\n")


def main():
    """
    Función principal que ejecuta el bucle del chatbot.
    """
    # Inicializar componentes
    model = create_chatbot()
    memory = initialize_memory()
    
    # Mostrar bienvenida
    display_welcome()
    
    # Bucle principal de conversación
    while True:
        try:
            # Obtener entrada del usuario
            user_input = input("Tú: ").strip()
            
            # Verificar comandos especiales
            if not user_input:
                continue
            
            if user_input.lower() == 'salir':
                print("\n¡Hasta luego! Gracias por chatear.\n")
                break
            
            if user_input.lower() == 'memoria':
                display_memory(memory)
                continue
            
            if user_input.lower() == 'limpiar':
                memory = initialize_memory()
                print("\n[Memoria limpiada. Nueva conversación iniciada.]\n")
                continue
            
            # Procesar mensaje y obtener respuesta
            response = chat(model, memory, user_input)
            print(f"\nAsistente: {response}\n")
            
        except KeyboardInterrupt:
            print("\n\n¡Hasta luego!\n")
            break
        except Exception as e:
            print(f"\nError: {e}\n")
            continue


if __name__ == "__main__":
    main()
