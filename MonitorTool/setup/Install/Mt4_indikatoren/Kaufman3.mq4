//+------------------------------------------------------------------+
//|                                                     Kaufman3.mq4 |
//|                              Copyright © 2004, by konKop & wellx |
//|                              Modified 2006, mbkennel             |
//|                                        http://www.metaquotes.net |
//+------------------------------------------------------------------+
#property copyright "Copyright © 2004, by konKop, GOODMAN, Mstera, af + wellx; 2006 mbkennel"
#property link      "http://www.metaquotes.net"

//
//  This is a modification of the Kaufman AMA indicator to add
//  an extra exponential moving average to the KAMA value.
//
//  It may be interesting as a basis for a trend following indicator,
//  as it may be whipped out of longer term trends more than conventional
//  methods.
//
//  Try on 1H or 4H charts, with various parameters. 


#property indicator_chart_window
#property indicator_buffers 2
#property indicator_color1 White
#property indicator_color2 Magenta

//---- input parameters
extern int       periodAMA=6; // length of time to compute signal-to-noise ratio
extern int       nfast=2;     // EMA length with high signal to noise
extern int       nslow=60;    // EMA length with lowest signal to noise
extern double    G=2.5;       // nonlinear squashing

extern double    EMAofKaufPeriod=16.0;  // for signal line, fixed EMA of kAMA
extern int       offset=1;    // offset this number of bars


//---- buffers
double kAMAbuffer[];
double kAMAfiltbuffer[];
//+------------------------------------------------------------------+

int    cbars=0;
double slowSC,fastSC;

//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int init()
  {
//---- indicators
   IndicatorBuffers(2); 
   SetIndexStyle(0,DRAW_LINE);
   //SetIndexDrawBegin(0,nslow+nfast);
   SetIndexBuffer(0,kAMAbuffer);
   SetIndexBuffer(1,kAMAfiltbuffer);

   IndicatorDigits(6);
   
   //slowSC=0.064516;
   //fastSC=0.2;
   
   cbars=IndicatorCounted();
//----
   return(0);
  }
//+------------------------------------------------------------------+
//| Custom indicator deinitialization function                       |
//+------------------------------------------------------------------+
int deinit()
  {
   return(0);
  }
//+------------------------------------------------------------------+
//| Custom indicator iteration function                              |
//+------------------------------------------------------------------+
int start()
  {
   int    i,pos=0;
   double noise,noise0,AMA,AMA0,signal,ER;
   double dSC,ERSC,wlxSSC;
   
//---- TODO: add your code here
   slowSC=(2.0 /(nslow+1));
   fastSC=(2.0 /(nfast+1));
   

   if (Bars<=(periodAMA+2)) return(0);
   
   
   //---- check for possible errors
   if (cbars<0) return(-1);
//---- last counted bar will be recounted
   if (cbars>0) cbars--;
   pos=Bars-periodAMA-2;
   //pos=100;
   //Print("cbars1: ", cbars);
   AMA0=Close[pos+1];
   while (pos>=offset)
     {
      if(pos==Bars-periodAMA-2) AMA0=Close[pos+1];
      signal=MathAbs(Close[pos]-Close[pos+periodAMA]);
      noise=0;
      for(i=0;i<periodAMA;i++)
       {
        noise=noise+MathAbs(Close[pos+i]-Close[pos+i+1]);
       }
      ER =signal/noise;
      dSC=(fastSC-slowSC);
      ERSC=ER*dSC;
      wlxSSC=ERSC+slowSC;
      AMA=AMA0+(MathPow(wlxSSC,G)*(Close[pos]-AMA0));
      kAMAbuffer[pos-offset]=AMA;
      
      /*
      Print("dsC: ", dSC);
      Print("ERSC: ", ERSC);
      Print("slowSc: ", slowSC);
      Print("fastSc: ", fastSC);
      Print("signal: ", signal);
      Print("noise: ",  noise);
      Print("SSC: ", wlxSSC);
      Print("AMA0: ", AMA0);
      Print("AMA: ", AMA);
      Print("MathPow: ", MathPow(wlxSSC,G));
      Print("pos: ", pos);
      Print("close[pos]: ", Close[pos]);
      Print("------------------------ ", 0);
    */
      AMA0=AMA;
      pos--;
     }
     //Print("cbars2: ", cbars);
     
     
//----
   pos=Bars-periodAMA-2;

   EMAOnArray(pos-1, 2.0/(EMAofKaufPeriod+1.0), kAMAbuffer,kAMAfiltbuffer,offset);
   return(0);
  }
  
  
void EMAOnArray(int N, double p, double input[], double& output[], int offset) {
   // Perform an "EMA" on array input[] with mixing parameter 'p'
   // 0 < p < 1.
   //
   // p, conventionally is 2.0/(L+1.0) where L is the 'length' parameter.
   // In an EMA, the length and thus 'p' need not be integers.
   // initial value is input[N-1], and will set output[N-1] down to output[0].
   //
   
   double omp = 1.0-p; 
   double ema = input[N-1];   
   for (int i=N-1; i>=offset; i--) {
      double v = input[i];
      ema = p*v + omp*ema;
      output[i-offset] = ema; 
   }
}


