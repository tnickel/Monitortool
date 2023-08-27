/*------------------------------------------------------------------------------------
   Name: SqSuperTrend.mq4
   Copyright ©2011, Xaphod, http://wwww.xaphod.com
   modified by Clonex@StrategyQuantX 20.2.2021
   this version is synchronized in SQX/TS/MT4/MT
   Description: SuperTrend Indicator
	          
   Change log: 
       2011-11-25. Xaphod, v1.00 
       2020-02-20 Clonex
         
-------------------------------------------------------------------------------------*/
// Indicator properties
#property copyright "Copyright © 2010, Xaphod"
#property link      "http://wwww.xaphod.com"
#property strict

#property indicator_chart_window

#property indicator_buffers 3
//////////////////////////////////////////
#property indicator_type1 DRAW_LINE
#property indicator_color1 DarkOrange
#property indicator_width1 1
#property indicator_style1 STYLE_DOT
 
#property indicator_type2 DRAW_LINE
#property indicator_color2 Red
#property indicator_width2 2
#property indicator_style2 STYLE_SOLID
 
#property indicator_type3 DRAW_LINE
#property indicator_color3 Lime
#property indicator_width3 2
#property indicator_style3 STYLE_SOLID
///////////////////////////////////////////
#property indicator_maximum 1
#property indicator_minimum 0

// Constant definitions
#define INDICATOR_NAME "SqSuperTrend"
#define INDICATOR_VERSION "v1.1, Clonex"

// Indicator parameters
//extern string VersionInfo=INDICATOR_VERSION;
//extern string SuperTrendInfo="——————————————————————————————";
input int    Mode=1;      // SuperTrend Mode
input int    ATRPeriod=24;      // SuperTrend ATR Period
input double ATRMultiplier=3.0; // SuperTrend Multiplier

// Global module varables
double gadUpBuf[];
double gadDnBuf[];
double gadSuperTrend[];


//-----------------------------------------------------------------------------
// function: init()
// Description: Custom indicator initialization function
//-----------------------------------------------------------------------------
int init() 
{
   
   SetIndexBuffer(0, gadSuperTrend);
   SetIndexLabel(0, "SuperTrend");
    
   SetIndexBuffer(1, gadDnBuf);
   SetIndexLabel(1, "SuperTrend Down");
    
   SetIndexBuffer(2, gadUpBuf);
   SetIndexLabel(2, "SuperTrend Up");
   IndicatorShortName(INDICATOR_NAME+"["+IntegerToString(ATRPeriod)+";"+DoubleToStr(ATRMultiplier,1)+"]");
  return(0);
}


//-----------------------------------------------------------------------------
// function: deinit()
// Description: Custom indicator deinitialization function
//-----------------------------------------------------------------------------
void OnDeinit(const int  reason)
{
 
}

///-----------------------------------------------------------------------------
// function: start()
// Description: Custom indicator iteration function
//-----------------------------------------------------------------------------
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
  int iNewBars, iCountedBars, i;
  double dAtr,dUpperLevel, dLowerLevel;  
  
  // Get unprocessed ticks
  iCountedBars=IndicatorCounted();
  if(iCountedBars < 0) return (-1); 
  if(iCountedBars>0) iCountedBars--;
  iNewBars=Bars-iCountedBars;
  if(iCountedBars==0)  iNewBars-=1+1;
  
  for(i=iNewBars; i>=0; i--) {
  
          // Calc SuperTrend
          dAtr = iATR(NULL, 0, ATRPeriod, i);
          dUpperLevel=(High[i]+Low[i])/2+ATRMultiplier*dAtr;
          dLowerLevel=(High[i]+Low[i])/2-ATRMultiplier*dAtr;
          
          // Set supertrend levels
          if (Close[i]>gadSuperTrend[i+1] && Close[i+1]<=gadSuperTrend[i+1]) {
            gadSuperTrend[i]=dLowerLevel;
          }
          else if (Close[i]<gadSuperTrend[i+1] && Close[i+1]>=gadSuperTrend[i+1]) {
            gadSuperTrend[i]=dUpperLevel;
          }
          else if (gadSuperTrend[i+1]<dLowerLevel)
              gadSuperTrend[i]=dLowerLevel;
          else if (gadSuperTrend[i+1]>dUpperLevel)
              gadSuperTrend[i]=dUpperLevel;
          else
            gadSuperTrend[i]=gadSuperTrend[i+1];
          
          // Draw SuperTrend lines
          gadUpBuf[i]=EMPTY_VALUE;
          gadDnBuf[i]=EMPTY_VALUE;
          if (Close[i]>gadSuperTrend[i] || (Close[i]==gadSuperTrend[i] && Close[i+1]>gadSuperTrend[i+1])) 
            gadUpBuf[i]=gadSuperTrend[i];
          else if (Close[i]<gadSuperTrend[i] || (Close[i]==gadSuperTrend[i] && Close[i+1]<gadSuperTrend[i+1])) 
            gadDnBuf[i]=gadSuperTrend[i];  
        }
        
   return(rates_total);
}
//+------------------------------------------------------------------+



