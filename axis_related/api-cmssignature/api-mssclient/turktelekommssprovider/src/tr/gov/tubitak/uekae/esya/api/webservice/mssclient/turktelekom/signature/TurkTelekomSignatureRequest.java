package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.signature;

import org.etsi.uri.ts102204.v1_1.turktelekom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.ega.mssp.services.ap.MSSSignaturePortType;
import tr.com.ega.mssp.services.ap.MSSSignatureService;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.tools.EDateTool;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Status;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer.ISignable;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Turkcell mobile signature request implementation
 *
 * @see tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureRequest
 */
public class TurkTelekomSignatureRequest implements ISignatureRequest {
    private MSSParams _params;
    MSSSignaturePortType mssSignaturePortType;
    MSSSignatureReqType mssSignatureReqType;
    Logger logger = LoggerFactory.getLogger(TurkTelekomSignatureRequest.class);

    /**
     * Constructs a new Turkcell signature request
     *
     * @param aParams Request parameters
     */
    public TurkTelekomSignatureRequest(MSSParams aParams) {
        try
        {
            LV.getInstance().checkLD(LV.Urunler.MOBILIMZA);
        }
        catch(LE ex)
        {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
        }

        MSSSignatureService mssSignatureService = null;

        String msspSignatureQueryUrl = aParams.getMsspSignatureQueryUrl();
        if(msspSignatureQueryUrl!=null){
            URL url = null;
            try {
                URL baseUrl;
                baseUrl = tr.com.ega.mssp.services.ap.MSSSignatureService.class.getResource(".");
                String queryUrl = msspSignatureQueryUrl;
                if(!queryUrl.endsWith("?wsdl"))
                {
                    queryUrl+="?wsdl";
                }
                url = new URL(baseUrl, queryUrl);
                mssSignatureService = new MSSSignatureService(url,new QName("http://ap.services.mssp.ega.com.tr", "MSS_SignatureService"));
            } catch (MalformedURLException e) {
                logger.warn("Failed to create URL for the wsdl Location: "+msspSignatureQueryUrl);
                logger.warn(e.getMessage());
                mssSignatureService = new MSSSignatureService();
            }
        }
        else
        {
            mssSignatureService = new MSSSignatureService();
        }
        mssSignaturePortType = mssSignatureService.getMSSSignature();
        Integer connectionTimeoutMs = aParams.getConnectionTimeoutMs();
        if(connectionTimeoutMs>0)
        {
            //Set timeout until a connection is established
            ((BindingProvider)mssSignaturePortType).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", connectionTimeoutMs);
        }
        mssSignatureReqType = new MSSSignatureReqType();
        setMSSParams(aParams);
    }

    /**
     * Sets connection parameters
     *
     * @param aParams Request parameter
     */
    public void setMSSParams(MSSParams aParams) {
        _params = aParams;
        try {
            _fillInSignatureRequest();
        } catch (Exception e) {
            logger.error("Error in TurkTelekomSignatureRequest:", e);
        }
    }

    /**
     * Set service endpoint
     *
     * @param aServiceUrl Service endpoint Url
     */
    public void setServiceUrl(String aServiceUrl) {
        //_binding._setProperty("javax.xml.rpc.service.endpoint.address", aServiceUrl);
    }

    /**
     * Sends signature request
     *
     * @param aTransId  Transaction number created by AP on a new transaction
     * @param aMSISDN   MSISDN whose Mobile Signature Profile must be retrieved
     * @param aSignable Data that will be signed
     * @return SignatureResponse
     */
    public ISignatureResponse sendRequest(String aTransId, String aMSISDN, ISignable aSignable) {

        try {
            _fillInSignatureRequest(aTransId, aMSISDN, aSignable);
            MSSSignatureRespType mssSignatureRespType = mssSignaturePortType.mssSignature(mssSignatureReqType);
            return new TurkTelekomSignatureResponse(mssSignatureRespType);
        } catch (Exception e) {
            if(e.getMessage().contains("502")) {
                MSSSignatureRespType mssSignatureRespType = createMSSSignatureRespType(Status.EXPIRED_TRANSACTION);
                return new TurkTelekomSignatureResponse(mssSignatureRespType);
            }
            logger.error("Error in TurkTelekomSignatureRequest:", e);
            return null;
        }
    }

