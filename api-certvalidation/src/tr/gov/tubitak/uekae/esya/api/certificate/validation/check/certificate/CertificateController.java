package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;

import com.objsys.asn1j.runtime.Asn1IA5String;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.Constants;
import tr.gov.tubitak.uekae.esya.api.asn.x509.*;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.*;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;
import tr.gov.tubitak.uekae.esya.asn.x509.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Performs Certificate validation.
 *
 * <ol>
 * <li>check is certificate in trusted certificates (if so return)
 * <li>self checks
 * <li>find issuer and check
 * <li>revocation check
 * </ol>
 *
 * @author IH
 * @see CertificateStatusInfo
 */
public class CertificateController
{

    private static Logger logger = LoggerFactory.getLogger(CertificateController.class);

    // Debug Variables
    private String mWorkingSubjectStr;
    private String mWorkingIssuerStr;
    private String mTrustCertStr;

    // Inputs
    private ECertificate mTrustCert;
    private ECertificate mTargetCert;

    private ECertificatePolicies mUserInitialPolicySet = new ECertificatePolicies(new CertificatePolicies());

    private boolean mInitialPolicyMappingInhibit;
    private boolean mInitialExplicitPolicy;
    private boolean mInitialAnyPolicyInhibit;

    //List<GeneralSubtree> mInitialPST;
    //List<GeneralSubtree> mInitialEST;

    private ValidationSystem mValidationSystem;
    private CertificatePath mCertificatePath;

    // State Variables
    private int mN;
    private int mI;

    private PolicyNode mValidPolicyTree;
    private List<EGeneralSubtree> mPermittedSubtree = new ArrayList<EGeneralSubtree>();// Permitted Subtree
    private List<EGeneralSubtree> mExcludedSubtree = new ArrayList<EGeneralSubtree>();// Excluded Subtree

    private long mExplicitPolicy;
    private long mInhibitAnyPolicy;
    private long mPolicyMapping;

    private ECertificate mWorkingIssuer;
    private CertificatePathNode mWorkingNode;

    private long mMaxPathLength;

    private List<Asn1ObjectIdentifier> mAuthoritiesConstrainedPolicySet = new ArrayList<Asn1ObjectIdentifier>(0);
    private List<Asn1ObjectIdentifier> mUserConstrainedPolicySet = new ArrayList<Asn1ObjectIdentifier>(0);
    private List<PolicyQualifierInfo> mValidQualifierSet = new ArrayList<PolicyQualifierInfo>(0);

    private boolean mDoNotUsePastRevocationInfo;

    int _findInList(Object aItem, List aListe)
    {
        return aListe!=null? aListe.indexOf(aItem) : -1;
    }

    void _initialize(ValidationSystem aValidationSystem, CertificatePath aCertificatePath)
    {
        mValidPolicyTree = null;

        mValidationSystem = aValidationSystem;
        mCertificatePath = aCertificatePath;
        mTrustCert = mCertificatePath.getTail().getSubject();
        mWorkingNode = mCertificatePath.getTail().previous();

        mPermittedSubtree.clear();
        mExcludedSubtree.clear();

        // Zincir Uzunluğu
        mN = mCertificatePath.pathLength() - 1;
        mI = 1;

        // Initialize Valid-Policy-Tree (RFC-5280 Pg:76)
        mValidPolicyTree = new PolicyNode(null);
        mValidPolicyTree.setValidPolicy(Constants.IMP_ANY_POLICY);
        mValidPolicyTree.appendExpectedPolicy(Constants.IMP_ANY_POLICY);

        mInitialExplicitPolicy = aValidationSystem.isInitialExplicitPolicy();
        mInitialAnyPolicyInhibit = aValidationSystem.isInitialAnyPolicyInhibit();
        mInitialPolicyMappingInhibit = aValidationSystem.isInitialPolicyMappingInhibit();

        for (String st : aValidationSystem.getUserInitialPolicySet()) {
            mUserInitialPolicySet.addPolicyInformation(new PolicyInformation(OIDUtil.parse(st)));
        }

        // Initialize Explicit-Policy (RFC-5280 Pg:77)
        if (mInitialExplicitPolicy)
            mExplicitPolicy = 0;
        else mExplicitPolicy = mN + 1;

        // Initialize Inhibit-AnyPolicy (RFC-5280 Pg:78)
        if (mInitialAnyPolicyInhibit)
            mInhibitAnyPolicy = 0;
        else mInhibitAnyPolicy = mN + 1;

        // Initialize Policy-Mapping (RFC-5280 Pg:78)
        if (mInitialPolicyMappingInhibit)
            mPolicyMapping = 0;
        else mPolicyMapping = mN + 1;

        // Initialize Working Issuer (RFC-5280 Pg:78)
        mWorkingIssuer = mTrustCert;

        // Initialize Max-Path-Length (RFC-5280 Pg:79)
        mMaxPathLength = mN;


        //////////////////////////////////////////////////////////////////////////
        mWorkingSubjectStr = mWorkingNode.getSubject().getSubject().stringValue();
        mWorkingIssuerStr = mWorkingIssuer.getSubject().stringValue();
        mTrustCertStr = mTrustCert.getSubject().stringValue();
        //////////////////////////////////////////////////////////////////////////
    }


