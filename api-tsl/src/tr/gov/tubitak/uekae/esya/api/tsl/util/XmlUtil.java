package tr.gov.tubitak.uekae.esya.api.tsl.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.NodeListImpl;

public class XmlUtil {
	public static Element getNextElement(Node el) {
		while ((el != null) && (el.getNodeType() != Node.ELEMENT_NODE)) {
			el = el.getNextSibling();
		}
		return (Element) el;

	}
	public static void addNSAttribute(Element aElement, String aPrefix,
			String aNamespace) {
		aElement.setAttributeNS("xmlns:" + aPrefix.trim(),
				Constants.NS_NAMESPACESPEC, aNamespace);
	}

	public static void removeChildren(Element aElement) {
		Node child;
		while ((child = aElement.getFirstChild()) != null) {
			aElement.removeChild(child);
		}
	}

	public static Element createDSctx(Document doc, String prefix, String namespace) {
		if ((prefix == null) || (prefix.trim().length() == 0)) {
			throw new IllegalArgumentException("You must supply a prefix");
		}

		Element ctx = doc.createElementNS(null, "namespaceContext");

		ctx.setAttributeNS(Constants.NS_NAMESPACESPEC, "xmlns:" +prefix.trim(), namespace);

		return ctx;
	}

	public static void addLineBreak(Element aElement) {
		aElement.appendChild(aElement.getOwnerDocument().createTextNode("\n"));
	}
    public static Element selectFirstElementNoNS(Node aSibling, String aNodeName)
    {
        while (aSibling != null) {
            if (aSibling.getLocalName()!=null && aSibling.getLocalName().equals(aNodeName)) {
                return (Element)aSibling;
            }
            aSibling = aSibling.getNextSibling();
        }
        return null;
    }

