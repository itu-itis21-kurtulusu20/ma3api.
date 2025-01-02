using System;
using System.Collections.Generic;
using System.Reflection;
using iaik.pkcs.pkcs11.wrapper;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.smartcard.bundle;
using tr.gov.tubitak.uekae.esya.api.smartcard.util;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11
{
    public class P11SmartCard : IBaseSmartCard
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
	
	    protected SmartCard sc;
	    protected long slot;
	    protected long session;
        /**
 * Create smart card with a card type
 * @param aCardType
 * @throws PKCS11Exception
 * @throws IOException
 */
        public P11SmartCard(CardType aCardType) 
	    {
		    sc = new SmartCard(aCardType);
	    }
        /**
         * openSession opens a session between the application and the token present in the given slot.
         * 
         * @param aSlotID slot id of the token
         * @throws SmartCardException
         */
        public void openSession(long aSlotID)
        {
            try
            {
                slot = aSlotID;
                session = sc.openSession(aSlotID);
            }
            catch (PKCS11Exception e)
            {
                throw new SmartCardException(e.Message, e);
            }
        }
        /**
         * getSignatureCertificates returns signing certificates.
         * @return list of signing certificates. If no signing certificate is found,returns empty list.
         * @throws SmartCardException
         */
        public List<byte[]> getSignatureCertificates()
        {
            try
            {
                return sc.getSignatureCertificates(session);
            }
            catch (PKCS11Exception e)
            {
                throw new SmartCardException(e.Message, e);
            }
        }
        /**
         * getEncryptionCertificates returns encryption certificates.
         * @return list of encryption certificates. If no encryption certificate is found,returns empty list.
         * @throws SmartCardException
         */
        public List<byte[]> getEncryptionCertificates()
        {
            try
            {                
                return sc.getEncryptionCertificates(session);
            }
            catch (PKCS11Exception e)
            {
                throw new SmartCardException(e.Message, e);
            }
        }
        /**
         *  logs user to the token
         * @param aCardPIN  pin of the token
         * @throws SmartCardException
         * @throws LoginException
         */
        public void login(string aCardPIN)
        {
            try
            {
                sc.login(session, aCardPIN);
            }
            catch (PKCS11Exception e)
            {
                                            
                if (e.ErrorCode == PKCS11Constants_Fields.CKR_PIN_INCORRECT)
                {
                    try
                    {
                        CK_TOKEN_INFO tokenInfo = sc.getTokenInfo(slot);

                        if ((tokenInfo.flags & PKCS11Constants_Fields.CKF_USER_PIN_FINAL_TRY) == PKCS11Constants_Fields.CKF_USER_PIN_FINAL_TRY)
                            throw new LoginException(SmartCardI18n.getMsg(SmartCardI18n.INCORRECT_PIN_FINAL_TRY), e, true, false);
                    }
                    catch (PKCS11Exception e1)
                    {
                        logger.Error("TokenInfo alınamadı", e1);
                    }

                    throw new LoginException(SmartCardI18n.getMsg(SmartCardI18n.INCORRECT_PIN), e, false, false);
                }
                else if (e.ErrorCode == PKCS11Constants_Fields.CKR_PIN_LOCKED)
                    throw new LoginException(SmartCardI18n.getMsg(SmartCardI18n.PIN_LOCKED), e, false, true);

                if (e.ErrorCode != PKCS11Constants_Fields.CKR_USER_ALREADY_LOGGED_IN)
                    throw new SmartCardException(e.Message, e);
            }
        }
        /**
         * logs a user out from a token.
         * @throws SmartCardException
         */
        public void logout()
        {
            try
            {
                sc.logout(session);
            }
            catch (PKCS11Exception e)
            {
                throw new SmartCardException(e.Message, e);
            }
        }
        /**
         * closeSession closes the session between the application and the token
         * @throws SmartCardException
         */
        public void closeSession()
        {
            try
            {
                sc.closeSession(session);
            }
            catch (PKCS11Exception e)
            {
                throw new SmartCardException(e.Message);
            }
        }
        /**
         * return serial number of token
         * @return 
         * @throws SmartCardException
         */
        public byte[] getSerial()
        {
            try
            {
                String serial = new String(sc.getTokenInfo(slot).serialNumber).Trim();
                return StringUtil.ToByteArray(serial);
            }
            catch (PKCS11Exception e)
            {
                throw new SmartCardException(e.Message, e);
            }
        }
        /**
         * return serial number of token
         * @param aSlotID
         * @return 
         * @throws SmartCardException
         */
        public byte[] getSerial(long aSlotID)
        {
            try
            {
                String serial = new String(sc.getTokenInfo(aSlotID).serialNumber);
                return StringUtil.ToByteArray(serial);
            }
            catch (PKCS11Exception e)
            {
                throw new SmartCardException(e.Message, e);
            }
        }
        /**
         * prepare and return signer from X509Certificate and signing algorithm
         * @return 
         * @throws SmartCardException
         */
        public BaseSigner getSigner(ECertificate aCert, string aSigningAlg)
        {
            return new SCSignerWithCertSerialNo(sc, session, slot, aCert.getSerialNumber().GetData(), aSigningAlg);
        }

         /**
	     * prepare and return signer from X509Certificate, signing algorithm and AlgorithmParameterSpec
	     * @return 
	     * @throws SmartCardException
	     */
        public BaseSigner getSigner(ECertificate aCert, String aSigningAlg, IAlgorithmParameterSpec aParams) 
        {
		   return new SCSignerWithCertSerialNo(sc, session, slot, aCert.getSerialNumber().GetData(), aSigningAlg, aParams);
        }

        public long getSlotNo()
        {
            return slot;
        }

        public long getSessionNo()
        {
            return session;
        }

        public SmartCard getSmartCard()
        {
            return sc;
        }



    }
}
