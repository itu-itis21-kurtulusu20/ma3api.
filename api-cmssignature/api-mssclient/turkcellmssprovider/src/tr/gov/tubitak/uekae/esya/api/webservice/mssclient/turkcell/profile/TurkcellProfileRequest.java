package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.profile;

import com.turkcelltech.mobilesignature.validation.soap.MSS_ProfileQueryBindingStub;
import com.turkcelltech.mobilesignature.validation.soap.MSS_ProfileQueryServiceLocator;
import org.apache.axis.types.NCName;
import org.apache.axis.types.URI;
import org.etsi.uri.TS102204.v1_1_2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;

import java.math.BigInteger;
import java.util.Calendar;
/**
 * Turkcell mobile signature profile request implementation
 * @see IProfileRequest
 */
public class TurkcellProfileRequest implements IProfileRequest {
    Logger logger = LoggerFactory.getLogger(TurkcellProfileRequest.class);
    private MSSParams _params;
    MSS_ProfileQueryBindingStub _binding;

    private MSS_ProfileReqType _request = new MSS_ProfileReqType();

    public TurkcellProfileRequest(MSSParams aParams) throws ESYAException {
        try {
            try
            {
                LV.getInstance().checkLD(LV.Urunler.MOBILIMZA);
            }
            catch(LE ex)
            {
                throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
            }
            logger.debug("Connecting to mssp profile query port.");
            MSS_ProfileQueryServiceLocator mss_profileQueryServiceLocator = new MSS_ProfileQueryServiceLocator();
            String msspProfileQueryUrl = aParams.getMsspProfileQueryUrl();
            if(msspProfileQueryUrl != null){
                mss_profileQueryServiceLocator.setMSS_ProfileQueryPortEndpointAddress(msspProfileQueryUrl);
            }
            _binding = (MSS_ProfileQueryBindingStub)mss_profileQueryServiceLocator.getMSS_ProfileQueryPort();
            Integer definedTimeout = aParams.getConnectionTimeoutMs();
            if(definedTimeout>0)
                _binding.setTimeout(definedTimeout);
            logger.debug("Connected to mssp profile query port.");
            setMSSParams(aParams);
        }
        catch (Exception exc){
            String error = "Error while trying to connnect mssp profile query port";
            logger.error(error,exc);
            throw new ESYAException(error,exc);
        }
    }

    /**
     * @param aParams Request parameter
     */
    public void setMSSParams(MSSParams aParams) throws ESYAException {
        logger.debug("Setting parameters");
        _params = aParams;
        try {
            _fillInStatusRequest();
        } catch (Exception exc) {
            String errorStr = "Error while setting mssp connection parameters.";
            logger.error(errorStr,exc);
            throw new ESYAException(errorStr,exc);
        }
        logger.debug("parameters settled.");
    }

    /**
     * @param aServiceUrl Service endpoint Url
     */
    public void setServiceUrl(String aServiceUrl) {
        _binding._setProperty("javax.xml.rpc.service.endpoint.address", aServiceUrl);
    }


    private void _fillInStatusRequest() throws URI.MalformedURIException {
        _request.setMajorVersion(new BigInteger(_params.get_majorVersion()));
        _request.setMinorVersion(new BigInteger(_params.get_minorVersion()));

        //AP_Info
        MessageAbstractTypeAP_Info ap_info = new MessageAbstractTypeAP_Info();
        ap_info.setAP_ID(new URI(_params.get_apId()));
        ap_info.setAP_PWD(_params.get_pwd());
        ap_info.setAP_TransID(new NCName());
        ap_info.setInstant(Calendar.getInstance());
        _request.setAP_Info(ap_info);

        //MSSP_Info
        MessageAbstractTypeMSSP_Info mssp_info = new MessageAbstractTypeMSSP_Info();
        MeshMemberType dnsName = new MeshMemberType();
        dnsName.setDNSName(_params.get_dnsName());
        mssp_info.setMSSP_ID(dnsName);
        _request.setMSSP_Info(mssp_info);
    }

    /**
     * @param aMSISDN    MSISDN whose Mobile Signature Profile must be retrieved(User Phone Number)
     * @param aApTransId Transaction number created by AP on a new transaction
     * @return
     */
    public IProfileResponse sendRequest(String aMSISDN, String aApTransId) throws ESYAException {
        //MobileUser
        MobileUserType mobilUser = new MobileUserType();
        _request.setMobileUser(mobilUser);
        _request.getMobileUser().setMSISDN(aMSISDN);

        _request.getAP_Info().getAP_TransID().setValue(aApTransId);

        try {
            logger.debug("Sending profile query request to mssp service");
            MSS_ProfileRespType trcellResp = _binding.MSS_ProfileQuery(_request);
            logger.debug("Profile query response received from mssp service");
            return new TurkcellProfileResponse(trcellResp);
        } catch (Exception exc) {
            String errorStr = "Error while sending profile request";
            logger.error(errorStr);
            throw new ESYAException(errorStr,exc);
        }
    }
}
