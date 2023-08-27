//+------------------------------------------------------------------+
//|                                                  SqTrueRange.mq4 |
//|                           Copyright © 2021, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright   "Copyright © 2021, StrategyQuant s.r.o."
#property link        "http://www.strategyquant.com"
#property description "True Range"
//--- indicator settings
#property indicator_separate_window
#property indicator_buffers 1
#property indicator_plots   1
#property indicator_type1   DRAW_LINE
#property indicator_color1  DodgerBlue
#property indicator_label1  "TrueRange"
//--- input parameters
//--- indicator buffers
double    ExtTRBuffer[];
//--- global variable
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
void OnInit()
  {
//--- indicator buffers mapping

   IndicatorBuffers(1);
   
   SetIndexBuffer(0,ExtTRBuffer,INDICATOR_DATA);
   ArraySetAsSeries(ExtTRBuffer, false);
//---
   IndicatorSetInteger(INDICATOR_DIGITS,_Digits);
//--- sets first bar from what index will be drawn
//--- name for DataWindow and indicator subwindow label
   string short_name="TrueRange";
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
//--- preliminary calculations
   if(prev_calculated==0){
      ExtTRBuffer[0] = high[0] - low[0];
      limit=1;
   }
   else limit=prev_calculated-1;
//--- the main loop of calculations
   for(i=limit;i<rates_total && !IsStopped();i++){
      double close1 = close[i-1];
		double curHigh = high[i];
		double curLow = low[i];
		double TrueHigh, TrueLow;

		if(close1 > curHigh) {
			TrueHigh = close1;
		} 
		else {
			TrueHigh = curHigh;
		}

		if(close1 < curLow) {
			TrueLow = close1;
		} else {
			TrueLow = curLow;
		}

		ExtTRBuffer[i] = TrueHigh - TrueLow;
   }
//--- return value of prev_calculated for next call
   return(rates_total);
  }
//+------------------------------------------------------------------+


