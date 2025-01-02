using System;
using System.Reflection;
using log4net;
using log4net.Core;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.stub;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.profile
{
    /**
 * Turkcell mobile signature profile request implementation
 * @see IProfileRequest
 */

    public class TurkcellProfileRequest : IProfileRequest
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private MSSParams _params;
        private readonly MSS_ProfileQueryService _service = new MSS_ProfileQueryService();
        private readonly MSS_ProfileReqType _request = new MSS_ProfileReqType();

        public TurkcellProfileRequest(MSSParams aParams)
        {
            EMSSPClientUtil.CheckLicense();
            string msspProfileQueryUrl = aParams.GetMsspProfileQueryUrl();
            if (msspProfileQueryUrl != null)
            {
                _service.Url = msspProfileQueryUrl;
            }

            int connectionTimeOutMs = aParams.ConnectionTimeOutMs;
            if (connectionTimeOutMs > 0)
            {
                //Set timeout until a connection is established
                _service.Timeout = connectionTimeOutMs;
            }
            logger.Debug("TurkcellProfileRequest parametreleri belirlenecek.");
            setMSSParams(aParams);
            logger.Debug("TurkcellProfileRequest parametreleri belirlendi.");
        }

        /**
     * @param aParams Request parameter
     */

        public void setMSSParams(MSSParams aParams)
        {
            _params = aParams;
            _fillInStatusRequest();
        }

        /**
     * @param aServiceUrl Service endpoint Url
     */

        public void setServiceUrl(String aServiceUrl)
        {
            _service.Url = aServiceUrl;
        }


        private void _fillInStatusRequest()
        {
            _request.MajorVersion = _params.MajorVersion;
            _request.MinorVersion = _params.MinorVersion;

            //AP_Info 
            _request.AP_Info = new MessageAbstractTypeAP_Info();
            _request.AP_Info.AP_ID = _params.AP_ID;
            _request.AP_Info.AP_PWD = _params.PWD;
            _request.AP_Info.Instant = DateTime.Now;

            //MSSP_Info
            _request.MSSP_Info = new MessageAbstractTypeMSSP_Info();
            _request.MSSP_Info.MSSP_ID = new MeshMemberType();
            _request.MSSP_Info.MSSP_ID.DNSName = _params.DnsName;

            //MobileUser
            _request.MobileUser = new MobileUserType();
        }

        /**
     * @param aMSISDN    MSISDN whose Mobile Signature Profile must be retrieved
     * @param aApTransId Transaction number created by AP on a new transaction
     * @return
     */
        public IProfileResponse sendRequest(String aMSISDN, String aApTransId)
        {
            logger.Debug("Request gönderilecek.");
            _request.MobileUser.MSISDN = aMSISDN;
            _request.AP_Info.AP_TransID = aApTransId;
            try
            {
                MSS_ProfileRespType trcellResp = _service.MSS_ProfileQuery(_request);
                logger.Debug("Request gönderildi. Response geldi.");
                return new TurkcellProfileResponse(trcellResp);
            }
            catch (Exception exc)
            {
                String errorStr = "Error while profile request";
                logger.Error(errorStr, exc);
                throw new ESYAException(errorStr,exc);
            }
        }
    }
}