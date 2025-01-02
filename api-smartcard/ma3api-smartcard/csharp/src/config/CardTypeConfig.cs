using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.config
{
    public class CardTypeConfig
    {
        private readonly String name;
        private readonly String lib;
        private readonly String lib32;
        private readonly String lib64;
        private readonly List<String> atrs = new List<String>();

        public CardTypeConfig()
        {
        }

        public CardTypeConfig(String aName, String aLib, String aLib32, String aLib64, List<String> aAtrs)
        {
            name = aName;
            lib = aLib;
            lib32 = aLib32;
            lib64 = aLib64;
            if (aAtrs != null)
                atrs = aAtrs;
        }

        public String getName()
        {
            return name;
        }

        public String getLib()
        {
            return lib;
        }

        public String getLib32()
        {
            return lib32;
        }

        public String getLib64()
        {
            return lib64;
        }

        public List<String> getAtrs()
        {           
            return atrs;
        }
        
        public String ToString()
        {
            StringBuilder buffer = new StringBuilder()
                    .Append("[ Card type: ").Append(name).Append("\n")
                    .Append("lib : ").Append(lib).Append(", lib32 : ").Append(lib32).Append(", lib64 : ").Append(lib64).Append("\n");
            foreach (String atr in atrs){
                buffer.Append("  atr : ").Append(atr).Append("\n");
            }
            buffer.Append("]\n");
            return buffer.ToString();
        }
    }
}
