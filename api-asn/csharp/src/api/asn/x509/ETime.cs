using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ETime : BaseASNWrapper<Time>
    {
        public ETime(Time aTime)
            : base(aTime)
        {
        }

        public ETime(byte[] aBytes)
            : base(aBytes, new Time())
        {
        }


        public /*Calendar*/DateTime? getTime()
        {
            try
            {
                if (mObject.ChoiceID == Time._UTCTIME)
                {
                    Asn1UTCTime utcObj = (Asn1UTCTime)mObject.GetElement();
                    int year = utcObj.Year;    //for Asn error
                    return utcObj.GetTime();
                }
                else if (mObject.ChoiceID == Time._GENERALTIME)
                {
                    Asn1GeneralizedTime generalizedTimeObj = (Asn1GeneralizedTime)mObject.GetElement();
                    int year = generalizedTimeObj.Year;    //for Asn error
                    return generalizedTimeObj.GetTime().ToLocalTime();
                }

                throw new ESYAException("Unknown Choice in Asn1 Time Object");
            }
            catch (Asn1Exception ex)
            {
                throw new ESYAException("Asn1Decode Error", ex);
            }
        }

        public void setGeneralTime(DateTime? aDateTime)
        {
            Asn1GeneralizedTime generalTime = new Asn1GeneralizedTime();
            generalTime.SetTime(aDateTime.Value);
            mObject.Set_generalTime(generalTime);
        }

        public void setUTCTime(DateTime? aDateTime)
        {
            Asn1UTCTime utcTime = new Asn1UTCTime();
            utcTime.SetTime(aDateTime.Value);
            mObject.Set_utcTime(utcTime);
        }

    }
}
