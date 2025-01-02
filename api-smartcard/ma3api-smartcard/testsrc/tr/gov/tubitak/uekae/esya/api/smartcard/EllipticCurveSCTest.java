package tr.gov.tubitak.uekae.esya.api.smartcard;

import gnu.crypto.key.ecdsa.ECDSAPublicKey;
import gnu.crypto.sig.ecdsa.ecmath.curve.CurveF2m;
import gnu.crypto.sig.ecdsa.ecmath.curve.CurveFp;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECDomainParameter;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECPointF2mPolynomial;
import gnu.crypto.sig.ecdsa.ecmath.curve.ECPointFp;
import gnu.crypto.sig.ecdsa.ecmath.field.FieldF2mPolynomial;
import gnu.crypto.sig.ecdsa.ecmath.field.FieldFp;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECFieldFp;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;

//import sun.security.ec.ECPrivateKeyImpl;
//import sun.security.ec.ECPublicKeyImpl;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import junit.framework.TestCase;

public class EllipticCurveSCTest extends TestCase{

    
    static SmartCard SC = null;
    static String PASSWORD = null;
    static long SLOT = -1;
    final static String  CREATED_KEY_LABEL = "CreatedECKey";
    final static String  IMPORTED_KEY_LABEL = "ImportedECKey";
    
    static
    {
	try
	{
	    CardType ct = CardType.UTIMACO;
	    PASSWORD = "123456";
	    SC = new SmartCard(ct);
	    SLOT = 5;
	}
	catch(Exception aEx)
	{
	    throw new RuntimeException("SmartCard objesi olusturulamadi", aEx);
	}
    }
    
    
    //prime field
    public void testCreateECKeyPair_secp160r1()
    throws PKCS11Exception,IOException,SmartCardException
    {
	String curveName = "secp160r1";
	ECGenParameterSpec spec = new ECGenParameterSpec(curveName);
	
	long sid = SC.openSession(SLOT);
	SC.login(sid, PASSWORD);
	String keyName = curveName+"_"+CREATED_KEY_LABEL;
	SC.createKeyPair(sid, keyName , spec, true, false);
	boolean sonuc1 = SC.isPrivateKeyExist(sid, keyName);
	boolean sonuc2 = SC.isPublicKeyExist(sid, keyName);
	ECPublicKeySpec readSpec = (ECPublicKeySpec) SC.readPublicKeySpec(sid, keyName);
	int fieldSize = readSpec.getParams().getCurve().getField().getFieldSize();
	boolean sonuc3 = fieldSize==160;
	    
	
	SC.logout(sid);
	SC.closeSession(sid);
	
	assertEquals(true, sonuc1&&sonuc2&&sonuc3);
	
    }
    
    //prime field
    public void testCreateECKeyPair_secp384r1()
    throws PKCS11Exception,IOException,SmartCardException
    {
	BigInteger p = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFF",16);
	BigInteger a = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFC",16);
	BigInteger b = new BigInteger("B3312FA7E23EE7E4988E056BE3F82D19181D9C6EFE8141120314088F5013875AC656398D8A2ED19D2A85C8EDD3EC2AEF",16);
	BigInteger x = new BigInteger("AA87CA22BE8B05378EB1C71EF320AD746E1D3B628BA79B9859F741E082542A385502F25DBF55296C3A545E3872760AB7",16);
	BigInteger y = new BigInteger("3617DE4A96262C6F5D9E98BF9292DC29F8F41DBD289A147CE9DA3113B5F0B8C00A60B1CE1D7E819D7A431D7C90EA0E5F",16);
	BigInteger n = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC7634D81F4372DDF581A0DB248B0A77AECEC196ACCC52973",16);
	int h = 1;
	
	ECFieldFp field = new ECFieldFp(p);
	EllipticCurve curve = new EllipticCurve(field, a, b);
	ECPoint g = new ECPoint(x, y);
	ECParameterSpec spec = new ECParameterSpec(curve, g, n, h);
	
	long sid = SC.openSession(SLOT);
	SC.login(sid, PASSWORD);
	String keyName = "secp384r1"+"_"+CREATED_KEY_LABEL;
	SC.createKeyPair(sid, keyName , spec, true, false);
	boolean sonuc1 = SC.isPrivateKeyExist(sid, keyName);
	boolean sonuc2 = SC.isPublicKeyExist(sid, keyName);
	
	ECPublicKeySpec readSpec = (ECPublicKeySpec) SC.readPublicKeySpec(sid, keyName);
	int fieldSize = readSpec.getParams().getCurve().getField().getFieldSize();
	boolean sonuc3 = fieldSize==384;
	
	SC.logout(sid);
	SC.closeSession(sid);
	
	assertEquals(true, sonuc1&&sonuc2&&sonuc3);
	
    }
    
