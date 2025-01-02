/* Copyright  (c) 2002 Graz University of Technology. All rights reserved.
 *
 * Redistribution and use in  source and binary forms, with or without 
 * modification, are permitted  provided that the following conditions are met:
 *
 * 1. Redistributions of  source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in  binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 * 
 *    "This product includes software developed by IAIK of Graz University of
 *     Technology."
 * 
 *    Alternately, this acknowledgment may appear in the software itself, if 
 *    and wherever such third-party acknowledgments normally appear.
 *  
 * 4. The names "Graz University of Technology" and "IAIK of Graz University of
 *    Technology" must not be used to endorse or promote products derived from 
 *    this software without prior written permission.
 *  
 * 5. Products derived from this software may not be called 
 *    "IAIK PKCS Wrapper", nor may "IAIK" appear in their name, without prior 
 *    written permission of Graz University of Technology.
 *  
 *  THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *  PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE LICENSOR BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 *  OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 *  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 *  OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY  OF SUCH DAMAGE.
 */

package tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.pkcs11wrapper;
/**
 * Objects of this class represent a mechansim as defined in PKCS#11. There are
 * constants defined for all mechanisms that PKCS#11 version 2.11 defines.
 *
 * @author <a href="mailto:Karl.Scheibelhofer@iaik.at"> Karl Scheibelhofer </a>
 * @version 1.0
 */
public class SCMechanism implements Cloneable {

  /*
   * For each predefined mechanism of PKCS#11 v2.11 there is a constant. Refer
   * to the standard fro details.
   */
  public static final SCMechanism RSA_PKCS_KEY_PAIR_GEN      = new SCMechanism(PKCS11Constants.CKM_RSA_PKCS_KEY_PAIR_GEN);
  public static final SCMechanism RSA_PKCS                   = new SCMechanism(PKCS11Constants.CKM_RSA_PKCS);
  public static final SCMechanism RSA_9796                   = new SCMechanism(PKCS11Constants.CKM_RSA_9796);
  public static final SCMechanism RSA_X_509                  = new SCMechanism(PKCS11Constants.CKM_RSA_X_509);
  public static final SCMechanism MD2_RSA_PKCS               = new SCMechanism(PKCS11Constants.CKM_MD2_RSA_PKCS);
  public static final SCMechanism MD5_RSA_PKCS               = new SCMechanism(PKCS11Constants.CKM_MD5_RSA_PKCS);
  public static final SCMechanism SHA1_RSA_PKCS              = new SCMechanism(PKCS11Constants.CKM_SHA1_RSA_PKCS);
  public static final SCMechanism RIPEMD128_RSA_PKCS         = new SCMechanism(PKCS11Constants.CKM_RIPEMD128_RSA_PKCS);
  public static final SCMechanism RIPEMD160_RSA_PKCS         = new SCMechanism(PKCS11Constants.CKM_RIPEMD160_RSA_PKCS);
  public static final SCMechanism SHA256_RSA_PKCS            = new SCMechanism(PKCS11Constants.CKM_SHA256_RSA_PKCS);
  public static final SCMechanism SHA384_RSA_PKCS            = new SCMechanism(PKCS11Constants.CKM_SHA384_RSA_PKCS);
  public static final SCMechanism SHA512_RSA_PKCS            = new SCMechanism(PKCS11Constants.CKM_SHA512_RSA_PKCS);
  public static final SCMechanism RSA_PKCS_OAEP              = new SCMechanism(PKCS11Constants.CKM_RSA_PKCS_OAEP);

  public static final SCMechanism RSA_X9_31_KEY_PAIR_GEN     = new SCMechanism(PKCS11Constants.CKM_RSA_X9_31_KEY_PAIR_GEN);
  public static final SCMechanism RSA_X9_31                  = new SCMechanism(PKCS11Constants.CKM_RSA_X9_31);
  public static final SCMechanism SHA1_RSA_X9_31             = new SCMechanism(PKCS11Constants.CKM_SHA1_RSA_X9_31);
  public static final SCMechanism RSA_PKCS_PSS               = new SCMechanism(PKCS11Constants.CKM_RSA_PKCS_PSS);
  public static final SCMechanism SHA1_RSA_PKCS_PSS          = new SCMechanism(PKCS11Constants.CKM_SHA1_RSA_PKCS_PSS);

  public static final SCMechanism SHA256_RSA_PKCS_PSS        = new SCMechanism(PKCS11Constants.CKM_SHA256_RSA_PKCS_PSS);
  public static final SCMechanism SHA384_RSA_PKCS_PSS        = new SCMechanism(PKCS11Constants.CKM_SHA384_RSA_PKCS_PSS);
  public static final SCMechanism SHA512_RSA_PKCS_PSS        = new SCMechanism(PKCS11Constants.CKM_SHA512_RSA_PKCS_PSS);

  public static final SCMechanism DSA_KEY_PAIR_GEN           = new SCMechanism(PKCS11Constants.CKM_DSA_KEY_PAIR_GEN);
  public static final SCMechanism DSA                        = new SCMechanism(PKCS11Constants.CKM_DSA);
  public static final SCMechanism DSA_SHA1                   = new SCMechanism(PKCS11Constants.CKM_DSA_SHA1);
  public static final SCMechanism DH_PKCS_KEY_PAIR_GEN       = new SCMechanism(PKCS11Constants.CKM_DH_PKCS_KEY_PAIR_GEN);
  public static final SCMechanism DH_PKCS_DERIVE             = new SCMechanism(PKCS11Constants.CKM_DH_PKCS_DERIVE);

