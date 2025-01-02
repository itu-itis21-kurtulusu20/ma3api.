using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;

namespace tr.gov.tubitak.uekae.esya.api.signature.certval
{
    public class CertValidationPolicies
    {
        private readonly Dictionary<CertificateType?, ValidationPolicy> policies = new Dictionary<CertificateType?, ValidationPolicy>();

        public enum CertificateType
        {
            QualifiedCertificate,
            MaliMuhurCertificate,
            KurumsalMuhurCertificate,
            NitelikliMuhurCertificate,
            OCSPSigningCertificate,
            TimeStampingCertificate,
            CACertificate,
            DEFAULT
        }

        //TODO enum values
        private static List<CertificateType> Values()
        {
          List<CertificateType> values=new List<CertificateType>();
          values.Add(CertificateType.QualifiedCertificate);
          values.Add(CertificateType.MaliMuhurCertificate);
          values.Add(CertificateType.KurumsalMuhurCertificate);
          values.Add(CertificateType.OCSPSigningCertificate);
          values.Add(CertificateType.TimeStampingCertificate);
          values.Add(CertificateType.CACertificate);
          values.Add(CertificateType.DEFAULT);
          return values;
        }

        public ValidationPolicy getPolicyFor(ECertificate certificate)
        {
            CertificateType? type = getCertificateType(certificate);
            return getPolicyFor(type);
        }

        public ValidationPolicy getPolicyFor(CertificateType? type)
        {
            if (policies.ContainsKey(type))
                return policies[type];
            if (policies.ContainsKey(CertificateType.DEFAULT))
                return policies[CertificateType.DEFAULT];
            throw new SignatureRuntimeException("No certificate validation policy registered for " + type);
        }

        public void register(String certType, ValidationPolicy policy)
        {
            CertificateType type = getCertificateType(certType);
            if (policies.ContainsKey(type))
                throw new SignatureRuntimeException("You cant have multiple certificate validation configurations for " + type);
            policies[type] = policy;
        }

        public CertificateType? getCertificateType(ECertificate certificate)
        {
            if (certificate == null)
                return null;
            if (certificate.isMaliMuhurCertificate())
                return CertificateType.MaliMuhurCertificate;
            if (certificate.isKurumsalMuhurCertificate())
                return CertificateType.KurumsalMuhurCertificate;
            if (certificate.isNitelikliMuhurCertificate())
                return CertificateType.NitelikliMuhurCertificate;
            if (certificate.isQualifiedCertificate())
                return CertificateType.QualifiedCertificate;
            if (certificate.isOCSPSigningCertificate())
                return CertificateType.OCSPSigningCertificate;
            if (certificate.isTimeStampingCertificate())
                return CertificateType.TimeStampingCertificate;
            if (certificate.isCACertificate())
                return CertificateType.CACertificate;

            return CertificateType.DEFAULT;
        }

        public static CertificateType getCertificateType(String certType)
        {
            if (certType == null || certType.Equals(""))
                return CertificateType.DEFAULT;
            foreach (CertificateType type in Values())
            {
                if (type.ToString().Equals(certType, StringComparison.OrdinalIgnoreCase))
                {
                    return type;
                }
            }
            throw new SignatureRuntimeException("Unknown certificate type " + certType + ". Use one of " + allowedNames());
        }

        public static String allowedNames()
        {
            StringBuilder builder = new StringBuilder();
            int i = 0;

            foreach (CertificateType type in Values())
            {
                i++;
                builder.Append(type);
                if (i < Values().Count())
                    builder.Append(", ");
            }
            return builder.ToString();
        }

        public static void Main(string[] args)
        {
            Console.WriteLine(allowedNames());
        }
    }
}
