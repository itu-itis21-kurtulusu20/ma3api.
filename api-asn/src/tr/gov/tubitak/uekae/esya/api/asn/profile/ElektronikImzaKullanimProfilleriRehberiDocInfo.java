package tr.gov.tubitak.uekae.esya.api.asn.profile;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElektronikImzaKullanimProfilleriRehberiDocInfo implements ProfileDocInfo
{
	public static final String msurl = "http://www.eimza.gov.tr/EimzaPolitikalari/216792161015070321.pdf";

	public static final List<Pair<int [], byte []>> hashes = new ArrayList<Pair<int [], byte []>>();
	
	static
	{
		hashes.add(new Pair(_algorithmsValues.sha_1, StringUtil.toByteArray("7E3B50D9020C676BBBF07A998E75E63734909CD8")));
		hashes.add(new Pair(_algorithmsValues.id_sha224, StringUtil.toByteArray("B6600FE76A7E1973367382D81F224620242A1957595C20C882A5EC8F")));
		hashes.add(new Pair(_algorithmsValues.id_sha256, StringUtil.toByteArray("FF39BD29463383F69B2052AC47439E06CE7C3B8646E888B6E5AE3E46BA08117A")));
		hashes.add(new Pair(_algorithmsValues.id_sha384, StringUtil.toByteArray("E7503CF83D21EB179C3A89FDE8BF2216E5F4F24C9DA9752D8FE86C8A8B71D4BD58A1EF426B8B0071B8C1D0754C71A810")));
		hashes.add(new Pair(_algorithmsValues.id_sha512, StringUtil.toByteArray("AF925EEE76562989CD5DA4000DA2C35F3D9E95BC6604BB13FE3924A6E223914756BF54FCE4CCFD2617DE906EA135B9474CCB1DA83F8468C2DB18341EC7552EDE")));
	}

	public byte[] getDigestOfProfile(int[] aDigestAlgOid) throws ESYAException 
	{
		for (Pair<int [], byte []> aHash : hashes) 
		{
			if(Arrays.equals(aHash.getObject1(), aDigestAlgOid))
				return aHash.getObject2();
		}
		
		throw new ESYAException("Digest of profile according to " + Arrays.toString(aDigestAlgOid) + " algorithm can not be found");
	}

	public InputStream getProfile() throws ESYAException 
	{
		try
		{
			URL url = new URL(msurl);
			URLConnection conn = url.openConnection();
			return conn.getInputStream();
		} 
		catch (Exception e)
		{
			//to-do read from bundle 
			throw new ESYAException("Problem at reading profile document.", e);
		} 
	}

	public String getURI() throws ESYAException
	{
		return msurl;
	}

}
