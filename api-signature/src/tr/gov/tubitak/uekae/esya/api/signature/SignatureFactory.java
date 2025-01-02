package tr.gov.tubitak.uekae.esya.api.signature;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
public class SignatureFactory {
    private static final Logger logger = LoggerFactory.getLogger(SignatureFactory.class);

    private static final String XADES_CONTAINER_CLASS = "tr.gov.tubitak.uekae.esya.api.xmlsignature.provider.ContainerImpl";
    private static final String CADES_CONTAINER_CLASS = "tr.gov.tubitak.uekae.esya.api.cmssignature.provider.CMSSignatureContainer";
    private static final String PADES_CONTAINER_CLASS = "tr.gov.tubitak.uekae.esya.api.pades.pdfbox.PAdESContainer";

    private static List<SignatureContainer> knownContainers = new ArrayList<SignatureContainer>();

    static {
        Context c = new Context();
        addToKnownContainersIfInClasspath(SignatureFormat.CAdES, c);
        addToKnownContainersIfInClasspath(SignatureFormat.XAdES, c);
        addToKnownContainersIfInClasspath(SignatureFormat.PAdES, c);
    }

    private static void addToKnownContainersIfInClasspath(SignatureFormat format, Context c){
        try {
            SignatureContainer sc = createContainerInstance(format);
            knownContainers.add(sc);
            logger.debug("Found provider for "+format);
        }
        catch (Exception e){
            logger.debug("No provider for "+format, e); //It can be ignored.
        }
    }

    /**
     * create signature container with default context(base uri=working dir!)
     * @param format signature format
     * @return  SignatureContainer
     */
    public static SignatureContainer createContainer(SignatureFormat format){
        return createContainer(format, new Context());
    }

    /**
     * create signature container with params set in context
     * @param format signature format
     * @return  SignatureContainer
     */
    public static SignatureContainer createContainer(SignatureFormat format, Context context){
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
    public static SignatureContainer readContainer(SignatureFormat format, InputStream stream, Context context)
        throws SignatureException
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
    public static SignatureContainer readContainer(InputStream stream, Context context)
        throws SignatureException
    {
        try {
            BufferedInputStream bis = new BufferedInputStream(stream);
            SignatureFormat format = detectSignatureFormat(bis);
            if (format==null)
                throw new NotSupportedException("Cannot detect signature format from stream!");

            SignatureContainer sc = readContainer(format, bis, context);
            bis.close();
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
    public static SignatureFormat detectSignatureFormat(BufferedInputStream is)
    {
        try {
            is.mark(1024);
            byte[] temp = new byte[512];
            int read = is.read(temp, 0, 512);
            ByteArrayInputStream bais = new ByteArrayInputStream(temp, 0, read);
            is.reset();
            for (SignatureContainer sc : knownContainers){
                try {
                    if (sc.isSignatureContainer(bais)){
                        return sc.getSignatureFormat();
                    }
                    bais.reset();
                } catch (Exception e){
                    logger.error("Error in SignatureFactory", e);
                }
            }
        }catch (Exception e){
            logger.error("Error in SignatureFactory", e);
        }
        return null;
    }

    static SignatureContainer createContainerInstance(SignatureFormat format){
        switch (format){
            case XAdES      : return createContainerInstance(XADES_CONTAINER_CLASS);
            case CAdES      : return createContainerInstance(CADES_CONTAINER_CLASS);
            case PAdES      : return createContainerInstance(PADES_CONTAINER_CLASS);
        }
        throw new NotSupportedException("Cannot create SignatureContainer for "+format);
    }

    static SignatureContainer createContainerInstance(String className){
        try {
            return (SignatureContainer)Class.forName(className).newInstance();
        } catch (Exception x){
            throw new SignatureRuntimeException(x);
        }
    }

}
