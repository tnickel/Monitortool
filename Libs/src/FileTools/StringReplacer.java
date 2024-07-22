package FileTools;

public class StringReplacer
{
	public static String replaceAll(final String str, final String searchChars, String replaceChars)
	{
	  if ("".equals(str) || "".equals(searchChars) || searchChars.equals(replaceChars))
	  {
	    return str;
	  }
	  if (replaceChars == null)
	  {
	    replaceChars = "";
	  }
	  final int strLength = str.length();
	  final int searchCharsLength = searchChars.length();
	  StringBuilder buf = new StringBuilder(str);
	  boolean modified = false;
	  for (int i = 0; i < strLength; i++)
	  {
	    int start = buf.indexOf(searchChars, i);

	    if (start == -1)
	    {
	      if (i == 0)
	      {
	        return str;
	      }
	      return buf.toString();
	    }
	    buf = buf.replace(start, start + searchCharsLength, replaceChars);
	    modified = true;

	  }
	  if (!modified)
	  {
	    return str;
	  }
	  else
	  {
	    return buf.toString();
	  }
	}
}
