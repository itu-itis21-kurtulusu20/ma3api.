using System;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class AddressType
    {
        //public static readonly String LDAP = "ldap";
        //public static readonly String HTTP = "http";
        //public static readonly String DN = "dn";

        public static readonly AddressType LDAP = new AddressType("ldap");
        public static readonly AddressType HTTP = new AddressType("http");
        public static readonly AddressType DN = new AddressType("dn");

        private readonly String mValue;

        AddressType(String aValue)
        {
            mValue = aValue;
        }

        public String asString()
        {
            return mValue;
        }
        public bool Equals(AddressType aAddressType)
        {
            return mValue == aAddressType.asString();
        }
    }
}
