package tr.gov.tubitak.uekae.esya.api.asic.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asic.core.PackageValidationResultImpl;
import tr.gov.tubitak.uekae.esya.api.asic.core.SignableEntry;
import tr.gov.tubitak.uekae.esya.api.asic.core.impl.packageinfo.PackageInfo;
import tr.gov.tubitak.uekae.esya.api.asic.model.PackageContents;
import tr.gov.tubitak.uekae.esya.api.asic.model.SignatureContainerEntryImpl;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageValidationResult;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageValidationResultType;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.SignaturePackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

/**
 * @author ayetgin
 */
public abstract class AbstractSignaturePackage implements SignaturePackage
{
    private static Logger logger = LoggerFactory.getLogger(AbstractSignaturePackage.class);

    protected PackageContents contents;
    protected Context context;

    protected AbstractSignaturePackage(Context context)
    {
        contents = createInitialPackage();
        this.context = context;
    }

    public Signable addData(Signable signable, String pathInZip)
    {
        SignableEntry se = new SignableEntry(signable, pathInZip);
        contents.getDatas().add(se);
        return se;
    }

    public List<Signable> getDatas(){
        return contents.getDatas();
    }

    public SignatureContainer createContainer()
    {
        if (contents.getContainers().size()>0 && !getPackageInfo().allowsMultipleSignatureContainers()){
            throw new SignatureRuntimeException("More signature containers is not allowed!");
        }
        SignatureContainer sc = createContainerImpl();
        String entryName = "META-INF/"+generateSignatureContainerName();
        SignatureContainerEntryImpl sce = new SignatureContainerEntryImpl(entryName, sc);
        contents.getContainers().add(sce);
        return sc;
    }

    public List<SignatureContainer> getContainers()
    {
        List<SignatureContainerEntryImpl> entries = contents.getContainers();
        List<SignatureContainer> containers = new ArrayList<SignatureContainer>(entries.size());
        for (SignatureContainerEntryImpl entry : entries){
            containers.add(entry.getContainer());
        }
        return containers;
    }

    public PackageValidationResult verifyAll()
    {
        boolean incompleteExists = false;
        boolean invalidExists = false;
        if (contents.getContainers().size()==0)
            throw new SignatureRuntimeException("No container found in package!");

        PackageValidationResultImpl spvr = new PackageValidationResultImpl();
        for (SignatureContainerEntryImpl containerEntry : contents.getContainers()){
            SignatureContainer container = containerEntry.getContainer();
            try {

                ContainerValidationResult cvr = container.verifyAll();

                spvr.addResult(containerEntry, cvr);

                if (cvr.getResultType().equals(ContainerValidationResultType.CONTAINS_INCOMPLETE)){
                    incompleteExists = true;
                }

                if (cvr.getResultType().equals(ContainerValidationResultType.CONTAINS_INVALID)){
                    invalidExists = true;
                }

            } catch (Exception x){
                logger.warn("Error in signature validation ", x);
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

    public void write(OutputStream stream) throws SignatureException
    {
        contents.write(stream);
    }

    public void write() throws SignatureException {
        File inputFile = contents.getInputFile();
        File tempFile;
        try {
            tempFile = File.createTempFile("tem-",inputFile.getName());
        } catch (Exception x){
            throw new SignatureException("Cant create temp file for " + inputFile.getPath(), x);
        }
        FileOutputStream tfos;
        try {
            tfos = new FileOutputStream(tempFile);
        } catch (Exception x){
            throw new SignatureException("Cant create temp stream for " + inputFile.getPath(), x);
        }
        write(tfos);

        try {
            ZipFile zipFile = contents.getZipFile();
            zipFile.close();
        } catch (Exception x){
            throw new SignatureException("Cant close resources for zip file " + inputFile.getPath(), x);
        }

        boolean deleted = inputFile.delete();
        if (!deleted){
            throw new SignatureRuntimeException("Cant delete "+inputFile.getPath()+". New file is "+tempFile.getPath());
        }

        try {
            FileUtil.move(tempFile, inputFile);
        } catch (Exception x){
            throw new SignatureRuntimeException("Cant move "+tempFile.getPath()+" as "+inputFile.getPath()+". Old package is lost!" , x);
        }
    }

    public PackageContents getContents()
    {
        return contents;
    }

    public void setContents(PackageContents aContents)
    {
        contents = aContents;
    }

    protected SignatureContainer createContainerImpl(){
        return SignatureFactory.createContainer(getSignatureFormat(), context.clone());
    }

    abstract PackageContents createInitialPackage();
    abstract PackageInfo getPackageInfo();

    /**
     * @return  signature file name like 'signatures.xml' or 'signature.p7s'
     */
    abstract String generateSignatureContainerName();
}
