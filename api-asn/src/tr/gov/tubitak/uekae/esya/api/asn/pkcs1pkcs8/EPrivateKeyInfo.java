package tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;

import com.objsys.asn1j.runtime.Asn1OctetString;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.pkcs10.EAttributes;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EVersion;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8.PrivateKeyInfo;


/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 10:13 AM
 */
public class EPrivateKeyInfo extends BaseASNWrapper<PrivateKeyInfo>{

    public EPrivateKeyInfo(byte[] aBytes) throws ESYAException {
        super(aBytes, new PrivateKeyInfo());
    }
    
    public EPrivateKeyInfo(EVersion aVersion, EAlgorithmIdentifier aAlg, byte[] aPrivateKeyBytes) throws ESYAException {
        super(new PrivateKeyInfo(aVersion.getObject(),aAlg.getObject(),new Asn1OctetString(aPrivateKeyBytes)));
    }
    
    
    public EAttributes getEAttributes(){
        return new EAttributes(mObject.attributes);
    }

    public byte[] getPrivateKey(){
        byte[] privateKey = null;
        if(mObject!=null && mObject.privateKey!=null){
            privateKey = mObject.privateKey.value;
        }
        return privateKey;
    }
}