    PathValidationResult _nameControl(CertificateStatusInfo aCertStatusInfo)
    {
        ECertificate certificate = mWorkingNode.getSubject();

        // RFC 5280 6.1.3 If Self-issued and not tee final cert , skip this step
        if ((!certificate.equals(mTargetCert)) && certificate.isSelfIssued())
            return PathValidationResult.SUCCESS;

        EName subject = certificate.getSubject();

        String subjectEmail = subject.getEmailAttribute();
        GeneralName gnEmail = null;

        if ((subjectEmail!=null) && (subjectEmail.length()>0))  // Name yapısı içinde email attribute var. kontrol etmeliyiz
        {
            gnEmail = new GeneralName();
            gnEmail.set_rfc822Name(new Asn1IA5String(subjectEmail));

            boolean subjectEmailPermitted = mPermittedSubtree.isEmpty(), emailTypeFound = false;

            for (EGeneralSubtree pst : mPermittedSubtree)   // PermittedTree yi control edelim
            {
                if (pst.getBase().getType() == GeneralName._RFC822NAME) {
                    emailTypeFound = true;
                    if (pst.permits(gnEmail)) {
                        subjectEmailPermitted = true;
                        break;
                    }
                }
            }
            if (!subjectEmailPermitted && emailTypeFound) {
                logger.error("Sertifika Ozne içindeki Eposta adı izin verilen listede bulunamadı");
                return PathValidationResult.NAMECONSTRAINTS_CONTROL_FAILURE;
            }
        }


        ESubjectAltName san = certificate.getExtensions().getSubjectAltName();

        boolean subjectPermitted = mPermittedSubtree.isEmpty(), nameTypeFound = false;

        for (EGeneralSubtree pst : mPermittedSubtree)   // PermittedTree yi control edelim
        {
            if (pst.getBase().getType() == GeneralName._DIRECTORYNAME) {
                nameTypeFound = true;
                if (pst.permits(subject)) {
                    subjectPermitted = true;
                    break;
                }
            }
        }

        if (!subjectPermitted && nameTypeFound) {
            logger.error("Sertifika Ozne adı izin verilen listede bulunamadı");
            return PathValidationResult.NAMECONSTRAINTS_CONTROL_FAILURE;
        }

        if (san != null) {
            for (int i=0; i< san.getElementCount(); i++) {
                EGeneralName gn = san.getElement(i);
                boolean sanPermitted = mPermittedSubtree.isEmpty(), gnTypeFound = false;
                for (EGeneralSubtree pst : mPermittedSubtree)// PermittedTree yi control edelim
                {
                    if (pst.getBase().getType() == gn.getType()) {
                        gnTypeFound = true;
                        if (pst.permits(gn.getObject())) {
                            sanPermitted = true;
                            break;
                        }
                    }
                }
                if (!sanPermitted && gnTypeFound) {
                    logger.error("Sertifika Alternatif Ozne adı izin verilen listede bulunamadı");
                    return PathValidationResult.NAMECONSTRAINTS_CONTROL_FAILURE;
                }
            }
        }

        for (EGeneralSubtree est : mExcludedSubtree)// ExcludedTree yi control edelim
        {
            if (est.excludes(subject)) {
                logger.error("Sertifika Ozne adı yasaklanan listede bulundu");
                return PathValidationResult.NAMECONSTRAINTS_CONTROL_FAILURE;
            }
            if (gnEmail!=null) {
                if (est.excludes(gnEmail)) {
                    logger.error("Sertifika Ozne adı içindeki eposta yasaklanan listede bulundu");
                    return PathValidationResult.NAMECONSTRAINTS_CONTROL_FAILURE;
                }

            }
            if (san != null) {
                for (int i=0; i<san.getElementCount(); i++) {
                    if (est.excludes(san.getElement(i).getObject())) {
                        logger.error("Sertifika Alternatif Ozne adı yasaklanan listede bulundu");
                        return PathValidationResult.NAMECONSTRAINTS_CONTROL_FAILURE;
                    }
                }
            }
        }

        return PathValidationResult.SUCCESS;
    }

