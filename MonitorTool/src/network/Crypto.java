package network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Crypto
{
	public void setProvider()
	{
		java.security.Security
				.addProvider(new com.sun.crypto.provider.SunJCE());
	}

	public void encryptFile(String originalFile, String encryptedFile,
			String password) throws Exception
	{
		CipherOutputStream out;
		InputStream in;
		Cipher cipher;
		SecretKey key;
		byte[] byteBuffer;
		cipher = Cipher.getInstance("DES");
		key = new SecretKeySpec(password.getBytes(), "DES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		in = new FileInputStream(originalFile);
		out = new CipherOutputStream(new FileOutputStream(encryptedFile),
				cipher);
		byteBuffer = new byte[1024];
		for (int n; (n = in.read(byteBuffer)) != -1; out
				.write(byteBuffer, 0, n))
			;
		in.close();
		out.close();

		// das quellfile löschen
		File delfile = new File(originalFile);
		if (delfile.exists())
			delfile.delete();

		File delfile2 = new File(originalFile.replace(".zip", ".gzip"));
		if (delfile2.exists())
			delfile2.delete();
	}

	public void decryptFile(String encryptedFile, String decryptedFile,
			String password) throws Exception
	{
		CipherInputStream in;
		OutputStream out;
		Cipher cipher;
		SecretKey key;
		byte[] byteBuffer;
		cipher = Cipher.getInstance("DES");
		key = new SecretKeySpec(password.getBytes(), "DES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		in = new CipherInputStream(new FileInputStream(encryptedFile), cipher);
		out = new FileOutputStream(decryptedFile);
		byteBuffer = new byte[1024];
		for (int n; (n = in.read(byteBuffer)) != -1; out
				.write(byteBuffer, 0, n))
			;
		in.close();
		out.close();
		// new File(encryptedFile).delete();
	}
}
