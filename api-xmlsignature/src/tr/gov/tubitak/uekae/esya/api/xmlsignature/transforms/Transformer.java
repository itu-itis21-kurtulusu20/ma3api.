package tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;

import java.util.List;

import org.w3c.dom.Element;

/**
 * <p>A <code>Transform</code> algorithm has a single implicit parameter:
 * an octet stream from the <code>Reference</code> or the output of an earlier
 * <code>Transform</code>.</p>
 *
 * <p>Implementations of this class works together with
 * <code>{@link TransformEngine}</code>. Necessary convertions are mostly done
 * on <code>TransformEngine</code> and implementations just declares which
 * <code>{@link DataType}</code>s they accept and return.
 *
 * @author ahmety
 * date: Jun 19, 2009
 */
public interface Transformer
{

    /**
     * @return what data types this transform operates on.
     */
    List<DataType> expectedDataTypes();

    /**
     * @return which datatype this transformer returns after transformation
     */
    DataType returnType();

    /**
     * Is transformer responsible for the algorithm given as parameter?
     * @param aAlgorithmURI algorithm URI declared in XMLdSig spec
     * @return true if algorithm is supported, false otherwise
     */
    boolean acceptsAlgorithm(String aAlgorithmURI);


    /**
     * Make the appropriate transform defined by algorithm according to
     * parameters <code>link{NodeList}</code>
     *
     * @param aObject object to be transformed, should be either
     *      <code>@link{NodeList}</code> or <code>@link{InputStream}</code>
     *      according to return type of <code>expectedDataTypes</code>
     * @param aAlgorithmURI algorithm defined b XMLdSig sprec
     * @param aParams algorithm parameters if any
     * @param aTransformElement xml element where transform is declared
     * @param aBaseURI where to find relative resourses
     * @return transform result in form of <code>{@link DataType}</code>
     *      declared by <code>{@link Transformer#returnType()}</code>
     * @throws XMLSignatureException if any problem occurs
     */
    Object transform(Object aObject, String aAlgorithmURI, Object[] aParams,
                     Element aTransformElement, String aBaseURI)
            throws XMLSignatureException;

}
