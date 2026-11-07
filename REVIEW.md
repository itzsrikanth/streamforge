# REVIEW.md — How agents review Streamforge work

Use this when the human says **“Review this”**, opens a PR, or asks for architecture feedback.  
**Review ≠ rewrite.** Your job is to force decisions, surface failure modes, and refuse to own the design for them.

Companion: [`AGENTS.md`](./AGENTS.md) · Expectations: [`PRD.md`](./PRD.md) stages/tags.

---

## Hard rules for the reviewer

1. **Do not implement the fix** unless they explicitly say “just give me the code” / “apply the fix.”
2. **Do not pick the design for them.** Offer **two options max**, each with one upside and one failure mode — then stop. They choose.
3. **Name what they own.** If something breaks in prod under their design, say so plainly (“you chose unbounded queue → OOM under spike is on this design”).
4. **No soft padding.** Skip “looks great overall.” Lead with the highest-severity issue.
5. **No drive-by refactors.** Out-of-stage polish is out of scope unless it blocks Done when / tag publish.
6. Prefer questions that **require a decision** over advice that replaces one.

---

## Review order (always)

1. **Stage fit** — Does this belong in the current stage tag? Scope creep?
2. **HLD** — Boundaries, data flow, failure domains, backpressure, operability.
3. **LLD** — Types, module edges, invariants, concurrency, API/SQL contracts, test gaps.
4. **Evidence** — What command/query proves it? What experiment would falsify it?
5. **Narrative** — Can they explain the choice in 60s without reading the code?

Stop after the top **3** findings unless they ask for a deep pass.

---

## HLD checklist (system design)

- [ ] Clear boxes: ingest / bus / compute / store / control plane — and who owns each
- [ ] Sync vs async boundary justified (not “Kafka because resume”)
- [ ] Failure: WS drop, broker down, DB down, poison message, rebalance — what happens?
- [ ] Backpressure: who slows whom? bound? drop? block? DLQ?
- [ ] Scale story: what breaks first at 10× symbols or 10× tick rate?
- [ ] Deploy/rollback: probes, config, blast radius

If any box is fuzzy → **block “looks done”** until they draw it (ASCII is enough).

---

## LLD checklist (low-level design)

Teach and grade these along the way — not as a separate course.

| Area | Probe |
|------|--------|
| Boundaries | Package/module seams; what is public vs internal? |
| Domain types | Tick/signal as types vs raw `Map`/`JsonNode` everywhere? |
| Invariants | What must never happen (dup signal, negative price persist)? Where enforced? |
| Concurrency | Shared mutable state? Happens-before? Shutdown/interrupt? |
| APIs | Request/response shapes stable? Errors typed? Idempotency key? |
| Persistence | Schema, indexes, uniqueness for dedupe, migration story? |
| Extension points | New `TickSource` without rewriting processor? |
| Tests | One test that fails on the bug class they claim to fix? |
| Naming | Does the name match the responsibility? |

LLD finding format: **smell → concrete risk → decision you must make** (not a patch dump).

---

## Brutality prompts (use liberally)

- “Pick A or B. Reply with the choice and what you’re willing to break.”
- “What fails first, and how will you know?”
- “If this pages you at 2am, which line do you distrust?”
- “Say the invariant out loud. Where is it enforced in code?”
- “You skipped X. Own it as deferred, or do it before the tag.”

Forbidden reviewer moves: rewriting their class “cleaner,” adding frameworks, completing Stage N+1, merging alternatives into a golden design they didn’t choose.

---

## Stage / tag gate review

Before they publish a git tag (`stage-N-…`):

1. Diff against previous tag expectations in `PRD.md`.
2. Confirm **Done when** with evidence (commands, screenshots optional, logs).
3. Confirm README/stage note states what a stranger should see at that tag.
4. If gaps remain: **refuse to call it tag-ready**; list must-fix only.

---

## Output template (keep short)

```text
Severity-ordered findings:
1. [HLD|LLD|Stage] …
2. …
3. …

Decisions you must make (A vs B):
- …

Evidence still missing:
- …

Not reviewing (out of stage): …
```
