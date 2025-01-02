package tr.gov.tubitak.uekae.esya.api.infra.directory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.common.bundle.I18nSettings;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * <p>Title: MA3 Dizin islemleri</p>
 * <p>Description: MA3 PKI projesinde, tüm dizin erisimlerini içinde toplayan
 * classlar bu pakette bulunmaktadir. DizinBase classi, dizine baglantiyi yapan,
 * ve diger classlarin ortak ozelliklerini icinde toplayan ana classdir.
 * Diger classlar bu classi exten etmelidirler.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 * @author M. Serdar SORAN
 * @version 1.0
 *
 */


public class DirectoryBase
{
	protected DirContext mConnection = null;
	private boolean mConnected = false;
	private String mMessage = null;
	private Hashtable<String, String> mTemp_hash;

	protected String mDizinTipi = null;

	//Buraya yeni bir sey eklenirse asagidaki attribute adlari da buna gore update edilmeli...
	public final static String ACTIVE_DIRECTORY = "Active Directory";
	public final static String NETSCAPE = "Netscape";
	public final static String CRITICAL_PATH = "Critical Path";

	public final static String ATTR_CN = "cn";
	public final static String ATTR_O = "o";
	public final static String ATTR_OU = "ou";
	public final static String ATTR_INSTANCETYPE = "instanceType";
	public final static String ATTR_OBJECTCLASS = "objectClass";
	public final static String ATTR_CAPRAZSERTIFIKA = "crossCertificatePair";
	public final static String ATTR_KULLANICISERTIFIKASI = "userCert";
	public final static String ATTR_YETKISERTIFIKASI = "attributeCertificateAttribute";
	public final static String ATTR_SMSERTIFIKASI = "cacertificate";
	public final static String ATTR_ARL = "authorityRevocationList";
	public final static String ATTR_CRL = "certificateRevocationList";
	public final static String ATTR_ACRL = "attributeCertificateRevocationList";
	public final static String ATTR_DELTACRL = "deltaRevocationList";
	public final static String ATTR_MAIL = "mail";
	public final static String ATTR_PAROLAESKIMESURESI = "businessCategory";
	public final static String ATTR_PAROLAUZUNUGU = "description";
	public final static String ATTR_PAROLABUYUK = "street";
	public final static String ATTR_PAROLAKUCUK = "st";
	public final static String ATTR_PAROLAALFANUM = "postalCode";
	public final static String ATTR_PAROLARAKAM = "postOfficeBox";
	public final static String ATTR_SN = "sn";
	public final static String ATTR_SAMACCOUNTNAME = "sAMAccountName";
	public final static String ATTR_GIVENNAME = "givenName";
	public final static String ATTR_USERACCOUNTCONTROL = "userAccountControl";
	public final static String ATTR_NAME = "Name";
	public final static String ATTR_MHSORADDRESSES = "mhsORAddresses";
	public final static String ATTR_OBJECTGUID = "ObjectGUID";
	public final static String ATTR_MEMBER_OF = "memberOf";
	public final static String ATTR_DESCPIPTION = "description";

	public final static String ATTR_OSVERSION = "operatingSystemVersion";
	public final static String ATTR_OS = "operatingSystem";
	public final static String ATTR_DNSHOSTNAME = "dNSHostName";
	public final static String ATTR_COUNTRYCODE = "countryCode";
	public final static String ATTR_DN = "distinguishedName";
	public final static String ATTR_OSSERVICEPACK = "operatingSystemServicePack";

	public final static String ATTR_MA3_1 = "ma3custom1";
	public final static String ATTR_MA3_2 = "ma3custom2";
	public final static String ATTR_MA3_3 = "ma3custom3";
	public final static String ATTR_MA3_4 = "ma3custom4";




    //   ** @todo Buradaki hashtable size'lari optimize edilmeli... */
	//

	private static Hashtable<String, String> msHashAD = new Hashtable<String, String>(10);
	private static Hashtable<String, String> msHashNetscape = new Hashtable<String, String>(10);
	private static Hashtable<String, String> msHashCP = new Hashtable<String, String>(10);

