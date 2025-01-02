package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1BitString;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.Constants;
import tr.gov.tubitak.uekae.esya.api.asn.pqixqualified.EQCStatements;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.certificate.CertificateUtil;
import tr.gov.tubitak.uekae.esya.asn.esya._esyaValues;
import tr.gov.tubitak.uekae.esya.asn.etsiqc._etsiqcValues;
import tr.gov.tubitak.uekae.esya.asn.util.*;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

import java.io.*;
import java.math.BigInteger;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author ahmety
 * date: Jan 28, 2010
 */
public class ECertificate extends BaseASNWrapper<Certificate>
{

    private static final Logger logger = LoggerFactory.getLogger(ECertificate.class);

    public ECertificate(Certificate aObject)
    {
        super(aObject);
    }
   
    /**
     * Create ECertificate from File
     * @param aFile File
     * @throws ESYAException
     * @throws IOException
     */
    public ECertificate(File aFile) throws ESYAException, IOException
    {
        super(null);
        FileInputStream fis = new FileInputStream(aFile);
        init(fis);
        fis.close();
    }

    /**
     * Create ECertificate from byte array
     * @param aBytes bytes
     * @throws ESYAException
     */
   	public ECertificate(byte[] aBytes)
            throws ESYAException
    {
        super(aBytes, new Certificate());
    }
    

    /**
     * Base64 yapısından ECertificate oluşturur.
     * @param aBase64Encoded Base 64 encoded certificate
     * @throws ESYAException if an encoding problem occurs
     */
    public ECertificate(String aBase64Encoded) throws ESYAException
    {
    	super(aBase64Encoded, new Certificate());
    }

    /**
     * Create ECertificate from InputStream
     * @param aCertStream InputStream
     * @throws ESYAException
     */
    public ECertificate(InputStream aCertStream) throws ESYAException
    {
    	super(null);
        init(aCertStream);
    }

    /**
     * Create ECertificate from elements of certificate
     * @param tbsCertificate ETBSCertificate
     * @param algorithmIdentifier EAlgorithmIdentifier
     * @param signature byte array which signature of certificate
     */
    public ECertificate(ETBSCertificate tbsCertificate, EAlgorithmIdentifier algorithmIdentifier, byte[] signature ) {
        super(new Certificate());
        setTBSCertificate(tbsCertificate);
        setSignatureAlgorithm(algorithmIdentifier);
        setSignatureValue(signature);
    }

    /**
     * Create ECertificate from a file path
     * @param aPath File path of certificate
     * @throws IOException
     * @throws Asn1Exception
     */
    public static ECertificate readFromFile(String aPath) throws IOException, Asn1Exception
    {
        Certificate cer = new Certificate();
        cer = (Certificate) AsnIO.dosyadanOKU(cer, aPath);
        return new ECertificate(cer);
    }

    public void init(InputStream aCertStream) throws ESYAException {
        try
        {
            byte [] certBytes = AsnIO.streamOku(aCertStream);
            mObject = new Certificate();
            mObject = (Certificate) AsnIO.arraydenOku(mObject, certBytes);
        }
        catch (Exception ex)
        {
            throw new ESYAException(ex);
        }
    }

    @Override
    public String getBase64EncodeHeader() {
        return "-----BEGIN CERTIFICATE-----";
    }

    @Override
    public String getBase64EncodeFooter() {
        return "-----END CERTIFICATE-----";
    }

    /**
	 * Returns tbsCertificate of certificate
	 * @return
	 */
    public ETBSCertificate getTBSCertificate(){
        return new ETBSCertificate(mObject.tbsCertificate);
    }

    /**
  	 * Set tbsCertificate of certificate
  	  * @param aTBSCertificate
  	 */
    public void setTBSCertificate(ETBSCertificate aTBSCertificate){
        if( aTBSCertificate == null )
            mObject.tbsCertificate = null;
        else
            mObject.tbsCertificate = aTBSCertificate.getObject();
    }

    /**
  	 * Returns SubjectPublicKeyInfo of certificate
  	 * @return
  	 */
    public ESubjectPublicKeyInfo getSubjectPublicKeyInfo(){
        return new ESubjectPublicKeyInfo(mObject.tbsCertificate.subjectPublicKeyInfo);
    }


    /**
     * @return Certificate serial number in BigInteger form
     */
    public BigInteger getSerialNumber()
    {
        return mObject.tbsCertificate.serialNumber.value;
    }

    /**
     * @return Certificate serial number in hexadecimal String form
     */
    public String getSerialNumberHex()
    {
        return StringUtil.toHexString(getSerialNumber().toByteArray());
    }

