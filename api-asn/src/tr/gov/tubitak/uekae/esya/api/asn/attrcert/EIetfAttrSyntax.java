package tr.gov.tubitak.uekae.esya.api.asn.attrcert;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1Type;
import com.objsys.asn1j.runtime.Asn1UTF8String;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EGeneralNames;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.attrcert.IetfAttrSyntax;
import tr.gov.tubitak.uekae.esya.asn.attrcert.IetfAttrSyntax_values_element;
import tr.gov.tubitak.uekae.esya.asn.attrcert._SeqOfIetfAttrSyntax_values_element;

/**
 * User: zeldal.ozdemir
 * Date: 3/23/11
 * Time: 1:34 PM
 */
public class EIetfAttrSyntax extends BaseASNWrapper<IetfAttrSyntax>{
    public EIetfAttrSyntax(IetfAttrSyntax aObject) {
        super(aObject);
    }

    public EIetfAttrSyntax() {
        super(new IetfAttrSyntax());
    }

    public EIetfAttrSyntax(byte[] aBytes) throws ESYAException {
        super(aBytes, new IetfAttrSyntax());
    }

    public void setPolicyAuthority(EGeneralNames policyAuthority){
        this.mObject.policyAuthority = policyAuthority.getObject();
    }

    public void addOctet(byte[] octetBytes){
        IetfAttrSyntax_values_element element = new IetfAttrSyntax_values_element();
        element.set_octets(new Asn1OctetString(octetBytes));
        addElement(element);
    }

    public void addString(String elementS){
        IetfAttrSyntax_values_element element = new IetfAttrSyntax_values_element();
        element.set_string(new Asn1UTF8String(elementS));
        addElement(element);
    }

    public void addObjectIdentifier(Asn1ObjectIdentifier objectIdentifier){
        IetfAttrSyntax_values_element element = new IetfAttrSyntax_values_element();
        element.set_oid(objectIdentifier);
        addElement(element);
    }

    private void addElement(IetfAttrSyntax_values_element element) {
        if(this.mObject.values == null)
            this.mObject.values = new _SeqOfIetfAttrSyntax_values_element();
        this.mObject.values.elements = extendArray(this.mObject.values.elements,element);
    }

    public int getElemCount(){
        if(mObject == null || mObject.values == null || mObject.values.elements == null)
            return 0;
        return mObject.values.elements.length;
    }

    public Asn1Type getElem(int i){
        return mObject.values.elements[i].getElement();
    }

    public int getElemType(int i){
        return mObject.values.elements[i].getChoiceID();
    }


}
