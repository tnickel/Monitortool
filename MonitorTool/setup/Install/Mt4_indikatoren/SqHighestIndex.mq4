//+------------------------------------------------------------------+
//|                                               SqHighestIndex.mq4 |
//|                                    Copyright 2017, StrategyQuant |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright   "2017, StrategyQuant"
#property link        "http://www.strategyquant.com"
#property description "SqHighestIndex"

#property indicator_separate_window
#property indicator_buffers 1

//--- input parameters
input int InpPeriod=14; // Period
input int InpPrice=2;
//---- buffers
double    ExtBuffer[];
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+

int period, mode;

int OnInit()
  {
//--- check for input parameters
   if(InpPeriod<=0)
     {
      printf("Incorrect value for input variable InpPeriod=%d. Indicator will use value=%d for calculations.",InpPeriod,14);
      period=14;
     }
   else period = InpPeriod;
   
   switch(InpPrice){
      case PRICE_OPEN:
      case PRICE_HIGH:
      case PRICE_LOW:
      case PRICE_CLOSE:
      case PRICE_MEDIAN:
      case PRICE_TYPICAL:
      case PRICE_WEIGHTED:
         mode = InpPrice;
         break;
      default:
         printf("Incorrect value for input variable InpPrice=%d. Indicator will use value PRICE_HIGH for calculations.",InpPrice);
         mode=PRICE_HIGH;
   }
   
//---- indicator buffers
   SetIndexBuffer(0,ExtBuffer);
   ArraySetAsSeries(ExtBuffer, false);
//--- indicator short name
   string short_name="HighestIndex("+string(period)+")";
   IndicatorSetString(INDICATOR_SHORTNAME,short_name);
//---- end of initialization function
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
  
  ArraySetAsSeries(time, false);
  ArraySetAsSeries(open, false);
  ArraySetAsSeries(high, false);
  ArraySetAsSeries(low, false);
  ArraySetAsSeries(close, false);
  ArraySetAsSeries(tick_volume, false);
  ArraySetAsSeries(volume, false);
  ArraySetAsSeries(spread, false);
  
//--- detect start position
   int startIndex;
   if(prev_calculated>1) startIndex=prev_calculated-1;
   else
     {
      startIndex=0;
      ExtBuffer[0]=0.0;
     }
//--- main cycle
   for(int i=startIndex;i<rates_total && !IsStopped();i++)
   {
      int periodToUse = MathMin(i+1, period);
      int highestIndex = 0;
      double highestValue = -1;
      
      for(int a=i-periodToUse + 1; a<=i; a++){
         double value;
         
         switch(mode){
            case PRICE_OPEN:
               value = open[a];
               break;
            case PRICE_HIGH:
               value = high[a];
               break;
            case PRICE_LOW:
               value = low[a];
               break;
            case PRICE_CLOSE:
               value = close[a];
               break;
            case PRICE_MEDIAN:
               value = (high[a] + low[a]) / 2;
               break;
            case PRICE_TYPICAL:
               value = (high[a] + low[a] + close[a]) / 3;
               break;
            case PRICE_WEIGHTED:
               value = (high[a] + low[a] + close[a] + close[a]) / 4;
               break;
         }
       
         if(value - highestValue > 0.00000001){         //protection against imprecise double values
            highestIndex = i - a;
            highestValue = value;
         }
      }
      ExtBuffer[i] = highestIndex;
   }
//---- OnCalculate done. Return new prev_calculated.
   return(rates_total);
  }
//+------------------------------------------------------------------+
