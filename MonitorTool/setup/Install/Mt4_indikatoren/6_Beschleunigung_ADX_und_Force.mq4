
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
   
   /*
   if(sum/sum2 > Value_X)
   Buffer_WP1[i] = 1;
   else
   Buffer_WP1[i] = 0;
   */
   
   
   if(Einstellungs_Number == 1)
       {
        if(ADX_Beschleunigung()==true && ATR_Zusatz()==true)
        Buffer_WP1[1] = 1;
        else
        Buffer_WP1[1] = 0;
       }
      
   if(Einstellungs_Number == 2)
       {
        if(ADX_Beschleunigung_2()==true && ATR_Zusatz()==true)
        Buffer_WP1[1] = 1;
        else
        Buffer_WP1[1] = 0;
       }
   
   if(Einstellungs_Number == 3)
       {
        if(ADX_Beschleunigung_3()==true && ATR_Zusatz()==true)
        Buffer_WP1[1] = 1;
        else
        Buffer_WP1[1] = 0;
       }
   
   if(Einstellungs_Number == 4)
       {
        if(ADX_Beschleunigung_5()==true && ATR_Zusatz()==true)
        Buffer_WP1[1] = 1;
        else
        Buffer_WP1[1] = 0;
       }
   
   if(Einstellungs_Number == 5)
       {
        if(ADX_Beschleunigung_6()==true && ATR_Zusatz()==true)
        Buffer_WP1[1] = 1;
        else
        Buffer_WP1[1] = 0;
       }
   
   
   if(Einstellungs_Number == 6)
       {
        if(Force_Beschleunigung()==true && ATR_Zusatz()==true)
        Buffer_WP1[1] = 1;
        else
        Buffer_WP1[1] = 0;
       }
   
   if(Einstellungs_Number == 7)
       {
        if(Force_Beschleunigung_2()==true && ATR_Zusatz()==true)
        Buffer_WP1[1] = 1;
        else
        Buffer_WP1[1] = 0;
       }
   
   if(Einstellungs_Number == 8)
       {
        if(Force_Beschleunigung_3()==true && ATR_Zusatz()==true)
        Buffer_WP1[1] = 1;
        else
        Buffer_WP1[1] = 0;
       }
    
   return;
  }


//+------------------------------------------------------------------+



bool ADX_Beschleunigung()
  {
   
   double sum_a = 0;
   double sum_b = 0;
   
   
   for(int i=1;i<=5;i++)
     {
      sum_a += iADX(NULL,0,13,PRICE_CLOSE,MODE_MAIN,i);
     }
   
   for(int j=6;j<=21;j++)
     {
      sum_b += iADX(NULL,0,13,PRICE_CLOSE,MODE_MAIN,j);
     }
   
   sum_a = sum_a/5;
   sum_b = sum_b/16;
   
   if(sum_b == 0)
   return(false);
   
   if(sum_a/sum_b >= 1.27)
   
   return(true);
   
   return(false);
  }



bool ADX_Beschleunigung_2()
  {
   
   double sum_a = 0;
   double sum_b = 0;
   
   
   for(int i=1;i<=8;i++)
     {
      sum_a += iADX(NULL,0,13,PRICE_CLOSE,MODE_MAIN,i);
     }
   
   for(int j=9;j<=34;j++)
     {
      sum_b += iADX(NULL,0,13,PRICE_CLOSE,MODE_MAIN,j);
     }
   
   sum_a = sum_a/8;
   sum_b = sum_b/26;
   
   if(sum_b == 0)
   return(false);
   
   if(sum_a/sum_b <= 0.83 && sum_a/sum_b >= 0.53)
   
   return(true);
   
   return(false);
  }



bool ADX_Beschleunigung_3()
  {
   
   double sum_a = 0;
   double sum_b = 0;
   
   
   for(int i=1;i<=8;i++)
     {
      sum_a += iADX(NULL,0,21,PRICE_CLOSE,MODE_MAIN,i);
     }
   
   for(int j=9;j<=34;j++)
     {
      sum_b += iADX(NULL,0,21,PRICE_CLOSE,MODE_MAIN,j);
     }
   
   sum_a = sum_a/8;
   sum_b = sum_b/26;
   
   if(sum_b == 0)
   return(false);
   
   if(sum_a/sum_b <= 0.95)
   
   return(true);
   
   return(false);
  }



