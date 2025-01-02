using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp
{
    /**
     * Checks the validity of OCSP Response status 
     */
    public class ResponseStatusChecker : OCSPResponseChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        [Serializable]
        public class ResponseStatusCheckStatus : CheckStatus
        {
            public static readonly ResponseStatusCheckStatus SUCCESSFUL = new ResponseStatusCheckStatus(OCSPResponseStatus.successful().mValue);
            public static readonly ResponseStatusCheckStatus MALFORMED_REQUEST = new ResponseStatusCheckStatus(OCSPResponseStatus.malformedRequest().mValue);
            public static readonly ResponseStatusCheckStatus INTERNAL_ERROR = new ResponseStatusCheckStatus(OCSPResponseStatus.internalError().mValue);
            public static readonly ResponseStatusCheckStatus TRY_LATER = new ResponseStatusCheckStatus(OCSPResponseStatus.tryLater().mValue);
            public static readonly ResponseStatusCheckStatus SIG_REQUIRED = new ResponseStatusCheckStatus(OCSPResponseStatus.sigRequired().mValue);
            public static readonly ResponseStatusCheckStatus UNAUTHORIZED = new ResponseStatusCheckStatus(OCSPResponseStatus.unauthorized().mValue);

            readonly int mValue;
            
            ResponseStatusCheckStatus(int aEnum)
            {
                mValue = aEnum;
            }
            // todo

            // override object.Equals
            public override bool Equals(object obj)
            {
                //       
                // See the full list of guidelines at
                //   http://go.microsoft.com/fwlink/?LinkID=85237  
                // and also the guidance for operator== at
                //   http://go.microsoft.com/fwlink/?LinkId=85238
                //

                if (obj == null || GetType() != obj.GetType())
                {
                    return false;
                }

                // TODO: write your implementation of Equals() here
                return this.mValue == ((ResponseStatusCheckStatus)obj).mValue;
            }
            // override object.GetHashCode
            public override int GetHashCode()
            {
                return mValue.GetHashCode();
            }
            public String getText()
            {
                if (this == SUCCESSFUL)
                {
                    return Resource.message(Resource.BASARILI);
                }
                else if (this == MALFORMED_REQUEST)
                {
                    return Resource.message(Resource.MALFORMED_REQUEST);
                }
                else if (this == INTERNAL_ERROR)
                {
                    return Resource.message(Resource.INTERNAL_ERROR);
                }
                else if (this == TRY_LATER)
                {
                    return Resource.message(Resource.TRY_LATER);
                }
                else if (this == SIG_REQUIRED)
                {
                    return Resource.message(Resource.SIG_REQUIRED);
                }
                else if (this == UNAUTHORIZED)
                {
                    return Resource.message(Resource.UNAUTHORIZED);
                }
                else
                {
                    return Resource.message(Resource.KONTROL_SONUCU);
                }

            }
        }


        /**
         * OCSP Cevabı Cevap Durumu bilgisini kontrol eder.
         */
        protected override PathValidationResult _check(EOCSPResponse aOCSPResponse, OCSPResponseStatusInfo aOCSPCevapBilgisi)
        {
            int status = aOCSPResponse.getResponseStatus();
            if (logger.IsDebugEnabled)
                logger.Debug("Gelen cevap status : " + status);

            if (status == OCSPResponseStatus.successful().mValue)
            {
                if (logger.IsDebugEnabled)
                    logger.Debug("Cevap başarılı");
                aOCSPCevapBilgisi.addDetail(this, ResponseStatusCheckStatus.SUCCESSFUL, true);
                return PathValidationResult.SUCCESS; //successful
            }
            else if (status == OCSPResponseStatus.malformedRequest().mValue)
            {
                logger.Error("Cevap status: malformedRequest");
                aOCSPCevapBilgisi.addDetail(this, ResponseStatusCheckStatus.MALFORMED_REQUEST, false);
                return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE; //malformedRequest
            }
            else if (status == OCSPResponseStatus.internalError().mValue)
            {
                logger.Error("Cevap status: internalError");
                aOCSPCevapBilgisi.addDetail(this, ResponseStatusCheckStatus.INTERNAL_ERROR, false);
                return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE; //internalError
            }
            else if (status == OCSPResponseStatus.tryLater().mValue)
            {
                logger.Error("Cevap status: tryLater");
                aOCSPCevapBilgisi.addDetail(this, ResponseStatusCheckStatus.TRY_LATER, false);
                return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE; //tryLater
            }
            else if (status == OCSPResponseStatus.sigRequired().mValue)
            {
                logger.Error("Cevap status: sigRequired");
                aOCSPCevapBilgisi.addDetail(this, ResponseStatusCheckStatus.SIG_REQUIRED, false);
                return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE; //sigRequired
            }
            else if (status == OCSPResponseStatus.unauthorized().mValue)
            {
                logger.Error("Cevap status: unauthorized");
                aOCSPCevapBilgisi.addDetail(this, ResponseStatusCheckStatus.UNAUTHORIZED, false);
                return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE; //unauthorized
            }
            else
            {
                logger.Error("Cevap status: unknown");
                return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE;
            }

        }

        public override String getCheckText()
        {
            //return Resource.message(Resource.OCSP_CEVABI_KONTROLCU);
            return Resource.message(Resource.OCSP_CEVABI_KONTROLCU);
        }
    }
}
