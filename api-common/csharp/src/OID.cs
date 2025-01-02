using System;
using tr.gov.tubitak.uekae.esya.api.common.util;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.common
{
    public class OID
    {
        private readonly int[] _mValue;
        private readonly string _mStringValue;

        public OID(int[] aValue)
        {
            _mValue = aValue;
            _mStringValue = OIDUtil.concat(_mValue);
        }

        public int[] getValue()
        {
            return _mValue;
        }
        public static OID parse(String str)
        {
            return new OID(OIDUtil.parse(str));
        }

        public static OID fromURN(String str)
        {
            return new OID(OIDUtil.fromURN(str));
        }

        //@Override
        public override String ToString()
        {
            return _mStringValue;
        }

        //@Override
        public override int GetHashCode()
        {
            return _mStringValue.GetHashCode();
        }

        //@Override
        public override bool Equals(Object obj)
        {
            var oid = obj as OID;
            return oid != null && _mStringValue.Equals(oid.ToString());
        }
    }
}
