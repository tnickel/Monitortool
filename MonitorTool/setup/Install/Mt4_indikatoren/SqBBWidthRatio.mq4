//+------------------------------------------------------------------+
//|                                              BBandWidthRatio.mq4 |
//|                                                                  |
//+------------------------------------------------------------------+
#property copyright "Copyright © 2005, MetaQuotes Software Corp."
#property link      "http://www.metaquotes.net/"

#property indicator_separate_window
#property indicator_buffers 1
#property indicator_color1 Blue

//---- indicator parameters
extern int    BandsPeriod=20;
extern double BandsDeviations=2.0;
extern int    AppliedPrice=PRICE_CLOSE;

//---- buffers
double WidthBuffer[];

double MovingBuffer[];

//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int init()
  {
  IndicatorBuffers(2);

//---- indicators
   SetIndexStyle(0,DRAW_LINE);
   SetIndexBuffer(0,WidthBuffer);
   SetIndexStyle(1,DRAW_LINE);
   SetIndexBuffer(1,MovingBuffer);
//----
   return(0);
  }
//+------------------------------------------------------------------+
//| Bollinger Bands                                                  |
//+------------------------------------------------------------------+
int start()
  {
   int    i,k,counted_bars=IndicatorCounted();
   double deviation;
   double sum,oldval,newres;
   int period;

//----
   i=Bars-counted_bars-1;
   
   if(counted_bars > 0) i++;
   
   while(i>=0) {
      sum=0.0;
      period = MathMin(BandsPeriod, Bars-i);
      
      k=i+period-1;
      oldval=iMA(NULL,0,period,0,MODE_SMA,AppliedPrice,i);
      
      while(k>=i) {
         newres=Close[k]-oldval;
         sum+=newres*newres;
         k--;
      }
      
      deviation=BandsDeviations*MathSqrt(sum/period);
   
      double sko = MathSqrt(sum / BandsPeriod);
      if(oldval > 0) {
         WidthBuffer[i] = 2.0*(BandsDeviations*sko)/oldval; 
      } else {
         WidthBuffer[i] = 0;
      }      

      i--;
   }
//----
   return(0);
  }
//+------------------------------------------------------------------+
