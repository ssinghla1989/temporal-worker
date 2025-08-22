# temporal-worker

A minimal Spring Boot application that runs a Temporal worker and exposes an HTTP endpoint to start a workflow. The workflow calls an external API via Retrofit.

## Features
- Temporal Java SDK worker registered on a configurable task queue
- REST endpoint to start a workflow asynchronously (returns 202 with IDs)
- Activity retries and timeouts with explicit non-retryable failure types
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
Start the demo workflow asynchronously (returns 202 Accepted with workflow IDs):
```bash
curl -s -X POST "http://localhost:8080/api/workflows/my" \
  -H "Content-Type: application/json" \
  -d '{"input":"world"}'
```

Example response:
```json
{"workflowId":"my-<hash>","runId":"<uuid>"}
```

Notes:
- You can optionally pass a custom `workflowId` to achieve idempotent starts and later query/signal by ID:
  ```json
  {"input":"world","workflowId":"order-123"}
  ```
  If omitted, the server derives a deterministic ID from `input`.

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
  - Registers `MyWorkflowImpl` and `EchoActivityImpl` on `temporal.taskQueue`
- Workflow: `com.example.temporalworker.workflows.MyWorkflow` and `MyWorkflowImpl`
  - Uses per-method activity stubs with tailored `ActivityOptions` and `RetryOptions`
- Activity: `com.example.temporalworker.activities.EchoActivity` and `EchoActivityImpl`
  - Calls `httpbin` via Retrofit
- Retrofit config: `com.example.temporalworker.config.RetrofitConfig`
  - Configures base URL (`externalApi.baseUrl`) and OkHttp logging
- HTTP controller: `com.example.temporalworker.controller.WorkflowController`
  - `POST /api/workflows/my` starts the workflow asynchronously and returns `workflowId`/`runId`

### Timeouts and retries
- Activity defaults: start-to-close timeout, schedule-to-close timeout, and exponential backoff retries.
- Per-method options:
  - `process` and `postProcess` have separate `ActivityOptions` with different retry timing.
  - `doNotRetry("HttpClientError")` prevents retries on client-side errors (4xx).
- Failure semantics:
  - `EchoActivityImpl` throws `ApplicationFailure`:
    - 5xx and network errors are retryable.
    - 4xx errors use a non-retryable type (`HttpClientError`).

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
