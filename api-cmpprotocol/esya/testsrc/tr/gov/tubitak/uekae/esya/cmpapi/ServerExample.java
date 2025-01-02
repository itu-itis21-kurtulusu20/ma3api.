package tr.gov.tubitak.uekae.esya.cmpapi;

import org.apache.log4j.BasicConfigurator;
import org.apache.mina.common.IoSession;
import org.apache.mina.handler.StreamIoHandler;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIBody;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.asn.cmp.*;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMessages;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ConfigTypeEsya;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.PKIMessageType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ProtocolType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.content.CertificationBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.CmpTcpLayer;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.PKIMessagePacket;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.TCPExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.Utilcrmf;
import tr.gov.tubitak.uekae.esya.cmpapi.esya.MsgBuilder;

import java.io.*;
import java.net.InetSocketAddress;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 2, 2010
 * Time: 4:13:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerExample {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        try {
            SocketAcceptor socketAcceptor = new SocketAcceptor();
            socketAcceptor.bind(new InetSocketAddress("127.0.0.1", 6001), new StreamIoHandler() {
                @Override
                protected void processStreamIo(IoSession ioSession, final InputStream inputStream, final OutputStream outputStream) {
                    new Thread(new Runnable() {
                        public void run() {
                            serverIRExample(inputStream, outputStream);
                        }
                    }).start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void serverIRExample(InputStream inputStream, OutputStream outputStream) {
        try {
            ECertificate cACertificate = new ECertificate(new File("D:\\Projects\\ESYA_MA3API\\cmpapi\\headzeldalsm.cer"));
            ECertificate generatedCertificate = new ECertificate(new File("D:\\Projects\\ESYA_MA3API\\cmpapi\\yetkili_cer_sifre_kayitci2.cer"));
            EName cAName = cACertificate.getSubject();
            CmpTcpLayer cmpTcpLayer = new ServerCmpTcpLayer(new BufferedInputStream(inputStream), outputStream);
            PKIMessagePacket pkiRequest = cmpTcpLayer.readPKIMessagePacket();
            PKIMessageType messageType = PKIMessageType.getPKIMessageType(pkiRequest.getPkiMessage().getBody().getObject().getChoiceID());
            if (messageType != PKIMessageType.CR)
                throw new Exception("Desteklenmeyen Mesaj:" + messageType);
            //new style...
            MsgBuilder msgBuilder = new MsgBuilder(ProtocolType.CERTIFICATIONPROTOCOL, cAName,null,null);

            IProtectionTrustProvider<ConfigTypeEsya> protectionTrustProvider = new ConfigTypeEsyaIProtectionTrustProvider();

            CertificationBuilder bodyContent = msgBuilder.createCertificationBuilder();
            // read requesting PKIMessage
            { // check request



                msgBuilder.verifyHeader(pkiRequest.getPkiMessage().getHeader(), cAName);
                bodyContent.verifyCertReqMessage(pkiRequest.getPkiMessage().getBody().getObject());
                msgBuilder.verifyProtection(pkiRequest.getPkiMessage(), protectionTrustProvider);
            }

            PKIMessagePacket responsePacket;
            { // PREPARE HEADER FOR SEND-RESPONSE...

                String senderKID = "senderKID of SampeUsage";
                EPKIHeader responseHeader = msgBuilder.createPkiHeader(pkiRequest.getPkiMessage().getHeader());
                List<CertResponse> certResponses = new ArrayList<CertResponse>();
                CertReqMessages certReqMessages = (CertReqMessages) pkiRequest.getPkiMessage().getBody().getObject().getElement();
                for (CertReqMsg certReqMsg : certReqMessages.elements) {
                    CertOrEncCert certOrEncCert = new CertOrEncCert();
                    CMPCertificate cmpCertificate = new CMPCertificate();
                    cmpCertificate.set_x509v3PKCert(generatedCertificate.getObject());
                    Pair<CipherAlg,AlgorithmParams> pair = CipherAlg.fromAlgorithmIdentifier(generatedCertificate.getSubjectPublicKeyInfo().getAlgorithm());
                    BufferedCipher decryptor = Crypto.getDecryptor(pair.getObject1());
                    decryptor.init(generatedCertificate.getSubjectPublicKeyInfo().getSubjectPublicKey(),pair.getObject2());

                    certOrEncCert.set_encryptedCert(
                            Utilcrmf.encryptedValueOlustur(generatedCertificate.getEncoded(),
                                    decryptor,
                                    generatedCertificate.getSubjectPublicKeyInfo().getAlgorithm()).getObject());
//                    certOrEncCert.set_certificate(cmpCertificate);
                    CertResponse certResponse = new CertResponse(
                            certReqMsg.certReq.certReqId,
                            new PKIStatusInfo(PKIStatus.accepted),
                            new CertifiedKeyPair(certOrEncCert),
                            null);

                    certResponses.add(certResponse);
                }

                PKIBody responseBody = bodyContent.createCertResBody(certResponses);
                EPKIMessage responseMessage = msgBuilder.createPKIMessage(responseHeader, new EPKIBody(responseBody), protectionTrustProvider);
                responsePacket = msgBuilder.createCmpPacket(responseMessage);
                cmpTcpLayer.sendMessage(responsePacket);
            }

            PKIMessagePacket confirmPacket;
            { // READ CONFIRMATION RESPONSE
                confirmPacket = cmpTcpLayer.readPKIMessagePacket();
                EPKIMessage confMessage = confirmPacket.getPkiMessage();
                msgBuilder.verifyResponse(responsePacket.getPkiMessage(), confMessage, protectionTrustProvider);
                bodyContent.verifyCertConfBody(responsePacket.getPkiMessage().getBody().getObject(), confMessage.getBody().getObject());
            }

            // todo devaaaaaam.....
            { //
                EPKIHeader pkiConfHeader = msgBuilder.createPkiHeader(confirmPacket.getPkiMessage().getHeader());
                PKIMessagePacket pkiConfMessage = msgBuilder.createPkiConfMessage(pkiConfHeader, protectionTrustProvider);
                cmpTcpLayer.sendMessage(pkiConfMessage);

            }
            cmpTcpLayer.waitFinish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ServerCmpTcpLayer extends CmpTcpLayer {
        public ServerCmpTcpLayer(BufferedInputStream bis, OutputStream os) {
            super("", -1);
            this.bis = bis;
            this.os = os;
            this.defaultFixedErrorMessage = fixedServerErrorMessage;
            this.generalErrorType = TCPExceptionInfo.GeneralServerError;
        }
    }

    private static class ConfigTypeEsyaIProtectionTrustProvider implements IProtectionTrustProvider<ConfigTypeEsya> {
        public List<IProtectionController> getAcceptedProtectionContollers() {

            ArrayList<IProtectionController> protectionControllers = new ArrayList<IProtectionController>();
            protectionControllers.add(new ProtectionControllerWithHMac(new PBMParameterFinder("RF123456")));
            protectionControllers.add(new ProtectionControllerWithSign(new TrustedCertificateFinder()));
            return protectionControllers;
        }

        public IProtectionGenerator getProtectionGenerator() {
            return new ProtectionGeneratorWithSign(new BaseSigner() {
                public byte[] sign(byte[] aData) throws ESYAException {
                    try {
                        SmartCard sc = new SmartCard(CardType.UTIMACO);
                        long sessionID = sc.openSession(9L);
                        sc.login(sessionID, "123456");
                        try {

                            return SmartOp.sign(sc, sessionID, 9L, "HEADZELDAL_ESYA_SIGN", aData,
                                    getSignatureAlgorithm().getName());
                        } finally {
                            sc.logout(sessionID);
                            sc.closeSession(sessionID);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new byte[20];
                }

                public String getSignatureAlgorithmStr() {
                    return SignatureAlg.RSA_SHA1.getName();
                }

                public AlgorithmParameterSpec getAlgorithmParameterSpec() {
                    return null;
                }

                public SignatureAlg getSignatureAlgorithm() {
                    return SignatureAlg.RSA_SHA1;
                }
            });
        }
    }

    /**
     * Created by IntelliJ IDEA.
     * User: zeldal.ozdemir
     * Date: Dec 3, 2010
     * Time: 10:53:43 AM
     * To change this template use File | Settings | File Templates.
     */
    public static class TrustedCertificateFinder implements ITrustedCertificateFinder {
        private ECertificate trustedCertificate;

        public List<ECertificate> findTrustedCertificates(EPKIMessage incomingPkiMessage) {
            ArrayList<ECertificate> list = new ArrayList<ECertificate>();
            ECertificate cACertificate = null;
            try {
                cACertificate = new ECertificate(new File("D:\\Projects\\ESYA_MA3API\\cmpapi\\kayitcizeldal.cer"));
            } catch (Exception e) {
                throw new RuntimeException("Error while getting Ca Certificate:" + e.getMessage(), e);
            }
            list.add(cACertificate);
            return list;
        }

        public TrustedCertificateFinder() {
        }

        public void setProtectionCertificate(ECertificate trustedCertificate) {
            this.trustedCertificate = trustedCertificate;
        }
    }
}
