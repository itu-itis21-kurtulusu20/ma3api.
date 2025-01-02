using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EGeneralSubtree : BaseASNWrapper<GeneralSubtree>
    {
        public EGeneralSubtree(GeneralSubtree aObject)
            : base(aObject)
        {
        }
        public EGeneralSubtree(EGeneralName aBase, long aMinimum, long aMaximum)
            : base(new GeneralSubtree(aBase.getObject(), aMinimum, aMaximum))
        {
        }

        public EGeneralSubtree(EGeneralName aBase)
            : base(new GeneralSubtree(aBase.getObject()))
        {
        }

        public EGeneralName getBase()
        {
            return new EGeneralName(mObject.base_);
        }
        public long getMinimum()
        {
            return mObject.minimum.mValue;
        }

        public long? getMaximum()
        {
            if (mObject.maximum == null)
                return null;
            return mObject.maximum.mValue;
        }

        public bool permits(Name aName)
        {
            return permits(new EName(aName));
        }

        public bool permits(EName aName)
        {
            if (mObject.base_.ChoiceID != GeneralName._DIRECTORYNAME)
                return true;

            return aName.isSubNameOf((Name)mObject.base_.GetElement());
        }

        public bool permits(GeneralName aGeneralName)
        {
            if (mObject.base_.ChoiceID != aGeneralName.ChoiceID)
                return true;

            switch (mObject.base_.ChoiceID)
            {
                case GeneralName._DIRECTORYNAME:
                    {
                        return permits((Name)aGeneralName.GetElement());
                    }
                case GeneralName._RFC822NAME:
                    {
                        return EName.isSubNameOf(((Asn1IA5String)aGeneralName.GetElement()).mValue, ((Asn1IA5String)mObject.base_.GetElement()).mValue, "@");
                    }
                case GeneralName._DNSNAME:
                    {
                        return EName.isSubNameOf(((Asn1IA5String)aGeneralName.GetElement()).mValue, ((Asn1IA5String)mObject.base_.GetElement()).mValue);
                    }
                case GeneralName._UNIFORMRESOURCEIDENTIFIER:
                    {
                        return EName.isSubURINameOf(((Asn1IA5String)aGeneralName.GetElement()).mValue, ((Asn1IA5String)mObject.base_.GetElement()).mValue);
                    }
                default:
                    return true;
            }
        }

        public bool excludes(EName aName)
        {
            if (mObject.base_.ChoiceID != GeneralName._DIRECTORYNAME)
                return false;

            return aName.isSubNameOf((Name)mObject.base_.GetElement());
            //return EName.isSubNameOf(aName, new EName((Name)mObject.base_.GetElement()));

        }

        public bool excludes(GeneralName aGeneralName)
        {
            if (mObject.base_.ChoiceID != aGeneralName.ChoiceID)
                return true;

            switch (mObject.base_.ChoiceID)
            {
                case GeneralName._DIRECTORYNAME:
                    {
                        return permits((Name)aGeneralName.GetElement());
                    }
                case GeneralName._RFC822NAME:
                    {
                        return EName.isSubNameOf(((Asn1IA5String)aGeneralName.GetElement()).mValue, ((Asn1IA5String)mObject.base_.GetElement()).mValue, "@");
                    }
                case GeneralName._DNSNAME:
                    {
                        return EName.isSubNameOf(((Asn1IA5String)aGeneralName.GetElement()).mValue, ((Asn1IA5String)mObject.base_.GetElement()).mValue);
                    }
                case GeneralName._UNIFORMRESOURCEIDENTIFIER:
                    {
                        return EName.isSubURINameOf(((Asn1IA5String)aGeneralName.GetElement()).mValue, ((Asn1IA5String)mObject.base_.GetElement()).mValue);
                    }
                default:
                    return false;
            }
        }

        public static List<EGeneralSubtree> intersect(List<EGeneralSubtree> aListA, List<EGeneralSubtree> aListB)
        {
            List<EGeneralSubtree> intersection = new List<EGeneralSubtree>();
            foreach (EGeneralSubtree gstA in aListA)
            {
                bool found = false;
                foreach (EGeneralSubtree gstB in aListB)
                {
                    if (gstA.getBase().getType() != gstB.getBase().getType())
                        continue;

                    found = true;
                    switch (gstA.getBase().getType())
                    {
                        case GeneralName._DIRECTORYNAME:
                            {
                                //if (EName.isSubNameOf(gstA.getBase().getDirectoryName(), gstB.getBase().getDirectoryName()))
                                if (gstA.getBase().getDirectoryName().isSubNameOf(gstB.getBase().getDirectoryName()))
                                    intersection.Add(gstA);
                                else if (EName.isSubNameOf(gstB.getBase().getDirectoryName(), gstA.getBase().getDirectoryName()))
                                    intersection.Add(gstB);
                                else
                                {
                                    GeneralSubtree emptyGSTName = new GeneralSubtree();
                                    emptyGSTName.base_ = new GeneralName();
                                    emptyGSTName.base_.Set_directoryName(new Name());
                                    intersection.Add(new EGeneralSubtree(emptyGSTName));
                                }
                                break;
                            }
                        case GeneralName._RFC822NAME:
                            {
                                if (EName.isSubNameOf(gstA.getBase().getRfc822Name(), gstB.getBase().getRfc822Name(), "@"))
                                    intersection.Add(gstA);
                                else if (EName.isSubNameOf(gstB.getBase().getRfc822Name(), gstA.getBase().getRfc822Name(), "@"))
                                    intersection.Add(gstB);
                                else
                                {
                                    GeneralSubtree emptyGSTName = new GeneralSubtree();
                                    emptyGSTName.base_.Set_rfc822Name(new Asn1IA5String(""));
                                    intersection.Add(new EGeneralSubtree(emptyGSTName));
                                }
                                break;
                            }
                        case GeneralName._DNSNAME:
                            {
                                if (EName.isSubNameOf(gstA.getBase().getDNSName(), gstB.getBase().getDNSName()))
                                    intersection.Add(gstA);
                                else if (EName.isSubNameOf(gstB.getBase().getDNSName(), gstA.getBase().getDNSName()))
                                    intersection.Add(gstB);
                                else
                                {
                                    GeneralSubtree emptyGSTDNSName = new GeneralSubtree();
                                    emptyGSTDNSName.base_.Set_dNSName(new Asn1IA5String(""));
                                    intersection.Add(new EGeneralSubtree(emptyGSTDNSName));
                                }
                                break;
                            }
                        case GeneralName._UNIFORMRESOURCEIDENTIFIER:
                            {
                                if (EName.isSubURINameOf(gstA.getBase().getUniformResourceIdentifier(), gstB.getBase().getUniformResourceIdentifier()))
                                    intersection.Add(gstA);
                                else if (EName.isSubURINameOf(gstB.getBase().getUniformResourceIdentifier(), gstA.getBase().getUniformResourceIdentifier()))
                                    intersection.Add(gstB);
                                else
                                {
                                    GeneralSubtree emptyGSTURI = new GeneralSubtree();
                                    emptyGSTURI.base_.Set_uniformResourceIdentifier(new Asn1IA5String(""));
                                    intersection.Add(new EGeneralSubtree(emptyGSTURI));
                                }
                                break;
                            }
                        default:
                            continue;
                    }
                }
                if (!found)
                    intersection.Add(gstA);
            }

            foreach (EGeneralSubtree gstB in aListB)
            {
                bool found = false;
                foreach (EGeneralSubtree gstA in aListA)
                {
                    if (gstA.getBase().getType() != gstB.getBase().getType())
                        continue;
                    found = true;
                }
                if (!found)
                    intersection.Add(gstB);
            }

            return intersection;
        }

        public static List<EGeneralSubtree> unite(List<EGeneralSubtree> iListA, List<EGeneralSubtree> iListB)
        {
            List<EGeneralSubtree> united = new List<EGeneralSubtree>();
            foreach (EGeneralSubtree gstA in iListA)
            {
                bool found = false;
                foreach (EGeneralSubtree gstB in iListB)
                {
                    if (gstA.getBase().getType() != gstB.getBase().getType())
                        continue;

                    found = true;
                    switch (gstA.getBase().getType())
                    {
                        case GeneralName._DIRECTORYNAME:
                            {
                                if (EName.isSubNameOf(gstA.getBase().getDirectoryName(), gstB.getBase().getDirectoryName()))
                                    united.Add(gstB);
                                else if (EName.isSubNameOf(gstB.getBase().getDirectoryName(), gstA.getBase().getDirectoryName()))
                                    united.Add(gstA);
                                else
                                {
                                    united.Add(gstB);
                                    united.Add(gstA);
                                }
                                break;
                            }
                        case GeneralName._RFC822NAME:
                            {
                                if (EName.isSubNameOf(gstA.getBase().getRfc822Name(), gstB.getBase().getRfc822Name(), "@"))
                                    united.Add(gstA);
                                else if (EName.isSubNameOf(gstB.getBase().getRfc822Name(), gstA.getBase().getRfc822Name(), "@"))
                                    united.Add(gstB);
                                else
                                {
                                    united.Add(gstB);
                                    united.Add(gstA);
                                }
                                break;
                            }
                        case GeneralName._DNSNAME:
                            {
                                if (EName.isSubNameOf(gstA.getBase().getDNSName(), gstB.getBase().getDNSName()))
                                    united.Add(gstB);
                                else if (EName.isSubNameOf(gstB.getBase().getDNSName(), gstA.getBase().getDNSName()))
                                    united.Add(gstA);
                                else
                                {
                                    united.Add(gstB);
                                    united.Add(gstA);
                                }
                                break;
                            }
                        case GeneralName._UNIFORMRESOURCEIDENTIFIER:
                            {
                                if (EName.isSubURINameOf(gstA.getBase().getUniformResourceIdentifier(), gstB.getBase().getUniformResourceIdentifier()))
                                    united.Add(gstB);
                                else if (EName.isSubURINameOf(gstB.getBase().getUniformResourceIdentifier(), gstA.getBase().getUniformResourceIdentifier()))
                                    united.Add(gstA);
                                else
                                {
                                    united.Add(gstB);
                                    united.Add(gstA);
                                }
                                break;
                            }
                        default:
                            continue;
                    }
                }
                if (!found)
                    united.Add(gstA);
            }

            foreach (EGeneralSubtree gstB in iListB)
            {
                bool found = false;
                foreach (EGeneralSubtree gstA in iListA)
                {
                    if (gstA.getBase().getType() != gstB.getBase().getType())
                        continue;
                    found = true;
                }
                if (!found)
                    united.Add(gstB);
            }

            return united;
        }

    }
}
