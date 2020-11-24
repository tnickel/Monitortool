//+------------------------------------------------------------------+
//|                                             donchian-channel.mq4 |
//|      Copyright © 2011 Forex-indikatoren.com. All rights reserved |
//|                                 http://www.forex-indikatoren.com |
//+------------------------------------------------------------------+
#property copyright "Copyright © 2011 Forex-indikatoren.com."
#property link      "http://www.forex-indikatoren.com"
//---- indicator settings
#property  indicator_chart_window
#property  indicator_buffers 2
#property  indicator_color1  DodgerBlue
#property  indicator_color2  Tomato
#property  indicator_width1  1
#property  indicator_width2  1

//---- indicator parameters
extern int periods=20;

//---- indicator buffers
double     upper[];
double     lower[];
string Copyright="\xA9 WWW.FOREX-INDIKATOREN.COM";  
string MPrefix="FI";
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int init()
  {
//---- drawing settings
   SetIndexStyle(0,DRAW_LINE);
   SetIndexStyle(1,DRAW_LINE);
   
//---- indicator buffers mapping
   SetIndexBuffer(0,upper);
   SetIndexBuffer(1,lower);
   
//---- name for DataWindow and indicator subwindow label
   IndicatorShortName("Donchian Channel("+periods+")");
   SetIndexLabel(0,"Upper");
   SetIndexLabel(1,"Lower");
   
   DL("001", Copyright, 5, 20,Gold,"Arial",10,0); 
   return(0);
  }
//+------------------------------------------------------------------+
//| Custor indicator deinitialization function                       |
//+------------------------------------------------------------------+
int deinit()
  {
//----
   ClearObjects(); 
   return(0);
  }
//+------------------------------------------------------------------+
//| Custom indicator iteration function                              |
//+------------------------------------------------------------------+
int start()
  {
   int limit;
   int counted_bars=IndicatorCounted();
//---- last counted bar will be recounted
   if(counted_bars>0) counted_bars--;
   limit=Bars-counted_bars;

//---- calculate values
   for(int i=0; i<limit; i++) {
      upper[i]=iHigh(NULL,Period(),iHighest(NULL,Period(),MODE_HIGH,periods,i));
      lower[i]=iLow(NULL,Period(),iLowest(NULL,Period(),MODE_LOW,periods,i));
   }
   
   return(0);
  }
//+------------------------------------------------------------------+
//| DL function                                                      |
//+------------------------------------------------------------------+
 void DL(string label, string text, int x, int y, color clr, string FontName = "Arial",int FontSize = 12, int typeCorner = 1)
 
{
   string labelIndicator = MPrefix + label;   
   if (ObjectFind(labelIndicator) == -1)
   {
      ObjectCreate(labelIndicator, OBJ_LABEL, 0, 0, 0);
  }
   
   ObjectSet(labelIndicator, OBJPROP_CORNER, typeCorner);
   ObjectSet(labelIndicator, OBJPROP_XDISTANCE, x);
   ObjectSet(labelIndicator, OBJPROP_YDISTANCE, y);
   ObjectSetText(labelIndicator, text, FontSize, FontName, clr);
  
}  

//+------------------------------------------------------------------+
//| ClearObjects function                                            |
//+------------------------------------------------------------------+
void ClearObjects() 
{ 
  for(int i=0;i<ObjectsTotal();i++) 
  if(StringFind(ObjectName(i),MPrefix)==0) { ObjectDelete(ObjectName(i)); i--; } 
}
//+------------------------------------------------------------------+  