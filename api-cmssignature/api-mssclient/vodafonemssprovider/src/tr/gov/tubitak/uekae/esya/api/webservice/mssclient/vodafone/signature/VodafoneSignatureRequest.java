package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.signature;

import com.turktrust.dianta2.api.Dianta2IstemciNesnesi;
import com.turktrust.dianta2.api.ImzaDegerNesnesi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Status;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureResponse;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.signer.ISignable;

public class VodafoneSignatureRequest implements ISignatureRequest {

    private static Logger logger = LoggerFactory.getLogger(VodafoneSignatureRequest.class);

    private MSSParams params;

    public VodafoneSignatureRequest(MSSParams aParams) throws ESYAException {
        setMSSParams(aParams);
    }

    @Override
    public void setMSSParams(MSSParams aParams) throws ESYAException {
        params = aParams;
        logger.debug("setting mssp parameters completed.");
    }

    @Override
    public void setServiceUrl(String aServiceUrl) {
        params.setMsspSignatureQueryUrl(aServiceUrl);
    }

    @Override
    public ISignatureResponse sendRequest(String aTransId, String aMSISDN, ISignable aSignable) {
        try {
            String MSISDN = aMSISDN;
            if(MSISDN.startsWith("0"))
                MSISDN = MSISDN.substring(1);

            Dianta2IstemciNesnesi istemci = new Dianta2IstemciNesnesi(params.get_dnsName(), false);
            ImzaDegerNesnesi imzaDegerNesnesi = istemci.mobilImzaTalebiGonder(params.get_apId(),
                params.get_pwd(),
                aTransId,
                MSISDN,
                Base64.decode(aSignable.getValueToBeSigned()),
                aSignable.getValueToBeDisplayed(),
                null,
                params.getConnectionTimeoutMs(),
                Dianta2IstemciNesnesi.MOD_SENKRON,
                Base64.decode(aSignable.getValueToBeSigned()),
                null,
                true);


            return new VodafoneSignatureResponse(
                imzaDegerNesnesi.msspIslemIDAl(),
                MSISDN,
                imzaDegerNesnesi.degerAl(),
                new Status(Integer.toString(imzaDegerNesnesi.durumAl()), imzaDegerNesnesi.getMesaj()));

        } catch (Exception ex) {
            throw new ESYARuntimeException("Can not sign with Vodafone Mobile Signature", ex);
        }
    }
}
