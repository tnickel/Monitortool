//+------------------------------------------------------------------+
//|                                          !!_####_THOMAS_INDI.mq4 |
//|                                    Copyright? 2013, SIRIUS (HK). |
//|                                                 START 05-09-2013 |
//+------------------------------------------------------------------+
#property copyright "Copyright? 2013, SIRIUS (HK)"

#property indicator_separate_window

#property indicator_maximum  1.1
#property indicator_minimum -1.1

#property indicator_buffers 5

#property indicator_color1 Yellow
#property indicator_color2 Red
#property indicator_color3 Magenta
#property indicator_color4 Yellow
#property indicator_color5 White

#property  indicator_width1  3
#property  indicator_width2  1
#property  indicator_width3  1
#property  indicator_width4  1
#property  indicator_width5  1

//---- input parameters
extern int     X1          = 6;
extern int     X2          = 12;
extern int     X3          = 6;
extern int     X4          = 12;

//---- buffers
double TRADE_SIGNAL[];
double HIGH_FAST[];
double HIGH_SLOW[];
double LOW_FAST[];
double LOW_SLOW[];
int   i;
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int init()
  {
//---- indicators
   SetIndexBuffer(0,TRADE_SIGNAL);
   SetIndexBuffer(1,HIGH_FAST);
   SetIndexBuffer(2,HIGH_SLOW);   
   SetIndexBuffer(3,LOW_FAST);   
   SetIndexBuffer(4,LOW_SLOW);   
   
   SetIndexStyle(0,DRAW_HISTOGRAM);
   SetIndexStyle(1,DRAW_NONE);
   SetIndexStyle(2,DRAW_NONE);
   SetIndexStyle(3,DRAW_NONE);
   SetIndexStyle(4,DRAW_NONE);

   SetIndexLabel(0, "TRADE_SIGNAL");      
   SetIndexLabel(1, "HIGH_FAST");
   SetIndexLabel(2, "HIGH_SLOW");
   SetIndexLabel(3, "LOW_FAST");
   SetIndexLabel(4, "LOW_SLOW");
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
   HIGH_FAST[i] = High[iHighest(NULL, 0, MODE_HIGH,X1, i+1)];
   HIGH_SLOW[i] = High[iHighest(NULL, 0, MODE_HIGH,X2, i+1)];
   LOW_FAST[i]  = Low[iLowest  (NULL, 0, MODE_LOW, X3, i+1)];
   LOW_SLOW[i]  = Low[iLowest  (NULL, 0, MODE_LOW, X4, i+1)];
   }

//---- TRADE_SIGNAL erzeugen

   for (i = limit; i >= 0; i--)
   {
   TRADE_SIGNAL[i] = TRADE_SIGNAL[i+1];
   
   if(TRADE_SIGNAL[i] ==  1 && LOW_FAST[i]   >= Low[i])   TRADE_SIGNAL[i] =  0;
   if(TRADE_SIGNAL[i] == -1 && HIGH_FAST[i]  <= High[i])  TRADE_SIGNAL[i] =  0; 
   if(HIGH_SLOW[i]    == HIGH_FAST[i] && HIGH_FAST[i] <= High[i]) TRADE_SIGNAL[i] =  1;
   if(LOW_SLOW[i]     == LOW_FAST[i]  && LOW_FAST[i]  >= Low[i])  TRADE_SIGNAL[i] = -1;     

   }
   return(0);
  }
//+------------------------------------------------------------------+