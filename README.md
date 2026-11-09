# Streamforge

Ultralearning spine: live market stream → process → store.

Stages, Done-when criteria, and tags: [`PRD.md`](./PRD.md).  
Agent/coach contract: [`AGENTS.md`](./AGENTS.md). Reviews: [`REVIEW.md`](./REVIEW.md).

**Stage 0** is tagged `stage-0-hello-world` (Binance public WS → normalize `Tick` → Postgres).  
**Next:** Stage 1 — Boot + Kafka bus + `GET /ticks/recent`.

---

## Stage 0 — run the proof

Requires: JDK 17+, Maven, Docker Compose. Network access to Binance public market WS.

```bash
# 1. Postgres + Flyway (from repo root)
docker compose -f listener/compose.yml up -d --remove-orphans
# wait until db healthy; flyway service should exit 0 after migrate

# 2. Run listener (inserts a short probe of trades, then closes)
export DB_USER=admin DB_PASSWORD=admin
mvn -f listener/pom.xml -q compile exec:java -Dexec.mainClass=com.itzsrikanth.streamforge.App

# 3. Prove rows
docker compose -f listener/compose.yml exec db psql -U admin -d streamforge_listener \
  -c 'SELECT id, symbol, price, qty, event_time, ingest_time FROM tick ORDER BY id DESC LIMIT 10;'
```

**Path:** `BinanceSocket` (`btcusdt@trade`) → `TradeWire` → `TickParser` / `Tick` → `Postgres.persist` → table `tick`.

**Checkout this stage:** `git checkout stage-0-hello-world`

### Owned deferrals (Stage 0)

- WS text framed as single-frame messages (`last` not reassembled)
- One long-lived JDBC connection (no pool); credentials via env only
- Probe stops after ~10 messages (not a continuous daemon)
- No unit test on `TickParser` yet
- Unused `Java-WebSocket` dependency may still be in the POM
- Local Compose Postgres only (no cloud)
