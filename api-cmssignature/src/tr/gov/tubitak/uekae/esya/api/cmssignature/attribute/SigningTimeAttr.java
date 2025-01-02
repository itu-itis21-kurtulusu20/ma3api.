package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import java.util.Calendar;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.asn.x509.Time;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1UTCTime;

/**
 * The signing-time attribute specifies the time at which the signer claims to have 
 * performed the signing process.
 * 
 * Signing-time attribute values for ES have the ASN.1 type SigningTime as defined in CMS (RFC 3852 [4]).
 * NOTE: RFC 3852 [4] states that "dates between January 1, 1950 and December 31, 2049 (inclusive) must be
 * encoded as UTCTime. Any dates with year values before 1950 or after 2049 must be encoded as GeneralizedTime".
 * (etsi 101733v010801 5.9.1)  
 */

public class SigningTimeAttr
extends AttributeValue
{
	 public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_signingTime;
     
	
	 protected Calendar mSigningTime;
	 /**
	 * Create SigningTimeAttr with signing time
	  * @param aSigningTime Calendar
	  * @throws NullParameterException
	  */
	 public SigningTimeAttr(Calendar aSigningTime) 
     throws NullParameterException
	 {
          super();
          mSigningTime = aSigningTime;
          if(mSigningTime == null)
        	  throw new NullParameterException("Signing time must be set");
     }

     private Time _zamanAl(Calendar aTime) 
     throws CMSSignatureException
     {
          Asn1UTCTime now = new Asn1UTCTime();
          try
          {
               now.setTime(aTime);
          } catch (Asn1Exception ex)
          {
               throw new CMSSignatureException(GenelDil.ASN1_ENCODE_HATASI, ex);
          }
          Time time = new Time();
          time.set_utcTime(now);
          return time;
     }

     /**
   	 * Set signing time
   	 */
	public void setValue()
	throws CMSSignatureException 
	{
		 _setValue(_zamanAl(mSigningTime));
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
		 * Returns AttributeOID of signing time attribute
		 * @return
		 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
	
	/**
	 * Returns  signing time
	 * @param aAttribute EAttribute
	 * @return Calendar
	 * @throws ESYAException
	 */
	public static Calendar toTime(EAttribute aAttribute) throws ESYAException
	{
		Asn1DerDecodeBuffer buff = new Asn1DerDecodeBuffer(aAttribute.getValue(0));
		Asn1UTCTime utcTime = new Asn1UTCTime();
		try 
		{
			utcTime.decode(buff);
			return utcTime.getTime();
		} 
		catch (Exception e) 
		{
			throw new ESYAException("Asn1 decode error", e);
		}
		
		
	}

	
}