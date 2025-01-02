using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.asn.esya;

namespace tr.gov.tubitak.uekae.esya.api.asn.esya
{
    public class EESYAParametre : BaseASNWrapper<ESYAParametre>
    {
         public EESYAParametre(ESYAParametre aObject) : base(aObject) {}

        public EESYAParametre(byte[] aBytes):base(aBytes, new ESYAParametre()){}

        public EESYAParametre(String paramName, byte[] paramValue):base(new ESYAParametre(paramName, paramValue)){}

        public String getParamName() {
    	    if(mObject.paramName != null)
			    return mObject.paramName.mValue;
		    return null;
	    }

        public byte[] getParamValue() {
    	    if(mObject.paramValue != null)
			    return mObject.paramValue.mValue;
		    return null;
	    }
    }
}
