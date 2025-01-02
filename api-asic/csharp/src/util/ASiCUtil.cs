/**
 * @author yavuz.kahveci
 */
using System;
using System.Collections.Generic;
using System.Text;
using Org.BouncyCastle.Math;
using tr.gov.tubitak.uekae.esya.api.asic.core;
using tr.gov.tubitak.uekae.esya.api.asic.model;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver;

namespace tr.gov.tubitak.uekae.esya.api.asic.util
{
    using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;

    public static class ASiCUtil
    {
        // instead of very long unique ids, generate short almost unique id
        public static string id(){
            try {
                byte[] bytes = DigestUtil.digest(DigestAlg.SHA1, Encoding.UTF8.GetBytes(Guid.NewGuid().ToString()));
                string s = new BigInteger(bytes).Abs().ToString(16);
                return s.Substring(s.Length-9, 9);
            } catch (Exception x){
                throw new SignatureRuntimeException(x);
            }
        }

        public static void fixResolversConfig(SignatureContainer sc, PackageContents contents){
            Object impl = sc.getUnderlyingObject();

            tr.gov.tubitak.uekae.esya.api.xmlsignature.Context xc;

            if (impl  == typeof(SignedDocument)){
                xc = ((SignedDocument)impl).getContext();
            } else {
                xc = ((XMLSignature)impl).Context;
            }
            fixResolversConfig(xc, contents);
        }

        public static void fixResolversConfig(Context xc, PackageContents contents){
            IList<Type> resolvers = xc.Config.ResolversConfig.Resolvers;
            IList<Type> remove = new List<Type>();

            foreach (Type c in resolvers){
                if (c.Equals(typeof(FileResolver)) || c.Equals(typeof(HttpResolver)))
                {
                    remove.Add(c);
                }
            }
            foreach (var rm in remove)
            {
                if(resolvers.Contains(rm))
                {
                    resolvers.Remove(rm);
                }
            }
            //resolvers.RemoveAll();
            xc.Resolvers.Add(new PackageContentResolver(contents));
        }


        /*public static void main(String[] args)
        {
            Console.WriteLine(id());
            Console.WriteLine(id());
            Console.WriteLine(id());
            Console.WriteLine(id());
            Console.WriteLine(id());
            Console.WriteLine(id());
            Console.WriteLine(id());
            Console.WriteLine(id());
            Console.WriteLine(id());
        }*/
    }
}