    /**
     * Versiyon bilgisini String olarak verir.
     * @return version value as string i.e. "v1"
     */
    public String getVersionStr()
    {
        Version ver = mObject.tbsCertificate.version;
        if (ver == null)
            return "";

        switch ((int) ver.value) {
            case 0:
                return "v1";
            case 1:
                return "v2";
            case 2:
                return "v3";
        }
        return null;
    }
    /**
     * Versiyon bilgisini long olarak verir.
     * @return version value
     */
    public long getVersion()
    {
        Version ver = mObject.tbsCertificate.version;
        if (ver == null)
            return -1;

        return ver.value;
    }    
    /**
  	 * Returns signature value of certificate
  	 * @return
  	 */
    public byte[] getSignatureValue(){
        return mObject.signature.value;
    }
    /**
  	 * Set Signature value of certificate
  	 * @param aSignatureValue byte[]
  	 */
    public void setSignatureValue(byte[] aSignatureValue){
        mObject.signature = new Asn1BitString(aSignatureValue.length<<3, aSignatureValue);
    }
    /**
  	 * Returns time which certificate's validity begins
  	 * @return Calendar
  	 */
    public Calendar getNotBefore()
    {
        return UtilTime.timeToCalendar(mObject.tbsCertificate.validity.notBefore);
    }
    /**
  	 * Returns time which certificate's validity ends
  	 * @return Calendar
  	 */
    public Calendar getNotAfter()
    {
        return UtilTime.timeToCalendar(mObject.tbsCertificate.validity.notAfter);
    }
    /**
  	 * Returns PublicKeyAlgorithm of certificate
  	 * @return
  	 */
    public EAlgorithmIdentifier getPublicKeyAlgorithm()
    {
        return getSubjectPublicKeyInfo().getAlgorithm();
        //return Ozellikler.getAsimAlgoFromOID(mObject.tbsCertificate.subjectPublicKeyInfo.algorithm);
    }
    /**
  	 * Returns Algorithm of certificate's signature
  	 * @return
  	 */
    public EAlgorithmIdentifier getSignatureAlgorithm(){
        return new EAlgorithmIdentifier(mObject.signatureAlgorithm);
    }
    /**
  	 * Set algorithm of certificate's signature
  	 * @param aAlgorithm
  	 */
    public void setSignatureAlgorithm(EAlgorithmIdentifier aAlgorithm){
        if( aAlgorithm == null )
            mObject.signatureAlgorithm = null;
        else
            mObject.signatureAlgorithm = aAlgorithm.getObject();
    }
    /**
  	 * Returns byte array of tbsCertificate
  	 * @return byte[]
  	 */
    public byte[] getTBSEncodedBytes(){
        byte[] imzalanan = null;
        Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
        try
		{
			mObject.tbsCertificate.encode(encBuf);
			imzalanan = encBuf.getMsgCopy();
	        encBuf.reset();
		} catch (Asn1Exception aEx)
		{
			logger.error("Sertifika değeri alınırken hata oluştu.", aEx);
		}
        return imzalanan;
    }
    /**
  	 * Returns subject name of Certificate
  	 * @return EName
  	 */
    public EName getSubject()
    {
        return new EName(mObject.tbsCertificate.subject);
    }
    /**
  	 * Returns issuer name of Certificate
  	 * @return EName
  	 */
    public EName getIssuer()
    {
        return new EName(mObject.tbsCertificate.issuer);
    }
    /**
  	 * Returns email address of Certificate
  	 * @return String
  	 */
    public String getEmail()
    {
        ESubjectAltName san = getExtensions().getSubjectAltName();
        if (san != null) {
            for (int i=0; i<san.getElementCount(); i++) {
                EGeneralName gn = san.getElement(i);
                if (gn.getType() == GeneralName._RFC822NAME)
                    return gn.getRfc822Name();
            }
        }
        return null;
    }

