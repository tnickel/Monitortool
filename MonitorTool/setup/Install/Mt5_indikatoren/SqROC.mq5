//+------------------------------------------------------------------+
//|                                                        SqROC.mq4 |
//|                           Copyright © 2022, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright   "Copyright © 2022, StrategyQuant s.r.o."
#property link        "http://www.strategyquant.com"
#property description "Price Rate Of Change"
//--- indicator settings
#property indicator_separate_window
#property indicator_buffers 1
#property indicator_plots 1
#property indicator_type1   DRAW_LINE
#property indicator_color1  DodgerBlue
#property indicator_label1  "SqROC"
//--- input parameters
input int InpRocPeriod=5;  // ROC period
//--- indicator buffers
double    ExtROCBuffer[];
//--- global variable
int       ExtPeriodROC;
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int OnInit()
  {
//--- check for input value
   if(InpRocPeriod<=0)
     {
      ExtPeriodROC=5;
      printf("Incorrect input parameter InpRocPeriod = %d. Indicator will use value %d for calculations.",InpRocPeriod,ExtPeriodROC);
     }
   else ExtPeriodROC=InpRocPeriod;
   
//--- indicator buffers mapping
   
   ArraySetAsSeries(ExtROCBuffer, false);
   SetIndexBuffer(0,ExtROCBuffer,INDICATOR_DATA);
   
//---
   IndicatorSetInteger(INDICATOR_DIGITS,_Digits);
//--- sets first bar from what index will be drawn
   //PlotIndexSetInteger(0,PLOT_DRAW_BEGIN,InpRocPeriod);
//--- name for DataWindow and indicator subwindow label
   string short_name="ROC("+string(ExtPeriodROC)+")";
   IndicatorSetString(INDICATOR_SHORTNAME,short_name);
   PlotIndexSetString(0,PLOT_LABEL,short_name);
//--- initialization done
   return(INIT_SUCCEEDED);
  }
//+------------------------------------------------------------------+
//| Average True Range                                               |
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
   
   ArraySetAsSeries(time, false);
   ArraySetAsSeries(open, false);
   ArraySetAsSeries(high, false);
   ArraySetAsSeries(low, false);
   ArraySetAsSeries(close, false);
   
   int i,limit;
//--- check for bars count
   if(rates_total<=ExtPeriodROC)
      return(0); // not enough bars for calculation
//--- preliminary calculations
   if(prev_calculated==0){
      ExtROCBuffer[0] = 0;
      
      limit = 1;
   }
   else limit=prev_calculated-1;
//--- the main loop of calculations
   for(i=limit;i<rates_total && !IsStopped();i++){
      double prevClose = i >= ExtPeriodROC ? close[i-ExtPeriodROC] : 0;
      if(prevClose == 0){    
		   ExtROCBuffer[i] = 0;
		}
		else {
         double roc = (close[i] - prevClose) / prevClose * 100;
		   ExtROCBuffer[i] = roc;
		}
   }
//--- return value of prev_calculated for next call
   return(rates_total);
  }
//+------------------------------------------------------------------+


