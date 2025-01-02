#include "IMapper.h"

IMapper::IMapper(QObject *parent)
	: QObject(parent)
{

}

IMapper::IMapper(const IMapper &)
{	
}

IMapper & IMapper::operator =(const IMapper &)
{
	return *this;
}

IMapper::~IMapper()
{

}
