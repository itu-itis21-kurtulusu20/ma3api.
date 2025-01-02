using System;
using System.Collections.Generic;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cvc;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.cvc.oids;

namespace tr.gov.tubitak.uekae.esya.api.cvc
{
    public class CVCChainVerifier
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private ICVCChainInfoProvider chainInfoProvider;
        public bool verified = false;

        public CVCChainVerifier(ICVCChainInfoProvider chainInfoProvider)
        {
            this.chainInfoProvider = chainInfoProvider;
        }

        public CVCChainVerificationResult verify(ENonSelfDescCVCwithHeader cvcToVerify)
        {

            List<Pair<ENonSelfDescCVCwithHeader, CVCFields>> verifiedCvcAuthorities = new List<Pair<ENonSelfDescCVCwithHeader, CVCFields>>();

            IPublicKey cvcAuthPublicKey = getRootCVCAuthPublicKey();


            ENonSelfDescCVCwithHeader rootCVC = chainInfoProvider.getRootCVCAuthority();

            CVCFields rootCvcAuthFields = verifyRootCVCAuthority(cvcAuthPublicKey, rootCVC);


            verifiedCvcAuthorities.Add(new Pair<ENonSelfDescCVCwithHeader, CVCFields>(rootCVC, rootCvcAuthFields));

            DigestAlg digestToSign = getDigestAlg(rootCvcAuthFields.get_oid());


            String expectedCAR = getExpectedCARForAuthority(rootCvcAuthFields);

            carChrVerify(expectedCAR, rootCvcAuthFields);


            //todo  clarify and clean code pls...

            for (int i = chainInfoProvider.getSubCvcAuthorities().Count - 1; i >= 0; i--)
            {

                ENonSelfDescCVCwithHeader intermediateCVCAuth = chainInfoProvider.getSubCvcAuthorities()[i];

                CVCFields intermediateCVCAuthFields = verifyCVC(cvcAuthPublicKey, digestToSign, intermediateCVCAuth);

                carChrVerify(expectedCAR, intermediateCVCAuthFields);

                expectedCAR = getExpectedCARForAuthority(intermediateCVCAuthFields);

                if (!CVCRoleIDs.isSubAuth(intermediateCVCAuthFields.get_cha()))
                    throw new CVCVerifierException(StringUtil.ToString(intermediateCVCAuthFields.get_chr().getByteValues()) + " is not Sub CVC Authority");

                cvcAuthPublicKey = getRSAPublicKey(intermediateCVCAuthFields);

                digestToSign = getDigestAlg(intermediateCVCAuthFields.get_oid());

                verifiedCvcAuthorities.Add(new Pair<ENonSelfDescCVCwithHeader, CVCFields>(intermediateCVCAuth, intermediateCVCAuthFields));

                logger.Info("CVC Chain is Verified At lvl " + i + " for CVC Authority:" + StringUtil.ToString(intermediateCVCAuthFields.get_chr().getByteValues()));
            }

            CVCFields cvcFields = verifyCVC(cvcAuthPublicKey, digestToSign, cvcToVerify);

            carChrVerify(expectedCAR, cvcFields);

            // currently sophisticated...
            if (!CVCRoleIDs.isDevice(cvcFields.get_cha()))
                throw new CVCVerifierException(StringUtil.ToString(cvcFields.get_chr().getByteValues()) + " is not Device CVC");

            logger.Info("CVC Chain is Verified For CVC:" + StringUtil.ToString(cvcFields.get_chr().getByteValues()));

            return new CVCChainVerificationResult(cvcFields, verifiedCvcAuthorities);

        }

        private void carChrVerify(String expectedCAR, CVCFields cvcFields)
        {
            String carFound = StringUtil.ToString(cvcFields.get_car().getByteValues());
            if (!expectedCAR.Equals(carFound))
            {
                logger.Error("CAR is not Equal with Signer CVC Root");
                throw new CVCVerifierException("CAR is not Equal with Signer CVC Root CHR, CAR:"
                        + carFound + " Expected CAR:" + expectedCAR);
            }
        }

        private String getExpectedCARForAuthority(CVCFields rootCvcAuthFields)
        {
            /*String with 24 (12byte as Hex), after 4 filler byte (8 as in Hex String) its CAR*/
            return StringUtil.ToString(rootCvcAuthFields.get_chr().getByteValues()).Substring(8); // last 8 bytes as Hex
        }

