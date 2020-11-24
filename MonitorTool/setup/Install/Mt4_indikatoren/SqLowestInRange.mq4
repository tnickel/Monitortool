//+------------------------------------------------------------------+
//|                                              SqLowestInRange.mq4 |
//|                                    Copyright 2019, StrategyQuant |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright   "2019, StrategyQuant"
#property link        "http://www.strategyquant.com"
#property description "SqLowestInRange"

#property indicator_buffers 1
#property indicator_label1  "Lowest in range"
#property indicator_type1  DRAW_LINE
#property indicator_color1 Yellow
#property indicator_chart_window

#define INF 0x6FFFFFFF
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
double lowestValue = 0;

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
   string short_name="LowestInRange(" + TimeFrom + "-" + TimeTo + ")";
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
      
         lastValue = lowestValue;
         lowestValue = INF;
         
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
            lowestValue = low[i];
         }
      }
      else if(time[i] >= nextStartTime){
         lowestValue = MathMin(lowestValue, low[i]);
      }
      else {
         lowestValue = INF;
      }
      
      //avoid outputting INF values if there was a gap in data
      if(lastValue < INF){
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