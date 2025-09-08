package com.example;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;

public class AppTest {
    private AndroidDriver driver;  // 제네릭 제거 (Appium 9.x)
    private WebDriverWait wait;
    
    @BeforeClass
    public void setUp() throws Exception {
        System.out.println("=== Appium 테스트 초기화 시작 ===");
        
        // Appium 9.x 방식: UiAutomator2Options 사용
        UiAutomator2Options options = new UiAutomator2Options()
            .setDeviceName("emulator-5554")
            .setAutomationName("UiAutomator2")
            .setPlatformName("Android")
            .setAppPackage("com.google.android.calculator")
            .setAppActivity("com.android.calculator2.Calculator")
            .setNewCommandTimeout(Duration.ofSeconds(60))
            .setNoReset(true)
            .setAutoWebview(false)
            .setAppWaitActivity("com.android.calculator2.Calculator") // 앱 실행 대기
            .setAppWaitDuration(Duration.ofSeconds(20)); // 앱 실행 대기 시간

                // 추가 capabilities 설정
        options.amend("autoLaunch", true)
            .amend("forceAppLaunch", true)
            .amend("androidInstallTimeout", 90000)
            .amend("appWaitForLaunch", true);


        try {
            // Appium 서버 연결
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            // 앱 초기화 대기
            Thread.sleep(3000);
            System.out.println("✅ 계산기 앱 실행 성공");
            
            // 앱 패키지 확인
            String currentPackage = driver.getCurrentPackage();
            System.out.println("📱 현재 실행 중인 패키지: " + currentPackage);
            
        } catch (Exception e) {
            System.err.println("❌ 드라이버 초기화 실패: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 1)
    public void testAppLaunch() {
        System.out.println("\n=== 앱 실행 테스트 ===");
        String packageName = driver.getCurrentPackage();
        Assert.assertTrue(packageName != null && packageName.contains("calculator"), 
            "계산기 앱이 실행되지 않았습니다. 현재 패키지: " + packageName);
        System.out.println("✅ 계산기 앱 실행 확인");
    }
    
    @Test(priority = 2)
    public void testSimpleAddition() throws Exception {
        System.out.println("\n=== 덧셈 테스트 (2 + 3 = 5) ===");
        
        try {
            // 화면 초기화 (이전 계산 결과 클리어)
            clearCalculator();
            Thread.sleep(1000);
            
            // 2 클릭
            clickNumber("2");
            Thread.sleep(500);
            
            // + 클릭
            clickOperator("add");
            Thread.sleep(500);
            
            // 3 클릭
            clickNumber("3");
            Thread.sleep(500);
            
            // = 클릭
            clickEquals();
            Thread.sleep(1000);
            
            // 결과 확인
            String result = getResult();
            System.out.println("📊 계산 결과: " + result);
            
            // 스크린샷 저장
            takeScreenshot("addition-result");
            
            // 검증
            Assert.assertTrue(result.contains("5"), 
                "덧셈 결과가 잘못되었습니다. 예상: 5, 실제: " + result);
            System.out.println("✅ 덧셈 테스트 통과");
            
        } catch (Exception e) {
            takeScreenshot("addition-failed");
            System.err.println("❌ 덧셈 테스트 실패: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 3)
    public void testSimpleSubtraction() throws Exception {
        System.out.println("\n=== 뺄셈 테스트 (9 - 4 = 5) ===");
        
        try {
            // 화면 초기화
            clearCalculator();
            Thread.sleep(1000);
            
            // 9 클릭
            clickNumber("9");
            Thread.sleep(500);
            
            // - 클릭
            clickOperator("sub");
            Thread.sleep(500);
            
            // 4 클릭
            clickNumber("4");
            Thread.sleep(500);
            
            // = 클릭
            clickEquals();
            Thread.sleep(1000);
            
            // 결과 확인
            String result = getResult();
            System.out.println("📊 계산 결과: " + result);
            
            // 스크린샷 저장
            takeScreenshot("subtraction-result");
            
            // 검증
            Assert.assertTrue(result.contains("5"), 
                "뺄셈 결과가 잘못되었습니다. 예상: 5, 실제: " + result);
            System.out.println("✅ 뺄셈 테스트 통과");
            
        } catch (Exception e) {
            takeScreenshot("subtraction-failed");
            System.err.println("❌ 뺄셈 테스트 실패: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(priority = 4)
    public void testSimpleMultiplication() throws Exception {
        System.out.println("\n=== 곱셈 테스트 (3 × 4 = 12) ===");
        
        try {
            clearCalculator();
            Thread.sleep(1000);
            
            clickNumber("3");
            Thread.sleep(500);
            clickOperator("mul");
            Thread.sleep(500);
            clickNumber("4");
            Thread.sleep(500);
            clickEquals();
            Thread.sleep(1000);
            
            String result = getResult();
            System.out.println("📊 계산 결과: " + result);
            takeScreenshot("multiplication-result");
            
            Assert.assertTrue(result.contains("12"), 
                "곱셈 결과가 잘못되었습니다. 예상: 12, 실제: " + result);
            System.out.println("✅ 곱셈 테스트 통과");
            
        } catch (Exception e) {
            takeScreenshot("multiplication-failed");
            System.err.println("❌ 곱셈 테스트 실패: " + e.getMessage());
            throw e;
        }
    }
    
    // 숫자 버튼 클릭 헬퍼 메서드
    private void clickNumber(String number) {
        String resourceId = "com.google.android.calculator:id/digit_" + number;
        try {
            WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(By.id(resourceId))
            );
            element.click();
            System.out.println("  ➡️ 숫자 " + number + " 클릭");
        } catch (Exception e) {
            System.err.println("  ❌ 숫자 " + number + " 클릭 실패");
            throw new RuntimeException("숫자 버튼을 찾을 수 없습니다: " + number, e);
        }
    }
    
    // 연산자 버튼 클릭 헬퍼 메서드
    private void clickOperator(String operator) {
        String resourceId = "com.google.android.calculator:id/op_" + operator;
        String operatorSymbol = getOperatorSymbol(operator);
        
        try {
            WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(By.id(resourceId))
            );
            element.click();
            System.out.println("  ➡️ 연산자 " + operatorSymbol + " 클릭");
        } catch (Exception e) {
            System.err.println("  ❌ 연산자 " + operatorSymbol + " 클릭 실패");
            throw new RuntimeException("연산자 버튼을 찾을 수 없습니다: " + operator, e);
        }
    }
    
    // = 버튼 클릭
    private void clickEquals() {
        try {
            WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.id("com.google.android.calculator:id/eq")
                )
            );
            element.click();
            System.out.println("  ➡️ = 클릭");
        } catch (Exception e) {
            System.err.println("  ❌ = 버튼 클릭 실패");
            throw new RuntimeException("= 버튼을 찾을 수 없습니다", e);
        }
    }
    
    // 결과 가져오기
    private String getResult() {
        String[] resultIds = {
            "com.google.android.calculator:id/result_final",
            "com.google.android.calculator:id/result",
            "com.google.android.calculator:id/result_preview"
        };
        
        for (String id : resultIds) {
            try {
                WebElement element = driver.findElement(By.id(id));
                if (element.isDisplayed()) {
                    String text = element.getText();
                    if (text != null && !text.trim().isEmpty()) {
                        return text.trim();
                    }
                }
            } catch (Exception e) {
                // 다음 ID 시도
            }
        }
        
        // XPath로 재시도
        try {
            WebElement element = driver.findElement(
                By.xpath("//*[contains(@resource-id,'result')]")
            );
            return element.getText().trim();
        } catch (Exception e) {
            throw new RuntimeException("계산 결과를 찾을 수 없습니다");
        }
    }
    
    // 계산기 클리어
    private void clearCalculator() {
        System.out.println("  🔄 계산기 초기화 중...");
        
        String[] clearIds = {
            "com.google.android.calculator:id/clr",
            "com.google.android.calculator:id/del"
        };
        
        for (String id : clearIds) {
            try {
                WebElement element = driver.findElement(By.id(id));
                if (element.isDisplayed()) {
                    // 길게 누르기 (AC 효과)
                    element.click();
                    Thread.sleep(100);
                    element.click(); // 두 번 클릭으로 완전 초기화
                    System.out.println("  ✅ 계산기 초기화 완료");
                    return;
                }
            } catch (Exception e) {
                // 다음 방법 시도
            }
        }
        
        // C 또는 AC 버튼 찾기
        try {
            WebElement element = driver.findElement(
                By.xpath("//android.widget.Button[@text='C' or @text='AC']")
            );
            element.click();
            System.out.println("  ✅ 계산기 초기화 완료 (C/AC)");
        } catch (Exception e) {
            System.out.println("  ⚠️ 클리어 버튼을 찾을 수 없음");
        }
    }
    
    // 연산자 심볼 변환
    private String getOperatorSymbol(String operator) {
        switch (operator) {
            case "add": return "+";
            case "sub": return "-";
            case "mul": return "×";
            case "div": return "÷";
            default: return operator;
        }
    }
    
    // 스크린샷 저장
    private void takeScreenshot(String fileName) {
        try {
            File screenshotDir = new File("qa-reports/appium/screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            File src = driver.getScreenshotAs(OutputType.FILE);
            File target = new File(screenshotDir, fileName + "-" + 
                System.currentTimeMillis() + ".png");
            FileUtils.copyFile(src, target);
            
            System.out.println("  📸 스크린샷 저장: " + target.getName());
        } catch (IOException e) {
            System.err.println("  ⚠️ 스크린샷 저장 실패: " + e.getMessage());
        }
    }
    
    @AfterMethod
    public void afterMethod() {
        // 각 테스트 후 잠시 대기
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @AfterClass
    public void tearDown() {
        System.out.println("\n=== 테스트 종료 처리 ===");
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("✅ 드라이버 정상 종료");
            } catch (Exception e) {
                System.err.println("⚠️ 드라이버 종료 중 오류: " + e.getMessage());
            }
        }
    }
}