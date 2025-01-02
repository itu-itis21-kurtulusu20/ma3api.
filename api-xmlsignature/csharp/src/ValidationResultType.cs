namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{

	/// <summary>
	/// <p>The Validation Process validates an electronic signature, the output
	/// status of the validation process can be:
	/// <ul>
	/// <li>invalid;
	/// <li>incomplete validation;
	/// <li>valid.
	/// </ul>
	/// <p>An <b>Invalid</b> response indicates that either the signature format is
	/// incorrect or that the digital signature value fails validation (e.g. the
	/// integrity check on the digital signature value fails or any of the
	/// certificates on which the digital signature validation depends is known
	/// to be invalid or revoked).
	/// <p>An <b>Incomplete</b> Validation response indicates that the format and
	/// digital signature verifications have not failed but there is insufficient
	/// information to determine if the electronic signature is valid (for example
	/// when all the required certificates are not available or the grace period is
	/// not completed). In the case of Incomplete Validation, the electronic
	/// signature may be checked again at some later time when additional validation
	/// information becomes available. Also, in the case of incomplete validation,
	/// additional information may be made available to the application or user,
	/// thus allowing the application or user to decide what to do with partially
	/// correct electronic signatures.
	/// <p>A <b>Valid</b> response indicates that the signature has passed
	/// validation and it complies with the signature validation policy.
	/// 
	/// @author ahmety
	///  date: Oct 2, 2009
	/// </summary>
	public enum ValidationResultType
	{
        VALID,
        INVALID,
        INCOMPLETE,
        WARNING, /* for internal usage, this type does not mentioned in spec! */
	}

}