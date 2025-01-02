using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy

{
    public abstract class Decryptor: Cipher
    {
        public Decryptor(CipherAlg aCipherAlg) : base(aCipherAlg) { }
        public override bool isEncryptor()
        {
            return false;
        }
        //public abstract int getBlockSize();
    }
}
