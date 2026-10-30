# RPG Combat Simulator

> **Status: Work in Progress**

A turn-based RPG combat simulator built in Java as a hands-on project for exploring Java programming, object-oriented design, Maven, and eventually full-stack development.

The project currently focuses on the core combat engine. Future development may expand it into a Spring Boot backend with a React frontend and potentially real-time multiplayer battles using WebSockets.

## Project Goals

The main goal of this project is to learn and apply Java concepts through a small but expandable game system rather than building a large-scale game.

Concepts explored include:

- Object-oriented programming (OOP)
- Abstract classes and inheritance
- Interfaces and polymorphism
- Encapsulation
- Java Collections and generics
- Enums
- Exception handling
- Maven project management
- Turn-based game logic

As the project develops, it may also explore:

- JUnit testing
- Spring Boot
- REST APIs
- React
- WebSockets
- Docker
- Multiplayer state management

## Current Features

The combat system currently supports four character classes:

| Class | Description |
| --- | --- |
| Warrior | High health and defence with strong physical attacks |
| Mage | Lower health but high mana and access to offensive and healing abilities |
| Archer | Fast ranged character with balanced offensive capabilities |
| Rogue | Fast, offensive character specialising in high-damage attacks |

Each character has several core attributes:

- Health
- Mana
- Attack
- Defence
- Speed

Speed determines which character takes the first turn.

### Skills

Skills are implemented using a common `Skill` interface.

Current skill types include:

- Basic attacks
- Damage skills
- Healing skills

Example abilities include:

```text
Warrior
├── Basic Attack
└── Heavy Strike

Mage
├── Basic Attack
├── Fireball
└── Heal

Archer
├── Basic Attack
└── Power Shot

Rogue
├── Basic Attack
└── Backstab
```

Skills can consume mana and have targeting restrictions.

### Targeting System

Skills use a `TargetType` enum to determine valid targets:

```text
SELF
ENEMY
ANY
```

For example, offensive abilities such as `Fireball` can only target an enemy, while the current `Heal` implementation targets the caster.

Invalid targets are rejected by the battle engine.

### Battle System

The `Battle` class manages the flow of combat, including:

- Determining the first turn based on character speed
- Executing skills
- Validating skill targets
- Switching turns
- Tracking character defeat
- Determining the winner
- Recording battle events in a battle log

## Project Structure

The current project is organised approximately as follows:

```text
rpg-combat-simulator/
├── .gitignore
├── README.md
└── backend/
    ├── pom.xml
    └── src/
        └── main/
            └── java/
                └── com/
                    └── rpgsim/
                        ├── Main.java
                        ├── combat/
                        │   └── Battle.java
                        ├── enums/
                        │   └── TargetType.java
                        ├── model/
                        │   ├── Character.java
                        │   ├── Warrior.java
                        │   ├── Mage.java
                        │   ├── Archer.java
                        │   └── Rogue.java
                        └── skill/
                            ├── Skill.java
                            ├── DamageSkill.java
                            └── HealSkill.java
```

The structure will change as additional features are implemented.

## Technologies

### Current

- Java
- JDK 24
- Maven

### Planned / Under Consideration

- JUnit
- Spring Boot
- React
- Docker
- WebSockets
- PostgreSQL or another persistence solution

## Running the Project

### Prerequisites

Ensure Java and Maven are installed:

```bash
java -version
mvn -version
```

The project currently uses **JDK 24**.

### Compile

From the backend directory:

```bash
cd backend
mvn compile
```

### Run

After compilation:

```bash
java -cp target/classes com.rpgsim.Main
```

`Main.java` currently acts as a development/testing entry point for manually running combat scenarios.

## Example Battle

A battle can be created using any two `Character` subclasses:

```java
Archer archer = new Archer("Robin");
Rogue rogue = new Rogue("Shade");

Battle battle = new Battle(archer, rogue);
```

Skills can then be executed through the battle system:

```java
battle.useSkill(
    "Backstab",
    archer
);
```

Because characters are represented through the common `Character` abstraction, the battle engine does not need separate implementations for different matchups.

For example:

```java
new Battle(
    new Warrior("Arthur"),
    new Mage("Merlin")
);
```

and:

```java
new Battle(
    new Archer("Robin"),
    new Rogue("Shade")
);
```

can both use the same battle system.

## Development Roadmap

The project is still under active development.

### Phase 1 — Core Combat Engine 🚧

- [x] Base `Character` abstraction
- [x] Warrior
- [x] Mage
- [x] Archer
- [x] Rogue
- [x] Skill interface
- [x] Damage skills
- [x] Healing skills
- [x] Basic attacks
- [x] Skill targeting
- [x] Turn management
- [x] Battle logging
- [x] Win/defeat detection
- [ ] Automated tests

### Phase 2 — Testing

- [ ] Add JUnit
- [ ] Character tests
- [ ] Skill tests
- [ ] Mana validation tests
- [ ] Target validation tests
- [ ] Turn-order tests
- [ ] Battle tests

### Phase 3 — Backend API

- [ ] Introduce Spring Boot
- [ ] Character APIs
- [ ] Battle creation API
- [ ] Battle action API
- [ ] Battle state responses
- [ ] Exception handling and validation

### Phase 4 — Frontend

- [ ] React frontend
- [ ] Character selection
- [ ] Battle interface
- [ ] Health and mana bars
- [ ] Skill selection
- [ ] Battle log display

### Possible Future Extensions

- [ ] Status effects
- [ ] Buffs and debuffs
- [ ] Critical hits
- [ ] Improved damage calculations
- [ ] Enemy AI
- [ ] Character progression
- [ ] Equipment
- [ ] WebSocket-based multiplayer battles
- [ ] Dockerised deployment
- [ ] Persistent player/battle data

## Current Limitations

The project is currently an early-stage combat engine and is **not a finished game**.

Several systems are intentionally simplified while the underlying Java architecture is being developed. Character balancing, damage calculations, skill variety, UI, persistence, networking, and automated testing are expected to evolve as development continues.

## Motivation

This project was created primarily as a practical way to strengthen Java development skills while building something that can gradually increase in complexity.

Rather than implementing all features at once, the project is being developed incrementally, starting with pure Java domain logic before introducing frameworks such as Spring Boot and React.
