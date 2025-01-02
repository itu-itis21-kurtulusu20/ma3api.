package dev.esya.api.xmlsignature.legacy;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.esya.api.xmlsignature.creation.*;

/**
 * Created with IntelliJ IDEA.
 * User: yavuz.kahveci
 * Date: 22.11.2012
 * Time: 08:31
 * To change this template use File | Settings | File Templates.
 */
//runs all the unit tests!!!

@RunWith(Suite.class)

@Suite.SuiteClasses({SignWithRevoked.class,SignWithP2.class, SignWithP3.class, SignWithP4.class,T01_SignWithBES.class,T03_SignWithEPES.class,T02_SignWithEST.class,T04_SignWithESC.class,T05_SignWithESX.class,T06_SignWithESXL.class,T07_SignWithESA.class,
        SignEDefter.class ,AddExtraArchiveTimeStamp.class,CheckPolicyDigestValues.class,CheckArchiveTimeStamp.class,CheckCertificateDigestValues.class,CheckCompleteCertRefs.class,CheckCompleteRevocationRefsAndValues.class,
        CheckRevocationValues.class,CheckSigAndRefsTimeStamp.class,CheckSignatureType.class,CheckSignatureValue.class,CheckSignedObjectDigestValues.class,
        CheckSignedPropertiesDigestValues.class,CheckSigningCertificate.class,CheckTimeStamp.class,CheckTimeStampValidationData.class,
        SignParallel.class,SignSerialCounter.class,SignValidator.class})

public final class AllUnitTests{}
