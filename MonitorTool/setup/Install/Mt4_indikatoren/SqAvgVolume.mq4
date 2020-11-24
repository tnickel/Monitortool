//+------------------------------------------------------------------+
//|                                                    AvgVolume.mq4 |
//|                      Copyright © 2010, MetaQuotes Software Corp. |
//|                                        http://www.metaquotes.net |
//+------------------------------------------------------------------+
#property copyright ""
#property link      ""

#property indicator_separate_window
#property indicator_buffers 2
#property indicator_color1 Red
#property indicator_color2 White

#property indicator_width2 3


extern int     MAPeriod = 100;

double vol[], avgVol[];
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int init() {
//---- indicators
      SetIndexBuffer(0,avgVol);    
      SetIndexStyle(0,DRAW_LINE,0,2);
      SetIndexLabel(0,"Average Volume("+MAPeriod+")");
      
      SetIndexBuffer(1,vol);       
      SetIndexStyle(1,DRAW_HISTOGRAM);
      SetIndexLabel(1,"Volume");
      
      IndicatorShortName("AvgVolume");
      

//----
   return(0);
}

//+------------------------------------------------------------------+

int deinit() {
   return(0);
}

//+------------------------------------------------------------------+

int start() {
  
   double tempv;
   int limit;
   int counted_bars=IndicatorCounted();
//---- last counted bar will be recounted
   if(counted_bars>0) counted_bars--;

   limit = Bars-counted_bars;
      
   for(int i=limit-1; i>=0; i--) {
      vol[i] = Volume[i];
      
      tempv=0; 
      int period = MathMin(MAPeriod, Bars-i);
      
      for (int n=i;n<i+period;n++ ) {
         tempv = Volume[n] + tempv; 
      } 

      avgVol[i] = tempv/period;
   }
   return(0);
}
//+------------------------------------------------------------------+
         