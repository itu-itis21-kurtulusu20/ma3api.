package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Electronic Signature Formats defined in ETSI TS 101 733, and implemented
 * this API.
 */

public enum ESignatureType 
{
			TYPE_BES
			(
			 	new Asn1ObjectIdentifier[] 
			    {
					AttributeOIDs.id_contentType,
					AttributeOIDs.id_messageDigest
			    },
			 	new Asn1ObjectIdentifier[] 
				{
			 		AttributeOIDs.id_messageDigest
				},
				new Asn1ObjectIdentifier[]{}
		    ),

		    TYPE_EPES
		    (
				new Asn1ObjectIdentifier[] 
				{
			       AttributeOIDs.id_contentType,
			       AttributeOIDs.id_messageDigest,
			       AttributeOIDs.id_aa_ets_sigPolicyId
			     },
	
				new Asn1ObjectIdentifier[] 
		        {
		        	AttributeOIDs.id_messageDigest,
		            AttributeOIDs.id_aa_ets_sigPolicyId
		        },
		        new Asn1ObjectIdentifier[]{}
	      ),

	      TYPE_EST
	      (
				TYPE_BES.mSignedAttributeList,
				TYPE_BES.mCounterSignatureSignedAttributeList,
			    new Asn1ObjectIdentifier[]
			    {
						AttributeOIDs.id_aa_signatureTimeStampToken
			    }
		 ),
		 TYPE_ESC
		 (
			TYPE_BES.mSignedAttributeList,
			TYPE_BES.mCounterSignatureSignedAttributeList,
			new Asn1ObjectIdentifier[]
			{
					AttributeOIDs.id_aa_signatureTimeStampToken,
					AttributeOIDs.id_aa_ets_certificateRefs,
					AttributeOIDs.id_aa_ets_revocationRefs
		    }
		),
		TYPE_ESX_Type1
		(
			TYPE_BES.mSignedAttributeList,
			TYPE_BES.mCounterSignatureSignedAttributeList,
			new Asn1ObjectIdentifier[]
			{
					AttributeOIDs.id_aa_signatureTimeStampToken,
					AttributeOIDs.id_aa_ets_certificateRefs,
					AttributeOIDs.id_aa_ets_revocationRefs,
					AttributeOIDs.id_aa_ets_escTimeStamp
		    }
		),
		TYPE_ESX_Type2
		(
			TYPE_BES.mSignedAttributeList,
			TYPE_BES.mCounterSignatureSignedAttributeList,
			new Asn1ObjectIdentifier[]
			{
					AttributeOIDs.id_aa_signatureTimeStampToken,
					AttributeOIDs.id_aa_ets_certificateRefs,
					AttributeOIDs.id_aa_ets_revocationRefs,
					AttributeOIDs.id_aa_ets_certCRLTimestamp
			}
		),
		TYPE_ESXLong
		(
			TYPE_BES.mSignedAttributeList,
			TYPE_BES.mCounterSignatureSignedAttributeList,
			new Asn1ObjectIdentifier[]
			{
					AttributeOIDs.id_aa_signatureTimeStampToken,
					AttributeOIDs.id_aa_ets_certificateRefs,
					AttributeOIDs.id_aa_ets_revocationRefs,
					AttributeOIDs.id_aa_ets_certValues,
					AttributeOIDs.id_aa_ets_revocationValues
			}
		),
			  
			  
		TYPE_ESXLong_Type1
		(
				TYPE_BES.mSignedAttributeList,
				TYPE_BES.mCounterSignatureSignedAttributeList,
				new Asn1ObjectIdentifier[]
				{
					AttributeOIDs.id_aa_signatureTimeStampToken,
					AttributeOIDs.id_aa_ets_certificateRefs,
					AttributeOIDs.id_aa_ets_revocationRefs,
					AttributeOIDs.id_aa_ets_escTimeStamp,
					AttributeOIDs.id_aa_ets_certValues,
					AttributeOIDs.id_aa_ets_revocationValues
				}
		),
				  
