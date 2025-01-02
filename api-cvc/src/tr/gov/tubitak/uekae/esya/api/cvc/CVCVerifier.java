package tr.gov.tubitak.uekae.esya.api.cvc;

import com.objsys.asn1j.runtime.Asn1Tag;
import sun.security.rsa.RSACore;
import tr.gov.tubitak.uekae.esya.api.asn.cvc.*;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;

import javax.crypto.BadPaddingException;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 Created by IntelliJ IDEA.
 User: bilen.ogretmen
 Date: 6/14/11
 Time: 4:03 PM
 */
public class CVCVerifier {

    public final static Asn1Tag CPI_TAG = new Asn1Tag(Asn1Tag.APPL, Asn1Tag.PRIM, 41);
    public final static Asn1Tag CAR_TAG = new Asn1Tag(Asn1Tag.APPL, Asn1Tag.PRIM, 2);
    public final static Asn1Tag CHR_TAG = new Asn1Tag(Asn1Tag.APPL, Asn1Tag.PRIM, 32);
    public final static Asn1Tag CHA_TAG = new Asn1Tag(Asn1Tag.APPL, Asn1Tag.PRIM, 76);
    public final static Asn1Tag CXD_TAG = new Asn1Tag(Asn1Tag.APPL, Asn1Tag.PRIM, 36);
    public final static Asn1Tag CED_TAG = new Asn1Tag(Asn1Tag.APPL, Asn1Tag.PRIM, 37);
    public final static Asn1Tag ALGID_TAG = new Asn1Tag(Asn1Tag.UNIV, Asn1Tag.PRIM, 6);
    public final static Asn1Tag MODULUS_TAG = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.PRIM, 1);
    public final static Asn1Tag EXPONENT_TAG = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.PRIM, 2);

    RSAPublicKey _verifierKey;

    public CVCVerifier(RSAPublicKey aPublicKey) {
        _verifierKey = aPublicKey;
    }

    /**
     Calculate M (recoveredMessage + PukRemainder) and extract hashM from verified signature

     @param aDigestAlg
     @param aCVC
     @param aHeaderList
     @return
     @throws CVCVerifierException
     */
    private Pair<byte[], byte[]> calculateM_and_HashM(DigestAlg aDigestAlg, ENonSelfDescCVC aCVC, EHeaderList aHeaderList) throws CVCVerifierException {

        byte[] signature = aCVC.getSignature();

        byte[] verifiedBytes = null;
        try {
            verifiedBytes = RSACore.rsa(signature, _verifierKey);
        } catch (BadPaddingException e) {
            throw new CVCVerifierException("Error in signature verification!", e);
        }

        byte[] hashM = Arrays.copyOfRange(verifiedBytes, verifiedBytes.length - aDigestAlg.getDigestLength() - 1, verifiedBytes.length - 1);
        byte[] M = null;
        int headerMessageLen = -1;
        if (aHeaderList != null) {
            try {
                headerMessageLen = aHeaderList.getMessageLen();
            } catch (Exception e) {
                throw new CVCVerifierException("HeaderList length calculation error!", e);
            }
        }
        if (verifiedBytes[0] == 0x6A)    //PuKRemainder var
        {
            if (aCVC.getPuKRemainder() == null)
                throw new CVCVerifierException("PuKRemainder is not available");

            byte[] recoveredMessage = Arrays.copyOfRange(verifiedBytes, 1, verifiedBytes.length - aDigestAlg.getDigestLength() - 1);

            int messageLen = aCVC.getPuKRemainder().length + recoveredMessage.length;

            if (headerMessageLen != -1 && headerMessageLen != messageLen) {
                throw new CVCVerifierException("HeaderList length(" + headerMessageLen + ")" + " is not compatible with full message (recovered + puKRemainder)(" + messageLen + ")");
            }

            M = Arrays.copyOf(recoveredMessage, messageLen);
            System.arraycopy(aCVC.getPuKRemainder(), 0, M, recoveredMessage.length, aCVC.getPuKRemainder().length);

        } else if (verifiedBytes[0] == 0x4B) {  //PuKRemainder yok!
            //M = Arrays.copyOfRange()
            byte[] paddedM = Arrays.copyOfRange(verifiedBytes, 1, verifiedBytes.length - aDigestAlg.getDigestLength() - 1);
            int paddingNumber = findPaddingNumber(paddedM);
            M = Arrays.copyOfRange(paddedM, paddingNumber, paddedM.length);


        } else if (verifiedBytes[0] == 0x4A) {
            M = Arrays.copyOfRange(verifiedBytes, 1, verifiedBytes.length - aDigestAlg.getDigestLength() - 1);
            if (headerMessageLen != -1 && headerMessageLen != M.length) {
                throw new CVCVerifierException("HeaderList length(" + headerMessageLen + ")" + " is not compatible with full message (recovered + puKRemainder)(" + M.length + ")");
            }
        } else {
            throw new CVCVerifierException("Verified signature format is not compatible with ISO9796-2 SCHEME 1");
        }

        return new Pair<byte[], byte[]>(M, hashM);


    }

    /**
     Verify CVC signature

     @param aDigestAlg  Digest for M calculation
     @param cvcToVerify CVC
     @return
     @throws CVCVerifierException
     */
    public boolean verify(DigestAlg aDigestAlg, ENonSelfDescCVCwithHeader cvcToVerify) throws CVCVerifierException {
        EHeaderList headerList = null;
        try {
            headerList = cvcToVerify.getHeaderList();
        } catch (ESYAException e) {
            throw new CVCVerifierException("Invalid Header List:" + e.getMessage(), e);
        }
        return verify(aDigestAlg, cvcToVerify.getNonSelfDescCVC(), headerList);
    }

    /**
     Verify CVC signature

     @param aDigestAlg  Digest for M calculation
     @param aCVC        CVC
     @param aHeaderList HeaderList
     @return
     @throws CVCVerifierException
     */
    public boolean verify(DigestAlg aDigestAlg, ENonSelfDescCVC aCVC, EHeaderList aHeaderList) throws CVCVerifierException {

        try {
            Pair<byte[], byte[]> M_and_HashM = calculateM_and_HashM(aDigestAlg, aCVC, aHeaderList);
            byte[] calculatedHash = DigestUtil.digest(aDigestAlg, M_and_HashM.first());

            if (Arrays.equals(calculatedHash, M_and_HashM.second())) {
                //System.out.println("Dogrulama Tamam!");
                return true;
            }

        } catch (CryptoException ex) {
            throw new CVCVerifierException("Error in digest calculation!", ex);
        }
        return false;
    }

    /**
     Verify CVC and extract CVCFields

     @param aDigestAlg
     @param cvcToVerify
     @return
     @throws CVCVerifierException
     */
    public CVCFields calculateCVCFields(DigestAlg aDigestAlg, ENonSelfDescCVCwithHeader cvcToVerify) throws CVCVerifierException {
        EHeaderList headerList = null;
        try {
            headerList = cvcToVerify.getHeaderList();
        } catch (ESYAException e) {
            throw new CVCVerifierException("Invalid Header List:" + e.getMessage(), e);
        }
        return calculateCVCFields(aDigestAlg, cvcToVerify.getNonSelfDescCVC(), headerList);
    }

    /**
     Verify CVC and extract CVCFields

     @param aDigestAlg
     @param aCVC
     @param aHeaderList
     @return
     @throws CVCVerifierException
     */
    public CVCFields calculateCVCFields(DigestAlg aDigestAlg, ENonSelfDescCVC aCVC, EHeaderList aHeaderList) throws CVCVerifierException {
        Pair<byte[], byte[]> M_and_HashM = null;
        CVCFields cvcFields = null;
        try {
            M_and_HashM = calculateM_and_HashM(aDigestAlg, aCVC, aHeaderList);
            byte[] calculatedHash = DigestUtil.digest(aDigestAlg, M_and_HashM.first());

            if (!Arrays.equals(calculatedHash, M_and_HashM.second())) {
                //System.out.println("Dogrulama Hatali!");
                throw new CVCVerifierException("CVC Verification error!");
            }
        } catch (CryptoException ex) {
            throw new CVCVerifierException("Error in digest calculation!", ex);
        }
        cvcFields = extractCVCFields(M_and_HashM.first(), aHeaderList);


        return cvcFields;

    }

    /**
     Extract OID from CVC

     @param rootCVC
     @return
     @throws CVCVerifierException
     */
    public EAlgId extractOID(ENonSelfDescCVCwithHeader rootCVC) throws CVCVerifierException {
        EHeaderList headerList = null;
        try {
            headerList = rootCVC.getHeaderList();
        } catch (ESYAException e) {
            throw new CVCVerifierException("Invalid Header List:" + e.getMessage(), e);
        }
        return extractOID(rootCVC.getNonSelfDescCVC(), headerList);
    }

    /**
     Extract OID from CVC

     @param aCVC
     @param aHeaderList
     @return
     @throws CVCVerifierException
     */
    public EAlgId extractOID(ENonSelfDescCVC aCVC, EHeaderList aHeaderList) throws CVCVerifierException {
        byte[] signature = aCVC.getSignature();

        byte[] verifiedBytes = null;
        try {
            verifiedBytes = RSACore.rsa(signature, _verifierKey);
        } catch (BadPaddingException e) {
            throw new CVCVerifierException("Error in signature verification!", e);
        }

        //byte[] hashM = Arrays.copyOfRange(verifiedBytes, verifiedBytes.length - aDigestAlg.getDigestLength() - 1, verifiedBytes.length - 1);
        byte[] cvcFields = null;
        int headerMessageLen = -1;
        if (aHeaderList != null) {
            try {
                headerMessageLen = aHeaderList.getMessageLen();
            } catch (Exception e) {
                throw new CVCVerifierException("HeaderList length calculation error!", e);
            }
        }
        switch (verifiedBytes[0]) {
            case 0x6A:
            case 0x4A:
                cvcFields = Arrays.copyOfRange(verifiedBytes, 1, verifiedBytes.length);
                break;
            case 0x4B:
                int paddingNumber = findPaddingNumber(verifiedBytes);
                cvcFields = Arrays.copyOfRange(verifiedBytes, paddingNumber, verifiedBytes.length);
                break;
            default:
                throw new CVCVerifierException("Verified signature format is not compatible with ISO9796-2 SCHEME 1");
        }

        try {
            LinkedHashMap<Asn1Tag, Integer> listInfo = aHeaderList.getListInfo();
            Set<Asn1Tag> cvcFieldsSet = listInfo.keySet();
            int index = 0;
            for (Asn1Tag tag : cvcFieldsSet) {
                int len = listInfo.get(tag);
                if (tag.equals(ALGID_TAG)) {
                    byte[] field = new byte[len];
                    System.arraycopy(cvcFields, index, field, 0, len);
                    return EAlgId.fromValue(field);
                }
                index += len;
            }
        } catch (Exception e) {
            throw new CVCVerifierException("Error in field extraction from M", e);
        }
        return null;
    }

    /**
     Extract CVC fields from M using HeaderList

     @param M
     @param aHeaderList
     @return
     @throws CVCVerifierException
     */
    private CVCFields extractCVCFields(byte[] M, EHeaderList aHeaderList) throws CVCVerifierException {
        CVCFields fields = new CVCFields();
        try {
            LinkedHashMap<Asn1Tag, Integer> listInfo = aHeaderList.getListInfo();
            Set<Asn1Tag> cvcFields = listInfo.keySet();
            int index = 0;
            for (Asn1Tag tag : cvcFields) {
                int len = listInfo.get(tag);
                byte[] field = new byte[len];
                System.arraycopy(M, index, field, 0, len);
                if (tag.equals(CPI_TAG)) {
                    fields.set_cpi(new ECpi(field[0]));
                } else if (tag.equals(CAR_TAG)) {
                    fields.set_car(ECar.fromValue(field));
                } else if (tag.equals(CHR_TAG)) {
                    fields.set_chr(EChr.fromValue(field));
                } else if (tag.equals(CHA_TAG)) {
                    fields.set_cha(ECha.fromValue(field));
                } else if (tag.equals(CXD_TAG)) {
                    fields.set_cxd(ECxd.fromValue(field));
                } else if (tag.equals(CED_TAG)) {
                    fields.set_ced(ECed.fromValue(field));
                } else if (tag.equals(ALGID_TAG)) {
                    fields.set_oid(EAlgId.fromValue(field));
                } else if (tag.equals(MODULUS_TAG)) {
                    fields.setModulus(field);
                } else if (tag.equals(EXPONENT_TAG)) {
                    fields.setExponent(field);
                } else {
                    throw new CVCVerifierException("Unknown CVC Tag: " + tag.toString());
                }
                index += len;
            }
        } catch (Exception e) {
            throw new CVCVerifierException("Error in field extraction from M", e);
        }
        return fields;
    }

    /**
     Calculate number of padding bytes

     @param aPaddedArray padded byte array
     @return
     */
    private int findPaddingNumber(byte[] aPaddedArray) {
        //
        // find out how much padding we've got
        //
        int mStart = 0;

        for (mStart = 0; mStart != aPaddedArray.length; mStart++) {
            if (((aPaddedArray[mStart] & 0x0f) ^ 0x0a) == 0) {
                break;
            }
        }

        mStart++;
        return mStart;
    }

}
