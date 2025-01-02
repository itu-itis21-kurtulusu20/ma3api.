package tr.gov.tubitak.uekae.esya.api.cmsenvelope;

import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;

public class NotEnvelopeDataException extends CMSException {

    public NotEnvelopeDataException(String msg){
        super(msg);
    }

    public NotEnvelopeDataException(String msg, Exception e){
        super(msg, e);
    }
}
