//+------------------------------------------------------------------+
//|                               GenBuilder_Pattern_Recognition.mq4 |
//|                                                                  |
//|    Original logic taken from Carl Sanders's (traden4x@gmail.com) |
//|                                    Pattern Recognition Master v3 |
//+------------------------------------------------------------------+
//+------------------------------------------------------------------+
#property copyright "Copyright © 2012 Genetic Builder"
#property link      "http://www.GeneticBuilder.com"

#property indicator_chart_window
#property indicator_buffers 2
#property indicator_color1 Yellow
#property indicator_color2 Yellow

extern int FontSize = 8;
extern color Color_Down = Red;
extern color Color_Up = Green;

//---- buffers
double upArrow[];
double downArrow[];

double atr;
double gPointPow = 0;
double gPointCoef = 0;

//+------------------------------------------------------------------+

int init() {
   atr = 0;
   
   double realDigits;
   if(Digits < 4) { realDigits = 2; } else { realDigits = 4; }

   gPointPow = MathPow(10, realDigits);
   gPointCoef = 1/gPointPow;
   
   SetIndexStyle(0,DRAW_ARROW, EMPTY);
   SetIndexArrow(0,72);
   SetIndexBuffer(0, downArrow);
      
   SetIndexStyle(1,DRAW_ARROW, EMPTY);
   SetIndexArrow(1,71);
   SetIndexBuffer(1, upArrow);
      
   return(0);
}

//+------------------------------------------------------------------+

int deinit() {
   ObjectsDeleteAll(0, OBJ_TEXT);

   return(0);
}

//+------------------------------------------------------------------+

int start() {
   if(atr == 0) {
      atr = iATR(NULL,0,60,1);
   }

   Comment("\n", "\n", "Bearish",
            "\n", "S_S - Shooting Star", 
            "\n", "DCC      - Dark Cloud Pattern",
            "\n", "S_E      - Bearish Engulfing", 
            "\n", "\n", "Bullish",
            "\n", "HMR 2,3,4 - Hammer",
            "\n", "P_L       - Piercing Line",
            "\n", "L_E       - Bullish Engulfing",
            "\n", "\n", "Neutral (possible reversal)",
            "\n", "Doji - Doji"
            );

   for (int shift = 1; shift < Bars; shift++) {
      if(candlePatternBearishEngulfing(shift)) {
         signalDown("S_E", shift);
      }
      if(candlePatternBullishEngulfing(shift)) {
         signalUp("L_E", shift);
      }
      if(candlePatternDarkCloudCover(shift)) {
         signalDown("DCC", shift);
      }
      if(candlePatternDoji(shift)) {
         signalDown("Doji", shift);
      }
      if(candlePatternHammer(shift)) {
         signalUp("HMR", shift);
      }
      if(candlePatternPiercingLine(shift)) {
         signalUp("P_L", shift);
      }
      if(candlePatternShootingStar(shift)) {
         signalDown("S_S", shift);
      }
   }
   
   return(0);
}

//+------------------------------------------------------------------+

void signalDown(string name, int shift) {
   ObjectCreate(getName(name,shift), OBJ_TEXT, 0, Time[shift], High[shift] + atr);
   ObjectSetText(getName(name,shift), name, FontSize, "Times New Roman", Color_Down);
   downArrow[shift] = High[shift] + atr;
}

//+------------------------------------------------------------------+

void signalUp(string name, int shift) {
   ObjectCreate(getName(name,shift), OBJ_TEXT, 0, Time[shift], Low[shift] - atr);
   ObjectSetText(getName(name,shift), name, FontSize, "Times New Roman", Color_Up);
   downArrow[shift] = Low[shift] - atr;
}

//+------------------------------------------------------------------+

string getName(string aName,int shift) {
  return(aName+DoubleToStr(Time[shift],0));
}

//+------------------------------------------------------------------+
//+ Candle Pattern functions
//+------------------------------------------------------------------+

bool candlePatternBearishEngulfing(int shift) {
   double O = Open[shift];
   double O1 = Open[shift+1];
   double C = Close[shift];
   double C1 = Close[shift+1];

   if ((C1>O1)&&(O>C)&&(O>=C1)&&(O1>=C)&&((O-C)>(C1-O1))) {
      return(true);
   }
      
   return(false);
}

//+------------------------------------------------------------------+

bool candlePatternBullishEngulfing(int shift) {
   double O = Open[shift];
   double O1 = Open[shift+1];
   double C = Close[shift];
   double C1 = Close[shift+1];

   if ((O1>C1)&&(C>O)&&(C>=O1)&&(C1>=O)&&((C-O)>(O1-C1))) {
      return(true);
   }
      
   return(false);
}

//+------------------------------------------------------------------+

