# 🚗 생체 신호 기반 차량 스마트 조명 시스템  
> (2024.09 ~ 2024.12)

---

## 📌 프로젝트 소개
운전자의 생체 신호(심박수, 피부 전도도)를 분석하여 스트레스 상태를 감지하고,  
이에 따라 차량 내부 조명을 자동으로 제어하는 시스템입니다.  

운전 중 스트레스를 완화하고 안전성과 편의성을 높이는 것을 목표로 개발했습니다.

---

## 🚀 주요 기능

### 🧠 스트레스 상태 분석
- GSR(피부 전도도), Pulse/Heart Rate(심박수) 데이터를 기반으로 스트레스 상태 분류  
- 머신러닝 모델(RandomForestClassifier)을 활용하여  
  Non-Stress / Low-Stress / High-Stress 상태로 구분  

### 💡 조명 제어
- 스트레스 상태에 따라 조명 색상 자동 변경  
- Android 앱을 통한 수동 제어 기능 제공  

### 🔗 시스템 연동
- Android 앱, Node.js 서버, Arduino 센서, MySQL DB 연동  
- 실시간 데이터 수집 및 처리  

---

## 🧩 시스템 흐름
센서 데이터 수집 → 서버 전송 → AI 모델 분석 → 스트레스 상태 판단 → 조명 제어  

---

## 🔌 회로 구성
![회로도](https://github.com/user-attachments/assets/b67539c8-a2d8-4b30-9421-7482b1720997)

---

## 📊 Flow Chart
<img width="1420" height="780" alt="플로우차트" src="https://github.com/user-attachments/assets/9614ee69-1d06-444e-b0b7-b6c6a94a6c5f" />

---

## 📱 구현 UI
| 프로필 선택 | 프로필 추가 | 대시 보드 |수동 조명 제어|
|---|---|---|---|
|<img width="1440" height="3120" alt="시작_프로필추가2" src="https://github.com/user-attachments/assets/5e7050ae-0535-457e-a27b-146282e1a847" />| <img width="1440" height="3120" alt="프로필추가page" src="https://github.com/user-attachments/assets/969f18ee-47c6-4690-a06b-9aa961265eef" />|<img width="1440" height="3120" alt="그래프" src="https://github.com/user-attachments/assets/b7a9f419-2697-4c15-a411-d4a284b3e0bd" />|<img width="1440" height="3120" alt="조명조절2" src="https://github.com/user-attachments/assets/2ef9cb64-3d0e-4e5f-9dca-fd7a6c0f2dcc" />|


---

## ⚙️ Tech Stack

### 📱 Android
- Java (Android Studio)

### 🌐 Backend
- Node.js  
- MySQL  

### 🤖 AI
- Python (RandomForestClassifier)

### 🔌 Embedded
- Arduino  
- Galvanic Skin Response
- Pulse/Heart Rate
- DHT11
- (LED)NeoPixel(WS2812B)
- 
---

## 🙋‍♂️ 담당 역할
- 센서 데이터 수집 및 Arduino 연동 구현  
- Node.js 서버 및 MySQL 기반 데이터 처리 로직 개발  
- 머신러닝 모델을 활용한 스트레스 분류 로직 적용  
- Android 앱과 서버 간 데이터 연동 구현  

---

## 💡 기대 효과
- 운전 중 스트레스 감소 및 사고 위험 완화  
- 사용자 맞춤형 스마트 차량 환경 제공

---

## 🔗 참고 자료
- [현대모비스 기사](https://www.hyundai.co.kr/story/CONT0000000000166504)
- [AffectiveROAD 데이터셋](https://www.media.mit.edu/tools/affectiveroad/)
