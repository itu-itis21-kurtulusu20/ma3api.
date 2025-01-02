package tr.gov.tubitak.uekae.esya.api.asn.pqixqualified;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1OpenType;
import com.objsys.asn1j.runtime.Asn1PrintableString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.PKIXqualified.QCStatement;
import tr.gov.tubitak.uekae.esya.asn.esya._esyaValues;
import tr.gov.tubitak.uekae.esya.asn.etsiqc.Iso4217CurrencyCode;
import tr.gov.tubitak.uekae.esya.asn.etsiqc.MonetaryValue;
import tr.gov.tubitak.uekae.esya.asn.etsiqc._etsiqcValues;

/**
 * User: zeldal.ozdemir
 * Date: 1/28/11
 * Time: 1:20 PM
 */
public class EQCStatement extends BaseASNWrapper<QCStatement> {

    public EQCStatement(byte[] aBytes) throws ESYAException {
        super(aBytes, new QCStatement());
    }

    public EQCStatement(int[] statementId, Asn1OpenType statementInfo) {
        super(new QCStatement(statementId, statementInfo));
    }

    public EQCStatement(QCStatement aObject) {
        super(aObject);
    }

    public static EQCStatement createEtsiQCStatement() {
        return new EQCStatement(new QCStatement(_etsiqcValues.id_etsi_qcs_QcCompliance));
    }

    public static EQCStatement createTKNesQCStatement() {
        return new EQCStatement(new QCStatement(_esyaValues.id_TK_nesoid));
    }

    public static EQCStatement createEtsiQCLimitValueStatement(String currencyCode, long limit) throws ESYAException {

        Iso4217CurrencyCode code = new Iso4217CurrencyCode();
        code.set_alphabetic(new Asn1PrintableString(currencyCode));

        //exponenti hesaplayalim.
        long exp = 0;
        if (limit != 0) {
            while ((limit % 10) == 0) {
                exp++;
                limit = limit / 10;
            }
        }
        MonetaryValue manVal = new MonetaryValue(code, limit, exp);
        Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
        try {
            manVal.encode(encBuf);
        } catch (Asn1Exception ex) {

            throw new ESYAException("Sertifika ureticide MonetoryValue encode edilirken hata...", ex);
        }
        Asn1OpenType ot = new Asn1OpenType(encBuf.getMsgCopy());
        return new EQCStatement(new QCStatement(_etsiqcValues.id_etsi_qcs_QcLimitValue, ot));
    }


}
