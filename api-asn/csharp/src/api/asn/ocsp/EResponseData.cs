using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Version = tr.gov.tubitak.uekae.esya.asn.ocsp.Version;

namespace tr.gov.tubitak.uekae.esya.api.asn.ocsp
{
    /**
  * @author ayetgin
  */
    public class EResponseData : BaseASNWrapper<ResponseData>
    {
        public EResponseData(ResponseData aObject)
            : base(aObject)
        {
            //super(aObject);
        }

        /**
         *  _BYNAME = 1
         *  _BYKEY = 2;
         * @return responder option type
         */
        public int getResponderIDType()
        {
            return mObject.responderID.ChoiceID;
        }

        public EName getResponderIdByName()
        {
            return new EName((Name)mObject.responderID.GetElement());
        }

        public byte[] getResponderIdByKey()
        {
            return ((Asn1OctetString)mObject.responderID.GetElement()).mValue;
        }

        public EResponderID getResponderID()
        {
            return new EResponderID(mObject.responderID);
        }

        public DateTime? getProducedAt()
        {
            try
            {
                Asn1GeneralizedTime produced = mObject.producedAt;
                return new DateTime(produced.Year, produced.Month, produced.Day, produced.Hour, produced.Minute, produced.Second, produced.UTC ? DateTimeKind.Utc : DateTimeKind.Local).ToLocalTime();
            }
            catch (Asn1Exception x)
            {
                return null;
            }
        }

        public long getVersion()
        {
            Version ver = mObject.version;
            if (ver == null)
                return Version.v1;

            return ver.mValue;
        }

        public int getSingleResponseCount()
        {
            return mObject.responses.getLength();
        }

        public ESingleResponse getSingleResponse(int aIndex)
        {
            return new ESingleResponse(mObject.responses.elements[aIndex]);
        }

    }

}
