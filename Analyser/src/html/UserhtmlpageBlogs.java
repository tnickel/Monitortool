package html;

public class UserhtmlpageBlogs extends HtmlCore
{
	private String filename = null;

	public UserhtmlpageBlogs(String fnam)
	{
		filename = fnam;
		super.ReadHtmlPage(fnam);
	}

}
