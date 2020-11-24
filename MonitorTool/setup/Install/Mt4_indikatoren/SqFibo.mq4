//+------------------------------------------------------------------+
//|                                                         Fibo.mq4 |
//|                           Copyright © 2017, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright "Copyright © 2017, StrategyQuant s.r.o."
#property link      "http://www.strategyquant.com"
#property version   "1.00"
#property strict
#property indicator_chart_window
#property indicator_buffers 1
#property indicator_plots 1

#property indicator_label1  "Fibo level"
#property indicator_type1  DRAW_LINE
#property indicator_color1 Red

//---- input parameters
//+------------------------------------------------------------------+
//|  FiboRange options:                                              |
//|  1  - High-Low previous day                                      |
//|  2  - High-low previous week                                     |
//|  3  - High-low previous month                                    |
//|  5  - Open-Close previous day                                    |
//|  6  - Open-Close previous week                                   |
//|  7  - Open-Close previous month                                  |
//+------------------------------------------------------------------+
extern int FiboRange = 1;         //Fibo range mode [1-10]
extern double FiboLevel = 61.8;
extern datetime StartDate = 0;    //Start point for calculations

//---- buffers
double buffer[];

//---- variables
uint tfEndTime = 0;
double prevTFOpen = 0, prevTFHigh = 0, prevTFLow = 0, prevTFClose = 0;
double fiboLevel = 0;
datetime lastBarTime = 0;
bool startDateUsed = false;

//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int OnInit()
  {
   fiboLevel = 0;
   
//--- indicator buffers mapping
   SetIndexBuffer(0, buffer);
   ArraySetAsSeries(buffer, true);
//---
   string short_name = "Fibo(" + IntegerToString(FiboRange)+ ", " + DoubleToString(fiboLevel) + ")";
   IndicatorSetString(INDICATOR_SHORTNAME, short_name);
//---
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
   
   ArraySetAsSeries(time, true);
   ArraySetAsSeries(open, true);
   ArraySetAsSeries(high, true);
   ArraySetAsSeries(low, true);
   ArraySetAsSeries(close, true);
   
   if(prev_calculated == 0){
      tfEndTime = 0;
      prevTFOpen = 0;
		prevTFHigh = 0;
		prevTFLow = 0;
		prevTFClose = 0;
   }
   
   int limit = rates_total - (prev_calculated > 0 ? prev_calculated : 1);
   
   for(int i=limit; i>0; i--){
      if(isNewTFStart(time[i])){
			double upperValue = 0, lowerValue = 0;
			
			switch(FiboRange){
				case 1:
				case 2:
				case 3:
					upperValue = prevTFHigh;
					lowerValue = prevTFLow;
					break;
				case 5:
				case 6:
				case 7:
					upperValue = MathMax(prevTFOpen, prevTFClose);
					lowerValue = MathMin(prevTFOpen, prevTFClose);
					break;
			}
			
			double percentStep = (upperValue - lowerValue) / 100;
			double delta = FiboLevel * percentStep;

			bool bullish = prevTFClose > prevTFOpen;
			
			fiboLevel = bullish ? (upperValue - delta) : (lowerValue + delta);
		   
			prevTFOpen = open[i];
			prevTFHigh = high[i];
			prevTFLow = low[i];
			prevTFClose = close[i];
		}
		else {
			prevTFHigh = MathMax(prevTFHigh, high[i]);
			prevTFLow = MathMin(prevTFLow, low[i]); 
			prevTFClose = close[i]; 
		}

		buffer[i] = fiboLevel;
   }
  
   if(tfEndTime == 0 || tfEndTime <= (uint) time[0]){       //new period start, but we dont have the current bar completed
      double tempUpperValue = 0;
      double tempLowerValue = 0;
      
      switch(FiboRange){
			case 1:
			case 2:
			case 3:
				tempUpperValue = prevTFHigh;
				tempLowerValue = prevTFLow;
				break;
			case 5:
			case 6:
			case 7:
				tempUpperValue = MathMax(prevTFOpen, prevTFClose);
				tempLowerValue = MathMin(prevTFOpen, prevTFClose);
				break;
		}
		
		double percentStep = (tempUpperValue - tempLowerValue) / 100;
		double delta = FiboLevel * percentStep;
      bool bullish = prevTFClose > prevTFOpen;

		buffer[0] = bullish ? (tempUpperValue - delta) : (tempLowerValue + delta);
	}  
	else {
	   buffer[0] = fiboLevel;
	}
    
   return(rates_total);
  }
//+------------------------------------------------------------------+

bool isNewTFStart(datetime time){
   if(StartDate != 0 && !startDateUsed && StartDate <= time) {
      setEndTime(time);
      startDateUsed = true;
      return true;
   }

	if(tfEndTime == 0 || tfEndTime <= (uint) time){
		setEndTime(time);
		return true;
	}
	else return false;
}

void setEndTime(datetime time){
   uint curDayStart = (uint) time;
   curDayStart = curDayStart - (curDayStart % (24 * 3600)); //remove hours, minutes and seconds
   
   MqlDateTime startDateTime;
   TimeToStruct(time, startDateTime);
		
	switch(FiboRange){
		case 1:
		case 5:
			tfEndTime = curDayStart + (24 * 3600);
			break;
		case 2:
		case 6:
			curDayStart -= getWeekDayIndex(startDateTime) * 24 * 3600;
			tfEndTime = curDayStart + (7 * 24 * 3600);
			break;
		case 3:
		case 7:
		   curDayStart = curDayStart - ((startDateTime.day - 1) * 24 * 3600);
			tfEndTime = curDayStart + (getMonthDaysCount(curDayStart) * 24 * 3600);
			break;
	}
}

int getMonthDaysCount(uint time){
   MqlDateTime timeStruct;
   TimeToStruct(time, timeStruct);
   int month = timeStruct.mon;
   int days = 0;
   
   while(timeStruct.day != 1 || timeStruct.mon == month){
      time += 24 * 3600;
      TimeToStruct(time, timeStruct);
      days++;
   }
   
   return days;
}

int getWeekDayIndex(MqlDateTime &dateTime){
   int dayOfWeek = dateTime.day_of_week - 1;
   return dayOfWeek < 0 ? 6 : dayOfWeek;
}