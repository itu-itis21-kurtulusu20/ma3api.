package tr.gov.tubitak.uekae.esya.api.asn.cms.envelope;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EEncryptedContentInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.*;

import java.io.IOException;

/**
 * Created by sura.emanet on 26.06.2018.
 */
public class EAuthenticatedEnvelopedData extends EnvelopeASNWrapper<AuthEnvelopedData>  {

    public EAuthenticatedEnvelopedData(AuthEnvelopedData aObject) {
        super(aObject);
    }

    public EAuthenticatedEnvelopedData(byte[] aBytes) throws ESYAException
    {
        super(aBytes, new AuthEnvelopedData());
    }

    public long getVersion() {
        return mObject.version.value;
    }

    public void setVersion(long version) {
        mObject.version = new CMSVersion(version);
    }

    public RecipientInfos getRecipientInfos() {
        return mObject.recipientInfos;
    }
    public void setRecipientInfos(RecipientInfos recipientInfos) {
        mObject.recipientInfos = recipientInfos;
    }

    public EEncryptedContentInfo getEncryptedContentInfo() {
        return new EEncryptedContentInfo(mObject.authEncryptedContentInfo);
    }
    public void setEncryptedContentInfo(EEncryptedContentInfo aEncryptedContentInfo){
        mObject.authEncryptedContentInfo = aEncryptedContentInfo.getObject();
    }

    public UnprotectedAttributes getUnprotectedAttributes() { return null; }
    public void setUnprotectedAttributes(UnprotectedAttributes unprotectedAttributes) throws ESYAException { throw new ESYAException("Not implemented..");}

    public Asn1ObjectIdentifier getOID() {
        return new Asn1ObjectIdentifier(_cmsValues.id_ct_authEnvelopedData);
    }

    public OriginatorInfo getOriginatorInfo() {
        return mObject.originatorInfo;
    }
    public void setOriginatorInfo(OriginatorInfo originatorInfo) {
        mObject.originatorInfo = originatorInfo;
    }

    public byte[] getMac(){return mObject.mac.value; }
    public void setMac(byte[] mac){  mObject.mac = new Asn1OctetString(mac);}

    public void readAttrs(Asn1BerInputStream sifrelenmisVeri) throws IOException {

        int available = sifrelenmisVeri.available();
        if(available > 0) {
            Asn1Tag tag;
            Asn1Tag attrTag;

            tag = sifrelenmisVeri.peekTag();
            attrTag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 1);
            if (tag != null && tag.equals(attrTag)) {
                int len = sifrelenmisVeri.decodeTagAndLength(attrTag);
                AuthAttributes aa = new AuthAttributes();
                aa.decode(sifrelenmisVeri, false, len);
                mObject.authAttrs = aa;
            }

            tag= sifrelenmisVeri.peekTag();
            attrTag = new Asn1Tag(Asn1Tag.UNIV, Asn1Tag.PRIM, 4);
            if(tag != null && tag.equals(attrTag)){
                Asn1OctetString mac = new Asn1OctetString();
                mac.decode(sifrelenmisVeri);
                mObject.mac = mac;
            }

            if(sifrelenmisVeri.available() > 0) {
                tag = sifrelenmisVeri.peekTag();
                attrTag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 2);
                if (tag != null && tag.equals(attrTag)) {
                    int len = sifrelenmisVeri.decodeTagAndLength(attrTag);
                    UnauthAttributes uaa = new UnauthAttributes();
                    uaa.decode(sifrelenmisVeri, false, len);
                    mObject.unauthAttrs = uaa;
                }
            }
        }
    }

    public void writeAttr(Asn1BerOutputStream sifrelenmisVeri) throws IOException {

            if(mObject.authAttrs != null){
                Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();
                int len = mObject.authAttrs.encode(buffer,false);
                buffer.encodeTagAndLength (Asn1Tag.CTXT, Asn1Tag.CONS, 1, len);
                sifrelenmisVeri.write(buffer.getMsgCopy());
            }
            if(mObject.mac != null){
                Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();
                mObject.mac.encode(buffer, true);
                sifrelenmisVeri.write(buffer.getMsgCopy());

            }
            if(mObject.unauthAttrs != null){
                Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();
                int len = mObject.unauthAttrs.encode(buffer,false);
                buffer.encodeTagAndLength (Asn1Tag.CTXT, Asn1Tag.CONS, 2, len);
                sifrelenmisVeri.write(buffer.getMsgCopy());
            }
    }
}
