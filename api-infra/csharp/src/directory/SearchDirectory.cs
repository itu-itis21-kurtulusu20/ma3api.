using System;
using System.Collections.Generic;
using System.DirectoryServices;

//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.infra.directory
{
    internal class Scope
    {
        internal static SearchScope ToScope(string aScope)
        {
            switch (aScope)
            {
                case "base":
                    return SearchScope.Base;
                case "one":
                    return SearchScope.OneLevel;
                case "sub":
                    return SearchScope.Subtree;
                default:
                    throw new Exception("Verilen scope stringi hatali (" + aScope + ")");
            }
        }
    }
    /**
     * <p>Title: MA3 Dizin islemleri</p>
     * <p>Description: MA3 PKI projesinde, tüm dizin erisimlerini içinde toplayan
     * classlar bu pakette bulunmaktadir. Arama islemleri DizinArama classi
     * kullanilarak yapilacaktir.</p>
     * <p>Copyright: Copyright (c) 2002</p>
     * <p>Company: TUBITAK/UEKAE</p>
     * @author M. Serdar SORAN
     * @version 1.0
     *
     */
    public class SearchDirectory : DirectoryBase
    {
        private readonly string _mAramaNoktasi;

        //static private SearchControls msSC = new SearchControls();
        //static private SearchControls msLocalsc = new SearchControls();
        //static private SearchControls msTeklisc = new SearchControls();
        private static SearchScope _msSc;
        private static SearchScope _msLocalsc;
        private static SearchScope _msTeklisc;

        /**
         * Baglantinin saglandigi constructor. Ayni zamanda arama noktasi(Search Base)
         * de verilmelidir.
         * @param aBB Baglanti bilgilerinin alinacagi yer
         * @param aAramaNoktasi Arama noktasi (Search Base). Arama noktasi null veya bos olmamali.
         * null veya bos olursa problem cikabilir. Hatali bir arama noktasi verilmesi
         * durumunda loga hata mesaji yaziliyor.
         * @see #isConnected()
         */
        public SearchDirectory(DirectoryInfo aBB, String aAramaNoktasi)
            : base(aBB)
        {
            _mAramaNoktasi = aAramaNoktasi;
            _msSc = SearchScope.Subtree;
            _msLocalsc = SearchScope.Base;
            _msTeklisc = SearchScope.OneLevel;

            if ((_mAramaNoktasi == null) || _mAramaNoktasi.Equals(""))
            {
                _hataBildir("VERILEN_ARAMA_NOKTASI_NULL_VEYA_BOS");
            }

            mConnection = new DirectorySearcher(new DirectoryEntry("LDAP://" + aBB.getIP() + ":" + aBB.getPort() + "/" + _mAramaNoktasi, aBB.getUserName(), aBB.getUserPassword()));

        }


        ///////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////
        // TKA verilerek yapilan aramalar
        ///////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////

        /**
         * Attribute isimlerini hem kontrol edip hem de dizin tipine gore duzenleyip
         * attribute listesini cikarir ve bir String array olarak doner.
         * @param attrsAdlari Tek bir attribute adi tek bir String olarak verilebilir. Veya bir String array'i gonderilerek birden fazla attribute adi verilebilir.
         * @return Duzenlenmis attribute listesi
         */
        private string[] _getAttributeList(object attrsAdlari)
        {
            string[] temp;

            //Ilk arrayi olusturalim...
            var adlari = attrsAdlari as string[];
            if (adlari != null)
            {
                temp = adlari;
            }
            else
            {
                var s = attrsAdlari as string;
                if (s != null)
                {
                    temp = new string[1];
                    temp[0] = s;
                }
                else
                {
                    _hataBildirCiddi("GETATTRIBUTELIST_YANLIS_ARGUMANLA_CAGRILDI"); //yanlis argumanla cagrildi
                    return null; //Buraya hic gelmemeli...
                }
            }

            //Array'in her elemanininda bir attribute ismi olmali.
            //Isimleri DogruAttrAdi fonksiyonunu kullanarak duzelt...
            var r = new string[temp.Length];
            for (int i = 0; i < temp.Length; i++)
            {
                r[i] = normalizeAttrName(temp[i]);
            }

            return r;
        }


        /**
         * Verilen TKA'nin istenen attribute degerlerini doner.
         * @param aTKA Attribute'lari istenen entry'nin TKA'si
         * @param aAttrsAdlari Istenen Attributelar. Tek bir String olabilir veya bir
         * String listesi olabilir.
         * @return Dondugu Object[][]'in her elemani, sirayla, istenen attribute'larin
         * birine karsilik gelir. Ve her eleman icinde degerleri barindiran bir
         * arraydir. Tum array'lerin indisleri 0'dan baslar. Yani donulen
         * arrayin [1][2] elemani, verilen attribute listesinin 2. elemaninda verilen
         * attribute'un 3. degeridir.
         */
        public object[][] getAttributes(string aTKA, object aAttrsAdlari)
        {
            string[] attrsList = _getAttributeList(aAttrsAdlari);
            object[][] r = new object[attrsList.Length][];

            try
            {
                mConnection.SearchRoot = new DirectoryEntry("LDAP://" + mDirectoryInfo.getIP() + ":" + mDirectoryInfo.getPort() + "/" + aTKA, mDirectoryInfo.getUserName(), mDirectoryInfo.getUserPassword());
                mConnection.PropertiesToLoad.AddRange(attrsList);
                using (SearchResultCollection searchResultCollection = mConnection.FindAll())
                {
                    if (searchResultCollection.Count != 1)
                    {
                        Console.WriteLine("Result set sayisi 1 degil!: " + searchResultCollection.Count);
                        throw new Exception("Result set sayisi 1 degil!: ");
                    }
                    for (int i = 0; i < attrsList.Length; i++)
                    {
                        ResultPropertyCollection rpc = searchResultCollection[0].Properties;

                        r[i] = new object[rpc[attrsList[i]].Count];
                        rpc[attrsList[i]].CopyTo(r[i], 0);
                    }
                }
            }
            catch (Exception ex)
            {
                _hataBildir(ex, "LDAP_HATA");
                r = null;
            }
            return r;
        }


        /**
         * Capraz sertifikalari doner. getAttrDegerlerine dogru parametreleri
         * gondererek sadece capraz sertifikalari alir ve dogru bir yapiya cast ederek
         * doner.
         * @param aTKA Capraz sertifikalarin alinacagi TKA
         * @return Dondugu byte[][]'in her elamani (byte[] tipinden olacaktir) bir sertifikanin encoded halidir.
         */
        public byte[][] getCrossCertificates(String aTKA)
        {
            object[][] okunan = (getAttributes(aTKA, ATTR_CAPRAZSERTIFIKA));

            if (okunan == null)
            {
                return null; // Bir hata dolayisiyla okunamazsa...
            }
            var cerler = okunan[0];
            if (cerler == null)
            {
                return null;
            }
            var r = new byte[cerler.Length][];
            for (int i = 0; i < cerler.Length; i++)
            {
                r[i] = (byte[])cerler[i];
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
         *
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
         * @param aAramaNoktasi Aramaya baslanacak tka
         * @param aFiltre Yukaridaki sartlarda hazirlanmis bir filtre olmalidir. Bu
         * sartlarin saglandigi varsayilir. Bu konuda herhangi bir kontrol yapilmaz.
         * @param aSC Aramayi kontrol edecek searchControls
         * @return Arama Noktasindan baslayarak, aramayi yapar, kontrollere gore gerekiyorsa loga yazilmasi gerekeni yazar, buldugu sonuclari veya (bulamama veya hata olma durumunda) null doner.
         */
        private /*Enumeration*/SearchResultCollection _filtreliAramaYap(String aAramaNoktasi, String aFiltre, /*SearchControls*/SearchScope aSC)
        {

            //aramayi yapalim
            SearchResultCollection ne = null;
            try
            {
                //mConnection.SearchRoot.Path += "/" + aAramaNoktasi;
                //mConnection.Filter = aFiltre;
                //mConnection.SearchScope = aSC;
                //String ip = "10.1.0.15";
                //int port = 389;
                //String directoryType = DirectoryBase.ACTIVE_DIRECTORY;
                //String user = "esyakm@ug.net";
                //String pass = "Ugnet.2010";

                //String searchPoint = "dc=ug, dc=net";

                //DirectoryInfo di = new StaticDirectoryInfo(ip, port, directoryType, user, pass);
                //mConnection = new DirectorySearcher(new DirectoryEntry("LDAP://10.1.0.15:389/" + aAramaNoktasi), aFiltre, new String[] { "distinguishedName" }, aSC);
                mConnection.Filter = aFiltre;
                mConnection.SearchScope = aSC;
                ne = mConnection.FindAll();
            }
            catch (Exception ex)
            {
                _hataBildir(ex, "SEARCH HATASI");
            }

            return ne;
        }


        /**
         * Default arama notasindan baslayarak, default olarak tum tree'de arama yapar.
         * @param aFiltre aramada kullanilacak filtre...
         * @return Arama Noktasindan baslayarak, aramayi yapar, kontrollere gore gerekiyorsa loga yazilmasi gerekeni yazar, buldugu sonuclari veya (bulamama veya hata olma durumunda) null doner.
         * @see #_filtreliAramaYap(String,String,SearchControls) serdar
         */
        public /*Enumeration*/SearchResultCollection search(String aFiltre)
        {
            return _filtreliAramaYap(_mAramaNoktasi, aFiltre, _msSc);
        }


        /**
         * Filtrelemede bazi karakterlerin ozel anlami var. Verilen string icinde bu
         * karakterlerin degistirilmesi gerekiyor. Bu fonksiyon, ozel anlamli 4 karakteri
         * dogru sekilde encode eder. Verilen string'de \XX seklinde bir karakter
         * olmamalidir.
         * @param aST Ozel karakterlerden arindirilacak string
         * @return Filtrede rahatlikla kullanilabilecek, icinde ozel karakter bulunmayan stringi doner.
         */
        private String _filterSafe(String aST)
        {
            //En basta bunun olmasi onemli. Cunki \ karakteri digerlerinin de icinde geciyor.
            var i = 0;
            while ((i = aST.IndexOf('\\', i)) != -1) //NOPMD
            {
                aST = aST.Substring(0, i) + "\\5c" + aST.Substring(i + 1);
                i++;
            }

            while ((i = aST.IndexOf('*')) != -1) //NOPMD
            {
                aST = aST.Substring(0, i) + "\\2a" + aST.Substring(i + 1);
            }
            while ((i = aST.IndexOf('(')) != -1) //NOPMD
            {
                aST = aST.Substring(0, i) + "\\28" + aST.Substring(i + 1);
            }
            while ((i = aST.IndexOf(')')) != -1) //NOPMD
            {
                aST = aST.Substring(0, i) + "\\29" + aST.Substring(i + 1);
            }

            return aST;
        }


        /**
         * email'i verilen bir entry'nin TKA'sini doner. Email'lerin dizinde unique
         * oldugu varsayilmaktadir. Eger ayni emailli birden fazla kullanici varsa
         * loga hatayi yazar, ilk buldugu kullanicinin TKA'sini doner. Filtre
         * belirlenirken yukaridaki grammer kulaniliyor.
         * @param aEmail Aranan email
         * @return Bu email'e sahip entry'nin TKAsi. Arama noktasinda gore goreceli
         * TKA degil de, tam TKA donulecektir.
         */
        public String getTKAbyEmail(String aEmail)
        {
            /*Enumeration*/
            using (SearchResultCollection ne = search(normalizeAttrName(ATTR_MAIL) + "=" + _filterSafe(aEmail)))
            {
                if (ne == null)
                {
                    return null; //Demek ki bir hata olusmasi sonucu bir sey bulamadi
                }
                if (ne.Count > 1)
                {
                    //email unique olmali ama eger buraya girerse bu sart directoryde saglanmiyor demekti. Loga bunu yazalim...
                    _hataBildir("BIRDEN_FAZLA_EMAIL");
                }
                if (ne.Count != 1) return null; //Buraya gelmesi durumunda bir sey bulamadi demektir.
                ResultPropertyCollection rpc = ne[0].Properties;
                String tka = normalizeAttrName(ATTR_DN);
                return (string)rpc[tka][0];

            }
        }


        /**
         * Verilen arama noktasindan itibaren tarayarak KSM ve SM olarak niteledigi
         * TKA'lari doner. "caCertificate attribute'u dolu olan her entry KSM veya
         * SM'dir" varsayimi yapilmistir. Liste, caCertificate attribute'u dolu olan
         * entry'lerin listesidir.
         * @return Dondugu array'in her elemani bir KSM veya SM'nin tam TKA'sidir.
         */
        public String[] getKSMSMList()
        {
            List<string> v = new List<string>();

            /*Enumeration*/
            SearchResultCollection ne = search(normalizeAttrName(ATTR_SMSERTIFIKASI) + "=*");
            if (ne == null)
            {
                return null; //Demek ki bir hata olusmasi sonucu bir sey bulamadi
            }

            foreach (SearchResult sr in ne)
            {
                v.Add(sr.Path);
            }

            var r = new String[v.Count];
            for (int i = v.Count - 1; i >= 0; i--)
            {
                r[i] = v[i] + "," + _mAramaNoktasi;
            }
            return r;
        }


        /**
         * TKA'si verilen entry altindaki (sadece o seviyedeki) tum entrylerin listesini
         * object classlariyla birlikte verir.
         * @param aTka Aramanin yapilacagu yer.
         * @return Donulen String[][] in ilk elemani bulunan entry'lerin tkalari, ikinci
         * elemani da entry'lerin object classlarina karsilik gelen attribute'un toString
         * fonksiyonu sonucudur.
         */
        public String[][] getEntryListesiVeObjectcls(String aTka)
        {
            List<string> v = new List<string>();
            List<string> v_objcls = new List<string>();
            /*Enumeration*/
            //SearchResultCollection ne;

            //Arama yaparak bu seviyedeki entryleri ve object classlarini alalim.
            using (SearchResultCollection ne = _filtreliAramaYap(aTka, normalizeAttrName(ATTR_OBJECTCLASS) + "=*", _msTeklisc))
            {
                //Enumeration olarak gelen degerleri vectore atalim.
                if (ne != null)
                {
                    foreach (SearchResult sr in ne)
                    {

                        v.Add(sr.Path);
                        ResultPropertyValueCollection att = sr.Properties[normalizeAttrName(ATTR_OBJECTCLASS)];
                        string tmpst;
                        if (att == null)
                        {
                            tmpst = "DUMMY";
                        }
                        else
                        {
                            tmpst = att[0] as string;
                        }
                        v_objcls.Add(tmpst);
                    }
                }
            }

            //Vectoru array'a cevirip donelim.
            var r = new string[v.Count];
            var r2 = new string[v_objcls.Count];
            for (int i = v.Count - 1; i >= 0; i--)
            {
                r[i] = v[i] + "," + aTka;
                r2[i] = v_objcls[i];
            }

            return new[] { r, r2 };
        }


        /**
         * Verilen TKA'nin Dizinde olup olmadigini kontrol eder.
         * @param aTka Aranacak TKA
         * @return Eger TKA dizinde varsa true, yoksa false doner.
         */
        public bool isTKAPresent(String aTka)
        {
            int virgulYeri = aTka.IndexOf(",", StringComparison.Ordinal);
            string ara;
            if (virgulYeri >= 0)
            {
                ara = aTka.Substring(0, virgulYeri);
            }
            else
            {
                ara = aTka;
            }
            try
            {
                //Eger search'de bir hata cikarsa veya search sonucunda hic eleman donmezse
                //exception atacaktir ve bulamadigi anlamina gelir.
                mConnection.SearchRoot.Path = aTka;
                mConnection.Filter = _filterSafe(ara);
                mConnection.SearchScope = _msLocalsc;
                using (SearchResultCollection conn= mConnection.FindAll())
                {
                    if (conn.Count == 0)
                        return false;
                }
                //mConnection.search(aTka, _filterSafe(ara), msLocalsc).hasMore();
            }
            catch (Exception ex)
            {
                _hataBildir(ex, "LDAPA_BAGLANTI_BULUNURKEN_HATA_OLUSTU");
                return false;
            }

            return true; //Bulundu
        }


        private bool _byteArraylarEsit(byte[] aX, byte[] aY)
        {
            if ((aX == null) && (aY == null))
                return true;
            if (aX == null)
                return false;
            if (aY == null)
                return false;
            if (aX.Length != aY.Length)
            {
                return false;
            }

            for (int i = 0; i < aX.Length; i++)
            {
                if (aX[i] != aY[i])
                {
                    return false;
                }
            }
            return true;
        }


        /**
         * Verilen sertifika bytelarini dizindekiler ile karsilastirir ve bulduklari
         * icin true, digerleri icin false doner.
         * @param aTka Verilen sertifikalarin hangi entry'deki sertifikalar ile karsilastirilacagi
         * @param aCers Verilen sertifikalar
         * @return Bir boolean array. cers.length elemani olacak ve donulen arraydeki
         * i'ninci elemanin true olmasi, verilen arraydeki i'ninci elemanin
         * dizinde oldugu anlamina gelir. false olmasi da olmadigi anlamindadir.
         */
        public bool[] isUserCertPresent(String aTka, byte[][] aCers)
        {
            int i;

            if (aCers == null)
            {
                return null;
            }
            var r = new bool[aCers.Length];
            for (i = 0; i < r.Length; i++)
            {
                r[i] = false;
                //Dizinden sertifikalari alalim.
            }
            var dizinden = getAttributes(aTka, ATTR_KULLANICISERTIFIKASI);
            //Dizinde sertifika yoksa tumunu false don
            if ((dizinden == null) || (dizinden[0] == null) || (dizinden[0].Length < 1))
            {
                return r;
            }
            var dizinCers = new byte[dizinden[0].Length][];
            for (i = 0; i < dizinCers.Length; i++)
            {
                dizinCers[i] = (byte[])dizinden[0][i];
                //dizindeki her sertifika icin gelen sertifikalara bakalim ve
                //aynisini bulursak onu true yapalim.
            }
            for (i = 0; i < dizinCers.Length; i++)
            {
                int j;
                for (j = 0; j < aCers.Length; j++)
                {
                    if (_byteArraylarEsit(dizinCers[i], aCers[j]))
                    {
                        r[j] = true;
                    }
                }
            }
            return r;

        }


        /**
         * Verilen base64 seklindeki sertifika bytelarini dizindekiler ile karsilastirir ve bulduklari
         * icin true, digerleri icin false doner.
         * @param aTka Verilen sertifikalarin hangi entry'deki sertifikalar ile karsilastirilacagi
         * @param aCers Verilen sertifikalar base64 olarak
         * @return Bir boolean array. cers.length elemani olacak ve donulen arraydeki
         * i'ninci elemanin true olmasi, verilen arraydeki i'ninci elemanin
         * dizinde oldugu anlamina gelir. false olmasi da olmadigi anlamindadir.
         */
        public bool[] isUserCertPresent(String aTka, String[] aCers)
        {
            if (aCers == null)
            {
                return null;
            }
            var cerler = new byte[aCers.Length][];
            for (int i = 0; i < cerler.Length; i++)
            {
                //cerler[i]=tr.gov.tubitak.uekae.esya.genel.encoderdecoder.Base64.Base64Decode(cers[i].getBytes());
                try
                {
                    cerler[i] = Convert.FromBase64String(aCers[i]);
                }
                catch (Exception)
                {
                    cerler[i] = null;
                }
            }
            return isUserCertPresent(aTka, cerler);
        }
    }


    public class LdapQueryParser
    {
        private readonly Uri _uri;
        private readonly string _attributeStrings = "*";
        private readonly string _scope = "base", _filter = "(objectClass=*)", _path;
        readonly string[] _attributes;    //_attributeStrings değerinin liste tipinde gösterimi
        public LdapQueryParser(String aQuery)
        {
            try
            {
                _uri = new Uri(aQuery);
            }
            catch (Exception ex)
            {
                throw new Exception("Verine ldap query'de formatinda hata var!", ex);
            }

            _path = _uri.OriginalString.Replace(_uri.Query, "");

            string[] queryValues = _uri.Query.Split('?');
            switch (queryValues.Length)
            {
                case 5: //extensions var!                    
                case 4: //filter var!
                    if (!queryValues[1].Equals(""))
                        _attributeStrings = queryValues[1];
                    if (!queryValues[2].Equals(""))
                        _scope = queryValues[2];
                    if (!queryValues[3].Equals(""))
                        _filter = queryValues[3];
                    break;
                case 3: //scope var!
                    if (!queryValues[1].Equals(""))
                        _attributeStrings = queryValues[1];
                    if (!queryValues[2].Equals(""))
                        _scope = queryValues[2];
                    break;
                case 2: //attributes var!
                    if (!queryValues[1].Equals(""))
                        _attributeStrings = queryValues[1];
                    break;
            }
            _attributes = _attributeStrings.Split(',');
        }
        public String getScope()
        {
            return _scope;
        }
        public String getFilter()
        {
            return _filter;
        }
        public String[] getAttributes()
        {
            return _attributes;
        }
        public Uri getPath()
        {
            return new Uri(_path);
        }
        public SearchResults run()
        {
            DirectoryEntry root = new DirectoryEntry(_path);
            DirectorySearcher searcher = new DirectorySearcher(root, _filter, _attributes, Scope.ToScope(_scope));
            SearchResultCollection results = null;
            try
            {
                results = searcher.FindAll();
            }
            catch (Exception)
            {
                root = new DirectoryEntry(_path, "", "", AuthenticationTypes.Anonymous);
                searcher = new DirectorySearcher(root, _filter, _attributes, Scope.ToScope(_scope));
                try
                {
                    results = searcher.FindAll();
                }
                catch (Exception e)
                {
                    throw new Exception("Ldap sunucusu ile baglanti kurulamadı", e);
                }

            }
            return new SearchResults(results);
        }

        public string[] Attributes
        {
            get
            {
                return _attributes;
            }
        }

    }
}
