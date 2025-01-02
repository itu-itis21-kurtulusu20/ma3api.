
#ifndef __POLICYMAPPINGS__
#define __POLICYMAPPINGS__



#include "AY_Eklenti.h"
#include "PolicyMappingsElement.h"


namespace esya
{

	class Q_DECL_EXPORT PolicyMappings : public EASNWrapperTemplate<ASN1T_IMP_PolicyMappings,ASN1C_IMP_PolicyMappings>, public AY_Eklenti
	{
		QList<PolicyMappingsElement> mList;

	public:

		PolicyMappings(void);
		PolicyMappings(const ASN1T_IMP_PolicyMappings& iPM);
		PolicyMappings(const QByteArray & iPM );
		PolicyMappings(const PolicyMappings&iPM);


		PolicyMappings& operator=(const PolicyMappings&);
		Q_DECL_EXPORT friend bool operator==(const PolicyMappings & iRHS, const PolicyMappings & iLHS);
		Q_DECL_EXPORT friend bool operator!=(const PolicyMappings & iRHS, const PolicyMappings & iLHS);

		int copyFromASNObject(const ASN1T_IMP_PolicyMappings& iPM);
		int copyToASNObject(ASN1T_IMP_PolicyMappings & oPM) const;
		void freeASNObject(ASN1T_IMP_PolicyMappings & oPM)const;
		
		virtual ~PolicyMappings(void);

		// GETTERS AND SETTERS

		const QList<PolicyMappingsElement>& getList() const;

		void setList(const QList<PolicyMappingsElement>& iPM);
		void addMapping(const PolicyMappingsElement& iPME);

		QList<ASN1TObjId> getSubjectEquivalents(const ASN1TObjId & iIssuerDomainPolicy)const;

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

