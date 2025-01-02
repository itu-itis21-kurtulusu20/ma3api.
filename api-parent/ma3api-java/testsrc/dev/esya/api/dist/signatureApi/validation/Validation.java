package dev.esya.api.dist.signatureApi.validation;

import dev.esya.api.dist.signatureApi.SampleBase;
import tr.gov.tubitak.uekae.esya.api.signature.ContainerValidationResult;
import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureContainer;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureFactory;

import java.io.FileInputStream;

public class Validation extends SampleBase {

    public static ContainerValidationResult validateSignature(String fileName) throws Exception {
        return validateSignature(fileName, createContext());
    }

    public static ContainerValidationResult validateSignature(String fileName, Context c) throws Exception {

        FileInputStream fis = new FileInputStream(getPath(fileName));

        SignatureContainer container = SignatureFactory.readContainer(fis, c);

        fis.close();

        ContainerValidationResult cvr = container.verifyAll();
        debugCVR(cvr);

        return cvr;
    }

    public static void debugCVR(ContainerValidationResult cvr) {
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
}
