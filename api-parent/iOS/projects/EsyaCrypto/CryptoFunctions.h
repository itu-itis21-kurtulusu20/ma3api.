//
//  CryptoFunctions.h
//  MA3API
//
//  Created by Suleyman Uslu on 1/2/14.
//  Copyright (c) 2014 TUBITAK. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Security/Security.h>
#import <Security/SecCertificate.h>
#import <Security/SecBase.h>

@interface CryptoFunctions : NSObject

+ (BOOL)verifySignature:(NSData*)signature withCert:(NSData*)cert withData:(NSData*)data;

@end
