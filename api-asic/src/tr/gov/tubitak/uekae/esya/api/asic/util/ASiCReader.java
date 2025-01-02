package tr.gov.tubitak.uekae.esya.api.asic.util;

import tr.gov.tubitak.uekae.esya.api.asic.model.*;
import tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest.ASiCManifest;
import tr.gov.tubitak.uekae.esya.api.asic.model.signatures.SignaturesFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.StreamUtil;
import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author ayetgin
 */
public class ASiCReader
{

    public PackageContents read(File file, Context context) throws SignatureException
    {
        PackageContents contents = new PackageContents();
        contents.setInputFile(file);
        try {
            ZipFile zipFile = new ZipFile(file);
            contents.setZipFile(zipFile);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()){
                ZipEntry entry = entries.nextElement();

                String name = entry.getName();
                InputStream stream = zipFile.getInputStream(entry);
                System.out.println("> " + name);

                // mimetype
                if (name.equals("mimetype")){
                    byte[] mimeBytes = StreamUtil.readAll(stream);
                    contents.setMimetype(new String(mimeBytes));
                }
                // not in META-INF dir
                else if (!name.startsWith("META-INF")){
                    contents.getDatas().add(new SignableZipEntry(zipFile, entry));
                }
                // manifest.xml
                else if (name.equalsIgnoreCase("META-INF/manifest.xml")){
                    Manifest manifest = new Manifest(stream);
                    contents.setManifest(manifest);
                }
                // metadata.xml
                else if (name.equalsIgnoreCase("META-INF/metadata.xml")){
                    OCFMetadata metadata = new OCFMetadata(stream);
                    contents.setMetadata(metadata);
                }
                // container.xml
                else if (name.equalsIgnoreCase("META-INF/container.xml")){
                    ContainerInfo container = new ContainerInfo(stream);
                    contents.setContainerInfo(container);
                }
                // ASiCManifest
                else if (name.startsWith("META-INF/") && name.contains("ASiCManifest")){
                    ASiCManifest manifest = new ASiCManifest();
                    manifest.read(stream);
                    manifest.setASiCDocumentName(name);
                    contents.getAsicManifests().add(manifest);
                }
                // "signatures" in xml, "signature" in cades
                else if (name.startsWith("META-INF/") && name.contains("signature")){
                    Context cloned = context.clone();
                    cloned.setData(new DeferredSignable());
                    SignatureContainerEntryImpl sc = SignaturesFactory.readSignatureContainer(cloned, contents, stream, name);
                    contents.getContainers().add(sc);
                }
                else if (name.equals("META-INF/")){
                    // continue
                }
                else {
                    throw new ESYARuntimeException("Unknown file in archive: "+name);
                }

            }

        } catch (Exception x){
            throw new SignatureRuntimeException(x);
        }
        return contents;
    }


    public static void main(String[] args) throws Exception
    {
        System.out.println(System.getProperty("java.io.tmpdir"));
        System.out.println();

        String path1 = "E:\\ahmet\\prj\\asic\\asic\\ASIC_PLUGTEST_FILES\\ASiC_plugtest_20121227_145940(birstunas)\\ASiC-E_CSSC_X.SCOK\\POL\\Container-ASiC-E_CSSC_X-8.asice";
        String path2 = "E:\\ahmet\\prj\\asic\\asic\\ASIC_PLUGTEST_FILES\\ASiC_plugtest_20121227_145940(birstunas)\\ASiC-E_CSSC_X.SCOK\\POL\\Container-ASiC-E_CSSC_X-7.asice";
        String path3 = "E:\\ahmet\\prj\\asic\\asic\\ASIC_PLUGTEST_FILES\\ASiC_plugtest_20121227_145940(birstunas)\\ASiC-E_CSSC_C.SCOK\\POL\\Container-ASiC-E_CSSC_C-6.asice";

        ASiCReader reader = new ASiCReader();
        PackageContents contents = reader.read(new File(path1), new Context());
        System.out.println(contents);
        contents.write(new FileOutputStream("T:\\api-asic\\testresources\\test.asic"));

        /*
        contents = reader.read(new File(path3));
        System.out.println(contents);

        contents = reader.read(new File(path1));
        System.out.println(contents); */

    }
}
