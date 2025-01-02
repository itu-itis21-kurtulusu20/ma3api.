package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import com.objsys.asn1j.runtime.Asn1BitString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.util.UtilTime;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

import java.math.BigInteger;
import java.util.Calendar;

/**
 * @author ayetgin
 */
public class ETBSCertificate extends BaseASNWrapper<TBSCertificate>
{
    public ETBSCertificate()
    {
        super(new TBSCertificate());
        mObject.validity = new Validity();
    }

    public ETBSCertificate(long aVersion, BigInteger aSerialNumber,
                           EAlgorithmIdentifier aSignatureAlg, EName aIssuer,
                           Calendar aNotBefore, Calendar aNotAfter,
                           EName aSubject, ESubjectPublicKeyInfo aSubjectPublicKeyInfo,
                           byte[] aIssuerUniqueId, byte[] aSubjectUniqueId,
                           EExtensions aExtensions)
    {
        this();
        setVersion(aVersion);
        setSerialNumber(aSerialNumber);
        setSignature(aSignatureAlg);
        setIssuer(aIssuer);
        setNotBefore(aNotBefore);
        setNotAfter(aNotAfter);
        setSubject(aSubject);
        setSubjectPublicKeyInfo(aSubjectPublicKeyInfo);
        setIssuerUniqueID(aIssuerUniqueId);
        setSubjectUniqueID(aSubjectUniqueId);
        setExtensions(aExtensions);
    }

    public ETBSCertificate(TBSCertificate aTBSCertificate)
    {
        super(aTBSCertificate);
    }

    public long getVersion(){
        return mObject.version.value;
    }

    public void setVersion(long aVersion){
        mObject.version.value = aVersion;
    }

    public BigInteger getSerialNumber()
    {
        return mObject.serialNumber.value;
    }

    public void setSerialNumber(BigInteger aSerialNumber)
    {
        mObject.serialNumber = new Asn1BigInteger(aSerialNumber);
    }

    public EAlgorithmIdentifier getSignatureAlgorithm()
    {
        return new EAlgorithmIdentifier(mObject.signature);
    }

    public void setSignature(EAlgorithmIdentifier aSignature)
    {
        mObject.signature = aSignature.getObject();
    }

    public EName getIssuer()
    {
        return new EName(mObject.issuer);
    }

    public void setIssuer(EName aIssuer)
    {
        mObject.issuer = aIssuer.getObject();
    }

    public Calendar getNotBefore(){
        return UtilTime.timeToCalendar(mObject.validity.notBefore);
    }

    public void setNotBefore(Calendar aNotBefore){
        mObject.validity.notBefore = UtilTime.calendarToTimeFor3280(aNotBefore);
    }

    public Calendar getNotAfter(){
        return UtilTime.timeToCalendar(mObject.validity.notAfter);
    }

    public void setNotAfter(Calendar aNotAfter){
        mObject.validity.notAfter = UtilTime.calendarToTimeFor3280(aNotAfter);
    }

    public EName getSubject()
    {
        return new EName(mObject.subject);
    }

    public void setSubject(EName aSubject)
    {
        mObject.subject = aSubject.getObject();
    }

    public ESubjectPublicKeyInfo getSubjectPublicKeyInfo()
    {
        return new ESubjectPublicKeyInfo(mObject.subjectPublicKeyInfo);
    }

    public void setSubjectPublicKeyInfo(ESubjectPublicKeyInfo aSubjectPublicKeyInfo)
    {
        mObject.subjectPublicKeyInfo = aSubjectPublicKeyInfo.getObject();
    }

    public byte[] getIssuerUniqueID()
    {
        if (mObject.issuerUniqueID==null)
            return null;
        return mObject.issuerUniqueID.value;
    }

    public void setIssuerUniqueID(byte[] aIssuerUniqueID)
    {
        mObject.issuerUniqueID = (aIssuerUniqueID==null) ? null : new Asn1BitString(aIssuerUniqueID.length<<3, aIssuerUniqueID);
    }

    public byte[] getSubjectUniqueID()
    {
        if (mObject.subjectUniqueID == null)
            return null;
        return mObject.subjectUniqueID.value;
    }

    public void setSubjectUniqueID(byte[] aSubjectUniqueID)
    {
        mObject.subjectUniqueID = (aSubjectUniqueID==null) ? null : new Asn1BitString(aSubjectUniqueID.length<<3, aSubjectUniqueID);
    }

    public EExtensions getExtensions()
    {
        if (mObject.extensions==null)
            return null;
        return new EExtensions(mObject.extensions);
    }

    public void setExtensions(EExtensions aExtensions)
    {
        mObject.extensions = (aExtensions==null) ? null : aExtensions.getObject();
    }
}
