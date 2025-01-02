using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ECRLDistributionPoints : BaseASNWrapper<CRLDistributionPoints>, ExtensionType
    {
        private readonly ECertificate mCertificate;

        public ECRLDistributionPoints(CRLDistributionPoints aObject, ECertificate aCertificate)
            : base(aObject)
        {
            mCertificate = aCertificate;
        }

        public ECRLDistributionPoints(String[] cdps)
            : base(null)
        {
            //super(null);
            DistributionPoint[] elem = new DistributionPoint[cdps.Length];
            DistributionPointName temp;
            for (int i = 0; i < cdps.Length; i++)
            {
                GeneralName[] genName = new GeneralName[1];
                genName[0] = new GeneralName();
                genName[0].Set_uniformResourceIdentifier(new Asn1IA5String(cdps[i]));

                GeneralNames genNames = new GeneralNames(genName);

                temp = new DistributionPointName();
                temp.Set_fullName(genNames);

                elem[i] = new DistributionPoint();
                elem[i].distributionPoint = temp;
            }

            mObject = new CRLDistributionPoints(elem);
        }

        public ECRLDistributionPoints(byte[] bytes) : base(bytes, new CRLDistributionPoints())
        {
        }

        public int getCRLDistributionPointCount()
        {
            return mObject.getLength();
        }

        public ECRLDistributionPoint getCRLDistributionPoint(int aIndex)
        {
            return new ECRLDistributionPoint(mObject.elements[aIndex], mCertificate);
        }

        public ECRLDistributionPoint getCRLDistributionPointWithoutResolve(int aIndex)
        {
            return new ECRLDistributionPoint(mObject.elements[aIndex]);
        }

        public List<String> getHttpAddresses()
        {
            return getAddressesByType(AddressType.HTTP);
        }

        public List<String> getLdapAddresses()
        {
            return getAddressesByType(AddressType.LDAP);
        }

        public List<String> getDnAddresses()
        {
            return getAddressesByType(AddressType.DN);
        }

        private List<String> getAddressesByType(AddressType aAddressType)
        {
            List<String> addresses = new List<String>();
            for (int i = 0; i < getCRLDistributionPointCount(); i++)
            {
                ECRLDistributionPoint cdp = getCRLDistributionPoint(i);
                if (cdp.getAddressType().Equals(aAddressType))
                {
                    addresses.Add(cdp.getAddress());
                }
            }
            return addresses;
        }

        public List<EName> getHttpIssuers()
        {
            return getIssuersByType(AddressType.HTTP);
        }

        public List<EName> getLdapIssuers()
        {
            return getIssuersByType(AddressType.LDAP);
        }

        public List<EName> getDnIssuers()
        {
            return getIssuersByType(AddressType.DN);
        }

        private List<EName> getIssuersByType(AddressType aAddressType)
        {
            List<EName> issuers = new List<EName>();
            for (int i = 0; i < getCRLDistributionPointCount(); i++)
            {
                ECRLDistributionPoint cdp = getCRLDistributionPoint(i);
                if (cdp.getAddressType().Equals(aAddressType))
                {
                    issuers.Add(cdp.getCRLIssuer());
                }
            }
            return issuers;
        }

        public EExtension toExtension(bool aCritic)
        {
            //return new EExtension(EExtensions.oid_ce_extKeyUsage, aCritic, getBytes());
            return new EExtension(EExtensions.oid_ce_cRLDistributionPoints, aCritic, getBytes());
        }

        public override String ToString()
        {
            if ((mObject.elements == null) || (mObject.elements.Length == 0))
            {
                return "";
            }
            String str = "";
            int count = 1;
            for (int i = 0; i < getCRLDistributionPointCount(); i++)
            {
                ECRLDistributionPoint cdp = getCRLDistributionPoint(i);
                str += " [" + (count++) + "]\n" + cdp.getAddress() + "\n";
            }
            return str;
        }
    }
}