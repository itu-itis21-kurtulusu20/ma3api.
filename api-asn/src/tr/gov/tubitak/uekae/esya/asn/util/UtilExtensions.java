package tr.gov.tubitak.uekae.esya.asn.util;

/**
 * <p>Title: ESYA</p>
 * <p>Description: </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 *
 * @author Muhammed Serdar SORAN
 * @version 1.0
 * @deprecated no more use...
 */
@Deprecated
public class UtilExtensions {
/*

    static public final Asn1ObjectIdentifier oid_ce_authorityKeyIdentifier = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_authorityKeyIdentifier);
    static public final Asn1ObjectIdentifier oid_ce_subjectKeyIdentifier = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_subjectKeyIdentifier);
    static public final Asn1ObjectIdentifier oid_ce_keyUsage = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_keyUsage);
    static public final Asn1ObjectIdentifier oid_ce_certificatePolicies = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_certificatePolicies);
    static public final Asn1ObjectIdentifier oid_ce_policyMappings = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_policyMappings);
    static public final Asn1ObjectIdentifier oid_ce_basicConstraints = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_basicConstraints);
    static public final Asn1ObjectIdentifier oid_ce_extKeyUsage = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_extKeyUsage);
    static public final Asn1ObjectIdentifier oid_ce_cRLDistributionPoints = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_cRLDistributionPoints);
    static public final Asn1ObjectIdentifier oid_pe_authorityInfoAccess = new Asn1ObjectIdentifier(_ImplicitValues.id_pe_authorityInfoAccess);
    static public final Asn1ObjectIdentifier oid_pe_subjectInfoAccess = new Asn1ObjectIdentifier(_ImplicitValues.id_pe_subjectInfoAccess);
    static public final Asn1ObjectIdentifier oid_ce_subjectAltName = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_subjectAltName);
    static public final Asn1ObjectIdentifier oid_pe_qcStatements = new Asn1ObjectIdentifier(_PKIXqualifiedValues.id_pe_qcStatements);
    static public final Asn1ObjectIdentifier oid_win_certTemplate = new Asn1ObjectIdentifier(_ImplicitValues.id_win_certTemplate);
    static public final Asn1ObjectIdentifier oid_ce_nameConstraints = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_nameConstraints);
    static public final Asn1ObjectIdentifier oid_ce_policyConstraints = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_policyConstraints);
    static public final Asn1ObjectIdentifier oid_ce_inhibitAnyPolicy = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_inhibitAnyPolicy);
    static public final Asn1ObjectIdentifier oid_ce_subjectDirectoryAttributes = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_subjectDirectoryAttributes);
    static public final Asn1ObjectIdentifier oid_ce_cRLNumber = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_cRLNumber);
    static public final Asn1ObjectIdentifier oid_ce_issuingDistributionPoint = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_issuingDistributionPoint);
    static public final Asn1ObjectIdentifier oid_ce_deltaCRLIndicator = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_deltaCRLIndicator);
    static public final Asn1ObjectIdentifier oid_ce_freshestCRL = new Asn1ObjectIdentifier(_ImplicitValues.id_ce_freshestCRL);
    //static public final Asn1ObjectIdentifier o = new Asn1ObjectIdentifier(_ImplicitValues.);
    //_ImplicitValues.id_ce_authorityKeyIdentifier.
//_ImplicitValues.id_ce_cRLDistributionPoints

    static ExtensionHash hash = new ExtensionHash();

    private static final Logger LOGCU = LoggerFactory.getLogger(UtilExtensions.class);

    static {
//          try
//          {
//               //Bu eslestirmeler yapilirken RFC3280 kullanilmistir.
//               String paketx509 = "tr.gov.tubitak.uekae.esya.asn.x509.";
//               hash.put("AuthorityKeyIdentifier", oid_ce_authorityKeyIdentifier, Class.forName(paketx509 + "AuthorityKeyIdentifier"));
//               hash.put("SubjectKeyIdentifier", oid_ce_subjectKeyIdentifier, Class.forName(paketx509 + "SubjectKeyIdentifier"));
//               hash.put("KeyUsage", oid_ce_keyUsage, Class.forName(paketx509 + "KeyUsage"));
//               hash.put("CertificatePolicies", oid_ce_certificatePolicies, Class.forName(paketx509 + "CertificatePolicies"));
//               hash.put("PolicyMappings", oid_ce_policyMappings, Class.forName(paketx509 + "PolicyMappings"));
//               hash.put("BasicConstraints", oid_ce_basicConstraints, Class.forName(paketx509 + "BasicConstraints"));
//               hash.put("ExtKeyUsageSyntax", oid_ce_extKeyUsage, Class.forName(paketx509 + "ExtKeyUsageSyntax"));
//               hash.put("CRLDistributionPoints", oid_ce_cRLDistributionPoints, Class.forName(paketx509 + "CRLDistributionPoints"));
//               hash.put("AuthorityInfoAccessSyntax", oid_pe_authorityInfoAccess, Class.forName(paketx509 + "AuthorityInfoAccessSyntax"));
//               hash.put("SubjectInfoAccessSyntax", oid_pe_subjectInfoAccess, Class.forName(paketx509 + "SubjectInfoAccessSyntax"));
//               hash.put("SubjectAltName", oid_ce_subjectAltName, Class.forName(paketx509 + "SubjectAltName"));
//               //hash.put(, Class.forName(paketx509 + ""));
//               //hash.put(, Class.forName(paketx509 + ""));
//               //hash.put(, Class.forName(paketx509 + ""));
//               //hash.put(, Class.forName(paketx509 + ""));
//          } catch (ClassNotFoundException ex)
//          {
//               LOGCU.fatal("Extension tiplerinden biri icin class bulunamadi", ex);
//               ex.printStackTrace();
//          }
        hash.put("AuthorityKeyIdentifier", oid_ce_authorityKeyIdentifier, tr.gov.tubitak.uekae.esya.asn.x509.AuthorityKeyIdentifier.class);
        hash.put("SubjectKeyIdentifier", oid_ce_subjectKeyIdentifier, tr.gov.tubitak.uekae.esya.asn.x509.SubjectKeyIdentifier.class);
        hash.put("KeyUsage", oid_ce_keyUsage, tr.gov.tubitak.uekae.esya.asn.x509.KeyUsage.class);
        hash.put("CertificatePolicies", oid_ce_certificatePolicies, tr.gov.tubitak.uekae.esya.asn.x509.CertificatePolicies.class);
        hash.put("PolicyMappings", oid_ce_policyMappings, tr.gov.tubitak.uekae.esya.asn.x509.PolicyMappings.class);
        hash.put("BasicConstraints", oid_ce_basicConstraints, tr.gov.tubitak.uekae.esya.asn.x509.BasicConstraints.class);
        hash.put("ExtKeyUsageSyntax", oid_ce_extKeyUsage, tr.gov.tubitak.uekae.esya.asn.x509.ExtKeyUsageSyntax.class);
        hash.put("CRLDistributionPoints", oid_ce_cRLDistributionPoints, tr.gov.tubitak.uekae.esya.asn.x509.CRLDistributionPoints.class);
        hash.put("AuthorityInfoAccessSyntax", oid_pe_authorityInfoAccess, tr.gov.tubitak.uekae.esya.asn.x509.AuthorityInfoAccessSyntax.class);
        hash.put("SubjectInfoAccessSyntax", oid_pe_subjectInfoAccess, tr.gov.tubitak.uekae.esya.asn.x509.SubjectInfoAccessSyntax.class);
        hash.put("SubjectAltName", oid_ce_subjectAltName, tr.gov.tubitak.uekae.esya.asn.x509.SubjectAltName.class);
        hash.put("Qualified Certificate Statements", oid_pe_qcStatements, tr.gov.tubitak.uekae.esya.asn.PKIXqualified.QCStatements.class);
        hash.put("Certificate Template Name", oid_win_certTemplate, Asn1BMPString.class);
        //hash.put(, tr.gov.tubitak.uekae.esya.asn.x509..class);

    }


//     private static Asn1DerEncodeBuffer safeEncBuf = new Asn1DerEncodeBuffer();

    public static String prettyPrintExtensionVal(Asn1Type val) {
        String value = null;

        if ((val instanceof Extension)) {
            Extension ex = (Extension) val;
            Asn1Type yazilacak = null;

            String name = hash.don_oid_name(ex.extnID);
            if (name == null) {
                name = ex.extnID.toString();
                yazilacak = ex.extnValue;
            } else
                try {
                    yazilacak = (Asn1Type) hash.don_oid_sinif(ex.extnID).newInstance();
                    Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(ex.extnValue.value);
                    yazilacak.decode(decBuf);
                } catch (Exception ex1) {
                    yazilacak = ex.extnValue;
                }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            yazilacak.print(ps, name, 0);
            value = baos.toString();
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            val.print(ps, "serdar", 0);
            value = baos.toString();

        }

        return value;

    }


    public static Extension extensionFromValue(
            int[] aExtID,
            boolean aCritic,
            Asn1Type aValue
    ) throws ESYAException {
        Asn1DerEncodeBuffer safeEncBuf = new Asn1DerEncodeBuffer();
        try {
            aValue.encode(safeEncBuf);
        } catch (Exception ex) {
            LOGCU.error("Buraya hic gelmemeli", ex);
            throw new ESYAException("Extension islenirken hata: ExtID:" + aExtID, ex);
        }

        Extension ex = new Extension(aExtID
                , aCritic
                , safeEncBuf.getMsgCopy());
        return ex;
    }


    public static Extension authorityKeyIdFromArray(byte[] keyID, boolean critic) throws ESYAException {
        AuthorityKeyIdentifier keyVal = new AuthorityKeyIdentifier();
        keyVal.keyIdentifier = new Asn1OctetString(keyID);

        return extensionFromValue(_ImplicitValues.id_ce_authorityKeyIdentifier
                , critic
                , keyVal);

    }


    public static Extension subjectKeyIdFromArray(byte[] keyID, boolean critic) throws ESYAException {
        SubjectKeyIdentifier keyVal = new SubjectKeyIdentifier(keyID);

        return extensionFromValue(_ImplicitValues.id_ce_subjectKeyIdentifier
                , critic
                , keyVal);

    }


    public static Extension keyUsageFromArray(boolean[] keyu, boolean critic) throws ESYAException {


        //sondaki false degerleri atalim...
        int i = keyu.length - 1;
        boolean[] keyu2;
        while ((i > 0) && (!keyu[i])) i--;
        keyu2 = new boolean[i + 1];
        while (i >= 0) keyu2[i] = keyu[i--];

        return extensionFromValue(_ImplicitValues.id_ce_keyUsage
                , critic
                , new KeyUsage(keyu2));

    }


//     public static Extension certPolicyFromArray (int[] certPol, boolean critic)
//     {
//          PolicyInformation pol = new PolicyInformation(certPol);
//          CertificatePolicies val = new CertificatePolicies(new PolicyInformation[]
//              {pol});
//          safeEncBuf.reset();
//          try
//          {
//               val.encode(safeEncBuf);
//          } catch (Exception ex1)
//          {
//               LOGCU.error("Buraya hic gelmemeli", ex1);
//          }
//
//          Extension ex = new Extension(_ImplicitValues.id_ce_certificatePolicies
//                                       , critic
//                                       , safeEncBuf.getMsgCopy());
//          return ex;
//
//     }
//

    public static Extension policyMappingFromArrays(int[][] map1, int[][] map2, boolean critic) throws ESYAException {
        if (map1.length != map2.length)
            return null;
        int s = map1.length;
        PolicyMappings_element[] elem = new PolicyMappings_element[s];

        for (int i = 0; i < s; i++) {
            elem[i] = new PolicyMappings_element(map1[i], map2[i]);
        }

        PolicyMappings val = new PolicyMappings(elem);

        return extensionFromValue(_ImplicitValues.id_ce_policyMappings
                , critic
                , val);

    }


    public static Extension basicConstraints(boolean aIsCA, int aLen, boolean aKritik) throws ESYAException {
        BasicConstraints val;
        if (aLen < 0) {
            val = new BasicConstraints();
            val.cA = new Asn1Boolean(aIsCA);
        } else
            val = new BasicConstraints(aIsCA, aLen);

        return extensionFromValue(_ImplicitValues.id_ce_basicConstraints
                , aKritik
                , val);
    }


    public static Extension extendedKeyUsageFromArray(int[][] keyuse, boolean critic) throws ESYAException {
        Asn1ObjectIdentifier[] elem = new Asn1ObjectIdentifier[keyuse.length];

        for (int i = 0; i < keyuse.length; i++) {
            elem[i] = new Asn1ObjectIdentifier(keyuse[i]);
        }

        ExtKeyUsageSyntax val = new ExtKeyUsageSyntax(elem);

        return extensionFromValue(_ImplicitValues.id_ce_extKeyUsage
                , critic
                , val);
    }


    public static Extension cdpsFromArray(String[] cdps, boolean critic) throws ESYAException {
        DistributionPoint[] elem = new DistributionPoint[cdps.length];
        DistributionPointName temp;
        for (int i = 0; i < cdps.length; i++) {
            GeneralName[] genName = new GeneralName[1];
            genName[0] = new GeneralName();
            genName[0].set_uniformResourceIdentifier(new Asn1IA5String(cdps[i]));

            GeneralNames genNames = new GeneralNames(genName);

            temp = new DistributionPointName();
            temp.set_fullName(genNames);

            elem[i] = new DistributionPoint();
            elem[i].distributionPoint = temp;
        }

        CRLDistributionPoints val = new CRLDistributionPoints(elem);

        return extensionFromValue(_ImplicitValues.id_ce_cRLDistributionPoints
                , critic
                , val);
    }

    */
/*
    * Bu metod her bir uri adresi icin bir DistributionPoint olusturuyor.
    *
   public static Extension freshestCrlFromArray (String[] cdps, boolean critic) throws ESYAException
   {
        DistributionPoint[] elem = new DistributionPoint[cdps.length];
        DistributionPointName temp;
        for (int i = 0; i < cdps.length; i++)
        {
             GeneralName[] genName = new GeneralName[1];
             genName[0] = new GeneralName();
             genName[0].set_uniformResourceIdentifier(new Asn1IA5String(cdps[i]));

             GeneralNames genNames = new GeneralNames(genName);

             temp = new DistributionPointName();
             temp.set_fullName(genNames);

             elem[i] = new DistributionPoint();
             elem[i].distributionPoint = temp;
        }

        CRLDistributionPoints val = new CRLDistributionPoints(elem);

        return extensionFromValue(_ImplicitValues.id_ce_freshestCRL
                                     , critic
                                     , val);
   }
    *//*


    */
/*
    * Bu metod sadece bir tane DistributionPoint olusturuyor ve tum uri adreslerini bu DistributionPoint
    * icindeki DistributionPointName alanina yaziyor.
    *//*

    public static Extension freshestCrlFromArray(String[] cdps, boolean critic) throws ESYAException {
        DistributionPoint[] distPoints = new DistributionPoint[1];
        GeneralName[] genNameArray = new GeneralName[cdps.length];

        for (int i = 0; i < cdps.length; i++) {
            genNameArray[i] = new GeneralName();
            genNameArray[i].set_uniformResourceIdentifier(new Asn1IA5String(cdps[i]));
        }

        GeneralNames genNames = new GeneralNames(genNameArray);

        DistributionPointName dpn = new DistributionPointName();
        dpn.set_fullName(genNames);

        distPoints[0] = new DistributionPoint();
        distPoints[0].distributionPoint = dpn;

        CRLDistributionPoints val = new CRLDistributionPoints(distPoints);

        return extensionFromValue(_ImplicitValues.id_ce_freshestCRL
                , critic
                , val);
    }

    public static Extension certPoliciesFromArrays(int[][] aPolicies, String[] aYerler, String[] aNoticeler, boolean critic) throws ESYAException {
        if ((aPolicies.length != aYerler.length)
                || (aPolicies.length != aNoticeler.length)
                )
            return null;
        int s = aPolicies.length;

        PolicyInformation[] elem = new PolicyInformation[s];
        Vector<PolicyQualifierInfo> quas = new Vector<PolicyQualifierInfo>();
        for (int i = 0; i < s; i++) //Her bir policy icin...
        {
            elem[i] = new PolicyInformation(aPolicies[i]);
            quas.clear();


            //CPS varsa onu encode et.
            if ((aYerler[i] != null) && (!aYerler[i].equals(""))) {
                Asn1DerEncodeBuffer safeEncBuf = new Asn1DerEncodeBuffer();
                try {
                    Qualifier qua = new Qualifier();
                    qua.set_cPSuri(new Asn1IA5String(aYerler[i]));
                    qua.encode(safeEncBuf);
//                         (new Asn1IA5String(aYerler[i])).encode(safeEncBuf);
                } catch (Exception ex1) {
                    LOGCU.error("Buraya hic gelmemeli", ex1);
                    throw new ESYAException("CertPolicy eklentisinde adreslerle ilgili bir hata var.", ex1);
                }
                quas.add(
                        new PolicyQualifierInfo(
                                _ExplicitValues.id_qt_cps,
                                new Asn1OpenType(safeEncBuf.getMsgCopy())
                        )
                );
            }

            //User Notice varsa onu encode et.
            if ((aNoticeler[i] != null) && (!aNoticeler[i].equals(""))) {
                Asn1DerEncodeBuffer safeEncBuf = new Asn1DerEncodeBuffer();
                try {
                    Qualifier qua = new Qualifier();
                    qua.set_userNotice(new UserNotice(
                            null,
                            */
/* RFC 5280 e gore utf8 olmali. 7 Ags 2010 ED. *//*

                            //new DisplayText(DisplayText._UTF8STRING,new Asn1UTF8String(aNoticeler[i]))
                            new DisplayText(DisplayText._BMPSTRING, new com.objsys.asn1j.runtime.Asn1BMPString(aNoticeler[i]))
//                             new DisplayText(DisplayText._IA5STRING,new com.objsys.asn1j.runtime.Asn1IA5String(aNoticeler[i]))
//                             new DisplayText(DisplayText._VISIBLESTRING,new com.objsys.asn1j.runtime.Asn1VisibleString(aNoticeler[i]))
                    ));
                    qua.encode(safeEncBuf);
//                         (new UserNotice(
//                             null,
//                             new DisplayText(DisplayText._UTF8STRING,new Asn1UTF8String(aNoticeler[i]))
//                             )).encode(safeEncBuf);

                } catch (Exception ex1) {
                    LOGCU.error("Buraya da hic gelmemeli", ex1);
                    throw new ESYAException("CertPolicy eklentisinde noticelerle ilgili bir hata var.", ex1);
                }
                quas.add(
                        new PolicyQualifierInfo(
                                _ExplicitValues.id_qt_unotice,
                                new Asn1OpenType(safeEncBuf.getMsgCopy())
                        )
                );
            }

            //Eger Qualifier'lar varsa onlari ekle.
            if (quas.size() > 0) {
                PolicyQualifierInfo[] qua = new PolicyQualifierInfo[quas.size()];
                for (int j = 0; j < qua.length; j++)
                    qua[j] = quas.elementAt(j);
                elem[i].policyQualifiers = new PolicyInformation_policyQualifiers(qua);
            }

        }

        CertificatePolicies val = new CertificatePolicies(elem);

        return extensionFromValue(_ImplicitValues.id_ce_certificatePolicies
                , critic
                , val);
    }


    public static Extension aiasFromArrays(int[][] metodlar, String[] yerler, boolean critic) throws ESYAException {
        if (metodlar.length != yerler.length)
            return null;
        int s = metodlar.length;

        AccessDescription[] elem = new AccessDescription[s];
        GeneralName temp;

        for (int i = 0; i < s; i++) {
            temp = new GeneralName();
            temp.set_uniformResourceIdentifier(new Asn1IA5String(yerler[i]));
            elem[i] = new AccessDescription(metodlar[i], temp);
        }

        AuthorityInfoAccessSyntax val = new AuthorityInfoAccessSyntax(elem);

        return extensionFromValue(_ImplicitValues.id_pe_authorityInfoAccess
                , critic
                , val);
    }


    public static Extension siasFromArrays(int[][] metodlar, String[] yerler, boolean critic) throws ESYAException {
        if (metodlar.length != yerler.length)
            return null;
        int s = metodlar.length;

        AccessDescription[] elem = new AccessDescription[s];
        GeneralName temp;

        for (int i = 0; i < s; i++) {
            temp = new GeneralName();
            temp.set_uniformResourceIdentifier(new Asn1IA5String(yerler[i]));
            elem[i] = new AccessDescription(metodlar[i], temp);
        }

        SubjectInfoAccessSyntax val = new SubjectInfoAccessSyntax(elem);

        return extensionFromValue(_ImplicitValues.id_pe_subjectInfoAccess
                , critic
                , val);
    }


    public static Extension certTemplateName(String aValue, boolean aKritik) throws ESYAException {
        Asn1BMPString val;
        if (aValue == null) {
            val = new Asn1BMPString("");
        } else
            val = new Asn1BMPString(aValue);


        return extensionFromValue(_ImplicitValues.id_win_certTemplate
                , aKritik
                , val);
    }


    public static Extension getExtension(Asn1ObjectIdentifier aOID, Extensions aExts)
    // throws IOException, Asn1Exception
    {
        Extension[] extAr = aExts.elements;
        for (int i = 0; i < extAr.length; i++)
            if (extAr[i].extnID.equals(aOID)) {
                return extAr[i];
            }
        return null;
    }

    public static String uzantiIsmiAl(Extension aExtension) {
        Asn1ObjectIdentifier oid = aExtension.extnID;
        if (oid.equals(UtilExtensions.oid_pe_authorityInfoAccess)) {
            return CertI18n.mesaj(CertI18n.SERTIFIKA_AIA);
        } else if (oid.equals(UtilExtensions.oid_ce_keyUsage)) {
            return CertI18n.mesaj(CertI18n.SERTIFIKA_KEY_USAGE);
        } else if (oid.equals(UtilExtensions.oid_ce_subjectAltName)) {
            return CertI18n.mesaj(CertI18n.SERTIFIKA_SAN);
        } else if (oid.equals(UtilExtensions.oid_ce_cRLDistributionPoints)) {
            return CertI18n.mesaj(CertI18n.SERTIFIKA_CDP);
        } else if (oid.equals(UtilExtensions.oid_ce_authorityKeyIdentifier)) {
            return CertI18n.mesaj(CertI18n.SERTIFIKA_AUTHORITY_KEY_ID);
        } else if (oid.equals(UtilExtensions.oid_ce_extKeyUsage)) {
            return CertI18n.mesaj(CertI18n.SERTIFIKA_EXTENDED_KEY_USAGE);
        } else if (oid.equals(UtilExtensions.oid_ce_certificatePolicies)) {
            return CertI18n.mesaj(CertI18n.SERTIFIKA_POLICIES);
        } else if (oid.equals(UtilExtensions.oid_ce_basicConstraints)) {
            return CertI18n.mesaj(CertI18n.SERTIFIKA_BASIC_CONS);
        } else if (oid.equals(UtilExtensions.oid_ce_policyMappings)) {
            return CertI18n.mesaj(CertI18n.SERTIFIKA_POLICY_MAPPINGS);
        } else if (oid.equals(UtilExtensions.oid_ce_subjectKeyIdentifier)) {
            return CertI18n.mesaj(CertI18n.SERTIFIKA_SUBJECT_KEY_ID);
        } else if (oid.equals(UtilExtensions.oid_pe_qcStatements)) {
            return CertI18n.mesaj(CertI18n.SERTIFIKA_QC_STATEMENTS);
        } else if (oid.equals(UtilExtensions.oid_ce_subjectDirectoryAttributes)) {
            return CertI18n.mesaj(CertI18n.SERTIFIKA_SUBJECT_DIRECTORY_ATTR);
        } else {
            return oid.toString();
        }
    }

    public static void main(String[] args) {
        AuthorityKeyIdentifier aki = new AuthorityKeyIdentifier();
        aki.keyIdentifier = new Asn1OctetString(new byte[]
                {1, 2, 3, 4});
        Asn1OctetString ocaki = new Asn1OctetString();
        try {
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            aki.encode(encBuf);
            ocaki = new Asn1OctetString(encBuf.getMsgCopy());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        KeyUsage ku = new KeyUsage(new boolean[]
                {true, false, true, false, true, false, true, false, true});
        Asn1OctetString ocku = new Asn1OctetString();
        try {
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            ku.encode(encBuf);
            ocku = new Asn1OctetString(encBuf.getMsgCopy());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Extension xx = new Extension(oid_ce_authorityKeyIdentifier, ocaki);
        System.out.println(prettyPrintExtensionVal(xx));

        Extension xxku = new Extension(oid_ce_keyUsage, ocku);
        System.out.println(prettyPrintExtensionVal(xxku));

    }
*/

}

@Deprecated
class ExtensionHash {
   /* private Vector<Asn1ObjectIdentifier> oidVector = new Vector<Asn1ObjectIdentifier>();
    private Vector<Class> sinifVector = new Vector<Class>();
    private Vector<String> nameVector = new Vector<String>();

    public void put(String name, Asn1ObjectIdentifier oid, Class sinif) {
        nameVector.add(name);
        oidVector.add(oid);
        sinifVector.add(sinif);
    }


    public Class don_oid_sinif(Asn1ObjectIdentifier oid) {
        for (int i = 0; i < oidVector.size(); i++) {
            if (oidVector.get(i).equals(oid))
                return sinifVector.get(i);
        }
        return null;
    }


    public String don_oid_name(Asn1ObjectIdentifier oid) {
        for (int i = 0; i < oidVector.size(); i++) {
            if (oidVector.get(i).equals(oid))
                return nameVector.get(i);
        }
        return null;
    }*/

}
