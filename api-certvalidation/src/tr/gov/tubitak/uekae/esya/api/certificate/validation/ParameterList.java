package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>Politika olarak tanımlanan classların kullanacağı argüman listesinin tutulduğu yapı</p>
 * <p>{@link tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyClassInfo} classında class politika parametresi olarak
 * kullanılan bu yapı String-Object ikililerinden oluşan bir liste içermektedir.</p>
 *
 * @author IH
 */
public class ParameterList
{
	private Map<String, Object> mParameterList = new HashMap<String, Object>();

    public ParameterList()
	{

	}

	public ParameterList(Map<String, Object> aParameterList)
	{
		mParameterList = aParameterList;
	}

    public void addParameter(String aParamIsmi, Object aParamDeger)
	{
		mParameterList.put(aParamIsmi, aParamDeger);
	}

	public Object getParameter(String aParamIsmi)
	{
		return mParameterList.get(aParamIsmi);
	}

	public String getParameterAsString(String aParamIsmi)
	{
		Object value = mParameterList.get(aParamIsmi);
		if(value instanceof String)
		{
			return (String) value;
		}else
		{
			return null;
		}
	}

	public int getParameterInt(String aParamIsmi)
	{
		Object value = mParameterList.get(aParamIsmi);
		if (value instanceof String)
		{
			return Integer.parseInt((String) value);
		}else
		{
			return -1;
		}
	}

	public long getParameterLong(String aParamIsmi)
	{
		Object value = mParameterList.get(aParamIsmi);
		if (value instanceof String)
		{
			return Long.parseLong((String) value);
		}else
		{
			return -1;
		}
	}

	public boolean getParameterBoolean(String aParamIsmi) throws ESYARuntimeException
	{
		return getParameterBoolean(aParamIsmi, false);
	}


	public boolean getParameterBoolean(String aParamIsmi, boolean defaultValue) throws ESYARuntimeException
	{
		if(mParameterList.containsKey(aParamIsmi))
		{
			String value = mParameterList.get(aParamIsmi).toString();
			if(value.equalsIgnoreCase("true"))
				return true;

			if(value.equalsIgnoreCase("false"))
				return false;

			throw new ESYARuntimeException(aParamIsmi + " is not correctly set");
		}
		else
		{
			return defaultValue;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ParameterList {" + System.lineSeparator());
		Set<Map.Entry<String, Object>> entries = mParameterList.entrySet();
		for (Map.Entry<String, Object> entry : entries) {
			sb.append(entry.getKey() + " : " + entry.getValue().toString() + System.lineSeparator());
		}
		sb.append("}");
		return sb.toString();
	}
}
