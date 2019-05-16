package data;

import hilfsklasse.FileAccess;
import hilfsklasse.Inf;
import hilfsklasse.Tracer;

import java.io.File;
import java.util.ArrayList;

public class Clientlist
{
	private static int isloaded = 0;
	private static String clientlistenam = Rootpath.rootpath
			+ "\\data\\clientliste.xml";
	private static ArrayList<Client> clientliste = new ArrayList<Client>();

	public void init()
	{
		// semaphore on
		if (isloaded == 0)
		{
			clientliste.clear();

			if (FileAccess.FileAvailable0(clientlistenam) == false)
				return;

			// tradeliste laden
			Inf inf = new Inf();
			inf.setFilename(clientlistenam);

			// tradeliste einlesen
			clientliste = (ArrayList<Client>) inf.loadXST();
			// Tracer.WriteTrace(10, "anz trades geladen="+tradeliste.size());
			inf.close();
			isloaded = 1;
		}
	}

	public Client getClient(String username, String hostname, int serial)
	{
		int anz = clientliste.size();
		for (int i = 0; i < anz; i++)
		{
			Client c = clientliste.get(i);
			if ((c.getClientname().equalsIgnoreCase(hostname))
					&& (c.getSerial() == serial)
					&& (c.getUsername().equalsIgnoreCase(username)))
			{
				return c;
			}
		}
		return null;
	}

	public void addClient(Client cl)
	{
		clientliste.add(cl);
	}

	public void store(int nolengthprotectionflag)
	{
		if (clientliste.size() == 0)
			return;

		String clientliste_tmp = clientlistenam + ".tmp";
		String clientliste_old = clientlistenam + ".old";

		// das tmp löschen
		if (FileAccess.FileAvailable(clientliste_tmp))
			FileAccess.FileDelete(clientliste_tmp, 0);

		// speichern
		Inf inf = new Inf();
		inf.setFilename(clientliste_tmp);
		inf.saveXST(clientliste);
		inf.close();

		// längenvergleich, es darf nicht kürzer werden
		File f1 = new File(clientlistenam);
		File f2 = new File(clientliste_tmp);
		if (nolengthprotectionflag == 0)
			if (f2.length() < f1.length() - 10)
				Tracer.WriteTrace(20, "ERROR: Length protection error ealiste<"
						+ f1.length() + ">ealiste_new<" + f2.length() + ">");

		// wenn alles ok dann nach old speichern
		if (FileAccess.FileAvailable(clientliste_old))
			FileAccess.FileDelete(clientliste_old, 0);

		if (FileAccess.FileAvailable(clientlistenam) == true)
			FileAccess.FileMove(clientlistenam, clientliste_old);
		// und tmp umbenennen
		FileAccess.FileMove(clientliste_tmp, clientlistenam);
	}
}