    PathValidationResult _processPolicy(CertificateStatusInfo aCertStatusInfo)
    {
        ECertificate certificate = mWorkingNode.getSubject();
        ECertificatePolicies cp = certificate.getExtensions().getCertificatePolicies();

        List<PolicyNode> nodes = new ArrayList<PolicyNode>();

        if (mValidPolicyTree != null)
            nodes = mValidPolicyTree.getChildrenByDepth(mI - 1);

        if ((cp != null) && (mValidPolicyTree != null)) {
            for (int i=0; i< cp.getPolicyInformationCount(); i++) {

                PolicyInformation policyInformation = cp.getPolicyInformation(i);
                Asn1ObjectIdentifier p_oid = policyInformation.policyIdentifier;
                List<PolicyQualifierInfo> policyQualifierInfos = null; 
                if (policyInformation.policyQualifiers!=null){
                    policyQualifierInfos = new ArrayList<PolicyQualifierInfo>(Arrays.asList(policyInformation.policyQualifiers.elements));
                } else {
                    policyQualifierInfos = new ArrayList<PolicyQualifierInfo>(0);
                }
                for (PolicyNode policyNode : nodes) {
                    if (!p_oid.equals(Constants.IMP_ANY_POLICY)) {
                        if ((policyNode.getValidPolicy().equals(Constants.IMP_ANY_POLICY)) || (policyNode.getExpectedPolicySet().contains(p_oid))) {
                            PolicyNode newPolicy = new PolicyNode(null);
                            newPolicy.setValidPolicy(p_oid);    // RFC5280 6.1.3.d.1.i-ii
                            newPolicy.setQualifierSet(policyQualifierInfos);
                            newPolicy.appendExpectedPolicy(p_oid);
                            policyNode.appendChild(newPolicy);
                        }
                    }
                    else {
                        if (mInhibitAnyPolicy > 0 || ((mI < mN) && certificate.isSelfIssued())) // RFC5280 6.1.3.d.2
                            for (Asn1ObjectIdentifier e_oid : policyNode.getExpectedPolicySet()) {
                                if (!policyNode.hasChild(e_oid)) {
                                    PolicyNode newPolicy = new PolicyNode(null);
                                    newPolicy.setValidPolicy(e_oid);    // RFC5280 6.1.3.d.2
                                    newPolicy.setQualifierSet(policyQualifierInfos);
                                    newPolicy.appendExpectedPolicy(e_oid);
                                    policyNode.appendChild(newPolicy);
                                }
                            }
                    }
                }

            }
            if (mValidPolicyTree.deleteChildlessNodes(mI - 1)) // RFC5280 6.1.3.d.3
                mValidPolicyTree = null;
        }

        // RFC5280 6.1.3.e
        // If the certificate policies extension is not present, set the
        // valid_policy_tree to null.
        if (cp == null) {
            mValidPolicyTree = null;
            return PathValidationResult.SUCCESS;
        }

        // RFC5280 6.1.3.f
        // Verify that either explicit_policy is greater than 0 or the
        // valid_policy_tree is not equal to null;
        if ((mExplicitPolicy <= 0) && (mValidPolicyTree == null)) {
            logger.error("Either explicit_policy should be greater than 0 or the valid_policy_tree should not equal to null.");
            return PathValidationResult.POLICYCONSTRAINTS_CONTROL_FAILURE;
        }

        return PathValidationResult.SUCCESS;
    }


    PathValidationResult _crlValidation(CertificateStatusInfo pSDB) throws ESYAException
    {
    	if(pSDB.getCertificate().equals(mTargetCert))
    		mValidationSystem.setDoNotUsePastRevocationInfo(mDoNotUsePastRevocationInfo);
    	else
    		mValidationSystem.setDoNotUsePastRevocationInfo(false);
    	
        CheckSystem ks = mValidationSystem.getCheckSystem();
        return ks.checkRevocation(mWorkingIssuer, pSDB);
    }


    PathValidationResult _processCert(CertificateStatusInfo aCertStatusInfo) throws ESYAException
    {
        CheckSystem ks = mValidationSystem.getCheckSystem();

        ECertificate certificate = aCertStatusInfo.getCertificate();

        if (logger.isDebugEnabled()){
            logger.debug("-- Process cert --\n"+certificate.getSubject().stringValue());
        }

        PathValidationResult pvr;

        /* RFC5280 6.1.3.a*/
        pvr = ks.checkCertificateSelf(aCertStatusInfo);
        if (pvr != PathValidationResult.SUCCESS)
            return pvr;

        pvr = ks.checkIssuer(mValidationSystem.getCheckSystem().getConstraintCheckParam(), mWorkingIssuer, certificate, aCertStatusInfo);
        if (pvr != PathValidationResult.SUCCESS)
            return pvr;

        pvr = _crlValidation(aCertStatusInfo);
        if (pvr != PathValidationResult.SUCCESS)    // CRL Validation
            return pvr;

        /* RFC5280 6.1.3.b-c*/
        pvr = _nameControl(aCertStatusInfo);
        if (pvr != PathValidationResult.SUCCESS) // Name  raints
            return pvr;

        /* RFC5280 6.1.3.d*/
        pvr = _processPolicy(aCertStatusInfo);
        if (pvr != PathValidationResult.SUCCESS)
            return pvr;


        return pvr;
    }

    PathValidationResult _processPolicyConstraints()
    {
        PolicyConstraints pc = mWorkingNode.getSubject().getExtensions().getPolicyConstraints();
        if (pc == null)
            return PathValidationResult.SUCCESS;// PolicyMappings Extension yok

        // RFC5280 6.1.4.i.1
        // If requireExplicitPolicy is present and is less than
        // explicit_policy, set explicit_policy to the value of
        // requireExplicitPolicy.
        if ((pc.requireExplicitPolicy != null) && (pc.requireExplicitPolicy.value < mExplicitPolicy))
            mExplicitPolicy = pc.requireExplicitPolicy.value;

        // RFC5280 6.1.4.i.2
        // If inhibitPolicyMapping is present and is less than
        // policy_mapping, set policy_mapping to the value of
        // inhibitPolicyMapping.
        if ((pc.inhibitPolicyMapping != null) && (pc.inhibitPolicyMapping.value < mPolicyMapping))
            mPolicyMapping = pc.inhibitPolicyMapping.value;

        return PathValidationResult.SUCCESS;
    }

    PathValidationResult _processInhibitAnyPolicy()
    {
        InhibitAnyPolicy iap = mWorkingNode.getSubject().getExtensions().getInhibitAnyPolicy();

        // RFC5280 6.1.4.j
        // If the inhibitAnyPolicy extension is included in the
        // certificate and is less than inhibit_anyPolicy, set
        // inhibit_anyPolicy to the value of inhibitAnyPolicy.
        if ((iap != null) && (iap.value < mInhibitAnyPolicy))
            mInhibitAnyPolicy = iap.value;

        return PathValidationResult.SUCCESS;
    }

