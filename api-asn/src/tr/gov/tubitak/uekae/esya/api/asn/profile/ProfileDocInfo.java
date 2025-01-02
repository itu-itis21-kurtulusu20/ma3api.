package tr.gov.tubitak.uekae.esya.api.asn.profile;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.io.InputStream;

public interface ProfileDocInfo 
{
	byte [] getDigestOfProfile(int [] aDigestAlgOid) throws ESYAException;
	
	InputStream getProfile() throws ESYAException;

	String getURI() throws ESYAException;
}
