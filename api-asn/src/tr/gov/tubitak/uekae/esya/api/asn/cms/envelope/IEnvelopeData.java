package tr.gov.tubitak.uekae.esya.api.asn.cms.envelope;

import com.objsys.asn1j.runtime.Asn1BerInputStream;
import com.objsys.asn1j.runtime.Asn1BerOutputStream;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EEncryptedContentInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.OriginatorInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.RecipientInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.RecipientInfos;
import tr.gov.tubitak.uekae.esya.asn.cms.UnprotectedAttributes;

import java.io.IOException;

/**
 * Created by sura.emanet on 22.06.2018.
 */
public interface IEnvelopeData {

    long getVersion();
    void setVersion(long version);

    OriginatorInfo getOriginatorInfo();
    void setOriginatorInfo(OriginatorInfo originatorInfo);

    RecipientInfos getRecipientInfos();
    void setRecipientInfos(RecipientInfos recipientInfos);

    RecipientInfo getRecipientInfo(int aIndex);

    int getRecipientInfoCount();

    void addRecipientInfo(RecipientInfo aRecipientInfo);

    EEncryptedContentInfo getEncryptedContentInfo();
    void setEncryptedContentInfo(EEncryptedContentInfo encryptedContentInfo );

    UnprotectedAttributes getUnprotectedAttributes();
    void setUnprotectedAttributes(UnprotectedAttributes unprotectedAttributes) throws ESYAException;

    byte[] getEncoded();

    Asn1ObjectIdentifier getOID();

    byte[] getMac() throws ESYAException;
    void setMac(byte[] mac) throws ESYAException;

    void readAttrs(Asn1BerInputStream mSifrelenmisVeri)throws IOException;

    void writeAttr(Asn1BerOutputStream mSifrelenmisVeri)throws IOException;
}
