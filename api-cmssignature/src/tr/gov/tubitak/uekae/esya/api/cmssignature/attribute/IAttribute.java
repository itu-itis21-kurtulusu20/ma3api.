package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;


import java.util.Map;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

/**
 * General contract for signature attributes.
 */

public interface IAttribute {
    Asn1ObjectIdentifier getAttributeOID();
	boolean isSigned();
	void setParameters(Map<String, Object> aParameterAndValue);
	void setValue() throws CMSSignatureException;
	EAttribute getAttribute();
}
