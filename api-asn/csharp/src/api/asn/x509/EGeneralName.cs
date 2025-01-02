using System;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EGeneralName : BaseASNWrapper<GeneralName>
    {
        public static readonly byte _OTHERNAME = GeneralName._OTHERNAME;
        public static readonly byte _RFC822NAME = GeneralName._RFC822NAME;
        public static readonly byte _DNSNAME = GeneralName._DNSNAME;
        public static readonly byte _X400ADDRESS = GeneralName._X400ADDRESS;
        public static readonly byte _DIRECTORYNAME = GeneralName._DIRECTORYNAME;
        public static readonly byte _EDIPARTYNAME = GeneralName._EDIPARTYNAME;
        public static readonly byte _UNIFORMRESOURCEIDENTIFIER = GeneralName._UNIFORMRESOURCEIDENTIFIER;
        public static readonly byte _IPADDRESS = GeneralName._IPADDRESS;
        public static readonly byte _REGISTEREDID = GeneralName._REGISTEREDID;

        public EGeneralName(GeneralName aName)
            : base(aName)
        {
            //super(aName);
        }
        public EGeneralName()
            : base(new GeneralName())
        {
            //super(new GeneralName());
        }

        public int getType()
        {
            return mObject.ChoiceID;
        }

        public void setOtherName(AnotherName value)
        {
            mObject.SetElement(GeneralName._OTHERNAME, value);
        }
        public void setOtherNameForUPN(String value)
        {
            Asn1DerEncodeBuffer safeEncBuf = new Asn1DerEncodeBuffer();
            Asn1UTF8String upnUTF8 = new Asn1UTF8String(value);

            try
            {
                upnUTF8.Encode(safeEncBuf);
            }
            catch (Exception ex1)
            {
                throw new ESYARuntimeException(ex1.Message + "(Buraya hic gelmemeli)", ex1);
            }
            Asn1OpenType upnOpenType = new Asn1OpenType(safeEncBuf.MsgCopy);

            AnotherName upnAnother = new AnotherName(new Asn1ObjectIdentifier(_ImplicitValues.id_win_upn), upnOpenType);

            mObject.SetElement(GeneralName._OTHERNAME, upnAnother);
        }

        public void setOtherNameForDC(byte[] value)
        {
            EGeneralName genName = new EGeneralName();
            Asn1OctetString guid = new Asn1OctetString(value);
            Asn1DerEncodeBuffer safeEncBuf = new Asn1DerEncodeBuffer();

            try
            {
                guid.Encode(safeEncBuf);
            }
            catch (Exception ex1)
            {
                throw new ESYARuntimeException("Buraya hic gelmemeli", ex1);
            }
            Asn1OpenType guidOpenType = new Asn1OpenType(safeEncBuf.MsgCopy);

            AnotherName guidAnother = new AnotherName(new Asn1ObjectIdentifier(_ImplicitValues.id_win_otherNameforDC), guidOpenType);

            mObject.SetElement(GeneralName._OTHERNAME, guidAnother);
        }

        public void setRfc822Name(String value)
        {
            mObject.SetElement(GeneralName._RFC822NAME, new Asn1IA5String(value));
        }

        public void setDNSName(String value)
        {
            mObject.SetElement(GeneralName._DNSNAME, new Asn1IA5String(value));
        }

        public void setX400Address(ORAddress value)
        {
            mObject.SetElement(GeneralName._X400ADDRESS, value);
        }

        public void setDirectoryName(Name value)
        {
            mObject.SetElement(GeneralName._DIRECTORYNAME, value);
        }

        public void setEdiPartyName(EDIPartyName value)
        {
            mObject.SetElement(GeneralName._EDIPARTYNAME, value);
        }

        public void setUniformResourceIdentifier(String value)
        {
            mObject.SetElement(GeneralName._UNIFORMRESOURCEIDENTIFIER, new Asn1IA5String(value));
        }

        public void setIPAddress(byte[] value)
        {
            mObject.SetElement(GeneralName._IPADDRESS, new Asn1OctetString(value));
        }

        public void setRegisteredID(Asn1ObjectIdentifier value)
        {
            mObject.SetElement(GeneralName._REGISTEREDID, value);
        }

        public AnotherName getOtherName()
        {
            if (mObject.ChoiceID == GeneralName._OTHERNAME)
                return (AnotherName)mObject.GetElement();
            return null;
        }

        public String getRfc822Name()
        {
            if (mObject.ChoiceID == GeneralName._RFC822NAME)
                return ((Asn1IA5String)mObject.GetElement()).mValue;
            return null;
        }

        public String getDNSName()
        {
            if (mObject.ChoiceID == GeneralName._DNSNAME)
                return ((Asn1IA5String)mObject.GetElement()).mValue;
            return null;
        }

        public ORAddress getX400Address(ORAddress value)
        {
            if (mObject.ChoiceID == GeneralName._X400ADDRESS)
                return (ORAddress)mObject.GetElement();
            return null;
        }

        public EName getDirectoryName()
        {
            if (mObject.ChoiceID == GeneralName._DIRECTORYNAME)
                return new EName((Name)mObject.GetElement());
            return null;
        }

        public EDIPartyName getEdiPartyName()
        {
            if (mObject.ChoiceID == GeneralName._EDIPARTYNAME)
                return (EDIPartyName)mObject.GetElement();
            return null;
        }

        public String getUniformResourceIdentifier()
        {
            if (mObject.ChoiceID == GeneralName._UNIFORMRESOURCEIDENTIFIER)
                return ((Asn1IA5String)mObject.GetElement()).mValue;
            return null;
        }

        public byte[] getIPAddress()
        {
            if (mObject.ChoiceID == GeneralName._IPADDRESS)
                return ((Asn1OctetString)mObject.GetElement()).mValue;
            return null;
        }

        public String getIPAddressStr()
        {
            if (mObject.ChoiceID == GeneralName._IPADDRESS)
                return Encoding.Default.GetString(getIPAddress());
            return null;

        }

        public Asn1ObjectIdentifier getRegisteredID()
        {
            if (mObject.ChoiceID == GeneralName._REGISTEREDID)
                return (Asn1ObjectIdentifier)mObject.GetElement();
            return null;
        }
        //@Override
        public override String ToString()
        {
            return UtilName.generalName2String(mObject);
        }

    }
}
