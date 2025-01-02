#include "mainwindow.h"
#include "ui_mainwindow.h"

#include "Logger.h"
#include "UIException.h"

#include <QMessageBox>

#import <Foundation/Foundation.h>
#import "ObjBESSigner.h"


using namespace esya;

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_pushButton_clicked()
{
    //try {
        NSLog(@"alie");
        Logger::log("------------------------------ sign button pressed");

        ObjBESSigner* objBESSigner = [[ObjBESSigner alloc] init];
        [objBESSigner signBES:this];
        //[objBESSigner cppTrial:this];
//    }
//    catch(UIException e) {
//        Logger::log("Error: UIException caught: " + e.getErrorMessage());
//        QMessageBox msg;
//        msg.setText(e.getErrorMessage());
//        msg.exec();
//    }
}

void MainWindow::displayMessageBox(QString message)
{
    QMessageBox msg;
    msg.setText(message);
    msg.exec();
}
