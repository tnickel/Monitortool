package objects;

public class BadObjectException extends Exception
{
	private static final long serialVersionUID = 13435L;

	public BadObjectException()
	{
	}

	public BadObjectException(String s)
	{
		super(s);
	}
}
