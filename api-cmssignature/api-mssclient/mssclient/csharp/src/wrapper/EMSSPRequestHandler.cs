using System;
using System.Reflection;
using System.Text;
using System.Collections.Generic;
using System.Xml;
using System.Security.Cryptography;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.tools;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper
{
    public class EMSSPRequestHandler
    {
        public static String TURKCELL_PROFILE_2_MSS_URL = "http://uri.turkcellteknoloji.com.tr/MobileSignature/profile2";
        public static String TURKCELL_PROFILE_3_MSS_URL = "http://uri.turkcellteknoloji.com.tr/MobileSignature/profile3";
        private static readonly ILog Logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        public ECertificate signingCert;
        public ProfileInfo profileInfo;

        readonly MSSParams msspParams;
        bool skipMsspProfileQuery = false;

        public EMSSPRequestHandler(MSSParams msspParams)
        {
            EMSSPClientUtil.CheckLicense();
            this.msspParams = msspParams;
        }

        public bool isSkipMsspProfileQuery()
        {
            return skipMsspProfileQuery;
        }

        public void setSkipMsspProfileQuery(bool skipMsspProfileQuery)
        {
            this.skipMsspProfileQuery = skipMsspProfileQuery;
        }

        public void setCertificateInitials(PhoneNumberAndOperator phoneNumberAndOperator)
        {
            Operator mobileOperator = phoneNumberAndOperator.getOperator();
            String phoneNumber = phoneNumberAndOperator.getPhoneNumber();
            try
            {
                IMSSProvider provider = EMSSPProviderFactory.getProvider(mobileOperator);
                IProfileRequest profileRequester = provider.getProfileRequester(msspParams);
                String msspProfileQueryUrl = msspParams.GetMsspProfileQueryUrl();
                if (msspProfileQueryUrl != null)
                {
                    profileRequester.setServiceUrl(msspProfileQueryUrl);
                }
                IProfileResponse profileResponse = profileRequester.sendRequest(phoneNumber, ETransIdManager.GetNewTransId());
                if (profileResponse == null)
                {
                    String errorMsg = "MSSP profile response is null";
                    Logger.Error(errorMsg);
                    throw new EMSSPException(errorMsg);
                }

                signingCert = profileResponse.getCertificate();

                Status status = profileResponse.getStatus();
                if (status == null)
                {
                    Logger.Debug("Status is null");
                }
                else
                {
                    Logger.Debug("Profile Request completed . Return status = " + status.StatusMessage + "(" + status.StatusCode + ")");
                }
                profileInfo = profileResponse.getProfileInfo();
                if (profileInfo == null)
                {
                    throw new EMSSPException("Profile response is null", status);
                }
            }
            catch (Exception exc)
            {
                Logger.Error("Error while initializing certificate", exc);
                throw exc;
            }
        }

        public byte[] SignText(String textForSign, PhoneNumberAndOperator phoneNumberAndOperator, SignatureType iSignatureType)
        {
            if (textForSign.Length > 255)
            {
                String errorMsg = "Text size too long. Max 255 character.";
                Logger.Error(errorMsg);
                throw new EMSSPException(errorMsg);
            }

            SignatureType signatureType = SignatureType.PKCS7;
            if (iSignatureType != null)
            {
                signatureType = iSignatureType;
            }

            Operator mobileOperator = phoneNumberAndOperator.getOperator();
            String phoneNumber = phoneNumberAndOperator.getPhoneNumber();

            IMSSProvider msspProvider = EMSSPProviderFactory.getProvider(mobileOperator);
            ISignatureRequest signatureRequester = msspProvider.getSignatureRequester(msspParams);
            String msspSignatureQueryUrl = msspParams.GetMsspSignatureQueryUrl();
            if (msspSignatureQueryUrl != null)
            {
                signatureRequester.setServiceUrl(msspSignatureQueryUrl);
            }

            ISignable signable = new TextSignable(textForSign, signatureType);

            String transid = ETransIdManager.GetNewTransId();
            ISignatureResponse response = signatureRequester.sendRequest(transid, phoneNumber, signable);
            if (response == null)
            {
                String errorMsg = "MSSP signature response is null";
                Logger.Error(errorMsg);
                throw new EMSSPException(errorMsg);
            }

            byte[] signature = response.getSignature();
            if (signature == null)
            {
                String errorMsg = "MSSP Signature is null. TransId: " + response.getTransId();
                Logger.Error(errorMsg);
                Status status = response.getStatus();
                throw new EMSSPException(errorMsg, status);
            }
            return signature;
        }

        public byte[] Sign(byte[] dataToBeSigned, SigningMode aMode, PhoneNumberAndOperator phoneNumberAndOperator, String informativeText, String aSigningAlg)
        {
            try
            {
                checkSigningMode(aMode);

                String digestForSign64 = getBase64EncodedDigestForSign(dataToBeSigned, aSigningAlg);
                ISignable binarySignable = new BinarySignable(digestForSign64, informativeText, SignatureType.PKCS7);

                Operator mobileOperator = phoneNumberAndOperator.getOperator();
                String phoneNumber = phoneNumberAndOperator.getPhoneNumber();

                IMSSProvider mssProvider = EMSSPProviderFactory.getProvider(mobileOperator);
                ISignatureRequest signatureRequester = mssProvider.getSignatureRequester(msspParams);

                String msspSignatureQueryUrl = msspParams.GetMsspSignatureQueryUrl();
                if (msspSignatureQueryUrl != null)
                {
                    signatureRequester.setServiceUrl(msspSignatureQueryUrl);

                }

                String digestAlgOfSignatureAlg = Algorithms.getDigestAlgOfSignatureAlg(aSigningAlg);
                String profileURL = getMSSProfileURL(digestAlgOfSignatureAlg, mobileOperator);
                binarySignable.setHashURI(profileURL);

                ISignatureResponse response =
                    signatureRequester.sendRequest(ETransIdManager.GetNewTransId(), phoneNumber, binarySignable);
                if (response == null)
                {
                    String errorMsg = "MSSP response is null";
                    Logger.Error(errorMsg);
                    throw new EMSSPException(errorMsg);
                }

                byte[] rawSignature = response.getRawSignature();

                if (rawSignature != null)
                    return rawSignature;

                byte[] mobileContentInfoBytes = response.getSignature();
                if (mobileContentInfoBytes == null)
                {
                    String errorMsg = "MSSP Signature is null. TransId: " + response.getTransId();
                    Logger.Error(errorMsg);
                    Status status = response.getStatus();
                    throw new EMSSPException(errorMsg, status);
                }

                try
                {
                    EContentInfo contentInfo = new EContentInfo(mobileContentInfoBytes);
                    byte[] value = contentInfo.getContent();

                    ESignedData eSignedData = new ESignedData(value);
                    int signerInfoCount = eSignedData.getSignerInfoCount();
                    if (signerInfoCount == 0)
                    {
                        String errorMsg = "MSSP Signature signer info count is 0. TransId: " + response.getTransId();
                        Logger.Error(errorMsg);
                        throw new EMSSPException(errorMsg);
                    }
                    ESignerInfo signerInfo = eSignedData.getSignerInfo(0);
                    rawSignature = signerInfo.getSignature();

                    signingCert = eSignedData.getCertificates()[0];
                    return rawSignature;
                }
                catch (Exception exc)
                {
                    String errorMsg = "Error while decoding mssp signature. TransId: " + response.getTransId();
                    Logger.Error(errorMsg, exc);
                    throw new EMSSPException(errorMsg, exc);
                }
            }
            catch (Exception ex)
            {
                String errorMsg = "Error while creating single signature";
                Logger.Error(errorMsg, ex);
                throw new ESYAException(errorMsg, ex);
            }
        }

        public ECertificate getSigningCert(){
            return signingCert;
        }


        public String sign(List<byte[]> dataToBeSigned, SigningMode aMode, PhoneNumberAndOperator phoneNumberAndOperator,
                    List<String> informativeText, String aSigningAlg, IAlgorithmParameterSpec aParams)
        {

            String signatureValueStr = null;
            try {
                checkSigningMode(aMode);

                String multiSignDigests = getMultiSignDigests(dataToBeSigned, aSigningAlg);
                String multiSignInformativeTexts = getMultiSignInformativeTexts(informativeText);

                ISignable binarySignable = new BinarySignable(multiSignDigests,
                        multiSignInformativeTexts, SignatureType.MULTIPLESIGNATURE, "multipart/digest", "UTF-8");

                Operator op = phoneNumberAndOperator.getOperator();
                String phoneNumber = phoneNumberAndOperator.getPhoneNumber();

                IMSSProvider msspProvider = EMSSPProviderFactory.getProvider(op);
                ISignatureRequest signatureRequester = msspProvider.getSignatureRequester(msspParams);

                String msspSignatureQueryUrl = msspParams.GetMsspSignatureQueryUrl();
                if (msspSignatureQueryUrl != null)
                {
                    signatureRequester.setServiceUrl(msspSignatureQueryUrl);
                }

                String digestAlgOfSignatureAlg = Algorithms.getDigestAlgOfSignatureAlg(aSigningAlg);
                String profileURL = getMSSProfileURL(digestAlgOfSignatureAlg, op);
                binarySignable.setHashURI(profileURL);

                String transid = ETransIdManager.GetNewTransId();
                ISignatureResponse response = signatureRequester.sendRequest(transid, phoneNumber, binarySignable);
                if (response == null)
                {
                    String errorMsg = "MSSP response is null. TransId: " + response.getTransId();
                    throw new EMSSPException(errorMsg);
                }

                byte[] mobileContentInfoBytes = response.getSignature();
                if (mobileContentInfoBytes == null)
                {
                    String errorMsg = "MSSP Signature is null. TransId: " + response.getTransId();
                    Status status = response.getStatus();
                    throw new EMSSPException(errorMsg, status);
                }

                try
                {

                    StringBuilder responseBuilder = new StringBuilder();
                    responseBuilder.Append("<SignatureRootNode>");
                    responseBuilder.Append(Encoding.ASCII.GetString(mobileContentInfoBytes));
                    responseBuilder.Append("</SignatureRootNode>");
                    XmlDocument doc = getDocumentFromBytes(Encoding.ASCII.GetBytes(responseBuilder.ToString()));
                    XmlNode signatureValueNode = doc.GetElementsByTagName("SignatureValue").Item(0);

                    byte[] signatureValueBytes = Base64.Decode(signatureValueNode.InnerText);
                    signatureValueStr = Encoding.ASCII.GetString(signatureValueBytes);

                    XmlNode certificateValueNode = doc.GetElementsByTagName("X509Certificate").Item(0);
                    byte[] certificateBytes = Base64.Decode(certificateValueNode.InnerText);

                    signingCert = new ECertificate(certificateBytes);

                }
                catch (Exception ex)
                {
                    String errorMsg = "Error while decoding multiple signature response";
                    throw new EMSSPException(errorMsg, ex);
                }

            }catch(Exception ex) {
                String errorMsg = "Error while creating multiple signature";
                throw new ESYAException(errorMsg, ex);
            }

            return signatureValueStr;
        }

        private XmlDocument getDocumentFromBytes(byte[] bytesOfDocument)
        {
            XmlDocument doc = new XmlDocument();
            String xml = Encoding.ASCII.GetString(bytesOfDocument);
            doc.LoadXml(xml);
            return doc;
			  	   	
	    }

        private String getMSSProfileURL(String digestAlgOfSignatureAlg, Operator op)
        {

            DigestAlg digestAlgorithm = DigestAlg.fromName(digestAlgOfSignatureAlg);

            String url = null;
            if (!skipMsspProfileQuery && op == Operator.TURKCELL)
            {
                String msspProfileQueryUrl = msspParams.GetMsspProfileQueryUrl();
                String mssUrl = profileInfo.getMssProfileURI();


                //Eger profil-2 degilse ba�ka bir sey vermeye gerek yok
                if (mssUrl != null && mssUrl.ToLower().Equals(TURKCELL_PROFILE_2_MSS_URL.ToLower()))
                {
                    Logger.Debug("Kullanici sim kart applet versiyonu v2");
                    if (digestAlgorithm != DigestAlg.SHA256)
                    {
                        digestAlgOfSignatureAlg = digestAlgOfSignatureAlg.Replace("-", "").ToLower();
                        mssUrl = mssUrl + "#" + digestAlgOfSignatureAlg;
                    }

                    url = mssUrl;
                }
                else if (mssUrl != null && mssUrl.ToLower().Equals(TURKCELL_PROFILE_3_MSS_URL.ToLower()))
                {
                    Logger.Debug("Kullanici sim kart applet versiyonu v3");
                    if (digestAlgorithm != DigestAlg.SHA256)
                    {
                        digestAlgOfSignatureAlg = digestAlgOfSignatureAlg.Replace("-", "").ToLower();
                        mssUrl = mssUrl + "#" + digestAlgOfSignatureAlg;
                    }

                    url = mssUrl;
                }
                else
                {
                    Logger.Debug("Kullanici sim kart applet versiyonu v1");
                    //Burasi profil-1 imza olusturma yeri ozet algoritmasi mutlaka SHA1 olmalidir.
                    if (digestAlgorithm != DigestAlg.SHA1)
                    {
                        String msg = "Kullan�c� sim kart applet versiyonu v1 oldugundan sadece SHA1 ozet algoritmasi destekleniyor. Ozet Alg: "
                                + digestAlgorithm + " , Beklenen: " + DigestAlg.SHA1;
                        //logger.error(msg);
                        //logger.error("Ozet algoritmasi = " + digestAlgorithm);
                        throw new ESYAException(msg);
                    }
                    if (msspProfileQueryUrl == null)
                    {
                        url = mssUrl;
                    }
                    else if (msspProfileQueryUrl.ToLower().Contains("mssp2"))
                    {
                        url = mssUrl;
                    }
                }
            } else if (op == Operator.TURKTELEKOM && profileInfo.getDigestAlg().Contains("sha256")) {
                String digestAlg = profileInfo.getDigestAlg();
                url = digestAlg;
                Logger.Debug("Hash URI  " + digestAlg);
            }
		
        return url;
        }

        private void checkSigningMode(SigningMode aMode)
        {
    	
    	    if (aMode != SigningMode.SIGNHASH) {
                String errorStr = "Unsupported signing mode, only SIGN_HASH supported.";
                throw new EMSSPException(errorStr);
            }
        }

        private String getMultiSignDigests(List<byte[]> dataToBeSigned, String aSigningAlg)
        {
            int signatureCount = dataToBeSigned.Count;
            StringBuilder multiSignDigests = new StringBuilder();

            for (int i = 0; i < signatureCount; i++)
            {
                string digestForSign64 = getBase64EncodedDigestForSign(dataToBeSigned[i], aSigningAlg);
                multiSignDigests.Append(digestForSign64);

                if (i + 1 != signatureCount)
                    multiSignDigests.Append(";");
            }

            return multiSignDigests.ToString();
        }

        private String getMultiSignInformativeTexts(List<String> informativeText)
        {
            int informativeTextCount = informativeText.Count;
            StringBuilder informativeTexts = new StringBuilder();
    	
		    for(int i = 0; i<informativeTextCount; i++)
	        {
                informativeTexts.Append(informativeText[i]);
	            if(i+1 != informativeTextCount)
	            informativeTexts.Append(";");
	        }
		    return informativeTexts.ToString() ;
	    }
        public String getBase64EncodedDigestForSign(byte[] dataToBeSigned, String aSigningAlg)
        {
            byte[] digestForSign = null;
            try
            {
         	    String digestAlgOfSignatureAlg = Algorithms.getDigestAlgOfSignatureAlg(aSigningAlg);
                // digestForSign = DigestUtil.digest(DigestAlg.SHA256, dataToBeSigned);
                digestForSign = DigestUtil.digest(DigestAlg.fromName(digestAlgOfSignatureAlg), dataToBeSigned);
            } catch (Exception exc) {
                Logger.Error("Error while finding hash algorithm", exc);
                throw new ESYAException(exc);
            }

            String digestForSign64 = Convert.ToBase64String(digestForSign);
            digestForSign64 = digestForSign64.Replace("\n", "");
    	
            return digestForSign64;
        }

        public Name getName()
        {
            byte[] issuerBytes = Convert.FromBase64String(profileInfo.GetCertIssuerDN());
            EName issuer = new EName(issuerBytes);
            return issuer.getObject();
        }

        public Asn1BigInteger getSerialNumber()
        {
            return new Asn1BigInteger(new BigInteger(profileInfo.GetSerialNumber(), 16));
        }

        public Asn1OctetString getCertHash()
        {
            return new Asn1OctetString(Base64.Decode(profileInfo.GetCertHash()));
        }

        public byte[] getCertHashAsByte()
        {
            return Base64.Decode(profileInfo.GetCertHash());
        }

        public DigestAlg getDigestAlg()
        {
            DigestAlg alg = DigestAlgorithm.resolve(profileInfo.getDigestAlg()) ?? DigestAlg.SHA256;
            return alg;
        }

        public ESignerIdentifier getSignerIdentifier()
        {
            try
            {
                ESignerIdentifier sid = new ESignerIdentifier(new SignerIdentifier());
                IssuerAndSerialNumber issuerAndSerial = new IssuerAndSerialNumber(getName(), getSerialNumber());
                sid.setIssuerAndSerialNumber(new EIssuerAndSerialNumber(issuerAndSerial));
                return sid;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
                return null;
            }
        }

        public SigningCertificate getSigningCertAttr()
        {
            try
            {
                ESSCertID[] ids = new ESSCertID[1];

                IssuerSerial issuerSerial = createIssuerSerial();
                ids[0] = new ESSCertID(getCertHash(), issuerSerial);
                _SeqOfESSCertID certs = new _SeqOfESSCertID(ids);
                return new SigningCertificate(certs);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
                return null;
            }
        }

        public ESigningCertificateV2 getSigningCertAttrv2()
        {
            try
            {
                ESSCertIDv2[] ids = new ESSCertIDv2[1];
                IssuerSerial issuerSerial = createIssuerSerial();
                AlgorithmIdentifier aid = new AlgorithmIdentifier(getDigestAlg().getOID());
                if (getDigestAlg() == DigestAlg.SHA256)
                {
                    aid = null;
                }

                ids[0] = new ESSCertIDv2(aid, getCertHash(), issuerSerial);
                _SeqOfESSCertIDv2 certs = new _SeqOfESSCertIDv2(ids);
                return new ESigningCertificateV2(new SigningCertificateV2(certs, null));
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.StackTrace);
                return null;
            }
        }

        public IssuerSerial createIssuerSerial()
        {
            GeneralName gn = new GeneralName();
            gn.Set_directoryName(getName());

            GeneralNames gns = new GeneralNames(1);
            gns.elements[0] = gn;

            IssuerSerial issuerSerial = new IssuerSerial(gns, getSerialNumber());
            return issuerSerial;
        }
    }
}
