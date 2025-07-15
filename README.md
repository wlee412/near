<p align="center">
  <img src="https://github.com/user-attachments/assets/60ac6585-1227-414c-9aaa-52c6ba52cc70"
       width="345" height="113" alt="image" />
</p>
<p align="center">
‘near’(가까운)와 ‘ear’(귀)의 조합으로,<br>
언제나 가까이에서 귀 기울이며 당신의 이야기를 들어주는 사람을 의미합니다.
</p>

---

## 목차

- [소개](#소개)  
- [프로젝트 기간](#프로젝트-기간)  
- [프로젝트 주요 목표](#프로젝트-주요-목표)  
- [주요 기능](#주요-기능)  
  - [1. 화상 심리상담 시스템](#1-화상-심리상담-시스템)  
  - [2. 심리 설문 및 자가 테스트](#2-심리-설문-및-자가-테스트)  
  - [3. 병원 및 약국 찾기](#3-병원-및-약국-찾기)  
  - [4. 멘탈 케어 컨텐츠](#4-멘탈-케어-컨텐츠)  
  - [5. Chatbot 서비스](#5-chatbot-서비스)  
- [기술 스택](#기술-스택)  
- [프로젝트 구성원 및 업무](#프로젝트-구성원-및-업무)  
- [프로젝트 기대 효과](#프로젝트-기대-효과)  
- [메인화면](#메인화면)  

---

## 프로젝트 기간 
**기간:** 2025년 7월 1일 \~ 7월 27일


상담자와 내담자 모두에게 쉽고 편리한 상담 환경을 제공하고, 정신 건강 상태를 자가 진단 및 관리할 수 있는 다양한 도구와 컨텐츠를 제공하는 웹 플랫폼입니다.

---

## Notion 링크

<p align="center">
  <a href="https://www.notion.so/2-N-EAR-21f678f892a7803ca924ca3c5816cffe" target="_blank">
    <img width="363" height="100" alt="Group 1(1)" src="https://github.com/user-attachments/assets/5ce3cf32-7cd1-4214-af33-0d95e5fdc29f" />
  </a>
</p>




---

## 프로젝트 주요 목표

* 편리한 상담 예약 및 관리 시스템 제공 (이메일 알림 포함)
* 화상 상담 ,실시간 채팅 , Canvas 제공 (WebRTC, WebSocket)
* 심리상태 자가진단 설문조사 및 ChatGPT 기반 해석과 리포트 제공
* 지도 API를 통한 병원 및 약국 검색과 즐겨찾기 기능 제공
* 스트레스 완화를 위한 멘탈 케어 컨텐츠 제공
* Chatbot을 이용한 상담사 정보 및 예약 정보 문의 (Dialogflow CX API & ChatGPT API 연동)

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

* 카카오 지도 API를 이용한 병원/약국 검색 및 거리순 정렬 기능
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

* **Frontend:** HTML5, CSS3, JavaScript, JQuery, Bootstrap
* **Backend:** Java 17, Spring Boot, MyBatis
* **Database:** MySQL, Amazon RDS, eXERD
* **Cloud & Deployment:** AWS EC2, Tomcat
* **API & Service:** ChatGPT API, Google Calendar API, CoolSMS API, Kakao Map API, YouTube API
* **Version Control:** GitHub
* **Collaboration & Documentation:** Notion, PPT

---

## 프로젝트 구성원 및 업무

| 기능 영역                  | 담당자 |
| ---------------------- | --- |
| 마이페이지, 로그인, 회원가입, 심리도구 | 이재원 |
| 상담사 관리, Chatbot        | 양지선 |
| 관리자 페이지                | 이영교 |
| 지도기반 병원 찾기, 미니게임       | 윤태권 |
| 예약 관리 및 화상 상담          | 윤성찬 |
| 멘탈케어 컨텐츠 & 자료          | 조민정 |

---

## 프로젝트 기대 효과

* 간편한 예약과 상담 환경을 제공하여 사용자 편의성 증대
* 다양한 심리상태 진단과 관리 컨텐츠를 제공하여 정신 건강에 대한 자가 관리 능력 향상
* 외부 서비스 이동 없이도 병원과 약국 정보를 쉽게 접근하고 관리할 수 있도록 지원

---

## 메인화면 
<img width="1904" height="909" alt="image" src="https://github.com/user-attachments/assets/5052b39b-8be4-4ea3-bf8d-909bc918404d" />

