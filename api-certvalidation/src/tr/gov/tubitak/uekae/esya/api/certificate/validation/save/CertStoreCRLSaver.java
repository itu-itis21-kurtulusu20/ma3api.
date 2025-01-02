package tr.gov.tubitak.uekae.esya.api.certificate.validation.save;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.StoreFinder;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCRLOps;

public class CertStoreCRLSaver extends CRLSaver 
{
	private static final Logger logger = LoggerFactory.getLogger(CertStoreCRLSaver.class);

	public CertStoreCRLSaver()
	{
		super();
	}

	@Override
	protected void _addCRL(ECRL sil) throws ESYAException 
	{
		CertStore store;
		try
		{
			store = StoreFinder.createCertStore(mParameters, mParentSystem.getDefaultStorePath());
		}
		catch (CertStoreException aEx)
		{
			LOGGER.error("Sertifika deposuna ulaşılamadı", aEx);
			throw new ESYAException("Sertifika deposuna ulaşılamadı", aEx);
		}

		CertStoreCRLOps crlOps = new CertStoreCRLOps(store);

		try
		{
			// depo dizin numarasını 1 yaptım ama??
			crlOps.writeCRL(sil, 1L);
		}
		catch (CertStoreException aEx)
		{
			LOGGER.error("Depoya sertifika yazılırken hata oluştu", aEx);
			throw new ESYAException("Depoya sertifika yazılırken hata oluştu", aEx);
		}
		
		try
    	{
			store.closeConnection();
		} 
    	catch (CertStoreException e) 
    	{
    		logger.error("Connection couldn't closed", e);
		}
	}
}
