//
//  CIFFactory.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__CIFFactory__
#define __akisIOSCIF__CIFFactory__

#include <iostream>
#include "AbstractAkisCommands.h"
#include "ICommandTransmitter.h"
#include "CardErrorCodes.h"
#include "AkisVersion.h"
#include "CommandsV20.h"
#include "CommandsV12.h"

class CIFFactory{
    CardErrorCodes cardErrorCodes;
    CIFFactory();
    static bool instanceFlag;
    static CIFFactory *instance;
    AkisVersion akisVersion;
    
public:
    static CIFFactory *getInstance();
    AbstractAkisCommands *getAkisCommand(ICommandTransmitter &itrans);
    static unsigned char *GetHistorical(commandAttribute atr);
};

#endif



