//+------------------------------------------------------------------+
//|                                                         KAMA.mq4 |
//|                                    Copyright? 2009, Walter Choy. |
//|                                                                  |
//+------------------------------------------------------------------+
#property copyright "Copyright? 2009, Walter Choy."
#property link      ""

#property indicator_chart_window
#property indicator_buffers 1
#property indicator_color1 Red
#property indicator_color2 Red
#property indicator_color3 Red
//---- input parameters
extern int       kama_period = 10;
extern double    fast_ma_period = 2.0;
extern double    slow_ma_period = 30.0;
//---- buffers
double KAMA[];
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int init()
  {
//---- indicators
   SetIndexStyle(0,DRAW_LINE);
   SetIndexBuffer(0,KAMA);
   SetIndexLabel(0, "KAMA");
//----
   return(0);
  }
//+------------------------------------------------------------------+
//| Custom indicator deinitialization function                       |
//+------------------------------------------------------------------+
int deinit()
  {
//----
   
//----
   return(0);
  }
//+------------------------------------------------------------------+
//| Custom indicator iteration function                              |
//+------------------------------------------------------------------+
int start()
  {
   int    counted_bars=IndicatorCounted();
//----
   int i = Bars - counted_bars;
   
   double fastest = 2 / (fast_ma_period + 1);
   double slowest = 2 / (slow_ma_period + 1);
   
   while(i>0){
      double er = 1;
      double signal = MathAbs(Close[i] - Close[i+kama_period]);
      double noise = 0;
      for (int j=0; j<kama_period; j++)
         noise += MathAbs(Close[i+j] - Close[i+j+1]);
      if (noise > 0)
         er = signal / noise;
      double sc = MathPow((er * (fastest - slowest) + slowest), 2);
      KAMA[i] = KAMA[i+1] + sc * (Close[i] - KAMA[i+1]);
      i--;
   }
//----
   return(0);
  }
//+------------------------------------------------------------------+