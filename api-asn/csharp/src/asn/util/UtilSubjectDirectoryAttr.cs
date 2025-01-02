using System;
using System.Collections;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.PKIXqualified;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Attribute = tr.gov.tubitak.uekae.esya.asn.x509.Attribute;
namespace tr.gov.tubitak.uekae.esya.asn.util
{
    public class UtilSubjectDirectoryAttr
    {
        public static readonly Asn1ObjectIdentifier oid_pda_countryOfCitizenship = new Asn1ObjectIdentifier(_PKIXqualifiedValues.id_pda_countryOfCitizenship);
        public static readonly Asn1ObjectIdentifier oid_pda_countryOfResidence = new Asn1ObjectIdentifier(_PKIXqualifiedValues.id_pda_countryOfResidence);
        public static readonly Asn1ObjectIdentifier oid_pda_dateOfBirth = new Asn1ObjectIdentifier(_PKIXqualifiedValues.id_pda_dateOfBirth);
        public static readonly Asn1ObjectIdentifier oid_pda_gender = new Asn1ObjectIdentifier(_PKIXqualifiedValues.id_pda_gender);
        public static readonly Asn1ObjectIdentifier oid_pda_placeOfBirth = new Asn1ObjectIdentifier(_PKIXqualifiedValues.id_pda_placeOfBirth);
        //bu eslestirmede etsi 101862 kullanilmistir.
        public static readonly Asn1ObjectIdentifier oid_at_role = new Asn1ObjectIdentifier(_ImplicitValues.id_at_role);
        //bu eslestirme Windows smartcard logon icin yazilmis bir yazidan alinmistir.
        public static readonly Asn1ObjectIdentifier oid_win_upn = new Asn1ObjectIdentifier(_ImplicitValues.id_win_upn);

        static DirAttrHash hash = new DirAttrHash();

        private static readonly ILog LOGCU = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        static UtilSubjectDirectoryAttr()
        {
            //          try
            //          {
            //               //Bu eslestirmeler yapilirken RFC3739 kullanilmistir.
            //               hash.put(oid_pda_countryOfCitizenship, Class.forName("com.objsys.asn1j.runtime.Asn1PrintableString"));
            //               hash.put(oid_pda_countryOfResidence, Class.forName("com.objsys.asn1j.runtime.Asn1PrintableString"));
            //               hash.put(oid_pda_dateOfBirth, Class.forName("com.objsys.asn1j.runtime.Asn1GeneralizedTime"));
            //               hash.put(oid_pda_gender, Class.forName("com.objsys.asn1j.runtime.Asn1PrintableString"));
            //               hash.put(oid_pda_placeOfBirth, Class.forName("tr.gov.tubitak.uekae.esya.asn.x509.DirectoryString"));
            //               //bu eslestirmede etsi 101862 kullanilmistir.
            ////               hash.put(oid_at_role, Class.forName("tr.gov.tubitak.uekae.esya.genel.veritabani.DBObjects.Rol"));
            //          } catch (ClassNotFoundException ex)
            //          {
            //               LOGCU.fatal("Subject Directory Attribuet tiplerinden biri icin class bulunamadi", ex);
            //               ex.printStackTrace();
            //          }

            //Bu eslestirmeler yapilirken RFC3739 kullanilmistir.
            hash.put(oid_pda_countryOfCitizenship, typeof(Asn1PrintableString));
            hash.put(oid_pda_countryOfResidence, typeof(Asn1PrintableString));
            hash.put(oid_pda_dateOfBirth, typeof(Asn1GeneralizedTime));
            hash.put(oid_pda_gender, typeof(Asn1PrintableString));
            hash.put(oid_pda_placeOfBirth, typeof(DirectoryString));
            //bu eslestirmede etsi 101862 kullanilmistir.
            //          hash.put(oid_at_role, tr.gov.tubitak.uekae.esya.genel.veritabani.DBObjects.Rol.class);

        }


        //private static Asn1DerEncodeBuffer safeEncBuf = new Asn1DerEncodeBuffer();

        public static EAttribute attributeasPrintableFromString(Asn1ObjectIdentifier oid, String st)
        {
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            Asn1OpenType[] tempOpen = new Asn1OpenType[1];

            Asn1PrintableString val = new Asn1PrintableString(st);

            try
            {
                val.Encode(encBuf);
            }
            catch (Exception ex1)
            {
                LOGCU.Error("Buraya hic gelmemeli", ex1);
            }
            tempOpen[0] = new Asn1OpenType(encBuf.MsgCopy);
            // _SetOfAttributeValue temp = new _SetOfAttributeValue(tempOpen);
            return new EAttribute(oid, tempOpen);

        }

        public static EAttribute countryOfCitizenFromString(String c)
        {
            if (c == null || c.Length != 2)
            {
                LOGCU.Error("C=" + c + " iken bu fonksiyon cagrilamaz.");
                return null;
            }
            return attributeasPrintableFromString(oid_pda_countryOfCitizenship, c);
        }


        public static EAttribute countryOfResidenceFromString(String c)
        {
            if (c == null || c.Length != 2)
            {
                LOGCU.Error("C=" + c + " iken bu fonksiyon cagrilamaz.");
                return null;
            }
            return attributeasPrintableFromString(oid_pda_countryOfResidence, c);
        }


