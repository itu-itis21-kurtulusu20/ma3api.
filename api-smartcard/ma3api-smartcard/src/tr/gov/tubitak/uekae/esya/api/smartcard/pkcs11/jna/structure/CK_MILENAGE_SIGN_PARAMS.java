package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.ArrayList;
import java.util.List;

public class CK_MILENAGE_SIGN_PARAMS extends Structure {

    public NativeLong ulMilenageFlags = new NativeLong();
    public NativeLong ulEncKiLen = new NativeLong();
    public Pointer pEncKi = Pointer.NULL;
    public NativeLong ulEncOPcLen = new NativeLong();
    public Pointer pEncOPc = Pointer.NULL;
    public NativeLong hSecondaryKey = new NativeLong(0L, true);
    public NativeLong hRCKey = new NativeLong(0L, true);
    public byte[] sqn = new byte[6];
    public byte[] amf = new byte[2];
    public NativeLong count = new NativeLong(1L, true);

    public CK_MILENAGE_SIGN_PARAMS() {
        setAlignType(ALIGN_NONE);
    }

    @Override
    protected List<String> getFieldOrder() {
        List<String> orderList = new ArrayList<>();

        orderList.add("ulMilenageFlags");
        orderList.add("ulEncKiLen");
        orderList.add("pEncKi");
        orderList.add("ulEncOPcLen");
        orderList.add("pEncOPc");
        orderList.add("hSecondaryKey");
        orderList.add("hRCKey");
        orderList.add("sqn");
        orderList.add("amf");
        orderList.add("count");

        return orderList;
    }
}
