using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace smartcard.src.tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder
{
    class ModulusFinderFromObjectId : KeyFinder
    {
        readonly SmartCard mSmartCard;
        readonly long mSessionId;
        readonly long objID;

        public ModulusFinderFromObjectId(SmartCard aSC, long aSessionId, long aObjID)
        {
            mSmartCard = aSC;
            mSessionId = aSessionId;
            objID = aObjID;
        }
        public int getKeyLenght()
        {
            return mSmartCard.getModulusOfKey(mSessionId, objID).Length* 8;
        }
    }
}
