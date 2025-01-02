package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ECertificateValues;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ERevocationValues;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CertRevocationInfoFinder.CertRevocationInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreUtil;
import tr.gov.tubitak.uekae.esya.asn.cms.CRLListID;
import tr.gov.tubitak.uekae.esya.asn.cms.CertificateValues;
import tr.gov.tubitak.uekae.esya.asn.cms.CompleteCertificateRefs;
import tr.gov.tubitak.uekae.esya.asn.cms.CompleteRevocationRefs;
import tr.gov.tubitak.uekae.esya.asn.cms.CrlIdentifier;
import tr.gov.tubitak.uekae.esya.asn.cms.CrlOcspRef;
import tr.gov.tubitak.uekae.esya.asn.cms.CrlValidatedID;
import tr.gov.tubitak.uekae.esya.asn.cms.IssuerSerial;
import tr.gov.tubitak.uekae.esya.asn.cms.OcspIdentifier;
import tr.gov.tubitak.uekae.esya.asn.cms.OcspListID;
import tr.gov.tubitak.uekae.esya.asn.cms.OcspResponsesID;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherCertID;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHash;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherHashAlgAndValue;
import tr.gov.tubitak.uekae.esya.asn.cms.OtherRevVals;
import tr.gov.tubitak.uekae.esya.asn.cms.RevocationValues;
import tr.gov.tubitak.uekae.esya.asn.cms._SeqOfBasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.asn.cms._SeqOfCertificateList;
import tr.gov.tubitak.uekae.esya.asn.cms._SeqOfCrlValidatedID;
import tr.gov.tubitak.uekae.esya.asn.cms._SeqOfOcspResponsesID;
import tr.gov.tubitak.uekae.esya.asn.ocsp.BasicOCSPResponse;
import tr.gov.tubitak.uekae.esya.asn.ocsp.ResponderID;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.CertificateList;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralNames;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1GeneralizedTime;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1UTCTime;

/**
 * This class has the methods that are commonly used in attributes 
 * @author aslihan.kubilay
 *
 */

public class AttributeUtil 
{

	private static Logger logger = LoggerFactory.getLogger(CertStoreUtil.class);

	/**
	 * Creates the complete-revocation-references attribute. The first CrlOcspRef of the list refers to signing certificate. The 
     * second and subsequent CrlOcspRef elements refer to OtherCertID elements in order in complete-certificate-references. For trusted certificates,
     * CrlOcspRef elements with null CRLListID,OcspListID,OtherRevRefs are put in attribute
	 * @param aCertRevInfos
	 * @param aDigestAlg
	 * @return
	 * @throws CMSSignatureException
	 */
	public static CompleteRevocationRefs createRevocationReferences(List<CertRevocationInfo> aCertRevInfos,DigestAlg aDigestAlg)
	throws CMSSignatureException
	{
		CrlOcspRef[] crlOcspRefs= new CrlOcspRef[aCertRevInfos.size()];
		
		for(int i=0;i<aCertRevInfos.size();i++)
		{
			CertRevocationInfo info = aCertRevInfos.get(i);
			ECRL[] crls = info.getCRLs();
			EBasicOCSPResponse [] ocspResps = info.getOCSPResponses();
			CRLListID crlListID = null;
			OcspListID ocspListID = null;
			
			if(crls!=null)
			{
				crlListID = createCRLListID(crls,aDigestAlg);
			}
			if(ocspResps!=null)
			{
				ocspListID = createOCSPListID(ocspResps,aDigestAlg);
			}
			
			crlOcspRefs[i] = new CrlOcspRef(crlListID, ocspListID, null);
		}
		
		CompleteRevocationRefs revRefs = new CompleteRevocationRefs(crlOcspRefs);
		return revRefs;
	}
	
	
	
	public static OcspListID createOCSPListID(EBasicOCSPResponse [] aOCSPResp,DigestAlg aDigestAlg)
	throws CMSSignatureException
	{
		OcspResponsesID [] ids = new OcspResponsesID[aOCSPResp.length];
		for(int i=0; i < aOCSPResp.length; i++)
			ids[i] = createOCSPResponsesID(aOCSPResp[i], aDigestAlg);
		
		return new OcspListID(new _SeqOfOcspResponsesID(ids));
	}
	