    public ORAddress getX400Name()
    {
        ESubjectAltName san = getExtensions().getSubjectAltName();
        if (san != null) {
            for (int i=0; i<san.getElementCount(); i++) {
                EGeneralName gn = san.getElement(i);
                    if(gn.getX400Address() != null)
                        return gn.getX400Address();
            }
        }
        return null;
    }
    /**
  	 * Returns extension field of Certificate
  	 * @return 
  	 */
    public EExtensions getExtensions()
    {
        return new EExtensions(mObject.tbsCertificate.extensions, this);
    }
    /**
	 * Checks whether certificate is self signed or not.
	 * @return True if certificate is self signed,false otherwise.
	 */
    public boolean isSelfIssued()
    {
        return UtilEsitlikler.esitMi(mObject.tbsCertificate.issuer, mObject.tbsCertificate.subject);
    }
    /**
   	 * Checks whether certificate is qualified or not.
   	 * @return True if certificate is qualified,false otherwise.
   	 */
    public boolean isQualifiedCertificate()
    {
    	EQCStatements qc= this.getExtensions().getQCStatements();
		
		if (qc != null)
		{
			if (qc.checkStatement( new Asn1ObjectIdentifier(_etsiqcValues.id_etsi_qcs_QcCompliance))
					&& (qc.checkStatement(new Asn1ObjectIdentifier(_esyaValues.id_TK_nesoid)) || qc.checkStatement(new Asn1ObjectIdentifier(_esyaValues.id_TK_nesoid_2))))
			{
				return true;
			}
		}
		return false;
    }

    private static Asn1ObjectIdentifier MM_EXT_KEY_USAGE_OID = new Asn1ObjectIdentifier(OIDUtil.parse("2.16.792.1.2.1.1.5.7.50.1"));
    private static Asn1ObjectIdentifier MM_POLICY_OID_ = new Asn1ObjectIdentifier(OIDUtil.parse("2.16.792.1.2.1.1.5.7.4.1"));
    private static Asn1ObjectIdentifier MM_SCT_OID_ = new Asn1ObjectIdentifier(OIDUtil.parse("1.3.6.1.4.1.11129.2.4.2"));

    private static Asn1ObjectIdentifier ELECTRONIC_SEAL_QCSTATEMENTS_OID = new Asn1ObjectIdentifier(OIDUtil.parse("2.16.792.1.61.0.1.5070.1.1"));
    private static Asn1ObjectIdentifier ELECTRONIC_SEAL_QCSTATEMENTS_USAGE_RESTRICTION_OID = new Asn1ObjectIdentifier(OIDUtil.parse("2.16.792.1.61.0.1.5070.1.3"));

    private static Asn1ObjectIdentifier QUALIFIED_ELECTRONIC_SEAL_QCSTATEMENTS_OID = new Asn1ObjectIdentifier(OIDUtil.parse("2.16.792.1.61.0.1.5070.2.1"));
    private static Asn1ObjectIdentifier QUALIFIED_ELECTRONIC_SEAL_QCSTATEMENTS_USAGE_RESTRICTION_OID = new Asn1ObjectIdentifier(OIDUtil.parse("2.16.792.1.61.0.1.5070.2.2"));

    /**
     * Checks whether certificate is Mali Muhur or not.
     * @return True if certificate is Mali Muhur, false otherwise.
     */
    public boolean isMaliMuhurCertificate()
    {
        if(isPolicyIdentifierExists(MM_POLICY_OID_)){
            EExtendedKeyUsage eku = getExtensions().getExtendedKeyUsage();
            return (eku != null) && eku.hasElement(MM_EXT_KEY_USAGE_OID);
        }else
            return false;
    }

    public boolean isKurumsalMuhurCertificate()
    {
        return doesOIDExistInQCStatement(ELECTRONIC_SEAL_QCSTATEMENTS_OID) && doesOIDExistInQCStatement(ELECTRONIC_SEAL_QCSTATEMENTS_USAGE_RESTRICTION_OID);
    }

    public boolean isNitelikliMuhurCertificate()
    {
        return doesOIDExistInQCStatement(QUALIFIED_ELECTRONIC_SEAL_QCSTATEMENTS_OID) && doesOIDExistInQCStatement(QUALIFIED_ELECTRONIC_SEAL_QCSTATEMENTS_USAGE_RESTRICTION_OID);
    }

    public boolean isMuhurCertificate()
    {
        return isKurumsalMuhurCertificate() || isNitelikliMuhurCertificate();
    }

    public boolean isEncryptionCertificate()
    {
        EKeyUsage keyUsage = getExtensions().getKeyUsage();
        if (keyUsage != null) {
          if(keyUsage.isKeyEncipherment() || keyUsage.isDataEncipherment())
              return true;
        }
        return false;
    }

    /**
   	 * Checks whether certificate can sign OCSP or not.
   	 * @return True if certificate can sign OCSP,false otherwise.
   	 */
    public boolean isOCSPSigningCertificate()
    {
    	EExtendedKeyUsage eku = getExtensions().getExtendedKeyUsage();
        return (eku != null) && eku.hasElement(Constants.IMP_ID_KP_OCSPSIGNING);
    }

