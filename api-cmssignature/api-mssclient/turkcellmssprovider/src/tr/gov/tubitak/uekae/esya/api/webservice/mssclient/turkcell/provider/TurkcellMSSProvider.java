package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.provider;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider.IMSSProvider;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status.IStatusRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.profile.TurkcellProfileRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.signature.TurkcellSignatureRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.turkcell.status.TurkcellStatusRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;

/**
 * Gets main Interfaces (Status, Signature, Profile) of Turkcell MSSP
 * @see IMSSProvider
 */
public class TurkcellMSSProvider implements IMSSProvider {

    public IProfileRequest getProfileRequester(MSSParams aParams) throws ESYAException {
        try
        {
            LV.getInstance().checkLD(LV.Urunler.MOBILIMZA);
        }
        catch(LE ex)
        {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + ex.getMessage());
        }
        return new TurkcellProfileRequest(aParams);
    }

    public IStatusRequest getStatusRequester(MSSParams aParams) throws ESYAException {
        return new TurkcellStatusRequest(aParams);
    }

    public ISignatureRequest getSignatureRequester(MSSParams aParams) throws ESYAException {
        return new TurkcellSignatureRequest(aParams);
    }
}
