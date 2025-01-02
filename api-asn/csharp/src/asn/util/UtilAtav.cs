using System;
using System.IO;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.asn.util
{
    public class UtilAtav
    {
        private static readonly ILog LOGCU = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        public static String atavValue2String(AttributeTypeAndValue atav)
        {
            IkiliAttributeTandV ik = atav2IkiliAttributeTandV(atav);
            return ik.stringValue();
        }

        public static String atav2String(AttributeTypeAndValue atav)
        {
            IkiliAttributeTandV ik = atav2IkiliAttributeTandV(atav);
            return ik.mst + "=" + ik.stringValue();
        }

        private static IkiliAttributeTandV atav2IkiliAttributeTandV(AttributeTypeAndValue atav)
        {
            IkiliAttributeTandV ik = null;
            try
            {
                ik = OID_tipEslestirmeleri.tipDon(atav.type);
            }
            catch (Asn1Exception ex)
            {
                //Buraya gelmesi durumunda atav icindeki oid taninmiyor demektir.
                //Bu oid'i yaklasik bir sekilde ekleyerek yeniden deneyelim.
                LOGCU.Info("Yeni bir AttributeTypeAndValue eklenecek.");
                try
                {
                    OID_tipEslestirmeleri.listeyeAtavEkle(atav);
                    ik = OID_tipEslestirmeleri.tipDon(atav.type);
                }
                catch (Asn1Exception ex1)
                {
                    throw new SystemException("AttributeTypeAndValue String'e cevrilemedi....", ex1);
                }
            }

            try
            {
                Asn1DerEncodeBuffer enbuf =
                    new Asn1DerEncodeBuffer();

                atav.value_.Encode(enbuf);
                //byte[] encoded = enbuf.getMsgCopy();
                //System.out.println(gnu.crypto.util.Util.toString(encoded));

                Asn1DerDecodeBuffer buf =
                    new Asn1DerDecodeBuffer(enbuf.GetInputStream());

                ik.mType.Decode(buf);
            }
            catch (IOException ex)
            {
                LOGCU.Error("Bir hata cikti. Attribute dogru alinamamis olabilir.", ex);
            }
            catch (Asn1ConsVioException ex)
            {
                LOGCU.Error("Constraintlerle ilgili bir hata. Attribute dogru alinamamis olabilir (kucuk ihtimal...)", ex);
            }
            catch (Asn1Exception ex)
            {
                LOGCU.Error("Bir hata cikti. Attribute dogru alinamamis olabilir.", ex);
            }
            return ik;
        }


        public static AttributeTypeAndValue string2atav(String atav, bool aTurkce)
        {
            int k = atav.IndexOf('=');
            /** @todo k==-1 ise exception at... */
            if (k == -1)
            {
                throw new Asn1Exception("Gecersiz Name formati.");
            }
            String bas = atav.Substring(0, k).Trim();
            String son = atav.Substring(k + 1).Trim();

            IkiliAttributeTandV ik = OID_tipEslestirmeleri.atavDon(bas, son, aTurkce);
            //Olusturulan atav'lardan RelativeDistinguishedName yapisini olusturup donelim.

            Asn1OpenType openT = null;
            //          Asn1DerEncodeBuffer enbuf = new Asn1DerEncodeBuffer();
            //          ik.mType.encode(enbuf);
            //          openT = new Asn1OpenType();

            try
            {
                //               openT.decode(new Asn1DerDecodeBuffer(enbuf.getInputStream()));
                openT = UtilOpenType.toOpenType(ik.mType);
            }
            catch (IOException ex)
            {
                //ex.printStackTrace();
                Console.WriteLine(ex.StackTrace);
            }

            AttributeTypeAndValue r = new AttributeTypeAndValue(ik.mOid, openT);
            return r;
        }
    }
}
