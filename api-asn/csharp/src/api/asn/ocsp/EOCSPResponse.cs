using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.ocsp;
namespace tr.gov.tubitak.uekae.esya.api.asn.ocsp
{
    public class EOCSPResponse : BaseASNWrapper<OCSPResponse>
    {
        private EBasicOCSPResponse mBasicResponse;

        public static EOCSPResponse getEOCSPResponse(EBasicOCSPResponse aResponse)
        {
            OCSPResponse response = new OCSPResponse();
            response.responseStatus = OCSPResponseStatus.successful();
            response.responseBytes = new ResponseBytes(new Asn1ObjectIdentifier(_ocspValues.id_pkix_ocsp_basic), new Asn1OctetString(aResponse.getEncoded()));
            return new EOCSPResponse(response);
        }

        public EOCSPResponse(OCSPResponse aObject)
            : base(aObject)
        {

        }
        public EOCSPResponse(byte[] aBytes)
            : base(aBytes, new OCSPResponse())
        {
        }

        public EOCSPResponse(OCSPResponseStatus responseStatus,
                         Asn1ObjectIdentifier oid,
                         byte[] responseBytes)
            : base(new OCSPResponse(
                    responseStatus,
                    new ResponseBytes(
                            oid,
                            new Asn1OctetString(responseBytes)
                    )))
        {
    }

        public EBasicOCSPResponse getBasicOCSPResponse()
        {
            Console.Write("");
            if (mBasicResponse != null)
                return mBasicResponse;

            BasicOCSPResponse basicResponse = new BasicOCSPResponse();
            try
            {
                byte[] value = mObject.responseBytes.response.mValue;
                Asn1DerDecodeBuffer a = new Asn1DerDecodeBuffer(value);
                basicResponse.Decode(a);
            }
            catch (Exception aEx)
            {
                Console.WriteLine(aEx.StackTrace);
                return null;
            }
            mBasicResponse = new EBasicOCSPResponse(basicResponse);
            return mBasicResponse;
        }


        public ESingleResponse getSingleResponse()
        {
            return getSingleResponse(0);
        }

        public int getSingleResponseCount()
        {
            EBasicOCSPResponse basicResponse = getBasicOCSPResponse();
            if (basicResponse == null)
            {
                return -1;
            }
            return basicResponse.getTbsResponseData().getSingleResponseCount();
        }


        public ESingleResponse getSingleResponse(int aSorguSirasi)
        {
            EBasicOCSPResponse basicResponse = getBasicOCSPResponse();
            if (basicResponse == null)
            {
                return null;
            }
            //return new ESingleResponse(basicResponse.getTbsResponseData().responses.elements[aSorguSirasi]);
            return basicResponse.getTbsResponseData().getSingleResponse(aSorguSirasi);
        }

        public byte[] getTbsResponseData()
        {
            EBasicOCSPResponse basicResponse = getBasicOCSPResponse();
            if (basicResponse == null)
            {
                return null;
            }
            return basicResponse.getTbsResponseData().getBytes();
        }

        public byte[] getSignatureValue()
        {
            EBasicOCSPResponse basicResponse = getBasicOCSPResponse();
            if (basicResponse == null)
            {
                return null;
            }
            return basicResponse.getObject().signature.mValue;
        }

        public int getResponseStatus()
        {
            return mObject.responseStatus.mValue;
        }

        public Asn1ObjectIdentifier getResponseType()
        {
            if (mObject.responseBytes != null)
                return mObject.responseBytes.responseType;
            return null;
        }

        public byte[] getResponseBytes()
        {
            if (mObject.responseBytes != null)
                return mObject.responseBytes.response.mValue;
            return null;
        }
    }
}
