package StartFrame.panels;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import StartFrame.StartMonitorWork;
import StartFrame.events.StartMonitorEventBus;

/**
 * Trade Management Panel - Stub Implementation
 */
public class TradeManagementPanel extends Composite {
    
    private StartMonitorEventBus eventBus;
    private StartMonitorWork smw;
    
    public TradeManagementPanel(Composite parent, int style, StartMonitorEventBus eventBus, StartMonitorWork smw) {
        super(parent, style);
        this.eventBus = eventBus;
        this.smw = smw;
        
        createContent();
    }
    
    private void createContent() {
        // Temporärer Inhalt
        Label label = new Label(this, SWT.NONE);
        label.setText("Trade Management Panel");
        label.setBounds(10, 10, 200, 30);
    }
    
    public void onBrokerSelected(String brokerPath) {
        System.out.println("TradePanel: Broker selected: " + brokerPath);
    }
}