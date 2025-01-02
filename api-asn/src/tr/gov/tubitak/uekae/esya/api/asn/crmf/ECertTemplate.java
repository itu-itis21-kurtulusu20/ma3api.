package tr.gov.tubitak.uekae.esya.api.asn.crmf;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.*;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertTemplate;
import tr.gov.tubitak.uekae.esya.asn.util.UtilTime;

import java.math.BigInteger;
import java.util.Calendar;

/**
 * User: zeldal.ozdemir
 * Date: 2/2/11
 * Time: 4:05 PM
 */
public class ECertTemplate extends BaseASNWrapper<CertTemplate> {


    public ECertTemplate() {
        super(new CertTemplate());
    }

    public ECertTemplate(CertTemplate aObject) {
        super(aObject);
    }

    public EVersion getVersion() {
        return new EVersion(mObject.version.value);
    }

    public void setVersion(EVersion version) {
        this.mObject.version = version.getObject();
    }

    public BigInteger getSerialNumber() {
        if (mObject.serialNumber == null)
            return null;
        else
            return mObject.serialNumber.value;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        mObject.serialNumber = new Asn1BigInteger(serialNumber);
    }

    /**
     * @return Certificate serial number in hexadecimal String form
     */
    public String getSerialNumberHex() {
        return StringUtil.toString(getSerialNumber().toByteArray());
    }

    public EAlgorithmIdentifier getSigningAlg() {
        return new EAlgorithmIdentifier(mObject.signingAlg);
    }

    public void setSigningAlg(EAlgorithmIdentifier signingAlg) {
        mObject.signingAlg = signingAlg.getObject();
    }

    public EName getIssuer() {
        return new EName(mObject.issuer);
    }

    public void setIssuer(EName issuer) {
        mObject.issuer = issuer.getObject();
    }

    public EName getSubject() {
        if(mObject.subject == null)
            return null;
        return new EName(mObject.subject);
    }

    public void setSubject(EName subject) {
        mObject.subject = subject.getObject();
    }

    public ESubjectPublicKeyInfo getPublicKey() {
        return (mObject.publicKey == null ? null : new ESubjectPublicKeyInfo(mObject.publicKey));
    }

    public void setPublicKey(ESubjectPublicKeyInfo publicKey) {
        mObject.publicKey = publicKey.getObject();
    }

    public EExtensions getExtensions() {
        return new EExtensions(mObject.extensions);
    }

    public void setExtensions(EExtensions extensions) {
        mObject.extensions = extensions.getObject();
    }

    public Calendar getOptionalValidityStart(){
        if(mObject.validity == null || mObject.validity.notBefore == null)
            return null;
        else
            return UtilTime.timeToCalendar(mObject.validity.notBefore);
    }

    public Calendar getOptionalValidityEnd(){
        if(mObject.validity == null || mObject.validity.notAfter == null)
            return null;
        else
            return UtilTime.timeToCalendar(mObject.validity.notAfter);
    }
}
