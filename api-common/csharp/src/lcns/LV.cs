using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Security.Cryptography;
//using System.Security.Cryptography.Pkcs;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using System.Xml;
using System.Xml.Linq;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.common.lcns;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.common.license
{
    public partial class LV
    {
        private readonly string LISANSID = "LisansID";
        private readonly string KORUMA_KODU = "KorumaKodu";

        private readonly string TARIH_BILGI = "TarihBilgi";
        private readonly string TARIH_FORMAT = "dd/MM/yyyy";
        private readonly string BASLANGIC_TARIHI = "BaslangicTarihi";
        private readonly string BITIS_TARIHI = "BitisTarihi";

        private readonly string KURUM_BILGI = "KurumBilgi";
        private readonly string KURUM_ADI = "KurumAdi";
        private readonly string YETKILI = "Yetkili";
        private readonly string EPOSTA = "EPosta";
        private readonly string TELNO = "TelNo";
        private readonly string FAKS = "Faks";
        private readonly string ADRES = "Adres";

        private readonly string URUN_BILGI = "UrunBilgi";
        private readonly string URUN_ID = "UrunID";
        private readonly string KONTROL_BILGI = "KontrolBilgi";
        private readonly string AD = "Ad";
        private readonly string DEGER = "Deger";

        private readonly string TEST = "Test";
        private readonly string DEGIL = "degil";

        private readonly string JAVA = "Java";
        private readonly string CSHARP = "CSharp";
        private readonly string VAR = "var";

        private readonly string BAKIM_SOZLESME_BITIS_TARIHI = "BakimSozlesmeBitisTarihi";
        private readonly String SERTIFIKA_ISSUER = "SertifikaIssuer";

        private readonly string ESNEK = "Esnek";

        private static LV LVInstance;

        static LV() { }

        private readonly Dictionary<int, Dictionary<string, string>> _products;

        private int mLicenseID;
        private byte[] mKorumaKodu;

        private string mKurumAdi;
        private string mYetkili;
        private string mEposta;
        private string mTelno;
        private string mAdres;
        private string mFaks;

        private DateTime? mBaslangicTarihi;
        private DateTime? mBitisTarihi;

        private byte[] mLicenseData;

        /**
         * Get urun attribute
         * @param urun one of urun from the Urunler enum
         * @param controlName the control name. For example: "IP"
         * @return
         * @throws Exception
         */
        public string getUrunAtt(int urunID, string controlName)
        {
            Dictionary<string, string> urunAttr;
            _products.TryGetValue(urunID, out urunAttr);
            if (urunAttr == null) throw new LE("The requested urun does not exist. LicenseID: " + mLicenseID);
            string urunAtt;
            urunAttr.TryGetValue(controlName, out urunAtt);
            return urunAtt;
        }
        /**
         * Return attribute in long type
         *
         * @param urunID
         * @param controlName
         * @return
         * @throws LE
         */
        public long getUrunLongAtt(int urunID, string controlName)
        {
            string attr = getUrunAtt(urunID, controlName);
            try
            {
                return long.Parse(attr);
            }
            catch (Exception)
            {
                return 0;
            }
        }

        public bool isTestLicense(Products urun)
        {
            string isTest = getUrunAtt(urun.getID(), TEST);
            return isTest != null && TEST.Equals(isTest, StringComparison.OrdinalIgnoreCase);
        }

        public void checkVersion(int urunID, string version)
        {
            string licenseVersion = getUrunAtt(urunID, TEST);
            if (string.Compare(licenseVersion.ToUpper(), version, StringComparison.Ordinal) < 0)
                throw new LE("You don't have license for version " + version + ". Your license " +
                        "valid until version " + licenseVersion + " LicenseID: " + mLicenseID);
        }


        /**
	 * throws exception if urun dates are not valid
	 * @param urun one of urun from the Urunler enum
	 * @throws ESYAException is thrown if urun license dates are not valid
	 */
        public void checkLicenceDates(Products urun)
        {
            checkLicenseDates(urun.getID());
        }

        public void checkLicenseDates(int urunID)
        {
            DateTime? today = DateTime.Now;
            DateTime? startDate = getLicenseStartDate(urunID);
            DateTime? expirationDate = getLicenseExpirationDate(urunID);
            if (!(today > startDate && today < expirationDate))
                throw new LE("License dates are not valid. LicenseID: " + mLicenseID);
        }

        public void checkIsItJava(int urunID)
        {
            string java = getUrunAtt(urunID, JAVA);
            if (java != null && !java.Equals(VAR, StringComparison.OrdinalIgnoreCase))
                throw new LE("You don't have MA3 API java license. LicenseID: " + mLicenseID);
        }

        public void checkIsItCsharp(int urunID)
        {
            string csharp = getUrunAtt(urunID, CSHARP);
            if (csharp != null && !csharp.Equals(VAR, StringComparison.OrdinalIgnoreCase))
                throw new LE("You don't have MA3 API CSHARP license. LicenseID: " + mLicenseID);
        }

        public int getLicenseID()
        {
            return mLicenseID;
        }

        public string getKurumAdi()
        {
            return mKurumAdi;
        }

        public string getYetkili()
        {
            return mYetkili;
        }

        public string getEposta()
        {
            return mEposta;
        }

        public string getTelno()
        {
            return mTelno;
        }

        public string getAdres()
        {
            return mAdres;
        }

        public string getFaks()
        {
            return mFaks;
        }

        public DateTime? getLicenseStartDate(int urunID)
        {
            Dictionary<string, string> urunAttr;
            _products.TryGetValue(urunID, out urunAttr);
            if (urunAttr == null)
                return mBaslangicTarihi;

            string urunSpecificDate;
            urunAttr.TryGetValue(BASLANGIC_TARIHI, out urunSpecificDate);
            //DateTime urunSpecific;
            if (urunSpecificDate != null)
            {
                try
                {
                    return DateTime.ParseExact(urunSpecificDate, TARIH_FORMAT, CultureInfo.InvariantCulture);

                }
                catch (Exception)
                {
                    throw new LE("Can not parse License Date. LicenseID: " + mLicenseID);
                }
            }
            else
                return mBaslangicTarihi;
        }

        public DateTime? getLicenseExpirationDate(int urunID)
        {
            Dictionary<string, string> urunAttr;
            _products.TryGetValue(urunID, out urunAttr);
            if (urunAttr == null)
                return mBitisTarihi;

            string urunSpecificDate;
            urunAttr.TryGetValue(BITIS_TARIHI, out urunSpecificDate);
            if (urunSpecificDate != null)
            {
                //SimpleDateFormat format = new SimpleDateFormat(TARIH_FORMAT);
                try
                {
                    //return format.parse(urunSpecificDate);
                    return DateTime.ParseExact(urunSpecificDate, TARIH_FORMAT, CultureInfo.InvariantCulture);
                }
                catch (Exception)
                {
                    throw new LE("Can not parse License Date. LicenseID: " + mLicenseID);
                }
            }
            else
                return mBitisTarihi;
        }

        public String getCertificateIssuer(Products urun) 
        {

            String issuerNameStr;
            try 
            {
                issuerNameStr = getUrunAtt(urun.getID(), SERTIFIKA_ISSUER);

            } catch (Exception e) {
                throw new LE("Lisans dosyasında sertifika issuer alanı bulunamadı. Lisans dosyasını yenileyiniz! LicenseID: " + mLicenseID, e);
            }

        return issuerNameStr;
    }

        /**
         * Get urun attribute
         *
         * @param urun         one of urun from the Urunler enum
         * @param controlName  the control name. For example: "IP"
         * @param defaultValue the default value in case the corresponding key does not exist or is not paired with a value
         */
        public string getUrunAtt(Products urun, string controlName, string defaultValue)
        {
            Dictionary<string, string> urunAttr;
            _products.TryGetValue(urun.getID(), out urunAttr);
            if (urunAttr != null)
            {
                string att;
                urunAttr.TryGetValue(controlName, out att);

                if (att != null)
                {
                    return att;
                }
            }
            return defaultValue;
        }

        public bool isPermissiveTimeStamp()
        {
            try
            {
                // default: not permissive
                String urunAtt = getUrunAtt(Products.ZD_ISTEMCI, ESNEK, DEGIL);

                if (urunAtt.Equals(ESNEK, StringComparison.OrdinalIgnoreCase))
                {
                    return true;
                }
                else if (urunAtt.Equals(DEGIL))
                {
                    return false;
                }
            }
            catch (Exception e)
            {
                throw new LE(Resource.message(Resource.MESAJ_ESNEKZDYOK) + " LicenseID: " + mLicenseID, e);
            }

            // default: not permissive
            return false;
        }

        public static LV getInstance()
        {
            if(LVInstance == null)
                LVInstance = new LV();
            return LVInstance;
        }

        public static void reset()
        {
            LVInstance = null;
        }

        private void checkReleaseDate() 
	    {
		    Bld build = new Bld();
		    string releaseDateStr = build.gD();
		    string bakimBitisTarihiStr;
		    try
		    {
                bakimBitisTarihiStr = getUrunAtt(Products.ORTAK.getID(), BAKIM_SOZLESME_BITIS_TARIHI);
		    }
		    catch(Exception)
		    {
			    throw new LE("Lisans dosyasında bakım sözleşme tarihi bulunamadı. Lisans dosyasını yenileyiniz! LicenseID: " + mLicenseID);
		    }
		try
		    {
                DateTime bakimBitisTarihi = DateTime.ParseExact(bakimBitisTarihiStr, TARIH_FORMAT, CultureInfo.InvariantCulture);
                DateTime releaseDate = DateTime.ParseExact(releaseDateStr, "yyyyMMdd-HHmm", CultureInfo.InvariantCulture);
			
			    if(bakimBitisTarihi < releaseDate)
				    throw new LE("Bakim sözleşmeniz sonlanmıştır. LicenseID: " + mLicenseID);
		    }
		    catch (FormatException) 
		    {
			    throw new LE("Bakım sözleşme tarihini tarih formatına dönüştürmede hata. LicenseID: " + mLicenseID);
		    }   
	    }


        private  void checkKorumaKodu()
        {
            if (mKorumaKodu == null)
                return;
           
            string password = null;
            try
            {
                Type t = Type.GetType("tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil");
                if (t != null)
                {
                    MethodInfo method = t.GetMethod("getPassword", BindingFlags.Static | BindingFlags.Public);
                    password = (string)method.Invoke(null, null);
                }
            }
            catch (Exception)
            {
                throw new LE("Lisans parolanız alınamadı. LicenseID: " + mLicenseID);
            }
           
            if(password == null)
                throw new LE("Lisans parolanız ayarlanmamış. Lisansla birlikte lisans parolanızı da veriniz. LicenseID: " + mLicenseID);

            int decryptedLisansID;
            try
            {
                byte[] salt = Encoding.ASCII.GetBytes("87654321");
                byte[] iv = Encoding.ASCII.GetBytes("12345678");

                Rfc2898DeriveBytes rfc2898 = new Rfc2898DeriveBytes(password, salt, 100);
                byte[] key = rfc2898.GetBytes(24);


                TripleDES des3 = new TripleDESCryptoServiceProvider();
                des3.Mode = CipherMode.CBC;
                des3.Padding = PaddingMode.PKCS7;
                ICryptoTransform des3CryptoTransform = des3.CreateDecryptor(key, iv);

                MemoryStream memStreamDecryptedData = new MemoryStream();

                CryptoStream decStream = new CryptoStream(memStreamDecryptedData,
                                                        des3CryptoTransform, CryptoStreamMode.Write);

                decStream.Write(mKorumaKodu, 0, mKorumaKodu.Length);

                decStream.FlushFinalBlock();
                decStream.Close();
                
                // Send the data back.
                byte [] decrypedData = memStreamDecryptedData.ToArray();

                decryptedLisansID = int.Parse(Encoding.ASCII.GetString(decrypedData));
            }
            catch (Exception aEx)
            {
                throw new LE("Koruma kodu çözmede hata. LicenseID: " + mLicenseID, aEx);
            }

            if (decryptedLisansID != mLicenseID)
                throw new LE("Koruma kodunuz çözülemedi. Lisans parolanız hatalı olabilir. LicenseID: " + mLicenseID);
	
        }

        private LV()
        {
            bool valid = validateSignature();
            if (valid == false)
                throw new LE("Signature not valid. LicenseID: " + mLicenseID);
            try
            {
                MemoryStream bs = new MemoryStream(mLicenseData);
                bs.Position = 0;

                XmlReader reader = XmlReader.Create(bs);

                var doc = XDocument.Load(reader);

                mLicenseData = null;
                IEnumerable<XElement> mainNodes = doc.Elements().Elements();
                _products = new Dictionary<int, Dictionary<string, string>>();

                var xElements = mainNodes as IList<XElement> ?? mainNodes.ToList();
                loadCommonAttributes(xElements);
                loadUrunAttributes(xElements);

                checkKorumaKodu();
                checkReleaseDate();
                Dictionary<int, Dictionary<string, string>>.KeyCollection licensedUruns = _products.Keys;
                foreach (int urunID in licensedUruns)
                {
                    checkLicenseDates(urunID);
                    checkIsItCsharp(urunID);
                }
            }
            catch (LE lex)
            {
                throw lex;
            }
            catch (Exception e)
            {
                throw new LE("Can not parse license xml. LicenseID: " + mLicenseID, e);
            }
       }

        private void loadCommonAttributes(/*NodeList*/IList<XElement> mainNodes)
        {
            //LisansID tag
            mLicenseID = int.Parse(readNode(LISANSID, mainNodes));
            string korumaKoduStr = readNode(KORUMA_KODU, mainNodes);
            if (korumaKoduStr != null)
                mKorumaKodu = Convert.FromBase64String(korumaKoduStr);

            //TarihBilgi tag
            /*NodeList*/
            IEnumerable<XElement> tarih = getNodeList(TARIH_BILGI, mainNodes).Elements();
            //SimpleDateFormat format = new SimpleDateFormat(TARIH_FORMAT);
            DateTime baslangic, bitis;

            var xElements = tarih as IList<XElement> ?? tarih.ToList();
            string baslangicTarihiStr = readNode(BASLANGIC_TARIHI, xElements);
            DateTime.TryParseExact(baslangicTarihiStr, TARIH_FORMAT, CultureInfo.InvariantCulture, DateTimeStyles.None, out baslangic);
            mBaslangicTarihi = baslangic;

            string bitisTarihiStr = readNode(BITIS_TARIHI, xElements);
            DateTime.TryParseExact(bitisTarihiStr, TARIH_FORMAT, CultureInfo.InvariantCulture, DateTimeStyles.None, out bitis);
            mBitisTarihi = bitis;


            //KurumBilgi tag
            /*NodeList*/
            IEnumerable<XElement> kurumBilgi = getNodeList(KURUM_BILGI, mainNodes).Elements();
            var bilgi = kurumBilgi as IList<XElement> ?? kurumBilgi.ToList();
            mKurumAdi = readNode(KURUM_ADI, bilgi);
            mYetkili = readNode(YETKILI, bilgi);
            mEposta = readNode(EPOSTA, bilgi);
            mTelno = readNode(TELNO, bilgi);
            mFaks = readNode(FAKS, bilgi);
            mAdres = readNode(ADRES, bilgi);
        }

        private void loadUrunAttributes(/*NodeList*/IEnumerable<XElement> mainNodes)
        {
            IEnumerable<XElement>[] urunNodes = getAllNodeList(URUN_BILGI, mainNodes);
            foreach (IEnumerable<XElement> urun in urunNodes)
            {
                Dictionary<string, string> urunAttributes = new Dictionary<string, string>();
                var xElements = urun as IList<XElement> ?? urun.ToList();
                int urunID = int.Parse(readNode(URUN_ID, xElements));

                _products[urunID] = urunAttributes;

                IEnumerable<XElement>[] attributes = getAllNodeList(KONTROL_BILGI, xElements);
                foreach (IEnumerable<XElement> att in attributes)
                {
                    var attX = att as IList<XElement> ?? att.ToList();
                    string ad = readNode(AD, attX);
                    string deger = readNode(DEGER, attX);
                    urunAttributes[ad]= deger;
                }
            }
        }

        private /*NodeList*/IEnumerable<XElement>[] getAllNodeList(string tagName, /*NodeList*/IEnumerable<XElement> nodes)
        {
            List</*NodeList*/IEnumerable<XElement>> list = new List</*NodeList*/IEnumerable<XElement>>();
            foreach (XElement node in nodes)
            {
                if (node.NodeType == XmlNodeType.Element)
                {
                    if (node.Name.LocalName.Equals(tagName) && node.HasElements)
                        list.Add(node.Elements());
                }
            }
            return list.ToArray();
        }

        private /*NodeList*//*IEnumerable<XElement>*/XElement getNodeList(string tagName, /*NodeList*/IEnumerable<XElement> aNodes)
        {
            foreach (XElement node in aNodes)
            {
                if (node.NodeType != XmlNodeType.Element) continue;
                if (node.Name.LocalName.Equals(tagName) /*&& node.HasElements*/)
                    return node;//node.Elements();
            }
            return null;
        }

        private string readNode(string tagName, /*NodeList*/IEnumerable<XElement> nodes)
        {
            XElement element = getNodeList(tagName, nodes);
            if (element != null)
                return element.Value;
            return null;
        }

        

        private bool validateSignature()
        {
            string mLicenseCertStr = /*"-----BEGIN CERTIFICATE-----\n" +*/
                "MIIE6jCCAtKgAwIBAgIBLDANBgkqhkiG9w0BAQsFADAhMR8wHQYDVQQDDBZNQTMg" +
                "TGlzYW5zIFN1bnVjdXN1IDAxMB4XDTEwMDMxODExMzAxNloXDTMwMDMxODExMzAx" +
                "NlowITEfMB0GA1UEAwwWTUEzIExpc2FucyBTdW51Y3VzdSAwMTCCAiIwDQYJKoZI" +
                "hvcNAQEBBQADggIPADCCAgoCggIBAJ6ySMBTleYdNXt4kVPJEOWdWhDPXLPzrmNq" +
                "L/76mR1HzlSsV1NczcE+yuwcUN7lgnh24VhnSRq5mt0nr65GHHbHzILfrt600Obd" +
                "9NlhO+m71lsuMCIxmak6YPb7liQPlXbBAtKV3ZKQdsZ1tbeWm91xZOJWBGHp8SnG" +
                "Q2S7/OsYLpdWRo0m35XyiJeomEDonYTpM+lniws2AQQ4PTfyPoBSXkamQq8CE2ef" +
                "dcraCp3jEZMQ9U1BWqhuZ2s+INDHFFxMwf6a8EfKPgQi//g30DZq00yznE3Hm8Gz" +
                "t24qGnRoQxXyrbWYPxb11lAUDQ65jcv2qpUc3oqLsCWcMnmpgBQ/JZTdcg1QdovM" +
                "Jkiywqrx7ilGPXGh9m14+a8EOq9umdB12LqW1jXstdMYG4y/yBcZoy2tgshJ90ga" +
                "rz3sDOcDJCq5oetUI82YcteiG3MZC+zQPJti7sR4mYOi0oq66ohVyDXiOHfVjSvp" +
                "E4q0oWmvhbzyjep5ga480eEChTH2dDDfl2RaNuRTIf1ume4e1IESy9UbsxveG58c" +
                "QWskXfLOMyAvtLEsC4ck54tBrHEkB411UVx6+8AzNMN//DPE/bYrd5zo3UljklD6" +
                "svoMlDCnZ4ji2FiRVUheTD8am1COredm6lrz8TZk+LS/5SR6n13fXQ1e6UitzzkB" +
                "8R8vcSqNAgMBAAGjLTArMA4GA1UdDwEB/wQEAwIGwDAZBgNVHREEEjAQgQ5saXNh" +
                "bnNAbWEzLm5ldDANBgkqhkiG9w0BAQsFAAOCAgEAAWIBI3nx4TG757HS7tF2+8ZP" +
                "HFNvtRplUn2RlRUNK/HUBaPwasMYI+c+pcgZ+TjwMGSqUSQkyeuNWVQl9rJ7YIj7" +
                "Nwn2/1tbonaoLkRjam+31S8hF3kemmHqjeRDn0D7rGpXM+3ltptGiGFYAGw0T8uy" +
                "LrjrYQb1OlTuYjlHClp66wbn7aFZum/G9PqXM2MMxmRKC513nHVzCDVdWdXgMm+4" +
                "Gez7WggN0Lfj6FuIyxeMKaLTRkcZ6bsKu5sg226CUpZLULm0jiLAMyMH3LKEHxtn" +
                "AFYhm2701O5L6e6MzrXUFEMgGFH196liCfBr0l0qQ9GuSQB9L/7CxZ7mOa14th54" +
                "jLADIIRI95zTTrz2ic7NDCiZD71HXwPdfgoNsGauZc6DVKaTNR6uhRoHiadJCaMY" +
                "Qyaik5TMnXOKil3z6A1tci8JJcOVhzoC9e5ryZA/3mxkx16+UsrBQCGE3zl4GQ+h" +
                "HvlU2ROu0d+EFIeTXpq7qctNb3GGeaW2HCuzR5BvXYUdKzvpM48QdWKWwJsAf8T9" +
                "ewrj07QY9wG/wtrd38n2FdaARH8lAVCIyRPWCrxt6W6DdpanHJ0kOusZTgq02VLg" +
                "ZpN3N063ZCd4g1G/lL9BniiTduj8JZmBWlhA1tA3Sr/WnHpww9Xy+uyX7LsDVjKU" +
                "f+ew1LMAC62qHPSwxEE="/* +
                "\n-----END CERTIFICATE-----"*/;


            try
            {
                byte[] fileContent = null;

                Type t = Type.GetType("tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil");
                if (t != null)
                {
                    MethodInfo method = t.GetMethod("getLicense", BindingFlags.Static | BindingFlags.Public);
                    fileContent = (byte[]) method.Invoke(null, null);
                }

                if (fileContent == null)
                {
                    string license = Environment.GetEnvironmentVariable("ma3License");
                    if (license != null)
                        fileContent = Convert.FromBase64String(license); //StringUtil.toByteArray(license);
                }

                if (fileContent == null)
                {
                    string filePath = Environment.GetEnvironmentVariable("ma3LicenseFilePath") ?? "lisans/lisans.xml";
                    fileContent = File.ReadAllBytes(filePath);
                }


                try
                {
                    extractLicenseIDFromContent(fileContent);
                }
                catch (Exception ex)
                {
                    throw new LE("Can not get License ID.", ex);
                }

                try
                {
                    return validateSignature(fileContent, mLicenseCertStr);
                }
                catch (LE ex)
                {
                    throw ex;
                }
                catch (Exception e)
                {
                    throw new LE(e.Message + " LicenseID: " + mLicenseID, e);
                }
            }
            catch (LE ex)
            {
                throw ex;
            }
            catch (Exception e)
            {
                throw new LE("Can not decode license. LicenseID: " + mLicenseID, e);              

            }
            return true;
        }

        private bool validateSignature(byte[] fileContent, string mLicenseCertStr)
        {
            try
            {
                X509Certificate2 userCertFromSignature = new X509Certificate2(Convert.FromBase64String(mLicenseCertStr));

                PKCS7 p = new PKCS7(fileContent);
                SignerInfo signerInfo = p.getFirstSignerInfo();

                bool verifyResult = signerInfo.verify(userCertFromSignature);

                if (verifyResult = true)
                    mLicenseData = p.getContent();
                
                return verifyResult;
            }
            catch (Exception e)
            {
                throw new LE("Failed to verify license signature. The content of the license may have been changed. LicenseID: " + mLicenseID, e);
            }
        }

        private void extractLicenseIDFromContent(byte[] fileContent)
        {
            int startIndex = ByteUtil.findIndex(fileContent, 0, Encoding.ASCII.GetBytes("<LisansID>")) + "<LisansID>".Length;
            int endIndex = ByteUtil.findIndex(fileContent, startIndex, Encoding.ASCII.GetBytes("</LisansID>"));

            String mLicenseIDStr = Encoding.ASCII.GetString(fileContent, startIndex, endIndex - startIndex).Trim();

            mLicenseID = Int32.Parse(mLicenseIDStr);

        }

    }
}
