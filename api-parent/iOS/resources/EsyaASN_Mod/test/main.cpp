#include <Qt/qapplication.h>


#include "asn1type.h"
#include <QFile>
#include "rtconv.h"

#include "ASN1BERDecodeStream.h"
#include "OSRTFileInputStream.h"

#include "QtTest/QTest"

// #include "PrivateKeyInfo.h"
// #include "RSAPrivateKey.h"
// #include "ECertificateList.h"
// #include "PBES2_Params.h"
// #include "KeyUsage.h"
// #include "ExtensionGenerator.h"

#include "asn1compat.h"

#include "ECertificate.h"
#include "NameUtils.h"
#include "ECertificateList.h"
#include "FileUtil.h"
#include "KeyUsage.h"

using namespace esya;


void testKeyUsage()
{
	KeyUsage ku;
	QByteArray kub = ku.getEncodedBytes();
	KeyUsage ku2(kub);

}


void testCRL()
{
	int i= 0;
	QByteArray crlBytes = FileUtil::readFromFile("f:/kkk_sil.crl");

	ASN1BERDecodeBuffer buf((OSOCTET*)crlBytes.data(),crlBytes.size());
	ASN1T_EXP_CertificateList tcrl;
	ASN1C_EXP_CertificateList ccrl(tcrl);
	
	int res = ccrl.DecodeFrom(buf);





	while (1)
	{
		i++;

		ECertificateList crl;
		crl.lazyLoad(crlBytes);

		qDebug(qPrintable(QString("crl okundu %1").arg(i)));
	}
}

/*

QString carpanlarinaAyir(qlonglong V ) 
{
	QString res = "1" ; 
	for ( qlonglong i = 2 ; i*i<= V ; i++  ) 
	{
		if ( V % i == 0 )
		{	
			res = QString("%1 * %2").arg(res).arg(i);
			V /= i-- ;
		}			
	}

	res = QString("%1 * %2").arg(res).arg(V);
	return res;
}
*/





/*
QByteArray readFromFile(const QString &iFileName)
{
	QFile file(iFileName);
	if (!file.open(QIODevice::ReadOnly))
	{
		throw EException(QString("%1 adli dosya bulunamadi!").arg(iFileName),__FILE__, __LINE__);
	}
	QByteArray data = file.readAll();
	file.close();
	return data;

}
*/
// void testSignerAttribute()
// {
// 	QByteArray saData = readFromFile("d:\\sa.dat");
// 	ASN1T_ETSI101733_SignerAttribute sai;
// 	ASN1C_ETSI101733_SignerAttribute cSAI(sai);
// 	ASN1BERDecodeBuffer decBuf((OSOCTET*)saData.data(),saData.size());
// 
// 	try
// 	{
// 		int res = cSAI.DecodeFrom(decBuf);
// 		res = res + 1;
// 	}
// 	catch (EException&  exc)
// 	{
// 		
// 	}
// 
// }
// 
// void testSertifikaUretme()
// {
// 	ECertificate cert1,cert2;
// 	cert1.loadFromFile("f:/ornek/ornek.cer");
// 	Name  subject = cert1.getTBSCertificate().getSubject();
// 	
// 	KeyUsage ku;
// 
// 	ExtensionGenerator::getKeyUsageExtension(cert1.getTBSCertificate().getExtensions(),ku);
// 
// 	QByteArray kuBytes = ku.getEncodedBytes();
// 	KeyUsage ku2(kuBytes);
// 
// 	ETime before = cert1.getTBSCertificate().getNotBefore();
// 	ETime after = cert1.getTBSCertificate().getNotAfter();
// 
// 	QList<RelativeDistinguishedName> rdnList = subject.getList();
// 	RelativeDistinguishedName rdn = rdnList[0];
// 	
// 	QList<AttributeTypeAndValue> atvList = rdn.getList();
// 	AttributeTypeAndValue atv = atvList[0];
// 
// 	ASN1TObjId at = atv.getAttributeType();
// 	AttributeValue av =  atv.getAttributeValue();
// 
// 	Name mysubject;
// 	QList<RelativeDistinguishedName> myrdnList;
// 	RelativeDistinguishedName myRDN;
// 	QList<AttributeTypeAndValue> myatvList;
// 	AttributeTypeAndValue myatv;
// 	AttributeValue myav;
// 
// 	QByteArray encodedBytes = EASNToStringUtils::stringToCommonName(QString("test1"));
// 
// 	myav.setValue(encodedBytes);
// 	myatv.setAttributeType(at);
// 	myatv.setAttributeValue(av);
// 
// 	myatvList<<myatv;
// 	myRDN.setList(myatvList);
// 	myrdnList<<myRDN;
// 
// 	mysubject.setList(myrdnList);
// 
// 	QByteArray mybytes =  mysubject.getEncodedBytes();
// 	QByteArray bytes   =  subject.getEncodedBytes();
// 
// }
/*

void testBitString()
{
	QString libInfo = berGetLibInfo();

	QByteArray bytes = FileUtil::readFromFile("d:/publickey.ber");
	ASN1BERDecodeBuffer decBuf((ASN1OCTET*)bytes.data(),bytes.size() );
	ASN1T_EXP_UniqueIdentifier ui,uiAfter;
	ASN1C_EXP_UniqueIdentifier cUI(ui),cUIAfter(uiAfter);
	ASN1BEREncodeBuffer encBuf;

	int stat = cUI.DecodeFrom(decBuf);
	stat = cUI.EncodeTo(encBuf);

	encBuf.hexDump(stat);

	char buffer[10000];
	rtBitStrToString (ui.numbits, 
		ui.data, 
		buffer, 10000);

	QByteArray encBytes((const char*)encBuf.getMsgPtr(),stat);
	ASN1BERDecodeBuffer decBufAfter((ASN1OCTET*)encBuf.getMsgPtr(),stat );
	stat = cUIAfter.DecodeFrom(decBufAfter);
}
*/

