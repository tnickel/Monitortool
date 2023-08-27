//+------------------------------------------------------------------+
//|   Qualitative Quantitative Estimation Indicator for Metatrader 5 |
//|                            Copyright © 2017 StrategyQuant s.r.o. |
//|                                     http://www.strategyquant.com |
//+------------------------------------------------------------------+
#property copyright "Copyright © 2017 StrategyQuant s.r.o."
#property link      "http://www.strategyquant.com"

#property indicator_separate_window
#property indicator_plots 6
#property indicator_buffers 6

#property indicator_label1 "Value 1"
#property indicator_color1 Navy
#property indicator_style1 STYLE_SOLID
#property indicator_type1 DRAW_LINE
#property indicator_width1 2

#property indicator_label2 "Value 2"
#property indicator_color2 Red
#property indicator_style2 STYLE_DOT
#property indicator_type2 DRAW_LINE

input int RSI_Period = 14;
input int SF = 5;
input double WF = 4.236;

int Wilders_Period;
int StartBar;

int rsiHandle;

double TrLevelSlow[];
double AtrRsi[];
double MaAtrRsi[];
double Rsi[];
double RsiMa[];
double MaAtrRsiWP[];

void OnInit()
{
    Wilders_Period = RSI_Period * 2 - 1;
    if (Wilders_Period < SF)
        StartBar = SF;
    else
        StartBar = Wilders_Period;
        
    ArraySetAsSeries(RsiMa, true);
    ArraySetAsSeries(TrLevelSlow, true);
    ArraySetAsSeries(Rsi, true);
    ArraySetAsSeries(AtrRsi, true);
    ArraySetAsSeries(MaAtrRsi, true);
    ArraySetAsSeries(MaAtrRsiWP, true);
    
    SetIndexBuffer(0, RsiMa);
    SetIndexBuffer(1, TrLevelSlow);
    SetIndexBuffer(2, Rsi);
    SetIndexBuffer(3, AtrRsi);
    SetIndexBuffer(4, MaAtrRsi);
    SetIndexBuffer(5, MaAtrRsiWP);
    
    rsiHandle = iRSI(NULL, 0, RSI_Period, PRICE_CLOSE);
    
    IndicatorSetString(INDICATOR_SHORTNAME, "QQE(" + IntegerToString(SF) + ")");
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
    int counted, i;
    double rsi0, rsi1, dar, tr, dv;
    
    if(rates_total <= StartBar) return(0);
    
    if(prev_calculated == 0){
        for(i = rates_total - 1; i >= 0; i--){
            RsiMa[i] = 0.0;
            TrLevelSlow[i] = 0.0;
            Rsi[i] = 0.0;
            AtrRsi[i] = 0.0;
            MaAtrRsi[i] = 0.0;
            MaAtrRsiWP[i] = 0.0;
        }
    }
    
    counted = rates_total - (prev_calculated == 0 ? 2 : prev_calculated);
        
    for (i = counted; i >= 0; i--){
        Rsi[i] = getIndicatorValue(rsiHandle, 0, i);
        RsiMa[i] = Rsi[i];
    }
    
    for (i = counted; i >= 0; i--){
        RsiMa[i] = RsiMa[i] * (2.0 / (1 + SF)) + (1 - (2.0 / (1 + SF))) * RsiMa[i + 1];
        AtrRsi[i] = MathAbs(RsiMa[i + 1] - RsiMa[i]);
        MaAtrRsi[i] = AtrRsi[i];
    }

    for (i = counted; i >= 0; i--){
        MaAtrRsi[i] = MaAtrRsi[i] * (2.0 / (1 + Wilders_Period)) + (1 - (2.0 / (1 + Wilders_Period))) * MaAtrRsi[i + 1];
        MaAtrRsiWP[i] = MaAtrRsi[i];
    }

    i = counted;
    tr = TrLevelSlow[i + 1];
    rsi1 = RsiMa[i + 1];
    
    while (i >= 0){
        rsi0 = RsiMa[i];
        MaAtrRsiWP[i] = MaAtrRsiWP[i] * (2.0 / (1 + Wilders_Period)) + (1 - (2.0 / (1 + Wilders_Period))) * MaAtrRsiWP[i + 1];
        dar = MaAtrRsiWP[i] * WF;
        
        dv = tr;
        if (rsi0 < tr){
            tr = rsi0 + dar;
            if (rsi1 < dv)
                if (tr > dv)
                    tr = dv;
        }
        else if (rsi0 > tr){
            tr = rsi0 - dar;
            if (rsi1 > dv)
                if (tr < dv)
                    tr = dv;
        }
        TrLevelSlow[i] = tr;
        rsi1 = rsi0;
        
        i--;
    }
    
    return(rates_total);
}

double getIndicatorValue(int indyHandle, int bufferIndex, int shift){
   double buffer[];
   
   if(CopyBuffer(indyHandle, bufferIndex, shift, 1, buffer) < 0) { 
      //--- if the copying fails, tell the error code 
      PrintFormat("Failed to copy data from the indicator, error code %d", GetLastError()); 
      //--- quit with zero result - it means that the indicator is considered as not calculated 
      return(0); 
   } 
   
   double val = buffer[0];
   return (val >= 0 && val <= 100) ? val : 0;
}