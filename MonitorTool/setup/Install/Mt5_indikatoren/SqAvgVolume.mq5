//+------------------------------------------------------------------+
//|                                                    AvgVolume.mq5 |
//|                           Copyright © 2017, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright "Copyright © 2017, StrategyQuant s.r.o."
#property link      "http://www.strategyquant.com"
#property version   "1.00"
#property indicator_separate_window
#property indicator_buffers 2
#property indicator_plots   2           
#property indicator_type1   DRAW_LINE
#property indicator_type2   DRAW_HISTOGRAM
#property indicator_color1 Red
#property indicator_color2 White

#property indicator_width2 3

input int MAPeriod = 14;

double vol[];
double avgVol[];
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
void OnInit()
  {
   SetIndexBuffer(0,avgVol);
   PlotIndexSetString(0,PLOT_LABEL,"Average Volume("+IntegerToString(MAPeriod)+")");
   
   SetIndexBuffer(1,vol);
   PlotIndexSetString(1,PLOT_LABEL,"");

   IndicatorSetString(INDICATOR_SHORTNAME,"AvgVolume");
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
//---
   
   if(rates_total < MAPeriod) {
      int start = prev_calculated > 0 ? prev_calculated - 1 : 0;
      
      for(int i=start; i<rates_total; i++){
         vol[i] = (double) (volume[i] ? volume[i] : tick_volume[i]);
         avgVol[i] = 0;   
      }
      return(rates_total);
   }
   
   double tempv;
   int limit = prev_calculated < MAPeriod ? MAPeriod - 1 : prev_calculated - 1;
   
   for(int i=limit; i<rates_total; i++) {
      vol[i] = (double) (volume[i] ? volume[i] : tick_volume[i]);
      
      tempv = 0; 
      for (int n=i-MAPeriod+1;n<=i;n++) {
         tempv += vol[n]; 
      } 

      avgVol[i] = tempv / MAPeriod;
   }
   
//--- return value of prev_calculated for next call
   return(rates_total);
  }
//+------------------------------------------------------------------+