  public static final SCMechanism X9_42_DH_KEY_PAIR_GEN      = new SCMechanism(PKCS11Constants.CKM_X9_42_DH_KEY_PAIR_GEN);
  public static final SCMechanism X9_42_DH_DERIVE            = new SCMechanism(PKCS11Constants.CKM_X9_42_DH_DERIVE);
  public static final SCMechanism X9_42_DH_HYBRID_DERIVE     = new SCMechanism(PKCS11Constants.CKM_X9_42_DH_HYBRID_DERIVE);
  public static final SCMechanism X9_42_MQV_DERIVE           = new SCMechanism(PKCS11Constants.CKM_X9_42_MQV_DERIVE);

  public static final SCMechanism RC2_KEY_GEN                = new SCMechanism(PKCS11Constants.CKM_RC2_KEY_GEN);
  public static final SCMechanism RC2_ECB                    = new SCMechanism(PKCS11Constants.CKM_RC2_ECB);
  public static final SCMechanism RC2_CBC                    = new SCMechanism(PKCS11Constants.CKM_RC2_CBC);
  public static final SCMechanism RC2_MAC                    = new SCMechanism(PKCS11Constants.CKM_RC2_MAC);
  public static final SCMechanism RC2_MAC_GENERAL            = new SCMechanism(PKCS11Constants.CKM_RC2_MAC_GENERAL);
  public static final SCMechanism RC2_CBC_PAD                = new SCMechanism(PKCS11Constants.CKM_RC2_CBC_PAD);
  public static final SCMechanism RC4_KEY_GEN                = new SCMechanism(PKCS11Constants.CKM_RC4_KEY_GEN);
  public static final SCMechanism RC4                        = new SCMechanism(PKCS11Constants.CKM_RC4);
  public static final SCMechanism DES_KEY_GEN                = new SCMechanism(PKCS11Constants.CKM_DES_KEY_GEN);
  public static final SCMechanism DES_ECB                    = new SCMechanism(PKCS11Constants.CKM_DES_ECB);
  public static final SCMechanism DES_CBC                    = new SCMechanism(PKCS11Constants.CKM_DES_CBC);
  public static final SCMechanism DES_MAC                    = new SCMechanism(PKCS11Constants.CKM_DES_MAC);
  public static final SCMechanism DES_MAC_GENERAL            = new SCMechanism(PKCS11Constants.CKM_DES_MAC_GENERAL);
  public static final SCMechanism DES_CBC_PAD                = new SCMechanism(PKCS11Constants.CKM_DES_CBC_PAD);
  
  /*
  public static final SCMechanism DES_OFB64                  = new SCMechanism(PKCS11Constants.CKM_DES_OFB64);
  public static final SCMechanism DES_OFB8                   = new SCMechanism(PKCS11Constants.CKM_DES_OFB8);
  public static final SCMechanism DES_CFB64                  = new SCMechanism(PKCS11Constants.CKM_DES_CFB64);
  public static final SCMechanism DES_CFB8                   = new SCMechanism(PKCS11Constants.CKM_DES_CFB8); */
  
  public static final SCMechanism DES2_KEY_GEN               = new SCMechanism(PKCS11Constants.CKM_DES2_KEY_GEN);
  public static final SCMechanism DES3_KEY_GEN               = new SCMechanism(PKCS11Constants.CKM_DES3_KEY_GEN);
  public static final SCMechanism DES3_ECB                   = new SCMechanism(PKCS11Constants.CKM_DES3_ECB);
  public static final SCMechanism DES3_CBC                   = new SCMechanism(PKCS11Constants.CKM_DES3_CBC);
  public static final SCMechanism DES3_MAC                   = new SCMechanism(PKCS11Constants.CKM_DES3_MAC);
  public static final SCMechanism DES3_MAC_GENERAL           = new SCMechanism(PKCS11Constants.CKM_DES3_MAC_GENERAL);
  public static final SCMechanism DES3_CBC_PAD               = new SCMechanism(PKCS11Constants.CKM_DES3_CBC_PAD);
  public static final SCMechanism CDMF_KEY_GEN               = new SCMechanism(PKCS11Constants.CKM_CDMF_KEY_GEN);
  public static final SCMechanism CDMF_ECB                   = new SCMechanism(PKCS11Constants.CKM_CDMF_ECB);
  public static final SCMechanism CDMF_CBC                   = new SCMechanism(PKCS11Constants.CKM_CDMF_CBC);
  public static final SCMechanism CDMF_MAC                   = new SCMechanism(PKCS11Constants.CKM_CDMF_MAC);
  public static final SCMechanism CDMF_MAC_GENERAL           = new SCMechanism(PKCS11Constants.CKM_CDMF_MAC_GENERAL);
  public static final SCMechanism CDMF_CBC_PAD               = new SCMechanism(PKCS11Constants.CKM_CDMF_CBC_PAD);
  public static final SCMechanism MD2                        = new SCMechanism(PKCS11Constants.CKM_MD2);
  public static final SCMechanism MD2_HMAC                   = new SCMechanism(PKCS11Constants.CKM_MD2_HMAC);
  public static final SCMechanism MD2_HMAC_GENERAL           = new SCMechanism(PKCS11Constants.CKM_MD2_HMAC_GENERAL);
  public static final SCMechanism MD5                        = new SCMechanism(PKCS11Constants.CKM_MD5);
  public static final SCMechanism MD5_HMAC                   = new SCMechanism(PKCS11Constants.CKM_MD5_HMAC);
  public static final SCMechanism MD5_HMAC_GENERAL           = new SCMechanism(PKCS11Constants.CKM_MD5_HMAC_GENERAL);
  public static final SCMechanism SHA_1                      = new SCMechanism(PKCS11Constants.CKM_SHA_1);
  public static final SCMechanism SHA_1_HMAC                 = new SCMechanism(PKCS11Constants.CKM_SHA_1_HMAC);
  public static final SCMechanism SHA_1_HMAC_GENERAL         = new SCMechanism(PKCS11Constants.CKM_SHA_1_HMAC_GENERAL);
  
