package dev.esya.api.xmlsignature.legacy.plugtests2010;

import java.io.File;
import java.io.FileOutputStream;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.CompleteRevocationRefs;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.RevocationValues;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

public class AutomatedValidationTest extends PT2010BaseTest {
	
	public void testFile(File aFile) {
		try {
			if (aFile.isDirectory()){
				File[] files = aFile.listFiles();
				for (File file : files)
					testFile(file);
			}
			else {
				String fileName = aFile.getName();
				if (isSignatureToTest(fileName)){

					String baseURI = aFile.getParent();
					
					System.out.println(baseURI);
					System.out.println(fileName);
					
			        FileDocument d = new FileDocument(aFile, MIMETYPE_XML, null);

			        Context c = new Context(baseURI);

			        //todo
			        c.setValidateCertificates(true);

			        String output = null;
			        
			        try {
				        XMLSignature signature = XMLSignature.parse(d, c);

				        boolean crlsUsed = false;
				        UnsignedSignatureProperties usp = signature.getQualifyingProperties().getUnsignedSignatureProperties();
				        if (usp!=null){
				        	CompleteRevocationRefs refs = usp.getCompleteRevocationRefs();
				        	if (refs!=null && refs.getCRLReferenceCount()>0){
				        		crlsUsed = true;
				        	}
				        	
				        	RevocationValues vals = usp.getRevocationValues();
				        	if (vals!=null && vals.getCRLValueCount()>0){
				        		crlsUsed = true;
				        	}
				        }
				        
				        if (crlsUsed)
				            c.getConfig().getValidationConfig().setCertValidationPolicies(CRL_POLICIES);
				        else
				            c.getConfig().getValidationConfig().setCertValidationPolicies(OCSP_POLICIES);
				        	
				        
				        ValidationResult verified = signature.verify();
				        output = verified.toXml();
				        
				        System.out.println("--validation result---------");
				        System.out.println(verified.getType());
				        System.out.println("----------------------------");

				        System.out.println(""+output);
			        }
			        catch (Throwable x) {
						x.printStackTrace();
						output = "<Failed>\n"+XmlUtil.escapeXMLData(x.getMessage())+"\n";
						while (x.getCause()!=null){
							x = x.getCause();
							output+=XmlUtil.escapeXMLData(x.getMessage());
						}
						output +="</Failed>";
					}
			        
			        FileOutputStream fos = new FileOutputStream(outputFileName(baseURI, fileName));
			        fos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes("UTF-8"));
			        fos.write(output.getBytes("UTF-8"));
			        fos.close();

			        
			        
					//validate(aFile.getName(), aFile.getParent(), null, true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean isSignatureToTest(String aFileName){
		return (aFileName.startsWith("Signature-X-") || aFileName.startsWith("Signature-X141-")) && aFileName.endsWith(".xml");
	}
	
	private String contextDir(String parentPath, String fileName){
        String[] splitted = parentPath.split("\\\\");
        String parentDir = splitted[splitted.length-1];
        String testDir = splitted[splitted.length-2];
        String refsDir = parentDir;
        if (fileName.lastIndexOf("+")>0)
        	refsDir = fileName.substring(fileName.lastIndexOf("+")+1, fileName.lastIndexOf("."));
        return BASELOC+testDir+"\\"+refsDir+"\\";
	}
	
	private String outputFileName(String parentPath, String fileName){
        String[] splitted = parentPath.split("\\\\");
        String parentDir = splitted[splitted.length-1];
        String testDir = splitted[splitted.length-2];
        return BASELOC+testDir+"\\TU\\Verification_of_"+parentDir+"_"+fileName;
	}
	
	public void test() {
		testFile(new File("T:\\api-xmlsignature\\testdata\\pt2010\\XAdES-T.SCOK\\"));
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AutomatedValidationTest avt = new AutomatedValidationTest();
		avt.test();
	}

}