    /**
     * If this extention exists, no need to make revocation check.
     * @return Gets whether certificate has pkix_ocsp_nocheck.
     */
    public boolean hasOCSPNoCheckExtention()
    {
        return getExtensions().getExtension(EExtensions.oid_pkix_ocsp_nocheck) != null;
    }

    /**
   	 * Checks whether certificate can sign timestamp or not.
   	 * @return True if certificate can sign timestamp,false otherwise.
   	 */
    public boolean isTimeStampingCertificate()
    {
    	EExtendedKeyUsage eku = getExtensions().getExtendedKeyUsage();
        return (eku != null) && eku.hasElement(Constants.IMP_ID_KP_TIMESTAMPING);
    }
    /**
   	 * Checks whether certificate is certificate authority or not.
   	 * @return True if certificate is certificate authority,false otherwise.
   	 */
    public boolean isCACertificate()
    {
        EBasicConstraints bc = getExtensions().getBasicConstraints();
        return bc != null && bc.isCA();
    }

    /**
     * @return AIA uzantısında yer alan OCSP adresleri
     */
    public List<String> getOCSPAdresses()
    {
        EAuthorityInfoAccessSyntax aia = getExtensions().getAuthorityInfoAccessSyntax();
        AccessDescription[] ad = null; 
        
        if(aia != null && aia.getObject() != null)
        	ad = aia.getObject().elements;
        List<String> adresler = new ArrayList<String>();
        if(ad == null)
            return adresler;
        
        for (AccessDescription anAd : ad) {
            if (anAd.accessMethod.equals(Constants.EXP_ID_AD_OCSP)) {
                adresler.add(UtilName.generalName2String(anAd.accessLocation));
            }
        }
        return adresler;
    }
    /**
     * @return CRL distribution points addresses
     */
    public EName getCRLIssuer(){
        ECRLDistributionPoints cdps = getExtensions().getCRLDistributionPoints();
        if(cdps != null)
        {
	        for (int i=0; i<cdps.getCRLDistributionPointCount(); i++){
	            ECRLDistributionPoint cdp = cdps.getCRLDistributionPoint(i);
	            EName issuer =  cdp.getCRLIssuer();
	            if (issuer!=null)
	                return issuer;
	        }
        }
        // crlIssuer ile issuer aynı olmalı.
        return getIssuer();
    }

    /**
     * @return byte array of SCT (Signed Certificate Timestamp)
     */
    public byte[] getSCTValue()
    {
        return getExtensions().getExtension(MM_SCT_OID_).getValue();
    }
 
    public boolean hasIndirectCRL()
    {
        return (! getCRLIssuer().equals(getIssuer()) );
    }

    /**
     * @return certificate which belongs to X509Certificate type
     */
    public X509Certificate asX509Certificate() {
        try {
            return (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(getEncoded()));
        } catch (Exception x){
            throw new ESYARuntimeException("Error converting to X509 Certificate " + this, x);
        }
    }
    /**
     * @return certificate hashCode
     */
    public int hashCode(){
    	return getIssuer().stringValue().hashCode() & getSerialNumber().hashCode();
    }
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    /**
	 * Return certificates features as string
	 */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("\n----- Certificate -----\n")
                .append("Subject: ").append(getSubject().stringValue()).append('\n')
                .append("Issuer: ").append(getIssuer().stringValue()).append('\n')
                .append("Serial: ").append(getSerialNumberHex()).append('\n')
                .append("Valid from ").append(getNotBefore().getTime())
                .append(" to ").append(getNotAfter().getTime()).append('\n')
                .append("Ca: ").append(isCACertificate())
                .append(", self-issued:").append(isSelfIssued()).append('\n')
                .append("----------------------\n");
        return builder.toString();
    }


    private boolean isPolicyIdentifierExists(Asn1ObjectIdentifier oid){

        ECertificatePolicies cp = getExtensions().getCertificatePolicies();
        if(cp != null){
            for (int i = 0; i < cp.getPolicyInformationCount(); i++) {
                if (cp.getPolicyInformation(i).policyIdentifier.equals(oid))
                    return true;
            }
        }
        return false;
    }

    private boolean doesOIDExistInQCStatement(Asn1ObjectIdentifier oid){
        EQCStatements eqcStatements = getExtensions().getQCStatements();
        if(eqcStatements == null){
            return false;
        }
        return eqcStatements.checkStatement(oid);
    }

    public byte[] getId(){
        X509Certificate x509Certificate = this.asX509Certificate();
        byte[] id = CertificateUtil.calculateId(x509Certificate);
        return id;
    }

}

