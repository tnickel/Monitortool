//+---------------------------------------------------------------------+
//|                                              HistoryExporter.mq5   |
//|                        Copyright 2015-2023 Thomas Nickel ,18.8.2023|
//|                                                                     |
//+---------------------------------------------------------------------+
#import "shell32.dll"
int ShellExecuteA(int hWnd, string Verb, string File, string Parameter, string Path, int ShowCmd);
#import "user32.dll"
int GetAncestor(int a0, int a1);
int PostMessageA(int hWnd,int Msg,int wParam,int lParam);
#import


#property copyright "Copyright 2013-2023, Thomas Nickel"
#property link      "x"
#include <monitorlib.mqh>

string version_str = "1.53";
// 1.53 17.11.2023 update Kontoinformation File
// 1.52 29.8.2023 Bugfix historyexporter output refresh on left-up-corner
// 1.50 8.8.2023  first version of historyexporter for Mt5


//+------------------------------------------------------------------+
//| expert initialization function                                   |
//+------------------------------------------------------------------+
datetime lastchecktime=0;
datetime lastwritetime=0;

double spreadarray[1000];
int spreadcounter=0;


//Zeitintervalle wann das nächste mal gechecked wird
extern int checkintervall =  30;     //30 sekunden

//dies sind die counter für die add-historyexporter

int dealanzahl=0;
int lastorderopenticket_g=0;

//allgemeiner counter
int histanz=0;
int openanz=0;
int fileopenanz=0;

//zeiten wann letztes mal gechecked wurde

int lastcheckpongtime=0;

string lastchecktime_str="init";
string lastwritetime_str="init";

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
int OnInit()
  {
   Print(" Historyexporter OnInit-Start:AccountCompany<"+ AccountInfoString(ACCOUNT_COMPANY)+"> AccountName<"+AccountInfoString(ACCOUNT_NAME));
   Print("historyexporter version="+version_str);
//setze den timer auf z.b. 30sekunden=shortcheckintervall
//und da wird auch gepongt
   initTimer();
//fourceflag=0, falls fourceflag==1 ist dann wird immer gelöscht auch wenn kein *.del vorhanden ist

   if(market_opend("EURUSD"))
      deleteOpenDelTrades(0);

   datetime currtime=TimeCurrent();
//sage auf jeden Fall beim ersten Aufruf schreiben


//prüft nach ob neues konto, ggf. die Trades sichern
   checkNewKonto();
   gibKontoinfoaus();

//den historyexporter schreiben
   dealanzahl=AppendHistory("history.txt",dealanzahl);

//die Kopfinfozeile aktualisieren
   showinitheadline();
   showlastaccess();
   lastorderopenticket_g=WriteOpenHistory("history_open.txt",lastorderopenticket_g);

//prüft auf restart
   if(checkfile("restart.start")>0)
     {
      FileDelete("restart.start");
      FileDelete("restart.ok");
      int handle=FileOpen("restart.ok", FILE_BIN|FILE_WRITE);
      FileWrite(handle,"System startet at "+TimeToStr(TimeCurrent(),TIME_DATE|TIME_MINUTES));
      FileClose(handle);
     }
   Print(" Historyexporter OnInit-Ready:");
   return(0);
  }
//+------------------------------------------------------------------+
//| expert deinitialization function                                 |
//+------------------------------------------------------------------+
void OnDeinit(const int reason)
  {
   EventKillTimer();
  }
