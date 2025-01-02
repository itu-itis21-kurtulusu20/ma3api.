package tr.gov.tubitak.uekae.esya.api.cmssignature.provider;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidation;
import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;

import java.util.List;


/**
 * @author ayetgin
 */
class SDValidationData extends SignedDataValidation
{

    ESignedData signedData;

    SDValidationData(ESignedData aSignedData)
    {
        signedData = aSignedData;
        collectValidationData();
    }
    SDValidationData(ESignedData aSignedData,Context context)
    {
        signedData = aSignedData;
        collectValidationData(context);
    }
    void collectValidationData() throws SignatureRuntimeException
    {
        try {
            super._fillCertRevocationLists(signedData, null);
        } catch (Exception x){
            throw new SignatureRuntimeException(x);
        }
    }
    void collectValidationData(Context context) throws SignatureRuntimeException
    {
        try {
            super._fillCertRevocationLists(signedData, CMSSigProviderUtil.toSignatureParameters(context));
        } catch (Exception x){
            throw new SignatureRuntimeException(x);
        }
    }
    List<ECertificate> getCerts(){
        return mCerts;
    }
    List<ECRL> getCRLs(){
        return mCRLs;
    }
    List<EBasicOCSPResponse> getOCSPResponses(){
        return mOCSPs;
    }



}
