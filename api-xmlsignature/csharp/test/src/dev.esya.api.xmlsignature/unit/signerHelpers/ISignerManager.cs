using System;
using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;


namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.pfxsigner
{
    public interface ISignerManager
    {
        BaseSigner getSigner(int signSlotNo, int certSlotNo);
        ECertificate getSigningCertificate(int slot);
        void logout(int slot);
    }
}
