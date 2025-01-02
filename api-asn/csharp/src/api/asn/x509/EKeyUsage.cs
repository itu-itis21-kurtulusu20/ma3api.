using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EKeyUsage : BaseASNWrapper<KeyUsage>, ExtensionType
    {
        public EKeyUsage(KeyUsage aObject)
            : base(aObject)
        {
        }
        public EKeyUsage(bool[] keyu)
            : base(new KeyUsage())
        {
            //sondaki false degerleri atalim...
            int i = keyu.Length - 1;
            bool[] keyu2;
            while ((i > 0) && (!keyu[i])) i--;
            keyu2 = new bool[i + 1];
            while (i >= 0) keyu2[i] = keyu[i--];

            mObject = new KeyUsage(keyu2);
        }

        public bool isDigitalSignature() { return mObject.Get(KeyUsage.digitalSignature); }
        public bool isNonRepudiation() { return mObject.Get(KeyUsage.nonRepudiation); }
        public bool isKeyEncipherment() { return mObject.Get(KeyUsage.keyEncipherment); }
        public bool isDataEncipherment() { return mObject.Get(KeyUsage.dataEncipherment); }
        public bool isKeyAgreement() { return mObject.Get(KeyUsage.keyAgreement); }
        public bool isKeyCertSign() { return mObject.Get(KeyUsage.keyCertSign); }
        public bool isCRLSign() { return mObject.Get(KeyUsage.cRLSign); }
        public bool isEncipherOnly() { return mObject.Get(KeyUsage.encipherOnly); }
        public bool isDecipherOnly() { return mObject.Get(KeyUsage.decipherOnly); }

        public String getBitString()
        {
            return mObject.ToString();
        }

        public override String ToString()
        {
            String keyUsage = " ";
            for (int i = 0; i < Math.Min(mObject.numbits, 9); i++)
            {
                if (mObject.Get(i))
                {
                    keyUsage = keyUsage + _keyUsageBul(i) + " ";
                }
            }
            return keyUsage;
        }

        private String _keyUsageBul(int aBit)
        {
            String result = "";
            switch (aBit)
            {
                case 0:
                    //result = CertI18n.mesaj(CertI18n.DIGITALSIGNATURE);
                    result = Resource.message(Resource.DIGITALSIGNATURE);
                    break; //"Digital Signature";
                case 1:
                    //result = CertI18n.mesaj(CertI18n.NONREPUDIATION);
                    result = Resource.message(Resource.NONREPUDIATION);
                    break; //"Non-Repudiation";
                case 2:
                    //result = CertI18n.mesaj(CertI18n.KEYENCIPHERMENT);
                    result = Resource.message(Resource.KEYENCIPHERMENT);
                    break; //"Key Encipherment";
                case 3:
                    //result = CertI18n.mesaj(CertI18n.DATAENCIPHERMENT);
                    result = Resource.message(Resource.DATAENCIPHERMENT);
                    break; //"Data Encipherment";
                case 4:
                    //result = CertI18n.mesaj(CertI18n.KEYAGREEMENT);
                    result = Resource.message(Resource.KEYAGREEMENT);
                    break; //"Key Agreement";
                case 5:
                    //result = CertI18n.mesaj(CertI18n.CERTSIGN);
                    result = Resource.message(Resource.CERTSIGN);
                    break; //"Certificate Signing";
                case 6:
                    //result = CertI18n.mesaj(CertI18n.CRLSIGN);
                    result = Resource.message(Resource.CRLSIGN);
                    break; //CRL Signing";
                case 7:
                    //result = CertI18n.mesaj(CertI18n.ENCIPHERONLY);
                    result = Resource.message(Resource.ENCIPHERONLY);
                    break; //"Encipher Only";
                case 8:
                    //result = CertI18n.mesaj(CertI18n.DECIPHERONLY);
                    result = Resource.message(Resource.DECIPHERONLY);
                    break; //"Decipher Only";
                default:
                    result = "";
                    break;
            }
            return result + "\n";
        }

        public EExtension toExtension(bool aCritic)
        {
            return new EExtension(EExtensions.oid_ce_keyUsage, aCritic, getBytes());
        }

        public Asn1OpenType toOpenType()
        {
            return new Asn1OpenType(getBytes());
        }

    }
}
