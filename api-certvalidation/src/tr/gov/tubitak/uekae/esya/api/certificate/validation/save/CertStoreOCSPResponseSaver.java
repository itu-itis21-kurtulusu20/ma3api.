package tr.gov.tubitak.uekae.esya.api.certificate.validation.save;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreOCSPOps;

public class CertStoreOCSPResponseSaver extends OCSPResponseSaver
{
	public CertStoreOCSPResponseSaver()
	{
		super();
	}
	
	@Override
	protected void _addOCSP(EOCSPResponse aOcspResponse, ECertificate aCert)
			throws ESYAException 
	{
		CertStore depo;
        try
        {
            depo = new CertStore();
        }
        catch (CertStoreException aEx)
        {
            LOGGER.error("Sertifika deposuna ulaşılamadı", aEx);
            throw new ESYAException("Sertifika deposuna ulaşılamadı", aEx);
        }
        
        CertStoreOCSPOps ocspOps = new CertStoreOCSPOps(depo);
        
        try
        {
        	ocspOps.writeOCSPResponseAndCertificate(aOcspResponse, aCert);
        }
        catch (CertStoreException aEx)
        {
            LOGGER.error("Depoya sertifika yazılırken hata oluştu", aEx);
            throw new ESYAException("Depoya sertifika yazılırken hata oluştu", aEx);
        }
	}

}
