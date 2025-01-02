using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace tr.gov.tubitak.uekae.esya.api.asn.ocsp
{
    public class ESingleResponse : BaseASNWrapper<SingleResponse>
    {
        public static byte CERTSTATUS_GOOD = CertStatus._GOOD;
        public static byte CERTSTATUS_REVOKED = CertStatus._REVOKED;
        public static byte CERTSTATUS_UNKOWN = CertStatus._UNKNOWN;

        public ESingleResponse(SingleResponse aObject) : base(aObject) { }

        public ECertID getCertID()
        {
            return new ECertID(mObject.certID);
        }


        public int getCertificateStatus()
        {
            return mObject.certStatus.ChoiceID;
        }

        public int getRevocationReason()
        {
            if (mObject.certStatus.GetElement() is RevokedInfo)
            {
                RevokedInfo revoke = (RevokedInfo)mObject.certStatus.GetElement();
                if (revoke.revocationReason == null)
                    return -1;
                return revoke.revocationReason.mValue;
            }
            else
            {
                return -1;
            }
        }

        public DateTime? getRevocationTime()
        {
            if (mObject.certStatus.GetElement() is RevokedInfo)
            {
                RevokedInfo revoke = (RevokedInfo)mObject.certStatus.GetElement();
                try
                {
                    Asn1GeneralizedTime revocationTime = revoke.revocationTime;
                    int year = revocationTime.Year;   //for Asn error
                    return revocationTime.GetTime().ToLocalTime();
                }
                catch (Asn1Exception aEx)
                {
                    //aEx.printStackTrace();
                    Console.WriteLine(aEx.StackTrace);
                    return null;
                }
            }
            else
            {
                return null;
            }
        }

        public DateTime? getThisUpdate()
        {
            try
            {
                //return mObject.thisUpdate.GetTime();
                Asn1GeneralizedTime time = mObject.thisUpdate;
                return new DateTime(time.Year, time.Month, time.Day, time.Hour, time.Minute, 
                    time.Second, time.UTC ? DateTimeKind.Utc : DateTimeKind.Local).ToLocalTime();
            }
            catch (Exception x)
            {
                return null;
            }
        }

        public DateTime? getNextUpdate()
        {
            try
            {
                int year = mObject.nextUpdate.Year;   //for Asn error
                return mObject.nextUpdate.GetTime();
            }
            catch (Exception x)
            {
                return null;
            }
        }

        public EExtensions getSingleExtensions()
        {
            return new EExtensions(mObject.singleExtensions);
        }
    }
}
