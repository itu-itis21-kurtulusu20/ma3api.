package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.status;

import org.etsi.uri.ts102204.v1_1.turktelekom.MSSStatusReqType;
import org.etsi.uri.ts102204.v1_1.turktelekom.MSSStatusRespType;
import org.etsi.uri.ts102204.v1_1.turktelekom.MeshMemberType;
import org.etsi.uri.ts102204.v1_1.turktelekom.MessageAbstractType;
import tr.com.ega.mssp.services.ap.MSSStatusQueryPortType;
import tr.com.ega.mssp.services.ap.MSSStatusQueryService;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.tools.EDateTool;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status.IStatusRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status.IStatusResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Turkcell mobile signature status request implementation
 * @see tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status.IStatusRequest
 */
public class TurkTelekomStatusRequest implements IStatusRequest {

    protected static Logger logger = LoggerFactory.getLogger(TurkTelekomStatusRequest.class);

    private MSSParams _params;
    MSSStatusQueryPortType mssStatusQueryPortType;
    MSSStatusReqType mssStatusRequest;

    public TurkTelekomStatusRequest(MSSParams aParams) {
        try
        {
            LV.getInstance().checkLD(LV.Urunler.MOBILIMZA);
        }
        catch(LE ex)
        {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
        }
        MSSStatusQueryService mssStatusQueryService = new MSSStatusQueryService();
        mssStatusQueryPortType = mssStatusQueryService.getMSSStatusQuery();
        setMSSParams(aParams);
    }

    public void setMSSParams(MSSParams aParams) {
        _params = aParams;
        try {
            _fillInStatusRequest();
        } catch (Exception e) {
            logger.error("Error in TurkTelekomStatusRequest", e);
        }
    }


    public void setServiceUrl(String aServiceUrl) {
        //_service.Url = aServiceUrl;
        //_binding._setProperty("javax.xml.rpc.service.endpoint.address", aServiceUrl);
    }


    private void _fillInStatusRequest(){
        mssStatusRequest.setMajorVersion(new BigInteger(_params.get_majorVersion()));
        mssStatusRequest.setMinorVersion(new BigInteger(_params.get_minorVersion()));

        //AP_Info
        //AP_Info
        MessageAbstractType.APInfo apInfo = new MessageAbstractType.APInfo();
        apInfo.setAPID(_params.get_apId());
        apInfo.setAPPWD(_params.get_pwd());
        XMLGregorianCalendar currentDtGreg = EDateTool.getCurrentDtGreg();
        apInfo.setInstant(currentDtGreg);
        mssStatusRequest.setAPInfo(apInfo);

        //MSSP_Info
        MessageAbstractType.MSSPInfo msspInfo = new MessageAbstractType.MSSPInfo();
        MeshMemberType dnsName = new MeshMemberType();
        dnsName.setDNSName(_params.get_dnsName());
        msspInfo.setMSSPID(dnsName);
        mssStatusRequest.setMSSPInfo(msspInfo);
    }

    //text signature
    public IStatusResponse sendRequest(String aMsspTransId, String aApTransId) {

        mssStatusRequest.setMSSPTransID(aMsspTransId);
        mssStatusRequest.getAPInfo().setAPTransID(aApTransId);

        MSSStatusRespType mssStatusRespType = mssStatusQueryPortType.mssStatusQuery(mssStatusRequest);
        return new TurkTelekomStatusResponse(mssStatusRespType);
    }
}
