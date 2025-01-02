package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.status;

import com.turkcelltech.mobilesignature.validation.soap.MSS_StatusQueryBindingStub;
import com.turkcelltech.mobilesignature.validation.soap.MSS_StatusQueryServiceLocator;
import org.apache.axis.types.NCName;
import org.apache.axis.types.URI;
import org.etsi.uri.TS102204.v1_1_2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status.IStatusRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status.IStatusResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;

import java.math.BigInteger;
import java.util.Calendar;

/**
 * Turkcell mobile signature status request implementation
 * @see IStatusRequest
 */
public class TurkcellStatusRequest implements IStatusRequest {
    Logger logger = LoggerFactory.getLogger(TurkcellStatusRequest.class);
    private MSSParams _params;
    MSS_StatusQueryBindingStub _binding;
    private MSS_StatusReqType _request = new MSS_StatusReqType();

    public TurkcellStatusRequest(MSSParams aParams) throws ESYAException {
        try
        {
            LV.getInstance().checkLD(LV.Urunler.MOBILIMZA);
        }
        catch(LE ex)
        {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
        }
        try {
            logger.debug("Trying connect to mssp status query port");
            MSS_StatusQueryServiceLocator mss_statusQueryServiceLocator = new MSS_StatusQueryServiceLocator();
            String msspStatusQueryUrl = aParams.getMsspStatusQueryUrl();
            if(msspStatusQueryUrl!=null){
                mss_statusQueryServiceLocator.setMSS_StatusPortEndpointAddress(msspStatusQueryUrl);
            }
            _binding = (MSS_StatusQueryBindingStub)mss_statusQueryServiceLocator.getMSS_StatusPort();
            Integer definedTimeout = aParams.getConnectionTimeoutMs();
            if(definedTimeout>0)
                _binding.setTimeout(definedTimeout);
            setMSSParams(aParams);
            logger.debug("Connected to mssp status query port.");
        }  catch (Exception exc){
            String errorString = "Error while connecting to mssp status query port";
            logger.error(errorString,exc);
            throw  new ESYAException(errorString,exc);
        }
    }

    public void setMSSParams(MSSParams aParams) {
        _params = aParams;
        try {
            _initializeParams();
        } catch (Exception e) {
            logger.error("Error in TurkcellStatusRequest", e);
        }
    }

    private void _initializeParams() throws URI.MalformedURIException {

        _request.setMajorVersion(new BigInteger(_params.get_majorVersion()));
        _request.setMinorVersion(new BigInteger(_params.get_minorVersion()));

        //AP_Info
        MessageAbstractTypeAP_Info ap_info = new MessageAbstractTypeAP_Info();
        ap_info.setAP_ID(new URI(_params.get_apId()));
        ap_info.setAP_PWD(_params.get_pwd());
        ap_info.setInstant(Calendar.getInstance());
        _request.setAP_Info(ap_info);

        //MSSP_Info
        MessageAbstractTypeMSSP_Info mssp_info = new MessageAbstractTypeMSSP_Info();
        MeshMemberType dnsName = new MeshMemberType();
        dnsName.setDNSName(_params.get_dnsName());
        mssp_info.setMSSP_ID(dnsName);
        _request.setMSSP_Info(mssp_info);
    }


    public void setServiceUrl(String aServiceUrl) {
        _binding._setProperty("javax.xml.rpc.service.endpoint.address", aServiceUrl);
    }

    public IStatusResponse sendRequest(String aMsspTransId, String aApTransId) throws ESYAException {
        _request.setMSSP_TransID(new NCName(aMsspTransId));
        _request.getAP_Info().setAP_TransID(new NCName(aApTransId));

        try {
            logger.debug("Sending status request to mssp");
            MSS_StatusRespType trcellResp = _binding.MSS_StatusQuery(_request);
            logger.debug("Status response received from mssp");
            return new TurkcellStatusResponse(trcellResp);
        }
        catch (Exception exc){
            String errorString = "Error while connecting to mssp status query port";
            logger.error(errorString,exc);
            throw  new ESYAException(errorString,exc);
        }
    }
}
