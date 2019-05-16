package db;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.IsValid;
import hilfsklasse.Tools;
import hilfsklasse.Tracer;
import interfaces.DBinterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import kurse.KursDbObj;
import kurse.KursIconObj;
import kurse.KursvalueNativeDbObj;
import mainPackage.GC;
import objects.AktDbObj;
import objects.BBeventDbObj;
import objects.BadObjectException;
import objects.BoersenblattDbObj;
import objects.ChampionDbObj;
import objects.EventDbObj;
import objects.KeyDbObj;
import objects.MidDbObj;
import objects.Obj;
import objects.PrognoseDbObj;
import objects.SymbolErsetzungsObj;
import objects.ThreadDbObj;
import objects.UeberwachungDbObj;
import objects.UserCharakterDbObj;
import objects.UserDbGewinnZeitraumObjI;
import objects.UserDbObj;
import objects.UserGewStrategieObjII;
import objects.UserPostingverhaltenDbObj;
import objects.UsernameErsetzungsObj;
import objects.YuppieDbObj;
import slider.SlideruebersichtObj;
import attribute.ThreadAttribObjI;

//Spezifiziert eine Datenbankklasse
//Eine Datenbankklasse kann Datenobjekte handeln
//Eine Ansammlung von Datenobjekten bildet dies DB-Class.
//Die DB-Class bietet Funktionen um einzelne Datenelemente zu handeln
//z.B. hole Element, hole nächstes Element, reset...
//Dies hab überhaupt nix mit Webseiten zu tun.

abstract public class DB implements DBinterface
{

	// static Prop p = new Prop();
	static String rootpath = GC.rootpath;
	static private BufferedReader inf_p = null;

	private String filename_glob = null;
	private String speicherfile_glob = null;
	private int durchlaufmode = 0;
	public ArrayList<Obj> dbliste = new ArrayList<Obj>();
	// der Masterindex gibt an, an welcher Position sich das pivotelement
	// der Zeile befindet. Das Pivotelement ist eine eindeutige Kennung

	private int aktzaehler = 0, maxzaehler = 0;
	private int objclass = 0;
	protected String infostring_g="";
	
	
	abstract public void postprocess();
	public DB()
	{
		// System.out.println("DB-constructor leer");
	}

	public DB(String file, String kennung, int noerrorflag)
	{
		// System.out.println("DB-constructor load file");
		LoadDB(file, kennung, noerrorflag);
	}

	public String getInfostring()
	{
		return infostring_g;
	}
	public void setInfostring(String istring)
	{
		infostring_g= new String(istring);
	}
	public int calcHoursOfSpeicherfile()
	// hier möchte man wissen wann das datenbankfile letztes mal geschrieben
	// wurde
	// also vor wieviel Tagen
	{
		int ret = FileAccess.calcHours(speicherfile_glob);
		return ret;
	}

	public String GetDbFilename()
	{
		return (filename_glob);
	}

