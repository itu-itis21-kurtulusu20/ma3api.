package tr.gov.tubitak.uekae.esya.api.infra.ocsp;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV.Urunler;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.infra.util.HashMultiMap;
import tr.gov.tubitak.uekae.esya.asn.ocsp.*;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.CRLReason;
import tr.gov.tubitak.uekae.esya.asn.x509.Extension;
import tr.gov.tubitak.uekae.esya.asn.x509.Extensions;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: OCSP client </p>
 * <p>Description: RFC 2560 X.509 Internet Public Key Infrastructure
 * Online Certificate Status Protocol standard is implemented
 * </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: TUBITAK/UEKAE</p>
 *
 * @author IH
 * @version 1.0
 */

public class OCSPClient 
{
	protected static Logger logger = LoggerFactory.getLogger(OCSPClient.class);
	//silinme sebepleri
	public static final int REASON_UNSPECIFIED = CRLReason._UNSPECIFIED;
	public static final int REASON_KEY_COMPROMISE = CRLReason._KEYCOMPROMISE;
	public static final int REASON_CA_COMPROMISE = CRLReason._CACOMPROMISE;
	public static final int REASON_AFFILIATION_CHANGED = CRLReason._AFFILIATIONCHANGED;
	public static final int REASON_SUPERSEDED = CRLReason._SUPERSEDED;
	public static final int REASON_CESSATION_OF_OPERATION = CRLReason._CESSATIONOFOPERATION;
	public static final int REASON_CERTIFICATE_HOLD = CRLReason._CERTIFICATEHOLD;
	public static final int REASON_PRIVILEGE_WITHDRAWN= CRLReason._PRIVILEGEWITHDRAWN;
	public static final int REASON_AA_COMPROMISE = CRLReason._AACOMPROMISE;

	public static final int STATUS_RESPONSE_HAS_INVALID_SIGNATURE = 1;
	public static final int STATUS_RESPONSE_CANT_BE_RECEIVED = 2;
	public static final int STATUS_RESPONCE_INVALID_NONCE = 3;

	private static final String CONTENT_TYPE = "Content-Type";
	private static final String CONTENT_TYPE_OCSP_REQUEST = "application/ocsp-request";
	//private static final String CONTENT_TYPE_OCSP_RESPONSE = "application/ocsp-response";

	private static final String USER_AGENT = "User-Agent";
	private static final String USER_AGENT_OCSP = "UEKAE OCSP Client";

	//private static final String CONTENT_LENGTH = "Content-Length";
	//private static final String HOST = "Host";
	//private static final String KEEP_ALIVE = "Proxy-Connection: Keep-Alive";

	private static final String METHOD = "GET";
	//private static final String PROTOCOL_1_1 = "HTTP/1.1";
	//private static final String PROTOCOL_1_0 = "HTTP/1.0";
	//private static final String STATUS_CODE = "200";

