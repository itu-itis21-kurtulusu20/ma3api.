using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.infra.mobile
{
    [Serializable]
    public class PhoneNumberAndOperator : UserIdentifier
    {
        readonly String mPhoneNumber;
	    readonly Operator mOperator;
	
	    public PhoneNumberAndOperator(String aPhoneNumber, Operator aOperator)
	    {
		    mPhoneNumber = aPhoneNumber;
		    mOperator = aOperator;
	    }
	
	    public String getPhoneNumber()
	    {
		    return mPhoneNumber;
	    }
	
	    public Operator getOperator()
	    {
		    return mOperator;
	    }
    }
}
