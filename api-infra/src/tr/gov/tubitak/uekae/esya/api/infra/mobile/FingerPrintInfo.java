package tr.gov.tubitak.uekae.esya.api.infra.mobile;

import java.util.Observable;

public class FingerPrintInfo extends Observable {

    private String fingerPrint;
    private IMobileSigner mobileSigner;

    public FingerPrintInfo(IMobileSigner mobileSigner){
        this.mobileSigner = mobileSigner;
    }

    public void setFingerPrint(String fingerPrint){
        this.fingerPrint = fingerPrint;
        setChanged();
        notifyObservers(fingerPrint);
    }

    public String getFingerPrint(){
        return fingerPrint;
    }

    public IMobileSigner getMobileSigner(){
        return mobileSigner;
    }
}
