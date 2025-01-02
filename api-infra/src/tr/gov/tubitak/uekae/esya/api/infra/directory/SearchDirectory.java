package tr.gov.tubitak.uekae.esya.api.infra.directory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;
import java.util.*;

/**
 * <p>Title: MA3 Dizin islemleri</p>
 * <p>Description: MA3 PKI projesinde, tüm dizin erisimlerini içinde toplayan
 * classlar bu pakette bulunmaktadir. Arama islemleri DizinArama classi
 * kullanilarak yapilacaktir.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: TUBITAK/UEKAE</p>
 *
 * @author M. Serdar SORAN
 * @version 1.0
 */

public class SearchDirectory extends DirectoryBase {

    private static final Logger logger = LoggerFactory.getLogger(SearchDirectory.class);

    private String mAramaNoktasi;

    public static final  SearchControls msSC = new SearchControls();
    public static final SearchControls msLocalsc = new SearchControls();
    public static final SearchControls msTeklisc = new SearchControls();

    /**
     * Baglantinin saglandigi constructor. Ayni zamanda arama noktasi(Search Base)
     * de verilmelidir.
     *
     * @param aBB           Baglanti bilgilerinin alinacagi yer
     * @param aAramaNoktasi Arama noktasi (Search Base). Arama noktasi null veya bos olmamali.
     *                      null veya bos olursa problem cikabilir. Hatali bir arama noktasi verilmesi
     *                      durumunda loga hata mesaji yaziliyor.
     * @see #isConnected()
     */
    public SearchDirectory(DirectoryInfo aBB, String aAramaNoktasi) {
        super(aBB);
        mAramaNoktasi = aAramaNoktasi;
        //Tum subtree'de arama yapabilmek icin scope'u set edelim
        msSC.setSearchScope(msSC.SUBTREE_SCOPE);
        msLocalsc.setSearchScope(msLocalsc.OBJECT_SCOPE);
        msTeklisc.setSearchScope(msTeklisc.ONELEVEL_SCOPE);

        if ((mAramaNoktasi == null) || mAramaNoktasi.equals("")) {
            _hataBildir(GenelDil.VERILEN_ARAMA_NOKTASI_NULL_VEYA_BOS);
        }
    }


///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
// TKA verilerek yapilan aramalar
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

    /**
     * Attribute isimlerini hem kontrol edip hem de dizin tipine gore duzenleyip
     * attribute listesini cikarir ve bir String array olarak doner.
     *
     * @param attrsAdlari Tek bir attribute adi tek bir String olarak verilebilir. Veya bir String array'i gonderilerek birden fazla attribute adi verilebilir.
     * @return Duzenlenmis attribute listesi
     */
    private String[] _getAttributeList(Object attrsAdlari) {
        String[] temp;
        String[] r;

        //Ilk arrayi olusturalim...
        if ((attrsAdlari instanceof String[])) {
            temp = (String[]) attrsAdlari;
        } else if ((attrsAdlari instanceof String)) {
            temp = new String[1];
            temp[0] = (String) attrsAdlari;
        } else {
            _hataBildirCiddi(GenelDil.GETATTRIBUTELIST_YANLIS_ARGUMANLA_CAGRILDI); //yanlis argumanla cagrildi
            return null; //Buraya hic gelmemeli...
        }

        //Array'in her elemanininda bir attribute ismi olmali.
        //Isimleri DogruAttrAdi fonksiyonunu kullanarak duzelt...
        r = new String[temp.length];
        for (int i = 0; i < temp.length; i++) {
            r[i] = normalizeAttrName(temp[i]);
        }

        return r;
    }