	public static OcspResponsesID createOCSPResponsesID(EBasicOCSPResponse aOCSPResp,DigestAlg aDigestAlg)
	throws CMSSignatureException
	{
		OtherHash otherHash = createOtherHash(aOCSPResp.getEncoded(), aDigestAlg);
		OcspIdentifier ocspID = createOCSPIdentifier(aOCSPResp);
		return new OcspResponsesID(ocspID, otherHash);
	}
	
	
	
	public static OcspIdentifier createOCSPIdentifier(EBasicOCSPResponse aOCSPResp)
	{
		ResponderID respID = aOCSPResp.getTbsResponseData().getObject().responderID;
		Asn1GeneralizedTime time = aOCSPResp.getTbsResponseData().getObject().producedAt;
		return new OcspIdentifier(respID,time);
	}
	
	
	public static CRLListID createCRLListID(ECRL[] aCRLs,DigestAlg aDigestAlg)
	throws CMSSignatureException
	{
		if(aCRLs==null || aCRLs.length==0)
			return null;
		
		CrlValidatedID[] vIDs = new CrlValidatedID[aCRLs.length];
		for(int i=0;i<aCRLs.length;i++)
		{
			vIDs[i] = createCRLValidatedID(aCRLs[i], aDigestAlg);
		}
		
		return new CRLListID(new _SeqOfCrlValidatedID(vIDs));
	}
	
	public static CrlValidatedID createCRLValidatedID(ECRL aCRL,DigestAlg aDigestAlg)
	throws CMSSignatureException
	{
		OtherHash otherHash = createOtherHash(aCRL.getEncoded(), aDigestAlg);
		CrlIdentifier crlID = createCRLIdentifier(aCRL);
		return new CrlValidatedID(otherHash, crlID);
	}
	
	public static CrlIdentifier createCRLIdentifier(ECRL aCRL)
	{
		Name issuer = aCRL.getObject().tbsCertList.issuer;
		Asn1UTCTime time = (Asn1UTCTime) aCRL.getObject().tbsCertList.thisUpdate.getElement();
		
		BigInteger crlNumber = aCRL.getCRLNumber();
		if(crlNumber!=null)
			return new CrlIdentifier(issuer,time,new Asn1BigInteger(crlNumber));
		return new CrlIdentifier(issuer,time);
	}
	
	
	/*
	 * It creates complete-certificate-references attribute. Reference for signing certificate is not included in this attribute.
	 */
	public static CompleteCertificateRefs createCertificateReferences(List<CertRevocationInfo> aCertRevInfos,DigestAlg aDigestAlg)
	throws CMSSignatureException
	{
		List<OtherCertID> otherCertIDs = new ArrayList<OtherCertID>();
		for(int i=1;i<aCertRevInfos.size();i++)
		{
			otherCertIDs.add(createOtherCertID(aCertRevInfos.get(i).getCertificate(), aDigestAlg));			
		}
		return new CompleteCertificateRefs(otherCertIDs.toArray(new OtherCertID[]{}));
	}
	
	
	
	public static OtherHash createOtherHash(byte[] aData,DigestAlg aDigestAlg)
	throws CMSSignatureException
	{
		OtherHash otherHash = new OtherHash(); 
		if(aDigestAlg.equals(DigestAlg.SHA1))
		{
			byte[] hashValue = createOtherHashValue(aData,DigestAlg.SHA1);
			otherHash.set_sha1Hash(new Asn1OctetString(hashValue));
		}
		else
		{
			OtherHashAlgAndValue algAndValue = createOtherHashAlgAndValue(aData, aDigestAlg);
			otherHash.set_otherHash(algAndValue);
		}
		
		return otherHash;
	}
	
	public static byte[] createOtherHashValue(byte[] aData,DigestAlg aDigestAlg)
	throws CMSSignatureException
	{
		try
		{
			byte[] hashValue = DigestUtil.digest(aDigestAlg, aData);
			return hashValue;
		}
		catch(Exception e)
		{
			throw new CMSSignatureException("OtherHash degeri olusturmak icin ozet alinirken hata olustu.", e);
		}
	}
	
	
	public static OtherHashAlgAndValue createOtherHashAlgAndValue(byte[] aData,DigestAlg aDigestAlg)
	throws CMSSignatureException
	{
		byte[] hashValue = createOtherHashValue(aData, aDigestAlg);
		AlgorithmIdentifier algOID = new AlgorithmIdentifier(aDigestAlg.getOID(), null);
		OtherHashAlgAndValue algAndValue = new OtherHashAlgAndValue(algOID,new Asn1OctetString(hashValue));
		return algAndValue;
	}
	
