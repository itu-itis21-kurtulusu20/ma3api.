#ifndef __CLEARANCE__
#define __CLEARANCE__

#include "SecurityCategory.h"
#include "ClassList.h"


namespace esya
{
	class Q_DECL_EXPORT Clearance : public EASNWrapperTemplate<ASN1T_ATTRCERT_Clearance,ASN1C_ATTRCERT_Clearance>
	{
		bool mClassListPresent;
		bool mSecurityCategoriesPresent;

		ASN1TObjId	mPolicyId;
		ClassList	mClassList;
		QList<SecurityCategory> mSecurityCategories;
	
	public:
		Clearance(void);
		Clearance(const QByteArray & iClearance);
		Clearance(const ASN1T_ATTRCERT_Clearance & iClearance );
		Clearance(const Clearance& iClearance);

		Clearance& operator=(const Clearance& iClearance);
		friend bool operator==( const Clearance& iRHS, const Clearance& iLHS);
		friend bool operator!=( const Clearance& iRHS, const Clearance& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_Clearance & iClearance);
		int copyToASNObject(ASN1T_ATTRCERT_Clearance &oClearance)const;
		void freeASNObject(ASN1T_ATTRCERT_Clearance& oClearance)const;

		virtual ~Clearance(void);

		// GETTERS AND SETTERS

		bool isClassListPresent()const;
		bool isSecurityCategoriesPresent()const;

		const ASN1TObjId & getPolicyId()const;
		const ClassList & getClassList()const;
		const QList<SecurityCategory>& getSecurityCategories()const;

		void setClassListPresent(bool iCLP);
		void setSecurityCategoriesPresent(bool iSCP);
		void setPolicyId(const ASN1TObjId & iPId);
		void setClassList(const ClassList & iCL);
		void setSecurityCategories(const QList<SecurityCategory>& iSCs);
		void appendSecurityCategory(const SecurityCategory& iSC);

	};

}

#endif