    /**
     * Verilen TKA'nin istenen attribute degerlerini doner.
     *
     * @param aTKA         Attribute'lari istenen entry'nin TKA'si
     * @param aAttrsAdlari Istenen Attributelar. Tek bir String olabilir veya bir
     *                     String listesi olabilir.
     * @return Dondugu Object[][]'in her elemani, sirayla, istenen attribute'larin
     * birine karsilik gelir. Ve her eleman icinde degerleri barindiran bir
     * arraydir. Tum array'lerin indisleri 0'dan baslar. Yani donulen
     * arrayin [1][2] elemani, verilen attribute listesinin 2. elemaninda verilen
     * attribute'un 3. degeridir.
     */
    public Object[][] getAttributes(String aTKA, Object aAttrsAdlari) {
        String[] attrsList = _getAttributeList(aAttrsAdlari);
        Object[][] r = new Object[attrsList.length][];
        aTKA = aTKA.replaceAll("/", "\\\\/");

        try {
            Attributes attrs = mConnection.getAttributes(aTKA, attrsList);
            // System.out.println("attrs=" + attrs);
            int j;
            for (int i = 0; i < attrsList.length; i++) {
                //Attribute okuyayim
                Attribute attr = attrs.get(attrsList[i]);
                if (attr == null) {
                    r[i] = null;
                    continue;
                }
                //donecegim arraya eklemek icin gereken kadar yer alayim.
                Object[] temp = new Object[attr.size()];
                for (j = 0; j < attr.size(); j++) {
                    temp[j] = attr.get(j);
                }
                //Bu attribute'un tum degerleri temp icinde. Temp'i donecegim arraye atalim.
                r[i] = temp;
            }
        } catch (NamingException ex) {
            _hataBildir(ex, GenelDil.ARANAN_TKA_0_BULUNAMADI, new String[]
                    {aTKA});
            r = null;
        } catch (NullPointerException ex) {
            _hataBildir(ex, GenelDil.LDAPA_BAGLANTI_BULUNAMADI);
            r = null;
        }

        return r;
    }


    /**
     * Capraz sertifikalari doner. getAttrDegerlerine dogru parametreleri
     * gondererek sadece capraz sertifikalari alir ve dogru bir yapiya cast ederek
     * doner.
     *
     * @param aTKA Capraz sertifikalarin alinacagi TKA
     * @return Dondugu byte[][]'in her elamani (byte[] tipinden olacaktir) bir sertifikanin encoded halidir.
     */
    public byte[][] getCrossCertificates(String aTKA) {
        Object[][] okunan = (getAttributes(aTKA, DirectoryBase.ATTR_CAPRAZSERTIFIKA));
        Object[] cerler;
        byte[][] r;

        if (okunan == null) {
            return null; // Bir hata dolayisiyla okunamazsa...
        }
        cerler = okunan[0];
        if (cerler == null) {
            return null;
        }
        r = new byte[cerler.length][];
        for (int i = 0; i < cerler.length; i++) {
            r[i] = (byte[]) cerler[i];
        }
        return r;
    }


///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
// Filtre kullanilarak yapilan aramalar
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

    /**
     * from RFC2254
     * <p>
     * <pre>
     * filter     = "(" filtercomp ")"
     * filtercomp = and / or / not / item
     * and        = "&" filterlist
     * or         = "|" filterlist
     * not        = "!" filter
     * filterlist = 1*filter
     * item       = simple / present / substring / extensible
     * simple     = attr filtertype value
     * filtertype = equal / approx / greater / less
     * equal      = "="
     * approx     = "~="
     * greater    = ">="
     * less       = "&lt ="
     * extensible = attr [":dn"] [":" matchingrule] ":=" value
     * / [":dn"] ":" matchingrule ":=" value
     * present    = attr "=*"
     * substring  = attr "=" [initial] any [final]
     * initial    = value
     * any        = "*" *(value "*")
     * final      = value
     * attr       = AttributeDescription from Section 4.1.5 of RFC 2251
     * matchingrule = MatchingRuleId from Section 4.1.9 of RFC 2251
     * value      = AttributeValue from Section 4.1.6 of RFC 2251
     *
     *
     * Su karakterler, karsisindaki ile degistirilmeli
     *
     * \2a
     * (      \28
     * )      \29
     * \      \5c
     *
     * Ayrica hex olarak herseyi \XX seklinde yazmak mumkun
     *
     * </pre>
     *
     * @param aAramaNoktasi Aramaya baslanacak tka
     * @param aFiltre       Yukaridaki sartlarda hazirlanmis bir filtre olmalidir. Bu
     *                      sartlarin saglandigi varsayilir. Bu konuda herhangi bir kontrol yapilmaz.
     * @param aSC           Aramayi kontrol edecek searchControls
     * @return Arama Noktasindan baslayarak, aramayi yapar, kontrollere gore gerekiyorsa loga yazilmasi gerekeni yazar, buldugu sonuclari veya (bulamama veya hata olma durumunda) null doner.
     */
    private Enumeration _filtreliAramaYap(String aAramaNoktasi, String aFiltre, SearchControls aSC) {

        //aramayi yapalim
        NamingEnumeration ne = null;
        try {
            ne = mConnection.search(aAramaNoktasi, aFiltre, aSC);
        } catch (InvalidSearchFilterException ex) {
            _hataBildir(ex, GenelDil.ARAMA_FILTRESI_HATALI);
        } catch (InvalidSearchControlsException ex) {
            _hataBildir(ex, GenelDil.SEARCH_KONTROL_HATALI);
        } catch (NamingException ex) {
            _hataBildir(ex, GenelDil.ARAMADA_BILINMEYEN_HATA);
        } catch (NullPointerException ex) {
            _hataBildir(ex, GenelDil.LDAPA_BAGLANTI_BULUNAMADI);
        }
        return ne;
    }


