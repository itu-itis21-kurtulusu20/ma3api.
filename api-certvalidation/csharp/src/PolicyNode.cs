using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    public class PolicyNode
    {        
        Asn1ObjectIdentifier mValidPolicy;
        List<PolicyQualifierInfo> mQualifierSet = new List<PolicyQualifierInfo>();
        List<Asn1ObjectIdentifier> mExpectedPolicySet = new List<Asn1ObjectIdentifier>();
        List<PolicyNode> mChildren = new List<PolicyNode>();
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
            mQualifierSet.Add(aQualifier);
        }

        public void setExpectedPolicySet(List<Asn1ObjectIdentifier> aEPS)
        {
            mExpectedPolicySet = aEPS;
        }

        public void appendExpectedPolicy(Asn1ObjectIdentifier aExpectedPolicy)
        {
            mExpectedPolicySet.Add(aExpectedPolicy);
        }

        public void setChildren(List<PolicyNode> aChildren)
        {
            mChildren = aChildren;
        }

        public void appendChild(PolicyNode aChild)
        {
            mChildren.Add(aChild);
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
            List<PolicyNode> nodeList = new List<PolicyNode>();
            if (aDepth == 0)
            {
                nodeList.Add(this);
                return nodeList;
            }
            else if (aDepth > 0)
            {
                foreach (PolicyNode pPN in mChildren)
                {
                    if (pPN != null)
                        nodeList.AddRange(pPN.getChildrenByDepth(aDepth - 1));
                }
            }
            return nodeList;
        }


        public bool hasChild(Asn1ObjectIdentifier aValidPolicy)
        {
            foreach (PolicyNode policyNode in mChildren)
            {
                if ((policyNode != null) && (policyNode.getValidPolicy().Equals(aValidPolicy)))
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

        public bool deleteChildlessNodes(int aDepth)
        {
            bool rootDeleted = false;
            for (int i = aDepth; i >= 0; i--)
            {
                List<PolicyNode> nodeList = getChildrenByDepth(i);

                foreach (PolicyNode pPN in nodeList)
                {
                    if (pPN.getChildren().Count == 0)
                    {
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
            for (int i = 0; i < mChildren.Count; i++)
            {
                if (mChildren[i].Equals(aChild))
                {
                    mChildren.RemoveAt(i);
                }
            }
            aChild = null;
        }

        public void removeChildren()
        {
            mChildren.Clear();
            //qDeleteAll(mChildren);
        }


        public List<PolicyNode> generateValidPolicyNodeSet()
        {
            List<PolicyNode> nodeList = new List<PolicyNode>();
            if (mValidPolicy.Equals(Constants.IMP_ANY_POLICY))
                nodeList.AddRange(mChildren);

            foreach (PolicyNode pn in mChildren)
            {
                if (pn != null)
                    nodeList.AddRange(pn.generateValidPolicyNodeSet());
            }
            return nodeList;
        }

        public List<Asn1ObjectIdentifier> generateUserConstrainedPolicySet()
        {
            List<Asn1ObjectIdentifier> nodeList = new List<Asn1ObjectIdentifier>();

            if (hasLeaf(Constants.IMP_ANY_POLICY))
                nodeList.Add(Constants.IMP_ANY_POLICY);
            else
            {
                foreach (PolicyNode pn in mChildren)
                {
                    if (mValidPolicy.Equals(Constants.IMP_ANY_POLICY) && !pn.getValidPolicy().Equals(Constants.IMP_ANY_POLICY))
                        nodeList.Add(pn.getValidPolicy());

                    nodeList.AddRange(pn.generateUserConstrainedPolicySet());
                }
            }
            return nodeList;
        }

        public bool isLeaf()
        {
            return mChildren.Count == 0;
        }

        public List<PolicyQualifierInfo> generateValidQualifierSet()
        {
            if (isLeaf())
                return mQualifierSet;
            else
            {
                List<PolicyQualifierInfo> list = new List<PolicyQualifierInfo>();
                foreach (PolicyNode pn in mChildren)
                {
                    list.AddRange(pn.generateValidQualifierSet());
                }
                return list;
            }
        }

        public bool hasLeaf(Asn1ObjectIdentifier aPolicyId)
        {
            if (mChildren.Count == 0)
                return (mValidPolicy.Equals(aPolicyId));

            bool bHasLeaf = false;
            foreach (PolicyNode pn in mChildren)
            {
                bHasLeaf = (bHasLeaf || pn.hasLeaf(aPolicyId));
            }
            return bHasLeaf;
        }


        public static bool isExists(Asn1ObjectIdentifier aPolicyId, List<PolicyNode> aNodeList)
        {
            foreach (PolicyNode pn in aNodeList)
            {
                if ((pn != null) && pn.getValidPolicy().Equals(aPolicyId))
                    return true;
            }
            return false;
        }

    }
}
