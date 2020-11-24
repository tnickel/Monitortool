// FILE_NAME.: ##_HK_SR_MA_SD_01.mq4
//+------------------------------------------------------------------+
//|                                            ##_HK_SD_MA_01BIN.mq4 |
//|                                    Copyright © 2013, SIRIUS (HK) |
//|                                            START 09-10-2013 (HK) |
//+------------------------------------------------------------------+
//
// LAST UPDATE 09-10-2013 (HK)
//
#property copyright "2013, SIRIUS (HK)"

#property indicator_separate_window

#property indicator_maximum  1.1
#property indicator_minimum  0

#property indicator_buffers 4

#property indicator_color1 Magenta  // TRADE_SIGNAL
#property indicator_color2 Red      // STD_DEV 
#property indicator_color3 Yellow   // STD_DEV_SMMA
#property indicator_color4 White    // REF_LINE

#property indicator_width1 3
#property indicator_width2 2
#property indicator_width3 2
#property indicator_width4 2

//---- buffers
double TRADE_SIGNAL[];              // TRADE - SIGNAL (1)-(0)
double BUFFER_1[];                  // STD_DEV 
double BUFFER_2[];                  // STD_DEV_SMMA
double BUFFER_3[];                  // REF_LINE

int i;
 
extern   int      STD_DEV_PERIODEN  = 8;     // 
extern   int      STD_DEV_MA        = 4;     // 
extern   double   REFERENZ          = 3;     //

//+------------------------------------------------------------------+
//    INIT
//+------------------------------------------------------------------+
int init()
  {      
   SetIndexStyle(0,DRAW_HISTOGRAM);   
   SetIndexStyle(1,DRAW_NONE);
   SetIndexStyle(2,DRAW_NONE); 
   SetIndexStyle(3,DRAW_NONE);
       
   SetIndexDrawBegin(0,i-1);
   SetIndexDrawBegin(1,i-1);
   SetIndexDrawBegin(2,i-1);
   SetIndexDrawBegin(3,i-1);  
   
   SetIndexBuffer(0, TRADE_SIGNAL);   
   SetIndexBuffer(1, BUFFER_1);
   SetIndexBuffer(2, BUFFER_2);
   SetIndexBuffer(3, BUFFER_3);   
      
   SetIndexLabel(0,"TRADE_SIGNAL");   
   SetIndexLabel(1,"STD_DEV");
   SetIndexLabel(2,"STD_DEV_SMMA");
   SetIndexLabel(3,"REFERENZ_LINE");
   
   return(0);
  }
//+------------------------------------------------------------------+
//       START
//+------------------------------------------------------------------+
int start()
  {
//+------------------------------------------------------------------+
//+      STD_DEV   
//+------------------------------------------------------------------+ 
 
   for(i = Bars; i >= 0; i--) 
   {
   BUFFER_1[i] = iStdDev(NULL,0,STD_DEV_PERIODEN,0,MODE_SMMA,PRICE_CLOSE,i);   
   }

//+------------------------------------------------------------------+
//+      GLÄTTUNG von BUFFER_1 + Referenz-Line 
//+------------------------------------------------------------------+ 
     
   for(i = Bars; i >= 0; i--) 
   { 
   BUFFER_2[i] = iMAOnArray(BUFFER_1,Bars,STD_DEV_MA,0,MODE_SMMA,i);
   BUFFER_3[i] = REFERENZ / 10000;
   } 
   
//+------------------------------------------------------------------+
//+      IN - OUT     
//+------------------------------------------------------------------
 
   for(i = Bars; i >= 0; i--) 
   {   
   TRADE_SIGNAL[i] = 0;      
         
   if(BUFFER_2[i+1] <= BUFFER_2[i] && BUFFER_2[i] >= BUFFER_3[i])
      {
      TRADE_SIGNAL[i] = 1;
      }
   }
 return(0);
 }
 
//+------------------------------------------------------------------+