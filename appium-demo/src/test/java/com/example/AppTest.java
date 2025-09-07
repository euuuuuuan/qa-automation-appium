package com.example;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AppTest {
    private AndroidDriver<MobileElement> driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() throws Exception {
        String deviceName = System.getProperty("deviceName", "emulator-5554");
        String appPath = System.getProperty("appPath", "");

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);

        if (appPath != null && !appPath.isEmpty()) {
            caps.setCapability(MobileCapabilityType.APP, appPath);
        } else {
            // 다양한 계산기 패키지명 시도
            caps.setCapability("appPackage", "com.google.android.calculator");
            caps.setCapability("appActivity", "com.android.calculator2.Calculator");
        }

        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 15);
        
        // 앱 초기화 대기
        Thread.sleep(5000);
        System.out.println("앱 초기화 완료");
    }

    @Test(priority = 1)
    public void testCalculatorLaunch() throws Exception {
        // 앱이 정상적으로 실행되었는지 확인
        String packageName = driver.getCurrentPackage();
        System.out.println("현재 실행 중인 패키지: " + packageName);
        
        // 스크린샷 저장
        takeScreenshot("app-launch");
        
        Assert.assertTrue(packageName.contains("calculator"), "계산기 앱이 실행되지 않았습니다");
    }

    @Test(priority = 2)
    public void testSimpleAddition() throws Exception {
        try {
            System.out.println("덧셈 테스트 시작");
            
            // 간단한 덧셈: 2 + 3 = 5
            clickButton("2");
            Thread.sleep(500);
            
            clickButton("+");
            Thread.sleep(500);
            
            clickButton("3");
            Thread.sleep(500);
            
            clickButton("=");
            Thread.sleep(1000);

            // 결과 확인
            String result = getCalculatorResult();
            System.out.println("계산 결과: " + result);
            
            takeScreenshot("addition-result");
            
            Assert.assertTrue(result.contains("5"), "덧셈 결과가 잘못되었습니다. 실제 결과: " + result);
            
        } catch (Exception e) {
            takeScreenshot("addition-failed");
            System.err.println("덧셈 테스트 실패: " + e.getMessage());
            throw e;
        }
    }

    @Test(priority = 3)
    public void testSimpleSubtraction() throws Exception {
        try {
            System.out.println("뺄셈 테스트 시작");
            
            // Clear 먼저 시도
            clearCalculator();
            Thread.sleep(1000);
            
            // 뺄셈: 9 - 4 = 5
            clickButton("9");
            Thread.sleep(500);
            
            clickButton("-");
            Thread.sleep(500);
            
            clickButton("4");
            Thread.sleep(500);
            
            clickButton("=");
            Thread.sleep(1000);

            String result = getCalculatorResult();
            System.out.println("뺄셈 결과: " + result);
            
            takeScreenshot("subtraction-result");
            
            Assert.assertTrue(result.contains("5"), "뺄셈 결과가 잘못되었습니다. 실제 결과: " + result);
            
        } catch (Exception e) {
            takeScreenshot("subtraction-failed");
            System.err.println("뺄셈 테스트 실패: " + e.getMessage());
            throw e;
        }
    }

    private void clickButton(String buttonText) {
        System.out.println("버튼 클릭 시도: " + buttonText);
        
        // 다양한 방법으로 버튼 찾기 시도
        MobileElement element = null;
        
        // 방법 1: 텍스트로 찾기
        try {
            element = driver.findElement(By.xpath("//android.widget.Button[@text='" + buttonText + "']"));
            if (element.isDisplayed()) {
                element.click();
                System.out.println("버튼 클릭 성공 (텍스트): " + buttonText);
                return;
            }
        } catch (Exception e) {
            System.out.println("텍스트로 버튼 찾기 실패: " + buttonText);
        }

        // 방법 2: content-desc로 찾기
        try {
            element = driver.findElement(By.xpath("//*[@content-desc='" + buttonText + "']"));
            if (element.isDisplayed()) {
                element.click();
                System.out.println("버튼 클릭 성공 (content-desc): " + buttonText);
                return;
            }
        } catch (Exception e) {
            System.out.println("content-desc로 버튼 찾기 실패: " + buttonText);
        }

        // 방법 3: 리소스 ID로 찾기 (숫자와 연산자별로)
        String resourceId = getResourceId(buttonText);
        if (resourceId != null) {
            try {
                element = driver.findElement(By.id(resourceId));
                if (element.isDisplayed()) {
                    element.click();
                    System.out.println("버튼 클릭 성공 (리소스 ID): " + buttonText + " -> " + resourceId);
                    return;
                }
            } catch (Exception e) {
                System.out.println("리소스 ID로 버튼 찾기 실패: " + buttonText + " -> " + resourceId);
            }
        }

        // 방법 4: 클래스명과 텍스트 조합
        try {
            element = driver.findElement(By.xpath("//android.widget.TextView[@text='" + buttonText + "']"));
            if (element.isDisplayed()) {
                element.click();
                System.out.println("버튼 클릭 성공 (TextView): " + buttonText);
                return;
            }
        } catch (Exception e) {
            System.out.println("TextView로 버튼 찾기 실패: " + buttonText);
        }

        throw new RuntimeException("버튼을 찾을 수 없습니다: " + buttonText);
    }

    private String getResourceId(String buttonText) {
        switch (buttonText) {
            case "0": return "com.google.android.calculator:id/digit_0";
            case "1": return "com.google.android.calculator:id/digit_1";
            case "2": return "com.google.android.calculator:id/digit_2";
            case "3": return "com.google.android.calculator:id/digit_3";
            case "4": return "com.google.android.calculator:id/digit_4";
            case "5": return "com.google.android.calculator:id/digit_5";
            case "6": return "com.google.android.calculator:id/digit_6";
            case "7": return "com.google.android.calculator:id/digit_7";
            case "8": return "com.google.android.calculator:id/digit_8";
            case "9": return "com.google.android.calculator:id/digit_9";
            case "+": return "com.google.android.calculator:id/op_add";
            case "-": return "com.google.android.calculator:id/op_sub";
            case "*": return "com.google.android.calculator:id/op_mul";
            case "/": return "com.google.android.calculator:id/op_div";
            case "=": return "com.google.android.calculator:id/eq";
            default: return null;
        }
    }

    private String getCalculatorResult() {
        System.out.println("계산 결과 조회 시작");
        
        // 다양한 방법으로 결과 찾기
        String[] resultSelectors = {
            "com.google.android.calculator:id/result_final",
            "com.google.android.calculator:id/result",
            "com.android.calculator2:id/result",
            "com.android.calculator2:id/formula"
        };

        for (String selector : resultSelectors) {
            try {
                MobileElement resultElement = driver.findElement(By.id(selector));
                if (resultElement.isDisplayed()) {
                    String text = resultElement.getText();
                    if (text != null && !text.trim().isEmpty()) {
                        System.out.println("결과 찾음 (" + selector + "): " + text);
                        return text.trim();
                    }
                }
            } catch (Exception e) {
                System.out.println("결과 selector 실패: " + selector);
            }
        }

        // XPath로 결과 찾기 시도
        try {
            MobileElement resultElement = driver.findElement(By.xpath("//*[contains(@resource-id,'result')]"));
            String text = resultElement.getText();
            System.out.println("XPath로 결과 찾음: " + text);
            return text.trim();
        } catch (Exception e) {
            System.out.println("XPath 결과 찾기 실패");
        }

        throw new RuntimeException("계산 결과를 찾을 수 없습니다");
    }

    private void clearCalculator() {
        System.out.println("계산기 클리어 시도");
        
        String[] clearSelectors = {
            "com.google.android.calculator:id/clr",
            "com.android.calculator2:id/clr"
        };

        for (String selector : clearSelectors) {
            try {
                MobileElement clearElement = driver.findElement(By.id(selector));
                if (clearElement.isDisplayed()) {
                    clearElement.click();
                    System.out.println("클리어 성공: " + selector);
                    return;
                }
            } catch (Exception e) {
                System.out.println("클리어 selector 실패: " + selector);
            }
        }

        // C 또는 AC 버튼 찾기
        try {
            MobileElement clearElement = driver.findElement(By.xpath("//android.widget.Button[@text='C']"));
            clearElement.click();
            System.out.println("C 버튼으로 클리어 성공");
        } catch (Exception e) {
            try {
                MobileElement clearElement = driver.findElement(By.xpath("//android.widget.Button[@text='AC']"));
                clearElement.click();
                System.out.println("AC 버튼으로 클리어 성공");
            } catch (Exception e2) {
                System.out.println("클리어 버튼을 찾을 수 없습니다");
            }
        }
    }

    private void takeScreenshot(String fileName) {
        try {
            File screenshotDir = new File("qa-reports/appium/screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            File src = driver.getScreenshotAs(OutputType.FILE);
            File target = new File(screenshotDir, fileName + ".png");
            FileUtils.copyFile(src, target);
            System.out.println("스크린샷 저장됨: " + target.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("스크린샷 저장 실패: " + e.getMessage());
        }
    }

    @AfterMethod
    public void afterMethod() {
        try {
            Thread.sleep(2000); // 각 테스트 후 충분한 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("드라이버가 정상적으로 종료되었습니다.");
            } catch (Exception e) {
                System.err.println("드라이버 종료 중 오류: " + e.getMessage());
            }
        }
    }
}