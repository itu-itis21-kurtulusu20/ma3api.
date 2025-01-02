using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ImzaApiTest.src.dev.esya.api.xmlsignature
{
    class IdGenerator
    {
        static void Main(string[] args)
        {
            global::tr.gov.tubitak.uekae.esya.api.xmlsignature.util.IdGenerator id = new global::tr.gov.tubitak.uekae.esya.api.xmlsignature.util.IdGenerator();
            Console.WriteLine(id.uret("Signature"));
            Console.WriteLine(id.uret("Signature"));
            Console.WriteLine(id.uret("Signature"));
            Console.WriteLine(id.uret("Signature"));
        }


    }
}
