package kz.sdu.microelectronicslab.model.user;

import java.security.MessageDigest;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.util.Hex;

@Name("passwordManager")
public class PasswordManager
{
	private String digestAlgorithm;
	private String charset;
	
	public void setDigestAlgorithm(String digestAlgorithm)
	{
		this.digestAlgorithm = digestAlgorithm;
	}
	
	public void setCharset(String charset)
	{
		this.charset = charset;
	}
	
	public String hash(String plainTextPassword)
	{
		try
		{
			MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
			digest.update(plainTextPassword.getBytes(charset));
			byte [] rawHash = digest.digest();
			return new String(Hex.encodeHex(rawHash));
		}
		catch(Exception e)
		{ throw new RuntimeException(e); }
	}
}