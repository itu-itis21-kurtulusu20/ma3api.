package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.RevocationValues;
import tr.gov.tubitak.uekae.esya.asn.cms._SeqOfBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.asn.cms._SeqOfCertificateList;
import tr.gov.tubitak.uekae.esya.asn.ocsp.BasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.asn.x509.CertificateList;

import java.util.ArrayList;
import java.util.List;

public class ERevocationValues extends BaseASNWrapper<RevocationValues>
{
	public ERevocationValues(RevocationValues aObject)
	{
		super(aObject);
	}
	
	public ERevocationValues(byte[] aBytes)
	throws ESYAException
	{
		super(aBytes,new RevocationValues());
	}

    public ERevocationValues(List<ECRL> aCRLs, List<EBasicOCSPResponse> aOCSPResponses)
    {
        super(new RevocationValues());


        if(aCRLs!=null &&  aCRLs.size()>0){
            CertificateList[] crlArr = unwrapArray(aCRLs.toArray(new ECRL[aCRLs.size()]));
            mObject.crlVals = new _SeqOfCertificateList(crlArr);
        }
        if(aOCSPResponses!=null && aOCSPResponses.size()>0){
            BasicOCSPResponse[] ocspArr = unwrapArray(aOCSPResponses.toArray(new EBasicOCSPResponse[aOCSPResponses.size()]));
            mObject.ocspVals = new _SeqOfBasicOCSPResponse(ocspArr);
        }
    }


    public List<ECRL> getCRLs()
	{
		if(mObject.crlVals==null || mObject.crlVals.elements==null)
			return null;
		
		List<ECRL> crls = new ArrayList<ECRL>();
		for(CertificateList crl:mObject.crlVals.elements)
		{
			crls.add(new ECRL(crl));
		}
		
		return crls;
	}
	
	public List<EBasicOCSPResponse> getBasicOCSPResponses()
	{
		if(mObject.ocspVals==null || mObject.ocspVals.elements==null)
			return null;
		
		List<EBasicOCSPResponse> ocsps = new ArrayList<EBasicOCSPResponse>();
		for(BasicOCSPResponse ocsp:mObject.ocspVals.elements)
		{
			ocsps.add(new EBasicOCSPResponse(ocsp));
		}
		
		return ocsps;
	}
	public int getBasicOCSPResponseCount()
	{
		if (mObject.ocspVals==null || mObject.ocspVals.elements==null)
            return 0;
        return mObject.ocspVals.elements.length;
	}
	public int getCRLCount()
	{
		if (mObject.crlVals==null || mObject.crlVals.elements==null)
            return 0;
        return mObject.crlVals.elements.length;
	}
}
