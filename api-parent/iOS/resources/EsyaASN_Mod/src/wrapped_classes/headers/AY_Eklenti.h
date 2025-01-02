#ifndef __AY_EKLENTI__
#define __AY_EKLENTI__

#include <QString>
#include <QByteArray>


namespace esya
{
	/**
	* \ingroup EsyaASN
	* 
	* Eklenti nesneleri arayüzü
	*
	*
	* \author dindaro
	*
	*/
	class Q_DECL_EXPORT AY_Eklenti 
	{

	public:

		virtual QString eklentiAdiAl()			const = 0;
		virtual QString eklentiKisaDegerAl()	const = 0;
		virtual QString eklentiUzunDegerAl()	const = 0;
	
		virtual AY_Eklenti* kendiniKopyala() const = 0;

	public:

		virtual ~AY_Eklenti(){};
	};

}
#endif

