//+------------------------------------------------------------------+
//|                                                    SqFractal.mq4 |
//|                            Copyright © 2020, StrategyQuant s.r.o |
//|                                        https://strategyquant.com |
//+------------------------------------------------------------------+
#property copyright "Copyright © 2020, StrategyQuant s.r.o"
#property link      "https://strategyquant.com"

#property indicator_chart_window
#property indicator_buffers 2
#property indicator_color1 Red
#property indicator_color2 Blue
//---- input parameters

extern int Fractal = 3; 
int FractalUsed = 3;

//---- buffers
double ExtUpFractalsBuffer[];
double ExtDownFractalsBuffer[];
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int init()
  {
//---- indicator buffers mapping  
    SetIndexBuffer(0,ExtUpFractalsBuffer);
    SetIndexBuffer(1,ExtDownFractalsBuffer);   
//---- drawing settings
    SetIndexStyle(0,DRAW_ARROW);
    SetIndexArrow(0,119);
    SetIndexStyle(1,DRAW_ARROW);
    SetIndexArrow(1,119);
//----
    SetIndexEmptyValue(0,0.0);
    SetIndexEmptyValue(1,0.0);
//---- name for DataWindow
    SetIndexLabel(0,"Fractal Up");
    SetIndexLabel(1,"Fractal Down");
    
    if((Fractal - 1) / 2 <= 0){
      FractalUsed = 3;
      printf("Incorrect value for input variable Fractal=%d. Indicator will use value=%d for calculations.", Fractal, FractalUsed);
    }
    else {
      FractalUsed = Fractal;
    }
    
//---- initialization done   
   return(0);
  }
//+------------------------------------------------------------------+
//| Custor indicator deinitialization function                       |
//+------------------------------------------------------------------+
int deinit()
  {
//---- TODO: add your code here
   
//----
   return(0);
  }
//+------------------------------------------------------------------+
//| Custom indicator iteration function                              |
//+------------------------------------------------------------------+
int start()
  {
   int    i,a,nCountedBars;
   bool   bFoundHigh, bFoundLow;
   double dCurrentHigh, dCurrentLow;
   
   nCountedBars=IndicatorCounted();
//---- last counted bar will be recounted    
   nCountedBars--;
   i=Bars-nCountedBars;
     
   if(Bars < FractalUsed){
      for(a=i; a>=0; a++){
         ExtUpFractalsBuffer[i]=0;
         ExtDownFractalsBuffer[i]=0;
      }
      return(0);
   }  
     
   int eachSideLength = (FractalUsed - 1) / 2;
//----Up and Down Fractals
   while(i>=0)
     {
      
      int middleBar = i + eachSideLength + 1;
      
      dCurrentHigh = High[middleBar]; 
      dCurrentLow = Low[middleBar];
      bFoundHigh = true;
      bFoundLow = true;
      
      for(a=i+FractalUsed; a>i; a--){
         if(a == middleBar) continue;
         
      //----Fractals up
         if(High[a] >= dCurrentHigh) bFoundHigh = false;
      //----Fractals down
         if(Low[a] <= dCurrentLow) bFoundLow = false;
      }
      
      ExtUpFractalsBuffer[i]=bFoundHigh ? dCurrentHigh : 0;
      ExtDownFractalsBuffer[i]=bFoundLow ? dCurrentLow : 0;
                                    
      i--;
     }
//----
   return(0);
  }
//+------------------------------------------------------------------+