//+------------------------------------------------------------------+
//| expert start function                                            |
//+------------------------------------------------------------------+
void OnTimer()
  {
   datetime currtime=0;
   double spread=0;
   double mspread=0;
   int lockcounter=0;
   spread=(Ask-Bid)*10000;
   tickcounter++;

   if((DayOfWeekMQL4()==6 || DayOfWeekMQL4()==7))
      currtime=TimeCurrent() +tickcounter;//am wochenende
   else
      currtime=TimeCurrent();

//Print("get tick.."+tickcounter);
   lockcounter=0;
//warte bis der lock vom monitortool zuende ist
   while(checkfile("monitor.lock")>0)
     {
      Print("found monitor.lock wait 1 sec");
      Sleep(1000);
      lockcounter++;

      if(lockcounter>180)
        {
         Print("break lock form monitortool after 3 minutes");
         FileDelete("monitor.lock");
         break;
        }
     }

//hier kam der befehl vom monitortool alles zu schliessen, der account ist abgelaufen
   if(checkfile("closeAccount.start")>0)
     {
      Print("Detect closeAccount.start");
      deleteOpenDelTrades(0);
      FileDelete("closeAccount.start");

      //speichereHistoryExporter("history_expired_"+ TimeToStr(TimeCurrent(),TIME_DATE)+".txt");
      histanz=AppendHistory("history_expired_"+ TimeToStr(TimeCurrent(),TIME_DATE)+".txt",0);

      int handle=FileOpen("closeAccount.ok", FILE_BIN|FILE_WRITE);
      FileClose(handle);
      Alert("Please generate a new account and Restart Metatrader");
      return;
     }
   showheadline(spread);

//setze den Pong
   pong();

   lastorderopenticket_g=WriteOpenHistory("history_open.txt",lastorderopenticket_g);
//Print(TimeToStr(currtime)+"check shortintervall");

//restartcheck
   restartcheck();

   lastchecktime_str=TimeToStr(currtime, TIME_SECONDS);
   dealanzahl=AppendHistory("history.txt",dealanzahl);

//Print(TimeToStr(currtime)+"export longhistory");

   if(market_opend("EURUSD")==true)
      deleteOpenDelTrades(0);

   showheadline(spread);
   showlastaccess();

//+------------------------------------------------------------------+
  }


