package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map structure of revocation check
 */
public class RevocationInfoMap {

    public Map<ECertificate,RevocationInfoItems> mMap = new HashMap<ECertificate, RevocationInfoItems>();
    public enum RevocationInfoStatus {RIS_VALID, RIS_INVALID};

    public Map<ECertificate,RevocationInfoItems> getMap() {
        return mMap;
    }

    public void appendCRL(ECertificate iCert, ECRL iCRL) {
    	if (!mMap.containsKey(iCert)){
    		mMap.put(iCert, new RevocationInfoItems());
    	}
        mMap.get(iCert).appendCRL(iCRL);
    }

    public void appendOCSPResponse(ECertificate iCert, EOCSPResponse iOCSPResponse) {
    	if (!mMap.containsKey(iCert)){
    		mMap.put(iCert, new RevocationInfoItems());
    	}
        mMap.get(iCert).appendOCSPResponse(iOCSPResponse);
    }

    public boolean contains(ECertificate iCert) {
        return mMap.containsKey(iCert);
    }

    class RevocationInfoItems {
        List<ECRL> mCRLList = new ArrayList<ECRL>(0);
        List<EOCSPResponse> mOCSPResponseList= new ArrayList<EOCSPResponse>(0);

        public List<ECRL> getCRLList() {
            return mCRLList;
        }

        public List<EOCSPResponse> getOCSPResponseList() {
            return mOCSPResponseList;
        }

        public void appendCRL(ECRL iCRL) {
            mCRLList.add(iCRL);
        }

        public void appendOCSPResponse(EOCSPResponse iOCSPResponse) {
            mOCSPResponseList.add(iOCSPResponse);
        }
    }
}
