package tr.gov.tubitak.uekae.esya.api.xmlsignature;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.TimeStampValidationData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ayetgin
 */
public class CertValidationData
{

    private List<ECertificate> mCertificates = new ArrayList<ECertificate>();
    private List<ECRL> mCrls = new ArrayList<ECRL>();
    private List<EOCSPResponse> mOcspResponses = new ArrayList<EOCSPResponse>();

    private Map<XAdESTimeStamp, TimeStampValidationData> mTSValidationData = new HashMap<XAdESTimeStamp, TimeStampValidationData>(0);


    public void addCertificate(ECertificate aCertificate){
        mCertificates.add(aCertificate);
    }

    public void addCRL(ECRL aCRL){
        mCrls.add(aCRL);
    }

    public void addOCSPResponse(EOCSPResponse aResponse){
        mOcspResponses.add(aResponse);
    }

    public List<ECertificate> getCertificates()
    {
        return mCertificates;
    }

    public List<ECRL> getCrls()
    {
        return mCrls;
    }

    public List<EOCSPResponse> getOcspResponses()
    {
        return mOcspResponses;
    }

    public Map<XAdESTimeStamp, TimeStampValidationData> getTSValidationData()
    {
        return mTSValidationData;
    }

    public void setTSValidationData(Map<XAdESTimeStamp, TimeStampValidationData> aTSValidationData)
    {
        mTSValidationData = aTSValidationData;
    }

    public void addTSValidationData(XAdESTimeStamp aXAdESTimeStamp, TimeStampValidationData aTSValidationData)
    {
        mTSValidationData.put(aXAdESTimeStamp, aTSValidationData);
    }


    public TimeStampValidationData getTSValidationDataForTS(XAdESTimeStamp aTimeStamp)
    {
        return mTSValidationData.get(aTimeStamp);
    }

}
