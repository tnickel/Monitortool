//+------------------------------------------------------------------+
//|                                   HK_CCI_SUPERTEND_OFFSET_02BIN.mq4 |
//|                                    Copyright © 2012, SIRIUS (HK) |
//|                                                 START 12-09-2012 |
//+------------------------------------------------------------------+
//
// LAST UPDATE.: 112-09-2012 (HK)
//
#property copyright "Copyright © 2012, SIRIUS (HK)"

#property indicator_separate_window

#property indicator_maximum  1.1
#property indicator_minimum -1.1

#property indicator_level1   0

#property indicator_buffers 4

#property indicator_color1 Yellow
#property indicator_color2 Red
#property indicator_color3 White
#property indicator_color4 Magenta

#property indicator_width1 3
#property indicator_width2 1 
#property indicator_width3 1
#property indicator_width4 1

extern int    CCI_PERIODE     = 48;
extern double OFFSET          = 10;

double TRADE_SIGNAL[];
double TREND[];
double OFFSET_UP[];
double OFFSET_DN[];
int    STATUS         = 0;
//+------------------------------------------------------------------+
//| init function 
//+------------------------------------------------------------------+
int init()
  {
//---- indicators

   SetIndexBuffer(0, TRADE_SIGNAL);
   SetIndexBuffer(1, TREND);
   SetIndexBuffer(2, OFFSET_UP);   
   SetIndexBuffer(3, OFFSET_DN);
      
   SetIndexStyle(0, DRAW_HISTOGRAM);
   SetIndexStyle(1, DRAW_NONE);
   SetIndexStyle(2, DRAW_NONE);
   SetIndexStyle(3, DRAW_NONE);
//----
   return(0);
  }
//+------------------------------------------------------------------+
//| deinit function 
//+------------------------------------------------------------------+
int deinit()
  {
   return(0);
  }
//+------------------------------------------------------------------+
//| START function 
//+------------------------------------------------------------------+
int start()
  {
   
   int limit, i, counter;
   double Range, AvgRange, cciTrendNow, cciTrendPrevious, var;

   int counted_bars = IndicatorCounted();
   
//---- check for possible errors
   if(counted_bars < 0) return(-1);
   
//---- last counted bar will be recounted
   if(counted_bars > 0) counted_bars--;

   limit = Bars - counted_bars;
   
   for(i = limit; i >= 0; i--) 
      {
      cciTrendNow      = iCCI(NULL, 0, CCI_PERIODE, PRICE_TYPICAL, i);
      cciTrendPrevious = iCCI(NULL, 0, CCI_PERIODE, PRICE_TYPICAL, i+1);
          
//----    
      if (cciTrendNow >= 0 && cciTrendPrevious < 0) 
         {
         TREND[i+1] = TREND[i+1];
         }
      
      if (cciTrendNow <= 0 && cciTrendPrevious > 0) 
         {
         TREND[i+1] = TREND[i+1];
         }
//----      
      if (cciTrendNow >= 0) 
         {
         TREND[i] = Low[i] - iATR(NULL, 0, 6, i);
                  
         if (TREND[i] < TREND[i+1]) 
            {
            TREND[i] = TREND[i+1];
            }
         }
      else 
         if (cciTrendNow <= 0) 
         {
         TREND[i] = High[i] + iATR(NULL, 0, 6, i);
         
         if (TREND[i] > TREND[i+1]) 
            {
            TREND[i] = TREND[i+1];
            }
         }
         if(TREND[i+1] > TREND[i]+ 0.0001) STATUS = -1;
         if(TREND[i+1] < TREND[i]- 0.0001) STATUS =  1;
         
         if(STATUS == -1)
         { 
         OFFSET_UP[i] = TREND[i] + OFFSET /10000;
         if(High[i] >= OFFSET_UP[i])
            {
            STATUS = 0;
            }         
         }
         
         if(STATUS == 1)
         {
         OFFSET_DN[i] = TREND[i] - OFFSET /10000;
         if(Low[i] <= OFFSET_DN[i])
            {
            STATUS = 0;
            }
         }
         TRADE_SIGNAL[i] = 0;
         
         if(STATUS ==  1) TRADE_SIGNAL[i] =  1;
         if(STATUS == -1) TRADE_SIGNAL[i] = -1;
      }
  
//----
   return(0);
  }
//+------------------------------------------------------------------+