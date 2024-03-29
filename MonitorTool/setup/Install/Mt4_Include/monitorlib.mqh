//+---------------------------------------------------------------------+
//|                                                   monitorlib.mq4    |
//|                        Copyright 2013-2021, Thomas Nickel.          |
//|                                                                     |
//+---------------------------------------------------------------------+


#property copyright "Copyright 2013-2021, Thomas Nickel. 2.3.2021"
#property link      "x"
//1.24  17.8.20223 add random funciton
//1.23  24.05.2022 if del fails sleep 10 minutes
//1.22  05.01.2022 some code adjustments
//1.21  04.01.2022 bugfix clocom
//1.20  04.01.2021 add delete for .clo, this this delete once,1739
//1.19  13.12.2021 bugfix clocom, looks to comment
//1.18  11.12.2021 add del clocom with magic 99999 for autocreator
//1.17, 7.3.2021 bugfix, returnvalue missing
//1.16, 7.3.2021 add function delete missing eas, but I don´t use this function in historyexporter at the moment
//               tradecopy and portfolio eas are complicated
//1.15, 5.3.2021 delete all trades with <magic>.clo and delete the <magic>.clo file after action
//1.14, 2.3.2021 additional check if market is open
//1.13, 6.2.2021 check market is open
//1.12, 5.2.2021 redesign close all trades function
//1.11, 5.2.2021 text formating, close orders
//1.10, some fixes für SQ4.107, lotsize adjustments
//1.07 add mmRiskperCent...
//1.06



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
 

void DisplayText(string eName, int eYD, int eXD, string eText, int eSize, string eFont, color eColor) {

   ObjectCreate(eName, OBJ_LABEL, 0, 0, 0);
   ObjectSet(eName, OBJPROP_CORNER, 0);
   ObjectSet(eName, OBJPROP_XDISTANCE, eXD);
   ObjectSet(eName, OBJPROP_YDISTANCE, eYD);
   ObjectSetText(eName, eText, eSize, eFont, eColor);
}

 

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

 

int tradecheck(string mn,string ke)
{
//allgemeine funktion die prüft ob der ea-handeln darf

   string magno=mn;
   string such=magno+ke;
   int handle=0;
   tickcounter++;

   if(showfilepathflag==0)
   {
    Print("show filepath");
    DisplayText("path=", 20, 500, "path= "+TerminalPath(), 9, "Verdana", Gold);
    showfilepathflag=1;
   }

  

   if((tickcounter%30==0)&&(checkfile(such+".on")==1))
   {
      //Print("checkon tickcounter="+tickcounter);
      if(laststate!=1)
      {
         handle=FileOpen(such+".on.ok", FILE_BIN|FILE_WRITE);
         FileClose(handle);
         if(checkfile(such+".off")==1)     
              FileDelete(such+".off");

         if(checkfile(such+".off.ok")==1)         
              FileDelete(such+".off.ok");

         DisplayText("statusline", 40, 500, such+" state=on", 9, "Verdana", Gold);
      }
      laststate=1;
   }

  

   if((tickcounter%30==0)&&(checkfile(such+".off")==1))
   {
    //Print("checkoff tickcounter="+tickcounter);
    if(laststate!=2)
      {
       if(checkfile(such+".on")==1)
         FileDelete(such+".on");

       if(checkfile(such+".on.ok")==1)
         FileDelete(such+".on.ok");

         closeallopenorders(magno,ke);
         DisplayText("statusline", 40, 500, such+" state=off", 9, "Verdana", Gold);
      }
      laststate=2;
   }

   if((laststate!=1) &&(laststate!=2))
   {
     DisplayText("statusline", 40, 500, such+" state=waiting", 9, "Verdana", Gold); 
     laststate=-1;

     //Print("EA waiting for on/off <"+such+">");
     return (0);
   }

   if(laststate==1)
      return (1);
   else
      return (0);
}

