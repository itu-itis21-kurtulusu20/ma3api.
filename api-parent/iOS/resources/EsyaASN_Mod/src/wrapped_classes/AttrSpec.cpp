
#include "AttrSpec.h"
#include "ESeqOfList.h"


using namespace esya;

namespace esya
{

	AttrSpec::AttrSpec(void)
	{
	}

	AttrSpec::AttrSpec(const QByteArray & iAttrSpec)
	{
		constructObject(iAttrSpec);
	}

	AttrSpec::AttrSpec(const ASN1T_ATTRCERT_AttrSpec & iAttrSpec )
	{
		copyFromASNObject(iAttrSpec);
	}

	AttrSpec::AttrSpec(const AttrSpec& iAttrSpec)
	:	mList(iAttrSpec.getList())
	{
	}

	AttrSpec& AttrSpec::operator=(const AttrSpec& iAttrSpec)
	{
		mList	= iAttrSpec.getList();

		return *this;
	}

	bool operator==( const AttrSpec& iRHS, const AttrSpec& iLHS)
	{
		return  ( iRHS.getList()== iLHS.getList() );
	}

	bool operator!=( const AttrSpec& iRHS, const AttrSpec& iLHS)
	{
		return ( !(iRHS==iLHS) );
	}

	int AttrSpec::copyFromASNObject(const ASN1T_ATTRCERT_AttrSpec & iAttrSpec)
	{
		for (int i = 0 ; i<iAttrSpec.count ; i++	)
		{
			ASN1TObjId* pOID = (ASN1TObjId*)ESeqOfList::get(iAttrSpec,i);
			mList.append(*pOID);
		}

		return SUCCESS;
	}

	int AttrSpec::copyToASNObject(ASN1T_ATTRCERT_AttrSpec &oAttrSpec)const
	{
		oAttrSpec.count = 0;

		for (int i = 0 ; i<mList.size() ; i++	)
		{
			ASN1TObjId* pOID = new ASN1TObjId(mList[i]);
			ESeqOfList::append(oAttrSpec,pOID);
		}

		return SUCCESS;
	}

	void AttrSpec::freeASNObject(ASN1T_ATTRCERT_AttrSpec& oAttrSpec) const
	{
		ASN1C_ATTRCERT_AttrSpec cAttrSpec(oAttrSpec);
		for (int i = 0 ; i<cAttrSpec.size() ; i++)
		{
			ASN1TObjId* pOID = (ASN1TObjId*)cAttrSpec.get(i);
			DELETE_MEMORY(pOID);
		}
		ESeqOfList::free(oAttrSpec);
		return;
	}

	const QList<ASN1TObjId> & AttrSpec::getList()const
	{
		return mList;
	}	

	void AttrSpec::setList(const QList<ASN1TObjId> & iList)
	{
		mList = iList;
	}

	void AttrSpec::append(const ASN1TObjId & iElement)
	{
		mList.append(iElement);
	}

	AttrSpec::~AttrSpec(void)
	{
	}

}