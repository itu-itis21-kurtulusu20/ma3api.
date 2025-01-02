//
//  AkorCppLib.h
//  AkorLib


#ifndef __AkorLib__AkorCppLib__
#define __AkorLib__AkorCppLib__

#include "ICommandTransmitter.h"

class AkorCppLib : public ICommandTransmitter
{
    void* wrapped;
    commandAttribute atr;
public:
    AkorCppLib();
    ~AkorCppLib();
    void connectCard();
    void disconnectCard();
    APDUResponse transmit(unsigned char* buf, int len);
    commandAttribute getAttribute();
};



#endif /* defined(__AkorLib__AkorCppLib__) */
