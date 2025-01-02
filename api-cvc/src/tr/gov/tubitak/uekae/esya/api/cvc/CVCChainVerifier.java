package tr.gov.tubitak.uekae.esya.api.cvc;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cvc.EAlgId;
import tr.gov.tubitak.uekae.esya.api.asn.cvc.EHeaderList;
import tr.gov.tubitak.uekae.esya.api.asn.cvc.ENonSelfDescCVCwithHeader;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.cvc.oids.CVCOIDs;
import tr.gov.tubitak.uekae.esya.api.cvc.oids.CVCRoleIDs;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 11/17/11 - 2:00 PM <p>
 <b>Description</b>: <br>
 */
public class CVCChainVerifier {
    private static final Logger logger = LoggerFactory.getLogger(CVCChainVerifier.class);

    private ICVCChainInfoProvider chainInfoProvider;
    public boolean verified = false;

    public CVCChainVerifier(ICVCChainInfoProvider chainInfoProvider) {
        this.chainInfoProvider = chainInfoProvider;
    }

    public CVCChainVerificationResult verify(ENonSelfDescCVCwithHeader cvcToVerify) throws CVCVerifierException {

        List<Pair<ENonSelfDescCVCwithHeader, CVCFields>> verifiedCvcAuthorities = new ArrayList<Pair<ENonSelfDescCVCwithHeader, CVCFields>>();

        RSAPublicKey cvcAuthPublicKey = getRootCVCAuthPublicKey();


        ENonSelfDescCVCwithHeader rootCVC = chainInfoProvider.getRootCVCAuthority();

        CVCFields rootCvcAuthFields = verifyRootCVCAuthority(cvcAuthPublicKey, rootCVC);


        verifiedCvcAuthorities.add(new Pair<ENonSelfDescCVCwithHeader, CVCFields>(rootCVC, rootCvcAuthFields));

        DigestAlg digestToSign = getDigestAlg(rootCvcAuthFields.get_oid());


        String expectedCAR = getExpectedCARForAuthority(rootCvcAuthFields);

        carChrVerify(expectedCAR, rootCvcAuthFields);


        //todo  clarify and clean code pls...

        for (int i = chainInfoProvider.getSubCvcAuthorities().size() - 1; i >= 0; i--) {

            ENonSelfDescCVCwithHeader intermediateCVCAuth = chainInfoProvider.getSubCvcAuthorities().get(i);

            CVCFields intermediateCVCAuthFields = verifyCVC(cvcAuthPublicKey, digestToSign, intermediateCVCAuth);

            carChrVerify(expectedCAR, intermediateCVCAuthFields);

            expectedCAR = getExpectedCARForAuthority(intermediateCVCAuthFields);

            if (!CVCRoleIDs.isSubAuth(intermediateCVCAuthFields.get_cha()))
                throw new CVCVerifierException(StringUtil.toString(intermediateCVCAuthFields.get_chr().getByteValues())+ " is not Sub CVC Authority");

            cvcAuthPublicKey = getRSAPublicKey(intermediateCVCAuthFields);

            digestToSign = getDigestAlg(intermediateCVCAuthFields.get_oid());

            verifiedCvcAuthorities.add(new Pair<ENonSelfDescCVCwithHeader, CVCFields>(intermediateCVCAuth, intermediateCVCAuthFields));

            logger.info("CVC Chain is Verified At lvl " + i + " for CVC Authority:" + StringUtil.toString(intermediateCVCAuthFields.get_chr().getByteValues()));
        }

        CVCFields cvcFields = verifyCVC(cvcAuthPublicKey, digestToSign, cvcToVerify);

        carChrVerify(expectedCAR, cvcFields);

         // currently sophisticated...
        if (!CVCRoleIDs.isDevice(cvcFields.get_cha()))
            throw new CVCVerifierException(StringUtil.toString(cvcFields.get_chr().getByteValues())+ " is not Device CVC");

        logger.info("CVC Chain is Verified For CVC:" + StringUtil.toString(cvcFields.get_chr().getByteValues()));

        return new CVCChainVerificationResult(cvcFields, verifiedCvcAuthorities);

    }

