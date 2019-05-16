package examples.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

/**
* This example of a simple browser using the SWT Browser widget has
* been borrowed from the Eclipse Visual Editor Project.
*/
public class SWTBrowserExample {

    public static final String APP_TITLE = "Simple SWT Browser";
    public static final String HOME_URL = "http://www.cloudgarden.com/jigloo/";
    private Button refreshButton;
    private Label statusText;
    private ProgressBar progressBar;
    private Browser browser;
    private Button goButton;
    private Text locationText;
    private Button homeButton;
    private Button stopButton;
    private Button forwardButton;
    private Button backButton;

    private org.eclipse.swt.widgets.Shell sShell = null;

    /**
     * This method initializes browser	
     *
     */
    private void createBrowser() {
		browser = new Browser(sShell, SWT.BORDER);
		browser.setUrl(HOME_URL);
		GridData gridData3 = new GridData();
		browser.addLocationListener(new LocationAdapter() {
			public void changing(LocationEvent evt) {
				locationText.setText(evt.location);
			}
			public void changed(LocationEvent evt) {
			}
		});
		browser.addStatusTextListener(new StatusTextListener() {
			public void changed(StatusTextEvent evt) {
				statusText.setText(evt.text);
			}
		});
		browser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent evt) {
				sShell.setText(APP_TITLE + " - " + evt.title);
			}
		});
		browser.addProgressListener(new ProgressAdapter() {
			public void completed(ProgressEvent evt) {
				stopButton.setEnabled(false);
				backButton.setEnabled(browser.isBackEnabled());
				forwardButton.setEnabled(browser.isForwardEnabled());
				progressBar.setSelection(0);
			}
			public void changed(ProgressEvent evt) {
				if (!stopButton.isEnabled() && evt.total != evt.current) {
					stopButton.setEnabled(true);
				}
				progressBar.setMaximum(evt.total);
				progressBar.setSelection(evt.current);
			}
		});
		gridData3.verticalAlignment = GridData.FILL;
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.horizontalSpan = 7;
		gridData3.grabExcessVerticalSpace = true;
		browser.setLayoutData(gridData3);
    }

    public static void main(String[] args) {
        /* Before this is run, be sure to set up the following in the launch configuration 
         * (Arguments->VM Arguments) for the correct SWT library path. 
         * The following is a windows example:
         * -Djava.library.path="installation_directory\plugins\org.eclipse.swt.win32_3.0.0\os\win32\x86"
         */
        org.eclipse.swt.widgets.Display display = org.eclipse.swt.widgets.Display
                .getDefault();
        SWTBrowserExample thisClass = new SWTBrowserExample();
        thisClass.createSShell();
        thisClass.sShell.open();

        while (!thisClass.sShell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }

    /**
     * This method initializes sShell
     */
    private void createSShell() {
        sShell = new org.eclipse.swt.widgets.Shell();
        org.eclipse.swt.layout.GridLayout gridLayout1 = new GridLayout();
        sShell.setText(APP_TITLE);
        sShell.setLayout(gridLayout1);
        gridLayout1.numColumns = 7;
        sShell.setSize(new org.eclipse.swt.graphics.Point(553, 367));
		{
			backButton = new Button(sShell, SWT.ARROW | SWT.LEFT);
			GridData gridData6 = new GridData();
			backButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					browser.back();
				}
			});
			backButton.setToolTipText("Navigate back to the previous page");
			backButton.setEnabled(false);
			gridData6.verticalAlignment = GridData.FILL;
			gridData6.horizontalAlignment = GridData.FILL;
			backButton.setLayoutData(gridData6);
		}
		{
			forwardButton = new Button(sShell, SWT.ARROW | SWT.RIGHT);
			GridData gridData5 = new GridData();
			forwardButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					browser.forward();
				}
			});
			forwardButton.setToolTipText("Navigate forward to the next page");
			forwardButton.setEnabled(false);
			gridData5.verticalAlignment = GridData.FILL;
			gridData5.horizontalAlignment = GridData.FILL;
			forwardButton.setLayoutData(gridData5);
		}
		{
			stopButton = new Button(sShell, SWT.NONE);
			stopButton.setText("Stop");
			stopButton.setEnabled(false);
			stopButton.setToolTipText("Stop the loading of the current page");
			stopButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					browser.stop();
				}
			});
		}
		{
			refreshButton = new Button(sShell, SWT.NONE);
			refreshButton.setText("Refresh");
			refreshButton.setToolTipText("Refresh the current page");
			refreshButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					browser.refresh();
				}
			});
		}
		{
			homeButton = new Button(sShell, SWT.NONE);
			homeButton.setText("Home");
			homeButton.setToolTipText("Return to home page");
			homeButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					browser.setUrl(HOME_URL);
				}
			});
		}
		{
			locationText = new Text(sShell, SWT.BORDER);
			locationText.setText(HOME_URL);
			GridData gridData2 = new GridData();
			locationText.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent evt) {
					// Handle the press of the Enter key in the locationText.
					// This will browse to the entered text.
					if (evt.character == SWT.LF || evt.character == SWT.CR) {
						evt.doit = false;
						browser.setUrl(locationText.getText());
					}
				}
			});
			locationText.addMouseListener(new MouseAdapter() {
				public void mouseUp(MouseEvent evt) {
					locationText.selectAll();
				}
			});
			locationText.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					browser.setUrl(locationText.getText());
				}
			});
			locationText.setToolTipText("Enter a web address");
			gridData2.horizontalAlignment = GridData.FILL;
			gridData2.grabExcessHorizontalSpace = true;
			locationText.setLayoutData(gridData2);
		}
		{
			goButton = new Button(sShell, SWT.NONE);
			goButton.setText("Go!");
			GridData gridData8 = new GridData();
			goButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					browser.setUrl(locationText.getText());
				}
			});
			goButton.setToolTipText("Navigate to the selected web address");
			gridData8.horizontalAlignment = GridData.END;
			goButton.setLayoutData(gridData8);
		}

		createBrowser();
		
		{
			progressBar = new ProgressBar(sShell, SWT.BORDER);
			GridData gridData4 = new GridData();
			progressBar.setEnabled(false);
			gridData4.horizontalSpan = 5;
			progressBar.setLayoutData(gridData4);
		}
		{
			statusText = new Label(sShell, SWT.NONE);
			statusText.setText("Done");
			GridData gridData7 = new GridData();
			gridData7.horizontalAlignment = GridData.FILL;
			gridData7.grabExcessHorizontalSpace = true;
			statusText.setLayoutData(gridData7);
		}
    }
}
