//+---------------------------------------------------------------------+
//|                                                   monitorlib.mq4    |
//|                        Copyright 2013-2021, Thomas Nickel.          |
//|                                                                     |
//+---------------------------------------------------------------------+


#property copyright "Copyright 2013-2021, Thomas Nickel. 2.3.2021"


//1.0 16.08.2023 new version


int checkfile(string fnam)
{

//prüft ob ein file da ist

//1: file vorhanden

    int handle=FileOpen(fnam, FILE_BIN|FILE_READ);
    if(handle>0)
    {
     //file vorhanden
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



double extract_doubleval(string line_str,double minval, double maxval,string mn)
{
   //extract the doubleval
   //RiskInPercent = 2.0

   int posgleich=StringFind(line_str,"=",0);
   string substr_str=StringSubstr(line_str,posgleich+1);

   if(substr_str==-1)
     MessageBox(" configuration error cant find '=' in <"+line_str+"> --> EA Stopped",0x00000010);

   double val_double=StringToDouble(substr_str);
 
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