	private static final Logger LOGCU = LoggerFactory.getLogger(DirectoryBase.class);

	protected static java.util.ResourceBundle msRB = java.util.ResourceBundle.getBundle("tr.gov.tubitak.uekae.esya.api.common.bundle.GenelBundle"
			, I18nSettings.getLocale());


	public static String[] getAttributeList(String aDizinTipi)
	throws ESYAException
	{
		Hashtable<String, String> temp_hash;
		Vector<String> v = new Vector<String>();
		Enumeration<String> en;
		String[] r;

		if (aDizinTipi.equals(ACTIVE_DIRECTORY))
		{
			temp_hash = msHashAD;
		}
		else if (aDizinTipi.equals(NETSCAPE))
		{
			temp_hash = msHashNetscape;
		}
		else if (aDizinTipi.equals(CRITICAL_PATH))
		{
			temp_hash = msHashCP;
		}
		else
		{
			throw new ESYAException("CIDDI HATA - "+msRB.getString(GenelDil.LDAP_TIP_HATALI));
		}

		en = temp_hash.keys();
		if(en != null)
		{
			while (en.hasMoreElements())
			{
				v.add(en.nextElement());
			}
		}

		r = new String[v.size()];
		for(int i=0;i<r.length;i++)
		{
			r[i] = ((String) v.elementAt(i));
		}
		return r;
	}


