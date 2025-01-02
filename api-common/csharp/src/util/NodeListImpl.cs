using System.Collections;
using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    public class NodeListImpl : XmlNodeList
    {
        private readonly IList<XmlNode> _mList;

        public NodeListImpl(IList<XmlNode> aList)
        {
            _mList = aList;
        }


        public virtual int Length
        {
            get
            {
                return _mList.Count;
            }
        }

        public override XmlNode Item(int index)
        {
            return _mList[index];
        }

        public override IEnumerator GetEnumerator()
        {
            return _mList.GetEnumerator();
        }

        public override int Count
        {
            get
            {
                return _mList.Count;
            }
        }
    }
}
