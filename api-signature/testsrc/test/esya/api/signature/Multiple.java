package test.esya.api.signature;

import junit.framework.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.signature.*;

/**
 * @author ayetgin
 */
public class Multiple extends BaseTest
{

    @Test
    public void createParallelToExisting() throws Exception {
        Context c = createContext();
        SignatureContainer sc = readSignatureContainer(FileNames.BES_ENVELOPING, c);
        Signature s = sc.createSignature(settings.getSignersCertificate());
        s.addContent(settings.getContent(), true);
        s.sign(settings.getSigner());
        write(sc, FileNames.PARALLEL_BES);
    }

    @Test
    public void createParallelToExistingDetached() throws Exception {
        Context c = createContext();
        c.setData(null);
        SignatureContainer sc = readSignatureContainer(FileNames.BES_DETACHED, c);
        Signature s = sc.createSignature(settings.getSignersCertificate());
        s.addContent(settings.getContent(), false);
        s.sign(settings.getSigner());
        write(sc, FileNames.PARALLEL_BES_DETACHED);
    }

    @Test
    public void createSerialToExisting() throws Exception {
        Context c = createContext();
        SignatureContainer sc = readSignatureContainer(FileNames.BES_ENVELOPING, c);
        Signature cs = sc.getSignatures().get(0).createCounterSignature(settings.getSignersCertificate());
        cs.sign(settings.getSigner());
        write(sc, FileNames.SERIAL_BES);
    }

    @Test
    public void createSerialToSerial() throws Exception {
        Context c = createContext();
        SignatureContainer sc = readSignatureContainer(FileNames.SERIAL_BES, c);
        Signature s = sc.getSignatures().get(0);
        Signature counter = s.getCounterSignatures().get(0);
        Signature counterOfCounter = counter.createCounterSignature(settings.getSignersCertificate());
        counterOfCounter.sign(settings.getSigner());
        write(sc, FileNames.SERIAL_TO_SERIAL_BES);

    }

    @Test
    public void validateSerial() throws Exception {
        ContainerValidationResult cvr = validateSignature(FileNames.SERIAL_BES);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

    @Test
    public void validateParallel() throws Exception {
        ContainerValidationResult cvr = validateSignature(FileNames.PARALLEL_BES);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }
    @Test
    public void validateParallelDetached() throws Exception {
        ContainerValidationResult cvr = validateSignature(FileNames.PARALLEL_BES_DETACHED);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }
    @Test
    public void validateSerialToSerial() throws Exception {
        ContainerValidationResult cvr = validateSignature(FileNames.SERIAL_TO_SERIAL_BES);
        Assert.assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
    }

}
