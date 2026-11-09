# TOOLING.md — Locked defaults for Streamforge

Pick once; don’t re-litigate per stage. Agents: point here instead of debating tools.  
Human: install what’s listed for **your current stage**; ignore later rows until then.

---

## Build & packages (Java) — locked

| Choice | **Maven 3.9+** |
|--------|----------------|
| Why | Still the **most common** in enterprise / Spring Boot Staff JDs; docs & Stack Overflow snippets are Maven-first; simplest mental model (`pom.xml`) |
| Alt (OK if you already prefer it) | **Gradle** (Kotlin DSL) — fine for Boot; not worth learning *and* Boot *and* Flink on day 1 |
| Not for this repo | Ant, Bazel, sbt (unless a Flink example forces it later — still wrap via Maven if possible) |

**Spring Boot:** generate with [start.spring.io](https://start.spring.io) → **Maven** → Java **21**.

**Flink / Beam (later):** prefer official Maven archetypes / deps in the same multi-module or sibling module layout.

---

## Runtime & language

| Tool | Version / note |
|------|----------------|
| **JDK** | **21** (Eclipse Temurin or Microsoft Build of OpenJDK) |
| Version manager | **SDKMAN!** (`sdk install java 21.x-tem`) — optional but recommended |
| App style | Spring Boot **3.x** |

---

## Stage → install map

Install only what the current stage needs.

### Stage 0 — hello world

| Tool | Role |
|------|------|
| JDK 21 | Compile/run |
| Maven 3.9+ | Deps + `mvn spring-boot:run` / `mvn test` |
| Docker + **Docker Compose** | Postgres (and later Kafka) |
| `psql` or any SQL client | Prove `SELECT` on ticks |
| `curl` | Health / API later; fine now |
| **websocat** *or* a 10-line Java WS smoke | Probe Binance WS before wiring the app |
| Git | Repo + later tags |
| IDE | **IntelliJ IDEA Community** (best Java UX) — VS Code + Extension Pack for Java is OK |

### Stage 1 — Boot + bus

| Tool | Role |
|------|------|
| All of Stage 0 | |
| Compose services | **Redpanda** *or* Apache Kafka (+ ZooKeeper/KRaft as the image dictates) |
| **kcat** (kafkacat) | Produce/consume smoke tests without code |

### Stage 2 — concurrency

| Tool | Role |
|------|------|
| Same as Stage 1 | No new package manager |
| Optional | `async-profiler` / JFR later if you chase CPU — not required for Done when |

### Stage 3 — containers + K8s

| Tool | Role |
|------|------|
| Docker | Multi-stage builds |
| **kubectl** | Cluster CLI |
| **kind** (preferred) *or* minikube | Local K8s |
| Optional | **k9s** — nicer cluster UI in terminal |

### Stage 4 — OpenTofu + Azure

| Tool | Role |
|------|------|
| **OpenTofu** (`tofu`) | IaaC (Terraform-compatible) |
| **Azure CLI** (`az`) | Auth, account, quick checks |
| Optional | `tfenv`/`tenv`-style version pin for tofu |

### Stage 5–6 — Flink / Beam

| Tool | Role |
|------|------|
| Flink (version pinned in `pom.xml` / docs note at that stage) | Local mini-cluster or Compose |
| Beam | Maven deps + DirectRunner first |

### Stage 7 — OTel

| Tool | Role |
|------|------|
| OTel Java agent *or* Micrometer+OTel exporter | Traces/metrics |
| Jaeger **or** Grafana Tempo stack in Compose | Something scrapable |

---

## Do not install “for fun” early

- Multiple JDKs without SDKMAN discipline  
- Both Maven *and* Gradle in one repo  
- Full cloud Kafka (Event Hubs) before local Redpanda/Kafka  
- Helm charts before plain kubectl manifests work  
- HDFS toolchains  

---

## One-shot sanity (Stage 0)

After install, you should be able to run (versions will differ):

```bash
java -version          # 21.x
mvn -version           # 3.9.x
docker version
docker compose version
git --version
```

WS probe (optional):

```bash
websocat wss://data-stream.binance.vision/ws/btcusdt@trade
```

---

## Agent rule

If the human asks “what should I use for X?” and X is listed here → answer with the **locked** row, one sentence why, and the stage it belongs to. Do not reopen Maven vs Gradle unless they explicitly override this file.
