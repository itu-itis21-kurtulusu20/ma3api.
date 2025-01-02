using System;
using System.IO;
using System.Reflection;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.esya;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.etsiqc;
using tr.gov.tubitak.uekae.esya.asn.PKIXqualified;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace tr.gov.tubitak.uekae.esya.api.asn.pqixqualified
{
    public class EQCStatements : BaseASNWrapper<QCStatements>
    {

        readonly Asn1ObjectIdentifier etsi = new Asn1ObjectIdentifier(_etsiqcValues.id_etsi_qcs_QcCompliance);
        readonly Asn1ObjectIdentifier money = new Asn1ObjectIdentifier(_etsiqcValues.id_etsi_qcs_QcLimitValue);

        public EQCStatements()
            : base(new QCStatements())
        {
        }

        public EQCStatements(QCStatements aQCStatements)
            : base(aQCStatements)
        {
        }

        public void addStatement(EQCStatement statement)
        {
            mObject.elements = extendArray(mObject.elements, statement.getObject());
        }

        public EQCStatement[] getQCStatements()
        {
            return wrapArray<EQCStatement, QCStatement>(mObject.elements, typeof(EQCStatement));
        }

        public EExtension toExtension(bool critic)
        {
            return new EExtension(EExtensions.oid_pe_qcStatements, critic, getBytes());
        }

        public string getMoneyString()
        {
            for (int i = 0; i < mObject.elements.Length; i++)
            {
                Asn1ObjectIdentifier oid = mObject.elements[i].statementId;
                Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
                if (oid.Equals(money))
                {
                    try
                    {
                        Asn1OpenType statement = mObject.elements[i].statementInfo;
                        statement.Encode(enc);
                        Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(enc.MsgCopy);
                        MonetaryValue value = new MonetaryValue();
                        value.Decode(decBuf);
                        double sonuc = value.amount.mValue * Math.Pow(10, value.exponent.mValue);
                        return sonuc.ToString();
                    }
                    catch (Asn1Exception ex)
                    {
                        throw new ESYAException(ex);
                    }
                    catch (IOException ex)
                    {
                        throw new ESYAException(ex);
                    }
                }
            }

            return null;
        }

        public String getPlainTKComplianceStr()
        {
            EQCStatement [] qcStatementsArray = getQCStatements();
            try
            {
                for (int i = 0; i < qcStatementsArray.Length; i++)
                {
                    if (isTRQualifiedOID(qcStatementsArray[i].getObject().statementId))
                    {
                        Asn1OpenType statementInfo = qcStatementsArray[1].getObject().statementInfo;

                        Asn1DerDecodeBuffer derDecodeBuffer = new Asn1DerDecodeBuffer(statementInfo.mValue);
                        Asn1UTF8String utf8String = new Asn1UTF8String();
                        utf8String.Decode(derDecodeBuffer);

                        String statement = utf8String.ToString();
                        return statement;
                    }
                }
            }
            catch (IOException ex)
            {
                throw new ESYAException(ex);
            }
            return null;
        }

        public static bool isTRQualifiedOID(Asn1ObjectIdentifier oid)
        {
            return oid.Equals(EESYAOID.oid_TK_nesoid) || oid.Equals(EESYAOID.oid_TK_nesoid_2);
        }

        public override String ToString()
        {
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < mObject.elements.Length; i++)
            {
                QCStatement stat = mObject.elements[i];
                result.Append(" [" + (i + 1) + "]\n");
                Asn1ObjectIdentifier oid = stat.statementId;
                Asn1OpenType aciklama = stat.statementInfo;
                /*etsi uyumu*/
                if (oid.Equals(etsi))
                {
                    result.Append(Resource.message(Resource.NITELIKLI_COMPLIANCE) + "(" + etsi.ToString() + ")" + "\n");
                }
                /*tk uyumu*/
                else if (isTRQualifiedOID(oid))
                {
                    result.Append(Resource.message(Resource.NITELIKLI_TK) + "(" + oid.ToString() + ")" + "\n");
                    if (aciklama != null)
                    {
                        try
                        {
                            result.Append(_utf8ToString(aciklama));
                        }
                        catch (Exception ex)
                        {
                            logger.Warn("QCStatement oluşturulurken hata oluştu", ex);
                            result.Append(aciklama.ToString());
                        }
                    }
                }

                /*para limiti*/
                else if (oid.Equals(money))
                {
                    result.Append(Resource.message(Resource.NITELIKLI_MONEY_LIMIT) + "\n");

                    Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
                    if (aciklama != null)
                    {
                        try
                        {
                            aciklama.Encode(enc);
                            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(enc.MsgCopy);
                            MonetaryValue value = new MonetaryValue();
                            value.Decode(decBuf);
                            double sonuc = value.amount.mValue * Math.Pow(10, value.exponent.mValue);
                            result.Append(" " + result + (long)sonuc + " " + value.currency.GetElement().ToString() + "\n");
                        }
                        catch (Exception aEx)
                        {
                            logger.Warn("QCStatement oluşturulurken hata oluştu", aEx);
                            result.Append(aciklama.ToString());
                        }
                    }
                }
                /*tanimadigim bir oid*/
                else
                {
                    result.Append(oid.ToString() + "\n");
                    if (aciklama != null)
                    {
                        try
                        {
                            result.Append(_utf8ToString(aciklama));
                        }
                        catch (Exception aEx)
                        {
                            logger.Warn("QCStatement oluşturulurken hata oluştu", aEx);
                            result.Append(aciklama.ToString());
                        }
                    }
                }
            }
            return result.ToString();
        }
        public bool checkStatement(Asn1ObjectIdentifier aOID)
        {
            bool found = false;

            for (int i = 0; i < mObject.elements.Length; i++)
            {
                QCStatement stat = mObject.elements[i];
                Asn1ObjectIdentifier oid = stat.statementId;
                found = oid.Equals(aOID);
                if (found) break;
            }

            return found;
        }

        private String _utf8ToString(Asn1OpenType aStatInfo)
        {
            String result = "";
            Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
            aStatInfo.Encode(enc);
            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(enc.MsgCopy);
            Asn1UTF8String value = new Asn1UTF8String();
            value.Decode(decBuf);
            result = value.ToString() + "\n";
            return result;
        }
    }
}
