#include "IssuingDistributionPoint.h"
#include "OrtakDil.h"

using namespace esya;

IssuingDistributionPoint::IssuingDistributionPoint(void)
{
}

IssuingDistributionPoint::IssuingDistributionPoint(const IssuingDistributionPoint & iIDP)
:	mDistributionPointPresent(iIDP.isDistributionPointPresent()),
	mOnlySomeReasonsPresent(iIDP.isOnlySomeReasonsPresent()),
	mOnlyContainsUserCerts(iIDP.isOnlyContainsUserCerts()),
	mOnlyContainsCACerts(iIDP.isOnlyContainsCACerts()),
	mOnlyContainsAttributeCerts(iIDP.isOnlyContainsAttributeCerts()),
	mIndirectCRL(iIDP.isIndirectCRL()),
	mDistributionPoint(iIDP.getDistributionPoint()),
	mOnlySomeReasons(iIDP.getOnlySomeReasons())
{
}

IssuingDistributionPoint::IssuingDistributionPoint(const ASN1T_IMP_IssuingDistributionPoint & iIDP)
{
	copyFromASNObject(iIDP);	
}

IssuingDistributionPoint::IssuingDistributionPoint(const QByteArray & iIDP)
{
	constructObject(iIDP);	
}



IssuingDistributionPoint & IssuingDistributionPoint::operator=(const IssuingDistributionPoint& iIDP)
{
	mDistributionPointPresent	= iIDP.isDistributionPointPresent();
	mOnlySomeReasonsPresent		= iIDP.isOnlySomeReasonsPresent();
	mOnlyContainsUserCerts		= iIDP.isOnlyContainsUserCerts();
	mOnlyContainsCACerts		= iIDP.isOnlyContainsCACerts();
	mOnlyContainsAttributeCerts = iIDP.isOnlyContainsAttributeCerts();
	mIndirectCRL				= iIDP.isIndirectCRL();
	mDistributionPoint			= iIDP.getDistributionPoint();
	mOnlySomeReasons			= iIDP.getOnlySomeReasons();
	return *this;
}

bool esya::operator==(const esya::IssuingDistributionPoint& iRHS, const esya::IssuingDistributionPoint& iLHS)
{
	return (
				iRHS.isDistributionPointPresent()	== iLHS.isDistributionPointPresent()	&&
				iRHS.isOnlySomeReasonsPresent()		== iLHS.isOnlySomeReasonsPresent()		&&
				iRHS.isOnlyContainsUserCerts()		== iLHS.isOnlyContainsUserCerts()		&&
				iRHS.isOnlyContainsCACerts()		== iLHS.isOnlyContainsCACerts()			&&
				iRHS.isOnlyContainsAttributeCerts()	== iLHS.isOnlyContainsAttributeCerts()	&&
				iRHS.isIndirectCRL()					== iLHS.isIndirectCRL()					&&
				iRHS.getDistributionPoint()			== iLHS.getDistributionPoint()			&&
				iRHS.getOnlySomeReasons()			== iLHS.getOnlySomeReasons()				
			);
}

bool esya::operator!=(const esya::IssuingDistributionPoint& iRHS, const esya::IssuingDistributionPoint& iLHS)
{
	return ( ! ( iRHS == iLHS ) );
}

int IssuingDistributionPoint::copyFromASNObject(const ASN1T_IMP_IssuingDistributionPoint& iIDP)
{
	mDistributionPointPresent	= iIDP.m.distributionPointPresent;
	mOnlySomeReasonsPresent		= iIDP.m.onlySomeReasonsPresent;
	mOnlyContainsUserCerts		= iIDP.onlyContainsUserCerts;
	mOnlyContainsCACerts		= iIDP.onlyContainsCACerts;
	mOnlyContainsAttributeCerts = iIDP.onlyContainsAttributeCerts;
	mIndirectCRL				= iIDP.indirectCRL;

	mDistributionPoint.copyFromASNObject(iIDP.distributionPoint);
	mOnlySomeReasons.copyFromASNObject(iIDP.onlySomeReasons);

	return SUCCESS;
}

