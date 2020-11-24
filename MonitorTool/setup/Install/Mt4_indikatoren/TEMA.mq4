//+------------------------------------------------------------------+
//|                                                           TEMA   |
//|                Based on work of Patrick Mulloy and Robert Hill   | 
//|                                        simplified by Mark Fric   |
//|                                                                  |
//| Based on the formula developed by Patrick Mulloy                 |
//|                                                                  |
//| It can be used in place of EMA or to smooth other indicators.    |
//|                                                                  |
//| TEMA = 3 * EMA - 3 * (EMA of EMA) + EMA of EMA of EMA            |
//|                                                                  |
//+------------------------------------------------------------------+
#property  copyright "Copyright © 2012, Mark Fric "
#property  link      "http://www.geneticbuilder.com/"

//---- indicator settings
#property  indicator_chart_window
#property  indicator_buffers 1
#property  indicator_color1  Red
#property  indicator_width1  1
//----
extern int EMA_Period=14;
//---- buffers
double Ema[];
double EmaOfEma[];
double EmaOfEmaOfEma[];
double Tema[];

//+------------------------------------------------------------------+

int init() {
   IndicatorBuffers(4);
   
   SetIndexStyle(0,DRAW_LINE);
   SetIndexStyle(1,DRAW_NONE);
   SetIndexStyle(2,DRAW_NONE);
   SetIndexStyle(3,DRAW_NONE);
   SetIndexDrawBegin(0,EMA_Period);
   IndicatorDigits(MarketInfo(Symbol(),MODE_DIGITS)+2);

   SetIndexBuffer(0,Tema);
   SetIndexBuffer(1,EmaOfEma);
   SetIndexBuffer(2,EmaOfEmaOfEma);
   SetIndexBuffer(3,Ema);

   IndicatorShortName("TEMA("+EMA_Period+")");

   return(0);
}

//+------------------------------------------------------------------+

int start() {
   int i, limit;
   int    counted_bars=IndicatorCounted();
   if(counted_bars>0) counted_bars--;
   limit=Bars-counted_bars;

   for(i=limit; i>=0; i--)
      Ema[i]=iMA(NULL,0,EMA_Period,0,MODE_EMA,PRICE_CLOSE,i);
   for(i=limit; i >=0; i--)
      EmaOfEma[i]=iMAOnArray(Ema,Bars,EMA_Period,0,MODE_EMA,i);

   for(i=limit; i >=0; i--)
      EmaOfEmaOfEma[i]=iMAOnArray(EmaOfEma,Bars,EMA_Period,0,MODE_EMA,i);

   for(i=limit; i >=0; i--)
      Tema[i]=3 * Ema[i] - 3 * EmaOfEma[i] + EmaOfEmaOfEma[i];
   return(0);
}

//+------------------------------------------------------------------+

