using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.license;
using tr.gov.tubitak.uekae.esya.api.common.tools;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.infra.util;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.x509;
using CRLReason = tr.gov.tubitak.uekae.esya.asn.x509.CRLReason;
using Version = tr.gov.tubitak.uekae.esya.asn.ocsp.Version;


namespace tr.gov.tubitak.uekae.esya.api.infra.ocsp
{
    /**
     * <p>Title: OCSP client</p>
     * <p>Description: RFC 2560 X.509 Internet Public Key Infrastructure
          Online Certificate Status Protocol standard is implemented
       </p>
     * <p>Copyright: Copyright (c) 2004</p>
     * <p>Company: TUBITAK/UEKAE</p>
     * @author IH
     * @version 1.0
     */
    public class OCSPClient
    {
        //silinme sebepleri
        public static readonly int REASON_UNSPECIFIED = CRLReason.unspecified().mValue;//CRLReason._UNSPECIFIED;
        public static readonly int REASON_KEY_COMPROMISE = CRLReason.keyCompromise().mValue;//CRLReason._KEYCOMPROMISE;
        public static readonly int REASON_CA_COMPROMISE = CRLReason.cACompromise().mValue;//CRLReason._CACOMPROMISE;
        public static readonly int REASON_AFFILIATION_CHANGED = CRLReason.affiliationChanged().mValue;//CRLReason._AFFILIATIONCHANGED;
        public static readonly int REASON_SUPERSEDED = CRLReason.superseded().mValue;//CRLReason._SUPERSEDED;
        public static readonly int REASON_CESSATION_OF_OPERATION = CRLReason.cessationOfOperation().mValue;//CRLReason._CESSATIONOFOPERATION;
        public static readonly int REASON_CERTIFICATE_HOLD = CRLReason.certificateHold().mValue;//CRLReason._CERTIFICATEHOLD;
        public static readonly int REASON_REMOVE_FROM_CRL = CRLReason.removeFromCRL().mValue;//CRLReason._REMOVEFROMCRL;

        public static readonly int STATUS_RESPONSE_HAS_INVALID_SIGNATURE = 1;
        public static readonly int STATUS_RESPONSE_CANT_BE_RECEIVED = 2;
        public static readonly int STATUS_RESPONCE_INVALID_NONCE = 3;

        private static readonly String CONTENT_TYPE = "Content-Type";
        private static readonly String CONTENT_TYPE_OCSP_REQUEST = "application/ocsp-request";
        //private static readonly String CONTENT_TYPE_OCSP_RESPONSE = "application/ocsp-response";

        private static readonly String USER_AGENT = "User-Agent";
        private static readonly String USER_AGENT_OCSP = "UEKAE OCSP Client";

        //private static readonly String CONTENT_LENGTH = "Content-Length";
        //private static readonly String HOST = "Host";
        //private static readonly String KEEP_ALIVE = "Proxy-Connection: Keep-Alive";

        private static readonly String METHOD = "GET";
        //private static readonly String PROTOCOL_1_1 = "HTTP/1.1";
        //private static readonly String PROTOCOL_1_0 = "HTTP/1.0";
        //private static readonly String STATUS_CODE = "200";

        private static readonly Asn1ObjectIdentifier NONCE_OID = new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_nonce);

        private int mResponseStatus;
        //private int[] mCertificateStatuses;
        protected HashMultiMap<BigInteger, ESingleResponse> singleResponses;

        //private int[] mRevocationReasons;
        //private DateTime?[] mRevocationDates = null;

        protected ECertificate signingCertificate;
        protected EBasicOCSPResponse mBasicResponse = null;
        protected EOCSPResponse mOCSPResponse = null;

        //private static readonly Logger LOGGER = Logger.getLogger(OCSPClient.class);
        private static readonly ILog LOGGER = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private readonly String mHost = null;
        private /*HttpURLConnection*/ readonly EWebClient mCon = new EWebClient();
        private /*URL*/readonly Uri mURL = null;
        private byte[] mNonce = null;

        private int mDurum = 0;

        private bool mCheckSignature = true;
        private bool mCheckNonce = true;
        private bool mCheckResponseStatus = true;
        private bool mCheckDate = false;
        public DigestAlg digestAlgForOcspRequest = DigestAlg.SHA256;
	
