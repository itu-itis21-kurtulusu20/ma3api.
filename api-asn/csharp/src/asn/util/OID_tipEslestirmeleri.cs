using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.asn.util
{
    public class OID_tipEslestirmeleri
    {
        private static readonly ILog LOGCU = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        public static readonly Asn1ObjectIdentifier oid_id_at_name = new Asn1ObjectIdentifier(
            _ExplicitValues.id_at_name);
        public static readonly Asn1ObjectIdentifier oid_id_at_surname = new Asn1ObjectIdentifier(
            _ExplicitValues.id_at_surname);
        public static readonly Asn1ObjectIdentifier oid_id_at_givenName = new Asn1ObjectIdentifier(
            _ExplicitValues.id_at_givenName);
        public static readonly Asn1ObjectIdentifier oid_id_at_commonName = new Asn1ObjectIdentifier(
            _ExplicitValues.id_at_commonName);
        public static readonly Asn1ObjectIdentifier oid_id_at_localityName = new Asn1ObjectIdentifier(
            _ExplicitValues.id_at_localityName);
        public static readonly Asn1ObjectIdentifier oid_id_at_stateOrProvinceName = new
            Asn1ObjectIdentifier(_ExplicitValues.id_at_stateOrProvinceName);
        public static readonly Asn1ObjectIdentifier oid_id_at_organizationName = new Asn1ObjectIdentifier(
            _ExplicitValues.id_at_organizationName);
        public static readonly Asn1ObjectIdentifier oid_id_at_organizationalUnitName = new
            Asn1ObjectIdentifier(_ExplicitValues.id_at_organizationalUnitName);
        public static readonly Asn1ObjectIdentifier oid_id_at_title = new Asn1ObjectIdentifier(
            _ExplicitValues.id_at_title);
        public static readonly Asn1ObjectIdentifier oid_id_at_countryName = new Asn1ObjectIdentifier(
            _ExplicitValues.id_at_countryName);
        public static readonly Asn1ObjectIdentifier oid_id_at_serialNumber = new Asn1ObjectIdentifier(
            _ExplicitValues.id_at_serialNumber);
        public static readonly Asn1ObjectIdentifier oid_id_at_pseudonym = new Asn1ObjectIdentifier(
            _ExplicitValues.id_at_pseudonym);
        public static readonly Asn1ObjectIdentifier oid_id_domainComponent = new Asn1ObjectIdentifier(
            _ExplicitValues.id_domainComponent);
        public static readonly Asn1ObjectIdentifier oid_id_emailAddress = new Asn1ObjectIdentifier(_ExplicitValues.id_emailAddress);
        public static readonly Asn1ObjectIdentifier oid_id_at_organizationIdentifier = new Asn1ObjectIdentifier(_ExplicitValues.id_at_organizationIdentifier);

        static AtavHash msHash = new AtavHash();

        static OID_tipEslestirmeleri()
        {

            try
            {
                msHash.put("cn", oid_id_at_commonName, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520CommonName));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      cn"); }

            try
            {
                //               msHash.put("c", oid_id_at_countryName, tr.gov.tubitak.uekae.esya.asn.x509.X520countryName.class);
                msHash.put("c", oid_id_at_countryName, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520CountrySerialUTF8liUcubeName));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      c"); }

            try
            {
                msHash.put("givenName", oid_id_at_givenName, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520name));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      givenName"); }

            try
            {
                msHash.put("l", oid_id_at_localityName, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520LocalityName));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      l"); }

            try
            {
                msHash.put("name", oid_id_at_name, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520name));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      name"); }

            try
            {
                msHash.put("ou", oid_id_at_organizationalUnitName, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520OrganizationalUnitName));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      ou"); }

            try
            {
                msHash.put("o", oid_id_at_organizationName, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520OrganizationName));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      o"); }

            try
            {
                msHash.put("pseudonym", oid_id_at_pseudonym, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520Pseudonym));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      pseudonym"); }

            try
            {
                //             msHash.put("serialNumber", oid_id_at_serialNumber, tr.gov.tubitak.uekae.esya.asn.x509.X520SerialNumber.class);
                msHash.put("serialNumber", oid_id_at_serialNumber, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520CountrySerialUTF8liUcubeName));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      serialNumber"); }

            try
            {
                msHash.put("st", oid_id_at_stateOrProvinceName, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520StateOrProvinceName));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      st"); }

            try
            {
                msHash.put("stateOrProvince", oid_id_at_stateOrProvinceName, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520StateOrProvinceName));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      stateOrProvince"); }

            try
            {
                msHash.put("sn", oid_id_at_surname, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520name));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      sn"); }

            try
            {
                msHash.put("title", oid_id_at_title, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520Title));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      titlr"); }

            try
            {
                msHash.put("dc", oid_id_domainComponent, typeof(Com.Objsys.Asn1.Runtime.Asn1IA5String));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      dc"); }

            try
            {
                msHash.put("e", oid_id_emailAddress, typeof(Com.Objsys.Asn1.Runtime.Asn1IA5String));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      e"); }

            try
            {
                msHash.put("organizationIdentifier", oid_id_at_organizationIdentifier, typeof(tr.gov.tubitak.uekae.esya.asn.x509.X520OrganizationIdentifier));
            }
            catch (Exception er) { LOGCU.Error("OID_tipEslestirmeleri : Buraya hic gelmemeli!      organizationIdentifier"); }
        }


        public static void listeyeAtavEkle(AttributeTypeAndValue aAtav)
        {
            int[] oidInt = aAtav.type.mValue;
            int i;
            String st = "" + oidInt[0];
            for (i = 1; i < oidInt.Length; i++)
            {
                st += "." + oidInt[i];
            }

            //          System.out.println(st);
            //          aAtav.type

            LOGCU.Debug(st + " oidli AttributeTypeAndValue eklenmeye calisilacak.");

            //tipinin ne oldugunu tam olarak bilemeyiz. Fakat asagidakilerden biri olma ihtimali yuksek:
            Type[] ihtimaller = new Type[] { typeof(DirectoryString), typeof(Asn1PrintableString), typeof(Asn1IA5String) };
            Asn1Type xx = null;
            Asn1DerDecodeBuffer decBuf;
            for (i = 0; i < ihtimaller.Length; i++)
            {
                try
                {
                    //xx = (Asn1Type) ihtimaller[i].newInstance();
                    xx = (Asn1Type)Activator.CreateInstance(ihtimaller[i]);
                    decBuf = new Asn1DerDecodeBuffer(aAtav.value_.mValue);
                    xx.Decode(decBuf);
                    LOGCU.Debug("Tip olarak " + ihtimaller[i].Name + " secildi.");
                    break;
                }
                catch (Exception ex)
                {
                    LOGCU.Debug(ihtimaller[i].Name + " icine decode edilemedi.", ex);
                }
            }

            if (i < ihtimaller.Length)
            {
                LOGCU.Debug("Atav listesine ekleniyor.... " + st + " - " + xx.GetType().Name);
                msHash.put(st, aAtav.type, xx.GetType());
            }
            else
            {
                LOGCU.Error("Atav listeye eklenemedi. " + st);
                throw new Asn1Exception("Atav listeye eklenemedi.");
            }

        }


        public static IkiliAttributeTandV tipDon(Asn1ObjectIdentifier aOid)
        {
            Asn1Type xx = null;
            String st = null;
            st = msHash.don_oid_dn(aOid);

            //Bu oid bizim listemizde olmayabilir. Bu durumda exception atmaliyiz.
            if (st == null)
            {
                throw new Asn1Exception("Oid " + aOid.ToString() + " bizim listemizde bulunmuyor.");
            }

            try
            {
                //xx = (Asn1Type) msHash.don_oid_sinif(aOid).newInstance();
                xx = (Asn1Type)Activator.CreateInstance(msHash.don_oid_sinif(aOid));
            }
            catch (Exception ex)
            {
                //               ex.printStackTrace();                        
                throw new Asn1Exception("Class instantiate edilemiyor: " + ex.ToString());
            }

            return new IkiliAttributeTandV(st, xx, aOid);
        }


        public static IkiliAttributeTandV atavDon(String aDN, String aValue, bool aTurkce)
        {
            Asn1ObjectIdentifier oid = msHash.don_dn_oid(aDN);
            //Class x = hash.don_oid_sinif(oid);
            Type x = msHash.don_dn_sinif(aDN);

            //Bu Class bizim listemizde olmayabilir. Bu durumda exception atmaliyiz.
            if (x == null)
            {
                throw new Asn1Exception("Gecersiz Name formati.");
            }

            ConstructorInfo[] cons = x.GetConstructors();


            ConstructorInfo con = null;
            Object[] params_ = null;
            if (HasConstructor(cons, ref con, new Type[] { typeof(String) }))
            {

                params_ = new Object[] { aValue };
            }
            else if (HasConstructor(cons, ref con, new Type[]
                     {typeof(Byte),
                         typeof(Com.Objsys.Asn1.Runtime.Asn1Type)}))
            {
                Asn1Type xx = null;
                byte bb = 0;
                try
                {
                    if (aTurkce && x != typeof(X520CountrySerialUTF8liUcubeName))
                    {

                        bb = (byte)x.GetField("_UTF8STRING").GetValue(x);
                        xx = new Asn1UTF8String(aValue);
                    }
                    else
                    {
                        bb = (byte)x.GetField("_PRINTABLESTRING").GetValue(x);
                        xx = new Asn1PrintableString(aValue);
                    }
                }
                catch (Exception ex2)
                {
                    throw new Asn1Exception("Can not create Asn1 object!", ex2);
                }
                params_ = new Object[] { bb, xx };
            }
            else
            {
                throw new Asn1Exception("Can not find convenient Constructor!");
            }

            Object val = null;
            try
            {
                val = con.Invoke(params_);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
            }

            return new IkiliAttributeTandV(aDN, (Asn1Type)val, oid);
        }

        private static bool HasConstructor(ConstructorInfo[] constructors, ref ConstructorInfo con, params Type[] parameters)
        {
            foreach (var constructor in constructors)
            {
                if (constructor.GetParameters().Select(p => p.ParameterType).SequenceEqual(parameters))
                {
                    con = constructor;
                    return true;
                }
            }
            return false;
        }
    }



    class AtavHash
    {
        private Dictionary<String, Asn1ObjectIdentifier> attr_dn_oid = new Dictionary<String, Asn1ObjectIdentifier>();
        private Dictionary<String, Type> attr_dn_class = new Dictionary<String, Type>();
        private List<Asn1ObjectIdentifier> oidVector = new List<Asn1ObjectIdentifier>();
        private List<String> dnVector = new List<String>();

        public void put(String aDN, Asn1ObjectIdentifier aOid, Type aSinif)
        {
            String dn = aDN.ToUpper(new CultureInfo("en-US", false));
            attr_dn_oid[dn]= aOid;
            attr_dn_class[dn] = aSinif;
            oidVector.Add(aOid);
            dnVector.Add(dn);
        }


        public String don_oid_dn(Asn1ObjectIdentifier aOid)
        {
            for (int i = 0; i < oidVector.Count; i++)
            {
                if (oidVector[i].Equals(aOid))
                    return dnVector[i];
            }
            return null;
        }


        public Asn1ObjectIdentifier don_dn_oid(String aDN)
        {
            Asn1ObjectIdentifier asn1ObjectIdentifier = null;
            attr_dn_oid.TryGetValue(aDN.ToUpper(new CultureInfo("en-US", false)), out asn1ObjectIdentifier);
            return asn1ObjectIdentifier;
        }


        public Type don_dn_sinif(String aDN)
        {
            Type type = null;
            attr_dn_class.TryGetValue(aDN.ToUpper(new CultureInfo("en-US", false)), out type);
            return type;
        }


        public Type don_oid_sinif(Asn1ObjectIdentifier aOid)
        {
            Type type = null;
            attr_dn_class.TryGetValue(don_oid_dn(aOid), out type);
            return type;
        }

    }
}
