package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import java.util.Calendar;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;


import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;


/**
 * The content-time-stamp attribute is an attribute that is the time-stamp token of the signed data content before it
 * is signed.
 * (etsi 101733v010801 5.11.4)
 * @author aslihan.kubilay
 *
 */

public class ContentTimeStampAttr extends AttributeValue
{
    private static final DigestAlg OZET_ALG = DigestAlg.SHA256;
	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_contentTimestamp;

	/**
	 * Default constructor
	 */    
     public ContentTimeStampAttr ()
     {
          super();
     }
     /**
     * Take time stamp
      * @throws CMSSignatureException
      * @throws NullParameterException
     */
     public void setValue () throws CMSSignatureException
     {
          Object content = mAttParams.get(AllEParameters.P_CONTENT);
    	 
    	  if (content == null)
          {
    		  content = mAttParams.get(AllEParameters.P_EXTERNAL_CONTENT);
    		  if(content==null)
               throw new CMSSignatureException("For contenttimestamp attribute,content couldnot be found in signeddata or in parameters");
          }
          
    	  Object digestAlgO = mAttParams.get(AllEParameters.P_TS_DIGEST_ALG);
    	  
    	  if (digestAlgO == null)
          {
               digestAlgO = OZET_ALG;
          }
          
    	  Object tssSpec = mAttParams.get(AllEParameters.P_TSS_INFO);
    	  
          if (tssSpec == null)
          {
               throw new NullParameterException("P_TSS_INFO parameter is not set");
          }
          
         
          ISignable contentI = null;

          try
          {
        	  contentI = (ISignable) content ;
          } catch (ClassCastException ex)
          {
               throw new CMSSignatureException("P_EXTERNAL_CONTENT parameter is not of type ISignable",ex);
          }
          DigestAlg digestAlg = null;
          try
          {
        	  digestAlg = (DigestAlg) digestAlgO;
          } catch (ClassCastException ex)
          {
               throw new CMSSignatureException("P_TS_DIGEST_ALG parameter is not of type DigestAlg",ex);
          }
          
          TSSettings tsSettings = null;
          try
          {
        	  tsSettings = (TSSettings) tssSpec;
          } catch (ClassCastException ex)
          {
               throw new CMSSignatureException("P_TSS_INFO parameter is not of type TSSettings",ex);
          }
          
          //P_TS_DIGEST_ALG deprecated,take digest alg from tsSettings
          if(digestAlg==tsSettings.getDigestAlg() && digestAlgO != null)
          	logger.debug("P_TS_DIGEST_ALG deprecated,digest alg taken from tsSettings");
          digestAlg=tsSettings.getDigestAlg();
          
          ContentInfo token = new ContentInfo();
          try
          {
        	  TSClient tsClient = new TSClient();
               token = tsClient.timestamp(contentI.getMessageDigest(digestAlg), tsSettings).getContentInfo().getObject();
          } catch (Exception ex1)
          {
               throw new CMSSignatureException("Zaman damgasi alinirken hata olustu", ex1);
          }
          _setValue(token);
     }
     
     /**
		 * Checks whether attribute is signed or not.
		 * @return True 
		 */     
	public boolean isSigned() 
	{
		return true;
	}

	 /**
		 * Returns Attribute OID of content time stamp attribute
		 * @return
		 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
	
	/**
	 * Returns  time of content time stamp
	 * @param aAttribute EAttribute
	 * @return Calendar
	 * @throws ESYAException
	 */
	public static Calendar toTime(EAttribute aAttribute) throws ESYAException
	{
		return SignatureTimeStampAttr.toTime(aAttribute);
	}
}