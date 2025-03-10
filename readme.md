# Inscribe Flashcard Application

This Java flashcard application, **Inscribe**, displays Bible verses as flashcards. When you flip a card, the application retrieves the full Bible passage from the ESV API.

## Setup

### 1. Create a `config.properties` File

Before running the application, you **must** create a file named `config.properties` in the project's root directory. This file should contain your ESV API key in the following format:

ESV_API_KEY=your_actual_api_key_here