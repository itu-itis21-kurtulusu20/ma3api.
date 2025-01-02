#ifndef __BLOKISLEYICI__
#define __BLOKISLEYICI__

#include <QByteArray>

namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* Blok iþleyici arayüzü
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT BlokIsleyici
	{
	public:
		virtual void blokIsle(const QByteArray & iBlock)= 0;
	public:		
	};
}

#endif

