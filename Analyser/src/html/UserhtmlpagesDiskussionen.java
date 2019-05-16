package html;

public class UserhtmlpagesDiskussionen extends HtmlCore
{
	private String zeile;

	public UserhtmlpagesDiskussionen(String filename)
	{
		super.ReadHtmlPage(filename);
	}

	public String getNextListeneintrag()
	{
		zeile = new String(super.GetFullUserhtmlListeneintrag());
		// falls am ende der Threadpostliste
		if (zeile.equals("0") == true)
			return null;
		return zeile;
	}

	public String getThreadname()
	{
		String zeil = new String(super.GetKeywordPart2(zeile, zeile, "\">",
				"</a>"));
		zeil = zeil.replace("#", ".");
		return zeil;
	}

	public int getThreadid()
	{
		int tid = Integer.valueOf(Keyword.GetThreadIdFromMem(zeile));
		return tid;
	}

	public String getLastposting()
	{
		String lpost = new String(this.GetPostDateTimeFromUserHtmlPosting(
				zeile, zeile));
		return lpost;
	}
}
