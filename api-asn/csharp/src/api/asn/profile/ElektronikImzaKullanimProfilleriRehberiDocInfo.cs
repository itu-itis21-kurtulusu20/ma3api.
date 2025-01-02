using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.tools;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.asn.algorithms;

namespace tr.gov.tubitak.uekae.esya.api.asn.profile
{
    public class ElektronikImzaKullanimProfilleriRehberiDocInfo : ProfileDocInfo
    {
        public static String msurl =
            "http://www.eimza.gov.tr/EimzaPolitikalari/216792161015070321.pdf";

        public static List<Pair<int[], byte[]>> hashes = new List<Pair<int[], byte[]>>();

        static ElektronikImzaKullanimProfilleriRehberiDocInfo()
        {
            hashes.Add(new Pair<int[], byte[]>(_algorithmsValues.sha_1, StringUtil.ToByteArray("7E3B50D9020C676BBBF07A998E75E63734909CD8")));
            hashes.Add(new Pair<int[], byte[]>(_algorithmsValues.id_sha224, StringUtil.ToByteArray("B6600FE76A7E1973367382D81F224620242A1957595C20C882A5EC8F")));
            hashes.Add(new Pair<int[], byte[]>(_algorithmsValues.id_sha256,StringUtil.ToByteArray("FF39BD29463383F69B2052AC47439E06CE7C3B8646E888B6E5AE3E46BA08117A")));
            hashes.Add(new Pair<int[], byte[]>(_algorithmsValues.id_sha384,StringUtil.ToByteArray("E7503CF83D21EB179C3A89FDE8BF2216E5F4F24C9DA9752D8FE86C8A8B71D4BD58A1EF426B8B0071B8C1D0754C71A810")));
            hashes.Add(new Pair<int[], byte[]>(_algorithmsValues.id_sha512,StringUtil.ToByteArray("AF925EEE76562989CD5DA4000DA2C35F3D9E95BC6604BB13FE3924A6E223914756BF54FCE4CCFD2617DE906EA135B9474CCB1DA83F8468C2DB18341EC7552EDE")));
        }

        public byte[] getDigestOfProfile(int[] aDigestAlgOid)
        {
            foreach (Pair<int[], byte[]> aHash in hashes)
            {
                if (aDigestAlgOid.SequenceEqual(aHash.getmObj1()))
                    return aHash.getmObj2();
            }
            StringBuilder algOid = null;
            foreach (int i in aDigestAlgOid)
            {                
                algOid.Append(i).Append(",");
            }       
            algOid.Remove(algOid.Length - 2, 1);

            //throw new ESYAException("Digest of profile according to " + string.Join(",", aDigestAlgOid) +
            //                        " algorithm can not be found");
            throw new ESYAException("Digest of profile according to " + algOid +
                                    " algorithm can not be found");
        }

        public Stream getProfile()
        {
            EWebClient client = new EWebClient();
            client.UseDefaultCredentials = true;
            Stream stream = null;
            try
            {
                stream = client.OpenRead(msurl);
            }
            catch (Exception ex)
            {
                throw new ESYAException("Problem at reading profile document.", ex);
            }
            return stream;
        }

        public String getURI() 
        {
		   return msurl;
        }

    }

}
