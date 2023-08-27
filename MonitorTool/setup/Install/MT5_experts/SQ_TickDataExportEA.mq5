//+-----------------------------------------------------------------------------------+
//|                                                           SQ_TickDataExportEA.mq5 |
//|                                                                                   |
//|                                            EA to export tick data from MetaTrader |
//|                                               Tick data are exported to directory |
//| {MetaQuotes folder}/Tester/{Data folder}/AgentXY/MQL5/Files/{Symbol}_TickData.csv |
//+-----------------------------------------------------------------------------------+

#property copyright "Copyright © 2016 StrategyQuant"
#property link      "http://www.StrategyQuant.com"

int previousVolume = 0;
int handle;

int OnInit() {
   string fileName;
   StringConcatenate(fileName, Symbol(), "_TickData.csv");
   
   handle = FileOpen(fileName, FILE_ANSI | FILE_WRITE, ",", CP_UTF8);
   if(handle>0) {
      FileWrite(handle, "Time","Ask","Bid","Volume");
   }
   return(INIT_SUCCEEDED);
}

//+------------------------------------------------------------------+

void OnTick() {
   double Ask = SymbolInfoDouble(_Symbol, SYMBOL_ASK); 
   double Bid = SymbolInfoDouble(_Symbol, SYMBOL_BID); 

   if(handle>0) {
      FileWrite(handle, TimeToString(TimeCurrent(), TIME_DATE | TIME_SECONDS), Ask, Bid, 1);
   }

}

void OnDeinit(const int reason) {
    FileClose(handle);
}