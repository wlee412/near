## 소개

<p align="center">
  <img src="https://github.com/user-attachments/assets/60ac6585-1227-414c-9aaa-52c6ba52cc70"
       width="345" height="113" alt="image" />
</p>
<p align="center">
‘near’(가까운)와 ‘ear’(귀)의 조합으로,<br>
언제나 가까이에서 귀 기울이며 당신의 이야기를 들어주는 사람을 의미합니다.
</p>

<p align="left">
상담자와 내담자 모두에게 쉽고 편리한 상담 환경을 제공하고, 정신 건강 상태를 자가 진단 및 관리할 수 있는 다양한 도구와 컨텐츠를 제공하는 웹 플랫폼입니다.
</p>

---

## 목차

- [소개](#소개)
- [목차](#목차)
- [프로젝트 기간](#프로젝트-기간)
- [Notion 링크](#Notion-링크)  
- [프로젝트 주요 목표](#프로젝트-주요-목표)  
- [주요 기능](#주요-기능)  
  - [1. 화상 심리상담 시스템](#1-화상-심리상담-시스템)  
  - [2. 심리 설문 및 자가 테스트](#2-심리-설문-및-자가-테스트)  
  - [3. 병원 및 약국 찾기](#3-병원-및-약국-찾기)  
  - [4. 멘탈 케어 컨텐츠](#4-멘탈-케어-컨텐츠)  
  - [5. Chatbot 서비스](#5-chatbot-서비스)  
- [기술 스택](#기술-스택)  
- [프로젝트 구성원 및 업무](#프로젝트-구성원-및-업무)
- [프로젝트 구조](#프로젝트-구조) 
- [구현](#구현)
- [프로젝트 기대 효과](#프로젝트-기대-효과)    

---

## 프로젝트 기간 
> **기간:** 2025년 7월 1일 \~ 7월 27일
<p align="center">
 <img width="2388" height="1318" alt="Group 2" src="https://github.com/user-attachments/assets/9e759ff3-2cac-45a1-a297-813223133bd0" />
</p>

---

## Notion 링크

<p align="center">
  <a href="https://www.notion.so/2-N-EAR-21f678f892a7803ca924ca3c5816cffe" target="_blank">
    <img width="369" height="100" alt="Group 3" src="https://github.com/user-attachments/assets/0b2edc25-ef31-4515-8305-68c86ec131c7"/>
  </a>
</p>

---

## 프로젝트 주요 목표

* 편리한 상담 예약 및 관리 시스템 제공 (이메일 알림 포함)
* 화상 상담 ,실시간 채팅 , Canvas 제공 (WebRTC, WebSocket)
* 심리상태 자가진단 설문조사 및 ChatGPT 기반 해석과 리포트 제공
* 지도 API를 통한 병원 및 약국 검색과 즐겨찾기 기능 제공
* 스트레스 완화를 위한 멘탈 케어 컨텐츠 제공
* Chatbot을 이용한 상담사 정보 및 예약 정보 문의 (Dialogflow CX API)

---

## 주요 기능

### 1. 화상 심리상담 시스템

* 1:1 화상 상담(WebRTC)
* 실시간 채팅(WebSocket)
* 그림 심리 상담(Canvas)
* 상담 종료 후 피드백 제공 및 상담 녹화(동의 기반)

### 2. 심리 설문 및 자가 테스트

* 제공 설문: ASRS(ADHD 간이/전체형), Rosenberg 자존감 척도, ISI(불면증), CES-D(우울), BAI(불안), K-EDI(섭식장애), K-PSQI(수면 질), PSS(스트레스), GSE(자기효능감)
* 설문 결과에 따른 상태 해석 및 맞춤형 조언 제공 (ChatGPT API 사용)
* 심리 상태 변화 추이 리포트 제공

### 3. 병원 및 약국 찾기

* 공공데이터 API & 카카오 지도 API를 이용한 병원/약국 검색 
* 병원 및 약국 즐겨찾기 기능
* 지도 기반 길찾기 서비스

### 4. 멘탈 케어 컨텐츠

* 스트레스 완화용 미니게임 3종 제공 (풍선 터뜨리기, 감정카드 짝맞추기, 감정 타자게임)
* 행운 카드 (힘이 되는 랜덤 문구 제공)
* 감정 및 기분 상태 기반 유튜브 영상 추천 서비스
* 연령별 정신건강 현황 차트 제공 (공공데이터 기반)

### 5. Chatbot 서비스

* 상담사 정보 및 예약 정보 등 사용자 질문 응답 서비스 제공 (Dialogflow)

---

## 기술 스택

### Frontend:

<div align=left>
  <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> 
  <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> 
  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> 
  <img src="https://img.shields.io/badge/jquery-0769AD?style=for-the-badge&logo=jquery&logoColor=white">
</div>

  
### Backend:

<div align=left>
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=MyBatis&logoColor=white"> 
</div>
  Java 17, Spring Boot, MyBatis
  
### Database:

<div align=left>
   <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
   <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">
</div>
  MySQL, Amazon RDS, eXERD
  
### Cloud & Deployment: 

<div align=left>
<img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=Amazon%20EC2&logoColor=white">
<img src="https://img.shields.io/badge/apache tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=white">
</div>

### Build Tool: 

<div align=left>
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
</div>




### API & Service: 
<div align=left>
<img src="https://img.shields.io/badge/YouYube API-FF0000?style=for-the-badge&logo=youtube&logoColor=white">
</div>
  
ChatGPT API, Google Calendar API, CoolSMS API, Kakao Map API, YouTube API

### Version Control:

<div align=left>
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
</div>

### Collaboration & Documentation: 

<div align=left>
  <img src="https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white">
</div>

---

## 프로젝트 구성원 및 업무

| 기능 영역                  | 담당자 |Github                                                   |
| ---------------------- | --- | -------------------------------------------------------- |
| 마이페이지, 로그인, 회원가입, 심리도구 | 이재원 | [`@wlee412`](https://github.com/wlee412) |
| 상담사 관리, Chatbot        | 양지선 | [`@nyangji`](https://github.com/nyangji) |
| 관리자 페이지                | 이영교 | [`@YeongGyo`](https://github.com/YeongGyo) |
| 지도기반 병원 찾기, 미니게임       | 윤태권 | [`@TG-0806`](https://github.com/TG-0806) |
| 예약 관리 및 화상 상담          | 윤성찬 | [`@Dae-Ban`](https://github.com/Dae-Ban)
| 멘탈케어 컨텐츠 & 자료          | 조민정 | [`@minkimmin`](https://github.com/minkimmin) |

---

## 프로젝트 구조
```
near/
├── build.gradle
├── deps.txt
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── demo/
│   │   │               ├── config/
│   │   │               ├── controller/
│   │   │               ├── interceptor/
│   │   │               ├── mapper/
│   │   │               ├── model/
│   │   │               ├── page/
│   │   │               ├── scheduler/
│   │   │               ├── security/
│   │   │               ├── service/
│   │   │               └── util/
│   │   │                   ├── DbConnectionTest.java
│   │   │                   ├── NearApplication.java
│   │   │                   └── ServletInitializer.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── secret.properties
│   │       ├── intents/
│   │       ├── mapper/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── cursor/
│   │       │   ├── images/
│   │       │   ├── js/
│   │       │   ├── json/
│   │       │   └── mail/
│   │       └── webapp/
│   │           ├── includes/
│   │           │   ├── header.jsp
│   │           │   └── footer.jsp
│   │           ├── lottie/
│   │           │   ├── chart.json
│   │           │   ├── video.json
│   │           │   └── wheelOfFate.json
│   │           └── WEB-INF/
│   │               └── views/
│   │                   └── web.xml
│   └── test/
└── settings.gradle
```
---

## 구현

### 메인화면 

<img width="1905" height="911" alt="main" src="https://github.com/user-attachments/assets/7008be53-a630-449e-b55a-f22cb26c32b3" />

---

### 챗봇

![chatbot1](https://github.com/user-attachments/assets/e7543750-984d-4e7a-8f23-2d67ccc1c8ea)

---

### 문자인증

![회원가입인증](https://github.com/user-attachments/assets/8c181bd6-7489-401f-8d30-f010ff52259b)

![아이디찾기1](https://github.com/user-attachments/assets/8fb70491-6157-48b9-bb61-9783bd277e52)

---

### 상담예약

![예약](https://github.com/user-attachments/assets/739b6482-89a6-405e-b549-a239fe68967a)

---

### 상담하기

<img width="1905" height="909" alt="image" src="https://github.com/user-attachments/assets/a8f6d835-5a7d-43a8-959f-96552b9439f1" />

---

### 심리검사

![심리검사 ](https://github.com/user-attachments/assets/a2d44c73-98e4-4359-b168-721eecb17806)

---

### 멘탈케어

![멘탈케어](https://github.com/user-attachments/assets/c91eff03-b6da-4da0-9596-f607b7bf5b35)

---

### 병원찾기 

![병원3](https://github.com/user-attachments/assets/8e7e5294-4c2d-4537-b6e9-d23410511a06)

---

### 소셜로그인

![소셜로그인](https://github.com/user-attachments/assets/d1922382-ed70-4625-93b6-0018121fe694)

---

### 내담자 마이페이지

![mypage](https://github.com/user-attachments/assets/2ca4fbdc-ea62-4922-8beb-a717da7172a5)

---

### 상담사 마이페이지

![상담사](https://github.com/user-attachments/assets/8115abe5-4485-4e70-bf9c-bc0bcf9984a9)

---

### 관리자 페이지

![관리자](https://github.com/user-attachments/assets/68962d85-ffa9-436d-b892-d4c12434b0ea)

---

## 프로젝트 기대 효과

* 간편한 예약과 상담 환경을 제공하여 사용자 편의성 증대
* 다양한 심리상태 진단과 관리 컨텐츠를 제공하여 정신 건강에 대한 자가 관리 능력 향상
* 외부 서비스 이동 없이도 병원과 약국 정보를 쉽게 접근하고 관리할 수 있도록 지원




