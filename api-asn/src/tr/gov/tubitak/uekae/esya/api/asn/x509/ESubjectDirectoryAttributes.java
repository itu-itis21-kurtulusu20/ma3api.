package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.asn.util.UtilSubjectDirectoryAttr;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author ayetgin
 */
public class ESubjectDirectoryAttributes extends BaseASNWrapper<SubjectDirectoryAttributes> {
    private static final Logger logger = LoggerFactory.getLogger(ESubjectDirectoryAttributes.class);

    public ESubjectDirectoryAttributes(SubjectDirectoryAttributes aObject) {
        super(aObject);
    }

    public ESubjectDirectoryAttributes(EAttribute[] attributes) {
        super(new SubjectDirectoryAttributes());
        mObject.elements = BaseASNWrapper.unwrapArray(attributes );
    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();
        Attribute[] attributes = mObject.elements;
        if (attributes!=null){
            for (Attribute anAttrlar : attributes) {
                result.append(_attributeCikar(anAttrlar)).append("\n");
            }
        }
        return result.toString();
    }

    public Asn1OpenType[] getAttributeValue(Asn1ObjectIdentifier aOID) throws IOException, Asn1Exception {
        Attribute[] attrAr = this.getObject().elements;
        for (int i = 0; i < attrAr.length; i++)
            if (attrAr[i].type.equals(aOID))
                return attrAr[i].values.elements;
        return null;
    }

    public ERoleSyntax getRoleSyntax() {
        try {
            Asn1OpenType[] attributeValues = getAttributeValue(UtilSubjectDirectoryAttr.oid_at_role);
            if(attributeValues == null)
                return null;
            return new ERoleSyntax(attributeValues[0].value);
        } catch (Exception ex) {
            logger.error("Failed to Read RoleSyntax.", ex);
            return null;
        }
    }

    private String _attributeCikar(Attribute aAttribute) {
        Asn1ObjectIdentifier tip = aAttribute.type;
        Asn1OpenType[] degerler = aAttribute.values.elements;
        try {
            if (tip.equals(UtilSubjectDirectoryAttr.oid_pda_countryOfCitizenship)) {
                return CertI18n.message(CertI18n.COUNTRYCITIZENSHIP) + " = " + _printableToString(degerler);
            }
            else if (tip.equals(UtilSubjectDirectoryAttr.oid_pda_countryOfResidence)) {
                return CertI18n.message(CertI18n.COUNTRYRESIDENCE) + " = " + _printableToString(degerler);
            }
            else if (tip.equals(UtilSubjectDirectoryAttr.oid_pda_dateOfBirth)) {
                return CertI18n.message(CertI18n.DATEOFBIRTH) + " = " + _dateToString(degerler);
            }
            else if (tip.equals(UtilSubjectDirectoryAttr.oid_pda_gender)) {
                return CertI18n.message(CertI18n.GENDER) + " = " + _printableToString(degerler);
            }
            else if (tip.equals(UtilSubjectDirectoryAttr.oid_pda_placeOfBirth)) {
                return CertI18n.message(CertI18n.PLACEOFBIRTH) + " = " + _directoryToString(degerler);
            }
            else if (tip.equals(UtilSubjectDirectoryAttr.oid_at_role)) {
                return CertI18n.message(CertI18n.ROLE) + " = " + _roleToString(degerler);
            }/*else if(tip.equals(UtilSubjectDirectoryAttr.oid_win_upn))
       	 {
       		 
       	 }*/
        } catch (Exception aEx) {
            logger.warn("Attribute degeri alınırken hata oluştu", aEx);
            return "";
        }


        return "";
    }

    private String _printableToString(Asn1OpenType[] aDegerler) throws ESYAException {
        try {
            String attr = "";
            for (int i = 0; i < aDegerler.length; i++) {
                Asn1OpenType value = aDegerler[i];
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                value.encode(encBuf);
                Asn1PrintableString val = new Asn1PrintableString();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(encBuf.getMsgCopy());
                val.decode(decBuf);
                attr += val.value;
            }
            return attr;
        } catch (Exception e) {
            throw new ESYAException(e);
        }
    }

    private String _dateToString(Asn1OpenType[] aDegerler) throws ESYAException {
        try {
            String attr = "";
            for (int i = 0; i < aDegerler.length; i++) {
                Asn1OpenType value = aDegerler[i];
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                value.encode(encBuf);
                Asn1GeneralizedTime val = new Asn1GeneralizedTime();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(encBuf.getMsgCopy());
                val.decode(decBuf);

                attr += new SimpleDateFormat("dd.MM.yyyy").format(val.getTime().getTime());
            }
            return attr;
        } catch (Exception e) {
            throw new ESYAException(e);
        }
    }

    private String _directoryToString(Asn1OpenType[] aDegerler) throws ESYAException {
        try {
            String attr = "";
            for (int i = 0; i < aDegerler.length; i++) {
                Asn1OpenType value = aDegerler[i];
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                value.encode(encBuf);
                DirectoryString val = new DirectoryString();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(encBuf.getMsgCopy());
                val.decode(decBuf);
                switch (val.getChoiceID()) {
                    case DirectoryString._UTF8STRING: {
                        attr += ((Asn1UTF8String) val.getElement()).value;
                        break;
                    }
                    case DirectoryString._PRINTABLESTRING: {
                        attr += ((Asn1PrintableString) val.getElement()).value;
                        break;
                    }
                    case DirectoryString._TELETEXSTRING: {
                        attr += ((Asn1T61String) val.getElement()).value;
                        break;
                    }
                    case DirectoryString._UNIVERSALSTRING: {
                        attr += ((Asn1UniversalString) val.getElement()).value;
                        break;
                    }
                    case DirectoryString._BMPSTRING: {
                        attr += ((Asn1BMPString) val.getElement()).value;
                        break;
                    }
                    default:
                        throw new ESYAException("Unknown ChoiceID");
                }
            }
            return attr;
        } catch (Exception e) {
            throw new ESYAException(e);
        }
    }

    private String _roleToString(Asn1OpenType[] aDegerler) throws ESYAException {
        try {
            String attr = "";
            for (int i = 0; i < aDegerler.length; i++) {
                Asn1OpenType value = aDegerler[i];
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                value.encode(encBuf);
                RoleSyntax val = new RoleSyntax();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(encBuf.getMsgCopy());
                val.decode(decBuf);
                GeneralName rolName = val.roleName;
                if (rolName.getChoiceID() == GeneralName._REGISTEREDID) {
                    int[] oid = ((Asn1ObjectIdentifier) rolName.getElement()).value;
                    attr += "{";
                    for (int j = 0; j < oid.length; j++) {
                        attr += oid[j] + " ";
                    }
                    attr += "}";
                }

            }
            return attr;
        } catch (Exception e) {
            throw new ESYAException(e);
        }
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_subjectDirectoryAttributes, aCritic, this);
    }

}
