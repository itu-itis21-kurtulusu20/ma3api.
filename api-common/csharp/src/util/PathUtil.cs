using System;

namespace tr.gov.tubitak.uekae.esya.api.common.util
{
    public static class PathUtil
    {
        public static String getRawPath(String aPath)
        {
            if (aPath == null)
            {
                return null;
            }
            String path = new String(aPath.ToCharArray());
            int beginIndex = path.IndexOf("%", StringComparison.Ordinal);
            while (beginIndex >= 0)
            {
                int lastIndex = path.IndexOf("%", beginIndex + 1, StringComparison.Ordinal);

                String envVariable = path.Substring(beginIndex + 1, lastIndex);
                String envVariableValue = Environment.GetEnvironmentVariable(envVariable);

                path = path.Replace(path.Substring(beginIndex, lastIndex + 1), envVariableValue);

                beginIndex = path.IndexOf("%", lastIndex + 1, StringComparison.Ordinal);
            }

            return path;
        }
    }
}