int closeallopenorders(string magic,string ke)
{
   int magicno=StrToInteger(magic);
   Print("close all orders of magicno+kennung="+magicno+ke);

   for (int i=0; i<OrdersTotal(); i++)
     {
         OrderSelect(i, SELECT_BY_POS,MODE_TRADES);
         
         //falls der markt für das symbol geschlossen dann gehe weiter zum näcshten trade
         if(IsMarketOpen()==false)
           continue;
         
         string suchtext=StringChangeToUpperCase(ke);
         string commenttext=StringChangeToUpperCase(OrderComment());
         int foundpos=StringFind(  commenttext,suchtext, 0);
         int len=StringLen(suchtext);
   
         Print("foundpos="+foundpos+" len="+len +" suchtext="+suchtext);
         
 
         if ((OrderMagicNumber() == magicno)&&((foundpos>-1)&&(foundpos<=len)))
           {

             Print("Order <"+OrderTicket()+"> Magic <"+magicno+"> Comment<"+ke+"> wird geschlossen"); 

            //delete OrderClose
            if (OrderType()==OP_BUY )
               {
                   OrderClose(OrderTicket(),OrderLots(),MarketInfo(OrderSymbol(), MODE_BID),50000);
                  i--;
               }
            else if (OrderType()==OP_SELL )
              {
                  OrderClose(OrderTicket(),OrderLots(),MarketInfo(OrderSymbol(), MODE_ASK),50000);
                  i--;

              }
              else 
              {
               OrderDelete(OrderTicket()); 
               i--;      
              }
           }
           else

          Print("OrderSelect returned the error of ",GetLastError());
      }

     //alle orders wurden gelöscht

   int handle=FileOpen(magic+ke+".off.ok", FILE_BIN|FILE_WRITE);
   FileClose(handle);      
 return (0);
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

  

   int iOrders=OrdersTotal()-1, i;
   for (i=iOrders; i>=0; i--) 
   { 
     //Print("*** append open_exporter");
     OrderSelect(i, SELECT_BY_POS,MODE_TRADES);
     int type   = OrderType();
     bool result = false;
       
     //falls der markt für das symbol geschlossen dann gehe weiter zum näcshten trade
     if(IsMarketOpen()==false)
        continue;
   
     int magic=OrderMagicNumber();
     string comment = OrderComment();
     int ticket=OrderTicket();
     if(ticket==0)
      continue;
    
     GetMarketInfo();
 
     if(((checkfile(magic+".del"))>0)||
        ((checkfile(magic+".clo"))>0)||
        ((magic==99999)&&(checkfile(comment+".delcom"))>0)||
        ((magic==99999)&&(checkfile(comment+".clocom"))>0)||
        (forceflag==1))
      {
      
       Print("delete trade magic="+magic+" ticket="+ticket);
         switch(type)
         {
            
            case OP_BUY       : 
            case OP_SELL      :result=OrderClose(OrderTicket(),OrderLots(),Price[1-OrderType()],giSlippage); 
                               break;

            //Close pending orders
            case OP_BUYLIMIT  :
            case OP_BUYSTOP   :
            case OP_SELLLIMIT :
            case OP_SELLSTOP  : result = OrderDelete( OrderTicket() );
         }
    
         if(result == false)
         {
            Alert("Order " , OrderTicket() , " failed to close. Error: <"+magic+".del"+">" , GetLastError() );
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


bool checkMagicIsInstalled(int magic)
{
   //prüft ob für diese magic ein EA in dem Directory ../experts existiert

   string   file_name;      // variable for storing file names
   string   filter="*.ex4"; // filter for searching the files
   int      size=0;         // number of files
   string   magic_s="";
   //--- receive the search handle in the local folder's root
   long search_handle=FileFindFirst(filter,file_name);
   //--- check if FileFindFirst() executed successfully
   if(search_handle!=INVALID_HANDLE)
     {
      //--- searching files in the loop
      do
        {
         magic_s=IntegerToString(magic,0,' ');
        
         if(StringFind(file_name,magic_s,0)>=0)
         {
           FileFindClose(search_handle);
           return true;
         }
        }
      while(FileFindNext(search_handle,file_name));
      //--- close the search handle
      FileFindClose(search_handle);
     }
     Print("I:Can´t find magic<"+magic+"> in Directory experts, I will delete open trades for this ea");
     return false;
}

bool IsMarketOpen()
{
      int val=MarketInfo(Symbol(), MODE_TRADEALLOWED);
      if(val==1)return true;
      else return false;
}



//+------------------------------------------------------------------+
//| Function..: GetMarketInfo                                        |
//+------------------------------------------------------------------+
bool GetMarketInfo() {
  RefreshRates();
  Price[0]=MarketInfo(OrderSymbol(),MODE_ASK);
  Price[1]=MarketInfo(OrderSymbol(),MODE_BID);
  double dPoint=MarketInfo(OrderSymbol(),MODE_POINT);
  if(dPoint==0) return(false);
  giSlippage=(Price[0]-Price[1])/dPoint;
  return(Price[0]>0.0 && Price[1]>0.0);
}



string StringChangeToUpperCase(string sText) {

  int iLen=StringLen(sText), i, iChar;

  for(i=0; i < iLen; i++) {

    iChar=StringGetChar(sText, i);

    if(iChar >= 97 && iChar <= 122) sText=StringSetChar(sText, i, iChar-32);

  }

  return(sText);

}

 

string OrderError() {
  int iError=GetLastError();
  return(StringConcatenate("Order:",OrderTicket()," GetLastError()=",iError," "));
}

 


double extract_doubleval(string line_str,double minval, double maxval,string mn)
{
   //extract the doubleval
   //RiskInPercent = 2.0

   int posgleich=StringFind(line_str,"=",0);
   string substr_str=StringSubstr(line_str,posgleich+1,0);

   if(substr_str==-1)
     MessageBox(" configuration error cant find '=' in <"+line_str+"> --> EA Stopped",0x00000010);

   double val_double=StrToDouble(substr_str);
 
   if(val_double<minval)
     MessageBox( "value<"+val_double+"> muss groesser<"+minval+"> sein  magic<"+mn+"> EA Stopped !!",0x00000010);

   if(val_double>maxval)
     MessageBox( "value<"+val_double+"> muss kleiner<"+maxval+"> sein  magic<"+mn+">  EA Stopped !!",0x00000010);    

   return (val_double);         
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