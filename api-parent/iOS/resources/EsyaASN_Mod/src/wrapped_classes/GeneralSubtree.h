
#ifndef __GENERALSUBTREE__
#define __GENERALSUBTREE__

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
	class Q_DECL_EXPORT GeneralSubtree  : public EASNWrapperTemplate<ASN1T_IMP_GeneralSubtree,ASN1C_IMP_GeneralSubtree>
	{
		bool		mMaximumPresent;
	   
		GeneralName mBase;
		int			mMinimum;
		int			mMaximum;

	public:

		GeneralSubtree(const GeneralSubtree &);
		GeneralSubtree(const ASN1T_IMP_GeneralSubtree & );
		GeneralSubtree(const QByteArray & );
		GeneralSubtree(void);
		

		GeneralSubtree& operator=(const GeneralSubtree&);
		friend bool operator==(const GeneralSubtree& iRHS, const GeneralSubtree& iLHS);
		friend bool operator!=(const GeneralSubtree& iRHS, const GeneralSubtree& iLHS);


		int copyFromASNObject(const ASN1T_IMP_GeneralSubtree& iGST);
		int copyToASNObject(ASN1T_IMP_GeneralSubtree & oGST) const;
		void freeASNObject(ASN1T_IMP_GeneralSubtree & oGST)const;


		const bool		&isMaximumPresent()const;
	   
		GeneralName	&getBase();

		const GeneralName	&getBase()const;
		const int			&getMinimum()const;
		const int			&getMaximum()const;

		int copyGSTs(const ASN1T_IMP_GeneralSubtrees & iGSTs, QList<GeneralSubtree>& oList);
		int copyGSTs(const QList<GeneralSubtree> iList ,ASN1T_IMP_GeneralSubtrees & oGSTs);	

		virtual ~GeneralSubtree(void);

		// GETTERS AND SETTERS

		bool permits(const Name&)const;
		bool permits(const GeneralName&)const;

		bool excludes(const Name&)const;
		bool excludes(const GeneralName&)const;

		static QList<GeneralSubtree> intersect(const QList<GeneralSubtree>& iListA,const QList<GeneralSubtree>& iListB);
		static QList<GeneralSubtree> unite(const QList<GeneralSubtree>& iListA,const QList<GeneralSubtree>& iListB);

	};

}

#endif

