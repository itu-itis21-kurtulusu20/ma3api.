#ifndef __OUTPUTSTREAM__
#define __OUTPUTSTREAM__

#include <QIODevice>
#include <OSRTOutputStream.h>
#include "myasndefs.h"

#define ERR_NULL_STREAM "G/Ç Aygýtý tanýmlanmamýþ" 

namespace esya
{

	/**
	* \ingroup EsyaASN
	* 
	* ASN1 çýktý stream nesnesi
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT OutputStream
	{
	public: enum OS_Type { QIODEVICE , OSCOUTPUTSTREAM };

		QIODevice *			pIODevice;
		OSRTOutputStream *	pOSCOut;
		OS_Type				mType;

	public:
		
		OutputStream();
		OutputStream(QIODevice *);
		OutputStream(OSRTOutputStream *);
		OutputStream(OutputStream& iOS);


		OutputStream & operator= (OutputStream& iOS);

		int write(const QByteArray iData);
		int write(const char* data , const int len );

		
		QIODevice*			getIODevice();
		OSRTOutputStream*	getOSCOut();
		OS_Type				getType() const;
	

		void releaseStream();

	public:
		virtual ~OutputStream(void);
	};
}

#endif
