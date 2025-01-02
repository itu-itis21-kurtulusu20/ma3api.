package tr.gov.tubitak.uekae.esya.api.ca.passport.masterlist;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.*;
import tr.gov.tubitak.uekae.esya.api.asn.passport.ECscaMasterList;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureUtil;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NotSignedDataException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.ContentTypeAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.MessageDigestAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SigningTimeAttr;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.asn.cms.SignedData;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerIdentifier;
import tr.gov.tubitak.uekae.esya.asn.cms.SignerInfo;
import tr.gov.tubitak.uekae.esya.asn.cms._cmsValues;
import tr.gov.tubitak.uekae.esya.asn.passport._CscaMasterListValues;

import java.security.spec.AlgorithmParameterSpec;
import java.util.*;

/**
 * Created by orcun.ertugrul on 27-Sep-17.
 */
public class PassportMasterListSignedData
{
    private static final int DEFAULT_SIGNEDDATA_VERSION = 3;
    private static final int DEFAULT_SIGNERINFO_VERSION = 1;


    private static Logger logger = LoggerFactory.getLogger(PassportMasterListSignedData.class);
    private final ESignedData mSignedData;
    protected List<ECertificate> mCertificateList = new ArrayList<ECertificate>();
    protected boolean mSignSuccessfull = false;


    public PassportMasterListSignedData()
    {
        mSignedData = new ESignedData(new SignedData());
        mSignedData.setVersion(DEFAULT_SIGNEDDATA_VERSION);
    }

    public PassportMasterListSignedData(byte [] aContentInfo) throws NotSignedDataException, CMSSignatureException
    {
        EContentInfo contentInfo = null;
        try
        {
            contentInfo = new EContentInfo(aContentInfo);
            if(!Arrays.equals(contentInfo.getContentType().value, _cmsValues.id_signedData))
                throw new NotSignedDataException("Content type is not a signed data. Its oid is " + Arrays.toString(contentInfo.getContentType().value));

            mSignedData = new ESignedData(contentInfo.getContent());

            EEncapsulatedContentInfo masterListContentInfo = mSignedData.getEncapsulatedContentInfo();

            if(!Arrays.equals(masterListContentInfo.getContentType().value, _CscaMasterListValues.id_icao_cscaMasterList))
                throw new NotSignedDataException("Content type is not a CSCA MasterList. Its oid is " + Arrays.toString(masterListContentInfo.getContentType().value));

            ECscaMasterList masterList = new ECscaMasterList(masterListContentInfo.getContent());
            Collections.addAll(mCertificateList, masterList.getCertificates());
        }
        catch(ESYAException ex)
        {
            throw new NotSignedDataException("Error in decoding data", ex);
        }
    }

    public void addCertificate(ECertificate aCert) throws  CMSSignatureException
    {
        if(mSignSuccessfull == true)
            throw new CMSSignatureException("After sign operation, new content con not be added");

        mCertificateList.add(aCert);
    }



    public void sign(BaseSigner aSignerInterface, ECertificate aCert) throws SignatureException
    {
        setContent();

        SignatureAlg signatureAlg = SignatureAlg.fromName(aSignerInterface.getSignatureAlgorithmStr());
        DigestAlg digestAlg = signatureAlg.getDigestAlg();

        ESignerInfo signerInfo = new ESignerInfo(new SignerInfo());


        signerInfo.setVersion(DEFAULT_SIGNERINFO_VERSION);

        ContentTypeAttr contentTypeAttr = new ContentTypeAttr();
        contentTypeAttr.setValue(mSignedData.getEncapsulatedContentInfo().getContentType());


        ESignerIdentifier sid = new ESignerIdentifier(new SignerIdentifier());
        sid.setIssuerAndSerialNumber(new EIssuerAndSerialNumber(aCert));
        signerInfo.setSignerIdentifier(sid);


        signerInfo.setDigestAlgorithm(digestAlg.toAlgorithmIdentifier());

        MessageDigestAttr messageDigestAttr = new MessageDigestAttr();
        messageDigestAttr.setValue(digestAlg,
                new SignableByteArray(mSignedData.getEncapsulatedContentInfo().getContent()));


        setSignedAttributes(signerInfo, digestAlg);

        setSignatureAlgorithm(signerInfo, signatureAlg, aSignerInterface);

        try
        {
            byte[] encoded = signerInfo.getSignedAttributes().getEncoded();
            byte[] signature = aSignerInterface.sign(encoded);

            signerInfo.setSignature(signature);
        }
        catch(Exception ex)
        {
            throw new CMSSignatureException("Crypto Error in signing", ex);
        }

        mSignedData.addSignerInfo(signerInfo);

        CMSSignatureUtil.addCerIfNotExist(mSignedData, aCert);
        CMSSignatureUtil.addDigestAlgIfNotExist(mSignedData, digestAlg.toAlgorithmIdentifier());

        mSignSuccessfull = true;
    }

    private void setSignatureAlgorithm(ESignerInfo aSignerInfo, SignatureAlg aSignatureAlg, BaseSigner aSignerInterface) throws CMSSignatureException
    {
        try
        {
            AlgorithmParameterSpec spec = aSignerInterface.getAlgorithmParameterSpec();
            EAlgorithmIdentifier signatureAlgorithmIdentifier = aSignatureAlg.toAlgorithmIdentifierFromSpec(spec);
            aSignerInfo.setSignatureAlgorithm(signatureAlgorithmIdentifier);
        }
        catch (ESYAException ex)
        {
            throw new CMSSignatureException("Error at setSignatureAlgorith", ex);
        }
    }

    private void setSignedAttributes(ESignerInfo aSignerInfo, DigestAlg aDigestAlg) throws CMSSignatureException
    {
        ContentTypeAttr contentTypeAttr = new ContentTypeAttr();
        contentTypeAttr.setValue(mSignedData.getEncapsulatedContentInfo().getContentType());

        SigningTimeAttr signingTimeAttr = new SigningTimeAttr(Calendar.getInstance());
        signingTimeAttr.setValue();


        MessageDigestAttr messageDigestAttr = new MessageDigestAttr();
        messageDigestAttr.setValue(aDigestAlg,
                new SignableByteArray(mSignedData.getEncapsulatedContentInfo().getContent()));

        aSignerInfo.addSignedAttribute(contentTypeAttr.getAttribute());
        aSignerInfo.addSignedAttribute(signingTimeAttr.getAttribute());
        aSignerInfo.addSignedAttribute(messageDigestAttr.getAttribute());
    }

    private void setContent()
    {
        ECscaMasterList mastertList = new ECscaMasterList();

        mastertList.setCertificates(mCertificateList.toArray(new ECertificate[0]));


        EEncapsulatedContentInfo encapContentInfo = null;
        Asn1ObjectIdentifier contentType = new Asn1ObjectIdentifier(_CscaMasterListValues.id_icao_cscaMasterList);
        encapContentInfo = new EEncapsulatedContentInfo(contentType,new Asn1OctetString(mastertList.getEncoded()));

        mSignedData.setEncapsulatedContentInfo(encapContentInfo);
        if(logger.isDebugEnabled())
            logger.debug("Content is added to signeddata");
    }

    public ECertificate [] getCertificates()
    {
        return mCertificateList.toArray(new ECertificate[0]);
    }

    public byte [] getEncoded()
    {
        return mSignedData.getEncoded();
    }
}
