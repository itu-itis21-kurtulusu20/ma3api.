package tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

/**
 * @author ayetgin
 */
public enum PackageValidationResultType
{
    /**
     * All signature containers in signature package is valid
     */
    ALL_VALID,

    /**
     * at least one of the container in signature package has a problem
     */
    CONTAINS_INVALID,

    /**
     * At least one of the containers in signature package could not be
     * validated, possibly because of missing resources
     */
    CONTAINS_INCOMPLETE
}
