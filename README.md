# 생체 신호 기반 차량 스마트 조명 시스템(2024.09 - 2024.12)

이 프로젝트는 운전자의 생체 신호(심박수 및 피부 전도도)를 분석하여 스트레스 상태를 감지하고, 이에 따라 차량 내부 조명을 자동으로 제어하는 시스템입니다. 이를 통해 운전 중 안전성과 편의성을 높이고 운전자 스트레스를 완화하고자 합니다.

## 📋 주요 기능
1. **실시간 스트레스 감지**  
   - GSR(피부 전도도) 센서와 Pulse/Heart Rate(심박수) 센서를 사용하여 실시간 데이터를 수집합니다.
   - 머신러닝 모델(RandomForestClassifier)을 활용해 스트레스 상태를 Non-Stress, Low-Stress, High-Stress로 분류합니다.
   
2. **조명 제어**  
   - AI모델을 통해 스트레스 상태에 따라 조명의 색상을 자동으로 조정합니다.
   - Android 앱을 통해 수동으로 조명의 색상과 밝기를 제어할 수 있습니다.

3. **시스템 통합**  
   - 앱(Android), 서버(Node.js), 센서(Arduino), 데이터베이스(MySQL)를 연동하여 사용자 프로필 관리와 실시간 데이터 처리를 수행합니다.

## 📚 사용 기술
- **프로그래밍 언어**: Java (Android), JavaScript (Node.js), Python, Arduino
- **플랫폼 및 도구**:
  - **Android Studio**: 사용자 인터페이스 설계 및 센서 데이터 시각화.
  - **Node.js**: 서버 개발 및 데이터 처리.
  - **MySQL**: 사용자 프로필 및 센서 데이터 저장.
  - **Arduino**: 센서 데이터 수집 및 조명 제어.
  - **Python**: 머신러닝 모델 구현.
- **센서**:
  - Galvanic Skin Response
  - Pulse/Heart Rate
  - DHT11
  - NeoPixel(WS2812B)
![회로도](https://github.com/user-attachments/assets/b67539c8-a2d8-4b30-9421-7482b1720997)

## 🗂️ 프로젝트 구조
1. **데이터베이스 테이블**
   - **프로필 테이블**: 사용자 이름, 프로필 이미지, 가입일 저장.
   - **센서 데이터 테이블**: 심박수, GSR, 온도 및 습도 데이터 저장.

## 📈 AI 모델
- **데이터셋**: DriveDB, AffectiveROAD
- **전처리**: MinMaxScaler를 사용하여 데이터를 정규화.
- **모델**: RandomForestClassifier
- **스트레스 상태 분류**:
  - 0: Non-Stress
  - 1: Low-Stress
  - 2: High-Stress
- 참고 링크: [RandomForestClassifier 모델](https://github.com/iut-swe-20-dp-1/machine-learning-model/tree/main)


## 🛠️ 필수 환경
프로젝트 실행을 위해 아래 도구들이 필요합니다:
- **Android Studio**: Gradle Plugin `8.2.0`, Compile SDK `34`, Min SDK `30`, Target SDK `34`  
- **Node.js**: v18 이상  
- **MySQL**: v8.0 이상  
- **Python**: v3.8 이상  
- **Arduino IDE**: v2.0 이상  

## 🏆 기대 효과
- 운전 중 스트레스를 감소시켜 사고 위험 완화.
- 운전자의 심리적 안정 및 편의성 제공.
- 스마트 차량 기술 발전에 기여.

## 🔗 참고 자료
- [현대모비스 기사](https://www.hyundai.co.kr/story/CONT0000000000166504)
- [AffectiveROAD 데이터셋](https://www.media.mit.edu/tools/affectiveroad/) 
