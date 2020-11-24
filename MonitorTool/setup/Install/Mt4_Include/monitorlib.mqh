//+------------------------------------------------------------------+
//|                                                   monitorlib.mq4 |
//|                        Copyright 2018, Thomas Nickel. 09.08.2018 |
//|                                                                  |
//+------------------------------------------------------------------+



#property copyright "Copyright 2013, Thomas Nickel. 16.07.2013"
#property link      "x"

//Version 1.07 add mmRiskperCent...
//Version 1.06
//Version 1.10, some fixes für SQ4.107, lotsize adjustments
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

 

int closeAll()

{

   //alle offenen order werden geschlossen

   while(OrdersTotal()>0)

   {

      Print("NOCH ORDERS VORHANDEN !!! , weiter CLOSEVERSUCH");

               for (int i=0; i<OrdersTotal(); i++)

                    {

                     OrderSelect(i, SELECT_BY_POS,MODE_TRADES);

         Print("CLOSEALL Order <"+OrderTicket()+"> Magic <"+OrderMagicNumber()+"> Comment<"+OrderComment()+"> wird geschlossen"); 

 

            //delete OrderClose

            if (OrderType()==OP_BUY )

               {

                   OrderClose(OrderTicket(),OrderLots(),MarketInfo(OrderSymbol(), MODE_BID),50000);

                  //OrderClose(OrderTicket(),OrderLots(),Bid,0);

                  i--;

               }

           

            else if (OrderType()==OP_SELL )

              {

             

                  OrderClose(OrderTicket(),OrderLots(),MarketInfo(OrderSymbol(), MODE_ASK),50000);

                  //OrderClose(OrderTicket(),OrderLots(),Ask,0);

                  i--;

              }

           

             else 

              {

               OrderDelete(OrderTicket()); 

               i--;      

                 }

                    Sleep(1000);    

               }

  }

   
 return (0);
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