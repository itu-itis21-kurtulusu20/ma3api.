#ifndef __ESEQOFLIST__
#define __ESEQOFLIST__

#include "Explicit.h"
#include <QString>


namespace esya
{

	class Q_DECL_EXPORT ESeqOfList
	{
	public:
		static const void * get(const ASN1TPDUSeqOfList & list, int index);
		static void append(ASN1TPDUSeqOfList & list, const void * node );
		static void free(ASN1TPDUSeqOfList & list);
	public:
		virtual ~ESeqOfList(void);
	};

}

#endif 

