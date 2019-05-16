package gewinne;

import html.ThreadsPostElem;
import objects.Gewinn;
import slider.Sliderbewert;
import db.CompressedPostingObj;

abstract public class Gewinnalgo
{
	//xxxxxxxx to do, ein Gewinnalgo-Objekt definieren !!!
	//to do klasse !!!!!!!!!!!!!!!!!!
	public abstract void calcVertrauensfaktorAbstract(ThreadsPostElem obj);

	public Gewinn calcGewinnNachXtagen(String username,
			CompressedPostingObj copost, float startsumme, int xtage,
			String lasterrorsymbol, String aktthreadnamz, Sliderbewert slb,
			String methode,float vertrauensfaktor)
	{
		return null;
	}
	
}
