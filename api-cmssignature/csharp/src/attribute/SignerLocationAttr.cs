using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
     * The signer-location attribute specifies a mnemonic for an address associated with the signer at a particular
     * geographical (e.g. city) location. The mnemonic is registered in the country in which the signer is located and is used in
     * the provision of the Public Telegram Service (according to ITU-T Recommendation F.1 [11]).
     * 
     * (etsi 101733v010801 5.11.2)
     * @author aslihan.kubilay
     *
     */
    public class SignerLocationAttr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_signerLocation;
        protected String mCountry;
        protected String mLocality;
        protected String[] mPostal;

        /**
            * Create SigningTimeAttr with signing time
             * @param aCountry String country name
             * @param aLocality String
             * @param aPostal String[] Postal code
             * @throws NullParameterException
             */
        public SignerLocationAttr(String aCountry, String aLocality, String[] aPostal)
            : base()
        {
            mCountry = aCountry;
            mLocality = aLocality;
            mPostal = aPostal;
            if (mCountry == null && mLocality == null && mPostal == null)
            {
                throw new CMSSignatureException("All parameters for signerlocation attribute is null,one of them must be set");
            }
        }

        /**
         * Set signer location
         */
        public override void setValue()
        {
            _setValue(_signerLocationOlustur(mCountry, mLocality, mPostal));
        }

        //creates signerlocation attribute 
        private SignerLocation _signerLocationOlustur(String aUlke, String aSehir, String[] aPostaAdresi)
        {
            DirectoryString ulke = null;
            DirectoryString sehir = null;
            PostalAddress posta = null;

            if (aUlke != null)
            {
                ulke = new DirectoryString();
                Asn1UTF8String ulkeadi = new Asn1UTF8String(aUlke);
                ulke.Set_utf8String(ulkeadi);
            }

            if (aSehir != null)
            {
                sehir = new DirectoryString();
                Asn1UTF8String sehiradi = new Asn1UTF8String(aSehir);
                sehir.Set_utf8String(sehiradi);
            }

            if (aPostaAdresi != null && aPostaAdresi.Length > 0)
            {
                DirectoryString[] adresparcalari = new DirectoryString[aPostaAdresi.Length];
                int length = aPostaAdresi.Length;
                length = length > 6 ? 6 : length;
                for (int i = 0; i < length; i++)
                {
                    DirectoryString parca = new DirectoryString();
                    Asn1UTF8String adr = new Asn1UTF8String(aPostaAdresi[i]);
                    parca.Set_utf8String(adr);
                    adresparcalari[i] = parca;
                }
                posta = new PostalAddress(adresparcalari);
            }

            SignerLocation sl = new SignerLocation(ulke, sehir, posta);
            return sl;
        }

        /**
        * Checks whether attribute is signed or not.
        * @return True 
        */  
        public override bool isSigned()
        {
            return true;
        }

        /**
         * Returns AttributeOID of signer location attribute
         * @return
         */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }
        /**
         * Returns  signer location
         * @param aAttribute EAttribute
         * @return
         * @throws ESYAException
         */
        public static ESignerLocation toSignerLocation(EAttribute aAttribute)
        {
            return new ESignerLocation(aAttribute.getValue(0));
        }

    }
}