    PathValidationResult _processPathLengthConstraint()
    {
        EBasicConstraints bc = mWorkingNode.getSubject().getExtensions().getBasicConstraints();

        // RFC5280 6.1.4.m
        // If pathLen raint is present in the certificate and is
        // less than max_path_length, set max_path_length to the value
        // of pathLen raint.
        if ((bc != null) && (bc.getPathLenConstraint() != null) && (bc.getPathLenConstraint() < mMaxPathLength))
            mMaxPathLength = bc.getPathLenConstraint();

        return PathValidationResult.SUCCESS;
    }


    PathValidationResult _processPolicyMapping()
    {
        EPolicyMappings pm = mWorkingNode.getSubject().getExtensions().getPolicyMappings();

        if (pm == null)
            return PathValidationResult.SUCCESS;    // PolicyMappings Extension yok

        // Certificate Policy varsa ve içinde anyPolicy tanımlıysa qualifierini alıyoruz
        ECertificatePolicies cp = mWorkingNode.getSubject().getExtensions().getCertificatePolicies();
        int cpAnyPolicyIndeks = cp.indexOf(Constants.IMP_ANY_POLICY);
        List<PolicyQualifierInfo> cpAnyPolicyQualifiers = new ArrayList<PolicyQualifierInfo>();
        if (cpAnyPolicyIndeks >= 0){
            PolicyInformation pi = cp.getPolicyInformation(cpAnyPolicyIndeks);
            if (pi.policyQualifiers!=null)
                cpAnyPolicyQualifiers = Arrays.asList(pi.policyQualifiers.elements);
        }

        List<PolicyNode> nodeList = mValidPolicyTree.getChildrenByDepth(mI); // Children of depth i

        for (int i=0; i<pm.getPolicyMappingElementCount(); i++) {

            EPolicyMappingElement pme = pm.getPolicyMappingElement(i);
            // RFC5280 6.1.4.a
            // If a policy mappings extension is present, verify that the
            // special value anyPolicy does not appear as an
            // issuerDomainPolicy or a subjectDomainPolicy.
            if (pme.getIssuerDomainPolicy().equals(Constants.IMP_ANY_POLICY) || pme.getSubjectDomainPolicy().equals(Constants.IMP_ANY_POLICY)) {
                logger.error("Any policy should not appear issuerDomainPolicy or a subjectDomainPolicy. ");
                return PathValidationResult.POLICYMAPPING_CONTROL_FAILURE;
            }
            if (mPolicyMapping > 0) {
                boolean found = false;
                for (PolicyNode pn : nodeList) {
                    // RFC5280 6.1.4.b.1  If the policy_mapping variable is greater than 0, for each
                    //                     node in the valid_policy_tree of depth i where ID-P is the
                    //                     valid_policy, set expected_policy_set to the set of
                    //                     subjectDomainPolicy values that are specified as
                    //                     equivalent to ID-P by the policy mappings extension.
                    if ((pn != null) && pn.getValidPolicy().equals(pme.getIssuerDomainPolicy())) {
                        pn.setExpectedPolicySet(pm.getSubjectEquivalents(pme.getIssuerDomainPolicy()));
                        found = true;
                    }
                }
                if (!found) {
                    for (PolicyNode pn : nodeList) {
                        // RFC5280 6.1.4.b.1  If no node of depth i in the valid_policy_tree has a
                        //                    valid_policy of ID-P but there is a node of depth i with a
                        //                     valid_policy of anyPolicy, then generate a child node of
                        //                     the node of depth i-1 that has a valid_policy of anyPolicy
                        //                     as follows:
                        if ((pn != null) && pn.getValidPolicy().equals(Constants.IMP_ANY_POLICY)) {
                            PolicyNode newNode = new PolicyNode(null);
                            newNode.setValidPolicy(pme.getIssuerDomainPolicy());
                            newNode.setExpectedPolicySet(pm.getSubjectEquivalents(pme.getIssuerDomainPolicy()));
                            newNode.setQualifierSet(cpAnyPolicyQualifiers);
                            pn.getParent().appendChild(newNode);
                        }
                    }
                }
            }
            else // 6.1.4.b.2 mPolicyMapping == 0
            {
                for (PolicyNode pn : nodeList) {
                    // RFC5280 6.1.4.b.2.i  If the policy_mapping variable is greater than 0, for each
                    //                     node in the valid_policy_tree of depth i where ID-P is the
                    //                     valid_policy, set expected_policy_set to the set of
                    //                     subjectDomainPolicy values that are specified as
                    //                     equivalent to ID-P by the policy mappings extension.
                    if ((pn != null) && pn.getValidPolicy().equals(pme.getIssuerDomainPolicy())) {
                        PolicyNode.deleteNode(pn);
                    }
                }
                // RFC5280 6.1.4.b.2. If there is a node in the valid_policy_tree of depth
                //                    i-1 or less without any child nodes, delete that
                //                     node.  Repeat this step until there are no nodes of
                //                     depth i-1 or less without children.
                if (mValidPolicyTree.deleteChildlessNodes(mI - 1))
                    mValidPolicyTree = null;
            }
        }
        return PathValidationResult.SUCCESS;
    }

    PathValidationResult _prepareNameConstraints()
    {
        ENameConstraints nc = mWorkingNode.getSubject().getExtensions().getNameConstraints();

        if (nc == null){
            return PathValidationResult.SUCCESS;
        }
        else {
            mPermittedSubtree = EGeneralSubtree.intersect(mPermittedSubtree, nc.getPermittedSubtrees());
            mExcludedSubtree = EGeneralSubtree.unite(mExcludedSubtree, nc.getExcludedSubtrees());
        }

        return PathValidationResult.SUCCESS;
    }

