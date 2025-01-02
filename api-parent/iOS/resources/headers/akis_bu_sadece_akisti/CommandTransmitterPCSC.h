//
//  CommandTransmitterPCSC.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__CommandTransmitterPCSC__
#define __akisIOSCIF__CommandTransmitterPCSC__


#include <iostream>
#include "ICommandTransmitter.h"
#include "winscard.h"
#include "wintypes.h"

class CommandTransmitterPCSC: public ICommandTransmitter{
    int m_nLastError = 0;
    commandAttribute atr;
    
	SCARDHANDLE hCard;
	SCARDCONTEXT hContext;
	DWORD currentProto;
    
    LPTSTR mszReaders;
    DWORD dwReaders;
public:
	CommandTransmitterPCSC();
    void connectCard();
    void disconnectCard();
    APDUResponse transmit(unsigned char* buf, int len);
    void releaseContext();
    commandAttribute getAttribute();
};
#endif /* defined(__akisIOSCIF__CommandTransmitterPCSC__) */
