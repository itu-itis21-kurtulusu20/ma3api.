using System;
using System.Collections.Generic;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * <p>Politika olarak tanımlanan classların kullanacağı argüman listesinin tutulduğu yapı</p>
     * <p>{@link tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyClassInfo} classında class politika parametresi olarak
     * kullanılan bu yapı String-Object ikililerinden oluşan bir liste içermektedir.</p>
     *
     * @author IH
     */
    public class ParameterList
    {
        private readonly Dictionary<String, Object> mParameterList = new Dictionary<String, Object>();

        public ParameterList()
        {

        }

        public ParameterList(Dictionary<String, Object> aParameterList)
        {
            mParameterList = aParameterList;
        }

        public void addParameter(String aParamIsmi, Object aParamDeger)
        {
            mParameterList[aParamIsmi] = aParamDeger;
        }

        public Object getParameter(String aParamIsmi)
        {
            Object value = null;
            mParameterList.TryGetValue(aParamIsmi, out value);
            return value;
        }

        public String getParameterAsString(String aParamIsmi)
        {
            Object value;
            mParameterList.TryGetValue(aParamIsmi, out value);
            if (value is String)
            {
                return (String)value;
            }
            else
            {
                return null;
            }
        }

        public int getParameterInt(String aParamIsmi)
        {
            Object value;
            mParameterList.TryGetValue(aParamIsmi, out value);
            if (value is String)
            {
                //return Integer.parseInt((String) value);
                return Convert.ToInt32((String)value);
            }
            else
            {
                return -1;
            }
        }

        public long getParameterLong(String aParamIsmi)
        {
            Object value;
            mParameterList.TryGetValue(aParamIsmi, out value);
            if (value is String)
            {
                //return Long.parseLong((String) value);
                return Convert.ToInt64((String)value);
            }
            else
            {
                return -1;
            }
        }

        public bool getParameterBoolean(String aParamIsmi)
        {
            return getParameterBoolean(aParamIsmi, false);
        }


        public bool getParameterBoolean(String aParamIsmi, bool defaultValue)
        {
            if (mParameterList.ContainsKey(aParamIsmi))
            {
                String value = mParameterList[aParamIsmi].ToString();
                if (value.Equals("true", StringComparison.OrdinalIgnoreCase))
                    return true;

                if (value.Equals("false", StringComparison.OrdinalIgnoreCase))
                    return false;

                throw new Exception(aParamIsmi + " is not correctly set");
            }
            else
            {
                return defaultValue;
            }
        }

        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();
            sb.Append("ParameterList" + Environment.NewLine + "{" + Environment.NewLine);

            foreach (var entry in mParameterList)
            {
                sb.Append(entry.Key + " : " + entry.Value.ToString() + Environment.NewLine);
            }

            sb.Append("}");
            return sb.ToString();
        }
    }
}
