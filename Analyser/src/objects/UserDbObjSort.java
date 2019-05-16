package objects;


public class UserDbObjSort implements Comparable<UserDbObjSort>
{
	private String infostring_glob = null;
	private float points=0;
	private String name=null;
	private float gewinn=0;
	private int anzGewinne=0;
	private int anzVerluste=0;
	private int anzNeutral=0;
	
	private UserGewStrategieObjII userverwobj=null;
	
	public int compareTo(UserDbObjSort argument)
	{
		if (points > argument.points)
			return -1;
		if (points < argument.points)
			return 1;
		return 0;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public float getPoints()
	{
		return points;
	}

	public void setPoints(float points)
	{
		this.points = points;
	}

	public void setGewinninfostring(String inf)
	{
		infostring_glob = new String(inf);
	}

	public String getGewinninfostring()
	{
		return infostring_glob;
	}

	public UserGewStrategieObjII getUserverwobj()
	{
		return userverwobj;
	}

	public void setUserverwobj(UserGewStrategieObjII userverwobj)
	{
		this.userverwobj = userverwobj;
	}

	public float getGewinn()
	{
		return gewinn;
	}

	public void setGewinn(float gewinn)
	{
		this.gewinn = gewinn;
	}

	public int getAnzGewinne()
	{
		return anzGewinne;
	}

	public void setAnzGewinne(int anzGewinne)
	{
		this.anzGewinne = anzGewinne;
	}

	public int getAnzVerluste()
	{
		return anzVerluste;
	}

	public void setAnzVerluste(int anzVerluste)
	{
		this.anzVerluste = anzVerluste;
	}

	public int getAnzNeutral()
	{
		return anzNeutral;
	}

	public void setAnzNeutral(int anzNeutral)
	{
		this.anzNeutral = anzNeutral;
	}

	
}
