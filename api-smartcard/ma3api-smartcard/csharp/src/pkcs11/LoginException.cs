using System;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11
{
    public class LoginException : ESYAException
    {
        readonly bool mFinalTry;
        readonly bool mPinLocked;

        public LoginException(String aMsg, Exception aEx, bool aFinalTry, bool aPinLocked)
            : base(aMsg, aEx)
        {
            //super(aMsg, aEx);
            mFinalTry = aFinalTry;
            mPinLocked = aPinLocked;
        }

        public bool isFinalTry()
        {
            return mFinalTry;
        }

        public bool isPinLocked()
        {
            return mPinLocked;
        }

    }
}
