package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.provider;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider.IMSSProvider;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status.IStatusRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.profile.VodafoneProfileRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.vodafone.signature.VodafoneSignatureRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;

public class VodafoneMSSProvider implements IMSSProvider {

    @Override
    public IProfileRequest getProfileRequester(MSSParams aParams) {
        return new VodafoneProfileRequest(aParams);
    }

    @Override
    public IStatusRequest getStatusRequester(MSSParams aParams) throws ESYAException {
        throw new ESYAException("Not supported function!");
    }

    @Override
    public ISignatureRequest getSignatureRequester(MSSParams aParams) throws ESYAException {
        return new VodafoneSignatureRequest(aParams);
    }
}
