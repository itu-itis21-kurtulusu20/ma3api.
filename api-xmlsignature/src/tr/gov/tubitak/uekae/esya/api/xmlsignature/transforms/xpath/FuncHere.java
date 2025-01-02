package tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.xpath;

import org.w3c.dom.Node;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import java.util.List;

/**
 * Function: node-set  here()
 * <p>The here function returns a node-set containing the attribute or
 * processing instruction node or the parent element of the text node that
 * directly bears the XPath expression.  This expression results in an error if
 * the containing XPath expression does not appear in the same XML document
 * against which the XPath expression is being evaluated.</p>
 * 
 * @author ahmety
 * date: Aug 6, 2009
 */
public class FuncHere implements XPathFunction
{
    Node mNodeContainingHere;

    public FuncHere(Node aNodeContainingHere)
    {
        mNodeContainingHere = aNodeContainingHere;
    }

    public Object evaluate(List aList) throws XPathFunctionException
    {
        return mNodeContainingHere;  
    }
}