    PathValidationResult _prepareForNext()
    {
        PathValidationResult b;

        /* RFC5280 6.1.4.a-b*/
        if ((b = _processPolicyMapping()) != PathValidationResult.SUCCESS)
            return b;

        /* RFC5280 6.1.4.c-d-e*/
        mWorkingIssuer = mWorkingNode.getSubject();

        /* RFC5280 6.1.4.f*/
        _prepareNameConstraints();

        if (!mWorkingNode.getSubject().isSelfIssued()) {
            /* RFC5280 6.1.4.h.1*/
            if (mExplicitPolicy > 0) mExplicitPolicy--;

            /* RFC5280 6.1.4.h.2*/
            if (mPolicyMapping > 0) mPolicyMapping--;

            /* RFC5280 6.1.4.h.3*/
            if (mInhibitAnyPolicy > 0) mInhibitAnyPolicy--;
        }

        // RFC5280 6.1.4.i

        _processPolicyConstraints();

        // RFC5280 6.1.4.j

        _processInhibitAnyPolicy();

        // RFC5280 6.1.4.k
        // burası BasicconstraintsKontrolcu sm kontrolcüsümne kontrol ediliyor.

        // RFC5280 6.1.4.l If the certificate was not self-issued, verify that
        //                max_path_length is greater than zero and decrement
        //                max_path_length by 1.
        if (!mWorkingNode.getSubject().isSelfIssued()) {
            if (mMaxPathLength > 0)
                mMaxPathLength--;
            else {
                logger.error("Path Length violation error!");
                return PathValidationResult.PATHLENCONSTRAINTS_FAILURE; // Path Length violation!
            }
        }

        // RFC5280 6.1.4.m

        _processPathLengthConstraint();

        // RFC5280 6.1.4.n
        // burası SertifikaKeyUsageKontrolcu  smkontrolcüsünde kontrol ediliyor.

        // RFC5280 6.1.4.o
        // burası EklentiKontrolcu  smkontrolcüsünde kontrol ediliyor.


        // Incrementing i
        mI++;
        mWorkingNode = mWorkingNode.previous();// Bir sonraki Setifikaya geciyoruz
        return PathValidationResult.SUCCESS;
    }

    PathValidationResult _intersectPolicyTree()
    {
        //PolicyNode intersection = null;

        // RFC5280 6.1.5.g.i If the valid_policy_tree is null, the intersection is null.
        if (mValidPolicyTree == null) 
            mValidPolicyTree = null;

            // RFC5280 6.1.5.g.ii If the valid_policy_tree is not null and the user-
            //                    initial-policy-set is any-policy, the intersection is
            //                    the entire valid_policy_tree.
        else if (mUserInitialPolicySet.indexOf(Constants.IMP_ANY_POLICY) >= 0)
            mValidPolicyTree = mValidPolicyTree;

            // RFC5280 6.1.5.g.iii If the valid_policy_tree is not null and the user-
            //                    initial-policy-set is not any-policy, calculate the
            //                    intersection of the valid_policy_tree and the user-
            //                    initial-policy-set as follows:
        else {
            // RFC5280 6.1.5.g.iii.1 Determine the set of policy nodes whose parent nodes
            //                        have a valid_policy of anyPolicy.  This is the
            //                        valid_policy_node_set.
            List<PolicyNode> valid_policy_node_set = mValidPolicyTree.generateValidPolicyNodeSet();
            for (int k=0; k< valid_policy_node_set.size(); k++) {
                PolicyNode pn  = valid_policy_node_set.get(k);
                // RFC5280 6.1.5.g.iii.2  If the valid_policy of any node in the
                //                        valid_policy_node_set is not in the user-initial-
                //                        policy-set and is not anyPolicy, delete this node and
                //                        all its children.
                if (!pn.getValidPolicy().equals(Constants.IMP_ANY_POLICY) && mUserInitialPolicySet.indexOf(pn.getValidPolicy()) < 0) {
                    valid_policy_node_set.remove(pn);
                    PolicyNode.deleteNode(pn);
                }
            }
            List<PolicyNode> nodeList = mValidPolicyTree.getChildrenByDepth(mN);
            for (PolicyNode pn : nodeList) {
                // RFC5280 6.1.5.g.iii.3  If the valid_policy_tree includes a node of depth n
                //                        with the valid_policy anyPolicy and the user-initial-
                //                        policy-set is not any-policy, perform the following
                //                        steps:
                if (pn.getValidPolicy().equals(Constants.IMP_ANY_POLICY)) {
                    for (int j=0; j< mUserInitialPolicySet.getPolicyInformationCount(); j++) {
                        PolicyInformation pi = mUserInitialPolicySet.getPolicyInformation(j);

                        // RFC5280 6.1.5.g.iii.3.b  For each P-OID in the user-initial-policy-set that is
                        //                            not the valid_policy of a node in the
                        //                            valid_policy_node_set, create a child node whose
                        //                            parent is the node of depth n-1 with the valid_policy
                        //                            anyPolicy.  Set the values in the child node as
                        //                            follows: set the valid_policy to P-OID, set the
                        //                            qualifier_set to P-Q, and set the expected_policy_set
                        //                            to {P-OID}.
                        if (!PolicyNode.isExists(pi.policyIdentifier, valid_policy_node_set)) {
                            PolicyNode newNode = new PolicyNode(null);
                            newNode.setValidPolicy(pi.policyIdentifier);
                            newNode.setQualifierSet(pn.getQualifierSet());
                            newNode.appendExpectedPolicy(pi.policyIdentifier);
                            pn.getParent().appendChild(newNode);
                        }
                    }
                    // RFC5280 6.1.5.g.iii.3.c  Delete the node of depth n with the valid_policy
                    //                        anyPolicy
                    PolicyNode.deleteNode(pn);
                }
            }
            // RFC5280 6.1.5.g.iii.4 If there is a node in the valid_policy_tree of depth
            //                        n-1 or less without any child nodes, delete that node.
            //                        Repeat this step until there are no nodes of depth n-1
            //                        or less without children.
            if (mValidPolicyTree.deleteChildlessNodes(mN - 1))
                mValidPolicyTree = null;
        }

        return PathValidationResult.SUCCESS;
    }

