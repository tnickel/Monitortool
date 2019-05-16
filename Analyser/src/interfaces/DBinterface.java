package interfaces;

import objects.Obj;

public interface DBinterface
{
	// Maxzaehler Elemente hat die Datenbank
	public boolean WriteDB();
	public boolean ResetDB();
	public Obj GetAktObject();
	public boolean SetNextObject();
	public Obj GetNextObject();
	public boolean UpdateObject(Obj obj);
	public Obj GetObjectIDX(int index);
	public boolean UpdateObjectIDX(Obj obj, int index);
	public boolean DeleteObjectIDX(int index);
	public int GetThreadid();
	public int GetanzObj();
	public boolean plausiVorNeuaufnahme(Object obj);
	abstract public void postprocess();
	
}
