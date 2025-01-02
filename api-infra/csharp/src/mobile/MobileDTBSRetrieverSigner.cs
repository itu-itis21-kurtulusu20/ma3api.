using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.src;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;

namespace tr.gov.tubitak.uekae.esya.api.infra.src.mobile
{
    public class MobileDTBSRetrieverSigner : MobileSigner, IDTBSRetrieverSigner
    {
        protected byte[] dtbs;
        private static byte[] tempSignature = StringUtil.hexToByte("00000000");

        public MobileDTBSRetrieverSigner(MSSPClientConnector connector, UserIdentifier aUserIden, ECertificate aSigningCert, String informativeText, String aSigningAlg, IAlgorithmParameterSpec aParams)
            : base(connector, aUserIden, aSigningCert, informativeText, aSigningAlg, aParams)
        {

        }

        public byte[] sign(byte[] aData)
        {
            this.dtbs = aData;
            return tempSignature;
        }

        public List<MultiSignResult> sign(List<byte[]> aData, List<String> informativeText)
        {
            // todo: bakılacak
            throw new NotImplementedException();
        }

        public byte[] getDtbs()
        {
            return this.dtbs;
        }
    }
}
