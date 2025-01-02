using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;


//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    /**
 * General contract for signature attributes.
 */
    public interface IAttribute
    {
        Asn1ObjectIdentifier getAttributeOID();
        bool isSigned();
        void setParameters(Dictionary<String, Object> aParameterAndValue);
        void setValue();
        EAttribute getAttribute();
    }
}
