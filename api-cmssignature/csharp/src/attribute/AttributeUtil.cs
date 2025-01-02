using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Reflection;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * This class has the methods that are commonly used in attributes 
 * @author aslihan.kubilay
 *
 */

    //todo Annotation!
    //@ApiClass

    public static class AttributeUtil
    {
        /**
	 * Creates the complete-revocation-references attribute. The first CrlOcspRef of the list refers to signing certificate. The 
     * second and subsequent CrlOcspRef elements refer to OtherCertID elements in order in complete-certificate-references. For trusted certificates,
     * CrlOcspRef elements with null CRLListID,OcspListID,OtherRevRefs are put in attribute
	 * @param aCertRevInfos
	 * @param aDigestAlg
	 * @return
	 * @throws CMSSignatureException
	 */
        public static CompleteRevocationRefs createRevocationReferences(List<CertRevocationInfoFinder.CertRevocationInfo> aCertRevInfos, DigestAlg aDigestAlg)
        {
            CrlOcspRef[] crlOcspRefs = new CrlOcspRef[aCertRevInfos.Count];

            for (int i = 0; i < aCertRevInfos.Count; i++)
            {
                CertRevocationInfoFinder.CertRevocationInfo info = aCertRevInfos[i];
                ECRL[] crls = info.getCRLs();
                EBasicOCSPResponse [] ocspRespList = info.getOCSPResponses();
                CRLListID crlListID = null;
                OcspListID ocspListID = null;

                if (crls != null)
                {
                    crlListID = createCRLListID(crls, aDigestAlg);
                }
                if (ocspRespList != null)
                {
                    ocspListID = createOCSPListID(ocspRespList, aDigestAlg);
                }

                crlOcspRefs[i] = new CrlOcspRef(crlListID, ocspListID, null);
            }

            CompleteRevocationRefs revRefs = new CompleteRevocationRefs(crlOcspRefs);
            return revRefs;
        }



        public static OcspListID createOCSPListID(EBasicOCSPResponse [] aOCSPRespList, DigestAlg aDigestAlg)
        {
            int i = 0;
            OcspResponsesID[] ids = new OcspResponsesID[aOCSPRespList.Length];
            foreach (EBasicOCSPResponse ocspResp in aOCSPRespList)
            {
                ids[i] = createOCSPResponsesID(ocspResp, aDigestAlg);
                i++;
            }

            return new OcspListID(new _SeqOfOcspResponsesID(ids));
        }

        public static OcspResponsesID createOCSPResponsesID(EBasicOCSPResponse aOCSPResp, DigestAlg aDigestAlg)
        {
            OtherHash otherHash = createOtherHash(aOCSPResp.getBytes(), aDigestAlg);
            OcspIdentifier ocspID = createOCSPIdentifier(aOCSPResp);
            return new OcspResponsesID(ocspID, otherHash);
        }



        public static OcspIdentifier createOCSPIdentifier(EBasicOCSPResponse aOCSPResp)
        {
            ResponderID respID = aOCSPResp.getTbsResponseData().getObject().responderID;
            Asn1GeneralizedTime time = aOCSPResp.getTbsResponseData().getObject().producedAt;
            return new OcspIdentifier(respID, time);
        }


        public static CRLListID createCRLListID(ECRL[] aCRLs, DigestAlg aDigestAlg)
        {
            if (aCRLs == null || aCRLs.Length == 0)
                return null;

            CrlValidatedID[] vIDs = new CrlValidatedID[aCRLs.Length];
            for (int i = 0; i < aCRLs.Length; i++)
            {
                vIDs[i] = createCRLValidatedID(aCRLs[i], aDigestAlg);
            }

            return new CRLListID(new _SeqOfCrlValidatedID(vIDs));
        }

        public static CrlValidatedID createCRLValidatedID(ECRL aCRL, DigestAlg aDigestAlg)
        {
            OtherHash otherHash = createOtherHash(aCRL.getBytes(), aDigestAlg);
            CrlIdentifier crlID = createCRLIdentifier(aCRL);
            return new CrlValidatedID(otherHash, crlID);
        }

        public static CrlIdentifier createCRLIdentifier(ECRL aCRL)
        {
            Name issuer = aCRL.getObject().tbsCertList.issuer;
            Asn1UTCTime time = (Asn1UTCTime)aCRL.getObject().tbsCertList.thisUpdate.GetElement();

            BigInteger crlNumber = aCRL.getCRLNumber();
            if (crlNumber != null)
                return new CrlIdentifier(issuer, time, new Asn1BigInteger(crlNumber));
            return new CrlIdentifier(issuer, time);
        }


        /*
         * It creates complete-certificate-references attribute. Reference for signing certificate is not included in this attribute.
         */
        public static CompleteCertificateRefs createCertificateReferences(List<CertRevocationInfoFinder.CertRevocationInfo> aCertRevInfos, DigestAlg aDigestAlg)
        {
            List<OtherCertID> otherCertIDs = new List<OtherCertID>();
            for (int i = 1; i < aCertRevInfos.Count; i++)
            {
                otherCertIDs.Add(createOtherCertID(aCertRevInfos[i].getCertificate(), aDigestAlg));
            }
            return new CompleteCertificateRefs(otherCertIDs.ToArray());
        }



        public static OtherHash createOtherHash(byte[] aData, DigestAlg aDigestAlg)
        {
            OtherHash otherHash = new OtherHash();
            if (aDigestAlg.Equals(DigestAlg.SHA1))
            {
                byte[] hashValue = createOtherHashValue(aData, DigestAlg.SHA1);
                otherHash.Set_sha1Hash(new Asn1OctetString(hashValue));
            }
            else
            {
                OtherHashAlgAndValue algAndValue = createOtherHashAlgAndValue(aData, aDigestAlg);
                otherHash.Set_otherHash(algAndValue);
            }

            return otherHash;
        }

        public static byte[] createOtherHashValue(byte[] aData, DigestAlg aDigestAlg)
        {
            try
            {
                byte[] hashValue = DigestUtil.digest(aDigestAlg, aData);
                return hashValue;
            }
            catch (Exception tEx)
            {
                throw new CMSSignatureException("OtherHash degeri olusturmak icin ozet alinirken hata olustu.", tEx);
            }
        }


        public static OtherHashAlgAndValue createOtherHashAlgAndValue(byte[] aData, DigestAlg aDigestAlg)
        {
            byte[] hashValue = createOtherHashValue(aData, aDigestAlg);
            AlgorithmIdentifier algOID = new AlgorithmIdentifier(aDigestAlg.getOID(), null);
            OtherHashAlgAndValue algAndValue = new OtherHashAlgAndValue(algOID, new Asn1OctetString(hashValue));
            return algAndValue;
        }

        public static OtherCertID createOtherCertID(ECertificate aCer, DigestAlg aDigestAlg)
        {
            OtherHash otherHash = createOtherHash(aCer.getBytes(), aDigestAlg);
            IssuerSerial issuerSerial = createIssuerSerial(aCer);
            return new OtherCertID(otherHash, issuerSerial);
        }

        public static IssuerSerial createIssuerSerial(ECertificate aCer)
        {
            GeneralName gn = new GeneralName();
            gn.Set_directoryName(aCer.getObject().tbsCertificate.issuer);

            GeneralNames gns = new GeneralNames(1);
            gns.elements[0] = gn;

            IssuerSerial is_ = new IssuerSerial(gns, aCer.getObject().tbsCertificate.serialNumber);
            return is_;
        }


        //Since in certificate-values attribute definition,it says:
        //It holds the values of certificates referenced in the complete-certificate-references attribute
        //the signer certificate doesnot exist in this values
        public static CertificateValues createCertificateValues(List<CertRevocationInfoFinder.CertRevocationInfo> aList)
        {
            List<Certificate> certs = new List<Certificate>();

            //Certificate[] certs = new Certificate[aList.size()-1];
            for (int i = 1; i < aList.Count; i++)
            {
                certs.Add(aList[i].getCertificate().getObject());
            }

            return new CertificateValues(certs.ToArray());
        }

        public static RevocationValues createRevocationValues(List<CertRevocationInfoFinder.CertRevocationInfo> aList)
        {
            List<CertificateList> crls = new List<CertificateList>();
            List<BasicOCSPResponse> ocsps = new List<BasicOCSPResponse>();

            foreach (CertRevocationInfoFinder.CertRevocationInfo cri in aList)
            {
                if (cri.getCRLs() != null)
                {
                    ECRL[] crlsOfcri = cri.getCRLs();
                    foreach (ECRL crl in crlsOfcri)
                        crls.Add(crl.getObject());
                }

                if (cri.getOCSPResponses() == null) continue;
                EBasicOCSPResponse[] ocspResponses = cri.getOCSPResponses();
                foreach (EBasicOCSPResponse ocspResponse in ocspResponses)
                {
                    ocsps.Add(ocspResponse.getObject());
                }
            }

            _SeqOfCertificateList crlVals = null;
            _SeqOfBasicOCSPResponse ocspVals = null;

            if (crls.Count > 0)
                crlVals = new _SeqOfCertificateList(crls.ToArray());
            if (ocsps.Count > 0)
                ocspVals = new _SeqOfBasicOCSPResponse(ocsps.ToArray());

            return new RevocationValues(crlVals, ocspVals, null);
        }

        public static List<ECRL> getCRLs(List<CertRevocationInfoFinder.CertRevocationInfo> aList)
        {
            List<ECRL> crls = new List<ECRL>();
            foreach (CertRevocationInfoFinder.CertRevocationInfo cri in aList)
            {
                if (cri.getCRLs() != null)
                {
                    foreach (ECRL crl in cri.getCRLs())
                        crls.Add(crl);
                }
            }

            return crls;
        }
        public static List<EOCSPResponse> getOCSPResponses(List<CertRevocationInfoFinder.CertRevocationInfo> aList)
        {
            List<EOCSPResponse> ocsps = new List<EOCSPResponse>();
            for (int i = 0; i < aList.Count; i++)
            {
                CertRevocationInfoFinder.CertRevocationInfo cri = aList[i];
                if (cri.getOCSPResponses() != null)
                {
                    foreach (EBasicOCSPResponse ocsp in cri.getOCSPResponses())
                        ocsps.Add(EOCSPResponse.getEOCSPResponse(ocsp));
                }
            }

            return ocsps;
        }
        public static List<ECertificate> getCertificates(List<CertRevocationInfoFinder.CertRevocationInfo> aList)
        {
            List<ECertificate> certs = new List<ECertificate>();
            for (int i = 1; i < aList.Count; i++)
            {
                certs.Add(aList[i].getCertificate());
            }

            return certs;
        }

        public static DateTime? getTimeFromTimestamp(EContentInfo aCI)
        {
            try
            {
                ESignedData sd = new ESignedData(aCI.getContent());
                return getTimeFromTimestamp(sd);
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Error in retrieving time from contentinfo", aEx);
            }
        }

        public static DateTime? getTimeFromTimestamp(ESignedData aSD)
        {
            try
            {
                ETSTInfo tstInfo = new ETSTInfo(aSD.getEncapsulatedContentInfo().getContent());
                return tstInfo.getTime();
            }
            catch (Exception aEx)
            {
                throw new CMSSignatureException("Error in retrieving time from signeddata", aEx);
            }

        }
        //todo test et!
        public static String getAttributeMemberName(Asn1ObjectIdentifier objIden)
        {
            FieldInfo[] fields = typeof(AttributeOIDs).GetFields();
            foreach (FieldInfo field in fields)
            {
                if (field.FieldType != typeof(Asn1ObjectIdentifier)) continue;
                return field.Name;
            }
            return null;
        }

        public static readonly Asn1ObjectIdentifier[] timeStampArray = new[]
	{
		AttributeOIDs.id_aa_ets_contentTimestamp,
		AttributeOIDs.id_aa_signatureTimeStampToken,
		AttributeOIDs.id_aa_ets_escTimeStamp,
		AttributeOIDs.id_aa_ets_certCRLTimestamp,
		AttributeOIDs.id_aa_ets_archiveTimestamp,
		AttributeOIDs.id_aa_ets_archiveTimestampV2,
        AttributeOIDs.id_aa_ets_archiveTimestampV3
	};

        public static Asn1ObjectIdentifier toContentType(EAttribute aAttribute)
        {
            try
            {
                Asn1DerDecodeBuffer buff = new Asn1DerDecodeBuffer(aAttribute.getValue(0));
                Asn1ObjectIdentifier objIden = new Asn1ObjectIdentifier();
                objIden.Decode(buff);
                return objIden;
            }
            catch (Asn1Exception e)
            {
                throw new ESYAException("Asn1 decode error", e);
            }
            catch (IOException e)
            {
                throw new ESYAException("IOException", e);
            }
        }

        public static String getAttributeString(EAttribute attr)
        {
            Asn1ObjectIdentifier attrOID = attr.getType();

            StringBuilder sb = new StringBuilder();

            foreach (Asn1ObjectIdentifier timeStamptOid in timeStampArray)
            {
                if (timeStamptOid.Equals(attrOID))
                {
                    var dateTime = SignatureTimeStampAttr.toTime(attr);
                    if (dateTime != null)
                        return dateTime.Value.ToString(CultureInfo.InvariantCulture);
                }
            }

            if (attrOID.Equals(AttributeOIDs.id_signingTime))
            {
                var dateTime = SigningTimeAttr.toTime(attr);
                if (dateTime != null) return dateTime.Value.ToString(CultureInfo.InvariantCulture);
            }

            if (attrOID.Equals(AttributeOIDs.id_contentType))
            {
                return ContentTypeAttr.toContentType(attr).ToString();
            }


            if (attrOID.Equals(AttributeOIDs.id_aa_ets_certValues))
            {
                ECertificateValues values = new ECertificateValues(attr.getValue(0));
                List<ECertificate> certs = values.getCertificates();
                if (certs != null)
                {
                    foreach (ECertificate cert in certs)
                    {
                        sb.Append(cert);
                    }
                }

                return sb.ToString();
            }

            if (attrOID.Equals(AttributeOIDs.id_aa_ets_revocationValues))
            {
                ERevocationValues values = new ERevocationValues(attr.getValue(0));

                List<ECRL> crls = values.getCRLs();
                if (crls != null)
                {
                    foreach (ECRL crl in crls)
                    {
                        sb.Append(crl);
                    }
                }

                List<EBasicOCSPResponse> ocsps = values.getBasicOCSPResponses();
                if (ocsps != null)
                {
                    foreach (EBasicOCSPResponse ocsp in ocsps)
                    {
                        sb.Append(ocsp);
                    }
                }

                return sb.ToString();
            }

            return null;
        }


    }
}
