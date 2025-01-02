using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation
{
    public class ATSHashIndexCollector
    {
        internal List<ECertificate> mAllCerts = new List<ECertificate>();
        internal List<ECRL> mAllCrls = new List<ECRL>();
        internal List<EBasicOCSPResponse> mAllOcsps = new List<EBasicOCSPResponse>();

        public void checkATSHashIndex(Signer aSigner)
        {

            try
            {
                List<EAttribute> unsignedAttribute = aSigner.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
                //unsignedAttribute.size()-1 for wider validation
                EContentInfo ci = new EContentInfo(unsignedAttribute[unsignedAttribute.Count - 1].getValue(0));
                ESignedData sd = new ESignedData(ci.getContent());
                EAttribute atsHashIndexAttr = sd.getSignerInfo(0).getUnsignedAttribute(AttributeOIDs.id_aa_ATSHashIndex)[0];
                EATSHashIndex atsHashIndex = new EATSHashIndex(atsHashIndexAttr.getValue(0));

                mAllCerts = _checkCerts(aSigner, atsHashIndex);
                mAllCrls = _checkCrls(aSigner, atsHashIndex);
                mAllOcsps = _checkOcsps(aSigner, atsHashIndex);
                // _changeParameters(mAllCerts, crls);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
                exceptionHandler(aSigner);
            }
        }
        private void exceptionHandler(Signer aSigner)
        {
            mAllCerts = aSigner.getBaseSignedData().getSignedData().getCertificates();
            mAllCrls = aSigner.getBaseSignedData().getSignedData().getCRLs();
            List<EBasicOCSPResponse> basicOcspsAll = new List<EBasicOCSPResponse>();
            foreach (EOCSPResponse eocspResponse in aSigner.getBaseSignedData().getSignedData().getOSCPResponses())
            {
                basicOcspsAll.Add(eocspResponse.getBasicOCSPResponse());
            }
            mAllOcsps = basicOcspsAll;
        }
        private List<ECertificate> _checkCerts(Signer aSigner, EATSHashIndex atsHashIndex)
        {
            List<ECertificate> removeCerts = new List<ECertificate>();

            List<Asn1OctetString> certsHash = new List<Asn1OctetString>(atsHashIndex.getCertificatesHashIndex());
            DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atsHashIndex.gethashIndAlgorithm());

            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            Asn1OctetString hash = null;
            SignedData signedData = aSigner.getBaseSignedData().getSignedData().getObject();

            if (signedData.certificates != null && signedData.certificates.elements != null)
            {
                foreach (CertificateChoices cert in signedData.certificates.elements)
                {
                    if (cert != null)
                    {
                        cert.Encode(encBuf);
                        hash = new Asn1OctetString(DigestUtil.digest(digestAlg, encBuf.MsgCopy));
                        encBuf.Reset();
                        if (!certsHash.Contains(hash))
                        {
                            removeCerts.Add(new ECertificate((Certificate)cert.GetElement()));
                        }
                    }
                }
            }

            return removeCerts;
        }

        private List<ECRL> _checkCrls(Signer aSigner, EATSHashIndex atsHashIndex)
        {
            List<ECRL> removeCrls = new List<ECRL>();

            List<Asn1OctetString> crlsHash = new List<Asn1OctetString>(atsHashIndex.getCrlsHashIndex());
            DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atsHashIndex.gethashIndAlgorithm());

            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            Asn1OctetString hash = null;
            SignedData signedData = aSigner.getBaseSignedData().getSignedData().getObject();

            if (signedData.crls != null && signedData.crls.elements != null)
            {
                foreach (RevocationInfoChoice crl in signedData.crls.elements)
                {
                    if (crl != null && crl.ChoiceID == RevocationInfoChoice._CRL)
                    {
                        crl.Encode(encBuf);
                        hash = new Asn1OctetString(DigestUtil.digest(digestAlg, encBuf.MsgCopy));
                        encBuf.Reset();
                        if (!crlsHash.Contains(hash))
                        {
                            removeCrls.Add(new ECRL((CertificateList)crl.GetElement()));
                        }
                    }
                }
            }
            return removeCrls;
        }

        private List<EBasicOCSPResponse> _checkOcsps(Signer aSigner, EATSHashIndex atsHashIndex)
        {
            List<EOCSPResponse> removeOcsps = new List<EOCSPResponse>();

            List<Asn1OctetString> crlsHash = new List<Asn1OctetString>(atsHashIndex.getCrlsHashIndex());
            DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atsHashIndex.gethashIndAlgorithm());

            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            Asn1OctetString hash = null;
            SignedData signedData = aSigner.getBaseSignedData().getSignedData().getObject();
            List<EBasicOCSPResponse> basicOcsps = new List<EBasicOCSPResponse>();

            if (signedData.crls != null && signedData.crls.elements != null)
            {
                foreach (RevocationInfoChoice crl in signedData.crls.elements)
                {
                    if (crl != null && crl.ChoiceID == RevocationInfoChoice._OTHER)
                    {
                        crl.Encode(encBuf);
                        hash = new Asn1OctetString(DigestUtil.digest(digestAlg, encBuf.MsgCopy));
                        encBuf.Reset();
                        if (!crlsHash.Contains(hash))
                        {
                            OtherRevocationInfoFormat format = (OtherRevocationInfoFormat)crl.GetElement();
                            if (format.otherRevInfoFormat.Equals(new Asn1ObjectIdentifier(_cmsValues.id_ri_ocsp_response)))
                            {
                                removeOcsps.Add(new EOCSPResponse(format.otherRevInfo.mValue));
                            }
                            else if (format.otherRevInfoFormat.Equals(new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_basic)))
                            {
                                basicOcsps.Add(new EBasicOCSPResponse(format.otherRevInfo.mValue));
                            }
                        }
                    }
                }
            }

            if (removeOcsps != null && removeOcsps.Count != 0)
            {
                foreach (EOCSPResponse eocspResponse in removeOcsps)
                {
                    basicOcsps.Add(eocspResponse.getBasicOCSPResponse());
                }
            }
            return basicOcsps;
        }
    }
}
