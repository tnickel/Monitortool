//+------------------------------------------------------------------+
//|                                                       Pivots.mq4 |
//|                           Copyright © 2019, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright "Copyright © 2019, StrategyQuant s.r.o."
#property link      "http://www.strategyquant.com"
#property version   "1.00"
#property indicator_chart_window
#property indicator_buffers 7
#property indicator_plots 7

#property indicator_label1  "PP"
#property indicator_type1  DRAW_LINE
#property indicator_color1 Green

#property indicator_label2  "R1"
#property indicator_type2  DRAW_LINE
#property indicator_color2 Blue

#property indicator_label3  "R2"
#property indicator_type3  DRAW_LINE
#property indicator_color3 Blue

#property indicator_label4  "R3"
#property indicator_type4  DRAW_LINE
#property indicator_color4 Blue

#property indicator_label5  "S1"
#property indicator_type5  DRAW_LINE
#property indicator_color5 Red

#property indicator_label6  "S2"
#property indicator_type6  DRAW_LINE
#property indicator_color6 Red

#property indicator_label7  "S3"
#property indicator_type7  DRAW_LINE
#property indicator_color7 Red

//---- input parameters
input int       StartHour=8;
input int       StartMinute=20;
input int       DaysToPlot=0;
input color     SupportLabelColor=DodgerBlue;
input color     ResistanceLabelColor=OrangeRed;
input color     PivotLabelColor=Green;
input int       fontsize=8;
input int       LabelShift = 0;

//---- buffers
double R3Buffer[];
double R2Buffer[];
double R1Buffer[];
double PBuffer[];
double S1Buffer[];
double S2Buffer[];
double S3Buffer[];


string Pivot="Pivot",Sup1="S 1", Res1="R 1";
string Sup2="S 2", Res2="R 2", Sup3="S 3", Res3="R 3";

datetime LabelShiftTime;
int PeriodMinutes;
int StartMinutesIntoDay, CloseMinutesIntoDay;

int PreviousClosingBar = 1;
datetime PreviousClosingTime = 0;
	
double PreviousHigh = 0;
double PreviousLow = 0;
double PreviousClose = 0;

