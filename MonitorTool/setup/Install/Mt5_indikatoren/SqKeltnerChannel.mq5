//+------------------------------------------------------------------+
//|                                               KeltnerChannel.mq5 |
//|                           Copyright © 2017, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright "Copyright © 2017, StrategyQuant s.r.o."
#property link      "http://www.strategyquant.com"
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
#property indicator_chart_window
#property indicator_buffers 3
#property indicator_plots 3

#property indicator_label1  "Upper"
#property indicator_type1  DRAW_LINE
#property indicator_color1 Blue

#property indicator_label2  "Lower"
#property indicator_type2  DRAW_LINE
#property indicator_color2 Red

#property indicator_label3  "Middle"
#property indicator_type3  DRAW_LINE
#property indicator_color3 White


double upper[], middle[], lower[];
input int     MAPeriod = 20;
input double  Const = 1.5;

int period;

void OnInit()
  {
//--- check for input parameters
   if(MAPeriod <= 0){
      printf("Incorrect value for input variable MAPeriod=%d. Indicator will use value=%d for calculations.", MAPeriod, 14);
      period = 14;
   }
   else period = MAPeriod;
   
   ArraySetAsSeries(upper, true);
   ArraySetAsSeries(lower, true);
   ArraySetAsSeries(middle, true);
   
   SetIndexBuffer(0, upper);
   SetIndexBuffer(1, lower);
   SetIndexBuffer(2, middle);
   
   PlotIndexSetInteger(1, PLOT_LINE_STYLE, STYLE_DOT);
   
//--- indicator short name
   string short_name="SqKeltnerChannel("+string(period)+","+string(Const)+")";
   IndicatorSetString(INDICATOR_SHORTNAME,short_name);
//---- end of initialization function
}
  
//+------------------------------------------------------------------+
//| Custom indicator iteration function                              |
//+------------------------------------------------------------------+
int OnCalculate(const int rates_total,
                const int prev_calculated,
                const datetime &time[],
                const double &open[],
                const double &high[],
                const double &low[],
                const double &close[],
                const long &tick_volume[],
                const long &volume[],
                const int &spread[])
  {
//--- checking for bars count
   if(rates_total < period) return(0);
   
   ArraySetAsSeries(time, true);
   ArraySetAsSeries(open, true);
   ArraySetAsSeries(high, true);
   ArraySetAsSeries(low, true);
   ArraySetAsSeries(close, true);
   
   int limit;
   double offset;
   
   if(prev_calculated > 0) limit = rates_total - prev_calculated + 1;
   else {
      for(int a=0; a<rates_total; a++){
         upper[a] = 0.0;
         middle[a] = 0.0;
         lower[a] = 0.0;
      }
      
      limit = rates_total - period;
   }
   
   for(int x=0; x<limit && !IsStopped(); x++) {     
      offset = avgDiff(high, low, period, x) * Const;
       
      middle[x] = avgTrueRange(high, low, close, period, x);
      upper[x] = middle[x] + offset;
      lower[x] = middle[x] - offset;
   }
   
   return(rates_total);
}

//+------------------------------------------------------------------+

double avgTrueRange(const double &high[], const double &low[], const double &close[], int atrPeriod, int shift) {
  double sum=0;
  for (int x=shift;x<(shift+atrPeriod);x++) {     
     sum += (high[x] + low[x] + close[x]) / 3;
  }
  
  sum = sum / atrPeriod;
  return (sum);
}

double avgDiff(const double &high[], const double &low[], int atrPeriod, int shift) {
  double sum=0;
  for (int x=shift;x<(shift+atrPeriod);x++) {     
     sum += high[x] - low[x];
  }
  
  sum = sum / atrPeriod;
  return (sum);
}


double getIndicatorValue(int indyHandle, int bufferIndex, int shift){
   double buffer[];
   
   if(CopyBuffer(indyHandle, bufferIndex, shift, 1, buffer) < 0) { 
      //--- if the copying fails, tell the error code 
      PrintFormat("Failed to copy data from the indicator, error code %d", GetLastError()); 
      //--- quit with zero result - it means that the indicator is considered as not calculated 
      return(0); 
   } 
   
   return buffer[0];
}