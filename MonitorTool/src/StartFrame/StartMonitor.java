package StartFrame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import StartFrame.config.StartMonitorConfig;
import StartFrame.events.StartMonitorEventBus;
import StartFrame.managers.MenuManager;
import StartFrame.panels.BrokerManagementPanel;
import StartFrame.panels.ControlsPanel;
import StartFrame.panels.ProfitDisplayPanel;
import StartFrame.panels.TradeManagementPanel;
import StartFrame.events.ProfitData;
import data.GlobalVar;

/**
 * Hauptklasse für den StartMonitor - koordiniert alle Panels
 * Deutlich reduzierte Größe durch Aufteilung in funktionale Komponenten
 */
public class StartMonitor extends Composite {
    
    // === PANELS ===
    private TradeManagementPanel tradePanel;
    private BrokerManagementPanel brokerPanel;
    private ProfitDisplayPanel profitPanel;
    private ControlsPanel controlsPanel;
    
    // === MANAGERS ===
    private MenuManager menuManager;
    private StartMonitorEventBus eventBus;
    
    // === CORE DATA ===
    private static Display dis_glob = null;
    private StartMonitorWork smw = null;
    
    public StartMonitor(Composite parent, int style) {
        super(parent, style);
        initializeComponents();
        createLayout();
        wireEvents();
    }

    /**
     * Initialisiert alle Komponenten
     */
    private void initializeComponents() {
        // Event-Bus für Panel-Kommunikation
        eventBus = new StartMonitorEventBus();
        
        // Core Business Logic (vereinfacht für jetzt)
        // smw = new StartMonitorWork(dis_glob, null, null);
        
        // Panels erstellen
        tradePanel = new TradeManagementPanel(this, SWT.NONE, eventBus, smw);
        brokerPanel = new BrokerManagementPanel(this, SWT.NONE, eventBus, smw);
        profitPanel = new ProfitDisplayPanel(this, SWT.NONE, eventBus, smw);
        controlsPanel = new ControlsPanel(this, SWT.NONE, eventBus, smw);
        
        // Menu Manager
        menuManager = new MenuManager(getShell(), eventBus, smw);
    }

    /**
     * Erstellt das Layout
     */
    private void createLayout() {
        this.setSize(800, 600); // Kleinere Größe für Test
        FormLayout layout = new FormLayout();
        this.setLayout(layout);

        // === CONTROLS PANEL (Oben) ===
        FormData controlsData = new FormData();
        controlsData.left = new FormAttachment(0, 0);
        controlsData.top = new FormAttachment(0, 0);
        controlsData.right = new FormAttachment(100, 0);
        controlsData.height = 60;
        controlsPanel.setLayoutData(controlsData);

        // === TRADE MANAGEMENT (Links) ===
        FormData tradeData = new FormData();
        tradeData.left = new FormAttachment(0, 0);
        tradeData.top = new FormAttachment(controlsPanel, 5);
        tradeData.width = 400;
        tradeData.bottom = new FormAttachment(profitPanel, -5);
        tradePanel.setLayoutData(tradeData);

        // === BROKER MANAGEMENT (Rechts) ===
        FormData brokerData = new FormData();
        brokerData.left = new FormAttachment(tradePanel, 5);
        brokerData.top = new FormAttachment(controlsPanel, 5);
        brokerData.right = new FormAttachment(100, 0);
        brokerData.bottom = new FormAttachment(profitPanel, -5);
        brokerPanel.setLayoutData(brokerData);

        // === PROFIT DISPLAY (Unten) ===
        FormData profitData = new FormData();
        profitData.left = new FormAttachment(0, 0);
        profitData.right = new FormAttachment(100, 0);
        profitData.bottom = new FormAttachment(100, 0);
        profitData.height = 80;
        profitPanel.setLayoutData(profitData);
        
        this.layout();
    }

    /**
     * Verbindet Events zwischen Panels
     */
    private void wireEvents() {
        // Panel-zu-Panel Kommunikation über EventBus
        eventBus.addBrokerSelectionListener((brokerPath) -> {
            tradePanel.onBrokerSelected(brokerPath);
            profitPanel.onBrokerSelected(brokerPath);
        });
        
        eventBus.addTradeSelectionListener((tradeId) -> {
            profitPanel.onTradeSelected(tradeId);
        });
        
        eventBus.addProfitUpdateListener((profitData) -> {
            profitPanel.updateProfitData(profitData);
        });
    }

    /**
     * Öffentliche API für Panels
     */
    public StartMonitorWork getStartMonitorWork() {
        return smw;
    }
    
    public StartMonitorEventBus getEventBus() {
        return eventBus;
    }

    /**
     * Hauptmethode - deutlich vereinfacht
     */
    public static void main(String[] args) {
        try {
            // Basis-Initialisierung
            String userdir = initializeEnvironment(args);
            
            // Display und Shell
            dis_glob = new Display();
            Shell shell = new Shell(dis_glob);
            shell.setText("StartMonitor - Refactored");
            
            // Konfiguration laden
            StartMonitorConfig.initialize(userdir);
            
            // Hauptfenster erstellen
            StartMonitor monitor = new StartMonitor(shell, SWT.NULL);
            
            // Layout finalisieren
            shell.setLayout(new FillLayout());
            shell.layout();
            shell.pack();
            shell.open();
            
            // Event-Loop
            while (!shell.isDisposed()) {
                if (!dis_glob.readAndDispatch()) {
                    dis_glob.sleep();
                }
            }
            
            // Cleanup
            if (GlobalVar.class != null) {
                // GlobalVar.setIpthreadflag(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Environment-Initialisierung (ausgelagert für bessere Lesbarkeit)
     */
    private static String initializeEnvironment(String[] args) {
        String userdir = System.getProperty("user.dir");
        
        if (args.length > 0 && args[0] != null) {
            userdir = args[0];
        }
        
        // Vereinfachte Prüfung
        if (userdir == null) {
            userdir = System.getProperty("user.dir");
        }
        
        System.out.println("Userdir: " + userdir);
        return userdir;
    }
}