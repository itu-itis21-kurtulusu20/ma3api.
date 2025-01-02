

#ifndef __ECMSUTIL__
#define __ECMSUTIL__


//#include "ECMSException.h"
#include "SignedData.h"
//#include "StreamedSignedData.h"
//#include "EnvelopedData.h"
//#include "StreamEnvelopedData.h"
//#include "SignParam.h"
#include "ImzaDogrulamaAlgoritmasi.h"
//#include "AlgDetay.h"
//#include "pkcs5v2.h"
//#include "asnaes.h"

using namespace esya;


#define PBE_DEFAULT_KEY_DRV_ALG AlgorithmIdentifier((ASN1TObjId)PKCS5_id_PBKDF2)
#define PBE_DEFAULT_KEY_ENC_ALG AlgorithmIdentifier((ASN1TObjId)AES_id_aes128_CBC)

#define PBE_SALT_LENGTH		10
#define PBE_ITERATION_COUNT 2000

/**
* \ingroup EsyaCMS
* 
* CMS fonksiyonlar�n�n d��ar�ya sunuldu�u api s�n�f�
* 
*/
class Q_DECL_EXPORT ECMSUtil
{
	ECMSUtil(void);
	
//	static int _signBES(const QByteArray & iContent,const SignParam & iSP  , QByteArray & oSignedData);
//	static int _signBES(const QByteArray & iContent,  const ECertificate &iSignerCert,const AnahtarBilgisi& iAB, QByt¡eArray & oSignedData,bool iAyrikImza ,const QList< QPair<ASN1TObjId,QByteArray> > & iSignedAttributes );
//	static int _signBES(const QByteArray & iContent,  const ECertificate &iSignerCert,const AnahtarBilgisi& iAB, QByteArray & oSignedData,bool iAyrikImza = false);
//        static int _signFileBES(const QString & iContentFile,  const ECertificate &iSignerCert, const AnahtarBilgisi& iAB,const QString & oSignedDataFile , bool iAyrikImza);
//	static int _signFileBES(const QString & iContentFile, const SignParam & iSP,  const QString & oSignedDataFile  );

public:

	/********************************************************************/
	/*																	*/
	/*								SIGN								*/
	/*																	*/		
	/********************************************************************/
//	static int signBES(const QByteArray & iContent,const SignParam & iSP  , QByteArray & oSignedData );
//	static int signBES(const QByteArray & iData,  const ECertificate & iCert,const AnahtarBilgisi& iAB , QByteArray & oSignedData,bool iAyrikImza ,const QList< QPair<ASN1TObjId,QByteArray> > & iSignedAttributes );
//	static int signBES(const QByteArray & iData,  const ECertificate & iCert,const AnahtarBilgisi& iAB , QByteArray & oSignedData,bool iAyrikImza = false);
//    static int signFileBES(const QString & iDataFile,  const ECertificate & iCert, const AnahtarBilgisi &iAB ,const QString & oSignedDataFile, bool iAyrikImza);
//	static int signFileBES(const QString & iDataFile,  const SignParam & iSignParam ,const QString & oSignedDataFile);

//	static int constructSSD(QIODevice * iPDStream, QIODevice * iSDStream, const ECertificate & iCert, const AnahtarBilgisi & iAB, bool iAyrikImza);

//	static int seriImzaciEkle(const QString &  iPDFile, const QString& iSDFile, const ECertificate & iCert, const AnahtarBilgisi & iAB,const AlgorithmIdentifier & iDigestAlg);
//	static int seriImzaciEkle(QByteArray& ioSignedData, const ECertificate & iCert, const AnahtarBilgisi & iAB,const AlgorithmIdentifier & iDigestAlg);

//	static bool verifySignedData(const QByteArray & iSignedContent, const QList<ECertificate>& iCertificates = QList<ECertificate>());
//	static bool verifySignedFile(const QString & iSignedFile, const QList<ECertificate>& iCertificates );

	static void verifyBES(const SignedData & iSD , const QList<ECertificate>& iCertificates , const QByteArray& iPlainData,ImzaDogrulamaSonucu & oSonuc );
//	static void verifyBESData(const QByteArray & iSignedContent, const QList<ECertificate>& iCertificates , ImzaDogrulamaSonucu & oSonuc,const QByteArray& iPlainData= QByteArray());
//	static void verifyBESFile(const QString & iSignedFile, const QList<ECertificate>& iCertificates , ImzaDogrulamaSonucu & oSonuc , QIODevice* oPDStream = NULL );
//	static void verifyBESFile(const QString & iSignedFile, const QList<ECertificate>& iCertificates , ImzaDogrulamaSonucu & oSonuc , const QString& iPDFileName );



