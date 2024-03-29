//+---------------------------------------------------------------------+
//|                                                   monitorlib.mq4    |
//|                        Copyright 2013-2021, Thomas Nickel.          |
//|                                                                     |
//+---------------------------------------------------------------------+


#property copyright "Copyright 2013-2021, Thomas Nickel. 2.3.2021"

#include <Trade\AccountInfo.mqh>
#include <MT4Orders.mqh> // https://www.mql5.com/en/code/16006
#include <mql4_to_mql5.mqh>

#define  HR2400 (PERIOD_D1 * 60) // 86400 = 24 * 3600 = 1440 * 60
//1.50  8.8.2023 Redesign for mt5





//1=on, 2=off

int laststate=0;
int tickcounter=0;
int lotcounter=0;
double lastlotsize=0;
int showfilepathflag=0;

int mmRiskPercent=0;
int mmDecimals=0;
int mmStopLossPips=0;
int mmLotsIfNoMM=0;
int mmMaxLots=1;
 
#include <Trade\Trade.mqh> //Instatiate Trades Execution Library
#include <Trade\OrderInfo.mqh> //Instatiate Library for Orders Information
#include <Trade\PositionInfo.mqh> //Instatiate Library for Positions Information
//---
CTrade         m_trade; // Trades Info and Executions library
COrderInfo     m_order; //Library for Orders information
CPositionInfo  m_position; // Library for all position features and information


 

int checkfile(string fnam)
{

//prüft ob ein file da ist

//1: file vorhanden

    int handle=FileOpen(fnam, FILE_BIN|FILE_READ);
    if(handle>0)
    {
     FileClose(handle);
     return(1);
    }
     return(0);
}


int getFilelength(string fnam)

{
    int file_size=0;
    int handle=FileOpen(fnam, FILE_BIN|FILE_READ);

    if(handle>0)
    {
     file_size=FileSize(handle);
     FileClose(handle);
     return(file_size);
    }
    return(0);
}
//https://www.mql5.com/en/forum/11298#comment_457826
bool market_opend(string symbol)
{
    if(StringLen(symbol) > 1)
    {
        datetime begin=0;
        datetime end=0;
        datetime now=TimeCurrent();       
        uint     session_index=0;
        
        
        MqlDateTime today;
        TimeToStruct(now,today);
        if(   SymbolInfoSessionTrade(symbol,(ENUM_DAY_OF_WEEK ) today.day_of_week,session_index,begin,end) == true)
        {
            string snow=TimeToString(now,TIME_MINUTES|TIME_SECONDS);
            string sbegin=TimeToString(begin,TIME_MINUTES|TIME_SECONDS);
            string send=TimeToString(end-1,TIME_MINUTES|TIME_SECONDS);

            now=StringToTime(snow);
            begin=StringToTime(sbegin);
            end=StringToTime(send);

            if(now >= begin && now <= end)
            {
               //Print("Market is opened");
               return true;
            }
            Print("Market is closed");
            return false; 
        }
    }
    return true;
}


bool IsMarketOpenMt5x() 
{
 CAccountInfo myaccount;
 return(myaccount.TradeAllowed());
}

int tradecheck(string mn,string ke)
{
//den tradecheck brauchen wir nicht mehr da wir ja den tradekopierer verwenden und somit keine .on und .off files mehr
 return(0);
}

