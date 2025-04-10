# 💬 Spring WebSocket + STOMP 채팅 서비스

실시간 채팅 시스템을 Spring Boot + WebSocket + STOMP 기반으로 구현하고,  
확장성과 성능 향상을 위해 **Redis Pub/Sub** 구조를 도입하였습니다.

> ✅ 1:1 / 그룹 채팅, 메시지 저장, 읽음 처리, 채팅방 목록 조회 등 핵심 기능 구현  
> ✅ Redis Pub/Sub 구조로 멀티 인스턴스 확장에도 대비

---

## 🚀 주요 기능

- 🧑‍🤝‍🧑 **1:1 및 그룹 채팅방 개설/참여**
- 📨 **실시간 메시지 송수신 (WebSocket + STOMP)**
- 🧠 **JWT 인증 기반 메시지 권한 검증**
- 🕐 **메시지 DB 저장 및 읽음 여부 관리**
- 📬 **채팅방 목록 조회 + 안 읽은 메시지 수 표시**
- 📡 **Redis Pub/Sub 기반 메시지 브로드캐스트**

---

## 🛠️ 기술 스택

| 영역 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot 3.x, Spring WebSocket, STOMP |
| Messaging | Redis (Pub/Sub), SimpMessagingTemplate |
| Auth | Spring Security, JWT |
| DB | JPA (MySQL) |
| Infra | Docker|

---

## 🔁 메시지 처리 흐름 (요약)

```text
사용자 → /publish/{roomId} STOMP 메시지 전송
        → 메시지 DB 저장
        → Redis "chat" 채널에 메시지 발행
        → Redis 구독자들이 메시지 수신
        → SimpMessagingTemplate으로 구독자에게 브로드캐스트
```

---

## 📸 프로젝트 구성 일부

```

📁 chat/
├── 📂 config/
│   ├── WebSocketConfig.java       👉 STOMP 웹소켓 연결 설정
│   ├── StompHandler.java          👉 CONNECT, SUBSCRIBE 인터셉터 (JWT 인증)
│   └── StompEventListener.java    👉 세션 연결/해제 이벤트 로깅
│
├── 📂 controller/
│   └── StompController.java       👉 @MessageMapping 채팅 수신 핸들러
│
├── 📂 domain/
│   ├── ChatRoom.java              👉 채팅방 엔티티
│   ├── ChatMessage.java           👉 채팅 메시지 엔티티
│   ├── ChatParticipant.java       👉 참여자 엔티티
│   └── ReadStatus.java            👉 메시지 읽음 여부 엔티티
│
├── 📂 dto/
│   ├── ChatMessageDto.java        👉 메시지 전송용 DTO
│   └── MyChatListResDto.java      👉 채팅방 목록 응답 DTO
│
├── 📂 repository/
│   ├── ChatRoomRepository.java
│   ├── ChatMessageRepository.java
│   ├── ChatParticipantRepository.java
│   └── ReadStatusRepository.java
│
├── 📂 service/
│   ├── ChatService.java           👉 채팅 전체 비즈니스 로직
│   └── RedisPubSubService.java    👉 Redis 메시지 수신/브로드캐스트 처리

```
---


## ✍️ 개발 기록 (TIL)

구현 과정에서 배운 점과 문제 해결 과정을 아래 블로그에 상세히 정리해두었습니다.

[📚 TIL 블로그 바로가기 (STOMP + Redis 채팅 정리)](https://gabalsebal.tistory.com/category/%F0%9F%8D%8ESpring/%EC%9B%B9%EC%86%8C%EC%BC%93_STOMP%20%EC%B1%84%ED%8C%85%20%EC%84%9C%EB%B9%84%EC%8A%A4)

---

## 🧑‍💻 만든 사람

	💡 구혜승 (GoO Hyesung)
열정적으로 기록하고 성장 중인 백엔드 개발자!

---
