package tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.DepoVEN;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.DepoVTKatmani;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.JDBCUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.NotFoundException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.DizinYardimci;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoDizin;

import java.util.ArrayList;
import java.util.List;

public class CertStoreDirectoryOps
{
	private static Logger logger = LoggerFactory.getLogger(CertStoreDirectoryOps.class);
	
	private final CertStore mCertStore;

    public CertStoreDirectoryOps(final CertStore aDepo)
	{
    	try
    	{
    		LV.getInstance().checkLD(Urunler.ORTAK);
    	}
    	catch(LE ex)
    	{
    		logger.error("Lisans kontrolu basarisiz.");
    		throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
    	}
		mCertStore = aDepo;
	}

    public DepoDizin readDirectory(long aDizinNo)
	throws CertStoreException
	{
		if(aDizinNo <= 0)
		{
			throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
		}
		//Connection oturum = null;
		try
		{
			//oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
			DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
			DepoDizin dizin = ven.dizinOku(aDizinNo);
			return dizin;
		}
		catch(NotFoundException aEx)
		{
			throw new CertStoreException(aDizinNo+ " nolu dizin veritabaninda bulunamadi.",aEx);
		}
		catch (CertStoreException aEx)
		{
			throw new CertStoreException(aDizinNo+ " nolu dizin okunurken VT hatasi olustu.",aEx);
		}
		finally
		{
			try
			{
				if (mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
				//JDBCUtil.close(oturum);
			}
			catch (CertStoreException aEx)
			{
				throw new CertStoreException(aDizinNo+ " nolu dizin okunurken VT hatasi olustu.",aEx);
			}
		}
	}
	
	public DepoDizin findDirectory(String aDizinAdi)
	throws CertStoreException
	{
		//Connection oturum = null;
		try
		{
			//oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
			DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
			DepoDizin dizin = ven.dizinBul(aDizinAdi);
			return dizin;
		}
		catch(NotFoundException aEx)
		{
			throw new CertStoreException(aDizinAdi+" adli dizin veritabaninda bulunamadi."+aEx);
		}
		catch (CertStoreException aEx)
		{
			throw new CertStoreException(aDizinAdi+" adli dizin bulunurken VT hatasi olustu."+aEx);
		}
		finally
		{
			try
			{
				if (mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
				//JDBCUtil.close(oturum);
			}
			catch (CertStoreException aEx)
			{
				throw new CertStoreException(aDizinAdi+" adli dizin bulunurken VT hatasi olustu.",aEx);
			}
		}
	}
	
	public void renameDirectory(long aDizinNo,String aYeniAd)
	throws CertStoreException
	{
		if(aDizinNo <= 0)
		{
			throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
		}
		//Connection oturum = null;
		try
		{
			//oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
			DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
			ven.dizinAdiDegistir(aDizinNo, aYeniAd);
			JDBCUtil.commit(mCertStore.getConn());
		}
		catch (CertStoreException aEx)
		{
			if (mCertStore.getConn() != null)
				JDBCUtil.rollback(mCertStore.getConn());
			throw new CertStoreException(aDizinNo+" nolu dizinin adini "+aYeniAd+" olarak degistirirken VT hatasi olustu." +
					"Yapilan VT islemleri geri alindi.",aEx);
		}
		/*finally
		{
			JDBCUtil.close(oturum);
		}*/
	}
	
	
	public void writeDirectory(String aDizinAdi)
	throws CertStoreException
	{
		DepoDizin dizin = new DepoDizin();
		dizin.setDizinAdi(aDizinAdi);
		//Connection oturum = null;
		try
		{
			//oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
			DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
			ven.dizinYaz(dizin);
			JDBCUtil.commit(mCertStore.getConn());
		}
		catch (CertStoreException aEx)
		{
			if (mCertStore.getConn() != null)
				JDBCUtil.rollback(mCertStore.getConn());
			throw new CertStoreException(aDizinAdi+" adli dizin yazilirken VT hatasi olustu." +
					"Yapilan VT islemleri geri alindi.",aEx);
		}
		/*finally
		{
			JDBCUtil.close(oturum);
		}*/
		
	}
	
	public List<DepoDizin> listDirectory()
	throws CertStoreException
	{
		//Connection oturum = null;
		try
		{
			//oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
			DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
			List<DepoDizin> list = new ArrayList<DepoDizin>();

            ItemSource<DepoDizin> depoDizinItemSource = ven.dizinListele();

            DepoDizin depoDizin = depoDizinItemSource.nextItem();
            while (depoDizin != null)
            {
                list.add(depoDizin);
                depoDizin = depoDizinItemSource.nextItem();
            }

			return list;
		}
		catch (Exception aEx)
		{
			throw new CertStoreException("Tum dizinler listelenirken VT hatasi olustu.",aEx);
		}
		finally
		{
			try
			{
				if (mCertStore.getConn() != null) JDBCUtil.commit(mCertStore.getConn());
				//JDBCUtil.close(oturum);
			}
			catch (CertStoreException aEx)
			{
				throw new CertStoreException("Tum dizinler listelenirken VT hatasi olustu.",aEx);
			}
		}
	}
	
	public void deleteDirectory(long aDizinNo)
	throws CertStoreException
	{
		if(aDizinNo== DizinYardimci.getDefaultDizinNo())
		{
			throw new CertStoreException(1 +" nolu dizin default dizin oldugu icin silinemez.");
		}
		
		//Connection oturum = null;
		try
		{
			//oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
			DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
			ven.dizinSil(aDizinNo);
			JDBCUtil.commit(mCertStore.getConn());
		}
		catch (CertStoreException aEx)
		{
			if (mCertStore.getConn() != null)
				JDBCUtil.rollback(mCertStore.getConn());
			throw new CertStoreException(aDizinNo+" nolu dizin silinirken VT hatasi olustu.Yapilan VT islemleri geri alindi.",aEx);
		}
		/*finally
		{
			JDBCUtil.close(oturum);
		} */
	}
}
