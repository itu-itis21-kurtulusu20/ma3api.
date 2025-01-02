using System;
using System.Reflection;
using System.Xml;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.xml
{
    using Element = XmlElement;
    using Document = XmlDocument;
    using Node = XmlNode;

    public class XmlStoreUtil
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
	
	    public static void createXmlFromObject(Object obj, Element parent, Document document)
	    {
	        Type type = obj.GetType();
            FieldInfo[] fields = type.GetFields(BindingFlags.NonPublic | BindingFlags.Instance);
		    foreach (FieldInfo field in fields) 
		    {
			    try
			    {
                    //field.setAccessible(true);
				    Element element = document.CreateElement(field.Name);
				    element.AppendChild(document.CreateTextNode(dataToXmlString(field.GetValue(obj))));
				    parent.AppendChild(element);
			    }
			    catch (Exception e)
			    {
				    throw new ESYAException("Can not convert object to XML",e);
			    } 
		    }
	    }
    	
	    public static void fillObjectFromXml(Object obj, Element element)
	    {
            Type type = obj.GetType();
            FieldInfo[] fields = type.GetFields(/*BindingFlags.NonPublic | BindingFlags.Instance*/);
		    foreach (FieldInfo field in fields) 
		    {
			    try
			    {
				    //field.setAccessible(true);
				    String xmlStringValue = getElementStringFromName(element, field.Name);
				    setValueOfField(field, obj, xmlStringValue);
			    }
			    catch (Exception e)
			    {
				    logger.Error("Can not fill object",e);
			    } 
		    }
	    }

	    private static void setValueOfField(FieldInfo field, Object obj, String xmlStringValue)
	    {
		    if(field.FieldType.Equals(typeof(long)))
		    {
			    field.SetValue(obj, long.Parse(xmlStringValue));
		    }
    		
		    if(field.FieldType.Equals(typeof(DateTime)))
		    {
			    field.SetValue(obj, new DateTime(long.Parse(xmlStringValue)));
		    }
    		
		    if(field.FieldType.Equals(typeof(byte [])))
		    {
			    field.SetValue(obj, Base64.Decode(xmlStringValue));
		    }
    		
		    if(field.FieldType.Equals(typeof(String)))
		    {
			    field.SetValue(obj, xmlStringValue);
		    }
    		
		    if(field.FieldType.Equals(typeof(CertificateType)))
		    {
			    field.SetValue(obj, CertificateType.getNesne(int.Parse(xmlStringValue)));
		    }
    		
		    if(field.FieldType.Equals(typeof(SecurityLevel)))
		    {
			    field.SetValue(obj, SecurityLevel.getNesne(int.Parse(xmlStringValue)));
		    }
    		
	    }

	    private static String getElementStringFromName(XmlElement element, String name)
	    {
		    XmlNodeList mKokSertifikaNoNodeList = element.GetElementsByTagName(name);
		    Node mKokSertifikaNoNode = mKokSertifikaNoNodeList.Item(0);
		    return mKokSertifikaNoNode.FirstChild.Value;
	    }

	    private static String dataToXmlString(Object obj) 
	    {
    		
		    if(obj is long)
		    {
			    return obj.ToString();
		    }
		    if(obj is DateTime)
		    {
			    DateTime? date = (DateTime) obj;
			    return date.Value.ToString();
		    }
		    if(obj is byte [])
		    {
			    byte [] byteArr = (byte[]) obj;
			    return Base64.Encode(byteArr).ToString();
		    }
		    if(obj is CertificateType)
		    {
			    CertificateType sertifikaTipi = (CertificateType) obj;
			    return sertifikaTipi.getIntValue().ToString();
		    }
		    if(obj is SecurityLevel)
		    {
			    SecurityLevel guvenlikSeviyesi = (SecurityLevel) obj;
			    return guvenlikSeviyesi.getIntValue().ToString();
		    }
    		
		    return obj.ToString();
	    }
    }
}
