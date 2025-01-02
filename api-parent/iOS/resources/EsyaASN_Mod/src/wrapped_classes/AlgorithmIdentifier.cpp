#include "AlgorithmIdentifier.h"

using namespace esya;

AlgorithmIdentifier::AlgorithmIdentifier(void)
: mParameters(QByteArray(NULL_PARAMS,2)),mParametersPresent(true)
{
	mAlgorithm.numids = 0;
}

AlgorithmIdentifier::AlgorithmIdentifier(const QByteArray & iAlgorithmIdentifier)
: mParameters(QByteArray(NULL_PARAMS,2)),mParametersPresent(true)
{
	constructObject(iAlgorithmIdentifier);
}


AlgorithmIdentifier::AlgorithmIdentifier(const QByteArray & iParameters, const ASN1TObjId &iAlgorithm)
: mParameters(iParameters),mAlgorithm(iAlgorithm)
{
	mParametersPresent = !mParameters.isEmpty();
}

AlgorithmIdentifier::AlgorithmIdentifier(const ASN1T_EXP_AlgorithmIdentifier & iAlgorithmIdentifier)
: mParameters(QByteArray(NULL_PARAMS,2)),mParametersPresent(true)
{
	copyFromASNObject(iAlgorithmIdentifier);
}

AlgorithmIdentifier::AlgorithmIdentifier(const ASN1TObjId & iAlgorithm)
: mAlgorithm(iAlgorithm),mParametersPresent(false)
{

}

AlgorithmIdentifier::AlgorithmIdentifier(const AlgorithmIdentifier& iAlgorithmIdentifier)
:mParameters(iAlgorithmIdentifier.getParameters()),mAlgorithm(iAlgorithmIdentifier.getAlgorithm()),mParametersPresent(iAlgorithmIdentifier.mParametersPresent)
{
}

AlgorithmIdentifier& AlgorithmIdentifier::operator=(const AlgorithmIdentifier& iAlgorithmIdentifier)
{
	mParameters	= iAlgorithmIdentifier.getParameters();
	mAlgorithm	= iAlgorithmIdentifier.getAlgorithm();
	mParametersPresent = iAlgorithmIdentifier.mParametersPresent;
	return *this;
}


bool esya::operator==(const AlgorithmIdentifier & iRHS, const AlgorithmIdentifier& iLHS)
{
	return ( iRHS.getAlgorithm() == iLHS.getAlgorithm() && iRHS.getParameters()==iRHS.getParameters() );
}

bool esya::operator!=(const AlgorithmIdentifier & iRHS, const AlgorithmIdentifier& iLHS)
{
	return ( ! ( iRHS == iLHS ));
}


int AlgorithmIdentifier::copyFromASNObject(const ASN1T_EXP_AlgorithmIdentifier & iAlgorithmIdentifier) 
{
	mAlgorithm = iAlgorithmIdentifier.algorithm;
	mParametersPresent = (iAlgorithmIdentifier.m.parametersPresent == 1);

	if (iAlgorithmIdentifier.m.parametersPresent)
	{
		mParameters = QByteArray((char*)iAlgorithmIdentifier.parameters.data,iAlgorithmIdentifier.parameters.numocts);
	}

	return SUCCESS;
}


const QByteArray &AlgorithmIdentifier::getParameters()const 
{
	return mParameters;
}

const ASN1TObjId &AlgorithmIdentifier::getAlgorithm() const
{
	return mAlgorithm;
}

bool AlgorithmIdentifier::isParametersPresent() const
{
	QByteArray nullparams(NULL_PARAMS);
	return (mParametersPresent && !mParameters.isEmpty() && (mParameters!= nullparams ) );
}


QByteArray AlgorithmIdentifier::getParamsAsOctets()const
{
	return ASNUtils::decodeOctetString(mParameters);
}

void AlgorithmIdentifier::setParamsAsOctets(const QByteArray& iOctets)
{
	mParametersPresent = true;
	mParameters = ASNUtils::encodeOctetString(iOctets);
}

void AlgorithmIdentifier::setParameters(const QByteArray& iParams)
{
	mParametersPresent = true;
	mParameters = iParams;	
}

int AlgorithmIdentifier::copyToASNObject(ASN1T_EXP_AlgorithmIdentifier & oAlgorithmIdentifier) const 
{
	oAlgorithmIdentifier.algorithm = mAlgorithm;
	oAlgorithmIdentifier.m.parametersPresent = mParametersPresent ? 1:0;
	if (oAlgorithmIdentifier.m.parametersPresent)
	{
		oAlgorithmIdentifier.parameters.data = (OSOCTET*)myStrDup(mParameters.data(),mParameters.size());
		oAlgorithmIdentifier.parameters.numocts = mParameters.size(); 
	}
	return SUCCESS;
}

void AlgorithmIdentifier::freeASNObject(ASN1T_EXP_AlgorithmIdentifier & oAlgorithmIdentifier)const
{
	if (oAlgorithmIdentifier.m.parametersPresent && oAlgorithmIdentifier.parameters.numocts>0&&oAlgorithmIdentifier.parameters.data)
	{
		DELETE_MEMORY_ARRAY(oAlgorithmIdentifier.parameters.data);
		oAlgorithmIdentifier.parameters.numocts = 0;
	}
}

int AlgorithmIdentifier::copyAIs(const ASN1TPDUSeqOfList & iAIs, QList<AlgorithmIdentifier>& oList)
{
	return copyASNObjects<AlgorithmIdentifier>(iAIs,oList);
}

int AlgorithmIdentifier::copyAIs(const QByteArray&  iASNBytes,  QList<AlgorithmIdentifier> & oList )
{
	return copyASNObjects<ASN1T_PKCS7_DigestAlgorithmIdentifiers,ASN1C_PKCS7_DigestAlgorithmIdentifiers,AlgorithmIdentifier>(iASNBytes,oList);
}


int AlgorithmIdentifier::copyAIs(const QList<AlgorithmIdentifier> & iList ,QByteArray& oASNBytes)
{
	return copyASNObjects<ASN1T_PKCS7_DigestAlgorithmIdentifiers,ASN1C_PKCS7_DigestAlgorithmIdentifiers,AlgorithmIdentifier>(iList,oASNBytes);
}



int AlgorithmIdentifier::copyAIs(const QList<AlgorithmIdentifier> & iList ,ASN1TPDUSeqOfList& oAIs)
{
	return copyASNObjects<AlgorithmIdentifier>(iList,oAIs);
}

bool AlgorithmIdentifier::isNull() const
{
	return (mAlgorithm.numids == 0 );
}

AlgorithmIdentifier::~AlgorithmIdentifier(void)
{
}
