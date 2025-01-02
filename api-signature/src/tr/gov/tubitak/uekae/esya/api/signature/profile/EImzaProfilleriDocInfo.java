package tr.gov.tubitak.uekae.esya.api.signature.profile;

import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class EImzaProfilleriDocInfo implements ProfileDocInfo
{
	public static String URI = "http://www.eimza.gov.tr/EimzaPolitikalari/216792161015070321.pdf";
	
	public static Map<DigestAlg, byte []> hashes = new HashMap<DigestAlg, byte []>();
	
	static
	{
		hashes.put(DigestAlg.SHA1, StringUtil.toByteArray("7E3B50D9020C676BBBF07A998E75E63734909CD8"));
		hashes.put(DigestAlg.SHA224, StringUtil.toByteArray("B6600FE76A7E1973367382D81F224620242A1957595C20C882A5EC8F"));
		hashes.put(DigestAlg.SHA256, StringUtil.toByteArray("FF39BD29463383F69B2052AC47439E06CE7C3B8646E888B6E5AE3E46BA08117A"));
		hashes.put(DigestAlg.SHA384, StringUtil.toByteArray("E7503CF83D21EB179C3A89FDE8BF2216E5F4F24C9DA9752D8FE86C8A8B71D4BD58A1EF426B8B0071B8C1D0754C71A810"));
		hashes.put(DigestAlg.SHA512, StringUtil.toByteArray("AF925EEE76562989CD5DA4000DA2C35F3D9E95BC6604BB13FE3924A6E223914756BF54FCE4CCFD2617DE906EA135B9474CCB1DA83F8468C2DB18341EC7552EDE"));
	}

    public String getURI()
    {
        return URI;
    }

    public byte[] getDigestOfProfile(DigestAlg aDigestAlg)
	{
        byte[] digest = hashes.get(aDigestAlg);
        if (digest!=null)
            return digest;

		throw new SignatureRuntimeException("Digest of profile according to " + aDigestAlg + " algorithm can not be found");
	}

	public InputStream getProfile() throws SignatureException
	{
		try
		{
			URL url = new URL(URI);
			URLConnection conn = url.openConnection();
			return conn.getInputStream();
		} 
		catch (Exception e)
		{
			//to-do read from bundle 
			throw new SignatureException("Problem at reading profile document.", e);
		} 
	}
	
}
