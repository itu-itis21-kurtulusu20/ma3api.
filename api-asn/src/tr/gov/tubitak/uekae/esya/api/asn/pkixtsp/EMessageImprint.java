package tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;

import java.io.InputStream;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.MessageImprint;

import com.objsys.asn1j.runtime.Asn1OctetString;

public class EMessageImprint extends BaseASNWrapper<MessageImprint> 
{

	public EMessageImprint(MessageImprint aObject)
    {
        super(aObject);
    }

    public EMessageImprint(byte[] aBytes) throws ESYAException
    {
        super(aBytes, new MessageImprint());
    }

    public EMessageImprint(InputStream aIS) throws ESYAException
    {
        super(aIS, new MessageImprint());
    }
    
    public EMessageImprint(EAlgorithmIdentifier hashAlg, byte [] hashedMessage)
    {
    	super(new MessageImprint());
    	mObject.hashAlgorithm = hashAlg.getObject();
    	mObject.hashedMessage = new Asn1OctetString(hashedMessage);
    }
    
    public EAlgorithmIdentifier getHashAlgorithm()
    {
    	return new EAlgorithmIdentifier(mObject.hashAlgorithm);
    }
    
    public byte [] getHashedMessage()
    {
    	return mObject.hashedMessage.value;
    }
}
