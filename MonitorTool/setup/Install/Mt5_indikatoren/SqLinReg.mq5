//+------------------------------------------------------------------+
//|                                                       linreg.mq5 |
//|                           Copyright © @2017 StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property  copyright "Copyright © @2017 StrategyQuant s.r.o."
#property  link      "http://www.strategyquant.com"

#property indicator_chart_window
#property indicator_buffers 1
#property indicator_plots 1

#property indicator_label1  "SqLinReg"
#property indicator_type1  DRAW_LINE
#property indicator_color1 Blue

input int LRPeriod=14;
input int InpPrice=2;

int period, mode;

double ind_buffer[];

void OnInit()
  {
   if(LRPeriod <= 0){
      printf("Incorrect value for input variable LRPeriod=%d. Indicator will use value=%d for calculations.", LRPeriod, 14);
      period = 14;
   }
   else period = LRPeriod;
   
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
         mode=14;
   }
   
   ArraySetAsSeries(ind_buffer, true);
   
   SetIndexBuffer(0, ind_buffer);
   
//--- indicator short name
   string short_name="SqLinReg("+string(period)+")";
   IndicatorSetString(INDICATOR_SHORTNAME,short_name);
//---- end of initialization function
}
  
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
   
   if(rates_total < period) return(0);
   
   int limit;
   
   ArraySetAsSeries(time, true);
   ArraySetAsSeries(open, true);
   ArraySetAsSeries(high, true);
   ArraySetAsSeries(low, true);
   ArraySetAsSeries(close, true);
   
   if(prev_calculated > 0) limit = rates_total - prev_calculated + 1;
   else {
      for(int a=0; a<rates_total; a++){
         ind_buffer[a] = 0.0;
      }
      
      limit = rates_total - period;
   }
   
   for(int i=limit-1; i>=0; i--) {
      ind_buffer[i] = linreg(open, high, low, close, mode, period, i);
   }
   return(rates_total);
  }
//+------------------------------------------------------------------+

double linreg(const double &open[],
              const double &high[],
              const double &low[],
              const double &close[],
              int priceMode,
              int p,
              int i){
              
   double SumY=0;
   double Sum1=0;
   double Slope=0;
   double c;
   
   for (int x=0; x<p; x++) {
      c=getValue(open, high, low, close, priceMode, x+i);
      SumY+=c;
      Sum1+=x*c; 
   }
   
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