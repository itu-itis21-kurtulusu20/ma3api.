using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.etsiqc;
using tr.gov.tubitak.uekae.esya.asn.PKIXqualified;

namespace tr.gov.tubitak.uekae.esya.api.asn.pqixqualified
{
    public class EQCStatement : BaseASNWrapper<QCStatement>
    {
        public EQCStatement(byte[] aBytes)
            : base(aBytes, new QCStatement())
        {
            //super(aBytes, new QCStatement());
        }

        public EQCStatement(int[] statementId, Asn1OpenType statementInfo)
            : base(new QCStatement(statementId, statementInfo))
        {
            //super(new QCStatement(statementId, statementInfo));
        }

        public EQCStatement(QCStatement aObject)
            : base(aObject)
        {
            //super(aObject);
        }

        public static EQCStatement createEtsiQCStatement()
        {
            return new EQCStatement(new QCStatement(_etsiqcValues.id_etsi_qcs_QcCompliance));
        }

        public static EQCStatement createEtsiQCLimitValueStatement(String currencyCode, long limit)
        {

            Iso4217CurrencyCode code = new Iso4217CurrencyCode();
            code.Set_alphabetic(new Asn1PrintableString(currencyCode));

            //exponenti hesaplayalim.
            long exp = 0;
            if (limit != 0)
            {
                while ((limit % 10) == 0)
                {
                    exp++;
                    limit = limit / 10;
                }
            }
            MonetaryValue manVal = new MonetaryValue(code, limit, exp);
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            try
            {
                manVal.Encode(encBuf);
            }
            catch (Asn1Exception ex)
            {

                throw new ESYAException("Sertifika ureticide MonetoryValue encode edilirken hata...", ex);
            }
            Asn1OpenType ot = new Asn1OpenType(encBuf.MsgCopy);
            return new EQCStatement(new QCStatement(_etsiqcValues.id_etsi_qcs_QcLimitValue, ot));
        }
    }
}
