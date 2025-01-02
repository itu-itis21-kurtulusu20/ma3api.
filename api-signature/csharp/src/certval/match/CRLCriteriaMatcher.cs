using System;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.util;
using Logger = log4net.ILog;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace tr.gov.tubitak.uekae.esya.api.signature.certval.match
{
    public class CRLCriteriaMatcher
    {
        private static Logger logger = log4net.LogManager.GetLogger(typeof(CRLCriteriaMatcher));

        public bool match(CRLSearchCriteria aCriteria, ECRL aCrl){
            /*
              a) Check that the string format of the issuer's DN of the CRL generated
            as stated in XMLDSIG, is the same as the value present in the Issuer
            element.

            b) Check that the time indicated by the thisUpdate field in the CRL is
            the same as the time indicated by the IssueTime element.

            c) If the CRL contains the cRLNumber extension, check that its value is
            the same as the value indicated by the Number element.

            d) If the aforementioned checks are successful, compute the digest of
            the CRL according to the algorithm indicated in the ds:DigestMethod
            element, base-64 encode the result and check if this is the same as the
            contents of the ds:DigestValue element.
            */


            // aCriteria'da var olan kriterlere gore kardilastirma yapalim

            // ISSUER
            if(aCriteria.getIssuer() != null) {
                String crlIssuer = aCrl.getIssuer().stringValue();
                String criteriaIssuer = LDAPDNUtil.normalize(aCriteria.getIssuer());
                if(!crlIssuer.Equals(criteriaIssuer)) {
                    logger.Debug("Issuer in CRL: '"+crlIssuer+"' does not match '"+criteriaIssuer+"'");
                    return false;
                }
            }
            /* issuer
            String crlIssuer = aCrl.getIssuer().stringValue();
            String criteriaIssuer = LDAPDNUtil.normalize(aCriteria.getIssuer());
            if (!crlIssuer.equals(criteriaIssuer)){
                if (logger.isDebugEnabled())
                    logger.debug("Issuer in CRL: '"+crlIssuer+"' does not match '"+criteriaIssuer+"'");
                return false;
            }//*/

            // THIS UPDATE
            if(aCriteria.getIssueTime() != null) {
                DateTime? crlThisUpdate = aCrl.getThisUpdate().Value;
                DateTime? criteriaIssueTime = aCriteria.getIssueTime();
                if(!crlThisUpdate.Equals(criteriaIssueTime)) {
               // if(UtilEsitlikler.esitMi(crlThisUpdate,criteriaIssueTime)) {
                    logger.Debug("This update in CRL: '"+aCrl.getThisUpdate().Value+"' does not match '"+aCriteria.getIssueTime()+"'");
                    return false;
                }
            }
            /* this update
            if (!aCrl.getThisUpdate().getTime().equals(aCriteria.getIssueTime())){
                if (logger.isDebugEnabled())
                    logger.debug("This update in CRL: '"+aCrl.getThisUpdate().getTime()+"' does not match '"+aCriteria.getIssueTime()+"'");
                return false;
            }//*/

            if(aCriteria.getNumber() != null) {
                BigInteger crlNumber = aCrl.getCRLNumber();
                BigInteger criteriaNumber = aCriteria.getNumber();
                if(!crlNumber.Equals(criteriaNumber)) {
                    logger.Debug("CRL number : '"+aCrl.getCRLNumber()+"' does not match '"+aCriteria.getNumber()+"'");
                    return false;
                }
            }
            /* crl number
            if (aCriteria.getNumber()!=null && !aCrl.getCRLNumber().equals(aCriteria.getNumber())){
                if (logger.isDebugEnabled())
                    logger.debug("CRL number : '"+aCrl.getCRLNumber()+"' does not match '"+aCriteria.getNumber()+"'");
                return false;
            }//*/

            try {
                String crlIssuer = aCrl.getIssuer().stringValue();
                byte[] digest = DigestUtil.digest(aCriteria.getDigestAlg(), aCrl.getEncoded());
                if(!digest.SequenceEqual(aCriteria.getDigestValue())){
                    if (logger.IsDebugEnabled)
                        logger.Debug("Digest of CRL: '"+crlIssuer+"' does not match reference! ");
                    return false;
                }
            } catch (Exception x){
                Console.WriteLine(x.StackTrace);
                return false;
            }

            return true;
        }
    }
}
