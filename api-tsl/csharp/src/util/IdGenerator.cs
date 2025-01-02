using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using log4net;

namespace tr.gov.tubitak.uekae.esya.api.tsl.util
{
    public class IdGenerator
    {
        private static readonly ILog logger = log4net.LogManager.GetLogger(typeof(IdGenerator));

        private static readonly IDictionary<string, int> idMap = new SynchronizedDictionary<string, int>();

        public const string TYPE_SIGNATURE = "Signature";
        public const string TYPE_SIGNATURE_VALUE = "Signature-Value";
        public const string TYPE_REFERENCE = "Reference";
        public const string TYPE_SIGNED_INFO = "Signed-Info";
        public const string TYPE_KEY_INFO = "Key-Info";
        public const string TYPE_OBJECT = "Object";
        public const string TYPE_DATA = "Data";
        public const string TYPE_SIGNED_PROPERTIES = "Signed-Properties";
        public const string TYPE_TSLID = "ID";


        /// <summary>
        /// Generates sequential ids for different needs. For "Signature" input,
        /// this method generates "Signature-Id-1", "Signature-Id-2",
        /// "Signature-Id-3" etc..
        /// </summary>
        public virtual string uret(string aIdType)
        {
            lock (idMap)
            {
                int index = 0;
                if (idMap.ContainsKey(aIdType))
                {
                    index = idMap[aIdType];
                }
                index++;
                idMap[aIdType] = index;
                //return aIdType + "-Id-" + index;
                return aIdType + index;
            }
        }

        public virtual void update(string id)
        {
            lock (idMap)
            {
                try
                {
                    string key = id.Substring(0, id.IndexOf("ID"));
                    int value = Convert.ToInt32(id.Substring(id.LastIndexOf('-') + 1));

                    if (idMap.ContainsKey(key))
                    {
                        value = Math.Max(idMap[key], value);
                    }

                    Console.WriteLine("!! update : " + id + "; key: " + key + " : " + value);

                    idMap[key] = value;
                }
                catch (Exception exc)
                {
                    logger.Debug("Elsewhere generated id : " + id, exc);
                    // probably elsewhere generated ids, so no overl,excap should occur
                    //x.printStackTrace();
                }
            }
        }
    }
}
