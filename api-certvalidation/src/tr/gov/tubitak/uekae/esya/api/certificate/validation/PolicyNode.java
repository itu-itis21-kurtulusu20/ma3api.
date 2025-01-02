package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.Constants;
import tr.gov.tubitak.uekae.esya.asn.x509.PolicyQualifierInfo;

import java.util.ArrayList;
import java.util.List;

public class PolicyNode
{

    Asn1ObjectIdentifier mValidPolicy;
    List<PolicyQualifierInfo> mQualifierSet = new ArrayList<PolicyQualifierInfo>();
    List<Asn1ObjectIdentifier> mExpectedPolicySet = new ArrayList<Asn1ObjectIdentifier>();
    List<PolicyNode> mChildren = new ArrayList<PolicyNode>();
    PolicyNode mParent;


    public PolicyNode(PolicyNode iParent)
    {
        mParent = iParent;
    }


    public Asn1ObjectIdentifier getValidPolicy()
    {
        return mValidPolicy;
    }

    public List<PolicyQualifierInfo> getQualifierSet()
    {
        return mQualifierSet;
    }

    public List<Asn1ObjectIdentifier> getExpectedPolicySet()
    {
        return mExpectedPolicySet;
    }

    public List<PolicyNode> getChildren()
    {
        return mChildren;
    }


    public void setValidPolicy(Asn1ObjectIdentifier aValidPolicy)
    {
        mValidPolicy = aValidPolicy;
    }

    public void setQualifierSet(List<PolicyQualifierInfo> aQualifierSet)
    {
        mQualifierSet = aQualifierSet;
    }

    public void appendQualifier(PolicyQualifierInfo aQualifier)
    {
        mQualifierSet.add(aQualifier);
    }

    public void setExpectedPolicySet(List<Asn1ObjectIdentifier> aEPS)
    {
        mExpectedPolicySet = aEPS;
    }

    public void appendExpectedPolicy(Asn1ObjectIdentifier aExpectedPolicy)
    {
        mExpectedPolicySet.add(aExpectedPolicy);
    }

    public void setChildren(List<PolicyNode> aChildren)
    {
        mChildren = aChildren;
    }

    public void appendChild(PolicyNode aChild)
    {
        mChildren.add(aChild);
        aChild.setParent(this);
    }

    public PolicyNode getParent()
    {
        return mParent;
    }

    public void setParent(PolicyNode aParent)
    {
        mParent = aParent;
    }


    public List<PolicyNode> getChildrenByDepth(int aDepth)
    {
        List<PolicyNode> nodeList = new ArrayList<PolicyNode>();
        if (aDepth == 0) {
            nodeList.add(this);
            return nodeList;
        }
        else if (aDepth > 0) {
            for (PolicyNode pPN : mChildren) {
                if (pPN != null)
                    nodeList.addAll(pPN.getChildrenByDepth(aDepth - 1));
            }
        }
        return nodeList;
    }


    public boolean hasChild(Asn1ObjectIdentifier aValidPolicy)
    {
        for (PolicyNode policyNode : mChildren) {
            if ((policyNode != null) && (policyNode.getValidPolicy().equals(aValidPolicy)))
                return true;
        }
        return false;
    }

    public static void deleteNode(PolicyNode aPolicyNode)
    {
        if (aPolicyNode == null) return;

        if (aPolicyNode.getParent() != null)
            aPolicyNode.getParent().removeChild(aPolicyNode);

        else aPolicyNode = null;
    }

    public boolean deleteChildlessNodes(int aDepth)
    {
        boolean rootDeleted = false;
        for (int i = aDepth; i >= 0; i--) {
            List<PolicyNode> nodeList = getChildrenByDepth(i);

            for (PolicyNode pPN : nodeList) {
                if (pPN.getChildren().isEmpty()) {
                    if (pPN == this)
                        rootDeleted = true;
                    deleteNode(pPN);
                }
            }
        }
        return rootDeleted;
    }

    public void removeChild(PolicyNode aChild)
    {
        for (int i = 0; i < mChildren.size(); i++) {
            if (mChildren.get(i).equals(aChild)) {
                mChildren.remove(i);
            }
        }
        aChild = null;
    }

    public void removeChildren()
    {
        mChildren.clear();
        //qDeleteAll(mChildren);
    }


    public List<PolicyNode> generateValidPolicyNodeSet()
    {
        List<PolicyNode> nodeList = new ArrayList<PolicyNode>();
        if (mValidPolicy.equals(Constants.IMP_ANY_POLICY))
            nodeList.addAll(mChildren);

        for (PolicyNode pn : mChildren) {
            if (pn != null)
                nodeList.addAll(pn.generateValidPolicyNodeSet());
        }
        return nodeList;
    }

    public List<Asn1ObjectIdentifier> generateUserConstrainedPolicySet()
    {
        List<Asn1ObjectIdentifier> nodeList = new ArrayList<Asn1ObjectIdentifier>();

        if (hasLeaf(Constants.IMP_ANY_POLICY))
            nodeList.add(Constants.IMP_ANY_POLICY);
        else {
            for (PolicyNode pn : mChildren) {
                if (mValidPolicy.equals(Constants.IMP_ANY_POLICY) && !pn.getValidPolicy().equals(Constants.IMP_ANY_POLICY))
                    nodeList.add(pn.getValidPolicy());

                nodeList.addAll(pn.generateUserConstrainedPolicySet());
            }
        }
        return nodeList;
    }

    public boolean isLeaf()
    {
        return mChildren.isEmpty();
    }

    public List<PolicyQualifierInfo> generateValidQualifierSet()
    {
        if (isLeaf())
            return mQualifierSet;
        else {
            List<PolicyQualifierInfo> list = new ArrayList<PolicyQualifierInfo>();
            for (PolicyNode pn : mChildren) {
                list.addAll(pn.generateValidQualifierSet());
            }
            return list;
        }
    }

    public boolean hasLeaf(Asn1ObjectIdentifier aPolicyId)
    {
        if (mChildren.isEmpty())
            return (mValidPolicy.equals(aPolicyId));

        boolean bHasLeaf = false;
        for (PolicyNode pn : mChildren) {
            bHasLeaf = (bHasLeaf || pn.hasLeaf(aPolicyId));
        }
        return bHasLeaf;
    }


    public static boolean isExists(Asn1ObjectIdentifier aPolicyId, List<PolicyNode> aNodeList)
    {
        for (PolicyNode pn : aNodeList) {
            if ((pn != null) && pn.getValidPolicy().equals(aPolicyId))
                return true;
        }
        return false;
    }

}
