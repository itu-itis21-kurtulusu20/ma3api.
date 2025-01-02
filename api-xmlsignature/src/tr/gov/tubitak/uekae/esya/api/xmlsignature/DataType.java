package tr.gov.tubitak.uekae.esya.api.xmlsignature;


/**
 * The data-type of the result of URI dereferencing or subsequent 
 * Transforms is either an octet stream or an XPath node-set.
 *
 * @author ahmety
 * date: Jul 1, 2009
 */
public enum DataType
{
    //OCTET (byte[].class),
    OCTETSTREAM ,
    //NODE (Node.class),
    NODESET;


}
