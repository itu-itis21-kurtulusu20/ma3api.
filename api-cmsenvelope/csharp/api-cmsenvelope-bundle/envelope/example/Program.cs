using System;

namespace tr.gov.tubitak.uekae.esya.api.envelope.example
{
    public class Program
    {
        static void Main(string[] args)
        {
            try
            {
                EnvelopeTest envelopeTest = new EnvelopeTest();
                envelopeTest.testToEncrypt();

                Console.WriteLine(@"Envelope tests are completed! Press any key.");
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                Console.WriteLine(@"Error occured! Press any key.");
            }

            Console.Read();
        }
    }
}
