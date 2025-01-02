package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1BitString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;

/**
 * @author ahmety
 * date: Feb 12, 2010
 */
public class ESubjectPublicKeyInfo extends BaseASNWrapper<SubjectPublicKeyInfo>
{

    public ESubjectPublicKeyInfo(SubjectPublicKeyInfo aObject)
    {
        super(aObject);
    }
    public ESubjectPublicKeyInfo(byte[] publicKeyInfoInBytes) throws ESYAException {
        super(publicKeyInfoInBytes,new SubjectPublicKeyInfo());
    }

    public EAlgorithmIdentifier getAlgorithm(){
        return new EAlgorithmIdentifier(mObject.algorithm);
    }

    public void setAlgorithm(EAlgorithmIdentifier aAlgorithm){
        mObject.algorithm = aAlgorithm.getObject();
    }

    public byte[] getSubjectPublicKey(){
        return mObject.subjectPublicKey.value;
    }

    public void setSubjectPublicKey(byte[] aValue){
        mObject.subjectPublicKey = new Asn1BitString(aValue.length*8, aValue);
    }



}
