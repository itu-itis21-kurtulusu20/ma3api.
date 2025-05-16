package tr.gov.tubitak.uekae.esya.api.MobileSignatureOnlyServer;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableFile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.IAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.SigningTimeAttr;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.EUserException;
import tr.gov.tubitak.uekae.esya.api.common.util.FileUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil;

import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;

import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.*;
import tr.gov.tubitak.uekae.esya.api.infra.mobile.MultiMobileSigner.MultiMobileSignerForOne;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContainer;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContext;
import tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESSignature;
import tr.gov.tubitak.uekae.esya.api.signature.Signature;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.signature.config.Config;
import tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper.MSSParams;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;

@SpringBootApplication
public class MobileSignatureOnlyServerApplication {


	 static Logger logger = LoggerFactory.getLogger(MobileSignatureOnlyServerApplication.class);

	// timeout value used by org.apache.axis.AxisFault; is set a higher value than operator timeout to avoid overlap
	// todo the "0" value did not result in a timeout; check
	static final int CONNECTION_TIMEOUT_MS = 0;

	// this is used in MSS_SignatureReqType.equals() and hashCode()
	// todo remove if unnecessary
	static final int MSSP_REQUEST_TIMEOUT = 20;

	// timeout values for multi-signing (used as thread timeout parameter)
	static final int MOBILE_SIGNATURE_TIMEOUT_MS = 60000; // base timeout
	static final int TIMEOUT_PER_SIGNATURE_MS = 20000;

	 static String rootFolder = "C:/ma3api-java-bundle/";

	static String testDataFolder = rootFolder + "testdata/";
	static String pdfFile = testDataFolder + "sample.pdf";
	static String xmlFile = testDataFolder + "sample";
	static String file1Path = testDataFolder + "hello1.txt"; // document which will be signed
	 static String file2Path = testDataFolder + "hello2.txt"; // document which will be signed
	 static String file3Path = testDataFolder + "hello3.txt"; // document which will be signed


	static String licenseFilePath = rootFolder + "lisans/lisans.xml";
	static EMobileSignerConnector emsspClientConnector;
	static MobileSigner mobileSigner = null;

	@PostConstruct
	public void init() {

		loadLicense();

		try {
			String phoneNumber= "05318230547";
			Operator operator = Operator.TURKCELL;

			PhoneNumberAndOperator phoneNumberAndOperator = new PhoneNumberAndOperator(phoneNumber, operator);

			MSSParams mobilParams = getMobileParams(operator);
			mobilParams.setMsspRequestTimeout(MSSP_REQUEST_TIMEOUT); // todo remove if unnecessary
			mobilParams.setConnectionTimeoutMs(CONNECTION_TIMEOUT_MS);

			emsspClientConnector = new EMobileSignerConnector(mobilParams);

			// get signer certificate necessary field for signing from operator
			emsspClientConnector.setCertificateInitials(phoneNumberAndOperator);

			mobileSigner = new MobileSigner(emsspClientConnector, phoneNumberAndOperator, null, "rapor", SignatureAlg.RSA_SHA256.getName(), null);
		} catch (Exception ex) {
			logger.error("Error in initialization:", ex);
		}
	}

	private MSSParams getMobileParams(Operator operator) throws ESYAException {

		MSSParams mobileParams = null;
		if(operator == Operator.TURKCELL){
			mobileParams = new MSSParams("http://MImzaTubitak", "IITHC30G", "www.turkcelltech.com");
            mobileParams.setMsspSignatureQueryUrl("https://msign.turkcell.com.tr/MSSP2/services/MSS_Signature");
            mobileParams.setMsspProfileQueryUrl("https://msign.turkcell.com.tr/MSSP2/services/MSS_ProfileQueryPort");
		} else if (operator == Operator.TURKTELEKOM){
			mobileParams = new MSSParams("Tubitak_KamuSM", "*****", "");
			mobileParams.setMsspSignatureQueryUrl("https://mobilimza.turktelekom.com.tr/EGAMsspWSAP2/MSS_SignatureService");
			mobileParams.setMsspProfileQueryUrl("https://mobilimza.turktelekom.com.tr/EGAMsspWSAP2/MSS_ProfileQueryService");
		} else if(operator == Operator.VODAFONE){
			mobileParams = new MSSParams("tbtkbilgem", "******", "mobilimza.vodafone.com.tr");
			mobileParams.setMsspSignatureQueryUrl("https://mobilimza.vodafone.com.tr:443/Dianta2/MSS_SignatureService");
		} else {
			throw new ESYAException("Unknown mobile operator!");
		}

		return mobileParams;
	}

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(MobileSignatureOnlyServerApplication.class, args);

