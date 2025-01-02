package tr.gov.tubitak.uekae.esya.api.infra.crl.crlincache;

import java.util.*;
import java.lang.reflect.Array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: zeldalo
 * Date: 17.Nis.2009
 * Time: 15:42:41
 * To change this template use File | Settings | File Templates.
 */
class CertificateRevocationTree extends TreeMap<byte[], RevokedCertificateInfo> {
    private static final Logger logger = LoggerFactory.getLogger(CertificateRevocationTree.class);
    CertificateList certificateList;

    public CertificateRevocationTree() {
        super(new ByteArrayComparator());
    }

    public void putAll(List<Map.Entry<byte[], RevokedCertificateInfo>> revokedCertificateList) {
        for (Map.Entry<byte[], RevokedCertificateInfo> revokedCertificate : revokedCertificateList) {
            this.put(revokedCertificate.getKey(), revokedCertificate.getValue());
        }
    }

    private static class ByteArrayComparator implements Comparator<byte[]> {
        public int compare(byte[] arg1, byte[] arg2) {
            if (arg1.length < arg2.length)
                return -1;
            if (arg1.length > arg2.length)
                return 1;             
            // Argument lengths are equal; compare the values
            for (int i = 0; i < arg1.length; i++) {
                if (arg1[i] < arg2[i])
                    return -1;
                if (arg1[i] > arg2[i])
                    return 1;
            }
            return 0;
        }
    }
}
