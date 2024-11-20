package main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import browser.WebDriverManager;
import config.ConfigurationManager;
import config.Credentials;
import downloader.SignalDownloader;
import logging.LoggerManager;

public class StartDownloader
{
	private static final Logger logger = LogManager.getLogger(StartDownloader.class);
	public StartDownloader()
	{}
	
	public static void main(String[] args)
	{
		try
		{
			// Initialize configuration
			ConfigurationManager configManager = new ConfigurationManager("C:\\tmp\\mql5");
			configManager.initializeDirectories();
			
			// Initialize logger
			LoggerManager.initializeLogger(configManager.getLogConfigPath());
			
			// Get credentials
			Credentials credentials = configManager.getOrCreateCredentials();
			
			// Initialize WebDriver
			WebDriverManager webDriverManager = new WebDriverManager(configManager.getDownloadPath());
			WebDriver driver = webDriverManager.initializeDriver();
			
			// Start the download process
			SignalDownloader downloader = new SignalDownloader(driver, configManager, credentials);
			downloader.startDownloadProcess();
			
		} catch (Exception e)
		{
			logger.error("Error in main process", e);
		}
	}
}
