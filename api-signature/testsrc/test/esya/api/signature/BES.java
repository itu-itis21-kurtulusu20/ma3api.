package test.esya.api.signature;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import tr.gov.tubitak.uekae.esya.api.signature.*;

import static org.junit.Assert.*;


/**
 * @author ayetgin
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BES extends BaseTest
{

    @Test
    public void createDetached() throws Exception
    {
        SignatureContainer container = SignatureFactory.createContainer(config.getFormat(), createContext());

        Signature signature = container.createSignature(settings.getSignersCertificate());

        signature.addContent(settings.getContent(), false);

        signature.sign(settings.getSigner());

        write(container, FileNames.BES_DETACHED);
    }

    @Test
    public void createEnveloping() throws Exception
    {
        SignatureContainer container = SignatureFactory.createContainer(config.getFormat());

        Signature signature = container.createSignature(settings.getSignersCertificate());

        signature.addContent(settings.getContent(), true);

        signature.sign(settings.getSigner());

        write(container, FileNames.BES_ENVELOPING);
    }

    @Test
    public void validateDetached() throws Exception
    {
        Context c = createContext();
        ContainerValidationResult cvr = validateSignature("bes_detached", c);
        assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        assertEquals(1, cvr.getSignatureValidationResults().size());
    }

    @Test
    public void validateEnveloping() throws Exception
    {
        ContainerValidationResult cvr = validateSignature("bes_enveloping");
        assertEquals(ContainerValidationResultType.ALL_VALID, cvr.getResultType());
        assertEquals(1, cvr.getSignatureValidationResults().size());
    }

}