    //characteristic two field (pentanomial)
    public void testCreateECKeyPair_sect571r1()
    throws PKCS11Exception,IOException,SmartCardException
    {
	//reduction polynomial
	BigInteger p = new BigInteger("080000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000425",16);
	
	
	BigInteger a = new BigInteger("000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001",16);
	BigInteger b = new BigInteger("02F40E7E2221F295DE297117B7F3D62F5C6A97FFCB8CEFF1CD6BA8CE4A9A18AD84FFABBD8EFA59332BE7AD6756A66E294AFD185A78FF12AA520E4DE739BACA0C7FFEFF7F2955727A",16);
	
	//generator
	BigInteger x = new BigInteger("0303001D34B856296C16C0D40D3CD7750A93D1D2955FA80AA5F40FC8DB7B2ABDBDE53950F4C0D293CDD711A35B67FB1499AE60038614F1394ABFA3B4C850D927E1E7769C8EEC2D19",16);
	BigInteger y = new BigInteger("037BF27342DA639B6DCCFFFEB73D69D78C6C27A6009CBBCA1980F8533921E8A684423E43BAB08A576291AF8F461BB2A8B3531D2F0485C19B16E2F1516E23DD3C1A4827AF1B8AC15B",16);
	
	//order of generator
	BigInteger n = new BigInteger("03FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFE661CE18FF55987308059B186823851EC7DD9CA1161DE93D5174D66E8382E9BB2FE84E47",16);
	
	//cofactor
	int h = 2;
	
	ECFieldF2m field = new ECFieldF2m(571,p);
	EllipticCurve curve = new EllipticCurve(field, a, b);
	ECPoint g = new ECPoint(x,y);
	ECParameterSpec spec = new ECParameterSpec(curve, g, n, h);
	
	long sid = SC.openSession(SLOT);
	SC.login(sid, PASSWORD);
	String keyName = "sect571r1"+"_"+CREATED_KEY_LABEL;
	SC.createKeyPair(sid, keyName , spec, true, false);
	boolean sonuc1 = SC.isPrivateKeyExist(sid, keyName);
	boolean sonuc2 = SC.isPublicKeyExist(sid, keyName);
	
	ECPublicKeySpec readSpec = (ECPublicKeySpec) SC.readPublicKeySpec(sid, keyName);
	int fieldSize = readSpec.getParams().getCurve().getField().getFieldSize();
	boolean sonuc3 = fieldSize==571;
	
	SC.logout(sid);
	SC.closeSession(sid);
	
	assertEquals(true, sonuc1&&sonuc2&&sonuc3);
	
    }
    
    
    
   //characteristic two field (trinomial)
    public void testCreateECKeyPair_sect233r1()
    throws PKCS11Exception,IOException,SmartCardException
    {
	//reduction polynomial
	BigInteger p = new BigInteger("020000000000000000000000000000000000000004000000000000000001",16);
	
	
	BigInteger a = new BigInteger("000000000000000000000000000000000000000000000000000000000001",16);
	BigInteger b = new BigInteger("0066647EDE6C332C7F8C0923BB58213B333B20E9CE4281FE115F7D8F90AD",16);
	
	//generator
	BigInteger x = new BigInteger("00FAC9DFCBAC8313BB2139F1BB755FEF65BC391F8B36F8F8EB7371FD558B",16);
	BigInteger y = new BigInteger("01006A08A41903350678E58528BEBF8A0BEFF867A7CA36716F7E01F81052",16);
	
	//order of generator
	BigInteger n = new BigInteger("01000000000000000000000000000013E974E72F8A6922031D2603CFE0D7",16);
	
	//cofactor
	int h = 2;
	
	ECFieldF2m field = new ECFieldF2m(233,p);
	EllipticCurve curve = new EllipticCurve(field, a, b);
	ECPoint g = new ECPoint(x,y);
	ECParameterSpec spec = new ECParameterSpec(curve, g, n, h);
	
	long sid = SC.openSession(SLOT);
	SC.login(sid, PASSWORD);
	String keyName = "sect233r1"+"_"+CREATED_KEY_LABEL;
	SC.createKeyPair(sid, keyName , spec, true, false);
	boolean sonuc1 = SC.isPrivateKeyExist(sid, keyName);
	boolean sonuc2 = SC.isPublicKeyExist(sid, keyName);
	
	ECPublicKeySpec readSpec = (ECPublicKeySpec) SC.readPublicKeySpec(sid, keyName);
	int fieldSize = readSpec.getParams().getCurve().getField().getFieldSize();
	boolean sonuc3 = fieldSize==233;
	
	SC.logout(sid);
	SC.closeSession(sid);
	
	assertEquals(true, sonuc1&&sonuc2&&sonuc3);
	
    }
    
   
    
