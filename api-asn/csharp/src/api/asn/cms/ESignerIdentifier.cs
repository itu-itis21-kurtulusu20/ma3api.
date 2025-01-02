using System;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ESignerIdentifier : BaseASNWrapper<SignerIdentifier>
    {
        public ESignerIdentifier(SignerIdentifier aObject)
            : base(aObject)
        {
        }
        public ESignerIdentifier(byte[] aBytes)
            : base(aBytes, new SignerIdentifier())
        {

        }
        public int getType()
        {
            return mObject.ChoiceID;
        }

        public void setIssuerAndSerialNumber(EIssuerAndSerialNumber aIssuerAndSerial)
        {
            mObject.SetElement(SignerIdentifier._ISSUERANDSERIALNUMBER, aIssuerAndSerial.getObject());
        }

        public void setSubjectKeyIdentifier(byte[] aSubjectKeyIdentifier)
        {
            mObject.SetElement(SignerIdentifier._SUBJECTKEYIDENTIFIER, new Asn1OctetString(aSubjectKeyIdentifier));
        }

        public EIssuerAndSerialNumber getIssuerAndSerialNumber()
        {
            if (mObject.ChoiceID == SignerIdentifier._ISSUERANDSERIALNUMBER)
                return new EIssuerAndSerialNumber((IssuerAndSerialNumber)mObject.GetElement());

            return null;
        }

        public byte[] getSubjectKeyIdentifier()
        {
            if (mObject.ChoiceID == SignerIdentifier._SUBJECTKEYIDENTIFIER)
                return ((Asn1OctetString)mObject.GetElement()).mValue;

            return null;
        }

        public bool isEqual(ECertificate aCertificate)
        {
            EIssuerAndSerialNumber issuerAndSerial = getIssuerAndSerialNumber();
            if (issuerAndSerial != null)
            {
                EIssuerAndSerialNumber certIssuerAndSerial = new EIssuerAndSerialNumber(aCertificate);
                if (certIssuerAndSerial.Equals(issuerAndSerial))
                    return true;
                else
                    return false;
            }

            byte[] subjectKeyId = getSubjectKeyIdentifier();
            if (subjectKeyId != null)
            {
                EExtensions extensions = aCertificate.getExtensions();
                if (extensions == null)
                    return false;
                ESubjectKeyIdentifier certSki = extensions.getSubjectKeyIdentifier();

                if (certSki == null)
                    return false;

                if (subjectKeyId.SequenceEqual(certSki.getObject().mValue))
                    return true;
                else
                    return false;
            }

            return false;

        }

        public bool Equals(Object obj)
        {
            if (obj is ESignerIdentifier)
            {
                ESignerIdentifier objSignerID = (ESignerIdentifier)obj;
                if (getIssuerAndSerialNumber() != null && objSignerID.getIssuerAndSerialNumber() != null)
                    return getIssuerAndSerialNumber().Equals(objSignerID.getIssuerAndSerialNumber());

                if (getSubjectKeyIdentifier() != null && objSignerID.getSubjectKeyIdentifier() != null)
                    return getSubjectKeyIdentifier().SequenceEqual(objSignerID.getSubjectKeyIdentifier());
            }

            return base.Equals(obj);
        }

    }
}