    /**
     * Default arama notasindan baslayarak, default olarak tum tree'de arama yapar.
     *
     * @param aFiltre aramada kullanilacak filtre...
     * @return Arama Noktasindan baslayarak, aramayi yapar, kontrollere gore gerekiyorsa loga yazilmasi gerekeni yazar, buldugu sonuclari veya (bulamama veya hata olma durumunda) null doner.
     * @see #_filtreliAramaYap(String, String, SearchControls) serdar
     */
    public Enumeration search(String aFiltre) {
        return _filtreliAramaYap(mAramaNoktasi, aFiltre, msSC);
    }



    /**
     * Verilen arama notasindan baslayarak, default olarak tum tree'de arama yapar.
     *
     *  @param aAramaNoktasi Verilen arama noktasından itibaren arama yapar
     *  @param aFiltre aramada kullanilacak filtre...
     *  @param aSC verilen scope'da arama yapar
     * @return Arama Noktasindan baslayarak, aramayi yapar, kontrollere gore gerekiyorsa loga yazilmasi gerekeni yazar, buldugu sonuclari veya (bulamama veya hata olma durumunda) null doner.
     */
    public List<SearchResult> searchWithPaging(String aAramaNoktasi, String aFiltre, SearchControls aSC) {

        List<SearchResult> list = new ArrayList<SearchResult>();
        Enumeration ne;


        if (mConnection instanceof InitialLdapContext) {

            InitialLdapContext ctx = (InitialLdapContext) mConnection;

            //Set the page size and initialize the cookie that we pass back in subsequent pages
            int pageSize = 500;
            byte[] cookie = null;


            try {
                //Request the paged results control
                Control[] ctls = new Control[]{new PagedResultsControl(pageSize, false)};
                ctx.setRequestControls(ctls);

                do {

                    //Arama yaparak bu seviyedeki entryleri ve object classlarini alalim.
                    ne = _filtreliAramaYap(aAramaNoktasi, aFiltre, aSC);

                    //Enumeration olarak gelen degerleri vectore atalim.
                    if (ne != null) {
                        while (ne.hasMoreElements()) {
                            SearchResult sr = (SearchResult) ne.nextElement();
                            if (sr.getName().contains("\"")) {
                                sr.setName(sr.getName().replaceAll("\"", ""));
                            }








                            list.add(sr);
                        }
                    }

                    // examine the response controls
                    cookie = parseControls(ctx.getResponseControls());

                    // pass the cookie back to the server for the next page
                    ctx.setRequestControls(new Control[]{new PagedResultsControl(pageSize, cookie, Control.CRITICAL)});

                } while (cookie != null && cookie.length != 0);


            } catch (Exception e) {
                _hataBildir(e, +list.size() + " number of entries successfully retrieved. Error in retrieving entries from directory by paging. The rest could not be retrieved");
            }

        } else if (mConnection instanceof ExternalDirectoryContext) {
            //Arama yaparak bu seviyedeki entryleri ve object classlarini alalim.
            ne = _filtreliAramaYap(aAramaNoktasi, aFiltre, aSC);

            //Enumeration olarak gelen degerleri listeye atalim.
            if (ne != null) {
                while (ne.hasMoreElements()) {
                    SearchResult sr = (SearchResult) ne.nextElement();
                    if (sr.getName().contains("\"")) {
                        sr.setName(sr.getName().replaceAll("\"", ""));
                    }
                    list.add(sr);
                }
            }

        } else {
            _hataBildir(GenelDil.LDAP_TIP_HATALI);
        }

        return list;

    }

