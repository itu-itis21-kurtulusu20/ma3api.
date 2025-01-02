//
//  ObjBESSigner.m
//  MA3API
//
//  Created by Suleyman Uslu on 1/2/14.
//  Copyright (c) 2014 TUBITAK. All rights reserved.
//

#import "ObjBESSigner.h"

#import "ObjSmartCardManager.h"
#import "ObjMainWindow.h"

#include "SmartCardManager.h"
#include "CMSContainer.h"
#include "SignableBytes.h"
#include "SmartCardException.h"
#include "CMSException.h"
#include "Logger.h"
#include "UIException.h"

@interface ObjBESSigner()

@property (nonatomic) MainWindow* window;

@end

@implementation ObjBESSigner

/**
 * This functions is called when you press sign button
 * It adds an observer which listens the login
 * process to smartcard and detaches a new thread
 * to login process.
 */
-(void)signBES:(MainWindow*)window {
    
    self.window = window;
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(afterLogin:) name:@"loginnotify" object:nil];
    [NSThread detachNewThreadSelector:@selector(login:) toTarget:(id)self withObject:nil];
}

/**
 * This function is triggered after login process completes
 */
-(void)afterLogin:(NSNotification *)note {
    
    try {

        ObjSmartCardManager* objSmartCardManager = [[note userInfo] objectForKey:@"smartCardManager"];
        SmartCardManager smartCardManager = [objSmartCardManager smartCardManager];

        // read certificate from smartcard
        ECertificate cert = smartCardManager.getSignatureCertificate();

        // create signature
        SignatureContainer *container = new CMSContainer();
        Signature *signature = container->createSignature(cert);

        // add content to sign
        QByteArray dataBytes("data");
        Signable *data = new SignableBytes(dataBytes);
        signature->addContent(data, true);

        // sign
        BaseSigner *signer = smartCardManager.getSigner("12345", cert);
        bool isSigned = signature->sign(signer);

        if(isSigned) {
            // write signature
            QByteArray sig = signature->getSignature();

            // This is the name of the signature file that is going to be created in Documents folder
            // of the application. You can save it to computer from iTunes. Do not forget to add 'iTunes
            // file sharing' to .plist file.
            QString fileName = "../Documents/sig.der";
            QFile file(fileName);
            file.open(QIODevice::ReadWrite);
            file.write(sig);
            file.close();
        }

        delete signature;
        delete container;

    }
    catch(SmartCardException e) {
        Logger::log("Error: SmartCardException caught: " + e.getErrorMessage());
        [self performSelectorOnMainThread:@selector(displayErrorMessage:) withObject:e.getErrorMessage().toNSString() waitUntilDone:NO];
    }
    catch(CMSException e) {
        Logger::log("Error: CMSException caught: " + e.getErrorMessage());
        [self performSelectorOnMainThread:@selector(displayErrorMessage:) withObject:e.getErrorMessage().toNSString() waitUntilDone:NO];
    }
}

-(void)login:(ObjMainWindow*)window {
    
    try {

        SmartCardManager smartCardManager;
        smartCardManager.openSession();

        ObjSmartCardManager* objSmartCardManager = [[ObjSmartCardManager alloc] initWithSmartCardManager:smartCardManager];

        NSMutableDictionary * dict=[[NSMutableDictionary alloc]init];
        [dict setObject:objSmartCardManager forKey:@"smartCardManager"];

        [[NSNotificationCenter defaultCenter] postNotificationName:@"loginnotify" object:nil userInfo:dict];

    }
    catch (SmartCardException& e) {
        Logger::log("Error: SmartCardException caught: " + e.getErrorMessage());
        [self performSelectorOnMainThread:@selector(displayErrorMessage:) withObject:e.getErrorMessage().toNSString() waitUntilDone:NO];
    }
}

-(void)displayErrorMessage:(NSString*)note {
    self.window->displayMessageBox(QString::fromNSString(note));
}


///******** Logger *********///
-(void)logger:(NSString*)text {
    //self.logDisplay.text = [self.logDisplay.text stringByAppendingString:text];
    //self.logDisplay.text = [NSString stringWithFormat:@"\n%@", self.logDisplay.text];
    
    NSDateFormatter* formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"dd-MM-yyyy HH:mm:ss : "];
    NSString* dateString = [formatter stringFromDate:[NSDate date]];
    text = [dateString stringByAppendingString:text];
    
    NSString* logfile = @"log.txt";
    NSArray *path = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [path objectAtIndex:0];
    NSString *file = [documentsDirectory stringByAppendingPathComponent:logfile];
    
    NSString* logtext = [[NSString alloc] initWithContentsOfFile:file encoding:NSUTF8StringEncoding error:nil];
    if (logtext && [logtext length] > 0) {
        logtext = [NSString stringWithFormat:@"%@\n%@",logtext,text];
        [logtext writeToFile:file atomically:NO encoding:NSStringEncodingConversionAllowLossy error:nil];
    }
    else {
        [text writeToFile:file atomically:NO encoding:NSStringEncodingConversionAllowLossy error:nil];
    }
}

@end