        /*
            private List<ENonSelfDescCVCwithHeader> getCVCChain(ENonSelfDescCVCwithHeader cvcToVerify) throws CVCVerifierException {
                byte[] chrOfAuth = cvcToVerify.getNonSelfDescCVC().getCar();
                List<ENonSelfDescCVCwithHeader> cvcChain = new ArrayList<ENonSelfDescCVCwithHeader>();
                do {
                    ENonSelfDescCVCwithHeader subCvcAuth = chainInfoProvider.getCVCAuthWithCHR(chrOfAuth);
                    if (subCvcAuth == null)
                        throw new CVCVerifierException("CVCChainInfoProvider does not provide CVC Authority with CHR:" + StringUtil.toString(chrOfAuth));
                    cvcChain.add(subCvcAuth);

                    if (Arrays.equals(subCvcAuth.getNonSelfDescCVC().getCar(), chrOfAuth)) {
                        logger.info("RootCVC is found:" + StringUtil.toString(chrOfAuth));
                        break;
                    } else
                        chrOfAuth = subCvcAuth.getNonSelfDescCVC().getCar();
                } while (true);
                return cvcChain;
            }*/

        private /*ERSAPublicKey*/IPublicKey getRootCVCAuthPublicKey()
        {
            IPublicKey rootCVCAuthPublic = null;
            try
            {
                //rootCVCAuthPublic = new ERSAPublicKey(chainInfoProvider.getRootCVCAuthPublic().getEncoded());
                rootCVCAuthPublic = chainInfoProvider.getRootCVCAuthPublic();
            }
            catch (Exception e)
            {
                throw new CVCVerifierException("Verification only runs for RSA Key atm:" + e.Message, e);
            }
            return rootCVCAuthPublic;
        }

        private /*ERSAPublicKey*/IPublicKey getRSAPublicKey(CVCFields intermediateCVCAuthFields)
        {
            try
            {
                ERSAPublicKey rsaPublicKey = intermediateCVCAuthFields.get_rsaPuK().getAsPublicKey();
                ESubjectPublicKeyInfo subjectPublicKeyInfo =
                    ESubjectPublicKeyInfo.createESubjectPublicKeyInfo(rsaPublicKey);
                return new PublicKey(subjectPublicKeyInfo);
            }
            catch (Exception e)
            {
                throw new CVCVerifierException("Verification only runs for RSA Key atm:" + e.Message, e);
            }
        }

        private CVCFields verifyRootCVCAuthority(/*ERSAPublicKey*/IPublicKey superiorCVCAuthorityPublic, ENonSelfDescCVCwithHeader cvcAuthority)
        {
            CVCVerifier cvcAuthorityVerifier = new CVCVerifier(superiorCVCAuthorityPublic);

            EHeaderList headerList = null;
            try
            {
                headerList = cvcAuthority.getHeaderList();
            }
            catch (ESYAException e)
            {
                throw new CVCVerifierException("Error while getting HeaderList:" + e.Message, e);
            }
            EAlgId eAlgId = cvcAuthorityVerifier.extractOID(cvcAuthority.getNonSelfDescCVC(), headerList);

            DigestAlg currentDigestAlg = getDigestAlg(eAlgId);
            CVCFields cvcFields = cvcAuthorityVerifier.calculateCVCFields(currentDigestAlg, cvcAuthority.getNonSelfDescCVC(), headerList);
            if (!CVCRoleIDs.isRootAuth(cvcFields.get_cha()))
                throw new CVCVerifierException(StringUtil.ToString(cvcFields.get_chr().getByteValues()) + " is not Root CVC Authority");

            logger.Info("CVC Chain is Verified for CVC Authority:" + StringUtil.ToString(cvcFields.get_chr().getByteValues()));
            return cvcFields;
        }


        private CVCFields verifyCVC(/*ERSAPublicKey*/IPublicKey superiorCVCAuthorityPublic, DigestAlg currentDigestAlg, ENonSelfDescCVCwithHeader cvcAuthority)
        {
            CVCVerifier cvcAuthorityVerifier = new CVCVerifier(superiorCVCAuthorityPublic);

            EHeaderList headerList;
            try
            {
                headerList = cvcAuthority.getHeaderList();
            }
            catch (ESYAException e)
            {
                throw new CVCVerifierException("Error while getting HeaderList:" + e.Message, e);
            }

            return cvcAuthorityVerifier.calculateCVCFields(currentDigestAlg, cvcAuthority.getNonSelfDescCVC(), headerList);
        }

        private DigestAlg getDigestAlg(EAlgId eAlgId)
        {
            CVCOIDs cvcoiD = null;
            try
            {
                cvcoiD = CVCOIDs.fromOID(new Asn1ObjectIdentifier(eAlgId.toIntArray()));
            }
            catch (Exception e)
            {
                throw new CVCVerifierException("Error While Determining Signature Alg of CVC:" + e.Message, e);
            }
            if (cvcoiD == null)
                throw new CVCVerifierException("Could not Determine Signature Alg of CVC:");
            return cvcoiD.getSignatureAlg().getDigestAlg();
        }
    }
}
