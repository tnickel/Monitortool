//+------------------------------------------------------------------+
//|                                      SpearmanRankCorrelation.mq4 |
//|                      Copyright © 2007, MetaQuotes Software Corp. |
//|                                        http://www.metaquotes.net |
//+------------------------------------------------------------------+
// http://www.improvedoutcomes.com/docs/WebSiteDocs/Clustering/
// Clustering_Parameters/Spearman_Rank_Correlation_Distance_Metric.htm
// http://www.infamed.com/stat/s05.html
#property copyright "Copyright © 2007, MetaQuotes Software Corp."
#property link      "http://www.metaquotes.net"
//----
#property indicator_separate_window
#property indicator_buffers 1
#property indicator_color1 DarkBlue
//---- input parameters
extern int  rangeN = 14;
extern int  CalculatedBars = 0;
extern int  Maxrange = 30;
extern bool direction = true;
//---- buffers
double ExtMapBuffer1[];
double R2[];
double multiply;
int    PriceInt[];
int    SortInt[];
//+------------------------------------------------------------------+
//| calculate  RSP  function                                         |
//+------------------------------------------------------------------+
double SpearmanRankCorrelation(double Ranks[], int N)
  {
//----
   double res,z2;
   int i;
   for(i = 0; i < N; i++)
     {
       z2 += MathPow(Ranks[i] - i - 1, 2);
     }
   res = 1 - 6*z2 / (MathPow(N,3) - N);
//----
   return(res);
  }
//+------------------------------------------------------------------+
//| Ranking array of prices function                                 |
//+------------------------------------------------------------------+
void RankPrices(int InitialArray[])
  {
//----
   int i, k, m, dublicat, counter, etalon;
   double dcounter, averageRank;
   double TrueRanks[];
   ArrayResize(TrueRanks, rangeN);
   ArrayCopy(SortInt, InitialArray);
   for(i = 0; i < rangeN; i++) 
       TrueRanks[i] = i + 1;
   if(direction)
       ArraySort(SortInt, 0, 0, MODE_DESCEND);
   else
       ArraySort(SortInt, 0, 0, MODE_ASCEND);
   for(i = 0; i < rangeN-1; i++)
     {
       if(SortInt[i] != SortInt[i+1]) 
           continue;
       dublicat = SortInt[i];
       k = i + 1;
       counter = 1;
       averageRank = i + 1;
       while(k < rangeN)
         {
           if(SortInt[k] == dublicat)
             {
               counter++;
               averageRank += k + 1;
               k++;
             }
           else
               break;
         }
       dcounter = counter;
       averageRank = averageRank / dcounter;
       for(m = i; m < k; m++)
           TrueRanks[m] = averageRank;
       i = k;
     }
   for(i = 0; i < rangeN; i++)
     {
       etalon = InitialArray[i];
       k = 0;
       while(k < rangeN)
         {
           if(etalon == SortInt[k])
             {
               R2[i] = TrueRanks[k];
               break;
             }
           k++;
         }
     }
//----
   return;
  }
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int init()
  {
//---- indicators
   SetIndexStyle(0, DRAW_LINE);
   SetIndexBuffer(0, ExtMapBuffer1);
   ArrayResize(R2, rangeN);
   ArrayResize(PriceInt, rangeN);
   ArrayResize(SortInt, rangeN);
   if(Maxrange <= 0) 
       Maxrange = 10;
   if(rangeN > Maxrange) 
       IndicatorShortName("Decrease rangeN input!");
   else 
       IndicatorShortName("Spearman(" + rangeN + ")");
   if(CalculatedBars < 0) 
       CalculatedBars = 0;
   multiply = MathPow(10, Digits);
//----
   return(0);
  }
//+------------------------------------------------------------------+
//| Custom indicator deinitialization function                       |
//+------------------------------------------------------------------+
int deinit()
  {
//----
   return(0);
  }
//+------------------------------------------------------------------+
//| Custom indicator iteration function                              |
//+------------------------------------------------------------------+
int start()
  {
   int counted_bars = IndicatorCounted();
//----
   if(rangeN > Maxrange) 
       return(-1);
   int i, k, limit;
   if(counted_bars == 0)
     {
       if(CalculatedBars == 0)
           limit = Bars - rangeN;
       else
           limit = CalculatedBars;
     }
   if(counted_bars > 0)
       limit = Bars - counted_bars;
   for(i = limit; i >= 0; i--)
     {
       for(k = 0; k < rangeN; k++) 
           PriceInt[k] = Close[i+k]*multiply;
       RankPrices(PriceInt);
       ExtMapBuffer1[i] = SpearmanRankCorrelation(R2,rangeN);
     }
//----
   return(0);
  }
//+------------------------------------------------------------------+