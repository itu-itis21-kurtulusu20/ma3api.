package tr.gov.tubitak.uekae.esya.api.asn.attrcert;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.attrcert.Clearance;
import tr.gov.tubitak.uekae.esya.asn.attrcert.SecurityCategory;
import tr.gov.tubitak.uekae.esya.asn.attrcert._SetOfSecurityCategory;

import java.util.Arrays;
import java.util.List;

/**
 * User: zeldal.ozdemir
 * Date: 3/24/11
 * Time: 9:38 AM
 */
public class EClearance extends BaseASNWrapper<Clearance> {
    public EClearance(Clearance aObject) {
        super(aObject);
    }

    public EClearance(byte[] aBytes) throws ESYAException {
        super(aBytes, new Clearance());
    }

    public EClearance(Asn1ObjectIdentifier policyID, EClassList clearance) {
        super(new Clearance(policyID));
        mObject.classList = clearance.getObject();
    }

/*    public EClearance(Asn1ObjectIdentifier policyID, EClassList clearance, List<Pair<int[],byte[]>> securityCategories) {
        super(new Clearance(policyID));
        mObject.classList = clearance.getObject();
        SecurityCategory[] elements_ = new SecurityCategory[securityCategories.size()];
        for (int i = 0; i < elements_.length; i++) {
            Pair<int[], byte[]> categPair = securityCategories.get(i);
            elements_[i] = new SecurityCategory(categPair.getObject1(), new Asn1OpenType(categPair.getObject2()));
        }
        mObject.securityCategories = new _SetOfSecurityCategory(elements_);
    }  */

    public EClearance(Asn1ObjectIdentifier policyID, EClassList clearance, List<ESecurityCategory> securityCategories) {
        super(new Clearance(policyID));
        mObject.classList = clearance.getObject();
        SecurityCategory[] elements_ = unwrapArray(securityCategories.toArray(new ESecurityCategory[securityCategories.size()]));
        mObject.securityCategories = new _SetOfSecurityCategory(elements_);
    }

/*    public void addSecurityCategory() {  // not clear...
        SecurityCategory[] elements = null;
        if (mObject.securityCategories != null)
            elements = mObject.securityCategories.elements;
        SecurityCategory securityCategory = new SecurityCategory();
        mObject.securityCategories = new _SetOfSecurityCategory(extendArray(elements, securityCategory));
    }*/
    public Asn1ObjectIdentifier getPolicyID(){
        return mObject.policyId;
    }

    public EClassList getClearance(){
        if(mObject.classList == null
                || mObject.classList.value == null
                || mObject.classList.value.length < 1 )
            throw new ESYARuntimeException("ClassList is Empty!");
        return EClassList.getClassList(mObject.classList.value[0]);
    }


    public List<ESecurityCategory> getSecurityCategories(){
        return Arrays.asList(wrapArray(mObject.securityCategories.elements, ESecurityCategory.class));
    }

/*    public static void main(String[] args) {
        try {
            ArrayList<ESecurityCategory> securityCategories = new ArrayList<ESecurityCategory>();
            Asn1Integer asn1Integer = new Asn1Integer(123);
            Asn1DerEncodeBuffer asn1DerEncodeBuffer = new Asn1DerEncodeBuffer();
            asn1Integer.encode(asn1DerEncodeBuffer);
            ESecurityCategory e = new ESecurityCategory(new int[]{1,2,34,645},asn1DerEncodeBuffer.getMsgCopy());
            securityCategories.add(e);
            EClearance clearance = new EClearance(EAttrCertValues.oid_at_clearance, EClassList.TOPSECRET, securityCategories);
            byte[] encoded = clearance.getEncoded();
            EClearance clearance2 = new EClearance(encoded);
            System.out.println(clearance2.getClearance());
            System.out.println(clearance2.getSecurityCategories().size());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }*/
}