	/********************************************************************/
	/*																	*/
	/*								ENCRYPT								*/
	/*																	*/		
	/********************************************************************/

//	static QByteArray	decryptEnvelopedData(	const QByteArray& iEnvelopedData,
//												const ECertificate &iCert,
//												const OzelAnahtarBilgisi & iAB );

//	static QByteArray	decryptEnvelopedData(	const QByteArray& iEnvelopedData,
//												const QString &iParola );


//	static int			decryptEnvelopedFile(	const QString& iEnvDataFileName,
//												const QString& iPlainDataFileName,
//												const ECertificate& iCert,
//												const OzelAnahtarBilgisi& iAB );


//	static int			decryptEnvelopedFile(	const QString& iEnvDataFileName,
//												const QString& iPlainDataFileName,
//												const QString& iParola );


//	static QByteArray groupEnvelopeData(	const QByteArray			&iPlainData,
//											const QList<ECertificate>	&iCertList,
//											const AlgorithmIdentifier	&iContEncAlg );

//	static QByteArray envelopeData(	const QByteArray			&iPlainData,
//									const QList<ECertificate>	&iCertList,
//									const AlgorithmIdentifier	&iContEncAlg );


//	static QByteArray envelopeData(	const QByteArray	&iPlainData,
//									const QString		&iParola,
//									const AlgorithmIdentifier & iContEncAlg ,
//									const AlgorithmIdentifier & iKeyEncAlg = PBE_DEFAULT_KEY_ENC_ALG,
//									const AlgorithmIdentifier & iKeyDrvAlg = PBE_DEFAULT_KEY_DRV_ALG );


//	static EnvelopedData * createGroupEnvelopedData(	const QByteArray& iPlainData,
//														const QList<ECertificate>& iCertList ,
//														const AlgorithmIdentifier& iContEncAlg );

//	static EnvelopedData * createEnvelopedData(	const QByteArray& iPlainData,
//												const QList<ECertificate> & iCertList ,
//												const AlgorithmIdentifier & iContEncAlg );

//	static EnvelopedData * createEnvelopedData(	const QByteArray& iPlainData,
//												const ECertificate & iCert,
//												const AlgorithmIdentifier & iContEncAlg );


//	static EnvelopedData *	createEnvelopedData(	const QByteArray& iPlainData,
//													const QString& iParola ,
//													const AlgorithmIdentifier& iContEncAlg,
//													const AlgorithmIdentifier& iKeyEncAlg = PBE_DEFAULT_KEY_ENC_ALG,
//													const AlgorithmIdentifier& iKeyDrvAlg = PBE_DEFAULT_KEY_DRV_ALG );


//	//static StreamEnvelopedData * createEnvelopedFile(	const QString & iEnvDataFilePath );

//	static StreamEnvelopedData * createEnvelopedFile(	const ECertificate & iCert,
//														const AlgorithmIdentifier& iContEncAlg 	);

//	static StreamEnvelopedData * createEnvelopedFile(	const QList<ECertificate> & iCertList,
//														const AlgorithmIdentifier& iContEncAlg 	);


//	static StreamEnvelopedData * createEnvelopedFile(	const QString & iParola,
//														const AlgorithmIdentifier& iContEncAlg ,
//														const AlgorithmIdentifier& iKeyEncAlg = PBE_DEFAULT_KEY_ENC_ALG,
//														const AlgorithmIdentifier& iKeyDrvAlg = PBE_DEFAULT_KEY_DRV_ALG );

//	static StreamEnvelopedData * createGroupEnvelopedFile(	const QList<ECertificate>& iCertList,
//													const AlgorithmIdentifier& iContEncAlg  );


//	static QList<RecipientInfo> getEnvelopedFileRecipients(	const QString& iEnvDataFileName );
//	static int					getEnvelopedFileRecipientIndex(	const QString& iEnvDataFileName ,const ECertificate& iCert);

//	static AlgorithmIdentifier getContentEncryptionAlgorithm( const QString& iEnvDataFileName );


public:
~ECMSUtil(void);
};


#endif