bool candlePatternDarkCloudCover(int shift) {
   double L = Low[shift];
   double H = High[shift];

   double O = Open[shift];
   double O1 = Open[shift+1];
   double C = Close[shift];
   double C1 = Close[shift+1];
   double CL = H-L;

   double OC_HL;
   if((H - L) != 0) {
      OC_HL = (O-C)/(H-L);
   } else {
      OC_HL = 0;
   }

   double Piercing_Line_Ratio = 0.5;
   double Piercing_Candle_Length = 10;
   
   if ((C1>O1)&&(((C1+O1)/2)>C)&&(O>C)&&(C>O1)&&(OC_HL>Piercing_Line_Ratio)&&((CL>=Piercing_Candle_Length*gPointCoef))) {
      return(true);
   }
   
   return(false);
}

//+------------------------------------------------------------------+

bool candlePatternDoji(int shift) {
   if(MathAbs(Open[shift] - Close[shift])*gPointPow < 0.6) {
      return(true);
   }
   return(false);
}

//+------------------------------------------------------------------+

bool candlePatternHammer(int shift) {
   double H = High[shift];
   double L = Low[shift];
   double L1 = Low[shift+1];
   double L2 = Low[shift+2];
   double L3 = Low[shift+3];
    	
   double O = Open[shift];
   double C = Close[shift];
   double CL = H-L;
    	
   double BodyLow, BodyHigh;
   double Candle_WickBody_Percent = 0.9;
   double CandleLength = 12;
    	
   if (O > C) {
      BodyHigh = O;
      BodyLow = C;  
   } else {
      BodyHigh = C;
      BodyLow = O; 
   }
    	
   double LW = BodyLow-L;
   double UW = H-BodyHigh;
   double BLa = MathAbs(O-C);
   double BL90 = BLa*Candle_WickBody_Percent;
    	
   double pipValue = gPointCoef;
    	
   if ((L<=L1)&&(L<L2)&&(L<L3))  {
      if (((LW/2)>UW)&&(LW>BL90)&&(CL>=(CandleLength*pipValue))&&(O!=C)&&((LW/3)<=UW)&&((LW/4)<=UW)/*&&(H<H1)&&(H<H2)*/)  {
         return(true);
      }
      if (((LW/3)>UW)&&(LW>BL90)&&(CL>=(CandleLength*pipValue))&&(O!=C)&&((LW/4)<=UW)/*&&(H<H1)&&(H<H2)*/)  {
         return(true);
      }
      if (((LW/4)>UW)&&(LW>BL90)&&(CL>=(CandleLength*pipValue))&&(O!=C)/*&&(H<H1)&&(H<H2)*/)  {
         return(true);
      }
   }
   
   return(false);
}

//+------------------------------------------------------------------+

bool candlePatternPiercingLine(int shift) {
   double L = Low[shift];
   double H = High[shift];

   double O = Open[shift];
   double O1 = Open[shift+1];
   double C = Close[shift];
   double C1 = Close[shift+1];
   double CL = H-L;

   double CO_HL;
   if((H - L) != 0) {
      CO_HL = (C-O)/(H-L);
   } else {
      CO_HL = 0;
   }

   double Piercing_Line_Ratio = 0.5;
   double Piercing_Candle_Length = 10;

   if ((C1<O1)&&(((O1+C1)/2)<C)&&(O<C) && (CO_HL>Piercing_Line_Ratio)&&(CL>=(Piercing_Candle_Length*gPointCoef))) {
      return(true);
   }
   
   return(false);
}

//+------------------------------------------------------------------+

bool candlePatternShootingStar(int shift) {
   double L = Low[shift];
   double H = High[shift];
   double H1 = High[shift+1];
   double H2 = High[shift+2];
   double H3 = High[shift+3];
    	
   double O = Open[shift];
   double C = Close[shift];
   double CL = H-L;
    	
   double BodyLow, BodyHigh;
   double Candle_WickBody_Percent = 0.9;
   double CandleLength = 12;
    	
   if (O > C) {
      BodyHigh = O;
      BodyLow = C;  
   } else {
      BodyHigh = C;
      BodyLow = O; 
   }
    	
   double LW = BodyLow-L;
   double UW = H-BodyHigh;
   double BLa = MathAbs(O-C);
   double BL90 = BLa*Candle_WickBody_Percent;
    	
   double pipValue = gPointCoef;
    	
   if ((H>=H1)&&(H>H2)&&(H>H3))  {
      if (((UW/2)>LW)&&(UW>(2*BL90))&&(CL>=(CandleLength*pipValue))&&(O!=C)&&((UW/3)<=LW)&&((UW/4)<=LW)/*&&(L>L1)&&(L>L2)*/)  {
         return(true);
      }
      if (((UW/3)>LW)&&(UW>(2*BL90))&&(CL>=(CandleLength*pipValue))&&(O!=C)&&((UW/4)<=LW)/*&&(L>L1)&&(L>L2)*/)  {
         return(true);
      }
      if (((UW/4)>LW)&&(UW>(2*BL90))&&(CL>=(CandleLength*pipValue))&&(O!=C)/*&&(L>L1)&&(L>L2)*/)  {
         return(true);
      }
   }
   
   return(false);
}
  