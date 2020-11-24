//+------------------------------------------------------------------+
//|   Qualitative Quantitative Estimation Indicator for Metatrader 4 |
//|                                     Copyright © 2006Roman Ignatov |
//|                                   mailto:roman.ignatov@gmail.com |
//+------------------------------------------------------------------+
#property copyright "Copyright © 200 Roman Ignatov"
#property link      "mailto:roman.ignatov@gmail.com"

#property indicator_separate_window

#property indicator_buffers 2

#property indicator_color1 Navy
#property indicator_style1 STYLE_SOLID
#property indicator_width1 2

#property indicator_color2 Red
#property indicator_style2 STYLE_DOT


extern int RSI_Period = 14;
extern int SF = 5;
extern double WF = 4.236;

int Wilders_Period;
int StartBar;

double TrLevelSlow[];
double AtrRsi[];
double MaAtrRsi[];
double Rsi[];
double RsiMa[];

int init()
{
    Wilders_Period = RSI_Period * 2 - 1;
    if (Wilders_Period < SF)
        StartBar = SF;
    else
        StartBar = Wilders_Period;

    IndicatorBuffers(6);
    SetIndexStyle(0, DRAW_LINE, STYLE_SOLID, 2);
    SetIndexLabel(0, "Value 1");
    SetIndexDrawBegin(0, StartBar);
    SetIndexStyle(1, DRAW_LINE, STYLE_DOT);
    SetIndexLabel(1, "Value 2");
    SetIndexDrawBegin(1, StartBar);
    IndicatorShortName(StringConcatenate("QQE(", SF, ")"));

    SetIndexBuffer(0, RsiMa);
    SetIndexBuffer(1, TrLevelSlow);
    SetIndexBuffer(2, Rsi);
    SetIndexBuffer(3, AtrRsi);
    SetIndexBuffer(4, MaAtrRsi);

    return(0);
}


int start()
{
    int counted, i;
    double rsi0, rsi1, dar, tr, dv;

    if(Bars <= StartBar)
        return (0);

    counted = IndicatorCounted();
    if(counted < 1)
        for(i = Bars - StartBar; i < Bars; i++)
        {
            TrLevelSlow[i] = 0.0;
            AtrRsi[i] = 0.0;
            MaAtrRsi[i] = 0.0;
            Rsi[i] = 0.0;
            RsiMa[i] = 0.0;
        }

    counted = Bars - counted - 1;

    for (i = counted; i >= 0; i--)
        Rsi[i] = iRSI(NULL, 0, RSI_Period, PRICE_CLOSE, i);

    for (i = counted; i >= 0; i--)
    {
        RsiMa[i] = iMAOnArray(Rsi, 0, SF, 0, MODE_EMA, i);
        AtrRsi[i] = MathAbs(RsiMa[i + 1] - RsiMa[i]);
    }

    for (i = counted; i >= 0; i--)
        MaAtrRsi[i] = iMAOnArray(AtrRsi, 0, Wilders_Period, 0, MODE_EMA, i);

    i = counted + 1;
    tr = TrLevelSlow[i];
    rsi1 = iMAOnArray(Rsi, 0, SF, 0, MODE_EMA, i);
    while (i > 0)
    {
        i--;
        rsi0 = iMAOnArray(Rsi, 0, SF, 0, MODE_EMA, i);
        dar = iMAOnArray(MaAtrRsi, 0, Wilders_Period, 0, MODE_EMA, i) * WF; //* 4.236;

        dv = tr;
        if (rsi0 < tr)
        {
            tr = rsi0 + dar;
            if (rsi1 < dv)
                if (tr > dv)
                    tr = dv;
        }
        else if (rsi0 > tr)
        {
            tr = rsi0 - dar;
            if (rsi1 > dv)
                if (tr < dv)
                    tr = dv;
        }
        TrLevelSlow[i] = tr;
        rsi1 = rsi0;
    }

    return(0);
}