  public static final SCMechanism SHA256                     = new SCMechanism(PKCS11Constants.CKM_SHA256);
  public static final SCMechanism SHA256_HMAC                = new SCMechanism(PKCS11Constants.CKM_SHA256_HMAC);
  public static final SCMechanism SHA256_HMAC_GENERAL        = new SCMechanism(PKCS11Constants.CKM_SHA256_HMAC_GENERAL);
  public static final SCMechanism SHA384                     = new SCMechanism(PKCS11Constants.CKM_SHA384);
  public static final SCMechanism SHA384_HMAC                = new SCMechanism(PKCS11Constants.CKM_SHA384_HMAC);
  public static final SCMechanism SHA384_HMAC_GENERAL        = new SCMechanism(PKCS11Constants.CKM_SHA384_HMAC_GENERAL);
  public static final SCMechanism SHA512                     = new SCMechanism(PKCS11Constants.CKM_SHA512);
  public static final SCMechanism SHA512_HMAC                = new SCMechanism(PKCS11Constants.CKM_SHA512_HMAC);
  public static final SCMechanism SHA512_HMAC_GENERAL        = new SCMechanism(PKCS11Constants.CKM_SHA512_HMAC_GENERAL);
  
  public static final SCMechanism RIPEMD128                  = new SCMechanism(PKCS11Constants.CKM_RIPEMD128);
  public static final SCMechanism RIPEMD128_HMAC             = new SCMechanism(PKCS11Constants.CKM_RIPEMD128_HMAC);
  public static final SCMechanism RIPEMD128_HMAC_GENERAL     = new SCMechanism(PKCS11Constants.CKM_RIPEMD128_HMAC_GENERAL);
  public static final SCMechanism RIPEMD160                  = new SCMechanism(PKCS11Constants.CKM_RIPEMD160);
  public static final SCMechanism RIPEMD160_HMAC             = new SCMechanism(PKCS11Constants.CKM_RIPEMD160_HMAC);
  public static final SCMechanism RIPEMD160_HMAC_GENERAL     = new SCMechanism(PKCS11Constants.CKM_RIPEMD160_HMAC_GENERAL);
  public static final SCMechanism CAST_KEY_GEN               = new SCMechanism(PKCS11Constants.CKM_CAST_KEY_GEN);
  public static final SCMechanism CAST_ECB                   = new SCMechanism(PKCS11Constants.CKM_CAST_ECB);
  public static final SCMechanism CAST_CBC                   = new SCMechanism(PKCS11Constants.CKM_CAST_CBC);
  public static final SCMechanism CAST_MAC                   = new SCMechanism(PKCS11Constants.CKM_CAST_MAC);
  public static final SCMechanism CAST_MAC_GENERAL           = new SCMechanism(PKCS11Constants.CKM_CAST_MAC_GENERAL);
  public static final SCMechanism CAST_CBC_PAD               = new SCMechanism(PKCS11Constants.CKM_CAST_CBC_PAD);
  public static final SCMechanism CAST3_KEY_GEN              = new SCMechanism(PKCS11Constants.CKM_CAST3_KEY_GEN);
  public static final SCMechanism CAST3_ECB                  = new SCMechanism(PKCS11Constants.CKM_CAST3_ECB);
  public static final SCMechanism CAST3_CBC                  = new SCMechanism(PKCS11Constants.CKM_CAST3_CBC);
  public static final SCMechanism CAST3_MAC                  = new SCMechanism(PKCS11Constants.CKM_CAST3_MAC);
  public static final SCMechanism CAST3_MAC_GENERAL          = new SCMechanism(PKCS11Constants.CKM_CAST3_MAC_GENERAL);
  public static final SCMechanism CAST3_CBC_PAD              = new SCMechanism(PKCS11Constants.CKM_CAST3_CBC_PAD);
  public static final SCMechanism CAST5_KEY_GEN              = new SCMechanism(PKCS11Constants.CKM_CAST5_KEY_GEN);
  public static final SCMechanism CAST128_KEY_GEN            = new SCMechanism(PKCS11Constants.CKM_CAST128_KEY_GEN);
  public static final SCMechanism CAST5_ECB                  = new SCMechanism(PKCS11Constants.CKM_CAST5_ECB);
  public static final SCMechanism CAST128_ECB                = new SCMechanism(PKCS11Constants.CKM_CAST128_ECB);
  public static final SCMechanism CAST5_CBC                  = new SCMechanism(PKCS11Constants.CKM_CAST5_CBC);
  public static final SCMechanism CAST128_CBC                = new SCMechanism(PKCS11Constants.CKM_CAST128_CBC);
  public static final SCMechanism CAST5_MAC                  = new SCMechanism(PKCS11Constants.CKM_CAST5_MAC);
  public static final SCMechanism CAST128_MAC                = new SCMechanism(PKCS11Constants.CKM_CAST128_MAC);
  public static final SCMechanism CAST5_MAC_GENERAL          = new SCMechanism(PKCS11Constants.CKM_CAST5_MAC_GENERAL);
  public static final SCMechanism CAST128_MAC_GENERAL        = new SCMechanism(PKCS11Constants.CKM_CAST128_MAC_GENERAL);
  public static final SCMechanism CAST5_CBC_PAD              = new SCMechanism(PKCS11Constants.CKM_CAST5_CBC_PAD);
  public static final SCMechanism CAST128_CBC_PAD            = new SCMechanism(PKCS11Constants.CKM_CAST128_CBC_PAD);
  public static final SCMechanism RC5_KEY_GEN                = new SCMechanism(PKCS11Constants.CKM_RC5_KEY_GEN);
  public static final SCMechanism RC5_ECB                    = new SCMechanism(PKCS11Constants.CKM_RC5_ECB);
  public static final SCMechanism RC5_CBC                    = new SCMechanism(PKCS11Constants.CKM_RC5_CBC);
  public static final SCMechanism RC5_MAC                    = new SCMechanism(PKCS11Constants.CKM_RC5_MAC);
  public static final SCMechanism RC5_MAC_GENERAL            = new SCMechanism(PKCS11Constants.CKM_RC5_MAC_GENERAL);
  public static final SCMechanism RC5_CBC_PAD                = new SCMechanism(PKCS11Constants.CKM_RC5_CBC_PAD);
  public static final SCMechanism IDEA_KEY_GEN               = new SCMechanism(PKCS11Constants.CKM_IDEA_KEY_GEN);
  public static final SCMechanism IDEA_ECB                   = new SCMechanism(PKCS11Constants.CKM_IDEA_ECB);
  public static final SCMechanism IDEA_CBC                   = new SCMechanism(PKCS11Constants.CKM_IDEA_CBC);
  public static final SCMechanism IDEA_MAC                   = new SCMechanism(PKCS11Constants.CKM_IDEA_MAC);
  public static final SCMechanism IDEA_MAC_GENERAL           = new SCMechanism(PKCS11Constants.CKM_IDEA_MAC_GENERAL);
  public static final SCMechanism IDEA_CBC_PAD               = new SCMechanism(PKCS11Constants.CKM_IDEA_CBC_PAD);
  public static final SCMechanism GENERIC_SECRET_KEY_GEN     = new SCMechanism(PKCS11Constants.CKM_GENERIC_SECRET_KEY_GEN);
  public static final SCMechanism CONCATENATE_BASE_AND_KEY   = new SCMechanism(PKCS11Constants.CKM_CONCATENATE_BASE_AND_KEY);
  public static final SCMechanism CONCATENATE_BASE_AND_DATA  = new SCMechanism(PKCS11Constants.CKM_CONCATENATE_BASE_AND_DATA);
  public static final SCMechanism CONCATENATE_DATA_AND_BASE  = new SCMechanism(PKCS11Constants.CKM_CONCATENATE_DATA_AND_BASE);
  public static final SCMechanism XOR_BASE_AND_DATA          = new SCMechanism(PKCS11Constants.CKM_XOR_BASE_AND_DATA);
  public static final SCMechanism EXTRACT_KEY_FROM_KEY       = new SCMechanism(PKCS11Constants.CKM_EXTRACT_KEY_FROM_KEY);
  public static final SCMechanism SSL3_PRE_MASTER_KEY_GEN    = new SCMechanism(PKCS11Constants.CKM_SSL3_PRE_MASTER_KEY_GEN);
  public static final SCMechanism SSL3_MASTER_KEY_DERIVE     = new SCMechanism(PKCS11Constants.CKM_SSL3_MASTER_KEY_DERIVE);
  public static final SCMechanism SSL3_KEY_AND_MAC_DERIVE    = new SCMechanism(PKCS11Constants.CKM_SSL3_KEY_AND_MAC_DERIVE);

