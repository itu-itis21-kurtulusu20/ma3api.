using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.pqixqualified;
using tr.gov.tubitak.uekae.esya.asn.PKIXqualified;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EExtensions : BaseASNWrapper<Extensions>
    {
        // in alphabetical order
        public static readonly Asn1ObjectIdentifier oid_ce_authorityKeyIdentifier = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_authorityKeyIdentifier);
        public static readonly Asn1ObjectIdentifier oid_ce_basicConstraints = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_basicConstraints);
        public static readonly Asn1ObjectIdentifier oid_ce_certificateIssuer = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_certificateIssuer);
        public static readonly Asn1ObjectIdentifier oid_ce_certificatePolicies = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_certificatePolicies);
        public static readonly Asn1ObjectIdentifier oid_ce_cRLDistributionPoints = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_cRLDistributionPoints);
        public static readonly Asn1ObjectIdentifier oid_ce_cRLNumber = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_cRLNumber);
        public static readonly Asn1ObjectIdentifier oid_ce_cRLReasons = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_cRLReasons);
        public static readonly Asn1ObjectIdentifier oid_ce_deltaCRLIndicator = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_deltaCRLIndicator);
        public static readonly Asn1ObjectIdentifier oid_ce_extKeyUsage = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_extKeyUsage);
        public static readonly Asn1ObjectIdentifier oid_ce_freshestCRL = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_freshestCRL);
        public static readonly Asn1ObjectIdentifier oid_ce_inhibitAnyPolicy = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_inhibitAnyPolicy);
        public static readonly Asn1ObjectIdentifier oid_ce_issuerAltName = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_issuerAltName);
        public static readonly Asn1ObjectIdentifier oid_ce_issuingDistributionPoint = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_issuingDistributionPoint);
        public static readonly Asn1ObjectIdentifier oid_ce_keyUsage = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_keyUsage);
        public static readonly Asn1ObjectIdentifier oid_ce_nameConstraints = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_nameConstraints);
        public static readonly Asn1ObjectIdentifier oid_ce_policyMappings = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_policyMappings);
        public static readonly Asn1ObjectIdentifier oid_ce_policyConstraints = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_policyConstraints);
        public static readonly Asn1ObjectIdentifier oid_ce_subjectAltName = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_subjectAltName);
        public static readonly Asn1ObjectIdentifier oid_ce_subjectDirectoryAttributes = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_subjectDirectoryAttributes);
        public static readonly Asn1ObjectIdentifier oid_ce_subjectKeyIdentifier = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_subjectKeyIdentifier);

        public static readonly Asn1ObjectIdentifier oid_pe_authorityInfoAccess = new Asn1ObjectIdentifier(_ImplicitValues.id_pe_authorityInfoAccess);
        public static readonly Asn1ObjectIdentifier oid_pe_qcStatements = new Asn1ObjectIdentifier(_PKIXqualifiedValues.id_pe_qcStatements);
        public static readonly Asn1ObjectIdentifier oid_pe_subjectInfoAccess = new Asn1ObjectIdentifier(_ImplicitValues.id_pe_subjectInfoAccess);
        public static readonly Asn1ObjectIdentifier oid_win_certTemplate = new Asn1ObjectIdentifier(_ImplicitValues.id_win_certTemplate);

        public static readonly Asn1ObjectIdentifier oid_pkix_ocsp_nocheck = new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_nocheck);


        private readonly ECertificate mCertificate;

        public EExtensions(Extensions aObject)
            : base(aObject)
        {
        }

        public EExtensions(EExtension[] aExtensions)
            : base(new Extensions())
        {
            mObject.elements = unwrapArray<Extension, EExtension>(aExtensions);
        }

        public EExtensions(Extensions aObject, ECertificate aCertificate)
            : base(aObject)
        {
            mCertificate = aCertificate;
        }
        public EExtensions(EExtension[] aExtensions, ECertificate aCertificate)
            : base(new Extensions())
        {
            mCertificate = aCertificate;
            mObject.elements = unwrapArray<Extension, EExtension>(aExtensions);
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
            return wrap<EAuthorityInfoAccessSyntax, AuthorityInfoAccessSyntax>(oid_pe_authorityInfoAccess, new AuthorityInfoAccessSyntax(), typeof(EAuthorityInfoAccessSyntax));
        }

        public EAuthorityKeyIdentifier getAuthorityKeyIdentifier()
        {
            return wrap<EAuthorityKeyIdentifier, AuthorityKeyIdentifier>(oid_ce_authorityKeyIdentifier, new AuthorityKeyIdentifier(), typeof(EAuthorityKeyIdentifier));
        }

        public EBasicConstraints getBasicConstraints()
        {
            return wrap<EBasicConstraints, BasicConstraints>(oid_ce_basicConstraints, new BasicConstraints(), typeof(EBasicConstraints));
        }

        public ECertificatePolicies getCertificatePolicies()
        {
            return wrap<ECertificatePolicies, CertificatePolicies>(oid_ce_certificatePolicies, new CertificatePolicies(), typeof(ECertificatePolicies));
        }

        public ECertificateIssuer getCertificateIssuer()
        {
            CertificateIssuer certificateIssuer = decode<CertificateIssuer>(oid_ce_certificateIssuer, new CertificateIssuer());
            if (certificateIssuer == null)
                return null;
            else
                return new ECertificateIssuer(certificateIssuer);

            //return new ECertificateIssuer(decode<CertificateIssuer>(oid_ce_certificateIssuer, new CertificateIssuer()));     
        }

        public ECRLDistributionPoints getCRLDistributionPoints()
        {
            return wrap<ECRLDistributionPoints, CRLDistributionPoints>(oid_ce_cRLDistributionPoints, new CRLDistributionPoints(), typeof(ECRLDistributionPoints));
        }

        public EExtension getCRLNumber()
        {
            //return new EExtension(getExtension(oid_ce_cRLNumber));
            return getExtension(oid_ce_cRLNumber);
        }

        public EExtension getDeltaCRLIndicator()
        {
            return getExtension(oid_ce_deltaCRLIndicator);
        }

        public EExtendedKeyUsage getExtendedKeyUsage()
        {
            return wrap<EExtendedKeyUsage, ExtKeyUsageSyntax>(oid_ce_extKeyUsage, new ExtKeyUsageSyntax(), typeof(EExtendedKeyUsage));
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
            return wrap<EIssuingDistributionPoint, IssuingDistributionPoint>(oid_ce_issuingDistributionPoint, new IssuingDistributionPoint(), typeof(EIssuingDistributionPoint));
        }

        public EKeyUsage getKeyUsage()
        {
            KeyUsage keyUsage = decode(oid_ce_keyUsage, new KeyUsage());
            return (keyUsage == null) ? null : new EKeyUsage(keyUsage);
        }

        public ENameConstraints getNameConstraints()
        {
            return wrap<ENameConstraints, NameConstraints>(oid_ce_nameConstraints, new NameConstraints(), typeof(ENameConstraints));
        }

        public PolicyConstraints getPolicyConstraints()
        {
            return decode(oid_ce_policyConstraints, new PolicyConstraints());
        }

        public EPolicyMappings getPolicyMappings()
        {
            return wrap<EPolicyMappings, PolicyMappings>(oid_ce_policyMappings, new PolicyMappings(), typeof(EPolicyMappings));
        }

        public EQCStatements getQCStatements()
        {
            return wrap<EQCStatements, QCStatements>(oid_pe_qcStatements, new QCStatements(), typeof(EQCStatements));
        }
        public ESubjectAltName getSubjectAltName()
        {
            EGeneralNames generalNames = wrap<EGeneralNames, GeneralNames>(oid_ce_subjectAltName, new GeneralNames(), typeof(EGeneralNames));
            return (generalNames == null) ? null : new ESubjectAltName(generalNames);
        }
        public ESubjectDirectoryAttributes getSubjectDirectoryAttributes()
        {
            return wrap<ESubjectDirectoryAttributes, SubjectDirectoryAttributes>(oid_ce_subjectDirectoryAttributes, new SubjectDirectoryAttributes(), typeof(ESubjectDirectoryAttributes));
        }
        public ESubjectKeyIdentifier getSubjectKeyIdentifier()
        {
            return wrap<ESubjectKeyIdentifier, SubjectKeyIdentifier>(oid_ce_subjectKeyIdentifier, new SubjectKeyIdentifier(), typeof(ESubjectKeyIdentifier));
        }


        public EExtension getExtension(Asn1ObjectIdentifier aOID)
        {
            Extension[] extAr = mObject.elements;
            foreach (Extension extension in extAr)
                if (extension.extnID.Equals(aOID))
                {
                    return new EExtension(extension);
                }
            return null;
        }


        private /*<W extends BaseASNWrapper, T extends Asn1Type> W*/W wrap<W, T>(Asn1ObjectIdentifier aOid, T aObject, /*Class<W>*/Type aWrappper)
            where T : Asn1Type
            where W : BaseASNWrapper<T>
        {
            T t = decode(aOid, aObject);
            if (t != null)
            {
                try
                {
                    /*if (aObject instanceof CRLDistributionPoints){
                        return aWrappper.getConstructor(aObject.getClass(), ECertificate.class).newInstance(t, mCertificate);
                    }
                    return aWrappper.getConstructor(aObject.getClass()).newInstance(t);
                     * */
                    if (aObject is CRLDistributionPoints)
                    {
                        //return aWrappper.GetConstructor(new Type[] { Type.GetType(aObject.GetType().Name), Type.GetType(ECertificate) }).Invoke(new T[] { t, mCertificate });
                        return (W)aWrappper.GetConstructor(new Type[] { typeof(T), typeof(ECertificate) }).Invoke(new Object[] { t, mCertificate });
                    }
                    //return (BaseASNWrapper<T>)aWrappper.GetConstructor(new Type[] { Type.GetType(aObject.GetType().Name) }).Invoke(new Object[] { t });
                    return (W)aWrappper.GetConstructor(new Type[] { typeof(T) }).Invoke(new Object[] { t });
                }
                catch (Exception x)
                {
                    logger.Error("Constructor bulunamadı! " + x.Message);
                    //x.printStackTrace();
                }
            }
            return null;
        }

        private /*<T extends Asn1Type>*/ T decode<T>(Asn1ObjectIdentifier aOid, T aObject)
            where T : Asn1Type
        {
            try
            {
                EExtension ext = getExtension(aOid);
                if (ext != null)
                {
                    (aObject as Asn1Type).Decode(new Asn1DerDecodeBuffer(ext.getObject().extnValue.mValue));
                    return aObject;
                }
            }
            catch (Exception x)
            {
                logger.Debug(x.Message);
                //x.printStackTrace();
            }
            return default(T);  //Specifies the default value of the type parameter. This will be null for reference types and zero for value types!!!!

        }
    }
}