	public boolean LoadDB(String spfile, String kennung, int noerrorflag)
	// masterpfad des Datenbankfiles
	// das Datenbankfile sollte vorhanden sein. Aus dem Masterfile
	// liest die Klasse auch die Konfiguration
	{
		if (speicherfile_glob != null)
		{
			// System.out.println("DB-File constructor, db schon geladen");
			return true;
		}
		if(spfile.contains(":"))
			Tracer.WriteTrace(10, "GC.rootpath wird erst hier gesetzt-> stop");
		
		// System.out.println("DB-File constructor, lade");
		Tracer.WriteTrace(50, this.getClass().getName() + "LoadDB");
		if (kennung == null)
			speicherfile_glob = "\\db\\" + spfile + ".db";
		else if (spfile.toLowerCase().contains("summengewinnedb"))
		{
			speicherfile_glob = "\\db\\" + spfile + "\\" + kennung + ".db";

		} 
		else if (spfile.toLowerCase().contains("foundpattern"))
		{
			speicherfile_glob = "\\db\\" + spfile + "\\" + kennung + ".db";

		} 
		else if (spfile.toLowerCase().contains("gewinnverlaufdb"))
		{
			speicherfile_glob = "\\db\\" + spfile + "\\" + kennung + ".db";

		} 
		else if (spfile.toLowerCase().contains("attribute") == true)
		{
			speicherfile_glob =  spfile + "\\" + kennung + ".db";
		} else
		{
			if (kennung.contains("MID"))
				speicherfile_glob = "\\db\\kurse\\" + kennung + ".db";
			else
				speicherfile_glob = "\\db\\kurse\\" + kennung + ".csv";
		}
		speicherfile_glob = speicherfile_glob.toLowerCase();
		filename_glob = GC.rootpath + speicherfile_glob.toLowerCase();

		String klassenname = this.getClass().getName();
		if (klassenname.equals("stores.UserDB"))
			objclass = GC.UserDbObj;
		else if (klassenname.equals("stores.ThreadsDB"))
			objclass = GC.ThreadDbObj;
		else if (klassenname.equals("stores.AktDB"))
			objclass = GC.AktDbObj;
		else if (klassenname.toLowerCase().contains("kursedb"))
			objclass = GC.KursDbObj;
		else if (klassenname.equals("stores.YuppieDB"))
			objclass = GC.YuppieDbObj;
		else if (klassenname.toLowerCase().contains("kursicondb"))
			objclass = GC.KursiconDbObj;
		else if (klassenname.toLowerCase().contains("kursvaluenativedb"))
			objclass = GC.KursvalueNativeDbObj;
		else if (klassenname.toLowerCase().contains("summengewinnedbi"))
			objclass = GC.UserSummenGewinneDBI;
		else if (klassenname.equals("stores.UserGewStrategieDB2"))
			objclass = GC.UserGewStrategieObjII;
		else if (klassenname.equals("stores.SymbErsetzungsDB"))
			objclass = GC.SymbErsetzungsDB;
		else if (klassenname.equals("attribute.ThreadAttribStoreI"))
			objclass = GC.ThreadAttribObjI;
		else if (klassenname.equals("stores.SlideruebersichtDB"))
			objclass = GC.SlideruebersichtDB;
		else if (klassenname.equals("stores.UsernameErsetzungsDB"))
			objclass = GC.UsernameErsetzungsOb;
		else if (klassenname.equals("stores.PrognosenDB"))
			objclass = GC.PrognoseDbObj;
		else if (klassenname.equals("stores.MidDB"))
			objclass = GC.MidDbObj;
		else if (klassenname.equals("stores.UserCharakterDB"))
			objclass = GC.UserCharakterObj;
		else if (speicherfile_glob.toLowerCase().contains("bbevent") == true)
			objclass = GC.BBeventDbObj;
		else if (speicherfile_glob.toLowerCase().contains("events") == true)
			objclass = GC.EventDbObj;
		else if (speicherfile_glob.toLowerCase().contains("ueberwachung") == true)
			objclass = GC.UeberwachungDbObj;
		else if (speicherfile_glob.toLowerCase().contains("boersenblaetter") == true)
			objclass = GC.BoersenblattDbObj;
		else if (speicherfile_glob.toLowerCase().contains("userpostingverhaltendb") == true)
			objclass = GC.UserPostingverhaltenDbObj;
		else if (speicherfile_glob.toLowerCase().contains("keydb") == true)
			objclass = GC.KeyDbObj;
		else if(speicherfile_glob.toLowerCase().contains("champion")==true)
			objclass = GC.ChampionDbObj;
			else if (speicherfile_glob.contains("xxx") == true)
			objclass = GC.WebinfoObj;
		
		else
		{
			Tracer.WriteTrace(10, this.getClass().getName()
					+ "E: unbekannte datenbank <" + speicherfile_glob
					+ "> klassenname<" + klassenname + ">");
			return false;
		}
		// warnungen werden nur ausgegeben wenn dies flag nicht gesetzt ist

		if (ReadDB(noerrorflag) == false)
			if (noerrorflag == 0)
				Tracer.WriteTrace(20, this.getClass().getName() + "W:<"
						+ speicherfile_glob + "> not available 02");
		return true;
	}

