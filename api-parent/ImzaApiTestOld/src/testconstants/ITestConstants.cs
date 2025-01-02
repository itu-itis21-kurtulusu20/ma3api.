using System;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace NETAPI_TEST.src.testconstants
{
    interface ITestConstants
    {

        /**
         * Some examples needs more than one one signer. This function provides the second signer. 
         * @param aAlg
         * @return
         * @throws Exception
         */
        //BaseSigner getSecondSignerInterface(SignatureAlg aAlg);

        /**
         * Returns the signer certificate
         * @return
         * @throws Exception
         */
        ECertificate getSignerCertificate();


        ECertificate getEncryptionCertificate();


        /**
         * Some examples needs more than one one signer. This function provides the second signer certificate.
         * @return
         * @throws Exception
         */
        //ECertificate getSecondSignerCertificate();


        /**
         * Returns the policy
         * @return
         * @throws ESYAException
         * @throws FileNotFoundException
         */
        ValidationPolicy getPolicy();

        /**
         * Returns the time stamp settings
         * @return
         */
        TSSettings getTSSettings();

        /**
         * Returns the directory that generated signatures will be written. 
         * @return
         */
        //String getDirectory();

        /**
         * Returns the PIN.
         * @return
         */
        String getPIN();

        CardType getCardType();

    }
}

