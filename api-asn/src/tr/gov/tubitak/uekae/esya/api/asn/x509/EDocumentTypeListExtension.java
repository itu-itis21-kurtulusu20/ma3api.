package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1GeneralizedTime;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1PrintableString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

import java.util.Calendar;

/**
 * Created by ramazan.girgin on 26.12.2016.
 */
public class EDocumentTypeListExtension extends BaseASNWrapper<DocumentTypeListSyntax> implements ExtensionType {

    public final static String MULTI_VALUE_SPLITTER = "###";
    public EDocumentTypeListExtension() {
        super(new DocumentTypeListSyntax());
    }

    public EDocumentTypeListExtension(String multiValueString) {
        super(new DocumentTypeListSyntax());
        String[] values = multiValueString.split(MULTI_VALUE_SPLITTER);
        loadFromDocValueList(values);
    }

    public EDocumentTypeListExtension(String ... docTypeValueList) {
        super(new DocumentTypeListSyntax());
        loadFromDocValueList(docTypeValueList);
    }

    public void loadFromDocValueList(String ... docTypeValueList) {
        if(docTypeValueList == null)
            return;
        DocumentTypeListSyntax documentTypeObject = getObject();

        documentTypeObject.version = new DocumentTypeListVersion(0);

        Asn1PrintableString[] documentTypeValueElemList = new Asn1PrintableString[docTypeValueList.length];
        for (int i = 0; i < docTypeValueList.length; i++) {
            documentTypeValueElemList[i]=new Asn1PrintableString(docTypeValueList[i]);
        }

        _SetOfDocumentType documentType = new _SetOfDocumentType(documentTypeValueElemList);
        mObject.docTypeList = documentType;
    }

    public EDocumentTypeListExtension(DocumentTypeListSyntax aObject) {
        super(aObject);
    }

    public EDocumentTypeListExtension(byte[] encoded) throws ESYAException {
        super(encoded, new DocumentTypeListSyntax());
    }

    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_ce_id_icao_mrtd_security_extensionsdocumentTypeList, false, this);
    }

    public String[] getValueList(){
        String[] retList = null;
        DocumentTypeListSyntax documentTypeObject = getObject();
        if(documentTypeObject==null)
            return null;
        _SetOfDocumentType docTypeList = documentTypeObject.docTypeList;
        if(docTypeList == null)
            return null;
        Asn1PrintableString[] elements = docTypeList.elements;
        if(elements==null)
            return null;
        retList = new String[elements.length];
        for (int i = 0; i < elements.length; i++) {
            retList[i]=elements[i].value;
        }
        return retList;
    }
}
