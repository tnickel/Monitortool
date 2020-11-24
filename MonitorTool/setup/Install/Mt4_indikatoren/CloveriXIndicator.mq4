//+------------------------------------------------------------------+
//|                                          !!_####_THOMAS_INDI.mq4 |
//|                                    Copyright? 2013, SIRIUS (HK). |
//|                                                 START 05-09-2013 |
//+------------------------------------------------------------------+
#property copyright "Copyright? 2013, SIRIUS (HK)"

#property indicator_chart_window

#property indicator_buffers 4

#property indicator_color1 Red
#property indicator_color2 Magenta
#property indicator_color3 Yellow
#property indicator_color4 White

#property  indicator_width1  3
#property  indicator_width2  1
#property  indicator_width3  1
#property  indicator_width4  3
//---- input parameters
extern int     X1          = 2;
extern int     X2          = 10;
extern int     X3          = 16;
extern int     X4          = 4;
//---- buffers

double HIGH_SELL[];
double LOW_SELL[];
double HIGH_BUY[];
double LOW_BUY[];
int   i;
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int init()
  {
//---- indicators

   SetIndexBuffer(0,HIGH_SELL);
   SetIndexBuffer(1,LOW_SELL);   
   SetIndexBuffer(2,HIGH_BUY);   
   SetIndexBuffer(3,LOW_BUY);   
   
   SetIndexStyle(0,DRAW_LINE);
   SetIndexStyle(1,DRAW_LINE);
   SetIndexStyle(2,DRAW_LINE);
   SetIndexStyle(3,DRAW_LINE);
   
   SetIndexLabel(0, "HIGH_SELL");
   SetIndexLabel(1, "LOW_SELL");
   SetIndexLabel(2, "HIGH_BUY");
   SetIndexLabel(3, "LOW_BUY");
//----
   return(0);
  }
//+------------------------------------------------------------------+
//| Custom indicator deinitialization function                       |
//+------------------------------------------------------------------+
int deinit()
  {
   return(0);
  }
//+------------------------------------------------------------------+
//| Custom indicator iteration function                              |
//+------------------------------------------------------------------+
int start()
{
   int counted_bars=IndicatorCounted();

   int limit, i;
//---- indicator calculation
if (counted_bars == 0)


   if(counted_bars < 0) return(-1);
//---- last counted bar will be recounted
//   if(counted_bars > 0) counted_bars--;
   limit=(Bars-counted_bars)-1;



   for (i = limit; i >= 0; i--)
   {
   HIGH_SELL[i] = High[iHighest(NULL, 0, MODE_HIGH, X1, i+1)];
   LOW_SELL[i]  = Low[iLowest  (NULL, 0, MODE_LOW,  X2, i+1)];
   HIGH_BUY[i]  = High[iHighest(NULL, 0, MODE_HIGH, X3, i+1)];
   LOW_BUY[i]   = Low[iLowest  (NULL, 0, MODE_LOW,  X4, i+1)];
   }

//----
   return(0);
  }
//+------------------------------------------------------------------+