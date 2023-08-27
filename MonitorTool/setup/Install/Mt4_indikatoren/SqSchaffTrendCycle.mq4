//+------------------------------------------------------------------+
//|                                           Schaff Trend Cycle.mq4 |
//|                                                           mladen |
//+------------------------------------------------------------------+
#property copyright "mladen"
#property link      "mladenfx@gmail.com"

#property indicator_separate_window
#property indicator_buffers 3
#property indicator_color1  clrRed
#property indicator_color2  clrGreen
#property indicator_color3  clrGreen
#property strict

//
//
//
//
//

extern int STCPeriod    = 10; // Schaff period
extern int FastMAPeriod = 20; // Fast ema period
extern int SlowMAPeriod = 50; // Slow ema period


double stcBuffer[];
double stcBufferUA[];
double stcBufferUB[];
double macdBuffer[];
double fastKBuffer[];
double fastDBuffer[];
double fastKKBuffer[];
double trend[];


//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
//
//
//
//
//

int init()
{
   IndicatorBuffers(8);
      SetIndexBuffer(0,stcBuffer);
      SetIndexBuffer(1,stcBufferUA);
      SetIndexBuffer(2,stcBufferUB);
      SetIndexBuffer(3,macdBuffer);
      SetIndexBuffer(4,fastKBuffer);
      SetIndexBuffer(5,fastDBuffer);
      SetIndexBuffer(6,fastKKBuffer);
      SetIndexBuffer(7,trend);
   IndicatorShortName("Schaff Trend Cycle ("+(string)STCPeriod+","+(string)FastMAPeriod+","+(string)SlowMAPeriod+")");
   return(0);
}

int deinit()
{
   return(0);
}

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
//
//
//
//
//

int start()
{
   int counted_bars=IndicatorCounted();
      if(counted_bars < 0) return(-1);
      if(counted_bars>0) counted_bars--;
         int limit = MathMin(Bars-counted_bars,Bars-1);
         if (trend[limit]==1) CleanPoint(limit,stcBufferUA,stcBufferUB);

   //
   //
   //
   //
   //
   
   for(int i = limit; i >= 0; i--)
   {
      macdBuffer[i] = iMA(NULL,0,FastMAPeriod,0,MODE_EMA,PRICE_CLOSE,i)-
                      iMA(NULL,0,SlowMAPeriod,0,MODE_EMA,PRICE_CLOSE,i);

            double lowMacd  = macdBuffer[ArrayMinimum(macdBuffer,STCPeriod,i)];
            double highMacd = macdBuffer[ArrayMaximum(macdBuffer,STCPeriod,i)]-lowMacd;
                              fastKBuffer[i] = (highMacd > 0) ? 100*((macdBuffer[i]-lowMacd)/highMacd) : (i<Bars-1) ? fastKBuffer[i+1] : 0;
                              fastDBuffer[i] = (i<Bars-1) ? fastDBuffer[i+1]+0.5*(fastKBuffer[i]-fastDBuffer[i+1]) : fastKBuffer[i];
               
            double lowStoch  = fastDBuffer[ArrayMinimum(fastDBuffer,STCPeriod,i)];
            double highStoch = fastDBuffer[ArrayMaximum(fastDBuffer,STCPeriod,i)]-lowStoch;
                               fastKKBuffer[i] = (highStoch > 0) ? 100*((fastDBuffer[i]-lowStoch)/highStoch) : (i<Bars-1) ? fastKKBuffer[i+1] : 0;
                               stcBuffer[i]    = (i<Bars-1) ? stcBuffer[i+1]+0.5*(fastKKBuffer[i]-stcBuffer[i+1]) : fastKKBuffer[i];
                               stcBufferUA[i]  = EMPTY_VALUE;
                               stcBufferUB[i]  = EMPTY_VALUE;
                               trend[i]        = (i<Bars-1) ? (stcBuffer[i] > stcBuffer[i+1]) ? 1 : (stcBuffer[i] < stcBuffer[i+1]) ? -1 : trend[i+1] : 0;      
            if (trend[i] == 1) PlotPoint(i,stcBufferUA,stcBufferUB,stcBuffer);
   }   
   return(0);
}

//-------------------------------------------------------------------
//                                                                  
//-------------------------------------------------------------------
//
//
//
//
//

void CleanPoint(int i,double& first[],double& second[])
{
   if (i>=Bars-3) return;
   if ((second[i]  != EMPTY_VALUE) && (second[i+1] != EMPTY_VALUE))
        second[i+1] = EMPTY_VALUE;
   else
      if ((first[i] != EMPTY_VALUE) && (first[i+1] != EMPTY_VALUE) && (first[i+2] == EMPTY_VALUE))
          first[i+1] = EMPTY_VALUE;
}

void PlotPoint(int i,double& first[],double& second[],double& from[])
{
   if (i>=Bars-2) return;
   if (first[i+1] == EMPTY_VALUE)
      if (first[i+2] == EMPTY_VALUE) 
            { first[i]  = from[i];  first[i+1]  = from[i+1]; second[i] = EMPTY_VALUE; }
      else  { second[i] =  from[i]; second[i+1] = from[i+1]; first[i]  = EMPTY_VALUE; }
   else     { first[i]  = from[i];                           second[i] = EMPTY_VALUE; }
}