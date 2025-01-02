using System;
using System.Collections.Generic;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.x509;
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.asn.util
{
    public class UtilName
    {
        public static string name2String(Name aName)
        {          
            RelativeDistinguishedName[] rdnArray = ((RDNSequence)(aName.GetElement())).elements;

            if ((rdnArray == null) || (rdnArray.Length == 0))
                return "";

            var i = rdnArray.Length - 1;
            var r = UtilRDN.rdn2String(rdnArray[i--]);
            for (; i >= 0; i--)
            {
                r += ",";
                r += UtilRDN.rdn2String(rdnArray[i]);
            }

            return r;            
        }

        public static String generalName2String(GeneralName aGenName)
        {
            if ((aGenName == null) || (aGenName.GetElement() == null))
                return "";

            switch (aGenName.ChoiceID)
            {
                case GeneralName._DIRECTORYNAME:
                    return name2String((Name)aGenName.GetElement());
                case GeneralName._DNSNAME:
                case GeneralName._RFC822NAME:
                case GeneralName._UNIFORMRESOURCEIDENTIFIER:
                    return ((Asn1IA5String)aGenName.GetElement()).mValue;
                case GeneralName._IPADDRESS:
                    byte[] ip = ((Asn1OctetString)aGenName.GetElement()).mValue;
                    int i;
                    string r = "";
                    if ((ip == null) || (ip.Length == 0))
                        return "";
                    r += ip[0];
                    for (i = 1; i < ip.Length; i++)
                        r += "." + ip[i];
                    return r;
                case GeneralName._OTHERNAME:
                    AnotherName othername = (AnotherName)aGenName.GetElement();
                    if (othername.type_id.mValue.SequenceEqual(_ImplicitValues.id_win_upn))
                    {
                        try
                        {
                            Asn1UTF8String s = new Asn1UTF8String();
                            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(othername.value_.mValue);
                            s.Decode(decBuf);
                            return s.mValue;
                        }
                        catch (Exception)
                        {
                            return "??";
                        }
                    }
                    return "??";
                default:
                    return "??";
            }
        }

        public static Name string2Name(String aDN, bool aTurkce)
        {
            // Verilen dn'i rdnlere bol ve her bir rdn'i vectore koy.
            // (Genelde rdn'lerimiz tek bir atav'dan olusuyor, normalde rdn=set of atav)
            List<string> rdnsVector = new List<string>();
            try
            {
                _parseEt(aDN, rdnsVector, aTurkce);
            }
            catch (Exception e)
            {
                throw new Asn1Exception(e.Message);
            }

            int len = rdnsVector.Count;
            if (len == 0)
            {
                throw new Asn1Exception("Verilen dn hatali: " + aDN);
            }

            // Vektordeki rnd'leri rdnArrayine koy.
            var rdnsArray = new RelativeDistinguishedName[len];

            for (int i = 0; i < len; i++)
            {
                // Sira onemli sanirim!
                rdnsArray[i] = UtilRDN.string2rdn(rdnsVector[i], aTurkce);
            }

            // rdnArrayinden Name yapisini olustur ve don.
            var name = new Name();
            name.Set_rdnSequence(new RDNSequence(rdnsArray));
            return name;
            // throw new Exception("string2Name implement edilmedi");
            //return new Name();
        }

        /***
      * Verilen dn'i atav'lara boler ve her bir atav'i vektore koyar. Dn'i bolmeye
      * sagdan baslar. En sagdaki virgulun sag tarafina bakar ve atav mi diye kontrol
      * eder, atavsa bu atavi vektore koyar ve sol tarafi tekrar bolunmek uzere ayni
      * metoda gonderir. atav degilse atav bulana kadar virgullerden sola dogru ilerler.
      * @param aDn atavlarina bolunup vektore konacak olan dn stringi.
      * @param aVector atavlarin konacagi vektor
      * @param aUtf8 Verilen dn'in utf8 olup olmadigi
      * @throws Exception Arguman olarak verilen string dogru bir dn degilse Exception atar. 
      */
        private static void _parseEt(String aDn, List<String> aVector, bool aUtf8)
        {
            if (aDn == null || aDn.Trim().Length == 0)
                return;

            if (aDn.Trim().IndexOf(',') == 0)
            {
                //System.out.println("\"" + aDn + "\"" + " atav degil! Tum dn hatali.");
                aVector.Clear();
                throw new Exception("\"" + aDn + "\"" + " atav degil! Tum dn hatali.");
            }

            string basKismi;

            int lastIndex = aDn.LastIndexOf(',');

            var atavAdayi = aDn.Substring(lastIndex + 1);

            if (lastIndex != -1)
                basKismi = aDn.Substring(0, lastIndex);
            else
                basKismi = null;


            while (!_atavMi(atavAdayi, aUtf8))
            {
                if (basKismi == null)
                {
                    //System.out.println("\"" + aDn + "\"" + " atav degil! Tum dn hatali.");
                    aVector.Clear();
                    throw new Exception("\"" + aDn + "\"" + " atav degil! Tum dn hatali.");
                }
                lastIndex = basKismi.LastIndexOf(',');

                atavAdayi = basKismi.Substring(lastIndex + 1) + "," + atavAdayi;
                if (lastIndex != -1)
                    basKismi = basKismi.Substring(0, lastIndex);
                else
                    basKismi = null;
            }

            //System.out.println("\natavAdayi: "+atavAdayi);
            //System.out.println("basKismi : "+basKismi);

            atavAdayi = atavAdayi.Trim();
            aVector.Add(atavAdayi);
            //System.out.println("\"" + atavAdayi + "\"" + " bir atavdir. Vektore eklendi.");
            _parseEt(basKismi, aVector, aUtf8);

        }
        private static bool _atavMi(string aStr, bool aUtf8)
        {
            int k = aStr.IndexOf('=');

            if (k == -1)
            {
                //System.out.println("\n\"" + aStr+ "\"" + " atav mi diye soruldu...");
                //System.out.println("Verilen stringde = isareti yok, dolayisiyla bu bir atav degil");
                return false;
            }

            string bas = aStr.Substring(0, k).Trim();
            string son = aStr.Substring(k + 1).Trim();

            try
            {
                OID_tipEslestirmeleri.atavDon(bas, son, aUtf8);
            }
            catch (Asn1Exception)
            {
                //System.out.println( "\"" + aStr + "\"" + " icinde \"=\" var ama bu isaretin solundaki bir attribute type degil");
                return false;
            }

            return true;
        }
        public static Name byte2Name(byte[] aName)
        {
            /*
              Asn1DerDecodeBuffer buf = new Asn1DerDecodeBuffer(aName);
              Name name = new Name();

              name.decode(buf);

              return name;
             * */
            throw new NotImplementedException();
            //return new Name();
        }

        public static byte[] name2byte(Name aName)
        {
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            aName.Encode(encBuf);
            return encBuf.MsgCopy;
        }

        public static bool isUtf8String(Name aName)
        {
            /*
                  if(aName == null)
                       return false;
                  RDNSequence rdnSeq = (RDNSequence)aName.getElement();
                  if(rdnSeq == null)
                       return false;
                  RelativeDistinguishedName[] rdns = rdnSeq.elements;
                  if(rdns == null | rdns.length == 0 || rdns[0] == null)
                       return false;

                  int i,j;
                  AttributeTypeAndValue[] atavs;
                  AttributeTypeAndValue atav;

                  for(i=0; i<rdns.length; i++)
                  {
                       atavs = rdns[i].elements;
                       if(atavs == null || atavs.length == 0)
                            continue;
                       for(j=0; j<atavs.length; j++)
                       {
                            atav = atavs[j];
                            if(atav == null || atav.value == null)
                                 continue;
                            Asn1OpenType openType = atav.value;
                            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                            Asn1DerDecodeBuffer decBuf;
                            DirectoryString dirStr = new DirectoryString();
                            try
                            {
                                 openType.encode(encBuf);
                                 decBuf = new Asn1DerDecodeBuffer(encBuf.getMsgCopy());
                                 dirStr.decode(decBuf);
                            } catch (Exception e)
                            {
                                 continue;
                            }

                            if(dirStr.getChoiceID() == DirectoryString._UTF8STRING)
                                 return true;

                       }
                  }

                  return false;*/
            throw new NotImplementedException();
            //return false;

        }
    }
}