        public static EAttribute dateOfBirthFromDate(DateTime d)
        {
            Asn1OpenType[] tempOpen = new Asn1OpenType[1];
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();

            Asn1GeneralizedTime val = new Asn1GeneralizedTime();
            //Calendar cl = Calendar.getInstance();
            //cl.setTime(d);
            try
            {
                val.SetTime(d);
            }
            catch (Asn1Exception ex)
            {
                LOGCU.Error("Tarih=" + d + " iken bu fonksiyon cagrilamiyor.", ex);
                return null;
            }

            try
            {
                val.Encode(encBuf);
            }
            catch (Exception ex1)
            {
                LOGCU.Error("Buraya hic gelmemeli", ex1);
            }
            tempOpen[0] = new Asn1OpenType(encBuf.MsgCopy);
            //_SetOfAttributeValue temp = new _SetOfAttributeValue(tempOpen);
            return new EAttribute(oid_pda_dateOfBirth, tempOpen);
        }


        public static EAttribute genderFromString(String g)
        {
            if (g == null || g.Length != 1)
            {
                LOGCU.Error("gender=" + g + " iken bu fonksiyon cagrilamaz.");
                return null;
            }
            return attributeasPrintableFromString(oid_pda_gender, g);
        }


        public static EAttribute placeOfBirthFromString(String sehir)
        {
            Asn1OpenType[] tempOpen = new Asn1OpenType[1];
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            DirectoryString val = new DirectoryString();
            val.Set_utf8String(new Asn1UTF8String(sehir));

            try
            {
                val.Encode(encBuf);
            }
            catch (Exception ex1)
            {
                LOGCU.Error("Buraya hic gelmemeli", ex1);
            }
            tempOpen[0] = new Asn1OpenType(encBuf.MsgCopy);
            //_SetOfAttributeValue temp = new _SetOfAttributeValue(tempOpen);
            return new EAttribute(oid_pda_placeOfBirth, tempOpen);
        }


        public static EAttribute roleFromArray(int[] r)
        {
            Asn1OpenType[] tempOpen = new Asn1OpenType[1];
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            GeneralName roleName = new GeneralName();
            roleName.Set_registeredID(new Asn1ObjectIdentifier(r));
            RoleSyntax val = new RoleSyntax(roleName);
            //safeEncBuf.Reset();
            try
            {
                val.Encode(encBuf);
            }
            catch (Exception ex1)
            {
                LOGCU.Error("Buraya hic gelmemeli", ex1);
            }
            tempOpen[0] = new Asn1OpenType(encBuf.MsgCopy);
            //_SetOfAttributeValue temp = new _SetOfAttributeValue(tempOpen);
            return new EAttribute(oid_at_role, tempOpen);
        }

        //     public static Attribute uPNFromString(String aUPN)
        //     {
        //          Asn1OpenType[] tempOpen = new Asn1OpenType[1];
        //
        //
        //          Asn1UTF8String upnUTF8 = new Asn1UTF8String(aUPN);
        //          safeEncBuf.reset();
        //          try
        //          {
        //               upnUTF8.encode(safeEncBuf);
        //          } catch (Exception ex1)
        //          {
        //               LOGCU.error("Buraya hic gelmemeli", ex1);
        //          }
        //          Asn1OpenType upnOpenType = new Asn1OpenType(safeEncBuf.getMsgCopy());
        //
        //          AnotherName upnAnother = new AnotherName(new Asn1ObjectIdentifier(_ImplicitValues.id_win_upn),upnOpenType);
        //
        //          GeneralName val = new GeneralName();
        //          val.set_otherName(upnAnother);
        //
        //          safeEncBuf.reset();
        //          try
        //          {
        //               val.encode(safeEncBuf);
        //          } catch (Exception ex1)
        //          {
        //               LOGCU.error("Buraya hic gelmemeli", ex1);
        //          }
        //          tempOpen[0] = new Asn1OpenType(safeEncBuf.getMsgCopy());
        //          _SetOfAttributeValue temp = new _SetOfAttributeValue(tempOpen);
        //          return new Attribute(oid_win_upn, temp);
        //     }

        public static void getOneAttribute(Asn1ObjectIdentifier aOID, Asn1Type aAsn, SubjectDirectoryAttributes aSubjectDirAttrs)
        {
            Attribute[] attrAr = aSubjectDirAttrs.elements;
            for (int i = 0; i < attrAr.Length; i++)
                if (attrAr[i].type.Equals(aOID))
                {
                    if (attrAr[i].values == null) return;
                    Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(attrAr[i].values.elements[0].mValue);
                    aAsn.Decode(decBuf);
                    return;
                }
        }

        public static RoleSyntax getRol(SubjectDirectoryAttributes aSubjectDirAttrs)
        {
            RoleSyntax r = new RoleSyntax();
            try
            {
                getOneAttribute(oid_at_role, r, aSubjectDirAttrs);
            }
            catch (Exception ex)
            {
                LOGCU.Error("Rol okunamadi.", ex);
                return null;
            }
            return r;
        }
    }

    class DirAttrHash
    {
        private ArrayList oidVector = new ArrayList();
        private ArrayList sinifVector = new ArrayList();

        public void put(Asn1ObjectIdentifier oid, Type sinif)
        {
            oidVector.Add(oid);
            sinifVector.Add(sinif);
        }

        public Type don_oid_sinif(Asn1ObjectIdentifier oid)
        {
            for (int i = 0; i < oidVector.Count; i++)
            {
                if (((Asn1ObjectIdentifier)oidVector[i]).Equals(oid))
                    return (Type)sinifVector[i];
            }
            return null;
        }
    }
}
