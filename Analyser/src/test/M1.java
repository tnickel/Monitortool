package test;

public class M1
{
	public M1()
	{}
	
	public int methode1()
	{
		int a=5;
		x1(a);
		
		return 5;
	}

	private void x1(int a)
	{
		System.out.println("test1");
		System.out.println("test2"+a);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		// TODO Auto-generated method stub
		return super.clone();
	}
}
