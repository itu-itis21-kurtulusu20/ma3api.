package tr.gov.tubitak.uekae.esya.api.tsl.model.core;

import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListImpl implements NodeList {
	private List<Node> mList;

	public NodeListImpl(List<Node> aList) {
		mList = aList;
	}

	public int getLength() {
		return mList.size();
	}

	public Node item(int index) {
		return mList.get(index);
	}

	// gerekli mi?
	public Iterator GetEnumerator() {
		return mList.iterator(); // GetEnumerator();
	}

	public int getCount() {
		return mList.size();
	}
}
