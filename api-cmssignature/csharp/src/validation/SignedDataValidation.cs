using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation
{
    /**
 * This class validates all signatures of a document. 
 * @author orcun.ertugrul
 *
 */
    public class SignedDataValidation
    {
        internal List<ECertificate> mCerts = new List<ECertificate>();
        internal List<ECRL> mCRLs = new List<ECRL>();
        internal List<EBasicOCSPResponse> mOCSPs = new List<EBasicOCSPResponse>();
        protected ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        /**
    	 * Validates all signatures of a document
    	 * @param aContentInfo document data
    	 * @param aParams parameters of validation. At least certificate validation policy file must be given. 
    	 * @return returns the result of all signatures. You can get status of signatures 
    	 * from function getSDStatus().
    	 * @throws Exception
    	 */
        public SignedDataValidationResult verify(byte[] aContentInfo, Dictionary<String, Object> aParams)
        {
            Stopwatch sw = Stopwatch.StartNew();

            BaseSignedData bs = new BaseSignedData(aContentInfo);

            if (aParams.ContainsKey(EParameters.P_EXTERNAL_CONTENT))
            {
                if (bs.getSignedData().getEncapsulatedContentInfo().getContent() != null)
                {
                    logger.Error("Internal and external content must not be used together");
                    throw new CMSSignatureException("Internal and external content must NOT be used together!");
                }
            }

            _fillCertRevocationLists(bs.getSignedData(), aParams);

            List<Signer> sis = bs.getSignerList();

            SignedDataValidationResult sdvr = new SignedDataValidationResult();
            bool verified = true;

            foreach (Signer si in sis)
            {
                SignatureValidator sv = new SignatureValidator(aContentInfo);
                sv.setCertificates(mCerts);
                sv.setCRLs(mCRLs);
                sv.setOCSPs(mOCSPs);
                SignatureValidationResult svr = new SignatureValidationResult();
                sv.verify(svr, si, false, aParams);

                sdvr.addSignatureValidationResult(svr);
                verified = verified && _findCSStatus(svr);
            }

            if (!verified)
            {
                sdvr.setSDStatus(SignedData_Status.NOT_ALL_VALID);
                sdvr.setDescription("Imzali Veri icerisinde dogrulanamayan imzalar var.");
            }
            else
            {
                sdvr.setSDStatus(SignedData_Status.ALL_VALID);
                sdvr.setDescription("Imzali Veri icerisindeki tum imzalar dogrulandi.");
            }
            sw.Stop();
            logger.Info(" Verify : " + sw.Elapsed.TotalMilliseconds + "ms. ");
            return sdvr;
        }

        public SignatureValidationResult verifyByGivenSigner(byte[] aContentInfo, Signer signer, Dictionary<String, Object> aParams)
        {
            Stopwatch sw = Stopwatch.StartNew();

            BaseSignedData bs = new BaseSignedData(aContentInfo);

            if(aParams.ContainsKey(EParameters.P_EXTERNAL_CONTENT)){
                if (bs.getSignedData().getEncapsulatedContentInfo().getContent() != null) {
                    logger.Error("Internal and external content must not be used together");
                    throw new CMSSignatureException("Internal and external content must not be used together");
                }
            }

            _fillCertRevocationLists(bs.getSignedData(), signer, aParams);

            bool verified = true;

            SignatureValidator sv = new SignatureValidator(aContentInfo);
            sv.setCertificates(mCerts);
            sv.setCRLs(mCRLs);
            sv.setOCSPs(mOCSPs);
            //sv.setUserDefinedCheckers(mUserDefinedCheckers);
            SignatureValidationResult svr = new SignatureValidationResult();
            sv.verify(svr, signer, false, aParams);

            verified = verified && _findCSStatus(svr);

            if (!verified)
            {
                svr.setSignatureStatus(Types.Signature_Status.INVALID);
                svr.setDescription("Imzali Veri icerisinde dogrulanamayan imzalar var.");
            }
            else
            {
                svr.setSignatureStatus(Types.Signature_Status.VALID);
                svr.setDescription("Imzali Veri icerisindeki tum imzalar dogrulandi.");
            }

            sw.Stop();
            logger.Info(" Verify : " + sw.Elapsed.TotalMilliseconds + "ms. ");
            return svr;
        }

        //Certificates,crls and basicocspresponses are gathered from
        //1. SignedData
        //2. Given user parameters
        //3. From references(certstore) if type is one of ESC,ESX1,ESX2
        //4.From all signer
        //@SuppressWarnings("unchecked")
        internal void _fillCertRevocationLists(ESignedData aSD, Dictionary<String, Object> aParams)
        {
            CertificateRevocationInfoCollector collector = new CertificateRevocationInfoCollector();
            collector._extractAll(aSD, aParams);

            //gathering values from signeddata
            mCerts.AddRange(collector.getAllCertificates());
            mCRLs.AddRange(collector.getAllCRLs());
            mOCSPs.AddRange(collector.getAllBasicOCSPResponses());

            _addUserParamaters(aParams);
        }
        //Certificates,crls and basicocspresponses are gathered from
        //1. SignedData
        //2. Given user parameters
        //3. From references(certstore) if type is one of ESC,ESX1,ESX2
        //4.From given signer

        internal void _fillCertRevocationLists(ESignedData aSD, Signer aSigner, Dictionary<String, Object> aParams)
        {
            CertificateRevocationInfoCollector collector = new CertificateRevocationInfoCollector();
            collector._extractFromSigner(aSD, aSigner.getSignerInfo(), aParams);

            //gathering values from signeddata
            mCerts = collector.getAllCertificates();
            mCRLs = collector.getAllCRLs();
            mOCSPs = collector.getAllBasicOCSPResponses();

            _addUserParamaters(aParams);
        }
        private void _addUserParamaters(Dictionary<String, Object> aParams)
        {
            //gathering values from user parameters
            if (aParams != null)
            {
                Object certListO = null;
                aParams.TryGetValue(AllEParameters.P_INITIAL_CERTIFICATES, out certListO);
                if (certListO != null)
                {
                    try
                    {
                        List<ECertificate> certs = (List<ECertificate>)certListO;
                        new Adder<ECertificate>().addDifferent(mCerts, certs);
                    }
                    catch (InvalidCastException aEx)
                    {
                        throw new CMSSignatureException("P_INITIAL_CERTIFICATES parameter is not of type List<ECertificate>", aEx);
                    }
                }

                Object crlListO = null;
                aParams.TryGetValue(AllEParameters.P_INITIAL_CRLS, out crlListO);
                if (crlListO != null)
                {
                    try
                    {
                        List<ECRL> crls = (List<ECRL>)crlListO;
                        new Adder<ECRL>().addDifferent(mCRLs, crls);
                    }
                    catch (InvalidCastException aEx)
                    {
                        throw new CMSSignatureException("P_INITIAL_CRLS parameter is not of type List<ECRL>", aEx);
                    }
                }


                Object ocspListO = null;
                aParams.TryGetValue(AllEParameters.P_INITIAL_OCSP_RESPONSES, out ocspListO);
                if (ocspListO != null)
                {
                    try
                    {
                        List<EOCSPResponse> ocsps = (List<EOCSPResponse>)ocspListO;
                        List<EBasicOCSPResponse> basicOcsps = new List<EBasicOCSPResponse>();
                        foreach (EOCSPResponse resp in ocsps)
                        {
                            basicOcsps.Add(resp.getBasicOCSPResponse());
                        }

                        new Adder<EBasicOCSPResponse>().addDifferent(mOCSPs, basicOcsps);
                    }
                    catch (InvalidCastException aEx)
                    {
                        throw new CMSSignatureException("P_INITIAL_BASIC_OCSP_RESPONSES parameter is not of type List<EBasicOCSPResponse>", aEx);
                    }
                }
            }
        }
        private bool _findCSStatus(SignatureValidationResult aSVR)
        {
            if (aSVR.getSignatureStatus() != Types.Signature_Status.VALID)
                return false;


            List<SignatureValidationResult> csResults = aSVR.getCounterSigValidationResults();
            if (csResults == null || csResults.Count == 0)
                return true;

            foreach (SignatureValidationResult svr in csResults)
            {
                if (!_findCSStatus(svr))
                {
                    return false;
                }
            }

            return true;
        }
    }


    class Adder<T>
    {
        internal void addDifferent(List<T> aAll, List<T> aNews)
        {
            foreach (T t in aNews)
            {
                if (!aAll.Contains(t))
                    aAll.Add(t);
            }
        }
    }
}
