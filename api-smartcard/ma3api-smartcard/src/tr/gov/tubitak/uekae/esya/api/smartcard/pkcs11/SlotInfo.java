package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

public class SlotInfo {
    long slotId;

    String slotLabel;

    public long getSlotId() {
        return slotId;
    }

    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }

    public String getSlotLabel() {
        return slotLabel;
    }

    public void setSlotLabel(String slotLabel) {
        this.slotLabel = slotLabel;
    }
}