  public static final SCMechanism SSL3_MASTER_KEY_DERIVE_DH  = new SCMechanism(PKCS11Constants.CKM_SSL3_MASTER_KEY_DERIVE_DH);
  public static final SCMechanism TLS_PRE_MASTER_KEY_GEN     = new SCMechanism(PKCS11Constants.CKM_TLS_PRE_MASTER_KEY_GEN);
  public static final SCMechanism TLS_MASTER_KEY_DERIVE      = new SCMechanism(PKCS11Constants.CKM_TLS_MASTER_KEY_DERIVE);
  public static final SCMechanism TLS_KEY_AND_MAC_DERIVE     = new SCMechanism(PKCS11Constants.CKM_TLS_KEY_AND_MAC_DERIVE);
  public static final SCMechanism TLS_MASTER_KEY_DERIVE_DH   = new SCMechanism(PKCS11Constants.CKM_TLS_MASTER_KEY_DERIVE_DH);
  public static final SCMechanism TLS_PRF                    = new SCMechanism(PKCS11Constants.CKM_TLS_PRF);


  public static final SCMechanism WTLS_PRE_MASTER_KEY_GEN        = new SCMechanism(PKCS11Constants.CKM_WTLS_PRE_MASTER_KEY_GEN);
  public static final SCMechanism WTLS_MASTER_KEY_DERIVE         = new SCMechanism(PKCS11Constants.CKM_WTLS_MASTER_KEY_DERIVE);
  public static final SCMechanism WTLS_MASTER_KEY_DERIVE_DH_ECC  = new SCMechanism(PKCS11Constants.CKM_WTLS_MASTER_KEY_DERIVE_DH_ECC);
  public static final SCMechanism WTLS_PRF                       = new SCMechanism(PKCS11Constants.CKM_WTLS_PRF);
  public static final SCMechanism WTLS_SERVER_KEY_AND_MAC_DERIVE = new SCMechanism(PKCS11Constants.CKM_WTLS_SERVER_KEY_AND_MAC_DERIVE);
  public static final SCMechanism WTLS_CLIENT_KEY_AND_MAC_DERIVE = new SCMechanism(PKCS11Constants.CKM_WTLS_CLIENT_KEY_AND_MAC_DERIVE);


