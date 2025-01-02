package tr.gov.tubitak.uekae.esya.cmpapi.esya.caControl;

/**
 * Created with IntelliJ IDEA.
 * User: ramazan.girgin
 * Date: 02.07.2013
 * Time: 13:16
 * To change this template use File | Settings | File Templates.
 */
public class CAServiceStatus {
    boolean initialized=false;

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public CACertServiceState getCertServiceState() {
        return certServiceState;
    }

    public void setCertServiceState(CACertServiceState certServiceState) {
        this.certServiceState = certServiceState;
    }

    public CACRLServiceState getCacrlServiceState() {
        return cacrlServiceState;
    }

    public void setCacrlServiceState(CACRLServiceState cacrlServiceState) {
        this.cacrlServiceState = cacrlServiceState;
    }

    CACertServiceState certServiceState;
    CACRLServiceState cacrlServiceState;

    public CAServiceStatus(boolean initialized, CACertServiceState certServiceState, CACRLServiceState cacrlServiceState) {
        this.initialized = initialized;
        this.certServiceState = certServiceState;
        this.cacrlServiceState = cacrlServiceState;
    }

    public CAServiceStatus() {
    }
}
