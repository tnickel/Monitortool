//+------------------------------------------------------------------+
//|                                                    AvgVolume.mq4 |
//|                      Copyright © 2010, MetaQuotes Software Corp. |
//|                                        http://www.metaquotes.net |
//+------------------------------------------------------------------+
#property copyright ""
#property link      ""

#property indicator_separate_window
#property indicator_buffers 2
#property indicator_color1 White
#property indicator_color2 Red

#property indicator_width1 3


extern int     MAPeriod = 100;

double vol[], v4[];
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int init() {
//---- indicators
      SetIndexBuffer(0,vol);
      SetIndexStyle(0,DRAW_HISTOGRAM);
      SetIndexLabel(0,"Volume");
      
      SetIndexBuffer(1,v4);
      SetIndexStyle(1,DRAW_LINE,0,2);
      SetIndexLabel(1,"Average("+MAPeriod+")");
      
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
  
   double VolLowest,Range,Value2,Value3,HiValue2,HiValue3,LoValue3,tempv2,tempv3,tempv;
   int limit;
   int counted_bars=IndicatorCounted();
//---- last counted bar will be recounted
   if(counted_bars>0) counted_bars--;

   limit = Bars-counted_bars;
      
   for(int i=0; i<limit; i++) {
      vol[i] = Volume[i];
      
      tempv=0; 
      for (int n=i;n<i+MAPeriod;n++ ) {
         tempv = Volume[n] + tempv; 
      } 

      v4[i] = NormalizeDouble(tempv/MAPeriod,0);
   }
   return(0);
}
//+------------------------------------------------------------------+
         