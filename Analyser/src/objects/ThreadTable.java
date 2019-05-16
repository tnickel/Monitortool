package objects;

public class ThreadTable
{
	private ThreadDbObj tdbo=null;
	private String attLastdate=null;
	private float anzneuUser=0;
	private float anzguteuser=0;
	
	private float anzschluser=0;
	private float Slider20groesse=0;
	private float Slider20t3groesse=0;
	private float Slider20Steigung=0;
	private float prio=0;
	private String lastSliderUpdate=null;
	
	//"Pos#Threadname#lastdate#anzneueUser#anzguteUser#Slider20groesse#slider20t3groesse#Slider20Steigung#Prio#Lastsliderupdate";

	public String getLastSliderUpdate()
	{
		return lastSliderUpdate;
	}
	public void setLastSliderUpdate(String lastSliderUpdate)
	{
		this.lastSliderUpdate = lastSliderUpdate;
	}
	public ThreadDbObj getTdbo()
	{
		return tdbo;
	}
	public float getAnzschluser()
	{
		return anzschluser;
	}
	public void setAnzschluser(float anzschluser)
	{
		this.anzschluser = anzschluser;
	}
	public void setTdbo(ThreadDbObj tdbo)
	{
		this.tdbo = tdbo;
	}
	public String getAttLastdate()
	{
		return attLastdate;
	}
	public void setAttLastdate(String attLastdate)
	{
		this.attLastdate = attLastdate;
	}
	public float getAnzneuUser()
	{
		return anzneuUser;
	}
	public void setAnzneuUser(float anzneuUser)
	{
		this.anzneuUser = anzneuUser;
	}
	public float getAnzguteuser()
	{
		return anzguteuser;
	}
	public void setAnzguteuser(float anzguteuser)
	{
		this.anzguteuser = anzguteuser;
	}
	public float getSlider20groesse()
	{
		return Slider20groesse;
	}
	public void setSlider20groesse(float slider20groesse)
	{
		Slider20groesse = slider20groesse;
	}
	public float getSlider20t3groesse()
	{
		return Slider20t3groesse;
	}
	public void setSlider20t3groesse(float slider20t3groesse)
	{
		Slider20t3groesse = slider20t3groesse;
	}
	public float getSlider20Steigung()
	{
		return Slider20Steigung;
	}
	public void setSlider20Steigung(float slider20Steigung)
	{
		Slider20Steigung = slider20Steigung;
	}
	public float getPrio()
	{
		return prio;
	}
	public void setPrio(float prio)
	{
		this.prio = prio;
	}
	
	
	
}
