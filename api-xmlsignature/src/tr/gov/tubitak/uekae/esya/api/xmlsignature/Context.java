package tr.gov.tubitak.uekae.esya.api.xmlsignature;

import org.w3c.dom.Document;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.FindSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation.RevocationChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertValidationPolicies;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.Config;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ValidationConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.IdRegistry;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.IResolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.IdGenerator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.URI;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.UniqueIdGenerator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.Validator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

/**
 * Contains context information for construction and validation of XML
 * Signatures.
 *
 * <p>Note that <code>Context</code> instances can contain information and state
 * specific to the XML signature structure it is used with. 
 *
 * @author ahmety
 * date: May 14, 2009
 */
public class Context
{
    private org.w3c.dom.Document mDocument;

    private String mBaseURI;
    private String mOutputDir;

    private Config mConfig;

    private IdRegistry mIdRegistry = new IdRegistry();
    private IdGenerator mIdGenerator = new UniqueIdGenerator();

    private Set<IResolver> mResolvers = new HashSet<IResolver>(0);

    private List<Validator> mValidators = new ArrayList<Validator>();

    //private ValidationSystem mCertValidationSystem;
    private Map<XMLSignature, CertValidationData> mValidationDatas = new HashMap<XMLSignature, CertValidationData>();
    private Map<XMLSignature, CertificateStatusInfo> mValidationResults = new HashMap<XMLSignature, CertificateStatusInfo>();
    private Map<XMLSignature, List<SignedDataValidationResult>> mTimestampValidationResults = new HashMap<XMLSignature, List<SignedDataValidationResult>>();

    private boolean mValidateCertificates = true;
    private boolean mValidateCertificateBeforeSign = false;
    private boolean mValidateTimeStamps = true;

    private boolean mValidateManifests = true;

    private Calendar mUserValidationTime;

    public Context()
    {
    }

    public Context(String aBaseURI) throws XMLSignatureException
    {
        try {
            mBaseURI = aBaseURI;
        } catch (Exception x){
            throw new XMLSignatureException(x, "resolver.cantResolveUri", aBaseURI);
        }
    }

    public Context(File aBaseDir) throws XMLSignatureException
    {
        try {
            mBaseURI = aBaseDir.getPath();
        } catch (Exception x){
            throw new XMLSignatureException(x, "resolver.cantResolveUri", aBaseDir);
        }
    }

    public Context(java.net.URI aBaseURI)
    {
        try {
            mBaseURI = aBaseURI.toString();
        } catch (Exception x){
            throw new XMLSignatureRuntimeException(x, "resolver.cantResolveUri", aBaseURI);
        }
    }

    public Context(URI aBaseURI)
    {
        mBaseURI = aBaseURI.toString();
    }

