//+------------------------------------------------------------------+
//|                                       Index Komercnich Proxy.mq5 |
//|                                          Copyright 2018, ZephyCZ |
//|                                                                  |
//+------------------------------------------------------------------+
#property copyright "Copyright 2018, ZephyCZ"
#property link      "https://www.mql5.com"
#property version   "1.00"
#property indicator_separate_window
#property indicator_buffers 4
#property indicator_plots   1

//--- plot IKP
#property indicator_label1  "IKP"
#property indicator_type1   DRAW_LINE
#property indicator_color1  clrTurquoise
#property indicator_style1  STYLE_SOLID
#property indicator_width1  1

//--- levels
#property indicator_minimum    -5
#property indicator_maximum   105
#property indicator_level1     25
#property indicator_level2     75
#property indicator_levelcolor Orange
#property indicator_levelstyle 2
#property indicator_levelwidth 1

//--- include libraries
#include <MovingAverages.mqh>

//--- input parameters
input int      perioda_indexu=200;
input int      cotperioda=40;
input int      vyhlazeni=3;

//--- indicator buffers
double         IKPBuffer[];
double         OCBuffer[];
double         Value1Buffer[];
double         Value2Buffer[];

//--- indicator handles
int       ATRhandle;

//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+
int OnInit()
  {
//--- indicator buffers mapping
   SetIndexBuffer(0,IKPBuffer,INDICATOR_DATA);
   SetIndexBuffer(1,OCBuffer,INDICATOR_CALCULATIONS);
   SetIndexBuffer(2,Value1Buffer,INDICATOR_CALCULATIONS);
   SetIndexBuffer(3,Value2Buffer,INDICATOR_CALCULATIONS);

//--- bar, starting from which the indicator is drawn
   PlotIndexSetInteger(0,PLOT_DRAW_BEGIN,cotperioda+1);

//--- set a label do display in DataWindow
   string shortname;
   StringConcatenate(shortname,"IndexKomercnichProxy(",perioda_indexu,",",cotperioda,",",vyhlazeni,")");
   PlotIndexSetString(0,PLOT_LABEL,shortname);   

//--- set a name to show in a separate sub-window or a pop-up help
   IndicatorSetString(INDICATOR_SHORTNAME,shortname);

//--- set accuracy of displaying the indicator values
   IndicatorSetInteger(INDICATOR_DIGITS,5);

//--- get handles
   ATRhandle=iATR(Symbol(),0,cotperioda);
//---
   
//---
   return(INIT_SUCCEEDED);
  }
  
//+------------------------------------------------------------------+
//| Custom indicator iteration function                              |
//+------------------------------------------------------------------+
int OnCalculate(const int rates_total,
                const int prev_calculated,
                const datetime &time[],
                const double &open[],
                const double &high[],
                const double &low[],
                const double &close[],
                const long &tick_volume[],
                const long &volume[],
                const int &spread[])
  {
   //======================================================================
   //--- Nastaveni pozice a inicializace buffer hodnot
   //======================================================================
   int pos;
   if(prev_calculated<=0)
   {
      pos=0;
      ArrayInitialize(OCBuffer,0.0);
      ArrayInitialize(Value1Buffer,0.0);
      ArrayInitialize(Value2Buffer,0.0);
   }  
   else pos=prev_calculated-1;
     
   //======================================================================
   // Hlavni cyklus pro vypocet
   //======================================================================
   for(int i=pos;i<rates_total && !IsStopped();i++)
   {
      //vypocet OC
      OCBuffer[i] = (open[i] - close[i]);
      
      //soucet OC za cotperioda
      double soc = fSUMValueFromOCBuffer(i, cotperioda);
      
      double atrBuffer[1];
      if(CopyBuffer(ATRhandle,0,time[i],1,atrBuffer)
      && cotperioda != 0 && atrBuffer[0] != 0)
      {
         Value1Buffer[i] = (((soc/cotperioda) / atrBuffer[0])*100);         
      }
      
      //======================================================================
      // Vypocet INDEX hodnoty z vyslednych cisel Value1Buffer
      //======================================================================    
      //--- maximalni a minimalni hodnota z vypocitanych hodnot
      double max_value1 = GetHighestValueFromValue1Buffer(i);
      double min_value1 = GetLowestValueFromValue1Buffer(i);

      double index_bez_sma = 0.0;

      if((max_value1 - min_value1) != 0)  //ochrana deleni nulou
      {
         //vypocet hodnoty v procentech
          index_bez_sma = 100 * (Value1Buffer[i] - min_value1) / (max_value1 - min_value1);
      }
      
      Value2Buffer[i] = index_bez_sma;
      
      //======================================================================
      // Aplikace SMA na vyslednou hodnotu v procentech
      //======================================================================
      IKPBuffer[i] = SimpleMA(i, vyhlazeni, Value2Buffer);
   }
   
   //--- return value of prev_calculated for next call
   return(rates_total);
  }

//+------------------------------------------------------------------+
//+ Soucet hodnot OCBuffer v zadanem rozmezi
//+------------------------------------------------------------------+
double fSUMValueFromOCBuffer(const int aktPos, const int perioda)
{
   double result = 0.0;
   int zacatek = (aktPos - perioda);
   
   if(zacatek < 0)
      zacatek = 0;
   
   for(int i=zacatek;i<=aktPos;i++)
   {
      result += OCBuffer[i];
   }
   
   return result;
}

double GetHighestValueFromValue1Buffer(int pos)
{
   double result = 0.0; 
   
   int zacatek = (pos - perioda_indexu + 1);
   if(zacatek < 0) zacatek = 0;
   
   int konec = perioda_indexu;
   if(konec > pos) konec = (pos+1);
   
   int index = ArrayMaximum(Value1Buffer,zacatek,konec);

   if(index >=0)
   {
     result = Value1Buffer[index];
   }
   
   return result;
}

double GetLowestValueFromValue1Buffer(int pos)
{
   double result = 0.0;  

   int zacatek = (pos - perioda_indexu + 1);
   if(zacatek < 0) zacatek = 0;   

   int konec = perioda_indexu;
   if(konec > pos) konec = (pos+1);

   int index = ArrayMinimum(Value1Buffer,zacatek,konec);
   

   if(index >=0)
   {
     result = Value1Buffer[index];
   }
   
   return result;
}