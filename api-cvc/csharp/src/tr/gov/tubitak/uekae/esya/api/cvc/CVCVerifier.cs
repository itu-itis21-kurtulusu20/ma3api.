using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cvc;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace tr.gov.tubitak.uekae.esya.api.cvc
{
    public class CVCVerifier
    {
        public readonly static Asn1Tag CPI_TAG = new Asn1Tag(Asn1Tag.APPL, Asn1Tag.PRIM, 41);
        public readonly static Asn1Tag CAR_TAG = new Asn1Tag(Asn1Tag.APPL, Asn1Tag.PRIM, 2);
        public readonly static Asn1Tag CHR_TAG = new Asn1Tag(Asn1Tag.APPL, Asn1Tag.PRIM, 32);
        public readonly static Asn1Tag CHA_TAG = new Asn1Tag(Asn1Tag.APPL, Asn1Tag.PRIM, 76);
        public readonly static Asn1Tag CXD_TAG = new Asn1Tag(Asn1Tag.APPL, Asn1Tag.PRIM, 36);
        public readonly static Asn1Tag CED_TAG = new Asn1Tag(Asn1Tag.APPL, Asn1Tag.PRIM, 37);
        public readonly static Asn1Tag ALGID_TAG = new Asn1Tag(Asn1Tag.UNIV, Asn1Tag.PRIM, 6);
        public readonly static Asn1Tag MODULUS_TAG = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.PRIM, 1);
        public readonly static Asn1Tag EXPONENT_TAG = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.PRIM, 2);

        /*ERSAPublicKey*/IPublicKey _verifierKey;

        public CVCVerifier(/*ERSAPublicKey*/IPublicKey aPublicKey)
        {
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
        private Pair<byte[], byte[]> calculateM_and_HashM(DigestAlg aDigestAlg, ENonSelfDescCVC aCVC, EHeaderList aHeaderList)
        {

            byte[] signature = aCVC.getSignature();

            byte[] verifiedBytes = null;
            try
            {                
                //verifiedBytes = RSACore.rsa(signature, _verifierKey);
                verifiedBytes = BouncyProviderUtil.rsa(signature, _verifierKey);
            }
            catch (Exception e)
            {
                throw new CVCVerifierException("Error in signature verification!", e);
            }


            //byte[] hashM = Arrays.copyOfRange(verifiedBytes, verifiedBytes.Length - aDigestAlg.getDigestLength() - 1, verifiedBytes.Length - 1);
            byte[] hashM = new byte[aDigestAlg.getDigestLength()];
            Array.Copy(verifiedBytes, verifiedBytes.Length - aDigestAlg.getDigestLength() - 1, hashM, 0, hashM.Length);

            byte[] M = null;
            int headerMessageLen = -1;
            if (aHeaderList != null)
            {
                try
                {
                    headerMessageLen = aHeaderList.getMessageLen();
                }
                catch (Exception e)
                {
                    throw new CVCVerifierException("HeaderList length calculation error!", e);
                }
            }
            if (verifiedBytes[0] == 0x6A)    //PuKRemainder var
            {
                if (aCVC.getPuKRemainder() == null)
                    throw new CVCVerifierException("PuKRemainder is not available");

                //byte[] recoveredMessage = Arrays.copyOfRange(verifiedBytes, 1, verifiedBytes.Length - aDigestAlg.getDigestLength() - 1);
                byte[] recoveredMessage = new byte[verifiedBytes.Length - 2 - aDigestAlg.getDigestLength()];
                Array.Copy(verifiedBytes, 1, recoveredMessage, 0, recoveredMessage.Length);

                int messageLen = aCVC.getPuKRemainder().Length + recoveredMessage.Length;

                if (headerMessageLen != -1 && headerMessageLen != messageLen)
                {
                    throw new CVCVerifierException("HeaderList length(" + headerMessageLen + ")" + " is not compatible with full message (recovered + puKRemainder)(" + messageLen + ")");
                }

                //M = Arrays.copyOf(recoveredMessage, messageLen);
                M = new byte[messageLen];
                Array.Copy(recoveredMessage, 0, M, 0, recoveredMessage.Length);
                Array.Copy(aCVC.getPuKRemainder(), 0, M, recoveredMessage.Length, aCVC.getPuKRemainder().Length);

            }
            else if (verifiedBytes[0] == 0x4B)  //PuKRemainder yok!
            {
                //M = Arrays.copyOfRange()
                //byte[] paddedM = Arrays.copyOfRange(verifiedBytes, 1, verifiedBytes.Length - aDigestAlg.getDigestLength() - 1);
                byte[] paddedM = new byte[verifiedBytes.Length - 2 - aDigestAlg.getDigestLength()];
                Array.Copy(verifiedBytes, 1, paddedM, 0, paddedM.Length);
                int paddingNumber = findPaddingNumber(paddedM);
                //M = Arrays.copyOfRange(paddedM, paddingNumber, paddedM.Length);
                M = new byte[paddedM.Length - paddingNumber];
            }
            else if (verifiedBytes[0] == 0x4A)
            {
                //M = Arrays.copyOfRange(verifiedBytes, 1, verifiedBytes.Length - aDigestAlg.getDigestLength() - 1);
                M = new byte[verifiedBytes.Length - 2 - aDigestAlg.getDigestLength()];
                Array.Copy(verifiedBytes, 1, M, 0, M.Length);
                if (headerMessageLen != -1 && headerMessageLen != M.Length)
                {
                    throw new CVCVerifierException("HeaderList length(" + headerMessageLen + ")" + " is not compatible with full message (recovered + puKRemainder)(" + M.Length + ")");
                }
            }
            else
            {
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
        public bool verify(DigestAlg aDigestAlg, ENonSelfDescCVCwithHeader cvcToVerify)
        {
            EHeaderList headerList = null;
            try
            {
                headerList = cvcToVerify.getHeaderList();
            }
            catch (ESYAException e)
            {
                throw new CVCVerifierException("Invalid Header List:" + e.Message, e);
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
        public bool verify(DigestAlg aDigestAlg, ENonSelfDescCVC aCVC, EHeaderList aHeaderList)
        {

            try
            {
                Pair<byte[], byte[]> M_and_HashM = calculateM_and_HashM(aDigestAlg, aCVC, aHeaderList);
                byte[] calculatedHash = DigestUtil.digest(aDigestAlg, M_and_HashM.first());

                if (calculatedHash.SequenceEqual(M_and_HashM.second()))
                {
                    //System.out.println("Dogrulama Tamam!");
                    return true;
                }

            }
            catch (CryptoException ex)
            {
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
        public CVCFields calculateCVCFields(DigestAlg aDigestAlg, ENonSelfDescCVCwithHeader cvcToVerify)
        {
            EHeaderList headerList = null;
            try
            {
                headerList = cvcToVerify.getHeaderList();
            }
            catch (ESYAException e)
            {
                throw new CVCVerifierException("Invalid Header List:" + e.Message, e);
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
        public CVCFields calculateCVCFields(DigestAlg aDigestAlg, ENonSelfDescCVC aCVC, EHeaderList aHeaderList)
        {
            Pair<byte[], byte[]> M_and_HashM = null;
            CVCFields cvcFields = null;
            try
            {
                M_and_HashM = calculateM_and_HashM(aDigestAlg, aCVC, aHeaderList);
                byte[] calculatedHash = DigestUtil.digest(aDigestAlg, M_and_HashM.first());

                if (!calculatedHash.SequenceEqual(M_and_HashM.second()))
                {
                    //System.out.println("Dogrulama Hatali!");
                    throw new CVCVerifierException("CVC Verification error!");
                }
            }
            catch (CryptoException ex)
            {
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
        public EAlgId extractOID(ENonSelfDescCVCwithHeader rootCVC)
        {
            EHeaderList headerList = null;
            try
            {
                headerList = rootCVC.getHeaderList();
            }
            catch (ESYAException e)
            {
                throw new CVCVerifierException("Invalid Header List:" + e.Message, e);
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
        public EAlgId extractOID(ENonSelfDescCVC aCVC, EHeaderList aHeaderList)
        {
            byte[] signature = aCVC.getSignature();

            byte[] verifiedBytes = null;
            try
            {
                //verifiedBytes = RSACore.rsa(signature, _verifierKey);
                verifiedBytes = BouncyProviderUtil.rsa(signature, _verifierKey);
            }
            catch (Exception e)
            {
                throw new CVCVerifierException("Error in signature verification!", e);
            }

            //byte[] hashM = Arrays.copyOfRange(verifiedBytes, verifiedBytes.length - aDigestAlg.getDigestLength() - 1, verifiedBytes.length - 1);
            byte[] cvcFields = null;
            int headerMessageLen = -1;
            if (aHeaderList != null)
            {
                try
                {
                    headerMessageLen = aHeaderList.getMessageLen();
                }
                catch (Exception e)
                {
                    throw new CVCVerifierException("HeaderList length calculation error!", e);
                }
            }
            switch (verifiedBytes[0])
            {
                case 0x6A:
                case 0x4A:
                    //cvcFields = Arrays.copyOfRange(verifiedBytes, 1, verifiedBytes.Length);
                    cvcFields = new byte[verifiedBytes.Length - 1];
                    Array.Copy(verifiedBytes, 1, cvcFields, 0, cvcFields.Length);
                    break;
                case 0x4B:
                    int paddingNumber = findPaddingNumber(verifiedBytes);
                    //cvcFields = Arrays.copyOfRange(verifiedBytes, paddingNumber, verifiedBytes.Length);
                    cvcFields = new byte[verifiedBytes.Length - paddingNumber];
                    Array.Copy(verifiedBytes, paddingNumber, cvcFields, 0, cvcFields.Length);
                    break;
                default:
                    throw new CVCVerifierException("Verified signature format is not compatible with ISO9796-2 SCHEME 1");
            }

            try
            {
                Dictionary<Asn1Tag, int> listInfo = aHeaderList.getListInfo();
                Dictionary<Asn1Tag, int>.KeyCollection cvcFieldsSet = listInfo.Keys;
                int index = 0;
                foreach (Asn1Tag tag in cvcFieldsSet)
                {
                    int len = listInfo[tag];
                    if (tag.Equals(ALGID_TAG))
                    {
                        byte[] field = new byte[len];
                        Array.Copy(cvcFields, index, field, 0, len);
                        return EAlgId.fromValue(field);
                    }
                    index += len;
                }
            }
            catch (Exception e)
            {
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
        private CVCFields extractCVCFields(byte[] M, EHeaderList aHeaderList)
        {
            CVCFields fields = new CVCFields();
            try
            {
                Dictionary<Asn1Tag, int> listInfo = aHeaderList.getListInfo();
                Dictionary<Asn1Tag, int>.KeyCollection cvcFields = listInfo.Keys;
                int index = 0;
                foreach (Asn1Tag tag in cvcFields)
                {
                    int len = listInfo[tag];
                    byte[] field = new byte[len];
                    Array.Copy(M, index, field, 0, len);
                    if (tag.Equals(CPI_TAG))
                    {
                        fields.set_cpi(new ECpi(field[0]));
                    }
                    else if (tag.Equals(CAR_TAG))
                    {
                        fields.set_car(ECar.fromValue(field));
                    }
                    else if (tag.Equals(CHR_TAG))
                    {
                        fields.set_chr(EChr.fromValue(field));
                    }
                    else if (tag.Equals(CHA_TAG))
                    {
                        fields.set_cha(ECha.fromValue(field));
                    }
                    else if (tag.Equals(CXD_TAG))
                    {
                        fields.set_cxd(ECxd.fromValue(field));
                    }
                    else if (tag.Equals(CED_TAG))
                    {
                        fields.set_ced(ECed.fromValue(field));
                    }
                    else if (tag.Equals(ALGID_TAG))
                    {
                        fields.set_oid(EAlgId.fromValue(field));
                    }
                    else if (tag.Equals(MODULUS_TAG))
                    {
                        fields.setModulus(field);
                    }
                    else if (tag.Equals(EXPONENT_TAG))
                    {
                        fields.setExponent(field);
                    }
                    else
                    {
                        throw new CVCVerifierException("Unknown CVC Tag: " + tag.ToString());
                    }
                    index += len;
                }
            }
            catch (Exception e)
            {
                throw new CVCVerifierException("Error in field extraction from M", e);
            }
            return fields;
        }

        /**
         Calculate number of padding bytes

         @param aPaddedArray padded byte array
         @return
         */
        private int findPaddingNumber(byte[] aPaddedArray)
        {
            //
            // find out how much padding we've got
            //
            int mStart = 0;

            for (mStart = 0; mStart != aPaddedArray.Length; mStart++)
            {
                if (((aPaddedArray[mStart] & 0x0f) ^ 0x0a) == 0)
                {
                    break;
                }
            }

            mStart++;
            return mStart;
        }
    }
}
