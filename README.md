# temporal-worker

A minimal Spring Boot application that runs a Temporal worker and exposes an HTTP endpoint to start a workflow. The workflow calls an external API via Retrofit and returns a processed string.

## Features
- Temporal Java SDK worker registered on a configurable task queue
- REST endpoint to start a workflow synchronously
- Retrofit client with OkHttp logging to call an external API
- Simple, production-friendly structure (activities, workflows, worker registration, options factories)

## Requirements
- Java 17+
- macOS/Linux/Windows
- No external Temporal server is required for local development (uses in-process `WorkflowServiceStubs.newLocalServiceStubs()`)

## Getting started
```bash
# From the repo root
./gradlew bootRun
```

The app starts on `http://localhost:8080` by default.

Run tests:
```bash
./gradlew test
```

## API
Start the demo workflow and receive the result (plain text):
```bash
curl -s -X POST "http://localhost:8080/api/workflows/my" \
  -H "Content-Type: application/json" \
  -d '{"input":"world"}'
```

Response is a string such as:
```
Processed via API: { ...payload from external API... }
```

## Configuration
Application properties (see `app/src/main/resources/application.properties`):
```properties
server.port=8080

temporal.namespace=default
temporal.taskQueue=MY_TASK_QUEUE
temporal.worker.enabled=true

externalApi.baseUrl=https://httpbin.org/
```

- `temporal.worker.enabled`: When `true` (default), the worker registers on startup and starts polling.
- `temporal.taskQueue`: Worker and client must agree on this queue. Default is `MY_TASK_QUEUE`.
- `externalApi.baseUrl`: Base URL for the Retrofit client. Default uses `https://httpbin.org/`.

You can also provide these via environment variables using Spring Boot’s relaxed binding, e.g.:
```bash
SERVER_PORT=9090 \
TEMPORAL_NAMESPACE=default \
TEMPORAL_TASKQUEUE=MY_TASK_QUEUE \
TEMPORAL_WORKER_ENABLED=true \
EXTERNALAPI_BASEURL=https://httpbin.org/ \
./gradlew bootRun
```

## How it works
- Entry point: `com.example.temporalworker.App`
- Temporal setup: `com.example.temporalworker.config.TemporalConfig`
  - Uses in-process service stubs for local development
  - Builds `WorkflowClient` and `WorkerFactory`
  - Starts the factory on app startup (configurable via `temporal.worker.enabled`)
- Worker registration: `com.example.temporalworker.worker.MyWorkerRegistration`
  - Registers `MyWorkflowImpl` and `MyActivityImpl` on `temporal.taskQueue`
- Workflow: `com.example.temporalworker.workflows.MyWorkflow` and `MyWorkflowImpl`
  - Calls activity stub with default `ActivityOptions` (10s timeout)
- Activity: `com.example.temporalworker.activities.MyActivity` and `MyActivityImpl`
  - Calls `ExternalApiService.echo(name)` using Retrofit
- Retrofit config: `com.example.temporalworker.config.RetrofitConfig`
  - Configures base URL (`externalApi.baseUrl`) and OkHttp logging
- HTTP controller: `com.example.temporalworker.controller.WorkflowController`
  - `POST /api/workflows/my` starts the workflow synchronously using `MyWorkflowService`

## Project layout
Key directories:
- `app/src/main/java/com/example/temporalworker/workflows` – workflow interface and implementation
- `app/src/main/java/com/example/temporalworker/activities` – activity interface and implementation
- `app/src/main/java/com/example/temporalworker/worker` – worker registration
- `app/src/main/java/com/example/temporalworker/shared` – shared constants and options factories
- `app/src/main/java/com/example/temporalworker/controller` – REST controller
- `app/src/main/java/com/example/temporalworker/config` – Temporal and Retrofit configuration

## Notes
- This project is configured for local development. For production, configure a real Temporal service and replace `WorkflowServiceStubs.newLocalServiceStubs()` with client stubs for your cluster.
- Adjust timeouts and retry policies in `ActivityOptionsFactory` and `WorkflowOptionsFactory` as needed.
