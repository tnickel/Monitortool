//+------------------------------------------------------------------+
//|                                                         Fibo.mq5 |
//|                           Copyright © 2017, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright "Copyright © 2017, StrategyQuant s.r.o."
#property link      "http://www.strategyquant.com"
#property version   "1.00"
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
//|  4  - High-Low of last X days                                    |
//|  5  - Open-Close previous day                                    |
//|  6  - Open-Close previous week                                   |
//|  7  - Open-Close previous month                                  |
//|  8  - Open-Close of last X days                                  |
//|  9  - Highest-Lowest for last X bars back                        |
//|  10 - Open-Close for last X bars back                            |   
//|                                                                  |  
//|  Custom Fibo Level (to be used by SQ):                           |  
//|  Set FiboLevel to -9999999 and specify CustomFiboLevel           |         
//+------------------------------------------------------------------+

input int FiboRange = 1;         //Fibo range mode [1-10]
input int X;                     //Custom days/bars count
input double FiboLevel = 61.8;
input double CustomFiboLevel;
input datetime StartDate = 0;    //Start point for calculations

//---- buffers
double buffer[];

//---- variables
uint tfEndTime = 0;
int barsUsed = -1;
double prevTFOpen = 0, prevTFHigh = 0, prevTFLow = 0, prevTFClose = 0;
double fiboLevel = 0;
int fiboRangeUsed = 0;
datetime lastBarTime = 0;
bool startDateUsed = false;

//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int OnInit()
  {
  
   fiboLevel = FiboLevel == -9999999 ? CustomFiboLevel : FiboLevel;
   fiboRangeUsed = FiboRange >= 1 && FiboRange <= 7 ? FiboRange : 1;
//--- indicator buffers mapping
   
   ArraySetAsSeries(buffer, true);
   SetIndexBuffer(0, buffer);
   
//---
   string short_name = "Fibo(" + IntegerToString(fiboRangeUsed)+ ", " + DoubleToString(fiboLevel) + ")";
   IndicatorSetString(INDICATOR_SHORTNAME, short_name);
   
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
//---
   
   ArraySetAsSeries(time, true);
   ArraySetAsSeries(open, true);
   ArraySetAsSeries(high, true);
   ArraySetAsSeries(low, true);
   ArraySetAsSeries(close, true);
   
   int limit = rates_total - prev_calculated;
   
   for(int i=limit - 1; i>=0; i--){
      if(isNewTFStart(time[i])){
			double upperValue = 0, lowerValue = 0;
			
			switch(fiboRangeUsed){
				case 1:
				case 2:
				case 3:
				case 4:
				case 9:
					upperValue = prevTFHigh;
					lowerValue = prevTFLow;
					break;
				case 5:
				case 6:
				case 7:
				case 8:
				case 10:
					upperValue = MathMax(prevTFOpen, prevTFClose);
					lowerValue = MathMin(prevTFOpen, prevTFClose);
					break;
			}
			
			//Print("Time: " + TimeToString(time[i], TIME_DATE | TIME_MINUTES | TIME_SECONDS) + ", High: " + upperValue + ", Low:" + lowerValue);
			double percentStep = (upperValue - lowerValue) / 100;
			double fiboPct = FiboLevel == -9999999 ? CustomFiboLevel : FiboLevel;
			double delta = fiboPct * percentStep;

			bool bullish = prevTFClose > prevTFOpen;
			
			fiboLevel = bullish ? (upperValue - delta) : (lowerValue + delta);
		   
			prevTFOpen = open[i];
			prevTFHigh = high[i];
			prevTFLow = low[i];
			prevTFClose = close[i];
			
			barsUsed = i == 0 ? 0 : 1; //last bar of this call is the first one of the next call
		}
		else {
		   if(i != 0){
   			prevTFHigh = MathMax(prevTFHigh, high[i]);
   			prevTFLow = MathMin(prevTFLow, low[i]); 
   			prevTFClose = close[i];
   			
   			barsUsed++;   
   			
			}
		}

		buffer[i] = fiboLevel;
   }
  
//--- return value of prev_calculated for next call
   return(rates_total - 1);
  }
//+------------------------------------------------------------------+

bool isNewTFStart(datetime time){
   if(StartDate != 0 && !startDateUsed && StartDate <= time) {
      setEndTime(time);
      startDateUsed = true;
      return true;
   }

	switch(fiboRangeUsed){
		case 9:
		case 10:
			if(barsUsed == -1 || barsUsed == X){
				return true;
			}
			else return false;
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			if(tfEndTime == 0 || tfEndTime <= (uint) time){
				setEndTime(time);
				return true;
			}
			else return false;
		default: 
		   Alert("Invalid FiboRange used: " + IntegerToString(fiboRangeUsed));
		   return false;
	}
}

void setEndTime(datetime time){
   uint curDayStart = (uint) time;
   curDayStart = curDayStart - (curDayStart % (24 * 3600)); //remove hours, minutes and seconds
   
   MqlDateTime startDateTime;
   TimeToStruct(time, startDateTime);
		
	switch(fiboRangeUsed){
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
		case 4:
		case 8:
		   tfEndTime = curDayStart + (X * 24 * 3600);
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
