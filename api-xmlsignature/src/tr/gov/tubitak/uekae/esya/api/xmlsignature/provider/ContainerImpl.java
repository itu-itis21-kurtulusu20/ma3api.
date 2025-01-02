package tr.gov.tubitak.uekae.esya.api.xmlsignature.provider;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.Context;
import tr.gov.tubitak.uekae.esya.api.signature.impl.AbstractSignatureContainer;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.StreamingDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlSigDetector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * @author ayetgin
 */
public class ContainerImpl extends AbstractSignatureContainer
{
    private SignedDocument signedDocument;
    private tr.gov.tubitak.uekae.esya.api.xmlsignature.Context xmlContext;
    public ContainerImpl()
    {
    }

    public Signature createSignature(ECertificate certificate)
    {
        XMLSignature xmlSignature = signedDocument.createSignature();
        //tr.gov.tubitak.uekae.esya.api.xmlsignature.Context c = XMLProviderUtil.convert(context);
        //XMLSignature xmlSignature = new XMLSignature(c);
        xmlSignature.addKeyInfo(certificate);

        Signature created = new SignatureImpl(this, xmlSignature, null);
        rootSignatures.add(created);

        return created;
    }

    public void addExternalSignature(Signature signature) throws SignatureException
    {
        if (!(signature instanceof SignatureImpl))
            throw new SignatureRuntimeException("Unknown CMS Signature impl! "+signature.getClass());

        super.addExternalSignature(signature);
        signedDocument.addSignature(((SignatureImpl)signature).getInternalSignature());
    }

    public void detachSignature(Signature signature) throws SignatureException
    {
        try {
            SignatureImpl xmlSignature = (SignatureImpl)signature;
            signedDocument.removeSignature(xmlSignature.getInternalSignature());
            getSignatures().remove(signature);
        }
        catch (Exception x){
            throw new SignatureException("Cant extract signature form container "+ signature.getClass(), x);
        }
    }

    public SignatureFormat getSignatureFormat()
    {
        return SignatureFormat.XAdES;
    }

    public void setContext(Context context)
    {
        super.setContext(context);
        xmlContext = XMLProviderUtil.convert(context);
        if (signedDocument==null) {
            signedDocument = new SignedDocument(xmlContext);
        }
    }



    public boolean isSignatureContainer(InputStream stream)
    {
        XmlSigDetector detector = new XmlSigDetector();
        return detector.isSignature(stream);
    }

    public void read(InputStream stream) throws SignatureException
    {
        try {
            Document doc = new StreamingDocument(stream, xmlContext.getBaseURI().toString(), "text/xml", null);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document domDocument = db.parse(new ByteArrayInputStream(doc.getBytes()));
            Element element = domDocument.getDocumentElement();
            if (element.getLocalName().equals("envelope")){
                signedDocument = new SignedDocument(doc, xmlContext);
            }
            else if (element.getLocalName().equals(Constants.TAG_SIGNATURE)){
                XMLSignature signature = new XMLSignature(domDocument.getDocumentElement(), xmlContext);
                xmlContext.setDocument(null);
                signedDocument = new SignedDocument(xmlContext);
                signedDocument.addSignature(signature);
            }

            for(XMLSignature s :  signedDocument.getRootSignatures()){
                rootSignatures.add(new SignatureImpl(this, s, null));
            }
        } catch (Exception x){
            throw new SignatureException(x);
        }

    }

    public void write(OutputStream stream) throws SignatureException
    {
        if (signedDocument.getRootSignatureCount()==1)
            signedDocument.getSignature(0).write(stream);
        else
            signedDocument.write(stream);
    }

    public Object getUnderlyingObject()
    {
        return signedDocument;
    }

