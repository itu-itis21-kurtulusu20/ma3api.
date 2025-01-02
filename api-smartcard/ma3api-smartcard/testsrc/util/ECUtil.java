package util;

import gnu.crypto.sig.ecdsa.ecmath.curve.*;
import gnu.crypto.sig.ecdsa.ecmath.exceptions.EllipticCurveException;
import gnu.crypto.sig.ecdsa.ecmath.field.FieldF2mPolynomial;
import gnu.crypto.sig.ecdsa.ecmath.field.FieldFp;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;

import java.math.BigInteger;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;

/**
 * <b>Author</b>    : zeldal.ozdemir <br/>
 * <b>Project</b>   : MA3   <br/>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br/>
 * <b>Date</b>: 9/5/12 - 12:56 AM <p/>
 * <b>Description</b>: <br/>
 */
public class ECUtil {
    public static EcAsHexHolder toHolder(ECParameterSpec namedCurve) {
        EcAsHexHolder ecAsHexHolder = new EcAsHexHolder();
        ecAsHexHolder.setA(StringUtil.toString(namedCurve.getCurve().getA().toByteArray()));
        ecAsHexHolder.setB(StringUtil.toString(namedCurve.getCurve().getB().toByteArray()));
        if (namedCurve.getCurve().getField() instanceof ECFieldFp) {
            ecAsHexHolder.setCurveType(EcAsHexHolder.Prime);
            ECFieldFp field = (ECFieldFp) namedCurve.getCurve().getField();
            ecAsHexHolder.setP(StringUtil.toString(field.getP().toByteArray()));
        } else if (namedCurve.getCurve().getField() instanceof ECFieldF2m) {
            ecAsHexHolder.setCurveType(EcAsHexHolder.Binary);
            ECFieldF2m field = (ECFieldF2m) namedCurve.getCurve().getField();
            ecAsHexHolder.setP(StringUtil.toString(field.getReductionPolynomial().toByteArray()));
        } else
            throw new IllegalArgumentException("Curve Type is unknown:" + namedCurve.getCurve().getField());
        ecAsHexHolder.setGx(StringUtil.toString(namedCurve.getGenerator().getAffineX().toByteArray())); // todo check
        ecAsHexHolder.setGy(StringUtil.toString(namedCurve.getGenerator().getAffineY().toByteArray())); // todo check
        ecAsHexHolder.setN(StringUtil.toString(namedCurve.getOrder().toByteArray()));
        ecAsHexHolder.setH(namedCurve.getCofactor());
        return ecAsHexHolder;
    }

    public static EcAsHexHolder toHolder(ECDomainParameter domainParameter) {
        EcAsHexHolder ecAsHexHolder = new EcAsHexHolder();
        ecAsHexHolder.setA(StringUtil.toString(domainParameter.getMCurve().getA().toByteArray()));
        ecAsHexHolder.setB(StringUtil.toString(domainParameter.getMCurve().getB().toByteArray()));
        if (domainParameter.getCurve() instanceof CurveFp) {
            ecAsHexHolder.setCurveType(EcAsHexHolder.Prime);
            FieldFp field = (FieldFp) domainParameter.getMCurve().getField();
            ecAsHexHolder.setP(StringUtil.toString(field.getMP().toByteArray()));
        } else if (domainParameter.getCurve() instanceof CurveF2m) {
            ecAsHexHolder.setCurveType(EcAsHexHolder.Binary);
            FieldF2mPolynomial field = (FieldF2mPolynomial) domainParameter.getMCurve().getField();
            ecAsHexHolder.setP(StringUtil.toString(field.getMReductionP().toByteArray()));
        } else
            throw new IllegalArgumentException("Curve Type is unknown:" + domainParameter.getCurve());
        ecAsHexHolder.setGx(StringUtil.toString(domainParameter.getMG().getAffineX().toByteArray())); // todo check
        ecAsHexHolder.setGy(StringUtil.toString(domainParameter.getMG().getAffineY().toByteArray())); // todo check
        ecAsHexHolder.setN(StringUtil.toString(domainParameter.getMN().toByteArray()));
        ecAsHexHolder.setH(domainParameter.getMH().intValue());
        return ecAsHexHolder;
    }

    private static BigInteger bi(String s) {
        return new BigInteger(s, 16);
    }

    public static ECDomainParameter toNamedCurve(EcAsHexHolder ecAsHexHolder) throws CryptoException {
        if (ecAsHexHolder.getCurveType().equals(EcAsHexHolder.Prime)) {
            try {
                CurveFp curve = new CurveFp(
                        FieldFp.getInstance(bi(ecAsHexHolder.getP())),
                        bi(ecAsHexHolder.getA()), bi(ecAsHexHolder.getB()));
                ECPointFp point = new ECPointFp(curve,
                        bi(ecAsHexHolder.getGx()),
                        bi(ecAsHexHolder.getGy()));
                return ECDomainParameter.getInstance(curve, point, bi(ecAsHexHolder.getN()), BigInteger.valueOf(ecAsHexHolder.getH()));
            } catch (EllipticCurveException e) {
                throw new CryptoException("Unable to Convert ECDomainParameters:" + e.getMessage(), e);
            }
        } else if (ecAsHexHolder.getCurveType().equals(EcAsHexHolder.Binary)) {
            try {
                BigInteger p = bi(ecAsHexHolder.getP());
                CurveF2m curve = new CurveF2m(
                        FieldF2mPolynomial.getInstance(p.bitLength() - 1, p),
                        bi(ecAsHexHolder.getA()), bi(ecAsHexHolder.getB()));
                ECPointF2mPolynomial point = new ECPointF2mPolynomial(curve,
                        bi(ecAsHexHolder.getGx()),
                        bi(ecAsHexHolder.getGy()));

                return ECDomainParameter.getInstance(curve, point, bi(ecAsHexHolder.getN()), BigInteger.valueOf(ecAsHexHolder.getH()));

            } catch (EllipticCurveException e) {
                throw new CryptoException("Unable to Convert ECDomainParameters:" + e.getMessage(), e);
            }
        } else throw new IllegalArgumentException("Curve Type is Unknown:" + ecAsHexHolder.getCurveType());

    }

    public static void main(String[] args) {


        try {
//            ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec("sect571k1");
            ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec("sect163r1");
            EcAsHexHolder ecAsHexHolder2 = ECUtil.toHolder(ecParameterSpec);
            System.out.println(ecAsHexHolder2.getP());
            System.out.println(ecAsHexHolder2.getA());
            System.out.println(ecAsHexHolder2.getB());
            System.out.println(ecAsHexHolder2.getGx());
            System.out.println(ecAsHexHolder2.getGy());
            System.out.println(ecAsHexHolder2.getN());
            System.out.println(ecAsHexHolder2.getH());

//        ECDomainParameter instance = ECDomainParameter.getInstance(ECDomainParameter.getCurveOID(163));
            ECDomainParameter instance = ECDomainParameter.getInstance(ecParameterSpec);
            EcAsHexHolder ecAsHexHolder = ECUtil.toHolder(instance);
            System.out.println(ecAsHexHolder.getP());
            System.out.println(ecAsHexHolder.getA());
            System.out.println(ecAsHexHolder.getB());
            System.out.println(ecAsHexHolder.getGx());
            System.out.println(ecAsHexHolder.getGy());
            System.out.println(ecAsHexHolder.getN());
            System.out.println(ecAsHexHolder.getH());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
