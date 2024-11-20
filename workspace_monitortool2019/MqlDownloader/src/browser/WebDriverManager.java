package browser;





import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverManager {
    private final String downloadPath;
    private static final Logger logger = LogManager.getLogger(WebDriverManager.class);

    public WebDriverManager(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public WebDriver initializeDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\tools\\chromedriver.exe");
        
        ChromeOptions options = createChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        
        if (driver == null) {
            throw new RuntimeException("WebDriver could not be initialized");
        }
        
        return driver;
    }

    private ChromeOptions createChromeOptions() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadPath);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        
        return options;
    }
}