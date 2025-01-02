using System;
using System.Collections.Generic;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EAuthorityInfoAccessSyntax : BaseASNWrapper<AuthorityInfoAccessSyntax>, ExtensionType
    {
        public EAuthorityInfoAccessSyntax(AuthorityInfoAccessSyntax aObject)
            : base(aObject)
        {
        }
        public EAuthorityInfoAccessSyntax(int[][] metodlar, String[] yerler)
            : base(null)
        {
            //super(null);

            if (metodlar.Length != yerler.Length)
                throw new ESYAException("Invalid AIA arguments");

            int s = metodlar.Length;

            AccessDescription[] elem = new AccessDescription[s];
            GeneralName temp;

            for (int i = 0; i < s; i++)
            {
                temp = new GeneralName();
                temp.Set_uniformResourceIdentifier(new Asn1IA5String(yerler[i]));
                elem[i] = new AccessDescription(metodlar[i], temp);
            }

            mObject = new AuthorityInfoAccessSyntax(elem);
        }

        public int getAccessDescriptionCount()
        {
            return mObject.getLength();
        }

        public EAccessDescription getAccessDescription(int aIndex)
        {
            return new EAccessDescription(mObject.elements[aIndex]);
        }

        public List<String> getCAIssuerAddresses()
        {
            return getAddressesByType(null);
        }

        public String getCAIssuerAddressesStr()
        {
            List<String> aialar = getCAIssuerAddresses();
            if (aialar == null || aialar.Count == 0)
            {
                return "";
            }
            String aia = "";
            int count = 1;
            for (int i = 1; i < aialar.Count; i += 2)
            {
                aia = aia + " [" + (count++) + "]\n" + aialar[i] + "\n";
            }
            return aia;
        }

        public List<String> getLdapAddresses()
        {
            return getAddressesByType(/*AddressType.LDAP*/AddressType.LDAP);
        }

        public List<String> getHttpAddresses()
        {
            return getAddressesByType(/*AddressType.HTTP*/AddressType.HTTP);
        }


        private List<String> getAddressesByType(AddressType aAddressType)
        {
            if (mObject.elements == null)
            {
                return null;
            }
            List<String> adresler = new List<String>();
            foreach (AccessDescription ad in mObject.elements)
            {
                if (ad.accessMethod.Equals(Constants.EXP_ID_AD_CAISSUERS))
                {
                    String adres = UtilName.generalName2String(ad.accessLocation);
                    if (aAddressType == null || adres.StartsWith(aAddressType.asString()))
                    {
                        adresler.Add(adres);
                    }
                }
            }
            return adresler;
        }
        public EExtension toExtension(bool aCritic)
        {
            return new EExtension(EExtensions.oid_pe_authorityInfoAccess, aCritic, getBytes());
        }

        //@Override
        public override String ToString()
        {
            // todo localization
            StringBuilder buffer = new StringBuilder();
            int count = getAccessDescriptionCount();
            buffer.Append("Authority Access Info {\n");
            for (int i = 0; i < count; i++)
            {
                buffer.Append("[").Append(i).Append("] ");
                EAccessDescription accessDescription = getAccessDescription(i);
                buffer.Append(accessDescription.ToString());
            }
            buffer.Append("}\n");
            return buffer.ToString();
        }

    }
}