int IssuingDistributionPoint::copyToASNObject(ASN1T_IMP_IssuingDistributionPoint & oIDP) const
{
	oIDP.m.distributionPointPresent	= mDistributionPointPresent	;
	oIDP.m.onlySomeReasonsPresent	= mOnlySomeReasonsPresent;
	oIDP.onlyContainsUserCerts		= mOnlyContainsUserCerts ;
	oIDP.onlyContainsCACerts		= mOnlyContainsCACerts	;
	oIDP.onlyContainsAttributeCerts = mOnlyContainsAttributeCerts ;
	oIDP.indirectCRL				= mIndirectCRL;

	mDistributionPoint.copyToASNObject(oIDP.distributionPoint);
	mOnlySomeReasons.copyToASNObject(oIDP.onlySomeReasons);

	return SUCCESS;
}

void IssuingDistributionPoint::freeASNObject(ASN1T_IMP_IssuingDistributionPoint & oIDP)const
{
	DistributionPointName().freeASNObject(oIDP.distributionPoint);
	ReasonFlags().freeASNObject(oIDP.onlySomeReasons);
}

void IssuingDistributionPoint::setDistributionPointPresent( bool  iDPP)
{
	mDistributionPointPresent = iDPP;
}

void IssuingDistributionPoint::setOnlySomeReasonsPresent( bool  iOSRP)
{
	mOnlySomeReasonsPresent = iOSRP;
}

void IssuingDistributionPoint::setOnlyContainsUserCerts( bool  iCUC)
{
	mOnlyContainsUserCerts = iCUC;
}

void IssuingDistributionPoint::setOnlyContainsCACerts( bool  iCCC)
{
	mOnlyContainsCACerts = iCCC;
}

void IssuingDistributionPoint::setOnlyContainsAttributeCerts( bool  iCAC)
{
	mOnlyContainsAttributeCerts = iCAC;
}

void IssuingDistributionPoint::setIndirectCRL( bool  iIC)
{
	mIndirectCRL = iIC;
}

void IssuingDistributionPoint::setDistributionPoint( const DistributionPointName&	iDP)
{
	mDistributionPoint = iDP;
}

void IssuingDistributionPoint::setOnlySomeReasons( const ReasonFlags& iOSR)
{
	mOnlySomeReasons = iOSR;
}

bool IssuingDistributionPoint::isDistributionPointPresent()	const
{
	return mDistributionPointPresent;
}

bool IssuingDistributionPoint::isOnlySomeReasonsPresent()const
{
	return mOnlySomeReasonsPresent;
}

bool IssuingDistributionPoint::isOnlyContainsUserCerts()const
{
	return mOnlyContainsUserCerts;
}

bool IssuingDistributionPoint::isOnlyContainsCACerts()const
{
	return mOnlyContainsCACerts;
}

bool IssuingDistributionPoint::isOnlyContainsAttributeCerts()const
{
	return mOnlyContainsAttributeCerts;
}


bool IssuingDistributionPoint::isIndirectCRL()const
{
	return mIndirectCRL;
}

const DistributionPointName & IssuingDistributionPoint::getDistributionPoint()	const
{
	return mDistributionPoint;
}

const ReasonFlags & IssuingDistributionPoint::getOnlySomeReasons()const
{
	return mOnlySomeReasons;
}


IssuingDistributionPoint::~IssuingDistributionPoint(void)
{
}


/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString IssuingDistributionPoint::eklentiAdiAl() const 
{
	return DIL_EXT_YETKILI_DAGITIM_NOKTASI;
}

QString IssuingDistributionPoint::eklentiKisaDegerAl()	const 
{
	return "";
}

QString IssuingDistributionPoint::eklentiUzunDegerAl()	const 
{
	return "";
}

AY_Eklenti* IssuingDistributionPoint::kendiniKopyala() const 
{
	return (AY_Eklenti* )new IssuingDistributionPoint(*this);
}
