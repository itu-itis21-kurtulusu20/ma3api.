#ifndef __NAMEUTILS__
#define __NAMEUTILS__

#include "Name.h"

namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* Name sýnýfý ile ilgili bazý metodlarý içeren bir utility sýnýfý
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT NameUtils
	{
	public:

		static const QString SPECIAL_CHARS ;

		static bool isEqual(const Name & iRHS, const Name & iLHS );
		static bool isEqual(const RelativeDistinguishedName & iRHS, const RelativeDistinguishedName & iLHS );
		static bool isEqual(const AttributeTypeAndValue & iRHS, const AttributeTypeAndValue & iLHS );

		
		static bool isSubNameOf(const QString & iName,const QString & iBaseName,const QString & iSeperator=".");
		static bool isSubURINameOf(const QString & iURIName, const QString & iBaseURIName);

		static bool validateEmailAddress(const QString & iEmailAddress);
	
	public:

	};

}
#endif

