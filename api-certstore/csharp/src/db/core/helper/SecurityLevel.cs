
using System;
using System.Reflection;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{

    public class SecurityLevel
    {
        public static readonly SecurityLevel LEGAL = new SecurityLevel(_enum.LEGAL, "legal");
        public static readonly SecurityLevel ORGANIZATIONAL = new SecurityLevel(_enum.ORGANIZATIONAL, "organizational");
        public static readonly SecurityLevel PERSONAL = new SecurityLevel(_enum.PERSONAL, "personal");

        enum _enum
        {
            LEGAL = 1,
            ORGANIZATIONAL = 2,
            PERSONAL = 3
        }

        private readonly _enum mValue;
        private readonly String mDefinition;

        SecurityLevel(_enum aValue, String aDefinition)
        {
            mValue = aValue;
            mDefinition = aDefinition;
        }

        public int getIntValue()
        {
            return (int)mValue;
        }

        public String getDefinition()
        {
            return mDefinition;
        }

        //TODO default kismina bak
        public static SecurityLevel getNesne(int aTip)
        {
            switch (aTip)
            {
                case 1: return SecurityLevel.LEGAL;
                case 2: return SecurityLevel.ORGANIZATIONAL;
                case 3: return SecurityLevel.PERSONAL;
                default: return null;
            }
        }
        public static SecurityLevel getNesne(long aTip)
        {
            return getNesne((int)aTip);
        }

        public static SecurityLevel getNesne(String aDefinition)
        {
            FieldInfo[] values = typeof(SecurityLevel).GetFields(BindingFlags.Public | BindingFlags.Static);
            foreach (FieldInfo value in values)
            {
                SecurityLevel eachValue = (SecurityLevel)value.GetValue(null);
                if (eachValue.getDefinition().Equals(aDefinition))
                    return eachValue;
            }

            throw new CertStoreException("Unknown Security Level");
        }

        public override bool Equals(object obj)
        {
            // If parameter cannot be cast to Point return false.
            SecurityLevel p = obj as SecurityLevel;
            if (p == null)
            {
                return false;
            }

            // Return true if the fields match:           
            return ((int)mValue).Equals((int)mValue);
        }

        public bool Equals(SecurityLevel p)
        {
            // If parameter is null return false:
            if (p == null)
            {
                return false;
            }
            // Return true if the fields match:
            return ((int)mValue).Equals((int)p.mValue);
        }

        public override int GetHashCode()
        {
            return mValue.GetHashCode();
        }
    }

}
