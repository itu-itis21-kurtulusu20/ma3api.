//
//  FDB.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__FDB__
#define __akisIOSCIF__FDB__

#include <iostream>
#include <string.h>
#include "FileDescriptor.h"
#include "AkisException.h"
#include "CardErrorCodes.h"
#include <APDUResponse.h>

class FDB {
    
    CardErrorCodes cardErrorCodes;
    FileDescriptor fileDescriptor;
    int _fileType;
    
public:
    unsigned char dataCodingByte;
    int recordLength;
    int recordNumber;
    
    FDB();
    
    FDB(int fileType);
    
    FDB(int fileType, int recLength);
    
    FDB(int fileType, int recLength, int recNumber);
    
    FDB(commandAttribute data);
    
    int getFileType(){ return _fileType; }
    
    unsigned char* ToByteArray();
};
#endif /* defined(__akisIOSCIF__FDB__) */
