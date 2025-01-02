
#include "Clearance.h"
#include "ESeqOfList.h"


using namespace esya;

Clearance::Clearance(void)
: 	mClassListPresent(false),
	mSecurityCategoriesPresent(false)
{
}

Clearance::Clearance(const QByteArray & iClearance)
{
	constructObject(iClearance);
}

Clearance::Clearance(const ASN1T_ATTRCERT_Clearance & iClearance )
{
	copyFromASNObject(iClearance);
}

Clearance::Clearance(const Clearance& iClearance)
:	mPolicyId(iClearance.getPolicyId()),
	mClassListPresent(iClearance.isClassListPresent()),
	mSecurityCategoriesPresent(iClearance.isSecurityCategoriesPresent())
{
	if (mClassListPresent)
		mClassList = iClearance.getClassList();
	
	if (mSecurityCategoriesPresent)
		mSecurityCategories = iClearance.getSecurityCategories();
}

Clearance& Clearance::operator=(const Clearance& iClearance)
{
	mPolicyId = iClearance.getPolicyId();
	if (mClassListPresent)
		mClassList = iClearance.getClassList();

	if (mSecurityCategoriesPresent)
		mSecurityCategories = iClearance.getSecurityCategories();

	return *this;
}

bool esya::operator==( const Clearance& iRHS, const Clearance& iLHS)
{
	if (	( iRHS.getPolicyId()!= iLHS.getPolicyId() ) ||
			( iRHS.isSecurityCategoriesPresent()	!= iLHS.isSecurityCategoriesPresent() )	|| 
			( iRHS.isClassListPresent()	!= iLHS.isClassListPresent() ) )
		return false;

	if (	( iRHS.isClassListPresent() ) && 
			( iRHS.getClassList() != iLHS.getClassList() )	)
		return false;

	if (	( iRHS.isSecurityCategoriesPresent() ) && 
			( iRHS.isSecurityCategoriesPresent() != iLHS.isSecurityCategoriesPresent() )	)
		return false;

	return true;
}

bool esya::operator!=( const Clearance& iRHS, const Clearance& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int Clearance::copyFromASNObject(const ASN1T_ATTRCERT_Clearance & iClearance)
{
	mPolicyId = iClearance.policyId;	
	mClassListPresent = iClearance.m.classListPresent == 1;
	mSecurityCategoriesPresent = iClearance.m.securityCategoriesPresent == 1;

	if (mClassListPresent)
		mClassList.copyFromASNObject(iClearance.classList);

	if (mSecurityCategoriesPresent)
		SecurityCategory().copySecurityCategories(iClearance.securityCategories ,mSecurityCategories);

	return SUCCESS;
}

int Clearance::copyToASNObject(ASN1T_ATTRCERT_Clearance &oClearance)const
{
	oClearance.policyId = mPolicyId;	
	oClearance.m.classListPresent = mClassListPresent ? 1:0 ;
	oClearance.m.securityCategoriesPresent = mSecurityCategoriesPresent ? 1:0 ;


	if (mClassListPresent)
		mClassList.copyToASNObject(oClearance.classList);

	if (mSecurityCategoriesPresent)
		SecurityCategory().copySecurityCategories(mSecurityCategories,oClearance.securityCategories );

	return SUCCESS;
}

void Clearance::freeASNObject(ASN1T_ATTRCERT_Clearance& oClearance)const
{
	if (oClearance.m.classListPresent == 1)
		ClassList().freeASNObject(oClearance.classList);

	if (oClearance.m.securityCategoriesPresent == 1)
		SecurityCategory().freeASNObjects(oClearance.securityCategories );

}

bool Clearance::isClassListPresent()const
{
	return mClassListPresent;
}

bool Clearance::isSecurityCategoriesPresent()const
{
	return mSecurityCategoriesPresent;
}

const ASN1TObjId & Clearance::getPolicyId()const
{
	return mPolicyId;
}

const ClassList & Clearance::getClassList()const
{
	return mClassList;
}

const QList<SecurityCategory>& Clearance::getSecurityCategories()const
{
	return mSecurityCategories;
}

void Clearance::setClassListPresent(bool iCLP)
{
	mClassListPresent = iCLP;
}

void Clearance::setSecurityCategoriesPresent(bool iSCP)
{
	mSecurityCategoriesPresent= iSCP;
}

void Clearance::setPolicyId(const ASN1TObjId & iPId)
{
	mPolicyId = iPId;
}

void Clearance::setClassList(const ClassList & iCL)
{
	mClassListPresent = true;
	mClassList = iCL;
}

void Clearance::setSecurityCategories(const QList<SecurityCategory>& iSCs)
{
	mSecurityCategories = iSCs;
	mSecurityCategoriesPresent = !mSecurityCategories.isEmpty();
}

void Clearance::appendSecurityCategory(const SecurityCategory& iSC)
{
	mSecurityCategoriesPresent= true;
	mSecurityCategories.append(iSC);
}

Clearance::~Clearance(void)
{
}