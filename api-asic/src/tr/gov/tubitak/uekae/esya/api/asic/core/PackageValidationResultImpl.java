package tr.gov.tubitak.uekae.esya.api.asic.core;

import tr.gov.tubitak.uekae.esya.api.asic.model.SignatureContainerEntryImpl;
import tr.gov.tubitak.uekae.esya.api.signature.ContainerValidationResult;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageValidationResult;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.PackageValidationResultType;
import tr.gov.tubitak.uekae.esya.api.signature.sigpackage.SignatureContainerEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ayetgin
 */
public class PackageValidationResultImpl implements PackageValidationResult
{
    private PackageValidationResultType resultType;
    private Map<SignatureContainerEntryImpl, ContainerValidationResult> results = new HashMap<SignatureContainerEntryImpl, ContainerValidationResult>();

    public PackageValidationResultImpl()
    {
    }

    public PackageValidationResultImpl(PackageValidationResultType aResultType, Map<SignatureContainerEntryImpl, ContainerValidationResult> aResults)
    {
        resultType = aResultType;
        results = aResults;
    }

    public void setResultType(PackageValidationResultType resultType)
    {
        this.resultType = resultType;
    }

    public PackageValidationResultType getResultType()
    {
        return resultType;
    }

    public Map<SignatureContainerEntry, ContainerValidationResult> getAllResults()
    {
        return new HashMap<SignatureContainerEntry, ContainerValidationResult>(results); // stupid generics
    }

    public void addResult(SignatureContainerEntryImpl container, ContainerValidationResult validationResult){
        results.put(container, validationResult);
    }

    @Override
    public String toString()
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Package Validation Result : ").append(resultType).append("\n");
        //int index=0;
        for (SignatureContainerEntryImpl container : results.keySet()){
            ContainerValidationResult cvr = results.get(container);
            buffer.append(mark(cvr)).append("Container ").append(container.getASiCDocumentName()).append("\n> ");
            buffer.append(cvr.toString());
        }
        return buffer.toString();
    }

    private String mark(ContainerValidationResult cvr){
        switch (cvr.getResultType()){
            case ALL_VALID          : return "(+) ";
            case CONTAINS_INCOMPLETE: return "(?) ";
            default                 : return "(-) ";
        }
    }

}