  public static final SCMechanism SSL3_MD5_MAC               = new SCMechanism(PKCS11Constants.CKM_SSL3_MD5_MAC);
  public static final SCMechanism SSL3_SHA1_MAC              = new SCMechanism(PKCS11Constants.CKM_SSL3_SHA1_MAC);
  public static final SCMechanism MD5_KEY_DERIVATION         = new SCMechanism(PKCS11Constants.CKM_MD5_KEY_DERIVATION);
  public static final SCMechanism MD2_KEY_DERIVATION         = new SCMechanism(PKCS11Constants.CKM_MD2_KEY_DERIVATION);
  public static final SCMechanism SHA1_KEY_DERIVATION        = new SCMechanism(PKCS11Constants.CKM_SHA1_KEY_DERIVATION);

  public static final SCMechanism SHA256_KEY_DERIVATION        = new SCMechanism(PKCS11Constants.CKM_SHA256_KEY_DERIVATION);
  public static final SCMechanism SHA384_KEY_DERIVATION        = new SCMechanism(PKCS11Constants.CKM_SHA384_KEY_DERIVATION);
  public static final SCMechanism SHA512_KEY_DERIVATION        = new SCMechanism(PKCS11Constants.CKM_SHA512_KEY_DERIVATION);

  public static final SCMechanism PBE_MD2_DES_CBC            = new SCMechanism(PKCS11Constants.CKM_PBE_MD2_DES_CBC);
  public static final SCMechanism PBE_MD5_DES_CBC            = new SCMechanism(PKCS11Constants.CKM_PBE_MD5_DES_CBC);
  public static final SCMechanism PBE_MD5_CAST_CBC           = new SCMechanism(PKCS11Constants.CKM_PBE_MD5_CAST_CBC);
  public static final SCMechanism PBE_MD5_CAST3_CBC          = new SCMechanism(PKCS11Constants.CKM_PBE_MD5_CAST3_CBC);
  public static final SCMechanism PBE_MD5_CAST5_CBC          = new SCMechanism(PKCS11Constants.CKM_PBE_MD5_CAST5_CBC);
  public static final SCMechanism PBE_MD5_CAST128_CBC        = new SCMechanism(PKCS11Constants.CKM_PBE_MD5_CAST128_CBC);
  public static final SCMechanism PBE_SHA1_CAST5_CBC         = new SCMechanism(PKCS11Constants.CKM_PBE_SHA1_CAST5_CBC);
  public static final SCMechanism PBE_SHA1_CAST128_CBC       = new SCMechanism(PKCS11Constants.CKM_PBE_SHA1_CAST128_CBC);
  public static final SCMechanism PBE_SHA1_RC4_128           = new SCMechanism(PKCS11Constants.CKM_PBE_SHA1_RC4_128);
  public static final SCMechanism PBE_SHA1_RC4_40            = new SCMechanism(PKCS11Constants.CKM_PBE_SHA1_RC4_40);
  public static final SCMechanism PBE_SHA1_DES3_EDE_CBC      = new SCMechanism(PKCS11Constants.CKM_PBE_SHA1_DES3_EDE_CBC);
  public static final SCMechanism PBE_SHA1_DES2_EDE_CBC      = new SCMechanism(PKCS11Constants.CKM_PBE_SHA1_DES2_EDE_CBC);
  public static final SCMechanism PBE_SHA1_RC2_128_CBC       = new SCMechanism(PKCS11Constants.CKM_PBE_SHA1_RC2_128_CBC);
  public static final SCMechanism PBE_SHA1_RC2_40_CBC        = new SCMechanism(PKCS11Constants.CKM_PBE_SHA1_RC2_40_CBC);
  public static final SCMechanism PKCS5_PBKD2                = new SCMechanism(PKCS11Constants.CKM_PKCS5_PBKD2);
  public static final SCMechanism PBA_SHA1_WITH_SHA1_HMAC    = new SCMechanism(PKCS11Constants.CKM_PBA_SHA1_WITH_SHA1_HMAC);
  public static final SCMechanism KEY_WRAP_LYNKS             = new SCMechanism(PKCS11Constants.CKM_KEY_WRAP_LYNKS);
  public static final SCMechanism KEY_WRAP_SET_OAEP          = new SCMechanism(PKCS11Constants.CKM_KEY_WRAP_SET_OAEP);
  public static final SCMechanism SKIPJACK_KEY_GEN           = new SCMechanism(PKCS11Constants.CKM_SKIPJACK_KEY_GEN);
  public static final SCMechanism SKIPJACK_ECB64             = new SCMechanism(PKCS11Constants.CKM_SKIPJACK_ECB64);
  public static final SCMechanism SKIPJACK_CBC64             = new SCMechanism(PKCS11Constants.CKM_SKIPJACK_CBC64);
  public static final SCMechanism SKIPJACK_OFB64             = new SCMechanism(PKCS11Constants.CKM_SKIPJACK_OFB64);
  public static final SCMechanism SKIPJACK_CFB64             = new SCMechanism(PKCS11Constants.CKM_SKIPJACK_CFB64);
  public static final SCMechanism SKIPJACK_CFB32             = new SCMechanism(PKCS11Constants.CKM_SKIPJACK_CFB32);
  public static final SCMechanism SKIPJACK_CFB16             = new SCMechanism(PKCS11Constants.CKM_SKIPJACK_CFB16);
  public static final SCMechanism SKIPJACK_CFB8              = new SCMechanism(PKCS11Constants.CKM_SKIPJACK_CFB8);
  public static final SCMechanism SKIPJACK_WRAP              = new SCMechanism(PKCS11Constants.CKM_SKIPJACK_WRAP);
  public static final SCMechanism SKIPJACK_PRIVATE_WRAP      = new SCMechanism(PKCS11Constants.CKM_SKIPJACK_PRIVATE_WRAP);
  public static final SCMechanism SKIPJACK_RELAYX            = new SCMechanism(PKCS11Constants.CKM_SKIPJACK_RELAYX);
  public static final SCMechanism KEA_KEY_PAIR_GEN           = new SCMechanism(PKCS11Constants.CKM_KEA_KEY_PAIR_GEN);
  public static final SCMechanism KEA_KEY_DERIVE             = new SCMechanism(PKCS11Constants.CKM_KEA_KEY_DERIVE);
  public static final SCMechanism FORTEZZA_TIMESTAMP         = new SCMechanism(PKCS11Constants.CKM_FORTEZZA_TIMESTAMP);
  public static final SCMechanism BATON_KEY_GEN              = new SCMechanism(PKCS11Constants.CKM_BATON_KEY_GEN);
  public static final SCMechanism BATON_ECB128               = new SCMechanism(PKCS11Constants.CKM_BATON_ECB128);
  public static final SCMechanism BATON_ECB96                = new SCMechanism(PKCS11Constants.CKM_BATON_ECB96);
  public static final SCMechanism BATON_CBC128               = new SCMechanism(PKCS11Constants.CKM_BATON_CBC128);
  public static final SCMechanism BATON_COUNTER              = new SCMechanism(PKCS11Constants.CKM_BATON_COUNTER);
  public static final SCMechanism BATON_SHUFFLE              = new SCMechanism(PKCS11Constants.CKM_BATON_SHUFFLE);
  public static final SCMechanism BATON_WRAP                 = new SCMechanism(PKCS11Constants.CKM_BATON_WRAP);
  public static final SCMechanism ECDSA_KEY_PAIR_GEN         = new SCMechanism(PKCS11Constants.CKM_ECDSA_KEY_PAIR_GEN);
  public static final SCMechanism EC_KEY_PAIR_GEN            = new SCMechanism(PKCS11Constants.CKM_EC_KEY_PAIR_GEN);
  public static final SCMechanism ECDSA                      = new SCMechanism(PKCS11Constants.CKM_ECDSA);
  public static final SCMechanism ECDSA_SHA1                 = new SCMechanism(PKCS11Constants.CKM_ECDSA_SHA1);

