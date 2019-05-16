package data;

import java.util.Date;

public class Client
{
	// der clientname
	String clientname = null;
	// die Seriennummer des client
	int serial = 0;
	// datum wann sich der client das letzte Mal angemeldet hat
	Date lastloaded = null;

	// aktuelle versionsnummer vom client
	String versionsnummer = null;
	// hier stehen Anweisungen für den client
	String downloadcmd1 = null;
	String downloadcmd2 = null;

	String username = null;
	String email = null;

	public String getClientname()
	{
		return clientname;
	}

	public void setClientname(String clientname)
	{
		this.clientname = clientname;
	}

	public int getSerial()
	{
		return serial;
	}

	public void setSerial(int serial)
	{
		this.serial = serial;
	}

	public Date getLastloaded()
	{
		return lastloaded;
	}

	public void setLastloaded(Date lastloaded)
	{
		this.lastloaded = lastloaded;
	}

	public String getVersionsnummer()
	{
		return versionsnummer;
	}

	public void setVersionsnummer(String versionsnummer)
	{
		this.versionsnummer = versionsnummer;
	}

	public String getDownloadcmd1()
	{
		return downloadcmd1;
	}

	public void setDownloadcmd1(String downloadcmd1)
	{
		this.downloadcmd1 = downloadcmd1;
	}

	public String getDownloadcmd2()
	{
		return downloadcmd2;
	}

	public void setDownloadcmd2(String downloadcmd2)
	{
		this.downloadcmd2 = downloadcmd2;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

}
