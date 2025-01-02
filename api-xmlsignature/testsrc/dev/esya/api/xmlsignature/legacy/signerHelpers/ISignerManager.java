package dev.esya.api.xmlsignature.legacy.signerHelpers;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 30.01.2013
 * Time: 10:29
 * To change this template use File | Settings | File Templates.
 */
public interface ISignerManager {

    BaseSigner getSigner(int signSlotNo, int certSlotNo);
    ECertificate getSigningCertificate(int slot);
    void logout(int slot);
}
