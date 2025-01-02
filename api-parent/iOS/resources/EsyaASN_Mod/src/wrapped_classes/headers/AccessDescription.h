
#ifndef __ACCESSDESCRIPTION__
#define __ACCESSDESCRIPTION__



#include "EException.h"
#include "ESeqOfList.h"
#include "ortak.h"
#include "GeneralName.h"


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
	class Q_DECL_EXPORT AccessDescription : public EASNWrapperTemplate<ASN1T_IMP_AccessDescription,ASN1C_IMP_AccessDescription>
	{

	   ASN1TObjId	mAccessMethod;
	   GeneralName	mAccessLocation;

	public:

		AccessDescription(const AccessDescription &);
		AccessDescription(const ASN1T_IMP_AccessDescription & );
		AccessDescription(const QByteArray & );
		AccessDescription(void);
		AccessDescription(const ASN1OBJID & ,const GeneralName& );

		AccessDescription & operator=(const AccessDescription&);
		friend bool operator==(const AccessDescription& iRHS, const AccessDescription& iLHS);
		friend bool operator!=(const AccessDescription& iRHS, const AccessDescription& iLHS);


		int copyFromASNObject(const ASN1T_IMP_AccessDescription& iAD);
		int copyToASNObject(ASN1T_IMP_AccessDescription & oAD) const;
		void freeASNObject(ASN1T_IMP_AccessDescription & oAD)const;

		int copyADs(const ASN1T_IMP_AuthorityInfoAccessSyntax & iADs, QList<AccessDescription>& oList);
		int copyADs(const QList<AccessDescription> iList , ASN1T_IMP_AuthorityInfoAccessSyntax& oADs);	

		virtual ~AccessDescription(void);

		const ASN1OBJID& getAccessMethod() const;
		const GeneralName& getAccessLocation() const;


		static	QList<QString> adresleriAl(const QList<AccessDescription> & iAIA, const QString & iAdresTipi);
		static	QList<QString> ocspAdresleriAl( const QList<AccessDescription> & iAIA);	

	public:

	};

}
#endif