    private MSSSignatureRespType createMSSSignatureRespType(Status status) {
        MSSSignatureRespType mssSignatureRespType = new MSSSignatureRespType();
        StatusType statusType = new StatusType();
        StatusCodeType statusCodeType = new StatusCodeType();
        statusCodeType.setValue(BigInteger.valueOf(status.get_StatusCodeInt()));
        statusType.setStatusCode(statusCodeType);
        mssSignatureRespType.setStatus(statusType);
        return mssSignatureRespType;
    }

    private void _fillInSignatureRequest(String aTransId, String aMSISDN, ISignable aSigner) throws org.apache.axis.types.URI.MalformedURIException {

        //AP_Info
        mssSignatureReqType.getAPInfo().setAPTransID(aTransId);

        //MobileUser
        MobileUserType mobileUserType = new MobileUserType();
        mobileUserType.setMSISDN(aMSISDN);
        mssSignatureReqType.setMobileUser(mobileUserType);

        //MSSP_Info
        MessageAbstractType.MSSPInfo msspInfo = new MessageAbstractType.MSSPInfo();
        MeshMemberType dnsName = new MeshMemberType();
        dnsName.setDNSName(_params.get_dnsName());
        msspInfo.setMSSPID(dnsName);
        mssSignatureReqType.setMSSPInfo(msspInfo);

        //DataToBeSigned
        DataType dataToBeSigned = mssSignatureReqType.getDataToBeSigned();
        dataToBeSigned.setEncoding(aSigner.getEncoding());
        dataToBeSigned.setValue(aSigner.getValueToBeSigned());
        dataToBeSigned.setMimeType(aSigner.getMimeType());
        mssSignatureReqType.setDataToBeSigned(dataToBeSigned);


        //todo Bu kisim optional!, binary siging yapılırsa mandatory
        //DataToBeDisplayed, optional field !!!!!
        DataType dataToBeDisplayed = mssSignatureReqType.getDataToBeDisplayed();
        dataToBeDisplayed.setEncoding("UTF-8");
        dataToBeDisplayed.setMimeType("text/plain");
        dataToBeDisplayed.setValue(aSigner.getValueToBeDisplayed());
        mssSignatureReqType.setDataToBeDisplayed(dataToBeDisplayed);

        //MSSFormat
        MssURIType mssURIType = new MssURIType();
        String signatureUrl = aSigner.getSignatureType().getSignatureUrl();
        mssURIType.setMssURI(signatureUrl);
        mssSignatureReqType.setMSSFormat(mssURIType);

        if(aSigner.getHashURI()!=null){
            MssURIType signatureProfile = new MssURIType();
            signatureProfile.setMssURI(aSigner.getHashURI());
            mssSignatureReqType.setSignatureProfile(signatureProfile);
        }
    }

    private void _fillInSignatureRequest() {
        mssSignatureReqType.setMajorVersion(new BigInteger(_params.get_majorVersion()));
        mssSignatureReqType.setMinorVersion(new BigInteger(_params.get_minorVersion()));

        //AP_Info
        MessageAbstractType.APInfo apInfo = new MessageAbstractType.APInfo();
        apInfo.setAPID(_params.get_apId());
        apInfo.setAPPWD(_params.get_pwd());
        XMLGregorianCalendar currentDtGreg = EDateTool.getCurrentDtGreg();
        apInfo.setInstant(currentDtGreg);
        mssSignatureReqType.setAPInfo(apInfo);

        //MSSP_Info
        MessageAbstractType.MSSPInfo msspInfo = new MessageAbstractType.MSSPInfo();
        MeshMemberType dnsName = new MeshMemberType();
        dnsName.setDNSName(_params.get_dnsName());
        msspInfo.setMSSPID(dnsName);
        mssSignatureReqType.setMSSPInfo(msspInfo);

        //DataToBeSigned
        mssSignatureReqType.setDataToBeSigned(new DataType());


        DataType dataToBeDisplayed = new DataType();
        dataToBeDisplayed.setEncoding("UTF-8");
        dataToBeDisplayed.setMimeType("text/plain");
        mssSignatureReqType.setDataToBeDisplayed(dataToBeDisplayed);


        String timeOutStr = String.valueOf(_params.getMsspRequestTimeout());
        mssSignatureReqType.setTimeOut(new BigInteger(timeOutStr));
        mssSignatureReqType.setMessagingMode(MessagingModeType.SYNCH);
    }
}
