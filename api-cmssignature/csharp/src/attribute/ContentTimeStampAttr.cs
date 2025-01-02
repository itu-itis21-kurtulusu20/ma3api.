using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;
using tr.gov.tubitak.uekae.esya.asn.cms;


//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * The content-time-stamp attribute is an attribute that is the time-stamp token of the signed data content before it
 * is signed.
 * (etsi 101733v010801 5.11.4)
 * @author aslihan.kubilay
 *
 */
    public class ContentTimeStampAttr : AttributeValue
    {
        private static readonly DigestAlg OZET_ALG = DigestAlg.SHA256;
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_contentTimestamp;
        /**
         * Default constructor
         */  
        public ContentTimeStampAttr()
            : base()
        {
        }
        /**
        * Take time stamp
         * @throws CMSSignatureException
         * @throws NullParameterException
        */
        public override void setValue()
        {
            Object content = null;
            mAttParams.TryGetValue(AllEParameters.P_CONTENT, out content);

            if (content == null)
            {
                content = mAttParams[AllEParameters.P_EXTERNAL_CONTENT];
                if (content == null)
                    throw new CMSSignatureException("For contenttimestamp attribute,content couldnot be found in signeddata or in parameters");
            }

            Object digestAlgO = null;
            mAttParams.TryGetValue(AllEParameters.P_TS_DIGEST_ALG, out digestAlgO);

            if (digestAlgO == null)
            {
                digestAlgO = OZET_ALG;
            }

            Object tssSpec = null;
            mAttParams.TryGetValue(AllEParameters.P_TSS_INFO, out tssSpec);

            if (tssSpec == null)
            {
                throw new NullParameterException("P_TSS_INFO parameter is not set");
            }


            ISignable contentI = null;

            try
            {
                contentI = (ISignable)content;
            }
            catch (InvalidCastException ex)
            {
                throw new CMSSignatureException("P_EXTERNAL_CONTENT parameter is not of type ISignable", ex);
            }
            DigestAlg digestAlg = null;
            try
            {
                digestAlg = (DigestAlg)digestAlgO;
            }
            catch (InvalidCastException ex)
            {
                throw new CMSSignatureException("P_TS_DIGEST_ALG parameter is not of type DigestAlg", ex);
            }

            TSSettings tsSettings = null;
            try
            {
                tsSettings = (TSSettings)tssSpec;
            }
            catch (InvalidCastException ex)
            {
                throw new CMSSignatureException("P_TSS_INFO parameter is not of type TSSettings", ex);
            }

            //P_TS_DIGEST_ALG deprecated,take digest alg from tsSettings
            if (digestAlg == tsSettings.DigestAlg && digestAlgO != null)
                logger.Debug("P_TS_DIGEST_ALG deprecated,digest alg taken from tsSettings");
            digestAlg = tsSettings.DigestAlg;

            ContentInfo token = new ContentInfo();
            try
            {
                TSClient tsClient = new TSClient();
                token = tsClient.timestamp(contentI.getMessageDigest(digestAlg), tsSettings).getContentInfo().getObject();
            }
            catch (Exception ex1)
            {
                throw new CMSSignatureException("Zaman damgasi alinirken hata olustu", ex1);
            }
            _setValue(token);
        }

        /**
    * Checks whether attribute is signed or not.
    * @return True 
    */ 
        public override bool isSigned()
        {
            return true;
        }

        /**
            * Returns Attribute OID of content time stamp attribute
            * @return
            */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }
        /**
         * Returns  time of content time stamp
         * @param aAttribute EAttribute
         * @return Calendar
         * @throws ESYAException
         */
        public static DateTime? toTime(EAttribute aAttribute)
        {
            return SignatureTimeStampAttr.toTime(aAttribute);
        }

    }
}
