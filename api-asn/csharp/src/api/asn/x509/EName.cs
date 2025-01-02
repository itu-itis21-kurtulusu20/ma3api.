using System;
using System.Collections.Generic;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EName : BaseASNWrapper<Name>
    {
        public EName(Name aObject)
            : base(aObject)
        {
        }

        public EName(byte[] aNameBytes) : base(aNameBytes, new Name()) { }

        public EName(String nameInBase64)
            : base(nameInBase64, new Name())
        {
        }

        public EName(String aDN, bool aUTF8)
            : base(null)
        {
            Name _internal = null;
            try
            {
                _internal = UtilName.string2Name(aDN, aUTF8);
            }
            catch (Asn1Exception x)
            {
                throw new ESYAException("Invalid name string! " + aDN, x);
            }
            mObject = _internal;
        }

        public EName Clone()
        {
            return new EName(getBytes());
        }
        public String stringValue()
        {
            return LDAPDNUtil.normalize(_bosluklariTemizle(UtilName.name2String(mObject)));
        }

        private String _bosluklariTemizle(String aStr)
        {
            if (aStr == null)
                return null;

            String str = null;
            String[] splittedStr = aStr.Trim().Split(" ".ToCharArray());

            foreach (String part in splittedStr)
            {
                if (!part.Equals(""))
                {
                    if (str == null)
                        str = part.Trim();
                    else
                    {
                        if (part.ElementAt<Char>(0) == ',' || str.EndsWith("="))
                        {
                            str = str + part.Trim();
                        }
                        else
                        {
                            str = str + " " + part.Trim();
                        }
                    }
                }
            }
            return str;
        }
        /**
         * Returns Common Name Attribute of EName
         * @return
         */
        public String getCommonNameAttribute()
        {
            List<ERelativeDistinguishedName> mList = getRDNList();

            foreach (ERelativeDistinguishedName rdn in mList)
            {
                String name = rdn.getCommonNameAttribute();
                if (name != null)
                    return name;
            }

            return null;
        }
        /**
         * Returns Email Attribute of EName
         * @return
         */
        public String getEmailAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_emailAddress);
        }
        /**
           * Returns Serial Number Attribute of EName
           * @return
           */
        public String getSerialNumberAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_serialNumber);
        }
        /**
         * Returns Name Attribute of EName
         * @return
         */
        public String getNameAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_name);
        }
        /**
         * Returns Given Name Attribute of EName
         * @return
         */
        public String getGivenNameAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_givenName);
        }
        /**
         * Returns Surname Attribute of EName
         * @return
         */
        public String getSurnameAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_surname);
        }
        /**
         * Returns Locality Name Attribute of EName
         * @return
         */
        public String getLocalityNameAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_localityName);
        }
        /**
         * Returns State Or Province Name Attribute of EName
         * @return
         */
        public String getStateOrProvinceNameAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_stateOrProvinceName);
        }
        /**
         * Returns Organizational Unit Name Attribute of EName
         * @return
         */
        public String getOrganizationNameAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_organizationName);
        }
        /**
         * Returns Organizational Unit Name Attribute of EName
         * @return
         */
        public String getOrganizationalUnitNameAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_organizationalUnitName);
        }
        /**
 * Returns Title Attribute of EName
 * @return
 */
        public String getTitleAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_title);
        }
        /**
         * Returns Country Name Attribute of EName
         * @return
         */
        public String getCountryNameAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_countryName);
        }
        /**
         * Returns Pseudonym Attribute of EName
         * @return
         */
        public String getPseudonymAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_pseudonym);
        }
        /**
         * Returns Domain Component Attribute of EName
         * @return
         */
        public String getDomainComponentAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_domainComponent);
        }
        /**
         * Returns RDN String Attribute of EName
         * @return
         */
        public String getRDNStringAttribute(Asn1ObjectIdentifier oid)
        {
            List<ERelativeDistinguishedName> mList = getRDNList();
            foreach (ERelativeDistinguishedName rdn in mList)
            {
                String name = rdn.getStringAttribute(oid);
                if (name != null)
                    return name;
            }
            return null;
        }

        public String getOrganizationIdentifierAttribute()
        {
            return getRDNStringAttribute(OID_tipEslestirmeleri.oid_id_at_organizationIdentifier);
        }

        /**
         * Checks whether EName is Utf8String or not.
         * @return True if it is not null and its choiceId is UTF8STRING ,false otherwise
         */
        public bool isSubNameOf(EName aName)
        {
            return isSubNameOf(aName.getObject());
        }

        public bool isSubNameOf(Name iBaseName)
        {
            //RelativeDistinguishedName[] elemsBase = ((RDNSequence)(iBaseName.GetElement())).elements;
            RelativeDistinguishedName[] elemsBase = ((RDNSequence)(mObject.GetElement())).elements;


            if (elemsBase == null || elemsBase.Length < 1) // Empty Name is sub Name of all names!
                return true;

            //RelativeDistinguishedName[] elems = ((RDNSequence)(getObject().GetElement())).elements;
            RelativeDistinguishedName[] elems = iBaseName.GetElement() != null ? ((RDNSequence)(iBaseName.GetElement())).elements : null;
            if (elems == null || elems.Length < 1 || elems.Length > elemsBase.Length)
                return false;

            for (int i = 0; i < elems.Length; i++)
            {
                if (!UtilEsitlikler.esitMi(elemsBase[i], elems[i]))
                    return false;
            }
            return true;
        }
        /**
          * Returns RDN List of EName if its ChoiceID equals to Name._RDNSEQUENCE
          * @return
          */
        public List<ERelativeDistinguishedName> getRDNList()
        {
            if (mObject.ChoiceID == Name._RDNSEQUENCE)
            {
                RDNSequence rdnSequence = (RDNSequence)mObject.GetElement();
                List<ERelativeDistinguishedName> list = new List<ERelativeDistinguishedName>(rdnSequence.elements.Length);
                foreach (RelativeDistinguishedName rdn in rdnSequence.elements)
                {
                    list.Add(new ERelativeDistinguishedName(rdn));
                }
                return list;
            }
            return new List<ERelativeDistinguishedName>(0);
        }

        public void appendRDN(RelativeDistinguishedName aRDN)
        {
            RDNSequence rdnSequence = (RDNSequence)mObject.GetElement();
            rdnSequence.elements = extendArray(rdnSequence.elements, aRDN);
        }


        public static bool isSubNameOf(String iName, String iBaseName)
        {
            return isSubNameOf(iName, iBaseName, ".");
        }

        // static util methods
        public static bool isSubNameOf(EName iName, EName iBaseName)
        {

            RelativeDistinguishedName[] elemsBase = ((RDNSequence)(iBaseName.getObject().GetElement())).elements;

            if (elemsBase == null || elemsBase.Length < 1) // Empty Name is sub Name of all names!
                return true;

            RelativeDistinguishedName[] elems = ((RDNSequence)(iName.getObject().GetElement())).elements;
            if (elems == null || elems.Length < 1 || elems.Length > elemsBase.Length)
                return false;

            for (int i = 0; i < elems.Length; i++)
            {
                if (!UtilEsitlikler.esitMi(elemsBase[i], elems[i]))
                    return false;
            }
            return true;
        }

        public static bool isSubNameOf(String iName, String iBaseName, String iSeperator)
        {
            if (iName.Equals(iBaseName))
                return true;

            if (iBaseName.StartsWith("."))
                return iName.EndsWith(iBaseName);

            int dotPos = iName.IndexOf(iSeperator);

            if (dotPos < 0) return (iName.Equals(iBaseName));

            String tail = iName.Substring(dotPos + 1);

            return (tail.Equals(iBaseName));

        }

        public static bool isSubURINameOf(String iURIName, String iBaseURIName)
        {
            if (iURIName.Equals(iBaseURIName))
                return true;

            if (iBaseURIName.StartsWith("."))
                return (iURIName.IndexOf(iBaseURIName) >= 0);

            int dotPos = iURIName.IndexOf("//");

            if (dotPos < 0) return false;

            String tail = iURIName.Substring(dotPos + 2);

            return (tail.StartsWith(iBaseURIName));
        }
        /**
         * Checks whether EName is Utf8String or not.
         * @return True if it is not null and its choiceId is UTF8STRING ,false otherwise
         */  
        public bool isUtf8String()
        {
            if (mObject == null)
                return false;
            RDNSequence rdnSeq = (RDNSequence)mObject.GetElement();
            if (rdnSeq == null)
                return false;
            RelativeDistinguishedName[] rdns = rdnSeq.elements;
            if (rdns == null | rdns.Length == 0 || rdns[0] == null)
                return false;

            int i, j;
            AttributeTypeAndValue[] atavs;
            AttributeTypeAndValue atav;

            for (i = 0; i < rdns.Length; i++)
            {
                atavs = rdns[i].elements;
                if (atavs == null || atavs.Length == 0)
                    continue;
                for (j = 0; j < atavs.Length; j++)
                {
                    atav = atavs[j];
                    if (atav == null || atav.value_ == null)
                        continue;
                    Asn1OpenType openType = atav.value_;
                    Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                    Asn1DerDecodeBuffer decBuf;
                    DirectoryString dirStr = new DirectoryString();
                    try
                    {
                        openType.Encode(encBuf);
                        decBuf = new Asn1DerDecodeBuffer(encBuf.MsgCopy);
                        dirStr.Decode(decBuf);
                    }
                    catch (Exception e)
                    {
                        continue;
                    }

                    if (dirStr.ChoiceID == DirectoryString._UTF8STRING)
                        return true;

                }
            }

            return false;
        }

        public override bool Equals(Object obj)
        {
            if (obj == null)
                return false;
            if (obj is EName)
            {
                return stringValue().Equals(((EName)obj).stringValue(), StringComparison.OrdinalIgnoreCase);
            }
            return base.Equals(obj);    //To change body of overridden methods use File | Settings | File Templates.
        }

        //compile edildiğinde Equals metodunda warning verildiğinden Equals metoduna override keywor u eklendi, bu yapılınca aşağıdaki GetHashCode metodunun da yazılması gerekti
        public override int GetHashCode()
        {
            return base.GetHashCode();
        }
    }
}
