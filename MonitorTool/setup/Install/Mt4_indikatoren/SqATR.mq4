//+------------------------------------------------------------------+
//|                                                        SqATR.mq4 |
//|                           Copyright © 2017, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright   "Copyright © 2017, StrategyQuant s.r.o."
#property link        "http://www.strategyquant.com"
#property description "Average True Range"
//--- indicator settings
#property indicator_separate_window
#property indicator_buffers 1
#property indicator_type1   DRAW_LINE
#property indicator_color1  DodgerBlue
#property indicator_label1  "SqATR"
//--- input parameters
input int InpAtrPeriod=14;  // ATR period
//--- indicator buffers
double    ExtATRBuffer[];
double    ExtTRBuffer[];
//--- global variable
int       ExtPeriodATR;
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
void OnInit()
  {
//--- check for input value
   if(InpAtrPeriod<=0)
     {
      ExtPeriodATR=14;
      printf("Incorrect input parameter InpAtrPeriod = %d. Indicator will use value %d for calculations.",InpAtrPeriod,ExtPeriodATR);
     }
   else ExtPeriodATR=InpAtrPeriod;
   
   IndicatorBuffers(2);
   
//--- indicator buffers mapping
   SetIndexBuffer(0,ExtATRBuffer,INDICATOR_DATA);
   SetIndexBuffer(1,ExtTRBuffer,INDICATOR_CALCULATIONS);
   
   ArraySetAsSeries(ExtATRBuffer, false);
   ArraySetAsSeries(ExtTRBuffer, false);
   
//---
   IndicatorSetInteger(INDICATOR_DIGITS,_Digits);
//--- sets first bar from what index will be drawn
   //PlotIndexSetInteger(0,PLOT_DRAW_BEGIN,InpAtrPeriod);
//--- name for DataWindow and indicator subwindow label
   string short_name="ATR("+string(ExtPeriodATR)+")";
   IndicatorSetString(INDICATOR_SHORTNAME,short_name);
   PlotIndexSetString(0,PLOT_LABEL,short_name);
//--- initialization done
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
   if(rates_total<=ExtPeriodATR)
      return(0); // not enough bars for calculation
//--- preliminary calculations
   if(prev_calculated==0){
      ExtATRBuffer[0] = high[0] - low[0];
   	ExtTRBuffer[0] = high[0] - low[0];
      
      limit = 1;
   }
   else limit=prev_calculated-1;
//--- the main loop of calculations
   for(i=limit;i<rates_total && !IsStopped();i++){
      double trueRange = high[i] - low[i];
		double prevClose = close[i-1];
		trueRange = MathMax(MathAbs(low[i] - prevClose), MathMax(trueRange, MathAbs(high[i] - prevClose)));
		ExtATRBuffer[i] = ((MathMin(i + 1, ExtPeriodATR) - 1 ) * ExtATRBuffer[i-1] + trueRange) / MathMin(i + 1, ExtPeriodATR);
   }
//--- return value of prev_calculated for next call
   return(rates_total);
  }
//+------------------------------------------------------------------+


