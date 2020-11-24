//+------------------------------------------------------------------+
//|                                                       linreg.mq4 |
//|                                         Copyright © 2006, ch33z3 |
//|                                   http://4xjournal.blogspot.com/ |
//+------------------------------------------------------------------+
#property  copyright "Copyright © 2006, ch33z3"
#property  link      "http://4xjournal.blogspot.com/"

#property indicator_chart_window
#property indicator_buffers 1
#property indicator_color1 Blue

extern int LRPeriod=13;

double ind_buffer[];

int init()
  {
   IndicatorBuffers(1);
   SetIndexStyle(0,DRAW_LINE,0,1);
   SetIndexBuffer(0,ind_buffer);
   return(0);
  }
  
int start()
  {
   int limit, i;
   int counted_bars=IndicatorCounted();
   if(counted_bars<0) return(-1);
   if(counted_bars>0) counted_bars--;
   limit=Bars-counted_bars;
   for(i=limit; i>=0; i--) {
      ind_buffer[i]=linreg(LRPeriod,i);
   }
   return(0);
  }
//+------------------------------------------------------------------+

double linreg(int p,int i)
   {
   double SumY=0;
   double Sum1=0;
   double Slope=0;
   double c;
   
   for (int x=0; x<=p-1;x++) {
      c=Close[x+i];
      SumY+=c;
      Sum1+=x*c; }
   double SumBars=p*(p-1)*0.5;
   double SumSqrBars=(p-1)*p*(2*p-1)/6;
	double Sum2=SumBars*SumY;
	double Num1=p*Sum1-Sum2;
	double Num2=SumBars*SumBars-p*SumSqrBars;
	if(Num2!=0) Slope=Num1/Num2;
	else Slope=0;
	double Intercept=(SumY-Slope*SumBars)/p;
	double linregval=Intercept+Slope*(p-1);
	return(linregval);
	}