using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class CRLType
    {
        public static readonly CRLType BASE = new CRLType(_enum.BASE);
        public static readonly CRLType DELTA = new CRLType(_enum.DELTA);
        enum _enum
        {
            BASE = 1,
	        DELTA = 2
        }
        private readonly _enum mValue;
        private CRLType(_enum aEnum)
        {
            mValue = aEnum;
        }
        public int getIntValue()
        {
            return (int)mValue;
        }

        //TODO null donmesi durumu
        public static CRLType getNesne(int aTip)
        {
            switch (aTip)
            {
                case 1: return CRLType.BASE;
                case 2: return CRLType.DELTA;
                default: return null;
            }

        }
        public static CRLType getNesne(long aTip)
        {
            return getNesne((int)aTip);
        }
    }
}
