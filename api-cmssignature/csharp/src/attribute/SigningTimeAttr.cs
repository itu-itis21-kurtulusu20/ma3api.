using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.x509;


//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
     * The signing-time attribute specifies the time at which the signer claims to have 
     * performed the signing process.
     * 
     * Signing-time attribute values for ES have the ASN.1 type SigningTime as defined in CMS (RFC 3852 [4]).
     * NOTE: RFC 3852 [4] states that "dates between January 1, 1950 and December 31, 2049 (inclusive) must be
     * encoded as UTCTime. Any dates with year values before 1950 or after 2049 must be encoded as GeneralizedTime".
     * (etsi 101733v010801 5.9.1)  
     */
    public class SigningTimeAttr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_signingTime;
        protected DateTime? mSigningTime;

        /**
        * Create SigningTimeAttr with signing time
         * @param aSigningTime Calendar
         * @throws NullParameterException
         */
        public SigningTimeAttr(DateTime? aSigningTime)
            : base()
        {
            mSigningTime = aSigningTime;
            if (mSigningTime == null)
                throw new NullParameterException("Signing time must be set");
        }


        private Time _zamanAl(DateTime? aTime)
        {
            Asn1UTCTime now = new Asn1UTCTime();
            try
            {
                now.SetTime(aTime.Value);
            }
            catch (Asn1Exception ex)
            {
                throw new CMSSignatureException(/*GenelDil.ASN1_ENCODE_HATASI*/Resource.message(Resource.ASN1_ENCODE_HATASI), ex);
            }
            Time time = new Time();
            time.Set_utcTime(now);
            return time;
        }

        /**
        * Set signing time
        */
        public override void setValue()
        {
            _setValue(_zamanAl(mSigningTime));
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
         * Returns AttributeOID of signing time attribute
        * @return
        */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }
        /**
         * Returns  signing time
         * @param aAttribute EAttribute
         * @return Calendar
         * @throws ESYAException
         */
        public static DateTime? toTime(EAttribute aAttribute)
        {
            Asn1DerDecodeBuffer buff = new Asn1DerDecodeBuffer(aAttribute.getValue(0));
            Asn1UTCTime utcTime = new Asn1UTCTime();
            try
            {
                utcTime.Decode(buff);
                DateTime? dateTime = new DateTime(utcTime.Year, utcTime.Month, utcTime.Day, utcTime.Hour, utcTime.Minute, utcTime.Second, utcTime.UTC ? DateTimeKind.Utc : DateTimeKind.Local);
                DateTime? localTime = dateTime.Value.ToLocalTime();
                return localTime;
            }
            catch (Exception e)
            {
                throw new ESYAException("Asn1 decode error", e);
            }
        }
    }
}
