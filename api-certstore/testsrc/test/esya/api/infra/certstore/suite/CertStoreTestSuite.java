package test.esya.api.infra.certstore.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.esya.api.infra.certstore.*;
import test.esya.api.infra.certstore.xml.ConvertToXmlDb;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ConvertToXmlDb.class,
        CertStoreCertificateOpsTest.class,
        CertStoreCRLOpsTest.class,
        CertStoreDirectoryOpsTest.class,
        CertStoreOCSPOpsTest.class,
        CertStoreRootCertificateOpsTest.class,
})
public class CertStoreTestSuite {

}

