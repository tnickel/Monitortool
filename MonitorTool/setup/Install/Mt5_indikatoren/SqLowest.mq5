//+------------------------------------------------------------------+
//|                                                     SqLowest.mq5 |
//|                           Copyright © 2017, StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright   "Copyright © 2017, StrategyQuant s.r.o."
#property link        "http://www.strategyquant.com"
#property description "SqLowest"

#property indicator_chart_window
#property indicator_buffers 1
#property indicator_plots 1

#property indicator_label1  "Lowest"
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
         printf("Incorrect value for input variable InpPrice=%d. Indicator will use value PRICE_LOW for calculations.",InpPrice);
         mode=PRICE_LOW;
   }
   
//---- indicator buffers
   SetIndexBuffer(0,ExtBuffer);
//--- indicator short name
   string short_name="Lowest("+string(period)+")";
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
         double lowestValue = 10000000;
         
         for(int a=i-period+1; a<=i; a++){
            double value = 0;
            
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
          
            if(lowestValue - value > 0.00000001){        //protection against imprecise double values
               lowestValue = value;
            }
         }
            
         ExtBuffer[i] = lowestValue;
      }
   }
//---- OnCalculate done. Return new prev_calculated.
   return(rates_total);
  }
//+------------------------------------------------------------------+
