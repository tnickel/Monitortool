//+---------------------------------------------------------------------+
//|                                              HistoryExporter.mq4    |
//|                        Copyright 2015-2018 Thomas Nickel ,17.04.2018 |
//|                                                                     |
//+---------------------------------------------------------------------+
#import "shell32.dll"
int ShellExecuteA(int hWnd, string Verb, string File, string Parameter, string Path, int ShowCmd);
#import "user32.dll"
int GetAncestor(int a0, int a1);
int PostMessageA(int hWnd,int Msg,int wParam,int lParam);
#import


#property copyright "Copyright 2013-2021, Thomas Nickel"
#property link      "x"
#include <monitorlib.mqh>
string version_str = "1.36";
// 1.36 2.03.2021 Close at 21Clock at friday end
// 1.35 25.2.2021 Make kontoinformation shorter
// 1.34 15.2.2021 write pong only if market is open and expert Advisors are switched on
// 1.33 6.2.2021 check market is open
// 1.32 5.2.2021 use fridayend.txt to close all trades and metatrader on friday
// 1.31 4.2.2021 delete orders if <magic>.del was found
// 1.30 4.2.2021 delete Trades where the ea already has been deleted
// 1.29 3.2.2021 Dont store deleted Trades

//+------------------------------------------------------------------+
//| expert initialization function                                   |
//+------------------------------------------------------------------+
datetime lastchecktime=0;


double spreadarray[1000];
int spreadcounter=0;


//Zeitintervalle wann das nächste mal gechecked wird
extern int shortcheckintervall =  30;     //30 sekunden
extern int longcheckintervall  =  3600;   //1 stunde


//dies sind die counter für die add-historyexporter
int lastlongappendpos=0;
int lastshortappendpos=0;
int lastorderticket_g=0;

//allgemeiner counter
int histanz=0;
int openanz=0;
int fileopenanz=0;

//zeiten wann letztes mal gechecked wurde
int lastcheckedshorttime=0;
int lastcheckedlongtime=0;
int lastcheckpongtime=0;

string lastshortchecktime_str="init";
string lastlongchecktime_str="init";


int OnInit()
  {
   Print(" Historyexporter OnInit-Start:AccountCompany<"+AccountCompany()+"> AccountName<"+AccountName()+"> AccountNumber<"+AccountNumber()+">");
   Print("historyexporter version="+version_str);
   //setze den timer auf z.b. 30sekunden=shortcheckintervall
   //und da wird auch gepongt
   initTimer();
   //fourceflag=0, falls fourceflag==1 ist dann wird immer gelöscht auch wenn kein *.del vorhanden ist
   
   if(IsMarketOpen()==true)
      deleteOpenDelTrades(0);
 
   datetime currtime=TimeCurrent();
   //sage auf jeden Fall beim ersten Aufruf schreiben
   lastcheckedlongtime=currtime-(longcheckintervall*2);
 
   
   //prüft nach ob neues konto, ggf. die Trades sichern
   checkNewKonto();
   gibKontoinfoaus();
   
   //den historyexporter schreiben
   lastlongappendpos=AppendHistory("history.txt",1,lastlongappendpos);

   //die laenge merken
   lastshortappendpos=lastlongappendpos;
   //Print("lastshortappend="+lastshortappend);

   //die Kopfinfozeile aktualisieren
   showinitheadline();
   showlastaccess();
   
   lastorderticket_g=WriteOpenHistory("history_open.txt",lastorderticket_g);
 
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
 
    
    if((DayOfWeek()==5 || DayOfWeek()==6)) 
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
      histanz=AppendHistory("history_expired_"+ TimeToStr(TimeCurrent(),TIME_DATE)+".txt",1,0);
     
      int handle=FileOpen("closeAccount.ok", FILE_BIN|FILE_WRITE);
      FileClose(handle);
      Alert("Please generate a new account and Restart Metatrader");
      return;
   }
   
    showheadline(spread);
   
    //setze den Pong
    pong();
    
   
      //shortcheck
    
      lastshortchecktime_str=TimeToStr(currtime, TIME_SECONDS);
      lastshortappendpos=AppendHistory("history_small.txt",0,lastshortappendpos);
      histanz=lastshortappendpos;
      lastorderticket_g=WriteOpenHistory("history_open.txt",lastorderticket_g);
      //Print(TimeToStr(currtime)+"check shortintervall");  
      
      showlastaccess();
      
      //restartcheck
      restartcheck();
   
    
    if((currtime)>lastcheckedlongtime+longcheckintervall)
    {
      //longcheck
      lastcheckedlongtime=currtime;
      lastlongchecktime_str=TimeToStr(currtime, TIME_SECONDS);
      lastlongappendpos=AppendHistory("history.txt",1,lastlongappendpos);
      histanz=lastlongappendpos;
      if(checkfile("history_small.txt")==1)
         FileDelete("history_small.txt");
         
      showlastaccess();   
         
      histanz=lastlongappendpos;
      //Print(TimeToStr(currtime)+"export longhistory");  
      
      deleteOpenDelTrades(0);
      //falls endfriday.txt gefunden wird dann werden alle trades am freitag um 21 Uhr geschlossen
      //und der MT4 wird beendet
      fridayEndCheck();
    }
   /* else
     {
       Print("waitinglong ="+((lastcheckedlongtime+longcheckintervall)-currtime));
     }
 */
    showheadline(spread);
  
  