	public static final Asn1ObjectIdentifier NONCE_OID = new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_nonce);

	private int mResponseStatus;
	protected HashMultiMap<BigInteger, ESingleResponse> singleResponses;
	
	protected ECertificate signingCertificate;
	protected EBasicOCSPResponse mBasicResponse;
	protected EOCSPResponse mOCSPResponce;

	private static final Logger LOGGER = LoggerFactory.getLogger(OCSPClient.class);

	private String mHost = null;
	private HttpURLConnection mCon = null;
	private URL mURL = null;
	private byte[] mNonce = null;
	
	private int mDurum = 0;
	
	private boolean mCheckSignature = true;
	private boolean mCheckNonce = true;
	private boolean mCheckResponseStatus = true;
	private boolean mCheckDate = false;
	public static DigestAlg digestAlgForOcspRequest = DigestAlg.SHA256;
	
	protected OCSPClient()
	{
		singleResponses = new HashMultiMap<BigInteger, ESingleResponse>();
		try 
		{
			LV.getInstance().checkLD(Urunler.ORTAK);
		}
		catch (LE ex) 
		{
			throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
		}
	}

	/**
	 * @param aConnectionAddress Adress to the OCSP server
	 * ECertificate object getOCSPAdresses() function gives addresses.
	 * @throws ESYAException 
	 */
	public OCSPClient(String aConnectionAddress) throws ESYAException 
	{
		this();
		try 
		{
			mURL = new URL(aConnectionAddress);
		}
		catch (MalformedURLException aEx) 
		{
			throw new ESYAException(aEx);
		}
		mHost = aConnectionAddress;
	}
	
	/**
	 * Defines whether ocsp response signature will be checked
	 * Default: true
	 * @param aCheckSignature
	 */
	public void setCheckSignature(boolean aCheckSignature) 
	{
		mCheckSignature = aCheckSignature;
	}

	/**
	 * Defines whether ocsp response nonce will be checked
	 * Default: true
	 * @param aCheckNonce
	 */
	public void setCheckNonce(boolean aCheckNonce) 
	{
		mCheckNonce = aCheckNonce;
	}

	/**
	 * Defines whether ocsp response status will be checked
	 * @param aCheckResponseStatus
	 */
	public void setCheckResponseStatus(boolean aCheckResponseStatus) 
	{
		mCheckResponseStatus = aCheckResponseStatus;
	}

	/**
	 * Checks thisUpdate and nextUpdate interval in OCSP response covers now
	 * Default: false
	 * @param aCheckDate
	 */
	public void setCheckDate(boolean aCheckDate) 
	{
		mCheckDate = aCheckDate;
	}

	/**
	 * Opens connection with default timeout
	 * @throws ESYAException
	 */
	public void openConnection() throws ESYAException 
	{
		openConnection(null);
	}

	/**
	 * Closes connection
	 * @throws ESYAException
	 */
	public void closeConnection() throws ESYAException 
	{
		mCon.disconnect();
	}

	/**
	 * Open connection with defined timeout
	 * @param aTimeOut timeout in miliseconds. If it is null, default value is used. A timeout of zero is
     * intreped as an infinite timeout. 
	 * If 
	 * @throws ESYAException
	 */
	public void openConnection(String aTimeOut) throws ESYAException 
	{
		try 
		{
			mCon = (HttpURLConnection) mURL.openConnection();
			
			if (aTimeOut != null) 
			{
				mCon.setConnectTimeout(Integer.parseInt(aTimeOut));
				mCon.setReadTimeout(Integer.parseInt(aTimeOut));
			}
			mCon.setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_OCSP_REQUEST);
			mCon.setRequestProperty(USER_AGENT, USER_AGENT_OCSP);
			mCon.setRequestMethod(METHOD);
			mCon.setDoOutput(true);
			mCon.setUseCaches(false);

			mCon.connect();
		} 
		catch (IOException aEx) 
		{
			LOGGER.error("OCSP servera bağlanılırken hata oluştu:" + mHost, aEx);
			throw new ESYAException("OCSP servera bağlanılamadı:" + mHost, aEx);
		}
	}

	/**
	 * Creation time of ocsp response
	 * @return
	 */
	public Calendar getProducedAt() 
	{
		return mBasicResponse.getProducedAt();
	}

	/**
	 * Make single query.
	 *
	 * @param aCertificateToQuery Certificate
	 * @param aIssuerCertificate  Certificate
	 * @throws ESYAException
	 */
	public void queryCertificate(ECertificate aCertificateToQuery,
			ECertificate aIssuerCertificate) throws ESYAException
	{
		queryCertificate(new ECertificate[]{aCertificateToQuery}, new ECertificate[]{aIssuerCertificate});
	}

	/**
	 * Make multiple query at once.
	 *
	 * @param aCertificatesToQuery Certificate[]
	 * @param aIssuerCertificates  Certificate[]
	 * @throws ESYAException
	 */
	public EOCSPResponse queryCertificate(ECertificate[] aCertificatesToQuery,
			ECertificate[] aIssuerCertificates) throws ESYAException 
	{
		if (mCon == null) 
		{
			LOGGER.error("Servera bağlantı kurulmamış.");
			throw new ESYAException("Connection is not set");
		}
		
		
	
		
		//create OCSP request
		byte[] istek = _createRequest(aCertificatesToQuery, aIssuerCertificates);
		
		//sunucuya gönderip cevabı al
		_runProtokol(istek);
		LOGGER.debug("OCSP protokolu başarıyla tamamlandı");
		return mOCSPResponce;
	}

	public EOCSPResponse queryCertificate(BigInteger aSertifikaSeriNo, byte[] aSMSubjectHashDegeri, byte[] aSMAcikAnahtarHashDegeri) throws ESYAException 
	{
		if (mCon == null)
		{
			LOGGER.error("Servera bağlantı kurulmamış.");
			throw new ESYAException("Sertifika sorgulamadan önce servera bağlantı kurulmalı");
		}

		//OCSP isteği oluştur
		byte[] istek = _createRequest(aSertifikaSeriNo, aSMSubjectHashDegeri, aSMAcikAnahtarHashDegeri);
		//sunucuya gönderip cevabı al
		_runProtokol(istek);
		LOGGER.debug("OCSP protokolu başarıyla tamamlandı");
		return mOCSPResponce;
	}

	public int getStatus()
	{
		return mDurum;
	}

	private EOCSPResponse _runProtokol(byte[] aIstek) throws ESYAException 
	{
		//send request
		DataOutputStream output;
		try 
		{
			output = new DataOutputStream(mCon.getOutputStream());
			output.write(aIstek);
			output.flush();
			output.close();
		} 
		catch (IOException aEx)
		{
			LOGGER.error("İstek gönderilirken hata oluştu", aEx);
			throw new ESYAException("İstek gönderilirken hata oluştu", aEx);
		}

		//cevabi al
		try 
		{
			LOGGER.debug("Cevap alınacak");
			mOCSPResponce = _getResponse();
			LOGGER.debug("Cevap alındı");
		} 
		catch (Exception aEx) 
		{
			mDurum = STATUS_RESPONSE_CANT_BE_RECEIVED;
			LOGGER.error("OCSP serverdan cevap alınırken hata oluştu", aEx);
			throw new ESYAException("OCSP serverdan cevap alınamadı", aEx);
		}

		//cevabin statusunu kontrol et
		if (mCheckResponseStatus)
		{
			if (checkResponseStatus()) 
			{
				//sertifikanin statusunu, silinme tarihini(silinmisse) al
				_checkResponse(mOCSPResponce);
			}
			else 
			{
				mOCSPResponce = null;
				LOGGER.error("Cevap başarılı değil");
				throw new ESYAException("Responce status is not valid"); //cevap successful degil
			}
		}
		
		return mOCSPResponce;
	}

	/**
	 * OCSP request oluşturur.
	 *
	 * @param aSorgulanacakSertifikalar Certificate[]
	 * @param aSMSertifikalari          Certificate[]
	 * @return byte[]
	 * @throws ESYAException 
	 */
	private byte[] _createRequest(ECertificate[] aSorgulanacakSertifikalar, ECertificate[] aSMSertifikalari) throws ESYAException 
	{
		LOGGER.debug("OCSP isteği oluşturulacak");

		if ((aSorgulanacakSertifikalar == null) || (aSorgulanacakSertifikalar.length == 0)) 
		{
			LOGGER.error("Kontrol edilecek sertifika yok");
			throw new ESYAException("No certificate to query");
		}
		
		int len = aSorgulanacakSertifikalar.length;
		Request[] requests = new Request[len];
		//Create ocsp requests for each certificate
		for (int i = 0; i < len; i++) 
		{
			ECertificate cert = aSorgulanacakSertifikalar[i];
			ECertificate smcert = aSMSertifikalari[i];
			try 
			{
				requests[i] = _createRequest(cert, smcert);
			}
			catch (Exception aEx) 
			{
				LOGGER.error("İstek oluşturulamadı", aEx);
				throw new ESYAException("query can't be generated", aEx);
			}
		}
		return _createOcspRequest(requests);
	}

	private byte[] _createRequest(BigInteger aSertifikaSeriNo, byte[] aSMSubjectHashDegeri, byte[] aSMAcikAnahtarHashDegeri) throws ESYAException 
	{
		Request[] requests = new Request[1];
		try 
		{
			requests[0] = _createRequestObject(aSertifikaSeriNo, aSMSubjectHashDegeri, aSMAcikAnahtarHashDegeri);
		} 
		catch (Exception aEx) 
		{
			LOGGER.error("İstek oluşturulamadı", aEx);
			return null;
		}
		return _createOcspRequest(requests);
	}

	private byte[] _createOcspRequest(Request[] aRequests) throws ESYAException
	{
		OCSPRequest ocsprequest = new OCSPRequest();

		Extension nonce;
		TBSRequest tbsrequest = new TBSRequest();
		try 
		{
			nonce = _createNonce();
		} 
		catch (Exception aEx) 
		{
			LOGGER.warn("Nonce extension oluşturulamadı", aEx);
			throw new ESYAException("Nonce extension can't be generated");
		}
		tbsrequest.requestList = new _SeqOfRequest(aRequests);
		tbsrequest.requestExtensions = new Extensions(new Extension[]{nonce});
		tbsrequest.version = new Version(0); //version 1
		ocsprequest.tbsRequest = tbsrequest;

		//encode request
		Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();

		try 
		{
			ocsprequest.encode(encBuf);
		}
		catch (Exception ex)
		{
			LOGGER.error("OCSP istek mesaji olusturulurken hata olustu", ex);
			throw new ESYAException("query can't be generated");
		}
		byte[] requestArr = encBuf.getMsgCopy();
		LOGGER.debug("OCSP isteği başarıyla oluşturuldu");
		return requestArr;
	}

	/**
	 * İsteğe eklenecek nonce oluşturur.
	 *
	 * @return Extension
	 * @throws Exception
	 */
	private Extension _createNonce() throws ESYAException
	{
		try {
			Extension nonce = new Extension();
			nonce.extnID = NONCE_OID;
			mNonce = new byte[16];
			java.security.SecureRandom.getInstance("SHA1PRNG").nextBytes(mNonce);
			Asn1OctetString value = new Asn1OctetString(mNonce);
			Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
			value.encode(encBuf);
			nonce.extnValue = new Asn1OctetString(encBuf.getMsgCopy());
			return nonce;
		}catch (Exception e){
			throw new ESYAException(e);
		}
	}

	/**
	 * Receive OCSP response.
	 *
	 * @return OCSPResponse
	 * @throws ESYAException
	 */
	private EOCSPResponse _getResponse() throws ESYAException 
	{
		OCSPResponse response = new OCSPResponse();
		InputStream input = null;
		try 
		{
			input = mCon.getInputStream();
			Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(input);
			response.decode(decBuf);
			
			LOGGER.debug("OCSP mesaji basariyla decode edildi");
		}
		catch (IOException ex) 
		{
			LOGGER.error("OCSP cevap mesajini alirken hata olustu", ex);
			throw new ESYAException("OCSP cevap mesajini alirken hata olustu", ex);
		}
		catch (Exception ex)
		{
			LOGGER.error("OCSP cevap mesaji decode edilirken hata olustu", ex);
			throw new ESYAException("OCSP cevap mesaji decode edilirken hata olustu", ex);
		}
		return new EOCSPResponse(response);
	}

	/**
	 * Create OCSP request .
	 *
	 * @param aCertificateToCheck Certificate
	 * @param aIssuerCertificate  Certificate
	 * @return Request
	 * @throws Exception
	 */
	private Request _createRequest(ECertificate aCertificateToCheck, ECertificate aIssuerCertificate) throws Exception 
	{
		
		BigInteger sertifikaSeriNo =  aCertificateToCheck.getSerialNumber();
		byte[] nameHash = DigestUtil.digest(digestAlgForOcspRequest, aIssuerCertificate.getSubject().getEncoded());
		byte[] keyHash = DigestUtil.digest(digestAlgForOcspRequest, aIssuerCertificate.getSubjectPublicKeyInfo().getSubjectPublicKey());

		return _createRequestObject(sertifikaSeriNo, nameHash, keyHash);
	}

	/**
	 * Create OCSP request .
	 *
	 * @param aCertificateSerialNumber    Serial number of certificate to be checked
	 * @param aIssuerSubjectHash        Issuer subject hash
	 * @param aIssuerPublicKeyHash      Issuer public key hash
	 * @return
	 * @throws Exception
	 */
	private Request _createRequestObject(BigInteger aCertificateSerialNumber, byte[] aIssuerSubjectHash, byte[] aIssuerPublicKeyHash) throws ESYAException {
		CertID certID = new CertID();

		AlgorithmIdentifier hashAlgorithm = new AlgorithmIdentifier(digestAlgForOcspRequest.getOID());
		Asn1OctetString issuerNameHash = new Asn1OctetString(aIssuerSubjectHash);
		Asn1OctetString issuerKeyHash = new Asn1OctetString(aIssuerPublicKeyHash);
		Asn1BigInteger serialNumber = new Asn1BigInteger(aCertificateSerialNumber);

		certID.hashAlgorithm = hashAlgorithm;
		certID.issuerNameHash = issuerNameHash;
		certID.issuerKeyHash = issuerKeyHash;
		certID.serialNumber = serialNumber;

		return new Request(certID);
	}

	/**
	 * ResponseStatus.
	 * 0:successful
	 *
	 * @return response status
	 */
	public int getResponseStatus() 
	{
		return mResponseStatus;
	}

	

	public ESingleResponse getSingleResponse(ECertificate aCertificateToQuery,
			ECertificate aIssuerCertificate) throws ESYAException 
	{
		List<ESingleResponse> responses = singleResponses.get(aCertificateToQuery.getSerialNumber());
		if(responses == null)
			throw new ESYAException("OCSP response does not contain requsted certificate response");
		for (ESingleResponse response : responses) 
		{
			ECertID certID = response.getCertID();
			if(isEqual(certID, aCertificateToQuery, aIssuerCertificate))
				return response;
		}
		
		throw new ESYAException("OCSP response does not contain requsted certificate response");
		
	}
	
	/**
	 * Checks whether certID is generated for the aCertificateToQuery and the aIssuerCertificate.
	 * @param certID
	 * @param aCertificateToQuery
	 * @param aIssuerCertificate
	 * @return
	 */
	public static boolean isEqual(ECertID certID, ECertificate aCertificateToQuery,
			ECertificate aIssuerCertificate) 
	{
		if(certID.getSerialNumber().equals(aCertificateToQuery.getSerialNumber()))
		{
			try
			{
				byte [] issuerName = aIssuerCertificate.getSubject().getEncoded();
				byte [] key = aIssuerCertificate.getSubjectPublicKeyInfo().getSubjectPublicKey();
				
				DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(certID.getHashAlgorithm());
				byte[] nameHash = DigestUtil.digest(digestAlg, issuerName);
				byte[] keyHash = DigestUtil.digest(digestAlg, key);
				
				if(	Arrays.equals(certID.getIssuerNameHash(), nameHash)
						&& Arrays.equals(certID.getIssuerKeyHash(), keyHash))
					return true;
			}
			catch(CryptoException ex)
			{
				logger.warn("Warning in OCSPClient", ex);
				return false;
			}
		}
		return false;
	}

	

	public EBasicOCSPResponse getBasicResponse() 
	{
		return mBasicResponse;
	}

	public EOCSPResponse getOCSPResponse()
	{
		return mOCSPResponce;
	}

	/**
	 * Check Response status.
	 *
	 * @return
	 */
	public boolean checkResponseStatus() 
	{
		int status = mOCSPResponce.getResponseStatus();
		mResponseStatus = status;
		LOGGER.debug("Gelen cevap status:" + status);
		switch (status) {
		case OCSPResponseStatus._SUCCESSFUL:
			LOGGER.debug("Cevap başarılı");
			return true; //successful
		case OCSPResponseStatus._MALFORMEDREQUEST:
			LOGGER.debug("Cevap status: malformedRequest");
			return false; //malformedRequest
		case OCSPResponseStatus._INTERNALERROR:
			LOGGER.debug("Cevap status: internalError");
			return false; //internalError
		case OCSPResponseStatus._TRYLATER:
			LOGGER.debug("Cevap status: tryLater");
			return false; //tryLater
		case OCSPResponseStatus._SIGREQUIRED:
			LOGGER.debug("Cevap status: sigRequired");
			return false; //sigRequired
		case OCSPResponseStatus._UNAUTHORIZED:
			LOGGER.debug("Cevap status: unauthorized");
			return false; //unauthorized
		default:
			return false;
		}
	}

    public void checkResponse() throws ESYAException{
        _checkResponse(mOCSPResponce);
    }

	/**
	 * Check ocsp response for (nonce, signature value)
	 *
	 * @param aResponse OCSPResponse
	 * @throws ESYAException
	 */
	private void _checkResponse(EOCSPResponse aResponse) throws ESYAException 
	{
		try
		{
			mBasicResponse = aResponse.getBasicOCSPResponse();
		}
		catch (Exception aEx) 
		{
			LOGGER.error("OCSP cevap mesaji decode edilirken hata olustu", aEx);
			throw new ESYAException("OCSP cevap mesaji decode edilirken hata olustu", aEx);
		}

		if (mCheckNonce) 
		{
			if (!checkNonce()) 
			{
				mOCSPResponce = null;
				mDurum = STATUS_RESPONCE_INVALID_NONCE;
				LOGGER.error("OCSP cevap mesaji nonce kontrolü başarısız");
				throw new ESYAException("OCSP cevap mesaji nonce kontrolü başarısız");
			}
			LOGGER.debug("OCSP cevap mesaji nonce kontrolü başarılı");
		}


		if (mCheckSignature) 
		{
			if (!checkSignature()) 
			{
				mOCSPResponce = null;
				mDurum = STATUS_RESPONSE_HAS_INVALID_SIGNATURE;
				LOGGER.error("OCSP cevap mesaji imza kontrolü başarısız");
				throw new ESYAException("OCSP cevap mesaji imza kontrolü başarısız");
			}
			LOGGER.debug("OCSP cevap mesaji imza kontrolü başarılı");
		}

		EResponseData responseData = mBasicResponse.getTbsResponseData();
		int sorguSayisi = responseData.getSingleResponseCount();

		for (int i = 0; i < sorguSayisi; i++) 
		{
			ESingleResponse sr = responseData.getSingleResponse(i);
			if (mCheckDate) 
			{
				Calendar now = Calendar.getInstance();
				if (!checkDate(sr, now))
				{
					LOGGER.error("OCSP cevap geçerlilik tarihleri kontrolü hatalı");
					throw new ESYAException("OCSP cevap geçerlilik tarihleri kontrolü hatalı");
				}
			}
			
			singleResponses.put(sr.getCertID().getSerialNumber(), sr);
		}

	}

	/**
	 * Checks thisUpdate and nextUpdate in OCSP response agains given date.
	 *
	 * @param aResponse to be checked
	 * @param aDate to check
	 * @return
	 */
	public boolean checkDate(ESingleResponse aResponse, Calendar aDate)
	{
		if (aDate.before(aResponse.getThisUpdate())) 
		{
			LOGGER.warn("OCSP cevap thisUpdate değeri şimdiki zamandan sonra.");
			return false;
		}
		if (aResponse.getNextUpdate() != null)
		{
			//Calendar nextUpdate = Calendar.getInstance();
			//nextUpdate.setTimeInMillis(aResponse.nextUpdate.getTime().getTimeInMillis());
			if (aDate.after(aResponse.getNextUpdate())) 
			{
				LOGGER.warn("OCSP cevap nextUpdate değeri şimdiki zamandan önce.");
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the nonce value in the request
	 *
	 * @return nonce
	 */
	public byte[] getNonce()
	{
		return mNonce;
	}

	/**
	 * Check nonce value of response.
	 *
	 * @return nonce check result
	 */
	public boolean checkNonce() 
	{
		EExtensions responseExtensions = mBasicResponse.getResponseExtensions();
		if (responseExtensions == null) 
		{
			LOGGER.error("OCSP cevabı içinde extension bulunamadı");
			return false;
		}
		for (int i = 0; i < responseExtensions.getExtensionCount(); i++) 
		{
			EExtension ext = responseExtensions.getExtension(i);
			if (ext.getIdentifier().equals(NONCE_OID)) 
			{
				byte[] gelen = ext.getValue();
				Asn1OctetString nonceValue = new Asn1OctetString();
				Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(gelen);
				try 
				{
					nonceValue.decode(decBuf);
				} 
				catch (Exception aEx) 
				{
					LOGGER.error("OCSP cevabı içinde Nonce extension decode edilemedi", aEx);
					return false;
				}
				
				if (Arrays.equals(nonceValue.value, mNonce)) 
					return true;
				else 
					return false;
			}
		}
		LOGGER.error("OCSP cevabı içinde Nonce extension bulunamadı");
		return false;
	}

	/**
	 * Check signature value for response. Note that signing certificate is not
	 * checked. To check certificate get it by getSigningCertificate and use
	 * certificate validation API
	 *
	 * @return boolean
	 */
	public boolean checkSignature() 
	{
		byte[] imzalanan = null;
		
		imzalanan = mBasicResponse.getTbsResponseData().getEncoded();
		
		//imzayı ve imzalayan sertifikayı al
		byte[] imzali = mBasicResponse.getSignature();
		if (mBasicResponse.getObject() == null
				|| mBasicResponse.getObject().certs == null) 
		{
			LOGGER.error("Imzalayici sertifika bulunamadi");
			return false;
		}
		
		for (int i = 0; i < mBasicResponse.getCertificateCount(); i++) 
		{
			ECertificate imzalayan = mBasicResponse.getCertificate(i);
			try 
			{
				Pair<SignatureAlg, AlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(mBasicResponse.getSignatureAlgorithm());
				PublicKey key = KeyUtil.decodePublicKey(imzalayan.getSubjectPublicKeyInfo());
				if (SignUtil.verify(pair.first(),pair.second(), imzalanan, imzali, key))
					return true;

				//Java ile C arasında Little/Big Endian problemi olabilir
				//ters cevirerek deniyorum
				byte[] ters = new byte[imzali.length];
				int u = imzali.length - 1;
				for (int j = 0; j < imzali.length; j++)
				{
					ters[j] = imzali[u - j];
				}
				
				if (SignUtil.verify(pair.first(),pair.second(), imzalanan, ters, key))
					return true;

			}
			catch (CryptoException ex) 
			{
				LOGGER.error("Imza doğrulanırken hata oluştu", ex);
				return false;
			}

		}
		LOGGER.debug("Imza değeri Hiçbir Sertifika için dogrulanamadı:");
		return false;
	}

	/**
	 * Get the certificate that signs OCSP response, which should be validated.
	 *
	 * @return Certificate
	 * @throws ESYAException 
	 */
	public ECertificate getSigningCertificate() throws ESYAException 
	{
		if (mBasicResponse == null) 
			throw new ESYAException("No ocsp response");
		
		if(signingCertificate != null)
			return signingCertificate;
		
		if(checkSignature())
			return signingCertificate;
		
		return null;
	}
	
	public void setDigestAlgForOcspRequest(DigestAlg digestAlg){
		digestAlgForOcspRequest = digestAlg;
	}	
}
