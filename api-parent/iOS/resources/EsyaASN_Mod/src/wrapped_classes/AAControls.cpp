
#include "AAControls.h"
#include "ESeqOfList.h"


using namespace esya;

namespace esya
{


	AAControls::AAControls(void)
	:	mPathLenConstraintPresent(false),
		mPermittedAttrsPresent(false), 
		mExcludedAttrsPresent(false)
	{
	}

	AAControls::AAControls(const QByteArray & iAAControls)
	{
		constructObject(iAAControls);
	}

	AAControls::AAControls(const ASN1T_ATTRCERT_AAControls & iAAControls )
	{
		copyFromASNObject(iAAControls);
	}

	AAControls::AAControls(const AAControls& iAAControls)
	:	mPermitUnSpecified(iAAControls.getPermitUnSpecified()),
		mPermittedAttrsPresent(iAAControls.isPermittedAttrsPresent()),
		mExcludedAttrsPresent(iAAControls.isExcludedAttrsPresent()),
		mPathLenConstraintPresent(iAAControls.isPathLenConstraintPresent())
	{
		if (mPermittedAttrsPresent)
			mPermittedAttrs = iAAControls.getPermittedAttrs();

		if (mExcludedAttrsPresent)
			mExcludedAttrs = iAAControls.getExcludedAttrs();

		if (mPathLenConstraintPresent)
			mPathLenConstraint = iAAControls.getPathLenConstraint();
		
	}

	AAControls& AAControls::operator=(const AAControls& iAAControls)
	{
		mPermitUnSpecified			= iAAControls.getPermitUnSpecified();
		mPermittedAttrsPresent		= iAAControls.isPermittedAttrsPresent();
		mExcludedAttrsPresent		= iAAControls.isExcludedAttrsPresent();
		mPathLenConstraintPresent	= iAAControls.isPathLenConstraintPresent();

		if (mPermittedAttrsPresent)
			mPermittedAttrs = iAAControls.getPermittedAttrs();

		if (mExcludedAttrsPresent)
			mExcludedAttrs = iAAControls.getExcludedAttrs();

		if (mPathLenConstraintPresent)
			mPathLenConstraint = iAAControls.getPathLenConstraint();

		return *this;
	}

	bool operator==( const AAControls& iRHS, const AAControls& iLHS)
	{
		if (	( iRHS.getPermitUnSpecified()		!= iLHS.getPermitUnSpecified() )	||  
				( iRHS.isPermittedAttrsPresent()	!= iLHS.isPermittedAttrsPresent() )	||
				( iRHS.isExcludedAttrsPresent()		!= iLHS.isExcludedAttrsPresent() )	||
				( iRHS.isPathLenConstraintPresent()	!= iLHS.isPathLenConstraintPresent() ) )
			return false;
		
		if (	( iRHS.isPermittedAttrsPresent() ) &&
				( iRHS.getPermittedAttrs() != iLHS.getPermittedAttrs() ) )
			return false;

		if (	( iRHS.isExcludedAttrsPresent() ) &&
				( iRHS.getExcludedAttrs() != iLHS.getExcludedAttrs() ) )
			return false;

		if (	( iRHS.isPathLenConstraintPresent() ) &&
				( iRHS.getPathLenConstraint() != iLHS.getPathLenConstraint() ) )
			return false;

		return  true;
	}

	bool operator!=( const AAControls& iRHS, const AAControls& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int AAControls::copyFromASNObject(const ASN1T_ATTRCERT_AAControls & iAAControls)
	{
		mPermitUnSpecified			= iAAControls.permitUnSpecified;
		mPermittedAttrsPresent		= iAAControls.m.permittedAttrsPresent == 1;
		mExcludedAttrsPresent		= iAAControls.m.excludedAttrsPresent == 1;
		mPathLenConstraintPresent	= iAAControls.m.pathLenConstraintPresent == 1;

		if (mPermittedAttrsPresent)
			mPermittedAttrs.copyFromASNObject(iAAControls.permittedAttrs);

		if (mExcludedAttrsPresent)
			mExcludedAttrs.copyFromASNObject(iAAControls.excludedAttrs);

		if (mPathLenConstraintPresent)
			mPathLenConstraint= iAAControls.pathLenConstraint;

		return SUCCESS;
	}

	int AAControls::copyToASNObject(ASN1T_ATTRCERT_AAControls &oAAControls)const
	{
		oAAControls.permitUnSpecified = mPermitUnSpecified ;
		oAAControls.m.permittedAttrsPresent = mPermittedAttrsPresent ? 1:0 ;
		oAAControls.m.excludedAttrsPresent = mExcludedAttrsPresent ? 1:0 ;
		oAAControls.m.pathLenConstraintPresent = mPathLenConstraintPresent ? 1:0 ;

		if (mPermittedAttrsPresent)
			mPermittedAttrs.copyToASNObject(oAAControls.permittedAttrs);

		if (mExcludedAttrsPresent)
			mExcludedAttrs.copyToASNObject(oAAControls.excludedAttrs);

		if (mPathLenConstraintPresent)
			oAAControls.pathLenConstraint = mPathLenConstraint;

		return SUCCESS;
	}

	void AAControls::freeASNObject(ASN1T_ATTRCERT_AAControls& oAAControls)
	{
		if (oAAControls.m.permittedAttrsPresent == 1 )
			AttrSpec().freeASNObject(oAAControls.permittedAttrs);

		if (oAAControls.m.excludedAttrsPresent == 1 )
			AttrSpec().freeASNObject(oAAControls.excludedAttrs);

		return;
	}

	bool AAControls::isPathLenConstraintPresent()const
	{
		return mPathLenConstraintPresent;
	}

	bool AAControls::isPermittedAttrsPresent()const 
	{
		return mPermittedAttrsPresent;
	}

	bool AAControls::isExcludedAttrsPresent()const 
	{
		return mExcludedAttrsPresent;
	}

	int	AAControls::getPathLenConstraint()const
	{
		return mPathLenConstraint;
	}

	const AttrSpec & AAControls::getPermittedAttrs()const
	{
		return mPermittedAttrs;
	}

	const AttrSpec & AAControls::getExcludedAttrs()const
	{
		return mExcludedAttrs;
	}

	bool AAControls::getPermitUnSpecified()const
	{
		return mPermitUnSpecified;
	}

	void AAControls::setPathLenConstraintPresent(bool iPLCP) 
	{
		mPermittedAttrsPresent = iPLCP;
	}

	void AAControls::setPermittedAttrsPresent(bool iPAP)
	{
		mPermittedAttrsPresent = iPAP;
	}

	void AAControls::setExcludedAttrsPresent(bool iEAP)
	{
		mExcludedAttrsPresent = iEAP;
	}

	void AAControls::setPathLenConstraint(int iPLC)
	{
		mPathLenConstraint = iPLC;
	}

	void AAControls::setPermittedAttrs(const AttrSpec & iPA)
	{
		mPermittedAttrs = iPA;
	}

	void AAControls::setExcludedAttrs(const AttrSpec & iEA )
	{
		mExcludedAttrs = iEA;
	}

	void AAControls::setPermitUnSpecified(bool iPU)
	{
		mPermitUnSpecified = iPU;
	}

	AAControls::~AAControls(void)
	{
	}

}