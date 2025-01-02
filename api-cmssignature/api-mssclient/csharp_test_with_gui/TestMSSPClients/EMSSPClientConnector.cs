using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.mobile;
using tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

namespace TestMSSPClients
{
    public class EMSSPClientConnector:MSSPClientConnector
    {
        private EMSSPRequestHandler msspRequestHandler;
        public EMSSPClientConnector(EMSSPRequestHandler msspRequestHandler)
        {
            this.msspRequestHandler = msspRequestHandler;
        }

        public ECertificate[] getCertificates(UserIdentifier aUserID)
        {
            PhoneNumberAndOperator phoneNumberAndOperator = (PhoneNumberAndOperator) aUserID;
            return msspRequestHandler.GetCertificates(phoneNumberAndOperator);
        }

        public byte[] sign(byte[] dataToBeSigned, SigningMode aMode, UserIdentifier aUserID, ECertificate aSigningCert, string informativeText, string aSigningAlg)
        {
            if(aMode!=SigningMode.SIGNHASH)
            {
                throw new Exception("Unsuported signing mode. Only SIGNHASH supported.");
            }
            PhoneNumberAndOperator phoneNumberAndOperator = (PhoneNumberAndOperator)aUserID;
            return msspRequestHandler.Sign(dataToBeSigned, aMode, phoneNumberAndOperator, informativeText, aSigningAlg);
        }
    }
}