    PathValidationResult _wrapUp()
    {
        // RFC5280 6.1.5.a If explicit_policy is not 0, decrement explicit_policy by 1
        if (mExplicitPolicy != 0)
            mExplicitPolicy--;

        //RFC5280 6.1.5.b If a policy  raints extension is included in the
        //                certificate and requireExplicitPolicy is present and has a
        //                value of 0, set the explicit_policy state variable to 0.
        //int pcFound = ExtensionGenerator.getPolicyConstraintsExtension(mWorkingNode.subjectAl().tbsCertificate.extensions, pc);
        PolicyConstraints pc = mWorkingNode.getSubject().getExtensions().getPolicyConstraints();

        if ((pc != null) && (pc.requireExplicitPolicy != null) && (pc.requireExplicitPolicy.value == 0))
            mExplicitPolicy = 0;


        //RFC5280 6.1.5.c-d-e
        mWorkingIssuer = mWorkingNode.getSubject();

        if (mValidPolicyTree != null) // This is for test controls
            mAuthoritiesConstrainedPolicySet = mValidPolicyTree.generateUserConstrainedPolicySet();

        //int size = mAuthoritiesConstrainedPolicySet.size();

        //RFC5280 6.1.5.g Calculate the intersection of the valid_policy_tree and the
        //                user-initial-policy-set
        _intersectPolicyTree();


        if (mValidPolicyTree != null) // This is for test controls
        {
            mUserConstrainedPolicySet = mValidPolicyTree.generateUserConstrainedPolicySet();
            mValidQualifierSet = mValidPolicyTree.generateValidQualifierSet();
        }
        //int size = mUserConstrainedPolicySet.size();

        // RFC5280 Page:88    If either (1) the value of explicit_policy variable is greater than
        //                    zero or (2) the valid_policy_tree is not null, then path processing
        //                    has succeeded.
        if ((mExplicitPolicy > 0) || (mValidPolicyTree != null)){
            return PathValidationResult.SUCCESS;
        }else {
            logger.error("Either (1) the value of explicit_policy variable is not greater than zero or (2) the valid_policy_tree is null");
            return PathValidationResult.POLICYCONSTRAINTS_CONTROL_FAILURE;
        }
    }

    boolean _notLast()
    {
        return (mI < mN);
    }

    CertificateStatusInfo _saveResultChain(List<CertificateStatusInfo> aResultChain, CertificateStatusInfo aStatusInfo)
    {
        int lastIndex = aResultChain.size() - 1;
        if (!aResultChain.get(lastIndex).getCertificate().equals(mTargetCert))
            return aStatusInfo;

        else {
            aStatusInfo = aResultChain.get(lastIndex);
            CertificateStatusInfo tmpSDB = aResultChain.get(lastIndex);
            for (int i = aResultChain.size() - 2; i >= 0; i--) {
                tmpSDB.setSigningCertficateInfo(aResultChain.get(i));
                tmpSDB = tmpSDB.getSigningCertficateInfo();
            }
            //tmpSDB.setSigningCertficateInfo(new CertificateStatusInfo(mTrustCert));
            return aStatusInfo;
        }

    }


    private ValidationResult _validatePath(ValidationSystem aValidationSystem, CertificatePath aPath, CertificateStatusInfo aCertStatusInfo) throws ESYAException
    {
        if (logger.isDebugEnabled()){
            logger.debug("-- Validate path --\n"+aPath.toString());
        }
        PathValidationResult pvr;

        int n = aPath.pathLength() - 1;

        if (n < 0) return new ValidationResult(PathValidationResult.INVALID_PATH, aCertStatusInfo); // Gecersiz Zincir
        if (n == 0)
            return new ValidationResult(PathValidationResult.SUCCESS, aCertStatusInfo); // Zincirde tek sertifika var ve guvenilir

        List<CertificateStatusInfo> resultChain = new ArrayList<CertificateStatusInfo>();

        _initialize(aValidationSystem, aPath);

       	CertificateStatusInfo trustCertStatusInfo = new CertificateStatusInfo(mTrustCert, aValidationSystem.getBaseValidationTime());

        pvr = aValidationSystem.getCheckSystem().guvenilirSertifikaKontrolleriYap(trustCertStatusInfo);
        trustCertStatusInfo.setCertificateStatus((pvr==PathValidationResult.SUCCESS) ?  CertificateStatus.VALID :CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE);
        resultChain.add(trustCertStatusInfo);
        aValidationSystem.getCertificateValidationCache().addCheckResult(trustCertStatusInfo.clone());

        while(pvr==PathValidationResult.SUCCESS)
        {
            CertificateStatusInfo certStatusInfo = new CertificateStatusInfo(mWorkingNode.getSubject(), aValidationSystem.getBaseValidationTime());
            certStatusInfo.setSigningCertficateInfo(new CertificateStatusInfo(mTrustCert, aValidationSystem.getBaseValidationTime()));
            resultChain.add(certStatusInfo);
            
            
            pvr = _processCert(certStatusInfo);
            if (pvr != PathValidationResult.SUCCESS)
                continue;

            certStatusInfo.setCertificateStatus(CertificateStatus.VALID);

            if (_notLast()) {
                aValidationSystem.addToCACache(certStatusInfo.getCertificate());

                pvr = _prepareForNext();
                if (pvr != PathValidationResult.SUCCESS)
                    continue;
            }
            else
                break;
        }

        if (pvr == PathValidationResult.SUCCESS)
            pvr = _wrapUp();
        
        aCertStatusInfo = _saveResultChain(resultChain, aCertStatusInfo);
        
        return new ValidationResult(pvr, aCertStatusInfo);
    }

