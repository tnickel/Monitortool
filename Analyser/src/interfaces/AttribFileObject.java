package interfaces;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public interface AttribFileObject
{
	// Dies objekt wird zeilenweise in ein *.db Datenfile abgelegt
	// die Kennung ist die erste Zeile des Datenobjektes und dient
	// zur identifizierung

	public boolean Read(BufferedReader inf, String zeile);

	public boolean Write(BufferedWriter ouf);

	public String Kennung();

}
