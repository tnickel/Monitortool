
#property copyright "Sebastian Holtz forexsolutions"

#property indicator_separate_window
#property indicator_buffers 3
#property indicator_color1 clrYellow
#property indicator_color2 clrBlue
#property indicator_color3 clrRed
//#property indicator_color4 Red
//#property indicator_color5 Red
//#property indicator_color6 Yellow


extern int Einstellungs_Number = 1;

int Periode_X   = 13;
double Value_X  = 2.0;
int Over_Under  = 1;

//---- indicator parameters


//---- buffers
double Buffer_WP1[];
double Buffer_WP2[];
double Buffer_WP3[];
//double Buffer_WP4[];
//double Buffer_WP5[];
//double Buffer_Durchschnitt[];
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
void init()
  {
//---- indicators
   SetIndexStyle(0,DRAW_LINE);
   SetIndexBuffer(0,Buffer_WP1);
   SetIndexStyle(1,DRAW_LINE);
   SetIndexBuffer(1,Buffer_WP2);
   SetIndexStyle(2,DRAW_LINE);
   SetIndexBuffer(2,Buffer_WP3);
   
   //SetIndexStyle(3,DRAW_LINE);
   //SetIndexBuffer(3,Buffer_WP4);
   //SetIndexStyle(4,DRAW_LINE);
   //SetIndexBuffer(4,Buffer_WP5);
   //SetIndexStyle(5,DRAW_LINE);
   //SetIndexBuffer(5,Buffer_Durchschnitt);
//----
   SetIndexEmptyValue(0,0.0);
   SetIndexEmptyValue(1,0.0);

   SetIndexDrawBegin(0,100);
   SetIndexDrawBegin(1,100);
   SetIndexDrawBegin(2,100);
   //SetIndexDrawBegin(3,Left_Bars);
   //SetIndexDrawBegin(4,Left_Bars);
   //SetIndexDrawBegin(5,Left_Bars);
//----
   
   switch(Einstellungs_Number)
     {
      case 1 : Periode_X = 8;  Value_X = 1.5;  Over_Under = 1;   break;
      case 2 : Periode_X = 21; Value_X = 1.23; Over_Under = 1;   break;
      case 3 : Periode_X = 8;  Value_X = 2.0;  Over_Under = 1;   break;
      case 4 : Periode_X = 34; Value_X = 1.5;  Over_Under = 1;   break;
      
      case 5 : Periode_X = 8;  Value_X = 0.5;  Over_Under = 2;   break;
      case 6 : Periode_X = 21; Value_X = 0.72; Over_Under = 2;   break;
      case 7 : Periode_X = 8;  Value_X = 0.38; Over_Under = 2;   break;
      case 8 : Periode_X = 34; Value_X = 0.68; Over_Under = 2;   break;
     }


   return;
  }
//+------------------------------------------------------------------+
//| Bollinger Bands                                                  |
//+------------------------------------------------------------------+
void start()
  {
   double d1=0, d2=0, d3=0, d4=0, ma=0, atr=0, sum=0, sum2=0, h=0, l=9999999;
//----
   int counted_bars=IndicatorCounted();
   int limit=Bars-counted_bars;
   if(counted_bars>0) limit++;
   //int c=0, d=0, e=1;
   
   for(int i=1; i<limit; i++)
     {
      sum = 0; sum2=0;
      
      
      for (int j=1; j<=Periode_X; j++)
        {
         sum  += High[i+j]-Low[i+j];
         sum2 += MathAbs(Close[i+j]-Open[i+j]);
        }
      
      sum = sum-sum2;
      //sum2 = sum/r;
      
      if(sum2>0 && Over_Under == 1)
          {
           if(sum/sum2 > Value_X)
           Buffer_WP1[i] = 1;
           else
           Buffer_WP1[i] = 0;
          }
      
      if(sum2>0 && Over_Under == 2)
          {
           if(sum/sum2 < Value_X)
           Buffer_WP1[i] = 1;
           else
           Buffer_WP1[i] = 0;
          }
      
      //Buffer_WP2[i]=sum2;
      //Buffer_WP3[i]=ma-sum-sum2;
     }    
     
   return;
  }


//+------------------------------------------------------------------+