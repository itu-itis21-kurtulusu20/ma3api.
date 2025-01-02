using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.config
{
    public class SmartCardConfigParser
    {
        private static readonly String CONFIG_FILE_NAME = "smartcard-config.xml";
    
        private static readonly String ELEMENT_CARD_TYPE = "card-type";
        private static readonly String ELEMENT_LIB = "lib";
        private static readonly String ELEMENT_ATR = "atr";
        
        private static readonly String ATTRIBUTE_NAME = "name";
        private static readonly String ATTRIBUTE_ARCH = "arch";
        private static readonly String ATTRIBUTE_VALUE = "value";
        
        private static readonly String ARCH_32_BIT = "32";
        private static readonly String ARCH_64_BIT = "64";

        public List<CardTypeConfig> readConfig()
        {
            try {
                return readConfig(new FileStream(CONFIG_FILE_NAME, FileMode.Open, FileAccess.Read));
            } catch (FileNotFoundException fnfx){
                throw new SmartCardException("Can not find smartcard config on run path!", fnfx);
            }
        }
        public List<CardTypeConfig> readConfig(Stream istrm)
        {
            List<CardTypeConfig> results = new List<CardTypeConfig>();
            try {
                                                             
                XmlDocument doc = new XmlDocument();
                doc.PreserveWhitespace = true;
                doc.Load(istrm);

                XmlNodeList cardTypeList = doc.GetElementsByTagName(ELEMENT_CARD_TYPE);

                foreach (XmlElement cardTypeElm in cardTypeList)
                {
                    String name = cardTypeElm.GetAttribute(ATTRIBUTE_NAME);
                    String libName = null, lib32Name = null, lib64Name = null;
                    List<String> atrs = new List<String>();

                    XmlNodeList libList = cardTypeElm.GetElementsByTagName(ELEMENT_LIB);

                    foreach (XmlElement libElm in libList)
                    {
                        String libNameTemp = libElm.GetAttribute(ATTRIBUTE_NAME);
                        String arch = libElm.GetAttribute(ATTRIBUTE_ARCH);

                        if (string.IsNullOrEmpty(arch))
                        {
                            libName = libNameTemp;
                        }
                        if (arch.Equals(ARCH_32_BIT))
                        {
                            lib32Name = libNameTemp;
                        }
                        else if (arch.Equals(ARCH_64_BIT))
                        {
                            lib64Name = libNameTemp;
                        }
                        else
                        {
                            libName = libNameTemp;
                        }
                    }

                    XmlNodeList atrList = cardTypeElm.GetElementsByTagName(ELEMENT_ATR);

                    foreach (XmlElement atrElm in atrList)
                    {                        
                        String atrValue = atrElm.GetAttribute(ATTRIBUTE_VALUE);
                        if (atrValue != null && atrValue.Length > 0)
                        {
                            atrs.Add(atrValue);
                        }
                    }
                    CardTypeConfig config = new CardTypeConfig(name, libName, lib32Name, lib64Name, atrs);
                    results.Add(config);
                    
                }
                         
            }
            catch (Exception x){
                throw new SmartCardException("Can not parse smartcard config!", x);
            }
            return results;
        }
    }
}