	public static OtherCertID createOtherCertID(ECertificate aCer,DigestAlg aDigestAlg)
	throws CMSSignatureException
	{
		OtherHash otherHash = createOtherHash(aCer.getEncoded(), aDigestAlg);
		IssuerSerial issuerSerial = createIssuerSerial(aCer);
		return new OtherCertID(otherHash, issuerSerial);
	}

	public static IssuerSerial createIssuerSerial(ECertificate aCer)
	{
		GeneralName gn = new GeneralName();
        gn.set_directoryName(aCer.getObject().tbsCertificate.issuer);

        GeneralNames gns = new GeneralNames(1);
        gns.elements[0] = gn;
        
        IssuerSerial is = new IssuerSerial(gns, aCer.getObject().tbsCertificate.serialNumber);
        return is;
	}
	
	
	//Since in certificate-values attribute definition,it says:
	//It holds the values of certificates referenced in the complete-certificate-references attribute
	//the signer certificate doesnot exist in this values
	public static CertificateValues createCertificateValues(List<CertRevocationInfo> aList)
	{
		List<Certificate> certs = new ArrayList<Certificate>();
		
		//Certificate[] certs = new Certificate[aList.size()-1];
		for(int i=1;i<aList.size();i++)
		{
			certs.add(aList.get(i).getCertificate().getObject());
		}
		
		return new CertificateValues(certs.toArray(new Certificate[]{}));
	}
	
	public static RevocationValues createRevocationValues(List<CertRevocationInfo> aList)
	{
		ArrayList<CertificateList> crls = new ArrayList<CertificateList>();
		ArrayList<BasicOCSPResponse> ocsps = new ArrayList<BasicOCSPResponse>();
		
		for(int i=0;i<aList.size();i++)
		{
			CertRevocationInfo cri = aList.get(i);
			if(cri.getCRLs()!=null)
			{
				for(ECRL crl:cri.getCRLs())
					crls.add(crl.getObject());
			}
			
			if(cri.getOCSPResponses() !=null)
			{
				for(EBasicOCSPResponse basicOcspResponse : cri.getOCSPResponses())
				{
					ocsps.add(basicOcspResponse.getObject());
				}
			}
		}
		
		  _SeqOfCertificateList crlVals = null;
	      _SeqOfBasicOCSPResponse ocspVals = null;
	      OtherRevVals otherRevVals = null;
	      
	      if(crls.size()>0)
	    	  crlVals = new _SeqOfCertificateList(crls.toArray(new CertificateList[]{}));
	      if(ocsps.size()>0)
	    	  ocspVals = new _SeqOfBasicOCSPResponse(ocsps.toArray(new BasicOCSPResponse[]{}));
	      
	      return new RevocationValues(crlVals, ocspVals, otherRevVals);
	}
	
	public static List<ECRL> getCRLs(List<CertRevocationInfo> aList)
	{
		ArrayList<ECRL> crls = new ArrayList<ECRL>();
		for(int i=0;i<aList.size();i++)
		{
			CertRevocationInfo cri = aList.get(i);
			if(cri.getCRLs()!=null)
			{
				for(ECRL crl:cri.getCRLs())
					crls.add(crl);
			}
		}
		
		return crls;
	}
	
	public static List<EOCSPResponse> getOCSPResponses(List<CertRevocationInfo> aList)
	{
		ArrayList<EOCSPResponse> ocsps = new ArrayList<EOCSPResponse>();
		for(int i=0;i<aList.size();i++)
		{
			CertRevocationInfo cri = aList.get(i);
			if(cri.getOCSPResponses()!=null)
			{
				for(EBasicOCSPResponse ocsp:cri.getOCSPResponses())
					ocsps.add(EOCSPResponse.getEOCSPResponse(ocsp));
			}
		}
		
		return ocsps;
	}
	
	public static List<ECertificate> getCertificates(List<CertRevocationInfo> aList)
	{
		ArrayList<ECertificate> certs = new ArrayList<ECertificate>();
		for(int i=1;i<aList.size();i++)
		{
			certs.add(aList.get(i).getCertificate());
		}
		
		return certs;
	}
	
	public static Calendar getTimeFromTimestamp(EContentInfo aCI)
	throws CMSSignatureException
	{
		try
		{
			ESignedData sd = new ESignedData(aCI.getContent());
			return getTimeFromTimestamp(sd);
		}
		catch(Exception aEx)
		{
			throw new CMSSignatureException("Error in retrieving time from contentinfo",aEx);
		}
	}
	
