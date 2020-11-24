
#property copyright "Sebastian Holtz forexsolutions"

#property indicator_separate_window
#property indicator_buffers 4
#property indicator_color1 clrYellow
#property indicator_color2 clrYellow
#property indicator_color3 clrYellow
#property indicator_color4 clrYellow
//#property indicator_color5 Red
//#property indicator_color6 Yellow



extern int Einstellungs_Number = 1;
/*
int Periode_X   = 13;
double Value_X  = 2.0;
int Over_Under  = 1;
*/

//---- indicator parameters


//---- buffers
double Buffer_WP1[];
double Buffer_WP2[];
double Buffer_WP3[];
double Buffer_WP4[];
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
   SetIndexStyle(3,DRAW_LINE);
   SetIndexBuffer(3,Buffer_WP4);
   
   //SetIndexStyle(4,DRAW_LINE);
   //SetIndexBuffer(4,Buffer_WP5);
   //SetIndexStyle(5,DRAW_LINE);
   //SetIndexBuffer(5,Buffer_Durchschnitt);
//----
   SetIndexEmptyValue(0,0.0);
   SetIndexEmptyValue(1,0.0);
   SetIndexEmptyValue(2,0.0);
   SetIndexEmptyValue(3,0.0);

   SetIndexDrawBegin(0,100);
   SetIndexDrawBegin(1,100);
   SetIndexDrawBegin(2,100);
   SetIndexDrawBegin(3,100);
   //SetIndexDrawBegin(4,Left_Bars);
   //SetIndexDrawBegin(5,Left_Bars);
//----
   
   /*
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
      
      case 11 : Periode_X = 5;  Value_X = 1.62;  Over_Under = 2;   break;
     }
   */


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
   
   
   for(int i=1; i<=limit; i++)
     {
      
      if( (iRSI(NULL,0,5,PRICE_CLOSE,1) < 28)
      &&  (iADX(NULL,0,13,PRICE_CLOSE,MODE_MAIN,1)>=20)
      &&  (MathAbs(iCCI(Symbol(),0,21,PRICE_CLOSE,1)) < 250) )
      Buffer_WP1[i] = 1;
      else
      Buffer_WP1[i] = 0;
      
      if( (iRSI(NULL,0,5,PRICE_CLOSE,1) > 72)
      &&  (iADX(NULL,0,13,PRICE_CLOSE,MODE_MAIN,1)>=20)
      &&  (MathAbs(iCCI(Symbol(),0,21,PRICE_CLOSE,1)) < 250) )
      Buffer_WP2[i] = 1;
      else
      Buffer_WP2[i] = 0;
      
      if(iRSI(NULL,0,8,PRICE_CLOSE,1) > 70)
      Buffer_WP3[i] = 1;
      else
      Buffer_WP3[i] = 0;
      
      if(iRSI(NULL,0,8,PRICE_CLOSE,1) < 30)
      Buffer_WP4[i] = 1;
      else
      Buffer_WP4[i] = 0;
      
     }    
     
   return;
  }


//+------------------------------------------------------------------+