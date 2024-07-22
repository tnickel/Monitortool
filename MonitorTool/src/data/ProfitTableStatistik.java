package data;

public class ProfitTableStatistik
{
	private float prof7_g=0;
	private float prof30_g=0;
	private float profall_g=0;
	
	
	
	public String getProf7()
	{
		return String.valueOf(prof7_g);
	}


	public String getProf30()
	{
		return String.valueOf(prof30_g);
	}

	
	public String getProfAll()
	{
		return String.valueOf(profall_g);
	}



	



	public void addvalue(float prof7, float prof30, float profall)
	{
		prof7_g=prof7_g+prof7;
		prof30_g=prof30_g+prof30;
		profall_g=profall_g+profall;
	}
	
}
