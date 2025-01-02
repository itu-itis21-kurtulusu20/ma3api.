//
//  APDUResponse.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__APDUResponse__
#define __akisIOSCIF__APDUResponse__

#include <iostream>
#include <stdlib.h>
#include <string.h>



struct commandAttribute{
	unsigned char* value;
	int len;
};

class APDUResponse {
    
    commandAttribute m_baData; //unsigned char* m_baData;
    unsigned char m_bSw1;
    unsigned char m_bSw2;
    
    
public:
    static const int SW_LENGTH = 2;
    APDUResponse (commandAttribute baData); //unsigned char*
    APDUResponse (commandAttribute data, unsigned char sw1, unsigned char sw2); //unsigned char*
    
    commandAttribute getData(){ return m_baData; }
    unsigned char getSW1() { return m_bSw1; }
    unsigned char getSW2() { return m_bSw2; }
    
    unsigned short getStatus() { return (unsigned short) (((short) m_bSw1 << 8) + (short)m_bSw2 ); }
};

#endif /* defined(__akisCIF__APDUResponse__) */
