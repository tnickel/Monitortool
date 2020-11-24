//+------------------------------------------------------------------+
//|                                             SqHighestInRange.mq4 |
//|                                    Copyright 2019, StrategyQuant |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright   "2019, StrategyQuant"
#property link        "http://www.strategyquant.com"
#property description "SqHighestInRange"

#property indicator_buffers 1
#property indicator_label1  "Highest in range"
#property indicator_type1  DRAW_LINE
#property indicator_color1 Cyan
#property indicator_chart_window

#define DAY_SECONDS 24 * 60 * 60

//--- input parameters
extern string TimeFrom="00:00"; 
extern string TimeTo="00:00";
//---- buffers
double ExtBuffer[];
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+

datetime nextStartTime, nextEndTime;
double lastValue = 0;
double lastUsableValue = 0;
double highestValue = 0;

int OnInit()
  {
//--- check for input parameters
   if(StringFind(TimeFrom, ":") < 0){
      printf("Incorrect value for input variable TimeFrom. Time must be in format HH:MM", TimeFrom);
      return(INIT_FAILED);
   }
   
   if(StringFind(TimeTo, ":") < 0){
      printf("Incorrect value for input variable TimeTo. Time must be in format HH:MM", TimeTo);
      return(INIT_FAILED);
   }
   
//---- indicator buffers
   SetIndexBuffer(0,ExtBuffer);
   ArraySetAsSeries(ExtBuffer, false);
//--- indicator short name
   string short_name="HighestInRange(" + TimeFrom + "-" + TimeTo + ")";
   IndicatorSetString(INDICATOR_SHORTNAME,short_name);
//---- end of initialization function
   return(INIT_SUCCEEDED);
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
  ArraySetAsSeries(tick_volume, false);
  ArraySetAsSeries(volume, false);
  ArraySetAsSeries(spread, false);
  
//--- detect start position
   int startIndex;
   
   if(prev_calculated > 1) startIndex = prev_calculated - 1;
   else {
      nextStartTime = StringToTime(StringFormat("%04d.%02d.%02d %s", TimeYear(time[0]), TimeMonth(time[0]), TimeDay(time[0]), TimeFrom));
      nextEndTime = StringToTime(StringFormat("%04d.%02d.%02d %s", TimeYear(time[0]), TimeMonth(time[0]), TimeDay(time[0]), TimeTo));
      
      if(nextEndTime < nextStartTime){
         nextEndTime += DAY_SECONDS;
      }
      
      startIndex=1;
      ExtBuffer[0]=0.0;
   }
   
//--- main cycle
   for(int i=startIndex; i<rates_total && !IsStopped(); i++){
      if(time[i] >= nextEndTime){
         //time range is over, set new indicator value and recalculate start/end times
         nextStartTime = StringToTime(StringFormat("%04d.%02d.%02d %s", TimeYear(time[i]), TimeMonth(time[i]), TimeDay(time[i]), TimeFrom));
         nextEndTime = StringToTime(StringFormat("%04d.%02d.%02d %s", TimeYear(time[i]), TimeMonth(time[i]), TimeDay(time[i]), TimeTo));
      
         lastValue = highestValue;
         highestValue = 0;
         
         if(nextEndTime <= time[i]){      
            if(nextEndTime < nextStartTime){
               nextEndTime += DAY_SECONDS;
            }
            else {
               nextStartTime += DAY_SECONDS;
               nextEndTime += DAY_SECONDS;
            }
         }     
               
         if(nextStartTime <= time[i]){
            highestValue = high[i];
         }
      }
      else if(time[i] >= nextStartTime){
         highestValue = MathMax(highestValue, high[i]);
      }
      else {
         highestValue = 0;
      }
      
      //avoid outputting zero values if there was a gap in data
      if(lastValue > 0){
         lastUsableValue = lastValue;
         ExtBuffer[i] = lastValue;
      }
      else {
         ExtBuffer[i] = lastUsableValue;
      }
   }
   
//---- OnCalculate done. Return new prev_calculated.
   return(rates_total);
  }
//+------------------------------------------------------------------+