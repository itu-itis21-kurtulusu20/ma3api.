using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{

    public class MimeTypeAttr : AttributeValue
    {
        public static readonly Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_mimeType;

        protected EMimeType mMimeType;

        /**
        * Create MimeTypeAttr with mime type
        * @param aMimetype EMimeType
        * @throws NullParameterException
        */
        public MimeTypeAttr(EMimeType aMimetype)
        {
            //super();
            mMimeType = aMimetype;
            if (mMimeType == null)
            {
                throw new NullParameterException("Mime type must be set");
            }
        }

        /**
        * Set mime type
        */
        public override void setValue()
        {
            _setValue(mMimeType.getObject());
        }

        /**
        * Returns AttributeOID of Mime Type attribute
        * @return
        */
        public override Asn1ObjectIdentifier getAttributeOID()
        {
            return OID;
        }

        /**
        * Checks whether attribute is signed or not.
        * @return True
        */
        public override bool isSigned()
        {
            return true;
        }

        /**
         * Returns mime type
         * @param aAttribute EAttribute
         * @return
         * @throws ESYAException
         */
        public static EMimeType toMimeType(EAttribute aAttribute)
        {
            return new EMimeType(aAttribute.getValue(0));
        }
    }
}
