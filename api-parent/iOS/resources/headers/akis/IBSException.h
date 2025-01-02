//
//  IBSException.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__IBSException__
#define __akisIOSCIF__IBSException__

#include <iostream>
#include <string>

class IBSException
{
    int errorNo;
    const std::string message;
public:
    IBSException(int errNo, const std::string& msg);
    const std::string  getMessage() const;
    
};
#endif /* defined(__akisIOSCIF__IBSException__) */
