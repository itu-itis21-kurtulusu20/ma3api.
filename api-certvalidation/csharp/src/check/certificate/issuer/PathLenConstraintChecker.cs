using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer
{
    /**
     * 4.2.1.10 Basic Constraints pathLenConstraint field
     * it gives the maximum number of CA certificates that may
     * follow this certificate in a certification path. A value of zero
     * indicates that only an end-entity certificate may follow in the path.
     * Where it appears, the pathLenConstraint field MUST be greater than or
     * equal to zero. Where pathLenConstraint does not appear, there is no
     * limit to the allowed length of the certification path.
     *
     * @author IH
     */
    public class PathLenConstraintChecker : IssuerChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        [Serializable]
        public class PathLenConstraintCheckStatus : CheckStatus
        {

            public static readonly PathLenConstraintCheckStatus BASIC_CONST_EXTENSION_NOT_EXIST = new PathLenConstraintCheckStatus(_enum.BASIC_CONST_EXTENSION_NOT_EXIST);
            public static readonly PathLenConstraintCheckStatus BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NOT_EXISTS = new PathLenConstraintCheckStatus(_enum.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NOT_EXISTS);
            public static readonly PathLenConstraintCheckStatus BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NEGATIVE = new PathLenConstraintCheckStatus(_enum.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NEGATIVE);
            public static readonly PathLenConstraintCheckStatus BASIC_CONST_EXTENSION_LEN_CONS_VALUE_EXCEEDED = new PathLenConstraintCheckStatus(_enum.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_EXCEEDED);
            public static readonly PathLenConstraintCheckStatus BASIC_CONST_EXTENSION_LEN_CONS_VALUE_VALID = new PathLenConstraintCheckStatus(_enum.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_VALID);

            enum _enum
            {
                BASIC_CONST_EXTENSION_NOT_EXIST,
                BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NOT_EXISTS,
                BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NEGATIVE,
                BASIC_CONST_EXTENSION_LEN_CONS_VALUE_EXCEEDED,
                BASIC_CONST_EXTENSION_LEN_CONS_VALUE_VALID
            }

            readonly _enum mValue;
            PathLenConstraintCheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }
            public String getText()
            {
                switch (mValue)
                {
                    case _enum.BASIC_CONST_EXTENSION_NOT_EXIST:
                        return Resource.message(Resource.BASIC_CONST_EKLENTI_YOK);
                    case _enum.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NOT_EXISTS:
                        return Resource.message(Resource.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_YOK);
                    case _enum.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NEGATIVE:
                        return Resource.message(Resource.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_NEGATIF);
                    case _enum.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_EXCEEDED:
                        return Resource.message(Resource.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_ASILDI);
                    case _enum.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_VALID:
                        return Resource.message(Resource.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_GECERLI);

                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }

        /**
         * SM Sertifikasının Yol Uzunluğu Kısıtlamaları ile ilgili kontrolleri yapar
         */
        protected override PathValidationResult _check(IssuerCheckParameters aIssuerCheckParameters,
                                              ECertificate aIssuerCertificate, ECertificate aCertificate,
                                              CertificateStatusInfo aCertStatusInfo)
        {
            EBasicConstraints bc = aIssuerCertificate.getExtensions().getBasicConstraints();
            if (bc == null)
            {
                logger.Error("Sertifikada Basic Constraints uzantısı yok");
                aCertStatusInfo.addDetail(this, PathLenConstraintCheckStatus.BASIC_CONST_EXTENSION_NOT_EXIST, false);
                return PathValidationResult.PATHLENCONSTRAINTS_FAILURE;
            }

            if (bc.getPathLenConstraint() == null)
            {
                logger.Debug("BasicConstraints path length constraint değeri yok");
                aCertStatusInfo.addDetail(this, PathLenConstraintCheckStatus.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NOT_EXISTS, true);
                return PathValidationResult.SUCCESS;
            }

            long pathLength = bc.getPathLenConstraint().Value;
            if (pathLength < 0)
            {
                logger.Debug("BasicConstraints path length constraint değeri negatif");
                aCertStatusInfo.addDetail(this, PathLenConstraintCheckStatus.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NEGATIVE, true);
                return PathValidationResult.SUCCESS;
            }
            long kacinciSertifika = aIssuerCheckParameters.getCertificateOrder();
            if (kacinciSertifika > pathLength)
            {
                logger.Error("BasicConstraints path length constraint değeri aşıldı:" + kacinciSertifika + ">" + pathLength);
                aCertStatusInfo.addDetail(this, PathLenConstraintCheckStatus.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_EXCEEDED, false);
                return PathValidationResult.PATHLENCONSTRAINTS_FAILURE;
            }
            else
            {
                logger.Debug("BasicConstraints path length constraint değeri geçerli:" + kacinciSertifika + "<=" + pathLength);
                aCertStatusInfo.addDetail(this, PathLenConstraintCheckStatus.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_VALID, true);
                return PathValidationResult.SUCCESS;
            }

        }

        public override String getCheckText()
        {
            //return Resource.message(Resource.PATH_LEN_CONSTRAINT_KONTROLU);
            return Resource.message(Resource.PATH_LEN_CONSTRAINT_KONTROLU);
        }

    }
}
