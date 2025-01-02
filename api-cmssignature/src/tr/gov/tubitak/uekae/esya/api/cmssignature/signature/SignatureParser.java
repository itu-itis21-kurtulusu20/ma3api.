package tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

public class SignatureParser
{
	private static final String NEW_LINE = System.getProperty("line.separator");
	
	
	public static ESignatureType parse(ESignerInfo aSI) throws CMSSignatureException
	{
		//Counter signers have one less mandatory attribute(AttributeOIDs.id_contentType).
		//To cover counter signatures, it is accepted as counter signer.
		return parse(aSI, true);
	}
	
	public static ESignatureType parse(ESignerInfo aSI,boolean aIsCounterSigner)
	throws CMSSignatureException
	{

		List<Asn1ObjectIdentifier> signedAttrOIDs = new ArrayList<Asn1ObjectIdentifier>();
		int saCount = aSI.getSignedAttributes().getAttributeCount();
		for(int i=0;i<saCount;i++)
		{
			signedAttrOIDs.add(aSI.getSignedAttributes().getAttribute(i).getType());
		}

		checkSigningCertAttribute(signedAttrOIDs);

		try
		{

			List<Asn1ObjectIdentifier> unsignedAttrOIDs = new ArrayList<Asn1ObjectIdentifier>();

			int usaCount = aSI.getUnsignedAttributes().getAttributeCount();
			for(int i=0;i<usaCount;i++)
			{
				unsignedAttrOIDs.add(aSI.getUnsignedAttributes().getAttribute(i).getType());
			}
				
			ESignatureType matching = null;
			for(ESignatureType imzaTip:ESignatureType.values())
			{
				if(_isTypeCorrect(imzaTip,aIsCounterSigner, signedAttrOIDs,unsignedAttrOIDs))
					matching =  imzaTip;
			}
			
			if(matching == null)
			{
				String signedErrMessage = new String("Signed Attrs" + NEW_LINE);
				for (Asn1ObjectIdentifier signedAttr : signedAttrOIDs) 
				{
					signedErrMessage += Arrays.toString(signedAttr.value);
					signedErrMessage += NEW_LINE;
				}
				
				String unsignedErrMessage = new String("Unsigned Attrs");
				for (Asn1ObjectIdentifier unsignedAttr : unsignedAttrOIDs) 
				{
					unsignedErrMessage += Arrays.toString(unsignedAttr.value);
					unsignedErrMessage += NEW_LINE;
				}
				
				throw new CMSSignatureException("Signature type can not be detected" + NEW_LINE + signedErrMessage + unsignedErrMessage);
			}
			
			return matching;
		}
		catch(Exception aEx)
		{
			throw new CMSSignatureException("Error while finding signature type",aEx);
		}
	}

	//Amerika menşeli kütüphanelerde signingCertAttribute bulunmuyor. Daha anlaşılır bir hata vermek için bu fonksiyon eklendi.
	private static void checkSigningCertAttribute(List<Asn1ObjectIdentifier> signedAttrOIDs) throws CMSSignatureException {
		boolean isSigningCertificateAttrExist = signedAttrOIDs.contains(AttributeOIDs.id_aa_signingCertificate)
				|| signedAttrOIDs.contains(AttributeOIDs.id_aa_signingCertificateV2);
		if(isSigningCertificateAttrExist == false) {
			throw new CMSSignatureException("The mandatory 'signingCertificate' or 'signingCertificateV2' attribute is missing.");
		}
	}

	private static boolean _isTypeCorrect(ESignatureType aImzaTip,boolean aIsCounterSigner,List<Asn1ObjectIdentifier> aSignedAttOIDs,List<Asn1ObjectIdentifier> aUnSignedAttOIDs)
	{
		Asn1ObjectIdentifier signCertOID = AttributeOIDs.id_aa_signingCertificate;
		Asn1ObjectIdentifier signCertV2OID = AttributeOIDs.id_aa_signingCertificateV2;
		
		Asn1ObjectIdentifier archiveTS = AttributeOIDs.id_aa_ets_archiveTimestamp;
		Asn1ObjectIdentifier archiveTSv2 = AttributeOIDs.id_aa_ets_archiveTimestampV2;
		Asn1ObjectIdentifier archiveTSv3 = AttributeOIDs.id_aa_ets_archiveTimestampV3;

		boolean unsigned = true;
		boolean signed = aSignedAttOIDs.containsAll(Arrays.asList(aImzaTip.getMandatorySignedAttributes(aIsCounterSigner))) && (aSignedAttOIDs.contains(signCertOID) ||aSignedAttOIDs.contains(signCertV2OID));
		
		if(aUnSignedAttOIDs.isEmpty() && aImzaTip.getMandatoryUnSignedAttributes().length!=0)
			return false;
		
		if(aImzaTip == ESignatureType.TYPE_ESA)				
			unsigned =  (aUnSignedAttOIDs.contains(archiveTS) || aUnSignedAttOIDs.contains(archiveTSv2) || aUnSignedAttOIDs.contains(archiveTSv3));
			
		if(!aUnSignedAttOIDs.isEmpty() && aImzaTip != ESignatureType.TYPE_ESA)
		{
				unsigned = aUnSignedAttOIDs.containsAll(Arrays.asList(aImzaTip.getMandatoryUnSignedAttributes()));
		}
		
		return signed && unsigned;
	}
	
}
