
#ifndef __SIGNERTREE__
#define __SIGNERTREE__


#include "SignerInfo.h"

namespace esya
{

	class SignerTree
	{
		QList<SignerInfo> mSigners;

	public:
		SignerTree(void);
	public:
		~SignerTree(void);

	};

}
#endif