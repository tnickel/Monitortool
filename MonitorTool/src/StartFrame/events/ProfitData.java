package StartFrame.events;

/**
 * PUBLIC Datenklasse für Profit-Updates zwischen Panels
 * WICHTIG: Die Klasse MUSS public sein!
 */
public class ProfitData {
    private double weeklyProfit;
    private double monthlyProfit;
    private double totalProfit;
    private String brokerPath;
    private long timestamp;
    
    // === CONSTRUCTORS ===
    public ProfitData(double weeklyProfit, double monthlyProfit, double totalProfit) {
        this.weeklyProfit = weeklyProfit;
        this.monthlyProfit = monthlyProfit;
        this.totalProfit = totalProfit;
        this.timestamp = System.currentTimeMillis();
    }
    
    public ProfitData(double weeklyProfit, double monthlyProfit, double totalProfit, String brokerPath) {
        this(weeklyProfit, monthlyProfit, totalProfit);
        this.brokerPath = brokerPath;
    }
    
    // === GETTERS ===
    public double getWeeklyProfit() { 
        return weeklyProfit; 
    }
    
    public double getMonthlyProfit() { 
        return monthlyProfit; 
    }
    
    public double getTotalProfit() { 
        return totalProfit; 
    }
    
    public String getBrokerPath() { 
        return brokerPath; 
    }
    
    public long getTimestamp() { 
        return timestamp; 
    }
    
    // === SETTERS ===
    public void setWeeklyProfit(double weeklyProfit) { 
        this.weeklyProfit = weeklyProfit; 
    }
    
    public void setMonthlyProfit(double monthlyProfit) { 
        this.monthlyProfit = monthlyProfit; 
    }
    
    public void setTotalProfit(double totalProfit) { 
        this.totalProfit = totalProfit; 
    }
    
    public void setBrokerPath(String brokerPath) { 
        this.brokerPath = brokerPath; 
    }
    
    // === UTILITY METHODS ===
    @Override
    public String toString() {
        return String.format("ProfitData{weekly=%.2f, monthly=%.2f, total=%.2f, broker='%s'}", 
                            weeklyProfit, monthlyProfit, totalProfit, brokerPath);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ProfitData that = (ProfitData) obj;
        return Double.compare(that.weeklyProfit, weeklyProfit) == 0 &&
               Double.compare(that.monthlyProfit, monthlyProfit) == 0 &&
               Double.compare(that.totalProfit, totalProfit) == 0;
    }
    
    @Override
    public int hashCode() {
        return (int) (weeklyProfit + monthlyProfit + totalProfit);
    }
}