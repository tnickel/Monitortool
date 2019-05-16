package slider;

import hilfsklasse.Tracer;
import objects.UserDbObj;
import db.CompressedPostingObj;

public class PostSliderElem
{
	CompressedPostingObj elem_glob = null;
	UserDbObj udbo_glob = null;

	public PostSliderElem(CompressedPostingObj copost)
	{
		elem_glob = copost;
	}

	public void addUserObj(UserDbObj udbo)
	{
		udbo_glob = udbo;
	}

	public int getRank()
	{
		if (udbo_glob == null)
		{
			// System.out.println("udbo=0");
			return 50000;
		}
		int rp = udbo_glob.getRang();

		return rp;
	}
	public int getBoostrang()
	{
		if (udbo_glob == null)
			return 0;
		int br=udbo_glob.getBoostrang();
		return br;
	}
	public void plausicheck()
	{
		//prüft ob der username in udbo zu dem sliderposting passt
		if(udbo_glob!=null)
		{
			String unam=udbo_glob.get_username();
			if(unam.equals(elem_glob.getUsername())==false)
				Tracer.WriteTrace(10, "Error: PostSliderElemPlausicheck udbo-unam<"+unam+"> elem-unam<"+elem_glob.getUsername()+">");
		}
	}
	
}
