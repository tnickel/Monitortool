package tcp;

import java.text.DecimalFormat;

public class TPacket
{
	// das kommando
	int cmd = 0;
	// der kommandostring, das ist einfach das kommando als string
	String cmdstring = null;
	// die länge der anschliessenden daten
	int datalen = 0;
	// entweder hat das packet stringdaten
	String data = null;
	// oder binärdaten
	byte[] bytearray = null;

	public String getCmdstring()
	{
		return cmdstring;
	}

	public void setCmdstring(String cmdstring)
	{
		this.cmdstring = cmdstring;
	}

	public int getCmd()
	{
		return cmd;
	}

	public void setCmd(int cmd)
	{
		this.cmd = cmd;
	}

	public int getDatalen()
	{
		
		return datalen;
	}
	public void setDatalen(int len)
	{
		datalen=len;
	}
	

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public byte[] getBytearray()
	{
		return bytearray;
	}

	public void setBytearray(byte[] bytearray)
	{
		this.bytearray = bytearray;
	}
	public String calcLengthstring(int len)
	{
		//berechnet aus einem integer einen formatierten string der die länge beinhaltet
		DecimalFormat formatter = new DecimalFormat("000000000");
		//String stringlaenge = String.valueOf(len);
		String aFormattedlen = formatter.format(len);
		return aFormattedlen;
	}
	public String calcCommandLengthstring(int len)
	{
		//berechnet aus einem integer einen formatierten string der die länge beinhaltet
		DecimalFormat formatter = new DecimalFormat("00000");
		//String stringlaenge = String.valueOf(len);
		String aFormattedlen = formatter.format(len);
		return aFormattedlen;
	}
}
