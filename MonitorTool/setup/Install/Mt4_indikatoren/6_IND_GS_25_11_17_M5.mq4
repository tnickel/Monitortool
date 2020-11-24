
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
int counter=0;
int bcounter=0;
//---- indicator parameters


//---- buffers
double Buffer_Entry_Long[];
double Buffer_Entry_Short[];
double Buffer_Exit_Long[];
double Buffer_Exit_Short[];
//double Buffer_WP5[];
//double Buffer_Durchschnitt[];
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
void init()
  {
//---- indicators
   SetIndexStyle(0,DRAW_LINE);
   SetIndexBuffer(0,Buffer_Entry_Long);
   SetIndexStyle(1,DRAW_LINE);
   SetIndexBuffer(1,Buffer_Entry_Short);
   SetIndexStyle(2,DRAW_LINE);
   SetIndexBuffer(2,Buffer_Exit_Long);
   SetIndexStyle(3,DRAW_LINE);
   SetIndexBuffer(3,Buffer_Exit_Short);
   
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
   
   double ia1=iATR(NULL,60,12,1);
   double ia2=iATR(NULL,60,120,1);
   
   
   
   for(int i=1; i<=1; i++)
     {
      
      if(ia2==0) return false;
      
      if( (Entry_Long())
      &&  (iATR(NULL,0,5,1) >= 2.5*PointX())
      &&  (iATR(NULL,0,5,1) <= 9.5*PointX())
      &&  (ia1/ia2) >= 0.84) 
      Buffer_Entry_Long[i] = 1;
      else
      Buffer_Entry_Long[i] = 0;
      
      if( (Entry_Short())
      &&  (iATR(NULL,0,5,1) >= 2.5*PointX())
      &&  (iATR(NULL,0,5,1) <= 9.5*PointX())
      &&  ((ia1/ia2) >= 0.84)  )
      Buffer_Entry_Short[i] = 1;
      else
      Buffer_Entry_Short[i] = 0;
      
      if(Exit_Long())
      Buffer_Exit_Long[i] = 1;
      else
      Buffer_Exit_Long[i] = 0;
      
      if(Exit_Short())
      Buffer_Exit_Short[i] = 1;
      else
      Buffer_Exit_Short[i] = 0;
      
     }    
     
   return;
  }


//+------------------------------------------------------------------+



bool Entry_Long()
  {
  
   double bb1 = iBands(NULL,0,21,3.9,0,PRICE_MEDIAN,MODE_LOWER,1);
   double bb2 = iBands(NULL,0,21,2,0,PRICE_MEDIAN,MODE_LOWER,1);
   double bb3 = iBands(NULL,0,13,2.0,0,PRICE_MEDIAN,MODE_LOWER,1);
  
   double bb = (bb1+bb2+bb3)/3;
   
   //Print("close1="+Close[1]+" < 1 bb="+bb);
   
 
   if (Close[1] < bb)
  
       return(true);
  
   
   return(false);
  }


bool Entry_Short()
  {
   double bb1 = iBands(NULL,0,21,3.9,0,PRICE_MEDIAN,MODE_UPPER,1);
   double bb2 = iBands(NULL,0,21,2,0,PRICE_MEDIAN,MODE_UPPER,1);
   double bb3 = iBands(NULL,0,13,2.0,0,PRICE_MEDIAN,MODE_UPPER,1);
  
   double bb = (bb1+bb2+bb3)/3;
   //Print("close1="+Close[1]+" <  2 bb="+bb);
   if (Close[1] > bb)
  
       return(true);
  
   return(false);
  }


bool Exit_Short()
  {
   double bb1 = iBands(NULL,0,21,3.0,0,PRICE_MEDIAN,MODE_LOWER,1);
   double bb2 = iBands(NULL,0,21,2,0,PRICE_MEDIAN,MODE_LOWER,1);
   double bb3 = iBands(NULL,0,13,2.0,0,PRICE_MEDIAN,MODE_LOWER,1);
  
 
  
   double bb = (bb1+bb2+bb3)/3;
    //Print("close1="+Close[1]+" < 3 bb="+bb);
   if (Close[1] < bb)
  
       return(true);
  
   
   return(false);
  }


bool Exit_Long()
  {
   double bb1 = iBands(NULL,0,21,3.0,0,PRICE_MEDIAN,MODE_UPPER,1);
   double bb2 = iBands(NULL,0,21,2,0,PRICE_MEDIAN,MODE_UPPER,1);
   double bb3 = iBands(NULL,0,13,2.0,0,PRICE_MEDIAN,MODE_UPPER,1);
  
   double bb = (bb1+bb2+bb3)/3;
    //Print("close1="+Close[1]+" < 4 bb="+bb);
   if (Close[1] > bb)
   
   return(true);
   
   return(false);
  }
  

double PointX()
  {
   if (Digits==5 || Digits ==3)

   return(Point*10); else return(Point);
  }