	// XML Dökümanı içindeki geçersiz XML karakterlerini kaldırır.
	// Geçerli olanlarla yer değiştirir.
	public static String escapeXMLData(String aText) {
		StringBuilder result = new StringBuilder();
		StringCharacterIterator iterator = new StringCharacterIterator(aText);
		char character = iterator.current();
		while (character != CharacterIterator.DONE) {
			if (character == '<') {
				result.append("&lt;");
			} else if (character == '>') {
				result.append("&gt;");
			} else if (character == '\"') {
				result.append("&quot;");
			} else if (character == '\'') {
				result.append("&#039;");
			} else if (character == '&') {
				result.append("&amp;");
			} else {
				// the char is not a special one
				// add it to the result as is
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}

	public static Element selectFirstElement(Node aSibling, String aNamespace,
			String aNodeName) {
		while (aSibling != null) {
			if (isNamespaceElement(aSibling, aNodeName, aNamespace)) {
				return (Element) aSibling;
			}
			aSibling = aSibling.getNextSibling();
		}
		return null;
	}

	public static boolean isNamespaceElement(Node el, String type, String ns) {
		return !((el == null) || el.getNamespaceURI() == null
				|| !el.getNamespaceURI().startsWith(ns) || !el.getLocalName()
				.equals(type));

	}

	public static Element[] selectNodes(Node aSibling, String aNamespace,
			String aNodeName) {
		int size = 20;
		Element[] a = new Element[size];
		int curr = 0;
		// List list=new ArrayList();
		while (aSibling != null) {
			if (isNamespaceElement(aSibling, aNodeName, aNamespace)) {
				a[curr++] = (Element) aSibling;
				if (size <= curr) {
					int cursize = size << 2;
					Element[] cp = new Element[cursize];
					System.arraycopy(a, 0, cp, 0, size);
					a = cp;
					size = cursize;
				}
			}
			aSibling = aSibling.getNextSibling();
		}
		Element[] af = new Element[curr];
		System.arraycopy(a, 0, af, 0, curr);
		return af;
	}

	public static Element findByIdAttr(Element aRoot, String aAttributeValue) {
		Element result = findByIdAttrRec(aRoot, aAttributeValue);
		if (result == null) {
			result = findLikeIdAttr(aRoot, aAttributeValue);
		}
		return result;
	}

	private static Element findByIdAttrRec(Element aRoot, String aAttributeValue) {
		String id = aRoot.getAttribute(Constants.ATTR_ID);
		if (id != null && id.equals(aAttributeValue)) {
			return aRoot;
		}
		NodeList elements = aRoot.getElementsByTagName("*");
		for (int i = 0; i < elements.getLength(); i++) {
			Element next = (Element) elements.item(i);
			Element found = findByIdAttr(next, aAttributeValue);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	private static final String[] IDs = { "id", "ID", "iD" };

	private static Element findLikeIdAttr(Element aRoot, String aAttributeValue) {
		for (int j = 0; j < IDs.length; j++) {
			String id = aRoot.getAttribute(IDs[j]);
			if (id != null && id.equals(aAttributeValue)) {
				return aRoot;
			}
		}
		NodeList elements = aRoot.getElementsByTagName("*");
		for (int i = 0; i < elements.getLength(); i++) {
			Element next = (Element) elements.item(i);
			Element found = findLikeIdAttr(next, aAttributeValue);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	public static String getText(Element element) {
		Node sibling = element.getFirstChild();
		StringBuilder sb = new StringBuilder();

		while (sibling != null) {
			if (sibling.getNodeType() == Node.TEXT_NODE) {
				Text t = (Text) sibling;
				sb.append(t.getData());
			}
			sibling = sibling.getNextSibling();
		}
		return sb.toString();
	}

	public static final String XML_PREAMBLE_STR = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n";
	public static final byte[] XML_PREAMBLE = XML_PREAMBLE_STR.getBytes();

	public static byte[] getBase64DecodedText(Element element)
			throws TSLException {
		String text = getText(element);
		byte[] bytes;
		try {
			bytes = Base64.decode(text.getBytes(), 0, text.getBytes().length);
			// bytes = Base64.decode(text);
		} catch (Exception x) {
			throw new TSLException(x, "errors.base64");
		}
		return bytes;
	}

	public static void setBase64EncodedText(Element aElement, byte[] aValue) {
		Node n = aElement.getFirstChild();
		while (n != null) {
			aElement.removeChild(n);
			n = n.getNextSibling();
		}

		if (aValue == null) {
			return;
		}

		String base64Encoded = Base64.encode(aValue);
		Text t = aElement.getOwnerDocument().createTextNode(base64Encoded);

		aElement.appendChild(t);
	}

	public static List<Element> selectChildElements(Element aElement) {
		List<Element> selected = new ArrayList<Element>();
		NodeList list = aElement.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				selected.add((Element) node);
			}
		}
		return selected;
	}

	public static NodeList getNodeSet(Node rootNode, boolean comments) {
		List<Node> list = new ArrayList<Node>();
		getSetRec(rootNode, list, comments);
		return new NodeListImpl(list);
	}

	private static void getSetRec(Node rootNode, List<Node> result,
			boolean comments) {
		switch (rootNode.getNodeType()) {
		case Node.ELEMENT_NODE:
			result.add(rootNode);
			Element el = (Element) rootNode;
			if (el.hasAttributes()) {
				NamedNodeMap nl = el.getAttributes();
				// XmlAttributeCollection nl = rootNode.Attributes;
				for (int i = 0; i < nl.getLength(); i++) {
					Node nlItem = nl.item(i);
					result.add(nlItem);

				}
			}
			// no return keep working
		case Node.DOCUMENT_NODE:
			for (Node r = rootNode.getFirstChild(); r != null; r = r
					.getNextSibling()) {
				if (r.getNodeType() == Node.TEXT_NODE) {
					result.add(r);
					while ((r != null) && (r.getNodeType() == Node.TEXT_NODE)) {
						r = r.getNextSibling();
					}
					if (r == null) {
						return;
					}
				}
				getSetRec(r, result, comments);
			}
			return;
		case Node.COMMENT_NODE:
			if (comments) {
				result.add(rootNode);
			}
			return;
		case Node.DOCUMENT_TYPE_NODE:
			return;
		default:
			result.add(rootNode);
			break;
		}
	}

	public static XMLGregorianCalendar createDate(Calendar aDate) {
		try {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(aDate.getTime());
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(
					calendar);
		} catch (DatatypeConfigurationException e) {
			throw new Error(e);
		}
	}

	public static XMLGregorianCalendar getDate(Element aElement)
			throws TSLException {
		String text = getText(aElement);
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(text);
		} catch (Throwable e) {
			throw new TSLException(e, "Incorrect date format: " + text);
		}
	}

	public static Document getOwnerDocument(NodeList aNodeList) {
		for (int i = 0; i < aNodeList.getLength(); i++) {
			Document doc;
			Node node = aNodeList.item(i);
			int nodeType = node.getNodeType();
			if (nodeType == Node.DOCUMENT_NODE) {
				doc = (Document) node;
			} else if (nodeType == Node.ATTRIBUTE_NODE) {
				doc = ((Attr) node).getOwnerElement().getOwnerDocument();
			} else
				doc = node.getOwnerDocument();

			if (doc != null)
				return doc;
		}
		return null;
	}
}
