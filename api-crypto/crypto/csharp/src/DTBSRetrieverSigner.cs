using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.src;

namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public class DTBSRetrieverSigner : IDTBSRetrieverSigner
    {
        private static byte[] tempSignature = StringUtil.hexToByte("00000000");

        protected SignatureAlg signatureAlg;
        protected IAlgorithmParameterSpec algorithmParams;
        protected byte[] dtbs;

        public DTBSRetrieverSigner(SignatureAlg signatureAlg, IAlgorithmParameterSpec algorithmParams)
        {
            this.signatureAlg = signatureAlg;
            this.algorithmParams = algorithmParams;
        }

        public IAlgorithmParameterSpec getAlgorithmParameterSpec()
        {
            return algorithmParams;
        }

        public string getSignatureAlgorithmStr()
        {
            return this.signatureAlg.getName();
        }

        public byte[] sign(byte[] aData)
        {
            this.dtbs = aData;
            return tempSignature;
        }

        public byte[] getDtbs()
        {
            return this.dtbs;
        }

        public static byte[] getTempSignatureBytes()
        {
            return tempSignature;
        }
    }
}
