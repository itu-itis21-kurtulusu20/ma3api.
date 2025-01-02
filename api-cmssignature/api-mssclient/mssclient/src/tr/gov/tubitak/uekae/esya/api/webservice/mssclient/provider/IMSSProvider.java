package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.provider;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.profile.IProfileRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.signature.ISignatureRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.transaction.status.IStatusRequest;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;

/**
 * Gets main Interfaces (Status, Signature, Profile) of a provider
 */
public interface IMSSProvider {
    IProfileRequest getProfileRequester(MSSParams aParams) throws ESYAException;

    IStatusRequest getStatusRequester(MSSParams aParams) throws ESYAException;

    ISignatureRequest getSignatureRequester(MSSParams aParams) throws ESYAException;
}
