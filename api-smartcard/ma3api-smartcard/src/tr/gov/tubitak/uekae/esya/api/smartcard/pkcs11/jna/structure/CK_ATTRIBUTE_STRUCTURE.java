package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.jna.structure;

import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.NativeLongByReference;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;

import java.util.ArrayList;
import java.util.List;

public class CK_ATTRIBUTE_STRUCTURE extends Structure {

    static final ByteByReference tru = new ByteByReference((byte) 0x01);
    static final ByteByReference fals = new ByteByReference((byte) 0x00);

    public NativeLong type = new NativeLong(0, true);
    public Pointer pValue = Pointer.NULL;
    public NativeLong ulValueLen = new NativeLong(0, true);

    public CK_ATTRIBUTE_STRUCTURE() {
        super();
        setAlignType(ALIGN_NONE);
    }

    public CK_ATTRIBUTE_STRUCTURE(CK_ATTRIBUTE template) {
        this.type = new NativeLong(template.type, true);
        this.setValueAndLength(template.pValue);
    }

    @Override
    protected List<String> getFieldOrder() {
        List<String> fieldOrder = new ArrayList<>();

        fieldOrder.add("type");
        fieldOrder.add("pValue");
        fieldOrder.add("ulValueLen");

        return fieldOrder;
    }

    public void setFields(long type, com.sun.jna.ptr.ByReference pValue, long ulValueLen) {
        this.type = new NativeLong(type, true);
        this.pValue = pValue.getPointer();
        this.ulValueLen = new NativeLong(ulValueLen, true);
    }

    public void setFields(long type, long pValue) {
        setFields(
            type,
            new NativeLongByReference(new NativeLong(pValue, true)),
            NativeLong.SIZE
        );
    }

    public void setFields(long type, boolean pValue) {
        setFields(
            type,
            pValue ? tru : fals,
            1L
        );
    }

    public void setFields(CK_ATTRIBUTE attribute) {
        this.type = new NativeLong(attribute.type, true);
        setValueAndLength(attribute.pValue);
    }

    public void setValueAndLength(Object pValue) {
        long length;
        if (pValue == null) {
            this.pValue = Pointer.NULL;
            length = 0L;
        } else if (pValue instanceof Boolean) {
            this.pValue = (((boolean) pValue) ? tru : fals).getPointer();
            length = 1L;
        } else if (pValue instanceof Byte) {
            this.pValue = new ByteByReference((Byte) pValue).getPointer();
            length = 1L;
        } else if (pValue instanceof Integer) {
            this.pValue = new IntByReference((Integer) pValue).getPointer();
            length = Integer.SIZE;
        } else if (pValue instanceof Long) {
            this.pValue = new LongByReference((Long) pValue).getPointer();
            length = NativeLong.SIZE;
        } else {
            // try processing the value

            byte[] pValueBytes;

            if (pValue instanceof byte[]) {
                pValueBytes = (byte[]) pValue;
            } else /* if (pValue instanceof String) */ {
                String string = pValue.toString();
                pValueBytes = string.getBytes();
            }

            length = pValueBytes.length;

            this.pValue = new Memory(length);
            this.pValue.write(0L, pValueBytes, 0, (int) length);
        }

        this.ulValueLen = new NativeLong(length, true);
    }

    public static CK_ATTRIBUTE_STRUCTURE[] newArrayEmpty(int size) {
        CK_ATTRIBUTE_STRUCTURE[] array = new CK_ATTRIBUTE_STRUCTURE[size];
        for (int c = 0; c < size; c++) {
            array[c] = new CK_ATTRIBUTE_STRUCTURE();
        }

        return (CK_ATTRIBUTE_STRUCTURE[]) array[0].toArray(array);
    }

    public static CK_ATTRIBUTE_STRUCTURE[] newArrayFilled(CK_ATTRIBUTE[] template) {
        final int size = template.length;
        CK_ATTRIBUTE_STRUCTURE[] array = newArrayEmpty(size);

        for (int c = 0; c < size; c++) {
            array[c].setFields(template[c]);
        }

        return array;
    }

    public static CK_ATTRIBUTE_STRUCTURE[] newArrayFilled(long[] types) {
        final int size = types.length;
        CK_ATTRIBUTE_STRUCTURE[] array = newArrayEmpty(size);

        for (int c = 0; c < size; c++) {
            array[c].type = new NativeLong(types[c]);
        }

        return array;
    }
}
