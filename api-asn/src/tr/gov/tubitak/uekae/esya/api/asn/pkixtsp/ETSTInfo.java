package tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;

import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.TSTInfo;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.Calendar;

/**
 * @author ayetgin
 */
public class ETSTInfo extends BaseASNWrapper<TSTInfo>
{

    public ETSTInfo(TSTInfo aObject)
    {
        super(aObject);
    }

    public ETSTInfo(byte[] aBytes) throws ESYAException
    {
        super(aBytes, new TSTInfo());
    }

    public ETSTInfo(InputStream aStream) throws ESYAException {
        super(aStream, new TSTInfo());
    }

    /**
     * @return hash of time stamped data
     */
    public byte[] getHashedMessage(){
    	return mObject.messageImprint.hashedMessage.value;
    }

    /**
     * @return hash algorithm for the data
     */
    public EAlgorithmIdentifier getHashAlgorithm(){
    	return new EAlgorithmIdentifier(mObject.messageImprint.hashAlgorithm);
    }

    /**
     * @return time of timestamp
     * @throws Asn1Exception if cant convert time
     */
    public Calendar getTime() throws Asn1Exception
    {
		return mObject.genTime.getTime();
    }

    /**
     * @return nonce. null if not exists
     */
    public BigInteger getNonce(){
    	if (mObject.nonce != null)
    		return mObject.nonce.value;
    	return null;
    }

    /**
     * @return policy. null if not exists
     */
    public Asn1ObjectIdentifier getPolicy(){
    	return  mObject.policy;
    }

    /**
     * @return serial number of timestamp
     */
    public BigInteger getSerialNumber(){
    	return mObject.serialNumber.value;
    }

    /**
     * @return version of timestamp
     */
    public long getVersion(){
    	return mObject.version.value;
    }

    /**
     * @return micro seconds accuracy of timestamp. null if not exists
     */
    public BigInteger getAccuracyMicros(){
    	if (mObject.accuracy != null && mObject.accuracy.micros != null)
    		return BigInteger.valueOf(mObject.accuracy.micros.value);

    	return null;
    }

    /**
     * @return milli seconds accuracy of timestamp. null if not exists
     */
    public BigInteger getAccuracyMillis(){
    	if (mObject.accuracy != null && mObject.accuracy.millis != null)
    		return BigInteger.valueOf(mObject.accuracy.millis.value);

    	return null;
    }

    /**
     * @return seconds accuracy of timestamp. null if not exists
     */
    public BigInteger getAccuracySeconds(){
    	if (mObject.accuracy != null && mObject.accuracy.seconds != null)
    		return BigInteger.valueOf(mObject.accuracy.seconds.value);

    	return null;
    }

}
