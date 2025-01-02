package tr.gov.tubitak.uekae.esya.api.asn.attrcert;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import com.objsys.asn1j.runtime.Asn1BitString;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1GeneralizedTime;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralNames;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.asn.attrcert.*;
import tr.gov.tubitak.uekae.esya.asn.x509.Extensions;

import java.math.BigInteger;
import java.util.Calendar;

/**
 User: zeldal.ozdemir
 Date: 3/15/11
 Time: 4:38 PM
 */
public class EAttributeCertificate extends BaseASNWrapper<AttributeCertificate> {

    public EAttributeCertificate(AttributeCertificate aObject) {
        super(aObject);
    }

    public EAttributeCertificate() {
        super(new AttributeCertificate());
        mObject.acinfo = new AttributeCertificateInfo();
        mObject.acinfo.version = new AttCertVersion(AttCertVersion.v2);
    }

    public EAttributeCertificate(byte[] aBytes) throws ESYAException {
        super(aBytes, new AttributeCertificate());
    }

    public void setSignature(byte[] signature) {
        this.mObject.signatureValue = new Asn1BitString(signature.length << 3, signature);
    }

    public void setAlgorithm(EAlgorithmIdentifier algorithmIdentifier) {
        this.mObject.signatureAlgorithm = algorithmIdentifier.getObject();
        this.mObject.acinfo.signature = algorithmIdentifier.getObject();
    }

    public byte[] getSignature() {
        return this.mObject.signatureValue.value;
    }

    public EAlgorithmIdentifier getAlgorithmIdentifier() {
        return new EAlgorithmIdentifier(this.mObject.signatureAlgorithm);
    }

    public EHolder getHolder() {
        return new EHolder(mObject.acinfo.holder);
    }

    public void setHolder(EHolder holder) {
        this.mObject.acinfo.holder = holder.getObject();
    }

    public EGeneralNames getIssuer() {
        if (mObject.acinfo.issuer == null)
            return null;
        if (mObject.acinfo.issuer.getChoiceID() != AttCertIssuer._V2FORM)
            throw new ESYARuntimeException("AttCertIssuer must be V2 Form:" + mObject.acinfo.issuer.getChoiceID());
        V2Form v2Form = (V2Form) mObject.acinfo.issuer.getElement();
        if (v2Form.issuerName == null)
            return null;
        return new EGeneralNames(v2Form.issuerName);
    }

    public void setIssuer(EGeneralNames issuer) {
        AttCertIssuer attCertIssuer = new AttCertIssuer();
        V2Form v2Form = new V2Form();
        v2Form.issuerName = issuer.getObject();
        attCertIssuer.set_v2Form(v2Form);
        this.mObject.acinfo.issuer = attCertIssuer;
    }

    public BigInteger getSerialNumber() {
        return mObject.acinfo.serialNumber.value;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.mObject.acinfo.serialNumber = new Asn1BigInteger(serialNumber);
    }

    /**
     @return Certificate serial number in hexadecimal String form
     */
    public String getSerialNumberHex() {
        return StringUtil.toString(getSerialNumber().toByteArray());
    }

    public Calendar getAttrCertValidityNotBefore() throws Asn1Exception {
        return this.mObject.acinfo.attrCertValidityPeriod.notBeforeTime.getTime();
    }

    public Calendar getAttrCertValidityNotAfter() throws Asn1Exception {
        return this.mObject.acinfo.attrCertValidityPeriod.notAfterTime.getTime();
    }

    public void setAttrCertValidityNotBefore(Calendar notBefore) throws Asn1Exception {
        Asn1GeneralizedTime asn1GeneralizedTime = new Asn1GeneralizedTime();
        asn1GeneralizedTime.setTime(notBefore);
        asn1GeneralizedTime.setFraction("");
        if (this.mObject.acinfo.attrCertValidityPeriod == null)
            this.mObject.acinfo.attrCertValidityPeriod = new AttCertValidityPeriod();
        this.mObject.acinfo.attrCertValidityPeriod.notBeforeTime = asn1GeneralizedTime;
    }

    public void setAttrCertValidityNotAfter(Calendar notAfter) throws Asn1Exception {
        Asn1GeneralizedTime asn1GeneralizedTime = new Asn1GeneralizedTime();
        asn1GeneralizedTime.setTime(notAfter);
        asn1GeneralizedTime.setFraction("");
        if (this.mObject.acinfo.attrCertValidityPeriod == null)
            this.mObject.acinfo.attrCertValidityPeriod = new AttCertValidityPeriod();
        this.mObject.acinfo.attrCertValidityPeriod.notAfterTime = asn1GeneralizedTime;
    }

    public EAttribute[] getAttributes() {
        return wrapArray(mObject.acinfo.attributes.elements, EAttribute.class);
    }

    public void setAttributes(EAttribute[] attributes) {
        this.mObject.acinfo.attributes = new _SeqOfAttribute(unwrapArray(attributes));
    }

    public EACExtensions getExtensions() {
        return new EACExtensions(mObject.acinfo.extensions);
    }

    public void setExtensions(EACExtensions aExtensions) {
        this.mObject.acinfo.extensions = aExtensions.getObject();
    }

    public void setExtensions(EExtension[] extensions) {
        this.mObject.acinfo.extensions = new Extensions(unwrapArray(extensions));
    }

    public byte[] getAttributeCertificateInfoBytes() {
        return new BaseASNWrapper<AttributeCertificateInfo>(mObject.acinfo).getEncoded();
        // A: isnt it wiser?
        // B: test it first Dude..
    }

}
