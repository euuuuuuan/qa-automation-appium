# 📱 안드로이드 계산기 앱 자동화 테스트 (Appium)


![기술 스택](https://img.shields.io/badge/Java-007396?style=flat-square\&logo=Java\&logoColor=white)

![기술 스택](https://img.shields.io/badge/Appium-000000?style=flat-square\&logo=appium\&logoColor=white)

![테스트 프레임워크](https://img.shields.io/badge/TestNG-B31B1B?style=flat-square\&logo=testng\&logoColor=white)

---

### 🌟 프로젝트 소개

이 프로젝트는 Appium과 Java를 활용하여 안드로이드 기본 계산기 앱의 주요 기능을 자동화 테스트하는 예제입니다.

모바일 애플리케이션의 기본적인 기능 검증을 자동화하여 수동 테스트의 한계를 극복하고, 테스트 효율성을 높이는 데 중점을 두었습니다.

---

### 🚀 주요 기능

-   **덧셈/뺄셈/곱셈 테스트**: `2+3=5`, `9-4=5`, `3x4=12` 등 기본 연산에 대한 테스트를 자동화했습니다.

-   **드라이버 안정화**: `WebDriverWait`를 사용해 요소가 로드될 때까지 기다림으로써 테스트의 안정성을 확보했습니다.

-   **오류 및 예외 처리**: `NullPointerException`, `Deprecated` 경고 등 다양한 예외 상황을 고려하여 테스트 코드를 견고하게 작성했습니다.

-   **테스트 보고서**: 스크린샷 기능을 구현하여 실패 시점의 화면을 기록하고, 테스트 결과를 시각적으로 확인할 수 있습니다.

---

## 💡 기술적 성과 및 문제 해결

-   Appium, Maven 등 생소한 기술 스택을 **단기간 내에 빠르게 습득**하여, 계산기 앱 자동화 테스트 프로젝트를 성공적으로 구현했습니다.

-   테스트 과정에서 발생한 **버전 호환성 문제 및 런타임 오류(e.g., NullPointerException)에 대한 해결책을 스스로 찾아 적용**하며, 문제 해결 능력을 증명했습니다.

## 🤖 AI 도구 활용

본 프로젝트는 ChatGPT, Google Gemini, Claude 등의 생성형 AI 도구를 적극적으로 활용하여 진행되었습니다.

학습 지원: Appium, TestNG, Maven 등 생소한 기술의 학습 곡선을 단축

코드 품질 개선: 예외 처리, 리팩토링, 리포트 구조화에 AI 피드백 반영

문제 해결: 런타임 오류 및 버전 호환성 이슈 발생 시, AI 도구를 통해 다양한 해결책을 탐색 후 적용

문서화 보조: README, 테스트 전략 문서, 프로젝트 구조 설명 등을 AI를 활용해 가독성과 전문성을 높임

---

### 🛠️ 개발 환경 및 실행 방법

#### **실행 환경**

-   **Java JDK**: 21.08

-   **Android Studio**: 21.0. 7 (Pixel 9a API 36)

-   **Appium Server**: 3.0.1

-   **Maven**: 3.9.11



#### **실행 방법**

1.  **프로젝트 클론**

-   [git clone](https://github.com/euuuuuuan/qa-automation-appium.git)

2.  **프로젝트 열기**

-   IntelliJ IDEA 또는 VS Code에서 Maven 프로젝트를 엽니다.

3.  **Appium Server 실행**

-   `appium` 명령어로 서버를 실행합니다.

4.  **테스트 실행**

-   TestNG를 통해 테스트 스위트 또는 개별 테스트 클래스를 실행합니다.

---

### 📈 테스트 과정 및 결과

-   **테스트 절차**: 계산기 앱 실행 → 계산기 초기화 → `덧셈 2 + 3 = 5` '뺄셈 9 - 4 = 5', '곱셈 3 x 4 = 12' 연산

-   **테스트 통과 화면**: `2 + 3 = 5` 연산 성공 스크린샷

<img src="https://github.com/euuuuuuan/qa-automation-appium/blob/main/appium-demo/qa-reports/appium/screenshots/addition-result-1757344421489.png" width="400"/>


-   **결과 검증**: 계산 결과가 예상 값과 일치하는지 `Assert.assertTrue`를 통해 검증합니다.

📸 시연 예시

![성공 스크린샷](https://github.com/euuuuuuan/qa-automation-appium/blob/main/auto-excution-result2.png)

![성공 스크린샷](https://github.com/euuuuuuan/qa-automation-appium/blob/main/auto-excution-result3.png)

```bash

📂 프로젝트 구조 예시
qa-automation-appium/
 ┣ 📂 .vscode
 ┗ 📂 appium-demo
     ┣ 📂 qa-reports/appium/screenshots      # 작동결과 스크린샷
     ┣ 📂 src/test/java/com/example            # AppTest.java 테스트 자동화 코드
     ┣ testng.xml                                       # 테스트 스위트 설정
     ┣ pom.xml                                         # Maven 의존성 관리
     ┣ .gitignore          
     ┣ auto-excution-result.png                   #터미널 실행화면 스크린샷
     ┗ README.md
```

---

### 🧑‍💻 개발자 정보

|  이름  |        역할        |        연락처       |

| 전유안 | QA 자동화 엔지니어 | GitHub: `euuuuuuan` |

