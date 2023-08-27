//+------------------------------------------------------------------+
//|                                                        SqCCI.mq5 |
//|                           Copyright © 2017, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright   "Copyright © 2017, StrategyQuant s.r.o."
#property link        "http://www.strategyquant.com"
#property description "Commodity Channel Index"
#include <MovingAverages.mqh>
//---
#property indicator_separate_window
#property indicator_buffers       4
#property indicator_plots         1
#property indicator_type1         DRAW_LINE
#property indicator_color1        LightSeaGreen
#property indicator_level1       -100.0
#property indicator_level2        100.0
//--- input parametrs
input int  InpCCIPeriod=14; // Period
input int  InpPrice=PRICE_TYPICAL; // Applied price
//--- global variable
int        ExtCCIPeriod, ExtCCIPrice;
//---- indicator buffer
double     ExtSPBuffer[];
double     ExtDBuffer[];
double     ExtMBuffer[];
double     ExtCCIBuffer[];
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
void OnInit()
  {
//--- check for input value of period
   if(InpCCIPeriod<=0)
     {
      ExtCCIPeriod=14;
      printf("Incorrect value for input variable InpCCIPeriod=%d. Indicator will use value=%d for calculations.",InpCCIPeriod,ExtCCIPeriod);
     }
   else ExtCCIPeriod=InpCCIPeriod;
   
   switch(InpPrice){
      case PRICE_OPEN:
      case PRICE_HIGH:
      case PRICE_LOW:
      case PRICE_CLOSE:
      case PRICE_MEDIAN:
      case PRICE_TYPICAL:
      case PRICE_WEIGHTED:
         ExtCCIPrice = InpPrice;
         break;
      default:
         printf("Incorrect value for input variable InpPrice=%d. Indicator will use value PRICE_HIGH for calculations.",InpPrice);
         ExtCCIPrice = PRICE_TYPICAL;
   }
   
//--- define buffers
   SetIndexBuffer(0,ExtCCIBuffer);
   SetIndexBuffer(1,ExtDBuffer,INDICATOR_CALCULATIONS);
   SetIndexBuffer(2,ExtMBuffer,INDICATOR_CALCULATIONS);
   SetIndexBuffer(3,ExtSPBuffer,INDICATOR_CALCULATIONS);
//--- indicator name
   IndicatorSetString(INDICATOR_SHORTNAME,"CCI("+string(ExtCCIPeriod)+")");
//--- indexes draw begin settings
   PlotIndexSetInteger(0,PLOT_DRAW_BEGIN,ExtCCIPeriod-1);
//--- number of digits of indicator value
   IndicatorSetInteger(INDICATOR_DIGITS,2);
//---- OnInit done
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
//--- variables
   int    i,j;
   double dTmp,dMul=0.015/ExtCCIPeriod;
//--- start calculation
   int StartCalcPosition=ExtCCIPeriod-1;
//--- check for bars count
   if(rates_total<StartCalcPosition)
      return(0);
//--- correct draw begin
   if(prev_calculated>0) PlotIndexSetInteger(0,PLOT_DRAW_BEGIN,StartCalcPosition+(ExtCCIPeriod-1));
//--- calculate position
   int pos=prev_calculated-1;
   if(pos<StartCalcPosition)
      pos=StartCalcPosition;
//--- main cycle
   for(i=pos;i<rates_total && !IsStopped();i++)
     {
     
      //--- SMA on price buffer
      double sma=0.0;
      //--- check position
      if(i>=ExtCCIPeriod-1 && ExtCCIPeriod>0){
         //--- calculate value
         for(int a=0;a<ExtCCIPeriod;a++) sma+=getValue(open,high,low,close,ExtCCIPrice,i-a);
         sma/=ExtCCIPeriod;
      }
   
      ExtSPBuffer[i]=sma;
      //--- calculate D
      dTmp=0.0;
      for(j=0;j<ExtCCIPeriod;j++) dTmp+=MathAbs(getValue(open,high,low,close,ExtCCIPrice,i-j)-ExtSPBuffer[i]);
      ExtDBuffer[i]=dTmp*dMul;
      //--- calculate M
      ExtMBuffer[i]=getValue(open,high,low,close,ExtCCIPrice,i)-ExtSPBuffer[i];
      //--- calculate CCI
      if(ExtDBuffer[i] < 0.0000000001) ExtCCIBuffer[i]=0.0;
      else                             ExtCCIBuffer[i]=ExtMBuffer[i]/ExtDBuffer[i];
      //---
     }
//---- OnCalculate done. Return new prev_calculated.
   return(rates_total);
  }
//+------------------------------------------------------------------+

double getValue(const double &open[],
                const double &high[],
                const double &low[],
                const double &close[],
                int priceMode,
                int index)
{
   switch(priceMode){
      case PRICE_OPEN: return open[index];
      case PRICE_HIGH: return high[index];
      case PRICE_LOW: return low[index];
      case PRICE_CLOSE: return close[index];
      case PRICE_MEDIAN: return (high[index] + low[index]) / 2;
      case PRICE_TYPICAL: return (high[index] + low[index] + close[index]) / 3;
      case PRICE_WEIGHTED: return (high[index] + low[index] + close[index] + close[index]) / 4;
      default: return 0;
   }             
}