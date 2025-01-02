#include "BasicConstraints.h"
#include "OrtakDil.h"

using namespace esya;

BasicConstraints::BasicConstraints(void)
{
}

BasicConstraints::BasicConstraints(const BasicConstraints & iBC)
:	mPathLenConstraint(iBC.getPathLenConstraint()),
	mPathLenConstraintPresent(iBC.getPathLenConstraintPresent()),
	mCA(iBC.getCA())
{
}

BasicConstraints::BasicConstraints(const ASN1T_IMP_BasicConstraints & iBC )
{
	copyFromASNObject(iBC);
}

BasicConstraints::BasicConstraints(const QByteArray & iBC)
{
	constructObject(iBC);
}

BasicConstraints::BasicConstraints(const bool &iPLCP, const OSBOOL &iCA, const OSUINT32 &iPLC)
{
	mPathLenConstraintPresent	= iPLCP;
	mPathLenConstraint			= iPLC;
	mCA							= iCA;
}


BasicConstraints& BasicConstraints::operator=(const BasicConstraints &iBC)
{
	mPathLenConstraint = iBC.getPathLenConstraint();
	mPathLenConstraintPresent = iBC.getPathLenConstraintPresent();
	mCA = iBC.getCA();
	return (*this);
}

bool esya::operator==(const BasicConstraints& iRHS, const BasicConstraints& iLHS)
{
	return	(	( iRHS.getPathLenConstraintPresent() == iLHS.getPathLenConstraintPresent()) && 
				( iRHS.getCA() == iLHS.getCA()) && 
				( !iRHS.getPathLenConstraintPresent()|| ( iRHS.getPathLenConstraint() == iLHS.getPathLenConstraint() ) ) 
			);
}

bool esya::operator!=(const BasicConstraints& iRHS, const BasicConstraints& iLHS)
{
	return ( !( iRHS == iLHS ) ) ;
}

int BasicConstraints::copyFromASNObject(const ASN1T_IMP_BasicConstraints& iBC)
{
	mCA = iBC.cA;
	mPathLenConstraintPresent = iBC.m.pathLenConstraintPresent;
	if ( mPathLenConstraintPresent )
		mPathLenConstraint = iBC.pathLenConstraint;
	return SUCCESS;
}

int BasicConstraints::copyToASNObject(ASN1T_IMP_BasicConstraints & oBC) const
{
	oBC.cA = mCA;
	oBC.m.pathLenConstraintPresent = mPathLenConstraintPresent;
	if (oBC.m.pathLenConstraintPresent)
		oBC.pathLenConstraint = mPathLenConstraint;
	return SUCCESS;
}

void BasicConstraints::freeASNObject(ASN1T_IMP_BasicConstraints & oBC)const
{
	/* NOTHING TO FREE */
	return ;
}

const bool	& BasicConstraints::getPathLenConstraintPresent()const
{
	return mPathLenConstraintPresent;
}

const OSBOOL & BasicConstraints::getCA()const
{
	return mCA;
}

const OSUINT32 & BasicConstraints::getPathLenConstraint()const
{
	return mPathLenConstraint;
}

BasicConstraints::~BasicConstraints(void)
{
}

QStringList BasicConstraints::toStringList() const
{
	QString lBasicConstStr="";
	QStringList lBasicConstList;

	lBasicConstStr = lBasicConstStr + DIL_EXT_OZNE_TURU+" :";
	lBasicConstStr += ( mCA ? DIL_EXT_YAYINCI:DIL_EXT_KULLANICI ) ;
	lBasicConstList.append(lBasicConstStr);

	lBasicConstStr =  DIL_EXT_MESAFE_KISITLAMASI + " :";
	lBasicConstStr += (mPathLenConstraintPresent ? QString().setNum(mPathLenConstraint) : DIL_GNL_YOK );
	lBasicConstList.append(lBasicConstStr);

	return lBasicConstList;
}


/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString BasicConstraints::eklentiAdiAl() const 
{
	return DIL_EXT_TEMEL_KISITLAR;
}

QString BasicConstraints::eklentiKisaDegerAl()	const 
{
	return toStringList().join(",");
}

QString BasicConstraints::eklentiUzunDegerAl()	const 
{
	return toStringList().join("\n");
}

AY_Eklenti* BasicConstraints::kendiniKopyala() const 
{
	return (AY_Eklenti* )new BasicConstraints(*this);
}
