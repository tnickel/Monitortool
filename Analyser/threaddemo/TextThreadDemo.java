
class TextThread extends Thread
{
	String text;

	public TextThread(String text)
	{
		this.text = text;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < 10; i++)
		{
			try
			{
				sleep((int) (Math.random() * 1000));
			} catch (InterruptedException e)
			{
			}
			System.out.println(text);
		}
	}
}

public class TextThreadDemo
{

	public static void main(String args[])
	{
		TextThread java, espresso, capuccino;

		java = new TextThread("Java");
		espresso = new TextThread("Espresso");
		capuccino = new TextThread("Cappuccino");
		java.start();
		espresso.start();
		capuccino.start();
	}

}
