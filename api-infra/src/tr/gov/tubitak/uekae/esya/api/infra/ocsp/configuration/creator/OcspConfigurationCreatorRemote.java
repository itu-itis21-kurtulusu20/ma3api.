package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.creator;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element.*;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.reader.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/20/14
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class OcspConfigurationCreatorRemote extends AbstractOcspConfigurationCreator {
	public static String encrCert = "kyshs_cisdup_enc.cer";
	public static String caCert = "KYSHS-ser-SURUM1v2.cer";

	public OcspConfigurationCreatorRemote() throws Exception {
		super();
	}





	public static void main(String[] arg) throws Exception{
		File certFile = new File(System.getProperty("user.dir") + File.separator + encrCert);
		FileInputStream  stream = new FileInputStream(certFile);
		final ECertificate certificate = new ECertificate(stream);
		final List<ECertificate> certificates = new ArrayList<ECertificate>();
		certificates.add(certificate);
		final List<String> preferredSignAlgs = new ArrayList<String>();
		preferredSignAlgs.add("RSA-SHA224");
		preferredSignAlgs.add("RSA-SHA256");
		preferredSignAlgs.add("RSA-SHA384");
		preferredSignAlgs.add("RSA-SHA512");

		final ISmartCardConfigElement signer = new SignerConfigElement("abc", 15, "UTIMACO", "123456", 7);
		final ISmartCardConfigElement signer2 = new SignerConfigElement("abc2", 11, "UTIMACO", "123456789", 10);
		final List<IOcspConfigElement> signerElements = new ArrayList<IOcspConfigElement>();
		signerElements.add(signer);
		signerElements.add(signer2);


		IStatusProviderConfigElement element = new DbProviderConfigElement("uname1", "pass1", "view1", true, "123456", null, true, true, false, false, 10, true);
		IStatusProviderConfigElement element2 = new DbProviderConfigElement("uname2", "pass2", "view2", true, "1234567", null, true, true, false, false, 10, true);
		IStatusProviderConfigElement element3 = new DbProviderConfigElement("uname3", "pass3", "view3", true, "123456789", null, true, true, false, false, 10, true);
		IStatusProviderConfigElement element4 = new CrlProviderConfigElement("file path", 200);

		final List<IOcspConfigElement> responsibleCaElements = new ArrayList<IOcspConfigElement>();
		responsibleCaElements.add(element);
		responsibleCaElements.add(element2);
		responsibleCaElements.add(element3);
		responsibleCaElements.add(element4);


		File certFile2 = new File(System.getProperty("user.dir") + File.separator + caCert);
		FileInputStream  stream2 = new FileInputStream(certFile2);
		ECertificate certificate2 = new ECertificate(stream2);

		IOcspConfigElement responsibleCaElement = new ResponsibleCaElement(certificate2, "ca1", responsibleCaElements);
		final List<IOcspConfigElement> responsibleCAs = new ArrayList<IOcspConfigElement>();
		responsibleCAs.add(responsibleCaElement);

		NonceControlConfigElement nonceControlConfigElement = new NonceControlConfigElement(false, 24);
		DecryptorConfigElement decryptorConfigElement = new DecryptorConfigElement("avc", 2, "cs2_pkcs11");

		OcspConfigurationCreatorLocal ocspConfigurationRemote = new OcspConfigurationCreatorLocal();
		ocspConfigurationRemote.addDecryptor(decryptorConfigElement);
		ocspConfigurationRemote.createConfiguration(new IOcspConfigHolder() {
			public List<ECertificate> getSigningCertificates() {
				return certificates;  //To change body of implemented methods use File | Settings | File Templates.
			}

			public String getUrl() {
				return "http://localhost:8080/OcspInit";  //To change body of implemented methods use File | Settings | File Templates.
			}

			public String getDefaultSigningAlgorithm() {
				return "RSA-SHA1";  //To change body of implemented methods use File | Settings | File Templates.
			}

			public List<String> getSupportedSigningAlgorithms() {
				return preferredSignAlgs;  //To change body of implemented methods use File | Settings | File Templates.
			}

			public List<IOcspConfigElement> getSigners() {
				return signerElements;  //To change body of implemented methods use File | Settings | File Templates.
			}

			public List<IOcspConfigElement> getResponsibleCas() {
				return responsibleCAs;  //To change body of implemented methods use File | Settings | File Templates.
			}

			public ECertificate getEncryptionCertificate() {
				return certificate;  //To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
			public IOcspConfigElement getNonceControl() {
				return null;
			}
		});
		//ocspConfigurationRemote.send("http://localhost:8080/OcspInit");

		/*TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		File file = new File(System.getProperty("user.dir") + File.separator + "example.xml");
		StreamResult result = new StreamResult(file.toURI().getPath());
		DOMSource source = new DOMSource(ocspConfigurationRemote.doc);
		transformer.transform(source, result);*/
		//ocspConfigurationRemote.send("http://localhost:8080");
		ocspConfigurationRemote.saveToFile(new File(System.getProperty("user.dir") + File.separator + "example.xml"));

		InputStream streamRead = new FileInputStream(new File(System.getProperty("user.dir") + File.separator + "example.xml"));
		OcspConfigurationReaderLocal reader = new OcspConfigurationReaderLocal();
		reader.read(streamRead);
		System.out.println(reader.getOcspEncCertificate().getSubject().toShortTitle());
		for (String s: reader.getSupportedSignAlg())
			System.out.println(s);
		System.out.println(reader.getPreferredSignAlg());
		int i= 0;
		for (IOcspConfigElement elements : reader.getSigners()) {
			System.out.println("Signer # "+ i+" Key Label:"+((ISmartCardConfigElement)elements).getKeyLabel());
			System.out.println("Signer # "+ i+" Card Type:"+((ISmartCardConfigElement)elements).getCardType());
			System.out.println("Signer # "+ i+" Slot No:"+((ISmartCardConfigElement)elements).getSlotNo());
			System.out.println("Signer # "+ i+" Session Pool:"+((ISmartCardConfigElement)elements).getSessionPool());
			System.out.println("Signer # "+ i+" Pin:"+((ISmartCardConfigElement)elements).getPin());
			i++;
		}

		IOcspConfigElement decryptorConfigElement1 = reader.getDecryptor();
		System.out.println(((DecryptorConfigElement)decryptorConfigElement1).getKeyLabel());
		System.out.println(((DecryptorConfigElement)decryptorConfigElement1).getSlotNo());
		System.out.println(((DecryptorConfigElement)decryptorConfigElement1).getCardType());

		int k=0,l=0;
		for (IOcspConfigElement responsibleCA : reader.getResponsibleCas()) {
			System.out.println(((IResponsibleCaElement)responsibleCA).getCaCertificate().getIssuer().stringValue());
			System.out.println(((IResponsibleCaElement)responsibleCA).getName());
			for (IOcspConfigElement configElement : ((IResponsibleCaElement) responsibleCA).getStatusProviders()) {
				if (configElement instanceof DbProviderConfigElement){
					System.out.println("Db Provider # "+k+ "Username:"+((DbProviderConfigElement)configElement).getUserName());
					System.out.println("Db Provider # "+k+" Password:"+((DbProviderConfigElement)configElement).getPassword());
					System.out.println("Db Provider # "+k+" ViewName:"+((DbProviderConfigElement)configElement).getViewName());
					System.out.println("Db Provider # "+k+" HmacKey:"+((DbProviderConfigElement)configElement).getHmacKey());
					System.out.println("Db Provider # "+k+" OcspSignCheck:"+((DbProviderConfigElement)configElement).ocspSignCheck());
					k++;
				}

				if (configElement instanceof CrlProviderConfigElement){
					System.out.println("Crl Provider #"+k+ "Crl Address Path:"+((CrlProviderConfigElement)configElement).getCrlAddressPath());
					System.out.println("Crl Provider #"+k+ "Period:"+((CrlProviderConfigElement)configElement).getPeriod());
					l++;
				}
			}
		}

		int ind=0;
		for (ECertificate eCertificate : reader.getOcspSignCertificates()) {
			System.out.println("OCSP Sign Cert #"+ind + " Subject Name:" + eCertificate.getSubject().stringValue());
			ind++;
		}


		System.out.println("Url:"+reader.getUrl());

	}
}
