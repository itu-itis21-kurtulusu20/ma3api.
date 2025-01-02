
using tr.gov.tubitak.uekae.esya.asn.cms;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ECompleteRevocationReferences : BaseASNWrapper<CompleteRevocationRefs>
    {
        public ECompleteRevocationReferences(CompleteRevocationRefs aObject)
            : base(aObject)
        {
        }

        public ECompleteRevocationReferences(byte[] aBytes)
            : base(aBytes, new CompleteRevocationRefs())
        {
        }
        
         public ECrlOcspRef[] getCrlOcspRefs(){
             return wrapArray<ECrlOcspRef, CrlOcspRef>(mObject.elements, typeof(ECrlOcspRef));
        }
        
        public int getRefCount(){
            if (mObject.elements==null)
                return 0;
            int refSize=mObject.elements.Length;
            int  refCount=0;
            for(int i=0;i<refSize;i++){
        	    if(mObject.elements[i].crlids!=null){
        		    if(mObject.elements[i].crlids.crls!=null && mObject.elements[i].crlids.crls.elements!=null)
        			    refCount=refCount+mObject.elements[i].crlids.crls.elements.Length;
        	    }
        	    if(mObject.elements[i].ocspids!=null){
        		    if(mObject.elements[i].ocspids.ocspResponses!=null && mObject.elements[i].ocspids.ocspResponses.elements!=null)
        			    refCount=refCount+mObject.elements[i].ocspids.ocspResponses.elements.Length;
        	    }
            }
            
            return refCount;
        }
    
    public int getCount(){
        if (mObject.elements!=null)
            return mObject.elements.Length;
        return 0;
    }



    }
}
