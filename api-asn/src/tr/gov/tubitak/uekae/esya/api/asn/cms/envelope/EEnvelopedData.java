package tr.gov.tubitak.uekae.esya.api.asn.cms.envelope;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EEncryptedContentInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.*;

import java.io.IOException;

/**
 * @author ayetgin
 */
public class EEnvelopedData extends EnvelopeASNWrapper<EnvelopedData>
{
    public EEnvelopedData(EnvelopedData aObject)
    {
        super(aObject);
    }

    public EEnvelopedData(byte[] aBytes) throws ESYAException
    {
        super(aBytes, new EnvelopedData());
    }

    public long getVersion(){
        return mObject.version.value;
    }
    public void setVersion(long aVersion){
        mObject.version = new CMSVersion(aVersion);
    }

    public OriginatorInfo getOriginatorInfo(){
        return mObject.originatorInfo;
    }
    public void setOriginatorInfo(OriginatorInfo aOriginatorInfo){
        mObject.originatorInfo = aOriginatorInfo;
    }

    public RecipientInfos getRecipientInfos(){
        return mObject.recipientInfos;
    }
    public void setRecipientInfos(RecipientInfos recipientInfos){
        mObject.recipientInfos = recipientInfos;
    }

    public UnprotectedAttributes getUnprotectedAttributes(){ return mObject.unprotectedAttrs; }
    public void setUnprotectedAttributes(UnprotectedAttributes aAttr){mObject.unprotectedAttrs = aAttr;}

    public Asn1ObjectIdentifier getOID() {
        return new Asn1ObjectIdentifier(_cmsValues.id_envelopedData);
    }

    public byte[] getMac() throws ESYAException { throw new ESYAException("Not supported"); }
    public void setMac(byte[] mac) throws ESYAException { throw new ESYAException("Not supported.."); }

    public void readAttrs(Asn1BerInputStream sifrelenmisVeri) throws IOException {
        int available = sifrelenmisVeri.available();
        if(available > 0) {
            Asn1Tag tag = sifrelenmisVeri.peekTag();
            Asn1Tag unProtectedAttributeTag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 1);
            if (tag != null && tag.equals(unProtectedAttributeTag)) {
                int len = sifrelenmisVeri.decodeTagAndLength(unProtectedAttributeTag);
                UnprotectedAttributes ua = new UnprotectedAttributes();
                ua.decode(sifrelenmisVeri, false, len);
                mObject.unprotectedAttrs = ua;
            }
        }
    }

    public void writeAttr(Asn1BerOutputStream sifrelenmisVeri) throws IOException {
        if(mObject.unprotectedAttrs != null)
        {
            Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();
            int len = mObject.unprotectedAttrs.encode(buffer,false);
            buffer.encodeTagAndLength (Asn1Tag.CTXT, Asn1Tag.CONS, 1, len);
            sifrelenmisVeri.write(buffer.getMsgCopy());
        }
    }

    public EEncryptedContentInfo getEncryptedContentInfo(){ return new EEncryptedContentInfo(mObject.encryptedContentInfo); }

    public void setEncryptedContentInfo(EEncryptedContentInfo aEncryptedContentInfo){ mObject.encryptedContentInfo = aEncryptedContentInfo.getObject();}
}
