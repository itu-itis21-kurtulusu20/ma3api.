package tr.gov.tubitak.uekae.esya.cmpapi.base20.util;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertStatus;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertStatus;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIHeader;
import tr.gov.tubitak.uekae.esya.asn.cmp.ProtectedPart;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificationParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 11, 2010 - 3:00:16 PM <p>
 * <b>Description</b>: <br>
 * Utility Class related with CMP
 */

public class UtilCmp {
    public static byte[] getSigningData(PKIHeader header, PKIBody body) {
        ProtectedPart prt = new ProtectedPart(header, body);
        Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
        try {
            prt.encode(encBuf);
            return encBuf.getMsgCopy();
        } catch (Exception ex) {
            throw new RuntimeException("PKI mesajinda imzalanacak yapi olusturulamadi.", ex);
        }
    }
        /**
     * Rasgele sayı oluşturur.
     *
     * @return  @param idSet
     */
    public static long createNextID(Set<Long> idSet) {
        Random rand = new Random();
        long id;
        generate:
        while (true) {
            id = rand.nextLong();
            if (idSet.contains(id))
                continue generate;
            return id;
        }
    }

    public static List<ECertStatus> createSuccesfullCertificationStatuses(List<ICertificationParam> certificationResults) {
        try {
            ArrayList<ECertStatus> certStatuses = new ArrayList<ECertStatus>();
            for (ICertificationParam certificationResult : certificationResults) {
                byte[] certHash = DigestUtil.digest(DigestAlg.SHA1, certificationResult.getCertificateEncoded());
                long certReqId = certificationResult.getCertReqMsg().getCertRequest().getObject().certReqId.value;
                certStatuses.add(new ECertStatus(new CertStatus(certHash, certReqId)));
            }
            return certStatuses;
        } catch (Exception e) {
            throw new RuntimeException("Error while creating Statues:"+e.getMessage(),e);
        }
    }
}
