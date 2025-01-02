using System;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EAccessDescription : BaseASNWrapper<AccessDescription>
    {
        public EAccessDescription(AccessDescription aObject)
            : base(aObject)
        {
            //super(aObject);
        }

        public Asn1ObjectIdentifier getAccessMethod()
        {
            return mObject.accessMethod;
        }

        public EGeneralName getAccessLocation()
        {
            return new EGeneralName(mObject.accessLocation);
        }

        //@Override
        public override String ToString()
        {
            StringBuilder buffer = new StringBuilder();
            Asn1ObjectIdentifier method = getAccessMethod();
            String methodStr = "";
            if (Constants.EXP_ID_AD_CAISSUERS.Equals(method))
            {
                methodStr = "CA Issuer ";
            }
            else if (Constants.EXP_ID_AD_OCSP.Equals(method))
            {
                methodStr = "OCSP ";
            }
            buffer.Append("Access Description {\n")
                    .Append("\tmethod : ").Append(methodStr).Append(method).Append("\n")
                    .Append("\taddress : ").Append(getAccessLocation()).Append("\n")
                    .Append("}\n");
            return buffer.ToString();
        }

    }
}