    class ValidationResult {
        PathValidationResult pvr;
        CertificateStatusInfo csi;

        ValidationResult(PathValidationResult aPvr, CertificateStatusInfo aCsi)
        {
            pvr = aPvr;
            csi = aCsi;
        }
    }


    /**
     * Check certicate
     *
     * @param aValidationSystem validation system
     * @param aCertificate to be controlled
     */
    public CertificateStatusInfo check(ValidationSystem aValidationSystem, ECertificate aCertificate) throws ESYAException
    {
        CertificateStatusInfo certStatusInfo;
        mTargetCert = aCertificate;
        String subject = aCertificate.getSubject().stringValue();
        if (logger.isDebugEnabled()){
            logger.debug("------------------------------ Check certificate ---");
            logger.debug(aCertificate.toString());
        }

        // Daha önceden kontrol edilmiş ise hafızamdaki sonucu dönüyorum
        CertificateValidationCache certValidationCache = aValidationSystem.getCertificateValidationCache();
        certStatusInfo = certValidationCache.getCheckResult(aCertificate);
        if (certStatusInfo != null)
            return certStatusInfo.clone();

        // daha önce kontrol edilmemiş, yeni durum bilgisi oluşturalım
        // pSDB = new SertifikaDurumBilgisi(aSertifika);

        // dışarıdan verilmiş geçerli sertifikalar listesinde var mı kontrol edelim
        List<ECertificate> gecerliSertifikalar = aValidationSystem.getValidCertificateSet();
        if (_findInList(aCertificate, gecerliSertifikalar) >= 0) {
            if (logger.isDebugEnabled())
                logger.debug("Geçerli Sertifikalar içerisinde bulundu, geçerli dönülüyor");
            return _returnSertifikaDurumu(aValidationSystem, aCertificate, CertificateStatus.VALID);
        }

        // Döngü Koruma Listesinde var mı kontrol edelim. varsa kontrol yapmadan dönelim
        if (aValidationSystem.getCyclicCheckList().contains(aCertificate)) {
            if (logger.isDebugEnabled())
                logger.debug("Döngüsel kontrol tespit edildi. kontrol yapılmadan çıkılıyor subject: " + subject);
            return _returnSertifikaDurumu(aValidationSystem, aCertificate, CertificateStatus.NOT_CHECKED);
        }

        if (logger.isDebugEnabled())
            logger.debug(subject + " subjectli sertifika kontrolü yapılacak");

        if (aValidationSystem.getBaseValidationTime()!=null && aValidationSystem.getLastRevocationTime()==null){
            logger.warn("Last revocation time is not set, adjusting to certificate expiration time!");
            aValidationSystem.setLastRevocationTime(aCertificate.getNotAfter());
        }

        // Döngü Koruma Listemize ekleyelim ki recursive adımlarda bir daha kontrol etmeyelim
        aValidationSystem.addToCyclicCheckList(aCertificate);

        // bulma parametremizi alalım
        CheckSystem checkSystem = aValidationSystem.getCheckSystem();
        FindSystem findSystem = aValidationSystem.getFindSystem();

        // guvenilir sertifika listesini oluşturalım.
        findSystem.findTrustedCertificates();

        // Güvenilir Sertifika Listesi boş mu kontrol edelim. Boşsa kontrol yapmadan dönelim
        if (findSystem.getTrustedCertificates().isEmpty() && gecerliSertifikalar.isEmpty())
        {
            if (logger.isDebugEnabled())
                logger.debug("Güvenilir Sertifika Listesi boş. kontrol yapılmadan cikiliyor subject: "+subject);
            return _returnSertifikaDurumu(aValidationSystem, aCertificate, CertificateStatus.NO_TRUSTED_CERT_FOUND);
        }

        // guvenilir listede ise VALID diyelim
        if (findSystem.isTrustedCertificate(aCertificate)) {
            if (logger.isDebugEnabled())
                logger.debug("Sertifika Güvenilir listede");

            certStatusInfo = new CertificateStatusInfo(aCertificate, aValidationSystem.getBaseValidationTime());

            /* Guvenilir Sertifika kontrollerini yapalım */
            if (checkSystem.guvenilirSertifikaKontrolleriYap(certStatusInfo) == PathValidationResult.SUCCESS) {
                certStatusInfo.setCertificateStatus(CertificateStatus.VALID);
            }
            else {
                certStatusInfo.setCertificateStatus(CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE);
            }
            aValidationSystem.getCertificateValidationCache().addCheckResult(certStatusInfo.clone());
            return certStatusInfo;
        }

        //  Tek sertifika kontrolleri yapalım.
        certStatusInfo = new CertificateStatusInfo(aCertificate, aValidationSystem.getBaseValidationTime());
        PathValidationResult tskResult = checkSystem.checkCertificateSelf(certStatusInfo);
        if (tskResult != PathValidationResult.SUCCESS)  //Tek sertifika kontrolleri sorunlu  path validation başlatmıyoruz direk sorunlu diyoruz.
        {
            certStatusInfo.setCertificateStatus(CertificateStatus.CERTIFICATE_SELF_CHECK_FAILURE);
            aValidationSystem.getCertificateValidationCache().addCheckResult(certStatusInfo.clone());
            return certStatusInfo;
        } else {
            logger.debug("SELF CHECKS SUCCEEDED! ");
        }

        List<PathValidationRecord> validationHistory = new ArrayList<PathValidationRecord>();
        PathValidationResult pvr = PathValidationResult.INCOMPLETE_VALIDATION;
        boolean newPathFound = true;
        CertificatePath certificatePath = new CertificatePath(aCertificate, aValidationSystem);

        while ((pvr != PathValidationResult.SUCCESS) && newPathFound) {
            ValidationResult vr = _validatePath(aValidationSystem, certificatePath, certStatusInfo); //_zincirDogrula(pZincir,pSDB);
            pvr =vr.pvr;
            certStatusInfo = vr.csi;

            if (certificatePath.getHead() != null) //path geçerli ise validation record olarak dogrulama kayıtlarına ekliyoruz
            {
                List<ECertificate> certList = certificatePath.getCertificates();
                validationHistory.add(new PathValidationRecord(certList, _findInList(mWorkingNode.getSubject(),certList),pvr));
            }

            if (pvr != PathValidationResult.SUCCESS)    // Path dogrulanamadı
            {
                if ( pvr == PathValidationResult.CERTIFICATE_REVOKED )  // Eğer iptal olmuşsa bakmaya devam etmeye gerek yok??
                    break;

                newPathFound = certificatePath.createNewPath();
            }
        }

        certStatusInfo.setIncompletePathList(certificatePath.getIncompletePathList());  //Incomplete Pathleri kaydedelim.


        certificatePath = null;

        if (pvr != PathValidationResult.SUCCESS)    // Hiçbir local path doğrulanamadıysa remotepathleri  kontrol edelim
        {
            if (logger.isDebugEnabled())
                logger.warn(subject+" subjectli sertifika dogrulanamadı.");
            if(certStatusInfo.getCertificateStatus() == null){
                certStatusInfo.setCertificateStatus(CertificateStatus.PATH_VALIDATION_FAILURE);
            }
        }

        certStatusInfo.setValidationHistory(validationHistory);

        if (!validationHistory.isEmpty())   // Bir Path bulunmuşsa Registered itemları save edelim
        {
            aValidationSystem.getSaveSystem().processRegisteredItems(certStatusInfo);
        }
        
        // Doğrulama Hafızamıza ekleyelim
        if (certStatusInfo.getCertificateStatus()==CertificateStatus.VALID )
        {
            aValidationSystem.addToCACache(certStatusInfo.getCertificate());
            aValidationSystem.getCertificateValidationCache().addCheckResult(certStatusInfo.clone(), true);
        }
        else
            aValidationSystem.getCertificateValidationCache().addCheckResult(certStatusInfo.clone(),false);

        // Artık sonucunu bildiğimiz için Döngü Kontrol Listemizden çıkarabiliriz.
        aValidationSystem.removeFromCyclicCheckList(aCertificate);

        return certStatusInfo;

    }
    
