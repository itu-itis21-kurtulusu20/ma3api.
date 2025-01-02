using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo;
using tr.gov.tubitak.uekae.esya.api.asic.model;
using tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest;
using tr.gov.tubitak.uekae.esya.api.asic.util;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.impl;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic.core.impl
{
    /**
     * @author yavuz.kahveci
     */
    public class ASiCECAdESPackage : AbstractSignaturePackage
    {
        static PackageInfo packageInfo = new ASiCEPackageInfo();

        public ASiCECAdESPackage(Context context) :base(context)
        {
        }

        public override void setContents(PackageContents aContents)
        {
            base.Contents = aContents;
            foreach (ASiCManifest manifest in Contents.getAsicManifests()){
                SignatureContainerEntryImpl entry = findContainer(manifest);
                if (entry!=null){
                    DeferredSignable ds = (DeferredSignable)entry.getContainer().getContext().getData();
                    ds.setActualSignable(new SignableBytes(manifest.getInitialBytes(), manifest.getASiCDocumentName(), "text/xml"));
                }
            }

        }

        public override SignatureContainer createContainer()
        {
            SignatureContainer sc = base.createContainer();

            SignatureContainerEntryImpl entry = Contents.getContainers()[Contents.getContainers().Count-1];

            // not necessary but nice to pair signature and manifest names
            string sigName = entry.getASiCDocumentName();
            int idStart = sigName.LastIndexOf('-');
            int idEnd = sigName.LastIndexOf('.');
            string id =  sigName.Substring(idStart+1, (idEnd-idStart-1));

            ASiCManifest manifest = new ASiCManifest(id, entry.getASiCDocumentName());
            ContainerWithManifest cwm = new ContainerWithManifest((SignatureContainerEx)entry.getContainer(), manifest, this);

            entry.setContainer(cwm);
            Contents.getAsicManifests().Add(manifest);

            return cwm;
        }


        private SignatureContainerEntryImpl findContainer(ASiCManifest manifest){
            string signatureURI = manifest.getSignatureReference().getUri();
            foreach (SignatureContainerEntryImpl ce in Contents.getContainers()){
                if (ce.getASiCDocumentName().Equals(signatureURI)){
                    return ce;
                }
            }
            return null;
        }

        public override PackageValidationResult verifyAll()
        {
            foreach (ASiCManifest manifest in Contents.getAsicManifests()){
                bool valid = manifest.validateDataObjectRefs(new PackageContentResolver(Contents));
                if (!valid){

                    SignatureContainerEntryImpl entry = findContainer(manifest);

                    Dictionary<Signature, SignatureValidationResult> svrMap =  new Dictionary<Signature, SignatureValidationResult>();
                    svrMap.Add(entry.getContainer().getSignatures()[0], new SignatureValidaitonResultImpl(manifest)); 
                    /*new SignatureValidationResult()
                    {
                        public CertificateStatusInfo getCertificateStatusInfo() { return null; }
                        public ValidationResultType getResultType() { return ValidationResultType.INVALID; }
                        public string getCheckMessage(){ return "AsicManifestCheck"; } // todo i18n
                        public string getCheckResult(){return "Manifest '"+manifest.getSignatureReference().getUri()+"' has invalid digest value!"; } // todo i18n
                        public List<ValidationResultDetail> getDetails(){ return null; }
                        public List<SignatureValidationResult> getCounterSignatureValidationResults(){ return null; }
                    });*/

                    Dictionary<SignatureContainerEntryImpl, ContainerValidationResult> cvrMap = new Dictionary<SignatureContainerEntryImpl, ContainerValidationResult>();

                    ContainerValidationResult cvr = new ContainerValidationResultImpl(ContainerValidationResultType.CONTAINS_INVALID, svrMap);
                    cvrMap.Add(entry, cvr);
                    return new PackageValidationResultImpl(PackageValidationResultType.CONTAINS_INVALID, cvrMap);
                }
            }
            return base.verifyAll();
        }

        public class SignatureValidaitonResultImpl : SignatureValidationResult
        {
            private readonly ASiCManifest manifest;
            public SignatureValidaitonResultImpl(ASiCManifest manifest) { this.manifest = manifest; }
            public CertificateStatusInfo getCertificateStatusInfo() { return null; }
            public ValidationResultType getResultType() { return ValidationResultType.INVALID; }
            public string getCheckMessage() { return "AsicManifestCheck"; } // todo i18n
            public string getCheckResult() { return "Manifest '" + manifest.getSignatureReference().getUri() + "' has invalid digest value!"; } // todo i18n
            public List<ValidationResultDetail> getDetails() { return null; }
            public List<SignatureValidationResult> getCounterSignatureValidationResults() { return null; }
        }

        public override PackageType getPackageType()
        {
            return PackageType.ASiC_E;
        }

        public override SignatureFormat getSignatureFormat()
        {
            return SignatureFormat.CAdES;
        }

        public override PackageContents createInitialPackage()
        {
            PackageContents pc = new PackageContents();
            pc.setMimetype(ASiCMimetype.ASiC_E);
            return pc;
        }

        public override PackageInfo getPackageInfo()
        {
            return packageInfo;
        }

        public override string generateSignatureContainerName()
        {
            return "signature-"+ ASiCUtil.id()+".p7s";
        }

    }
}