  public static final SCMechanism ECDH1_DERIVE               = new SCMechanism(PKCS11Constants.CKM_ECDH1_DERIVE);
  public static final SCMechanism ECDH1_COFACTOR_DERIVE      = new SCMechanism(PKCS11Constants.CKM_ECDH1_COFACTOR_DERIVE);
  public static final SCMechanism ECMQV_DERIVE               = new SCMechanism(PKCS11Constants.CKM_ECMQV_DERIVE);


  public static final SCMechanism JUNIPER_KEY_GEN            = new SCMechanism(PKCS11Constants.CKM_JUNIPER_KEY_GEN);
  public static final SCMechanism JUNIPER_ECB128             = new SCMechanism(PKCS11Constants.CKM_JUNIPER_ECB128);
  public static final SCMechanism JUNIPER_CBC128             = new SCMechanism(PKCS11Constants.CKM_JUNIPER_CBC128);
  public static final SCMechanism JUNIPER_COUNTER            = new SCMechanism(PKCS11Constants.CKM_JUNIPER_COUNTER);
  public static final SCMechanism JUNIPER_SHUFFLE            = new SCMechanism(PKCS11Constants.CKM_JUNIPER_SHUFFLE);
  public static final SCMechanism JUNIPER_WRAP               = new SCMechanism(PKCS11Constants.CKM_JUNIPER_WRAP);
  public static final SCMechanism FASTHASH                   = new SCMechanism(PKCS11Constants.CKM_FASTHASH);

  public static final SCMechanism AES_KEY_GEN                = new SCMechanism(PKCS11Constants.CKM_AES_KEY_GEN);
  public static final SCMechanism AES_ECB                    = new SCMechanism(PKCS11Constants.CKM_AES_ECB);
  public static final SCMechanism AES_CBC                    = new SCMechanism(PKCS11Constants.CKM_AES_CBC);
  public static final SCMechanism AES_MAC                    = new SCMechanism(PKCS11Constants.CKM_AES_MAC);
  public static final SCMechanism AES_MAC_GENERAL            = new SCMechanism(PKCS11Constants.CKM_AES_MAC_GENERAL);
  public static final SCMechanism AES_CBC_PAD                = new SCMechanism(PKCS11Constants.CKM_AES_CBC_PAD);

  public static final SCMechanism BLOWFISH_KEY_GEN                = new SCMechanism(PKCS11Constants.CKM_BLOWFISH_KEY_GEN);
  public static final SCMechanism BLOWFISH_CBC                    = new SCMechanism(PKCS11Constants.CKM_BLOWFISH_CBC);
  
