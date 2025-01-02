using System;
using tr.gov.tubitak.uekae.esya.api.common.asn1;

namespace tr.gov.tubitak.uekae.esya.api.common.lcns
{
    class PKCS7
    {

        private byte[] content;
        private SignerInfo signerInfo;

        public PKCS7(byte[] pkcs7Bytes) 
        {
            parse(pkcs7Bytes);
        }

        private void parse(byte[] bytes) 
        {
            TLVInfo signedDataTLVInfo = TLVUtil.parseField(bytes, 0, 1);
            TLVInfo encapsulatedContentInfoTLVInfo = TLVUtil.parseField(bytes, signedDataTLVInfo.getDataStartIndex(), 2);
            TLVInfo octetStringContentTLVInfo = TLVUtil.parseFieldInsideTag(bytes, encapsulatedContentInfoTLVInfo.getDataStartIndex(), 1);
            TLVInfo dataTLVInfo = TLVUtil.parseField(bytes, octetStringContentTLVInfo.getDataStartIndex());

            this.content = TLVUtil.getData(bytes, dataTLVInfo);

            TLVInfo firstSignerInfo = TLVUtil.parseField(bytes, signedDataTLVInfo.getDataStartIndex(), 4);// CRLs is NULL
            this.signerInfo = new SignerInfo(bytes, firstSignerInfo);
            this.signerInfo.setPKCS7(this);
        }

        public SignerInfo getFirstSignerInfo()
        {
            return signerInfo;
        }

        public byte[] getContent()
        {
            return content;
        }
    }
}