// #include "cmp.h"
// #include "crmf.h"
// #include "FileUtil.h"
// 
// #include "ECertificate.h"

//using namespace esya;

//  void testPKIMessage()
//  {
// 	ASN1BEREncodeBuffer encBuf;
// 
// 	 ASN1T_CRMF_ProofOfPossession pop;
// 	ASN1C_CRMF_ProofOfPossession cPOP(pop);	 
// 	 pop.t = 1;
// 	 
// 
// 	 int stat = cPOP.EncodeTo(encBuf);
// 
// 	 QByteArray popBytes = QByteArray((char*)encBuf.getMsgPtr(),stat);
// 	 
// 	 QByteArray pmBytes = FileUtil::readFromFile("f:/2009.10.26-09.32.47.234-1create.der1");
// 	ASN1BERDecodeBuffer decBuf((OSOCTET*)pmBytes.data(),pmBytes.size());
// 	ASN1T_CMP_PKIMessage pm;
// 	ASN1C_CMP_PKIMessage cPM(pm);
// 
// 	int res = cPM.DecodeFrom(decBuf);
// 
// 
//  }

// void test64Bit()
// {
// 	ECertificate cert;
// 	cert.loadFromFile("f:/temp/umit.cer");
// 
// 	Name subject = cert.getTBSCertificate().getSubject();
// 	QString stSubject = subject.toString();
// 	QString ttSubject = subject.toTitle();
// 
// 	ASN1T_EXP_Certificate* pCert =  cert.getASNCopy();
// 	
// 	ECertificate cert2(*pCert);
// 
// 	DELETE_MEMORY(pCert);
// 
// 	QByteArray bSubject = subject.getEncodedBytes();
// 
// 	SubjectPublicKeyInfo spki ;
// 
// 	spki.getEncodedBytes();
// 
// 	PBES2_Params pbesParams;
// 	pbesParams.getEncodedBytes();
// }

//using namespace esya;

// #include "EASNStreamingUtils.h"
// int testASNFilePtr()
// {
// 	FILE * fp = NULL;
// 	ASN1TAG tag;
// 	int len;
// 
// 	QString sdFilePath		= "E:/sample.dat"; 
// 
// 	QString stMode("rb");
// 
// 	qDebug("0");
// 
// 	const wchar_t* wbytes = (const wchar_t*)sdFilePath.utf16();
// 	const wchar_t* wmode = (const wchar_t*)sdFilePath.utf16();
// 
// 	qDebug("01");
// 
// 	int res = _wfopen_s(&fp,wbytes,wmode);
// 	if (res != 0)
// 		fp= NULL;
// 
// 	qDebug("1");
// 
// 	EASNFileInputStream	* pIN = EASNStreamingUtils::createFileInputStream(fp);
// 
// 	qDebug("2");
// 
// 	pIN->peekTagLen(tag,len);
// 
// 	qDebug("bitti");
// 
// 	return 0;
// 
// }

