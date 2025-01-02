package tr.gov.tubitak.uekae.esya.cmpapi.base.content;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIBody;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAServiceConfigType;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.asn.cmp.ErrorMsgContent;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CmpConfigType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IConfigType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.IProtectionTrustProvider;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.PKIMessagePacket;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 8, 2010 - 9:10:21 AM <p>
 * <b>Description</b>: <br>
 *     Template Interface for PKI Message Builder implementations.
 */
public interface IMsgBuilder<T extends IConfigType> {

    EPKIHeader createPkiHeader(EPKIHeader previousHeader);

    CertificationBuilder createCertificationBuilder() throws CMPProtocolException;

    PKIMessagePacket createPkiConfMessage(EPKIHeader pkiHeader, IProtectionTrustProvider trustProvider);

    EPKIMessage createPKIMessage(EPKIHeader pkiHeader, EPKIBody pkiBody, IProtectionTrustProvider trustProvider);

    void verifyProtection(EPKIMessage message, IProtectionTrustProvider trustProvider) throws CMPProtocolException;

    void addProtection(EPKIMessage message, IProtectionTrustProvider trustProvider);

    void verifyHeader(EPKIHeader header) throws CMPProtocolException;

    void verifyHeader(EPKIHeader header, EName recipient) throws CMPProtocolException;

    void verifyHeader(EPKIHeader previousHeader, EPKIHeader incomingHeader) throws CMPProtocolException;

    void verifyResponse(EPKIMessage previousMessage, EPKIMessage incomingMessage, IProtectionTrustProvider trustProvider) throws CMPProtocolException;

    PKIMessagePacket createCmpPacket(EPKIMessage pkiRequest);

    PKIMessagePacket createCmpPacket(EPKIHeader pkiHeader, EPKIBody pkiBody, IProtectionTrustProvider trustProvider);

    void verifyPkiConfBody(EPKIBody pkiBody) throws CMPProtocolException;

    PKIMessagePacket createErrorMessage(EPKIHeader pkiHeader, CMPProtocolException e, IProtectionTrustProvider trustProvider);

    EPKIHeader createPkiHeader();

    PKCS10Builder createPKCS10Builder() throws CMPProtocolException;

    boolean isErrorMsg(EPKIBody body);

    void reportErrorMessage(ErrorMsgContent errorMsgContent);

    KeyRecoveryBuilder createKeyRecoveryBuilder();

    RevocationBuilder createRecovationBuilder();

    void setServiceConfigType(CmpConfigType cmpConfigType);
}