	static
	{
		//Active directory attribute isimlerini yazalim...
		msHashAD.put(ATTR_INSTANCETYPE, "instanceType");
		msHashAD.put(ATTR_SAMACCOUNTNAME, "sAMAccountName");

		msHashAD.put(ATTR_GIVENNAME, "givenName");
		msHashAD.put(ATTR_CN, "cn");
		msHashAD.put(ATTR_SN, "sn");
		msHashAD.put(ATTR_O, "o");
		msHashAD.put(ATTR_OU, "ou");
		msHashAD.put(ATTR_OBJECTCLASS, "objectClass");
		msHashAD.put(ATTR_CAPRAZSERTIFIKA, "crossCertificatePair");
		msHashAD.put(ATTR_KULLANICISERTIFIKASI, "userCertificate");
		msHashAD.put(ATTR_YETKISERTIFIKASI, "attributeCertificateAttribute");  // todo check
		msHashAD.put(ATTR_SMSERTIFIKASI, "caCertificate");
		msHashAD.put(ATTR_ARL, "authorityRevocationList");
		msHashAD.put(ATTR_CRL, "certificateRevocationList");
		msHashAD.put(ATTR_ACRL, "attributeCertificateRevocationList");   // todo check
		msHashAD.put(ATTR_DELTACRL, "deltaRevocationList");
		msHashAD.put(ATTR_MAIL, "mail");
		msHashAD.put(ATTR_PAROLAESKIMESURESI, "businessCategory");
		msHashAD.put(ATTR_PAROLAUZUNUGU, "description");
		msHashAD.put(ATTR_PAROLABUYUK, "street");
		msHashAD.put(ATTR_PAROLAKUCUK, "st");
		msHashAD.put(ATTR_PAROLAALFANUM, "postalCode");
		msHashAD.put(ATTR_PAROLARAKAM, "postOfficeBox");
		msHashAD.put(ATTR_USERACCOUNTCONTROL, "UserAccountControl");
		msHashAD.put(ATTR_NAME, "Name");
		msHashAD.put(ATTR_OBJECTGUID, "ObjectGUID");
		msHashAD.put(ATTR_MEMBER_OF, "memberOf");
		msHashAD.put(ATTR_DESCPIPTION, "description");
		msHashAD.put(ATTR_OSVERSION, "operatingSystemVersion");
		msHashAD.put(ATTR_OS, "operatingSystem");
		msHashAD.put(ATTR_DNSHOSTNAME, "dNSHostName");
		msHashAD.put(ATTR_COUNTRYCODE, "countryCode");
		msHashAD.put(ATTR_DN, "distinguishedName");
		msHashAD.put(ATTR_OSSERVICEPACK, "operatingSystemServicePack");
        msHashAD.put(ATTR_MHSORADDRESSES, "mhsORAddresses");

		//netscape attribute isimlerini yazalim
		msHashNetscape.put(ATTR_GIVENNAME, "givenName");

		msHashNetscape.put(ATTR_CN, "cn");
		msHashNetscape.put(ATTR_SN, "sn");
		msHashNetscape.put(ATTR_O, "o");
		msHashNetscape.put(ATTR_OU, "ou");
		msHashNetscape.put(ATTR_OBJECTCLASS, "objectClass");
		msHashNetscape.put(ATTR_CAPRAZSERTIFIKA, "crossCertificatePair");
		msHashNetscape.put(ATTR_KULLANICISERTIFIKASI, "userCertificate");
		msHashNetscape.put(ATTR_YETKISERTIFIKASI, "attributeCertificateAttribute"); // todo check
		msHashNetscape.put(ATTR_SMSERTIFIKASI, "cACertificate");
		msHashNetscape.put(ATTR_CRL, "certificateRevocationList");
		msHashNetscape.put(ATTR_ACRL, "attributeCertificateRevocationList"); // todo check
		msHashNetscape.put(ATTR_DELTACRL, "deltaRevocationList");
		msHashNetscape.put(ATTR_MAIL, "mail");
		msHashNetscape.put(ATTR_PAROLAESKIMESURESI, "businessCategory");
		msHashNetscape.put(ATTR_PAROLAUZUNUGU, "description");
		msHashNetscape.put(ATTR_PAROLABUYUK, "street");
		msHashNetscape.put(ATTR_PAROLAKUCUK, "st");
		msHashNetscape.put(ATTR_PAROLAALFANUM, "postalCode");
		msHashNetscape.put(ATTR_PAROLARAKAM, "postOfficeBox");
		msHashNetscape.put(ATTR_NAME, "Name");
        msHashNetscape.put(ATTR_MHSORADDRESSES, "mhsORAddresses");

		//critical path attribute isimlerini yazalim
		msHashCP.put(ATTR_GIVENNAME, "givenName");
		msHashCP.put(ATTR_CN, "cn");
		msHashCP.put(ATTR_SN, "sn");
		msHashCP.put(ATTR_O, "o");
		msHashCP.put(ATTR_OU, "ou");
		msHashCP.put(ATTR_OBJECTCLASS, "objectClass");
		msHashCP.put(ATTR_CAPRAZSERTIFIKA, "crossCertificatePair;binary");
		msHashCP.put(ATTR_KULLANICISERTIFIKASI, "userCertificate;binary");
		msHashCP.put(ATTR_YETKISERTIFIKASI, "attributeCertificateAttribute;binary"); // todo check
		msHashCP.put(ATTR_SMSERTIFIKASI, "caCertificate;binary");
		msHashCP.put(ATTR_CRL, "certificateRevocationList;binary");
		msHashCP.put(ATTR_ACRL, "attributeCertificateRevocationList;binary"); // todo check
		msHashCP.put(ATTR_DELTACRL, "deltaRevocationList;binary");
		msHashCP.put(ATTR_MAIL, "mail");
		msHashCP.put(ATTR_PAROLAESKIMESURESI, "businessCategory");
		msHashCP.put(ATTR_PAROLAUZUNUGU, "description");
		msHashCP.put(ATTR_PAROLABUYUK, "street");
		msHashCP.put(ATTR_PAROLAKUCUK, "st");
		msHashCP.put(ATTR_PAROLAALFANUM, "postalCode");
		msHashCP.put(ATTR_PAROLARAKAM, "postOfficeBox");
		msHashCP.put(ATTR_NAME, "Name");
		msHashCP.put(ATTR_MHSORADDRESSES, "mhsORAddresses");

	}


