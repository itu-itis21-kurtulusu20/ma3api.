package tr.gov.tubitak.uekae.esya.api.pades.pdfbox;

import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.api.signature.config.Config;

import java.net.URI;

/**
 * @author ayetgin
 */
public class PAdESContext extends Context {

    private boolean signWithTimestamp = false;

    public PAdESContext() {
        setPAdES(true);
    }

    public PAdESContext(URI aBaseURI) {
        super(aBaseURI);
        setPAdES(true);
    }

    public PAdESContext(URI aBaseURI, Config aConfig) {
        super(aBaseURI, aConfig);
        setPAdES(true);
    }

    public boolean isSignWithTimestamp() {
        return signWithTimestamp;
    }

    public void setSignWithTimestamp(boolean signWithTimestamp) {
        this.signWithTimestamp = signWithTimestamp;
    }

}
