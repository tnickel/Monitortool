// MQL4&5-code

#ifdef __MQL5__

#define show_inputs script_show_inputs

#define extern input

#define init OnInit

#define Point _Point
#define Digits _Digits

#define Bid (::SymbolInfoDouble(_Symbol, ::SYMBOL_BID))
#define Ask (::SymbolInfoDouble(_Symbol, ::SYMBOL_ASK))

#define True true
#define False false

#define TimeToStr TimeToString
#define DoubleToStr DoubleToString

#define CurTime TimeCurrent

#define HistoryTotal OrdersHistoryTotal

#define LocalTime TimeLocal

#define MODE_BID 9
#define MODE_ASK 10
#define MODE_DIGITS 12
#define MODE_SPREAD 13
#define MODE_STOPLEVEL 14
#define MODE_LOTSIZE 15
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double MarketInfo(const string Symb,const int Type)
  {
   switch(Type)
     {
      case MODE_BID:
         return(::SymbolInfoDouble(Symb, ::SYMBOL_BID));
      case MODE_ASK:
         return(::SymbolInfoDouble(Symb, ::SYMBOL_ASK));
      case MODE_DIGITS:
         return((double)::SymbolInfoInteger(Symb, ::SYMBOL_DIGITS));
      case MODE_SPREAD:
         return((double)::SymbolInfoInteger(Symb, ::SYMBOL_SPREAD));
      case MODE_STOPLEVEL:
         return((double)::SymbolInfoInteger(Symb, ::SYMBOL_TRADE_STOPS_LEVEL));
      case MODE_LOTSIZE:
         return(::SymbolInfoDouble(Symb, ::SYMBOL_TRADE_CONTRACT_SIZE));
     }

   return(-1);
  }

#define StringGetChar StringGetCharacter
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
string  StringSetChar(const string &String_Var,const int iPos,const ushort Value)
  {
   string Str=String_Var;

   ::StringSetCharacter(Str,iPos,Value);

   return(Str);
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
int TimeDayOfWeek(const datetime Date)
  {
   MqlDateTime mTime;

   ::TimeToStruct(Date,mTime);

   return(mTime.day_of_week);
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
bool IsTesting(void)
  {
   return(true);
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
bool IsTradeContextBusy(void)
  {
   return(false);
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
bool IsTradeAllowed(void)
  {
   return(::MQLInfoInteger(::MQL_TRADE_ALLOWED));
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
bool RefreshRates(void)
  {
   return(true);
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
string WindowExpertName(void)
  {
   return(::MQLInfoString(::MQL_PROGRAM_NAME));
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
string AccountName(void)
  {
   return(::AccountInfoString(::ACCOUNT_NAME));
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
int AccountNumber(void)
  {
   return((int)::AccountInfoInteger(::ACCOUNT_LOGIN));
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double AccountFreeMargin(void)
  {
   return(::AccountInfoDouble(::ACCOUNT_MARGIN_FREE));
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double AccountFreeMarginCheck(const string Symb,const int Cmd,const double dVolume)
  {
   double Margin;

   return(::OrderCalcMargin((ENUM_ORDER_TYPE)Cmd, Symb, dVolume,
          ::SymbolInfoDouble(Symb,(Cmd==::ORDER_TYPE_BUY) ? ::SYMBOL_ASK : ::SYMBOL_BID),Margin) ?
          ::AccountInfoDouble(::ACCOUNT_MARGIN_FREE) - Margin : -1);
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
double AccountEquity(void)
  {
   return(::AccountInfoDouble(::ACCOUNT_EQUITY));
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
int MT4Bars(void)
  {
   return(::Bars(_Symbol, _Period));
  }

#define Bars (::MT4Bars())


#define DEFINE_TIMESERIE(NAME,FUNC,T)                                                                         \
  class CLASS##NAME                                                                                           \
  {                                                                                                           \
  public:                                                                                                     \
    static T Get(const string Symb,const int TimeFrame,const int iShift)                                      \
    {                                                                                                         \
      T tValue[];                                                                                             \
                                                                                                              \
      return((Copy##FUNC((Symb == NULL) ? _Symbol : Symb, _Period, iShift, 1, tValue) > 0) ? tValue[0] : -1); \
    }                                                                                                         \
                                                                                                              \
    T operator[](const int iPos) const                                                                        \
    {                                                                                                         \
      return(CLASS##NAME::Get(_Symbol, _Period, iPos));                                                       \
    }                                                                                                         \
  };                                                                                                          \
                                                                                                              \
  CLASS##NAME NAME;                                                                                           \
                                                                                                              \
  T i##NAME(const string Symb,const int TimeFrame,const int iShift)                                           \
  {                                                                                                           \
    return(CLASS##NAME::Get(Symb, TimeFrame, iShift));                                                        \
  }

DEFINE_TIMESERIE(Volume,TickVolume,long)
DEFINE_TIMESERIE(Time,Time,datetime)
DEFINE_TIMESERIE(Open,Open,double)
DEFINE_TIMESERIE(High,High,double)
DEFINE_TIMESERIE(Low,Low,double)
DEFINE_TIMESERIE(Close,Close,double)

#endif // __MQL5__
