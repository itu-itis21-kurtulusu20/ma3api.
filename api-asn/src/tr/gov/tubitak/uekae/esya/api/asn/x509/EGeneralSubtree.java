package tr.gov.tubitak.uekae.esya.api.asn.x509;

import com.objsys.asn1j.runtime.Asn1IA5String;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralName;
import tr.gov.tubitak.uekae.esya.asn.x509.GeneralSubtree;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ahmety
 * date: Jan 29, 2010
 */
public class EGeneralSubtree extends BaseASNWrapper<GeneralSubtree>
{

    public EGeneralSubtree(GeneralSubtree aObject)
    {
        super(aObject);
    }

    public EGeneralSubtree(EGeneralName aBase, long aMinimum, long aMaximum){
        super(new GeneralSubtree(aBase.getObject(), aMinimum, aMaximum));
    }

    public EGeneralSubtree(EGeneralName aBase){
        super(new GeneralSubtree(aBase.getObject()));
    }

    public EGeneralName getBase(){
        return new EGeneralName(mObject.base);
    }

    public long getMinimum()
    {
        return mObject.minimum.value;
    }

    public Long getMaximum()
    {
        if (mObject.maximum==null)
            return null;
        return mObject.maximum.value;
    }

    public boolean permits(Name aName)
    {
        return permits(new EName(aName));
    }

    public boolean permits(EName aName)
    {
        if (mObject.base.getChoiceID() != GeneralName._DIRECTORYNAME)
            return true;

        return aName.isSubNameOf((Name) mObject.base.getElement());
    }

    public boolean permits(GeneralName aGeneralName)
    {
        if (mObject.base.getChoiceID() != aGeneralName.getChoiceID())
            return true;

        switch (mObject.base.getChoiceID()) {
            case GeneralName._DIRECTORYNAME: {
                return permits((Name) aGeneralName.getElement());
            }
            case GeneralName._RFC822NAME: {
                return EName.isSubNameOf(((Asn1IA5String) aGeneralName.getElement()).value, ((Asn1IA5String) mObject.base.getElement()).value, "@");
            }
            case GeneralName._DNSNAME: {
                return EName.isSubNameOf(((Asn1IA5String) aGeneralName.getElement()).value, ((Asn1IA5String) mObject.base.getElement()).value);
            }
            case GeneralName._UNIFORMRESOURCEIDENTIFIER: {
                return EName.isSubURINameOf(((Asn1IA5String) aGeneralName.getElement()).value, ((Asn1IA5String) mObject.base.getElement()).value);
            }
            default:
                return true;
        }
    }

    public boolean excludes(EName aName)
    {
        if (mObject.base.getChoiceID() != GeneralName._DIRECTORYNAME)
            return false;

        return aName.isSubNameOf((Name)mObject.base.getElement());
        //return EName.isSubNameOf(aName, new EName((Name)mObject.base.getElement()));

    }

    public boolean excludes(GeneralName aGeneralName)
    {
        if (mObject.base.getChoiceID() != aGeneralName.getChoiceID())
            return true;

        switch (mObject.base.getChoiceID()) {
            case GeneralName._DIRECTORYNAME: {
                return permits((Name)aGeneralName.getElement());
            }
            case GeneralName._RFC822NAME: {
                return EName.isSubNameOf(((Asn1IA5String) aGeneralName.getElement()).value, ((Asn1IA5String) mObject.base.getElement()).value, "@");
            }
            case GeneralName._DNSNAME: {
                return EName.isSubNameOf(((Asn1IA5String) aGeneralName.getElement()).value, ((Asn1IA5String) mObject.base.getElement()).value);
            }
            case GeneralName._UNIFORMRESOURCEIDENTIFIER: {
                return EName.isSubURINameOf(((Asn1IA5String) aGeneralName.getElement()).value, ((Asn1IA5String) mObject.base.getElement()).value);
            }
            default:
                return false;
        }
    }

