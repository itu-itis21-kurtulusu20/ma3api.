
#ifndef __MYERRORS__
#define __MYERRORS__

#define FAILURE -1  // Generic Failure
#define SUCCESS  0	// Generic Success

#define ERROR_FILE_OPEN 1001




#define CMS_INTERNAL_ERROR				"Bir hata oluþtu."
#define CMS_FILE_OPEN_ERROR				"Dosya açýlamadý."
#define CMS_NOT_A_SIGNED_CONTENT		"Bu bir imzalý dosya deðil."
#define CMS_NOT_A_ENVELOPED_CONTENT		"Bu bir imzalý dosya deðil."
#define CMS_NOT_A_SIGNED_DATA			"ContentInfo'nun tipi signedData deðil"
#define CMS_NOT_A_DATA					"geçerli b,r Data tipi deðil"
#define CMS_VERIFICATION_FAILED			"Ýmzalý dosya imzasý doðrulanamadý!"
#define CMS_SIGNER_VERIFICATION_FAILED	"Ýmzacý doðrulanamadý!"
#define CMS_MESSAGEDIGEST_NOT_FOUND     "messageDigest özniteliði bulunamadý"
#define CMS_SIGNEDATTRIBUTES_NOT_FOUND  "signedAttributes bulunamadý"
#define CMS_MESSAGEDIGEST_NOT_SINGLE	"messageDigest özniteliðinin sayýsý 1 deðil"
#define CMS_CERT_SUBJECT_NOT_FOUND	    "sertifikada  subject alaný bulunamadý"
#define CMS_SIGNINGTIME_NOT_FOUND	    "signingTime özniteliði bulunamadý"
#define CMS_WRONG_SID_TYPE				"SID tipi yanlýþ atanmýþ"
#define CMS_ENCRYPTEDCONTENT_NOT_FOUND	"encryptedContent bulunamadý"
#define CMS_STREAM_DATA					"Bu bir streamed veri"
#define CMS_NON_STREAM_DATA				"Bu bir non streamed veri"
#define CMS_PROTOKOL_ALREADY_DEFINED	"Daha önce bir protokol belirlenmiþ"



#define CMS_NO_DIGEST_ALG_FOUND			"Hiç bir Digest Algoritmasý bulunamadý "
#define CMS_NO_ENCAPCONTENTINFO_FOUND	"Hiç bir EncapContentInfo bulunamadý "
#define CMS_NO_SIGNERINFO_ADDED			"Hiç signerInfo eklenmemiþ "
#define CMS_NO_CERTIFICATE_ADDED		"Hiç sertifika eklenmemiþ "
#define CMS_NO_EXTENSION_FOUND			"Hiç eklenti bulunamadý "
#define CMS_NO_PARALLEL_SIGN_FOUND		"Hiç paralel imza bulunamadý "
#define CMS_NO_ORIGINATOR_INFO_ADDED    "Hiç originator info bulunamadý."
#define CMS_NO_UNPROTECTEDATTRIBUTES_ADDED    "Hiç unprotectedAttribute bulunamadý."
#define CMS_NO_RECIPIENTINFO_ADDED		"Hiç recipientInfo bulunamadý."

#define CMS_SIGNERTREES_DIFFERENT			"BesSignerTree ile BasicSignerTree ayný deðil"
#define CMS_PREDECESSOR_NOT_FOUND			"Verilen imzacýnýn atasý bulunamadý"
#define CMS_SIGNATUREDIGESTS_DIFFERENT		"Ýmzanýn  özeti ile imza-özeti özniteliði uyuþmuyor"
#define CMS_SIGNER_CERTIFICATE_NOT_FOUND	"Ýmzacýnýn sertifikasý bulunamadý"


#define CMS_ASN_BUFFER_INITIALIZATION_ERROR "Buffer ilklendirme sýrasýnda hata oluþtu"
#define CMS_ASN_BUFFER_NOT_INITIALIZED		"Buffer doðru bir þekilde ilklendirilmemiþ"




