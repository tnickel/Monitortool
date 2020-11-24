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
//----
   if(Bars<=BandsPeriod) return(0);
//---- initial zero
   if(counted_bars<1)
      for(i=1;i<=BandsPeriod;i++)
        {
         MovingBuffer[Bars-i]=EMPTY_VALUE;
        }
//----
   int limit=Bars-counted_bars;
   if(counted_bars>0) limit++;
   for(i=0; i<limit; i++)
      MovingBuffer[i]=iMA(NULL,0,BandsPeriod,0,MODE_SMA,PRICE_CLOSE,i);
//----
   i=Bars-BandsPeriod+1;
   if(counted_bars>BandsPeriod-1) i=Bars-counted_bars-1;
   while(i>=0)
     {
      sum=0.0;
      k=i+BandsPeriod-1;
      oldval=MovingBuffer[i];
      while(k>=i)
        {
         newres=Close[k]-oldval;
         sum+=newres*newres;
         k--;
        }
      deviation=BandsDeviations*MathSqrt(sum/BandsPeriod);
      
      double sko = MathSqrt(sum / BandsPeriod);
      if(oldval > 0) {
         WidthBuffer[i] = 2.0*(BandsDeviations*sko)/oldval; //2.0*(BandsDeviations*sko); ///
      } else {
         WidthBuffer[i] = 0;
      }      

      i--;
     }
//----
   return(0);
  }
//+------------------------------------------------------------------+