//+------------------------------------------------------------------+
//|                                                    SqFractal.mq5 |
//|                           Copyright © 2020, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright   "Copyright © 2020, StrategyQuant s.r.o."
#property link        "http://www.strategyquant.com"
#property description "Fractal"
//--- indicator settings
#property indicator_chart_window
#property indicator_buffers 2
#property indicator_plots   2
#property indicator_type1   DRAW_ARROW
#property indicator_type2   DRAW_ARROW
#property indicator_color1  Red
#property indicator_label1  "Fractal Up"
#property indicator_color2  Blue
#property indicator_label2  "Fractal Down"

//--- input parameters
input int Fractal=3;  // Fractal bars

//--- indicator buffers
double ExtUpFractalsBuffer[];
double ExtDownFractalsBuffer[];
//--- global variable
int FractalUsed = 3;
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
void OnInit()
  {
//--- check for input value
    if((Fractal - 1) / 2 <= 0){
      FractalUsed = 3;
      printf("Incorrect value for input variable Fractal=%d. Indicator will use value=%d for calculations.", Fractal, FractalUsed);
    }
    else {
      FractalUsed = Fractal;
    }
//--- indicator buffers mapping
   SetIndexBuffer(0,ExtUpFractalsBuffer,INDICATOR_DATA);
   SetIndexBuffer(1,ExtDownFractalsBuffer,INDICATOR_DATA);
//---
   IndicatorSetInteger(INDICATOR_DIGITS,_Digits);
//--- sets first bar from what index will be drawn
   PlotIndexSetInteger(0,PLOT_DRAW_BEGIN,FractalUsed);
//--- name for DataWindow and indicator subwindow label
   string short_name="Fractal";
   string short_name_up="Fractal Up";
   string short_name_down="Fractal Down";
   IndicatorSetString(INDICATOR_SHORTNAME,short_name);
   PlotIndexSetString(0,PLOT_LABEL,short_name_up);
   PlotIndexSetString(1,PLOT_LABEL,short_name_down);
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
   int i,limit;
   bool   bFoundHigh, bFoundLow;
   double dCurrentHigh, dCurrentLow;
   
//--- check for bars count
   if(rates_total<=FractalUsed)
      return(0); // not enough bars for calculation
//--- preliminary calculations
   if(prev_calculated==0){
      ExtUpFractalsBuffer[0] = 0;
      ExtDownFractalsBuffer[0] = 0;
      limit=FractalUsed;
   }
   else limit=prev_calculated-1;
//--- the main loop of calculations

   int eachSideLength = (FractalUsed - 1) / 2;

   for(i=limit;i<rates_total && !IsStopped();i++){
      int middleBar = i - eachSideLength - 1;
      
      dCurrentHigh = high[middleBar]; 
      dCurrentLow = low[middleBar];
      bFoundHigh = true;
      bFoundLow = true;
      
      for(int a=i-FractalUsed; a<i; a++){
         if(a == middleBar) continue;
         
      //----Fractals up
         if(high[a] >= dCurrentHigh) bFoundHigh = false;
      //----Fractals down
         if(low[a] <= dCurrentLow) bFoundLow = false;
      }
      
      ExtUpFractalsBuffer[i]=bFoundHigh ? dCurrentHigh : 0;
      ExtDownFractalsBuffer[i]=bFoundLow ? dCurrentLow : 0;
   }
//--- return value of prev_calculated for next call
   return(rates_total);
  }
//+------------------------------------------------------------------+


