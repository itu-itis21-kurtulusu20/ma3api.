

using System;
using System.Collections.Generic;
using System.IO;
using NUnit.Framework;
using test.esya.api.signature.settings;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.impl;

namespace test.esya.api.signature
{

    public class BaseTest
    {
        ResultFormatter formatter = new ResultFormatter();
        protected TestSettings settings;
        protected TestConfig config;
        protected SignatureFormat signatureFormat;

        [TestFixtureSetUp]
        public void init()
        {
            signatureFormat = SignatureTestSuite.SIGNATURE_FORMAT;
            settings = new UGSettings();
            config = new TestConfig(signatureFormat,
                                    SignatureTestSuite.ROOT + signatureFormat);

        }

        public void debugCVR(ContainerValidationResult cvr)
        {
            Console.WriteLine("--------------------------");
            Console.WriteLine(cvr);
            /*
            System.out.println(cvr.getResultType());
            int index = 0;
            for (SignatureValidationResult svr : cvr.getAllResults().values()){
                index++;
                System.out.println("Signature "+index);
                System.out.println(svr.getResultType());
                debugDetails(svr.getDetails(), 1);
            } */
            Console.WriteLine("--------------------------");
        }

        public void debugDetails<T>(List<T> details, int indent) where T : ValidationResultDetail
        {
            if (details == null)
                return;

            String tab = "";
            for (int i = 0; i < indent; i++)
            {
                tab += "\t";
            }
            foreach (ValidationResultDetail detail in details)
            {
                Console.WriteLine(tab + "class  : " + detail.getValidatorClass().FullName);
                Console.WriteLine(tab + "message: " + detail.getCheckMessage());
                Console.WriteLine(tab + "result : " + detail.getCheckResult());
                debugDetails(detail.getDetails<ValidationResultDetail>(), indent + 1);
            }
        }

        public Context createContext()
        {
            Uri uri = new Uri(SignatureTestSuite.ROOT);
            Context c = new Context(new Uri(uri.AbsoluteUri));
            c.setData(settings.getContent()); // for CAdES
            return c;
        }


        public SignatureContainer readSignatureContainer(String fileName)
        {
            return readSignatureContainer(fileName, createContext());
        }

        public void write(SignatureContainer sc, String fileName)
        {
            FileStream fis = new FileStream(config.getPath(fileName), FileMode.Create);
            sc.write(fis);
            fis.Close();
        }

        public SignatureContainer readSignatureContainer(String fileName, Context c)
        {
            FileStream fis = new FileStream(config.getPath(fileName), FileMode.Open, FileAccess.Read);
            SignatureContainer container = SignatureFactory.readContainer(fis, c);
            fis.Close();
            return container;
        }

        public ContainerValidationResult validateSignature(String fileName)
        {
            return validateSignature(fileName, createContext());
        }
        public ContainerValidationResult validateSignature(String fileName, Context c)
        {

            FileStream fis = new FileStream(config.getPath(fileName), FileMode.Open, FileAccess.Read);

            SignatureContainer container = SignatureFactory.readContainer(fis, c);

            fis.Close();

            ContainerValidationResult cvr = container.verifyAll();
            debugCVR(cvr);

            return cvr;
        }
    }
}
