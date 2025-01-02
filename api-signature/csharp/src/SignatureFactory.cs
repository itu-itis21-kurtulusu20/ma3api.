using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using log4net;

namespace tr.gov.tubitak.uekae.esya.api.signature
{
    /**
     * <p>This class is the starting point of signature related operations.</p>
     *
     * <p>SignatureContainers are created or read from streams of data by this class
     * according to signature format, and containers can hold multiple signatures.</p>
     *
     * <p>There is also an utility method for detecting a stream is type of any
     * supported format.</p>
     *
     * @see SignatureContainer
     * @see Signature
     * @author ayetgin
     */
    public class SignatureFactory
    {
        protected static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private static readonly String XADES_CONTAINER_CLASS = "tr.gov.tubitak.uekae.esya.api.xmlsignature.provider.ContainerImpl";
        private static readonly String XADES_ASSEMBLY = "ma3api-xmlsignature";
        private static readonly String CADES_CONTAINER_CLASS = "tr.gov.tubitak.uekae.esya.api.cmssignature.provider.CMSSignatureContainer";
        private static readonly String CADES_ASSEMBLY = "ma3api-cmssignature";
        private static readonly String ASiC_XADES_CONTAINER_CLASS = "tr.gov.tubitak.uekae.esya.api.asic.ASiCXAdESSignatureContainer";
        private static readonly String ASiC_CADES_CONTAINER_CLASS = "tr.gov.tubitak.uekae.esya.api.asic.ASiCCAdESSignatureContainer";

        private static List<SignatureContainer> knownContainers = new List<SignatureContainer>();

        static SignatureFactory()
        {
            Context c = new Context();
            addToKnownContainersIfInClasspath(SignatureFormat.CAdES, c);
            addToKnownContainersIfInClasspath(SignatureFormat.XAdES, c);
        }
        private static void addToKnownContainersIfInClasspath(SignatureFormat format, Context c)
        {
            try
            {
                SignatureContainer container = createContainerInstance(format);
                knownContainers.Add(container);
                logger.Debug("Found provider for " + format);
            }
            catch (Exception x)
            {
                logger.Debug("No provider for " + format);
            }
        }
        /**
         * create signature container with default context(base uri=working dir!)
         * @param format signature format
         * @return  SignatureContainer
         */
        public static SignatureContainer createContainer(SignatureFormat format)
        {
            return createContainer(format, new Context());
        }
        /**
     * create signature container with params set in context
     * @param format signature format
     * @return  SignatureContainer
     */
        public static SignatureContainer createContainer(SignatureFormat format, Context context)
        {
            SignatureContainer container = createContainerInstance(format);
            container.setContext(context);
            return container;
        }
        /**
    * Read signature container from incoming stream of data.
    *
    * @param format signature format
    * @return  SignatureContainer
    *
    * @throws SignatureException
    *  if stream is not in form of any supported signature format
    *  if IOError occurs
    *  or signature is structural errors etc.
    */
        public static SignatureContainer readContainer(SignatureFormat format, Stream stream, Context context)
        {
            SignatureContainer container = createContainerInstance(format);
            container.setContext(context);
            container.read(stream);
            return container;
        }
           /**
     * Read signature container from incoming stream of data. Note that if
     * streams signature format is known, use readContainer format with the
     * signatureFormat parameter, so that to resource is wasted for detecting
     * signature format.
     *
     * @return  read SignatureContainer
     *
     * @throws SignatureException
     *  if stream is not in form of any supported signature format
     *  if IOError occurs
     *  or signature is structural errors etc.
     */
    public static SignatureContainer readContainer(Stream stream, Context context)
    {
        try {
            BufferedStream bis = new BufferedStream(stream);
            SignatureFormat? format = detectSignatureFormat(bis);
            if (format==null)
                throw new NotSupportedException("Cannot detect signature format from stream!");
            SignatureFormat formatNet = format.Value;
            SignatureContainer sc = readContainer(formatNet, bis, context);
            bis.Close();
            return sc;
        } catch (Exception x){
            throw new SignatureException(x);
        }
    }

    /**
     * Try to figure out format of signature stream on the best effort basis.
     * @param is which is possibly contains signature bytes
     * @return detected signature format if any
     */
    public static SignatureFormat? detectSignatureFormat(BufferedStream isNet)
    {
        try {
            //isNet.SetLength(1024);
            byte[] temp = new byte[512];
            int read = isNet.Read(temp, 0, 512);
            MemoryStream bais = new MemoryStream(temp, 0, read);
            isNet.Position=0;
            foreach (SignatureContainer sc in knownContainers){
                try {
                    if (sc.isSignatureContainer(bais)){
                        return sc.getSignatureFormat();
                    }
                    bais.Position=0;
                } catch (Exception x){
                    Console.WriteLine(x.StackTrace);
                }
            }
        }catch (Exception x){
            Console.WriteLine(x.StackTrace);
        }
        return null;
    }

        //todo
        // detectsignatureFormat yuzunden kapattim
        /*public static SignatureContainer readContainer(Stream stream, Context context){
            SignatureFormat format = detectSignatureFormat(stream);
            if (format==null)
                throw new NotSupportedException("Cannot detect signature format from stream!");

            return readContainer(format, stream, context);
        }*/

        // bunu kapadim cunku mark-reset olayini cozemedim, baktim bi yerde cagiriliyor
        // onu da kapattim (ustte) readContainer
        /*public static SignatureFormat detectSignatureFormat(Stream stream){
            // todo
            BufferedStream bis = new BufferedStream(stream);
            bis.mark(int.MaxValue);

            foreach (SignatureContainer sc in knownContainers){
                try {
                    bis.reset();
                    if (sc.isSignatureContainer(bis)){
                        return sc.getSignatureFormat();
                    }

                } catch (Exception x){
                    Console.WriteLine(x.StackTrace);
                }
            }

            return SignatureFormat.XAdES;
        }*/

        static SignatureContainer createContainerInstance(SignatureFormat format)
        {
            switch (format)
            {
                case SignatureFormat.XAdES: return createContainerInstance(XADES_CONTAINER_CLASS, XADES_ASSEMBLY);
                case SignatureFormat.CAdES: return createContainerInstance(CADES_CONTAINER_CLASS, CADES_ASSEMBLY);
            }
            throw new NotSupportedException("Cannot create SignatureContainer for " + format);
        }

        static SignatureContainer createContainerInstance(String className, String assembly)
        {
            try
            {
                //return (SignatureContainer)Class.forName(className).newInstance();
                return (SignatureContainer)Activator.CreateInstance(assembly, className).Unwrap();
                
            }
            catch (Exception x)
            {
                throw new SignatureRuntimeException(x);
            }
        }
    }
}
