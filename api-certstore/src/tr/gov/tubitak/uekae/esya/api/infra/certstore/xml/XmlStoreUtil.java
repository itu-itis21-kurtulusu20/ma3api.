package tr.gov.tubitak.uekae.esya.api.infra.certstore.xml;

import java.lang.reflect.Field;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.GuvenlikSeviyesi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.SertifikaTipi;

public class XmlStoreUtil 
{
	private static final Logger logger = LoggerFactory.getLogger(XmlStoreUtil.class);
	
	public static void createXmlFromObject(Object obj, Element parent, Document document) throws ESYAException
	{
		Class clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) 
		{
			try
			{
				field.setAccessible(true);
				Element element = document.createElement(field.getName());
				element.appendChild(document.createTextNode(dataToXmlString(field.get(obj))));
				parent.appendChild(element);
			}
			catch (Exception e)
			{
				throw new ESYAException("Can not convert object to XML",e);
			} 
		}
	}
	
	public static void fillObjectFromXml(Object obj, Element element) throws ESYAException
	{
		Class clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) 
		{
			try
			{
				field.setAccessible(true);
				String xmlStringValue = getElementStringFromName(element, field.getName());
				setValueOfField(field, obj, xmlStringValue);
				
			}
			catch (Exception e)
			{
				logger.error("Can not fill object",e);
			} 
		}
	}

	private static void setValueOfField(Field field, Object obj, String xmlStringValue) throws ESYAException
	{
		try {
			if(field.getType().equals(Long.class))
			{
				field.set(obj, Long.parseLong(xmlStringValue));
			}

			if(field.getType().equals(Date.class))
			{
				field.set(obj, new Date(Long.parseLong(xmlStringValue)));
			}

			if(field.getType().equals(byte [].class))
			{
				field.set(obj, Base64.decode(xmlStringValue));
			}

			if(field.getType().equals(String.class))
			{
				field.set(obj, xmlStringValue);
			}

			if(field.getType().equals(SertifikaTipi.class))
			{
				field.set(obj, SertifikaTipi.getNesne(Integer.parseInt(xmlStringValue)));
			}

			if(field.getType().equals(GuvenlikSeviyesi.class))
			{
				field.set(obj, GuvenlikSeviyesi.getNesne(Integer.parseInt(xmlStringValue)));
			}

		}catch (Exception e){
			throw new ESYAException(e);
		}
	}

	private static String getElementStringFromName(Element element, String name)
	{
		NodeList mKokSertifikaNoNodeList = element.getElementsByTagName(name);
		Node mKokSertifikaNoNode = mKokSertifikaNoNodeList.item(0);
		return mKokSertifikaNoNode.getChildNodes().item(0).getNodeValue();
	}

	private static String dataToXmlString(Object obj) 
	{
		
		if(obj instanceof Long)
		{
			return obj.toString();
		}
		if(obj instanceof Date)
		{
			Date date = (Date) obj;
			return Long.toString(date.getTime());
		}
		if(obj instanceof byte [])
		{
			byte [] byteArr = (byte[]) obj;
			return Base64.encode(byteArr);
		}
		if(obj instanceof SertifikaTipi)
		{
			SertifikaTipi sertifikaTipi = (SertifikaTipi) obj;
			return Integer.toString(sertifikaTipi.getIntValue());
		}
		if(obj instanceof GuvenlikSeviyesi)
		{
			GuvenlikSeviyesi guvenlikSeviyesi = (GuvenlikSeviyesi) obj;
			return Integer.toString(guvenlikSeviyesi.getIntValue());
		}
		
		return obj.toString();
	}
	
	
}
