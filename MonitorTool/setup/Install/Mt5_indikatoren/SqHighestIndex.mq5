//+------------------------------------------------------------------+
//|                                               SqHighestIndex.mq5 |
//|                           Copyright © 2017, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright   "Copyright © 2017, StrategyQuant s.r.o."
#property link        "http://www.strategyquant.com"
#property description "SqHighestIndex"

#property indicator_chart_window
#property indicator_buffers 1
#property indicator_plots 1

#property indicator_label1  "HighestIndex"
#property indicator_type1  DRAW_LINE
#property indicator_color1 Red

//--- input parameters
input int InpPeriod=14; // Period
input int InpPrice=2;
//---- buffers
double    ExtBuffer[];
//+------------------------------------------------------------------+
//| Custom indicator initialization function                         |
//+------------------------------------------------------------------+

int period, mode;

void OnInit()
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
//--- indicator short name
   string short_name="HighestIndex("+string(period)+")";
   IndicatorSetString(INDICATOR_SHORTNAME,short_name);
//---- end of initialization function
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
//--- checking for bars count
   if(rates_total<period)
      return(0);
//--- detect start position
   int start;
   if(prev_calculated>1) start=prev_calculated-1;
   else
     {
      start=1;
      ExtBuffer[0]=0.0;
     }
  
//--- main cycle
   for(int i=start;i<rates_total && !IsStopped();i++)
   {
      if(i < period - 1){
         ExtBuffer[i] = 0.0;
      }
      else {
         int highestIndex = 0;
         double highestValue = -1;
         
         for(int a=i-period + 1; a<=i; a++){
            double value = getValue(open, high, low, close, mode, a);
          
            if(value - highestValue > 0.00000001){         //protection against imprecise double values
               highestIndex = i - a;
               highestValue = value;
            }
         }
            
         ExtBuffer[i] = highestIndex;
      }
   }
//---- OnCalculate done. Return new prev_calculated.
   return(rates_total);
  }
//+------------------------------------------------------------------+

double getValue(const double &open[],
                const double &high[],
                const double &low[],
                const double &close[],
                int priceMode,
                int index)
{
   switch(priceMode){
      case PRICE_OPEN: return open[index];
      case PRICE_HIGH: return high[index];
      case PRICE_LOW: return low[index];
      case PRICE_CLOSE: return close[index];
      case PRICE_MEDIAN: return (high[index] + low[index]) / 2;
      case PRICE_TYPICAL: return (high[index] + low[index] + close[index]) / 3;
      case PRICE_WEIGHTED: return (high[index] + low[index] + close[index] + close[index]) / 4;
      default: return 0;
   }             
}