double P = 0, S1 = 0, R1 = 0, S2 = 0, R2 = 0, S3 = 0, R3 = 0;
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int OnInit()
  {
//--- indicator buffers mapping

   SetIndexBuffer(0,PBuffer);
   SetIndexBuffer(1,R1Buffer);
   SetIndexBuffer(2,R2Buffer);
   SetIndexBuffer(3,R3Buffer);
   SetIndexBuffer(4,S1Buffer);
   SetIndexBuffer(5,S2Buffer);
   SetIndexBuffer(6,S3Buffer);
   
   ArraySetAsSeries(PBuffer, true);
   ArraySetAsSeries(S1Buffer, true);
   ArraySetAsSeries(R1Buffer, true);
   ArraySetAsSeries(S2Buffer, true);
   ArraySetAsSeries(R2Buffer, true);
   ArraySetAsSeries(S3Buffer, true);
   ArraySetAsSeries(R3Buffer, true);
   
   IndicatorSetString(INDICATOR_SHORTNAME,"Pivots");
   
   PeriodMinutes = PeriodSeconds(Period()) / 60;
   StartMinutesIntoDay = correctStartMinutes((StartHour * 60) + StartMinute); // 8' o'clock x 60 = 480
   CloseMinutesIntoDay = StartMinutesIntoDay - PeriodMinutes;
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
  
//---- indicator calculation
   int min = 1440 / PeriodMinutes;
   int start = prev_calculated;
   
   if(start < 0) return -1;
   if(start > 0) start--;
   
   for(int a=0; a<rates_total - start; a++){
       R3Buffer[a]=0;
       R2Buffer[a]=0;
       R1Buffer[a]=0;
       PBuffer[a]=0;
       S1Buffer[a]=0;
       S2Buffer[a]=0;
       S3Buffer[a]=0;
   }
   
   if(rates_total < min) return(0);

   int limit=rates_total - ((start + 1) > min ? start : min);
   
   ArraySetAsSeries(time, true);
   ArraySetAsSeries(open, true);
   ArraySetAsSeries(high, true);
   ArraySetAsSeries(low, true);
   ArraySetAsSeries(close, true);
   
   // ****************************************************
   //    Check That close time isn't now a negative number.
   //    Correct if it is by adding a full day's worth 
   //    of minutes.
   // ****************************************************
   
   if (CloseMinutesIntoDay < 0){
      CloseMinutesIntoDay = CloseMinutesIntoDay + 1440;
   }
      
   // ****************************************************
   //    Establish the nuber of bars in a day.
   // ****************************************************
   int BarsInDay = 1440 / PeriodMinutes;
    
   // ******************************************************************************************
   // ******************************************************************************************
   //                                        Main Loop                                      
   // ******************************************************************************************
   // ******************************************************************************************
   
   for(int i=limit; i>0; i--){ 
      // ***************************************************************************
      //       Only do all this if we are within the plotting range we want.
      //       i.e. DaysToPlot 
      //       If DaysToPlot is "0" (zero) we plot ALL available data. This can be 
      //    VERY slow with a large history and at small time frames. Expect to
      //    wait if plotting a year or more on a say a five minute chart. On my PC 
      //    two years of data at Five Minutes takes around 30 seconds per indicator. 
      //    i.e. 30 seconds for main pivot levels indicator then another 30 seconds 
      //    to plot the seperate mid-levels indicator. 
      // ***************************************************************************
      if ((i < ((DaysToPlot + 1) * BarsInDay)) || DaysToPlot == 0){   // Used to limit the number of days
                                                                    // that are mapped out. Less waiting ;)
         // *****************************************************
         //    Find previous day's opening and closing bars.
         // *****************************************************

         PreviousClosingBar = FindLastTimeMatchFast(CloseMinutesIntoDay, i + 1, time, PreviousClosingBar, true);
         
         //Print(TimeToString(time[i], TIME_DATE | TIME_MINUTES), " - ", PreviousClosingBar, ", ", TimeToString(time[PreviousClosingBar], TIME_DATE | TIME_MINUTES));
         
         if(PreviousClosingTime != time[PreviousClosingBar]) {
            PreviousClosingTime = time[PreviousClosingBar];
            
            int PreviousOpeningBar = FindLastTimeMatchFast(StartMinutesIntoDay, PreviousClosingBar + 1, time, 1000000, false);
         
            PreviousHigh = high[PreviousClosingBar];
            PreviousLow = low [PreviousClosingBar];
            PreviousClose = close[PreviousClosingBar];
         
            // *****************************************************
            //    Find previous day's high and low.
            // *****************************************************
            
            for (int SearchHighLow = PreviousClosingBar; SearchHighLow < PreviousOpeningBar + 1; SearchHighLow++){
               if(SearchHighLow == ArraySize(time)) break;
               
               if (high[SearchHighLow] > PreviousHigh) PreviousHigh = high[SearchHighLow];
               if (low[SearchHighLow] < PreviousLow) PreviousLow = low[SearchHighLow];
            }
         }
         
         // ************************************************************************
         //    Calculate Pivot lines and map into indicator buffers.
         // ************************************************************************
         
         P =  (PreviousHigh + PreviousLow + PreviousClose) / 3;
         R1 = (2 * P) - PreviousLow;
         S1 = (2 * P) - PreviousHigh;
         R2 =  P + (PreviousHigh - PreviousLow);
         S2 =  P - (PreviousHigh - PreviousLow);
         R3 =  P + 2 * (PreviousHigh - PreviousLow);
         S3 =  P - 2 * (PreviousHigh - PreviousLow);
         
         LabelShiftTime = time[LabelShift];
         
         if (i == 0){
            ObjectCreate(ChartID(), "Pivot", OBJ_TEXT, 0, LabelShiftTime, 0);   
            ObjectSetString(ChartID(), "Pivot", OBJPROP_TEXT, "                           Pivot " +DoubleToString(P,4));//,fontsize,"tahoma ",PivotLabelColor);
            //SetIndexLabel(0, "Pivot Point");
            ObjectCreate(ChartID(),"Sup1", OBJ_TEXT, 0, LabelShiftTime, 0);
            ObjectSetString(ChartID(), "Sup1", OBJPROP_TEXT, "                    S1 " +DoubleToString(S1,4));//,fontsize,"tahoma ",SupportLabelColor);
            //SetIndexLabel(1, "S1");
            ObjectCreate(ChartID(),"Res1", OBJ_TEXT, 0, LabelShiftTime, 0);
            ObjectSetString(ChartID(), "Res1", OBJPROP_TEXT, "                    R1 " +DoubleToString(R1,4));//,fontsize,"tahoma ",ResistanceLabelColor);
            //SetIndexLabel(2, "R1");
            ObjectCreate(ChartID(),"Sup2", OBJ_TEXT, 0, LabelShiftTime, 0);
            ObjectSetString(ChartID(), "Sup2", OBJPROP_TEXT, "                    S2 " +DoubleToString(S2,4));//,fontsize,"tahoma ",SupportLabelColor);
            //SetIndexLabel(3, "S2");
            ObjectCreate(ChartID(),"Res2", OBJ_TEXT, 0, LabelShiftTime, 0);
            ObjectSetString(ChartID(), "Res2", OBJPROP_TEXT, "                    R2 " +DoubleToString(R2,4));//,fontsize,"tahoma ",ResistanceLabelColor);
            //SetIndexLabel(4, "R2");
            ObjectCreate(ChartID(),"Sup3", OBJ_TEXT, 0, LabelShiftTime, 0);
            ObjectSetString(ChartID(), "Sup3", OBJPROP_TEXT, "                    S3 " +DoubleToString(S3,4));//,fontsize,"tahoma ",SupportLabelColor);
            //SetIndexLabel(5, "S3");
            ObjectCreate(ChartID(),"Res3", OBJ_TEXT, 0, LabelShiftTime, 0);
            ObjectSetString(ChartID(), "Res3", OBJPROP_TEXT, "                    R3 " +DoubleToString(R3,4));//,fontsize,"tahoma ",ResistanceLabelColor);
           // SetIndexLabel(6, "R3");
            ObjectMove(ChartID(),"Res3", 0, LabelShiftTime,R3);
            ObjectMove(ChartID(),"Res2", 0, LabelShiftTime,R2);
            ObjectMove(ChartID(),"Res1", 0, LabelShiftTime,R1);
            ObjectMove(ChartID(),"Pivot", 0, LabelShiftTime,P);
            ObjectMove(ChartID(),"Sup1", 0, LabelShiftTime,S1);
            ObjectMove(ChartID(),"Sup2", 0, LabelShiftTime,S2);
            ObjectMove(ChartID(),"Sup3", 0, LabelShiftTime,S3);
         } 
      }  
    
      R3Buffer[i]=R3;
      R2Buffer[i]=R2;
      R1Buffer[i]=R1;
      PBuffer[i]=P;
      S1Buffer[i]=S1;
      S2Buffer[i]=S2;
      S3Buffer[i]=S3;
   }
//--- return value of prev_calculated for next call
   return(rates_total);
//+------------------------------------------------------------------+
}


