package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

import java.security.PublicKey;

/**
 * @author ahmety
 * date: Jun 10, 2009
 */
public interface KeyValueElement
{

    PublicKey getPublicKey() throws XMLSignatureException;

}
