//+------------------------------------------------------------------+
//|                                     tnickel_TickDataExportEA.mq4 |
//|                                                                  |
//|                           EA to export tick data from MetaTrader |
//|                              Tick data are exported to directory |
//|              /MetaTrader directory/tester/files/XXX_TickData.csv |
//+------------------------------------------------------------------+
// Version 1.0.2 , 9.10.2015

#property copyright "tnickel"
#property link      "x"

//gibt an wieviele Ticks zwischengespeichert werden
extern int maxstore=50;
extern int magicNumber = 00;
extern double Lots = 0.1;

int storepos=0;
int previousVolume = 0;
int starttickcount=0;
string time_a[100];
double ask_a[100];
double bid_a[100];
int vol_a[100]; 


void init()
{
   printf("Aufzeichnung für symbol<"+Symbol()+"> gestartet");
   starttickcount=GetTickCount();
   storepos=0;
   //init array
   cleanMem();
   
   
   //sync
   datetime time1=TimeCurrent();
   datetime time2=0;
   while(5==5)
   {
    time2=TimeCurrent();
 
    //Falls die sekunde umgesprungen ist
    if(time1!=time2)
      {
           starttickcount=GetTickCount();     
           break; 
      }  
       Sleep(100);
   }


   int handle = FileOpen(StringConcatenate(Symbol(),"_TickData.csv"), FILE_READ |FILE_WRITE, ",");
   if(handle>0) 
   {
      FileSeek(handle,0,SEEK_END);
      int size=FileSize(handle);

      //Falls das File noch leer ist dann schreibe die Kopfzeile
      if(size==0)
        FileWrite(handle, "Time","Ask","Bid","Volume");
      FileClose(handle);
   }
   
   
   
}

//+------------------------------------------------------------------+

int start() 
{
   if(maxstore>100)
   {
       MessageBox("maxstore has a limit of 100","Info",IDABORT);
       maxstore=100;
   }

   int volume = Volume[0] - previousVolume;
   if(volume <= 0) 
   {
      volume = Volume[0];
   }
   previousVolume = Volume[0];
 
  //Die Zeit in sekunden ermitteln, es wird die Uhr vom Rechner verwendet
  string timex=TimeToStr(TimeCurrent(), TIME_DATE|TIME_SECONDS)+".";
 
  //ticktime ermitteln

  uint ticktime=(GetTickCount()-starttickcount)/1000;
  //ticktime=StringSubstr( ticktime, StringLen(ticktime)-3, 3);
  timex=StringConcatenate(timex,ticktime);

 
   store(timex,Ask,Bid,volume);
 
   return (0);
}

int deinit()
{
   printf("deinit für symbol<"+Symbol()+"> ausgeführt");
   storeOnFile();
   return 0;
}

void store(string timex,double ask, double bid, int volume)
//forceflag==1 dann wird auf jeden fall der puffer geleert und geschrieben
{
   //falls gepuffert wird
   if(storepos<maxstore)
   {
      time_a[storepos]=timex;
      ask_a[storepos]=ask;
      bid_a[storepos]=bid;
      vol_a[storepos]=volume;
      storepos++;
      return;
   }
   else
     //speichert alles im Filesystem
     storeOnFile();
    
   return;
}

void storeOnFile()
{
      //speichert den internen speicher auf harddisk
      int handle = FileOpen(StringConcatenate(Symbol(),"_TickData.csv"), FILE_READ | FILE_WRITE, ",");
      if(handle>0) 
      {
         //gehe ans Ende des Files
         FileSeek(handle,0,SEEK_END);
        
         for(int i=0; i<storepos; i++)
            FileWrite(handle, time_a[i], ask_a[i], bid_a[i],vol_a[i]);
           
         FileClose(handle);
         //storepos wieder auf Null setzen nach dem Speichern und mem löschen
         cleanMem();
         storepos=0;
      }
}

void cleanMem()
{
   for(int i=0; i<100; i++)
   {
      time_a[i]="";
      ask_a[i]=0;
      bid_a[i]=0;
      vol_a[i]=0;
      return;
   }
}




