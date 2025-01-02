using System;
using System.Collections.Generic;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using Attribute = tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.attribute
{
    public abstract class AttributeValue : IAttribute
    {
        private readonly EAttribute mAttribute;
        protected Dictionary<String, Object> mAttParams = new Dictionary<String, Object>();
        protected ILog logger = null;

        #region IAttribute Members
        public abstract Asn1ObjectIdentifier getAttributeOID();

        public abstract bool isSigned();
        public abstract void setValue();
        #endregion

        protected AttributeValue()
        {
            logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
            mAttribute = new EAttribute(new Attribute());
            mAttribute.setType(getAttributeOID());
        }
        /**
        * Set parameters
        * @param aParameterAndValue 
        */
        public void setParameters(Dictionary<String, Object> aParameterAndValue)
        {
            mAttParams = aParameterAndValue;
        }

        protected void _setValue(Asn1Type aValue)
        {
            _setValue(new Asn1Type[] { aValue });
        }

        protected void _setValue(params Asn1Type[] aValue)
        {
            foreach (Asn1Type deger in aValue)
            {
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                try
                {
                    deger.Encode(encBuf);
                }
                catch (Asn1Exception ex1)
                {
                    throw new CMSSignatureException(/*GenelDil.ASN1_ENCODE_HATASI*/Resource.message(Resource.ASN1_ENCODE_HATASI), ex1);
                }

                mAttribute.addValue(encBuf.MsgCopy);
            }
        }
        /**
         * @return attribute
         */
        public EAttribute getAttribute()
        {
            return mAttribute;
        }


    }
}
