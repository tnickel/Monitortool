//+------------------------------------------------------------------+
//|                                                   Stochastic.mq4 |
//|                           Copyright © 2017, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright   "Copyright © 2017, StrategyQuant s.r.o."
#property link        "http://www.strategyquant.com"
#include <MovingAverages.mqh>
//--- indicator settings
#property indicator_separate_window
#property indicator_buffers 2
#property indicator_type1   DRAW_LINE
#property indicator_type2   DRAW_LINE
#property indicator_color1  LightSeaGreen
#property indicator_color2  Red
#property indicator_style2  STYLE_DOT
//--- input parameters
input int InpKPeriod=5;  // K period
input int InpDPeriod=3;  // D period
input int InpSlowing=3;  // Slowing
input ENUM_MA_METHOD       InpAppliedMA=MODE_SMA;                       // Applied MA method for signal line
input ENUM_STO_PRICE       InpAppliedPrice=STO_LOWHIGH;                 // Applied price

ENUM_MA_METHOD AppliedMA;
ENUM_STO_PRICE AppliedPrice;

//--- indicator buffers
double ExtMainBuffer[];
double ExtSignalBuffer[];

double _k[];           

//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int OnInit()
  {
  
   IndicatorBuffers(3);
  
//--- indicator buffers mapping

   SetIndexBuffer(0,ExtMainBuffer, INDICATOR_DATA);
   SetIndexBuffer(1,ExtSignalBuffer, INDICATOR_DATA);
   SetIndexBuffer(2,_k, INDICATOR_CALCULATIONS);
   
   ArraySetAsSeries(ExtMainBuffer, false);
   ArraySetAsSeries(ExtSignalBuffer, false);
   ArraySetAsSeries(_k, false);
   
//--- set accuracy
   IndicatorSetInteger(INDICATOR_DIGITS,2);
//--- set levels
   IndicatorSetInteger(INDICATOR_LEVELS,2);
   IndicatorSetDouble(INDICATOR_LEVELVALUE,0,20);
   IndicatorSetDouble(INDICATOR_LEVELVALUE,1,80);
//--- set maximum and minimum for subwindow 
   IndicatorSetDouble(INDICATOR_MINIMUM,0);
   IndicatorSetDouble(INDICATOR_MAXIMUM,100);
//--- name for DataWindow and indicator subwindow label
   IndicatorSetString(INDICATOR_SHORTNAME,"Stochastic("+(string)InpKPeriod+","+(string)InpDPeriod+","+(string)InpSlowing+")");
   PlotIndexSetString(0,PLOT_LABEL,"Main");
   PlotIndexSetString(1,PLOT_LABEL,"Signal");
   
   switch(InpAppliedMA){
      case MODE_SMA:
	   case MODE_EMA:
	   case MODE_SMMA:
	   case MODE_LWMA:
	      AppliedMA = InpAppliedMA;
	      break;
	   default: 
	      Print("Incorrect MA method selected - '" + InpAppliedMA + "'. Using SMA...");
	      AppliedMA = MODE_SMA;
	      break;
	}
	
	if(InpAppliedPrice == STO_LOWHIGH || InpAppliedPrice == STO_CLOSECLOSE){
	   AppliedPrice = InpAppliedPrice;
	}
	else {
	   Print("Incorrect applied price selected - '" + InpAppliedMA + "'. Using Low/High...");
	   AppliedPrice = STO_LOWHIGH;
	}
	
   return(INIT_SUCCEEDED);
//--- initialization done
  }
//+------------------------------------------------------------------+
//| Stochastic Oscillator                                            |
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
   
   double nom, den;
   int limit;
   
   if(prev_calculated == 0 || prev_calculated < 0 || prev_calculated > rates_total){
      for(int a=0; a<rates_total; a++){
   		_k[a] = 50;
   		ExtMainBuffer[a] = 50;
   		ExtSignalBuffer[a] = 50;
      }
      
      limit = 0;
   } 
   else {
      limit = prev_calculated - 1;
   }
      
   for(int i=limit; i<rates_total; i++){
      if(AppliedPrice == STO_LOWHIGH){
         nom = NormalizeDouble(close[i] - lowest(low, i), 8);
         den = NormalizeDouble(highest(high, i) - lowest(low, i), 8);
      }
      else {
         nom = NormalizeDouble(close[i] - lowest(close, i), 8);
         den = NormalizeDouble(highest(close, i) - lowest(close, i), 8);
      }
      
      if(den < 0.00000001 && den > -0.00000001){
			_k[i] = i == 0 ? 50 : _k[i-1];
		} else {
			_k[i] = MathMin(100, MathMax(0, 100 * nom / den));
		}
		
		switch(AppliedMA){
		   case MODE_SMA:
		      ExtMainBuffer[i] = SimpleMA(i, InpSlowing, _k);
		      ExtSignalBuffer[i] = SimpleMA(i, InpDPeriod, ExtMainBuffer);
		      break;
		   case MODE_EMA:
		      ExtMainBuffer[i] = ExponentialMA(i, InpSlowing, i == 0 ? 50 : ExtMainBuffer[i-1], _k);
		      ExtSignalBuffer[i] = ExponentialMA(i, InpDPeriod, i == 0 ? 50 : ExtSignalBuffer[i-1], ExtMainBuffer);
		      break;
		   case MODE_SMMA:
		      ExtMainBuffer[i] = SmoothedMA(i, InpSlowing, i == 0 ? 50 : ExtMainBuffer[i-1], _k);
		      ExtSignalBuffer[i] = SmoothedMA(i, InpDPeriod, i == 0 ? 50 : ExtSignalBuffer[i-1], ExtMainBuffer);
		      break;
		   case MODE_LWMA:
		      ExtMainBuffer[i] = LinearWeightedMA(i, InpSlowing, _k);
		      ExtSignalBuffer[i] = LinearWeightedMA(i, InpDPeriod, ExtMainBuffer);
		      break;
		   
		}
		
   }
//--- OnCalculate done. Return new prev_calculated.
   return(rates_total);
  }
//+------------------------------------------------------------------+

double highest(const double &price[], int index){ 
   if(index < InpKPeriod + 1){
      return 50;
   }
   else {
      double highestValue = -1;
      
      for(int a=index-InpKPeriod+1; a<=index; a++){
         if(price[a] - highestValue > 0.00000001){        //protection against imprecise double values
            highestValue = price[a];
         }
      }
      
      return highestValue;
   }
}

//+------------------------------------------------------------------+

double lowest(const double &price[], int index){ 
   if(index < InpKPeriod + 1){
      return 50;
   }
   else {
      double lowestValue = 100000000000000;
      
      for(int a=index-InpKPeriod+1; a<=index; a++){
         if(lowestValue - price[a] > 0.00000001){        //protection against imprecise double values
            lowestValue = price[a];
         }
      }
      
      return lowestValue;
   }
}