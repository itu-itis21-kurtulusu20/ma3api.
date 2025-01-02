//
//  ARR.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__ARR__
#define __akisIOSCIF__ARR__

#include <iostream>
#include <stdlib.h>
#include <string.h>
#include "AkisException.h"
#include "CardErrorCodes.h"
#include "Tags.h"
#include "APDUResponse.h"



class ARR {
    CardErrorCodes cardErrorCodes;
    Tags tags;
    unsigned char tag;
    unsigned char* fidCB;
    unsigned char* fidCL;
    unsigned char recordNumberCB;
    unsigned char recordNumberCL;
    
public:
    unsigned char* getContactBasedFID(){ return fidCB; }
    
    unsigned char getContactBasedRecordNo() { return recordNumberCB; }
    
    ARR();
    
    ARR(commandAttribute accessTLVData);
    
    ARR(unsigned char* mFidCB, unsigned char mRecordNumberCB, unsigned char* mFidCL, unsigned char mRecordNumberCL);
    
    unsigned char* ToByteArray();
    
};
#endif /* defined(__akisIOSCIF__ARR__) */
