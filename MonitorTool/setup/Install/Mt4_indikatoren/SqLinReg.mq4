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
extern int InputPrice=PRICE_CLOSE;

double ind_buffer[];
int mode;

int init() {
   switch(InputPrice){
      case PRICE_OPEN:
      case PRICE_HIGH:
      case PRICE_LOW:
      case PRICE_CLOSE:
      case PRICE_MEDIAN:
      case PRICE_TYPICAL:
      case PRICE_WEIGHTED:
         mode = InputPrice;
         break;
      default:
         printf("Incorrect value for input variable InputPrice=%d. Indicator will use value PRICE_CLOSE for calculations.",InputPrice);
         mode=PRICE_CLOSE;
   }
   
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
   
      switch(mode){
               case PRICE_OPEN:
                  c=Open[x+i];
                  break;
               case PRICE_HIGH:
                  c=High[x+i];
                  break;
               case PRICE_LOW:
                  c=Low[x+i];
                  break;
               case PRICE_CLOSE:
                  c=Close[x+i];
                  break;
               case PRICE_MEDIAN:
                  c = (High[x+i] + Low[x+i]) / 2;
                  break;
               case PRICE_TYPICAL:
                  c = (High[x+i] + Low[x+i] + Close[x+i]) / 3;
                  break;
               case PRICE_WEIGHTED:
                  c = (High[x+i] + Low[x+i] + Close[x+i] + Close[x+i]) / 4;
                  break;
            }

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