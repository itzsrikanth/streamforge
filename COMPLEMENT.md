# Complementary spine (not this repo)

Streamforge covers **stream / async / stateful processing / platform**.  
Concepts it cannot honestly own need a **second handwritten project**. Working name: **Ledgerforge**.

Do **not** start Ledgerforge until Streamforge Stage 2 (or Stage 1 minimum) is tagged — avoid splitting focus.

---

## Why a second spine

| Hard in Streamforge | Natural in Ledgerforge |
|---------------------|-------------------------|
| Strong OLTP / multi-row transactions | Transfers, balances, double-entry |
| Ledger / money invariants | Account + journal lines; conserved balance |
| AuthN/Z, multi-tenant SaaS | API keys/JWT, tenant isolation, RBAC |
| Cache correctness under reads | Redis balance/read models + invalidation |
| Search / reporting indexes | Txn search, statements |
| Saga / workflow across services | Hold → capture → settle; outbox + consumers |
| Optimistic locking / oversell | `version` columns; conditional updates |
| Read replicas / CQRS (serious) | Write ledger, read projections |
| Multi-region **discussion + limited drill** | Primary + replica failover story on Azure |
| Using consensus systems as a client | Leader election for job runners via etcd/ZK/Blob lease — still not “implement Raft” |

Still **out of both** unless you add a third track: implement Raft yourself, CDN/media, ML platforms, mobile offline sync.

---

## Ledgerforge — one-line spine

**Multi-tenant double-entry ledger:** idempotent `POST /transfers` → journal entries in Postgres → outbox → async projection (optionally Kafka, which can later feed Streamforge-style consumers) → statement/read API + auth.

### Suggested stages (mirror Streamforge tagging culture)

| Tag | Expectation |
|-----|-------------|
| `lf-stage-0-transfer-oltp` | Single-tenant transfer + balance invariant in one DB txn |
| `lf-stage-1-idempotency-api` | Idempotency-Key, Boot API, tests for double-submit |
| `lf-stage-2-outbox-projection` | Outbox → worker → read model; no dual-write lies |
| `lf-stage-3-auth-tenancy` | Tenant isolation + basic authz |
| `lf-stage-4-cache-search` | Redis + search/list path with explicit invalidation rules |
| `lf-stage-5-k8s-tofu` | Same ops bar as Streamforge stages 3–4 |
| `lf-stage-6-sagas` | Multi-step settle/refund with failure ownership |

Same agent contract: brutal coach, handwritten, HLD+LLD, `REVIEW.md`-style reviews — copy the contract when the repo is created.

---

## Bridge between spines (optional later)

- Ledgerforge **outbox events** → Kafka → Streamforge-like consumer (fraud window, velocity).  
- Teaches: OLTP truth + stream reaction without conflating the two systems.

---

## Decision

- **Now:** only Streamforge.  
- **Later:** create `~/dev/itzsrikanth/ledgerforge` with its own `PRD.md` / `AGENTS.md` when you intentionally switch spines.
