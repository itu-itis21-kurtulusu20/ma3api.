using System;
using System.Globalization;
using System.IO;
using System.Text;
/**
 * @author $Author: mullan $
 */
namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    public static class LDAPDNUtil
    {
        /** {@link org.apache.commons.logging} logging facility */
        /* static org.apache.commons.logging.Log log =
             org.apache.commons.logging.LogFactory.getLog(RFC2253Parser.class.getName());
        */

        static bool _TOXML = true;

        /**
         * Method rfc2253toXMLdsig
         *
         * @param dn
         * @return normalized string
         *
         */
        public static string rfc2253toXMLdsig(string dn)
        {

            _TOXML = true;

            // Transform from RFC1779 to RFC2253
            string normalized = normalize(dn);

            return rfctoXML(normalized);
        }

        /**
         * Method xmldsigtoRFC2253
         *
         * @param dn
         * @return normalized string
         */
        public static string xmldsigtoRFC2253(string dn)
        {

            _TOXML = false;

            // Transform from RFC1779 to RFC2253
            string normalized = normalize(dn);

            return xmltoRFC(normalized);
        }

        /**
         * Method normalize
         *
         * @param dn
         * @return normalized string
         */
        public static string normalize(string dn)
        {

            //if empty string
            if ((dn == null) || dn.Equals(""))
            {
                return "";
            }

            try
            {
                string _DN = semicolonToComma(dn);
                StringBuilder sb = new StringBuilder();
                int i = 0;
                int l = 0;
                int k;

                //for name component
                for (int j = 0; (k = _DN.IndexOf(",", j, StringComparison.Ordinal)) >= 0; j = k + 1)
                {
                    l += countQuotes(_DN, j, k);

                    if ((k <= 0) || (_DN[k - 1] == '\\') || (l%2) == 1) continue;
                    sb.Append(parseRDN(_DN.Substring(i, k - i).Trim()) + ",");

                    i = k + 1;
                    l = 0;
                }

                sb.Append(parseRDN(trim(_DN.Substring(i))));

                return sb.ToString();
            }
            catch (IOException)
            {
                return dn;
            }
        }

        /**
         * Method parseRDN
         *
         * @param str
         * @return normalized string
         * @throws IOException
         */
        private static string parseRDN(string str)
        {

            StringBuilder sb = new StringBuilder();
            int i = 0;
            int l = 0;
            int k;

            for (int j = 0; (k = str.IndexOf("+", j, StringComparison.Ordinal)) >= 0; j = k + 1)
            {
                l += countQuotes(str, j, k);

                if ((k <= 0) || (str[k - 1] == '\\') || (l%2) == 1) continue;
                sb.Append(parseATAV(trim(str.Substring(i, k-i))) + "+");

                i = k + 1;
                l = 0;
            }

            sb.Append(parseATAV(trim(str.Substring(i))));

            return sb.ToString();
        }

        /**
         * Method parseATAV
         *
         * @param str
         * @return normalized string
         * @throws IOException
         */
        private static string parseATAV(string str)
        {

            int i = str.IndexOf("=", StringComparison.Ordinal);

            if ((i == -1) || ((i > 0) && (str[i - 1] == '\\')))
            {
                return str;
            }
            string attrType = normalizeAT(str.Substring(0, i));
            // only normalize if value is a String
            string attrValue;
            if (attrType[0] >= '0' && attrType[0] <= '9')
            {
                attrValue = str.Substring(i + 1);
            }
            else
            {
                attrValue = normalizeV(str.Substring(i + 1));
            }

            return attrType + "=" + attrValue;

        }

        /**
         * Method normalizeAT
         *
         * @param str
         * @return normalized string
         */
        private static string normalizeAT(string str)
        {

            string at = str.ToUpper().Trim();

            if (at.StartsWith("OID"))
            {
                at = at.Substring(3);
            }

            return at;
        }

        /**
         * Method normalizeV
         *
         * @param str
         * @return normalized string
         * @throws IOException
         */
        private static string normalizeV(string str)
        {

            string value = trim(str);

            if (value.StartsWith("\""))
            {
                StringBuilder sb = new StringBuilder();
                StringReader sr = new StringReader(value.Substring(1,
                                     value.Length - 1 -1));
                int i;

                for (; (i = sr.Read()) > -1; )
                {
                    var c = (char)i;

                    //the following char is defined at 4.Relationship with RFC1779 and LDAPv2 inrfc2253
                    if ((c == ',') || (c == '=') || (c == '+') || (c == '<')
                            || (c == '>') || (c == '#') || (c == ';'))
                    {
                        sb.Append('\\');
                    }

                    sb.Append(c);
                }

                value = trim(sb.ToString());
            }

            if (_TOXML)
            {
                if (value.StartsWith("#"))
                {
                    value = '\\' + value;
                }
            }
            else
            {
                if (value.StartsWith("\\#"))
                {
                    value = value.Substring(1);
                }
            }

            return value;
        }

        /**
         * Method rfctoXML
         *
         * @param string
         * @return normalized string
         */
        private static string rfctoXML(string string_)
        {

            try
            {
                string s = changeLess32toXML(string_);

                return changeWStoXML(s);
            }
            catch (Exception)
            {
                return string_;
            }
        }

        /**
         * Method xmltoRFC
         *
         * @param string
         * @return normalized string
         */
        private static string xmltoRFC(string string_)
        {

            try
            {
                string s = changeLess32toRFC(string_);

                return changeWStoRFC(s);
            }
            catch (Exception)
            {
                return string_;
            }
        }

        /**
         * Method changeLess32toRFC
         *
         * @param string
         * @return normalized string
         * @throws IOException
         */
        private static string changeLess32toRFC(string string_)
        {

            StringBuilder sb = new StringBuilder();
            StringReader sr = new StringReader(string_);
            int i;

            for (; (i = sr.Read()) > -1; )
            {
                var c = (char)i;

                if (c == '\\')
                {
                    sb.Append(c);

                    char c1 = (char)sr.Read();
                    char c2 = (char)sr.Read();

                    //65 (A) 97 (a)
                    if ((((c1 >= 48) && (c1 <= 57)) || ((c1 >= 65) && (c1 <= 70)) || ((c1 >= 97) && (c1 <= 102)))
                            && (((c2 >= 48) && (c2 <= 57))
                                || ((c2 >= 65) && (c2 <= 70))
                                || ((c2 >= 97) && (c2 <= 102))))
                    {
                        char ch = (char)byte.Parse("" + c1 + c2, NumberStyles.HexNumber);

                        sb.Append(ch);
                    }
                    else
                    {
                        sb.Append(c1);
                        sb.Append(c2);
                    }
                }
                else
                {
                    sb.Append(c);
                }
            }

            return sb.ToString();
        }

        /**
         * Method changeLess32toXML
         *
         * @param string
         * @return normalized string
         * @throws IOException
         */
        private static string changeLess32toXML(string string_)
        {

            StringBuilder sb = new StringBuilder();
            StringReader sr = new StringReader(string_);
            int i;

            for (; (i = sr.Read()) > -1; )
            {
                if (i < 32)
                {
                    sb.Append('\\');
                    sb.Append(i.ToString("X"));
                }
                else
                {
                    sb.Append((char)i);
                }
            }

            return sb.ToString();
        }

        /**
         * Method changeWStoXML
         *
         * @param string
         * @return normalized string
         * @throws IOException
         */
        private static string changeWStoXML(string string_)
        {

            StringBuilder sb = new StringBuilder();
            StringReader sr = new StringReader(string_);
            int i;

            for (; (i = sr.Read()) > -1; )
            {
                var c = (char)i;

                if (c == '\\')
                {
                    char c1 = (char)sr.Read();

                    if (c1 == ' ')
                    {
                        sb.Append('\\');

                        string s = "20";

                        sb.Append(s);
                    }
                    else
                    {
                        sb.Append('\\');
                        sb.Append(c1);
                    }
                }
                else
                {
                    sb.Append(c);
                }
            }

            return sb.ToString();
        }

        /**
         * Method changeWStoRFC
         *
         * @param string
         * @return normalized string
         */
        private static string changeWStoRFC(string string_)
        {

            StringBuilder sb = new StringBuilder();
            int i = 0;
            int k;

            for (int j = 0; (k = string_.IndexOf("\\20", j, StringComparison.Ordinal)) >= 0; j = k + 3)
            {
                sb.Append(trim(string_.Substring(i, k - i)) + "\\ ");

                i = k + 3;
            }

            sb.Append(string_.Substring(i));

            return sb.ToString();
        }

        /**
         * Method semicolonToComma
         *
         * @param str
         * @return normalized string
         */
        private static string semicolonToComma(string str)
        {
            return removeWSandReplace(str, ";", ",");
        }

        /**
         * Method removeWhiteSpace
         *
         * @param str
         * @param symbol
         * @return normalized string
         */
        static string removeWhiteSpace(string str, string symbol)
        {
            return removeWSandReplace(str, symbol, symbol);
        }

        /**
         * Method removeWSandReplace
         *
         * @param str
         * @param symbol
         * @param replace
         * @return normalized string
         */
        private static string removeWSandReplace(string str, string symbol, string replace)
        {

            StringBuilder sb = new StringBuilder();
            int i = 0;
            int l = 0;
            int k;

            for (int j = 0; (k = str.IndexOf(symbol, j, StringComparison.Ordinal)) >= 0; j = k + 1)
            {
                l += countQuotes(str, j, k);

                if ((k <= 0) || (str[k - 1] == '\\') || (l%2) == 1) continue;
                sb.Append(trim(str.Substring(i, k -i)) + replace);

                i = k + 1;
                l = 0;
            }

            sb.Append(trim(str.Substring(i)));

            return sb.ToString();
        }

        /**
         * Returns the number of Quotation from i to j
         *
         * @param s
         * @param i
         * @param j
         * @return number of quotes
         */
        private static int countQuotes(string s, int i, int j)
        {

            int k = 0;

            for (int l = i; l < j; l++)
            {
                if (s[l] == '"')
                {
                    k++;
                }
            }

            return k;
        }

        //only for the end of a space character occurring at the end of the string from rfc2253

        /**
         * Method trim
         *
         * @param str
         * @return the string
         */
        private static string trim(string str)
        {

            string trimed = str.Trim();
            int i = str.IndexOf(trimed, StringComparison.Ordinal) + trimed.Length;

            if ((str.Length <= i) || !trimed.EndsWith("\\") || trimed.EndsWith("\\\\")) return trimed;
            if (str[i] == ' ')
            {
                trimed = trimed + " ";
            }

            return trimed;
        }
    }
}
