# One-Lane Bridge Traffic Simulator (Java • Multithreading • Synchronization)

A Java GUI simulation where a **two-lane road narrows into a single-lane bridge**. Cars spawn from **both directions** and must cross the bridge safely **without collisions**.  
I implemented the core synchronization logic using **Java monitors (`synchronized`, `wait`, `notifyAll`)** to coordinate multiple car threads.

> ✅ Key idea: many car threads run concurrently, but **only one direction** is allowed on the bridge at a time.

---

## Demo

### Expected behavior (no collisions)
- Cars can enter from the **Left** or **Right** using the GUI buttons.
- The controller ensures cars do **not** collide on the bridge.
- Cars from one direction may pass in a group (batch), then the direction can switch.

📸 Screenshot:  
![Bridge Simulation](./assets/screenshot.png)

---

## What I Built

This project simulates real-world traffic control with concurrency constraints:

- Each car is a **separate thread**
- Cars approach the bridge and request permission to enter
- A shared `TrafficController` acts like a **bridge traffic signal**
- Synchronization prevents:
  - head-on collisions (cars from both directions on bridge)
  - unsafe bridge entry
  - race conditions on shared state

---

## Concurrency Model

### Threads
- Every time you spawn a car, the program starts a new **Car thread**.
- Cars independently move and attempt to cross.

### Shared Resource
- The **bridge** is a critical section: it must not be used by cars traveling in opposite directions at the same time.

### Synchronization Strategy (Monitors)
I used Java’s built-in monitor primitives (no `java.util.concurrent`):
- `synchronized` methods/blocks
- `wait()`
- `notifyAll()`

The `TrafficController` enforces:
- **Mutual exclusion across directions**
- Optional fairness via turn-switching when the bridge becomes empty

---

## How the TrafficController Works

At a high level:

1. A car calls `enterBridge(direction)`
2. If the bridge is currently occupied by the opposite direction, the car waits
3. If safe, the car enters and the controller updates state
4. When the car exits, it calls `leaveBridge(direction)`
5. If the bridge becomes empty, waiting cars may be released and direction can switch

Typical state tracked:
- `carsOnBridge`
- `currentDirection` (LEFT / RIGHT / NONE)
- `waitingLeft`, `waitingRight` (optional for fairness)

---

## Testing the Synchronization

### Try these scenarios:
	•	Rapidly spawn cars from both sides
	•	Spawn only one side → should flow continuously
	•	Alternate sides quickly → no collisions, safe switching

If synchronization is removed/disabled, cars will eventually collide on the bridge.

⸻

## Skills Demonstrated
	•	Java multithreading (one thread per car)
	•	Monitor-based synchronization (synchronized, wait, notifyAll)
	•	Critical section protection
	•	Shared-state design for concurrency control
	•	GUI-driven concurrency simulation

⸻

## Notes

This repo includes my implementation of the traffic control logic in TrafficController.
The goal is correctness (no collisions) and clean coordination between multiple threads attempting to access a shared single-lane bridge.