    /**
     * Filtrelemede bazi karakterlerin ozel anlami var. Verilen string icinde bu
     * karakterlerin degistirilmesi gerekiyor. Bu fonksiyon, ozel anlamli 4 karakteri
     * dogru sekilde encode eder. Verilen string'de \XX seklinde bir karakter
     * olmamalidir.
     *
     * @param aST Ozel karakterlerden arindirilacak string
     * @return Filtrede rahatlikla kullanilabilecek, icinde ozel karakter bulunmayan stringi doner.
     */
    private String _filterSafe(String aST) {
        int i;
        //En basta bunun olmasi onemli. Cunki \ karakteri digerlerinin de icinde geciyor.
        i = 0;
        while ((i = aST.indexOf('\\', i)) != -1) //NOPMD
        {
            aST = aST.substring(0, i) + "\\5c" + aST.substring(i + 1);
            i++;
        }

        while ((i = aST.indexOf('*')) != -1) //NOPMD
        {
            aST = aST.substring(0, i) + "\\2a" + aST.substring(i + 1);
        }
        while ((i = aST.indexOf('(')) != -1) //NOPMD
        {
            aST = aST.substring(0, i) + "\\28" + aST.substring(i + 1);
        }
        while ((i = aST.indexOf(')')) != -1) //NOPMD
        {
            aST = aST.substring(0, i) + "\\29" + aST.substring(i + 1);
        }

        return aST;
    }


    /**
     * email'i verilen bir entry'nin TKA'sini doner. Email'lerin dizinde unique
     * oldugu varsayilmaktadir. Eger ayni emailli birden fazla kullanici varsa
     * loga hatayi yazar, ilk buldugu kullanicinin TKA'sini doner. Filtre
     * belirlenirken yukaridaki grammer kulaniliyor.
     *
     * @param aEmail Aranan email
     * @return Bu email'e sahip entry'nin TKAsi. Arama noktasinda gore goreceli
     * TKA degil de, tam TKA donulecektir.
     */
    public String getTKAbyEmail(String aEmail) {
        Enumeration ne = search(normalizeAttrName(ATTR_MAIL) + "=" + _filterSafe(aEmail));
        if (ne == null) {
            return null; //Demek ki bir hata olusmasi sonucu bir sey bulamadi
        }
        if (ne.hasMoreElements()) {
            SearchResult sr = (SearchResult) ne.nextElement();
            if (ne.hasMoreElements()) {
                //email unique olmali ama eger buraya girerse bu sart directoryde saglanmiyor demekti. Loga bunu yazalim...
                _hataBildir(GenelDil.BIRDEN_FAZLA_EMAIL);
            }
            return sr.getName() + "," + mAramaNoktasi;
        }

        return null; //Buraya gelmesi durumunda bir sey bulamadi demektir.
    }

    /**
     * email'i verilen bir entry'nin TKA' Listesini doner. Dizinde ayni Emai'in birden fazla olabilecegi
     * varsayilmaktadir. Filtre belirlenirken yukaridaki grammer kulaniliyor.
     *
     * @param aEmail Aranan email
     * @return Bu email'e sahip entry'nin TKA listesi. Arama noktasinda gore goreceli
     * TKA degil de, tam TKA listesi donulecektir.
     */
    public List<String> getTKAsbyEmail(String aEmail) {
        List<String> tkaList = new ArrayList<String>();

        Enumeration ne = search(normalizeAttrName(ATTR_MAIL) + "=" + _filterSafe(aEmail));

        if (ne != null) {
            while (ne.hasMoreElements()) {
                SearchResult sr = (SearchResult) ne.nextElement();
                tkaList.add(sr.getName().replaceAll("\"", "") + "," + mAramaNoktasi);
            }

        }

        return tkaList;
    }


