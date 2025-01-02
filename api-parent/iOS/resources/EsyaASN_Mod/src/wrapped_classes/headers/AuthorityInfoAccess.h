#ifndef __AUTHORITYINFOACCESS__
#define __AUTHORITYINFOACCESS__

#include "AccessDescription.h"
#include "AY_Eklenti.h"

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
	class Q_DECL_EXPORT AuthorityInfoAccess  : public EASNWrapperTemplate<ASN1T_IMP_AuthorityInfoAccessSyntax,ASN1C_IMP_AuthorityInfoAccessSyntax> , public AY_Eklenti
	{
		QList<AccessDescription> mList;

	public:
		AuthorityInfoAccess(void);
		AuthorityInfoAccess(const ASN1T_IMP_AuthorityInfoAccessSyntax &);
		AuthorityInfoAccess(const QByteArray &);
		AuthorityInfoAccess(const AuthorityInfoAccess&);
		AuthorityInfoAccess(const QList<AccessDescription>&);

		AuthorityInfoAccess & operator=(const AuthorityInfoAccess&);
		friend bool operator==(const AuthorityInfoAccess & iRHS, const AuthorityInfoAccess& iLHS);
		friend bool operator!=(const AuthorityInfoAccess & iRHS, const AuthorityInfoAccess& iLHS);

		int copyFromASNObject(const ASN1T_IMP_AuthorityInfoAccessSyntax &);
		int copyToASNObject(ASN1T_IMP_AuthorityInfoAccessSyntax& )const;
		void freeASNObject(ASN1T_IMP_AuthorityInfoAccessSyntax & )const;

		virtual ~AuthorityInfoAccess(void);

		// GETTERS AND SETTERS


		const QList<AccessDescription> &getList() const;

		virtual QString toString() const;

		QList<QString> adresleriAl(const QString & iAdresTipi);
		QList<QString> ocspAdresleriAl();	

		QStringList toStringList() const ;

		/************************************************************************/
		/*					AY_EKLENTI FONKSIYONLARI                            */
		/************************************************************************/


		virtual QString eklentiAdiAl()			const ;
		virtual QString eklentiKisaDegerAl()	const ;
		virtual QString eklentiUzunDegerAl()	const ;

		virtual AY_Eklenti* kendiniKopyala() const ;

	};

}

#endif

