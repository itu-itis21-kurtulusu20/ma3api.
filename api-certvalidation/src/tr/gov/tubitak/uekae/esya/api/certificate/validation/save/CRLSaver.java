package tr.gov.tubitak.uekae.esya.api.certificate.validation.save;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Sil kaydetme
 * @author IH
 */
public abstract class CRLSaver extends Saver
{
	public CRLSaver()
	{
		super();
	}

    public void addCRL(ECRL aSil) throws ESYAException
	{
		_addCRL(aSil);
	}

	protected abstract void _addCRL(ECRL aSil) throws ESYAException;
}

