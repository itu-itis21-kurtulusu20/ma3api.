//
//  Pin.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__Pin__
#define __akisIOSCIF__Pin__

#include <iostream>
#include "SdoHeader.h"
#include "Tags.h"
#include "AkisException.h"
#include "CardErrorCodes.h"

class Pin {
    static CardErrorCodes cardErrorCodes;
    Tags tags;
    SdoHeader mHeader;
    unsigned char mMinPinLen;
    unsigned char mMaxPinLen;
    char* mPin;
    char* mPen;
    
public:
    SdoHeader getHeader(){ return mHeader; }
    void setHeader(SdoHeader header) { mHeader = header; }
    
    Pin();
    
    Pin (SdoHeader header, unsigned char minPinLen, unsigned char maxPinLen, char* pin, char* pen);
    
    unsigned char* SDOUpdate();
    
    unsigned char* CreateSDOHeaderList (bool op);
    
    void CreateKeyFromSDO(unsigned char* sdo);
    
    unsigned char* UpdatePin(unsigned char pinNo, unsigned char* newPin);
    
};
#endif /* defined(__akisIOSCIF__Pin__) */
