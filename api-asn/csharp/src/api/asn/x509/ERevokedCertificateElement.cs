using System;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ERevokedCertificateElement : BaseASNWrapper<TBSCertList_revokedCertificates_element>
    {
        public ERevokedCertificateElement(TBSCertList_revokedCertificates_element aElement)
            : base(aElement)
        {
        }

        public ERevokedCertificateElement(BigInteger userCertificate, ETime revocationDate)
            : base(new TBSCertList_revokedCertificates_element(new Asn1BigInteger(userCertificate), revocationDate.getObject()))
        {
        }

        public BigInteger getUserCertificate()
        {
            return mObject.userCertificate.mValue;
        }

        public DateTime? getRevocationDate()
        {
            return UtilTime.timeToDate(mObject.revocationDate);
        }

        public EExtensions getCrlEntryExtensions()
        {
            return new EExtensions(mObject.crlEntryExtensions);
        }

        public void setCrlEntryExtensions(EExtensions crlEntryExtensions)
        {
            mObject.crlEntryExtensions = crlEntryExtensions.getObject();
        }

        public int getCRLReason()
        {
            if (mObject.crlEntryExtensions == null)
            {
                return CRLReason.unspecified().mValue;
            }
            Extension[] extensions = mObject.crlEntryExtensions.elements;

            if (extensions == null || extensions.Length == 0)
            {
                return CRLReason.unspecified().mValue;
            }

            foreach (Extension extension in extensions)
            {
                if (extension.extnID.mValue.SequenceEqual<int>(_ImplicitValues.id_ce_cRLReasons))
                {
                    byte[] value = extension.extnValue.mValue;
                    Asn1DerDecodeBuffer dec = new Asn1DerDecodeBuffer(value);
                    CRLReason crlReason = CRLReason.unspecified();
                    try
                    {
                        crlReason.Decode(dec);
                        return crlReason.mValue;
                    }
                    catch (Exception aEx)
                    {
                        Console.Error.WriteLine(aEx.StackTrace);
                        return CRLReason.unspecified().mValue;
                    }
                }
            }
            return CRLReason.unspecified().mValue;
        }

    }
}
