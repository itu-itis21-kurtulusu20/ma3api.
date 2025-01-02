using System;
using System.IO;
using System.Text.RegularExpressions;

namespace tr.gov.tubitak.uekae.esya.api.common.tools
{
    public class IniFile
    {
        private readonly Regex _iniKeyValuePatternRegex;

        public IniFile(string iniFileName)
        {
            _iniKeyValuePatternRegex = new Regex(
                @"((\s)*(?<Key>([^\=^\s^\n]+))[\s^\n]*
                # key part (surrounding whitespace stripped)
                \=
                (\s)*(?<Value>([^\n^\s]+(\n){0,1})))
                # value part (surrounding whitespace stripped)
                ",
                RegexOptions.IgnorePatternWhitespace |
                RegexOptions.Compiled |
                RegexOptions.CultureInvariant);

            _iniFileName = iniFileName;
        }

        public string ParseFileReadValue(string key)
        {
            using (StreamReader reader =
                  new StreamReader(_iniFileName))
            {
                do
                {
                    string line = reader.ReadLine();
                    if (line == null) continue;
                    var match =
                        _iniKeyValuePatternRegex.Match(line);
                    if (!match.Success) continue;
                    string currentKey =
                        match.Groups["Key"].Value;
                    if (string.Compare(currentKey.Trim(), key, StringComparison.Ordinal) != 0) continue;
                    string value =
                        match.Groups["Value"].Value;
                    return value;
                }
                while (reader.Peek() != -1);
            }
            return null;
        }

        public string IniFileName
        {
            get { return _iniFileName; }
        }
        private readonly string _iniFileName;
    }
}


