using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ESignerAttribute : BaseASNWrapper<SignerAttribute>
    {
        public ESignerAttribute(byte[] aBytes)
            : base(aBytes, new SignerAttribute())
        {

        }

        public ESignerAttribute(SignerAttribute aObject)
            : base(aObject)
        {
        }

        public ESignerAttribute_element[] getElements()
        {
            ESignerAttribute_element[] elements = new ESignerAttribute_element[mObject.elements.Length];
            for (int i = 0; i < elements.Length; i++)
            {
                elements[i] = new ESignerAttribute_element(mObject.elements[i]);
            }

            return elements;

        }
    }
}
