package tr.gov.tubitak.uekae.esya.api.signature.profile;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;

import java.io.InputStream;

public interface ProfileDocInfo 
{
    String getURI();

	byte[] getDigestOfProfile(DigestAlg digestAlg);
	
	InputStream getProfile() throws SignatureException;
}
