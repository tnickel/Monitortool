//+------------------------------------------------------------------+
//|                                                  SqSuperTrend.mq5|
//|                            Copyright © @2021 StrategyQuant s.r.o.|
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property  copyright "Copyright © @2021 StrategyQuant s.r.o."
#property  link      "http://www.strategyquant.com"

#property indicator_chart_window
#property indicator_buffers 1
#property indicator_plots 1

#property indicator_label1  "SqSuperTrend"
#property indicator_type1  DRAW_LINE
#property indicator_color1 Red

//---- indicator parameters
input int    STMode=1;
input int    ATRPeriod=24;
input double ATRMultiplication=3;

//---- internal periods
int inSTMode;
double inATRMultiplication;
int inATRPeriod;
//---- buffers
double ind_buffer[];
//---- handle
int atrHandle;

void OnInit()
  {
   // Refer to SQX Supertrend.java, Mode wasn't used
   inSTMode = 1;
   if(ATRPeriod <= 1 ){
      printf("Incorrect value for input variable ATRPeriod=%d. Indicator will use value=%d for calculations.", ATRPeriod, 24);
      inATRPeriod = 24;
   }
   else inATRPeriod = ATRPeriod;
   
   if(ATRMultiplication <= 0 ){
      printf("Incorrect value for input variable ATRMultiplication=%d. Indicator will use value=%d for calculations.", ATRMultiplication, 3);
      inATRMultiplication = 3;
   }
   else inATRMultiplication = (double)ATRMultiplication;
      

   
      
   ArraySetAsSeries(ind_buffer, true);
   SetIndexBuffer(0, ind_buffer,INDICATOR_DATA);
   PlotIndexSetInteger(0,PLOT_DRAW_BEGIN,inATRPeriod);
   
   atrHandle = iATR(NULL,0,inATRPeriod);

   
//--- indicator short name
   string short_name="SqSRPercRank("+string(inATRPeriod)+","+string(inATRMultiplication)+")";
   IndicatorSetString(INDICATOR_SHORTNAME,short_name);
//---- end of initialization function
}
  
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
   ArraySetAsSeries(open, true);
   ArraySetAsSeries(high, true);
   ArraySetAsSeries(low, true);
   ArraySetAsSeries(close, true);
   
   if(rates_total < ATRPeriod) return(0);
   
   int limit;
   
   if(prev_calculated > 0) limit = rates_total - prev_calculated + 1;
   else {
      for(int a=0; a<rates_total; a++){
         ind_buffer[a] = 0.0;
      }
      
      limit = rates_total - inATRPeriod;
   }
 //--- main indicator loop
 
   for(int i=limit-1; i>=0; i--) {
   
      if(inSTMode == 1){
   
   
          double dAtr = getIndicatorValue(atrHandle, 0, i);
          double dUpperLevel=(high[i]+low[i])/2+inATRMultiplication*dAtr;
          double dLowerLevel=(high[i]+low[i])/2-inATRMultiplication*dAtr;
          
          if(close[i]>ind_buffer[i+1] && close[i+1]<=ind_buffer[i+1]){
          ind_buffer[i] = dLowerLevel;
          }
          else if(close[i]<ind_buffer[i+1] && close[i+1]>=ind_buffer[i+1]){
            ind_buffer[i] = dUpperLevel;
          }
          else if(ind_buffer[i+1]<dLowerLevel){
            ind_buffer[i] = dLowerLevel;
          }
          else if(ind_buffer[i+1]>dUpperLevel){
            ind_buffer[i] = dUpperLevel;
          }
          else ind_buffer[i] = ind_buffer[i+1];
             
          }
   }
   
   return(rates_total);
   
  }
//+------------------------------------------------------------------+



double getIndicatorValue(int indyHandle, int bufferIndex, int shift){
   double buffer[];
   
   if(CopyBuffer(indyHandle, bufferIndex, shift, 1, buffer) < 0) { 
      //--- if the copying fails, tell the error code 
      PrintFormat("Failed to copy data from the indicator, error code %d", GetLastError()); 
      //--- quit with zero result - it means that the indicator is considered as not calculated 
      return(0); 
   } 
   
   double val = buffer[0];
   return val;
}