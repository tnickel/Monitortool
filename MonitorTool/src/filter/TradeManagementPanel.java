package StartFrame.panels;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import StartFrame.StartMonitorWork;
import StartFrame.events.StartMonitorEventBus;
import filter.Tradefilter;

/**
 * Panel für Trade-Management
 * Enthält: Trade-Tabellen, Filter, Search, Trade-Controls
 */
public class TradeManagementPanel extends Composite {
    
    // === COMPONENTS ===
    private Table table1;           // Übersichtstabelle
    private Table table2;           // Haupt-Trade-Tabelle  
    private Text search;            // Suchfeld
    private Button searchButton;    // Such-Button
    private Button tradeFilter;     // Trade-Filter Toggle
    private Button setTradeFilter;  // Trade-Filter Config
    private Button fontPlus;        // Schrift vergrößern
    private Button fontMinus;       // Schrift verkleinern
    private Label overviewLabel;    // "Overview" Label
    
    // === DATA & LOGIC ===
    private StartMonitorEventBus eventBus;
    private StartMonitorWork smw;
    private Tradefilter tf;
    private int table2FontSize = 9;
    
    public TradeManagementPanel(Composite parent, int style, 
                               StartMonitorEventBus eventBus, 
                               StartMonitorWork smw) {
        super(parent, style);
        this.eventBus = eventBus;
        this.smw = smw;
        this.tf = new Tradefilter();
        
        createComponents();
        createLayout();
        wireEvents();
    }

