# AI Email Reply Assistant (Spring Boot + Chrome Extension)

An AI-powered Gmail reply assistant built with a **backend-first architecture**.

The core logic lives in a **Java / Spring Boot** REST service that constructs context-constrained prompts, communicates with the Google Gemini API using Spring's `WebClient`, and parses nested JSON payloads via Jackson (`JsonNode`) to return clean, ready-to-send text without placeholder artifacts. A minimal **Manifest V3 Chrome Extension** acts as a thin client inside Gmail, observing DOM mutations to inject an **AI Reply** trigger and forward thread context to the backend.

## Architecture
- **`email-writer/`**: Spring Boot REST API (`/api/email-generator`) integrating with Gemini API via `WebClient` and Jackson 3.
- **`email-writer-extension/`**: Chrome Manifest V3 extension injecting an **AI Reply** button into Gmail's compose toolbar using `MutationObserver`.

## Setup & Run

### 1. Backend (Spring Boot)
Configure the following environment variables on your system:
- `GEMINI_URL`
- `GEMINI_KEY`

Run from `email-writer/`:
```bash
mvn spring-boot:run
