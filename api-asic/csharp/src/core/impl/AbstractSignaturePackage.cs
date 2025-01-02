using System;
using System.Collections.Generic;
using System.IO;
using ICSharpCode.SharpZipLib.Zip;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo;
using tr.gov.tubitak.uekae.esya.api.asic.model;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic.core.impl
{
    /**
     * @author yavuz.kahveci
     */
    public abstract class AbstractSignaturePackage : SignaturePackage
    {
        private static readonly ILog Logger = LogManager.GetLogger(typeof(AbstractSignaturePackage));

        private PackageContents _contents;

        private readonly Context _context;

        protected Context Context
        {
            get { return _context; }
        }

        protected AbstractSignaturePackage(Context context)
        {
            _context = context;
        }

        protected PackageContents Contents
        {
            get
            {
                if (_contents == null)
                    _contents = createInitialPackage();
                return _contents;
            }
            set { _contents = value; }
        }

        public Signable addData(Signable signable, string pathInZip)
        {
            SignableEntry se = new SignableEntry(signable, pathInZip);
            Contents.getDatas().Add(se);
            return se;
        }

        public List<Signable> getDatas()
        {
            return Contents.getDatas();
        }

        public virtual SignatureContainer createContainer()
        {
            if (Contents.getContainers().Count>0 && !getPackageInfo().allowsMultipleSignatureContainers()){
                throw new SignatureRuntimeException("More signature containers is not allowed!");
            }
            SignatureContainer sc = createContainerImpl();
            string entryName = "META-INF/"+generateSignatureContainerName();
            SignatureContainerEntryImpl sce = new SignatureContainerEntryImpl(entryName, sc);
            Contents.getContainers().Add(sce);
            return sc;
        }

        public List<SignatureContainer> getContainers()
        {
            List<SignatureContainerEntryImpl> entries = Contents.getContainers();
            List<SignatureContainer> containers = new List<SignatureContainer>(entries.Count);
            foreach (SignatureContainerEntry entry in entries){
                containers.Add(entry.getContainer());
            }
            return containers;
        }

        public virtual PackageValidationResult verifyAll()
        {
            bool incompleteExists = false;
            bool invalidExists = false;
            if (Contents.getContainers().Count==0)
                throw new SignatureRuntimeException("No container found in package!");

            PackageValidationResultImpl spvr = new PackageValidationResultImpl();
            foreach (SignatureContainerEntryImpl containerEntry in Contents.getContainers()){
                SignatureContainer container = containerEntry.getContainer();
                try {

                    ContainerValidationResult cvr = container.verifyAll();

                    spvr.addResult(containerEntry, cvr);

                    if (cvr.getResultType().Equals(ContainerValidationResultType.CONTAINS_INCOMPLETE)){
                        incompleteExists = true;
                    }

                    if (cvr.getResultType().Equals(ContainerValidationResultType.CONTAINS_INVALID)){
                        invalidExists = true;
                    }

                } catch (Exception x){
                    Logger.Warn("Error in signature validation ", x);
                    invalidExists = true;
                }

            }
            if (invalidExists){
                spvr.setResultType(PackageValidationResultType.CONTAINS_INVALID);
            }
            else if (incompleteExists){
                spvr.setResultType(PackageValidationResultType.CONTAINS_INCOMPLETE);
            }
            else {
                spvr.setResultType(PackageValidationResultType.ALL_VALID);
            }
            return spvr;
        }

        public void write(Stream stream)
        {
            Contents.write(stream);
        }

        public void write()
        {
            FileInfo inputFile = Contents.getInputFile();
            FileInfo tempFile;
            try {
                tempFile = new FileInfo( Path.GetTempPath() +  "tem-" + inputFile.Name );
            } catch (Exception){
                throw new SignatureException("Cant create temp file for "+inputFile.FullName);
            }
            FileStream tfos;
            try {
                tfos = new FileStream(tempFile.FullName, FileMode.Create);
            } catch (Exception){
                throw new SignatureException("Cant create temp stream for "+inputFile.FullName);
            }
            write(tfos);

            try {
                ZipFile zipFile = Contents.getZipFile();
                zipFile.Close();
            } catch (Exception x){
                throw new SignatureException("Cant close resources for zip file "+inputFile.FullName, x);
            }
            inputFile.Delete();
            try
            {
                FileUtil.move(tempFile, inputFile);
            }
            catch (Exception)
            {
                throw new SignatureRuntimeException("Cant move " + tempFile.FullName + " as " + inputFile.FullName + ". Old package is lost!");
            }

        }


        public virtual void setContents(PackageContents aContents)
        {
            Contents = aContents;
        }

        protected virtual SignatureContainer createContainerImpl(){
            return SignatureFactory.createContainer(getSignatureFormat(), Context);
        }

        public abstract PackageContents createInitialPackage();
        public abstract PackageInfo getPackageInfo();

        /**
         * @return  signature file name like 'signatures.xml' or 'signature.p7s'
         */
        public abstract string generateSignatureContainerName();

        public abstract PackageType getPackageType();
        public abstract SignatureFormat getSignatureFormat();
    }
}
