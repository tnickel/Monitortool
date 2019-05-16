package mainPackage;

public interface GC
{
	int ONLINEMODE = 1;
	int TESTMODE=0;
	int MAXLOW=8;
	int DOWNLOADDELAY=300;
	int RANDOMSLEEP=500;
	//Jede Html-Seite muss mindestens 25k haben
	int HTMLLENGTHMIN=25000;
	

	//dies ist die Startsumme in Euro mit der gehandelt wird
	float STARTSUMME = 1000000;
	
	// globale Konstanten
	int maxthreads = 50;
	int MAXWEBSIDES = 60000;
	int TDBLIMIT = 7000;
	int USERLIMIT = 95000;
	int OBSERVERLIMIT = 100;
	int ALLFLAG=1000;

	
	//stati
	int STATUSBAD = 5000;
	// threadtypen
	int MODE_AKTIENTHREADS = 1000;

	// Konstanten für BAWorkUser
	int MODE_OBSERVE = 8000;
	int MODE_ALL = 0;
	int NOWARNING = 1000;
	int NUR_AKTIENTHREADS_UPDATEN = 1000;
	// durchläuft nur die threadseiten wo der username vorkommt
	int MODE_ONLY_THREAPAGES_WITH_USERNAME = 2000;

	// Konstanten für bestimmte Zwecke
	// durchläuft nur threads wo breakid gesetzt ist
	int MODE_ONLYBREAKID = 5;
	int MODE_ONLYAKTIENTHREADS = 6;
	int MODE_ONLYAKTIENTHREADSKEINEDAX = 6;
	int TURBOMODE =1000;

	// openflags für db´s
	int DB_RW = 0;
	int DB_READONLY = 2000;
	int DB_READONLY_OBSERVE = 8000;
	// maximal 300 objekte werden beobachtet
	int MAXOBSERVE = 300;
	
	int PDBALLPOST = 0011;
	int PDBUSER = 0022;

	// objektdefinitionen
	int WebinfoObj = 1000;
	int ThreadDbObj = 2000;
	int AktDbObj = 3000;
	int KursDbObj = 4000;
	int UserDbObj = 5000;
	int YuppieDbObj = 6000;
	int KursiconDbObj = 7000;
	int KursvalueNativeDbObj = 8000;
	int UserSummenGewinneDBI = 9000;
	int UserGewStrategieObjII = 10000;
	int SymbErsetzungsDB = 11000;
	int ThreadAttribObjI = 12000;
	int CompressedThreadDB = 13000;
	int SlideruebersichtDB = 14000;
	int UsernameErsetzungsOb= 15000;
	int KurseDB=16000;
	int PrognoseDbObj=17000;
	int MidDbObj=18000;
	int UserCharakterObj=19000;
	int EventDbObj=20000;
	int UeberwachungDbObj=21000;
	int BoersenblattDbObj=22000;
	int UserPostingverhaltenDbObj=23000;
	int KeyDbObj=24000;
	int ChampionDbObj=25000;
	int BBeventDbObj=26000;

	
	// Stringdefinitionen
	String AlleObserveuser="AlleObserveuser";
	
	//Delimiter
	String delimiter="@@@@@";
	
	// klassen
	int HtmlUserElem = 1001;
	int HtmlPostElem = 1002;

	// threadseitentype
	int AktienThread = 1000; // aktientype mit isin, symbol etc
	int Sofathread = 1002; // dies ist kein Aktientype
	int ThreadFehlerhaft = 5000; // falls der Thread so fehlerhaft ist das der
		// Steitentype nicht ermittelt werden kann
	
	//Masteruser oder Standartuser
	int UsertypeStandart=0;
	int UsertypeMasteruser=1;
	
	int AktSymbol = 1000;
	int SymbNum = 1001;
	String startdatum = "1.1.1999";
	String enddatum="1.1.2050";

	// definitionen
	int DEFAULT = 0;
	int LOESCHE = 100;
	int SUCHMODE_UEBERSPRINGE_FEHLER = 2000;
	int KEINE_EINZELGEWINNE = 5000;
	int USERGEWINNRANG=1;
	int KEIN_USERGEWINNRANG=0;

	// definitionen für den Slider
	int SLIDERINDEX20TAGE = 1;
	int SLMAXSLIDER = 200;
	int ALTERFLAG=1;
	int TURBOFLAG=1;
	int CHECKONLYFLAG=1;

	// definitonen für attrib
	int ATTRIB_LASTPOSTID = 0;
	int ATTRIB_USERIN20TAGEN = 1;
	int ATTRIB_MITTLERERRANK = 2;
	int ATTRIB_anzGuteUser =   3;
	int ATTRIB_anzSchlechteUser=4;
	int ATTRIB_anzBadUser=5;
	int ATTRIB_anzSchlechtePostings=6;
	int ATTRIB_anzGutePostings=7;
	int ATTRIB_anzNeueGuteUser=8;
	int ATTRIB_anzNeueSchlechteUser=9;
	int ATTRIB_anzNeueBadUser=10;
	
	int FILTER_MIN_ZUGRIFFE = 10;
	int FILTER_MIN_YEAR = 2006;
	int FILTER_MIN_POSTINGS = 500;
	String rootpath = "m:\\offline";
	String pdfzielbase= "m:\\Mail\\pdf";
	String textzielbase = "m:\\Mail\\text";
	String mailbase= "m:\\ThunderbirdPortable\\Data\\profile\\Mail\\pop.gmx.net\\Inbox.sbd";
	String acroreaderbase="C:\\Program Files (x86)\\Adobe\\Reader 10.0\\Reader\\AcroRd32.exe";
	String notepadbase="c:\\Windows\\notepad.exe";
	//String acroreaderbase="C:\\Program Files\\Adobe\\Acrobat 7.0\\Reader\\AcroRd32.exe";
	//String acroreaderbase="C:\\Programme\\Adobe\\Reader 9.0\\Reader\\AcroRd32.exe";
	//String rootpath="f:\\offline_test";

}
