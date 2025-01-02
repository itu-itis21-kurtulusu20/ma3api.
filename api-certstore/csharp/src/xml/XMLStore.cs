using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Xml;
using System.Xml.Linq;
using System.Xml.XPath;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.xml
{
    public class XMLStore
    {
        [DllImport("shell32.dll", CharSet = CharSet.Auto)]
        private static extern int SHGetSpecialFolderPath(IntPtr hwndOwner, StringBuilder lpszPath, int nFolder, bool fCreate);
        private static readonly String DEFAULT_DEPO_DOSYA_ADI = "SertifikaDeposu.xml";
	    XmlNode rootNode;

	    public XMLStore(Stream stream)
	    {
		    init(stream);
	    }

        public XMLStore(String path)
        {
            init(path);
        }

	    public XMLStore()
	    {
            const int MAX_PATH = 260;
            StringBuilder userFolder = new StringBuilder(MAX_PATH);

            const int CSIDL_PROFILE = 40;

            SHGetSpecialFolderPath(IntPtr.Zero, userFolder, CSIDL_PROFILE, false);

            String filePath = userFolder.ToString() + 
                Path.DirectorySeparatorChar +
                CertStoreUtil.DEPO_DIZIN_ADI + 
                Path.DirectorySeparatorChar +
                DEFAULT_DEPO_DOSYA_ADI;
		    
            try
		    {
			    init(new FileStream(filePath, FileMode.Open, FileAccess.Read));
		    } 
		    catch (FileNotFoundException e) 
		    {
			    throw new ESYAException("Sertifika Deposu XML dosyası bulunamadı." ,e);
		    }
	    }

	    private void init(Stream stream)
	    {
		    try 
		    {
		        XmlReader reader = XmlReader.Create(stream);
		        XmlDocument doc = new XmlDocument();
                doc.Load(reader);

		        //rootNode = doc.FirstChild;
                rootNode = doc.ChildNodes.Item(1);

		    } 
		    /*catch (ParserConfigurationException e) 
		    {
			    throw new ESYAException("Sertifika deposu için XML dosyası çözümlenemedi." ,e);
		    } 
		    catch (SAXException e) 
		    {
			    throw new ESYAException("Sertifika deposu için XML dosyası çözümlenemedi." ,e);
		    }*/ 
		    catch (/*IO*/Exception e) 
		    {
			    throw new ESYAException("Sertifika deposu için Stream'den okunan XML dosyası çözümlenemedi." ,e);
		    }

	    }

        private void init(String path)
        {
            try
            {
                XmlReader reader = XmlReader.Create(path);
                XmlDocument doc = new XmlDocument();
                doc.Load(reader);
                rootNode = doc.ChildNodes.Item(1);
            }
            catch (Exception e)
            {
                throw new ESYAException("Sertifika deposu için URI'dan okunan XML dosyası çözümlenemedi.", e);
            }
        }

	    public List<DepoKokSertifika> getTrustedCertificates()
	    {
		    List<DepoKokSertifika> depoKokCertList = new List<DepoKokSertifika>();
		    try
		    {
                XmlNodeList kokSertifikaNodeList = rootNode.SelectNodes("/depo/koksertifikalar/koksertifika");

			    for(int i=0; i < kokSertifikaNodeList.Count; i++)
			    {
				    DepoKokSertifika depoKokCert = new DepoKokSertifika();
				    XmlNode node = kokSertifikaNodeList.Item(i);
				    if(node.NodeType == XmlNodeType.Element)
				    {
					    XmlElement element = (XmlElement)node;
					    XmlStoreUtil.fillObjectFromXml(depoKokCert, element);
					    depoKokCertList.Add(depoKokCert);
				    }
			    }
		    }
		    catch(Exception ex)
		    {
			    throw new ESYAException(ex);
		    }

		    return depoKokCertList;
	    }


	    public List<ECertificate> getCertificates()
	    {
		    List<ECertificate> certList = new List<ECertificate>();
		    try
		    {
                XmlNodeList sertifikaNodeList = rootNode.SelectNodes("/depo/sertifikalar/sertifika");

			    for(int i=0; i < sertifikaNodeList.Count; i++)
			    {
				    XmlDepoSertifika depoXmlCert = new XmlDepoSertifika();
				    XmlNode node = sertifikaNodeList.Item(i);
				    if(node.NodeType == XmlNodeType.Element)
				    {
					    XmlElement element = (XmlElement)node;
					    XmlStoreUtil.fillObjectFromXml(depoXmlCert, element);
					    certList.Add(new ECertificate(depoXmlCert.mValue));
				    }
			    }
		    }
		    catch(Exception ex)
		    {
			    throw new ESYAException(ex);
		    }

		    return certList;
	    }


	    public static void main(String[] args)
	    {
		    XMLStore store = new XMLStore(new FileStream("C:\\file.xml", FileMode.Open, FileAccess.Read));
		    List<DepoKokSertifika> trustedCertificates = store.getTrustedCertificates();
		    Console.WriteLine(trustedCertificates.Count);
	    }
    }
}
