//
//  Algorithm.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__Algorithm__
#define __akisIOSCIF__Algorithm__

#include <iostream>
class Algorithm {
    
    
public:
    static const int PKCS_PSS = 1;
    static const int ISO_9796_2_S_1 = 2;
    static const int ISO_9796_2_S_2 = 3;
    static const int PKCS_1_5 = 4;
    static const int PKCS_2_1 = 5;
    static const int OAEP = 6;
    static const int SHA_1 = 7;
    static const int SHA_256 = 8;
};
#endif /* defined(__akisIOSCIF__Algorithm__) */
