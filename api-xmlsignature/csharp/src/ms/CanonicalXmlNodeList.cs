using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.ms
{
   public class CanonicalXmlNodeList : XmlNodeList
    {
        readonly List<XmlNode> mArray = new List<XmlNode>();
        public override XmlNode Item(int index)
        {
            return mArray[index];
        }

        public override IEnumerator GetEnumerator()
        {
            return mArray.GetEnumerator();
        }

        public void Add(XmlNode node)
        {
            mArray.Add(node);
        }
        public override int Count
        {
            get
            {
                return mArray.Count;
            }
        }
    }
}
