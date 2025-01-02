package dev.esya.api.cmssignature.performance;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import java.io.FileInputStream;

public class BaseSignedDataMemoryTest {

    String P7S_PATH = "C:\\a\\files\\100MB.txt.p7s";

    @Test
    public void testNewBaseSignedData() throws Exception {
        FileInputStream fis = new FileInputStream(P7S_PATH);
        BaseSignedData bsd = new BaseSignedData(fis);
        System.out.printf(bsd.toString()); // 127 MB
    }

    @Test
    public void testOldBaseSignedData() throws Exception {
        byte[] p7sBytes = AsnIO.dosyadanOKU(P7S_PATH);
        BaseSignedData bsd = new BaseSignedData(p7sBytes);
        System.out.printf(bsd.toString()); // 588 MB
    }

}
