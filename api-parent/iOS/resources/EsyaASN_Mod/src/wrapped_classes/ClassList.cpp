
#include "ClassList.h"


using namespace esya;

namespace esya
{

ClassList::ClassList(void)
: mNumBits(0)
{
}

ClassList::ClassList(const QByteArray & iClassList)
{
	constructObject(iClassList);
}

ClassList::ClassList(const ASN1T_ATTRCERT_ClassList & iClassList )
{
	copyFromASNObject(iClassList);
}

ClassList::ClassList(const ClassList& iClassList)
:	mNumBits(iClassList.getNumBits()),
	mData(iClassList.getData())
{
}

ClassList& ClassList::operator=(const ClassList& iClassList)
{
	mNumBits	= iClassList.getNumBits();
	mData		= iClassList.getData();

	return *this;
}

bool operator==( const ClassList& iRHS, const ClassList& iLHS)
{
	return  (	( iRHS.getNumBits()	== iLHS.getNumBits() ) &&
				( iRHS.getData()	== iLHS.getData() )	); 
}

bool operator!=( const ClassList& iRHS, const ClassList& iLHS)
{
	return ( !(iRHS==iLHS) );
}

int ClassList::copyFromASNObject(const ASN1T_ATTRCERT_ClassList & iClassList)
{
	mNumBits = iClassList.numbits;	
	mData	= iClassList.data[0];

	return SUCCESS;
}

int ClassList::copyToASNObject(ASN1T_ATTRCERT_ClassList &oClassList)const
{
	oClassList.numbits = mNumBits;	
	oClassList.data[0] = mData;

	return SUCCESS;
}

void ClassList::freeASNObject(ASN1T_ATTRCERT_ClassList& oClassList)const
{
	return;
}

int	ClassList::getNumBits()const
{
	return mNumBits;
}

unsigned char ClassList::getData()const
{
	return mData;
}

void ClassList::setNumBits(int iNumBits)
{
	mNumBits = iNumBits;
}

void ClassList::setData(unsigned char iData)
{
	mData = iData;
}

ClassList::~ClassList(void)
{
}

}