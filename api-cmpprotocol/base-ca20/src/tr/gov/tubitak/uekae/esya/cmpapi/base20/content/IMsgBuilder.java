package tr.gov.tubitak.uekae.esya.cmpapi.base20.content;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIBody;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.ErrorMsgContent;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CmpConfigType;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.IConfigType;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 8, 2010 - 9:10:21 AM <p>
 * <b>Description</b>: <br>
 *     Template Interface for PKI Message Builder implementations.
 */
public interface IMsgBuilder<T extends IConfigType> {

    EPKIHeader createPkiHeader(EPKIHeader previousHeader) throws ESYAException;

    CertificationBuilder createCertificationBuilder() throws CMPProtocolException;

    EPKIMessage createPkiConfMessage(EPKIHeader pkiHeader);

    EPKIMessage createPKIMessage(EPKIHeader pkiHeader, EPKIBody pkiBody);

    void verifyProtection(EPKIMessage message) throws CMPProtocolException;

    void addProtection(EPKIMessage message);

    void verifyHeader(EPKIHeader header) throws CMPProtocolException;

    void verifyHeader(EPKIHeader header, EName recipient) throws CMPProtocolException;

    void verifyHeader(EPKIHeader previousHeader, EPKIHeader incomingHeader) throws CMPProtocolException;

    void verifyResponse(EPKIMessage previousMessage, EPKIMessage incomingMessage) throws CMPProtocolException;

    EPKIMessage createCmpMessage(EPKIHeader pkiHeader, EPKIBody pkiBody);

    void verifyPkiConfBody(EPKIBody pkiBody) throws CMPProtocolException;

    EPKIMessage createErrorMessage(EPKIHeader pkiHeader, CMPProtocolException e);

    EPKIHeader createPkiHeader() throws ESYAException;

    PKCS10Builder createPKCS10Builder() throws CMPProtocolException;

    boolean isErrorMsg(EPKIBody body);

    void reportErrorMessage(ErrorMsgContent errorMsgContent);

    KeyRecoveryBuilder createKeyRecoveryBuilder();

    RevocationBuilder createRecovationBuilder();

    void setCmpResponseTimeout(long cmpResponseTimeout);

    void setServiceConfigType(CmpConfigType cmpConfigType);
}
