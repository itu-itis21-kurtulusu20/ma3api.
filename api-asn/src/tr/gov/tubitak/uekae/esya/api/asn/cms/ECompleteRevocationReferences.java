package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.CompleteRevocationRefs;

public class ECompleteRevocationReferences extends BaseASNWrapper<CompleteRevocationRefs>{
	
	public ECompleteRevocationReferences(CompleteRevocationRefs aObject)
	{
		super(aObject);
	}
	
	public ECompleteRevocationReferences(byte[] aBytes)
	    throws ESYAException
	{
		super(aBytes,new CompleteRevocationRefs());
	}

    public ECrlOcspRef[] getCrlOcspRefs(){
        return wrapArray(mObject.elements, ECrlOcspRef.class);
    }
    public int getRefCount(){
        if (mObject.elements==null)
            return 0;
        int refSize=mObject.elements.length;
       int  refCount=0;
        for(int i=0;i<refSize;i++){
        	if(mObject.elements[i].crlids!=null){
        		if(mObject.elements[i].crlids.crls!=null && mObject.elements[i].crlids.crls.elements!=null)
        			refCount=refCount+mObject.elements[i].crlids.crls.elements.length;
        	}
        	if(mObject.elements[i].ocspids!=null){
        		if(mObject.elements[i].ocspids.ocspResponses!=null && mObject.elements[i].ocspids.ocspResponses.elements!=null)
        			refCount=refCount+mObject.elements[i].ocspids.ocspResponses.elements.length;
        	}
        }
        
        return refCount;
    }
    
    public int getCount(){
        if (mObject.elements!=null)
            return mObject.elements.length;
        return 0;
    }

}
