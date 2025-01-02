package tr.gov.tubitak.uekae.esya.api.smartcard.config;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.Application;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ayetgin
 */
public class SmartCardConfigParser
{
    private static final String CONFIG_FILE_NAME = "smartcard-config.xml";
    
    private static final String ELEMENT_CARD_TYPE = "card-type";
    private static final String ELEMENT_LIB = "lib";
    private static final String ELEMENT_ATR = "atr";
    
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_ARCH = "arch";
    private static final String ATTRIBUTE_VALUE = "value";
    
    private static final String ARCH_32_BIT = "32";
    private static final String ARCH_64_BIT = "64";

    
    public List<CardTypeConfig> readConfig() throws SmartCardException
    {
        try {
            return readConfig(new FileInputStream(CONFIG_FILE_NAME));
        } catch (FileNotFoundException fnfx){
            throw new SmartCardException("Can not find smartcard config on run path!", fnfx);
        }
    }

    public List<CardTypeConfig> readConfig(InputStream is) throws SmartCardException
    {
        List<CardTypeConfig> results = new ArrayList<CardTypeConfig>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            org.w3c.dom.Document doc = db.parse(is);

            doc.getDocumentElement();

            NodeList cardTypeList = doc.getDocumentElement().getElementsByTagName(ELEMENT_CARD_TYPE);

            for (int cartTypeIndex=0; cartTypeIndex<cardTypeList.getLength(); cartTypeIndex++){
                Element cardTypeElm = (Element)cardTypeList.item(cartTypeIndex);
                String name = cardTypeElm.getAttribute(ATTRIBUTE_NAME);
                String libName=null, lib32Name=null, lib64Name=null;
                List<String> atrs = new ArrayList<String>();

                NodeList libList = cardTypeElm.getElementsByTagName(ELEMENT_LIB);
                for (int libIndex=0; libIndex<libList.getLength(); libIndex++){
                    Element libElm = (Element)libList.item(libIndex);
                    String libNameTemp = libElm.getAttribute(ATTRIBUTE_NAME);
                    String arch = libElm.getAttribute(ATTRIBUTE_ARCH);
                    if (arch==null){
                        libName = libNameTemp;
                    }
                    else if (arch.equals(ARCH_32_BIT)){
                        lib32Name = libNameTemp;
                    }
                    else  if (arch.equals(ARCH_64_BIT)){
                        lib64Name = libNameTemp;
                    }
                    else {
                        libName = libNameTemp;
                    }
                }

                NodeList atrList = cardTypeElm.getElementsByTagName(ELEMENT_ATR);
                for (int atrIndex=0; atrIndex<atrList.getLength(); atrIndex++){
                    Element atrElm = (Element)atrList.item(atrIndex);
                    String atrValue = atrElm.getAttribute(ATTRIBUTE_VALUE);
                    if (atrValue!=null && atrValue.length()>0){
                        atrs.add(atrValue);
                    }
                }
                CardTypeConfig config = new CardTypeConfig(name, libName, lib32Name, lib64Name, atrs);
                results.add(config);
            }

        }
        catch (Exception x){
            throw new SmartCardException("Can not parse smartcard config!", x);
        }
        return results;
    }

    public static void main(String[] args) throws SmartCardException
    {
        List<CardTypeConfig> cards = new SmartCardConfigParser().readConfig();
        System.out.println(cards);
        CardType.applyCardTypeConfig(cards);
        CardType found = CardType.getCardTypeFromATR("3BF2180002C10A31FE58C80874", Application.ESIGNATURE);
        System.out.println(found);
        System.out.println(found.getLibName());

    }
}
