package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OpenType;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

import java.util.Arrays;

/**
 * @author ayetgin
 */
public class EAlgorithmIdentifier extends BaseASNWrapper<AlgorithmIdentifier>
{
    //private static Logger logger = LoggerFactory.getLogger(EAlgorithmIdentifier.class);

    public static final byte[] ASN_NULL = new byte[]{0x05,0x00};

    // wrapper methods start here
    public EAlgorithmIdentifier(AlgorithmIdentifier aObject)
    {
        super(aObject);
    }
    
    public EAlgorithmIdentifier(byte[] aBytes)
    throws ESYAException
    {
    	super(aBytes,new AlgorithmIdentifier());
    }

/*
    public EAlgorithmIdentifier(int[] aOID)
    {
        super(new AlgorithmIdentifier(aOID));
    }
*/

    public EAlgorithmIdentifier(int[] aAlgorithm, byte[] aParams)
    {
        super(new AlgorithmIdentifier(aAlgorithm, aParams==null?null:new Asn1OpenType(aParams)));
    }

    public EAlgorithmIdentifier(Asn1ObjectIdentifier aIdentifier, Asn1OpenType aOpenType)
    {
        super(new AlgorithmIdentifier(aIdentifier, aOpenType));
    }
    
    public Asn1ObjectIdentifier getAlgorithm()
    {
        return mObject.algorithm;
    }

    public void setAlgorithm(Asn1ObjectIdentifier aAlgorithm)
    {
        mObject.algorithm = aAlgorithm;
    }

    public boolean hasParameters(){
        return mObject.parameters!=null && !Arrays.equals(mObject.parameters.value, ASN_NULL);
    }

    public Asn1OpenType getParameters()
    {
        return mObject.parameters;
    }

    public void setParameters(Asn1OpenType aParameters)
    {
        mObject.parameters = aParameters;
    }

    public boolean isAlgorithmEquals(Asn1ObjectIdentifier objectIdentifier){
        if(mObject == null || mObject.algorithm == null || objectIdentifier == null )
            return  false;
        return mObject.algorithm.equals(objectIdentifier);
    }

}
