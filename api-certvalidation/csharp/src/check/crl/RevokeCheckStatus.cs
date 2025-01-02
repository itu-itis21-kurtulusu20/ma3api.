using System;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl
{
    [Serializable]
    public class RevokeCheckStatus : CheckStatus
    {
        public static readonly RevokeCheckStatus CANT_CHECK = new RevokeCheckStatus(_enum.CANT_CHECK);
        public static readonly RevokeCheckStatus REVOKED = new RevokeCheckStatus(_enum.REVOKED);
        public static readonly RevokeCheckStatus NOT_REVOKED = new RevokeCheckStatus(_enum.NOT_REVOKED);
        
        
        enum _enum
        {
            CANT_CHECK,
            REVOKED,
            NOT_REVOKED
        }        
        
        readonly _enum mValue;
        
        RevokeCheckStatus(_enum aEnum)
        {
            mValue = aEnum;
        }

        public String getText()
        {
            return null;  //Todo
        }
          
    }
}
