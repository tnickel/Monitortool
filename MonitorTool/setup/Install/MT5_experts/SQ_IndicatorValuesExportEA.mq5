//+------------------------------------------------------------------------------------+
//|                                                      SqIndicatorValuesExportEA.mq4 |
//|                                                                                    |
//|                                      EA to export indicator values from MetaTrader |
//|  Output to: {MetaQuotes folder}/Tester/{Data folder}/AgentXY/MQL5/Files/******.csv |
//+-----------------------------------------------------------------------------------+/

#property copyright "Copyright © 2018 StrategyQuant"
#property link      "http://www.StrategyQuant.com"
#property version   "1.00"

#include <Expert\Expert.mqh>

string currentTime = "";
string lastTime = "";

int indicatorHandle;
int indicatorBufferIndex = 0;
int indicatorShift = 0;

int bars_calculated = 0;
string fileName;

double IndicatorBuffer[]; 
double SignalBuffer[]; 

//+------------------------------------------------------------------+
//| Expert initialization function                                   |
//+------------------------------------------------------------------+
int OnInit()
  {
  
   //+------------------------------------------------------------------+
   //| Indicator settings    
   //+------------------------------------------------------------------+
   
   fileName = "ATR_14.csv";
   indicatorBufferIndex = 0;
   
   indicatorHandle = iCustom(NULL, 0, "SqATR", 14);
   
   //+------------------------------------------------------------------+
   //+------------------------------------------------------------------+
   
   //--- if the handle is not created 
   if(indicatorHandle == INVALID_HANDLE) { 
      //--- tell about the failure and output the error code 
      PrintFormat("Failed to create handle of the indicator, error code %d", GetLastError()); 
      //--- the indicator is stopped early 
      return(INIT_FAILED); 
   } 
   
   //remove old file content
   int handle = FileOpen(fileName, FILE_WRITE | FILE_TXT | FILE_ANSI, ";");
	if(handle != INVALID_HANDLE) {
      FileClose(handle);
	}

   return(INIT_SUCCEEDED);
  }
//+------------------------------------------------------------------+
//| Expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason)
  {
//---
   
  }
//+------------------------------------------------------------------+
//| Expert tick function                                             |
//+------------------------------------------------------------------+

void OnTick()
  {
  
   if(!FillArraysFromBuffers(IndicatorBuffer, indicatorHandle, 2)) return; 

   MqlRates rt[2];
   int count = CopyRates(_Symbol, _Period, 0, 2, rt);
   
   if(count == 2){
      currentTime = TimeToString(rt[0].time, TIME_DATE | TIME_MINUTES | TIME_SECONDS);
      if(currentTime == lastTime) {
         return;
      }
   
   	int handle = FileOpen(fileName, FILE_READ | FILE_WRITE | FILE_TXT | FILE_ANSI, ";");
   	if(handle != INVALID_HANDLE) {
       	FileSeek(handle,0,SEEK_END);
         
         FileWrite(handle, currentTime, rt[0].open, rt[0].high, rt[0].low, rt[0].close, rt[0].tick_volume, IndicatorBuffer[0]);
      
         FileClose(handle);
   	}
   
   	lastTime = currentTime;
   }
   else {
      Alert("CopyRates error, count : ", count);
   }
  }
  
//+------------------------------------------------------------------+ 
//| Filling indicator buffers from the selected indicator            | 
//+------------------------------------------------------------------+ 
bool FillArraysFromBuffers(double &indicator_buffer[],    // indicator buffer 
                           int ind_handle,           // handle of the indicator 
                           int amount                // number of copied values 
                           ) 
  { 
//--- reset error code 
   ResetLastError(); 
//--- fill a part of the Buffer array with values from the indicator buffer that has 0 index 
   if(CopyBuffer(ind_handle,indicatorBufferIndex,indicatorShift,amount,indicator_buffer)<0) 
     { 
      //--- if the copying fails, tell the error code 
      PrintFormat("Failed to copy data from the indicator, error code %d",GetLastError()); 
      //--- quit with zero result - it means that the indicator is considered as not calculated 
      return(false); 
     } 
  
//--- everything is fine 
   return(true); 
  } 
