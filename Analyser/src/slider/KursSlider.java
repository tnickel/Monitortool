package slider;

import java.util.ArrayList;

public class KursSlider
{
	// diese Klasse ist eine allgemeine Sliderklasse für kurse

	private ArrayList<Float> sliderliste = new ArrayList<Float>();
	private int slidergroesse_g = 3;

	public KursSlider(int slgroesse)
	{
		slidergroesse_g = slgroesse;
	}

	public boolean addSliderElem(float elem)
	{
		int anz = sliderliste.size();
		sliderliste.add(elem);

		anz = sliderliste.size();
		// falls slider voll, dann lösche das element 0;
		if (anz > slidergroesse_g)
			sliderliste.remove(0);

		return true;
	}

	public float calcSliderValue()
	{

		float sum = 0;
		int anz = sliderliste.size();
		for (int i = 0; i < anz; i++)
		{
			float elem = sliderliste.get(i);
			sum = sum + elem;
		}
		return (sum / anz);
	}

	public int getSliderFuellstand()
	{
		return sliderliste.size();
	}

}
