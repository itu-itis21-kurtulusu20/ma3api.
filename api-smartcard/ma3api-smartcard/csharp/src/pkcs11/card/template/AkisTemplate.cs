using System;
using System.Collections.Generic;
using System.Text;
using System.Text.RegularExpressions;
using Com.Objsys.Asn1.Runtime;
using iaik.pkcs.pkcs11.wrapper;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
using tr.gov.tubitak.uekae.esya.api.smartcard.src.winscard;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template
{
    public class AkisTemplate : CardTemplate
    {
        public static readonly List<String> ATR_HASHES = new List<String>();
        public static readonly List<String> HISTORICAL_BYTE_REGEXES = new List<String>();


        static AkisTemplate()
	    {
            ATR_HASHES.Add("3BBA11008131FE4D55454B41452056312E30AE");
            ATR_HASHES.Add("3B9F968131FE45806755454B41451112318073B3A180E9");
            ATR_HASHES.Add("3B9F968131FE45806755454B41451212318073B3A180EA");
            ATR_HASHES.Add("3B9F968131FE45806755454B41451213318073B3A180EB");
            ATR_HASHES.Add("3B9F968131FE45806755454B41451252318073B3A180AA");
            ATR_HASHES.Add("3B9F968131FE45806755454B41451253318073B3A180AB");
            ATR_HASHES.Add("3B9F158131FE45806755454B41451221318073B3A1805A");
            ATR_HASHES.Add("3B9F968131FE45806755454B41451221318073B3A180D9");
            ATR_HASHES.Add("3B9F138131FE45806755454B41451221318073B3A1805C");
            ATR_HASHES.Add("3B9F138131FE45806755454B41451261318073B3A1801C");
            ATR_HASHES.Add("3B9F158131FE45806755454B41451261318073B3A1801A");
            ATR_HASHES.Add("3B9F968131FE45806755454B41451261318073B3A18099");
            ATR_HASHES.Add("3B9F968131FE458065544320201231C073F621808105B3");
            ATR_HASHES.Add("3B9F968131FE45806755454B41451292318073B3A1806A");
            ATR_HASHES.Add("3B9F968131FE45806755454B41451293318073B3A1806B");
            ATR_HASHES.Add("3B9F968131FE45806755454B41451312318073B3A180EB");
            ATR_HASHES.Add("3B9F968131FE45806755454B414512A4318073B3A1805C");
            ATR_HASHES.Add("3B9F968131FE45806755454B414512A5318073B3A1805D");
            ATR_HASHES.Add("3B9F978131FE458065544312210031C073F62180810593");
            ATR_HASHES.Add("3B9F978131FE458065544312210731C073F62180810594");
            ATR_HASHES.Add("3B9F978131FE458065544312210731C073F62180810595");
            ATR_HASHES.Add("3B9F978131FE4580655443D2210831C073F6218081015F"); //SAHA
            ATR_HASHES.Add("3B9F978131FE458065544312210831C073F6218081019F"); //SAHA Test
            ATR_HASHES.Add("3B9F978131FE4580655443D2210831C073F6218081055B"); // 2.2.8 INF
            ATR_HASHES.Add("3B9F978131FE4580655443E4210831C073F6218081056D"); // 2.2.8 UKTUM
            ATR_HASHES.Add("3B9F968131FE4580655443D3210831C073F6218081055B"); // 2.2.8 NXP
            ATR_HASHES.Add("3B9F968131FE4580655443D2210831C073F6218081055A"); // 2.2.8 UDEA
            ATR_HASHES.Add("3B9F978131FE458065544312210831C073F6218081059B"); // 2.2.8 INF Saha Test
            ATR_HASHES.Add("3B9F978131FE458065544353228231C073F62180810553"); // 2.5.2 NXP

            HISTORICAL_BYTE_REGEXES.Add("806755454B4145....318073B3A180");
            HISTORICAL_BYTE_REGEXES.Add("80655443......31C073F6218081..");

            HISTORICAL_BYTE_REGEXES.Add("8066683209......31958105"); // V3 SAHA
            HISTORICAL_BYTE_REGEXES.Add("8066683249......31958105"); // V3 SAHA Test
        }
        /**
            * Create Akis  template
             * @param aCardType
             */
        public AkisTemplate(CardType aCardType)
            : base(aCardType)
        {
        }

        public override IPKCS11Ops getPKCS11Ops()
        {
            lock (PKCS11GetLock)
            {
                if (mIslem == null)
                    mIslem = new AkisOps(cardType);
                return mIslem;
            }
        }

        
        public override List<List<CK_ATTRIBUTE_NET>> getCertSerialNumberTemplates(byte[] aSerialNumber)
        {
            List<List<CK_ATTRIBUTE_NET>> list = base.getCertSerialNumberTemplates(aSerialNumber);
            CK_ATTRIBUTE_NET classAttr = new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_CLASS, PKCS11Constants_Fields.CKO_CERTIFICATE);
            CK_ATTRIBUTE_NET privateAttr = new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_PRIVATE, false);
            CK_ATTRIBUTE_NET tokenAttr = new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_TOKEN, true);
            BigInteger biSN = new BigInteger(aSerialNumber, 1);
            List<CK_ATTRIBUTE_NET> template1 = new List<CK_ATTRIBUTE_NET>();
            template1.Add(classAttr);
            template1.Add(privateAttr);
            template1.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SERIAL_NUMBER, biSN.ToString(16)));
            template1.Add(tokenAttr);

            List<CK_ATTRIBUTE_NET> template2 = new List<CK_ATTRIBUTE_NET>();
            template2.Add(classAttr);
            template2.Add(privateAttr);
            template2.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SERIAL_NUMBER, aSerialNumber.ToString()));
            template2.Add(tokenAttr);

            String hexSerial = new BigInteger(aSerialNumber, 1).ToString(16);
            for (int i = 0; i <= aSerialNumber.Length * 2 - hexSerial.Length; i++)
            {
                hexSerial = '0' + hexSerial;
            }

            Encoding iso_8859_1 = Encoding.GetEncoding("iso-8859-1");
            byte[] iso_8859_1_encoded_buf = Encoding.Convert(Encoding.Default, iso_8859_1, Encoding.Default.GetBytes(hexSerial.ToCharArray()));

            List<CK_ATTRIBUTE_NET> template3 = new List<CK_ATTRIBUTE_NET>();
            template3.Add(classAttr);
            template3.Add(privateAttr);
            template3.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SERIAL_NUMBER, iso_8859_1_encoded_buf));
            template3.Add(tokenAttr);

            Asn1BigInteger BI = new Asn1BigInteger(new BigInteger(aSerialNumber, 1));
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            try
            {
                BI.Encode(encBuf);
            }
            catch (Exception aEx)
            {
                throw aEx;
            }
            byte[] tlvli = encBuf.MsgCopy;

            List<CK_ATTRIBUTE_NET> template4 = new List<CK_ATTRIBUTE_NET>();
            template4.Add(classAttr);
            template4.Add(privateAttr);
            template4.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_SERIAL_NUMBER, tlvli));
            template4.Add(tokenAttr);

            list.Add(template1);
            list.Add(template2);
            list.Add(template3);
            list.Add(template4);

            return list;
        }

        public override List<CK_ATTRIBUTE_NET> getRSAPublicKeyImportTemplate(String aLabel, EPrivateKeyInfo aPrivKeyInfo, ECertificate aCer, bool aIsSign, bool aIsEncrypt)
        {
            AsymmetricKeyParameter privKey = PrivateKeyFactory.CreateKey(BouncyProviderUtil.ToBouncy(aPrivKeyInfo));

            if (privKey is RsaPrivateCrtKeyParameters)
            {
                RsaPrivateCrtKeyParameters rsaPrivCrtKey = (RsaPrivateCrtKeyParameters)privKey;
                byte[] modBytes = rsaPrivCrtKey.Modulus.ToByteArrayUnsigned();
                List<CK_ATTRIBUTE_NET> list = base.getRSAPublicKeyImportTemplate(aLabel, aPrivKeyInfo, aCer, aIsSign, aIsEncrypt);
                list.Add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_MODULUS_BITS, modBytes.Length * 8));
                return list;
            }
            else
            {
                throw new Exception("Metoda verilen ozel anahtar RsaPrivateCrtKey tipinde degil!, Tipi:  " + privKey.GetType().ToString());
            }
        }

        public static bool isAkisATR(String atrHex)
        {
            //looking ascii values of "UEKAE" or "uekae" in ATR
            String UEKAE = "55454B4145"; String uekae = "75656B6165";
            if (atrHex.Contains(UEKAE) || atrHex.Contains(uekae))
            {
                return true;
            }
           
            ATR atr = new ATR(StringUtil.ToByteArray(atrHex));
            byte[] historicalBytes = atr.getHistoricalBytes();
            String historicalBytesHex = StringUtil.ToHexString(historicalBytes);

            foreach (String regexStr in HISTORICAL_BYTE_REGEXES)
            {
                Regex regex = new Regex(regexStr);
                Match match = regex.Match(historicalBytesHex);
                if (match.Success)
                    return true;
            }

            return false;
        }

        /**
         * Returns ATR hashes of template as string
         * @return
         */
        public override String[] getATRHashes()
        {
            return ATR_HASHES.ToArray();
        }
        /**
         * Add ATR hash to  template's hashes
         * @param aATR String
         */
        public void addATR(String aATR)
        {
            ATR_HASHES.Add(aATR);
        }

    }
}