    public void setDoNotUsePastRevocationInfo(boolean aDoNotUsePastRevocationInfo)
    {
    	mDoNotUsePastRevocationInfo = aDoNotUsePastRevocationInfo;
    }

    private CertificateStatusInfo _returnSertifikaDurumu(ValidationSystem aValidationSystem, ECertificate aCertificate, CertificateStatus aCertificateStatus)
    {
        CertificateStatusInfo pSDB = new CertificateStatusInfo(aCertificate, aValidationSystem.getBaseValidationTime());
        pSDB.setCertificateStatus(aCertificateStatus);
        aValidationSystem.getCertificateValidationCache().addCheckResult(pSDB.clone());
        return pSDB;
    }


    boolean _hasValidCrossCertificate(ECertificate aSertifika, ValidationSystem aValidationSystem)
            throws ESYAException
    {
        FindSystem bs = aValidationSystem.getFindSystem();
        ECertificate pSertifika = bs.findCrossCertificate(aValidationSystem, aSertifika);

        // Geçerli bir çapraz bulundu ve doğrulandı mı.
        return pSertifika != null;
    }


    public List<Asn1ObjectIdentifier> getAuthoritiesConstrainedPolicySet()
    {
        return mAuthoritiesConstrainedPolicySet;
    }

    public List<Asn1ObjectIdentifier> getUserConstrainedPolicySet()
    {
        return mUserConstrainedPolicySet;
    }

    public List<PolicyQualifierInfo> getValidQualifierSet()
    {
        return mValidQualifierSet;
    }
}
