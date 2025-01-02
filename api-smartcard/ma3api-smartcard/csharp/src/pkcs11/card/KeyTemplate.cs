using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using iaik.pkcs.pkcs11.wrapper;
using log4net.Repository.Hierarchy;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card
{
    public abstract class KeyTemplate
    {
        protected String label;
        protected long keyId = -1;

        public Dictionary<long, CK_ATTRIBUTE> attributes = new Dictionary<long, CK_ATTRIBUTE>();

        protected KeyTemplate(String label)
        {
            this.label = label;
            this.add(new CK_ATTRIBUTE_NET(PKCS11Constants_Fields.CKA_LABEL, getLabel()));
        }

        public String getLabel()
        {
            return label;
        }

        public long getKeyId()
        {
            return keyId;
        }

        public void setKeyId(long keyId)
        {
            this.keyId = keyId;
        }

        public String getFormat()
        {
            throw new ESYARuntimeException("KeyTemplate Has No Format");
        }

        public KeyTemplate add(CK_ATTRIBUTE attribute)
        {
            if (attributes.ContainsKey(attribute.type))
                attributes[attribute.type] = attribute;
            else
                attributes.Add(attribute.type, attribute);

            return this;
        }

        public void remove(long attributeType)
        {
            if (attributes.ContainsKey(attributeType))
            {
                attributes.Remove(attributeType);
            }
        }

        public Object getAttribute(long attributeType)
        {
            return attributes[attributeType].pValue;
        }

        public bool containsAttribute(long attributeType)
        {
            return attributes.ContainsKey(attributeType);
        }

        /**
         * gets Copy of attributes
         * @return
         */
        public List<CK_ATTRIBUTE> getAttributes()
        {
            return new List<CK_ATTRIBUTE>(attributes.Values);
        }

        /**
         * gets attribute as array
         * @return
         */
        public CK_ATTRIBUTE[] getAttributesAsArr()
        {
            return attributes.Values.ToArray();
        }

    }
}
