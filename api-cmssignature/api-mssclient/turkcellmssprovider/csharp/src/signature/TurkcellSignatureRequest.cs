using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.stub;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.signature
{
    /**
 * Turkcell mobile signature request implementation
 *
 * @see ISignatureRequest
 */

    public class TurkcellSignatureRequest : ISignatureRequest
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private MSSParams _params;
        private readonly MSS_SignatureService _service = new MSS_SignatureService();
        private readonly MSS_SignatureReqType _request = new MSS_SignatureReqType();

        public TurkcellSignatureRequest(MSSParams aParams)
        {
            EMSSPClientUtil.CheckLicense();
            string msspSignatureQueryUrl = aParams.GetMsspSignatureQueryUrl();
            if (msspSignatureQueryUrl != null)
            {
                _service.Url = msspSignatureQueryUrl;
            }

            int connectionTimeOutMs = aParams.ConnectionTimeOutMs;
            if (connectionTimeOutMs > 0)
            {
                //Set timeout until a connection is established
                _service.Timeout = connectionTimeOutMs;
            }

            logger.Debug("Signature request için parametreler belirlenecek.");
            setMSSParams(aParams);
            logger.Debug("Signature request için parametreler belirlendi.");
        }

        public void setMSSParams(MSSParams aParams)
        {
            _params = aParams;
            _fillInSignatureRequest();
        }


        public void setServiceUrl(String aServiceUrl)
        {
            _service.Url = aServiceUrl;
        }

        //text signature
        public ISignatureResponse sendRequest(String aTransId, String aMSISDN, ISignable aSignable)
        {
            logger.Debug("Signature request gönderilecek.");
            _fillInSignatureRequest(aTransId, aMSISDN, aSignable);
            try
            {
                MSS_SignatureRespType trcellResp = _service.MSS_Signature(_request);
                logger.Debug("Signature request gönderildi. Sİgnature Response geldi.");
                return new TurkcellSignatureResponse(trcellResp);
            }
            catch (Exception exc)
            {
                String errorStr = "Error while signature requesting.";
                logger.Error(errorStr,exc);
                throw new ESYAException(errorStr,exc);
            }
        }

        private void _fillInSignatureRequest(String aTransId, String aMSISDN, ISignable aSignable)
        {
            //AP_Info             
            _request.AP_Info.AP_TransID = aTransId;

            //MSSP_Info                       

            //MobileUser             
            _request.MobileUser.MSISDN = aMSISDN;

            //DataToBeSigned             
            _request.DataToBeSigned.Value = aSignable.getValueToBeSigned();
            _request.DataToBeSigned.Encoding = aSignable.getEncoding();
            _request.DataToBeSigned.MimeType = aSignable.getMimeType();

            //todo Bu kisim optional!, binary siging yapılırsa mandatory
            //DataToBeDisplayed, optional field !!!!!           
            _request.DataToBeDisplayed.Value = aSignable.getValueToBeDisplayed();


            //MSSFormat
            _request.MSS_Format = new mssURIType();
            _request.MSS_Format.mssURI = aSignable.getSignatureType().getSignatureUrl();

            if (aSignable.getHashURI() != null)
            {
                mssURIType signatureProfile = new mssURIType();
                signatureProfile.mssURI=aSignable.getHashURI();
                _request.SignatureProfile=signatureProfile;
            }            
        }

        private void _fillInSignatureRequest()
        {
            _request.MajorVersion = _params.MajorVersion;
            _request.MinorVersion = _params.MinorVersion;

            //AP_Info 
            _request.AP_Info = new MessageAbstractTypeAP_Info();
            _request.AP_Info.AP_ID = _params.AP_ID;
            _request.AP_Info.AP_PWD = _params.PWD;
            //_request.AP_Info.AP_TransID = aTransId;
            _request.AP_Info.Instant = DateTime.Now;

            //MSSP_Info
            _request.MSSP_Info = new MessageAbstractTypeMSSP_Info();
            _request.MSSP_Info.MSSP_ID = new MeshMemberType();
            _request.MSSP_Info.MSSP_ID.DNSName = _params.DnsName;

            //MobileUser 
            _request.MobileUser = new MobileUserType();
            //_request.MobileUser.MSISDN = aMSISDN;

            //DataToBeSigned 
            _request.DataToBeSigned = new DataType();
            //_request.DataToBeSigned.Value = aSigner.getValueToBeSigned();
            //_request.DataToBeSigned.Encoding = aSigner.getEncoding();
            //_request.DataToBeSigned.MimeType = aSigner.getMimeType();

            //todo Bu kisim optional!, binary siging yapılırsa mandatory
            //DataToBeDisplayed, optional field !!!!!
            _request.DataToBeDisplayed = new DataType();
            //_request.DataToBeDisplayed.Value = aSigner.getValueToBeDisplayed();
            _request.DataToBeDisplayed.Encoding = "UTF-8";
            _request.DataToBeDisplayed.MimeType = "text/plain";

            //TODO BU PARAMETRIK OLMALI
            //MSSFormat
            //_request.MSS_Format = new mssURIType();
            //_request.MSS_Format.mssURI = SignatureType.PKCS7;

            //Messaging Mode
            _request.MessagingMode = MessagingModeType.synch;

            int queryTimeOutInSeconds = _params.QueryTimeOutInSeconds;
            _request.TimeOut = "" + queryTimeOutInSeconds;

        }
    }
}