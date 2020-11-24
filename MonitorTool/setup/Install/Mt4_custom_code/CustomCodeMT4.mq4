/**
 * function that is called on init
 */
void customInit() {
lastmarketpos=0;
}

/**
 * function that is called on start. If it returns false, the processing will end.
 * You can use it for your own custom checks.
 */
bool customStart() {
   int marketpos=getMarketPosition();

   if(marketpos!=lastmarketpos)
   {
      lastmarketpos=marketpos;
      if(emailnotifyOnTradePatrik==true)
      {
         if((marketpos==1)||(marketpos==-1))
            SendMail(CustomComment+" - Order opened", getNotificationText());
        
         if(marketpos==0)
          SendMail(CustomComment+" - Order closed", getNotificationText());
      }
   }

   return(true);
}