    /**
     * Verilen arama noktasindan itibaren tarayarak KSM ve SM olarak niteledigi
     * TKA'lari doner. "caCertificate attribute'u dolu olan her entry KSM veya
     * SM'dir" varsayimi yapilmistir. Liste, caCertificate attribute'u dolu olan
     * entry'lerin listesidir.
     *
     * @return Dondugu array'in her elemani bir KSM veya SM'nin tam TKA'sidir.
     */
    public String[] getKSMSMList() {
        Vector v = new Vector();
        String[] r;

        Enumeration ne = search(normalizeAttrName(ATTR_SMSERTIFIKASI) + "=*");
        if (ne == null) {
            return null; //Demek ki bir hata olusmasi sonucu bir sey bulamadi
        }
        while (ne.hasMoreElements()) {
            SearchResult sr = (SearchResult) ne.nextElement();
            v.add(sr.getName());
        }
        r = new String[v.size()];
        for (int i = v.size() - 1; i >= 0; i--) {
            r[i] = ((String) v.elementAt(i)) + "," + mAramaNoktasi;
        }
        return r;
    }


    /**
     * TKA'si verilen entry altindaki (sadece o seviyedeki) tum entrylerin listesini
     * object classlariyla birlikte verir.
     *
     * @param aTka Aramanin yapilacagu yer.
     * @return Donulen String[][] in ilk elemani bulunan entry'lerin tkalari, ikinci
     * elemani da entry'lerin object classlarina karsilik gelen attribute'un toString
     * fonksiyonu sonucudur.
     */
    public String[][] getEntryListesiVeObjectcls(String aTka) {
        String tmpst;

        List<SearchResult> results= searchWithPaging(aTka,normalizeAttrName(ATTR_OBJECTCLASS) + "=*", msTeklisc);

        int len = results.size();
        String[] r = new String[len];
        String[] r2 = new String[len];

        for(int i=0;i<len;i++){

            String name = results.get(i).getName();
            name = name.replaceAll("\"", "");
            r[i] = name + "," + aTka;

            Attribute att = results.get(i).getAttributes().get(normalizeAttrName(ATTR_OBJECTCLASS));

            if (att == null) {
                tmpst = "DUMMY";
            } else {
                tmpst = att.toString().toUpperCase(new Locale("US"));
            }
            r2[i]= tmpst;
        }

        return new String[][]{r, r2};
    }

    /**
     * Verilen TKA'nin Dizinde olup olmadigini kontrol eder.
     *
     * @param aTka Aranacak TKA
     * @return Eger TKA dizinde varsa true, yoksa false doner.
     */
    public boolean isTKAPresent(String aTka) {

        int virgulYeri = aTka.indexOf(",");
        String ara;
        if (virgulYeri >= 0) {
            ara = aTka.substring(0, virgulYeri);
        } else {
            ara = aTka;

        }
        try {
            //Eger search'de bir hata cikarsa veya search sonucunda hic eleman donmezse
            //exception atacaktir ve bulamadigi anlamina gelir.
            aTka = aTka.replaceAll("/", "\\\\/");//aTka.replace("/", "\\/");
            mConnection.search(aTka, _filterSafe(ara), msLocalsc).hasMore();
        } catch (NamingException ex) {
            return false; //Bulunamadi
        } catch (NullPointerException ex) {
            _hataBildir(ex, GenelDil.LDAPA_BAGLANTI_BULUNAMADI);
            return false;
        }

        return true; //Bulundu
    }


    private boolean _byteArraylarEsit(byte[] aX, byte[] aY) {
        if ((aX == null) && (aY == null))
            return true;
        else if (aX == null)
            return false;
        else if (aY == null)
            return false;
        else if (aX.length != aY.length) {
            return false;
        }

        for (int i = 0; i < aX.length; i++) {
            if (aX[i] != aY[i]) {
                return false;
            }
        }
        return true;
    }


