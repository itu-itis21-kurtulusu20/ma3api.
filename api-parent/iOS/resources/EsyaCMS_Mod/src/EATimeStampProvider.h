#ifndef EATIMESTAMPPROVIDER_H
#define EATIMESTAMPPROVIDER_H

#include <QByteArray>

namespace esya
{
	class EATimeStampProvider 
	{
	public:
		virtual ~EATimeStampProvider() {};

		virtual QByteArray getTimeStamp(const QByteArray& ) = 0;

	private:

	};

}


#endif // EATIMESTAMPPROVIDER_H
