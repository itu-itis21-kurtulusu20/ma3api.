package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

/**
 * @author ahmety
 *         date: Jan 28, 2010
 */
public class EGeneralName extends BaseASNWrapper<GeneralName> {
    public final static byte _OTHERNAME = GeneralName._OTHERNAME;
    public final static byte _RFC822NAME = GeneralName._RFC822NAME;
    public final static byte _DNSNAME = GeneralName._DNSNAME;
    public final static byte _X400ADDRESS = GeneralName._X400ADDRESS;
    public final static byte _DIRECTORYNAME = GeneralName._DIRECTORYNAME;
    public final static byte _EDIPARTYNAME = GeneralName._EDIPARTYNAME;
    public final static byte _UNIFORMRESOURCEIDENTIFIER = GeneralName._UNIFORMRESOURCEIDENTIFIER;
    public final static byte _IPADDRESS = GeneralName._IPADDRESS;
    public final static byte _REGISTEREDID = GeneralName._REGISTEREDID;


    public EGeneralName(GeneralName aName) {
        super(aName);
    }

    public EGeneralName() {
        super(new GeneralName());
    }

    public int getType() {
        return mObject.getChoiceID();
    }

    public void setOtherName(AnotherName value) {
        mObject.setElement(GeneralName._OTHERNAME, value);
    }

    public void setOtherNameForUPN(String value) {
        Asn1DerEncodeBuffer safeEncBuf = new Asn1DerEncodeBuffer();
        Asn1UTF8String upnUTF8 = new Asn1UTF8String(value);

        try {
            upnUTF8.encode(safeEncBuf);
        } catch (Exception ex1) {
            throw new ESYARuntimeException(ex1.getMessage() + "(Buraya hic gelmemeli)", ex1);
        }
        Asn1OpenType upnOpenType = new Asn1OpenType(safeEncBuf.getMsgCopy());

        AnotherName upnAnother = new AnotherName(new Asn1ObjectIdentifier(_ImplicitValues.id_win_upn), upnOpenType);

        mObject.setElement(GeneralName._OTHERNAME, upnAnother);
    }

    public void setOtherNameForDC(byte[] value) {
        EGeneralName genName = new EGeneralName();
        Asn1OctetString guid = new Asn1OctetString(value);
        Asn1DerEncodeBuffer safeEncBuf = new Asn1DerEncodeBuffer();

        try {
            guid.encode(safeEncBuf);
        } catch (Exception ex1) {
            throw new ESYARuntimeException("Buraya hic gelmemeli", ex1);
        }
        Asn1OpenType guidOpenType = new Asn1OpenType(safeEncBuf.getMsgCopy());

        AnotherName guidAnother = new AnotherName(new Asn1ObjectIdentifier(_ImplicitValues.id_win_otherNameforDC), guidOpenType);

        mObject.setElement(GeneralName._OTHERNAME, guidAnother);
    }

    public void setRfc822Name(String value) {
        mObject.setElement(GeneralName._RFC822NAME, new Asn1IA5String(value));
    }

    public void setDNSName(String value) {
        mObject.setElement(GeneralName._DNSNAME, new Asn1IA5String(value));
    }

    public void setX400Address(ORAddress value) {
        mObject.setElement(GeneralName._X400ADDRESS, value);
    }

    public void setDirectoryName(Name value) {
        mObject.setElement(GeneralName._DIRECTORYNAME, value);
    }

    public void setDirectoryName(EName value) {
        mObject.setElement(GeneralName._DIRECTORYNAME, value.getObject());
    }

    public void setEdiPartyName(EDIPartyName value) {
        mObject.setElement(GeneralName._EDIPARTYNAME, value);
    }

    public void setUniformResourceIdentifier(String value) {
        mObject.setElement(GeneralName._UNIFORMRESOURCEIDENTIFIER, new Asn1IA5String(value));
    }

    public void setIPAddress(byte[] value) {
        mObject.setElement(GeneralName._IPADDRESS, new Asn1OctetString(value));
    }

    public void setRegisteredID(Asn1ObjectIdentifier value) {
        mObject.setElement(GeneralName._REGISTEREDID, value);
    }

    public AnotherName getOtherName() {
        if (mObject.getChoiceID() == GeneralName._OTHERNAME)
            return (AnotherName) mObject.getElement();
        return null;
    }

    public String getRfc822Name() {
        if (mObject.getChoiceID() == GeneralName._RFC822NAME)
            return ((Asn1IA5String) mObject.getElement()).value;
        return null;
    }

    public String getDNSName() {
        if (mObject.getChoiceID() == GeneralName._DNSNAME)
            return ((Asn1IA5String) mObject.getElement()).value;
        return null;
    }

    public ORAddress getX400Address() {
        if (mObject.getChoiceID() == GeneralName._X400ADDRESS)
            return (ORAddress) mObject.getElement();
        return null;
    }

    public EName getDirectoryName() {
        if (mObject.getChoiceID() == GeneralName._DIRECTORYNAME)
            return new EName((Name) mObject.getElement());
        return null;
    }

    public EDIPartyName getEdiPartyName() {
        if (mObject.getChoiceID() == GeneralName._EDIPARTYNAME)
            return (EDIPartyName) mObject.getElement();
        return null;
    }

    public String getUniformResourceIdentifier() {
        if (mObject.getChoiceID() == GeneralName._UNIFORMRESOURCEIDENTIFIER)
            return ((Asn1IA5String) mObject.getElement()).value;
        return null;
    }

    public byte[] getIPAddress() {
        if (mObject.getChoiceID() == GeneralName._IPADDRESS)
            return ((Asn1OctetString) mObject.getElement()).value;
        return null;
    }

    public String getIPAddressStr() {
        if (mObject.getChoiceID() == GeneralName._IPADDRESS)
            return new String(((Asn1OctetString) mObject.getElement()).value);
        return null;
    }

    public Asn1ObjectIdentifier getRegisteredID() {
        if (mObject.getChoiceID() == GeneralName._REGISTEREDID)
            return (Asn1ObjectIdentifier) mObject.getElement();
        return null;
    }

    @Override
    public String toString() {
        return UtilName.generalName2String(mObject);
    }


}
