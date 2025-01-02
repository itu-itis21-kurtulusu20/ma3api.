package tr.gov.tubitak.uekae.esya.api.tsl.model.core;

import java.math.BigInteger;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Base64;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.IdGenerator;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public abstract class BaseElement {
	protected Element mElement;
	protected Document mDocument;
	// protected Context mContext;

	protected String mId = null;

	private NamespacePrefixMap nsPrefixMap = new NamespacePrefixMap();
	private IdGenerator idGenerator = new IdGenerator();

	// / <summary>
	// / Construct new BaseElement according to context </summary>
	// / <param name="aContext"> where some signature spesific properties
	// reside. </param>
	public BaseElement(Document Document) {
		mDocument = Document;

		mElement = createElement(getNamespace(), getLocalName());
	}

	// / <summary>
	// / Construct BaseElement from existing </summary>
	// / <param name="aElement"> xml element </param>
	// / <param name="aContext"> according to context </param>
	// / <exception cref="XMLSignatureException"> when structure is invalid or
	// can not be
	// / resolved appropriately </exception>
	public BaseElement(Element aElement) throws TSLException {
		if (aElement == null) {
			throw new TSLException("errors.nullElement");
		}

		this.mDocument = aElement.getOwnerDocument();
		this.mElement = aElement;

		mId = getAttribute(aElement, Constants.ATTR_ID);

		checkNamespace();
	}

	// / <summary>
	// / Get requested attribute value from given element as String </summary>
	// / <param name="aElement"> element containing the attribute </param>
	// / <param name="aAttributeName"> name of the attribute </param>
	// / <returns> null or attribute value </returns>
	protected String getAttribute(Element aElement, String aAttributeName) {
		if (aElement.hasAttribute(aAttributeName)) {
			return aElement.getAttribute(aAttributeName);
		}
		return null;
	}

	protected Element createElement(String aNamespace, String aName) {
		String prefix = nsPrefixMap.getPrefix(aNamespace);
		Element e = null;
		if (prefix == null) {
			e = mDocument.createElement(aName);
		} else {
			e = mDocument.createElementNS(aNamespace, aName);
			e.setPrefix(prefix);
		}

		// String attrName = prefix == null ? "xmlns" : "xmlns:" +
		// String.Intern(prefix);
		// e.SetAttribute(attrName,aNamespace);
		return e;
	}

	protected Element insertElement(String aNamespace, String aName) {
		Element e = createElement(aNamespace, aName);
		mElement.appendChild(e);
		addLineBreak();
		return e;
	}

	protected Element insertTextElement(String aNamespace, String aTagname,
			String aText) {
		Element element = insertElement(aNamespace, aTagname);
		element.setTextContent(aText);
		return element;
	}

	protected String getChildText(String aNamespace, String aTagname) {
		Element e = selectChildElement(aNamespace, aTagname);
		if (e != null) {
			return XmlUtil.getText(e);
		}
		return null;
	}

	protected Element insertBase64EncodedElement(String aNamespace,
			String aTagname, byte[] aValue) {
		return insertTextElement(aNamespace, aTagname, Base64.encode(aValue));
	}

	/*
	 * protected Element selectChildElementXML(String aNSPrefix, String
	 * aTagname) { XmlNameTable nsTable = mElement.getOwnerDocument.NameTable;
	 * XmlNamespaceManager nsManager = new XmlNamespaceManager(nsTable);
	 * nsManager.AddNamespace("ds", Constants.NS_XMLDSIG);
	 * nsManager.AddNamespace("xades", Constants.NS_XADES);
	 * nsManager.AddNamespace("tsl", Constants.NS_TSL);
	 * nsManager.AddNamespace("tslx", Constants.NS_TSLX);
	 * nsManager.AddNamespace("ecc", Constants.NS_ECC); NodeList xmlNodeList =
	 * mElement.SelectNodes(".//" + aNSPrefix + ":" + aTagname, nsManager);
	 * return (Element)mElement.SelectSingleNode(".//" + aNSPrefix + ":" +
	 * aTagname, nsManager); }
	 */
	protected Element selectChildElement(String aNamespace, String aTagname) {
		return XmlUtil.selectFirstElement(mElement.getFirstChild(), aNamespace,
				aTagname);
	}

	protected Element[] selectChildren(String aNamespace, String aTagname) {
		return XmlUtil.selectNodes(mElement.getFirstChild(), aNamespace,
				aTagname);
	}

	// / <summary>
	// / Check if the namespace and localname is valid. </summary>
	// / <exception cref="XMLSignatureException"> if wrong xml element given to
	// constructor </exception>
	protected void checkNamespace() throws TSLException {
		String localnameSHOULDBE = getLocalName();
		String namespaceSHOULDBE = getNamespace();

		String localnameIS = mElement.getLocalName();
		String namespaceIS = mElement.getNamespaceURI();
		if ((!namespaceIS.startsWith(namespaceSHOULDBE))
				|| !localnameSHOULDBE.equals(localnameIS)) {
			// bu değişim ancak ve ancak optional tag ler için yapılacaktır.
			// Bunu niçin bir kontrol mekanizması düşünülebilir!!!
			// Ex: list of required and list of optional tags gibi
			// mElement = (Element)mElement.NextSibling;
			// checkNamespace();
			throw new TSLException("xml.WrongElement" + namespaceIS + ":"
					+ localnameIS + namespaceSHOULDBE + ":" + localnameSHOULDBE);
		}
	}

	public Element getElement() {
		return mElement;
	}

	public void setElement(Element aElement) {
		mElement = aElement;
	}

	public Document getDocument() {
		return mDocument;
	}

	public void setDocument(Document aDocument) {
		mDocument = aDocument;
	}

	public String getId() {
		return mId;
	}

	public void setId(String value) {
		if (value == null) {
			mElement.removeAttributeNS(null, Constants.ATTR_ID);
		} else {
			if (mElement.hasAttribute(Constants.ATTR_ID)) {
				mElement.removeAttribute(Constants.ATTR_ID);
			}
			mElement.setAttributeNS(null, Constants.ATTR_ID, value);
			mId = value;
		}
	}

	// utility methods

	// Add line break text node to xml structure for pretty print
	protected void addLineBreak() {
		mElement.appendChild(mDocument.createTextNode("\n"));
	}

	// Add line break text node to given xml element for pretty print
	protected void addLineBreak(Element aElement) {
		aElement.appendChild(mDocument.createTextNode("\n"));
	}

	protected void generateAndSetId(String aType) {
		String id = idGenerator.uret(aType);
		setId(id);
	}

	protected void addBigIntegerElement(BigInteger aValue, String aNamespace,
			String aTagName) {
		if (aValue != null) {
			Element e = mDocument.createElementNS(aNamespace, aTagName);
			e.setPrefix(nsPrefixMap.getPrefix(aNamespace));

			// common dependency?
			String val = Base64.encode(aValue.toByteArray());
			Text text = mDocument.createTextNode(val);
			e.appendChild(text);
			mElement.appendChild(e);
		}
	}

	// get base64 encoded content from child element as BigInteger
	protected BigInteger getBigIntegerFromElement(String aNamespace,
			String aLocalname) {
		Element text = selectChildElement(aNamespace, aLocalname);
		byte[] bytes = Base64.decode(XmlUtil.getText(text).getBytes(), 0,
				XmlUtil.getText(text).getBytes().length);
		return new BigInteger(1, bytes);
	}

	// not sure
	public void RemoveAll() {
		removeAll(mElement);
	}

	private void removeAll(Node node) {
		NodeList nodelist = node.getChildNodes();
		for (int i = 0; i < nodelist.getLength(); i++) {
			Node n = nodelist.item(i);
			if (n.hasChildNodes()) // edit to remove children of children
			{
				removeAll(n);
				node.removeChild(n);
			} else
				node.removeChild(n);
		}
	}

	public String getNamespace() {
		return Constants.NS_TSL;
	}

	public abstract String getLocalName();
}
