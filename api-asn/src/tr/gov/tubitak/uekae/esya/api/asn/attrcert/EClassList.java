package tr.gov.tubitak.uekae.esya.api.asn.attrcert;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.attrcert.ClassList;

/**
 * User: zeldal.ozdemir
 * Date: 3/24/11
 * Time: 10:47 AM
 */
public class EClassList extends BaseASNWrapper<ClassList> {
    public final static EClassList UNMARKED = new EClassList(ClassList.unmarked);
    public final static EClassList UNCLASSIFIED = new EClassList(ClassList.unclassified);
    public final static EClassList RESTRICTED = new EClassList(ClassList.restricted);
    public final static EClassList CONFIDENTIAL = new EClassList(ClassList.confidential);
    public final static EClassList SECRET = new EClassList(ClassList.secret);
    public final static EClassList TOPSECRET = new EClassList(ClassList.topSecret);

    public EClassList(byte[] aBytes) throws ESYAException {
        super(aBytes, new ClassList());
    }

    public EClassList(int clearanceLvl) {
        super(new ClassList());
        mObject.set(clearanceLvl);
    }

    public EClassList(boolean[] bitValues) {
        super(new ClassList(bitValues));
    }

    public EClassList(int numbits, byte[] data) {
        super(new ClassList(numbits, data));
    }

    public static EClassList getClassList(int clearanceLvl) {
        switch (clearanceLvl) {
            case ClassList.unmarked:
                return UNMARKED;
            case ClassList.unclassified:
                return UNCLASSIFIED;
            case ClassList.restricted:
                return RESTRICTED;
            case ClassList.confidential:
                return CONFIDENTIAL;
            case ClassList.secret:
                return SECRET;
            case ClassList.topSecret:
                return TOPSECRET;
            default:
                return new EClassList(clearanceLvl);
        }

    }
}
