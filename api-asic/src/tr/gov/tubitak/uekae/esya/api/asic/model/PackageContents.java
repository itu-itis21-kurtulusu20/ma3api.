package tr.gov.tubitak.uekae.esya.api.asic.model;

import tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest.ASiCManifest;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StreamUtil;
import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author ayetgin
 */
public class PackageContents
{
    private String mimetype;
    private List<Signable> datas = new ArrayList<Signable>(1);

    // in META-INF
    private List<SignatureContainerEntryImpl> containers = new ArrayList<SignatureContainerEntryImpl>(1);
    private List<ASiCManifest> asicManifests = new ArrayList<ASiCManifest>(0);  // for CAdES or TST

    // other standarts
    private ContainerInfo containerInfo; // OCF
    private Manifest manifest;           // ODF
    private OCFMetadata metadata;        // OCF

    private File inputFile;
    private ZipFile zipFile;

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public ZipFile getZipFile() {
        return zipFile;
    }

    public void setZipFile(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    public String getMimetype()
    {
        return mimetype;
    }

    public void setMimetype(String aMimetype)
    {
        mimetype = aMimetype;
    }

    public List<Signable> getDatas()
    {
        return datas;
    }

    public void setDatas(List<Signable> aDatas)
    {
        datas = aDatas;
    }

    public List<SignatureContainerEntryImpl> getContainers()
    {
        return containers;
    }

    public void setContainers(List<SignatureContainerEntryImpl> aContainers)
    {
        containers = aContainers;
    }

    public List<ASiCManifest> getAsicManifests()
    {
        return asicManifests;
    }

    public void setAsicManifests(List<ASiCManifest> aAsicManifests)
    {
        asicManifests = aAsicManifests;
    }

    public ContainerInfo getContainerInfo()
    {
        return containerInfo;
    }

    public void setContainerInfo(ContainerInfo aContainerInfo)
    {
        containerInfo = aContainerInfo;
    }

    public Manifest getManifest()
    {
        return manifest;
    }

    public void setManifest(Manifest aManifest)
    {
        manifest = aManifest;
    }

    public OCFMetadata getMetadata()
    {
        return metadata;
    }

    public void setMetadata(OCFMetadata aMetadata)
    {
        metadata = aMetadata;
    }

    public void write(OutputStream stream)
        throws SignatureException
    {
        try {
            ZipOutputStream zos = new ZipOutputStream(stream);
            if (mimetype!=null) {
                zos.setLevel(ZipEntry.STORED);
                write(zos, "mimetype", mimetype.getBytes());
                zos.setLevel(ZipEntry.DEFLATED);

            }
            if (datas!=null){
                for (Signable s : datas){
                    write(zos, s.getURI(), s.getContent());
                }
            }
            if (asicManifests!=null){
                for (ASiCManifest m : asicManifests){
                    write(zos, m);
                }
            }
            if (containers !=null){
                for (SignatureContainerEntryImpl s : containers){
                    write(zos, s);
                }
            }
            if (manifest!=null){
                write(zos, manifest);
            }
            if (metadata!=null){
                write(zos, metadata);
            }
            if (containerInfo!=null){
                write(zos, containerInfo);
            }
            zos.close();
        }
        catch (Exception x){
            throw new SignatureException(x);
        }
    }

    private void write(ZipOutputStream stream, String entryName, byte[] content)
        throws ESYAException
    {
        try {
            stream.putNextEntry(new ZipEntry(entryName));
            stream.write(content);
            stream.closeEntry();
        }catch (Exception e){
            throw new ESYAException(e);
        }

    }

    private void write(ZipOutputStream stream, String entryName, InputStream content)
        throws ESYAException
    {
        try {
            stream.putNextEntry(new ZipEntry(entryName));
            StreamUtil.copy(content, stream);
            stream.closeEntry();
        }catch (Exception e){
            throw new ESYAException(e);
        }

    }

    private void write(ZipOutputStream stream, ASiCDocument document)
        throws ESYAException
    {
        try {
            stream.putNextEntry(new ZipEntry(document.getASiCDocumentName()));
            document.write(stream);
            stream.closeEntry();
        }catch (Exception e){
            throw new ESYAException(e);
        }

    }

    @Override
    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("mimetype: ").append(mimetype).append("\n");
        for (Signable s : datas){
            buffer.append("data: ").append(s.getURI()).append("\n");
        }
        for (SignatureContainerEntryImpl s : containers){
            buffer.append("signature: ").append(s.getASiCDocumentName()).append("\n");
        }
        for (ASiCManifest m : asicManifests){
            buffer.append("asic manifest: ").append(m.getASiCDocumentName()).append("\n");
        }
        buffer.append("manifest.xml : ").append(manifest!=null).append("\n");
        buffer.append("metadata.xml : ").append(metadata!=null).append("\n");
        buffer.append("container.xml: ").append(containerInfo!=null).append("\n\n");

        return buffer.toString();
    }
}
