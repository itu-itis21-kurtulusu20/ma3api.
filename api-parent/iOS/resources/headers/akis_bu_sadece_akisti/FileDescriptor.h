//
//  FileDescriptor.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__FileDescriptor__
#define __akisIOSCIF__FileDescriptor__

#include <iostream>
class FileDescriptor {
    
public:
    static const int DedicatedFile = 0x38;
    static const int Binary = 0x01;
    static const int FixedRecord = 0x2;
    static const int VarRecord = 0x4;
    static const int FixedCycRecord = 0x6;
    
    
};
#endif /* defined(__akisIOSCIF__FileDescriptor__) */