//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void showheadline(double spread)
  {
   ObjectSetString(ChartID(),"headline",OBJPROP_TEXT,"count=" + tickcounter +" openanz= "+openanz+" dealanzahl= "+dealanzahl+" Spread="+spread);

  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void showinitheadline()
  {
   ObjectCreate(ChartID(),"headline",OBJ_LABEL,0,0,0,0,0,0,0);
   ObjectSetString(ChartID(),"headline",OBJPROP_TEXT,"tc=" + tickcounter +" openanz= "+openanz+" histanz= "+dealanzahl);
   ObjectSetInteger(ChartID(),"headline",OBJPROP_XDISTANCE,50);
   ObjectSetInteger(ChartID(),"headline",OBJPROP_YDISTANCE,50);
   ObjectSetInteger(ChartID(),"headline",OBJPROP_CORNER,CORNER_LEFT_UPPER);
   ObjectSetInteger(ChartID(),"headline",OBJPROP_COLOR,clrAliceBlue);

   ObjectCreate(ChartID(),"path",OBJ_LABEL,0,0,0,0,0,0,0);
   ObjectSetString(ChartID(),"path",OBJPROP_TEXT,"path="+ TerminalInfoString(TERMINAL_DATA_PATH)+histanz);
   ObjectSetInteger(ChartID(),"path",OBJPROP_XDISTANCE,50);
   ObjectSetInteger(ChartID(),"path",OBJPROP_YDISTANCE,80);
   ObjectSetInteger(ChartID(),"path",OBJPROP_CORNER,CORNER_LEFT_UPPER);
   ObjectSetInteger(ChartID(),"path",OBJPROP_COLOR,clrAliceBlue);

   ObjectCreate(ChartID(),"leverage",OBJ_LABEL,0,0,0,0,0,0,0);
   ObjectSetString(ChartID(),"leverage",OBJPROP_TEXT,"leverage="+ AccountInfoInteger(ACCOUNT_LEVERAGE));
   ObjectSetInteger(ChartID(),"leverage",OBJPROP_XDISTANCE,50);
   ObjectSetInteger(ChartID(),"leverage",OBJPROP_YDISTANCE,110);
   ObjectSetInteger(ChartID(),"leverage",OBJPROP_CORNER,CORNER_LEFT_UPPER);
   ObjectSetInteger(ChartID(),"leverage",OBJPROP_COLOR,clrAliceBlue);

   ObjectCreate(ChartID(),"lastcheck",OBJ_LABEL,0,0,0,0,0,0,0);
   ObjectSetString(ChartID(),"lastcheck",OBJPROP_TEXT,"lastcheck=" +   lastchecktime_str);
   ObjectSetInteger(ChartID(),"lastcheck",OBJPROP_XDISTANCE,50);
   ObjectSetInteger(ChartID(),"lastcheck",OBJPROP_YDISTANCE,150);
   ObjectSetInteger(ChartID(),"lastcheck",OBJPROP_CORNER,CORNER_LEFT_UPPER);
   ObjectSetInteger(ChartID(),"lastcheck",OBJPROP_COLOR,clrAliceBlue);

   ObjectCreate(ChartID(),"lastwrite",OBJ_LABEL,0,0,0,0,0,0,0);
   ObjectSetString(ChartID(),"lastwrite",OBJPROP_TEXT,"lastwrite=" +   lastwritetime_str);
   ObjectSetInteger(ChartID(),"lastwrite",OBJPROP_XDISTANCE,50);
   ObjectSetInteger(ChartID(),"lastwrite",OBJPROP_YDISTANCE,180);
   ObjectSetInteger(ChartID(),"lastwrite",OBJPROP_CORNER,CORNER_LEFT_UPPER);
   ObjectSetInteger(ChartID(),"lastwrite",OBJPROP_COLOR,clrAliceBlue);

  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void showlastaccess()
  {
   ObjectSetString(ChartID(),"lastcheck",OBJPROP_TEXT,"lastcheck=" +   lastchecktime_str);
   ObjectSetString(ChartID(),"lastwrite",OBJPROP_TEXT,"lastwrite=" +   lastwritetime_str);
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void initTimer()
  {
//set the timer for the pong/shortcheckintervall
//in problemcase it will retry 5 times
   stat(41);
   bool timersuccess=false;
   int anztry=0;
   int errorcode=0;

   while((timersuccess= EventSetTimer(checkintervall))==false)
     {
     
      anztry++;
      errorcode=GetLastError();
      Print("timer set errorcode="+errorcode+" I sleep 5 sec and try again");
      Sleep(5000);
      if(anztry>5)
        {
         Alert("Can´t set pong timer errorcode=<"+errorcode+"> --> STOP");
        }
     }
   stat(43);
   Print("Timer for shortcheckintervall successfull set after trys<"+anztry+">");
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
bool checkNewKonto()
  {
//prüft nach ob dies ein neues Konto ist
//erst mal die neue history schreiben
   AppendHistory("history_init_check.txt",0);

//dann vergleiche ob die neue signifikant kürzer als die alte ist
//dann wird dasvon ausgegangen das das neue ein neues konto ist
//dann wird das alte gsichert.
   ulong len_init=getFilelength("history_init_check.txt");
   ulong len_old=getFilelength("history.txt");

//falls das init >100 bytes, dann ist das ein altes konto
   if(len_init>100)
     {
      //wieder löschen da dies nicht gebraucht wird
      FileDelete("history_init_check.txt");
      return false;
     }
   else
     {
      //falls init<100
      //falls old >init
      if(len_old>len_init)
        {
         //prüfe nach ob ein altes konto existiert, wenn ja
         //dann sichere das alte als expired
         FileCopy("history.txt",0,"history_expired_"+ TimeToString(TimeCurrent(),TIME_DATE)+".txt",FILE_REWRITE);
         FileDelete("history.txt");

         return true;
        }
      else
        {
         //beide gleich dann brauche ich das testfile nicht mehr
         FileDelete("history_init_check.txt");
         return false;

        }
     }
   return false;
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void gibKontoinfoaus()
{
//und gib die Kontoinformationen in einem File aus
   string accountinfo="";
   if(IsDemo()==true)
      accountinfo="Demoaccount";
   else
      accountinfo="Realaccount";

   FileDelete("KontoInformation.txt");
   int handle = FileOpen("KontoInformation.txt",  FILE_WRITE|FILE_READ|FILE_CSV, "#");
   FileSeek(handle,0,SEEK_END);
   
   
   
   FileWrite(handle,TimeToString(TimeLocal(),TIME_DATE|TIME_MINUTES),AccountInfoString(ACCOUNT_NAME),  AccountInfoInteger(ACCOUNT_LOGIN),      
   AccountInfoString(ACCOUNT_SERVER), AccountInfoDouble(ACCOUNT_BALANCE), AccountInfoString(ACCOUNT_COMPANY),"00",AccountInfoDouble(ACCOUNT_BALANCE),
   accountinfo);
   FileClose(handle);

  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
int AppendHistory(string fullfnam,int lastcounter)
// er kann doch an der ordernummer feststellen ob was neues hinzugekommen ist

  {
//lastcounter: ist der index der letztes mal geschrieben wurde
//bei fullflag wird alles geschrieben also ohne append
   datetime currtime=TimeCurrent();
   int hanz=0;
  

//anzahl der  orders ermitteln (geschlossene), den oht brauchen wir noch da wir mit OrderSelect durch die LIste gehen.
//Das OrderSelect stammt noch aus MQL4
   int oht=OrdersHistoryTotal();

//ermittle Anzahl Deals
//Hinweis: Wir schreiben jetzt die Datei jedesmal komplett neu, wir schreiben aber nur wenn sich die Anzahl der Deals geändert hat.
//Bei änderungen von limitorders wird nix neues geschrieben, da wir diese Informationen sowieso nicht in history.txt benötigen.
   HistorySelect(0,TimeCurrent());
   int hdt=HistoryDealsTotal();

//nix schreiben da anzahl gleich
   if(lastcounter==hdt)
      return(lastcounter);

   lastwritetime_str=TimeToStr(currtime, TIME_SECONDS);
   
   lock();
  

//Print("*** write full historyexporter <"+fullfnam+"> von<0> bis<"+histanz+">");
//das alte löschen
   const int encoding   = GetFileEncoding();
   const int handle = FileOpen(fullfnam,  FILE_ANSI | FILE_WRITE, "#", CP_UTF8);


   for(int i=0; i <oht; i++)
     {
      OrderSelect(i, SELECT_BY_POS,MODE_HISTORY);

      //nur buy und sell order in history_small schreiben
      if((OrderType()!=OP_BUY)&&(OrderType()!=OP_SELL))
         continue;

      //falls der Ea gelöscht wurde dann nimm den nicht mehr in der liste auf
      int magic=OrderMagicNumber();
      string comment = OrderComment();
      if(checkfile(magic+".del")>0)
         continue;

      if((magic==99999)&&(checkfile(comment+".delcom"))>0)
         continue;

      //OrderPrint();
      //Ordernumber, OrderProfit, Orderlots, OrderOpentime, OrderCloseTime, OrderComment,AccountNumber,Symbol,type
      //0#-59.3#1#2012.11.21 15:25:11#1970.01.01 00:00:00##480681

      string closetime=TimeToStr(OrderCloseTime(), TIME_DATE|TIME_SECONDS);
      string opentime= TimeToStr(OrderOpenTime(), TIME_DATE|TIME_SECONDS);
      FileWrite(handle,OrderMagicNumber(),OrderType(),OrderOpenPrice(),OrderClosePrice(),OrderCommission(),OrderSwap(),OrderProfit(),OrderLots(),opentime,closetime,OrderComment(),AccountNumber(),OrderTicket(),OrderSymbol(),OrderType());
     }
  

   FileClose(handle);
   unlock();
 
   return (hdt);
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
int WriteOpenHistory(string fullfnam,int lastorderticket)
//schreibt die openhistory immer, schreibt aber nur wenn sich was geändert hat
//fullfnam: namen des FileSeek
//lastorderticket:ticketnummer der letzten order
//rückgabewert = orderticket der neusten order
  {
   int handle=0;
  
//hole die letzte ticketnummer der open order, wenn 0 zurückkommt dann ist nix offen
   int lastticketnumber=getLastOpenOrderTicketNumber();
  
//falls kein open trade da ist dann 0
   if(lastticketnumber==0)
     {
      //Print("lastticketnumber=0");

      //wenn nix offen ist dann wird history_open gelöscht und das war es erstmal
      if(checkfile(fullfnam)==1)
         FileDelete(fullfnam);
      return (0);
     }
  
//falls nix neues dann zurück
   if(lastticketnumber==lastorderticket)
     {
      //Print("keine neuen open orders");
      return (lastorderticket);
     }
  
   Print("Änderung openhistory"+lastticketnumber+" != "+lastorderticket);
   lock();
 

//das alte löschen
   if(checkfile(fullfnam)==1)
      FileDelete(fullfnam);


  

   Print("*** write open history  von<0> bis<"+openanz+">");
   handle = FileOpen(fullfnam,  FILE_WRITE, "#");
   if(handle>0)
      FileSeek(handle,0,SEEK_END);
  
   int allopen=OrdersTotal();
//alles schreiben, und globale openanz ermitteln
   openanz=0;
   for(int i=0; i <allopen; i++)
     {
      //Print("*** append open_exporter");
      OrderSelect(i, SELECT_BY_POS,MODE_TRADES);

      //nur buy und sell order in history_small schreiben
      if((OrderType()!=OP_BUY)&&(OrderType()!=OP_SELL))
         continue;

      openanz++;

      //OrderPrint();
      //Ordernumber, OrderProfit, Orderlots, OrderOpentime, OrderCloseTime, OrderComment,AccountNumber,Symbol,type
      //0#-59.3#1#2012.11.21 15:25:11#1970.01.01 00:00:00##480681

      string closetime=TimeToStr(OrderCloseTime(), TIME_DATE|TIME_SECONDS);
      string opentime= TimeToStr(OrderOpenTime(), TIME_DATE|TIME_SECONDS);


      if((opentime!="1970.01.01 00:00:00")&&(closetime=="1970.01.01 00:00:00"))
         closetime="2050.01.01 00:00:00";

      FileWrite(handle,OrderMagicNumber(),OrderType(),OrderOpenPrice(),OrderClosePrice(),OrderCommission(),OrderSwap(),OrderProfit(),OrderLots(),opentime,closetime,OrderComment(),AccountNumber(),OrderTicket(),OrderSymbol(),OrderType());
     }
  
//den neuen letztenzuweisen
   lastorderticket=lastticketnumber;
   FileClose(handle);
   unlock();
   
   return (lastorderticket);
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
int getLastOpenOrderTicketNumber()
  {
   int oanz=OrdersTotal();
   if(oanz==0)
      return (0);

//holt die letzte open order die kein stop-buy oder stop-sell ist
   for(int i=oanz-1; i >=0; i--)
     {
      //Print("*** append open_exporter");
      if(OrderSelect(i, SELECT_BY_POS,MODE_TRADES)==false)
         Alert("Cant order select on pos <"+i+">");

      if((OrderType()==OP_BUY)||(OrderType()==OP_SELL))
        {
         int lastorderticket=OrderTicket();
         return (lastorderticket);
        }
     }
   return (0);
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void pong()
  {
   stat(81);
//der pong wird nur geschrieben wenn der Markt offen ist und der Expert aktiviert ist
   if((market_opend("EURUSD")==false))
      return;
   int handle = FileOpen("pong.txt",  FILE_WRITE, "#");
   if(handle<=0)
     {
      Alert("Can´t open Pongfile  "+ AccountInfoString(ACCOUNT_COMPANY)+"> AccountName<"+AccountName()+"> AccountNumber<"+AccountNumber());
     }
   int charsWritten = FileWrite(handle,"test file write");
   if(charsWritten <=0)
     {
      Alert("Cant write to Pongfile  "+ AccountInfoString(ACCOUNT_COMPANY)+"> AccountName<"+AccountName()+"> AccountNumber<"+AccountNumber());
     }
//Print("Write new pong "+TimeToStr(TimeCurrent(),TIME_SECONDS));
   FileClose(handle);
   stat(82);
  }
//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void lock()
  {
//blockiert damit das Monitortool mit dem lesen wartet
   int handle = FileOpen("exporter.lock",  FILE_WRITE, "#");
   FileClose(handle);
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void unlock()
  {
//freigabe, jetzt darf das Monitortool wieder lesen
   FileDelete("exporter.lock");
  }

//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void restartcheck()
  {
//prüft jede 30 Sekunden auf das restart kommando
   if(checkfile("restart.start")>0)
     {
      Print("restart Terminal");

      //und jetzt restarte
      //Startet die Batch Datei RestartTerminal.bat im MT4 Verzeichnis
      ShellExecuteA(0, "open", "RestartTerminal.bat", "/c", 0, 0);

      // Warte 15 Sekunden bis die RestartTerminal.bat ausgefuehrt wird
      Sleep(15000);

      Print("close Terminal");
      // Close Terminal
      int main = GetAncestor(WindowHandleMQL4(Symbol(), Period()), 2);
      PostMessageA(main, 16, 0, 0);
     }
  }



//+------------------------------------------------------------------+
//|                                                                  |
//+------------------------------------------------------------------+
void stat(int value)
  {
   GlobalVariableSet("hist_exp_state", value);


  }
//+------------------------------------------------------------------+