    public static void main(String[] args) throws Exception
    {
        String cert64 = "MIIExTCCA62gAwIBAgICCngwDQYJKoZIhvcNAQEFBQAwfDETMBEGCgmSJomT8ixkARkWA05FVDES\n" +
                "MBAGCgmSJomT8ixkARkWAlVHMRIwEAYDVQQKDAlUw5xCxLBUQUsxDjAMBgNVBAsMBVVFS0FFMS0w\n" +
                "KwYDVQQDDCTDnHLDvG4gR2VsacWfdGlybWUgU2VydGlmaWthIE1ha2FtxLEwHhcNMTExMTI1MDgw\n" +
                "MjE4WhcNMTMxMTI0MDgwMjE4WjAXMRUwEwYDVQQDDAxBaG1ldCBZZXRnaW4wgZ8wDQYJKoZIhvcN\n" +
                "AQEBBQADgY0AMIGJAoGBAIBIVZmMrnpHPDhKxqDWfCi2HROs34q8y24WNqdu6I16OpuliGBsHucD\n" +
                "e/MQ/lWrO2qvrWBvQx/2Nmky1Ssgv/2VRgwqnQKScGQhmLC3AQaJRhaukWNUTmXvS0f2evvv8tzV\n" +
                "gbDnVDaPf2tzfh9vmR7gminm8MPWKeVrV809RjshAgMBAAGjggI4MIICNDAfBgNVHSMEGDAWgBT8\n" +
                "6E7NnJEHLKFB2Txhcn+VEC/3qzAdBgNVHQ4EFgQU7LoWDaNDhYOBcEtqhhAqAOJZJYEwDgYDVR0P\n" +
                "AQH/BAQDAgUgMAkGA1UdEwQCMAAwgbMGA1UdHwSBqzCBqDAioCCgHoYcaHR0cDovL2RlcG8udWcu\n" +
                "bmV0L1VHU0lMLmNybDCBgaB/oH2Ge2xkYXA6Ly91Zy5uZXQvQ049VUcgU00sQ049VUcsT1U9VUVL\n" +
                "QUUsTz1U3EIwVEFLLERDPVVHLERDPU5FVD9jZXJ0ZmljYXRlUmV2b2NhdGlvbkxpc3Q/YmFzZT9v\n" +
                "YmplY3RjbGFzcz1jUkxEaXN0cmlidXRpb25Qb2ludDCB2wYIKwYBBQUHAQEEgc4wgcswKAYIKwYB\n" +
                "BQUHMAKGHGh0dHA6Ly9kZXBvLnVnLm5ldC9VR0tPSy5jcnQwfgYIKwYBBQUHMAKGcmxkYXA6Ly91\n" +
                "Zy5uZXQvQ049VUcgU00sQ049VUcsT1U9VUVLQUUsTz1U3EIwVEFLLERDPVVHLERDPU5FVD9jQUNl\n" +
                "cnRpZmljYXRlP2Jhc2U/b2JqZWN0Y2xhc3M9Y2VydGlmaWNhdGlvbkF1dGhvcml0eTAfBggrBgEF\n" +
                "BQcwAYYTaHR0cDovL29jc3AudWcubmV0LzBDBgNVHREEPDA6gRNhaG1ldC55ZXRnaW5AdWcubmV0\n" +
                "oCMGCisGAQQBgjcUAgOgFQwTYWhtZXQueWV0Z2luQHVnLm5ldDANBgkqhkiG9w0BAQUFAAOCAQEA\n" +
                "X/7ytDkWZaHChcnS3g25WPU9X0fbi/H4kY0fBemiSziVxPvDP9L82DQIzXd+qXl5jTMYSfYZdeld\n" +
                "lmd6hd5DJ7xE1sI8p80wENWF9y61pY0XjPhMS3Zumnkd4AtoXz5XOtIZUW6tPcKXn3DkEuBBYYr9\n" +
                "YnKKcroAGZycTSvgsimtUc+CAX8CgxFaGIIn/WdiFzlQ9IrOgq7evCnZsuEk6B9fRBHKaHU+1T14\n" +
                "O9coIXVHfOLbhSL8+pmiOeQNpzz/nGBX+HOt4PrFuQspIq8gsJCpyn5Yisbq/yOrYcCuhZh07ldE\n" +
                "25OimWEaW0+vceBTGs8Aq5gXSaIq41OBqu+r1g==";

        byte[] bytes = Base64.decode(cert64);
        byte[] bytes2 = new byte[]{48,-126,4,-59,48,-126,3,-83,-96,3,2,1,2,2,2,10,120,48,13,6,9,42,-122,72,-122,-9,13,1,1,5,5,0,48,124,49,19,48,17,6,10,9,-110,38,-119,-109,-14,44,100,1,25,22,3,78,69,84,49,18,48,16,6,10,9,-110,38,-119,-109,-14,44,100,1,25,22,2,85,71,49,18,48,16,6,3,85,4,10,12,9,84,-61,-100,66,-60,-80,84,65,75,49,14,48,12,6,3,85,4,11,12,5,85,69,75,65,69,49,45,48,43,6,3,85,4,3,12,36,-61,-100,114,-61,-68,110,32,71,101,108,105,-59,-97,116,105,114,109,101,32,83,101,114,116,105,102,105,107,97,32,77,97,107,97,109,-60,-79,48,30,23,13,49,49,49,49,50,53,48,56,48,50,49,56,90,23,13,49,51,49,49,50,52,48,56,48,50,49,56,90,48,23,49,21,48,19,6,3,85,4,3,12,12,65,104,109,101,116,32,89,101,116,103,105,110,48,-127,-97,48,13,6,9,42,-122,72,-122,-9,13,1,1,1,5,0,3,-127,-115,0,48,-127,-119,2,-127,-127,0,-128,72,85,-103,-116,-82,122,71,60,56,74,-58,-96,-42,124,40,-74,29,19,-84,-33,-118,-68,-53,110,22,54,-89,110,-24,-115,122,58,-101,-91,-120,96,108,30,-25,3,123,-13,16,-2,85,-85,59,106,-81,-83,96,111,67,31,-10,54,105,50,-43,43,32,-65,-3,-107,70,12,42,-99,2,-110,112,100,33,-104,-80,-73,1,6,-119,70,22,-82,-111,99,84,78,101,-17,75,71,-10,122,-5,-17,-14,-36,-43,-127,-80,-25,84,54,-113,127,107,115,126,31,111,-103,30,-32,-102,41,-26,-16,-61,-42,41,-27,107,87,-51,61,70,59,33,2,3,1,0,1,-93,-126,2,56,48,-126,2,52,48,31,6,3,85,29,35,4,24,48,22,-128,20,-4,-24,78,-51,-100,-111,7,44,-95,65,-39,60,97,114,127,-107,16,47,-9,-85,48,29,6,3,85,29,14,4,22,4,20,-20,-70,22,13,-93,67,-123,-125,-127,112,75,106,-122,16,42,0,-30,89,37,-127,48,14,6,3,85,29,15,1,1,-1,4,4,3,2,5,32,48,9,6,3,85,29,19,4,2,48,0,48,-127,-77,6,3,85,29,31,4,-127,-85,48,-127,-88,48,34,-96,32,-96,30,-122,28,104,116,116,112,58,47,47,100,101,112,111,46,117,103,46,110,101,116,47,85,71,83,73,76,46,99,114,108,48,-127,-127,-96,127,-96,125,-122,123,108,100,97,112,58,47,47,117,103,46,110,101,116,47,67,78,61,85,71,32,83,77,44,67,78,61,85,71,44,79,85,61,85,69,75,65,69,44,79,61,84,-36,66,48,84,65,75,44,68,67,61,85,71,44,68,67,61,78,69,84,63,99,101,114,116,102,105,99,97,116,101,82,101,118,111,99,97,116,105,111,110,76,105,115,116,63,98,97,115,101,63,111,98,106,101,99,116,99,108,97,115,115,61,99,82,76,68,105,115,116,114,105,98,117,116,105,111,110,80,111,105,110,116,48,-127,-37,6,8,43,6,1,5,5,7,1,1,4,-127,-50,48,-127,-53,48,40,6,8,43,6,1,5,5,7,48,2,-122,28,104,116,116,112,58,47,47,100,101,112,111,46,117,103,46,110,101,116,47,85,71,75,79,75,46,99,114,116,48,126,6,8,43,6,1,5,5,7,48,2,-122,114,108,100,97,112,58,47,47,117,103,46,110,101,116,47,67,78,61,85,71,32,83,77,44,67,78,61,85,71,44,79,85,61,85,69,75,65,69,44,79,61,84,-36,66,48,84,65,75,44,68,67,61,85,71,44,68,67,61,78,69,84,63,99,65,67,101,114,116,105,102,105,99,97,116,101,63,98,97,115,101,63,111,98,106,101,99,116,99,108,97,115,115,61,99,101,114,116,105,102,105,99,97,116,105,111,110,65,117,116,104,111,114,105,116,121,48,31,6,8,43,6,1,5,5,7,48,1,-122,19,104,116,116,112,58,47,47,111,99,115,112,46,117,103,46,110,101,116,47,48,67,6,3,85,29,17,4,60,48,58,-127,19,97,104,109,101,116,46,121,101,116,103,105,110,64,117,103,46,110,101,116,-96,35,6,10,43,6,1,4,1,-126,55,20,2,3,-96,21,12,19,97,104,109,101,116,46,121,101,116,103,105,110,64,117,103,46,110,101,116,48,13,6,9,42,-122,72,-122,-9,13,1,1,5,5,0,3,-126,1,1,0,95,-2,-14,-76,57,22,101,-95,-62,-123,-55,-46,-34,13,-71,88,-11,61,95,71,-37,-117,-15,-8,-111,-115,31,5,-23,-94,75,56,-107,-60,-5,-61,63,-46,-4,-40,52,8,-51,119,126,-87,121,121,-115,51,24,73,-10,25,117,-23,93,-106,103,122,-123,-34,67,39,-68,68,-42,-62,60,-89,-51,48,16,-43,-123,-9,46,-75,-91,-115,23,-116,-8,76,75,118,110,-102,121,29,-32,11,104,95,62,87,58,-46,25,81,110,-83,61,-62,-105,-97,112,-28,18,-32,65,97,-118,-3,98,114,-118,114,-70,0,25,-100,-100,77,43,-32,-78,41,-83,81,-49,-126,1,127,2,-125,17,90,24,-126,39,-3,103,98,23,57,80,-12,-118,-50,-126,-82,-34,-68,41,-39,-78,-31,36,-24,31,95,68,17,-54,104,117,62,-43,61,120,59,-41,40,33,117,71,124,-30,-37,-123,34,-4,-6,-103,-94,57,-28,13,-89,60,-1,-100,96,87,-8,115,-83,-32,-6,-59,-71,11,41,34,-81,32,-80,-112,-87,-54,126,88,-118,-58,-22,-1,35,-85,97,-64,-82,-123,-104,116,-18,87,68,-37,-109,-94,-103,97,26,91,79,-81,113,-32,83,26,-49,0,-85,-104,23,73,-94,42,-29,83,-127,-86,-17,-85,-42};

        System.out.println(Arrays.equals(bytes, bytes2));
        System.out.println(bytes.length);
        ECertificate certificate = new ECertificate(bytes2);

        System.out.println(certificate);
    }
}
