//+------------------------------------------------------------------+
//|                                               SQSRPercentRank.mq4|
//|                            Copyright © @2021 StrategyQuant s.r.o.|
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property  copyright "Copyright © @2021 StrategyQuant s.r.o."
#property  link      "http://www.strategyquant.com"

//---- indicator settings
#property  indicator_separate_window;
#property  indicator_buffers 1
#property  indicator_color1  Red
#property  indicator_width1  1
//----
extern int Mode=1;
extern int Lenght=120;
extern int ATRPeriod=12;

//---- buffers
double IndiBuffer[];


//+------------------------------------------------------------------+

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
int init()
  {
   IndicatorBuffers(1);
   SetIndexStyle(0,DRAW_LINE);
   SetIndexDrawBegin(0,Lenght);
   IndicatorDigits(MarketInfo(Symbol(),MODE_DIGITS)+2);
   SetIndexBuffer(0,IndiBuffer);
   IndicatorShortName("SqSRPercRank("+Mode+","+Lenght+","+ATRPeriod+")");

   return(0);
  }

//+------------------------------------------------------------------+

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
int start()
  {
   int i, limit;
   int counted_bars=IndicatorCounted();
   if(counted_bars>0)
      counted_bars--;
   limit=Bars-counted_bars;

   for(i=limit; i>=0; i--)
     {
      int count =0;
       
      for(int a = 1; a<= Lenght; a++)
        {

         if( Mode ==1){ //// mode without ATR only HIGH-LOW Range
         
         
            if(Close[i]> Low[i+a] && Close[i]< High[i+a])
              {
               count++;
              }

        }
        else if(Mode ==2){ //// mode with ATR +- HIGH-LOW Range
        
        
            double atrValue = iATR(NULL,0,ATRPeriod,i);
            if(Close[i]> (Low[i+a]-atrValue) && Close[i]< High[i+a]+atrValue)
              {
               count++;
              }
        }

       }
        
         double percrank = (double)count/Lenght*100;
         IndiBuffer[i] =percrank;
        
         
     }
    
   return(0);
  }

//+------------------------------------------------------------------+

//+------------------------------------------------------------------+