	public static Calendar getTimeFromTimestamp(ESignedData aSD)
	throws CMSSignatureException
	{
		try
		{
			ETSTInfo tstInfo = new ETSTInfo(aSD.getEncapsulatedContentInfo().getContent());
			return tstInfo.getTime();
		}
		catch(Exception aEx)
		{
			throw new CMSSignatureException("Error in retrieving time from signeddata",aEx);
		}
		
	}
	
	public static String getAttributeMemberName(Asn1ObjectIdentifier objIden)
	{
		Field [] fields = AttributeOIDs.class.getFields();
		for (Field field : fields) 
		{
			if(field.getType().equals(Asn1ObjectIdentifier.class))
			{
				Asn1ObjectIdentifier fieldObjIden = null;
				try 
				{
					fieldObjIden = (Asn1ObjectIdentifier) field.get(fieldObjIden);
				} 
				catch (IllegalArgumentException e) 
				{
					logger.warn("Warning in AttributeUtil", e);
				} 
				catch (IllegalAccessException e) 
				{
					logger.warn("Warning in AttributeUtil", e);
				}
				if(fieldObjIden == null) {
					logger.error("fieldObjIden is null");
					return null;
				}
				if(fieldObjIden.equals(objIden))
					return field.getName();
			}
		}
		return null;
	}
	
	protected static final Asn1ObjectIdentifier [] timeStampArray =
	{
		AttributeOIDs.id_aa_ets_contentTimestamp,
		AttributeOIDs.id_aa_signatureTimeStampToken,
		AttributeOIDs.id_aa_ets_escTimeStamp,
		AttributeOIDs.id_aa_ets_certCRLTimestamp,
		AttributeOIDs.id_aa_ets_archiveTimestamp,
		AttributeOIDs.id_aa_ets_archiveTimestampV2,
		AttributeOIDs.id_aa_ets_archiveTimestampV3
	};
	
    public static Asn1ObjectIdentifier toContentType(EAttribute aAttribute) throws ESYAException
    {
    	try 
		{
			Asn1DerDecodeBuffer buff = new Asn1DerDecodeBuffer(aAttribute.getValue(0));
			Asn1ObjectIdentifier objIden = new Asn1ObjectIdentifier();
			objIden.decode(buff);
			return objIden;
		} 
		catch (Asn1Exception e) 
		{
			throw new ESYAException("Asn1 decode error", e);
		} 
		catch (IOException e)
		{
			throw new ESYAException("IOException", e);
		}
    }
	
	public static String getAttributeString(EAttribute attr) throws ESYAException
	{
		Asn1ObjectIdentifier attrOID = attr.getType();
		
		StringBuilder sb = new StringBuilder();
		
		for (Asn1ObjectIdentifier timeStamptOid : timeStampArray)
		{
			if(timeStamptOid.equals(attrOID))
			{
                return SignatureTimeStampAttr.toTime(attr).getTime().toString();
            }
		}
		
		if(attrOID.equals(AttributeOIDs.id_signingTime))
		{
			return SigningTimeAttr.toTime(attr).getTime().toString();
		}
		
		if(attrOID.equals(AttributeOIDs.id_contentType))
		{
			return ContentTypeAttr.toContentType(attr).toString();
		}
		
		
		if(attrOID.equals(AttributeOIDs.id_aa_ets_certValues))
		{
			ECertificateValues values = new ECertificateValues(attr.getValue(0));
			List<ECertificate> certs = values.getCertificates();
			if(certs != null)
			{
				for (ECertificate cert : certs) 
				{
					sb.append(cert.toString());
				}
			}
			
			return sb.toString();
		}
		
		if(attrOID.equals(AttributeOIDs.id_aa_ets_revocationValues))
		{
			ERevocationValues values = new ERevocationValues(attr.getValue(0));
			
			List<ECRL> crls = values.getCRLs();
            if(crls != null)
            {
                for (ECRL crl : crls)
                {
                    sb.append(crl.toString());
                }
            }
			
			List<EBasicOCSPResponse> ocsps = values.getBasicOCSPResponses();
            if(ocsps != null)
            {
                for (EBasicOCSPResponse ocsp : ocsps)
                {
                    sb.append(ocsp.toString());
                }
            }
			
			return sb.toString();
		}
		
		return null;
	}
}
