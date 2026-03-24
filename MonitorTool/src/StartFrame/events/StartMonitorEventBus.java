package StartFrame.events;

import java.util.ArrayList;
import java.util.List;

/**
 * Event-Bus für Panel-Kommunikation
 * OHNE eingebettete ProfitData-Klasse
 */
public class StartMonitorEventBus {
    
    // === BROKER EVENTS ===
    public interface BrokerSelectionListener {
        void onBrokerSelected(String brokerPath);
    }
    
    private List<BrokerSelectionListener> brokerListeners = new ArrayList<>();
    
    public void addBrokerSelectionListener(BrokerSelectionListener listener) {
        if (listener != null) {
            brokerListeners.add(listener);
        }
    }
    
    public void removeBrokerSelectionListener(BrokerSelectionListener listener) {
        brokerListeners.remove(listener);
    }
    
    public void fireBrokerSelected(String brokerPath) {
        for (BrokerSelectionListener listener : brokerListeners) {
            try {
                listener.onBrokerSelected(brokerPath);
            } catch (Exception e) {
                System.err.println("Error in broker selection listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    // === TRADE EVENTS ===
    public interface TradeSelectionListener {
        void onTradeSelected(int tradeId);
    }
    
    private List<TradeSelectionListener> tradeListeners = new ArrayList<>();
    
    public void addTradeSelectionListener(TradeSelectionListener listener) {
        if (listener != null) {
            tradeListeners.add(listener);
        }
    }
    
    public void removeTradeSelectionListener(TradeSelectionListener listener) {
        tradeListeners.remove(listener);
    }
    
    public void fireTradeSelected(int tradeId) {
        for (TradeSelectionListener listener : tradeListeners) {
            try {
                listener.onTradeSelected(tradeId);
            } catch (Exception e) {
                System.err.println("Error in trade selection listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    // === PROFIT EVENTS ===
    public interface ProfitUpdateListener {
        void onProfitUpdate(ProfitData profitData);
    }
    
    private List<ProfitUpdateListener> profitListeners = new ArrayList<>();
    
    public void addProfitUpdateListener(ProfitUpdateListener listener) {
        if (listener != null) {
            profitListeners.add(listener);
        }
    }
    
    public void removeProfitUpdateListener(ProfitUpdateListener listener) {
        profitListeners.remove(listener);
    }
    
    public void fireProfitUpdate(ProfitData profitData) {
        for (ProfitUpdateListener listener : profitListeners) {
            try {
                listener.onProfitUpdate(profitData);
            } catch (Exception e) {
                System.err.println("Error in profit update listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    // === STATUS EVENTS ===
    public interface StatusUpdateListener {
        void onStatusUpdate(String message, StatusType type);
    }
    
    public enum StatusType {
        INFO, WARNING, ERROR, SUCCESS
    }
    
    private List<StatusUpdateListener> statusListeners = new ArrayList<>();
    
    public void addStatusUpdateListener(StatusUpdateListener listener) {
        if (listener != null) {
            statusListeners.add(listener);
        }
    }
    
    public void removeStatusUpdateListener(StatusUpdateListener listener) {
        statusListeners.remove(listener);
    }
    
    public void fireStatusUpdate(String message, StatusType type) {
        for (StatusUpdateListener listener : statusListeners) {
            try {
                listener.onStatusUpdate(message, type);
            } catch (Exception e) {
                System.err.println("Error in status update listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    // === CONVENIENCE METHODS ===
    public void fireInfoStatus(String message) {
        fireStatusUpdate(message, StatusType.INFO);
    }
    
    public void fireWarningStatus(String message) {
        fireStatusUpdate(message, StatusType.WARNING);
    }
    
    public void fireErrorStatus(String message) {
        fireStatusUpdate(message, StatusType.ERROR);
    }
    
    public void fireSuccessStatus(String message) {
        fireStatusUpdate(message, StatusType.SUCCESS);
    }
    
    // === CLEANUP ===
    public void clearAllListeners() {
        brokerListeners.clear();
        tradeListeners.clear();
        profitListeners.clear();
        statusListeners.clear();
    }
    
    // === DEBUG INFO ===
    public int getBrokerListenerCount() { return brokerListeners.size(); }
    public int getTradeListenerCount() { return tradeListeners.size(); }
    public int getProfitListenerCount() { return profitListeners.size(); }
    public int getStatusListenerCount() { return statusListeners.size(); }
}