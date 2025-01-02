package tr.gov.tubitak.uekae.esya.api.asic.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asic.core.PackageContentResolver;
import tr.gov.tubitak.uekae.esya.api.asic.core.PackageValidationResultImpl;
import tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo.ASiCEPackageInfo;
import tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo.PackageInfo;
import tr.gov.tubitak.uekae.esya.api.asic.model.DeferredSignable;
import tr.gov.tubitak.uekae.esya.api.asic.model.PackageContents;
import tr.gov.tubitak.uekae.esya.api.asic.model.SignatureContainerEntryImpl;
import tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest.ASiCManifest;
import tr.gov.tubitak.uekae.esya.api.asic.util.ASiCMimetype;
import tr.gov.tubitak.uekae.esya.api.asic.util.ASiCUtil;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.impl.ContainerValidationResultImpl;
import tr.gov.tubitak.uekae.esya.api.signature.impl.SignableBytes;
import tr.gov.tubitak.uekae.esya.api.signature.impl.SignatureContainerEx;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageType;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageValidationResult;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageValidationResultType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ayetgin
 */
public class ASiCECAdESPackage extends AbstractSignaturePackage
{
    private static Logger logger = LoggerFactory.getLogger(ASiCECAdESPackage.class);

    static PackageInfo packageInfo = new ASiCEPackageInfo();

    public ASiCECAdESPackage(Context context)
    {
        super(context);
    }

    @Override
    public void setContents(PackageContents aContents)
    {
        super.setContents(aContents);
        for (ASiCManifest manifest : contents.getAsicManifests()){
            SignatureContainerEntryImpl entry = findContainer(manifest);
            if (entry!=null){
                DeferredSignable ds = (DeferredSignable)entry.getContainer().getContext().getData();
                ds.setActualSignable(new SignableBytes(manifest.getInitialBytes(), manifest.getASiCDocumentName(), "text/xml"));
            }
        }

    }

    @Override
    public SignatureContainer createContainer()
    {
        SignatureContainer sc = super.createContainer();

        SignatureContainerEntryImpl entry = contents.getContainers().get(contents.getContainers().size()-1);

        // not necessary but nice to pair signature and manifest names
        String sigName = entry.getASiCDocumentName();
        int idStart = sigName.lastIndexOf('-');
        int idEnd = sigName.lastIndexOf('.');
        String id =  sigName.substring(idStart, idEnd);

        ASiCManifest manifest = new ASiCManifest(id, entry.getASiCDocumentName());
        ContainerWithManifest cwm = new ContainerWithManifest((SignatureContainerEx)entry.getContainer(), manifest, this);

        entry.setContainer(cwm);
        contents.getAsicManifests().add(manifest);

        return cwm;
    }


    private SignatureContainerEntryImpl findContainer(ASiCManifest manifest){
        String signatureURI = manifest.getSignatureReference().getUri();
        for (SignatureContainerEntryImpl ce : contents.getContainers()){
            if (ce.getASiCDocumentName().equals(signatureURI)){
                return ce;
            }
        }
        return null;
    }

    @Override
    public PackageValidationResult verifyAll()
    {
        for (final ASiCManifest manifest : contents.getAsicManifests()){
            boolean valid = manifest.validateDataObjectRefs(new PackageContentResolver(contents));
            if (!valid){

                SignatureContainerEntryImpl entry = findContainer(manifest);
                if(entry == null) {
                    logger.error("entry is null");
                    return null;
                }

                Map<Signature, SignatureValidationResult> svrMap =  new HashMap<Signature, SignatureValidationResult>();
                svrMap.put(entry.getContainer().getSignatures().get(0),
                new SignatureValidationResult()
                {
                    public CertificateStatusInfo getCertificateStatusInfo() { return null; }
                    public ValidationResultType getResultType() { return ValidationResultType.INVALID; }
                    public String getCheckMessage(){ return "AsicManifestCheck"; } // todo i18n
                    public String getCheckResult(){return "Manifest '"+manifest.getSignatureReference().getUri()+"' has invalid digest value!"; } // todo i18n
                    public List<ValidationResultDetail> getDetails(){ return Collections.EMPTY_LIST; }
                    public List<SignatureValidationResult> getCounterSignatureValidationResults(){ return Collections.EMPTY_LIST; }
                });

                Map<SignatureContainerEntryImpl, ContainerValidationResult> cvrMap = new HashMap<SignatureContainerEntryImpl, ContainerValidationResult>();

                ContainerValidationResult cvr = new ContainerValidationResultImpl(ContainerValidationResultType.CONTAINS_INVALID, svrMap);
                cvrMap.put(entry, cvr);
                return new PackageValidationResultImpl(PackageValidationResultType.CONTAINS_INVALID, cvrMap);
            }
        }
        return super.verifyAll();
    }

    public PackageType getPackageType()
    {
        return PackageType.ASiC_E;
    }

    public SignatureFormat getSignatureFormat()
    {
        return SignatureFormat.CAdES;
    }

    PackageContents createInitialPackage()
    {
        PackageContents pc = new PackageContents();
        pc.setMimetype(ASiCMimetype.ASiC_E);
        return pc;
    }

    PackageInfo getPackageInfo()
    {
        return packageInfo;
    }

    String generateSignatureContainerName()
    {
        return "signature-"+ ASiCUtil.id()+".p7s";
    }

}