#define CMS_ASND_CONTENTINFO_TAGLEN			"contentInfo Tag ve Len okunamadý"
#define CMS_ASND_SIGNEDDATA_OBJID			"signed_data object identifier okunamadý"
#define CMS_ASND_CONTENT_TAGLEN				"content Tag ve Len okunamadý"
#define CMS_ASND_SIGNEDDATA_TAGLEN			"signed_data Tag ve Len okunamadý"
#define CMS_ASND_SIGNEDDATA_VERSION			"signed_data->version okunamadý"
#define CMS_ASND_ENCAPCONTENTINFO_TAGLEN	"encapcontentinfo Tag ve Len okunamadý.."
#define CMS_ASND_ENCRYPTCONTENTINFO_TAGLEN	"enccryptedcontentinfo Tag ve Len okunamadý.."
#define CMS_ASND_IDDATA_OBJID				"id_data object identifier okunamadý"
#define CMS_ASND_DATA_PARSE_ERROR			"Veri parse edilemedi"
#define CMS_ASND_CERTIFICATESET				"certificateSet okunamadý"
#define	CMS_ASND_CERTIFICATESET_1			"certificateSet okunamadý. Hata : %1"
#define CMS_ASND_SIGNERINFO					"signerInfo okunamadý"
#define CMS_ASND_SIGNERINFOS				"signerInfos okunamadý"
#define CMS_ASND_DIGESTALGORITHMS			"DigestAlgorithms okunamadý"
#define CMS_ASND_DIGESTALGORITHMS_1			"DigestAlgorithms okunamadý. Hata : %1"
#define CMS_ASND_CERTIFICATES_TAGLEN		"certficates Tag ve Len okunamadý"
#define CMS_ASND_SIGNEDDATA_1				"Veri SignedData olarak okunamadý. Hata : %1"
#define CMS_ASND_CONTENTINFO_1				"contentInfo okunamadý. Hata : %1"
#define CMS_ASND_CERTIFICATE_1				"certificate okunamadý. Hata : %1"
#define CMS_ASND_SUBJECTKEYIDENTIFIER_1		"SubjectKeyIdentifier okunamadý. Hata : %1"
#define CMS_ASND_CONTENTTYPE				"contentType özniteliði okunamadý."
#define CMS_ASND_MESSAGEDIGEST_1			"messageDigest okunamadý. Hata : %1"
#define CMS_ASND_SIGNINGCERTIFICATE_1		"signingCertificate okunamadý. Hata : %1"
#define CMS_ASND_SIGNINGTIME_1				"time attribute okunamadý. Hata : %1"
#define CMS_ASND_COUNTERSIGNATURE_1			"counterSignature attribute okunamadý. Hata : %1"
// ENVELOPEDDATA
#define CMS_ASND_ENVELOPEDDATA_1				"envelopedData okunamadý. Hata : %1"
#define CMS_ASND_ORIGINATORINFO_1				"originatorInfo okunamadý. Hata : %1"
#define CMS_ASND_ORIGINATORINFO_TAGLEN			"originatorInfo Tag ve Len okunamadý. Hata : %1"
#define CMS_ASND_ENCRYPTEDCONTENTINFO_1			"encryptedContentInfo okunamadý. Hata : %1"
#define CMS_ASND_UNPROTECTEDATTRIBUTES_1		"unprotectedAttributes okunamadý. Hata : %1"
#define CMS_ASND_CONTENTENCRYPTIONALGORITHM_1	"contentEncryptionAlgorithm okunamadý. Hata : %1"
#define CMS_ASND_ENVELOPEDDATA_OBJID			"enveloped_data object identifier okunamadý"
#define CMS_ASND_ENVELOPEDDATA_TAGLEN			"enveloped_data Tag ve Len okunamadý"
#define CMS_ASND_ENVELOPEDDATA_VERSION			"enveloped_data->version okunamadý"
#define CMS_ASND_RECIPIENTINFO_1				"recipientInfo okunamadý. Hata : %1"
#define CMS_ASND_KEYENCRYPTIONALGORITHM_1		"keyEncryptionAlgorithm okunamadý. Hata : %1"





#define CMS_ASNE_DIGESTALGORITHMS			"digestAlgorithms encode edilemedi"
#define CMS_ASNE_CERTIFICATE				"certificate encode edilemedi"
#define CMS_ASNE_SIGNEDDATA					"signedData encode edilemedi"
#define CMS_ASNE_CONTENTINFO				"contentInfo encode edilemedi"
#define CMS_ASNE_SIGNEDDATA_OBJID			"signedData object identifier encode edilemedi"
#define CMS_ASNE_CERTIFICATESET				"certificateSet encode edilemedi"
#define CMS_ASNE_SIGNERINFO					"signerInfos encode edilemedi"
#define CMS_ASNE_SIGNERINFOS				"signerInfos encode edilemedi"
#define CMS_ASNE_SIGNEDDATA_VERSION			"signed_data->version encode edilemedi"
#define CMS_ASNE_SIGNEDATTRIBUTES			"signedAttributes encode edilemedi"
#define CMS_ASNE_ISSUERSID					"IssuerSID encode edilemedi"
#define CMS_ASNE_ISSUERCERT					"Issuer certificate encode edilemedi"
#define CMS_ASNE_SUBJECTKEYIDENTIFIER		"SubjectKeyIdentifier encode edilemedi."
#define CMS_ASNE_CONTENTTYPE				"contentType encode edilemedi."
#define CMS_ASNE_MESSAGEDIGEST				"messageDigest encode edilemedi."
#define CMS_ASNE_SIGNINGCERTIFICATE			"signingCertificate encode edilemedi."
#define CMS_ASNE_SIGNINGTIME				"time attribute encode edilemedi."
// ENVELOPEDDATA
#define CMS_ASNE_ORIGINATORINFO				"sriginatorInfo attribute encode edilemedi."
#define CMS_ASNE_ENCRYPTEDCONTENTINFO		"encryptedContentInfo encode edilemedi"
#define CMS_ASNE_UNPROTECTEDATTRIBUTES		"unprotectedAttributes encode edilemedi"
#define CMS_ASNE_CONTENTENCRYPTIONALGORITHM	"contentEncryptionAlgorithm encode edilemedi"
#define CMS_ASNE_ENVELOPEDDATA				"envelopedData encode edilemedi"
#define CMS_ASNE_ENVELOPEDDATA_OBJID		"envelopedData object identifier encode edilemedi"
#define CMS_ASNE_ENVELOPEDDATA_VERSION		"envelopedData version encode edilemedi"
#define CMS_ASNE_RECIPIENTINFO				"recipientInfo encode edilemedi"
#define CMS_ASNE_IDDATA_OBJID				"id_data object identifier encode edilemedi"
#define CMS_ASNE_KEYENCRYPTIONALGORITHM		"keyEncryptionAlgorithm encode edilemedi"


#endif

