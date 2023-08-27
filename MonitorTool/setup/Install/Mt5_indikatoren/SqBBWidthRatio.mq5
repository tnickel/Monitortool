//+------------------------------------------------------------------+
//|                                               SQBBWidthRatio.mq5 |
//|                           Copyright © @2017 StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property  copyright "Copyright © @2017 StrategyQuant s.r.o."
#property  link      "http://www.strategyquant.com"

#property indicator_separate_window
#property indicator_buffers 1
#property indicator_plots 1

#property indicator_label1  "Bollinger Bands width ratio"
#property indicator_type1  DRAW_LINE
#property indicator_color1 Blue

//---- indicator parameters
input int    BandsPeriod=20;
input double BandsDeviations=2.0;
input int    AppliedPrice=PRICE_CLOSE;

//---- buffers
double ind_buffer[];

int period, mode;
int stdDevHandle, smaHandle;

void OnInit()
  {
   if(BandsPeriod <= 0){
      printf("Incorrect value for input variable BandsPeriod=%d. Indicator will use value=%d for calculations.", BandsPeriod, 14);
      period = 14;
   }
   else period = BandsPeriod;
   
   switch(AppliedPrice){
      case PRICE_OPEN:
      case PRICE_HIGH:
      case PRICE_LOW:
      case PRICE_CLOSE:
      case PRICE_MEDIAN:
      case PRICE_TYPICAL:
      case PRICE_WEIGHTED:
         mode = AppliedPrice;
         break;
      default:
         printf("Incorrect value for input variable AppliedPrice=%d. Indicator will use value PRICE_CLOSE for calculations.", AppliedPrice);
         mode = PRICE_CLOSE;
   }
   
   ArraySetAsSeries(ind_buffer, true);
   
   SetIndexBuffer(0, ind_buffer);
   
   stdDevHandle = iStdDev(NULL, 0, period, 0, MODE_SMA, mode); 
   smaHandle = iMA(NULL, 0, period, 0, MODE_SMA, mode); 
   
//--- indicator short name
   string short_name="BBWidthRatio("+string(period)+","+string(BandsDeviations)+")";
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
   
   if(rates_total < period) return(0);
   
   int limit;
   
   if(prev_calculated > 0) limit = rates_total - prev_calculated + 1;
   else {
      for(int a=0; a<rates_total; a++){
         ind_buffer[a] = 0.0;
      }
      
      limit = rates_total - period;
   }
   
   for(int i=limit-1; i>=0; i--) {
      double stdDev = getIndicatorValue(stdDevHandle, 0, i);
      double sma = getIndicatorValue(smaHandle, 0, i);
      
      if(sma == 0){
         ind_buffer[i] = 0;
      }
      else {
         ind_buffer[i] = 2.0 * BandsDeviations * stdDev / sma;
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
   return (val >= -1000 && val <= 1000) ? val : 0;
}