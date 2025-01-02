package dev.esya.api.xmlsignature.legacy;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 30.01.2013
 * Time: 08:31
 * To change this template use File | Settings | File Templates.
 */
//runs checker unit tests (no signing operation!!!)

@RunWith(Suite.class)

@Suite.SuiteClasses({CheckPolicyDigestValues.class,CheckArchiveTimeStamp.class,CheckCertificateDigestValues.class,CheckCompleteCertRefs.class,CheckCompleteRevocationRefsAndValues.class,
        CheckRevocationValues.class,CheckSigAndRefsTimeStamp.class,CheckSignatureType.class,CheckSignedObjectDigestValues.class,
        CheckSignedPropertiesDigestValues.class,CheckSigningCertificate.class,CheckTimeStamp.class,CheckTimeStampValidationData.class,SignValidator.class})

public final class SubUnitTests {}
