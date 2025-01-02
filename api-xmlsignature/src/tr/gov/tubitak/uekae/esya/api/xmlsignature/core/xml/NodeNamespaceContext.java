package tr.gov.tubitak.uekae.esya.api.xmlsignature.core.xml;

import org.w3c.dom.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

/**
 * Utility class for extracting namespaces from given Node via
 * NamespaceContext interface.
 *
 * @see javax.xml.namespace.NamespaceContext
 *
 * @author ayetgin
 */

public class NodeNamespaceContext implements NamespaceContext
{
    private static final Logger logger =
                                LoggerFactory.getLogger(NodeNamespaceContext.class);

    private Node mNode;
    private NodeList mNodes;

    public NodeNamespaceContext(Node aNode)
    {
        mNode = aNode;
    }

    public NodeNamespaceContext(Node aNode, NodeList aNodes)
    {
        mNode = aNode;
        mNodes = aNodes;
    }

    public String getNamespaceURI(String s)
    {
        String r = null;
        if (mNode!=null)
            r = mNode.lookupNamespaceURI(s);

        if (r==null && mNodes!=null){
            for (int i=0; i<mNodes.getLength(); i++){
                r = mNodes.item(i).lookupNamespaceURI(s);
                if (r!=null)
                    break;
            }
        }

        if (r==null && s.equals(Constants.NS_MA3_PREFIX))
            return Constants.NS_MA3;

        if (logger.isDebugEnabled())
            logger.debug("get namespace uri: " + s + " is '" + r + "'");
        return r;
    }

    public String getPrefix(String s)
    {
        String r = null;
        if (mNode!=null)
            r = mNode.lookupPrefix(s);

        if (r==null && mNodes!=null){
            for (int i=0; i<mNodes.getLength(); i++){
                r = mNodes.item(i).lookupPrefix(s);
                if (r!=null)
                    break;
            }
        }

        if (r==null && s.equals(Constants.NS_MA3))
            return Constants.NS_MA3_PREFIX;
        
        if (logger.isDebugEnabled())
            logger.debug("get prefix: " + s + " is '" + r + "'");
        return r;
    }

    public Iterator getPrefixes(String s)
    {
        if (logger.isDebugEnabled())
            logger.debug("Not expected! getPrefixes called for '" + s+"'");
        return null;
    }

}
