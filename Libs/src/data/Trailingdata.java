package data;

public class Trailingdata
{
	float[] lastgewinnstaende =null;;
	int arraylength=0;
	
	public Trailingdata(int n)
	{
		arraylength=n;
		lastgewinnstaende = new float[arraylength];
	}
	
	public void setAktGewinn(int i,float aktgewinn)
	{
		lastgewinnstaende[i%arraylength]=aktgewinn;
	}
	public float getMinX()
	{
		//ermittle das minimum der letzten n stände
		float min = 5000000;
		for(int i=0; i<arraylength; i++)
		{
			if(lastgewinnstaende[i]<min)
				min = lastgewinnstaende[i];
		}
		return (min*(float)0.8);
	}
	
}
