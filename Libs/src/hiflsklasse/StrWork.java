package hiflsklasse;

public class StrWork
{
	public static String intToFourDigitString(int number) {
	    if (number < 0 || number > 999) {
	        throw new IllegalArgumentException("Der Wert muss zwischen 0 und 9999 liegen.");
	    }

	    // Zerlege die Zahl in einzelne Ziffern
	   
	    int hundreds = (number % 1000) / 100;
	    int tens = (number % 100) / 10;
	    int ones = number % 10;

	    // Baue den 4-stelligen String
	    StringBuilder stringBuilder = new StringBuilder();
	   
	    stringBuilder.append(hundreds);
	    stringBuilder.append(tens);
	    stringBuilder.append(ones);

	    return stringBuilder.toString();
	}
	
}
