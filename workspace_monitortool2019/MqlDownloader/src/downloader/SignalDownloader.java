package downloader;



import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Wait;
import com.google.common.base.Function;
import config.ConfigurationManager;
import config.Credentials;

import config.ConfigurationManager;
import config.Credentials;

public class SignalDownloader {
    private final WebDriver driver;
    private final ConfigurationManager configManager;
    private final Credentials credentials;
    private final WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(SignalDownloader.class);

    public SignalDownloader(WebDriver driver, ConfigurationManager configManager, Credentials credentials) {
        this.driver = driver;
        this.configManager = configManager;
        this.credentials = credentials;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    public void startDownloadProcess() {
        try {
            performLogin();
            processSignalProviders();
        } catch (Exception e) {
            logger.error("Error in download process", e);
        } finally {
            closeDriver();
        }
    }

    private void performLogin() {
        logger.info("Starting login process...");
        driver.get("https://www.mql5.com/en/auth_login");

        Function<WebDriver, WebElement> waitFunction = ExpectedConditions.visibilityOfElementLocated(By.id("Login"));
        WebElement usernameField = wait.until(waitFunction);
        WebElement passwordField = driver.findElement(By.id("Password"));


        usernameField.sendKeys(credentials.getUsername());
        passwordField.sendKeys(credentials.getPassword());

        clickLoginButton();
        verifyLogin();
    }

    private void clickLoginButton() {
        WebElement loginButton = findLoginButton();
        if (loginButton != null) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginButton);
        } else {
            throw new RuntimeException("Login button could not be found");
        }
    }

    private WebElement findLoginButton() {
        try {
            return driver.findElement(By.id("loginSubmit"));
        } catch (Exception e) {
            try {
                return driver.findElement(By.cssSelector("input.button.button_yellow.qa-submit"));
            } catch (Exception ex) {
                return null;
            }
        }
    }

    private void verifyLogin() {
        try {
            wait.until(ExpectedConditions.urlContains("/en"));
            Thread.sleep(getRandomWaitTime());
        } catch (Exception e) {
            throw new RuntimeException("Login verification failed", e);
        }
    }

    private void processSignalProviders() {
        int currentPage = 1;
        boolean hasNextPage = true;

        while (hasNextPage) {
            String pageUrl = "https://www.mql5.com/en/signals/mt5/list/page" + currentPage;
            try {
                processSignalProvidersPage(pageUrl);
                currentPage++;
            } catch (RuntimeException e) {
                if (e.getMessage().equals("No signal providers found")) {
                    hasNextPage = false;
                } else {
                    throw e;
                }
            }
        }
    }

    private void processSignalProvidersPage(String pageUrl) {
        driver.get(pageUrl);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("signal")));

        List<WebElement> providerLinks = driver.findElements(By.cssSelector(".signal a[href*='/signals/']"));
        if (providerLinks.isEmpty()) {
            throw new RuntimeException("No signal providers found");
        }

        for (int i = 0; i < providerLinks.size(); i++) {
            processSignalProvider(pageUrl, i);
        }
    }

    private void processSignalProvider(String pageUrl, int index) {
        List<WebElement> providerLinks = driver.findElements(By.cssSelector(".signal a[href*='/signals/']"));
        if (index >= providerLinks.size()) {
            logger.warn("Provider index out of bounds, skipping");
            return;
        }

        WebElement link = providerLinks.get(index);
        String providerUrl = link.getAttribute("href");
        String providerName = link.getText().trim();

        downloadTradeHistory(providerUrl, providerName);
        driver.get(pageUrl);
    }

    private void downloadTradeHistory(String providerUrl, String providerName) {
        driver.get(providerUrl);

        WebElement tradeHistoryTab = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[text()='Trading history']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tradeHistoryTab);

        List<WebElement> exportLinks = driver.findElements(By.xpath("//*[text()='History']"));
        if (exportLinks.isEmpty()) {
            logger.warn("No export link found for provider: " + providerName);
            return;
        }

        WebElement exportLink = exportLinks.get(exportLinks.size() - 1);
        exportLink.click();

        handleDownloadedFile(providerName);
    }

    private void handleDownloadedFile(String providerName) {
        try {
            Thread.sleep(getRandomWaitTime());
            File downloadedFile = findDownloadedFile(configManager.getDownloadPath());
            if (downloadedFile != null && downloadedFile.exists()) {
                File targetFile = new File(configManager.getDownloadPath(), 
                    providerName.replaceAll("[\\/:*?\"<>|]", "_") + ".csv");
                Files.move(downloadedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                logger.info("File downloaded for " + providerName + ": " + targetFile.getAbsolutePath());
            }
        } catch (Exception e) {
            logger.error("Error handling downloaded file for " + providerName, e);
        }
    }

    private void closeDriver() {
        try {
            Thread.sleep(2000);
            if (driver != null) {
                driver.quit();
            }
        } catch (InterruptedException e) {
            logger.error("Error while closing driver", e);
        }
    }

    private static int getRandomWaitTime() {
        return (int) (Math.random() * (30000 - 10000)) + 10000;
    }

    private static File findDownloadedFile(String downloadDirectory) {
        File dir = new File(downloadDirectory);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".csv"));
        return files != null && files.length > 0 ? files[0] : null;
    }
}