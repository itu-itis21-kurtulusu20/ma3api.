#ifndef __GENERALNAME__
#define __GENERALNAME__


#include "Implicit.h"
#include "Name.h"
#include "AnotherName.h"
#include "ORAddress.h"

namespace esya
{

	enum GN_Type { GNT_RFC822Name = T_IMP_GeneralName_rfc822Name , GNT_DNSName = T_IMP_GeneralName_dNSName, GNT_DirectoryName = T_IMP_GeneralName_directoryName , GNT_URI = T_IMP_GeneralName_uniformResourceIdentifier , GNT_IPAddress = T_IMP_GeneralName_iPAddress , GNT_OtherName = T_IMP_GeneralName_otherName, GNT_X400Address = T_IMP_GeneralName_x400Address};

	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper sýnýfý. Detaylar için .asn dökümanýna bakýnýz
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT GeneralName  : public EASNWrapperTemplate<ASN1T_IMP_GeneralName,ASN1C_IMP_GeneralName>
	{
		GN_Type		mType;

		QString		mRFC822Name;
		QString		mDNSName;
		QString		mURI;
		Name		mDirectoryName;
		QByteArray	mIPAddress;
		AnotherName mAnotherName;
		ORAddress	mX400Address;

	public:
		GeneralName();
		GeneralName(const ASN1T_IMP_GeneralName & );
		GeneralName(const GeneralName & );
		GeneralName(const  QByteArray &);

		GeneralName& operator=(const GeneralName& );
		Q_DECL_EXPORT friend bool operator==(const GeneralName & ,const GeneralName & );
		Q_DECL_EXPORT friend bool operator!=(const GeneralName & iRHS, const GeneralName & iLHS);


		int copyFromASNObject(const ASN1T_IMP_GeneralName & iGN);
		int copyToASNObject(ASN1T_IMP_GeneralName & ) const;	
		void freeASNObject(ASN1T_IMP_GeneralName & )const;

		int copyGeneralNames(const ASN1T_IMP_GeneralNames & iGNs, QList<GeneralName>& oList);
		int copyGeneralNames(const QList<GeneralName> iList ,ASN1T_IMP_GeneralNames & oGNs);	
		int copyGeneralNames(const QByteArray & iASNBytes, QList<GeneralName>& oList);

		virtual ~GeneralName(void);

		// GETTERS AND SETTERS

		const GN_Type& getType()const;
		const QString& getRFC822Name()const;
		const QString& getDNSName()const ;
		const QString& getURI()const ;
		const Name& getDirectoryName()const;
		const QByteArray& getIPAddress()const;
		const AnotherName& getAnotherName()const;
		const ORAddress& getX400Address()const;


		void setDirectoryName(const Name& );
		void setRFC822Name(const QString& );
		void setDNSName(const QString& );
		void setURI(const QString& );
		void setX400Address(const ORAddress& );

		virtual QString toString()const;

		static bool hasMatch(const QList<GeneralName>&iList, const Name& );

	};

}

#endif 

