using System;
using System.IO;
using System.Text;
using ICSharpCode.SharpZipLib.Zip;
using tr.gov.tubitak.uekae.esya.api.asic.model;
using tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest;
using tr.gov.tubitak.uekae.esya.api.asic.model.signatures;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace tr.gov.tubitak.uekae.esya.api.asic.util
{
    /**
     * @author yavuz.kahveci
     */
    public class ASiCReader
    {
        public PackageContents read(FileInfo fileInfo, Context context)
        {
            PackageContents contents = new PackageContents();
            contents.setInputFile(fileInfo);
            try
            {
                string filePath = fileInfo.FullName;
                ZipFile zipFile = new ZipFile(filePath);
                contents.setZipFile(zipFile);
                foreach (ZipEntry entry in zipFile)
                {
                    string name = entry.Name;
                    Stream stream = zipFile.GetInputStream(entry);
                    Console.WriteLine("> " + name);

                    // mimetype
                    if (name.Equals("mimetype",StringComparison.OrdinalIgnoreCase)/*.equals("mimetype")*/){
                        byte[] mimeBytes = StreamUtil.readAll(stream);
                        contents.setMimetype(Encoding.UTF8.GetString(mimeBytes)/*new String(mimeBytes)*/);
                    }
                    // not in META-INF dir
                    else if (!name.StartsWith("META-INF")){
                        contents.getDatas().Add(new SignableZipEntry(zipFile, entry));
                    }
                    // manifest.xml
                    else if (name.Equals("META-INF/manifest.xml", StringComparison.OrdinalIgnoreCase)/*.equalsIgnoreCase("META-INF/manifest.xml")*/){
                        Manifest manifest = new Manifest(stream);
                        contents.setManifest(manifest);
                    }
                    // metadata.xml
                    else if (name.Equals("META-INF/metadata.xml", StringComparison.OrdinalIgnoreCase)/*.equalsIgnoreCase("META-INF/metadata.xml")*/){
                        OCFMetadata metadata = new OCFMetadata(stream);
                        contents.setMetadata(metadata);
                    }
                    // container.xml
                    else if (name.Equals("META-INF/container.xml", StringComparison.OrdinalIgnoreCase)/*.equalsIgnoreCase("META-INF/container.xml")*/){
                        ContainerInfo container = new ContainerInfo(stream);
                        contents.setContainerInfo(container);
                    }
                    // ASiCManifest
                    else if (name.StartsWith("META-INF/") && name.Contains("ASiCManifest")){
                        ASiCManifest manifest = new ASiCManifest();
                        manifest.read(stream);
                        manifest.setASiCDocumentName(name);
                        contents.getAsicManifests().Add(manifest);
                    }
                    // "signatures" in xml, "signature" in cades
                    else if (name.StartsWith("META-INF/") && name.Contains("signature"))
                    {
                        Context cloned = (Context) context.Clone();
                        cloned.setData(new DeferredSignable());
                        //Context context = new Context();
                        //context.setConfig(new Config());
                        context.setData(new DeferredSignable());
                        SignatureContainerEntryImpl sc = SignaturesFactory.readSignatureContainer(cloned, contents, stream, name);
                        contents.getContainers().Add(sc);
                    }
                    else if (name.Equals("META-INF/")){
                        // continue
                    }
                    else {
                        throw new SignatureRuntimeException("Unknown file in archive: "+name);
                    }
                }

            } catch (Exception x){
                Console.WriteLine(x.StackTrace);
                throw new SignatureRuntimeException(x);
            }
            return contents;
        }


        /*public static void main(String[] args)
        {
            //Console.WriteLine(System.getProperty("java.io.tmpdir"));
            Console.WriteLine();

            string path1 = "E:\\ahmet\\prj\\asic\\asic\\ASIC_PLUGTEST_FILES\\ASiC_plugtest_20121227_145940(birstunas)\\ASiC-E_CSSC_X.SCOK\\POL\\Container-ASiC-E_CSSC_X-8.asice";
            string path2 = "E:\\ahmet\\prj\\asic\\asic\\ASIC_PLUGTEST_FILES\\ASiC_plugtest_20121227_145940(birstunas)\\ASiC-E_CSSC_X.SCOK\\POL\\Container-ASiC-E_CSSC_X-7.asice";
            string path3 = "E:\\ahmet\\prj\\asic\\asic\\ASIC_PLUGTEST_FILES\\ASiC_plugtest_20121227_145940(birstunas)\\ASiC-E_CSSC_C.SCOK\\POL\\Container-ASiC-E_CSSC_C-6.asice";

            ASiCReader reader = new ASiCReader();
            PackageContents contents = reader.read(new FileInfo(path1), new Context());
            Console.WriteLine(contents);
            contents.write(new FileStream("T:\\api-asic\\testresources\\test.asic",FileMode.Create));

            contents = reader.read(new FileInfo(path3));
            Console.WriteLine(contents);

            contents = reader.read(new FileInfo(path1));
            Console.WriteLine(contents);

        }*/
    }
}