// int testKeyUsage()
// {
// 	//QByteArray kuBytes("0x01\0x03\0x07\0x06\0x00")
// 
// 	ASN1BEREncodeBuffer encBuf;
// 	ASN1T_IMP_KeyUsage ku;
// 	ku.numbits = 9;
// 	ku.data[0] = 6;
// 	ku.data[1] = 0;
// 
// 	ASN1C_IMP_KeyUsage cKU(ku);
// 
// 	int stat = cKU.EncodeTo(encBuf);
// 	if (stat < ASN_OK)
// 	{
// 		//hata!!!
// 		return -1;
// 	}
// 
// 	ASN1BERDecodeBuffer decBuf(encBuf.getMsgPtr(),stat);
// 
// 	ASN1T_IMP_KeyUsage ku2;
// 	ASN1C_IMP_KeyUsage cKU2(ku2);
// 
// 	stat = cKU2.DecodeFrom(decBuf);
// 	
// 	if (stat != ASN_OK)
// 	{
// 		//hata!!!
// 		return -1;
// 	}
// }
// 
// void testCRL()
// {
// 	ECertificateList crl(QString("e:/UGKOK2.crt"));
// }

// 
// int testDecodeOpenType()
// {
// 	FILE* fp = fopen("e:/asncompare_Temp/testOpenType.ber","rb");
// 	OSRTFileInputStream *pFIN =  new OSRTFileInputStream( fp);
// 	if (pFIN->getStatus() != 0)
// 	{
// 		pFIN->printErrorInfo();
// 	}
// 
// 	ASN1BERDecodeStream* ds = new ASN1BERDecodeStream(pFIN);
// 
// 	int len;
// 	OSUINT32 tag;
// 
// 	if (ds->decodeTagAndLen(tag,len) != ASN_OK )//reading tag SEQUENCE
// 	{
// 		return -1;
// 	}
// 
// 	ASN1UINT val;
// 	if ( ds->decodeUInt(val,ASN1EXPL,0) != ASN_OK )
// 	{
// 		return -1;// Error
// 	}
// 	size_t buflen = 0;
// 	OSOCTET buf[100000];
// 
// 	int res = ds->peekTagAndLen(tag,len);
// 	if(res != ASN_OK)
// 	{
// 		return -1;
// 	}
// 	
// // 	ASN1TOpenType ot;
// // 	ds->decodeOpenType(ot);
// // 	QByteArray bytes((char*)ot.data,ot.numocts);
// 
// //////////////////////////////////////////////////////////////////////////
// // 	ds->decodeOpenType(ot);
// // 	QByteArray bytes2((char*)ot.data,ot.numocts);
// //////////////////////////////////////////////////////////////////////////
// 
// 	buflen = ds->readTLV(buf,100000);
// 
// 	res = ds->peekTagAndLen(tag,len);
// // 	if(res != ASN_OK)
// // 	{
// // 		return -1;
// // 	}
// 
// 	res = ds->decodeTagAndLen(tag,len);
// 	if (res != ASN_OK )//reading tag SEQUENCE
// 	{
// 		return -1;
// 	}
// 
// 	ASN1TOpenType ot;
// 	ds->decodeOpenType(ot);
// 	QByteArray bufBytes((char*)ot.data,ot.numocts);
// 	
// 	res = ds->decodeTagAndLen(tag,len);
// 	if (res != ASN_OK )//reading tag SEQUENCE
// 	{
// 		return -1;
// 	}
// 
// 	res = ds->decodeTagAndLen(tag,len);
// 	if (res != ASN_OK )//reading tag SEQUENCE
// 	{
// 		return -1;
// 	}
// 
// 	res = ds->peekTagAndLen(tag,len);
// 	if (res != ASN_OK )//reading tag SEQUENCE
// 	{
// 		return -1;
// 	}
// 
// 
// 
// 	buflen = ds->readTLV(buf,100000); // read asdasdasdasd
// 
// 	buflen = ds->decodeEoc();
// 
// 	buflen = ds->decodeEoc();
// 
// 	buflen = ds->decodeEoc();
// 
// 	buflen = ds->readTLV(buf,100000);
// 
// 	buflen = ds->readTLV(buf,100000);
// 
// 	if (buflen < 0 )
// 	{
// 		return -1;
// 	}
// 
// }


// void testDecodePrivateKey()
// {
// 	RSAPrivateKey privKey;
// 	//privKey.loadFromFile("e:/temp/rsa_orj.ber");
// 	privKey.loadFromFile("e:/temp/rsa_001.der");
// 
// }
// 
// void testInvalidKeyUsage()
// {
// 	ECertificate cert;
// 	cert.loadFromFile("e:/asncompare_temp/ayse2_der.cer");
// 
// 	KeyUsage ku;
// 
// 	bool res = ExtensionGenerator::getKeyUsageExtension(cert.getTBSCertificate().getExtensions(),ku);
// 	qDebug(qPrintable(ku.toBitString()));
// }

//qDebug(qPrintable(QString("Hata: %1").arg(res)));

