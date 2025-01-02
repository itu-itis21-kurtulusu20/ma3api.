package test.esya.api.signature;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CRLSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.OCSPSearchCriteria;

import static junit.framework.TestCase.assertNotNull;

public class TestPojos {

    @Test
    public void testPojos() {
        assertNotNull(PojoUtils.testPojo(CertificateSearchCriteria.class));
        assertNotNull(PojoUtils.testPojo(CRLSearchCriteria.class));
        assertNotNull(PojoUtils.testPojo(OCSPSearchCriteria.class));
    }
}