		TYPE_ESXLong_Type2
		(
				TYPE_BES.mSignedAttributeList,
				TYPE_BES.mCounterSignatureSignedAttributeList,
				new Asn1ObjectIdentifier[]
				{
					AttributeOIDs.id_aa_signatureTimeStampToken,
					AttributeOIDs.id_aa_ets_certificateRefs,
					AttributeOIDs.id_aa_ets_revocationRefs,
					AttributeOIDs.id_aa_ets_certCRLTimestamp,
					AttributeOIDs.id_aa_ets_revocationValues,
					AttributeOIDs.id_aa_ets_certValues
				}
		),
		TYPE_ESAv2
		(
			TYPE_BES.mSignedAttributeList,
			TYPE_BES.mCounterSignatureSignedAttributeList,
			new Asn1ObjectIdentifier[]
			{
			    
				AttributeOIDs.id_aa_signatureTimeStampToken,				
				AttributeOIDs.id_aa_ets_certificateRefs,
				AttributeOIDs.id_aa_ets_revocationRefs,
				AttributeOIDs.id_aa_ets_revocationValues,
				AttributeOIDs.id_aa_ets_certValues,
				AttributeOIDs.id_aa_ets_archiveTimestampV2
			}
		  ),
		TYPE_ESA
		(
			TYPE_BES.mSignedAttributeList,
			TYPE_BES.mCounterSignatureSignedAttributeList,
			new Asn1ObjectIdentifier[]
			{
				/*
                AttributeOIDs.id_aa_signatureTimeStampToken,
                AttributeOIDs.id_aa_ets_certificateRefs,
                AttributeOIDs.id_aa_ets_revocationRefs,
                AttributeOIDs.id_aa_ets_revocationValues,
                AttributeOIDs.id_aa_ets_certValues,
                **/
				//id_aa_ets_archiveTimestampV2 veya id_aa_ets_archiveTimestamp olabilir
				//look SignatureParser.java
				//AttributeOIDs.id_aa_ets_archiveTimestampV2
			}
		);

private final Asn1ObjectIdentifier[] mSignedAttributeList;
private final Asn1ObjectIdentifier[] mCounterSignatureSignedAttributeList;
private final Asn1ObjectIdentifier[] mUnSignedAttributeList;
/**
	 * Create ESignatureType from signed attribute list,counter signature signed attribute list and unsigned attribute list
	  * @param aSignedList signed attribute list
	  * @param aCSSignedList counter signature signed attribute list
	  * @param aUnSignedList unsigned attribute list
	  */
ESignatureType(Asn1ObjectIdentifier[] aSignedList,Asn1ObjectIdentifier[] aCSSignedList, Asn1ObjectIdentifier[] aUnSignedList) 
{
	mSignedAttributeList = aSignedList;
	mCounterSignatureSignedAttributeList = aCSSignedList;
	mUnSignedAttributeList = aUnSignedList;
}
/**
 * Returns mandatory signed attribute of signature
 * @param aIsCounterSignature
 * @return
 */
public Asn1ObjectIdentifier[] getMandatorySignedAttributes(boolean aIsCounterSignature)
{
	if (aIsCounterSignature)
		return mCounterSignatureSignedAttributeList;
	
	return mSignedAttributeList;
}
/**
 * Returns mandatory unsigned attribute of signature
 * @return
 */
public Asn1ObjectIdentifier[] getMandatoryUnSignedAttributes()
{
	return mUnSignedAttributeList;
}

/**
 * Create signer from e-signature type and base signed data
  * @param aType ESignature Type
  * @param aSignedData BaseSignedData
  */
public static Signer createSigner(ESignatureType aType, BaseSignedData aSignedData)
throws ESYAException
{
	if(aType==TYPE_BES)
		return new BES(aSignedData);
	else if(aType==TYPE_EPES)
		return new EPES(aSignedData);
	else if(aType==TYPE_EST)
		return new EST(aSignedData);
	else if(aType==TYPE_ESC)
		return 	new ESC(aSignedData);
	else if(aType==TYPE_ESX_Type1)
		return new ESX1(aSignedData);
	else if(aType==TYPE_ESX_Type2)
		return new ESX2(aSignedData);
	else if(aType==TYPE_ESXLong)
		return new ESXLong(aSignedData);
	else if(aType==TYPE_ESXLong_Type1)
		return new ESXLong1(aSignedData);
	else if(aType==TYPE_ESXLong_Type2)
		return new ESXLong2(aSignedData);
	else if(aType==TYPE_ESA)
		return new ESA(aSignedData);
	else if(aType==TYPE_ESAv2)
		return new ESAv2(aSignedData);
	return null;
}
/**
 * Create signer from e-signature type, base signed data and signer info
  * @param aType ESignature Type
  * @param aSignedData BaseSignedData
  * @param aSignerInfo ESignerInfo
  */
public static Signer createSigner(ESignatureType aType, BaseSignedData aSignedData, ESignerInfo aSignerInfo)
throws ESYAException
{
	if(aType==TYPE_BES)
		return new BES(aSignedData, aSignerInfo);
	else if(aType==TYPE_EPES)
		return new EPES(aSignedData, aSignerInfo);
	else if(aType==TYPE_EST)
		return new EST(aSignedData, aSignerInfo);
	else if(aType==TYPE_ESC)
		return 	new ESC(aSignedData, aSignerInfo);
	else if(aType==TYPE_ESX_Type1)
		return new ESX1(aSignedData, aSignerInfo);
	else if(aType==TYPE_ESX_Type2)
		return new ESX2(aSignedData, aSignerInfo);
	else if(aType==TYPE_ESXLong)
		return new ESXLong(aSignedData, aSignerInfo);
	else if(aType==TYPE_ESXLong_Type1)
		return new ESXLong1(aSignedData, aSignerInfo);
	else if(aType==TYPE_ESXLong_Type2)
		return new ESXLong2(aSignedData, aSignerInfo);
	else if(aType==TYPE_ESA)
		return new ESA(aSignedData, aSignerInfo);
	else if(aType==TYPE_ESAv2)
		return new ESAv2(aSignedData, aSignerInfo);
	return null;
}
//public static Class<? extends Signer> getSignatureClass(ESignatureType aTip)
//{
//	if(aTip==TYPE_BES)
//		return BES.class;
//	else if(aTip==TYPE_EPES)
//		return EPES.class;
//	else if(aTip==TYPE_EST)
//		return EST.class;
//	else if(aTip==TYPE_ESC)
//		return 	ESC.class;
//	else if(aTip==TYPE_ESX_Type1)
//		return ESX1.class;
//	else if(aTip==TYPE_ESX_Type2)
//		return ESX2.class;
//	else if(aTip==TYPE_ESXLong)
//		return ESXLong.class;
//	else if(aTip==TYPE_ESXLong_Type1)
//		return ESXLong1.class;
//	else if(aTip==TYPE_ESXLong_Type2)
//		return ESXLong2.class;
//	else if(aTip==TYPE_ESA)
//		return ESA.class;
//	return null;
//	
//}

//@SuppressWarnings("unchecked")
//public static <T extends Signer> T createSigner(ESignatureType aType, BaseSignedData aSignedData)
//throws Exception 
//{
//
//	Class<? extends Signer> c =  ESignatureType.getSignatureClass(aType);
//
//	Class[] paramClasses = new Class[] { BaseSignedData.class };
//
//	Object[] paramValues = new Object[] { aSignedData };
//
//	T t;
//	try {
//		t = (T) c.getDeclaredConstructor(paramClasses).newInstance(paramValues);
//	} catch (Exception e) {
//		throw new CMSSignatureException("", e);
//	} 
//
//	return t;
//
//}


//@SuppressWarnings("unchecked")
//public static <T extends Signer> T createSigner(ESignatureType aType, BaseSignedData aSignedData, ESignerInfo aSignerInfo)
//throws Exception 
//{
//
//	Class<? extends Signer> c =  ESignatureType.getSignatureClass(aType);
//
//	Class[] paramClasses = new Class[] { BaseSignedData.class, ESignerInfo.class };
//
//	Object[] paramValues = new Object[] { aSignedData, aSignerInfo };
//
//	T t;
//	try {
//		t = (T) c.getDeclaredConstructor(paramClasses).newInstance(paramValues);
//	} catch (Exception e) {
//		throw new CMSSignatureException("", e);
//	} 
//
//	return t;
//}
	
	
}
