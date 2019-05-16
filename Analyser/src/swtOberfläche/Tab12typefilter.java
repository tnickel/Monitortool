package swtOberfläche;

import hilfsklasse.Tracer;

public class Tab12typefilter
{
	private Boolean type1 = false;
	private Boolean type2 = false;
	private Boolean type3 = false;
	private Boolean typem = false;

	public void setTypes(Boolean type1, Boolean type2, Boolean type3,Boolean typem)
	{
		this.type1 = type1;
		this.type2 = type2;
		this.type3 = type3;
		this.typem = typem;
	}

	public boolean checkType(int type,int marker)
	{
		//falls marker gesucht dann muss auf jeden fall das
		//event einen marker haben sonst wird nix angzeigt.
		//der weitere type ist dann unrelevant
		if((this.typem==true) && (marker ==0))
			return false;
		
		
		switch (type)
		{
		case 1:
			if(this.type1==true)
				return true;
			break;
		case 2:
			if(this.type2==true)
				return true;
			break;
		case 3:
			if(this.type3==true)
				return true;
			break;
		default:
			Tracer.WriteTrace(10, "internal 123454545");
			return false;
			
		}
		return false;
	}

	public Boolean getType1()
	{
		return type1;
	}

	public void setType1(Boolean type1)
	{
		this.type1 = type1;
	}

	public Boolean getType2()
	{
		return type2;
	}

	public void setType2(Boolean type2)
	{
		this.type2 = type2;
	}

	public Boolean getType3()
	{
		return type3;
	}

	public void setType3(Boolean type3)
	{
		this.type3 = type3;
	}

	public Boolean getTypem()
	{
		return typem;
	}

	public void setTypem(Boolean typem)
	{
		this.typem = typem;
	}

}
