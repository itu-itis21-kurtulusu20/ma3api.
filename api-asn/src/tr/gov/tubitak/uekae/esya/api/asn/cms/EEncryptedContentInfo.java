package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.cms.EncryptedContentInfo;

/**
 * @author ayetgin
 */
public class EEncryptedContentInfo extends BaseASNWrapper<EncryptedContentInfo>
{
    public EEncryptedContentInfo(EncryptedContentInfo aObject)
    {
        super(aObject);
    }

    public EEncryptedContentInfo(Asn1ObjectIdentifier aContentType,
                                 EAlgorithmIdentifier aEncryptionAlgorithm,
                                 byte[] aEncryptedContent)
    {
        super(new EncryptedContentInfo());
        setContentType(aContentType);
        setEncryptionAlgorithm(aEncryptionAlgorithm);
        setEncryptedContent(aEncryptedContent);
    }

    public EAlgorithmIdentifier getEncryptionAlgorithm(){
        return new EAlgorithmIdentifier(mObject.contentEncryptionAlgorithm);
    }

    public void setEncryptionAlgorithm(EAlgorithmIdentifier aEncryptionAlgorithm){
        mObject.contentEncryptionAlgorithm = aEncryptionAlgorithm.getObject();
    }

    public Asn1ObjectIdentifier getContentType(){
        return mObject.contentType;
    }

    public void setContentType(Asn1ObjectIdentifier aContentType){
        mObject.contentType = aContentType;
    }

    public byte[] getEncryptedContent(){
        if (mObject.encryptedContent!=null){
            return mObject.encryptedContent.value;
        }
        return null;
    }

    public void setEncryptedContent(byte[] aEncryptedContent){
        mObject.encryptedContent = new Asn1OctetString(aEncryptedContent);
    }


}
