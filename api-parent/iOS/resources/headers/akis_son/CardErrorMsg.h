//
//  CardErrorMsg.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__CardErrorMsg__
#define __akisIOSCIF__CardErrorMsg__

#include <iostream>
#include <stdlib.h>
#include <string.h>
#include "CardErrorCodes.h"

class CardErrorMessage {
    
    CardErrorCodes cardErrorCodes;
    
    
public:
    
    CardErrorMessage();
    
    static struct ErrorCode{
        int errCode;
        char errMsg[60];
    }errorTable[84];
    
    
    static char* GetAkisErrorMessage(int code);
    
};
#endif /* defined(__akisIOSCIF__CardErrorMsg__) */
