package tr.gov.tubitak.uekae.esya.api.common.util;

/**
 * @author ayetgin
 */
import org.w3c.dom.*;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathConstants;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;


/**
 * @author ahmety
 * date: Apr 2, 2009
 */
public class XmlUtil
{

    public static final String NS_NAMESPACESPEC     = "http://www.w3.org/2000/xmlns/";
    public static final String ATTR_ID                  = "Id";

    public static Element getNextElement(Node el)
    {
        while ((el != null) && (el.getNodeType() != Node.ELEMENT_NODE)) {
            el = el.getNextSibling();
        }
        return (Element) el;

    }

    public static void removeChildren(Element aElement)
    {
        Node child;
        while ((child = aElement.getFirstChild()) !=null){
            aElement.removeChild(child);
        }
    }

    public static void addLineBreak(Element aElement){
        aElement.appendChild(aElement.getOwnerDocument().createTextNode("\n"));
    }


    public static Element createDSctx(Document doc, String prefix, String namespace)
    {
        if ((prefix == null) || (prefix.trim().length() == 0)) {
            throw new IllegalArgumentException("You must supply a prefix");
        }

        Element ctx = doc.createElementNS(null, "namespaceContext");

        ctx.setAttributeNS(NS_NAMESPACESPEC, "xmlns:" + prefix.trim(), namespace);

        return ctx;
    }

    public static void addNSAttribute(Element aElement, String aPrefix,  String aNamespace)
    {
        aElement.setAttributeNS(NS_NAMESPACESPEC, "xmlns:" + aPrefix.trim(), aNamespace);
    }

    public static String escapeXMLData(String aText){
        StringBuilder result = new StringBuilder();
        StringCharacterIterator iterator = new StringCharacterIterator(aText);
        char character =  iterator.current();
        while (character != CharacterIterator.DONE ){
          if (character == '<') {
            result.append("&lt;");
          }
          else if (character == '>') {
            result.append("&gt;");
          }
          else if (character == '\"') {
            result.append("&quot;");
          }
          else if (character == '\'') {
            result.append("&#039;");
          }
          else if (character == '&') {
             result.append("&amp;");
          }
          else {
            //the char is not a special one
            //add it to the result as is
            result.append(character);
          }
          character = iterator.next();
        }
        return result.toString();
      }


    public static Element findByIdAttr(Element aRoot, String aAttributeValue){
    	Element result = findByIdAttrRec(aRoot, aAttributeValue);
    	if (result==null){
    		result=findLikeIdAttr(aRoot, aAttributeValue);
    	}
    	return result;
    }

    private static Element findByIdAttrRec(Element aRoot, String aAttributeValue){
        String id = aRoot.getAttribute(ATTR_ID);
        if (id!=null && id.equals(aAttributeValue)){
            return aRoot;
        }
        NodeList elements = aRoot.getElementsByTagName("*");
        for (int i =0; i< elements.getLength(); i++){
            Element next = (Element)elements.item(i);
            Element found = findByIdAttr(next, aAttributeValue);
            if (found!=null)
                return found;
        }
        return null;
    }

    private static final String[] IDs = {"id", "ID", "iD"};

    private static Element findLikeIdAttr(Element aRoot, String aAttributeValue){
        for (int j=0; j<IDs.length; j++){
	    	String id = aRoot.getAttribute(IDs[j]);
	        if (id!=null && id.equals(aAttributeValue)){
	            return aRoot;
	        }
        }
        NodeList elements = aRoot.getElementsByTagName("*");
        for (int i =0; i< elements.getLength(); i++){
            Element next = (Element)elements.item(i);
            Element found = findLikeIdAttr(next, aAttributeValue);
            if (found!=null)
                return found;
        }
        return null;
    }

    /*public static Node findByEquals(Node aRoot, Node aNode){
        if (aRoot.equals(aNode)){
            return aRoot;
        }
        NodeList elements = aRoot.getChildNodes();
        for (int i =0; i< elements.getLength(); i++){
            Node next = elements.item(i);
            Node found = findByEquals(next, aNode);
            if (found!=null)
                return found;
        }
        return null;
    } */

