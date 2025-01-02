//
//  SdoHeader.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__SdoHeader__
#define __akisIOSCIF__SdoHeader__

#include <iostream>
#include "ARR.h"
#include "Tags.h"
#include "AkisException.h"
#include "CardErrorCodes.h"

class SdoHeader {
    CardErrorCodes cardErrorCodes;
    unsigned char mKeyID;
    char* mName;
    int mMaxTryCount;
    int mRemTryCount;
    int mMaxUsageCount;
    int mRemUsageCount;
    int mNonRepFlag;
    int mLen;
    ARR *mAccess;
    unsigned char* mBerTLVInfo;
    unsigned char* mInfo;
    
public:
    static int RSAPrivate;
    
    static int RSAPublic;
    
    static int Symmetric;
    
    static int PinType;
    
    char* getName(){ return mName; }
    void setName(char* name){ mName = name; }
    
    unsigned char getkeyID(){ return mKeyID; }
    void setkeyID (unsigned char keyID){ mKeyID=keyID; }
    
    int getMaxTryCount(){ return mMaxTryCount; }
    void setMaxTryCount(int maxTryCount){ mMaxTryCount = maxTryCount; }
    
    int getRemTryCount(){ return mRemTryCount;}
    void setRemTryCount (int remTryCount){ mRemTryCount=remTryCount; }
    
    int getMaxUsageCount(){ return mMaxUsageCount; }
    void setMaxUsageCount(int maxUsageCount){ mMaxUsageCount = maxUsageCount; }
    
    int getRemUsageCount(){ return mRemUsageCount;}
    void setRemUsageCount (int remUsageCount){ mRemUsageCount=remUsageCount; }
    
    int getNonRepFlag () { return mNonRepFlag; }
    void setNonRepFlag( int nonRepFlag) { mNonRepFlag = nonRepFlag; }
    
    int getLen() { return mLen; }
    void setLen( int len) { mLen = len; }
    
    ARR *getAccess() { return mAccess; }
    void setAccess( ARR *access ) { mAccess = access; }
    
    unsigned char* getBerTLVInfo() { return mBerTLVInfo; }
    void setBerTLVInfo( unsigned char* berTLVInfo ) { mBerTLVInfo = berTLVInfo; }
    
    unsigned char* getInfo(){ return mInfo; }
    void setInfo ( unsigned char* info) { mInfo = info; }
    
    void createHeaderFromSDO (unsigned char* sdo);
    
    SdoHeader();
    
    SdoHeader(unsigned char ID, char* Name, int MaxTryCount, int MaxUsageCount, int NonRepFlag, int Len, ARR *Access, unsigned char* BerTLVInfo, unsigned char* Info);
};
#endif /* defined(__akisIOSCIF__SdoHeader__) */
