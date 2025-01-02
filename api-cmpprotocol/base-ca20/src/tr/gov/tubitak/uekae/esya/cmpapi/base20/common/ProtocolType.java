package tr.gov.tubitak.uekae.esya.cmpapi.base20.common;


import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Dec 7, 2010 - 9:55:24 AM <p>
 * <b>Description</b>: <br>
 *  <pre>
 * ProtocolType Defines which message types must be handled for easch protocol type according to rfc 4210
 * Herein Initialization request...
 *
 * Step  End Entity                       PKI
 * --------------------------------------------------------------------
 * 1   Format ir
 * 2                    -&gt; ir      -&gt;
 * 3                                    Handle ir, Create Certs
 * 5                    &lt;- ip      &lt;-
 * 6   Process ip
 * 21  Format certConf
 * 22                   -&gt; certConf -&gt;
 * 23                                   Handle certConf
 * 24                                   Format ack
 * 25                   &lt;- pkiConf   &lt;-
 * 36 End

 * </pre>
 */

public enum ProtocolType {
    INITIALIZATIONPROTOCOL("INITIALIZATION", PKIMessageType.IR),

    CERTIFICATIONPROTOCOL("CERTIFICATION",PKIMessageType.CR),

    KEYRECOVERYPROTOCOL("KEYRECOVERY", PKIMessageType.KRR),

    PKCS10PROTOCOL("PKCS10", PKIMessageType.P10CR),

    KEYUPDATEPROTOCOL("KEYUPDATE",PKIMessageType.KUR),

    REVOCATIONPROTOCOL("REVOCATION", PKIMessageType.RR),

    CVCPROTOCOL("CVC", PKIMessageType.CVCREQ),

    GENERALMESSAGEPROTOCOL("GENERALMESSAGEPROTOCOL", PKIMessageType.GENM);

    //
    private String protocolName;
    private PKIMessageType.IRequestType requestType;
    // code name mentioned in RFC4210
    // asn choice ID of protocol type. see PKIBody

    ProtocolType(String protocolName, PKIMessageType.IRequestType requestType) {
        this.protocolName = protocolName;
        this.requestType = requestType;
    }


    public ProtocolState createProtocolState(){
        switch (this){
            case INITIALIZATIONPROTOCOL:
                return createFourWay(PKIMessageType.IR,PKIMessageType.IP);
            case CERTIFICATIONPROTOCOL:
                return createFourWay(PKIMessageType.CR,PKIMessageType.CP);
            case KEYUPDATEPROTOCOL:
                return createFourWay(PKIMessageType.KUR,PKIMessageType.KUP);
            case CVCPROTOCOL:
                return createFourWay(PKIMessageType.CVCREQ,PKIMessageType.IP);
            case PKCS10PROTOCOL:
                return createTwoWay(PKIMessageType.P10CR, PKIMessageType.IP);
            case KEYRECOVERYPROTOCOL:
                return createTwoWay(PKIMessageType.KRR, PKIMessageType.KRP);
            case REVOCATIONPROTOCOL:
                return createTwoWay(PKIMessageType.RR, PKIMessageType.RP);
            case GENERALMESSAGEPROTOCOL:
                return createTwoWay(PKIMessageType.GENM, PKIMessageType.GENP);
            default:
                throw new ESYARuntimeException("Unknown Protocol Type:"+this);

        }
    }

    private ProtocolState createFourWay(PKIMessageType.CertReqType reqType, PKIMessageType.CertResType resType) {
        ProtocolState.Item head = new ProtocolState.Item (null);
        ProtocolState.Item tail = new ProtocolState.Item (null);
        ProtocolState.Item req = new ProtocolState.Item (reqType);
        ProtocolState.Item res = new ProtocolState.Item (resType);
        ProtocolState.Item pollReq = new ProtocolState.Item (PKIMessageType.POLLREQ);
        ProtocolState.Item pollRep = new ProtocolState.Item (PKIMessageType.POLLREP);
        ProtocolState.Item certConf = new ProtocolState.Item (PKIMessageType.CERTCONF);
        ProtocolState.Item pkiConf = new ProtocolState.Item (PKIMessageType.PKICONF);

        head.setNext(req);
        req.setNext(res);
        res.setNext(pollReq, certConf);
        pollReq.setNext(pollRep,res);
        pollRep.setNext(pollReq);
        certConf.setNext(pkiConf);
        pkiConf.setNext(pollReq,tail);
        return new ProtocolState(head, true);
    }

    private ProtocolState createTwoWay(PKIMessageType.IRequestType reqType, PKIMessageType.IResponseType resType) {
        ProtocolState.Item head = new ProtocolState.Item (null);
        ProtocolState.Item tail = new ProtocolState.Item (null);
        ProtocolState.Item req = new ProtocolState.Item (reqType);
        ProtocolState.Item res = new ProtocolState.Item (resType);

        head.setNext(req);
        req.setNext(res);
        res.setNext(tail);
        return new ProtocolState(head, false);
    }


    public String getProtocolName() {
        return protocolName;
    }


    public PKIMessageType.IRequestType getReqType() {
        return requestType;
    }

    public static ProtocolType getProtocolTypeForRequest(PKIMessageType.IRequestType requestType){
        for (ProtocolType protocolType : ProtocolType.values()) {
            if(protocolType.getReqType() == requestType)
                return protocolType;
        }
        throw new ESYARuntimeException("Unepxtected Request Type:"+requestType);
    }
}
