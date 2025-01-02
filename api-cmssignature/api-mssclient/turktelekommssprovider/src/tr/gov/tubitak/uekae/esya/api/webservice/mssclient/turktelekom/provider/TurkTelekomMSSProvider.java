package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.provider;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.profile.TurkTelekomProfileRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turktelekom.signature.TurkTelekomSignatureRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider.IMSSProvider;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status.IStatusRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;

/**
 * Gets main Interfaces (Status, Signature, Profile) of Turkcell MSSP
 * @see IMSSProvider
 */
public class TurkTelekomMSSProvider implements IMSSProvider {
    public IProfileRequest getProfileRequester(MSSParams aParams) {
        try
        {
            LV.getInstance().checkLD(LV.Urunler.MOBILIMZA);
        }
        catch(LE ex)
        {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
        }
        return new TurkTelekomProfileRequest(aParams);
    }

    @Override
    public IStatusRequest getStatusRequester(MSSParams aParams) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ISignatureRequest getSignatureRequester(MSSParams aParams) {
        return new TurkTelekomSignatureRequest(aParams);
    }
}