void closeAllTrades(int magic)
{
//quelle: https://www.mql5.com/en/code/36010
//close all trades with magic

 ulong mag=0;
 m_trade.RequestMagic();

        for(int i = PositionsTotal() - 1; i >= 0; i--) // loop all Open Positions
         if(m_position.SelectByIndex(i))  // select a position
           {
            mag=m_trade.RequestMagic();
            if(mag!=magic)
              continue;
           
            m_trade.PositionClose(m_position.Ticket()); // then delete it --period
            Sleep(100); // Relax for 100 ms
           }

         for(int i = OrdersTotal() - 1; i >= 0; i--) // loop all orders available
         if(m_order.SelectByIndex(i))  // select an order
           {
            mag=m_trade.RequestMagic();
            if(mag!=magic)
              continue;
            
            m_trade.OrderDelete(m_order.Ticket()); // delete it --Period
            Sleep(100); // Relax for 100 ms
        
           }



} 
double extract_doubleval(string line_str,double minval, double maxval,string mn)
{
   //extract the doubleval
   //RiskInPercent = 2.0

   int posgleich=StringFind(line_str,"=",0);
   string substr_str=StringSubstr(line_str,posgleich+1,0);

   if(substr_str==-1)
     MessageBox(" configuration error cant find '=' in <"+line_str+"> --> EA Stopped",0x00000010);

   double val_double=StringToDouble(substr_str);
 
   if(val_double<minval)
     MessageBox( "value<"+val_double+"> muss groesser<"+minval+"> sein  magic<"+mn+"> EA Stopped !!",0x00000010);

   if(val_double>maxval)
     MessageBox( "value<"+val_double+"> muss kleiner<"+maxval+"> sein  magic<"+mn+">  EA Stopped !!",0x00000010);    

   return (val_double);         
}

bool OrderDelete(int magic1)
{
   //--- variables for returning values from order properties 
   ulong    ticket; 
   double   open_price; 
   double   initial_volume; 
   datetime time_setup; 
   string   symbol; 
   string   type; 
   long     order_magic; 
   long     positionID; 
   uint     total=OrdersTotal(); 
   CTrade CT;
//--- go through orders in a loop


   if(IsTradeAllowed()==false)
      return false;
 
   for(uint i=0;i<total;i++)
   { 
      //--- return order ticket by its position in the list 
      if((ticket=OrderGetTicket(i))>0) 
        { 
         //--- return order properties 
         open_price    =OrderGetDouble(ORDER_PRICE_OPEN); 
         time_setup    =(datetime)OrderGetInteger(ORDER_TIME_SETUP); 
         symbol        =OrderGetString(ORDER_SYMBOL); 
         order_magic   =OrderGetInteger(ORDER_MAGIC); 
         positionID    =OrderGetInteger(ORDER_POSITION_ID); 
         initial_volume=OrderGetDouble(ORDER_VOLUME_INITIAL); 
         type          =EnumToString(ENUM_ORDER_TYPE(OrderGetInteger(ORDER_TYPE))); 
            if(symbol==_Symbol && order_magic==magic1)
            {
               Print("Deleting pending orders for ", order_magic);
               ulong OrderTicket=OrderGetTicket(i);
               if(!CT.PositionClose(ticket,-1))
               {
                  Print("Could not delete pending order for ", order_magic);
                 
               }
             
            } 
         }
   }    
   return true; 
}

double Price[2];
int    giSlippage;
bool   CloseOpenOrders = true;
void deleteOpenDelTrades(int forceflag)
{
   //this function will be called every hour
   //see technical documentation 'ForexMonitortoolTechnical.odt'
   //all open trades with a file <magic>.del was found in
   //mql4/files/<magic>.del
   //will be deleted
   //the <magic>.del was generated by the monitortool to indicate the deleted eas
   //if forceflag==1, than all trades will be closed
   //the <magic>.clo is the del function but it will run once

  
  
    for(int i=0; i<(int)PositionsTotal(); i++)
     {
      ulong ticket=PositionGetTicket(i);
      if(ticket==0)
            continue;
      
     
      
      string symbol= PositionGetString(POSITION_SYMBOL);
      int magic= PositionGetInteger(POSITION_MAGIC);
      int type=PositionGetInteger(POSITION_TYPE);
      string comment = PositionGetString(POSITION_COMMENT);
      
  
     if(((checkfile(magic+".del"))>0)||
        ((checkfile(magic+".clo"))>0)||
        ((magic==99999)&&(checkfile(comment+".delcom"))>0)||
        ((magic==99999)&&(checkfile(comment+".clocom"))>0)||
        (forceflag==1))
      {
      
         bool result=OrderDelete(magic);
    
         if(result == false)
         {
            Alert("Order " , magic , " failed to close. Error: <"+magic+".del"+">" , GetLastError() );
            //Sleep 10 Min
            Sleep(600000);
            
         }
         else 
         { 
            //der Trade mit der magic wurde gelöscht
            //und es war ein *.clo vorhanden
            if(checkfile(magic+".clo")>0)
            {
               //dann lösche das *.clo file, da der trade ja gelöscht wurde
               if(FileDelete(magic+".clo")==false)
                   Alert("cant delete file <"+magic+".clo"+">");
               else
                  Print("Order ticket<"+ticket+"> magic<"+magic+"> was successfully deleted because I found <"+magic+".clo"+">"); 
            }
            else if ((magic==99999)&&(checkfile(comment+".clocom"))>0)
             {
 
               //dann lösche das *.clocom file, da der trade ja gelöscht wurde
               if(FileDelete(comment+".clocom")==false)
                   Alert("cant delete file <"+comment+".clocom"+">");
               else
                Print("Order ticket<"+ticket+"> magic<"+magic+"> was successfully deleted because I found <"+comment+".clocom"+">"); 
             }
            
            //es ist ein *.del oder *.delcom vorhanden
            else
              Print("Order ticket<"+ticket+"> magic<"+magic+"> was successfully deleted because I found <"+magic+".del"+">"); 
         }
      }   
   
   } 
}