bool ADX_Beschleunigung_5()
  {
   
   double sum_a = 0;
   double sum_b = 0;
   
   
   for(int i=1;i<=8;i++)
     {
      sum_a += iADX(NULL,0,34,PRICE_CLOSE,MODE_MAIN,i);
     }
   
   for(int j=9;j<=34;j++)
     {
      sum_b += iADX(NULL,0,34,PRICE_CLOSE,MODE_MAIN,j);
     }
   
   sum_a = sum_a/8;
   sum_b = sum_b/26;
   
   if(sum_b == 0)
   return(false);
   
   if(sum_a/sum_b <= 0.90)
   
   return(true);
   
   return(false);
  }



bool ADX_Beschleunigung_6()
  {
   
   double sum_a = 0;
   double sum_b = 0;
   
   
   for(int i=1;i<=13;i++)
     {
      sum_a += iADX(NULL,0,8,PRICE_CLOSE,MODE_MAIN,i);
     }
   
   for(int j=14;j<=55;j++)
     {
      sum_b += iADX(NULL,0,8,PRICE_CLOSE,MODE_MAIN,j);
     }
   
   sum_a = sum_a/13;
   sum_b = sum_b/42;
   
   if(sum_b == 0)
   return(false);
   
   if(sum_a/sum_b <= 0.75 && sum_a/sum_b >= 0.56)
   
   return(true);
   
   return(false);
  }



bool Force_Beschleunigung()
  {
   
   double sum_a = 0;
   double sum_b = 0;
   
   
   for(int i=1;i<=8;i++)
     {
      sum_a += MathAbs(iForce(NULL,0,13,MODE_SMA,PRICE_CLOSE,i));
     }
   
   for(int j=9;j<=21;j++)
     {
      sum_b += MathAbs(iForce(NULL,0,13,MODE_SMA,PRICE_CLOSE,j));
     }
   
   sum_a = sum_a/8;
   sum_b = sum_b/13;
   
   if(sum_b == 0)
   return(false);
   
   if(sum_a/sum_b <= 0.34)
   
   return(true);
   
   return(false);
  }


bool Force_Beschleunigung_2()
  {
   
   double sum_a = 0;
   double sum_b = 0;
   
   
   for(int i=1;i<=8;i++)
     {
      sum_a += MathAbs(iForce(NULL,0,8,MODE_SMA,PRICE_CLOSE,i));
     }
   
   for(int j=9;j<=21;j++)
     {
      sum_b += MathAbs(iForce(NULL,0,8,MODE_SMA,PRICE_CLOSE,j));
     }
   
   sum_a = sum_a/8;
   sum_b = sum_b/13;
   
   if(sum_b == 0)
   return(false);
   
   if(sum_a/sum_b <= 0.45)
   
   return(true);
   
   return(false);
  }


bool Force_Beschleunigung_3()
  {
   
   double sum_a = 0;
   double sum_b = 0;
   
   
   for(int i=1;i<=5;i++)
     {
      sum_a += MathAbs(iForce(NULL,0,5,MODE_SMA,PRICE_CLOSE,i));
     }
   
   for(int j=6;j<=13;j++)
     {
      sum_b += MathAbs(iForce(NULL,0,5,MODE_SMA,PRICE_CLOSE,j));
     }
   
   sum_a = sum_a/5;
   sum_b = sum_b/8;
   
   if(sum_b == 0)
   return(false);
   
   if(sum_a/sum_b <= 1.05)
   
   return(true);
   
   return(false);
  }




bool ATR_Zusatz()
  {
   //return(true);
   
   if(iATR(NULL,60,120,1) == 0)  return(true);
   
   if(iATR(NULL,60,12,1)/iATR(NULL,60,120,1) > 0.84)
   
   return(true);
   
   return(false);
  }