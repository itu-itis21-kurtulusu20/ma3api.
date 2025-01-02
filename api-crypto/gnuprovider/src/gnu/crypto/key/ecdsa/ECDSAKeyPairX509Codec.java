/**
 * <p>Title: ESYA</p>
 * <p>Description:
 * </p>
 * <p>Copyright: TUBITAK Copyright (c) 2004</p>
 * <p>Company: TUBITAK UEKAE</p>
 * @author Muhammed Serdar SORAN
 * @version 1.0
 */
package gnu.crypto.key.ecdsa;

import com.objsys.asn1j.runtime.*;
import gnu.crypto.key.IKeyPairCodec;
import gnu.crypto.sig.ecdsa.ecmath.curve.*;
import gnu.crypto.sig.ecdsa.ecmath.exceptions.EllipticCurveException;
import gnu.crypto.sig.ecdsa.ecmath.field.FieldF2mPolynomial;
import gnu.crypto.sig.ecdsa.ecmath.field.FieldFp;
import tr.gov.tubitak.uekae.esya.asn.algorithms.*;
import tr.gov.tubitak.uekae.esya.asn.algorithms.Curve;
import tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8.ECPrivateKey;
import tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8.ECPrivateKey_version;
import tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8.PrivateKeyInfo;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;
import tr.gov.tubitak.uekae.esya.asn.util.UtilOpenType;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;

import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

/**
 * @author mss
 *
 */
