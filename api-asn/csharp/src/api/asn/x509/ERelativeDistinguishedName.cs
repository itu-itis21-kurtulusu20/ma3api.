using System;
using System.Text;
using System.Text.RegularExpressions;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ERelativeDistinguishedName : BaseASNWrapper<RelativeDistinguishedName>
    {
        public ERelativeDistinguishedName(RelativeDistinguishedName aObject)
            : base(aObject)
        {
        }

        public AttributeTypeAndValue[] getAttibutes()
        {
            return mObject.elements;
        }

        public String getCommonNameAttribute()
        {
            foreach (AttributeTypeAndValue av in mObject.elements)
            {
                if (av.type.Equals(Constants.EXP_ID_COMMON_NAME))
                {
                    //return new String(av.value_.mValue).trim();
                    //return Encoding.Default.GetString(av.value_.mValue).Trim();
                    return UtilAtav.atavValue2String(av);
                }
            }
            return null;
        }

        /*
        public String getEmailAttribute()
        {
            foreach (AttributeTypeAndValue av in mObject.elements)
            {
                if (av.type.Equals(Constants.EXP_ID_EMAIL_ADDRESS))
                    return Encoding.ASCII.GetString(av.value_.mValue).Trim();
                //return new String(av.value_.mValue).trim();
            }
            return null;
        }
        public String getSerialNumberAttribute()
        {
            foreach (AttributeTypeAndValue av in mObject.elements)
            {
                if (av.type.Equals(Constants.EXP_ID_AT_SERIAL_NUMBER))
                    return Encoding.ASCII.GetString(av.value_.mValue).Trim();
            }
            return null;
        }*/

        public String getStringAttribute(Asn1ObjectIdentifier oid)
        {
            foreach (AttributeTypeAndValue av in mObject.elements)
            {
                if (av.type.Equals(oid))
                    return Regex.Replace(ASCIIEncoding.ASCII.GetString(av.value_.mValue), "[\x00-\x1f]", "").Trim();
            }
            return null;
        }

    }
}