    /**
     * Erstellt alle Komponenten
     */
    private void createComponents() {
        
        // === LABELS ===
        overviewLabel = new Label(this, SWT.NONE);
        overviewLabel.setText("Overview");
        
        // === TABLES ===
        table1 = new Table(this, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        table1.setFont(getFont("Segoe UI", 8));
        
        table2 = new Table(this, SWT.CHECK | SWT.FULL_SELECTION | SWT.BORDER);
        table2.setFont(getFont("Segoe UI", table2FontSize));
        
        // === SEARCH ===
        search = new Text(this, SWT.BORDER);
        searchButton = new Button(this, SWT.PUSH | SWT.CENTER);
        searchButton.setText("Search");
        
        // === FILTER CONTROLS ===
        tradeFilter = new Button(this, SWT.CHECK | SWT.LEFT);
        tradeFilter.setText("Trade Filter");
        
        setTradeFilter = new Button(this, SWT.PUSH | SWT.CENTER);
        setTradeFilter.setText("Set Filter");
        
        // === FONT CONTROLS ===
        fontPlus = new Button(this, SWT.PUSH | SWT.CENTER);
        fontPlus.setText("+");
        
        fontMinus = new Button(this, SWT.PUSH | SWT.CENTER);
        fontMinus.setText("-");
    }

    /**
     * Erstellt das Layout
     */
    private void createLayout() {
        FormLayout layout = new FormLayout();
        this.setLayout(layout);
        
        // === OVERVIEW LABEL ===
        FormData overviewData = new FormData();
        overviewData.left = new FormAttachment(0, 5);
        overviewData.top = new FormAttachment(0, 5);
        overviewData.width = 72;
        overviewData.height = 26;
        overviewLabel.setLayoutData(overviewData);
        
        // === TABLE 1 (Links - Übersicht) ===
        FormData table1Data = new FormData();
        table1Data.left = new FormAttachment(0, 5);
        table1Data.top = new FormAttachment(overviewLabel, 5);
        table1Data.width = 313;
        table1Data.bottom = new FormAttachment(100, -40);
        table1.setLayoutData(table1Data);
        
        // === TABLE 2 (Rechts - Haupttabelle) ===
        FormData table2Data = new FormData();
        table2Data.left = new FormAttachment(table1, 10);
        table2Data.top = new FormAttachment(overviewLabel, 5);
        table2Data.right = new FormAttachment(100, -5);
        table2Data.bottom = new FormAttachment(search, -10);
        table2.setLayoutData(table2Data);
        
        // === SEARCH CONTROLS (Unten) ===
        FormData searchData = new FormData();
        searchData.left = new FormAttachment(table1, 10);
        searchData.bottom = new FormAttachment(100, -5);
        searchData.width = 168;
        searchData.height = 28;
        search.setLayoutData(searchData);
        
        FormData searchButtonData = new FormData();
        searchButtonData.left = new FormAttachment(search, 5);
        searchButtonData.bottom = new FormAttachment(100, -5);
        searchButtonData.width = 80;
        searchButtonData.height = 28;
        searchButton.setLayoutData(searchButtonData);
        
        // === FILTER CONTROLS ===
        FormData tradeFilterData = new FormData();
        tradeFilterData.left = new FormAttachment(searchButton, 20);
        tradeFilterData.bottom = new FormAttachment(100, -10);
        tradeFilterData.width = 100;
        tradeFilterData.height = 20;
        tradeFilter.setLayoutData(tradeFilterData);
        
        FormData setFilterData = new FormData();
        setFilterData.left = new FormAttachment(tradeFilter, 10);
        setFilterData.bottom = new FormAttachment(100, -10);
        setFilterData.width = 70;
        setFilterData.height = 20;
        setTradeFilter.setLayoutData(setFilterData);
        
        // === FONT CONTROLS ===
        FormData fontPlusData = new FormData();
        fontPlusData.right = new FormAttachment(100, -30);
        fontPlusData.bottom = new FormAttachment(100, -10);
        fontPlusData.width = 25;
        fontPlusData.height = 20;
        fontPlus.setLayoutData(fontPlusData);
        
        FormData fontMinusData = new FormData();
        fontMinusData.right = new FormAttachment(fontPlus, -5);
        fontMinusData.bottom = new FormAttachment(100, -10);
        fontMinusData.width = 25;
        fontMinusData.height = 20;
        fontMinus.setLayoutData(fontMinusData);
    }

    /**
     * Event-Handler verbinden
     */
    private void wireEvents() {
        
        // === TABLE EVENTS ===
        table2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent evt) {
                handleTable2Selection(evt);
            }
        });
        
        // === SEARCH EVENTS ===
        searchButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent evt) {
                handleSearch();
            }
        });
        
        // === FILTER EVENTS ===
        tradeFilter.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent evt) {
                tf.setTradefiltermainselection(tradeFilter.getSelection());
            }
        });
        
        setTradeFilter.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent evt) {
                smw.TradefilterConfig(tf);
                tradeFilter.setSelection(true);
            }
        });
        
        // === FONT EVENTS ===
        fontPlus.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent evt) {
                table2FontSize++;
                table2.setFont(getFont("Segoe UI", table2FontSize));
            }
        });
        
        fontMinus.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent evt) {
                table2FontSize--;
                table2.setFont(getFont("Segoe UI", table2FontSize));
            }
        });
    }

    /**
     * Behandelt Tabelle 2 Selektion
     */
    private void handleTable2Selection(SelectionEvent evt) {
        String seltext = evt.item.toString();
        String name = "";
        
        if (seltext.contains("{")) {
            String sel = seltext.substring(seltext.indexOf("{") + 1);
            name = sel.replace("}", "");
        }
        
        try {
            int tradeId = Integer.parseInt(name);
            // Event an andere Panels senden
            eventBus.fireTradeSelected(tradeId);
            
            // Business Logic
            smw.table2clicked(tradeId, name, null, table2);
        } catch (NumberFormatException e) {
            System.err.println("Ungültige Trade-ID: " + name);
        }
    }

    /**
     * Behandelt Suche
     */
    private void handleSearch() {
        String searchId = search.getText();
        if (searchId != null && !searchId.trim().isEmpty()) {
            smw.searchSelId(searchId.trim());
        }
    }

    /**
     * Wird aufgerufen wenn Broker selektiert wird
     */
    public void onBrokerSelected(String brokerPath) {
        // Trade-Daten für selektierten Broker laden
        if (smw != null && brokerPath != null) {
            // Hier würde die Broker-spezifische Trade-Daten laden
            refreshTradeData(brokerPath);
        }
    }

    /**
     * Lädt Trade-Daten für Broker neu
     */
    private void refreshTradeData(String brokerPath) {
        // TODO: Trade-Daten für Broker laden
        // smw.loadTradesForBroker(brokerPath, table1, table2, tf);
    }

    /**
     * Hilfsmethode für Font-Erstellung
     */
    private org.eclipse.swt.graphics.Font getFont(String name, int size) {
        return new org.eclipse.swt.graphics.Font(getDisplay(), name, size, SWT.NORMAL);
    }
    
    // === PUBLIC API ===
    
    public Table getTable1() { return table1; }
    public Table getTable2() { return table2; }
    public Tradefilter getTradefilter() { return tf; }
    
    public void setTradeFilterEnabled(boolean enabled) {
        tradeFilter.setSelection(enabled);
        tf.setTradefiltermainselection(enabled);
    }
    
    public void clearSearch() {
        search.setText("");
    }
}