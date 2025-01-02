
#ifndef __EXTENSIONT__
#define __EXTENSIONT__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "Explicit.h"
#include "EASNWrapperTemplate.h"


namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT  Extension  : public EASNWrapperTemplate<ASN1T_EXP_Extension,ASN1C_EXP_Extension>
	{

		bool mCritical;
		ASN1TObjId mExtensionId;
		QByteArray mExtensionValue;

		
		

	public:

		Extension(const Extension &);
		Extension(const ASN1T_EXP_Extension & iExtension);
		Extension(const QByteArray & );
		Extension(void);
		Extension(const ASN1OBJID & extnID,bool critical, const QByteArray &extnValue);

		Extension & operator=(const Extension&);
		Q_DECL_EXPORT friend bool operator==(const Extension & iRHS,const Extension & iLHS);
		Q_DECL_EXPORT friend bool operator!=(const Extension & iRHS, const Extension & iLHS);


		int copyFromASNObject(const ASN1T_EXP_Extension& iExtension);
		int copyToASNObject(ASN1T_EXP_Extension & oExtension) const;
		void freeASNObject(ASN1T_EXP_Extension & oExtension)const;


		int copyExtensions(const ASN1T_EXP_Extensions & iExtensions, QList<Extension>& oList)const;
		int copyExtensions(const QList<Extension> iList ,ASN1T_EXP_Extensions & oExtensions)const;	
		int copyExtensions(const QByteArray & iASNBytes, QList<Extension>& oList)const;
		int copyExtensions(const QList<Extension>& iList , QByteArray & oASNBytes)const;
		
		void freeExtensions(ASN1T_EXP_Extensions & oExtensions)const;


		void setExtensionValue(const QByteArray& );
		void setExtensionId(const ASN1TObjId& ) ;
		void setCritical(const bool );


		const QByteArray& getExtensionValue() const;
		const ASN1TObjId& getExtensionId() const;
		const bool isCritical() const ;

	public:
		virtual ~Extension(void);
	};

}

#endif


