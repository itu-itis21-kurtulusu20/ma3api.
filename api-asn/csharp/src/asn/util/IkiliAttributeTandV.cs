using System;
using Com.Objsys.Asn1.Runtime;

namespace tr.gov.tubitak.uekae.esya.asn.util
{
    public class IkiliAttributeTandV
    {
        public String mst;
        public Asn1Type mType;
        public Asn1ObjectIdentifier mOid;

        internal IkiliAttributeTandV(String ast, Asn1Type aType, Asn1ObjectIdentifier aOid)
        {
            this.mst = ast;
            this.mType = aType;
            this.mOid = aOid;
        }

        internal String stringValue()
        {
            if (mType == null)
                return "";
            if (mType is Asn1Choice)
                return ((Asn1Choice)mType).GetElement().ToString();
            return mType.ToString();
        }
    }
}
