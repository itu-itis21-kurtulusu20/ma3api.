package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.profile;

import org.etsi.uri.ts102204.v1_1.turktelekom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.com.ega.mssp.services.ap.MSSProfileQueryPortType;
import tr.com.ega.mssp.services.ap.MSSProfileQueryService;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.tools.EDateTool;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.ETransIdManager;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Turk Telekom mobile signature profile request implementation
 * @see IProfileRequest
 */
public class TurkTelekomProfileRequest implements IProfileRequest {
    private MSSParams _params;
    MSSProfileQueryService mssProfileQueryService;
    MSSProfileQueryPortType mssProfileQuery;
    MSSProfileReqType mssProfileRequest;
    Logger logger = LoggerFactory.getLogger(TurkTelekomProfileRequest.class);

    //MSS_ProfileQueryBindingStub _binding;

   // private MSS_ProfileReqType _request = new MSS_ProfileReqType();

    public TurkTelekomProfileRequest(MSSParams aParams) {
        try
        {
            LV.getInstance().checkLD(LV.Urunler.MOBILIMZA);
        }
        catch(LE ex)
        {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
        }
        String msspProfileQueryUrl = aParams.getMsspProfileQueryUrl();
        if(msspProfileQueryUrl!=null) {
            URL url = null;
            try {
                URL baseUrl;
                baseUrl = tr.com.ega.mssp.services.ap.MSSProfileQueryService.class.getResource(".");
                String queryUrl = msspProfileQueryUrl;
                if(!queryUrl.endsWith("?wsdl"))
                {
                    queryUrl+="?wsdl";
                }
                url = new URL(baseUrl, queryUrl);
                mssProfileQueryService = new MSSProfileQueryService(url,new QName("http://ap.services.mssp.ega.com.tr", "MSS_ProfileQueryService"));
            } catch (MalformedURLException e) {
                logger.warn("Failed to create URL for the wsdl Location: "+msspProfileQueryUrl);
                logger.warn(e.getMessage());

                try {
                    mssProfileQueryService = new MSSProfileQueryService();
                } catch (Exception ex) {
                    throw new ESYARuntimeException("Can not initiate MSSProfileQueryService.", e);
                }
            }
        } else {
            mssProfileQueryService = new MSSProfileQueryService();
        }

        mssProfileQuery = mssProfileQueryService.getMSSProfileQuery();

        Integer connectionTimeoutMs = aParams.getConnectionTimeoutMs();
        if(connectionTimeoutMs>0)
        {
                      //Set timeout until a connection is established
            ((BindingProvider)mssProfileQuery).getRequestContext().put("javax.xml.ws.client.connectionTimeout", String.valueOf(connectionTimeoutMs));
        }

        mssProfileRequest = new MSSProfileReqType();
        setMSSParams(aParams);
    }

    /**
     * @param aParams Request parameter
     */
    public void setMSSParams(MSSParams aParams) {
        _params = aParams;
        try {
            _fillInStatusRequest();
        } catch (Exception e) {
            logger.error("Error in TurkTelekomProfileRequest:", e);
        }
    }

        public void setServiceUrl(String aServiceUrl) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    private void _fillInStatusRequest() {

        mssProfileRequest.setMajorVersion(new BigInteger(_params.get_majorVersion()));
        mssProfileRequest.setMinorVersion(new BigInteger(_params.get_minorVersion()));

        XMLGregorianCalendar currentDtGreg = EDateTool.getCurrentDtGreg();
        //AP_Info
        MessageAbstractType.APInfo apInfo = new MessageAbstractType.APInfo();
        apInfo.setAPID(_params.get_apId());
        apInfo.setAPPWD(_params.get_pwd());
        apInfo.setAPTransID(ETransIdManager.getNewTransId());
        apInfo.setInstant(currentDtGreg);
        mssProfileRequest.setAPInfo(apInfo);

        //MSSP_Info
        MessageAbstractType.MSSPInfo msspInfo = new MessageAbstractType.MSSPInfo();
        MeshMemberType dnsName = new MeshMemberType();
        dnsName.setDNSName(_params.get_dnsName());
        msspInfo.setMSSPID(dnsName);
        msspInfo.setInstant(currentDtGreg);
        mssProfileRequest.setMSSPInfo(msspInfo);
    }

    //text signature

    /**
     * @param aMSISDN    MSISDN whose Mobile Signature Profile must be retrieved
     * @param aApTransId Transaction number created by AP on a new transaction
     * @return
     */
    public IProfileResponse sendRequest(String aMSISDN, String aApTransId) throws ESYAException {

        MobileUserType mobileUserType = new MobileUserType();
        mobileUserType.setMSISDN(aMSISDN);
        mssProfileRequest.setMobileUser(mobileUserType);

        MessageAbstractType.APInfo apInfo = mssProfileRequest.getAPInfo();
        apInfo.setAPTransID(aApTransId);

        MSSProfileRespType mssProfileRespType = mssProfileQuery.mssProfileQuery(mssProfileRequest);
        return new TurkTelekomProfileResponse(mssProfileRespType);
    }
}
