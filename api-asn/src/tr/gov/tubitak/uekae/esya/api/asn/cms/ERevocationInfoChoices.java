package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherRevocationInfoFormat;
import tr.gov.tubitak.uekae.esya.asn.cms.RevocationInfoChoice;
import tr.gov.tubitak.uekae.esya.asn.cms.RevocationInfoChoices;
import tr.gov.tubitak.uekae.esya.asn.cms._cmsValues;
import tr.gov.tubitak.uekae.esya.asn.ocsp._ocspValues;
import tr.gov.tubitak.uekae.esya.asn.x509.CertificateList;

import java.util.ArrayList;
import java.util.List;

public class ERevocationInfoChoices extends BaseASNWrapper<RevocationInfoChoices> {

	protected static Logger logger = LoggerFactory.getLogger(ERevocationInfoChoices.class);

	public ERevocationInfoChoices(RevocationInfoChoices aObject)
	{
		super(aObject);
	}
	
	public int getRevocationInfoChoiceCount() {
		if(mObject.elements==null)
			return 0;
		
		return mObject.elements.length;
	}
	
	public ERevocationInfoChoice getRevocationInfoChoice(int aIndex) {
		if(mObject.elements==null)
			return null;
		
		return new ERevocationInfoChoice(mObject.elements[aIndex]);
	}
	
	public void addRevocationInfoChoice(ERevocationInfoChoice aRevocationInfoChoice) {
		mObject.elements = extendArray(mObject.elements, aRevocationInfoChoice.getObject());
	}
	
	
	public List<ECRL> getCRLs() {
		List<ECRL> crls = new ArrayList<ECRL>();
		if (mObject.elements == null)
			return crls;

		for (int i = 0; i < mObject.elements.length; i++) {
			RevocationInfoChoice ric = mObject.elements[i];
			if (ric.getChoiceID() == RevocationInfoChoice._CRL)
				crls.add(new ECRL((CertificateList) ric.getElement()));
		}
		return crls;
	}
	
	public List<EOCSPResponse> getOSCPResponses() {
		List<EOCSPResponse> ocsps = new ArrayList<EOCSPResponse>();
		if (mObject.elements == null)
			return ocsps;
		try {
			for (int i = 0; i < mObject.elements.length; i++) {
				RevocationInfoChoice ric = mObject.elements[i];
				if (ric.getChoiceID() == RevocationInfoChoice._OTHER) {
					OtherRevocationInfoFormat format = (OtherRevocationInfoFormat) ric.getElement();
					EOCSPResponse ocsp = null;
					if (format.otherRevInfoFormat.equals(new Asn1ObjectIdentifier(_cmsValues.id_ri_ocsp_response))) {
						ocsp = new EOCSPResponse(format.otherRevInfo.value);
					} else if (format.otherRevInfoFormat.equals(new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_basic))) {
						EBasicOCSPResponse basicOcsp = new EBasicOCSPResponse(format.otherRevInfo.value);
						ocsp = EOCSPResponse.getEOCSPResponse(basicOcsp);
					}
					if (ocsp != null)
						ocsps.add(ocsp);
				}
			}
		} catch (Exception ex) {
			logger.warn("Warning in ERevocationInfoChoices", ex);
			return new ArrayList<EOCSPResponse>();
		}
		return ocsps;
	}
}
