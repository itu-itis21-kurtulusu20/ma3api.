package test.esya.api.signature;

import org.junit.Before;
import test.esya.api.signature.settings.TestConfig;
import test.esya.api.signature.settings.TestSettings;
import test.esya.api.signature.settings.UGSettings;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.impl.ResultFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author ayetgin
 */
public class BaseTest
{
    ResultFormatter formatter = new ResultFormatter();
    protected TestSettings settings;
    protected TestConfig config;
    protected SignatureFormat signatureFormat;

    @Before
    public void init() throws Exception {
        signatureFormat = SignatureTestSuite.SIGNATURE_FORMAT;
        settings = new UGSettings();
        config = new TestConfig(signatureFormat,
                                SignatureTestSuite.ROOT+signatureFormat);

    }

    public void debugCVR(ContainerValidationResult cvr){
        System.out.println("--------------------------");
        System.out.println(cvr);
        /*
        System.out.println(cvr.getResultType());
        int index = 0;
        for (SignatureValidationResult svr : cvr.getAllResults().values()){
            index++;
            System.out.println("Signature "+index);
            System.out.println(svr.getResultType());
            debugDetails(svr.getDetails(), 1);
        } */
        System.out.println("--------------------------");
    }

    public void debugDetails(List<? extends ValidationResultDetail> details, int indent){
        if(details==null)
            return;

        String tab = "";
        for (int i=0; i<indent;i++){
            tab+= "\t";
        }
        for (ValidationResultDetail detail : details){
            System.out.println(tab+"class  : " + detail.getValidatorClass().getSimpleName());
            System.out.println(tab+"message: " + detail.getCheckMessage());
            System.out.println(tab+"result : " + detail.getCheckResult());
            debugDetails(detail.getDetails(), indent + 1);
        }
    }

    public Context createContext(){
        Context c = new Context(new File(SignatureTestSuite.ROOT).toURI());

        c.setData(settings.getContent()); // for CAdES
        return c;
    }


    public SignatureContainer readSignatureContainer(String fileName) throws Exception {
        return readSignatureContainer(fileName, createContext());
    }

    public void write(SignatureContainer sc, String fileName) throws Exception {
        FileOutputStream fis = new FileOutputStream(config.getPath(fileName));
        sc.write(fis);
        fis.close();
    }

    public SignatureContainer readSignatureContainer(String fileName, Context c) throws Exception {
        FileInputStream fis = new FileInputStream(config.getPath(fileName));
        SignatureContainer container = SignatureFactory.readContainer(fis, c);
        fis.close();
        return container;
    }

    public ContainerValidationResult validateSignature(String fileName) throws Exception {
        return validateSignature(fileName, createContext());
    }
    public ContainerValidationResult validateSignature(String fileName, Context c) throws Exception {

        FileInputStream fis = new FileInputStream(config.getPath(fileName));

        SignatureContainer container = SignatureFactory.readContainer(fis, c);

        fis.close();

        ContainerValidationResult cvr = container.verifyAll();
        debugCVR(cvr);

        return cvr;
    }
}