    public static String getText(NodeList aList)
    {
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i<aList.getLength();i++){
            Node aNode = aList.item(i);
            if (aNode.getNodeType() == Node.TEXT_NODE){
                Text t = (Text)aNode;
                buffer.append(t.getData());
            }
        }
        return buffer.toString();
    }

    public static String getText(Element element)
    {
        Node sibling = element.getFirstChild();
        StringBuffer sb = new StringBuffer();

        while (sibling != null) {
            if (sibling.getNodeType() == Node.TEXT_NODE) {
                Text t = (Text) sibling;
                sb.append(t.getData());
            }
            sibling = sibling.getNextSibling();
        }
        return sb.toString();
    }


    public static byte[] getBase64DecodedText(Element element)
            throws ESYAException
    {
        String text = getText(element);
        byte[] bytes = Base64.decode(text);
        return bytes;
    }

    public static void setBase64EncodedText(Element aElement, byte[] aValue)
    {
        Node n = aElement.getFirstChild();
        while (n != null) {
            aElement.removeChild(n);
            n = n.getNextSibling();
        }

        if (aValue==null)
            return;

        String base64Encoded = Base64.encode(aValue);
        Text t = aElement.getOwnerDocument().createTextNode(base64Encoded);

        aElement.appendChild(t);
    }

	public static void setText(Element aElement, String aTextValue){
		Node n = aElement.getFirstChild();
		while (n != null) {
			aElement.removeChild(n);
			n = n.getNextSibling();
		}

		if (aTextValue==null)
			return;
		Text t = aElement.getOwnerDocument().createTextNode(aTextValue);

		aElement.appendChild(t);
	}

	public static void addElement(Element root, String tagName, String value){
		Element element = root.getOwnerDocument().createElement(tagName);
		setText(element, value);
		root.appendChild(element);
	}


    public static NodeList getNodeSet(Node rootNode, boolean comments) {
        List<Node> list = new ArrayList<Node>();
        getSetRec(rootNode, list, comments);
        return new NodeListImpl(list);
    }

    private static void getSetRec(final Node rootNode, final List<Node> result, final boolean comments)
    {
        switch (rootNode.getNodeType()) {
            case Node.ELEMENT_NODE:
                result.add(rootNode);
                Element el = (Element) rootNode;
                if (el.hasAttributes()) {
                    NamedNodeMap nl = rootNode.getAttributes();
                    for (int i = 0; i < nl.getLength(); i++) {
                        result.add(nl.item(i));
                    }
                }
                //no return keep working
            case Node.DOCUMENT_NODE:
                for (Node r = rootNode.getFirstChild(); r != null; r = r.getNextSibling()) {
                    if (r.getNodeType() == Node.TEXT_NODE) {
                        result.add(r);
                        while ((r != null) && (r.getNodeType() == Node.TEXT_NODE)) {
                            r = r.getNextSibling();
                        }
                        if (r == null)
                            return;
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
        }
    }


    public static List<Element> selectChildElements(Element aElement)
    {
        List<Element> selected = new ArrayList<Element>();
        NodeList list = aElement.getChildNodes();

        for (int i = 0; i < list.getLength(); i++)
        {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                selected.add((Element) node);
            }
        }
        return selected;
    }

    public static Element[] selectNodes(Node aSibling, String aNamespace, String aNodeName)
    {
        int size = 20;
        Element[] a = new Element[size];
        int curr = 0;
        //List list=new ArrayList();
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

    public static Element selectFirstElement(Node aSibling, String aNamespace, String aNodeName)
    {
        while (aSibling != null) {
            if (isNamespaceElement(aSibling, aNodeName, aNamespace)) {
                return (Element)aSibling;
            }
            aSibling = aSibling.getNextSibling();
        }
        return null;
    }

    public static boolean isNamespaceElement(Node el, String type, String ns)
    {
        return !((el == null) || el.getNamespaceURI()==null || !el.getNamespaceURI().startsWith(ns)
                 || !el.getLocalName().equals(type));

    }

    public static boolean isDescendantOrSelf(Node ctx, Node descendantOrSelf) {

       if (ctx == descendantOrSelf) {
          return true;
       }

       Node parent = descendantOrSelf;

       while (true) {
          if (parent == null) {
             return false;
          }

          if (parent == ctx) {
             return true;
          }

          if (parent.getNodeType() == Node.ATTRIBUTE_NODE) {
             parent = ((Attr) parent).getOwnerElement();
          } else {
             parent = parent.getParentNode();
          }
       }
    }


    private static final byte[] XML_PREAMBLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes();



    public static XMLGregorianCalendar createDate() throws ESYAException {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(
                    new GregorianCalendar());
        }
        catch (DatatypeConfigurationException e) {
            throw new ESYAException("Problem in data type configuration", e);
        }
    }

    public static XMLGregorianCalendar createDate(Calendar aDate) throws ESYAException {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(aDate.getTime());
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        }
        catch (DatatypeConfigurationException e) {
            throw new ESYAException("Problem in data type configuration", e);
        }
    }

    public static XMLGregorianCalendar getDate(Element aElement)
    {
        String text = getText(aElement);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(text);
        }
        catch (Exception e) {
            throw new ESYARuntimeException("Incorrect date format: "+text, e);
        }
    }


    /**
     * This method spreads all namespace attributes in a DOM document to their
     * children. This is needed because the XML Signature XPath transform
     * must evaluate the XPath against all nodes in the input, even against
     * XPath namespace nodes. Through a bug in XalanJ2, the namespace nodes are
     * not fully visible in the Xalan XPath model, so we have to do this by
     * hand in DOM spaces so that the nodes become visible in XPath space.
     * @param aDocument to fix bug
     * @see <A HREF="http://nagoya.apache.org/bugzilla/show_bug.cgi?id=2650">
     *      Namespace axis resolution is not XPath compliant </A>
     */
    public static void circumventBug2650(Document aDocument)
    {

        Element documentElement = aDocument.getDocumentElement();

        // if the document element has no xmlns definition, we add xmlns=""
        Attr xmlnsAttr = documentElement.getAttributeNodeNS(NS_NAMESPACESPEC, "xmlns");

        if (xmlnsAttr == null) {
            documentElement.setAttributeNS(NS_NAMESPACESPEC, "xmlns", "");
        }

        circumventBug2650internal(aDocument);
    }

    /**
     * This is the work horse for {@link #circumventBug2650}.
     * @param aNode to fix
     * @see <A HREF="http://nagoya.apache.org/bugzilla/show_bug.cgi?id=2650">
     *              Namespace axis resolution is not XPath compliant </A>
     */
    public static void circumventBug2650internal(Node aNode)
    {
        Node parent = null;
        Node sibling = null;
        do {
            switch (aNode.getNodeType()) {
                case Node.ELEMENT_NODE:
                    Element element = (Element) aNode;
                    if (!element.hasChildNodes()) {
                        break;
                    }
                    if (element.hasAttributes())
                    {
                        NamedNodeMap attributes = element.getAttributes();
                        int attributesLength = attributes.getLength();

                        for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
                        {
                            if (child.getNodeType() != Node.ELEMENT_NODE) {
                                continue;
                            }
                            Element childElement = (Element) child;

                            for (int i = 0; i < attributesLength; i++)
                            {
                                Attr currentAttr = (Attr) attributes.item(i);
                                if (!NS_NAMESPACESPEC.equals(currentAttr.getNamespaceURI())){
                                    continue;
                                }
                                if (childElement.hasAttributeNS(NS_NAMESPACESPEC, currentAttr.getLocalName())) {
                                    continue;
                                }
                                childElement.setAttributeNS(NS_NAMESPACESPEC, currentAttr.getName(), currentAttr.getNodeValue());
                            }
                        }
                    }
                case Node.ENTITY_REFERENCE_NODE:
                case Node.DOCUMENT_NODE:
                    parent = aNode;
                    sibling = aNode.getFirstChild();
                    break;
            }
            while ((sibling == null) && (parent != null)) {
                sibling = parent.getNextSibling();
                parent = parent.getParentNode();
            }
            if (sibling == null) {
                return;
            }

            aNode = sibling;
            sibling = aNode.getNextSibling();
        }
        while (true);
    }

    public static NodeList extract(Node aRootNode) throws ESYAException
    {
        /*
        The input required by this transform is an XPath node-set. Note that if
        the actual input is an XPath node-set resulting from a null URI or
        shortname XPointer dereference, then comment nodes will have been
        omitted. If the actual input is an octet stream, then the application
        MUST convert the octet stream to an XPath node-set suitable for use by
        Canonical XML with Comments. (A subsequent application of the REQUIRED
        Canonical XML algorithm would strip away these comments.) In other
        words, the input node-set should be equivalent to the one that would be
        created by the following process:

            1. Initialize an XPath evaluation context by setting the initial
               node equal to the input XML document's root node, and set the
               context position and size to 1.
            2. Evaluate the XPath expression (//. | //@* | //namespace::*)
        */
        try {
            //System.out.println("extract : "+aRootNode);
            XPathFactory factory = XPathFactory.newInstance();

            XPath xpath = factory.newXPath();

            // xpath expression verilen Node degil, Node'in ait olduğu tüm
            // dokumani isledigi icin modifiye edildi....
            XPathExpression expression = xpath.compile(".//. | .//@* | .//namespace::* ");
            //XPathExpression expression = xpath.compile("//. | //@* | //namespace::* ");

            return (NodeList) expression.evaluate(aRootNode, XPathConstants.NODESET);
        }
        catch (Exception x) {
            throw new ESYAException("Node '" + aRootNode + "' NodeList'e donusturulemedi", x);
        }
    }

    /**
     * This method returns the first non-null owner document of the Node's in
     * this Set.
     *
     * <p>This method is necessary because it <I>always</I> returns a
     * {@link org.w3c.dom.Document}. {@link org.w3c.dom.Node#getOwnerDocument} returns <CODE>null</CODE>
     * if the {@link org.w3c.dom.Node} is a {@link org.w3c.dom.Document}.
     *
     * @param aNodeList to find owner document
     * @return the owner document
     */
    public static Document getOwnerDocument(NodeList aNodeList)
    {
        for (int i=0; i<aNodeList.getLength(); i++) {
            Document doc;
            Node node = aNodeList.item(i);
            int nodeType = node.getNodeType();
            if (nodeType == Node.DOCUMENT_NODE) {
                doc = (Document) node;
            }
            else if (nodeType == Node.ATTRIBUTE_NODE) {
                doc = ((Attr) node).getOwnerElement().getOwnerDocument();
            }
            else doc = node.getOwnerDocument();

            if (doc!=null)
                return doc;
        }
        return null;
    }
}
