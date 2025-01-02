package tr.gov.tubitak.uekae.esya.api.asn.scencryptedpackage;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.scencryptedpackage.EncryptedDataPackage;

import java.io.InputStream;

public class EEncryptedDataPackage extends BaseASNWrapper<EncryptedDataPackage> {
    public EEncryptedDataPackage(EncryptedDataPackage aObject) {
        super(aObject);
    }

    public EEncryptedDataPackage(byte[] aBytes, EncryptedDataPackage aObject) throws ESYAException {
        super(aBytes, aObject);
    }

    public EEncryptedDataPackage(String aBase64Encoded, EncryptedDataPackage aObject) throws ESYAException {
        super(aBase64Encoded, aObject);
    }

    public EEncryptedDataPackage(InputStream aStream, EncryptedDataPackage aObject) throws ESYAException {
        super(aStream, aObject);
    }

    public EEncryptedDataPackage(byte[] aBytes) throws ESYAException {
        super(aBytes, new EncryptedDataPackage());
    }

    public EEncryptedDataPackage(long version, byte[] wrappedKey, byte[] iv, byte[] aad, byte[] encryptedData) {
        super(new EncryptedDataPackage(version, wrappedKey, iv, aad, encryptedData));
    }

    public EEncryptedDataPackage(long version, byte[] wrappedKey, byte[] iv, byte[] encryptedData) {
        super(new EncryptedDataPackage(version, wrappedKey, iv, encryptedData));
    }

    public long getVersion(){
        return mObject.version.value;
    }

    public byte[] getWrappedKey(){
        return mObject.wrappedKey.value;
    }

    public byte[] getIv(){
        return mObject.iv.value;
    }

    public byte[] getAad(){
        if(mObject.aad == null)
            return null;
        return mObject.aad.value;
    }

    public byte[] getEncryptedData(){
        return mObject.encryptedData.value;
    }
}
