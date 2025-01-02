package tr.gov.tubitak.uekae.esya.cmpapi.base.common;


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
    INITIALIZATIONPROTOCOL("INITIALIZATION",
            PKIMessageType.IR,
            PKIMessageType.IP,
            PKIMessageType.CERTCONF,
            PKIMessageType.PKICONF),

    CERTIFICATIONPROTOCOL("CERTIFICATION",
            PKIMessageType.CR,
            PKIMessageType.CP,
            PKIMessageType.CERTCONF,
            PKIMessageType.PKICONF),

    KEYUPDATEPROTOCOL("KEYUPDATE",
            PKIMessageType.KUR,
            PKIMessageType.KUP,
            PKIMessageType.CERTCONF,
            PKIMessageType.PKICONF),

    PKCS10PROTOCOL("PKCS10",
            PKIMessageType.P10CR,
            PKIMessageType.IP,
            null,
            null),

    KEYRECOVERYPROTOCOL("KEYRECOVERY",
            PKIMessageType.KRR,
            PKIMessageType.KRP,
            null,
            null),

    REVOCATIONPROTOCOL("REVOCATION",
            PKIMessageType.RR,
            PKIMessageType.RP,
            null,
            null),
    CVCPROTOCOL("CVC",
            PKIMessageType.CVCREQ,
            PKIMessageType.IP,
            PKIMessageType.CERTCONF,
            PKIMessageType.PKICONF),
    ;
    //
    private String protocolName;
    private PKIMessageType.IRequestType reqType;
    private PKIMessageType.IResponseType resType;
    private PKIMessageType.IRequestType confType;
    private PKIMessageType.IResponseType pkiConfType;
    // code name mentioned in RFC4210
    // asn choice ID of protocol type. see PKIBody

    ProtocolType(String protocolName, PKIMessageType.IRequestType reqType, PKIMessageType.IResponseType resType, PKIMessageType.IRequestType confType, PKIMessageType.IResponseType pkiConfType) {
        this.protocolName = protocolName;
        this.reqType = reqType;
        this.resType = resType;
        this.confType = confType;
        this.pkiConfType = pkiConfType;
    }

    /**
     * returns protocol type with corresponding choiceID. see PKIBody
     *
     * @param choiceID
     * @return
     */
    public static ProtocolType getProtocolTypeWithChoiceID(int choiceID) {
        for (ProtocolType protocolType : ProtocolType.values()) {
            if (protocolType.getReqType().getChoice() == choiceID)
                return protocolType;
        }
        return null;
    }


    public String getProtocolName() {
        return protocolName;
    }

    public PKIMessageType.IRequestType getReqType() {
        return reqType;
    }

    public PKIMessageType.IResponseType getResType() {
        return resType;
    }

    public PKIMessageType.IRequestType getConfType() {
        return confType;
    }

    public PKIMessageType.IResponseType getPkiConfType() {
        return pkiConfType;
    }


}
