package tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.attrcert.EAttributeCertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.DepoVEN;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.DepoVTKatmani;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.JDBCUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.NitelikSertifikasiYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzneTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SertifikaYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoNitelikSertifikasi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOzet;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.AttributeCertificateSearchTemplate;

import java.util.ArrayList;
import java.util.List;

public class CertStoreAttributeCertificateOps {

	private static Logger logger = LoggerFactory.getLogger(CertStoreAttributeCertificateOps.class);

    private final CertStore mCertStore;
    
    public CertStoreAttributeCertificateOps(final CertStore aDepo) {
        try {
            LV.getInstance().checkLD(Urunler.ORTAK);
        } catch (LE e) {
            logger.error("Lisans kontrolu basarisiz.", e);
            throw new ESYARuntimeException("Lisans kontrolu basarisiz.", e);
        }
        mCertStore = aDepo;
    }
	
    
    
    
	
    public List<Pair<DepoNitelikSertifikasi,DepoSertifika>> listAttributeAndPKICertificates(AttributeCertificateSearchTemplate aTemplate)
    throws CertStoreException
    {
    	try
    	{
    		List<Pair<DepoNitelikSertifikasi,DepoSertifika>> sonuclar = new ArrayList<Pair<DepoNitelikSertifikasi,DepoSertifika>>();
    		DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
			Pair<String, List<Object>> ikili = _createQuery(aTemplate);
			String sorgu = ikili.first();
       	 	List<Object> params = ikili.second();
       	 	ItemSource<DepoNitelikSertifikasi> nsItemSource = ven.nitelikSertifikasiListele(sorgu, params.toArray());
       	 	DepoNitelikSertifikasi ns = nsItemSource.nextItem();
       	 	
       	 	while(ns != null)
       	 	{
       	 		DepoSertifika s = ven.sertifikaOku(ns.getSertifikaNo());
       	 		sonuclar.add(new Pair<DepoNitelikSertifikasi, DepoSertifika>(ns, s));
       	 		ns = nsItemSource.nextItem();
       	 	}
       	 	
       	 	return sonuclar;
    	}
    	catch(Exception e)
    	{
    		throw new CertStoreException("Nitelik sertifikalari listelenirken hata olustu.",e);
    	}
    	finally
    	{
    		if(mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
    	}
    	
    }
    
    
	public int deleteAttributeCertificates(AttributeCertificateSearchTemplate aTemplate)
	throws CertStoreException
	{
		try
		{
			DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
			Pair<String, List<Object>> ikili = _createQuery(aTemplate);
			String sorgu = ikili.first();
       	 	List<Object> params = ikili.second();
       	 	ItemSource<DepoNitelikSertifikasi> depoSertifikaItemSource = ven.nitelikSertifikasiListele(sorgu, params.toArray());
       	 	int etkilenen = 0;

       	 	DepoNitelikSertifikasi depoSertifika = depoSertifikaItemSource.nextItem();

       	 	while(depoSertifika != null)
       	 	{
       	 		etkilenen += ven.nitelikSertifikasiSil(depoSertifika.getNitelikSertifikaNo());
       	 		depoSertifika = depoSertifikaItemSource.nextItem();
       	 	}
       	 	JDBCUtil.commit(mCertStore.getConn());
       	 	return etkilenen;
		} 
		catch (Exception e)
		{
			if (mCertStore.getConn() != null) JDBCUtil.rollback(mCertStore.getConn());
			throw new CertStoreException("Sablona gore sertifika silinirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", e);
		}
	}
	
	public void writeAttributeAndPKICertificate(ECertificate aCer,List<EAttributeCertificate> aAttrCerts,String aX400Name,long aDizinNo)
	throws CertStoreException
	{
		DepoSertifika ds = CertStoreUtil.eSYASertifikaTODepoSertifika(aCer);
		ds.setX400Address(aX400Name);
		List<DepoNitelikSertifikasi> list = new ArrayList<DepoNitelikSertifikasi>();
		for(EAttributeCertificate ac:aAttrCerts)
		{
			
			list.add(CertStoreUtil.asnAttributeCertToDepoNitelikSertifikasi(ac));
		}
		
		try
		{
			DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
			List<DepoOzet> sertifikaOzetler = CertStoreUtil.convertToDepoOzet(aCer.getEncoded(), OzneTipi.SERTIFIKA);
			ven.attributeAndPKICertYaz(ds, sertifikaOzetler, list, aDizinNo);
			JDBCUtil.commit(mCertStore.getConn());
        } 
		catch (CertStoreException e)
		{
            if (mCertStore.getConn() != null)
                JDBCUtil.rollback(mCertStore.getConn());
            throw new CertStoreException("Nitelik sertifikasi yazilirken VT hatasi olustu.Yapilan VT islemleri geri alindi.", e);
        }
	}
	
	
	private Pair<String, List<Object>> _createQuery(AttributeCertificateSearchTemplate aTemplate) {
        StringBuffer sb = new StringBuffer("");
        List<Object> params = new ArrayList<Object>();
        sb.append("1=1");
        
        String x500name = aTemplate.getX500Name();
        if (x500name != null) {
            sb.append(" AND " + NitelikSertifikasiYardimci.COLUMN_HOLDER_DNNAME + " = ? ");
            params.add(x500name);
        }

        String rfc822Name = aTemplate.getRfc822Name();
        String x400Name = aTemplate.getX400Address();
        ECertificate cer = aTemplate.getCertificate();
        
        if(rfc822Name != null || x400Name != null || cer != null)
        {
        	StringBuffer selectFromCertTableSB = new StringBuffer("");
        	selectFromCertTableSB.append(" AND "+ NitelikSertifikasiYardimci.COLUMN_SERTIFIKA_ID+ " IN (");
        	selectFromCertTableSB.append("SELECT "+SertifikaYardimci.COLUMN_SERTIFIKA_NO +" FROM "+SertifikaYardimci.SERTIFIKA_TABLO_ADI+" WHERE ");
        	selectFromCertTableSB.append("1=1");
            if(rfc822Name !=null)
            {	
            	selectFromCertTableSB.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_EPOSTA + " LIKE ? ");
            	params.add(rfc822Name);
            }
            
            if(x400Name !=null)
            {	
            	selectFromCertTableSB.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_X400ADDRESS + " LIKE ? ");
            	params.add(x400Name);
            }
            
            if(cer !=null)
            {	
            	selectFromCertTableSB.append(" AND " + SertifikaYardimci.COLUMN_SERTIFIKA_SUBJECT_KEY_ID + " = ? ");
            	params.add(cer.getExtensions().getSubjectKeyIdentifier().getValue());
            }
            
            selectFromCertTableSB.append(" )");
            sb.append(selectFromCertTableSB);
         }
        
        return new Pair<String, List<Object>>(sb.toString(), params);
    }
}
