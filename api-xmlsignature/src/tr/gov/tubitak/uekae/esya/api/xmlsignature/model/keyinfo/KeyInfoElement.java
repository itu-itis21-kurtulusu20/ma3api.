package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;

/**
 * <p>The following list summarizes the <code>KeyInfo</code> types that are
 * allocated an identifier in the <code>&amp;dsig;</code> namespace; these can
 * be used within the <code>RetrievalMethod</code> <code>Type</code> attribute
 * to describe a remote <code>KeyInfo</code> structure.</p>
 *
 * <ul>
    <li><a href="http://www.w3.org/2000/09/xmldsig#DSAKeyValue">http://www.w3.org/2000/09/xmldsig#DSAKeyValue</a></li>
    <li><a href="http://www.w3.org/2000/09/xmldsig#RSAKeyValue">http://www.w3.org/2000/09/xmldsig#RSAKeyValue</a></li>
    <li><a href="http://www.w3.org/2000/09/xmldsig#X509Data">http://www.w3.org/2000/09/xmldsig#X509Data</a></li>
    <li><a href="http://www.w3.org/2000/09/xmldsig#PGPData">http://www.w3.org/2000/09/xmldsig#PGPData</a></li>
    <li><a href="http://www.w3.org/2000/09/xmldsig#SPKIData">http://www.w3.org/2000/09/xmldsig#SPKIData</a></li>
    <li><a href="http://www.w3.org/2000/09/xmldsig#MgmtData">http://www.w3.org/2000/09/xmldsig#MgmtData</a></li>
  </ul>

 * <p>In addition to the types above for which we define an XML structure, we
 * specify one additional type to indicate a <a name="rawX509Certificate"
 * >binary (ASN.1 DER) X.509 Certificate</a>.</p>

  <ul>
    <li><a href="http://www.w3.org/2000/09/xmldsig#rawX509Certificate">http://www.w3.org/2000/09/xmldsig#rawX509Certificate</a></li>
  </ul>
 *
 *
 * @see KeyName
 * @see KeyValue
 * @see MgmtData
 * @see PGPData
 * @see RetrievalMethod
 * @see SPKIData
 * @see X509Data
 *
 * @author ahmety
 * date: Jun 10, 2009
 */
public interface KeyInfoElement
{
}