//+------------------------------------------------------------------+
}




void showheadline(double spread)
{
   DisplayText("m1", 10, 10, "tc=" + tickcounter +" openanz= "+openanz+" histanz= "+histanz+" spr="+spread, 7, "Verdana", Gold);
   DisplayText("m4", 55, 10, "path="+ TerminalPath(), 7, "Verdana", Gold);
}
void showinitheadline()
{
   DisplayText("m2", 25, 10, "leverage=" + AccountLeverage()+" Stoplevel="+AccountStopoutLevel(),7, "Verdana", Gold);
}
void showlastaccess()
{
   DisplayText("m3", 40, 10, "shortwrite=" +   lastshortchecktime_str+"     longwrite="+lastlongchecktime_str,7, "Verdana", Gold);
}

void initTimer()
{
   //set the timer for the pong/shortcheckintervall
   //in problemcase it will retry 5 times
   
   bool timersuccess=false;
   int anztry=0;
   int errorcode=0;

   while((timersuccess= EventSetTimer(shortcheckintervall))==false)
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
      Print("Timer for shortcheckintervall successfull set after trys<"+anztry+">");
}

bool checkNewKonto()
{
   //prüft nach ob dies ein neues Konto ist
   //erst mal die neue history schreiben
   AppendHistory("history_init_check.txt",1,0);
   
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
         FileCopy("history.txt",0,"history_expired_"+ TimeToStr(TimeCurrent(),TIME_DATE)+".txt",FILE_REWRITE);
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

void gibKontoinfoaus()
{
          //und gib die Kontoinformationen in einem File aus
         string accountinfo="";
         if(IsDemo()==true)
           accountinfo="Demoaccount";
         else
           accountinfo="Realaccount";
         
         FileDelete( "KontoInformation.txt"); 
         int handle = FileOpen("KontoInformation.txt",  FILE_WRITE|FILE_READ|FILE_CSV, "#");  
         FileSeek(handle,0,SEEK_END);
         FileWrite(handle,TimeToStr(TimeLocal(),TIME_DATE|TIME_MINUTES),AccountName(),AccountNumber(),AccountServer(),AccountBalance(),AccountCompany(),AccountStopoutLevel(),AccountBalance(),accountinfo);
         FileClose(handle);
}

int AppendHistory(string fullfnam,int fullflag,int lastcounter)
// er kann doch an der ordernummer feststellen ob was neues hinzugekommen ist
{
  //lastcounter: ist der index der letztes mal geschrieben wurde
  //bei fullflag wird alles geschrieben also ohne append
  int handle=0;
  int hanz=0;
 
  
   //anzahl der  orders ermitteln (geschlossene)
   hanz=OrdersHistoryTotal();
   
   //nix schreiben da anzahl gleich
   if(fullflag==0)
    if(lastcounter==hanz)
     return(lastcounter);
    
  lock();
  if(fullflag==1)
    //alles schreiben
   {
    lastcounter=0;
     //Print("*** write full historyexporter <"+fullfnam+"> von<0> bis<"+histanz+">");
    //das alte löschen
    handle = FileOpen(fullfnam,  FILE_WRITE, "#");  
   }
  else
   {
      //append
      handle=FileOpen(fullfnam,FILE_READ |FILE_WRITE  , "#");  
      //Print("*** append historyexporter <"+fullfnam+"> von<"+lastcounter+"> bis<"+histanz+">");
   }
  if(handle>0) 
     FileSeek(handle,0,SEEK_END);  
 
  
      
  for (int i=lastcounter; i <hanz; i++) 
  { 
     OrderSelect(i, SELECT_BY_POS,MODE_HISTORY);

     //nur buy und sell order in history_small schreiben
     if((OrderType()!=OP_BUY)&&(OrderType()!=OP_SELL))
        continue;
   
     //falls der Ea gelöscht wurde dann nimm den nicht mehr in der liste auf
     int magic=OrderMagicNumber();
     if(checkfile(magic+".del")>0)
       continue;
       
     //OrderPrint();   
     //Ordernumber, OrderProfit, Orderlots, OrderOpentime, OrderCloseTime, OrderComment,AccountNumber,Symbol,type
     //0#-59.3#1#2012.11.21 15:25:11#1970.01.01 00:00:00##480681
     
      string closetime=TimeToStr(OrderCloseTime(), TIME_DATE|TIME_SECONDS);
      string opentime= TimeToStr(OrderOpenTime(), TIME_DATE|TIME_SECONDS); 
      FileWrite(handle,OrderMagicNumber(),OrderType(),OrderOpenPrice(),OrderClosePrice(),OrderCommission(),OrderSwap(),OrderProfit(),OrderLots(),opentime,closetime,OrderComment(),AccountNumber(),OrderTicket(),OrderSymbol(),OrderType());
  }
 lastcounter=hanz;
 FileClose(handle);   
 unlock();
 return (lastcounter);
}

int WriteOpenHistory(string fullfnam,int lastorderticket)
//schreibt die openhistory immer, schreibt aber nur wenn sich was geändert hat
//fullfnam: namen des FileSeek
//lastorderticket:ticketnummer der letzten order
//rückgabewert = orderticket der neusten order
{
   int handle=0;
   
   //hole die letzte ticketnummer der open order
   int lastticketnumber=getLastOpenOrderTicketNumber();

   //falls kein open trade da ist dann 0
   if(lastticketnumber==0)
   {
      //Print("lastticketnumber=0");
      
      //das alte löschen
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
 
  Print("änderung openhistory"+lastticketnumber+" != "+lastorderticket);
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
  for (int i=0; i <allopen; i++) 
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

int getLastOpenOrderTicketNumber()
{
    int oanz=OrdersTotal();
    if(oanz==0)
      return (0);
    
     //holt die letzte open order die kein stop-buy oder stop-sell ist
     for (int i=oanz-1; i >0; i--) 
     { 
         //Print("*** append open_exporter");
         OrderSelect(i, SELECT_BY_POS,MODE_TRADES);
         
         if((OrderType()==OP_BUY)||(OrderType()==OP_SELL))
         {
            int lastorderticket=OrderTicket();
            return (lastorderticket);
         }
     }
     return (0);
}

void pong()
{
   //der pong wird nur geschrieben wenn der Markt offen ist und der Expert aktiviert ist
   if((IsMarketOpen()==false)||(IsExpertEnabled()==false))
      return;
   int handle = FileOpen("pong.txt",  FILE_WRITE, "#");
   if (handle<=0)
   {
      Alert("Can´t open Pongfile  "+ AccountCompany()+"> AccountName<"+AccountName()+"> AccountNumber<"+AccountNumber());
   }
   int charsWritten = FileWrite(handle,"test file write");
   if (charsWritten <=0)
   {
      Alert("Cant write to Pongfile  "+ AccountCompany()+"> AccountName<"+AccountName()+"> AccountNumber<"+AccountNumber());
   }
   //Print("Write new pong "+TimeToStr(TimeCurrent(),TIME_SECONDS));
   FileClose(handle);
}

void lock()
{
   //blockiert damit das Monitortool mit dem lesen wartet
   int handle = FileOpen("exporter.lock",  FILE_WRITE, "#");  
   FileClose(handle);
}

void unlock()
{
   //freigabe, jetzt darf das Monitortool wieder lesen
   FileDelete("exporter.lock");
}

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
    int main = GetAncestor(WindowHandle(Symbol(), Period()), 2);
    PostMessageA(main, 16, 0, 0);
   }
}

void fridayEndCheck()
{
   //if friday 21clock
   //all trades will be closed
   //the terminal will endet
  
   int w=DayOfWeek();
   Print("day="+w);
   int h=Hour();
   
   if(checkfile("fridayend.txt")>0)
   {
   if((w==5)&&(h>20)&&(h<=23))
     {
      //trades löschen
      Alert("FridayEnd, close all and exit in 60sec");
      //delete all trades with forceflag
      deleteOpenDelTrades(1);
       Sleep(60000);
      //metatrader beenden
      TerminalClose(99);
     }
   }
}


