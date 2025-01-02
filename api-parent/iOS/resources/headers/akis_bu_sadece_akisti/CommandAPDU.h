//
//  CommandAPDU.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__CommandAPDU__
#define __akisIOSCIF__CommandAPDU__

#include <iostream>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "APDUParam.h"
#include "AkisException.h"
#include "CardErrorCodes.h"

class CommandAPDU {
    CardErrorCodes cardErrorCodes;
    char* apdu;
    int length;
    unsigned char m_bCla;
    unsigned char m_bIns;
    unsigned char m_bP1;
    unsigned char m_bP2;
    unsigned char* m_baData;
    unsigned char m_bLe;
    
public:
    
    static const int APDU_MIN_LENGTH = 4;
    static const int APDU_MAX_LENGTH = 0xc4;
    static const int iClass = 0;
    static const int iINS = 1;
    static const int iP1 = 2;
    static const int iP2 = 3;
    static const int iLC = 4;
    static const int iDATAPOS = 5;
    
    CommandAPDU(unsigned char bCla, unsigned char bIns, unsigned char bP1, unsigned char bP2, unsigned char baData[], unsigned char bLe);
    
    CommandAPDU(const char *command , int len);
    
    CommandAPDU();
    
    void Update(APDUParam apduParam);
    
    char* getCommand() const { return apdu; }
    int getCommandLen() const { return length; }
    
};

#endif /* defined(__akisIOSCIF__CommandAPDU__) */
