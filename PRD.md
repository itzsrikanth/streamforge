# Streamforge — PRD

Ultralearning spine: live market stream → process → store.  
**HLD + LLD** judgment first; frameworks are vehicles. Handwritten work; agents coach per [`AGENTS.md`](./AGENTS.md) / [`REVIEW.md`](./REVIEW.md).

Evolve only after each stage’s **Done when** is true. Public repo: **one annotated git tag per completed stage**.

---

## Public stages & git tags

Anyone (you included) can `git checkout <tag>` to see what “done” meant at that level.

| Stage | Tag (exact) | Expectation (crisp) |
|-------|-------------|---------------------|
| **0** | `stage-0-hello-world` | One symbol Binance WS → normalize tick → Postgres; `SELECT` proves live rows; 60s path narrative |
| **1** | `stage-1-boot-bus` | Spring Boot service + Kafka/Redpanda `ticks` + Actuator health + `GET /ticks/recent` + parse test |
| **2** | `stage-2-concurrency` | Race→fix, bounded queue+workers+backpressure, idempotent double-delivery; can explain pipeline+shutdown cold |
| **3** | `stage-3-containers-k8s` | Multi-stage image; kind/minikube Deploy+Service+probes+ConfigMap/Secret; narrate rollout/probe fail |
| **4** | `stage-4-opentofu-azure` | OpenTofu provisions small Azure footprint; app runs on it; destroy/recreate without portal clickops |
| **5** | `stage-5-flink-windows` | Flink keyed windows (e.g. 1m OHLC/spike); checkpoints survive restart; watermark choice explained |
| **6** | `stage-6-beam-batch` | Same logical agg as Beam batch (file/object dump) + DirectRunner (or one real runner) |
| **7** | `stage-7-otel` | OTel traces/metrics/logs on the hot path; one useful dashboard or trace view |

**Tag publish rules**

- Tag only when **Done when** has evidence (commands in tag annotation or `stages/STAGE-N.md` note).
- Annotation must list **owned deferrals** (what you skipped and accept).
- Do not rewrite history of published tags; fix forward in the next stage.
- README at each tag should tell a stranger how to run *that* stage’s proof.

Optional per-stage note file: `stages/stage-N.md` (human-written) summarizing decisions owned at tag time.

---

## Non-goals (until a later stage names them)

- Trading / order placement  
- Perfect TA product  
- Multi-cloud  
- HDFS as primary (object storage first if batch files needed)  
- Agent-generated app standing in for handwritten practice  
- Concepts reserved for the complementary spine — [`COMPLEMENT.md`](./COMPLEMENT.md)

---

## External stream source (resolved)

| When | Source | Why |
|------|--------|-----|
| **Stage 0 default** | Binance public WS (`btcusdt@trade` / `@miniTicker`; prefer `data-stream.binance.vision`) | No API key; real push stream |
| **Optional later** | Finnhub WS (free key) | Equity narrative; thinner free feed |
| **Later** | `TickSource` adapter | Swappable ingest |

No secrets in repo for Stage 0.

---

## Stage 0 — Hello world (`stage-0-hello-world`)

**Goal:** stream → process once → DB.

1. Binance public WS, one symbol (`btcusdt`).  
2. Normalize: `symbol`, `price`, `qty?`, `event_time`, `ingest_time`.  
3. Trivial process only (parse / drop bad). No windows/TA.  
4. Persist to Postgres (Compose OK).  
5. Prove with live `SELECT`.

**Out:** Boot polish, concurrency labs, K8s, OpenTofu, Flink/Beam, OTel, Azure.

**Done when:** cold start → 2 minutes → recent rows without tutorial; &lt;60s path narrative.

**LLD at this stage:** tick type vs raw JSON; where invalid prices are rejected; single-responsibility main/loop.

---

## Stage 1 — Boot + bus (`stage-1-boot-bus`)

- Boot: DI, profiles, Actuator health.  
- Kafka/Redpanda between ingest and processor.  
- `GET /ticks/recent` (+ health).  
- ≥1 test on tick parsing.

**Done when:** Boot E2E without tutorial; health green; recent ticks via API.

**LLD:** package seams (ingest/api/persistence); API error shape; config vs hardcoding.

---

## Stage 2 — Concurrency (`stage-2-concurrency`)

- Race→fix; bounded queue + workers + backpressure; idempotent double-delivery.  
- Shared state correctness under load.

**Done when:** correct concurrent pipeline (workers + shared state + shutdown) from scratch, explained cold.

**LLD:** happens-before, pool sizing rationale, shutdown hooks, invariant under redelivery.

---

## Stage 3 — Containers + local K8s (`stage-3-containers-k8s`)

- Multi-stage Dockerfile + healthcheck.  
- kind/minikube: Deployment, Service, probes, ConfigMap/Secret.  
- Narrate rolling update + probe failure.

**Done when:** build + deploy local cluster + cold probe/rollout explanation.

---

## Stage 4 — OpenTofu + Azure (`stage-4-opentofu-azure`)

- Small Azure footprint via OpenTofu (RG + AKS or Container Apps + Postgres or equivalent).  
- Remote state when local apply works.  
- No kitchen-sink cloud.

**Done when:** `tofu apply` → app runs; destroy/recreate without portal.

---

## Stage 5 — Flink (`stage-5-flink-windows`)

- Flink job: keyed windows, sink Postgres/Kafka.  
- Checkpoint + restart story.

**Done when:** survives restart with checkpoints; watermark/window choice explained.

---

## Stage 6 — Beam + batch (`stage-6-beam-batch`)

- Beam for same logical aggregation; DirectRunner locally, then one real runner when practical.  
- Batch over historical file/object dump.

**Done when:** stream (Flink) and batch (Beam) share the logical transform story.

---

## Stage 7 — OpenTelemetry (`stage-7-otel`)

- Traces/metrics/logs on ingest→process→DB.  
- One scrapable view (Jaeger/Grafana/Azure Monitor).

**Done when:** can follow one tick/signal across components in a trace or equivalent.

---

## Stack north star (not all at once)

| Layer | Choice |
|-------|--------|
| Ingest | Java WS → Kafka |
| Services | Spring Boot 3 / Java 21 |
| Stream compute | Flink first; Beam later |
| Store | Postgres |
| Local orch | Compose → kind |
| Cloud IaaC | OpenTofu + Azure |
| Obs | OTel at stage 7 |

---

## Changelog

| Date | Note |
|------|------|
| 2026-11-06 | Initial PRD: Phase 0; Binance; roadmap. |
| 2026-11-07 | Stages + public git tags; LLD; pointer to REVIEW/COMPLEMENT; brutal-coach alignment. |
