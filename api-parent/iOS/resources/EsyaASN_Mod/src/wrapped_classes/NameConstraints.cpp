#include "NameConstraints.h"
#include "OrtakDil.h"

using namespace esya;

NameConstraints::NameConstraints(void)
{
}

NameConstraints::NameConstraints(const NameConstraints & iNC)
:	mPSTPresent(iNC.isPSTPresent()),
	mPST(iNC.getPST()),
	mESTPresent(iNC.isESTPresent()),
	mEST(iNC.getEST())
{
}

NameConstraints::NameConstraints(const ASN1T_IMP_NameConstraints & iNC)
{
	copyFromASNObject(iNC);
}

NameConstraints::NameConstraints(const QByteArray & iNC)
{
	constructObject(iNC);
}

NameConstraints& NameConstraints::operator=(const NameConstraints&iNC)
{
	mPSTPresent = iNC.isPSTPresent();
	if (mPSTPresent)
		mPST		= iNC.getPST();
	mESTPresent	= iNC.isESTPresent();
	if (mESTPresent)
		mEST		= iNC.getEST();

	return (*this);
}

bool esya::operator==(const NameConstraints& iRHS, const NameConstraints& iLHS)
{
	if ( iRHS.isPSTPresent() != iLHS.isPSTPresent() || iRHS.isESTPresent() != iRHS.isESTPresent() )
		return false;

	if ( iRHS.isPSTPresent() )
	{
		const QList<GeneralSubtree> &rList = iRHS.getPST();
		const QList<GeneralSubtree> &lList = iLHS.getPST();
		if ( rList.size() != lList.size() ) return false;
		for (int i = 0 ; i < rList.size() ; i++ )
		{
			if ( rList[i] != lList[i] )
				return false;
		}
	}
	if ( iRHS.isESTPresent() )
	{
		const QList<GeneralSubtree> &rList = iRHS.getEST();
		const QList<GeneralSubtree> &lList = iLHS.getEST();
		if ( rList.size() != lList.size() ) return false;
		for (int i = 0 ; i < rList.size() ; i++ )
		{
			if ( rList[i] != lList[i] )
				return false;
		}
	}
	return true;
}

bool esya::operator!=(const NameConstraints& iRHS, const NameConstraints& iLHS)
{
	return ( !( iRHS == iLHS ) );
}


int NameConstraints::copyFromASNObject(const ASN1T_IMP_NameConstraints& iNC)
{
	mPSTPresent = iNC.m.permittedSubtreesPresent;
	if (mPSTPresent)
		GeneralSubtree().copyGSTs(iNC.permittedSubtrees,mPST);
	mESTPresent	= iNC.m.excludedSubtreesPresent;
	if (mESTPresent)
		GeneralSubtree().copyGSTs(iNC.excludedSubtrees,mEST);

	return SUCCESS;	
}

int NameConstraints::copyToASNObject(ASN1T_IMP_NameConstraints & oNC) const
{
	oNC.m.permittedSubtreesPresent = mPSTPresent;
	if (mPSTPresent)
		GeneralSubtree().copyGSTs(mPST,oNC.permittedSubtrees);
	oNC.m.excludedSubtreesPresent = mESTPresent;
	if (mESTPresent)
		GeneralSubtree().copyGSTs(mEST,oNC.excludedSubtrees);

	return SUCCESS;	
}

void NameConstraints::freeASNObject(ASN1T_IMP_NameConstraints & oNC)const
{
	if (oNC.m.permittedSubtreesPresent)
		GeneralSubtree().freeASNObjects(oNC.permittedSubtrees);
	
	if (oNC.m.excludedSubtreesPresent)
		GeneralSubtree().freeASNObjects(oNC.excludedSubtrees);
}

const bool &NameConstraints::isPSTPresent()const 
{
	return mPSTPresent;
}

const bool &NameConstraints::isESTPresent()const 
{
	return mESTPresent;
}

const QList<GeneralSubtree> &NameConstraints::getPST()const
{
	return mPST;
}

const QList<GeneralSubtree> &NameConstraints::getEST()const
{
	return mEST;
}

NameConstraints::~NameConstraints(void)
{
}

/************************************************************************/
/*					AY_EKLENTI FONKSIYONLARI                            */
/************************************************************************/

QString NameConstraints::eklentiAdiAl() const 
{
	return DIL_EXT_ISIM_KISITLAMALARI;
}

QString NameConstraints::eklentiKisaDegerAl()	const 
{
	return "";
}

QString NameConstraints::eklentiUzunDegerAl()	const 
{
	return "";
}

AY_Eklenti* NameConstraints::kendiniKopyala() const 
{
	return (AY_Eklenti* )new NameConstraints(*this);
}