    //characteristic two field (trinomial) (x^593+x^86+1)(custom curve)
    public void testCreateECKeyPair_custom593()
    throws PKCS11Exception,IOException,SmartCardException
    {
	BigInteger a = new BigInteger("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001", 16);
	BigInteger b = new BigInteger("027a23f8c92099b67c557ec2f8ef1ca0b90b37c5731bec211bf9a08fa52cf47778cf89158113c5fe8a2cc613707159b38f82676b2fcb4d95385c24db055613190a6a535653835a7f1e854", 16);
	BigInteger x = new BigInteger("18bef1024046e6c5457bc9bd07735559e154376cfe0615a11f4f2f0ec42aee2be323110a72fe0ffa8fde0f46c7d7a3192cc90835fb4718a05e6d6e70c337fc98f481676bd6270ae12382a", 16);
	BigInteger y = new BigInteger("177fa6f33abe42c9d54c32d97945d3dbba2fe0eb600e8310f98fd725d410429f56893c0a7d520ab17c57f8fd2cb7c14d831343571bfc767570bbdd2aba003b1b810b82551fa8566004a1c", 16);
	BigInteger n = new BigInteger("100000000000000000000000000000000000000000000000000000000000000000000000000e008a85be193eb288dd75ab4154e54043f5756da537cea2facb7144dd33845d5266bdb193b", 16);
	    
	
	//cofactor
	int h = 2;
	
	 ECFieldF2m f = new ECFieldF2m(593, new int[]{86});
	 EllipticCurve ec = new EllipticCurve(f, a, b);
	 ECPoint point = new ECPoint(x, y);
	 ECParameterSpec spec = new ECParameterSpec(ec, point, n, h);
	
	long sid = SC.openSession(SLOT);
	SC.login(sid, PASSWORD);
	String keyName = "custom593"+"_"+CREATED_KEY_LABEL;
	SC.createKeyPair(sid, keyName , spec, true, false);
	boolean sonuc1 = SC.isPrivateKeyExist(sid, keyName);
	boolean sonuc2 = SC.isPublicKeyExist(sid, keyName);
	
	ECPublicKeySpec readSpec = (ECPublicKeySpec) SC.readPublicKeySpec(sid, keyName);
	int fieldSize = readSpec.getParams().getCurve().getField().getFieldSize();
	boolean sonuc3 = fieldSize==593;
	
	SC.logout(sid);
	SC.closeSession(sid);
	
	assertEquals(true, sonuc1&&sonuc2&&sonuc3);
	
    }
    
    //ecpublickeyimpl de invalid EC key hatasi aliyor. Point does not match field size
   /* public void testImportECKeyPair_secp521r1()
    throws Exception
    {
	KeyPair kp = KeyUtil.generateKeyPair(AsymmetricAlg.ECDSA, 521);
	KeyPair kp2 = new KeyPair(new ECPublicKeyImpl(kp.getPublic().getEncoded()), new ECPrivateKeyImpl(kp.getPrivate().getEncoded()));
	
	
	long sid = SC.openSession(SLOT);
	SC.login(sid, PASSWORD);
	String keyName = "secp521r1"+"_"+IMPORTED_KEY_LABEL;
	SC.importKeyPair(sid, keyName, kp2, null, true, false);
	boolean sonuc1 = SC.isPrivateKeyExist(sid, keyName);
	boolean sonuc2 = SC.isPublicKeyExist(sid, keyName);
	
	ECPublicKeySpec readSpec = (ECPublicKeySpec) SC.readPublicKeySpec(sid, keyName);
	int fieldSize = readSpec.getParams().getCurve().getField().getFieldSize();
	boolean sonuc3 = fieldSize==521;
	
	SC.logout(sid);
	SC.closeSession(sid);
	
	assertEquals(true, sonuc1&&sonuc2&&sonuc3);
    }*/
    
    
    public void testSign_secp160r1()
    throws Exception
    {
	long sid = SC.openSession(SLOT);
	SC.login(sid, PASSWORD);
	String keyName = "secp160r1"+"_"+CREATED_KEY_LABEL;
	byte[] signature = SC.signData(sid, keyName, "test".getBytes(), PKCS11Constants.CKM_ECDSA_SHA1);
	ECPublicKeySpec spec = (ECPublicKeySpec) SC.readPublicKeySpec(sid, keyName);
	boolean scSonuc = true;
	try
	{
	    SC.verifyData(sid, keyName, "test".getBytes(), signature, PKCS11Constants.CKM_ECDSA_SHA1);
	}
	catch(PKCS11Exception aEx)
	{
	    scSonuc = false;
	}
	SC.logout(sid);
	SC.closeSession(sid);
	
	boolean gnuSonuc = verifyWithGNU_PrimeCurves(signature, "test".getBytes(), spec);
	assertEquals(true, gnuSonuc && scSonuc);
    }
    
