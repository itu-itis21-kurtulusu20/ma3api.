
#ifndef __STREAMEDSIGNEDDATA__
#define __STREAMEDSIGNEDDATA__

#include "SignedData.h"
#include "EASNStreamingUtils.h"
#include "rtPrintStream.h"
#include "cms.h"
#include "ELogger.h"
#include "myasndefs.h"
#include "Besleyici.h"
#include "SignParam.h"

#include "TAlg_Paralel_Adaptor.h"

namespace esya
{

#define EMIT_PROGRESS_TEXT(x)	emit progressTextChange(x);
#define EMIT_PROGRESS			emit tamamlandi(mTamamlandi);

	/**
	* \ingroup EsyaASN
	* 
	* SignedData ASN1Wrapper sýnýfýna stream özelliði eklenmiþ alt sýnýf
	*
	* \author dindaro
	*
	*/
	class StreamedSignedData : public QObject,public SignedData 
	{

		Q_OBJECT

		friend class SignerInfo;

		int							mEocCount;
		FILE*						mSDFile;
		OutputStream				mOUT;	
		EASNFileInputStream		*	pIN;
		QIODevice				*	mPDStream;		


		bool mSignersConstructed;

		QList< QPair<AlgorithmIdentifier,QByteArray> > mDigestList;
		
		SignParam					mSignParam;
		
		qint64 mTamamlandi;

	public:

		Q_DECL_EXPORT StreamedSignedData(const QString &iSDFileName, QIODevice * oPDStream = NULL );
		Q_DECL_EXPORT StreamedSignedData(EASNFileInputStream &iSDStream, QIODevice * oPDStream = NULL );
		Q_DECL_EXPORT StreamedSignedData(void);

		Q_DECL_EXPORT void loadFromFile(FILE * iSDFile, QIODevice* oPDStream );

		Q_DECL_EXPORT const QString & getPlainDataFileName();	


		Q_DECL_EXPORT ~StreamedSignedData(void);


		Q_DECL_EXPORT virtual int addParallelBESSigners(	SignParam & iSP, QIODevice& iPDSource );
		Q_DECL_EXPORT virtual int addParallelBESSigners(	const QList<SignerParam> & iSPs );
		Q_DECL_EXPORT virtual int addParallelBESSigner(	const SignerParam & iSP, QIODevice& iPDSource);

		Q_DECL_EXPORT virtual int addParallelSigner(const SignerInfo& iSigner , const ECertificate & iCert);

		Q_DECL_EXPORT void fillDigestAlgorithms( const SignParam & iSP );
		Q_DECL_EXPORT void addDigestAlgorithm( const AlgorithmIdentifier & iDigestAlg);

		Q_DECL_EXPORT int constructSSD( QIODevice * iPDStream , QIODevice * iSDStream, const SignParam * iSP);
		Q_DECL_EXPORT int	verifyFile(const QList<ECertificate>& = QList<ECertificate>())const;

		Q_DECL_EXPORT void	setTamamlandi(const int iTamamlandi);


		QByteArray calculateDigest(	const QString & iKaynakAdi,  
									const AlgorithmIdentifier &iDigestAlg ,bool isSigned );

		int getDigest(const AlgorithmIdentifier & ,QByteArray & qbDigest) const;

	signals:

		Q_DECL_EXPORT void tamamlandi(quint64 );
		Q_DECL_EXPORT void progressTextChange(const QString &);

	private:

		StreamedSignedData(const ASN1T_CMS_SignedData & ){};

		/*STREAM FUNCTIONS*/

        void _appendDigestList(QList<DigestInfo> & iDigestList);

        void _ozetcileriOlustur(TAlg_Paralel_Adaptor & iOzetciler);
        void _ozetcileriYokEt(TAlg_Paralel_Adaptor & iOzetciler);
        void _fillDigestList(TAlg_Paralel_Adaptor & iOzetciler);
        bool _checkEncapContent();
        void _seek2EncapContent();
		

        void _readContentInfo();
        void _readContentType();
        void _readContent()	;
        void _readSignedData();
        void _readVersion();
        void _readDigestAlgorithms();
        void _readEncapContentInfo();
        void _readEncapContentInfoType();
        void _readEncapContent();
        void _readEncapContent( TAlg_Paralel_Adaptor & iOzetciler);
        void _readCertificateSet();
        void _readCRLs();
        void _readSignerInfos();

        void processEncapContent(EASNFileInputStream *pIN , BlokIsleyici& iBP );
        void processEncapContent_Constructed(EASNFileInputStream *pIN , BlokIsleyici& iBP );
        void writeOctetString(OutputStream& pOUT, QIODevice & iIN , ASN1TagType tagging,bool iAyrikImza = false ,TAlg_Paralel_Adaptor * pOzetciler = NULL);

        void _writeContentInfo();
        void _writeContentType();
        void _writeContent();
        void _writeSignedData();
        void _writeVersion();
        void _writeDigestAlgorithms();
        void _writeEncapContentInfo();
        void _writeEncapContentInfoType();
        void _writeEncapContent();
        void _writeCertificates();
        int _writeSignerInfos();
        int _writeFileEnd();
		






	};

}

#endif

