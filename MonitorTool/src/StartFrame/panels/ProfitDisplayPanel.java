package StartFrame.panels;

import org.eclipse.swt.widgets.Composite;
import StartFrame.StartMonitorWork;
import StartFrame.events.StartMonitorEventBus;
import StartFrame.events.ProfitData;

public class ProfitDisplayPanel extends Composite {
    public ProfitDisplayPanel(Composite parent, int style, StartMonitorEventBus eventBus, StartMonitorWork smw) {
        super(parent, style);
        // TODO: Implementierung später hinzufügen
    }
    
    public void onBrokerSelected(String brokerPath) {
        // TODO: Implementierung
    }
    
    public void onTradeSelected(int tradeId) {
        // TODO: Implementierung
    }
    
    public void updateProfitData(ProfitData profitData) {
        // TODO: Implementierung
    }
}
