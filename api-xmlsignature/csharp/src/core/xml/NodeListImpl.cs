using System.Collections;
using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.core.xml
{

    using NodeList = XmlNodeList;
    using Node = XmlNode;


	/// <summary>
	/// Immutable NodeList helper imlementation. </summary>
	/// <seealso cref= org.w3c.dom.NodeList 
	/// 
	/// @author ahmety
	/// date: Jul 7, 2009 </seealso>
	public class NodeListImpl : XmlNodeList
	{
		private readonly IList<Node> mList;

		public NodeListImpl(IList<Node> aList)
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