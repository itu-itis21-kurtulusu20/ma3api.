package tr.gov.tubitak.uekae.esya.api.asn.cms;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.cms.*;
import tr.gov.tubitak.uekae.esya.asn.util.UtilTime;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;
import tr.gov.tubitak.uekae.esya.asn.x509.Time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ESignerInfo extends BaseASNWrapper<SignerInfo> {

	protected static Logger logger = LoggerFactory.getLogger(ESignerInfo.class);
	
	public ESignerInfo(SignerInfo aSignerInfo)
	{
		super(aSignerInfo);
	}
	
	public ESignerInfo(byte[] aBytes) throws ESYAException {
		super(aBytes,new SignerInfo());
	}
	
	public int getVersion()
	{
		return (int) mObject.version.value;
	}
	
	public void setVersion(int aVersion)
	{
		mObject.version = new CMSVersion(aVersion);
	}
	
	public ESignerIdentifier getSignerIdentifier()
	{
		return new ESignerIdentifier(mObject.sid);
	}
	
	public void setSignerIdentifier(ESignerIdentifier aSignerIdentifier)
	{
		mObject.sid = aSignerIdentifier.getObject();
	}

	public ECertificate getSignerCertificate(List<ECertificate> certs)
	{
		ESignerIdentifier signerIden = getSignerIdentifier();
		for (ECertificate certificate : certs)
		{
			if(signerIden.isEqual(certificate))
				return certificate;
		}
		return null;
	}
	
	public EAlgorithmIdentifier getDigestAlgorithm()
	{
		return new EAlgorithmIdentifier(mObject.digestAlgorithm);
	}
	
	public void setDigestAlgorithm(EAlgorithmIdentifier aDigestAlgorithm) {
		mObject.digestAlgorithm = aDigestAlgorithm.getObject();
	}
	
	public ESignedAttributes getSignedAttributes()
	{
		return new ESignedAttributes(mObject.signedAttrs);
	}
	
	public void addSignedAttribute(EAttribute aAttribute) {
		if(mObject.signedAttrs==null) {
			mObject.signedAttrs = new SignedAttributes();
			mObject.signedAttrs.elements = new Attribute[0];
		}
		mObject.signedAttrs.elements = extendArray(mObject.signedAttrs.elements, aAttribute.getObject());
	}
	
	public void setSignedAttributes(ESignedAttributes aSignedAttributes) {
		mObject.signedAttrs = aSignedAttributes.getObject();
	}
	
	public EAlgorithmIdentifier getSignatureAlgorithm()
	{
		return new EAlgorithmIdentifier(mObject.signatureAlgorithm);
	}
	
	public void setSignatureAlgorithm(EAlgorithmIdentifier aSignatureAlgorithm) {
		mObject.signatureAlgorithm = aSignatureAlgorithm.getObject();
	}
	
	public byte[] getSignature()
	{
		return mObject.signature.value;
	}
	
	public void setSignature(byte[] aSignature)
	{
		mObject.signature = new Asn1OctetString(aSignature);
	}
	
	public EUnsignedAttributes getUnsignedAttributes()
	{
		return new EUnsignedAttributes(mObject.unsignedAttrs);
	}
	
	public void addUnsignedAttribute(EAttribute aAttribute) {
		if(mObject.unsignedAttrs==null) {
			mObject.unsignedAttrs = new UnsignedAttributes();
			mObject.unsignedAttrs.elements = new Attribute[0];
		}
		
		mObject.unsignedAttrs.elements = extendArray(mObject.unsignedAttrs.elements, aAttribute.getObject());
	}
	
	public void setUnsignedAttributes(EUnsignedAttributes aAttributes)
	{
		mObject.unsignedAttrs = aAttributes.getObject();
	}
	
	public List<EAttribute> getSignedAttribute(Asn1ObjectIdentifier aOID) {
		List<EAttribute> list = new ArrayList<EAttribute>();
		if(mObject.signedAttrs!=null && mObject.signedAttrs.elements!=null) {
			for(Attribute attr:mObject.signedAttrs.elements) {
				if(attr.type.equals(aOID))
					list.add(new EAttribute(attr));
			}
		}
		return list;
	}
	
	public List<EAttribute> getUnsignedAttribute(Asn1ObjectIdentifier aOID) {
		List<EAttribute> list = new ArrayList<EAttribute>();
		if(mObject.unsignedAttrs!=null && mObject.unsignedAttrs.elements!=null) {
			for(Attribute attr:mObject.unsignedAttrs.elements) {
				if(attr.type.equals(aOID))
					list.add(new EAttribute(attr));
			}
		}
		return list;

	}
	
	public boolean removeUnSignedAttribute(EAttribute aAttribute) {
		if(mObject.unsignedAttrs!=null && mObject.unsignedAttrs.elements!=null) {
			try {
				mObject.unsignedAttrs.elements = removeFromArray(mObject.unsignedAttrs.elements, aAttribute.getObject());
				if(mObject.unsignedAttrs.elements.length == 0)
					mObject.unsignedAttrs = null;
				return true;
			} catch(Exception ex) {
				logger.warn("Warning in ESignerInfo", ex);
				return false;
			}
		} else {
			return false;
		}
	}

    public ERevocationValues getRevocationValues() throws ESYAException {
        List<EAttribute> unsignedAttribute = getUnsignedAttribute(new Asn1ObjectIdentifier(_etsi101733Values.id_aa_ets_revocationValues));
        if(unsignedAttribute.size() <= 0)
            return null;
        return new ERevocationValues(unsignedAttribute.get(0).getValue(0));
    }

    public ECertificateValues getCertificateValues() throws ESYAException {
        List<EAttribute> unsignedAttribute = getUnsignedAttribute(new Asn1ObjectIdentifier(_etsi101733Values.id_aa_ets_certValues));
        if(unsignedAttribute.size() <= 0)
            return null;
        return new ECertificateValues(unsignedAttribute.get(0).getValue(0));
    }

    public ECompleteCertificateReferences getCompleteCertificateReferences() throws ESYAException {
        List<EAttribute> unsignedAttribute = getUnsignedAttribute(new Asn1ObjectIdentifier(_etsi101733Values.id_aa_ets_certificateRefs));
        if(unsignedAttribute.size() <= 0)
            return null;
        return new ECompleteCertificateReferences(unsignedAttribute.get(0).getValue(0));
    }

    public ECompleteRevocationReferences getCompleteRevocationReferences() throws ESYAException {
        List<EAttribute> unsignedAttribute = getUnsignedAttribute(new Asn1ObjectIdentifier(_etsi101733Values.id_aa_ets_revocationRefs));
        if(unsignedAttribute.size() <= 0)
            return null;
        return new ECompleteRevocationReferences(unsignedAttribute.get(0).getValue(0));
    }

	public TurkishESigProfile getProfile() throws ESYAException {
		ESignaturePolicy signaturePolicy = getPolicyAttr();
		if(signaturePolicy == null)
			return null;
		ESignaturePolicyId signaturePolicyId = signaturePolicy.getSignaturePolicyId();
		return TurkishESigProfile.getSignatureProfileFromOid(signaturePolicyId.getPolicyObjectIdentifier().value);
	}
	
	public ESignaturePolicy getPolicyAttr() throws ESYAException {
		List<EAttribute> unsignedAttribute = getSignedAttribute(new Asn1ObjectIdentifier(_etsi101733Values.id_aa_ets_sigPolicyId));
		if(unsignedAttribute.size() <= 0)
			return null;
		return new ESignaturePolicy(unsignedAttribute.get(0).getValue(0));
	}

    public Calendar getSigningTime(){
        List<EAttribute> stAttrs = getSignedAttribute(new Asn1ObjectIdentifier(_cmsValues.id_signingTime));
        if(!stAttrs.isEmpty()) {
            EAttribute stAttr = stAttrs.get(0);
            Time time = new Time();
            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(stAttr.getValue(0));
            try {
                time.decode(decBuf);
            } catch(Exception aEx) {
                throw new ESYARuntimeException(aEx);
            }
            return UtilTime.timeToCalendar(time);
        }
        return null;
    }
}