    public void testSign_custom593()
    throws Exception
    {
	long sid = SC.openSession(SLOT);
	SC.login(sid, PASSWORD);
	String keyName = "custom593"+"_"+CREATED_KEY_LABEL;
	byte[] signature = SC.signData(sid, keyName, "test".getBytes(), PKCS11Constants.CKM_ECDSA_SHA1);
	ECPublicKeySpec spec = (ECPublicKeySpec) SC.readPublicKeySpec(sid, keyName);
	boolean scSonuc = true;
	try
	{
	    SC.verifyData(sid, keyName, "test".getBytes(), signature, PKCS11Constants.CKM_ECDSA_SHA1);
	}
	catch(PKCS11Exception aEx)
	{
	    scSonuc = false;
	}
	SC.logout(sid);
	SC.closeSession(sid);
	
	boolean gnuSonuc = verifyWithGNU_PolynomialCurves(signature, "test".getBytes(), spec);
	assertEquals(true, gnuSonuc && scSonuc);
    }
    
    
    
    
    
    public boolean verifyWithGNU_PrimeCurves(byte[] aSignature,byte[] aData,ECPublicKeySpec aSpec)
    throws Exception
    {
	ECDomainParameter param = ECDomainParameter.getInstance(aSpec.getParams());
	ECFieldFp fp = (ECFieldFp)aSpec.getParams().getCurve().getField();
	
	
	FieldFp fieldfp = FieldFp.getInstance(fp.getP());
	CurveFp curvefp = new CurveFp(fieldfp, aSpec.getParams().getCurve().getA(), aSpec.getParams().getCurve().getB()); 
	ECPointFp pointfp = new ECPointFp(curvefp, aSpec.getW().getAffineX(), aSpec.getW().getAffineY());
	ECDSAPublicKey pubkey = new ECDSAPublicKey(param, pointfp);
	
	
	return SignUtil.verify(SignatureAlg.ECDSA_SHA1, aData, aSignature, pubkey);
    }
    
    public boolean verifyWithGNU_PolynomialCurves(byte[] aSignature,byte[] aData,ECPublicKeySpec aSpec)
    throws Exception
    {
	ECDomainParameter param = ECDomainParameter.getInstance(aSpec.getParams());
	ECFieldF2m f2m = (ECFieldF2m)aSpec.getParams().getCurve().getField();
	
	
	FieldF2mPolynomial field2m = FieldF2mPolynomial.getInstance(f2m.getM(), f2m.getReductionPolynomial());
	CurveF2m curve2m = new CurveF2m(field2m, aSpec.getParams().getCurve().getA(), aSpec.getParams().getCurve().getB()); 
	ECPointF2mPolynomial point2m = new ECPointF2mPolynomial(curve2m, aSpec.getW().getAffineX(), aSpec.getW().getAffineY());
	ECDSAPublicKey pubkey = new ECDSAPublicKey(param, point2m);
	
	
	return SignUtil.verify(SignatureAlg.ECDSA_SHA1, aData, aSignature, pubkey);
    }
    
    
   
    public static void main(String[] args) {
	try
	{
	    EllipticCurveSCTest test = new EllipticCurveSCTest();
	    test.testSign_custom593();
	}
	catch (Exception e) 
	{
	    e.printStackTrace();
	}
    }
    
    
    
    
}
