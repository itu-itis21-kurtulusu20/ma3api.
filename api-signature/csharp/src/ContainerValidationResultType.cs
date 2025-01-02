namespace tr.gov.tubitak.uekae.esya.api.signature
{
    /**
 * Result type summing verification of all signatures in a signature container
 * @author ayetgin
 */
    public enum ContainerValidationResultType
    {
        /**
       * All isgnatures in signature container is valid
       */
        ALL_VALID,

        /**
         * at least one of the signatures in signature container has a problem
         */
        CONTAINS_INVALID,

        /**
         * At least one of the signatures in signature container could not be
         * validated, possibly because of missing resources
         */
        CONTAINS_INCOMPLETE
    }
}
