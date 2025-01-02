#include "SpecifiedECDomain.h"

using namespace esya;

SpecifiedECDomain::SpecifiedECDomain(void)
: mCofactorPresent(false), mHashPresent(false)
{
}

SpecifiedECDomain::SpecifiedECDomain(const QByteArray & iECParameters)
{
	constructObject(iECParameters);
}

SpecifiedECDomain::SpecifiedECDomain(const ASN1T_ALGOS_SpecifiedECDomain & iSpecifiedECDomain)
{
	copyFromASNObject(iSpecifiedECDomain);
}

SpecifiedECDomain::SpecifiedECDomain(const SpecifiedECDomain& iECParameters)
:	mFieldID(iECParameters.getFieldID()),
	mCurve(iECParameters.getCurve()),
	mBase(iECParameters.getBase()),
	mOrder(iECParameters.getOrder()),
	mCofactorPresent(iECParameters.isCofactorPresent()),
	mCofactor(iECParameters.getCofactor()),
	mHashPresent(iECParameters.isHashPresent()),
	mHash(iECParameters.getHash())
{
}

SpecifiedECDomain& SpecifiedECDomain::operator=(const SpecifiedECDomain& iECParameters)
{
	mFieldID			= iECParameters.getFieldID();
	mCurve				= iECParameters.getCurve();
	mBase				= iECParameters.getBase();
	mOrder				= iECParameters.getOrder();
	mCofactorPresent	= iECParameters.isCofactorPresent();
	mCofactor			= iECParameters.getCofactor();
	mHashPresent		= iECParameters.isCofactorPresent();
	mHash				= iECParameters.getHash();
	return * this;
}

bool esya::operator==(const SpecifiedECDomain & iRHS, const SpecifiedECDomain& iLHS)
{
	return (	(iRHS.getFieldID()	== iLHS.getFieldID())					&&
				(iRHS.getCurve()	== iLHS.getCurve())						&&
				(iRHS.getBase()		== iLHS.getBase())						&&
				(iRHS.getOrder()	== iLHS.getOrder())						&&
				(iRHS.isCofactorPresent()	== iLHS.isCofactorPresent())	&&
				(iRHS.getCofactor()			== iLHS.getCofactor())			&&				
				(iRHS.isHashPresent()		== iLHS.isHashPresent())		&&
				(iRHS.getHash()				== iLHS.getHash())								
				) ;
}

bool esya::operator!=(const SpecifiedECDomain & iRHS, const SpecifiedECDomain& iLHS)
{
	return (!(iRHS==iLHS));
}


int SpecifiedECDomain::copyFromASNObject(const ASN1T_ALGOS_SpecifiedECDomain & iECParameters) 
{
	mFieldID.copyFromASNObject(iECParameters.fieldID);
	mCurve.copyFromASNObject(iECParameters.curve);
	mVersion = iECParameters.version;
	
	if (iECParameters.base.numocts>0)
		mBase = QByteArray((const char*)iECParameters.base.data,iECParameters.base.numocts);
	
	mOrder = QString(iECParameters.order);
	
	mCofactorPresent = (iECParameters.m.cofactorPresent == 1);
	mHashPresent = (iECParameters.m.hashPresent== 1);
	if (mCofactorPresent)
		mCofactor = QString(iECParameters.cofactor);		

	if (mHashPresent)
		mHash.copyFromASNObject(iECParameters.hash);		

	return SUCCESS;
}

const FieldID &	SpecifiedECDomain::getFieldID()const 
{
	return mFieldID;
}

const Curve & SpecifiedECDomain::getCurve()const 
{
	return mCurve;
}

const QByteArray &	SpecifiedECDomain::getBase() const
{
	return mBase;
}

const QString &	SpecifiedECDomain::getOrder() const
{
	return mOrder;
}

const QString &	SpecifiedECDomain::getCofactor() const
{
	return mCofactor;
}

const AlgorithmIdentifier &	SpecifiedECDomain::getHash() const
{
	return mHash;
}

bool SpecifiedECDomain::isCofactorPresent()const
{
	return mCofactorPresent;
}

bool SpecifiedECDomain::isHashPresent()const
{
	return mHashPresent;
}



void SpecifiedECDomain::setFieldID(const FieldID & iFieldID)
{
	mFieldID = iFieldID;
}

void SpecifiedECDomain::setCurve(const Curve & iCurve)
{
	mCurve = iCurve;
}

void SpecifiedECDomain::setBase(const QByteArray & iBase)
{
	mBase = iBase;
}

void SpecifiedECDomain::setOrder(const QString & iOrder)
{
	mOrder = iOrder;

}

void SpecifiedECDomain::setCofactor(const QString & iCofactor)
{
	mCofactorPresent = true;
	mCofactor = iCofactor;
}

void SpecifiedECDomain::setHash(const AlgorithmIdentifier & iHash)
{
	mHashPresent = true;
	mHash = iHash;
}


void SpecifiedECDomain::setCofactorPresent(bool iCP)
{
	mCofactorPresent = iCP;
}

int SpecifiedECDomain::copyToASNObject(ASN1T_ALGOS_SpecifiedECDomain & oECParameters) const
{
	mFieldID.copyToASNObject(oECParameters.fieldID);
	mCurve.copyToASNObject(oECParameters.curve);
	oECParameters.version = mVersion;
	
	if (mBase.size()>0)
	{
		oECParameters.base.data		= (OSOCTET*)myStrDup(mBase.data(),mBase.size());
		oECParameters.base.numocts	= mBase.size(); 
	}	
			
	if (!mOrder.isEmpty())
		oECParameters.order = myStrDup(mOrder);

	oECParameters.m.cofactorPresent = (mCofactorPresent ? 1:0);
	oECParameters.m.hashPresent = (mHashPresent ? 1:0);
	
	if (mCofactorPresent)
	{
		oECParameters.cofactor = myStrDup(mCofactor);
	}
	
	if (mHashPresent)
	{
		mHash.copyToASNObject(oECParameters.hash);
	}

	return SUCCESS;	
}

void SpecifiedECDomain::freeASNObject(ASN1T_ALGOS_SpecifiedECDomain & oECParameters) const
{
	FieldID().freeASNObject(oECParameters.fieldID);
	Curve().freeASNObject(oECParameters.curve);

	if (oECParameters.base.numocts > 0)
	{
		DELETE_MEMORY_ARRAY(oECParameters.base.data);
	}	
	if (oECParameters.m.hashPresent> 0)
	{
		AlgorithmIdentifier().freeASNObject(oECParameters.hash);
	}	
	if (oECParameters.m.cofactorPresent> 0)
	{
		DELETE_MEMORY_ARRAY(oECParameters.cofactor);
	}	
	DELETE_MEMORY_ARRAY(oECParameters.order);
}

SpecifiedECDomain::~SpecifiedECDomain(void)
{
}