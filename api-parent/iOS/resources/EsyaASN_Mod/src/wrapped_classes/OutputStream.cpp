#include "OutputStream.h"

using namespace esya;

OutputStream::OutputStream()
:	pIODevice(NULL),
	pOSCOut(NULL),
	mType(QIODEVICE)
{

}


OutputStream::OutputStream(QIODevice * iIODevice)
:	pIODevice(iIODevice),
	pOSCOut(NULL),
	mType(QIODEVICE)
{
	
}

OutputStream::OutputStream(OutputStream& iOS)
:	pIODevice(iOS.getIODevice()),
	pOSCOut(iOS.getOSCOut()),
	mType(iOS.getType())
{

}


OutputStream::OutputStream(OSRTOutputStream *iOSCOut)
:	pIODevice(NULL), 
	pOSCOut(iOSCOut), 
	mType(OSCOUTPUTSTREAM)
{
}


OutputStream & OutputStream::operator= (OutputStream& iOS)
{
	pIODevice	= iOS.getIODevice();
	pOSCOut		= iOS.getOSCOut();
	mType		= iOS.getType();
	return *this;
}

int OutputStream::write(const char* data , const int len )
{
	switch (mType)
	{
	case QIODEVICE : 
		{
			if (!pIODevice) throw EException(ERR_NULL_STREAM,__FILE__,__LINE__);
			return pIODevice->write(data,len);
			break;
					 } 
	case OSCOUTPUTSTREAM : 
		{
			if (!pOSCOut) throw EException(ERR_NULL_STREAM,__FILE__,__LINE__);
			return pOSCOut->write((ASN1OCTET*)data,len);
			break;
		}
	}
}

int OutputStream::write(const QByteArray iData)
{
	return write(iData.constData(),iData.size());
}


QIODevice* OutputStream::getIODevice()
{
	return pIODevice;
}

OSRTOutputStream* OutputStream::getOSCOut()
{
	return pOSCOut;
}



OutputStream::OS_Type OutputStream::getType() const
{
	return mType;
}



void OutputStream::releaseStream()
{
	DELETE_MEMORY(pIODevice)
	DELETE_MEMORY(pOSCOut)
}


OutputStream::~OutputStream(void)
{
}
