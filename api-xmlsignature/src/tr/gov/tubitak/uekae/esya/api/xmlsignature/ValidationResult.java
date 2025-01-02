package tr.gov.tubitak.uekae.esya.api.xmlsignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultDetail;
import tr.gov.tubitak.uekae.esya.api.signature.impl.ResultFormatter;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.provider.XMLProviderUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ahmety
 * date: Oct 1, 2009
 */
public class ValidationResult implements ValidationResultDetail
{
    protected static Logger logger = LoggerFactory.getLogger(ValidationResult.class);

    protected ValidationResultType mResultType;
    protected String mCheckMessage;
    protected String mMessage;
    protected String mInfo;
    protected Class mVerifierClass;

    protected List<ValidationResult> items = new ArrayList<ValidationResult>();

    public ValidationResult(Class aVerifierClass)
    {
        this.mVerifierClass = aVerifierClass;
    }
    /*
    public ValidationResult(ValidationResultType aResultType, String aMessage)
    {
        this(aResultType, aMessage, null, null);
    }

    public ValidationResult(ValidationResultType aResultType, String aMessage, String aInfo)
    {
        this(aResultType, aMessage, aInfo, null);
    }    */

    public ValidationResult(ValidationResultType aResultType,
                            String aCheckMessage,
                            String aMessage,
                            String aInfo,
                            Class aValidatorClass)
    {
        mResultType = aResultType;
        mCheckMessage = aCheckMessage;
        mMessage = aMessage;
        mInfo = aInfo;
        mVerifierClass = aValidatorClass;
    }

    /*
    public void setStatus(ValidationResultType aType, String aCheckMessage, String aMessage){
        setStatus(aType, aCheckMessage, aMessage, null);
    }  */

    public void setStatus(ValidationResultType aType, String aCheckMessage, String aMessage, String aInfo){
        mResultType = aType;
        mCheckMessage = aCheckMessage;
        mMessage = aMessage;
        mInfo = aInfo;
    }

    public void setType(ValidationResultType aResultType)
    {
        mResultType = aResultType;
    }

    public ValidationResultType getType()
    {
        return mResultType;
    }

    public String getCheckMessage()
    {
        return mCheckMessage;
    }

    public void setCheckMessage(String aMCheckMessage)
    {
        mCheckMessage = aMCheckMessage;
    }

    public void setMessage(String aMessage)
    {
        mMessage = aMessage;
    }

    public String getMessage()
    {
        return mMessage;
    }

    public String getVerifierClass()
    {
        return mVerifierClass.getName();
    }

    public void setVerifierClass(Class aVerifierClass)
    {
        mVerifierClass = aVerifierClass;
    }

    public void addItem(ValidationResult aItem){
        if (aItem!=null)
            items.add(aItem);
    }

    public int getItemCount(){
        return items.size();
    }

    public ValidationResult getItem(int aIndex){
        return items.get(aIndex);
    }

    public tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType getResultType()
    {
        return XMLProviderUtil.convert(mResultType);
    }

    public Class getValidatorClass()
    {
        return mVerifierClass;
    }

    public String getCheckResult()
    {
        return mMessage + ((mInfo!=null) ? "\n"+mInfo : "");
    }

    public List<ValidationResultDetail> getDetails()
    {
        return new ArrayList<ValidationResultDetail>(items);
    }

    public String toXml(){
        return toXml(0);
    }

    private ResultFormatter formatter = new ResultFormatter();
    @Override
    public String toString() {
        return formatter.prettyPrint(this, 0);
    }

    String toXml(int indent){
        StringBuffer buffer = new StringBuffer();
        boolean verified = mResultType == ValidationResultType.VALID;
        StringBuffer indentStr = new StringBuffer();
        for (int i=0; i<indent;i++){
            indentStr.append('\t');
        }
        buffer.append(indentStr);
        buffer.append(verified ? "<Verified" : "<Failed");
        if (mVerifierClass!=null){
            buffer.append(" by=\"").append(mVerifierClass).append('\"');
        }
        buffer.append(">\n");
        buffer.append(indentStr).append('\t')
                .append("<ResultType>").append(mResultType).append("</ResultType>\n");
        buffer.append(indentStr).append('\t')
                .append("<Message>").append(XmlUtil.escapeXMLData(mMessage)).append("</Message>\n");
        if (mInfo!=null){
            buffer.append(indentStr).append('\t')
                    .append("<Info>").append(XmlUtil.escapeXMLData(mInfo))
                    .append(indentStr).append('\t').append("</Info>\n");
        }
        if (items!=null && items.size()>0){
            buffer.append(indentStr).append('\t')
                    .append("<Details>\n");
            for (ValidationResult item : items) {
                buffer.append(item.toXml(indent+2));
            }
            buffer.append(indentStr).append("\t")
                    .append("</Details>\n");
        }
        buffer.append(indentStr)
                .append(verified ? "</Verified>" : "</Failed>").append('\n');

        //return prettyPrint(buffer.toString());
        return buffer.toString();
    }



    private static void prettyPrint(StringBuffer result, Node aNode, int level)
    {
        switch (aNode.getNodeType()) {
            case Node.DOCUMENT_NODE :
                prettyPrint(result, ((Document)aNode).getDocumentElement(), level);
                break;
            case Node.TEXT_NODE :
                String text = aNode.getTextContent();
                System.out.println("text: '"+text+"'");
                result.append(text);
                if (text.equals("\n")){
                    for (int i=0;i<level;i++)
                        result.append('\t');
                }
                break;

            case Node.ELEMENT_NODE :

                result.append("<").append(aNode.getNodeName());
                result.append(">");

                if (aNode.hasChildNodes()){
                    NodeList children = aNode.getChildNodes();
                    for (int j = 0; j<children.getLength(); j++){
                        Node child = children.item(j);
                        prettyPrint(result, child, level+1);
                    }
                }

                result.append("</").append(aNode.getNodeName());
                result.append(">");
                break;
        }
    }


    private static String prettyPrint(String str){
        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(new ByteArrayInputStream(str.getBytes()));

            StringBuffer result = new StringBuffer();
            prettyPrint(result, doc, 0);

            return result.toString();
        } catch (Exception e){
            logger.warn("Warning in ValidationResult", e);
            System.out.println("Cant format output xml: " + e.getMessage());
        }
        return str;
    }

    public static void main(String[] args)
    {
        System.out.println(
                prettyPrint(
                        "<Failed>\n" +
                        "<ResultType>INVALID</ResultType>\n<Message>Cant validate signer certificate: Sertifika Sorunlu</Message>\n" +
                        "<Items>\n" +
                        "<Verified>\n" +
                        "<ResultType>VALID</ResultType>\n<Message>Signature value validated.</Message>\n" +
                        "</Verified>\n" +
                        "<Verified>\n" +
                        "<ResultType>VALID</ResultType>\n<Message>References are valid.</Message>\n" +
                        "</Verified>\n" +
                        "<Failed>\n" +
                        "<ResultType>INVALID</ResultType>\n<Message>Cant validate signer certificate: Sertifika Sorunlu</Message>\n" +
                        "<Items>\n" +
                        "</Items>\n" +
                        "</Failed>\n" +
                        "</Items>\n" +
                        "</Failed>"
                )
        );
    }

}
