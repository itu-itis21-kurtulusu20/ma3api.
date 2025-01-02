using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.stub.status;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.status
{
    /**
 * Turkcell mobile signature status request implementation
 * @see IStatusRequest
 */

    public class TurkTelekomStatusRequest : IStatusRequest
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private MSSParams _params;
        private readonly MSS_StatusQueryService _service = new MSS_StatusQueryService();
        private readonly MSS_StatusReqType _request = new MSS_StatusReqType();

        public TurkTelekomStatusRequest(MSSParams aParams)
        {
            EMSSPClientUtil.CheckLicense();
            logger.Debug("Status request için parametreler belirlenecek.");
            setMSSParams(aParams);
            logger.Debug("Status request için parametreler belirlendi.");
        }

        public void setMSSParams(MSSParams aParams)
        {
            _params = aParams;
            _fillInStatusRequest();
        }


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
            //_request.AP_Info.AP_TransID = aTransId;
            _request.AP_Info.Instant = DateTime.Now;

            //MSSP_Info
            _request.MSSP_Info = new MessageAbstractTypeMSSP_Info();
            _request.MSSP_Info.MSSP_ID = new MeshMemberType();
            _request.MSSP_Info.MSSP_ID.DNSName = _params.DnsName;
        }

        //text signature
        public IStatusResponse sendRequest(String aMsspTransId, String aApTransId)
        {
            logger.Debug("İmzalama status isteği gönderilecek.");
            _request.MSSP_TransID = aMsspTransId;
            _request.AP_Info.AP_TransID = aApTransId;
            try
            {
                MSS_StatusRespType trcellResp = _service.MSS_StatusQuery(_request);
                logger.Debug("İmzalama status isteği gönderildi. Cevap geldi.");
                return new TurkTelekomStatusResponse(trcellResp);
            }
            catch (Exception exc)
            {
                String errorStr = "Error while signature status requesting.";
                logger.Error(errorStr,exc);
                throw  new ESYAException(errorStr,exc);
            }
        }
    }
}