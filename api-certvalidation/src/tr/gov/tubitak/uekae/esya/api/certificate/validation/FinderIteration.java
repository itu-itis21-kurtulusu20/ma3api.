package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Iteration for certificate finding 
 */
public abstract class FinderIteration {

	public abstract boolean nextIteration(ValidationSystem aValidationSystem, ECertificate iCertificate) throws ESYAException;

	protected abstract boolean _nextSource(ValidationSystem aValidationSystem, ECertificate iCertificate) throws ESYAException;

}

