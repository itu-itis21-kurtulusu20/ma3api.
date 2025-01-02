//
//  AkisException.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__AkisException__
#define __akisIOSCIF__AkisException__

#include <iostream>
#include "CardErrorMsg.h"

class AkisException {
    
    int akisErrCode;
    CardErrorMessage cardErrorMessage;
    
public:
    
    AkisException(const int code){ akisErrCode = code;}
    
    int GetErrorCode() { return akisErrCode; }
    
    
    char* ToString();
    
    
};
#endif /* defined(__akisIOSCIF__AkisException__) */