    /**
	 * Verilen dizin tipine gore, gerekli bilgileri verilen BaglantiBilgileri'nden
	 * alarak directory'ye baglanir.
	 * @param aBB Baglanti bilgileri almak icin kullanilacak  class
	 */
	public DirectoryBase(DirectoryInfo aBB)    {

		try
		{
			LV.getInstance().checkLD(Urunler.ORTAK);
		}
		catch(LE ex)
		{
			throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
		}

        if(aBB instanceof ExternalDirectoryContext){
            mDizinTipi = aBB.getType();
            try
            {
                mConnection = ((ExternalDirectoryContext) aBB).openConnection();
            } catch (AuthenticationException ex)
            {
                _hataBildir(ex, GenelDil.DIZINSIF_VEYA_KULLADI_HATALI);
                return;
            } catch (CommunicationException ex)
            {
                _hataBildir(ex, GenelDil.IP_PORTTA_DIZIN_BULUNAMADI);
                return;
            } catch (Exception ex)
            {
                _hataBildir(ex, GenelDil.LDAPA_BAGLANILAMADI);
                return;
            }
        } else{
            Hashtable<String, String> env = new Hashtable<String, String>(10);
            mDizinTipi = aBB.getType();
            //ldap tipine gore gerekli argumanlari hash icine yazalim
            if (mDizinTipi.equals(ACTIVE_DIRECTORY) ||
                    mDizinTipi.equals(NETSCAPE) ||
                    mDizinTipi.equals(CRITICAL_PATH))
            {
                env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                //-Desya.ldap.389=ldap:// -Desya.ldap.636=ldaps:// -Djavax.net.ssl.trustStore=C:/.keystore

				if (aBB.getURI()!= null) {
					env.put(Context.PROVIDER_URL,aBB.getURI().toString());
				}
				else {
					String protokol = System.getProperty("esya.ldap." + aBB.getPort());
					if (protokol == null || protokol.equals(""))
						protokol = "ldap://";
					//env.put(Context.PROVIDER_URL, "ldap://krmdcsrv.kermen.net" + ":" + aBB.getPort());
					env.put(Context.PROVIDER_URL, protokol + aBB.getIP() + ":" + aBB.getPort());
				}
                /** @todo  SECURITY_AUTHENTICATION simple olmamali. Guvenlik acigi...*/
                env.put(Context.SECURITY_AUTHENTICATION, "simple");
                env.put(Context.SECURITY_PRINCIPAL, aBB.getUserName());
                env.put(Context.SECURITY_CREDENTIALS, aBB.getUserPassword());
                if(mDizinTipi.equals(ACTIVE_DIRECTORY))
                {
                    env.put("java.naming.ldap.attributes.binary", "objectGUID");
                }
            }
            else
            {
                _hataBildirCiddi(GenelDil.LDAP_TIP_HATALI);
                return;
            }
            //ldap baglantisini saglayalim.
            try
            {
                //mConnection = new InitialDirContext(env);
				mConnection = new InitialLdapContext(env,null);
            } catch (AuthenticationException ex)
            {
                _hataBildir(ex, GenelDil.DIZINSIF_VEYA_KULLADI_HATALI);
                return;
            } catch (CommunicationException ex)
            {
                _hataBildir(ex, GenelDil.IP_PORTTA_DIZIN_BULUNAMADI);
                return;
            } catch (NamingException ex)
            {
                _hataBildir(ex, GenelDil.LDAPA_BAGLANILAMADI);
                return;
            }
        }
		//Hata cikarsa bu noktaya yetisemeden return ile cikmali...
		//Hata cikarsa bu noktaya yetisemeden return ile cikmali...
		mConnected = true;
	}


