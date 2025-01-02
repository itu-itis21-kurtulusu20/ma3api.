//
//  Tags.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__Tags__
#define __akisIOSCIF__Tags__

#include <iostream>
class Tags {
    
    
public:
    static const int HeaderList = 0x4D;
    static const int Key = 0xBF;
    static const int SymmKey = 0x8A;
    static const int AsymmPriKey = 0x90;
    static const int AsymmPubKey = 0xA0;
    static const int PinObj = 0x81;
    static const int KeyHeader = 0xA0;
    static const int ObjName = 0x84;
    static  const int MaxTryCount = 0x9A;
    static const int RemTryCount = 0x9B;
    static const int MaxUsageCount = 0x9C;
    static const int RemUsageCount = 0x9D;
    static const int NonRepFlag = 0x9E;
    static const int ObjLen = 0x80;
    static  const int Access = 0xA1;
    static  const int ContactAccess = 0xCB;
    static  const int ContactlessAccess = 0xCC;
    static  const int BerTLVInfo = 0xA5;
    static const int Info = 0x85;
    static const int Pin = 0x41;
    static  const int RSAPriKey = 0x48;
    static  const int RSAPubKey = 0x49;
    static const int RSA = 0x7F;
    static  const int P = 0x92;
    static  const int Q = 0x93;
    static  const int QInv = 0x94;
    static const int DP = 0x95;
    static const int DQ = 0x96;
    static const int N = 0x81;
    static  const int E = 0x82;
    static const int CHTag = 0x5F;
    static const int CHR = 0x20;
    static const int CHA = 0x4C;
    static const int SymDesKey = 0xA2;
    static const int KMac = 0x90;
    static const int KEnc = 0x91;
    static  const int MaxPinLen = 0x80;
    static  const int MinPinLen = 0x81;
    static  const int PinVal = 0x82;
    static  const int Pen = 0xC0;
    
};

#endif /* defined(__akisIOSCIF__Tags__) */
