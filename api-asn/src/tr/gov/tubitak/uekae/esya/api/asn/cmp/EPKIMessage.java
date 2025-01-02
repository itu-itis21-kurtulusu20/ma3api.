package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import com.objsys.asn1j.runtime.Asn1BitString;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.CMPCertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIMessage;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIMessage_extraCerts;
import tr.gov.tubitak.uekae.esya.asn.cmp.ProtectedPart;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: zeldal.ozdemir
 * Date: 1/31/11
 * Time: 1:37 PM
 */
public class EPKIMessage extends BaseASNWrapper<PKIMessage> {

    protected static Logger logger = LoggerFactory.getLogger(EPKIMessage.class);

    public EPKIMessage(PKIMessage aObject) {
        super(aObject);
    }

    public EPKIMessage(InputStream aStream) throws ESYAException {
        super(aStream, new PKIMessage());
    }

    public EPKIMessage(byte[] aBytes) throws ESYAException {
        super(aBytes, new PKIMessage());
    }

    public EPKIMessage(EPKIHeader header, EPKIBody body, byte[] signature, List<ECertificate> extraCerts) throws ESYAException {
        this(header, body);
        setSignatureValue(signature);
        addExtraCerts(extraCerts);
    }

    public EPKIMessage(EPKIHeader header, EPKIBody body) {
        super(new PKIMessage());
        mObject.header = header.getObject();
        mObject.body = body.getObject();
    }

    public void setSignatureValue(byte[] signature) {
        mObject.protection = new Asn1BitString(signature.length << 3, signature);
    }

    public byte[] getSignatureValue() {
        return mObject.protection.value;
    }

    public void addExtraCerts(List<ECertificate> extraCerts) {
        CMPCertificate[] cmpCertificates = new CMPCertificate[extraCerts.size()];
        for (int i = 0; i < extraCerts.size(); i++)
            cmpCertificates[i] = new CMPCertificate(CMPCertificate._X509V3PKCERT, extraCerts.get(i).getObject());

        if (mObject.extraCerts != null
                && mObject.extraCerts.elements != null
                && mObject.extraCerts.elements.length > 0) {
            List<CMPCertificate> newExtraCerts = new ArrayList<CMPCertificate>();
            newExtraCerts.addAll(Arrays.asList(mObject.extraCerts.elements));
            newExtraCerts.addAll(Arrays.asList(cmpCertificates));
            cmpCertificates = newExtraCerts.toArray(new CMPCertificate[newExtraCerts.size()]);
        }
        if(cmpCertificates.length >0)
            mObject.extraCerts = new PKIMessage_extraCerts(cmpCertificates);
    }

    public List<ECertificate> getExtraCerts() {
        ArrayList<ECertificate> certificates = new ArrayList<ECertificate>();
        if (mObject.extraCerts == null
                || mObject.extraCerts.elements == null
                || mObject.extraCerts.elements.length == 0)
            return certificates;

        for (CMPCertificate element : mObject.extraCerts.elements)
            certificates.add(new ECertificate((Certificate) element.getElement()));

        return certificates;
    }

    public String getMessageOrigin() {
        String mesajKaynagiBilgileri;
        int i;

        if ((mObject == null) || (mObject.header == null))
            return "";
        mesajKaynagiBilgileri = "Mesaji gonderen:" + new EGeneralName(mObject.header.sender);
        if (mObject.header.freeText != null) {
            mesajKaynagiBilgileri += "\n MesajdakiFreeText:";
            if (mObject.header.freeText.elements != null)
                for (i = 0; i < mObject.header.freeText.elements.length; i++)
                    mesajKaynagiBilgileri += "\n\t" + mObject.header.freeText.elements[i].value;
        }
        if (mObject.header.senderKID != null) {
            try {
                mesajKaynagiBilgileri += "\nGonderenin referans no:" + new String(mObject.header.senderKID.value, "ASCII");
            } catch (java.io.UnsupportedEncodingException e) {
				logger.debug("Ref no alinirken problem cikti.", e);
            }
        }
        return mesajKaynagiBilgileri;
    }

    public EPKIHeader getHeader() {
        return new EPKIHeader(mObject.header);
    }

    public EPKIBody getBody() {
        return new EPKIBody(mObject.body);
    }

    public int getChoiceID() {
        return mObject.body.getChoiceID();
    }

    public byte[] getProtectedPart() throws Asn1Exception {
        ProtectedPart protectedPart = new ProtectedPart(mObject.header, mObject.body);
        Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
        protectedPart.encode(encBuf);
        return encBuf.getMsgCopy();
    }
}
