package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.signature;

import com.turkcelltech.mobilesignature.validation.soap.MSS_SignatureBindingStub;
import com.turkcelltech.mobilesignature.validation.soap.MSS_SignatureServiceLocator;
import org.apache.axis.AxisFault;
import org.apache.axis.types.NCName;
import org.apache.axis.types.PositiveInteger;
import org.apache.axis.types.URI;
import org.etsi.uri.TS102204.v1_1_2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.EUserException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer.ISignable;

import java.math.BigInteger;
import java.util.Calendar;

/**
 * Turkcell mobile signature request implementation
 *
 * @see ISignatureRequest
 */
public class TurkcellSignatureRequest implements ISignatureRequest {
    Logger logger = LoggerFactory.getLogger(TurkcellSignatureRequest.class);
    private MSSParams _params;
    MSS_SignatureBindingStub _binding;
    private MSS_SignatureReqType _request = new MSS_SignatureReqType();

    /**
     * Constructs a new Turkcell signature request
     *
     * @param aParams Request parameters
     */
    public TurkcellSignatureRequest(MSSParams aParams) throws ESYAException {
        try
        {
            LV.getInstance().checkLD(LV.Urunler.MOBILIMZA);
        }
        catch(LE ex)
        {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
        }
        try {
            logger.debug("Trying connect to mssp signature port");
            MSS_SignatureServiceLocator mss_signatureServiceLocator = new MSS_SignatureServiceLocator();
            String msspSignatureQueryUrl = aParams.getMsspSignatureQueryUrl();
            if(msspSignatureQueryUrl!=null){
                mss_signatureServiceLocator.setMSS_SignatureEndpointAddress(msspSignatureQueryUrl);
            }
            _binding = (MSS_SignatureBindingStub)mss_signatureServiceLocator.getMSS_Signature();
            Integer definedTimeout = aParams.getConnectionTimeoutMs();
            if(definedTimeout>0)
                _binding.setTimeout(definedTimeout);
            logger.debug("Connected to mssp signature port.");
            setMSSParams(aParams);
        }
        catch (Exception exc){
            String errorString = "Error while connecting to mssp signature port";
            logger.error(errorString,exc);
            throw  new ESYAException(errorString,exc);
        }
    }

    /**
     * Sets connection parameters
     *
     * @param aParams Request parameter
     */
    public void setMSSParams(MSSParams aParams) throws ESYAException {
        logger.debug("Trying to set mssp parameters");
        _params = aParams;
        try {
            _initializeMsspParams();
        }
        catch (Exception exc){
            String errorString = "Error while setting mssp parameters";
            logger.error(errorString,exc);
            throw  new ESYAException(errorString,exc);
        }
        logger.debug("setting mssp parameters completed.");
    }

    private void _initializeMsspParams() throws URI.MalformedURIException {
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


        //DataToBeSigned
        _request.setDataToBeSigned(new DataType());

        //todo Bu kisim optional!, binary siging yapılırsa mandatory
        //DataToBeDisplayed, optional field !!!!!
        DataType dataToBeDisplayed = new DataType();
        dataToBeDisplayed.setEncoding("UTF-8");
        dataToBeDisplayed.setMimeType("text/plain");
        _request.setDataToBeDisplayed(new DataType());

        _request.setMessagingMode(MessagingModeType.synch);
        String timeOutStr = String.valueOf(_params.getMsspRequestTimeout());
        PositiveInteger positiveInt = new PositiveInteger(timeOutStr);
        _request.setTimeOut(positiveInt);
        //return request;
    }

    /**
     * Set service endpoint
     *
     * @param aServiceUrl Service endpoint Url
     */
    public void setServiceUrl(String aServiceUrl) {
        _binding._setProperty("javax.xml.rpc.service.endpoint.address", aServiceUrl);
    }

    /**
     * Sends signature request
     *
     * @param aTransId  Transaction number created by AP on a new transaction
     * @param aMSISDN   MSISDN whose Mobile Signature Profile must be retrieved
     * @param aSignable Data that will be signed
     * @return SignatureResponse
     */
    public ISignatureResponse sendRequest(String aTransId, String aMSISDN, ISignable aSignable) throws ESYAException {
        logger.debug("Sending signature request to mssp");
        try {
            _fillInSignatureRequest(aTransId, aMSISDN, aSignable);
            MSS_SignatureRespType trcellResp = _binding.MSS_Signature(_request);
            logger.debug("Signature response received from mssp");
            return new TurkcellSignatureResponse(trcellResp);
        } catch (Exception exc){
            String errorString = "Error while signature request";
            logger.error(errorString, exc);

            // todo elaborate
            if (exc instanceof AxisFault && ((AxisFault) exc).detail.getMessage().contains("timed out")) {
                throw new EUserException(EUserException.TIMEOUT, GenelDil.mesaj(GenelDil.MOBIL_IMZA_ZAMAN_ASIMI));
            }

            throw  new ESYAException(errorString,exc);
        }
    }

    private void _fillInSignatureRequest(String aTransId, String aMSISDN, ISignable aSigner) throws URI.MalformedURIException {
        //AP_Info
        _request.getAP_Info().setAP_TransID(new NCName(aTransId));

        //MobileUser
        MobileUserType mobilUser = new MobileUserType();
        _request.setMobileUser(mobilUser);
        _request.getMobileUser().setMSISDN(aMSISDN);

        //DataToBeSigned
        _request.getDataToBeSigned().set_value(aSigner.getValueToBeSigned());
        _request.getDataToBeSigned().setEncoding(aSigner.getEncoding());
        _request.getDataToBeSigned().setMimeType(aSigner.getMimeType());

        //todo Bu kisim optional!, binary siging yapılırsa mandatory
        //DataToBeDisplayed, optional field !!!!!
        _request.getDataToBeDisplayed().set_value(aSigner.getValueToBeDisplayed());
        _request.getDataToBeDisplayed().setMimeType("text/plain");
        _request.getDataToBeDisplayed().setEncoding("UTF-8");

        //MSSFormat
        MssURIType mssURIType = new MssURIType();
        mssURIType.setMssURI(new URI(aSigner.getSignatureType().getSignatureUrl()));
        _request.setMSS_Format(mssURIType);

        if(aSigner.getHashURI()!=null){
            MssURIType signatureProfile = new MssURIType();
            signatureProfile.setMssURI(new URI(aSigner.getHashURI()));
            _request.setSignatureProfile(signatureProfile);
        }
    }
}
