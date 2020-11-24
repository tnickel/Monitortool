
//+------------------------------------------------------------------+
//|                                        Derivative Oscillator.mq4 |
//|                                                          Maury74 |
//|                                      molinari.maurizio@gmail.com |
//+------------------------------------------------------------------+
#property copyright "Maury74"
#property link      "molinari.maurizio@gmail.com"

#property indicator_separate_window
#property indicator_buffers 1
#property indicator_color1 SteelBlue


extern int RSIDer = 14;



double Rsi[];
double Ema0[];
double Ema1[];
double Ema2[];




int init()
  {
   IndicatorBuffers(5);
   SetIndexStyle(0,DRAW_HISTOGRAM,STYLE_SOLID,2);
   SetIndexStyle(1,DRAW_LINE,STYLE_SOLID,2);
   SetIndexBuffer(0,Ema2);
   SetIndexBuffer(1,Ema1);
   SetIndexBuffer(2,Ema0);
   SetIndexBuffer(3,Rsi);
   
   
   IndicatorDigits(4);
        
   return(0);
  }

int deinit()
   { 
    return(0);
   } 

int start()
   {
    int shift;
    int limit = 0;
  	
  	if(limit==0) limit = Bars;
     
   for(shift=limit-1;shift>=0;shift--)
      Rsi[shift] = iRSI(NULL,0,RSIDer,PRICE_CLOSE,shift);
  
   for(shift=limit-1;shift>=0;shift--) 
      Ema0[shift] = iMAOnArray(Rsi,0,5,0,MODE_EMA,shift);
  
   for(shift=limit-1;shift>=0;shift--) 
      Ema1[shift] = iMAOnArray(Ema0,0,3,0,MODE_EMA,shift);   
         
   for(shift=limit-1;shift>=0;shift--)
      Ema2[shift] = iMAOnArray(Ema0,0,3,0,MODE_EMA,shift) - iMAOnArray(Ema1,0,9,0,MODE_SMA,shift);
      
         
  
  
  return(0);
  
  }
  

