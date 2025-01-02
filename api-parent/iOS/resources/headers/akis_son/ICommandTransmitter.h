//
//  ICommandTransmitter.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__ICommandTransmitter__
#define __akisIOSCIF__ICommandTransmitter__

#include "APDUResponse.h"

class ICommandTransmitter {
    
public:
    
    ICommandTransmitter();
    virtual void connectCard()=0;
    virtual void disconnectCard()=0;
    virtual APDUResponse transmit(unsigned char* buf, int len)=0;
    virtual commandAttribute getAttribute() = 0;

};
#endif /* defined(__akisIOSCIF__ICommandTransmitter__) */
