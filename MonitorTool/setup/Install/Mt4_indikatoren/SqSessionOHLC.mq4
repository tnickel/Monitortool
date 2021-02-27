//+------------------------------------------------------------------+
//|                                                SqSessionOHLC.mq4 |
//|                           Copyright © 2021, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright   "Copyright © 2021, StrategyQuant s.r.o."
#property link        "http://www.strategyquant.com"
#property description "Session OHLC"

#property indicator_chart_window
#property indicator_buffers 1
#property indicator_plots   1
#property indicator_type1   DRAW_LINE
#property indicator_color1  DodgerBlue

#define DAY_SECONDS 24 * 60 * 60 

#define TYPE_OPEN 1
#define TYPE_HIGH 2
#define TYPE_LOW 3
#define TYPE_CLOSE 4

double values[];

double sessionOpen[];
double sessionHigh[];
double sessionLow[];
double sessionClose[];

extern int Type = TYPE_OPEN;   //Price type (Open=1, High=2, Low=3, Close=4)
extern int StartHours = 0;
extern int StartMinutes = 0;
extern int EndHours = 0;
extern int EndMinutes = 0;
extern int DaysAgo = 0;

int startHHMM = -1;
int endHHMM = -1;

datetime sessionStartTime = -1;
datetime sessionEndTime = -1;

bool waitingForSession = true;

//+------------------------------------------------------------------+
//+------------------------------------------------------------------+
//+------------------------------------------------------------------+

int OnInit(void){
   ArraySetAsSeries(values, true);
   SetIndexBuffer(0,values,INDICATOR_DATA);

   IndicatorSetInteger(INDICATOR_DIGITS,_Digits);
   string short_name="Session ";
   
   switch(Type){
      case TYPE_OPEN: 
         short_name += "Open (" + IntegerToString(StartHours) + ":" + IntegerToString(StartMinutes) + ")[" + IntegerToString(DaysAgo) + "]"; 
         startHHMM = StartHours * 100 + StartMinutes;
         endHHMM = StartHours * 100 + StartMinutes;
         break;
      case TYPE_HIGH: 
         short_name += "High (" + IntegerToString(StartHours) + ":" + IntegerToString(StartMinutes) + " - " + IntegerToString(EndHours) + ":" + IntegerToString(EndMinutes) + ")[" + IntegerToString(DaysAgo) + "]"; 
         startHHMM = StartHours * 100 + StartMinutes;
         endHHMM = EndHours * 100 + EndMinutes;
         break;
      case TYPE_LOW: 
         short_name += "Low (" + IntegerToString(StartHours) + ":" + IntegerToString(StartMinutes) + " - " + IntegerToString(EndHours) + ":" + IntegerToString(EndMinutes) + ")[" + IntegerToString(DaysAgo) + "]"; 
         startHHMM = StartHours * 100 + StartMinutes;
         endHHMM = EndHours * 100 + EndMinutes;
         break;
      case TYPE_CLOSE: 
         short_name += "Close (" + IntegerToString(EndHours) + ":" + IntegerToString(EndMinutes) + ")[" + IntegerToString(DaysAgo) + "]"; 
         startHHMM = EndHours * 100 + EndMinutes;
         endHHMM = EndHours * 100 + EndMinutes;
         break;
      default: 
         short_name += "N/A"; 
         startHHMM = 0;
         endHHMM = 0;
         break;
   }
   
   
   ArrayResize(sessionOpen, DaysAgo + 1);
   ArrayResize(sessionHigh, DaysAgo + 1);
   ArrayResize(sessionLow, DaysAgo + 1);
   ArrayResize(sessionClose, DaysAgo + 1);
   
   ArrayInitialize(sessionOpen, -1);
   ArrayInitialize(sessionHigh, -1);
   ArrayInitialize(sessionLow, -1);
   ArrayInitialize(sessionClose, -1);
   
   IndicatorSetString(INDICATOR_SHORTNAME,short_name);
   PlotIndexSetString(0,PLOT_LABEL,short_name);
   
   return(INIT_SUCCEEDED);
}

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
  
   ArraySetAsSeries(time, true);
   ArraySetAsSeries(open, true);
   ArraySetAsSeries(high, true);
   ArraySetAsSeries(low, true);
   ArraySetAsSeries(close, true);
   
   int limit = rates_total - prev_calculated;
   
   if(prev_calculated == 0){
      limit = limit - 1;
   }
   
   for(int i=limit; i>=0; i--){
      datetime currentTime = time[i];
      if(currentTime >= sessionEndTime){
         if(sessionClose[0] >= 0){
            sessionClose[0] = close[i+1];
            
            if(time[i+1] >= sessionStartTime && time[i+1] < sessionEndTime){
               sessionHigh[0] = MathMax(sessionHigh[0], high[i+1]);
               sessionLow[0] = MathMin(sessionLow[0], low[i+1]);
            }
         }
         
         calculateSessionTimes(currentTime);
			waitingForSession = true;
      }
      
      if(currentTime >= sessionStartTime) {
			if(waitingForSession) {
				waitingForSession = false;
            
            shiftBuffers();
            
				sessionOpen[0] = open[i];
				sessionHigh[0] = high[i];
				sessionLow[0] = low[i];
				sessionClose[0] = close[i];
			}
			else {
            sessionHigh[0] = MathMax(sessionHigh[0], high[i]);
            sessionLow[0] = MathMin(sessionLow[0], low[i]);
            sessionClose[0] = close[i];
			}
		}
      
      switch(Type){
         case TYPE_OPEN: values[i] = sessionOpen[DaysAgo]; break;
         case TYPE_HIGH: values[i] = sessionHigh[DaysAgo]; break;
         case TYPE_LOW: values[i] = sessionLow[DaysAgo]; break;
         case TYPE_CLOSE: values[i] = sessionClose[DaysAgo]; break;
         default: values[i] = 0; break;
      }
   }
   
   return(rates_total);
}
  
//+------------------------------------------------------------------+

void calculateSessionTimes(datetime currentTime){
   datetime dayStart = (datetime) (currentTime - MathMod(currentTime, DAY_SECONDS));
   
   datetime startTime = dayStart + (StartHours * 60 + StartMinutes) * 60;
   datetime endTime = dayStart + (EndHours * 60 + EndMinutes) * 60;
   
   if(startHHMM >= endHHMM){
      endTime += DAY_SECONDS;
   }
   
   if(currentTime < (endTime - DAY_SECONDS)){
      startTime -= DAY_SECONDS;
      endTime -= DAY_SECONDS;
   }
   else if(currentTime > endTime || endTime == sessionEndTime){
      startTime += DAY_SECONDS;
      endTime += DAY_SECONDS;
   }
   
   sessionStartTime = startTime;
   sessionEndTime = endTime;
   
}

//+------------------------------------------------------------------+

void shiftBuffers(){
   for(int i=DaysAgo; i>0; i--){
      sessionOpen[i] = sessionOpen[i-1];
      sessionHigh[i] = sessionHigh[i-1];
      sessionLow[i] = sessionLow[i-1];
      sessionClose[i] = sessionClose[i-1];
   }
}