int testDecodeObjID()
{
		int res = 0;

		OSRTFileInputStream* prtIN =  new OSRTFileInputStream("e:/temp/asnSample.dat");
		
		res = prtIN->getStatus(); // res = OK 

		if (res != ASN_OK)
		{
			return res;	
		}

		ASN1BERDecodeStream* pIN = new ASN1BERDecodeStream(prtIN);

		ASN1TAG tag; 
		int len;

		res = pIN->peekTagAndLen(tag,len);	 // res = OK and tag len filled with right values
		res = pIN->decodeTagAndLen(tag,len); // res = OK and tag len filled with right values

		ASN1TObjId objID;
		res = pIN->decodeObjId(objID,ASN1EXPL,0); // PROBLEM 1 : res = -22 ERROR ???

		pIN->close();
		delete pIN;		// PROBLEM 2 : Application Crashes. Why ? 

		if (res != ASN_OK)
		{
			return res;
		}
}

int testDecodeSSD()
{
	int res = 0;

	while(1)
	{
		OSRTFileInputStream* prtIN =  new OSRTFileInputStream("e:/temp/smime.m3i.dat");

		res = prtIN->getStatus(); // res = OK 

		if (res != ASN_OK)
		{
			return res;	
		}

		ASN1BERDecodeStream* pIN = new ASN1BERDecodeStream(prtIN);

		ASN1TAG tag; 
		int len;

		res = pIN->peekTagAndLen(tag,len);	 // res = OK and tag len filled with right values
		res = pIN->decodeTagAndLen(tag,len); // res = OK and tag len filled with right values

		ASN1TObjId objID;
		res = pIN->decodeObjId(objID,ASN1EXPL,0); // res = OK and objID filled with correct oid

		res = pIN->peekTagAndLen(tag,len); // res = -101 ? 

		pIN->close();
		delete pIN;		

		if (res != ASN_OK)
		{
			break;
		}
		qDebug("success");
	}
	int a= res;
}

void testSignature()
{
	ECertificate cert3,cert4;

	cert3.loadFromFile("e:/temp/test3.cer");
	cert4.loadFromFile("e:/temp/test4.cer");

	ECertificate tmpCert3(cert3.getEncodedBytes());
	ECertificate tmpCert4(cert4.getEncodedBytes());

	EBitString sig3 = tmpCert3.getSignature();

}

void testNameCompare()
{
	ECertificate subject,issuer;

	subject.loadFromFile("E:/temp/ser.cer");
	//subject.loadFromFile("E:/temp/tckkv2/tckkv2/KYSHS-surum1v2_02.cer");
	issuer.loadFromFile("E:/temp/tckkv2/tckkv2/tckk-kok-surum1v2.cer");

	QString sn = subject.getTBSCertificate().getSerialNumber().getValue();
	QByteArray snb = sn.toLocal8Bit().toHex();

	QString subjectIssuer = subject.getTBSCertificate().getIssuer().toString();
	QString issuerSubject = issuer.getTBSCertificate().getSubject().toString();

	bool b = NameUtils::isEqual(subject.getTBSCertificate().getIssuer(),issuer.getTBSCertificate().getSubject());
}


int main(int argc, char *argv[])
{
	QApplication app(argc,argv);
	
	testCRL();

	testNameCompare();

	testKeyUsage();
	
	//testDecodeSSD();
	//testDecodeObjID();
}




/*

	// 	int c = 0;
	// 	if (ui.numbits % 8 == 0)
	// 	{
	// 		byte b = bytesAfter[1];
	// 		c = abs(b-128);
	// 		bytesAfter[1+c+1]= 0;
	// 	}


//	ContentInfo ci;
//	ci.loadFromFile("f:/tsresponse.dat");

	//rtOIDToString(oid.numids,(OSUINT32*)oid.subid,buf,10000*sizeof(char));

// 	QString s = cert.getTBSCertificate().getIssuer().toString();
// 	
// 	testSertifikaUretme();
// 	
// 	return 0;
// 	
// 	QString st = carpanlarinaAyir(278);
// 
// 	QApplication a(argc, argv);
// 
// 	try
// 	{
// 		ECertificate cert1,cert;
// 		cert.loadFromFile("f:\\work\\test\\umit.cer");
// 		cert1.getTBSCertificate().getSerialNumber();
// 
// 		QList<QString>cdpList =  cert.getCDPAddresses(AT_LDAP);
// 
// 		for (int i= 0 ; i<cdpList.size();i++)
// 		{
// 			qDebug(qPrintable(cdpList[i]));
// 		}
// 	}
// 
// 	catch (EException& exc)
// 	{
// 		qDebug()<<exc.printStackTrace();				
// 	}
// 
// 
// 	QByteArray b1("1234567");
// 
// 	QString str = EASNToStringUtils::byteArrayToStr(b1);
// 
// 
// 	
// 	

  //  return a.exec();

}

*/

