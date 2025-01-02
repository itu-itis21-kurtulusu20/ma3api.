using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using ICSharpCode.SharpZipLib.Zip;
using tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace tr.gov.tubitak.uekae.esya.api.asic.model
{
    /**
     * @author yavuz.kahveci
     */
    public class PackageContents
    {
        private string mimetype;
        private List<Signable> datas = new List<Signable>(1);

        // in META-INF
        private List<SignatureContainerEntryImpl> containers = new List<SignatureContainerEntryImpl>(1);
        private List<ASiCManifest> asicManifests = new List<ASiCManifest>(0);  // for CAdES or TST

        // other standarts
        private ContainerInfo containerInfo; // OCF
        private Manifest manifest;           // ODF
        private OCFMetadata metadata;        // OCF

        private FileInfo inputFile;
        private ZipFile zipFile;

        public FileInfo getInputFile()
        {
            return inputFile;
        }

        public void setInputFile(FileInfo inputFile)
        {
            this.inputFile = inputFile;
        }

        public ZipFile getZipFile()
        {
            return zipFile;
        }

        public void setZipFile(ZipFile zipFile)
        {
            this.zipFile = zipFile;
        }

        public string getMimetype()
        {
            return mimetype;
        }

        public void setMimetype(string aMimetype)
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

        public void write(Stream stream)
        {
            try { 
                ZipOutputStream zos = new ZipOutputStream(stream);
                zos.UseZip64 = UseZip64.Off;
                if (mimetype!=null) {
                    zos.SetLevel(0/*ZipEntry.STORED*/);
                    write(zos, "mimetype", Encoding.UTF8.GetBytes(mimetype));
                    zos.SetLevel(8/*ZipEntry.DEFLATED*/);

                }
                if (datas!=null){
                    foreach (Signable s in datas)
                    {
                        //s.closeContentStream();
                        write(zos, s.getURI(), s.getContent());
                    }
                }
                if (asicManifests!=null){
                    foreach (ASiCManifest m in asicManifests){
                        write(zos, m);
                    }
                }
                if (containers !=null){
                    foreach (SignatureContainerEntryImpl s in containers){
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
                zos.Close();
            }
            catch (Exception x){
                throw new SignatureException(x);
            }
        }

        private void write(ZipOutputStream stream, String entryName, byte[] content)
        {
            stream.PutNextEntry(new ZipEntry(entryName));
            stream.Write(content,0,content.Length);
            stream.CloseEntry();
        }

        private void write(ZipOutputStream stream, String entryName, Stream content)
        {
            stream.PutNextEntry(new ZipEntry(entryName));
            StreamUtil.copy(content, stream);
            stream.CloseEntry();
        }

        private void write(ZipOutputStream stream, ASiCDocument document)
        {
            stream.PutNextEntry(new ZipEntry(document.getASiCDocumentName()));
            document.write(stream);
            stream.CloseEntry();
        }

        public override string ToString()
        {
            StringBuilder buffer = new StringBuilder();
            buffer.Append("mimetype: ").Append(mimetype).Append("\n");
            foreach (Signable s in datas){
                buffer.Append("data: ").Append(s.getURI()).Append("\n");
            }
            foreach (SignatureContainerEntryImpl s in containers){
                buffer.Append("signature: ").Append(s.getASiCDocumentName()).Append("\n");
            }
            foreach (ASiCManifest m in asicManifests){
                buffer.Append("asic manifest: ").Append(m.getASiCDocumentName()).Append("\n");
            }
            buffer.Append("manifest.xml : ").Append(manifest!=null).Append("\n");
            buffer.Append("metadata.xml : ").Append(metadata!=null).Append("\n");
            buffer.Append("container.xml: ").Append(containerInfo!=null).Append("\n\n");

            return buffer.ToString();
        }
    }
}