	private boolean ReadDB(int noerrorflag)
	{
		// ist im Augenblick nur für das objekt "WebobserveObj" ausgelegt
		int erstezeileflag=0;
		
		if(filename_glob==null)
			Tracer.WriteTrace(10, "Error: internal filename_glob==null");
		
		if (FileAccess.FileAvailable(filename_glob) == false)
		{
			if (noerrorflag == 0)
				System.out.println("DB open error filname <" + filename_glob
						+ "> nicht verfügbar");
			return false;
		}

		inf_p = FileAccess.ReadFileOpen(filename_glob, "UTF-8");
		int objcounter = 0;
		String zeile = "";
		String lastzeile="";
		try
		{
			
			while ((zeile = inf_p.readLine()) != null)
			{
				//Tracer.WriteTrace(20, "DBzeile=<"+zeile+">");
					
				if(zeile.length()>500)
				{
					Tracer.WriteTrace(10, "Error: threadsdb zeilenlaenge<"+zeile.length()+"> zeile zu lang<"+zeile.length()+"> ->ignorieren <"+zeile+">");
					continue;
				}
				//plausicheck
				if((erstezeileflag==0)&&(zeile.length()>10))
				{
					if((zeile.contains("****")==true)||(zeile.contains("Date,Open")==true))
						erstezeileflag=1;
					else
					{
						Tracer.WriteTrace(20, "Error: Datei <"+filename_glob+">aufbaufehler");
						inf_p.close();
						return false;
					}
						
				}
				//Tracer.WriteTrace(20, "class<"+this.getClass().getSimpleName()+"> lese: anz<"+objcounter+"> <"+SG.calcAnfangsstring(zeile,30));
				
				objcounter++;
				if (objclass != GC.KursvalueNativeDbObj)
					if (objcounter % 1000 == 0)
						System.out.println("eingelesene objekte =<"
								+ this.getClass().getSimpleName() + ":"
								+ objcounter + ">");
				Tracer.WriteTrace(40, this.getClass().getSimpleName()
						+ "zeile=<" + zeile + ">");

				if ((zeile.contains("*****"))
						|| (zeile.contains("High,Low,Close,Volume")))

				{
					continue;
				}
				
				if(zeile.contains("*@*INFOSTRING*@*="))
				{
					infostring_g= new String(zeile.substring(zeile.indexOf("*@*INFOSTRING*@*=")+"*@*INFOSTRING*@*=".length()));
					continue;
				}
				
				if (zeile.length() < 2)
					continue;
				if (objclass == GC.WebinfoObj)
				{
					WebInfoObj wobj = null;
					wobj = new WebInfoObj(zeile);
					dbliste.add(wobj);
				}
				if (objclass == GC.ThreadDbObj)
				{
					ThreadDbObj tdbo = null;
					tdbo = new ThreadDbObj(zeile);

					this.plausiVorNeuaufnahme(tdbo);
					dbliste.add(tdbo);

				} else if (objclass == GC.AktDbObj)
				{
					AktDbObj aktobj = null;
					aktobj = new AktDbObj(zeile);
					if (this.plausiVorNeuaufnahme(aktobj) == true)
						dbliste.add(aktobj);
				} else if (objclass == GC.KursDbObj)
				{
					KursDbObj kobj = null;
					kobj = new KursDbObj(zeile);
					dbliste.add(kobj);
				} else if (objclass == GC.YuppieDbObj)
				{
					YuppieDbObj yobj = null;
					yobj = new YuppieDbObj(zeile);
					dbliste.add(yobj);
				} else if (objclass == GC.KursiconDbObj)
				{
					KursIconObj kobj = null;
					kobj = new KursIconObj(zeile);
					dbliste.add(kobj);
				} else if (objclass == GC.UserCharakterObj)
				{
					UserCharakterDbObj kobj = null;
					kobj = new UserCharakterDbObj(zeile);
					dbliste.add(kobj);
				}
				else if (objclass == GC.BBeventDbObj)
				{
					BBeventDbObj kobj = null;
					kobj = new BBeventDbObj(zeile);
					dbliste.add(kobj);
				}
				else if (objclass == GC.UsernameErsetzungsOb)
				{
					UsernameErsetzungsObj uobj = null;
					uobj = new UsernameErsetzungsObj(zeile);
					dbliste.add(uobj);
				} else if (objclass == GC.UserSummenGewinneDBI)
				{
					if (zeile.contains("#pinguin#0#0#0.0") == true)
					{
						Tracer.WriteTrace(20, "W:zeile fehlerhaft <" + zeile
								+ "> I drop it");
						continue;
					}
					UserDbGewinnZeitraumObjI ugew = null;
					ugew = new UserDbGewinnZeitraumObjI(zeile);
					dbliste.add(ugew);
				} else if (objclass == GC.UserGewStrategieObjII)
				{
					if (zeile.contains("#pinguin#0#0#0.0") == true)
					{
						Tracer.WriteTrace(20, "W:zeile fehlerhaft <" + zeile
								+ "> I drop it");
						continue;
					}
					UserGewStrategieObjII ugew = null;
					ugew = new UserGewStrategieObjII(zeile);
					dbliste.add(ugew);
				} else if (objclass == GC.KursvalueNativeDbObj)
				{
					KursvalueNativeDbObj kdbv = null;

					kdbv = new KursvalueNativeDbObj(zeile);
					dbliste.add(kdbv);

				} else if (objclass == GC.SymbErsetzungsDB)
				{
					SymbolErsetzungsObj kdbv = null;
					kdbv = new SymbolErsetzungsObj(zeile);
					dbliste.add(kdbv);
				} else if (objclass == GC.ThreadAttribObjI)
				{
					ThreadAttribObjI ob = null;
					ob = new ThreadAttribObjI(zeile);
					dbliste.add(ob);
				} else if (objclass == GC.PrognoseDbObj)
				{
					PrognoseDbObj ob = null;
					ob = new PrognoseDbObj(zeile);
					if (ob.getSymb().equalsIgnoreCase("DAX") == false)
						dbliste.add(ob);
					else
						Tracer
								.WriteTrace(20,
										"Warning: Prognose mit Symbol DAX aus prognosen.db entfernt");
				} else if (objclass == GC.MidDbObj)
				{
					MidDbObj ob = null;
					ob = new MidDbObj(zeile);
					dbliste.add(ob);
				} else if (objclass == GC.SlideruebersichtDB)
				{
					SlideruebersichtObj ob = null;
					ob = new SlideruebersichtObj(zeile);
					dbliste.add(ob);
				} 
				else if (objclass == GC.KeyDbObj)
				{
					KeyDbObj ob = null;
					ob = new KeyDbObj(zeile);
					dbliste.add(ob);
				} 
				 else if (objclass == GC.UeberwachungDbObj)
					{
					 UeberwachungDbObj ob = null;
						ob = new UeberwachungDbObj(zeile);
						dbliste.add(ob);
					}
				 else if (objclass == GC.EventDbObj)
					{
						EventDbObj ob = null;
						ob = new EventDbObj(zeile);
						dbliste.add(ob);
					}
				 else if (objclass == GC.BoersenblattDbObj)
					{
					 BoersenblattDbObj ob = null;
						ob = new BoersenblattDbObj(zeile);
						dbliste.add(ob);
					}
				 else if (objclass == GC.UserPostingverhaltenDbObj)
					{
					 if (zeile.contains("#pinguin") == true)
						{
							Tracer.WriteTrace(20, "W:zeile fehlerhaft <" + zeile
									+ "> I drop it");
							continue;
						}
					 
					 UserPostingverhaltenDbObj ob = null;
						ob = new UserPostingverhaltenDbObj(zeile);
						dbliste.add(ob);
					}
				 else if (objclass == GC.ChampionDbObj)
					{
								 
					 ChampionDbObj ob = null;
						ob = new ChampionDbObj(zeile);
						dbliste.add(ob);
					}
				 else if (objclass == GC.UserDbObj)
				{
					UserDbObj userobj = null;
					if (IsValid.checkUserobjektName(zeile) == false)
					{
						Tracer.WriteTrace(20, "W:Username error <" + zeile
								+ "> I drop it");
						continue;
					}

					userobj = new UserDbObj(zeile);
					if (IsValid.isValidUsername(userobj.get_username()) == false)
					{
						Tracer.WriteTrace(20, "W:not allowed username <"
								+ userobj.get_username() + "> I drop it");
						continue;
					}

					dbliste.add(userobj);
				} else
					System.out.println("unbekanntes ojekt objclass=<"
							+ objclass + ">");
				lastzeile=zeile;
			}
			inf_p.close();
			maxzaehler = dbliste.size();
			return true;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (DbException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (BadObjectException e)
		{
			String msg = Tools.get_aktdatetime_str() + ": Error: File<"
					+ filename_glob
					+ "> möglicherweise fehlerhaft (Lösche von Hand ?)";
			Inf inf = new Inf();
			inf.setFilename(GC.rootpath
					+ "\\db\\reporting\\corrupt_von_Hand_korrektor.txt");
			inf.writezeile(msg);
			Tracer.WriteTrace(10, msg);
			e.printStackTrace();
			return false;
		}
	}

	public boolean WriteDB()
	{
		String zeile = null;
		int i = 0, anz = 0, oldlen = 0, newlen = 0;
		anz = dbliste.size();

		if (anz == 0)
		{
			Tracer.WriteTrace(20, this.getClass().getName()
					+ "Warning: WriteDB liste=0");
			return true;
		}
		String klassenname = this.getClass().getName();
		
		if((klassenname.toLowerCase().contains("summengewinnedb"))||
			(klassenname.toLowerCase().contains("gewinnverlauf"))
			)
			System.out.println("found summengewinne");
		
		if ((klassenname.toLowerCase().contains("kursvalue") == true)
				&& (filename_glob.contains(".csv") == true))
		{
			Tracer.WriteTrace(10,
					"Error: speichern von yahoo kursen nicht erlaubt fn<"
							+ filename_glob + ">");
		}

		if(klassenname.toLowerCase().contains("champion")==true)
			Tracer.WriteTrace(20, "Info: championdb darf nicht verändert werden !! STOP");
		
		BufferedWriter ouf;
		Tracer.WriteTrace(50, this.getClass().getName() + "WriteDB");
		try
		{
			ouf = FileAccess.WriteFileOpen(filename_glob + ".tmp", "UTF-8");
			ouf.write("******" + speicherfile_glob + " anz=" + anz);
			ouf.newLine();
			ouf.write("*****" + dbliste.get(0).GetSaveInfostring());
			ouf.newLine();
			ouf.write("*@*INFOSTRING*@*="+infostring_g);
			ouf.newLine();
			for (i = 0; i < anz; i++)
			{
				zeile = dbliste.get(i).toString();

				Tracer.WriteTrace(50, this.getClass().getName() + "write<"
						+ zeile + ">");
				ouf.write(zeile);
				ouf.newLine();
			}
			ouf.close();

			// length protection, compare lenght
			oldlen = FileAccess.FileLength(filename_glob);
			newlen = FileAccess.FileLength(filename_glob + ".tmp");

			if (filename_glob.contains("compressed")==false)
				if (newlen < oldlen - (oldlen / 5))
				{
					// lenght error, new is shorter then the oldfile
					Tracer.WriteTrace(10, this.getClass().getName()
							+ "ERROR: length protection error !!!!!<" + filename_glob
							+ "> <" + filename_glob + ".tmp" + "> oldlen<" + oldlen
							+ "> newlen<" + newlen + ">");
					
				}
			FileAccess.Rename(filename_glob, filename_glob + ".bak");
			FileAccess.Rename(filename_glob + ".tmp", filename_glob);
			this.postprocess();
			return true;

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			Tracer.WriteTrace(10, this.getClass().getName()
					+ "ERROR: file not found<" + filename_glob + ">");
			return false;
		} catch (IOException e)
		{

			e.printStackTrace();
			Tracer.WriteTrace(10, this.getClass().getName()
					+ "ERROR: IO-error<" + filename_glob + ">");
			return false;
		}
		
	}

	public boolean ResetDB()
	{
		aktzaehler = 0;
		return true;
	}

	public boolean ResetDB(int i)
	{
		aktzaehler = i;
		return true;
	}

	public int AddObject(Obj obj)
	{
		//return pos
		//return -1 falls fehler
		
		boolean b = false;
		if (SucheObject(obj) < 0)
		{
			// falls objekt noch nicht drin (wird threadid vergleichen)
			// System.out.println("Objekt symbol<"+obj.toString()+">
			// aufgenommen");
			if (objclass == GC.UserDbObj)
			{
				UserDbObj udbo = (UserDbObj) obj;
				String username = udbo.get_username();
				if (IsValid.checkFilesystemname(username) == false)
				{
					Tracer.WriteTrace(20, "W:username <" + username
							+ "> not allowed, forget it");
					return -1;
				}
			}
			String objname = obj.getClass().getName();
			if (obj.getClass().getName().contains("UserVerwaltungObjII") == true)
			{
				// prüft den usernamen auf tauglichen filenamen
				UserGewStrategieObjII uvw = (UserGewStrategieObjII) obj;
				String username = uvw.getUsername();
				if (IsValid.checkFilesystemname(username) == false)
				{
					Tracer.WriteTrace(20, "W:username <" + username
							+ "> not allowed, forget it");
					return -1;
				}
			}
			if (objclass == GC.ThreadDbObj)
			{
				ThreadDbObj tdbo = (ThreadDbObj) obj;
			
				
				if (IsValid.threadname(dbliste, tdbo) == false)
				{
					Tracer.WriteTrace(20, "W:Threadname <"
							+ tdbo.getThreadname() + "> Symb<"
							+ tdbo.getSymbol() + "> Pageanz<"
							+ tdbo.getPageanz() + "> to big or not allowed->drop it");
					return -1;
				}

			}
			
			if(objclass==GC.ChampionDbObj)
			{
				Tracer.WriteTrace(20, "keine Objektaufnahme möglich !!! STOP");
				return 0;
			}
			b = dbliste.add(obj);
			if (b == true)
				maxzaehler++;
			else
				Tracer.WriteTrace(10, "Error: internal xxx12");
		}
		else
			Tracer.WriteTrace(20, "Error: Objekt schon vorhanden<"+obj.GetSaveInfostring()+">, mache nix");
		return (maxzaehler-1);
	}

	public int GetThreadid()
	{
		int tid = 0;
		if (objclass == GC.ThreadDbObj)
		{
			ThreadDbObj obj = (ThreadDbObj) GetAktObject();
			tid = obj.getThreadid();
		}
		if (objclass == GC.AktDbObj)
		{
			AktDbObj akobj = (AktDbObj) GetAktObject();
			tid = akobj.getThreadid();
		}
		return tid;
	}

	public int SetDurchlaufmode(int dumode)
	{
		// meldet zurück wieviele elemente diesen mode haben
		// userobjekte und threadobjekte
		durchlaufmode = dumode;
		if (dumode == 0)
			return dbliste.size();

		int anz = dbliste.size();
		int zaehler = 0;
		for (int i = 0; i < anz; i++)
		{
			if (objclass == GC.ThreadDbObj)
			{
				ThreadDbObj tdbo = (ThreadDbObj) dbliste.get(i);
				if ((tdbo.getMode()) == durchlaufmode)
					zaehler++;
			}
			if (objclass == GC.UserDbObj)
			{
				UserDbObj udbo = (UserDbObj) dbliste.get(i);
				if ((udbo.getMode()) == durchlaufmode)
					zaehler++;
			}
		}
		return zaehler;
	}

	public int CheckThreadid(int tid)
	{
		// prüft nach ob tid in der liste, wenn ja wird die position
		// zurückgegeben, wenn nein -1
		int k;

		maxzaehler = dbliste.size();
		if (objclass == GC.ThreadDbObj)
		{
			for (k = 0; k < maxzaehler; k++)
			{
				ThreadDbObj tdbo = (ThreadDbObj) dbliste.get(k);
				int thid = tdbo.getThreadid();
				if (thid == tid)
				{
					return k;
				}
			}
			return -1;
		}

		if (objclass == GC.AktDbObj)
		{
			for (k = 0; k < maxzaehler; k++)
			{
				AktDbObj aktobj = (AktDbObj) dbliste.get(k);
				int thid = aktobj.getThreadid();
				if (thid == tid)
				{
					return k;
				}
			}
			return -1;
		}
		if (objclass == GC.SlideruebersichtDB)
		{
			for (k = 0; k < maxzaehler; k++)
			{
				SlideruebersichtObj tdbo = (SlideruebersichtObj) dbliste.get(k);
				int thid = tdbo.getThreadid();
				int wochenindex = tdbo.getWochenindex();
				// die tid muss stimmen und der wochenindex muss 0 sein
				if ((thid == tid) && (wochenindex == 0))
				{
					return k;
				}
			}
			return -1;
		}
		return -1;
	}

	public Object GetObject(int tid)
	{
		// gibt ein object mit einer bestimmten tid zurück
		// wenn nicht vorhanden dann null
		int pos = CheckThreadid(tid);

		if (pos < 0)
			return null;

		return (dbliste.get(pos));
	}

	public Obj GetAktObject()
	{
		return (dbliste.get(aktzaehler));
	}

	public Obj GetNextObject()
	{
		if (durchlaufmode == GC.MODE_ALL)
		{
			// nimm jedes objekt
			aktzaehler = aktzaehler + 1;
			if (aktzaehler < maxzaehler)
				return (dbliste.get(aktzaehler));
			else
				return null;
		} else
		// nimm nur objekte mit passenden durchlaufmode
		{
			while (aktzaehler < maxzaehler - 1)
			{
				aktzaehler++;
				if (objclass == GC.ThreadDbObj)
				{
					ThreadDbObj tdbo = (ThreadDbObj) dbliste.get(aktzaehler);
					if (tdbo.getMode() == durchlaufmode)
						return tdbo;
					else
						continue;
				}
				if (objclass == GC.UserDbObj)
				{
					UserDbObj udbo = (UserDbObj) dbliste.get(aktzaehler);
					if (udbo.getMode() == durchlaufmode)
						return udbo;
					else
						continue;
				}
			}
			return null; // ende erreicht
		}
	}

	public boolean SetNextObject()
	{
		if (aktzaehler < maxzaehler - 1)
		{
			aktzaehler = aktzaehler + 1;
			// System.out.println("aktzaehler="+aktzaehler);
			return (true);
		} else
			return false;
	}

	private int SucheObject(Object obj)
	{
		// sucht ein Object,
		int maxzaehler = this.GetanzObj();
		for (int i = 0; i < maxzaehler; i++)
		{
			Object t1 = dbliste.get(i);
			if (t1.equals(obj) == true)
				return i;
		}
		return -1;
	}

	public boolean UpdateObject(Obj obj)
	{
		// sucht wo das objekt in der liste ist
		int pos = SucheObject(obj);
		// ersetzt das Listenelement durch das neue Element
		if (pos == -1)
		{
			System.out.println("obj nicht in db gefunden");
			return false;
		}
		dbliste.set(pos, obj);
		return true;
	}

	public boolean GenUpdateObject(Obj obj)
	{
		// sucht wo das objekt in der liste ist
		// Wenn es drin ist dann wird upgedatet,
		// wenn nicht drinn dann wird es erzeugt
		int pos = SucheObject(obj);
		// ersetzt das Listenelement durch das neue Element
		if (pos == -1)
		{
			dbliste.add(obj);
			return true;
		}
		dbliste.set(pos, obj);
		return true;
	}
	
	public Obj GetObjectIDX(int index)
	{
		int anz=dbliste.size();
		if (anz > 0)
		{
			if(index>anz)
				Tracer.WriteTrace(10, "Error: internal arraysize<"+anz+"> zugriffsversuch pos<"+index+"> out of bounds");
			
			Obj t1 = dbliste.get(index);
			return t1;
		} else
			return null;
	}

	public boolean SetAktIDX(int index)
	{
		// setzt den suchindex
		if (index < maxzaehler)
		{
			aktzaehler = index;
			return true;
		}
		return false;
	}

	public boolean UpdateObjectIDX(Obj obj, int index)
	{
		// sucht wo das objekt in der liste ist
		Obj t1 = dbliste.get(index);

		// das pivotelement passt nicht
		if (t1.equals(obj) == false)
			return false;

		// ersetzt das Listenelement durch das neue Element
		dbliste.set(index, obj);
		return true;
	}

	public boolean DeleteObjectIDX(int index)
	{
		Obj t1 = dbliste.remove(index);
		if (t1 != null)
			return true;
		else
			return false;
	}

	public int GetanzObj()
	{
		return dbliste.size();
	}

	public void clearAll()
	{
		//Hier wird alles gelöscht
		int anz = dbliste.size();
		for(int i=0; i<anz; anz-- )
		{
			this.DeleteObjectIDX(i);
			
		}
		FileAccess.FileDelete(filename_glob, 0);
		this.WriteDB();
	}
	
	public void CloseDB()
	{
		try
		{
			if (inf_p != null)
				inf_p.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