bool IsDemo()
{
   ENUM_ACCOUNT_TRADE_MODE account_type=(ENUM_ACCOUNT_TRADE_MODE)AccountInfoInteger(ACCOUNT_TRADE_MODE);
   //--- Now transform the value of  the enumeration into an understandable form
   string trade_mode;
   switch(account_type)
     {
      case  ACCOUNT_TRADE_MODE_DEMO:
         trade_mode="demo";
         return true;
         break;
      case  ACCOUNT_TRADE_MODE_CONTEST:
         trade_mode="contest";
         return false;
         break;
      default:
         trade_mode="real";
         return false;
         break;
     }

}


bool LabelCreate(const long              chart_ID=0,// ID des Charts 
                 const string            name="Label",             // Name des Labels 
                 const int               sub_window=0,             // Nummer des Unterfensters 
                 const int               x=0,                      // X-Koordinate 
                 const int               y=0,                      // Y-Koordinate 
                 const ENUM_BASE_CORNER  corner=CORNER_LEFT_LOWER, // Winkel des Charts zu Binden 
                 const string            text="Label",             // Text 
                 const string            font="Arial",             // Schrift 
                 const int               font_size=10,             // Schriftgröße 
                 const color             clr=clrRed,               // Farbe 
                 const double            angle=0.0,                // Text Winkel 
                 const ENUM_ANCHOR_POINT anchor=ANCHOR_LEFT_UPPER, // Bindungsmethode 
                 const bool              back=false,               // Im Hintergrund 
                 const bool              selection=false,          // Wählen um zu bewegen 
                 const bool              hidden=true,              // Ausgeblendet in der Objektliste 
                 const long              z_order=0)                // Priorität auf Mausklick 
  {
//--- Setzen den Wert des Fehlers zurück 
   ResetLastError();
//--- ein Text-Label erstellen 
   if(!ObjectCreate(chart_ID,name,OBJ_LABEL,sub_window,0,0))
     {
      Print(__FUNCTION__,
            ": Text-Label konnte nicht erstellt werden! Fehlercode = ",GetLastError());
      return(false);
   ;}
//--- Die Koordinaten des Schilds setzen 
   ObjectSetInteger(chart_ID,name,OBJPROP_XDISTANCE,x);
   ObjectSetInteger(chart_ID,name,OBJPROP_YDISTANCE,y);
//--- wählen die Ecke des Charts, relativ zu der die Punktkoordinaten eingegeben werden 
   ObjectSetInteger(chart_ID,name,OBJPROP_CORNER,corner);
//--- den Text setzen 
   ObjectSetString(chart_ID,name,OBJPROP_TEXT,text);
//--- Textschrift setzen 
   ObjectSetString(chart_ID,name,OBJPROP_FONT,font);
//--- Schriftgröße setzen 
   ObjectSetInteger(chart_ID,name,OBJPROP_FONTSIZE,font_size);
//--- Text-Winkel angeben 
   ObjectSetDouble(chart_ID,name,OBJPROP_ANGLE,angle);
//--- die Bindungsmethode setzen 
   ObjectSetInteger(chart_ID,name,OBJPROP_ANCHOR,anchor);
//--- Farbe setzen 
   ObjectSetInteger(chart_ID,name,OBJPROP_COLOR,clr);
//--- Im Vordergrund (false) oder Hintergrund (true) anzeigen 
   ObjectSetInteger(chart_ID,name,OBJPROP_BACK,back);
//--- Aktivieren (true) oder deaktivieren (false) Mausbewegung Modus 
   ObjectSetInteger(chart_ID,name,OBJPROP_SELECTABLE,selection);
   ObjectSetInteger(chart_ID,name,OBJPROP_SELECTED,selection);
//--- Verbergen (true) oder Anzeigen (false) den Namen des graphischen Objektes in der Objektliste 
   ObjectSetInteger(chart_ID,name,OBJPROP_HIDDEN,hidden);
//--- setzen die Priorität für eine Mausklick-Ereignisse auf dem Chart 
   ObjectSetInteger(chart_ID,name,OBJPROP_ZORDER,z_order);
//--- die erfolgreiche Umsetzung 
   return(true);
;}
//https://www.mql5.com/en/articles/81
int WindowHandleMQL4(string symbol,
                     int tf)
  {
   ENUM_TIMEFRAMES timeframe=TFMigrate(tf);
   long currChart,prevChart=ChartFirst();
   int i=0,limit=100;
   while(i<limit)
     {
      currChart=ChartNext(prevChart);
      if(currChart<0) break;
      if(ChartSymbol(currChart)==symbol
         && ChartPeriod(currChart)==timeframe)
         return((int)currChart);
      prevChart=currChart;
      i++;
     }
   return(0);
  }

