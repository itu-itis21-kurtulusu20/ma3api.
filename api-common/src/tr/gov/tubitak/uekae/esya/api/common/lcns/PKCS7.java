package tr.gov.tubitak.uekae.esya.api.common.lcns;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.asn1.TLVInfo;
import tr.gov.tubitak.uekae.esya.api.common.asn1.TLVUtil;

import java.util.Arrays;

// Use this class only for license file.
class PKCS7 {

    private byte [] content;
    private SignerInfo signerInfo;

    public PKCS7(byte [] pkcs7Bytes) throws ESYAException {
        parse(pkcs7Bytes);
    }

    private void parse(byte[] bytes) throws ESYAException {

        TLVInfo signedDataTLVInfo = TLVUtil.parseField(bytes, 0, 1);
        TLVInfo encapsulatedContentInfoTLVInfo = TLVUtil.parseField(bytes, signedDataTLVInfo.getDataStartIndex(), 2);
        TLVInfo octetStringContentTLVInfo = TLVUtil.parseFieldInsideTag(bytes, encapsulatedContentInfoTLVInfo.getDataStartIndex(), 1);
        TLVInfo dataTLVInfo = TLVUtil.parseField(bytes, octetStringContentTLVInfo.getDataStartIndex());

        this.content = Arrays.copyOfRange(bytes, dataTLVInfo.getDataStartIndex(), dataTLVInfo.getDataEndIndex());

        TLVInfo firstSignerInfo = TLVUtil.parseField(bytes, signedDataTLVInfo.getDataStartIndex(), 4);// CRLs is NULL
        this.signerInfo = new SignerInfo(bytes, firstSignerInfo);
        this.signerInfo.setPKCS7(this);
    }

    public SignerInfo getFirstSignerInfo(){
        return signerInfo;
    }

    public byte [] getContent(){
        return content;
    }

}
