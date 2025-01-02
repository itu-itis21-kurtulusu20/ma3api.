package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.profile;

import com.turktrust.dianta2.api.Dianta2IstemciNesnesi;
import com.turktrust.dianta2.api.SertifikaDegerNesnesi;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.Status;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;

public class VodafoneProfileRequest implements IProfileRequest {

    private MSSParams params;

    public VodafoneProfileRequest(MSSParams aParams) {
        setMSSParams(aParams);
    }

    @Override
    public VodafoneProfileResponse sendRequest(String aMSISDN, String aApTransId) throws ESYAException {

        String MSISDN = aMSISDN;
        if(MSISDN.startsWith("0"))
            MSISDN = MSISDN.substring(1);

        ECertificate certificate = getSignerCertificate(MSISDN);
        return new VodafoneProfileResponse(certificate, params);
    }

    @Override
    public void setMSSParams(MSSParams aParams) {
        params = aParams;
    }

    @Override
    public void setServiceUrl(String aServiceUrl) {
        params.setMsspProfileQueryUrl(aServiceUrl);
    }

    public ECertificate getSignerCertificate(String aMSISDN) throws ESYAException {

        String MSISDN = aMSISDN;
        if(MSISDN.startsWith("0"))
            MSISDN = MSISDN.substring(1);

        Dianta2IstemciNesnesi istemci = new Dianta2IstemciNesnesi(params.get_dnsName(), false);
        SertifikaDegerNesnesi sertifikaDegerNesnesi = istemci.mobilImzaSertifikasiSorgulaSDN(params.get_apId(), params.get_pwd(), MSISDN);

        if(sertifikaDegerNesnesi.getDurumKodu() != Status.REGISTRATION_OK.get_StatusCodeInt())
            throw new ESYAException("Sertifika çekmede hata! Hata Kodu: " + sertifikaDegerNesnesi.getDurumKodu() + " Hata Mesajı: " + sertifikaDegerNesnesi.getMesaj());

        byte[] certBytes = sertifikaDegerNesnesi.getSertifika();
        return new ECertificate(certBytes);
    }
}
