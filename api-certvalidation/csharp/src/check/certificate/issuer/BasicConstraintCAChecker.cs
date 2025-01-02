using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer
{
    /**
     * Checks whether Basic Constraints extension information in the issuer
     * certificate has CA feature. 
     *
     * @author IH
     */
    public class BasicConstraintCAChecker : IssuerChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        [Serializable]
        public class BasicConstraintCACheckStatus : CheckStatus
        {
            public static readonly BasicConstraintCACheckStatus NO_BASIC_CONST_EXTENSION = new BasicConstraintCACheckStatus(_enum.NO_BASIC_CONST_EXTENSION);
            public static readonly BasicConstraintCACheckStatus INVALID_BASIC_CONST_EXTENSION = new BasicConstraintCACheckStatus(_enum.INVALID_BASIC_CONST_EXTENSION);
            public static readonly BasicConstraintCACheckStatus NO_BASIC_CONST_EXTENSION_CA = new BasicConstraintCACheckStatus(_enum.NO_BASIC_CONST_EXTENSION_CA);
            public static readonly BasicConstraintCACheckStatus INVALID_BASIC_CONST_EXTENSION_CA = new BasicConstraintCACheckStatus(_enum.INVALID_BASIC_CONST_EXTENSION_CA);
            public static readonly BasicConstraintCACheckStatus BASIC_CONST_EXTENSION_CA_OK = new BasicConstraintCACheckStatus(_enum.BASIC_CONST_EXTENSION_CA_OK);

            enum _enum
            {
                NO_BASIC_CONST_EXTENSION,
                INVALID_BASIC_CONST_EXTENSION,
                NO_BASIC_CONST_EXTENSION_CA,
                INVALID_BASIC_CONST_EXTENSION_CA,
                BASIC_CONST_EXTENSION_CA_OK
            }
            readonly _enum mValue;
            BasicConstraintCACheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }

            public String getText()
            {
                switch (mValue)
                {
                    case _enum.NO_BASIC_CONST_EXTENSION:
                        return Resource.message(Resource.BASIC_CONST_EKLENTI_YOK);
                    case _enum.INVALID_BASIC_CONST_EXTENSION:
                        return Resource.message(Resource.BASIC_CONST_EKLENTISI_BOZUK);
                    case _enum.NO_BASIC_CONST_EXTENSION_CA:
                        return Resource.message(Resource.BASIC_CONST_EKLENTI_CA_DEGERI_YOK);
                    case _enum.INVALID_BASIC_CONST_EXTENSION_CA:
                        return Resource.message(Resource.BASIC_CONST_EKLENTI_CA_DEGERI_YANLIS);
                    case _enum.BASIC_CONST_EXTENSION_CA_OK:
                        return Resource.message(Resource.BASIC_CONST_EKLENTI_CA_DEGERI_DOGRU);
                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }

        /**
         * Temel Kısıtlamalar Eklentisi ile ilgili kontrolleri yapar
         */
        protected override PathValidationResult _check(IssuerCheckParameters aConstraintcheckParam,
                                              ECertificate aIssuerCertificate, ECertificate aCertificate,
                                              CertificateStatusInfo aCertStatusInfo)
        {
            EBasicConstraints bc = aIssuerCertificate.getExtensions().getBasicConstraints();
            if (bc == null)
            {
                logger.Error("Sertifikada Basic Constraints uzantısı yok");
                aCertStatusInfo.addDetail(this, BasicConstraintCACheckStatus.NO_BASIC_CONST_EXTENSION, false);
                return PathValidationResult.BASICCONSTRAINTS_FAILURE;
            }
            if (bc.getObject().cA == null)
            {
                logger.Error("BasicConstraints CA değeri yok");
                aCertStatusInfo.addDetail(this, BasicConstraintCACheckStatus.NO_BASIC_CONST_EXTENSION_CA, false);
                return PathValidationResult.BASICCONSTRAINTS_FAILURE;
            }
            if (bc.getObject().cA.mValue)
            {
                aCertStatusInfo.addDetail(this, BasicConstraintCACheckStatus.BASIC_CONST_EXTENSION_CA_OK, true);
                return PathValidationResult.SUCCESS;
            }
            else
            {
                logger.Error("BasicConstraints CA geçersiz.");
                aCertStatusInfo.addDetail(this, BasicConstraintCACheckStatus.INVALID_BASIC_CONST_EXTENSION_CA, false);
                return PathValidationResult.BASICCONSTRAINTS_FAILURE;
            }
        }

        public override String getCheckText()
        {
            //return Resource.message(Resource.BASIC_CONST_CA_KONTROLU);
            return Resource.message(Resource.BASIC_CONST_CA_KONTROLU);
        }
    }
}