    public static List<EGeneralSubtree> intersect(List<EGeneralSubtree> aListA, List<EGeneralSubtree> aListB)
    {
        List<EGeneralSubtree> intersection = new ArrayList<EGeneralSubtree>();
        for (EGeneralSubtree gstA : aListA) {
            boolean found = false;
            for (EGeneralSubtree gstB : aListB) {
                if (gstA.getBase().getType() != gstB.getBase().getType())
                    continue;

                found = true;
                switch (gstA.getBase().getType()) {
                    case GeneralName._DIRECTORYNAME: {
                        if (gstA.getBase().getDirectoryName().isSubNameOf(gstB.getBase().getDirectoryName()))
                            intersection.add(gstA);
                        else if (gstB.getBase().getDirectoryName().isSubNameOf(gstA.getBase().getDirectoryName()))
                            intersection.add(gstB);
                        else {
                            GeneralSubtree emptyGSTName = new GeneralSubtree();
                            // todo
                            emptyGSTName.base = new GeneralName();
                            emptyGSTName.base.set_directoryName(new Name());
                            intersection.add(new EGeneralSubtree(emptyGSTName));
                        }
                        break;
                    }
                    case GeneralName._RFC822NAME: {
                        if (EName.isSubNameOf(gstA.getBase().getRfc822Name(), gstB.getBase().getRfc822Name(), "@"))
                            intersection.add(gstA);
                        else if (EName.isSubNameOf(gstB.getBase().getRfc822Name(), gstA.getBase().getRfc822Name(), "@"))
                            intersection.add(gstB);
                        else {
                            GeneralSubtree emptyGSTName = new GeneralSubtree();
                            emptyGSTName.base.set_rfc822Name(new Asn1IA5String(""));
                            intersection.add(new EGeneralSubtree(emptyGSTName));
                        }
                        break;
                    }
                    case GeneralName._DNSNAME: {
                        if (EName.isSubNameOf(gstA.getBase().getDNSName(), gstB.getBase().getDNSName()))
                            intersection.add(gstA);
                        else if (EName.isSubNameOf(gstB.getBase().getDNSName(), gstA.getBase().getDNSName()))
                            intersection.add(gstB);
                        else {
                            GeneralSubtree emptyGSTDNSName = new GeneralSubtree();
                            emptyGSTDNSName.base.set_dNSName(new Asn1IA5String(""));
                            intersection.add(new EGeneralSubtree(emptyGSTDNSName));
                        }
                        break;
                    }
                    case GeneralName._UNIFORMRESOURCEIDENTIFIER: {
                        if (EName.isSubURINameOf(gstA.getBase().getUniformResourceIdentifier(), gstB.getBase().getUniformResourceIdentifier()))
                            intersection.add(gstA);
                        else if (EName.isSubURINameOf(gstB.getBase().getUniformResourceIdentifier(), gstA.getBase().getUniformResourceIdentifier()))
                            intersection.add(gstB);
                        else {
                            GeneralSubtree emptyGSTURI = new GeneralSubtree();
                            emptyGSTURI.base.set_uniformResourceIdentifier(new Asn1IA5String(""));
                            intersection.add(new EGeneralSubtree(emptyGSTURI));
                        }
                        break;
                    }
                    default:
                        continue;
                }
            }
            if (!found)
                intersection.add(gstA);
        }

        for (EGeneralSubtree gstB : aListB) {
            boolean found = false;
            for (EGeneralSubtree gstA : aListA) {
                if (gstA.getBase().getType() != gstB.getBase().getType())
                    continue;
                found = true;
            }
            if (!found)
                intersection.add(gstB);
        }

        return intersection;
    }

    public static List<EGeneralSubtree> unite(List<EGeneralSubtree> aListA, List<EGeneralSubtree> aListB)
    {
        List<EGeneralSubtree> united = new ArrayList<EGeneralSubtree>();
        for (EGeneralSubtree gstA : aListA) {
            boolean found = false;
            for (EGeneralSubtree gstB : aListB) {
                if (gstA.getBase().getType() != gstB.getBase().getType())
                    continue;

                found = true;
                switch (gstA.getBase().getType()) {
                    case GeneralName._DIRECTORYNAME: {
                        if (gstA.getBase().getDirectoryName().isSubNameOf(gstB.getBase().getDirectoryName()))
                            united.add(gstB);
                        else if (gstB.getBase().getDirectoryName().isSubNameOf(gstA.getBase().getDirectoryName()))
                            united.add(gstA);
                        else {
                            united.add(gstB);
                            united.add(gstA);
                        }
                        break;
                    }
                    case GeneralName._RFC822NAME: {
                        if (EName.isSubNameOf(gstA.getBase().getRfc822Name(), gstB.getBase().getRfc822Name(), "@"))
                            united.add(gstA);
                        else if (EName.isSubNameOf(gstB.getBase().getRfc822Name(), gstA.getBase().getRfc822Name(), "@"))
                            united.add(gstB);
                        else {
                            united.add(gstB);
                            united.add(gstA);
                        }
                        break;
                    }
                    case GeneralName._DNSNAME: {
                        if (EName.isSubNameOf(gstA.getBase().getDNSName(), gstB.getBase().getDNSName()))
                            united.add(gstB);
                        else if (EName.isSubNameOf(gstB.getBase().getDNSName(), gstA.getBase().getDNSName()))
                            united.add(gstA);
                        else {
                            united.add(gstB);
                            united.add(gstA);
                        }
                        break;
                    }
                    case GeneralName._UNIFORMRESOURCEIDENTIFIER: {
                        if (EName.isSubURINameOf(gstA.getBase().getUniformResourceIdentifier(), gstB.getBase().getUniformResourceIdentifier()))
                            united.add(gstB);
                        else if (EName.isSubURINameOf(gstB.getBase().getUniformResourceIdentifier(), gstA.getBase().getUniformResourceIdentifier()))
                            united.add(gstA);
                        else {
                            united.add(gstB);
                            united.add(gstA);
                        }
                        break;
                    }
                    default:
                        continue;
                }
            }
            if (!found)
                united.add(gstA);
        }

        for (EGeneralSubtree gstB : aListB) {
            boolean found = false;
            for (EGeneralSubtree gstA : aListA) {
                if (gstA.getBase().getType() != gstB.getBase().getType())
                    continue;
                found = true;
            }
            if (!found)
                united.add(gstB);
        }

        return united;
    }

}
