using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.core
{

    using Element = XmlElement;

	/// <summary>
	/// Holder for "id<->xml element" relations for xml signature tree.
	/// 
	/// @author ahmety
	/// date: May 20, 2009
	/// </summary>
	public class IdRegistry
	{
		private readonly IDictionary<string, Element> mRegistry = new Dictionary<string, Element>();
		private readonly IDictionary<Element, string> mValue2Id = new Dictionary<Element, string>();

		/// <summary>
		/// Keeps id<->element mappings. If same object added twice for different
		/// ids, this is tracked and first one is dismissed. Registering null element
		/// means erase of mapping.
		/// </summary>
		/// <param name="aId"> of element </param>
		/// <param name="aElement"> that maps to an Id </param>
		public virtual void put(string aId, Element aElement)
		{
			if (aElement == null)
			{
				mRegistry.Remove(aId);
			}
			else
			{
				if (mValue2Id.ContainsKey(aElement))
				{
					string oldId = mValue2Id[aElement];
					mRegistry.Remove(oldId);
					mValue2Id.Remove(aElement);
				}
                if (mRegistry.ContainsKey(aId))
                {
                    mRegistry[aId] = aElement;
                }
                else
                {
                   mRegistry.Add(aId,aElement);
                }
				
                if (mValue2Id.ContainsKey(aElement))
                {
                    mValue2Id[aElement] = aId;
                }
                else
                {
                    mValue2Id.Add(aElement,aId);
                }
			}
		}

		public virtual Element get(string aId)
		{
            if (!mRegistry.ContainsKey(aId))
		        return null;
			return mRegistry[aId];
		}


	}

}