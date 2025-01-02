package tr.gov.tubitak.uekae.esya.api.asn.pqixqualified;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAOID;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtension;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.asn.PKIXqualified.QCStatement;
import tr.gov.tubitak.uekae.esya.asn.PKIXqualified.QCStatements;
import tr.gov.tubitak.uekae.esya.asn.esya._esyaValues;
import tr.gov.tubitak.uekae.esya.asn.etsiqc.MonetaryValue;
import tr.gov.tubitak.uekae.esya.asn.etsiqc._etsiqcValues;

import java.io.IOException;

/**
 * @author ayetgin
 */
public class EQCStatements extends BaseASNWrapper<QCStatements>
{
    private static Logger logger = LoggerFactory.getLogger(EQCStatements.class);

    Asn1ObjectIdentifier etsi = new Asn1ObjectIdentifier(_etsiqcValues.id_etsi_qcs_QcCompliance);
    Asn1ObjectIdentifier money = new Asn1ObjectIdentifier(_etsiqcValues.id_etsi_qcs_QcLimitValue);


    public EQCStatements()
    {
        super(new QCStatements());
    }

    public EQCStatements(QCStatements aQCStatements)
    {
        super(aQCStatements);
    }
    
    public void addStatement(EQCStatement statement){
        mObject.elements = BaseASNWrapper.extendArray(mObject.elements, statement.getObject());
    }

    public EQCStatement[] getQCStatements(){
        return BaseASNWrapper.wrapArray(mObject.elements, EQCStatement.class);
    }

    public EExtension toExtension(boolean critic) throws ESYAException {
        return new EExtension(EExtensions.oid_pe_qcStatements, critic, this);
    }
    
    public String getMoneyString() throws ESYAException
    {
    	 for (int i = 0; i < mObject.elements.length; i++) 
    	 {
    		 Asn1ObjectIdentifier oid = mObject.elements[i].statementId;
    		 Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
    		 if(oid.equals(money))
    		 {
    			 try
    			 {
	    			 Asn1OpenType statement = mObject.elements[i].statementInfo;
	    			 statement.encode(enc);
	    			 Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(enc.getMsgCopy());
                     MonetaryValue value = new MonetaryValue();
                     value.decode(decBuf);
                     double sonuc = value.amount.value * Math.pow(10, value.exponent.value);
                     return Double.toString(sonuc);
    			 }
    			 catch(Asn1Exception ex)
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

    public String getPlainTKComplianceStr() throws ESYAException {
        EQCStatement[] qcStatementsArray = getQCStatements();
        try
        {
            for (int i = 0; i < qcStatementsArray.length; i++) {
                if (isTRQualifiedOID(qcStatementsArray[i].getObject().statementId)) {
                    Asn1OpenType statementInfo = qcStatementsArray[1].getObject().statementInfo;

                    Asn1DerDecodeBuffer derDecodeBuffer = new Asn1DerDecodeBuffer(statementInfo.value);
                    Asn1UTF8String utf8String = new Asn1UTF8String();
                    utf8String.decode(derDecodeBuffer);

                    String statement = utf8String.toString();
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

    public static boolean isTRQualifiedOID(Asn1ObjectIdentifier oid) {
        return oid.equals(EESYAOID.oid_TK_nesoid) || oid.equals(EESYAOID.oid_TK_nesoid_2);
    }

    @Override
    public String toString()
    {
        String retStr="";
        for (int i = 0; i < mObject.elements.length; i++) {
            QCStatement stat = mObject.elements[i];
            retStr+=" [" + (i + 1) + "]\n";
            Asn1ObjectIdentifier oid = stat.statementId;
            Asn1OpenType aciklama = stat.statementInfo;
            /*etsi uyumu*/
            if (oid.equals(etsi)) {
                retStr+=CertI18n.message(CertI18n.NITELIKLI_COMPLIANCE) + "(" + etsi.toString() + ")" + "\n";
            }
            /*tk uyumu*/
            else if (isTRQualifiedOID(oid)) {
                retStr += constructTKComplianceStr(aciklama, oid);
            }
            /*para limiti*/
            else if (oid.equals(money)) {
                retStr += constructMoneyLimitStr(aciklama);
            }
            /*tanimadigim bir oid*/
            else {
                retStr += constructUnknownOidStr(oid, aciklama);
            }
        }
        return retStr;
    }

    private String constructUnknownOidStr( Asn1ObjectIdentifier oid, Asn1OpenType aciklama) {
        String retStr=oid.toString() + "\n";
        if (aciklama != null) {
            try {
                retStr+=_utf8ToString(aciklama);
            }
            catch (Exception aEx) {
                logger.warn("QCStatement oluşturulurken hata oluştu", aEx);
                retStr+=aciklama.toString();
            }
        }
        return retStr;
    }

    private String constructMoneyLimitStr(Asn1OpenType aciklama) {
        String retStr = CertI18n.message(CertI18n.NITELIKLI_MONEY_LIMIT) + "\n";

        Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
        if (aciklama != null) {
            try {
                aciklama.encode(enc);
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(enc.getMsgCopy());
                MonetaryValue value = new MonetaryValue();
                value.decode(decBuf);
                double sonuc = value.amount.value * Math.pow(10, value.exponent.value);
                retStr+="  " + (long) sonuc + " " + value.currency.getElement().toString() + "\n";
            }
            catch (Exception aEx) {
                logger.warn("QCStatement oluşturulurken hata oluştu", aEx);
                retStr+=aciklama.toString();
            }
        }
        return retStr;
    }

    private String constructTKComplianceStr(Asn1OpenType aciklama, Asn1ObjectIdentifier tk) {
        String retStr = CertI18n.message(CertI18n.NITELIKLI_TK) + "(" + tk.toString() + ")" + "\n";
        if (aciklama != null) {
            try {
                retStr+=_utf8ToString(aciklama);
            }
            catch (Exception ex) {
                logger.warn("QCStatement oluşturulurken hata oluştu", ex);
                retStr+=aciklama.toString();
            }
        }
        return retStr;
    }

    public boolean checkStatement(Asn1ObjectIdentifier aOID){
    	boolean found = false;
    	
    	for (int i = 0; i < mObject.elements.length; i++) {
            QCStatement stat = mObject.elements[i];           
            Asn1ObjectIdentifier oid = stat.statementId;
            found = oid.equals(aOID);
            if (found) break;
    	}    
    	
    	return found;
    }


    private String _utf8ToString (Asn1OpenType aStatInfo) throws ESYAException
	{
        try {

            String result = "";
            Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
            aStatInfo.encode(enc);
            Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(enc.getMsgCopy());
            Asn1UTF8String value = new Asn1UTF8String();

            value.decode(decBuf);

            result = value.toString() + "\n";
            return result;
        } catch (IOException e) {
            throw new ESYAException(e);
        }
	}

}
