



  

  



  
    ASN1C V6.6.x README
  

  This file contains release notes on the latest release of the ASN1C
  compiler (version 6.6.x).

  Contents

  
    Introduction

    Release Notes

    Compatibility

    Documentation

    Windows Installation

    Contents of the Release

    Getting Started with C or
    C++

    Getting Started with Java

    Getting Started with C#

    Reporting Problems
  

  Introduction

  
    Thank you for downloading this release of the ASN1C software. ASN1C is
    a powerful ASN.1 compiler capable of parsing and generating C, C++, C#,
    or Java source code for the most advanced ASN.1 syntax.

    This package contains the ASN1C compiler executables and run-time
    libraries. Documentation is available online at http://www.obj-sys.com/docs/documents.shtml
  

  Release Notes

  
    This release of ASN1C adds the following new capabilities:

    
      Capability to Encode/Decode Data in JSON Format
      
        The capability to generate encoders/decoders that support
          Javascript Object Notation (JSON) has been added.  JSON is a 
          reasonably simple and compact textual encoding format that is 
          now being used in many domains where XML was used before.
          No formal encoding rule specification exists for JSON, so 
          we created our own.  The document is available 
          at 
          http://www.obj-sys.com/docs/JSONEncodingRules.pdf
      
    
      Capability to Encode/Decode 3GPP Layer 3 Messages
      
        The capability to encode/decode binary 3GPP layer 3 messages
          has been added.  These include messages specified in the 
          Non-Access Stratum (NAS) layer for 3G and LTE.  These messages 
          are not specified in ASN.1 but rather in tabular format and 
          using Concrete Syntax Notation 1 (CSN.1).  However, it was 
          determined that a fairly close approximation of the messages 
          could be created in ASN.1 along with a special encoding rules 
          capability (3G Layer 3) and additional configuration directives
          to refine the encoding format.  A document on the approach used
          to work with these message types is available at 
          
          http://www.obj-sys.com/docs/UsingASNtoDescribe3GPPMessages.pdf
      

      Open Source Version of C BER/DER Run-time
      
        An open source version of the C BER/DER run-time (ooberrt) has
          been added.  This is a limited version of our BER/DER run-time 
          that does not contain all features and is not fully optimized as 
          is our commercial version.  Nonetheless, it may be sufficient 
          for some applications.
      

      Capability to Embed Configuration Directives in  ASN.1 Comments
      
        An external XML file was the only method available up until this
          release to apply configuration directives to specific ASN.1 types
          or elements.  It is now possible to embed directives as special 
          ASN.1 comments immediately precding the item to be configured.
      

      Addition of ASN.1 Comments and Type Definitions in C/C++ Header Files
      
        ASN.1 comments as well as the ASN.1 definition itself are now 
          embedded as comments in generated C/C++ header files for each 
          generated C/C++ type or class.  This makes it easy to see the 
          ASN.1 definition a generated C structure represents.
      

      More Efficient Table Constraint Index Handling
      
        Modified C/C++ code generation of SEQUENCE-based index values 
          in table constraints to be more efficient (replaced if-else with 
          switch at each level).
      

      Option to Use Enumerated Types in C/C++ Code
      
        A new command-line option, -use-enum-types, has been added which 
          replaces the integer types that were used to represent enumerated 
          types with generated enum types.  Integers were always used in 
          the past because the size was known (different C compilers 
          generated different sized items for enum types) and to account 
          for the possibility of unknown extensible enumerated items. 
          This option allows the user to override this behavior and use 
          the enum type instead.
      
      
      Added support for ASN.1 2008 IRI Type
      
        Support was added for the ASN.1 2008 IRI Type for all 
          languages.  ASN1C now supports all items in the ASN.1 2008
          standard with the exception of some Extended-XER encoding 
          instructions.
      
      Addition of hashCode Method in Java
      
        A hashCode method is now generated in all Java classed when 
          the -compare command-line qualifier is specified.
      

    
  

  Compatibility

  
    In an ongoing effort to improve the product, changes have been made in
    how code is generated in some cases. The following are known areas
    where differences with the previous version exist:

    
      The format of the enumerated type used to represent CHOICE 
        tag constants was changed when the -use-enum-types directive 
        is used.  The enum type is now embedded directly in the 
        generated structure as has a tag value of T (i.e. enum T).
        It was only possible to generate this type of constant before 
        using the undocumented -enumchoice option, so this change should 
        not impact many users.
      
    
  

  Documentation

  
    Documentation for release is available online at the following
    web-link:

    http://www.obj-sys.com/asn1c-manuals.shtml
  

  UNIX/Linux
  Installation

  
    The steps to install ASN1C on a UNIX or Linux system are as
    follows:

    
      Download the ASN1C package that is of interest to you. Packages
      are available online for Solaris (Sparc and x86), HP-UX (IA64 and
      PA-RISC), Linux (x86 and x64), and Mac OSX operating systems. There
      are different C/C++ versions of these packages containing run-time
      libraries built with GNU gcc/g++ or native compilers. There is also a
      Java package available for each of these operating systems.

      ASN1C for UNIX/Linux is packaged in a gzipped .tar file. To
      install, unzip the file (gunzip) and untar (tar xf) in any directory.
      Note that in order to run the sample programs, write access to the
      sample directories is required, so make sure that you have write
      access to the base directory where the compiler is installed.

      After installation is complete, the license for the product must
        be installed.  This can come in two forms - as a license file 
        or a license key value:

        
          If a license file was provided either on the user's download 
            page or as an E-mail attachment, this file should be copied to 
            the same directory that the ASN1C compiler executable file is 
            located in. This is in the bin subdirectory located under the 
            top-level install directory.

          Alternately, a license key value may have been provided for 
            product activation.  This can be used either through the GUI 
            interface by following the on-screen instructions, or by running
            ASN1C from the command-line using the -lickey switch.
        

        The compiler should now be operational. The following command
        can be executed:

        
          <rootdir>/bin/asn1c
        

        to verify operation.
      
    
  

  Contents of the
  Release

  
    The following subdirectories contain the following files (note:
    <installdir> refers to the installation directory that
    was specified during the installation process):

    Base Compiler Package

    
      <installdir>/bin/asn1c

      The command-line compiler executable file. This is invoked on
      ASN.1 or XSD source files to generate C, C++, C#, or Java
      encode/decode classes and functions. It is recommended you modify
      your PATH environment variable to include
      <installdir>/bin to allow the compiler executable to
      be run from anywhere.

      <installdir>/bin/asn1cgui

      The compiler graphic user interface (GUI) wizard executable file.
      This GUI guides a user through the process of specifying ASN.1 or XSD
      source files and options. (Note: this binary is currently only
      available on Linux and Solaris systems. Source code is available to
      build it on other UNIX systems).

      <installdir>/bin/asn2xsd

      
        ASN.1 to XML Schema (XSD) translation tool.
      

      <installdir>\bin\berfdump.exe
      <installdir>\bin\ber2def.exe
      <installdir>\bin\ber2indef.exe

      <installdir>/bin/berfdump
      <installdir>/bin/ber2def
      <installdir>/bin/ber2indef

      Utility programs for operating on BER-encoded files. The first
      program allows a file to be dumped in a human-readable format. The
      other two utilities convert files from the use of indefinite to
      definite length encoding and vice-versa.

      <installdir>/bin/dumpasn1

      A public-domain ASN.1 BER/DER encoded data dump tool. Thanks to
      Peter Gutmann for making this available for public use. The full
      source code for this program can be found in the utils subdirectory
      of the installation.

      <installdir>/bin/xsd2asn1

      XSD-to-ASN.1 translation program executable file. This programs
      translates an XSD file to its ASN.1 equivalent as per the ITU-T X.694
      standard.

      <installdir>/doc

      This directory contains documentation files. Note that the bulk
      of the documentation items must be downloaded as a separate package
      (see the Documentation section
      above).

      <installdir>/scripts

      This directory contains Perl script files for doing source code
      editing and other transformations. The rtport.pl script is
      included in this release to port existing C/C++ applications that use
      ASN1C generated code from version 5.8 or lower to be compatible with
      the latest release of the product.

      <installdir>/utils

      This directory contains the source code and build makefile for
      some of the utility programs included in the bin subdirectory.

      <installdir>/xsd/lib/asn1.xsd

      This directory contains the common XML schema definitions (XSD)
      library. This contains type mappings for built-in ASN.1 types that do
      not have an equivalent type defined in XSD.

      <installdir>/xsd/sample

      
        This directory contains sample programs related to the
        conversion of ASN.1 to XML Schema. It also contains the XSD.asn
        ASN.1 specification which contains the XSD ASN.1 module that is
        sometimes referenced in ASN.1 files that are the result of an
        XSD-to-ASN.1 translation.
      
    

    C/C++ run-time libraries and source files

    
      <installdir>/c/lib/*.a

      <installdir>/c/lib/*.so

      The ASN1C run-time static libraries and shared object files.
        These contain the low-level run-time functions for the various 
        encoding rules supported by ASN1C. Note that the evaluation and 
        development libraries are not fully optimized (they contain 
        diagnostic tracing and are not compiled with compiler optimization 
        turned on). The deployment libraries are fully optimized.

      <installdir>/c/lib_opt/*.a

      <installdir>/c/lib_opt/*.so

      The optimized version of the ASN1C run-time libraries. This
      version has all diagnostic messages, error stack trace and text, and
      non-essential status checks removed. (Note: these libraries are only
      available in the licensed deployment version of the product).

      <installdir>/c/sample_*

      <installdir>/cpp/sample_*

      The sample directories contain sample programs demonstrating the
      use of the compiler. There are a set of sample programs that
      correspond to each encoding rule set supported by ASN1C. Most sample
      programs are broken down into a writer and a reader. The writer
      encodes a sample data record and writes it to a disk file. The reader
      reads the encoded message from the file, decodes it, and then prints
      the results of the decode operation.

      <installdir>/rtsrc/*

      <installdir>/rtxsrc/*

      
        Run-time source directories containing common type and class
      definitions used by all encoding rules. The installation run-time
      source directories contain the header files required to compile the
      compiler generated code. The C or C++ source files will also be
      located here if the run-time source code kit option was
        selected.
      

      <installdir>/rt*ersrc/*

      
        Run-time source directories for various ASN.1 encoding rules. 
          These contain common code for encoding and decoding using the 
          specific rules.  Directories are currently present for BER/DER/CER, 
          PER, MDER, OER, and XER rules.
      

      <installdir>\rtjsonsrc\*

      
        JSON specific run-time source directory. These contain common
        code for encoding/decoding JSON messages.
      

      <installdir>/rtxmlsrc/*

      
        XML specific run-time source directory. These contain common
        code for encoding/decoding XML messages.
      

      <installdir>/expatsrc/*

      
        The XML parser run-time source directories contain the source
        files for the Expat C XML parser.
      

      <installdir>/libxml2src/*

      
        The LibXML2 parser run-time source directories contain the
        source files for the GNOME libxml2 C XML parser.
      
    

    Java run-time libraries

    
      <installdir>/java/asn1rt.jar

      
        ASN.1 Java run-time libraries. These contain the low-level 
          encode/decode classes for the various encoding rules supported by
          ASN1C. The asn1rt.jar file contains classes compatible with 
          the Java 5 JRE.
      

      <installdir>/java/sample_*

      
        Sample programs illustrating the use of the Java version of
        ASN1C. As was the case for C/C++, most have a writer and a reader.
        Some contain support code used by other samples (for example,
        SimpleROSE contains the ROSE headers used by CSTA).
      

      <installdir>/java/doc/*

      
        The ASN.1 Java run-time libraries documentation files. These are
        html files generated with the javadoc documentation tool. To view
        the documentation, open the index.html file with a web
        browser and follow the hyperlinks.
      

      <installdir>/java/xerces/*

      The Apache Xerces Java XML parser implementation. This parser is
      used in the generated XER and XML decode classes.
    
  

  Getting
  Started with C or C++

  
    The compiler must be run from a command shell prompt. The sample
    progams were developed using the bash shell and should be compatible
    with the sh and ksh shells. Users of the C-shell (csh) will probably
    need to make some modifications.

    
      Open a terminal window.
    

    
      Change directory (cd) to one of the employee sample directories
      (for example, c/sample_ber/employee).
    

    
      Execute the make utility program:

        make

        This should cause the compiler to be invoked to compile the
        employee.asn sample file. It will then invoke the C or C++
        compiler to compile the generated C file and the test drivers. The
        result should be a writer and reader program file
        which, when invoked, will encode and decode a sample employee
        record.
      

      Invoke writer from the command line:

        writer

        This will generate an encoded record and write it to a disk
        file. By default, the file generated is message.dat (in the
        case of XER, it is message.xml). The test program has a
        number of command line switches that control the encoding. To view
        the switches, enter writer ? and a usage display will be
        shown.
      

      Invoke reader from the command line:

        reader

        This will read the disk file that was just created by the writer
        program and decode its contents. The resulting decoded data will be
        written to standard output. The test program has a number of
        command line switches that control the encoding. To view the
        switches, enter reader ? and a usage display will be
        shown.
      
    
  

  Getting
  Started with Java

  
    To run a simple test from the command line, do the following:

    
      Open a terminal window.

      Change directory (cd) to one of the employee sample directories
      (for example, java/sample_ber/Employee).

      Execute the makefile:

        make

        This will cause the ASN1C compiler to be invoked to compile the
        employee.asn sample file. It will then invoke the Java
        compiler (javac) to compile all generated java files and
        the reader and writer programs (Note: JDK 6 was used to build all
        the run-time library classes). It will also automatically execute
        the writer and reader programs. These programs will produce a
        writer.log and reader.log file respectively.
      

      View the writer and reader log files. The writer.log file will
        contain a dump of the encoded message contents. The reader.log file
        will contain a printout of the decoded data.
    
  

  Reporting
  Problems

  
    Report problems you encounter by sending E-mail to support@obj-sys.com. The preferred
    format of example programs is the same as the sample programs. Please
    provide a writer and reader and indicate where in the code the problem
    occurs.

    If you have any further questions or comments on what you would like
    to see in the product or what is difficult to use or understand, please
    communicate them to us. Your feedback is important to us. Please let us
    know how it works out for you - either good or bad.
  


