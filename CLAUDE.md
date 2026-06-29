# Apache Sling Jobs

## Overview

This bundle provides a message-queue-backed job processing system for Apache Sling, distinct from the older Sling Event / Sling Jobs bundle (`org.apache.sling.event`). It is designed for distributed environments: jobs are transported via a Message-Oriented Middleware (MoM) queue (using the `org.apache.sling.mom` SPI), allowing job processing to be distributed across multiple Sling instances. The API is intentionally constrained to operations supported by AMQP/JMS semantics and does not include features (like queue browsing or arbitrary item removal) that require repository-backed queues.

## Tech Stack

- Java 8, OSGi bundle (bnd-maven-plugin, `sling-bundle-parent` v47)
- Depends on `org.apache.sling.mom` (Message-Oriented Middleware SPI) — must be deployed separately
- OSGi DS annotations for component wiring
- Testing: JUnit 4, Mockito 4, slf4j-simple

## Build & Test Commands

```
mvn clean install                         # build and run all tests
mvn test -Dtest=JobManagerImplTest        # single test class
```

## Architecture

**Public API** (`org.apache.sling.jobs`):
- `Job` — represents a unit of work with a type, properties, and lifecycle state
- `JobManager` — creates jobs and submits them to the MoM queue via `JobBuilder`
- `JobConsumer` — implement this to process jobs; register as an OSGi service declaring supported `JobType`s
- `JobBuilder` / `JobUpdateBuilder` — fluent builders for creating/updating jobs
- `JobUpdate` / `JobUpdateListener` — event-driven job state changes
- `JobTypeValve` — optional filter to further restrict which jobs a consumer accepts
- `Types` — factory for `JobType` instances

**Implementation** (`org.apache.sling.jobs.impl`):
- `JobSubsystem` — central dispatcher; registers as a MoM queue consumer via `JobQueueConsumerFactory`, routes incoming messages to `JobConsumer` services
- `JobManagerImpl` — implements `JobManager`; publishes job-create messages to MoM queue
- `InMemoryJobStorage` — transient job state store (jobs are not persisted in the repository)
- `OutboundJobUpdateListener` — publishes job state transitions back onto the MoM queue
- `ManagerSubscriber` — subscribes to job status updates from the MoM queue

Data flow: `JobManager.newJobBuilder(type).add()` → message on MoM queue → `JobSubsystem` receives message → finds a matching `JobConsumer` → calls `consumer.execute(job)` → publishes status update back to queue.

## Conventions & Gotchas

- **Not a replacement for `org.apache.sling.event.jobs`**: the two systems coexist. This bundle is designed for message-queue-native distributed processing; the event bundle is repository-backed. Do not mix the two APIs.
- **`org.apache.sling.mom` must be deployed**: without a `MoM` provider OSGi service, `JobSubsystem` will not activate. This is a snapshot dependency (`1.0.1-SNAPSHOT`) — ensure your local Maven repo has it built.
- **In-memory storage only**: `InMemoryJobStorage` does not survive bundle restarts or instance failures. Job durability depends entirely on the MoM broker's persistence guarantees.
- `JobConsumer` services are discovered via OSGi DS; they must declare `job.types` as a service property (array of `JobType` string representations). The `JobTypeValve` interface allows further runtime filtering.
- `JobType.ANY_TYPE` causes a consumer to receive all job types — use with care in multi-tenant deployments.
- The POM uses `sling-bundle-parent` v47 (older than the journal bundles). The Java source/target level is 8 (`sling.java.version=8`).
