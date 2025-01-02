package tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.TransformType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.xml.NodeListImpl;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.xml.NodeNamespaceContext;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.xpath.FuncHere;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.xpath.XPathFunctionResolverImpl;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <p>The input required by this transform is an XPath node-set. Note that
 * if the actual input is an XPath node-set resulting from a null URI or
 * shortname XPointer dereference, then comment nodes will have been
 * omitted. If the actual input is an octet stream, then the application
 * MUST convert the octet stream to an XPath node-set suitable for use by
 * Canonical XML with Comments. (A subsequent application of the REQUIRED
 * Canonical XML algorithm would strip away these comments.) In other
 * words, the input node-set should be equivalent to the one that would be
 * created by the following process:</p>
 * <ul>
 * <li>1. Initialize an XPath evaluation context by setting the initial
 * node equal to the input XML document's root node, and set the
 * context position and size to 1.</li>
 * <li>2. Evaluate the XPath expression (//. | //@* | //namespace::*)</li>
 * </ul>
 * <p>
 * <p>The evaluation of this expression includes all of the document's nodes
 * (including comments) in the node-set representing the octet stream.</p>
 * <p>
 * <p>The transform output is also an XPath node-set. The XPath expression
 * appearing in the XPath parameter is evaluated once for each node in the input
 * node-set. The result is converted to a boolean. If the boolean is true, then
 * the node is included in the output node-set. If the boolean is false, then
 * the node is omitted from the output node-set.</p>
 * <p>
 * <p>The primary purpose of this transform is to ensure that only specifically
 * defined changes to the input XML document are permitted after the signature
 * is affixed. This is done by omitting precisely those nodes that are allowed
 * to change once the signature is affixed, and including all other input nodes
 * in the output. It is the responsibility of the XPath expression author to
 * include all nodes whose change could affect the interpretation of the
 * transform output in the application context. </p>
 *
 * @author ahmety
 * date: Jul 2, 2009
 */
public class XPathTransformer implements Transformer
{

    private static Logger logger = LoggerFactory.getLogger(XPathTransformer.class);

    private static List<DataType> TYPES = Arrays.asList(DataType.NODESET);


    public List<DataType> expectedDataTypes()
    {
        return TYPES;
    }

    public DataType returnType()
    {
        return DataType.NODESET;
    }

    public boolean acceptsAlgorithm(String aAlgorithmURI)
    {
        return TransformType.XPATH.getUrl().equals(aAlgorithmURI);
    }

    @SuppressWarnings("unchecked")
    public NodeList transform(Object aObject, String aAlgorithmURI,
                              Object[] aParams, Element aElement, String aBaseURI)
            throws XMLSignatureException
    {
        if (logger.isDebugEnabled())
            logger.debug("XPath donusum o: " + aObject + ", a: " + aAlgorithmURI
                            + ", p " + Arrays.asList(aParams));

        try {
            NodeList list = (NodeList) aObject;
            Node namespaceNode = (Node) aParams[1];
            String xpathExpr = (String) aParams[0];

            //if (needsCircunventBug(xpathExpr)){
            //Document doc = XmlUtil.getOwnerDocument(list);
            //XmlUtil.circumventBug2650(doc);
            //}

            return process(list, namespaceNode, xpathExpr);
        }
        catch (Exception x) {
            throw new XMLSignatureException(x, "transform.error", "XPath");
        }
    }


    private NodeList process(NodeList nodes,  Node namespaceNode, String xpathExpr)
            throws XMLSignatureException
    {
        if (logger.isDebugEnabled())
            logger.debug("process called with nodes.length : " + nodes.getLength());

        try {
            List<Node> list = new ArrayList<Node>();


            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();

            xpath.setNamespaceContext(new NodeNamespaceContext(namespaceNode, nodes));
            xpath.setXPathFunctionResolver(new XPathFunctionResolverImpl(new FuncHere(namespaceNode)));

            /*
            The here function returns a node-set containing the attribute or
            processing instruction node or the parent element of the text node
            that directly bears the XPath expression.  This expression results
            in an error if the containing XPath expression does not appear in
            the same XML document against which the XPath expression is being
            evaluated.
            trivial solution:
            extension functions required to have explicit namespaces. a real
            solution requires a lot of hacking! so we simply search and replace
            */
            xpathExpr = xpathExpr.replaceAll("here\\(\\s*\\)", "ma3:here()");
            if (logger.isDebugEnabled())
                logger.debug("xpath expression: " + xpathExpr);

            XPathExpression expression = xpath.compile(xpathExpr);


            /*
            The transform output is also an XPath node-set. The XPath expression
            appearing in the XPath parameter is evaluated once for each node in
            the input node-set. The result is converted to a boolean. If the
            boolean is true, then the node is included in the output node-set.
            If the boolean is false, then the node is omitted from the output
            node-set.
            */
            for (int i = 0; i < nodes.getLength(); i++) {
                Boolean x = (Boolean) expression.evaluate(nodes.item(i), XPathConstants.BOOLEAN);
                if (x.booleanValue()) {
                    list.add(nodes.item(i));
                    //System.out.println("selected: " + new String(XmlUtil.outputDOM(nodes.item(i), Constants.DEFAULT_REFERENCE_C14N)));
                    System.out.println("selected: " + nodes.item(i));
                }
            }

            if (logger.isDebugEnabled())
                logger.debug("xpath output node length: " + list.size());


            return new NodeListImpl(list);
        }
        catch (Exception tx) {
            throw new XMLSignatureException(tx, "transform.error", "XPath");
        }

    }


    /*private boolean needsCircunventBug(String str)
    {
        return (str.indexOf("namespace") != -1) || (str.indexOf("name()") != -1);
    }*/


    public static void main(String[] args) throws Exception
    {
        /*
        XPathFactory factory = XPathFactory.newInstance();

        XPath xpath = factory.newXPath();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();


        Document doc = db.parse(new ByteArrayInputStream(
                ("<XPath xmlns:ietf=\"http://www.ietf.org\" xmlns:pl=\"http://test.test\">self::pl:policy1</XPath>").getBytes()
        ));
        xpath.setNamespaceContext(new NodeNamespaceContext(doc));

        // xpath expression verilen Node degil, Node'in ait olduğu tüm
        // dokumani isledigi icin modifiye edildi....
        XPathExpression expression = xpath.compile("self::pl:policy1");


        ByteArrayInputStream bis = new ByteArrayInputStream(
                ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                 "<?xml-stylesheet type=\"text/xsl\" href=\"PolicyData.xsl\"?>\n" +
                 "<pl:compPolicy xmlns:pl=\"http://test.test\">\n" +
                 "\t<pl:title>Policy for signing XADES signatures</pl:title>\n" +
                 "\t<pl:policies>\n" +
                 "    <pl:policy1>\n" +
                 "      <pl:policy>This policy is applied to internal affairs only.</pl:policy>\n" +
                 "    </pl:policy1>\n" +
                 "    <pl:policy2>\n" +
                 "      <pl:policy>This policy is applied to our private customers.</pl:policy>\n" +
                 "    </pl:policy2>\n" +
                 "  \t<pl:policy3>\n" +
                 "      <pl:policy>This policy is applied to our public customers.</pl:policy>\n" +
                 "    </pl:policy3>\n" +
                 "  </pl:policies>\n" +
                 "</pl:compPolicy>").getBytes());

        Document node = db.parse(bis);
        XmlUtil.circumventBug2650(node);

        NodeList nl = XmlUtil.getNodeSet(node, true);
        //NodeList nl = extract(node);


        List<Node> list = new ArrayList<Node>();
        for (int i = 0; i < nl.getLength(); i++) {
            System.out.println("check : " + nl.item(i));
            Node result = (Node) expression.evaluate(nl.item(i), XPathConstants.NODE);
            if (result != null) {
                list.add(result);
                System.out.println(">>> match : " + new String(XmlUtil.outputDOM(result, C14nMethod.EXCLUSIVE)));
            }
        }
        Canonicalizer canon = Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");


        byte[] bytes = canon.canonicalizeXPathNodeSet(new NodeListImpl(list));
        System.out.println("canoned: " + new String(bytes));
        byte[] digested = KriptoUtil.digest(bytes, "SHA-1");
        System.out.println("digest: " + Base64.encode(digested));

        System.out.println("?igest: " + Base64.encode(KriptoUtil.digest("<pl:policy1></pl:policy1>".getBytes(), "SHA-1")));
        */
        /*
        String test = " as  here(\n ) ";
        test = test.replaceAll("here\\(\\s*\\)", "a:here()");
        System.out.println(test);*/
    }

}