  public static final SCMechanism DSA_PARAMETER_GEN          = new SCMechanism(PKCS11Constants.CKM_DSA_PARAMETER_GEN);
  public static final SCMechanism DH_PKCS_PARAMETER_GEN      = new SCMechanism(PKCS11Constants.CKM_DH_PKCS_PARAMETER_GEN);
  public static final SCMechanism X9_42_DH_PARAMETER_GEN     = new SCMechanism(PKCS11Constants.CKM_X9_42_DH_PARAMETER_GEN);


  public static final SCMechanism DES_ECB_ENCRYPT_DATA       = new SCMechanism(PKCS11Constants.CKM_DES_ECB_ENCRYPT_DATA);
  public static final SCMechanism DES_CBC_ENCRYPT_DATA       = new SCMechanism(PKCS11Constants.CKM_DES_CBC_ENCRYPT_DATA);
  public static final SCMechanism DES3_ECB_ENCRYPT_DATA      = new SCMechanism(PKCS11Constants.CKM_DES3_ECB_ENCRYPT_DATA);
  public static final SCMechanism DES3_CBC_ENCRYPT_DATA      = new SCMechanism(PKCS11Constants.CKM_DES3_CBC_ENCRYPT_DATA);
  public static final SCMechanism AES_ECB_ENCRYPT_DATA       = new SCMechanism(PKCS11Constants.CKM_AES_ECB_ENCRYPT_DATA);
  public static final SCMechanism AES_CBC_ENCRYPT_DATA       = new SCMechanism(PKCS11Constants.CKM_AES_CBC_ENCRYPT_DATA);

  
  public static final SCMechanism VENDOR_DEFINED             = new SCMechanism(PKCS11Constants.CKM_VENDOR_DEFINED);

  /**
   * The code of the machanism as defined in PKCS11Constants (or pkcs11t.h
   * likewise).
   */
  protected long pkcs11MechanismCode_;

  /**
   * The parameters of the mechanism. Not all mechanisms use these parameters.
   */
  //protected Parameters parameters_;

  /**
   * Constructor taking just the mechansim code as defined in PKCS11Constants.
   *
   * @param pkcs11MechanismCode The mechanism code.
   */
  public SCMechanism(long pkcs11MechanismCode) {
    pkcs11MechanismCode_ = pkcs11MechanismCode;
  }


  /**
   * Makes a clone of this object.
   *
   * @return A shallow clone of this object.
   */
  public Object clone() {
    SCMechanism clone = null;

    try {
      clone = (SCMechanism) super.clone();
    } catch (CloneNotSupportedException ex) {
      // this must not happen according to Java specifications
    }

    return clone ;
  }

  /**
   * Override equals to check for the equality of mechanism code and parameter.
   *
   * @param otherObject The other SCMechanism object.
   * @return True, if other is an instance of this class and
   *         pkcs11MechanismCode_ and parameter_ of both objects are equal.
   */
  public boolean equals(Object otherObject) {
    boolean euqal = false;

    if (otherObject instanceof SCMechanism) {
      SCMechanism other = (SCMechanism) otherObject;
      euqal = (this == other)
              || (this.pkcs11MechanismCode_ == other.pkcs11MechanismCode_);
                 //&& (((this.parameters_ == null) && other.parameters_ == null)
                 //    || ((this.parameters_ != null) && this.parameters_.equals(other.parameters_)));
    }

    return euqal ;
  }

  /**
   * Override hashCode to ensure that hashtable still works after overriding
   * equals.
   *
   * @return The hash code of this object. Taken from the mechanism code.
   */
  public int hashCode() {
    return (int) pkcs11MechanismCode_ ;
  }

  /**
   * This method checks, if this mechanism is a digest mechanism.
   * This is the information as provided by the table on page 229
   * of the PKCS#11 v2.11 standard.
   * If this method returns true, the mechanism can be used with the digest
   * functions.
   *
   * @return True, if this mechanism is a digest mechanism. False,
   *         otherwise.
   */
  public boolean isDigestMechanism() {
    return SCFunctions.isDigestMechanism(pkcs11MechanismCode_) ;
  }

  /**
   * This method checks, if this mechanism is a full
   * encrypt/decrypt mechanism; i.e. it supports the encryptUpdate()
   * and decryptUpdate() functions.
   * This is the information as provided by the table on page 229
   * of the PKCS#11 v2.11 standard.
   * If this method returns true, the mechanism can be used with the encrypt and
   * decrypt functions including encryptUpdate and decryptUpdate.
   *
   * @return True, if this mechanism is a full encrypt/decrypt
   *         mechanism. False, otherwise.
   */
  public boolean isFullEncryptDecryptMechanism() {
    return SCFunctions.isFullEncryptDecryptMechanism(pkcs11MechanismCode_) ;
  }

  /**
   * This method checks, if this mechanism is a full
   * sign/verify mechanism; i.e. it supports the signUpdate()
   * and verifyUpdate() functions.
   * This is the information as provided by the table on page 229
   * of the PKCS#11 v2.11 standard.
   * If this method returns true, the mechanism can be used with the sign and
   * verify functions including signUpdate and verifyUpdate.
   *
   * @return True, if th�s mechanism is a full sign/verify
   *         mechanism. False, otherwise.
   */
  public boolean isFullSignVerifyMechanism() {
    return SCFunctions.isFullSignVerifyMechanism(pkcs11MechanismCode_) ;
  }

