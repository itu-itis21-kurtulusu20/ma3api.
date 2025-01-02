#ifndef __ATTRSPEC__
#define __ATTRSPEC__


#include "ortak.h"
#include "attrcert.h"


namespace esya
{
	class Q_DECL_EXPORT AttrSpec : public EASNWrapperTemplate<ASN1T_ATTRCERT_AttrSpec,ASN1C_ATTRCERT_AttrSpec>
	{
		QList<ASN1TObjId> mList;

	public:
		AttrSpec(void);
		AttrSpec(const QByteArray & iAttrSpec);
		AttrSpec(const ASN1T_ATTRCERT_AttrSpec & iAttrSpec );
		AttrSpec(const AttrSpec& iAttrSpec);

		AttrSpec& operator=(const AttrSpec& iAttrSpec);
		friend bool operator==( const AttrSpec& iRHS, const AttrSpec& iLHS);
		friend bool operator!=( const AttrSpec& iRHS, const AttrSpec& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_AttrSpec & iAttrSpec);
		int copyToASNObject(ASN1T_ATTRCERT_AttrSpec &oAttrSpec)const;
		void freeASNObject(ASN1T_ATTRCERT_AttrSpec& oAttrSpec)const;

		virtual ~AttrSpec(void);

		// GETTERS AND SETTERS

		const QList<ASN1TObjId> & getList()const;

		void setList(const QList<ASN1TObjId> & iList);
		void append(const ASN1TObjId & iElement);
	};

}

#endif