int DayOfWeekMQL4()
  {
   MqlDateTime tm;
   TimeCurrent(tm);
   return(tm.day_of_week);
  }

 
ENUM_TIMEFRAMES TFMigrate(int tf)
  {
   switch(tf)
     {
      case 0: return(PERIOD_CURRENT);
      case 1: return(PERIOD_M1);
      case 5: return(PERIOD_M5);
      case 15: return(PERIOD_M15);
      case 30: return(PERIOD_M30);
      case 60: return(PERIOD_H1);
      case 240: return(PERIOD_H4);
      case 1440: return(PERIOD_D1);
      case 10080: return(PERIOD_W1);
      case 43200: return(PERIOD_MN1);
      
      case 2: return(PERIOD_M2);
      case 3: return(PERIOD_M3);
      case 4: return(PERIOD_M4);      
      case 6: return(PERIOD_M6);
      case 10: return(PERIOD_M10);
      case 12: return(PERIOD_M12);
      case 16385: return(PERIOD_H1);
      case 16386: return(PERIOD_H2);
      case 16387: return(PERIOD_H3);
      case 16388: return(PERIOD_H4);
      case 16390: return(PERIOD_H6);
      case 16392: return(PERIOD_H8);
      case 16396: return(PERIOD_H12);
      case 16408: return(PERIOD_D1);
      case 32769: return(PERIOD_W1);
      case 49153: return(PERIOD_MN1);      
      default: return(PERIOD_CURRENT);
     }
  }
  
int GetFileEncoding(void)
  {
   return IsAscii(_Symbol) &&
          IsAscii(SymbolInfoString(_Symbol, SYMBOL_DESCRIPTION)) &&
          IsAscii(AccountInfoString(ACCOUNT_SERVER))  &&
          IsAscii(AccountInfoString(ACCOUNT_COMPANY)) &&
          IsAscii(TerminalInfoString(TERMINAL_NAME))
          ? FILE_ANSI
          : FILE_UNICODE;
  }
  
  bool IsAscii(const string text)
  {
   for(int i = 0; i < StringLen(text); i += 1)
     {
      const int charCode = StringGetCharacter(text, i);
      if(charCode < 32 || charCode > 126)
         return false;
     }

   return true;
  }
int MathRandInt(const int min, const int max)
  {
   int RAND_MAX = 32767;
   int range = max - min;
   if (range > RAND_MAX) range = RAND_MAX;
   int randMin = RAND_MAX % range;
   int rand;  do{ rand = MathRand(); }while (rand <= randMin);
   return rand % range + min;
  }