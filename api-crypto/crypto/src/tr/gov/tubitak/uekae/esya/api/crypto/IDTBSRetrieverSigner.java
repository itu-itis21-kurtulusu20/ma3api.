package tr.gov.tubitak.uekae.esya.api.crypto;

import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;

public interface IDTBSRetrieverSigner extends BaseSigner {

    byte[] tempSignature = StringUtil.hexToByte("00000000");

    static byte[] getTempSignatureBytes() {
        return tempSignature;
    }

    byte[] getDtbs();
}
