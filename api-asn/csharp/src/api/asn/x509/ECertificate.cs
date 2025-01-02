using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Runtime.Serialization;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.pqixqualified;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.asn.etsiqc;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Version = tr.gov.tubitak.uekae.esya.asn.x509.Version;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    [Serializable]
    public class ECertificate : BaseASNWrapper<Certificate>, ISerializable 
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
     
        public ECertificate(Certificate aObject)
            : base(aObject)
        {
        }
        /**
              * Create ECertificate from File
              * @param aFile File
              * @throws ESYAException, IOException
              */
        public ECertificate(FileInfo aFile)
            : base(new Certificate())
        {
            FileStream fis = new FileStream(aFile.FullName, FileMode.Open, FileAccess.Read);
            init(fis);
            fis.Close();
        }

        /**
         * Create ECertificate from byte array
         * @param aBytes bytes
         * @throws ESYAException, IOException
         */
        public ECertificate(byte[] aBytes)
            : base(aBytes, new Certificate())
        {
        }

        /**
     * Base64 yapisindan ECertificate olusturur.
     * @param aBase64Encoded Base 64 encoded certificate
     * @throws ESYAException if an encoding problem occurs
     */
        public ECertificate(String aBase64Encoded)
            : base(aBase64Encoded, new Certificate())
        {
        }
        /**
         * Create ECertificate from InputStream
         * @param aCertStream InputStream
         * @throws ESYAException
         */
        public ECertificate(Stream aCertStream)
            : base(new Certificate())
        {
            init(aCertStream);
        }

        public void init(Stream aCertStream)
        {
            try
            {
                byte[] certBytes = AsnIO.streamOku(aCertStream);
                mObject = new Certificate();
                AsnIO.arraydenOku(mObject, certBytes);
            }
            catch (Exception ex)
            {
                throw new ESYAException(ex);
            }
        }

        /**
         * Create ECertificate from elements of certificate
         * @param tbsCertificate ETBSCertificate
         * @param algorithmIdentifier EAlgorithmIdentifier
         * @param signature byte array which signature of certificate
         */
        public ECertificate(ETBSCertificate tbsCertificate, EAlgorithmIdentifier algorithmIdentifier, byte[] signature)
            : base(new Certificate())
        {
            //super(new Certificate());
            setTBSCertificate(tbsCertificate);
            setSignatureAlgorithm(algorithmIdentifier);
            setSignatureValue(signature);
        }

        protected ECertificate(SerializationInfo info, StreamingContext context)
            : base(new Certificate())
        {
            byte[] certBytes = new byte[0];
            certBytes = (byte [])info.GetValue("mObject", certBytes.GetType());
            AsnIO.arraydenOku(mObject, certBytes);
        }


        /**
         * Create ECertificate from a file path
         * @param aPath File path of certificate
         * @throws IOException, Asn1Exception, ESYAException
         */
        public static ECertificate readFromFile(String aPath)
        {
            Certificate cer = new Certificate();
            AsnIO.dosyadanOKU(cer, aPath);
            return new ECertificate(cer);
        }

        public void GetObjectData(SerializationInfo info, StreamingContext context)
        {
            info.AddValue("mObject", getEncoded());
        }
        /**
         * Returns tbsCertificate of certificate
         * @return
         */
        public ETBSCertificate getTBSCertificate()
        {
            return new ETBSCertificate(mObject.tbsCertificate);
        }
        /**
         * Set tbsCertificate of certificate
          * @param aTBSCertificate
         */
        public void setTBSCertificate(ETBSCertificate aTBSCertificate)
        {
            if (aTBSCertificate == null)
                mObject.tbsCertificate = null;
            else
                mObject.tbsCertificate = aTBSCertificate.getObject();
        }
        /**
         * Returns SubjectPublicKeyInfo of certificate
         * @return
         */
        public ESubjectPublicKeyInfo getSubjectPublicKeyInfo()
        {
            return new ESubjectPublicKeyInfo(mObject.tbsCertificate.subjectPublicKeyInfo);
        }


        /**
         * @return Certificate serial number in BigInteger form
         */
        public BigInteger getSerialNumber()
        {
            return mObject.tbsCertificate.serialNumber.mValue;
        }
        /**
         * @return Certificate serial number in hexadecimal String form
         */
        public String getSerialNumberHex()
        {
            //todo biginteger signed olduğunda önüne sign bitleri ekleyebilir. Java'da boyle oldugundan StringUtil classi kullaniliyor            
            //return getSerialNumber().ToString(16);
            return BitConverter.ToString(getSerialNumber().GetData()).Replace("-", String.Empty);
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

            switch ((int)ver.mValue)
            {
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

            return ver.mValue;
        }
        /**
         * Returns signature value of certificate
         * @return
         */
        public byte[] getSignatureValue()
        {
            return mObject.signature.mValue;
        }
        /**
         * Set Signature value of certificate
         * @param aSignatureValue byte[]
         */
        public void setSignatureValue(byte[] aSignatureValue)
        {
            mObject.signature = new Asn1BitString(aSignatureValue.Length << 3, aSignatureValue);
        }
        /**
         * Returns time which certificate's validity begins
         * @return Calendar
         */
        public DateTime? getNotBefore()
        {
            return UtilTime.timeToDate(mObject.tbsCertificate.validity.notBefore);
        }
        /**
         * Returns time which certificate's validity ends
         * @return Calendar
         */
        public DateTime? getNotAfter()
        {
            return UtilTime.timeToDate(mObject.tbsCertificate.validity.notAfter);
        }

        /**
         * Returns PublicKeyAlgorithm of certificate
         * @return
         */

        public EAlgorithmIdentifier getPublicKeyAlgorithm()
        {
            try
            {
                //return Ozellikler.getAsimAlgoFromOID(mObject.tbsCertificate.subjectPublicKeyInfo.algorithm);
                return getSubjectPublicKeyInfo().getAlgorithm();
                //return mObject.tbsCertificate.subjectPublicKeyInfo.algorithm.algorithm.ToString();
            }
            catch (ESYAException aEx)
            {
                logger.Warn("Sertifikadan anahtar algoritması alınırken hata oluştu", aEx);
                return null;
            }
        }
        /**
         * Returns Algorithm of certificate's signature
         * @return
         */
        public EAlgorithmIdentifier getSignatureAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.signatureAlgorithm);
        }
        /**
         * Set algorithm of certificate's signature
         * @param aAlgorithm
         */
        public void setSignatureAlgorithm(EAlgorithmIdentifier aAlgorithm)
        {
            if (aAlgorithm == null)
                mObject.signatureAlgorithm = null;
            else
                mObject.signatureAlgorithm = aAlgorithm.getObject();
        }
        /**
        * Returns byte array of tbsCertificate
        * @return byte[]
        */       
        public byte[] getTBSEncodedBytes()
        {
            byte[] imzalanan = null;
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            try
            {
                mObject.tbsCertificate.Encode(encBuf);
                imzalanan = encBuf.MsgCopy;
                encBuf.Reset();
            }
            catch (Asn1Exception aEx)
            {
                //logger.error("Sertifika değeri alınırken hata oluştu.", aEx);
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
        {       /*    
                SubjectAltName san = getExtensions().getSubjectAltName();
                if (san != null)
                {
                    foreach (GeneralName gn in san.elements)
                    {
                        if (gn.ChoiceID == GeneralName._RFC822NAME)
                            return UtilName.generalName2String(gn);
                    }
                }
                return null;    
                 * */
            ESubjectAltName san = getExtensions().getSubjectAltName();
            if (san != null)
            {
                for (int i = 0; i < san.getElementCount(); i++)
                {
                    EGeneralName gn = san.getElement(i);
                    if (gn.getType() == GeneralName._RFC822NAME)
                        return gn.getRfc822Name();
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
            return (mObject.tbsCertificate.extensions == null) ? null : new EExtensions(mObject.tbsCertificate.extensions, this);
        }
        /**
         * Checks whether certificate is self signed or not.
         * @return True if certificate is self signed,false otherwise.
         */
        public bool isSelfIssued()
        {
            return UtilEsitlikler.esitMi(mObject.tbsCertificate.issuer, mObject.tbsCertificate.subject);
        }
        /**
         * Checks whether certificate is qualified or not.
         * @return True if certificate is qualified,false otherwise.
         */
        public bool isQualifiedCertificate()
        {
            EQCStatements qc = this.getExtensions().getQCStatements();

            if (qc != null)
            {
                if (qc.checkStatement(new Asn1ObjectIdentifier(_etsiqcValues.id_etsi_qcs_QcCompliance))
                        && (qc.checkStatement(new Asn1ObjectIdentifier(OIDESYA.id_TK_nesoid)) || qc.checkStatement(new Asn1ObjectIdentifier(OIDESYA.id_TK_nesoid_2))))
                {
                    return true;
                }
            }
            return false;
        }
        private static Asn1ObjectIdentifier MM_EXT_KEY_USAGE_OID = new Asn1ObjectIdentifier(OIDUtil.parse("2.16.792.1.2.1.1.5.7.50.1"));
        private static Asn1ObjectIdentifier MM_POLICY_OID_ = new Asn1ObjectIdentifier(OIDUtil.parse("2.16.792.1.2.1.1.5.7.4.1"));
        private static Asn1ObjectIdentifier KM_POLICY_OID_ = new Asn1ObjectIdentifier(OIDUtil.parse("2.16.792.1.2.1.1.5.7.1.10"));

        private static Asn1ObjectIdentifier ELECTRONIC_SEAL_QCSTATEMENTS_OID = new Asn1ObjectIdentifier(OIDUtil.parse("2.16.792.1.61.0.1.5070.1.1"));
        private static Asn1ObjectIdentifier ELECTRONIC_SEAL_QCSTATEMENTS_USAGE_RESTRICTION_OID = new Asn1ObjectIdentifier(OIDUtil.parse("2.16.792.1.61.0.1.5070.1.3"));

        private static Asn1ObjectIdentifier QUALIFIED_ELECTRONIC_SEAL_QCSTATEMENTS_OID = new Asn1ObjectIdentifier(OIDUtil.parse("2.16.792.1.61.0.1.5070.2.1"));
        private static Asn1ObjectIdentifier QUALIFIED_ELECTRONIC_SEAL_QCSTATEMENTS_USAGE_RESTRICTION_OID = new Asn1ObjectIdentifier(OIDUtil.parse("2.16.792.1.61.0.1.5070.2.2"));

        /**
         * Checks whether certificate is Mali Muhur or not.
         * @return True if certificate is Mali Muhur, false otherwise.
         */
        public bool isMaliMuhurCertificate()
        {
            if (isPolicyIdentifierExists(MM_POLICY_OID_))
            {
                EExtendedKeyUsage eku = getExtensions().getExtendedKeyUsage();
                return (eku != null) && eku.hasElement(MM_EXT_KEY_USAGE_OID);
            }
            else
                return false;
        }

        public bool isKurumsalMuhurCertificate()
        {
            return doesOIDExistInQCStatement(ELECTRONIC_SEAL_QCSTATEMENTS_OID) && doesOIDExistInQCStatement(ELECTRONIC_SEAL_QCSTATEMENTS_USAGE_RESTRICTION_OID);
        }

        public bool isNitelikliMuhurCertificate()
        {
            return doesOIDExistInQCStatement(QUALIFIED_ELECTRONIC_SEAL_QCSTATEMENTS_OID) && doesOIDExistInQCStatement(QUALIFIED_ELECTRONIC_SEAL_QCSTATEMENTS_USAGE_RESTRICTION_OID);
        }

        public bool isMuhurCertificate()
        {
            return isKurumsalMuhurCertificate() || isNitelikliMuhurCertificate();
        }

        public bool isEncryptionCertificate()
        {
            EKeyUsage keyUsage = getExtensions().getKeyUsage();
            if (keyUsage != null)
            {
                if (keyUsage.isKeyEncipherment() || keyUsage.isDataEncipherment())
                    return true;
            }
            return false;
        }

        /**
         * Checks whether certificate can sign OCSP or not.
         * @return True if certificate can sign OCSP,false otherwise.
         */
        public bool isOCSPSigningCertificate()
        {
            EExtendedKeyUsage eku = getExtensions().getExtendedKeyUsage();
            return (eku != null) && eku.hasElement(Constants.IMP_ID_KP_OCSPSIGNING);
        }

        /**
        * If this extention exists, no need to make revocation check.
        * @return Gets whether certificate has pkix_ocsp_nocheck.
        */
        public bool hasOCSPNoCheckExtention()
        {
            return getExtensions().getExtension(EExtensions.oid_pkix_ocsp_nocheck) != null;
        }

        /**
         * Checks whether certificate can sign timestamp or not.
         * @return True if certificate can sign timestamp,false otherwise.
         */
        public bool isTimeStampingCertificate()
        {
            EExtendedKeyUsage eku = getExtensions().getExtendedKeyUsage();
            return (eku != null) && eku.hasElement(Constants.IMP_ID_KP_TIMESTAMPING);
        }
        /**
         * Checks whether certificate is certificate authority or not.
         * @return True if certificate is certificate authority,false otherwise.
         */
        public bool isCACertificate()
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
            if (aia != null && aia.getObject() != null)
                ad = aia.getObject().elements;
            List<String> adresler = new List<String>();
            if (ad == null)
                return adresler;

            foreach (AccessDescription anAd in ad)
            {
                if (anAd.accessMethod.Equals(Constants.EXP_ID_AD_OCSP))
                {
                    adresler.Add(UtilName.generalName2String(anAd.accessLocation));
                }
            }
            return adresler;
        }
        /**
         * @return CRL distribution points addresses
         */
        public EName getCRLIssuer()
        {
            ECRLDistributionPoints cdps = getExtensions().getCRLDistributionPoints();
            if (cdps != null)
            {
                for (int i = 0; i < cdps.getCRLDistributionPointCount(); i++)
                {
                    ECRLDistributionPoint cdp = cdps.getCRLDistributionPoint(i);
                    EName issuer = cdp.getCRLIssuer();
                    if (issuer != null)
                        return issuer;
                } 
            }
            
            // crlIssuer ile issuer ayni olmali.
            return getIssuer();
        }

        public bool hasIndirectCRL()
        {
            return (!getCRLIssuer().Equals(getIssuer()));
        }
        /**
         * @return certificate which belongs to X509Certificate type
         */
        public X509Certificate2 asX509Certificate2()
        {
            try
            {
                //return (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(getEncoded()));
                return new X509Certificate2(getBytes());
            }
            catch (Exception x)
            {
                throw new ESYARuntimeException("Error converting to X509 Certificate " + this);
            }
        }
        /**
         * @return certificate hashCode
         */
        public override int GetHashCode()
        {
            return getIssuer().stringValue().GetHashCode() & getSerialNumber().GetHashCode();
        }
        /**
         * Return certificates features as string
         */
        //@Override
        public override String ToString()
        {
            StringBuilder builder = new StringBuilder();
            builder.Append("\n----- Certificate -----\n")
                    .Append("Subject: ").Append(getSubject().stringValue()).Append('\n')
                    .Append("Issuer: ").Append(getIssuer().stringValue()).Append('\n')
                    .Append("Serial: ").Append(getSerialNumberHex()).Append('\n')
                    .Append("Valid from ").Append(getNotBefore().Value.ToLocalTime())
                    .Append(" to ").Append(getNotAfter().Value.ToLocalTime()).Append('\n')
                    .Append("Ca: ").Append(isCACertificate())
                    .Append(", self-issued:").Append(isSelfIssued()).Append('\n')
                    .Append("----------------------\n");
            return builder.ToString();
        }


        

        private bool isPolicyIdentifierExists(Asn1ObjectIdentifier oid)
        {
            ECertificatePolicies cp = getExtensions().getCertificatePolicies();
            if (cp != null)
            {
                for (int i = 0; i < cp.getPolicyInformationCount(); i++)
                {
                    if (cp.getPolicyInformation(i).policyIdentifier.Equals(oid))
                        return true;
                }
            }
            return false;
        }

        private bool doesOIDExistInQCStatement(Asn1ObjectIdentifier oid)
        {
            EQCStatements eqcStatements = getExtensions().getQCStatements();
            if (eqcStatements == null)
            {
                return false;
            }
            return eqcStatements.checkStatement(oid);
        }
    }
}
