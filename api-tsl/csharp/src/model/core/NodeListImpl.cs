using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.core
{
    public class NodeListImpl : XmlNodeList
    {
        private readonly IList<XmlNode> mList;

        public NodeListImpl(IList<XmlNode> aList)
        {
            mList = aList;
        }


        public virtual int Length
        {
            get
            {
                return mList.Count;
            }
        }

        public override XmlNode Item(int index)
        {
            return mList[index];
        }

        public override IEnumerator GetEnumerator()
        {
            return mList.GetEnumerator();
        }

        public override int Count
        {
            get
            {
                return mList.Count;
            }
        }
    }
}
