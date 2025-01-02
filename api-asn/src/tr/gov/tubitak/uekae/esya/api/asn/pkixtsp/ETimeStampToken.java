package tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;


import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ECmsValues;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EEncapsulatedContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;

public class ETimeStampToken extends BaseASNWrapper<ContentInfo>
{
    private ESignedData mSignedData;
    private ETSTInfo mTSTInfo;
    
    public ETimeStampToken(ContentInfo aCI)
    {
	super(aCI);
    }
    
    public ETimeStampToken(byte[] aBytes) throws ESYAException
    {
	super(aBytes,new ContentInfo());
    }
    
    public ESignedData getSignedData() throws ESYAException
    {
	if(mSignedData==null)
	{
	    if(!mObject.contentType .equals(ECmsValues.OID_SIGNEDDATA))
		return null;
	    mSignedData = new ESignedData(mObject.content.value);
	}
	return mSignedData;
    }
    
    
    public ETSTInfo getTSTInfo() throws ESYAException
    {
	if(mTSTInfo==null)
	{
	    EEncapsulatedContentInfo eci = getSignedData().getEncapsulatedContentInfo();
	    if(!eci.getContentType().equals(EPKIXTSPValues.OID_ct_TSTInfo))
		return null;
	    mTSTInfo = new ETSTInfo(eci.getContent());
	    
	}
	return mTSTInfo;
    }
    
    
    public ECertificate getTSACertificate() throws ESYAException
    {
	return getSignedData().getCertificates().get(0);
    }
}
