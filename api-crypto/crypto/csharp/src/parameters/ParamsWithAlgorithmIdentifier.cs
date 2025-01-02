using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.crypto.parameters
{
    public class ParamsWithAlgorithmIdentifier : IAlgorithmParams
    {
        private readonly EAlgorithmIdentifier _mAlgIden;

        public ParamsWithAlgorithmIdentifier(AlgorithmIdentifier aAlgIden)
        {
            this._mAlgIden = new EAlgorithmIdentifier(aAlgIden);
        }

        public ParamsWithAlgorithmIdentifier(EAlgorithmIdentifier aAlgIden)
        {
            this._mAlgIden = aAlgIden;
        }

        public EAlgorithmIdentifier getAlgorithmIdentifier()
        {
            return _mAlgIden;
        }

        public byte[] getEncoded()
        {
            return _mAlgIden.getEncoded();
        }
    }
}
