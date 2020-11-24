//+------------------------------------------------------------------+
//|                                                        SqADX.mq5 |
//|                           Copyright © 2017, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright   "Copyright © 2017, StrategyQuant s.r.o."
#property link        "http://www.strategyquant.com"
#property description "Average Directional Movement Index"
#include <MovingAverages.mqh>

#property indicator_separate_window
#property indicator_buffers 3
#property indicator_type1   DRAW_LINE
#property indicator_color1  LightSeaGreen
#property indicator_style1  STYLE_SOLID
#property indicator_width1  1
#property indicator_type2   DRAW_LINE
#property indicator_color2  YellowGreen
#property indicator_style2  STYLE_DOT
#property indicator_width2  1
#property indicator_type3   DRAW_LINE
#property indicator_color3  Wheat
#property indicator_style3  STYLE_DOT
#property indicator_width3  1
#property indicator_label1  "ADX"
#property indicator_label2  "+DI"
#property indicator_label3  "-DI"
//--- input parameters
input int InpPeriodADX=14; // Period
//---- buffers
double    ExtADXBuffer[];
double    ExtPDIBuffer[];
double    ExtNDIBuffer[];
double    ExtSumDmPlusBuffer[];
double    ExtSumDmMinusBuffer[];
double    ExtSumTrBuffer[];
//--- global variables
int       ExtADXPeriod;
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
void OnInit()
  {
//--- check for input parameters
   if(InpPeriodADX<=0)
     {
      ExtADXPeriod=14;
      printf("Incorrect value for input variable Period_ADX=%d. Indicator will use value=%d for calculations.",InpPeriodADX,ExtADXPeriod);
     }
   else ExtADXPeriod=InpPeriodADX;
//---- indicator buffers
   IndicatorBuffers(6);

   SetIndexBuffer(0,ExtADXBuffer,INDICATOR_DATA);
   SetIndexBuffer(1,ExtPDIBuffer,INDICATOR_DATA);
   SetIndexBuffer(2,ExtNDIBuffer,INDICATOR_DATA);
   SetIndexBuffer(3,ExtSumDmPlusBuffer,INDICATOR_CALCULATIONS);
   SetIndexBuffer(4,ExtSumDmMinusBuffer,INDICATOR_CALCULATIONS);
   SetIndexBuffer(5,ExtSumTrBuffer,INDICATOR_CALCULATIONS);
   
   ArraySetAsSeries(ExtADXBuffer, false);
   ArraySetAsSeries(ExtPDIBuffer, false);
   ArraySetAsSeries(ExtNDIBuffer, false);
   ArraySetAsSeries(ExtSumDmPlusBuffer, false);
   ArraySetAsSeries(ExtSumDmMinusBuffer, false);
   ArraySetAsSeries(ExtSumTrBuffer, false);
   
//--- indicator digits
   IndicatorSetInteger(INDICATOR_DIGITS,2);
//--- set draw begin
   /*
   PlotIndexSetInteger(0,PLOT_DRAW_BEGIN,ExtADXPeriod<<1);
   PlotIndexSetInteger(1,PLOT_DRAW_BEGIN,ExtADXPeriod);
   PlotIndexSetInteger(2,PLOT_DRAW_BEGIN,ExtADXPeriod);
   */
//--- indicator short name
   string short_name="ADX("+string(ExtADXPeriod)+")";
   IndicatorSetString(INDICATOR_SHORTNAME,short_name);
//--- change 1-st index label
   PlotIndexSetString(0,PLOT_LABEL,short_name);
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
  
   ArraySetAsSeries(time, false);
   ArraySetAsSeries(open, false);
   ArraySetAsSeries(high, false);
   ArraySetAsSeries(low, false);
   ArraySetAsSeries(close, false);
   
//--- detect start position
   int start;
   if(prev_calculated>1) start=prev_calculated-1;
   else
     {
      start=1;
      
      ExtSumDmPlusBuffer[0] = 0.0;
      ExtSumDmMinusBuffer[0] = 0.0;
      ExtSumTrBuffer[0] = high[0] - low[0];
      ExtPDIBuffer[0] = 0.0;
      ExtNDIBuffer[0] = 0.0;
      ExtADXBuffer[0] = 0.0;
     }
//--- main cycle
   for(int i=start;i<rates_total && !IsStopped();i++){
      double trueRange = high[i] - low[i];
      
      double _High = high[i];
      double _Low = low[i];
      
      double prevHigh = high[i-1];
      double prevLow = low[i-1];
      double prevClose = close[i-1];
      
      double deltaHH = NormalizeDouble(_High - prevHigh, 8);
      double deltaLL = NormalizeDouble(prevLow - _Low, 8);
      double deltaHC = NormalizeDouble(_High - prevClose, 8);
      double deltaLC = NormalizeDouble(_Low - prevClose, 8);
      
      double tr = MathMax(MathAbs(deltaLC), MathMax(trueRange, MathAbs(deltaHC)));
		double dmPlus = deltaHH > deltaLL ? MathMax(deltaHH, 0) : 0;
		double dmMinus = deltaLL > deltaHH ? MathMax(deltaLL, 0) : 0;

		if (i < ExtADXPeriod){
			ExtSumTrBuffer[i] = NormalizeDouble(ExtSumTrBuffer[i-1] + tr, 8);
			ExtSumDmPlusBuffer[i] = ExtSumDmPlusBuffer[i-1] + dmPlus;
			ExtSumDmMinusBuffer[i] = ExtSumDmMinusBuffer[i-1] + dmMinus;
		}
		else {
			ExtSumTrBuffer[i] = NormalizeDouble(ExtSumTrBuffer[i-1] - ExtSumTrBuffer[i-1] / ExtADXPeriod + tr, 8);
			ExtSumDmPlusBuffer[i] = ExtSumDmPlusBuffer[i-1] - ExtSumDmPlusBuffer[i-1] / ExtADXPeriod + dmPlus;
			ExtSumDmMinusBuffer[i] = ExtSumDmMinusBuffer[i-1] - ExtSumDmMinusBuffer[i-1] / ExtADXPeriod + dmMinus;
		}

		ExtPDIBuffer[i] = 100 * (ExtSumTrBuffer[i] == 0 ? 0 : ExtSumDmPlusBuffer[i] / ExtSumTrBuffer[i]);
		ExtNDIBuffer[i] = 100 * (ExtSumTrBuffer[i] == 0 ? 0 : ExtSumDmMinusBuffer[i] / ExtSumTrBuffer[i]);
		
		double diff	= MathAbs(ExtPDIBuffer[i] - ExtNDIBuffer[i]);
		double sum = NormalizeDouble(ExtPDIBuffer[i] + ExtNDIBuffer[i], 8);

		ExtADXBuffer[i] = sum == 0 ? 50 : ((ExtADXPeriod - 1) * ExtADXBuffer[i-1] + 100 * diff / sum) / ExtADXPeriod;
      
   }
//---- OnCalculate done. Return new prev_calculated.
   return(rates_total);
  }
//+------------------------------------------------------------------+
