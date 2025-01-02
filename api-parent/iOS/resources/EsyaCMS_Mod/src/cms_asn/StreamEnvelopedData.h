#ifndef __STREAMENVELOPEDDATA__
#define __STREAMENVELOPEDDATA__

#include "EASNStreamingUtils.h"
#include "cms.h"
#include "EnvelopedData.h"
#include "AnahtarBilgisi.h"

#define EMIT_PROGRESS emit tamamlandi(mTamamlandi);

#define BLOK_SIZE  262144//OCTETSTR_BLOCK_SIZE/8

namespace esya
{

	class KeyDecryptor;

	/**
	* \ingroup EsyaASN
	* 
	* EnvelopedData ASN1Wrapper sýnýfýna stream özelliði eklenmiþ alt sýnýf
	*
	* \author dindaro
	*
	*/
	class StreamEnvelopedData : public QObject,  public EnvelopedData
	{
		Q_OBJECT

	public:

		enum ED_Type { EDT_ASYMMETRIC , EDT_PASSWORDBASED };

	protected:


		EASNFileInputStream	*	pIN;
		OutputStream			mOUT;

		QIODevice* mPDStream;
		QIODevice* mEncDataStream;

		KeyDecryptor* mpKeyDecryptor;


		long mEncDataByteIndex; 
		QByteArray mEncryptionKey;

		int  mEocCount;
		ECertificate mCert;
		AnahtarBilgisi mAB;
		QString		 mParola;
		ED_Type		 mType;
		
		quint64 mTamamlandi;

		//////////////// STREAM DECODE FUNCTIONS ///////////////////
		int _initOuptutBuffer(QString aFileName);
		void _releaseOutputBuffer();

		void _readEnvelopedData(const QString & iEnvelopedData );
		
		void _readContentInfo();
		void _readContentType();
		void _readContent();
		void _readEnvelopedData();
		void _readVersion();
		void _readOriginatorInfo();
		void _readRecipientInfos();
		void _readEncryptedContent();
		void _readEncryptedContentInfo();
		void _readEncryptedContentInfoType();
		void _readContentEncryptionAlgorithm();
		void _readUnProtectedAttributes();


		void _readHeader();
		void _readContentHeader();
		void _readEnvelopedDataHeader();
		void _readEncryptedContentInfoHeader();

		//////////////// STREAM ENCODE FUNCTIONS ///////////////////

		void _writeFromEncDataStream();

		void _writeContentInfo();
		void _writeContentType();
		void _writeContent();
		void _writeEnvelopedData();
		void _writeVersion();		
		void _writeOriginatorInfo();
		void _writeRecipientInfos();
		void _writeUnprotectedAttributes();
		void _writeEncryptedContentInfo();
		void _writeEncryptedContentInfoType();
		void _writeContentEncryptionAlgorithm();
		void _writeEncryptedContent();
		void _encryptContent();


		int _decryptStreamContent(const QString &oDataFile);

	public:
		
		Q_DECL_EXPORT StreamEnvelopedData(void);
		Q_DECL_EXPORT StreamEnvelopedData(EASNFileInputStream* iEnvDataStream);
		Q_DECL_EXPORT StreamEnvelopedData(EASNFileInputStream* iEnvDataStream,QIODevice* oPDStream ,const ECertificate &iCert, const AnahtarBilgisi& iAB );
		Q_DECL_EXPORT StreamEnvelopedData(EASNFileInputStream* iEnvDataStream,QIODevice *oPlainDataStream,const QString iParola);
		Q_DECL_EXPORT StreamEnvelopedData(const QByteArray & iContEncKey, const AlgorithmIdentifier& iContEncAlg );
		Q_DECL_EXPORT StreamEnvelopedData(const QByteArray & iContEncKey, const AlgorithmIdentifier& iContEncAlg, const QString& iParola );

		Q_DECL_EXPORT void constructSED(QIODevice* iPDStream,QIODevice* iEncDataStream  , QIODevice*oEDStream);

		Q_DECL_EXPORT void decryptStream(	EASNFileInputStream* iEnvDataStream,
											QIODevice* oPDStream ,
											const ECertificate &iCert, 
											const AnahtarBilgisi& iAB );


		Q_DECL_EXPORT void decryptStream(	EASNFileInputStream* iEnvDataStream,
											QIODevice* oPDStream ,
											const QString &iParola );


		Q_DECL_EXPORT void decryptStream(	EASNFileInputStream* iEnvDataStream,
											QIODevice* oPDStream ,
											const QList< QPair<ECertificate,OzelAnahtarBilgisi> >  &iCertKeyList);



		 static Q_DECL_EXPORT int encryptStream(	QIODevice* iPDStream , 
													QIODevice*oEDStream  ,
													const QList<ECertificate> &iRecipients ,
													const AlgorithmIdentifier &iContEncAlg , 
													const QByteArray &iEncryptionKey  
			    								);


		 static Q_DECL_EXPORT int encryptStream(	QIODevice * iPDStream, 
													QIODevice * oEDStream, 
													const QString &iParola ,
													const AlgorithmIdentifier &iContEncAlg , 
													const QByteArray &iEncryptionKey  ,
													const AlgorithmIdentifier &iKeyDrvAlg , 
													const AlgorithmIdentifier &iKeyEncAlg 
												);

		static Q_DECL_EXPORT int encryptStreamToGroup(	QIODevice* iPDStream , 
														QIODevice*oEDStream  ,
														const QList<ECertificate> &iRecipients ,
														const AlgorithmIdentifier &iContEncAlg , 
														const QByteArray &iEncryptionKey  
													 );

		 

		 Q_DECL_EXPORT void	setTamamlandi(const int iTamamlandi);



	signals:
		Q_DECL_EXPORT void tamamlandi(quint64 );
		Q_DECL_EXPORT void progressTextChange(const QString &);

	public:
		Q_DECL_EXPORT ~StreamEnvelopedData(void);
	};

}
#endif

