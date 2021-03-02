//+---------------------------------------------------------------------+
//|                                                   monitorlib.mq4    |
//|                        Copyright 2013-2021, Thomas Nickel.          |
//|                                                                     |
//+---------------------------------------------------------------------+


#property copyright "Copyright 2013-2021, Thomas Nickel. 2.3.2021"
#property link      "x"

//1.14, 2.3.2021 additional check if market is open
//1.13, 6.2.2021 check market is open
//1.12, 5.2.2021 redesign close all trades function
//1.11, 5.2.2021 text formating, close orders
//1.10, some fixes für SQ4.107, lotsize adjustments
//1.07 add mmRiskperCent...
//1.06

//+------------------------------------------------------------------+
//| defines                                                          |
//+------------------------------------------------------------------+
// #define MacrosHello   "Hello, world!"
// #define MacrosYear    2005



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
   //all open trades with a file <magic>.del was found in
   //mql4/files/<magic>.del
   //will be deleted
   //the <magic>.del wsa generated by the monitortool to indicate the deleted eas
   //if forceflag==1, than all trades will be closed

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
     int ticket=OrderTicket();
     if(ticket==0)
      continue;
     Print("delete trade magic="+magic+" ticket="+ticket);
     GetMarketInfo();
 
     if(((checkfile(magic+".del"))>0)||(forceflag==1))
      {
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
            Sleep(3000);
         } 
         Print("Order ticket<"+ticket+"> magic<"+magic+"> was successfully deleted because I found <"+magic+".del"+">"); 
      }   

    }
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