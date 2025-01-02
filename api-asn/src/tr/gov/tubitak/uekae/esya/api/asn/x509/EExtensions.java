package tr.gov.tubitak.uekae.esya.api.asn.x509;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.pqixqualified.EQCStatements;
import tr.gov.tubitak.uekae.esya.asn.PKIXqualified.QCStatements;
import tr.gov.tubitak.uekae.esya.asn.PKIXqualified._PKIXqualifiedValues;
import tr.gov.tubitak.uekae.esya.asn.ocsp._ocspValues;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1Type;

/**
 * @author ahmety
 *         date: Jan 29, 2010
 */
public class EExtensions extends BaseASNWrapper<Extensions> {
    // in alphabetical order
    public static final Asn1ObjectIdentifier oid_ce_authorityKeyIdentifier = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_authorityKeyIdentifier);
    public static final Asn1ObjectIdentifier oid_ce_basicConstraints = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_basicConstraints);
    public static final Asn1ObjectIdentifier oid_ce_certificateIssuer = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_certificateIssuer);
    public static final Asn1ObjectIdentifier oid_ce_certificatePolicies = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_certificatePolicies);
    public static final Asn1ObjectIdentifier oid_ce_cRLDistributionPoints = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_cRLDistributionPoints);
    public static final Asn1ObjectIdentifier oid_ce_cRLNumber = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_cRLNumber);
    public static final Asn1ObjectIdentifier oid_ce_cRLReasons = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_cRLReasons);
    public static final Asn1ObjectIdentifier oid_ce_deltaCRLIndicator = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_deltaCRLIndicator);
    public static final Asn1ObjectIdentifier oid_ce_extKeyUsage = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_extKeyUsage);
    public static final Asn1ObjectIdentifier oid_ce_freshestCRL = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_freshestCRL);
    public static final Asn1ObjectIdentifier oid_ce_inhibitAnyPolicy = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_inhibitAnyPolicy);
    public static final Asn1ObjectIdentifier oid_ce_issuerAltName = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_issuerAltName);
    public static final Asn1ObjectIdentifier oid_ce_issuingDistributionPoint = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_issuingDistributionPoint);
    public static final Asn1ObjectIdentifier oid_ce_keyUsage = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_keyUsage);
    public static final Asn1ObjectIdentifier oid_ce_nameConstraints = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_nameConstraints);
    public static final Asn1ObjectIdentifier oid_ce_policyMappings = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_policyMappings);
    public static final Asn1ObjectIdentifier oid_ce_policyConstraints = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_policyConstraints);
    public static final Asn1ObjectIdentifier oid_ce_subjectAltName = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_subjectAltName);
    public static final Asn1ObjectIdentifier oid_ce_subjectDirectoryAttributes = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_subjectDirectoryAttributes);
    public static final Asn1ObjectIdentifier oid_ce_subjectKeyIdentifier = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_subjectKeyIdentifier);

    public static final Asn1ObjectIdentifier oid_pe_authorityInfoAccess = new Asn1ObjectIdentifier(_ImplicitValues.id_pe_authorityInfoAccess);
    public static final Asn1ObjectIdentifier oid_pe_qcStatements = new Asn1ObjectIdentifier(_PKIXqualifiedValues.id_pe_qcStatements);
    public static final Asn1ObjectIdentifier oid_pe_subjectInfoAccess = new Asn1ObjectIdentifier(_ImplicitValues.id_pe_subjectInfoAccess);
    public static final Asn1ObjectIdentifier oid_win_certTemplate = new Asn1ObjectIdentifier(_ImplicitValues.id_win_certTemplate);
    public static final Asn1ObjectIdentifier oid_win_certTemplate_v2 = new Asn1ObjectIdentifier(_ImplicitValues.id_win_certTemplate_v2);
    public static final Asn1ObjectIdentifier oid_win_application_cert_policies = new Asn1ObjectIdentifier(_ImplicitValues.id_win_application_cert_policies);

    public static final Asn1ObjectIdentifier oid_kp_serverAuth  = new Asn1ObjectIdentifier(_ImplicitValues.id_kp_serverAuth);
    public static final Asn1ObjectIdentifier oid_kp_clientAuth  = new Asn1ObjectIdentifier(_ImplicitValues.id_kp_clientAuth);
    
    public static final Asn1ObjectIdentifier oid_pkix_ocsp_nocheck = new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_nocheck);

    public static final Asn1ObjectIdentifier oid_ce_privateKeyUsagePeriod = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_privateKeyUsagePeriod);

    /* ICAO Specific Extensions */
    public static final Asn1ObjectIdentifier oid_id_icao = new Asn1ObjectIdentifier(_ImplicitValues.id_icao);
    public static final Asn1ObjectIdentifier oid_id_icao_mrtd = new Asn1ObjectIdentifier(_ImplicitValues.id_icao_mrtd);
    public static final Asn1ObjectIdentifier oid_id_icao_mrtdsecurity = new Asn1ObjectIdentifier(_ImplicitValues.id_icao_mrtdsecurity);
    public static final Asn1ObjectIdentifier oid_ce_id_icao_mrtd_security_extensionsdocumentTypeList = new Asn1ObjectIdentifier(_ImplicitValues.id_icao_mrtd_security_extensionsdocumentTypeList);


    private ECertificate mCertificate;

    public EExtensions(Extensions aObject)
    {
        super(aObject);
    }

    public EExtensions(EExtension[] aExtensions)
    {
        super(new Extensions());
        mObject.elements = unwrapArray(aExtensions);
    }

    public EExtensions(Extensions aObject, ECertificate aCertificate)
    {
        super(aObject);
        mCertificate = aCertificate;
    }

    public EExtensions(EExtension[] aExtensions, ECertificate aCertificate)
    {
        super(new Extensions());
        mCertificate = aCertificate;
        mObject.elements = unwrapArray(aExtensions);
    }

    public int getExtensionCount()
    {
        return mObject.getLength();
    }

    public EExtension getExtension(int aIndex)
    {
        return new EExtension(mObject.elements[aIndex]);
    }

    //  known extensions in alphabetical order!

    public EAuthorityInfoAccessSyntax getAuthorityInfoAccessSyntax()
    {
        return wrap(oid_pe_authorityInfoAccess, new AuthorityInfoAccessSyntax(), EAuthorityInfoAccessSyntax.class);

    }

    public EAuthorityKeyIdentifier getAuthorityKeyIdentifier()
    {
        return wrap(oid_ce_authorityKeyIdentifier, new AuthorityKeyIdentifier(), EAuthorityKeyIdentifier.class);
    }

    public EBasicConstraints getBasicConstraints()
    {
        return wrap(oid_ce_basicConstraints, new BasicConstraints(), EBasicConstraints.class);
    }

    public ECertificatePolicies getCertificatePolicies()
    {
        return wrap(oid_ce_certificatePolicies, new CertificatePolicies(), ECertificatePolicies.class);
    }

    public ECertificateIssuer getCertificateIssuer()
    {
        return wrap(oid_ce_certificateIssuer, new CertificateIssuer(), ECertificateIssuer.class);
    }

    public EPrivateKeyUsagePeriodExtension getPrivateKeyUsagePeriodExtension()
    {
        return wrap(oid_ce_privateKeyUsagePeriod, new PrivateKeyUsagePeriod(), EPrivateKeyUsagePeriodExtension.class);
    }

    public ECRLDistributionPoints getCRLDistributionPoints()
    {
        return wrap(oid_ce_cRLDistributionPoints, new CRLDistributionPoints(), ECRLDistributionPoints.class);
    }

    public EExtension getCRLNumber()
    {
        return getExtension(oid_ce_cRLNumber);
        //return wrap(oid_ce_cRLNumber, new Extension(), EExtension.class);
    }

    public EExtension getDeltaCRLIndicator()
    {
        return getExtension(oid_ce_deltaCRLIndicator);
        //return wrap(oid_ce_deltaCRLIndicator, new Extension(), EExtension.class);
    }

    public EExtendedKeyUsage getExtendedKeyUsage()
    {
        return wrap(oid_ce_extKeyUsage, new ExtKeyUsageSyntax(), EExtendedKeyUsage.class);
    }

    public Extension getFreshestCRL()
    {
        return decode(oid_ce_freshestCRL, new Extension());
    }

    public InhibitAnyPolicy getInhibitAnyPolicy()
    {
        return decode(oid_ce_inhibitAnyPolicy, new InhibitAnyPolicy());
    }

    public IssuerAltName getIssuerAltName()
    {
        return decode(oid_ce_issuerAltName, new IssuerAltName());
    }

    public EIssuingDistributionPoint getIssuingDistributionPoint()
    {
        return wrap(oid_ce_issuingDistributionPoint, new IssuingDistributionPoint(), EIssuingDistributionPoint.class);
    }

    public EKeyUsage getKeyUsage()
    {
        return wrap(oid_ce_keyUsage, new KeyUsage(), EKeyUsage.class);
    }

    public ENameConstraints getNameConstraints()
    {
        return wrap(oid_ce_nameConstraints, new NameConstraints(), ENameConstraints.class);
    }

    public PolicyConstraints getPolicyConstraints()
    {
        return decode(oid_ce_policyConstraints, new PolicyConstraints());
    }

    public EPolicyMappings getPolicyMappings()
    {
        return wrap(oid_ce_policyMappings, new PolicyMappings(), EPolicyMappings.class);
    }

    public EQCStatements getQCStatements()
    {
        return wrap(oid_pe_qcStatements, new QCStatements(), EQCStatements.class);
    }

    public ESubjectAltName getSubjectAltName()
    {
        return wrap(oid_ce_subjectAltName, new SubjectAltName(), ESubjectAltName.class);
    }

    public ESubjectDirectoryAttributes getSubjectDirectoryAttributes(){
        return wrap(oid_ce_subjectDirectoryAttributes, new SubjectDirectoryAttributes(), ESubjectDirectoryAttributes.class);
    }

    public ESubjectKeyIdentifier getSubjectKeyIdentifier() {
        return wrap(oid_ce_subjectKeyIdentifier, new SubjectKeyIdentifier(), ESubjectKeyIdentifier.class);
    }

    public ECertificateTemplateOID getCertificateTemplateOID(){
        return wrap(oid_win_certTemplate_v2, new CertificateTemplateOID(), ECertificateTemplateOID.class);
    }

    public ECertificatePolicies getWinApplicationCertificatePolicies(){
        return wrap(oid_win_application_cert_policies, new CertificatePolicies(), ECertificatePolicies.class);
    }


    public EExtension getExtension(Asn1ObjectIdentifier aOID)
    // throws IOException, Asn1Exception
    {
        Extension[] extAr = mObject.elements;
        for (Extension extension : extAr)
            if (extension.extnID.equals(aOID)) {
                return new EExtension(extension);
            }
        return null;
    }

    private <W extends BaseASNWrapper, T extends Asn1Type> W wrap(Asn1ObjectIdentifier aOid, T aObject, Class<W> aWrappper)
    {
        T t = decode(aOid, aObject);
        if (t != null) {
            try {
                if (aObject instanceof CRLDistributionPoints){  // yivranç :)
                    return aWrappper.getConstructor(aObject.getClass(), ECertificate.class).newInstance(t, mCertificate);
                }
                return aWrappper.getConstructor(aObject.getClass()).newInstance(t);
            }
            catch (Exception x) {
                logger.error("Constructor bulunamadı! ", x);
                //x.printStackTrace();
            }
        }
        return null;
    }

    private <T extends Asn1Type> T decode(Asn1ObjectIdentifier aOid, T aObject)
    {
        try {
            EExtension ext = getExtension(aOid);
            if (ext != null) {
            	aObject = (T) AsnIO.derOku(aObject, new Asn1DerDecodeBuffer(ext.getObject().extnValue.value));
                return aObject;
            }
        }
        catch (Exception x) {
            logger.debug("Debug in EExtensions", x);
            //x.printStackTrace();
        }
        return null;
    }

}
