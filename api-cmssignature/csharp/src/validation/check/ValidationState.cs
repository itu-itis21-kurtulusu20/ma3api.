
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    public enum ValidationState
    {
        /**
	 * Grace period does not pass for accurate validation.
	 */
        PREMATURE,
        /**
         * Grace period has passed. This is the accurate validation
         */
        MATURE
    }
}
