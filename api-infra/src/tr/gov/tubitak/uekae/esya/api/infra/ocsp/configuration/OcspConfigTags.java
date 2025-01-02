package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/21/14
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
public enum OcspConfigTags {
	OCSPCONFIGROOT("ocsp-configuration"),
	OCSPCONFIG("configuration"),
	OCSPCONFIGNAME("config-name"),
	OCSPSIGNCERTS("ocsp-sign-certificates"),
	OCSPSIGNCERT("ocsp-sign-certificate"),
	OCSPENCRCERT("ocsp-enc-certificate"),
	PREFERREDSIGNALG("preferred-sign-alg"),
	SUPPORTEDSIGNALGS("supported-sign-algs"),
	SUPPORTEDSIGNALG("supported-sign-alg"),
	SIGNERS("signers"),
	RESPONSIBLECAS("responsible-cas"),

	DBPROVIDER("dbprovider"),
	USERNAME("username"),
	PASSWORD("password"),
	VIEWNAME("viewname"),
	OCSPSIGNCHECK("ocspsigncheck"),
	OCSPLOG("ocsplog"),
	HMAC("hmackey"),
	DBPROPERTIES("dbproperties"),
	ISESYA20("is-esya-20"),
	NONCECHECK("noncecheck"),
	NONCECACHETIME("noncecachetime"),
	NONCECONTROL("noncecontrol"),
	NEXTUPDATE("nextupdate"),
	NEXTUPDATEMINUTE("nextupdateminute"),
	SAVERESPONSE("saveresponse"),

	CRLPROVIDER("crlprovider"),
	CRLADDRESSPATH("crladdresspath"),
	PERIOD("period"),

	RESPONSIBLECA("responsible-ca"),
	NAME("name"),
	CACERTIFICATE("cacertificate"),
	STATUSPROVIDERS("status-providers"),

	SIGNER("signer"),
	DECRYPTOR("decryptor"),
	KEYLABEL("keylabel"),
	SLOTNO("slotno"),
	CARDTYPE("cardtype"),
	PIN ("pin"),
	SESSIONPOOL("session-pool"),


	URL("url");

	private String tagName;

	OcspConfigTags(String aTagName){
		this.tagName = aTagName;
	}

	public String getTagName(){
		return tagName;
	}

	public static OcspConfigTags fromValue(String tagName) {
		for (OcspConfigTags tags : OcspConfigTags.values()) {
			if (tags.getTagName().equals(tagName)) {
				return tags;
			}
		}
		return null;
	}

}