// *****************************************************************************************
// *****************************************************************************************
// -----------------------------------------------------------------------------------------
//    The following routine will use "StartingBar"'s time and use it to find the 
//    general area that SHOULD contain the bar that matches "TimeToLookFor"
// -----------------------------------------------------------------------------------------
int FindLastTimeMatchFast(int TimeToLookFor, int StartingBar, const datetime &time[], int prevBarFound, bool isClosingBar){
   int HowManyBarsBack = MathMin(ArraySize(time) - 1, 1440 / PeriodMinutes * 3);
		
	if(checkBarIsWhatWeLookFor(TimeToLookFor, StartingBar, time, isClosingBar)) {
		return StartingBar;
	}
	else if(prevBarFound < HowManyBarsBack && checkBarIsWhatWeLookFor(TimeToLookFor, prevBarFound, time, isClosingBar)) {
		return prevBarFound;
	}
	else if(prevBarFound < HowManyBarsBack && checkBarIsWhatWeLookFor(TimeToLookFor, prevBarFound + 1, time, isClosingBar)) {
		return prevBarFound + 1;
	}
	else {
		for(int a=StartingBar + 1; a<HowManyBarsBack; a++) {
			if(checkBarIsWhatWeLookFor(TimeToLookFor, a, time, isClosingBar)) {
				return a;
			}
		}
		
		return HowManyBarsBack + 1;
	}
}

bool checkBarIsWhatWeLookFor(int TimeToLookFor, int bar, const datetime &time[], bool isClosingBar){
   if(bar >= ArraySize(time) - 1) return false;

   int PreviousBarsTime = (TimeHour(time[bar - 1]) * 60) + TimeMinute(time[bar - 1]);
   int CurrentBarsTime = (TimeHour(time[bar]) * 60) + TimeMinute(time[bar]);
	int NextBarsTime = (TimeHour(time[bar + 1]) * 60) + TimeMinute(time[bar + 1]);
      
   if(CurrentBarsTime == TimeToLookFor) return true;  
   
   int PreviousBarDay = TimeDayOfYear(time[bar - 1]);
   int CurrentBarDay = TimeDayOfYear(time[bar]);
   int NextBarDay = TimeDayOfYear(time[bar + 1]);
   
   if(NextBarDay != CurrentBarDay) {
   	NextBarsTime = NextBarsTime - 1440;
   }
   
   if(PreviousBarDay != CurrentBarDay) {
   	if(PreviousBarsTime > TimeToLookFor && CurrentBarsTime > TimeToLookFor) {		//Handling Pivots time outside trading session
        	return true;
      }
   	PreviousBarsTime = PreviousBarsTime + 1440;
   }
   
   if(PreviousBarsTime > TimeToLookFor && NextBarsTime < TimeToLookFor) {
   	return isClosingBar ? CurrentBarsTime < TimeToLookFor : true;
   }
   
   return false;
}  

int correctStartMinutes(int minutes){
   int temp = minutes;
   while(temp % PeriodMinutes != 0){
      temp++;
   }
   return temp >= 1440 ? temp-1440 : temp;
}  