		try {

			//creating single signature
			createSingleCAdES(mobileSigner);
			//createSingleCAdESInTwoStep(mobileSigner);

			//createSingleXAdES(mobileSigner);

			//createSinglePAdES(mobileSigner);
			//createSinglePAdESInTwoStep(mobileSigner);

			//creating multiple signature
			//multi-signature is only supported by Turkcell operator
			//createMultipleSigning(mobileSigner);

		} catch (Exception ex) {
			logger.error("Error in TestMobileSignClient:", ex);
		}finally {
			context.close();
		}
	}

	private static void createSingleXAdES(MobileSigner mobileSigner) throws SignatureException, IOException {

		mobileSigner.getFingerPrintInfo().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object fingerPrintObj) {
				String fingerPrint = (String) fingerPrintObj;
				System.out.println("Tekil imza fingerprint: " + fingerPrint);
			}
		});

		Context context = new Context(new File(testDataFolder).toURI());
		context.setConfig(new tr.gov.tubitak.uekae.esya.api.xmlsignature.config.Config(rootFolder + "config/xmlsignature-config.xml"));
		context.setValidateCertificateBeforeSign(false);

		XMLSignature signature = new XMLSignature(context);
		signature.addDocument(file1Path, "text/plain", true);
		signature.setSigningTime(Calendar.getInstance());
		signature.sign(mobileSigner);

		String signedXmlFileName = FileUtil.getNameWithoutExtension(xmlFile) + "_signed.xml";
		FileOutputStream fileOutputStream = new FileOutputStream(signedXmlFileName);
		signature.write(fileOutputStream);


	}

	private static void createSinglePAdES(MobileSigner mobileSigner) throws SignatureException, FileNotFoundException {
		PAdESContext context = new PAdESContext(new File(testDataFolder).toURI());
		context.setConfig(new Config(rootFolder + "config/esya-signature-config.xml"));

		context.getConfig().getCertificateValidationConfig().setValidateCertificateBeforeSigning(false);

		SignatureContainer signatureContainer = new PAdESContainer();
		signatureContainer.setContext(context);

		FileInputStream fileInputStream = new FileInputStream(pdfFile);
		signatureContainer.read(fileInputStream);


		String signedPdfFileName = FileUtil.getNameWithoutExtension(pdfFile) + "_signed.pdf";
		FileOutputStream fileOutputStream = new FileOutputStream(signedPdfFileName);


		// add signature
		Signature signature = signatureContainer.createSignature(null);
		signature.setSigningTime(Calendar.getInstance());
		signature.sign(mobileSigner);
		signatureContainer.write(fileOutputStream);
	}

	private static void createSinglePAdESInTwoStep(MobileSigner mobileSigner) throws Exception {
		PAdESContext context = new PAdESContext(new File(testDataFolder).toURI());
		context.setConfig(new Config(rootFolder + "config/esya-signature-config.xml"));

		context.getConfig().getCertificateValidationConfig().setValidateCertificateBeforeSigning(false);

		SignatureContainer signatureContainer = new PAdESContainer();
		signatureContainer.setContext(context);

		FileInputStream fileInputStream = new FileInputStream(pdfFile);
		signatureContainer.read(fileInputStream);


		String signedPdfFileName = FileUtil.getNameWithoutExtension(pdfFile) + "_signed.pdf";
		FileOutputStream fileOutputStream = new FileOutputStream(signedPdfFileName);


		// add signature
		Signature signature = signatureContainer.createSignature(null);
		signature.setSigningTime(Calendar.getInstance());

		byte[] dtbs = ((PAdESSignature)signature).initSignForMobile(mobileSigner);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		signatureContainer.write(bos);
		bos.close();

		String calculateFingerPrint = calculateFingerPrint(mobileSigner.getDigestAlg(), dtbs);

		byte[] signatureValue = mobileSigner.sign(dtbs);

		byte[] unfinishedBytes = bos.toByteArray();
		finishPAdESSigning(unfinishedBytes, signatureValue, mobileSigner.getSigningCert(), fileOutputStream, context);
	}

	private static void finishPAdESSigning(byte[] unfinishedBytes, byte[] signature, ECertificate eCertificate, FileOutputStream fileOutputStream, PAdESContext context) throws Exception {
		FileUtil.writeBytes("C:\\pades\\unfinished-1.pdf", unfinishedBytes);

		PAdESContainer signatureContainer = new PAdESContainer();
		signatureContainer.setContext(context);

		ByteArrayInputStream bis = new ByteArrayInputStream(unfinishedBytes);
		signatureContainer.read(bis);
		bis.close();

		signatureContainer.finishAddingSignerForMobile(signature, eCertificate);

		signatureContainer.write(fileOutputStream);
	}

	public static String getLicenseFilePath() throws ESYAException
	{
		return licenseFilePath;
	}

	private static void loadLicense() {

		FileInputStream fis;
		try {
			// write license path below
			fis = new FileInputStream(getLicenseFilePath());
			LicenseUtil.setLicenseXml(fis);
			fis.close();
		} catch (Exception exc) {
			logger.error("Error int TestSingleServerSideSign:", exc);
		}
	}

	private static final Map<Integer, Operator> intToTypeMap = new HashMap<Integer, Operator>();
	static {
		for (Operator type : Operator.values()) {
			intToTypeMap.put(type.ordinal(), type);
		}
	}

	private static Operator fromInt(int i) {
		Operator type = intToTypeMap.get(Integer.valueOf(i));
		if (type == null)
			return Operator.TURKCELL;
		return type;
	}

	public static void createSingleCAdES(MobileSigner mobileSigner) throws ESYAException {
		try {

			mobileSigner.getFingerPrintInfo().addObserver(new Observer() {
				@Override
				public void update(Observable o, Object fingerPrintObj) {
					String fingerPrint = (String) fingerPrintObj;
					System.out.println("Tekil imza fingerprint: " + fingerPrint);
				}
			});

			SignatureResult result = signCAdES(file1Path, mobileSigner);

			if(result.isExceptionOccured() == true) {
       		    System.out.println("Exception occured for: " + result.getInformativeText());
       		    result.getException().printStackTrace();
          	} else
          		System.out.println("Signature created for: " + result.getInformativeText());

		}catch (Exception ex) {
			String errorString = "Error in creating single signature";
			logger.error(errorString, ex);
			throw new ESYAException(errorString, ex);
		}
	}

	public static void createMultipleSigning(MobileSigner mobileSigner) throws ESYAException {
		if(!emsspClientConnector.isMultipleSignSupported()){
			throw new ESYAException("Multiple sign not supported");
		}

		try {

			String[] filesToBeSigned = {file1Path, file2Path, file3Path};
			String[] informativeTexts = {"rapor1", "rapor2", "rapor3"};
			int signatureCount = filesToBeSigned.length;


			MultiMobileSigner multiMobileSigner = new MultiMobileSigner(mobileSigner, signatureCount);

			ExecutorService executorService = Executors.newCachedThreadPool();

			final int TOTAL_TIMEOUT_MS = MOBILE_SIGNATURE_TIMEOUT_MS + TIMEOUT_PER_SIGNATURE_MS * multiMobileSigner.getSignatureCount();
			logger.debug("Total timeout: {} ms", TOTAL_TIMEOUT_MS);

			Observer multiSignFingerPrintObserver = new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					FingerPrintInfo fingerPrintInfo = (FingerPrintInfo) o;
					System.out.println("Fingerprint for " + fingerPrintInfo.getMobileSigner().getInformativeText() + " : " + fingerPrintInfo.getFingerPrint());
				}
			};

			MultiMobileSignerForOne signer0 =  new MultiMobileSignerForOne(multiMobileSigner, informativeTexts[0], 0);
			MultiMobileSignerForOne signer1 =  new MultiMobileSignerForOne(multiMobileSigner, informativeTexts[1], 1);
			MultiMobileSignerForOne signer2 =  new MultiMobileSignerForOne(multiMobileSigner, informativeTexts[2], 2);

			signer0.getFingerPrintInfo().addObserver(multiSignFingerPrintObserver);
			signer1.getFingerPrintInfo().addObserver(multiSignFingerPrintObserver);
			signer2.getFingerPrintInfo().addObserver(multiSignFingerPrintObserver);

			List<Callable<SignatureResult>> callables = Arrays.asList(
					() -> signCAdES(filesToBeSigned[0], signer0),
					() -> signCAdES(filesToBeSigned[1], signer1),
					() -> signCAdES(filesToBeSigned[2], signer2)
			);

			List<SignatureResult> results = executorService.invokeAll(callables, TOTAL_TIMEOUT_MS, TimeUnit.MILLISECONDS).stream().map(future ->
			{
				try {
					return future.get();

				}catch(CancellationException ex) {
					System.out.println("Timeout occured");
					throw ex;
				}
				catch(Exception ex) {
					System.out.println("Future get: " + ex.toString());
					ex.printStackTrace();
					throw new IllegalStateException(ex);
				}
			}).collect(Collectors.toList());


			for(SignatureResult aResult : results)
			{
				if(aResult.isExceptionOccured() == true) {
					System.out.println("Exception occured for: " + aResult.getInformativeText());
					aResult.getException().printStackTrace();
				}
			}

			try {
				executorService.shutdown();
				executorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
			}catch(InterruptedException ex) {
				throw new InterruptedException("Tasks interrupted");
			}finally{
				if(!executorService.isTerminated()) {
					System.err.println("Cancel non finish tasks");
				}
				executorService.shutdownNow();
			}

		}catch (Exception ex) {
			// throw EUserException beforehand to make it more visible
			if (ex instanceof EUserException) {
				throw (EUserException) ex;
			}

			String errorString = "Error in creating multiple signature";
			logger.error(errorString, ex);
			throw new ESYAException(errorString, ex);
		}
	}

	public static SignatureResult signCAdES(String filePath, IMobileSigner signer) throws Exception
    {
		try {

	        BaseSignedData bs = new BaseSignedData();
			bs.addContent(new SignableFile(new File(filePath)), true);

			ValidationPolicy validationPolicy = PolicyReader.readValidationPolicy(new FileInputStream( rootFolder + "config/certval-policy.xml"));
			HashMap<String, Object> params = new HashMap<String, Object>();

			//In real system, validate certificate by giving parameter "true" instead of "false"
	        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);
	        params.put(EParameters.P_CERT_VALIDATION_POLICY, validationPolicy);

	        //Since SigningTime attribute is optional, add it to optional attributes list
	     	java.util.List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
	     	optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));

	        bs.addSigner(ESignatureType.TYPE_BES, null, signer, optionalAttributes, params);

	        FileUtil.writeBytes(filePath + ".p7s", bs.getEncoded());

	   }
	    catch (Exception ex) {

			   return new SignatureResult(ex, true, signer.getInformativeText());
		}

		return new SignatureResult(null, false, signer.getInformativeText());
    }

	public static SignatureResult signDataXLong(String filePath, IMobileSigner signer) {
		try {
			BaseSignedData bs = new BaseSignedData();
			bs.addContent(new SignableFile(new File(filePath)), true);

			ValidationPolicy validationPolicy = PolicyReader.readValidationPolicy(new FileInputStream(rootFolder + "config/certval-policy.xml"));
			HashMap<String, Object> params = new HashMap<String, Object>();

			//In real system, validate certificate by giving parameter "true" instead of "false"
			params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);
			params.put(EParameters.P_CERT_VALIDATION_POLICY, validationPolicy);
			params.put(EParameters.P_TSS_INFO, new TSSettings("******", 0, "******", DigestAlg.SHA256));

			//Since SigningTime attribute is optional, add it to optional attributes list
			java.util.List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
			optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));

			bs.addSigner(ESignatureType.TYPE_ESXLong, null, signer, optionalAttributes, params);

			FileUtil.writeBytes(filePath + ".p7s", bs.getEncoded());
		} catch (Exception ex) {

			return new SignatureResult(ex, true, signer.getInformativeText());
		}

		return new SignatureResult(null, false, signer.getInformativeText());
    }

	public static void createSingleCAdESInTwoStep(MobileSigner mobileSigner) throws ESYAException {
		try {
			SignatureResult result = signCAdESInTwoStep(file1Path, mobileSigner);

			if (result.isExceptionOccured() == true) {
				System.out.println("Exception occured for: " + result.getInformativeText());
				result.getException().printStackTrace();
			} else
				System.out.println("Signature created for: " + result.getInformativeText());

		} catch (Exception ex) {
			String errorString = "Error in creating single signature";
			logger.error(errorString, ex);
			throw new ESYAException(errorString, ex);
		}
	}

	public static SignatureResult signCAdESInTwoStep(String filePath, MobileSigner mobileSigner) throws Exception {
		try {

			BaseSignedData bs = new BaseSignedData();
			bs.addContent(new SignableFile(new File(filePath)), true);

			ValidationPolicy validationPolicy = PolicyReader.readValidationPolicy(new FileInputStream(rootFolder + "config/certval-policy.xml"));
			HashMap<String, Object> params = new HashMap<String, Object>();

			//In real system, validate certificate by giving parameter "true" instead of "false"
			params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);
			params.put(EParameters.P_CERT_VALIDATION_POLICY, validationPolicy);

			//Since SigningTime attribute is optional, add it to optional attributes list
			java.util.List<IAttribute> optionalAttributes = new ArrayList<IAttribute>();
			optionalAttributes.add(new SigningTimeAttr(Calendar.getInstance()));

			byte[] dtbs = bs.initAddingSignerForMobile(ESignatureType.TYPE_BES, mobileSigner, optionalAttributes, params);

			String calculateFingerPrint = calculateFingerPrint(mobileSigner.getDigestAlg(), dtbs);

			byte[] signature = mobileSigner.sign(dtbs);

			byte[] bsdBytes = bs.getEncoded();

			byte[] encoded = finishCAdESSigning(bsdBytes, signature, mobileSigner.getSigningCert());

			FileUtil.writeBytes(filePath + ".p7s", encoded);

		} catch (Exception ex) {

			return new SignatureResult(ex, true, mobileSigner.getInformativeText());
		}

		return new SignatureResult(null, false, mobileSigner.getInformativeText());
	}

	private static byte[] finishCAdESSigning(byte[] bsdBytes, byte[] signature, ECertificate eCertificate) throws CMSSignatureException, ESYAException {
		BaseSignedData bs = new BaseSignedData(bsdBytes);
		bs.finishAddingSignerForMobile(signature, eCertificate);
		return bs.getEncoded();
	}

	protected static String calculateFingerPrint(DigestAlg digestAlg, byte[] dataToBeSigned) throws CryptoException {
		byte[] fingerPrintBytes = DigestUtil.digest(digestAlg, dataToBeSigned);
		String fingerPrint = StringUtil.toString(fingerPrintBytes);
		return fingerPrint;
	}
}
