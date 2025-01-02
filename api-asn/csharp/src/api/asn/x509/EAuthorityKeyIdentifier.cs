using System;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EAuthorityKeyIdentifier : BaseASNWrapper<AuthorityKeyIdentifier>, ExtensionType
    {
        public EAuthorityKeyIdentifier(AuthorityKeyIdentifier aObject)
            : base(aObject)
        {
            //super(aObject);
        }

        public EAuthorityKeyIdentifier(Asn1OctetString aKeyID)
            : this(new AuthorityKeyIdentifier(aKeyID, null, null))
        {
        }

        public EAuthorityKeyIdentifier(byte[] aBytes)
            : base(aBytes, new AuthorityKeyIdentifier())
        {
        }

        public byte[] getKeyIdentifier()
        {
            return (mObject.keyIdentifier == null) ? null : mObject.keyIdentifier.mValue;
        }

        public EGeneralNames getAuthorityCertIssuer()
        {
            return (mObject.authorityCertIssuer == null) ? null : new EGeneralNames(mObject.authorityCertIssuer);
        }

        public BigInteger getAuthorityCertSerialNumber()
        {
            return (mObject.authorityCertSerialNumber == null) ? null : mObject.authorityCertSerialNumber.mValue;
        }

        public EExtension toExtension(bool aCritic)
        {
            return new EExtension(EExtensions.oid_ce_authorityKeyIdentifier, aCritic, getBytes());
        }

        public override String ToString()
        {
            StringBuilder result = new StringBuilder();

            if (mObject.keyIdentifier != null)
            {
                result.Append(Resource.message(Resource.AKI_ID) + "=" + mObject.keyIdentifier + "\n");
            }
            if (mObject.authorityCertIssuer != null)
            {
                result.Append(Resource.message(Resource.AKI_ISSUER) + "=" +
                              UtilName.name2String(((Name) mObject.authorityCertIssuer.elements[0].GetElement())) + "\n");
            }
            if (mObject.authorityCertSerialNumber != null)
            {
                result.Append(Resource.message(Resource.AKI_SERIAL) + "=" +
                              mObject.authorityCertSerialNumber.mValue + "\n");
            }
            return result.ToString();
        }
    }
}