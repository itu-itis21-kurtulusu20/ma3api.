package tr.gov.tubitak.uekae.esya.api.signature.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.SignaturePackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for SignatureContainer implementations
 *
 * @author ayetgin
 */
public abstract class AbstractSignatureContainer implements SignatureContainerEx
{
    private static Logger logger = LoggerFactory.getLogger(AbstractSignatureContainer.class);

    protected List<Signature> rootSignatures = new ArrayList<Signature>(1);
    protected Context context;
    protected SignaturePackage signaturePackage;


    public void setContext(Context context)
    {
        this.context = context;
    }

    public Context getContext()
    {
        return context;
    }

    public void addExternalSignature(Signature signature) throws SignatureException
    {
        rootSignatures.add(signature);
    }

    public List<Signature> getSignatures()
    {
        return rootSignatures;
    }

    public void setSignaturePackage(SignaturePackage signaturePackage) {
        this.signaturePackage = signaturePackage;
    }

    public SignaturePackage getPackage() {
        return signaturePackage;
    }

    public ContainerValidationResult verifyAll()
    {
        boolean incompleteExists = false;
        boolean invalidExists = false;
        ContainerValidationResultImpl cvr = new ContainerValidationResultImpl();
        for (Signature signature : rootSignatures){
            try {
                SignatureValidationResult svr = signature.verify();

                cvr.addResult(signature, svr);

                ValidationResultType subResult = getCounterSeriesType(svr);
                if (subResult.equals(ValidationResultType.INCOMPLETE)){
                    incompleteExists = true;
                }

                if (subResult.equals(ValidationResultType.INVALID)){
                    invalidExists = true;
                }

            } catch (Exception x){
                logger.warn("Error in signature validation ", x);
                invalidExists = true;
            }

        }
        if (invalidExists){
            cvr.setResultType(ContainerValidationResultType.CONTAINS_INVALID);
        }
        else if (incompleteExists){
            cvr.setResultType(ContainerValidationResultType.CONTAINS_INCOMPLETE);
        }
        else {
            cvr.setResultType(ContainerValidationResultType.ALL_VALID);
        }
        return cvr;
    }
    
    private ValidationResultType getCounterSeriesType(SignatureValidationResult svr){
    	ValidationResultType found = ValidationResultType.VALID;
    	if (svr.getResultType()==ValidationResultType.INVALID)
    		return ValidationResultType.INVALID;
    	else if (svr.getResultType()==ValidationResultType.INCOMPLETE)
    		found = ValidationResultType.INCOMPLETE;
    	
    	for (SignatureValidationResult csvr : svr.getCounterSignatureValidationResults()){
    		ValidationResultType counter = getCounterSeriesType(csvr);
        	if (counter==ValidationResultType.INVALID)
        		return ValidationResultType.INVALID;
        	else if (counter==ValidationResultType.INCOMPLETE)
        		found = ValidationResultType.INCOMPLETE;
    	}
    	
    	return found;
    }

    @Override
    public void close() throws IOException {

    }
}