	/**
	 * Attribute adlari ile ilgili zaman zaman problem cikabilmektedir. Ornegin
	 * bazi adlar case sensitive olabilmektedir. Veya bazi serverlarda attribute adi
	 * biraz farkli olabilmektedir. Bu sorunlardan kurtulmak icin bizim standart
	 * olarak kullanacagimiz adlar bu fonksiyon tarafindan dogru sekle cevrilecektir.
	 * Bu fonksiyon sadece dizinislemleri icinde kullanilmak icindir. Buna ragmen
	 * kullanmak isteyene acik olmasi amaciyla public tanimlanmistir. Dizin tipi
	 * construction sirasinda belirlenmistir.
	 * @param aAttrAdi Bizim standart olarak belirledigimiz attribute adlarindan biri
	 * @return Dizin tipine gore uyarlanmis attribute adi.
	 */
	public String normalizeAttrName(String aAttrAdi)
	{
		String r;

		if (mDizinTipi.equals(ACTIVE_DIRECTORY))
		{
			mTemp_hash = msHashAD;
		}
		else if (mDizinTipi.equals(NETSCAPE))
		{
			mTemp_hash = msHashNetscape;
		}
		else if (mDizinTipi.equals(CRITICAL_PATH))
		{
			mTemp_hash = msHashCP;
		}
		else
		{
			_hataBildirCiddi(GenelDil.LDAP_TIP_HATALI);
			return "";
		}

		r = (String) mTemp_hash.get(aAttrAdi);
		if (r == null)
		{
			_hataBildirCiddi(GenelDil.ATTRIBUTE_BILINMIYOR);
			r = aAttrAdi;   // eper tanımlı değilse ne ise onu alsın.
		}
		return r;
	}


	/**
	 * Directory'ye baglanip baglanamadigimi doner.
	 * @return Directory ile baglantim varsa true, yoksa false doner.
	 */
	public boolean isConnected()  //NOPMD
	{
		return mConnected;
	}


	/**
	 * Son hata mesaji devamli tutulmaktadir. Bu fonksiyon ile olusan son hata mesaji
	 * alinabilir. Herhangi bir ldap isleminde, hata cikmasi durumunda (ki bu durumda
	 * hatanin olustugu fonksiyon false donecektir) kullaniciya hata mesaji getMesaj
	 * kullanilarak donulebilir.
	 * @return Son olusan hata mesaji
	 */
	public String getMessage()  //NOPMD
	{
		return mMessage;
	}


	/*
        protected void hataBildir(Exception e, int hataid)
        {
       mesaj = LoggerFactory.getLogger(null).LogaYaz(e, hataid);
        }
        protected void hataBildir(Exception e, int hataid,String[] argumanlar)
        {
       mesaj = LoggerFactory.getLogger(null).LogaYaz(e, hataid,argumanlar);
        }
        protected void hataBildir(int hataid)
        {
       mesaj = LoggerFactory.getLogger(null).LogaYaz(hataid);
        }
	 */

	protected void _hataBildir (Exception aEx, String aHataMesaji)
	{
		mMessage = msRB.getString(aHataMesaji)+"-"+aEx.getMessage();
		LOGCU.error(mMessage, aEx);
	}


	protected void _hataBildir (Exception aEx, String aMesajKod, Object[] aArgumanlar)
	{
		MessageFormat formatter;

		mMessage = msRB.getString(aMesajKod)+"-"+aEx.getMessage();
		formatter = new MessageFormat(mMessage);

		LOGCU.error(formatter.format(aArgumanlar), aEx);

	}


	protected void _hataBildir (String aHataMesaji)
	{
		mMessage = msRB.getString(aHataMesaji);
		LOGCU.error(mMessage);
	}


	protected void _hataBildirCiddi (String aHataMesaji)
	{
		int i;

		mMessage = msRB.getString(aHataMesaji);
/*		for (i = 0; i < 10; i++)
		{
			LOGCU.fatal(
					"**********************************************************");
		}
		LOGCU.fatal(
				"**************** Ciddi Programlama Hatasi ****************\n" + mMessage);
		for (i = 0; i < 10; i++)
		{
			LOGCU.fatal(
					"**********************************************************");

		}*/
		LOGCU.error("Infra Hatası:"+mMessage);
	}
}
