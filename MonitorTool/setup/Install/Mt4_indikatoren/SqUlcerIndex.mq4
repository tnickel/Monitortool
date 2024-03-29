//+------------------------------------------------------------------+
//|                                                  SQUlcerIndex.mq4|
//|                            Copyright © @2021 StrategyQuant s.r.o.|
//|                                      http://www.strategyquant.com|
//+------------------------------------------------------------------+
#property copyright "Copyright © @2021 StrategyQuant s.r.o."
#property link      "http://www.strategyquant.com"

#property indicator_separate_window
#property indicator_buffers 2
#property indicator_color1 Red

extern int UIMode = 1;  /// UP UI = 1; Down UI = 2
extern int UIPeriod=24;



double UI[];
double ddBuffer[];

int init()
{
 IndicatorShortName("Ulcer Index");
 IndicatorBuffers(2);
 IndicatorDigits(Digits);
 SetIndexStyle(0,DRAW_LINE);
 SetIndexBuffer(0,UI);
 SetIndexStyle(1,DRAW_NONE);
 SetIndexBuffer(1,ddBuffer);
 IndicatorDigits(MarketInfo(Symbol(),MODE_DIGITS)+2);
 IndicatorShortName("SqUlcerIndex("+UIMode+","+UIPeriod+")");

 return(0);
}

int deinit()
{

 return(0);
}

int start()
  {
   int i, limit;
   int counted_bars=IndicatorCounted();
   if(counted_bars>0)
      counted_bars--;
   limit=Bars-counted_bars;
   int index =0;
   double sum = 0;
   for(i=limit; i>=0; i--)
     {

     if(UIMode ==1){
        
           index= iHighest(NULL, 0, MODE_CLOSE, UIPeriod, i);
           ddBuffer[i] = 100*((Close[i]-Close[index])/(Close[index]>0 ?Close[index]:0.000001));
           sum = 0;
           
           for(int ka=0;ka<UIPeriod;ka++){sum = sum +MathPow(ddBuffer[i+ka],2);}
           UI[i] = NormalizeDouble(MathSqrt(sum/UIPeriod),4);
           
           }
     else if(UIMode ==2){

           index= iLowest(NULL, 0, MODE_CLOSE, UIPeriod, i);
           double invClose = (Close[i]!=0?1/Close[i]:0.000001);
           double lc = (Close[index]!=0?1/Close[index]:0.000001);
           ddBuffer[i] = 100*((invClose-lc)/(lc));
           sum = 0;
           
           for(int kb=0;kb<UIPeriod;kb++){sum = sum +MathPow(ddBuffer[i+kb],2);}
           UI[i] = NormalizeDouble(MathSqrt(sum/UIPeriod),4);
          
           }
     }
 
   return(0);
}
