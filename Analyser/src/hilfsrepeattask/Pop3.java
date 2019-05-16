package hilfsrepeattask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;

public class Pop3
{
	public static void main(String[] args)
	{
		sammleMails();
	}
	
	static public void sammleMails()
	{
		Properties props = new Properties();

		String host = "pop.gmx.net";
		String username = "analyser2009@gmx.de";
		String password = "terminator";
		String provider = "pop3";
		String smtpHost = "smtp.gmx.net";

		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.auth", "false");

		try
		{

			// Connect to the POP3 server
			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(true);

			Store store = session.getStore(provider);
			store.connect(host, username, password);

			// Open the folder
			Folder inbox = store.getFolder("INBOX");
			if (inbox == null)
			{
				System.out.println("No INBOX");
				System.exit(1);
			}
			inbox.open(Folder.READ_ONLY);

			// Get the messages from the server
			Message[] message = inbox.getMessages();

			for (int i = 0; i < message.length; i++)
			{
				Message m = message[i];

				System.out.println("-----------------------\nNachricht: " + i);
				System.out.println("Von: " + Arrays.toString(m.getFrom()));
				System.out.println("Betreff: " + m.getSubject());
				System.out.println("Gesendet am: " + m.getSentDate());
				System.out.println("ContentType: "
						+ new ContentType(m.getContentType()));
				System.out.println("Content: " + m.getContent());

				// Nachricht ist eine einfache Text- bzw. HTML-Nachricht
				if (m.isMimeType("text/plain"))
				{
					System.out.println(m.getContent());
				}

				// Nachricht ist eine Multipart-Nachricht (besteht aus mehreren
				// Teilen)
				if (m.isMimeType("multipart/*"))
				{
					Multipart mp = (Multipart) m.getContent();

					for (int j = 0; j < mp.getCount(); j++)
					{
						Part part = mp.getBodyPart(j);
						String disposition = part.getDisposition();

						if (disposition == null)
						{
							MimeBodyPart mimePart = (MimeBodyPart) part;

							if (mimePart.isMimeType("text/plain"))
							{
								BufferedReader in = new BufferedReader(
										new InputStreamReader(mimePart
												.getInputStream()));

								for (String line; (line = in.readLine()) != null;)
								{
									System.out.println(line);
								}
							}
						}
					}
				}// if Multipart

			}

			// Close the connection
			// but don't remove the messages from the server
			inbox.close(false);
			store.close();

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
