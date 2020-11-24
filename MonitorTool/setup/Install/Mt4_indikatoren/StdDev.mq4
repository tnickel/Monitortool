//+------------------------------------------------------------------+
//|                                           Standard Deviation.mq4 |
//|                      Copyright © 2005, MetaQuotes Software Corp. |
//|                                       http://www.metaquotes.net/ |
//+------------------------------------------------------------------+
#property copyright "Copyright © 2005, MetaQuotes Software Corp."
#property link      "http://www.metaquotes.net/"

#property indicator_separate_window
#property indicator_minimum 0
#property indicator_buffers 1
#property indicator_color1 Blue
//---- input parameters
extern int ExtStdDevPeriod=20;
extern int ExtStdDevMAMethod=0;
extern int ExtStdDevAppliedPrice=0;
extern int ExtStdDevShift=0;
//---- buffers
double ExtStdDevBuffer[];
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int init()
  {
   string sShortName;
//---- indicator buffer mapping
   SetIndexBuffer(0,ExtStdDevBuffer);
//---- indicator line
   SetIndexStyle(0,DRAW_LINE);
//---- line shifts when drawing
   SetIndexShift(0,ExtStdDevShift);   
//---- name for DataWindow and indicator subwindow label
   sShortName="StdDev("+ExtStdDevPeriod+")";
   IndicatorShortName(sShortName);
   SetIndexLabel(0,sShortName);
//---- first values aren't drawn
   SetIndexDrawBegin(0,ExtStdDevPeriod);
//----
   return(0);
  }
//+------------------------------------------------------------------+
//| Standard Deviation                                               |
//+------------------------------------------------------------------+
int start()
  {
   int    i,j,nLimit,nCountedBars;
   double dAPrice,dAmount,dMovingAverage;  
//---- insufficient data
   if(Bars<=ExtStdDevPeriod) return(0);
//---- bars count that does not changed after last indicator launch.
   nCountedBars=IndicatorCounted();
//----Standard Deviation calculation
   i=Bars-ExtStdDevPeriod-1;
   if(nCountedBars>ExtStdDevPeriod) 
      i=Bars-nCountedBars;  
   while(i>=0)
     {
      dAmount=0.0;
      dMovingAverage=iMA(NULL,0,ExtStdDevPeriod,0,ExtStdDevMAMethod,ExtStdDevAppliedPrice,i);
      for(j=0; j<ExtStdDevPeriod; j++)
        {
         dAPrice=GetAppliedPrice(ExtStdDevAppliedPrice,i+j);
         dAmount+=(dAPrice-dMovingAverage)*(dAPrice-dMovingAverage);
        }
      ExtStdDevBuffer[i]=MathSqrt(dAmount/ExtStdDevPeriod);
      i--;
     }
//----
   return(0);
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double GetAppliedPrice(int nAppliedPrice, int nIndex)
  {
   double dPrice;
//----
   switch(nAppliedPrice)
     {
      case 0:  dPrice=Close[nIndex];                                  break;
      case 1:  dPrice=Open[nIndex];                                   break;
      case 2:  dPrice=High[nIndex];                                   break;
      case 3:  dPrice=Low[nIndex];                                    break;
      case 4:  dPrice=(High[nIndex]+Low[nIndex])/2.0;                 break;
      case 5:  dPrice=(High[nIndex]+Low[nIndex]+Close[nIndex])/3.0;   break;
      case 6:  dPrice=(High[nIndex]+Low[nIndex]+2*Close[nIndex])/4.0; break;
      default: dPrice=0.0;
     }
//----
   return(dPrice);
  }
//+------------------------------------------------------------------+