    public Document getDocument()
    {
        if (mDocument==null){
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                DocumentBuilder db = factory.newDocumentBuilder();
                mDocument = db.newDocument();
            }
            catch (Exception x){
                // should not happen so no bundle message
                throw new ESYARuntimeException(x);
            }
        }
        return mDocument;
    }

    public void setDocument(org.w3c.dom.Document aDocument)
    {
        mDocument = aDocument;
    }

    public void setConfig(Config aConfig)
    {
        mConfig = aConfig;
        setValidateCertificateBeforeSign(mConfig.getValidationConfig().isValidateCertificateBeforeSigning());
        setValidateTimeStamps(mConfig.getValidationConfig().isValidateTimestampWhileSigning());
    }

    public Config getConfig()
    {
        if (mConfig==null){
            mConfig = new Config();
            setValidateCertificateBeforeSign(mConfig.getValidationConfig().isValidateCertificateBeforeSigning());
            setValidateTimeStamps(mConfig.getValidationConfig().isValidateTimestampWhileSigning());
        }
        return mConfig;
    }

    public IdRegistry getIdRegistry()
    {
        return mIdRegistry;
    }

    public void setIdRegistry(IdRegistry aIdRegistry)
    {
        mIdRegistry = aIdRegistry;
    }

    public IdGenerator getIdGenerator()
    {
        return mIdGenerator;
    }

    public void setIdGenerator(IdGenerator aIdGenerator)
    {
        mIdGenerator = aIdGenerator;
    }

    public String getBaseURI()
    {
        return mBaseURI;
    }

    public String getBaseURIStr()
    {
        return mBaseURI != null ? mBaseURI.toString() : null;
    }

    public void setBaseURI(String aBaseURI)
    {
        mBaseURI = aBaseURI;
    }

    public String getOutputDir(){
    	return mOutputDir;
    }

    public void setOutputDir(String aOutputDir){
    	mOutputDir = aOutputDir;
    }

    public Set<IResolver> getResolvers()
    {
        return mResolvers;
    }

    public void addExternalResolver(IResolver aResolver)
    {
        mResolvers.add(aResolver);
    }

    public List<Validator> getValidators()
    {
        return mValidators;
    }

    public void addValidator(Validator aValidator){
        mValidators.add(aValidator);
    }

    public CertValidationData getValidationData(XMLSignature signature)
    {
        if (!mValidationDatas.containsKey(signature)){
            mValidationDatas.put(signature, new CertValidationData());
        }
        return mValidationDatas.get(signature);
    }

    public List<CertValidationData> getAllValidationData()
    {
        return new ArrayList<CertValidationData>(mValidationDatas.values());
    }

    public CertificateStatusInfo getValidationResult(XMLSignature signature)
    {
        return mValidationResults.get(signature);
    }

    public void setValidationResult(XMLSignature signature, CertificateStatusInfo aValidationResult)
    {
        mValidationResults.put(signature, aValidationResult);
    }

    public List<SignedDataValidationResult> getTimestampValidationResults(XMLSignature signature)
    {
        return mTimestampValidationResults.get(signature);
    }

    public void addTimestampValidationResult(XMLSignature signature, SignedDataValidationResult aValidationResult)
    {
        if (!mTimestampValidationResults.containsKey(signature)){
            mTimestampValidationResults.put(signature, new ArrayList<SignedDataValidationResult>());
        }
        mTimestampValidationResults.get(signature).add(aValidationResult);
    }

    public boolean isValidateCertificates()
    {
        return mValidateCertificates;
    }

    public boolean isValidateCertificateBeforeSign() {
        return mValidateCertificateBeforeSign;
    }

    public boolean isValidateTimeStamps()
    {
        return mValidateTimeStamps;
    }

    public void setValidateCertificates(boolean aValidateCertificates)
    {
        mValidateCertificates = aValidateCertificates;
    }

    public void setValidateCertificateBeforeSign(boolean mValidateCertificateBeforeSign) {
        this.mValidateCertificateBeforeSign = mValidateCertificateBeforeSign;
    }

    public void setValidateTimeStamps(boolean aValidateTimeStamps)
    {
        mValidateTimeStamps = aValidateTimeStamps;
    }


    public ValidationSystem getCertValidationSystem(ECertificate certificate, boolean useExternalResources, XMLSignature xmlSignature)
    {
        // todo cache validation system ?
        ValidationSystem validationSystem;
        ValidationPolicy policy;
        ValidationSystem vs;
        String configFile = null;
        try {
            ValidationConfig config = getConfig().getValidationConfig();
            // policy = config.getCertificateValidationPolicy();
            CertValidationPolicies policies = config.getCertValidationPolicies();
            policy = policies.getPolicyFor(certificate);
            validationSystem = CertificateValidation.createValidationSystem(policy);
        }
        catch (Exception x) {
            throw new XMLSignatureRuntimeException(x, "config.cantFind",  I18n.translate("configFile"));
        }

        ValidationSystem result = validationSystem;


        if (!useExternalResources){

            result = (ValidationSystem)validationSystem.clone();

            for (RevocationChecker rc : result.getCheckSystem().getRevocationCheckers()){
                rc.setParentSystem(result);
                rc.getFinders().clear();
            }

            FindSystem emptyCloneFS = new FindSystem();
            FindSystem originalFS = validationSystem.getFindSystem();
            emptyCloneFS.setTrustedCertificateFinders(originalFS.getTrustedCertificateFinders());

            result.setFindSystem(emptyCloneFS);

        }

        try {
            result.setBaseValidationTime(getValidationTime(xmlSignature));
        } catch (Exception x) {
            throw new XMLSignatureRuntimeException(x, "Get not set validation time");
        }

        return result;
    }
    /*
    public void setCertValidationSystem(ValidationSystem aCertValidationSystem)
    {
        mCertValidationSystem = aCertValidationSystem;
    }         */

    /**
     * @return should manifest be validated automatically
     */
    public boolean isValidateManifests()
    {
        return mValidateManifests;
    }

    /**
     * @param aValidateManifests should manifest be validated automatically
     */
    public void setValidateManifests(boolean aValidateManifests)
    {
        mValidateManifests = aValidateManifests;
    }


    public Calendar getUserValidationTime()
    {
        return mUserValidationTime;
    }

    public void setUserValidationTime(Calendar aUserValidationTime)
    {
        mUserValidationTime = aUserValidationTime;
    }



    public Calendar getValidationTime(XMLSignature signature) throws XMLSignatureException {

        Calendar timestampTime = signature.getTimestampTime();
        if (timestampTime != null)
            return timestampTime;
        if(mConfig.getValidationConfig().isTrustSigningTime()){
            Calendar signingTimePropertyTime = signature.getSigningTimePropertyTime();
            if (signingTimePropertyTime != null)
                return signingTimePropertyTime;
        }
        if (mUserValidationTime!=null)
            return mUserValidationTime;
        // else rertun now
        return Calendar.getInstance();
    }

    public static void main(String[] args) throws Exception
    {
        // todo context URI turkish letters...
    }
}
