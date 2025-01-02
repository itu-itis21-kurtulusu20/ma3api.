//
//  FCI.h
//  akisIOSCIF
//
//  Created by ilksen ozcan on 12/30/13.
//  Copyright (c) 2013 ilksen ozcan. All rights reserved.
//

#ifndef __akisIOSCIF__FCI__
#define __akisIOSCIF__FCI__

#include <iostream>
#include "ARR.h"
#include "FDB.h"
#include "FCITags.h"
#include "AkisException.h"
#include "CardErrorCodes.h"
#include "APDUResponse.h"

class FCI {
    CardErrorCodes cardErrorCodes;
    FCITags fciTags;
    int mFileLen;
    FDB *mFDB;
    unsigned char* mFID;
    unsigned char* mDFName;
    unsigned char* mFCI_FID;
    unsigned char mSFI;
    ARR *mAccess;
    
    commandAttribute fileProperties;
    
    
public:
	FCI();
    
	FCI(commandAttribute fciData);
    
    FCI(int mFileLen, FDB *fdb, unsigned char* fid, unsigned char* dfName, unsigned char* fci_FID, unsigned char sfi, ARR *arr);
    
	int getFileLen(){ return mFileLen; }
    void setFileLen (int fileLen) { mFileLen = fileLen;}
    
    FDB *getFDB(){ return mFDB; }
    void setFDB(FDB *fdb) { mFDB = fdb; }
    
    unsigned char* getFID() { return mFID; }
    void setFID( unsigned char* FID) { mFID=FID; }
    
    unsigned char* getDFName() { return mDFName; }
    void setDFName( unsigned char* dfName) { mDFName=dfName; }
    
    unsigned char* getFciFID() { return mFCI_FID; }
    void setFciFID( unsigned char* FciFID) { mFCI_FID = FciFID; }
    
    unsigned char getSFI(){ return mSFI;}
    void setSFI (unsigned char SFI) { mSFI = SFI; }
    
    ARR *getAccess() { return mAccess; }
    void setAccess ( ARR *access) { mAccess = access; }
    
    
    int getFileLength();
    
    commandAttribute getTagData(commandAttribute TLVData, int tag);
    
    commandAttribute getTagValue(int tag){ return getTagData(fileProperties, tag); }
    
};
#endif /* defined(__akisCIF__FCI__) */