        protected OCSPClient()
        {
            singleResponses = new HashMultiMap<BigInteger, ESingleResponse>();
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.ORTAK);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
            //do nothing
        }

        /**
	 * @param aConnectionAddress Adress to the OCSP server
	 * ECertificate object getOCSPAdresses() function gives addresses.	 
	 */
        public OCSPClient(String aConnectionAddress)
            : this()
        {
            try
            {
                mURL = new Uri(aConnectionAddress);
            }
            catch (Exception aEx)
            {
                //aEx.printStackTrace();
                Console.WriteLine(aEx.StackTrace);
                LOGGER.Fatal(aEx.Message, aEx);
            }
            mHost = aConnectionAddress;
        }

        /**
	 * Defines whether ocsp response signature will be checked
	 * Default: true
	 * @param aCheckSignature
	 */
        public void setCheckSignature(bool aCheckSignature)
        {
            mCheckSignature = aCheckSignature;
        }

        /**
         * Defines whether ocsp response nonce will be checked
         * Default: true
         * @param aCheckSignature
         */
        public void setCheckNonce(bool aCheckNonce)
        {
            mCheckNonce = aCheckNonce;
        }

        /**
         * Defines whether ocsp response status will be checked
         * @param aCheckSignature
         */
        public void setCheckResponseStatus(bool aCheckResponseStatus)
        {
            mCheckResponseStatus = aCheckResponseStatus;
        }

        /**
         * Checks thisUpdate and nextUpdate interval in OCSP response covers now
         * Default: false
         * @param aCheckSignature
         */
        public void setCheckDate(bool aCheckDate)
        {
            mCheckDate = aCheckDate;
        }

        /**
     * Proxy uzerinden baglaniyorsa set edilmelidir.
     * @param aProxyHost  host
     * @param aProxyPort  port
     * @param aDomain     domain
     * @param aKullaniciAdi userName
     * @param aParola       password
     */
        public void setProxySetting(String aProxyHost, int aProxyPort, String aDomain, String aUserName, String aPassword)
        {
            //ProxyUtil.proxyAyarlariniBelirle(aProxyHost, aProxyPort, aDomain, aKullaniciAdi, aParola);

            WebProxy myProxy = new WebProxy(aProxyHost, aProxyPort);
            myProxy.Credentials = new NetworkCredential(aUserName, aPassword, aDomain);
            mCon.Proxy = myProxy;

        }

        /*Cevap kontrol ayarlamaları*/
        public void isCheckSignature(bool aCheckSignature)
        {
            mCheckSignature = aCheckSignature;
        }

        public void isCheckNonce(bool aCheckNonce)
        {
            mCheckNonce = aCheckNonce;
        }

        public void isCheckResponseStatus(bool aCheckResponseStatus)
        {
            mCheckResponseStatus = aCheckResponseStatus;
        }

        public void isCheckDate(bool aCheckDate)
        {
            mCheckDate = aCheckDate;
        }

        /**
         * Connect to OCSP server.
         * @throws ESYAException
         */
        //public void openConnection()
        //{
        //    openConnection(0);
        //}

        //public void closeConnection()
        //{
        //    mCon.disconnect();
        //}

        //public void openConnection(int aTimeOut)
        //{
        //    try
        //    {
        //        /*HttpURLConnection*/
        //        //mCon = new WebClient();//(HttpURLConnection) mURL.openConnection();           
        //        if (aTimeOut != 0)
        //        {
        //            //con.setConnectTimeout(aTimeOut);
        //            //con.setReadTimeout(aTimeOut);
        //        }
        //        //con.setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_OCSP_REQUEST);
        //        mCon.Headers.Add(CONTENT_TYPE, CONTENT_TYPE_OCSP_REQUEST);
        //        //con.setRequestProperty(USER_AGENT, USER_AGENT_OCSP);
        //        mCon.Headers.Add(USER_AGENT, USER_AGENT_OCSP);
        //        //con.setRequestMethod(METHOD);
        //        //con.setDoOutput(true);
        //        //con.setUseCaches(false);            
        //        //mCon = new NtlmHttpURLConnection(con);
        //        //mCon.connect();
        //    }
        //    catch (Exception aEx)
        //    {
        //        LOGGER.Error("OCSP servera bağlanılırken hata oluştu:" + mHost, aEx);
        //        throw new ESYAException("OCSP servera bağlanılamadı:" + mHost, aEx);
        //    }
        //}
        /**
	 * Creation time of ocsp response
	 * @return
	 */
        public DateTime? getProducedAt()
        {
            return mBasicResponse.getProducedAt();
        }

        /**
         * Make single Certificate query.
         * @param aCertificateToQuery Certificate
         * @param aIssuerCertificate Certificate
         * @throws ESYAException
         */
        public void queryCertificate(ECertificate aCertificateToQuery, ECertificate aIssuerCertificate)
        {
            queryCertificate(new ECertificate[] { aCertificateToQuery }, new ECertificate[] { aIssuerCertificate });
        }

        /**
         * Make multiple query at once.
         * @param aCertificatesToQuery  Certificate[]
         * @param aIssuerCertificates Certificate[]
         * @throws ESYAException
         */
        public EOCSPResponse queryCertificate(ECertificate[] aCertificatesToQuery, ECertificate[] aIssuerCertificates)
        {
            //if (mCon == null)
            //{
            //    LOGGER.Error("Servera bağlantı kurulmamış.");
            //    throw new ESYAException("Sertifika sorgulamadan önce servera bağlantı kurulmalı");
            //}
            //create OCSP request
            byte[] istek = _createRequest(aCertificatesToQuery, aIssuerCertificates);
            //sunucuya gönderip cevabı al
            _runProtocol(istek);
            LOGGER.Debug("OCSP protokolu basariyla tamamlandi");
            return mOCSPResponse;
        }

        public EOCSPResponse queryCertificate(BigInteger aSertifikaSeriNo, byte[] aSMSubjectHashDegeri, byte[] aSMAcikAnahtarHashDegeri)
        {
            //if (mCon == null)
            //{
            //    LOGGER.Error("Servera bağlantı kurulmamış.");
            //    throw new ESYAException("Sertifika sorgulamadan önce servera bağlantı kurulmalı");
            //}

            //OCSP isteği oluştur
            byte[] istek = _createRequest(aSertifikaSeriNo, aSMSubjectHashDegeri, aSMAcikAnahtarHashDegeri);
            //sunucuya gönderip cevabı al
            _runProtocol(istek);
            LOGGER.Debug("OCSP protokolu başarıyla tamamlandı");
            return mOCSPResponse;
        }
        public int getStatus()
        {
            return mDurum;
        }

        private EOCSPResponse _runProtocol(byte[] aIstek)
        {
            
            //istek gönder
            //DataOutputStream output;
            byte[] responseBytes = null;
            try
            {
                //output = new DataOutputStream(mCon.getOutputStream());
                //output.write(aIstek);
                //output.flush();
                //output.close();                
                mCon.UseDefaultCredentials = true;
                mCon.Headers.Add(CONTENT_TYPE, CONTENT_TYPE_OCSP_REQUEST);
                mCon.Headers.Add(USER_AGENT, USER_AGENT_OCSP);                
                responseBytes = mCon.UploadData(mURL, /*METHOD*/null, aIstek);
            }
            catch (Exception aEx)
            {
                LOGGER.Error("İstek gönderilirken hata oluştu", aEx);
                throw new ESYAException("İstek gönderilirken hata oluştu", aEx);
            }

            //cevabi al
            EOCSPResponse response = null;
            try
            {
                LOGGER.Debug("Cevap alınacak");
                mOCSPResponse = _getResponse(responseBytes);
                LOGGER.Debug("Cevap alındı");
            }
            catch (Exception aEx)
            {
                mDurum = STATUS_RESPONSE_CANT_BE_RECEIVED;
                LOGGER.Error("OCSP serverdan cevap alınırken hata oluştu", aEx);
                throw new ESYAException("OCSP serverdan cevap alınamadı", aEx);
            }

            //cevabin statusunu kontrol et
            if (mCheckResponseStatus)
            {
                if (checkResponseStatus())
                {
                    //sertifikanin statusunu, silinme tarihini(silinmisse) al
                    _checkResponse(mOCSPResponse);
                }
                else
                {
                    mOCSPResponse = null;
                    LOGGER.Error("Cevap başarılı değil");
                    throw new ESYAException("Response status is not valid"); //cevap successful degil
                }
            }
            return mOCSPResponse;
        }

        /**
         * OCSP request oluşturur.
         * @param aSorgulanacakSertifikalar ECertificate[]
         * @param aSMSertifikalari ECertificate[]
         * @return byte[]
         */
        private byte[] _createRequest(ECertificate[] aSorgulanacakSertifikalar, ECertificate[] aSMSertifikalari)
        {
            LOGGER.Debug("OCSP isteği oluşturulacak");

            if ((aSorgulanacakSertifikalar == null) || (aSorgulanacakSertifikalar.Length == 0))
            {
                LOGGER.Error("Kontrol edilecek sertifika yok");
                throw new ESYAException("No certificate to query");
            }
            int len = aSorgulanacakSertifikalar.Length;
            Request[] requests = new Request[len];
            //Create ocsp requests for each certificate
            for (int i = 0; i < len; i++)
            {
                Certificate cert = aSorgulanacakSertifikalar[i].getObject();
                Certificate smcert = aSMSertifikalari[i].getObject();
                try
                {
                    requests[i] = _createRequest(cert, smcert);
                }
                catch (Exception aEx)
                {
                    LOGGER.Error("İstek oluşturulamadı", aEx);
                    throw new ESYAException("query can't be generated", aEx);
                }
            }
            return _createOcspRequest(requests);
        }

        private byte[] _createRequest(BigInteger aSertifikaSeriNo, byte[] aSMSubjectHashDegeri, byte[] aSMAcikAnahtarHashDegeri)
        {
            Request[] requests = new Request[1];
            try
            {
                requests[0] = _createRequestObject(aSertifikaSeriNo, aSMSubjectHashDegeri, aSMAcikAnahtarHashDegeri);
            }
            catch (Exception aEx)
            {
                LOGGER.Error("İstek oluşturulamadı", aEx);
                return null;
            }
            return _createOcspRequest(requests);
        }

        private byte[] _createOcspRequest(Request[] aRequests)
        {
            OCSPRequest ocsprequest = new OCSPRequest();

            Extension nonce = null;
            TBSRequest tbsrequest = new TBSRequest();
            try
            {
                nonce = _createNonce();
            }
            catch (Exception aEx)
            {
                LOGGER.Warn("Nonce extension oluşturulamadı", aEx);
                throw new ESYAException("Nonce extension can't be generated");
            }
            tbsrequest.requestList = new _SeqOfRequest(aRequests);
            tbsrequest.requestExtensions = new Extensions(new Extension[] { nonce });
            //version 1 uyguluyoruz
            tbsrequest.version = new Version(0); //versiyon 1
            ocsprequest.tbsRequest = tbsrequest;

            //requestimizi encode edelim
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();

            try
            {
                ocsprequest.Encode(encBuf);
            }
            catch (Asn1Exception ex)
            {
                LOGGER.Error("OCSP istek mesaji olusturulurken hata olustu", ex);
                throw new ESYAException("query can't be generated");
            }
            byte[] requestArr = encBuf.MsgCopy;
            LOGGER.Debug("OCSP isteği başarıyla oluşturuldu");
            return requestArr;
        }

        /**
         * İsteğe eklenecek nonce oluşturur.
         * @return Extension
         * @throws Exception
         */
        private Extension _createNonce()
        {
            Extension nonce = new Extension();
            nonce.extnID = NONCE_OID;
            mNonce = new byte[16];
            SecureRandom.GetInstance("SHA1PRNG").NextBytes(mNonce);
            //java.security.SecureRandom.getInstance("SHA1PRNG").nextBytes(mNonce);
            Asn1OctetString value = new Asn1OctetString(mNonce);
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            value.Encode(encBuf);
            nonce.extnValue = new Asn1OctetString(encBuf.MsgCopy);
            return nonce;
        }
        /**
         * Receive OCSP response.
         * @return OCSPResponse
         * @throws ESYAException
         */
        private EOCSPResponse _getResponse(byte[] aResponse)
        {
            OCSPResponse response = new OCSPResponse();
            //InputStream input = null;
            try
            {
                //input = mCon.getInputStream();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(/*input*/aResponse);
                response.Decode(decBuf);
                LOGGER.Debug("OCSP mesaji basariyla decode edildi");
            }
            //catch (IOException ex)
            //{
            //    LOGGER.Error("OCSP cevap mesajini alirken hata olustu", ex);
            //    throw new ESYAException("OCSP cevap mesajini alirken hata olustu", ex);
            //}
            catch (Asn1Exception ex)
            {
                LOGGER.Error("OCSP cevap mesaji decode edilirken hata olustu", ex);
                throw new ESYAException("OCSP cevap mesaji decode edilirken hata olustu", ex);
            }
            return new EOCSPResponse(response);
        }
        /**
         * Create OCSP request.
         * @param aCertificateToCheck Certificate
         * @param aIssuerCertificate Certificate
         * @return Request
         * @throws Exception
         */
        private Request _createRequest(Certificate aCertificateToCheck, Certificate aIssuerCertificate)
        {
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            aIssuerCertificate.tbsCertificate.subject.Encode(encBuf);

            BigInteger sertifikaSeriNo = aCertificateToCheck.tbsCertificate.serialNumber.mValue;
            byte[] nameHash = DigestUtil.digest(digestAlgForOcspRequest, encBuf.MsgCopy);
            byte[] keyHash = DigestUtil.digest(digestAlgForOcspRequest, aIssuerCertificate.tbsCertificate.subjectPublicKeyInfo.subjectPublicKey.mValue);

            return _createRequestObject(sertifikaSeriNo, nameHash, keyHash);
        }

        /**
         * Create OCSP request.
         * @param aCertificateSerialNumber Serial number of certificate to be checked
         * @param aIssuerSubjectHash Issuer subject hash
         * @param aIssuerPublicKeyHash      Issuer public key hash
         * @return
         * @throws Exception
         */
        private Request _createRequestObject(BigInteger aCertificateSerialNumber, byte[] aIssuerSubjectHash, byte[] aIssuerPublicKeyHash)
        {
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
         * @return response status
         */
        public int getResponseStatus()
        {
            return mResponseStatus;
        }

        public ESingleResponse getSingleResponse(ECertificate aCertificateToQuery,
            ECertificate aIssuerCertificate)
        {
            List<ESingleResponse> responses = singleResponses.get(aCertificateToQuery.getSerialNumber());
            if (responses == null)
                throw new ESYAException("OCSP response does not contain requsted certificate response");
            foreach (ESingleResponse response in responses)
            {
                ECertID certID = response.getCertID();
                if (isEqual(certID, aCertificateToQuery, aIssuerCertificate))
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
        public static bool isEqual(ECertID certID, ECertificate aCertificateToQuery,
                ECertificate aIssuerCertificate)
        {
            if (certID.getSerialNumber().Equals(aCertificateToQuery.getSerialNumber()))
            {
                try
                {
                    byte[] issuerName = aIssuerCertificate.getSubject().getBytes();
                    byte[] key = aIssuerCertificate.getSubjectPublicKeyInfo().getSubjectPublicKey();

                    DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(certID.getHashAlgorithm());
                    byte[] nameHash = DigestUtil.digest(digestAlg, issuerName);
                    byte[] keyHash = DigestUtil.digest(digestAlg, key);

                    if (certID.getIssuerNameHash().SequenceEqual(nameHash)
                            && certID.getIssuerKeyHash().SequenceEqual(keyHash))
                        return true;
                }
                catch (CryptoException ex)
                {
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
            return mOCSPResponse;
        }

        /**
         * Check Response status.
         *
         * @return
         */
        public bool checkResponseStatus()
        {
            int status = mOCSPResponse.getResponseStatus();
            mResponseStatus = status;
            LOGGER.Debug("Gelen cevap status:" + status);
            if (status == OCSPResponseStatus.successful().mValue)
            {
                LOGGER.Debug("Cevap basarili");
                return true; //successful
            }
            else if (status == OCSPResponseStatus.malformedRequest().mValue)
            {
                LOGGER.Warn("Cevap status: malformedRequest");
                return false; //malformedRequest
            }
            else if (status == OCSPResponseStatus.internalError().mValue)
            {
                LOGGER.Warn("Cevap status: internalError");
                return false; //internalError
            }
            else if (status == OCSPResponseStatus.tryLater().mValue)
            {
                LOGGER.Warn("Cevap status: tryLater");
                return false; //tryLater
            }
            else if (status == OCSPResponseStatus.sigRequired().mValue)
            {
                LOGGER.Warn("Cevap status: sigRequired");
                return false; //sigRequired
            }
            else if (status == OCSPResponseStatus.unauthorized().mValue)
            {
                LOGGER.Warn("Cevap status: unauthorized");
                return false; //unauthorized
            }
            else
            {
                LOGGER.Warn("Cevap status: Undefined OCSPResponseStatus!!!");
                return false;
            }
        }

        /**
         * Check ocsp response for (nonce, signature value).        
         * @param aResponse OCSPResponse
         * @throws ESYAException
         */
        private void _checkResponse(EOCSPResponse aResponse)
        {
            try
            {
                mBasicResponse = aResponse.getBasicOCSPResponse();
            }
            catch (Exception aEx)
            {
                LOGGER.Error("OCSP cevap mesaji decode edilirken hata olustu", aEx);
                throw new ESYAException("OCSP cevap mesaji decode edilirken hata olustu", aEx);
            }

            if (mCheckNonce)
            {
                if (!checkNonce())
                {
                    mOCSPResponse = null;
                    mDurum = STATUS_RESPONCE_INVALID_NONCE;
                    LOGGER.Error("OCSP cevap mesaji nonce kontrolu basarisiz");
                    throw new ESYAException("OCSP cevap mesaji nonce kontrolu basarisiz");
                }
                LOGGER.Debug("OCSP cevap mesaji nonce kontrolu basarili");
            }


            if (mCheckSignature)
            {
                if (!checkSignature())
                {
                    mOCSPResponse = null;
                    mDurum = STATUS_RESPONSE_HAS_INVALID_SIGNATURE;
                    LOGGER.Error("OCSP cevap mesaji imza kontrolu basarisiz");
                    throw new ESYAException("OCSP cevap mesaji imza kontrolu basarisiz");
                }
                LOGGER.Debug("OCSP cevap mesaji imza kontrolu basarili");
            }

            EResponseData responseData = mBasicResponse.getTbsResponseData();
            int sorguSayisi = responseData.getSingleResponseCount();

            for (int i = 0; i < sorguSayisi; i++)
            {
                ESingleResponse sr = responseData.getSingleResponse(i);
                if (mCheckDate)
                {
                    DateTime? now = DateTime.UtcNow;
                    if (!checkDate(sr, now.Value))
                    {
                        LOGGER.Error("OCSP cevap gecerlilik tarihleri kontrolu hatali");
                        throw new ESYAException("OCSP cevap gecerlilik tarihleri kontrolu hatali");
                    }
                }

                singleResponses.put(sr.getCertID().getSerialNumber(), sr);
            }

            /*
            byte[] v = aResponse.getResponseBytes();
            EBasicOCSPResponse basicResponse;
            try
            {
                basicResponse = new EBasicOCSPResponse(v);
            }
            catch (Exception aEx)
            {
                LOGGER.Error("OCSP cevap mesaji decode edilirken hata olustu", aEx);
                throw new ESYAException("OCSP cevap mesaji decode edilirken hata olustu", aEx);
            }
            mBasicResponse = basicResponse;

            if (mCheckNonce)
            {
                if (!checkNonce(basicResponse, mNonce))
                {
                    mDurum = RESPONSE_INVALID_NONCE;
                    LOGGER.Error("OCSP cevap mesaji nonce kontrolü başarısız");
                    throw new ESYAException("OCSP cevap mesaji nonce kontrolü başarısız");
                }
                LOGGER.Debug("OCSP cevap mesaji nonce kontrolü başarılı");
            }


            if (mCheckSignature)
            {
                if (!checkSignature(basicResponse))
                {
                    mDurum = RESPONSE_HAS_INVALID_SIGNATURE;
                    LOGGER.Error("OCSP cevap mesaji imza kontrolü başarısız");
                    throw new ESYAException("OCSP cevap mesaji imza kontrolü başarısız");
                }
                LOGGER.Debug("OCSP cevap mesaji imza kontrolü başarılı");
            }

            EResponseData responseData = basicResponse.getTbsResponseData();
            int sorguSayisi = responseData.getSingleResponseCount();

            mCertificateStatuses = new int[sorguSayisi];
            mRevocationReasons = new int[sorguSayisi];
            mRevocationDates = new DateTime?[sorguSayisi];

            for (int i = 0; i < sorguSayisi; i++)
            {
                ESingleResponse sr = responseData.getSingleResponse(i);
                if (mCheckDate)
                {
                    //Calendar now = Calendar.getInstance();
                    DateTime now = DateTime.Now;
                    if (!checkDate(sr, now))
                    {
                        LOGGER.Error("OCSP cevap geçerlilik tarihleri kontrolü hatalı");
                        throw new ESYAException("OCSP cevap geçerlilik tarihleri kontrolü hatalı");
                    }
                }
                int status = sr.getCertificateStatus();
                if (status == STATUS_REVOKED)//silinmisse silinme tarihini set et
                {
                    mRevocationDates[i] = sr.getRevocationTime();
                    mRevocationReasons[i] = sr.getRevocationReason();
                }
                mCertificateStatuses[i] = status;
            }
             * */

        }
        /**
         * Checks thisUpdate and nextUpdate in OCSP response agains given date.
         * @param aResponse to be checked
         * @param aDate to check
         * @return
         */
        public bool checkDate(ESingleResponse aResponse, /*Calendar*/DateTime aDate)
        {

            //Calendar thisUpdate = Calendar.getInstance();
            DateTime? thisUpdate = aResponse.getThisUpdate();
            if (thisUpdate.HasValue)
            {
                if (aDate.ToUniversalTime() < thisUpdate.Value.ToUniversalTime())
                {
                    LOGGER.Warn("OCSP cevap thisUpdate degeri simdiki zamandan sonra.");
                    return false;
                }
            }
            DateTime? nextUpdate = aResponse.getNextUpdate();
            if (nextUpdate.HasValue)
            {
                //Calendar nextUpdate = Calendar.getInstance();                    
                if (aDate.ToUniversalTime() > nextUpdate.Value.ToUniversalTime())
                {
                    LOGGER.Warn("OCSP cevap nextUpdate degeri simdiki zamandan once.");
                    return false;
                }
            }

            return true;
        }

        /**
         * Returns the nonce value in the request
         * @return nonce
         */
        public byte[] getNonce()
        {
            return mNonce;
        }

        /**
         * Check nonce value of response.
         * @param aResponse BasicOCSPResponse
         * @param aNonce Nonce value
         * @return nonce check result
         */
        public bool checkNonce(/*EBasicOCSPResponse aResponse, byte[] aNonce*/)
        {
            EExtensions responseExtensions = mBasicResponse.getResponseExtensions();
            if (responseExtensions == null)
            {
                LOGGER.Error("OCSP cevabi icinde extension bulunamadi");
                return false;
            }
            for (int i = 0; i < responseExtensions.getExtensionCount(); i++)
            {
                EExtension ext = responseExtensions.getExtension(i);
                if (ext.getIdentifier().Equals(NONCE_OID))
                {
                    byte[] gelen = ext.getValue();
                    Asn1OctetString nonceValue = new Asn1OctetString();
                    Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(gelen);
                    try
                    {
                        nonceValue.Decode(decBuf);
                    }
                    catch (Exception aEx)
                    {
                        LOGGER.Error("OCSP cevabi icinde Nonce extension decode edilemedi");
                        return false;
                    }

                    if (nonceValue.mValue.SequenceEqual(mNonce))
                        return true;
                    else
                        return false;
                }
            }
            LOGGER.Error("OCSP cevabi icinde Nonce extension bulunamadi");
            return false;

        }
        /**
         * Check signature value for response. Note that signing certificate is not
	    * checked. To check certificate get it by getSigningCertificate and use
	    * certificate validation API
        * @param aResponse BasicOCSPResponse
        * @return boolean
        */
        public bool checkSignature(/*EBasicOCSPResponse aResponse*/)
        {
            byte[] imzalanan = null;
            imzalanan = mBasicResponse.getTbsResponseData().getBytes();

            //imzayı ve imzalayan sertifikayı al
            byte[] imzali = mBasicResponse.getSignature();
            if (mBasicResponse.getObject() == null
               || mBasicResponse.getObject().certs == null)
            {
                LOGGER.Error("Imzalayici sertifika bulunamadi");
                return false;
            }
            for (int i = 0; i < mBasicResponse.getCertificateCount(); i++)
            {
                ECertificate imzalayan = mBasicResponse.getCertificate(i);

                try
                {
                    Pair<SignatureAlg, IAlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(mBasicResponse.getSignatureAlgorithm());

                    PublicKey key = new PublicKey(imzalayan.getSubjectPublicKeyInfo());
                    if (SignUtil.verify(pair.first(), pair.second(), imzalanan, imzali, key))
                        return true;

                    //Java ile C arasında Little/Big Endian problemi olabilir
                    //ters cevirerek deniyorum
                    byte[] ters = new byte[imzali.Length];
                    int u = imzali.Length - 1;
                    for (int j = 0; j < imzali.Length; j++)
                    {
                        ters[j] = imzali[u - j];
                    }
                    if (SignUtil.verify(pair.first(), pair.second(), imzalanan, ters, key))
                        return true;

                }
                catch (CryptoException ex)
                {
                    LOGGER.Error("Imza dogrulanirken hata olustu", ex);
                }

            }
            LOGGER.Debug("Imza degeri hicbir Sertifika icin dogrulanamadi:");

            return false;

        }
        /**
        * Get the certificate that signs OCSP response, which should be validated.
        * @param aResponse to get signing certificate
        * @return Certificate
        */
        public ECertificate getSigningCertificate(EBasicOCSPResponse aResponse)
        {
            if (mBasicResponse == null)
                throw new ESYAException("No ocsp response");

            if (signingCertificate != null)
                return signingCertificate;

            if (checkSignature())
                return signingCertificate;

            return null;
        }

        public void setTimeOut(String aTimeOut)
        {
            mCon.setTimeOut(Convert.ToInt32(aTimeOut));
        }

        public void setDigestAlgForOcspRequest(DigestAlg digestAlg)
        {
            digestAlgForOcspRequest = digestAlg;
        }
        /**
         * OCSP cevabını imzalayan sertifikanın ExtendedKeyUsage
         * uzantısındaki OCSP signing değerini kontrol eder.
         * @param aCertificate
         * @return
         */
        /*private boolean _ocspSertifikasiMi(Certificate aCertificate)
        {
            return Ma3Certificate.isOIDExist(aCertificate, new Asn1ObjectIdentifier(_ImplicitValues.id_kp_OCSPSigning));
        }*/
        /**
         * Response imzasini kontrol eder.
         * @return aResponse
         */
        /*public byte[] getOCSPResponse()
        {
            return mBasicResponse.getTbsResponseData().getBytes();
        }*/

        /**
         * OCSP cevabı imza değerini verir.
         * @return
         */
        /*
        public byte[] getSignedOCSPResponse()
        {
            return mBasicResponse.getSignature();
        }*/


        /*public static void main(String[] args)
        {
            //PropertyConfigurator.configure("c:\\Log4j.properties");
            OCSPClient ocsp = null;
            try
            {
                String cerfile = "D:\\Sertifikalar\\ekds\\aysm\\aysm-isil.cer";//args[0];//
            
                Certificate cer = new Certificate();
                AsnIO.dosyadanOKU(cer, cerfile);

                String smcerfile = "D:\\Sertifikalar\\ekds\\aysm\\aysm-ser-TEST1.crt";//args[1];//
                Certificate smcer = new Certificate();
                AsnIO.dosyadanOKU(smcer, smcerfile);

            
                String adres = "http://cisdup.aysm.ekds.gov.tr";
                //adres = "http://ocsp2.e-guven.com/ocsp.xuda";//zaman hatalı
                //adres = "http://ocsp.e-tugra.com/status/ocsp";//tamam
                //adres = "http://ocsp.turktrust.com.tr";//internal error
                ocsp = new OCSPClient(adres);
                //OCSPClient ocsp = new OCSPClient("http://ocsp.turktrust.com.tr");
            
            
                ocsp.baglantiKur();
                //ocsp.sertifikaSorgula(cer, smcer);
                Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
                smcer.tbsCertificate.subject.encode(enc);
                byte[] shash = HafizadaTumKripto.getInstance().ozetAl(enc.getMsgCopy(), "SHA-1");
                byte[] khash = HafizadaTumKripto.getInstance().ozetAl(smcer.tbsCertificate.subjectPublicKeyInfo.subjectPublicKey.value, "SHA-1");
                ocsp.sertifikaSorgula(cer.tbsCertificate.serialNumber.value,shash, khash);
            
            
                System.out.println("Cevap durumu:" + ocsp.getmCevapDurumu());
                System.out.println("Sertifika durumu:" + ocsp.getMSertifikaDurumu());
                System.out.println("Silinme sebebi:" + ocsp.getMSilinmeSebebi());
            }catch(Exception aEx)
            {
                System.out.println("Cevap durumu:" + ocsp.getmCevapDurumu());
                aEx.printStackTrace();
            }
        }*/
    }
}