    private void carChrVerify(String expectedCAR, CVCFields cvcFields) throws CVCVerifierException {
        String carFound = StringUtil.toString(cvcFields.get_car().getByteValues());
        if (!expectedCAR.equals(carFound)) {
            logger.error("CAR is not Equal with Signer CVC Root");
            throw new CVCVerifierException("CAR is not Equal with Signer CVC Root CHR, CAR:"
                    + carFound + " Expected CAR:" + expectedCAR);
        }
    }

    private String getExpectedCARForAuthority(CVCFields rootCvcAuthFields) {
        /*String with 24 (12byte as Hex), after 4 filler byte (8 as in Hex String) its CAR*/
        return StringUtil.toString(rootCvcAuthFields.get_chr().getByteValues()).substring(8); // last 8 bytes as Hex
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

    private RSAPublicKey getRootCVCAuthPublicKey() throws CVCVerifierException
    {
        try
        {
            X509EncodedKeySpec rsaPublicKeySpec = new X509EncodedKeySpec(chainInfoProvider.getRootCVCAuthPublic().getEncoded());

            KeyFactory instance = KeyFactory.getInstance("RSA", "SunRsaSign");
            RSAPublicKey rootCVCAuthPublic = (RSAPublicKey) instance.generatePublic(rsaPublicKeySpec);
            return rootCVCAuthPublic;
        }
        catch (Exception e)
        {
            throw new CVCVerifierException("Verification only runs for RSA Key atm:" + e.getMessage(), e);
        }
    }

    private RSAPublicKey getRSAPublicKey(CVCFields intermediateCVCAuthFields) throws CVCVerifierException {
        try {
            return intermediateCVCAuthFields.get_rsaPuK().getAsPublicKey();
        } catch (InvalidKeyException e) {
            throw new CVCVerifierException("Verification only runs for RSA Key atm:" + e.getMessage(), e);
        }
    }

    private CVCFields verifyRootCVCAuthority(RSAPublicKey superiorCVCAuthorityPublic, ENonSelfDescCVCwithHeader cvcAuthority) throws CVCVerifierException {
        CVCVerifier cvcAuthorityVerifier = new CVCVerifier(superiorCVCAuthorityPublic);

        EHeaderList headerList = null;
        try {
            headerList = cvcAuthority.getHeaderList();
        } catch (ESYAException e) {
            throw new CVCVerifierException("Error while getting HeaderList:" + e.getMessage(), e);
        }
        EAlgId eAlgId = cvcAuthorityVerifier.extractOID(cvcAuthority.getNonSelfDescCVC(), headerList);

        DigestAlg currentDigestAlg = getDigestAlg(eAlgId);
        CVCFields cvcFields = cvcAuthorityVerifier.calculateCVCFields(currentDigestAlg, cvcAuthority.getNonSelfDescCVC(), headerList);
        if (!CVCRoleIDs.isRootAuth(cvcFields.get_cha()))
            throw new CVCVerifierException(StringUtil.toString(cvcFields.get_chr().getByteValues()) + " is not Root CVC Authority");

        logger.info("CVC Chain is Verified for CVC Authority:" + StringUtil.toString(cvcFields.get_chr().getByteValues()));
        return cvcFields;
    }


    private CVCFields verifyCVC(RSAPublicKey superiorCVCAuthorityPublic, DigestAlg currentDigestAlg, ENonSelfDescCVCwithHeader cvcAuthority) throws CVCVerifierException {
        CVCVerifier cvcAuthorityVerifier = new CVCVerifier(superiorCVCAuthorityPublic);

        EHeaderList headerList;
        try {
            headerList = cvcAuthority.getHeaderList();
        } catch (ESYAException e) {
            throw new CVCVerifierException("Error while getting HeaderList:" + e.getMessage(), e);
        }

        return cvcAuthorityVerifier.calculateCVCFields(currentDigestAlg, cvcAuthority.getNonSelfDescCVC(), headerList);
    }

    private DigestAlg getDigestAlg(EAlgId eAlgId) throws CVCVerifierException {
        CVCOIDs cvcoiD = null;
        try {
            cvcoiD = CVCOIDs.fromOID(new Asn1ObjectIdentifier(eAlgId.toIntArray()));
        } catch (Exception e) {
            throw new CVCVerifierException("Error While Determining Signature Alg of CVC:" + e.getMessage(), e);
        }
        if (cvcoiD == null)
            throw new CVCVerifierException("Could not Determine Signature Alg of CVC:");
        return cvcoiD.getSignatureAlg().getDigestAlg();
    }

}
