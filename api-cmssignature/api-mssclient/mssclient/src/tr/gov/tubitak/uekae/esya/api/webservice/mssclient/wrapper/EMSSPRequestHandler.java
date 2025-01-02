package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

import com.objsys.asn1j.runtime.Asn1BigInteger;
import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import tr.gov.tubitak.uekae.esya.api.asn.cms.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.EUserException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Operator;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.PhoneNumberAndOperator;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.SigningMode;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Status;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider.IMSSProvider;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer.BinarySignable;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer.ISignable;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer.TextSignable;
import tr.gov.tubitak.uekae.esya.asn.cms.*;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralNames;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;

/**
 * @author ramazan.girgin
 */
public class EMSSPRequestHandler{

    public static String TURKCELL_PROFILE_2_MSS_URL="http://uri.turkcellteknoloji.com.tr/MobileSignature/profile2";
    public static String TURKCELL_PROFILE_3_MSS_URL="http://uri.turkcellteknoloji.com.tr/MobileSignature/profile3";


    private ECertificate signingCert;
    private ProfileInfo profileInfo;
    Operator operator;

    MSSParams mssParams;
    Logger logger = LoggerFactory.getLogger(EMSSPRequestHandler.class);
    boolean skipMsspProfileQuery=false;

    public EMSSPRequestHandler(MSSParams mssParams) {
        this.mssParams = mssParams;
        try
        {
            LV.getInstance().checkLD(LV.Urunler.MOBILIMZA);
        }
        catch(LE e)
        {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
    }

    public boolean isSkipMsspProfileQuery() {
        return skipMsspProfileQuery;
    }

    public void setSkipMsspProfileQuery(boolean skipMsspProfileQuery) {
        this.skipMsspProfileQuery = skipMsspProfileQuery;
    }

    public void setCertificateInitials(PhoneNumberAndOperator phoneNumberAndOperator) throws ESYAException {

    	operator = phoneNumberAndOperator.getOperator();
        String phoneNumber = phoneNumberAndOperator.getPhoneNumber();

        try {

        	IMSSProvider provider = EMSSPProviderFactory.getProvider(operator);
            IProfileRequest profileRequester = provider.getProfileRequester(mssParams);
            String msspProfileQueryUrl = mssParams.getMsspProfileQueryUrl();
            if(msspProfileQueryUrl!=null){
                profileRequester.setServiceUrl(msspProfileQueryUrl);
            }

            IProfileResponse profileResponse = profileRequester.sendRequest(phoneNumber,ETransIdManager.getNewTransId());
            if(profileResponse==null)
            {
                String errorMsg = "MSSP profile response is null";
                logger.error(errorMsg);
                throw new EMSSPException(errorMsg);
            }

            signingCert = profileResponse.getCertificate();

            Status status = profileResponse.getStatus();
            if(status == null)
                logger.debug("Status is null in profile response");
            else
            {
                String statusMsg = status.get_statusMessage() + "(" + status.get_statusCode() + ")";
                logger.debug("Profile request completed, return status = " + statusMsg);
            }

            profileInfo = profileResponse.getProfileInfo();
            if(profileInfo == null)
            {
                String errorMsg = "Profile info is null for :"+ phoneNumber;
                logger.error(errorMsg);
                throw new EMSSPException(errorMsg,status);
            }

        } catch (Exception ex) {
            logger.error("Error while setting certificate initials", ex);
            throw new ESYAException(ex);
        }
    }

    public byte[] SignText(String textForSign, PhoneNumberAndOperator phoneNumberAndOperator, SignatureType iSignatureType) throws ESYAException {
        try {
            if (textForSign.length() > 255) {
                String errorMsg = "Text size too long. Max 255 character.";
                logger.error(errorMsg);
                throw new EMSSPException(errorMsg);
            }

            SignatureType signatureType = SignatureType.PKCS7;
            if (iSignatureType != null) {
                signatureType = iSignatureType;
            }

            Operator mobileOperator = phoneNumberAndOperator.getOperator();
            String phoneNumber = phoneNumberAndOperator.getPhoneNumber();

            IMSSProvider msspProvider = EMSSPProviderFactory.getProvider(mobileOperator);
            ISignatureRequest signatureRequester = msspProvider.getSignatureRequester(mssParams);

            String msspSignatureQueryUrl = mssParams.getMsspSignatureQueryUrl();
            if (msspSignatureQueryUrl != null) {
                signatureRequester.setServiceUrl(msspSignatureQueryUrl);
            }

            ISignable signable = new TextSignable(textForSign, signatureType);

            String transid = ETransIdManager.getNewTransId();
            ISignatureResponse response = signatureRequester.sendRequest(transid, phoneNumber, signable);
            if (response == null) {
                String errorMsg = "MSSP signature response is null";
                logger.error(errorMsg);
                throw new EMSSPException(errorMsg);
            }

            byte[] signature = response.getSignature();
            if (signature == null) {
                String errorMsg = "MSSP Signature is null";
                logger.error(errorMsg);
                Status status = response.getStatus();
                throw new EMSSPException(errorMsg, status);
            }
            return signature;
        } catch (Exception e) {
            throw new ESYAException(e);
        }
    }

    public byte[] sign(byte[] dataToBeSigned, SigningMode aMode,
                       PhoneNumberAndOperator phoneNumberAndOperator, String informativeText, String aSigningAlg, AlgorithmParameterSpec aParams) throws ESYAException, NoSuchAlgorithmException {
        try {
            checkSigningMode(aMode);

            String digestForSign64 = getBase64EncodedDigestForSign(dataToBeSigned, aSigningAlg);
            ISignable binarySignable = new BinarySignable(digestForSign64, informativeText, SignatureType.PKCS7);

            Operator operator = phoneNumberAndOperator.getOperator();
            String phoneNumber = phoneNumberAndOperator.getPhoneNumber();

            IMSSProvider msspProvider = EMSSPProviderFactory.getProvider(operator);
            ISignatureRequest signatureRequester = msspProvider.getSignatureRequester(mssParams);

            String msspSignatureQueryUrl = mssParams.getMsspSignatureQueryUrl();
            if (msspSignatureQueryUrl != null) {
                signatureRequester.setServiceUrl(msspSignatureQueryUrl);
            }

            String digestAlgOfSignatureAlg = Algorithms.getDigestAlgOfSignatureAlg(aSigningAlg);
            String profileURL = getMSSProfileURL(digestAlgOfSignatureAlg, operator);
            binarySignable.setHashURI(profileURL);

            ISignatureResponse response = signatureRequester.sendRequest(ETransIdManager.getNewTransId(), phoneNumber, binarySignable);
            if (response == null) {
                String errorMsg = "MSSP response is null";
                logger.error(errorMsg);
                throw new EMSSPException(errorMsg);
            }

            Status status = response.getStatus();

            if (!Status.REQUEST_OK.isStatusCodeEquals(status)) {
                logger.debug("Status is not OK: {}. TransID: {}", status, response.getTransId());

                if (Status.USER_CANCEL.isStatusCodeEquals(status)) {
                    throw new EUserException(EUserException.USER_CANCELLED, GenelDil.mesaj(GenelDil.MOBIL_IMZA_KULLANICI_IPTALI));
                } else if (Status.EXPIRED_TRANSACTION.isStatusCodeEquals(status)) {
                    throw new EUserException(EUserException.TIMEOUT, GenelDil.mesaj(GenelDil.MOBIL_IMZA_ZAMAN_ASIMI));
                }
            }

            byte[] rawSignature = response.getRawSignature();

            if (rawSignature == null) {
                byte[] mobileContentInfoBytes = response.getSignature();
                if (mobileContentInfoBytes == null) {
                    String errorMsg = "MSSP Signature is null. TransId: " + response.getTransId();
                    logger.error(errorMsg);
                    throw new EMSSPException(errorMsg, status);
                }

                try {
                    EContentInfo contentInfo = new EContentInfo(mobileContentInfoBytes);
                    byte[] value = contentInfo.getContent();

                    ESignedData eSignedData = new ESignedData(value);

                    int signerInfoCount = eSignedData.getSignerInfoCount();
                    if (signerInfoCount == 0) {
                        String errorMsg = "MSSP Signature signer info count is 0. TransId: " + response.getTransId();
                        logger.error(errorMsg);
                        throw new EMSSPException(errorMsg);
                    }

                    ESignerInfo signerInfo = eSignedData.getSignerInfo(0);
                    rawSignature = signerInfo.getSignature();

                    signingCert = eSignedData.getCertificates().get(0);

                    return rawSignature;

                } catch (Exception exc) {
                    String errorMsg = "Error while decoding mssp signature. TransId: " + response.getTransId();
                    logger.error(errorMsg, exc);
                    throw new EMSSPException(errorMsg, exc);
                }
            }

            return rawSignature;

        } catch (EUserException ex) {
            throw ex;
        } catch (Exception ex) {
            String errorMsg = "Error while creating single signature";
            logger.error(errorMsg, ex);
            throw new ESYAException(errorMsg, ex);
        }
    }

    public String sign(ArrayList<byte[]> dataToBeSigned, SigningMode aMode, PhoneNumberAndOperator phoneNumberAndOperator,
                       ArrayList<String> informativeText, String aSigningAlg, AlgorithmParameterSpec aParams) throws ESYAException {

        String signatureValueStr = null;
        try {
            checkSigningMode(aMode);

            String multiSignDigests = getMultiSignDigests(dataToBeSigned, aSigningAlg);
            String multiSignInformativeTexts = getMultiSignInformativeTexts(informativeText);

            ISignable binarySignable = new BinarySignable(multiSignDigests,
                    multiSignInformativeTexts, SignatureType.MULTIPLESIGNATURE, "multipart/digest", "UTF-8");

            Operator operator = phoneNumberAndOperator.getOperator();
            String phoneNumber = phoneNumberAndOperator.getPhoneNumber();

            IMSSProvider msspProvider = EMSSPProviderFactory.getProvider(operator);
            ISignatureRequest signatureRequester = msspProvider.getSignatureRequester(mssParams);

            String msspSignatureQueryUrl = mssParams.getMsspSignatureQueryUrl();
            if (msspSignatureQueryUrl != null) {
                signatureRequester.setServiceUrl(msspSignatureQueryUrl);
            }

            String digestAlgOfSignatureAlg = Algorithms.getDigestAlgOfSignatureAlg(aSigningAlg);
            String profileURL = getMSSProfileURL(digestAlgOfSignatureAlg, operator);
            binarySignable.setHashURI(profileURL);

            String transid = ETransIdManager.getNewTransId();
            ISignatureResponse response = signatureRequester.sendRequest(transid, phoneNumber, binarySignable);
            if (response == null) {
                String errorMsg = "MSSP response is null. TransId: " + response.getTransId();
                logger.error(errorMsg);
                throw new EMSSPException(errorMsg);
            }

            Status status = response.getStatus();
            if (!Status.REQUEST_OK.isStatusCodeEquals(status)) {
                logger.debug("Status is not OK: {}. TransID: {}", status, response.getTransId());
            }

            byte[] mobileContentInfoBytes = response.getSignature();
            if (mobileContentInfoBytes == null) {
                String errorMsg = "MSSP Signature is null. TransId: " + response.getTransId();
                logger.error(errorMsg);
                throw new EMSSPException(errorMsg, status);
            }

            try {

                StringBuilder responseBuilder = new StringBuilder();
                responseBuilder.append("<SignatureRootNode>");
                responseBuilder.append(new String(mobileContentInfoBytes));
                responseBuilder.append("</SignatureRootNode>");

                Document doc = getDocumentFromBytes(responseBuilder.toString().getBytes());
                Node signatureValueNode = doc.getElementsByTagName("SignatureValue").item(0);

                byte [] signatureValueBytes = Base64.decode(signatureValueNode.getTextContent());
                signatureValueStr = new String(signatureValueBytes);

                Node certificateValueNode = doc.getElementsByTagName("X509Certificate").item(0);
                byte [] certificateBytes = Base64.decode(certificateValueNode.getTextContent());

                signingCert = new ECertificate(certificateBytes);

            } catch (Exception ex) {
                String errorMsg = "Error while decoding multiple signature response. TransId" + response.getTransId();
                logger.error(errorMsg, ex);
                throw new EMSSPException(errorMsg, ex);
            }

        }catch(Exception ex) {
            String errorMsg = "Error while creating multiple signature";
            logger.error(errorMsg, ex);
            throw new ESYAException(errorMsg, ex);
        }

        return signatureValueStr;
    }

    private Document getDocumentFromBytes(byte[] bytesOfDocument) throws Exception{

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();

		    return builder.parse(new ByteArrayInputStream(bytesOfDocument));

    }

	private String getMultiSignDigests(ArrayList<byte[]> dataToBeSigned, String aSigningAlg) throws ESYAException {

		String digestForSign64 = null;
		int signatureCount = dataToBeSigned.size();
        StringBuilder multiSignDigests = new StringBuilder();

		 for(int i=0; i < signatureCount; i++)
	     {
			 digestForSign64 = getBase64EncodedDigestForSign(dataToBeSigned.get(i), aSigningAlg);
			 multiSignDigests.append(digestForSign64);

             if(i+1 != signatureCount)
	            multiSignDigests.append(";");
	     }
		return multiSignDigests.toString();
	}

    private String getMultiSignInformativeTexts(ArrayList<String> informativeText) {

       int informativeTextCount = informativeText.size();
    	StringBuilder informativeTexts = new StringBuilder();

		 for(int i=0; i < informativeTextCount; i++)
	     {
			    informativeTexts.append(informativeText.get(i));
	            if(i+1 != informativeTextCount)
	            	informativeTexts.append(";");

	     }
		return informativeTexts.toString() ;
	}

    public boolean isMultipleSignSupported(){
        if(operator != null && operator == Operator.TURKCELL){
            if(profileInfo != null && profileInfo.getMssProfileURI().contains("profile3")){
                return true;
            }
        }
        return false;
    }

    private String getMSSProfileURL(String digestAlgOfSignatureAlg, Operator operator) throws ESYAException {

        DigestAlg digestAlgorithm = DigestAlg.fromName(digestAlgOfSignatureAlg);

        String url = null;
        if (!skipMsspProfileQuery && operator == Operator.TURKCELL) {
            String msspProfileQueryUrl = mssParams.getMsspProfileQueryUrl();
            String mssUrl = profileInfo.getMssProfileURI();

            // Eğer profil-2 değilse başka bir şey vermeye gerek yok
            if (mssUrl != null && mssUrl.equalsIgnoreCase(TURKCELL_PROFILE_2_MSS_URL)) {
                logger.debug("Kullanıcı SIM kart applet versiyonu v2");
                if (digestAlgorithm != DigestAlg.SHA256) {
                    digestAlgOfSignatureAlg = digestAlgOfSignatureAlg.replaceAll("-", "").toLowerCase();
                    mssUrl = mssUrl + "#" + digestAlgOfSignatureAlg;
                }

                url = mssUrl;
            } else if (mssUrl != null && mssUrl.equalsIgnoreCase(TURKCELL_PROFILE_3_MSS_URL)) {
                logger.debug("Kullanıcı SIM kart applet versiyonu v3");
                 if (digestAlgorithm != DigestAlg.SHA256) {
                     digestAlgOfSignatureAlg = digestAlgOfSignatureAlg.replaceAll("-", "").toLowerCase();
                     mssUrl = mssUrl + "#" + digestAlgOfSignatureAlg;
                 }

                 url = mssUrl;
            } else {
                logger.debug("Kullanıcı SIM kart applet versiyonu v1");
                // Burası profil-1 imza oluşturma yeri özet algoritması mutlaka SHA1 olmalıdır.
                if (digestAlgorithm != DigestAlg.SHA1) {
                    String msg = "Kullanıcı SIM kart applet versiyonu v1 olduğundan sadece SHA1 özet algoritması destekleniyor. Özet alg.: "
                        + digestAlgorithm + ", beklenen: " + DigestAlg.SHA1;
                    logger.error(msg);
                    logger.error("Özet algoritması = " + digestAlgorithm);
                    throw new ESYAException(msg);
                }
                if (msspProfileQueryUrl == null) {
                    url = mssUrl;
                } else if (msspProfileQueryUrl.toLowerCase().contains("mssp2")) {
                    url= mssUrl;
                }
            }
        } else if (operator == Operator.TURKTELEKOM && profileInfo.digestAlg.contains("sha256")) {
            url = profileInfo.digestAlg;
        } else if (operator == Operator.VODAFONE) {
            // todo diğer algoritmalarla da kontrol etmeye gerek var mı?
            if (digestAlgorithm == DigestAlg.SHA256) {
                url = profileInfo.getDigestAlg();
        }
        }
        logger.debug("Hash URI: " + url);
        return url;
	}

    private void checkSigningMode(SigningMode aMode) throws EMSSPException {

    	if (aMode != SigningMode.SIGNHASH) {
            String errorStr = "Unsupported signing mode, only SIGN_HASH supported.";
            logger.error(errorStr);
            throw new EMSSPException(errorStr);
        }
    }

    public String getBase64EncodedDigestForSign(byte[] dataToBeSigned, String aSigningAlg) throws ESYAException {

    	 byte[] digestForSign = null;
         try {

         	String digestAlgOfSignatureAlg = Algorithms.getDigestAlgOfSignatureAlg(aSigningAlg);
             MessageDigest digester = MessageDigest.getInstance(digestAlgOfSignatureAlg);
             digester.update(dataToBeSigned);
             digestForSign = digester.digest();
         } catch (Exception exc) {
             logger.error("Error while finding hash algorithm", exc);
             throw new ESYAException(exc);
         }

         String digestForSign64 = Base64.encode(digestForSign);
         digestForSign64 = digestForSign64.replaceAll("\n", "");

         return digestForSign64;
    }

	public ECertificate getSigningCert(){
    	return signingCert;
    }

    public Name getName() throws ESYAException {
        try {
            byte[] certIssuerBytes = profileInfo.getCertIssuerDN().getBytes();

            // try interpreting as Base64 (otherwise it will be used as is)
            try {
                certIssuerBytes = Base64.decode(certIssuerBytes, 0, certIssuerBytes.length);
            } catch (IllegalArgumentException ignored) {
            }

            EName issuer = new EName(certIssuerBytes);
            return issuer.getObject();
        } catch (Exception e) {
            throw new ESYAException(e);
        }
    }

    public Asn1BigInteger getSerialNumber() throws ESYAException {
        try {
            return new Asn1BigInteger(new BigInteger(profileInfo.getSerialNumber(), 16));
        } catch (Exception e) {
            throw new ESYAException(e);
        }

    }

    public Asn1OctetString getCertHash() throws ESYAException {
        try {

            return new Asn1OctetString(Base64.decode(profileInfo.getCertHash()));
        } catch (Exception e) {
            throw new ESYAException(e);
        }
    }

    public byte[] getCertHashAsByte() {
        return Base64.decode(profileInfo.getCertHash());
    }

    public DigestAlg getDigestAlg() {
        DigestAlg alg = DigestAlgorithm.resolve(profileInfo.getDigestAlg());
        if (alg == null)
            alg = DigestAlg.SHA1; //TODO hash boyutuna g�re bak�labilir.
        return alg;
    }

    public ESignerIdentifier getSignerIdentifier() {
        try {
            ESignerIdentifier sid = new ESignerIdentifier(new SignerIdentifier());
            IssuerAndSerialNumber issuerAndSerial = new IssuerAndSerialNumber(getName(), getSerialNumber());
            sid.setIssuerAndSerialNumber(new EIssuerAndSerialNumber(issuerAndSerial));
            return sid;
        } catch (Exception e) {
            logger.error("Error in EMSSPRequestHandler", e);
            return null;
        }
    }

	public SigningCertificate getSigningCertAttr() {
		try {
			ESSCertID[] ids = new ESSCertID[1];

			IssuerSerial is = createIssuerSerial();
			ids[0] = new ESSCertID(getCertHash(), is);
			_SeqOfESSCertID certs = new _SeqOfESSCertID(ids);
			return new SigningCertificate(certs);
		} catch (Exception e) {
            logger.error("Error in EMSSPRequestHandler", e);
			return null;
		}
	}

	public ESigningCertificateV2 getSigningCertAttrv2() {
		try {
			ESSCertIDv2[] ids = new ESSCertIDv2[1];
			IssuerSerial is = createIssuerSerial();
            AlgorithmIdentifier aid = new AlgorithmIdentifier(getDigestAlg().getOID());
            if(getDigestAlg() == DigestAlg.SHA256){
          	  aid = null;
            }
			ids[0] = new ESSCertIDv2(aid, getCertHash(), is);
			_SeqOfESSCertIDv2 certs = new _SeqOfESSCertIDv2(ids);
            SigningCertificateV2 signingCertificateV2 = new SigningCertificateV2(certs, null);
            return new ESigningCertificateV2(signingCertificateV2);
		} catch (Exception e) {
            logger.error("Error in EMSSPRequestHandler", e);
			return null;
		}
	}
	public IssuerSerial createIssuerSerial() throws ESYAException
	{
		GeneralName gn = new GeneralName();
        gn.set_directoryName(getName());

        GeneralNames gns = new GeneralNames(1);
        gns.elements[0] = gn;

        IssuerSerial is = new IssuerSerial(gns, getSerialNumber());
        return is;
	}
}
