# Core Guardrails Backend API

## 📌 Overview

This project implements a backend system with guardrails to control bot behavior and ensure fair interactions on posts.

The system supports Users, Bots, Posts, Comments, Likes, and a Notification system. It also includes a real-time Virality Engine powered by Redis.

* **PostgreSQL** → Source of truth (persistent storage)
* **Redis** → Real-time state management (guardrails + scoring)

---

## 🚀 Features

* User, Bot, Post management
* Nested comments (Max depth = 20)
* Bot spam protection (Max 100 replies per post)
* Cooldown system (10 minutes per bot-user interaction)
* Real-time virality scoring using Redis
* Like / Unlike system
* Notification system with scheduler
* Global exception handling

---

## 🧠 System Architecture

Client → Controller → Service → Repository → PostgreSQL
                        ↓
                      Redis

* PostgreSQL stores permanent data
* Redis handles real-time constraints and scoring

---

## 🔥 Virality Engine

### Formula:

* Like → +20
* User Comment → +50
* Bot Comment → +1

### Redis Key:

post:{postId}:virality

### Example:

GET post:1:virality

👉 Returns:
"1013"

---

## ⚙️ Tech Stack

* Java (Core + OOP)
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Redis
* Docker

---

## 🐳 Running the Project

### Start services:

docker-compose up

### Application runs at:

http://localhost:8080

---

## 📬 API Endpoints

### Create User

POST http://localhost:8080/api/users

```json
{
  "username": "mohit",
  "isPremium": true
}
```

---

### Create Bot

POST http://localhost:8080/api/bots

```json
{
  "name": "bot-alpha",
  "personaDescription": "engagement bot"
}
```

---

### Create Post

POST http://localhost:8080/api/posts

```json
{
  "authorId": 1,
  "authorType": "USER",
  "content": "Hello world"
}
```

---

### Add Comment

POST http://localhost:8080/api/posts/1/comments

```json
{
  "authorId": 1,
  "authorType": "USER",
  "content": "First comment"
}
```

---

### Bot Reply

POST http://localhost:8080/api/posts/1/comments

```json
{
  "authorId": 1,
  "authorType": "BOT",
  "content": "Bot reply",
  "parentCommentId": 1
}
```

---

### Like Post

POST http://localhost:8080/api/posts/1/like

```json
{
  "userId": 1
}
```

---

### Unlike Post

DELETE http://localhost:8080/api/posts/1/like
```json
{
  "userId": 1
}
```

---

## 🧪 Testing

* APIs tested using Postman
* Redis verified using Docker CLI

### Check virality score:

docker exec -it guardrails-redis redis-cli

Then:
GET post:1:virality

---

## 🔐 Thread Safety & Atomic Locks (Phase 2)

To ensure correct behavior under concurrent requests, Redis is used as an atomic state manager.

### Why Redis?

Redis operations are atomic by design, preventing race conditions.

---

### 1. Bot Reply Limit (Max 100 per post)

Key:
post:{postId}:bot_count

Logic:

* INCR used to increment count atomically
* If count > 100 → request rejected (429)
* DECR used to rollback

---

### 2. Cooldown Mechanism (10 minutes)

Key:
cooldown:bot_{botId}:human_{userId}

Logic:

* SET with TTL = 10 minutes
* If key exists → request rejected

---

### 3. Virality Score (Atomic Updates)

Key:
post:{postId}:virality

Logic:

* INCR used for scoring
* Ensures thread-safe updates

---

### ✅ Result

* No race conditions
* Consistent bot limits
* Reliable cooldown enforcement
* Accurate real-time scoring

---

## 🔔 Notification System

* First bot interaction → Instant notification
* Further interactions → Queued
* Scheduler sends summary

Example:
Bot 1 interacted with user 1 and [2] others

---

## ⚠️ Notes / Limitations

* Redis is used as a real-time store
* Data resets on Redis restart
* No DB-to-Redis rebuild implemented
* Designed for real-time scoring, not historical analytics

---


---

## 📦 Deliverables

* Spring Boot source code
* docker-compose.yml
* Postman collection
* README with atomic lock explanation

---

