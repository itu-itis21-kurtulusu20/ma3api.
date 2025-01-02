package tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.DepoVEN;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.DepoVTKatmani;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.JDBCUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.*;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoOzet;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSilinecekKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.CertificateSearchTemplate;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.KeyUsageSearchTemplate;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.util.RsItemSource;
import tr.gov.tubitak.uekae.esya.asn.depo.*;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CertStoreRootCertificateOps
{
	private static Logger logger = LoggerFactory.getLogger(CertStoreRootCertificateOps.class);
	
	private final CertStore mCertStore;

    public CertStoreRootCertificateOps(final CertStore aDepo)
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

    public void deleteRootCertificate(long aKokNo)
	throws CertStoreException
	{
		if(aKokNo <= 0)
		{
			throw new CertStoreException("Nesne ID leri 0 dan buyuk olmak zorundadir");
		}
		//Connection oturum = null;
		try
		{
			//oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
			DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
			ven.kokSertifikaSil(aKokNo);
			JDBCUtil.commit(mCertStore.getConn());
		}
		catch (CertStoreException aEx)
		{
			if (mCertStore.getConn() != null)
				JDBCUtil.rollback(mCertStore.getConn());
			throw new CertStoreException(aKokNo+" nolu koksertifika silinirken VT Hatasi Olustu." +
					"Yapilan VT islemleri geri alindi.",aEx);
		}
		/*finally
		{
			JDBCUtil.close(oturum);
		}*/
	}
	
	public byte[] createRootCertificatesTobeSigned(List<Certificate> aEklenecekList,List<SertifikaTipi> aTipler,List<GuvenlikSeviyesi> aSeviyeler,List<Certificate> aSilinecekList)
	throws CertStoreException,Asn1Exception
	{
		int eklenecekSize = 0 ;
		int tipSize = 0;
		int seviyeSize = 0;
		int silinecekSize = 0;
		
		if(aEklenecekList != null)
		{
			eklenecekSize = aEklenecekList.size();
			tipSize = aTipler.size();
			seviyeSize = aSeviyeler.size(); 
		}
		
		if(aSilinecekList != null)
		{
			silinecekSize = aSilinecekList.size();
		}
		
		if (!((eklenecekSize == tipSize) && (tipSize ==seviyeSize)))
		{
			throw new CertStoreException("Eklenecek sertifikalar icin liste boyutlari ayni olmak zorundadir");
		}
		
		DepoASNKokSertifikalar sertifikalar = new DepoASNKokSertifikalar();
		sertifikalar.elements = new DepoASNKokSertifika[eklenecekSize+silinecekSize];
		int i = 0;
		for(;i<eklenecekSize;i++)
		{
			DepoASNKokSertifika sertifika = new DepoASNKokSertifika();
			DepoASNEklenecekKokSertifika eklenecek = CertStoreUtil.asnCertTOAsnEklenecek(aEklenecekList.get(i),aTipler.get(i),aSeviyeler.get(i));
			sertifika.set_eklenecekSertifika(eklenecek);
			sertifikalar.elements[i]= sertifika;
		}
		
		for(int j=0;j<silinecekSize;i++,j++)
		{
			DepoASNKokSertifika sertifika = new DepoASNKokSertifika();
			DepoASNSilinecekKokSertifika silinecek = CertStoreUtil.asnCertTOAsnSilinecek(aSilinecekList.get(j));
			sertifika.set_silinecekSertifika(silinecek);
			sertifikalar.elements[i]=sertifika;
		}
		
		Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
		sertifikalar.encode(enc);
		return enc.getMsgCopy();
	}
	
	
	
	
	public void addPersonalRootCertificate(Certificate aCert, SertifikaTipi aTip)
	throws CertStoreException
	{
		DepoKokSertifika kok = CertStoreUtil.asnCertTODepoEklenecek(aCert);
		kok.setKokTipi(aTip);
		kok.setKokGuvenSeviyesi(GuvenlikSeviyesi.PERSONAL);
		//Connection oturum = null;

		try
		{
			//oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
			DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
			List<DepoOzet> ozetler = CertStoreUtil.convertToDepoOzet(kok.getValue(), OzneTipi.KOK_SERTIFIKA);
			ven.kokSertifikaYaz(kok,ozetler);
			JDBCUtil.commit(mCertStore.getConn());
		}
		catch (CertStoreException aEx)
		{
			if (mCertStore.getConn() != null)
				JDBCUtil.rollback(mCertStore.getConn());
			throw new CertStoreException("KokSertifika yazilirken VT hatasi olustu." +
					"Yapilan VT islemleri geri alindi.",aEx);
		}
		/*finally
		{
			JDBCUtil.close(oturum);
		} */
	}
	
	public boolean verifySignature(DepoKokSertifika aKok)
	throws CertStoreException
	{
		boolean sonuc = CertStoreUtil.verifyDepoKokSertifika(aKok);
		if(sonuc==false)
			deleteRootCertificate(aKok.getKokSertifikaNo());
		return sonuc;
	}

	public List<DepoKokSertifika> listStoreRootCertificates()
			throws CertStoreException{
    	return listStoreRootCertificates(null);
	}
	
	public List<DepoKokSertifika> listStoreRootCertificates(String hash)
	throws CertStoreException
	{
		//Connection oturum = null;
		try
		{
			//oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
			DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
			List<DepoKokSertifika> list = ((RsItemSource)ven.kokSertifikaListele()).toList();
			List<DepoKokSertifika> dogrulananlar = new ArrayList<DepoKokSertifika>();
			for(DepoKokSertifika kok:list)
			{
				try
				{
					boolean sonuc = CertStoreUtil.verifyDepoKokSertifika(kok, hash);
					if(sonuc == true)
					{
						dogrulananlar.add(kok);
					}
					else
					{
						ven.kokSertifikaSil(kok.getKokSertifikaNo());
					}
				}
				catch(Exception aEx)
				{
					logger.error("Kok sertifika dogrulamada hata", aEx);
				}
			}
			JDBCUtil.commit(mCertStore.getConn());
			return dogrulananlar;
		}
		catch (CertStoreException aEx)
		{
			throw new CertStoreException("Tum koksertifikalar listelenirken VT hatasi olustu.",aEx);
		}
		/*finally
		{
			JDBCUtil.close(oturum);
		} */
	}

	public List<DepoKokSertifika> listStoreRootCertificates(CertificateSearchTemplate aSAS,SertifikaTipi[] aTipler,GuvenlikSeviyesi[] aSeviyeler)
			throws CertStoreException
	{
		return listStoreRootCertificates(aSAS, aTipler, aSeviyeler, null);
	}
	
	
	public List<DepoKokSertifika> listStoreRootCertificates(CertificateSearchTemplate aSAS,SertifikaTipi[] aTipler,GuvenlikSeviyesi[] aSeviyeler, String hash)
	throws CertStoreException
	{
		//Connection oturum = null;
		try
		{
			//oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
			DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
			Pair<String,List<Object>> ikili = _createQuery(aSAS, aTipler, aSeviyeler);
			String sorgu = ikili.first();
			List<Object> params = ikili.second();
			List<DepoKokSertifika> list = ((RsItemSource)ven.kokSertifikaListele(sorgu, params.toArray())).toList();
			List<DepoKokSertifika> dogrulananlar = new ArrayList<DepoKokSertifika>();
			for(DepoKokSertifika kok:list)
			{
				try
				{
					boolean sonuc = CertStoreUtil.verifyDepoKokSertifika(kok, hash);
					if(sonuc == true)
					{
						dogrulananlar.add(kok);
					}
					else
					{
						ven.kokSertifikaSil(kok.getKokSertifikaNo());
					}
				}
				catch(Exception aEx)
				{
					logger.error("Kok sertifika dogrulamada hata", aEx);
				}
			}
			JDBCUtil.commit(mCertStore.getConn());
			return dogrulananlar;
		}
		catch (CertStoreException aEx)
		{
			throw new CertStoreException("Sablona gore koksertifika listelenirken VT hatasi olustu.",aEx);
		}/*
		finally
		{
			JDBCUtil.close(oturum);
		}  */
	}
	
	
	
	public void addSignedRootCertificates(byte[] aImzaliKokSertifikalar)
	throws CertStoreException
	{
		//Connection oturum = null;
		try
		{
			Asn1DerDecodeBuffer dec = new Asn1DerDecodeBuffer(aImzaliKokSertifikalar);
			DepoASNImzalar imzalar = new DepoASNImzalar();
			imzalar.decode(dec);
			
			DepoASNImza[] imzaList = imzalar.elements;
			//oturum = DepoVTKatmani.yeniOturum(mCertStore.getStoreName());
			DepoVEN ven = DepoVTKatmani.yeniDepoVEN(mCertStore.getConn());
		
			for(DepoASNImza imza:imzaList)
			{
				byte[] asnkokb = null;
				DepoASNKokSertifika asnkok = imza.imzalanan;
				
				int tip = asnkok.getChoiceID();
				try
				{
					
					DepoASNRawImza asnimza = imza.imza;
	
					Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer(); 
					asnimza.encode(enc);
					byte[] depoimza = enc.getMsgCopy();
					
					Asn1DerEncodeBuffer enc2 = new Asn1DerEncodeBuffer(); 
					asnkok.encode(enc2);
					asnkokb = enc2.getMsgCopy();
				
					boolean dogru = false;
					try
					{
						dogru = CertStoreUtil.verifyEncodedRootCertificate(asnkokb, asnimza);
					}
					catch(Exception aEx)
					{
						logger.error("Imzali dosyadaki kok sertifikalardan biri dogrulanamadi.",aEx);
						continue;
					}
					if(!dogru)
					{
						try
						{
							byte [] certValue = null;
							if(tip == DepoASNKokSertifika._EKLENECEKSERTIFIKA){
								DepoASNEklenecekKokSertifika eklenecek = (DepoASNEklenecekKokSertifika) asnkok.getElement();
								certValue = eklenecek.kokSertifikaValue.value;
							}else if(tip == DepoASNKokSertifika._SILINECEKSERTIFIKA){
								DepoASNSilinecekKokSertifika silinecek = (DepoASNSilinecekKokSertifika) asnkok.getElement();
								certValue = silinecek.kokSertifikaValue.value;
							}
							
							ECertificate cert = new ECertificate(certValue);
							String serino = cert.getSerialNumberHex();
							String subject = cert.getSubject().stringValue();
							logger.error("Imzali Dosyadan "+serino+" seri nolu "+subject+" subjectli kok sertifika dogrulanamadi.");
							continue;
						}
						catch(Exception aEx)
						{
							logger.error("Imzali dosyadaki kok sertifikalardan biri dogrulanamadi.Sertifika bilgileri alinamiyor",aEx);
							continue;
						}
					}
				
					if(tip == DepoASNKokSertifika._EKLENECEKSERTIFIKA)
					{
						DepoASNEklenecekKokSertifika eklenecek = (DepoASNEklenecekKokSertifika) asnkok.getElement();
						DepoKokSertifika depokok = CertStoreUtil.asnEklenecekTODepoKok(eklenecek);
						depokok.setSatirImzasi(depoimza);
						List<DepoOzet> ozetler = CertStoreUtil.convertToDepoOzet(depokok.getValue(), OzneTipi.KOK_SERTIFIKA);
						ven.kokSertifikaYaz(depokok,ozetler);
					}
					if(tip == DepoASNKokSertifika._SILINECEKSERTIFIKA)
					{
						DepoASNSilinecekKokSertifika silinecek = (DepoASNSilinecekKokSertifika) asnkok.getElement();
						DepoSilinecekKokSertifika depokok = CertStoreUtil.asnSilinecekToDepoSilinecek(silinecek);
						depokok.setSatirImzasi(depoimza);
						ven.silinecekKokSertifikaYaz(depokok);
					}
				}
				catch(Exception aEx)
				{
					try
					{
						byte [] certValue = null;
						if(tip == DepoASNKokSertifika._EKLENECEKSERTIFIKA){
							DepoASNEklenecekKokSertifika eklenecek = (DepoASNEklenecekKokSertifika) asnkok.getElement();
							certValue = eklenecek.kokSertifikaValue.value;
						}else if(tip == DepoASNKokSertifika._SILINECEKSERTIFIKA){
							DepoASNSilinecekKokSertifika silinecek = (DepoASNSilinecekKokSertifika) asnkok.getElement();
							certValue = silinecek.kokSertifikaValue.value;
						}
						
						ECertificate cert = new ECertificate(certValue);
						String serino = cert.getSerialNumberHex();
						String subject = cert.getSubject().stringValue();

						logger.error("Imzali Dosyadan "+serino+" seri nolu "+subject+" subjectli kok sertifika eklenemedi.", aEx);
					}
					catch(Exception bEx)
					{
						logger.error("Imzali dosyadaki kok sertifikalardan biri eklenemedi.Sertifika bilgileri alinamiyor",bEx);
					}
				}
			}
			
			JDBCUtil.commit(mCertStore.getConn());
		}
		catch(Exception aEx)
		{
			if (mCertStore.getConn() != null)
				JDBCUtil.rollback(mCertStore.getConn());
			logger.error("Imzali kok sertifikalar veritabanina aktarilirken hata olustu.Yapilan VT islemleri geri alinacak.",aEx);
			throw new CertStoreException("Imzali kok sertifikalar veritabanina aktarilirken hata olustu.Yapilan VT islemleri geri alinacak.",aEx);
		}
		/*finally
		{
			JDBCUtil.close(oturum);
		}*/
	}
	
	
	private Pair<String,List<Object>> _createQuery(CertificateSearchTemplate aSAS,SertifikaTipi[] aTipler,GuvenlikSeviyesi[] aSeviyeler)
	{
		StringBuffer sb = new StringBuffer("");
		List<Object> params = new ArrayList<Object>();
		
		sb.append("1=1");
		
		if(aSAS != null)
		{
			byte[] value = aSAS.getValue();
			if(value != null)
			{
				sb.append(" AND " + KokSertifikaYardimci.COLUMN_KOKSERTIFIKA_VALUE + " = ? ");
				params.add(value);
			}
		
			byte[] hash = aSAS.getHash();
			OzetTipi hashtype = aSAS.getHashType();
			if(hash != null)
			{
				sb.append(" AND " +KokSertifikaYardimci.COLUMN_KOKSERTIFIKA_NO + " IN (SELECT "+OzetYardimci.COLUMN_OBJECT_NO+" FROM "+
						OzetYardimci.OZET_TABLO_ADI+" WHERE ( "+OzetYardimci.COLUMN_OBJECT_TYPE + " = ? AND "+ OzetYardimci.COLUMN_HASH_VALUE +" = ? " );
				params.add(OzneTipi.KOK_SERTIFIKA.getIntValue());
				params.add(hash);
				
				if(hashtype !=null)
				{
					sb.append(" AND "+OzetYardimci.COLUMN_HASH_TYPE+ " = ? ))");
					params.add(hashtype.getIntValue());
				}
				else
				{
					sb.append(" ))");
				}
			}
			else
			{
				if(hashtype!=null)
				{
					sb.append(" AND " +KokSertifikaYardimci.COLUMN_KOKSERTIFIKA_NO + " IN (SELECT "+OzetYardimci.COLUMN_OBJECT_NO+" FROM "+
							OzetYardimci.OZET_TABLO_ADI+" WHERE ( "+OzetYardimci.COLUMN_OBJECT_TYPE + " = ? AND "+ OzetYardimci.COLUMN_HASH_TYPE +" = ? ))" );
					params.add(OzneTipi.KOK_SERTIFIKA.getIntValue());
					params.add(hashtype.getIntValue());
				}
			}
		
			byte[] issuer = aSAS.getIssuer();
			if(issuer != null)
			{
				sb.append(" AND " +KokSertifikaYardimci.COLUMN_KOKSERTIFIKA_ISSUER + " = ? ");
				params.add(issuer);
			}
		
			byte[] subject = aSAS.getSubject();
			if(subject != null)
			{
				sb.append(" AND "+KokSertifikaYardimci.COLUMN_KOKSERTIFIKA_SUBJECT + " = ?");
				params.add(subject);
			}
		
		
			byte[] serial = aSAS.getSerialNumber();
			if(serial != null)
			{
				sb.append(" AND "+KokSertifikaYardimci.COLUMN_KOKSERTIFIKA_SERIAL + " = ? " );
				params.add(serial);
			}
		
			byte[] subjectkeyID = aSAS.getSubjectKeyID();
			if(subjectkeyID != null)
			{
				sb.append(" AND "+KokSertifikaYardimci.COLUMN_KOKSERTIFIKA_SUBJECT_KEY_ID + " = ?");
				params.add(subjectkeyID);
			}
		
		
			KeyUsageSearchTemplate anahtarkullanim = aSAS.getAnahtarKullanim();
			if(anahtarkullanim != null)
			{
				sb.append(" AND "+KokSertifikaYardimci.COLUMN_KOKSERTIFIKA_KEYUSAGE + " LIKE ?");
				params.add(anahtarkullanim.sorguOlustur());
			}
		}
		
		if(aTipler != null && aTipler.length != 0)
		{
			sb.append(" AND ");
			sb.append(KokSertifikaYardimci.COLUMN_KOKSERTIFIKA_TIP+" IN (");
    		for(int i=0;i<aTipler.length-1;i++)
    		{
    			sb.append(aTipler[i].getIntValue()+" , ");
    		}
    		sb.append(aTipler[aTipler.length-1].getIntValue()+" )");
		}
		
		if(aSeviyeler != null && aSeviyeler.length != 0)
		{
			sb.append(" AND ");
			sb.append(KokSertifikaYardimci.COLUMN_KOKSERTIFIKA_GUVENLIK_SEVIYESI+" IN (");
    		for(int i=0;i<aSeviyeler.length-1;i++)
    		{
    			sb.append(aSeviyeler[i].getIntValue()+" , ");
    		}
    		sb.append(aSeviyeler[aSeviyeler.length-1].getIntValue()+" )");
		}
		
		return new Pair<String,List<Object>>(sb.toString(),params);
		
	}

}