    /**
     * Verilen sertifika bytelarini dizindekiler ile karsilastirir ve bulduklari
     * icin true, digerleri icin false doner.
     *
     * @param aTka  Verilen sertifikalarin hangi entry'deki sertifikalar ile karsilastirilacagi
     * @param aCers Verilen sertifikalar
     * @return Bir boolean array. cers.length elemani olacak ve donulen arraydeki
     * i'ninci elemanin true olmasi, verilen arraydeki i'ninci elemanin
     * dizinde oldugu anlamina gelir. false olmasi da olmadigi anlamindadir.
     */
    public boolean[] isUserCertPresent(String aTka, byte[][] aCers) {
        boolean[] r;
        int i, j;
        Object[][] dizinden;
        byte[][] dizinCers;

        if (aCers == null) {
            return null;
        }
        r = new boolean[aCers.length];
        for (i = 0; i < r.length; i++) {
            r[i] = false;
            //Dizinden sertifikalari alalim.
        }
        dizinden = this.getAttributes(aTka, ATTR_KULLANICISERTIFIKASI);
        //Dizinde sertifika yoksa tumunu false don
        if ((dizinden == null) || (dizinden[0] == null) || (dizinden[0].length < 1)) {
            return r;
        }
        dizinCers = new byte[dizinden[0].length][];
        for (i = 0; i < dizinCers.length; i++) {
            dizinCers[i] = (byte[]) dizinden[0][i];
            //dizindeki her sertifika icin gelen sertifikalara bakalim ve
            //aynisini bulursak onu true yapalim.
        }
        for (i = 0; i < dizinCers.length; i++) {
            for (j = 0; j < aCers.length; j++) {
                if (_byteArraylarEsit(dizinCers[i], aCers[j])) {
                    r[j] = true;
                }
            }
        }
        return r;

    }


    /**
     * Verilen base64 seklindeki sertifika bytelarini dizindekiler ile karsilastirir ve bulduklari
     * icin true, digerleri icin false doner.
     *
     * @param aTka  Verilen sertifikalarin hangi entry'deki sertifikalar ile karsilastirilacagi
     * @param aCers Verilen sertifikalar base64 olarak
     * @return Bir boolean array. cers.length elemani olacak ve donulen arraydeki
     * i'ninci elemanin true olmasi, verilen arraydeki i'ninci elemanin
     * dizinde oldugu anlamina gelir. false olmasi da olmadigi anlamindadir.
     */
    public boolean[] isUserCertPresent(String aTka, String[] aCers) {
        byte[][] cerler;

        if (aCers == null) {
            return null;
        }
        cerler = new byte[aCers.length][];
        for (int i = 0; i < cerler.length; i++) {
            //cerler[i]=tr.gov.tubitak.uekae.esya.genel.encoderdecoder.Base64.Base64Decode(cers[i].getBytes());
            try {
                cerler[i] = Base64.decode(aCers[i]);
            } catch (Exception ex) {
                cerler[i] = null;
            }
        }
        return isUserCertPresent(aTka, cerler);
    }

    private static byte[] parseControls(Control[] controls) throws NamingException {

        byte[] cookie = null;

        if (controls != null) {

            for (int i = 0; i < controls.length; i++) {
                if (controls[i] instanceof PagedResultsResponseControl) {
                    PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
                    cookie = prrc.getCookie();
                }
            }
        }
        return (cookie == null) ? new byte[0] : cookie;

    }

    public List<String> getGroupsByCommonName(String name) {

        List<String> groups = new ArrayList<String>();

        Object[][] attributes = getAttributes(name, ATTR_MEMBER_OF);
        if (attributes!= null && attributes.length > 0) {
            for (Object attribute : attributes[0]) {
                groups.add(attribute.toString());
            }
        }

        return groups;
    }

    public List<String> getUsersByGroup(String group) {

        List<String> users = new ArrayList<String>();
        try {
            Enumeration search = search("distinguishedName=" + group);

            if (search != null) {
                while (search.hasMoreElements()) {
                    SearchResult searchResult = (SearchResult) search.nextElement();
                    Attribute member = searchResult.getAttributes().get("member");
                    for (int i = 0; i < member.size(); i++) {

                        users.add(member.get(i).toString());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error in SearchDirectory", e);
        }
        return users;
    }

    public String getNameByGroup(String group) {
        return getAttributeValue(group, ATTR_NAME);
    }

    public String getDescriptionByGroup(String group) {
        return getAttributeValue(group, ATTR_DESCPIPTION);
    }

    public String getEmailByGroup(String group) {
        return getAttributeValue(group, ATTR_MAIL);
    }

    private String getAttributeValue(String group, String attributeName ) {
        String result = null;
        try {
            Enumeration search = search(ATTR_DN + "=" + group);

            if (search != null && search.hasMoreElements()) {

                SearchResult searchResult = (SearchResult) search.nextElement();
                Object member = searchResult.getAttributes().get(attributeName).get();
                result = member.toString();
            }
        } catch (Exception e) {
            logger.error("Error while getting attribute " + attributeName, e);
        }
        return result;
    }

}