public class ECDSAKeyPairX509Codec
          implements IKeyPairCodec
{

     /**
      * 
      */
     public ECDSAKeyPairX509Codec()
     {
          super();
     }


     public int getFormatID()
     {
       return X509_FORMAT;
     }

     public AlgorithmIdentifier toAlgID(ECDomainParameter aDomainParameter)
     throws Asn1Exception, IOException
     {
    	 
          if(aDomainParameter.getmParamOID() != null)
          {
        	  return new AlgorithmIdentifier(_algorithmsValues.id_ecPublicKey,
                      UtilOpenType.toOpenType(new Asn1ObjectIdentifier(aDomainParameter.getmParamOID())));
          }
          else
          {
        	  return _toAlgIDOld(aDomainParameter);
          }
     }

     public AlgorithmIdentifier toUnCompressedAlgID(ECDomainParameter aDomainParameter) throws Asn1Exception, IOException{
          return _toAlgIDOld(aDomainParameter);
     }

     
     private AlgorithmIdentifier _toAlgIDOld(ECDomainParameter aDomainParameter)
     throws Asn1Exception, IOException
     {
          int[] fieldTypeOID;
          Asn1Type fieldTypeParams;
          if(aDomainParameter.getMCurve() instanceof CurveFp)
          {
               fieldTypeOID = _algorithmsValues.prime_field;
               fieldTypeParams = new Asn1BigInteger(aDomainParameter.getMCurve().getMField().getMSize());
          }
          else
          {
               fieldTypeOID = _algorithmsValues.characteristic_two_field;
               
               CurveF2m curve = (CurveF2m) aDomainParameter.getMCurve();
               if( curve.getMField() instanceof FieldF2mPolynomial )
               {
                    FieldF2mPolynomial field = (FieldF2mPolynomial)curve.getMField();
                    int[] basis;
                    Asn1Type basisParam;
                    if(field.getMReductionPOnes().length == 3)
                    {
                         basis = _algorithmsValues.tpBasis;
                         basisParam = new Asn1Integer(field.getMReductionPOnes()[1]);
                    }
                    else if(field.getMReductionPOnes().length == 5)
                    {
                         basis = _algorithmsValues.ppBasis;
                         basisParam = new Pentanomial(field.getMReductionPOnes()[1],field.getMReductionPOnes()[2],field.getMReductionPOnes()[3]);
                    }
                    else
                         throw new IllegalArgumentException("key, F2m not trinomial or pentanomial basis");
                         
                         
                    fieldTypeParams = new Characteristic_two(
                                                             field.getMM(),
                                                             basis,
                                                             UtilOpenType.toOpenType(basisParam)
                    );
               }
               else
                    throw new IllegalArgumentException("key, F2m not polynomial");
          }
          
          FieldID fieldID = new FieldID(fieldTypeOID,UtilOpenType.toOpenType(fieldTypeParams));
          
          
          Curve curve = new Curve(new Asn1OctetString(aDomainParameter.getMCurve().getMA().toByteArray()),
                                  new Asn1OctetString(aDomainParameter.getMCurve().getMB().toByteArray()
                                  ));
          ECParameters ecParams = new ECParameters();
          ecParams.version = new ECPVer(ECPVer.ecpVer1);
          ecParams.fieldID = fieldID;
          ecParams.curve = curve;
          ecParams.cofactor =  new Asn1BigInteger(aDomainParameter.getMH());
          ecParams.base = new Asn1OctetString( aDomainParameter.getMG().toOctetString(ECGNUPoint.UNCOMPRESSED) );
          ecParams.order = new Asn1BigInteger( aDomainParameter.getMN());
          if(aDomainParameter.getMH() != null)
        	  ecParams.cofactor = new Asn1BigInteger(aDomainParameter.getMH());
          
          EcpkParameters params = new EcpkParameters();
          params.set_ecParameters(ecParams);
          AlgorithmIdentifier algID = new AlgorithmIdentifier(_algorithmsValues.id_ecPublicKey,
                                                              UtilOpenType.toOpenType(params));

          return algID;
     }
     
     /* (non-Javadoc)
      * @see gnu.crypto.key.IKeyPairCodec#encodePublicKey(java.security.PublicKey)
      */
     public byte[] encodePublicKey(PublicKey aKey)
     {
          try
          {
               SubjectPublicKeyInfo pubKeyInfo = getSubjectPublicKeyInfo(aKey);
               
               Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
               pubKeyInfo.encode(encBuf);
               return encBuf.getMsgCopy();
          } catch (IOException ex)
          {
               throw new RuntimeException("IO Error in ASN1 process",ex);
          } catch (Asn1Exception ex)
          {
               throw new RuntimeException("ASN1 Error in ASN1 process",ex);
          }
          
     }
     
     
     public SubjectPublicKeyInfo getSubjectPublicKeyInfo(PublicKey aKey) throws Asn1Exception, IOException
     {
    	 if(! (aKey instanceof ECDSAPublicKey)) {
             throw new IllegalArgumentException("key");
        }
        ECDSAPublicKey pubKey = (ECDSAPublicKey)aKey;

        
        AlgorithmIdentifier algID = toAlgID(pubKey.getMParameters());
        
        byte[] pubKeyBytes = pubKey.getMQ().toOctetString(ECGNUPoint.UNCOMPRESSED);
        SubjectPublicKeyInfo pubKeyInfo = new SubjectPublicKeyInfo(algID,
                                                                   new Asn1BitString(pubKeyBytes.length<<3,pubKeyBytes)
                                                                   );
        return pubKeyInfo;
        
     }


     /* (non-Javadoc)
      * @see gnu.crypto.key.IKeyPairCodec#encodePrivateKey(java.security.PrivateKey)
      */
     public byte[] encodePrivateKey(PrivateKey aKey)
     {
          try
          {
               if(! (aKey instanceof ECDSAPrivateKey)) {
                    throw new IllegalArgumentException("key");
               }
               ECDSAPrivateKey privKey = (ECDSAPrivateKey)aKey;

               byte[] mdValue = privKey.getMD().toByteArray();
               if(mdValue[0]==0x00){
                    mdValue = Arrays.copyOfRange(mdValue, 1, mdValue.length);
               }

               ECPrivateKey pk = new ECPrivateKey(ECPrivateKey_version.ecPrivkeyVer1, mdValue);
              
               Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
               pk.encode(enc);

               PrivateKeyInfo privKeyInfo = new PrivateKeyInfo(0L,
                                                               toAlgID(privKey.getMParameters()),
                                                               //privKey.getMD().toByteArray());
                                                               enc.getMsgCopy());

               Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
               privKeyInfo.encode(encBuf);
               return encBuf.getMsgCopy();
          } catch (IOException ex)
          {
               throw new RuntimeException("IO Error in ASN1 process",ex);
          } catch (Asn1Exception ex)
          {
               throw new RuntimeException("ASN1 Error in ASN1 process",ex);
          }
     }

     public byte[] encodePrivateKey(PrivateKey aPriKey,PublicKey aPubKey)
     {
          try
          {
               if(! (aPriKey instanceof ECDSAPrivateKey)) {
                    throw new IllegalArgumentException("key");
               }
               
               if(! (aPubKey instanceof ECDSAPublicKey)) {
                   throw new IllegalArgumentException("key");
              }
              
              ECDSAPublicKey pubKey = (ECDSAPublicKey)aPubKey;
              byte[] pubKeyBytes = pubKey.getMQ().toOctetString(ECGNUPoint.UNCOMPRESSED);
              
              ECDSAPrivateKey privKey = (ECDSAPrivateKey)aPriKey;
              ECPrivateKey pk = new ECPrivateKey(ECPrivateKey_version.ecPrivkeyVer1, privKey.getMD().toByteArray(),null,new Asn1BitString(pubKeyBytes.length<<3,pubKeyBytes));
              
               Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
               pk.encode(enc);
               
               PrivateKeyInfo privKeyInfo = new PrivateKeyInfo(0L,
                                                               toAlgID(privKey.getMParameters()),
                                                               //privKey.getMD().toByteArray());
                                                               enc.getMsgCopy());
               
               Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
               privKeyInfo.encode(encBuf);
               return encBuf.getMsgCopy();
          } catch (IOException ex)
          {
               throw new RuntimeException("IO Error in ASN1 process",ex);
          } catch (Asn1Exception ex)
          {
               throw new RuntimeException("ASN1 Error in ASN1 process",ex);
          }
     }
     
     
     public ECDomainParameter toDomainParameters(AlgorithmIdentifier aAlgID)
     throws Asn1Exception, IOException, EllipticCurveException
     {
          if(! UtilEsitlikler.esitMi(aAlgID.algorithm.value,_algorithmsValues.id_ecPublicKey))
          {
               throw new IllegalArgumentException("Not an ECDSA Algorithm ID");
          }
          
          EcpkParameters params = new EcpkParameters();
          UtilOpenType.fromOpenType(aAlgID.parameters,params);
          
          if(params.getChoiceID() == EcpkParameters._ECPARAMETERS)
          {
               ECParameters ecParams = (ECParameters)params.getElement();
               //
               gnu.crypto.sig.ecdsa.ecmath.curve.Curve curve;
               ECGNUPoint g;
               
               if(UtilEsitlikler.esitMi(ecParams.fieldID.fieldType.value,_algorithmsValues.prime_field))
               {
                    //The field is a Prime Field
                    Asn1BigInteger p = new Asn1BigInteger();
                    UtilOpenType.fromOpenType(ecParams.fieldID.parameters,p);
                    FieldFp field = FieldFp.getInstance(p.value);
                    
                    curve = new CurveFp(field,new BigInteger(1,ecParams.curve.a.value),new BigInteger(1,ecParams.curve.b.value));
                    g = new ECPointFp(curve,ecParams.base.value);
               }
               else if(UtilEsitlikler.esitMi(ecParams.fieldID.fieldType.value,_algorithmsValues.characteristic_two_field))
               {
                    //The Field is a F2m Field
                    Characteristic_two charTwo = new Characteristic_two();
                    UtilOpenType.fromOpenType(ecParams.fieldID.parameters,charTwo);
                    //Determine the reduction polynomial
                    BigInteger reductionP = new BigInteger("1");
                    reductionP = reductionP.setBit((int)charTwo.m.value);
                    if(UtilEsitlikler.esitMi(charTwo.basis.value,_algorithmsValues.tpBasis) )
                    {
                         Asn1Integer trinomial = new Asn1Integer();
                         UtilOpenType.fromOpenType(charTwo.parameters,trinomial);
                         
                         //trinomial bases
                         reductionP = reductionP.setBit((int)trinomial.value);
                    }
                    else if(UtilEsitlikler.esitMi(charTwo.basis.value,_algorithmsValues.ppBasis) )
                    {
                         Pentanomial pentanomial = new Pentanomial();
                         UtilOpenType.fromOpenType(charTwo.parameters,pentanomial);
                         
                         //pentanomial bases
                         reductionP = reductionP.setBit((int)pentanomial.k1.value);
                         reductionP = reductionP.setBit((int)pentanomial.k2.value);
                         reductionP = reductionP.setBit((int)pentanomial.k3.value);
                    }
                    else
                         throw new IllegalArgumentException("F2m not trinomial or pentanomial basis");
                    
                    FieldF2mPolynomial field = FieldF2mPolynomial.getInstance((int)charTwo.m.value,reductionP);

                    curve = new CurveF2m(field,new BigInteger(1,ecParams.curve.a.value),new BigInteger(1,ecParams.curve.b.value));
                    g = new ECPointF2mPolynomial(curve,ecParams.base.value);

               }
               else
                    throw new IllegalArgumentException("Field type not known");
                
               ECDomainParameter domainParam = null;
               if(ecParams.cofactor != null)
            	   domainParam=  ECDomainParameter.getInstance(curve,
								g,
								ecParams.order.value,
								ecParams.cofactor.value);
               else
            	   domainParam=  ECDomainParameter.getInstance(curve,
            		   								g,
            		   								ecParams.order.value); //TO-DO Cofactor hesaplanmali
               
               return domainParam;
          }
          else if(params.getChoiceID() == EcpkParameters._NAMEDCURVE)
          {
               ECDomainParameter p = ECDomainParameter.getInstance(((Asn1ObjectIdentifier)params.getElement()).value);
               if(p==null)
                    throw new IllegalArgumentException("Curve OID not known: "+((Asn1ObjectIdentifier)params.getElement()));
               return p;
          }
          else
               throw new IllegalArgumentException("Parameter not known");
     }
     

     /* (non-Javadoc)
      * @see gnu.crypto.key.IKeyPairCodec#decodePublicKey(byte[])
      */
     public PublicKey decodePublicKey(byte[] aInput)
     {
          try
          {
               Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(aInput);
               SubjectPublicKeyInfo pubKeyInfo = new SubjectPublicKeyInfo();
               pubKeyInfo.decode(decBuf);
               
               ECDomainParameter params = toDomainParameters(pubKeyInfo.algorithm);

               ECGNUPoint q = getECPoint(params, pubKeyInfo.subjectPublicKey.value);
               
               ECDSAPublicKey pubKey = new ECDSAPublicKey(params,q);

               return pubKey;
          } catch (IOException ex)
          {
               throw new RuntimeException("IO Error in ASN1 process",ex);
          } catch (Asn1Exception ex)
          {
               throw new RuntimeException("ASN1 Error in ASN1 process",ex);
          } catch (EllipticCurveException ex)
          {
               throw new RuntimeException("Wrong parameters",ex);
          }
     }

     public ECGNUPoint getECPoint(ECDomainParameter params, byte[] pointBytes) {
          try {
               ECGNUPoint q;
               if (params.getMCurve() instanceof CurveFp) {
                    q = new ECPointFp(params.getMCurve(), pointBytes);
               } else if (params.getMCurve() instanceof CurveF2m)
                    q = new ECPointF2mPolynomial(params.getMCurve(), pointBytes);
               else
                    throw new IllegalArgumentException("What's going on??");

               return q;
          } catch (EllipticCurveException ex) {
               throw new RuntimeException("Wrong parameters", ex);
          }
     }

     /* (non-Javadoc)
      * @see gnu.crypto.key.IKeyPairCodec#decodePrivateKey(byte[])
      */
     public PrivateKey decodePrivateKey(byte[] aInput)
     {

          try
          {
               Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(aInput);
               PrivateKeyInfo privKeyInfo = new PrivateKeyInfo();
               privKeyInfo.decode(decBuf);
               
               ECDomainParameter params = toDomainParameters(privKeyInfo.privateKeyAlgorithm);
               
               ECPrivateKey ecKey = new ECPrivateKey();
               decBuf = new Asn1DerDecodeBuffer(privKeyInfo.privateKey.value);
               ecKey.decode(decBuf);
               return new ECDSAPrivateKey(params,new BigInteger(1, ecKey.privateKey.value));
          } catch (IOException ex)
          {
               throw new RuntimeException("IO Error in ASN1 process",ex);
          } catch (Asn1Exception ex)
          {
               throw new RuntimeException("ASN1 Error in ASN1 process",ex);
          } catch (EllipticCurveException ex)
          {
               throw new RuntimeException("Wrong parameters",ex);
          }
     }
     
     public Object[] decodePriAndPubKey(byte[] aPriKey)
     {

          try
          {
               Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(aPriKey);
               PrivateKeyInfo privKeyInfo = new PrivateKeyInfo();
               privKeyInfo.decode(decBuf);
               
               ECDomainParameter params = toDomainParameters(privKeyInfo.privateKeyAlgorithm);
               
               ECPrivateKey ecKey = new ECPrivateKey();
               decBuf = new Asn1DerDecodeBuffer(privKeyInfo.privateKey.value);
               ecKey.decode(decBuf);
               
               ECDSAPrivateKey prikey = new ECDSAPrivateKey(params,new BigInteger(ecKey.privateKey.value));
               
               ECGNUPoint q;
               if(params.getMCurve() instanceof CurveFp)
               {
                    q = new ECPointFp(params.getMCurve(),ecKey.publicKey.value);
               }
               else if(params.getMCurve() instanceof CurveF2m)
                    q = new ECPointF2mPolynomial(params.getMCurve(),ecKey.publicKey.value);
               else
                    throw new IllegalArgumentException("What's going on??");
               
               ECDSAPublicKey pubkey = new ECDSAPublicKey(params,q);
               
               Object[] keys = new Object[2];
               keys[0] = prikey;
               keys[1] = pubkey;
               
               return keys;
          } catch (IOException ex)
          {
               throw new RuntimeException("IO Error in ASN1 process",ex);
          } catch (Asn1Exception ex)
          {
               throw new RuntimeException("ASN1 Error in ASN1 process",ex);
          } catch (EllipticCurveException ex)
          {
               throw new RuntimeException("Wrong parameters",ex);
          }
     }

}