  /**
   * This method checks, if this mechanism is a
   * key derivation mechanism.
   * This is the information as provided by the table on page 229
   * of the PKCS#11 v2.11 standard.
   * If this method returns true, the mechanism can be used with the deriveKey
   * function.
   *
   * @return True, if this mechanism is a key derivation mechanism.
   *         False, otherwise.
   */
  public boolean isKeyDerivationMechanism() {
    return SCFunctions.isKeyDerivationMechanism(pkcs11MechanismCode_) ;
  }

  /**
   * This method checks, if this mechanism is a key
   * generation mechanism for generating symmetric keys.
   * This is the information as provided by the table on page 229
   * of the PKCS#11 v2.11 standard.
   * If this method returns true, the mechanism can be used with the generateKey
   * function.
   *
   * @return True, if this mechanism is a key generation mechanism.
   *         False, otherwise.
   */
  public boolean isKeyGenerationMechanism() {
    return SCFunctions.isKeyGenerationMechanism(pkcs11MechanismCode_) ;
  }

  /**
   * This method checks, if this mechanism is a key-pair
   * generation mechanism for generating key-pairs.
   * This is the information as provided by the table on page 229
   * of the PKCS#11 v2.11 standard.
   * If this method returns true, the mechanism can be used with the generateKeyPair
   * function.
   *
   * @return True, if this mechanism is a key-pair generation mechanism.
   *         False, otherwise.
   */
  public boolean isKeyPairGenerationMechanism() {
    return SCFunctions.isKeyPairGenerationMechanism(pkcs11MechanismCode_) ;
  }

  /**
   * This method checks, if this mechanism is a sign/verify
   * mechanism with message recovery.
   * This is the information as provided by the table on page 229
   * of the PKCS#11 v2.11 standard.
   * If this method returns true, the mechanism can be used with the signRecover
   * and verifyRecover functions.
   *
   * @return True, if this mechanism is a sign/verify mechanism with
   *         message recovery. False, otherwise.
   */
  public boolean isSignVerifyRecoverMechanism() {
    return SCFunctions.isSignVerifyRecoverMechanism(pkcs11MechanismCode_) ;
  }

  /**
   * This method checks, if this mechanism is a
   * single-operation encrypt/decrypt mechanism; i.e. it does not support the
   * encryptUpdate() and decryptUpdate() functions.
   * This is the information as provided by the table on page 229
   * of the PKCS#11 v2.11 standard.
   * If this method returns true, the mechanism can be used with the encrypt and
   * decrypt functions excluding encryptUpdate and decryptUpdate.
   *
   * @return True, if this mechanism is a single-operation
   *         encrypt/decrypt mechanism. False, otherwise.
   */
  public boolean isSingleOperationEncryptDecryptMechanism() {
    return SCFunctions.isSingleOperationEncryptDecryptMechanism(pkcs11MechanismCode_) ;
  }

  /**
   * This method checks, if this mechanism is a
   * single-operation sign/verify mechanism; i.e. it does not support the
   * signUpdate() and encryptUpdate() functions.
   * This is the information as provided by the table on page 229
   * of the PKCS#11 v2.11 standard.
   * If this method returns true, the mechanism can be used with the sign and
   * verify functions excluding signUpdate and encryptUpdate.
   *
   * @return True, if this mechanism is a single-operation
   *         sign/verify mechanism. False, otherwise.
   */
  public boolean isSingleOperationSignVerifyMechanism() {
    return SCFunctions.isSingleOperationSignVerifyMechanism(pkcs11MechanismCode_) ;
  }

  /**
   * This method checks, if this mechanism is a
   * wrap/unwrap mechanism; i.e. it supports the wrapKey()
   * and unwrapKey() functions.
   * This is the information as provided by the table on page 229
   * of the PKCS#11 v2.11 standard.
   * If this method returns true, the mechanism can be used with the wrapKey
   * and unwrapKey functions.
   *
   * @return True, if this mechanism is a wrap/unwrap mechanism.
   *         False, otherwise.
   */
  public boolean isWrapUnwrapMechanism() {
    return SCFunctions.isWrapUnwrapMechanism(pkcs11MechanismCode_) ;
  }

  /**
   * Get the parameters object of this mechanism.
   *
   * @return The parameters of this mechansim. May be null.
   * @preconditions
   * @postconditions
   */
  /*
  public Parameters getParameters() {
    return parameters_ ;
  }    */

  /**
   * Set the parameters for this mechanism.
   *
   * @param parameters The mechanism parameters to set.
   * @preconditions
   * @postconditions
   */
  /*
  public void setParameters(Parameters parameters) {
    parameters_ = parameters;
  } */

  /**
   * Get the code of this mechanism as defined in PKCS11Constants (of pkcs11t.h
   * likewise).
   *
   * @return The code of this mechnism.
   */
  public long getMechanismCode() {
    return pkcs11MechanismCode_ ;
  }

  /**
   * Get the name of this mechanism.
   *
   * @return The name of this mechnism.
   */
  public String getName() {
    return SCFunctions.mechanismCodeToString(pkcs11MechanismCode_) ;
  }

  /**
   * Returns the string representation of this object.
   *
   * @return the string representation of this object
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer(128);

    buffer.append(SCConstants.INDENT);
    buffer.append("SCMechanism: ");
    buffer.append(SCFunctions.mechanismCodeToString(pkcs11MechanismCode_));
    buffer.append(SCConstants.NEWLINE);

      /*
    buffer.append(SCConstants.INDENT);
    buffer.append("Parameters: ");
    buffer.append(SCConstants.NEWLINE);
    buffer.append(parameters_);
      */
    // buffer.append(SCConstants.NEWLINE);

    return buffer.toString() ;
  }

}

