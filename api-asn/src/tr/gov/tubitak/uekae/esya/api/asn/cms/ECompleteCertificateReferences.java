package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.CompleteCertificateRefs;

public class ECompleteCertificateReferences extends BaseASNWrapper<CompleteCertificateRefs>{

	public ECompleteCertificateReferences(CompleteCertificateRefs aObject)
	{
		super(aObject);
	}


	public ECompleteCertificateReferences(byte[] aBytes)
	throws ESYAException
	{
		super(aBytes,new CompleteCertificateRefs());
	}
	
	public EOtherCertID[] getCertIDs(){
        return wrapArray(mObject.elements, EOtherCertID.class);
    }

    public int getCount(){
        if (mObject.elements!=null)
            return mObject.elements.length;
        return 0;
    }
	
}
