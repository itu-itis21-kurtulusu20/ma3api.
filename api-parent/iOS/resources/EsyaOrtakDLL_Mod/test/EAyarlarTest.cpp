#include "EAyarlarTest.h"
#include <QList>
#include <QSqlQuery>
#include <QSqlDatabase>
#include <QSqlError>
#include <QDir>
#include <QFile>

#include "EGenelAyarManager.h"
#include "EYerelAyarManager.h"
#include "EAyarAlici.h"
#include "EsyaOrtak_Ayar.h"
#include "EAyarKullaniciManager.h"
#include "EAyarTanimlari.h"

EAyarlarTest::EAyarlarTest()
{
}

EAyarlarTest::~EAyarlarTest()
{
}

static void _sorguYap(QSqlDatabase& irVT,const QString & irQueryText)
{
	QSqlQuery * pQuery = new QSqlQuery(irVT);
	pQuery->prepare(irQueryText);

	if (!pQuery->exec())
	{
		QString hataText = pQuery->lastError().text();
		DELETE_MEMORY(pQuery);
		throw EAyarException(EAyarException::VTAcilamadi,QString("VERITABANI HATASI, Query calistirilamadi. Hata : %1").arg(hataText),__FILE__,__LINE__);
	}

	DELETE_MEMORY(pQuery);
}

static void _genelvtOlustur(QSqlDatabase& vt)
{
	QString
	query = "CREATE TABLE [TBL_AYARLAR] ("
		"[AYARID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, "
		"[Sinif] VARCHAR(100)  NOT NULL, "
		"[Ad] VARCHAR(100)  NOT NULL, "
		"[Tip] INTEGER  NOT NULL, "
		"[Deger] VARCHAR(10000)  NOT NULL, "
		"[Degistirilebilir] BOOLEAN  NOT NULL, "
		"[Genel] BOOLEAN  NOT NULL ,"
		"[Aciklama] VARCHAR(10000)  NULL"
		"); ";
	_sorguYap(vt,query);


	query = 
		" CREATE TABLE [TBL_LDAP] ( "
		" [LdapID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, "
		" [IP] VARCHAR(500)  NOT NULL, "
		" [Port] INTEGER  NOT NULL, "
		" [BaglantiTipi] INTEGER  NOT NULL, "
		" [SizeLimit] INTEGER  NULL, "
		" [TimeLimit] INTEGER  NULL, "
		" [SearchBase] VARCHAR(1000)  NOT NULL, "
		" [LdapTipi] INTEGER  NOT NULL, "
		" [Varsayilan] BOOLEAN DEFAULT 'TRUE' NOT NULL "
		" ); ";
	_sorguYap(vt,query);

	query = 
		" CREATE TABLE [TBL_KARTLAR] ( "
		" [KartID] INTEGER  UNIQUE NOT NULL PRIMARY KEY AUTOINCREMENT, "
		" [KartAdi] VARCHAR(500)  NOT NULL, "
		" [KartLib] VARCHAR(100)  NOT NULL "
		" ); ";

	_sorguYap(vt,query);

	query = 
			" CREATE TABLE [TBL_ONTANIMLIGDMDIZINBILGISI] ( "
			" [DizinID] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, "
			" [Tip] NUMERIC  NOT NULL, "
			" [DizinYolu] VARCHAR(1000) UNIQUE NOT NULL, "
			" [OzneSecimTipi] NUMERIC  NOT NULL, "
			" [Degistirilebilir] BOOLEAN  NOT NULL "
			" ); ";

	_sorguYap(vt,query);
}


static void _ayarEkle(QSqlDatabase* yerelvt,
					 QSqlDatabase* genelvt,
					 int iKulID,
					 const QString &irSinif,
					 const QString &irAd,
					 int iTip,
					 const QString &irDeger,
					 bool iDegistirilebilir,
					 bool iGenel
					 )
{
	QString query;
	if(yerelvt)
	{
		query = QString(
			"insert into TBL_AYARLAR (KulID,Sinif,Ad,Tip,Deger,Degistirilebilir,Genel) "
			"values                (%1,'%2','%3',%4,'%5','%6','%7');"
			)
			.arg(iKulID).arg(irSinif).arg(irAd).arg(iTip).arg(irDeger).arg(iDegistirilebilir).arg(iGenel);
		_sorguYap(*yerelvt,query);

	}
	if(genelvt)
	{
		query = QString(
			"insert into TBL_AYARLAR (Sinif,Ad,Tip,Deger,Degistirilebilir,Genel) "
			"values                ('%1','%2',%3,'%4','%5','%6');"
			)
			.arg(irSinif).arg(irAd).arg(iTip).arg(irDeger).arg(iDegistirilebilir).arg(iGenel);
		_sorguYap(*genelvt,query);

	}
}

#define TESTYERELDBDOSYAADI "yerel.sdb"
#define TESTGENELDBDOSYAADI "genel.sdb"

static bool vtAc(QSqlDatabase &vt,const QString & vtAdi,const QString &dosyaAdi)
{
	if(QSqlDatabase::contains(vtAdi))
	{
		vt = QSqlDatabase::database(vtAdi);
	}
	else
	{
		vt = QSqlDatabase::addDatabase("QSQLITE",vtAdi);
		vt.setDatabaseName(dosyaAdi);
	}

	if(!vt.isOpen() && !vt.open())
		return false;

	return true;
}
void EAyarlarTest::testKullaniciSil()
{
	EAyarlar::kullaniciSil("umita@ug.uekae");
}
void EAyarlarTest::tumAyarlariAlmaTesti()
{
	//veriyi gir....
	QSqlDatabase yervt = QSqlDatabase::database("eayarlardb");
	QSqlDatabase genvt = QSqlDatabase::database("egenelayarlardb");

	_ayarEkle(&yervt,&genvt,1,"sinif1","GendevarYerdevarTip1Deg",1,"d1",true,false);
	_ayarEkle(&yervt,NULL,1,"sinif1","GendeyokYerdevarTip1Deg",1,"d2",true,false);
	_ayarEkle(NULL,&genvt,1,"sinif2","GendevarYerdeyokTip1Deg",1,"d3",false,true);
	_ayarEkle(NULL,&genvt,1,"sinif3","GendevarYerdeyokTip2Deg",2,"15",true,false);
	_ayarEkle(NULL,&genvt,1,"sinif3","GendevarYerdeyokTip3Deg",3,"true",true,false);

	EAyarlar ayarlar;
	EAyar ayar;

	//Birkac okuma testi
	QList<EAyar> liste = ayarlar.ayarlariAl();
	QCOMPARE(liste.size(),5);
	QCOMPARE(liste[1].getStringDeger(),QString("d1"));
	QCOMPARE(liste[0].getStringDeger(),QString("d2"));
	QCOMPARE(liste[2].getStringDeger(),QString("d3"));
	QCOMPARE(liste[4].getIntDeger(),15);
	QCOMPARE(liste[3].getBoolDeger(),true);

	//genelde olan ayari degistirelim.
	liste[4].setDeger(22);
	//yeni bir ayar ekleyelim
	EAyar yeniayar("class","name",EAyar::AyarStringTipi,"value",true,false);
	ayarlar.ayarYaz(yeniayar);

	liste = ayarlar.ayarlariAl();
	QCOMPARE(liste.size(),6);
	QCOMPARE(liste[1].getStringDeger(),QString("d1"));
	QCOMPARE(liste[0].getStringDeger(),QString("d2"));
	QCOMPARE(liste[2].getStringDeger(),QString("d3"));
	QCOMPARE(liste[4].getIntDeger(),22);
	QCOMPARE(liste[3].getBoolDeger(),true);
	QCOMPARE(liste[5].getStringDeger(),QString("value"));

}
#define ozneAl(ad,no) \
	EAyarGDMOzneBilgisi ad = ozneler[no];\
	QCOMPARE(ad.getAd(),QString(#ad));

#define dizinAl(ad,no) \
	EAyarGDMDizinBilgisi ad = dizinler[no];\
	QCOMPARE(ad.getDizinYolu(),QString(#ad));

#define _gdmVerisiGir \
	EAyarlar ayar; \
	ayar.ozneYazYerelKullanici("k1","k1@uekae"); \
	ayar.ozneYazYerelKullanici("k2","k2@uekae"); \
	ayar.ozneYazYerelKullanici("k3","k3@uekae"); \
	ayar.ozneYazYerelKullanici("k4","k4@uekae"); \
	ayar.ozneYazYerelGrup("g1"); \
	ayar.ozneYazYerelKullanici("k5g1","k5g1@uekae"); \
	ayar.ozneYazYerelKullanici("k6g1","k6g1@uekae"); \
	ayar.ozneYazYerelGrup("g2"); \
	ayar.ozneYazYerelGrup("g3g1"); \
	ayar.ozneYazYerelKullanici("k7g3g1","k7g3g1@uekae"); \
	ayar.ozneYazYerelKullanici("k8g3g1","k8g3g1@uekae"); \
	ayar.dizinEkle(EAyarGDMDizinBilgisi::DTImzali,"d1",EAyarGDMOzneBilgisi::OSTListeyeGore); \
	ayar.ozneYazYerelKullanici("k9d1","k9d1@uekae"); \
	ayar.ozneYazYerelGrup("g4d1"); \
	ayar.ozneYazLdapGrup("dldap","ldapeposta","ldapDN"); \
	ayar.dizinEkle(EAyarGDMDizinBilgisi::DTImzali,"d2",EAyarGDMOzneBilgisi::OSTKullaniciyaSor); \
	EAyarGDMOzneBilgisi ozne; \
	QList<EAyarGDMOzneBilgisi> ozneler = ayar.gdmOzneleriAl(); \
	QCOMPARE(ozneler.size(),14); \
	ozneAl(k1,0); \
	ozneAl(k2,1); \
	ozneAl(k3,2); \
	ozneAl(k4,3); \
	ozneAl(g1,4); \
	ozneAl(k5g1,5); \
	ozneAl(k6g1,6); \
	ozneAl(g2,7); \
	ozneAl(g3g1,8); \
	ozneAl(k7g3g1,9); \
	ozneAl(k8g3g1,10); \
	ozneAl(k9d1,11); \
	ozneAl(g4d1,12); \
	ozneAl(dldap,13); \
	ayar.ozneEkleGruba(g1,k5g1); \
	ayar.ozneEkleGruba(g1,k6g1); \
	ayar.ozneEkleGruba(g1,g3g1); \
	ayar.ozneEkleGruba(g3g1,k7g3g1); \
	ayar.ozneEkleGruba(g3g1,k8g3g1); \
	QList<EAyarGDMDizinBilgisi> dizinler = ayar.gdmDizinleriAl(); \
	QCOMPARE(dizinler.size(),2); \
	dizinAl(d1,0); \
	dizinAl(d2,1); \
	ayar.ozneEkleDizine(d1,k9d1); \
	ayar.ozneEkleDizine(d1,g4d1); \


void EAyarlarTest::gdmOnTanimliDizinlerTesti()
{
	try
	{
		EAyarlar ayarlar;
		QList<EAyarGDMDizinBilgisi> liste1 =  ayarlar.gdmDizinleriAl();
		QList<EAyarGDMDizinBilgisi> liste =  ayarlar.gdmOnTanimliDizinleriAl();
	}
	catch (EException& exc)
	{
		qDebug(qPrintable(exc.printStackTrace()));
	}
}

void EAyarlarTest::grubaAyniElemanEklenmesiTesti()
{
	_gdmVerisiGir;

	EAyarlar ayarlar;

	try
	{
		ayarlar.ozneEkleGruba(g1,g3g1);
		QFAIL("Grup zaten icinde oldugu gruba eklenemez!");
	}
	catch (EException& e){}

	try
	{
		ayarlar.ozneEkleGruba(g1,k6g1);
		QFAIL("Ozne zaten icinde oldugu gruba eklenemez!");
	}
	catch (EException& e){}

	try
	{
		ayarlar.ozneEkleDizine(d1,k9d1);
		QFAIL("Ozne zaten icinde oldugu dizine eklenemez!");
	}
	catch (EException& e){}

}



void EAyarlarTest::ozneAdiDegistirme()
{
	_gdmVerisiGir;

	EAyarlar ayarlar;

	EAyarGDMOzneBilgisi ozne1,ozne2;

	ozne1 = ayarlar.gdmOzneAlAdinaGore("k1");
	ayarlar.ozneAdiDegistir(ozne1,"yeniisim");
	try
	{
		ayarlar.gdmOzneAlAdinaGore("k1");
		QFAIL("Artik boyle bir ozne yok!");
	}
	catch (EException& e){}
	ozne2 = ayarlar.gdmOzneAlAdinaGore("yeniisim");

	ozne2.ozneAdiDegistir("dahayeni");

	try
	{
		ayarlar.gdmOzneAlAdinaGore("yeniisim");
		QFAIL("Artik boyle bir ozne yok!");
	}
	catch (EException& e){}

	ozne1 = ayarlar.gdmOzneAlAdinaGore("dahayeni");

	QCOMPARE(ozne1,ozne2);
}


void EAyarlarTest::ozneliGrupSilmeTesti()
{
	_gdmVerisiGir;

	EAyarlar ayarlar;

	try
	{
		ayarlar.ozneSil(g1);
		QFAIL("Icinde ozne olan grup silinemez!");
	}
	catch (EException& e){}

	try
	{
		ayarlar.ozneSilTamamen(g1);
	}
	catch (EException& e){
		QFAIL("Icinde ozne olan grup ozneSilTamamen() ile silinebilmeli!");
	}

}


void EAyarlarTest::ozneSilTamamenTesti()
{
	_gdmVerisiGir;

	EAyarlar ayarlar;

	ayarlar.ozneSilTamamen(k2);
	try
	{
		ayarlar.gdmOzneAlAdinaGore("k2");
		QFAIL("k2 oznesi bulunamamali");
	}catch (...){}

	ayarlar.ozneSilTamamen(k7g3g1);
	try
	{
		ayarlar.gdmOzneAlAdinaGore("k7g3g1");
		QFAIL("k7g3g1 oznesi bulunamamali");
	}catch (...){}

	ayarlar.ozneSilTamamen(k9d1);
	try
	{
		ayarlar.gdmOzneAlAdinaGore("k9d1");
		QFAIL("k9d1 oznesi bulunamamali");
	}catch (...){}

	ayarlar.ozneEkleDizine(d1,k8g3g1);
	ayarlar.ozneSilTamamen(k8g3g1);
	try
	{
		ayarlar.gdmOzneAlAdinaGore("k8g3g1");
		QFAIL("k8g3g1 oznesi bulunamamali");
	}catch (...){}
}

void EAyarlarTest::ayarAlDefaultValueluTest()
{
	//olmayan bir degeri okumaya calisalim...
	QString def="defaultDeger";
	EAyarlar ayarlar;
	EAyar ayar = ayarlar.ayarAl("olmayanSinif","olmayanAd",def);
	QCOMPARE(ayar.getStringDeger(),def);

	//olan degeri bununla okumaya calisalim...
	EAyar yeniAyar("yeniSinif","yeniAd",EAyar::AyarStringTipi,"yeniDeger",true,false);
	ayarlar.ayarYaz(yeniAyar);
	ayar = ayarlar.ayarAl("yeniSinif","yeniAd",def);
	QCOMPARE(ayar.getStringDeger(),QString("yeniDeger"));
}

void EAyarlarTest::kartTestleri()
{
	//bir kac kart ekleyelim...

	//genel vtyi ac
	QSqlDatabase genvt;
	if(!vtAc(genvt,"egenelayarlardb",QDir::homePath()+"/"+TESTGENELDBDOSYAADI))
		QFAIL("genel veritabani acilamadi...");

	//kart ekleyelim.
	QString query = QString("insert into TBL_KARTLAR (KartAdi,KartLib) "
		"values ('kartadi1','kartlib1')");
	_sorguYap(genvt,query);
	query = QString("insert into TBL_KARTLAR (KartAdi,KartLib) "
		"values ('kartadi2','kartlib2')");
	_sorguYap(genvt,query);
	query = QString("insert into TBL_KARTLAR (KartAdi,KartLib) "
		"values ('kartadi3','kartlib3')");
	_sorguYap(genvt,query);
	query = QString("insert into TBL_KARTLAR (KartAdi,KartLib) "
		"values ('kartadi4','kartlib4')");
	_sorguYap(genvt,query);

	EAyarlar ayar;

#define KARTKARSILASTIR(ll,no) \
	QCOMPARE(ll.getKartAdi(),QString("kartadi"#no)); \
	QCOMPARE(ll.getKartLib(),QString("kartlib"#no)); 
	

	//okuma
	QList<EAyarKartlar> kartlar = ayar.kartlariAl();
	QCOMPARE(kartlar.size(),4);
	KARTKARSILASTIR(kartlar[0],1);
	KARTKARSILASTIR(kartlar[1],2);
	KARTKARSILASTIR(kartlar[2],3);
	KARTKARSILASTIR(kartlar[3],4);

}




void EAyarlarTest::vtVarkenCalismasi()
{
	
	//yerel vtyi ac
	QSqlDatabase yervt;
	if(!vtAc(yervt,"eayarlardb",QDir::homePath()+"/"+TESTYERELDBDOSYAADI))
	QFAIL("yerel veritabani acilamadi...");

	//Veritabanini olustur...
	EAyarlar::yerelvtOlustur(yervt);

	kullaniciTestleri();
}


void EAyarlarTest::kullaniciTestleri()
{
	QString k1 = "kullanici1@uekae";
	QString k2 = "kullanici2@uekae";
	QString k3 = "kullanici3@uekae";

	QCOMPARE(EAyarlar::varsayilanKullaniciAl(),k1);
	QList<QString> kullar = EAyarlar::digerKullanicilariAl();
	QCOMPARE(kullar.size(),1);
	QCOMPARE(kullar[0],k2);

	EAyarlar::varsayilanKullaniciYap(k2);
	QCOMPARE(EAyarlar::varsayilanKullaniciAl(),k2);

	EAyarlar::kullaniciEkle(k3);
	kullar = EAyarlar::digerKullanicilariAl();
	QCOMPARE(kullar.size(),2);
	QString aa = kullar[0];
	QCOMPARE(kullar[0],k1);
	QCOMPARE(kullar[1],k3);

	//Ayni kullaniciyi tekrar yazmaya calisma testi
	try
	{
		EAyarlar::kullaniciEkle(k3);
		QFAIL("Olan kullaniciyi tekrar yazamayiz.");
	}
	catch (EException& e)
	{

	}

	//olmayan kullaniciyi varsayilan yapma testi
	try
	{
		EAyarlar::varsayilanKullaniciYap("olmayaKullanici");
		QFAIL("Olmayan kullaniciyi varsayilan yapamayiz.");
	}
	catch (EException& e)
	{

	}

}


#define HATALIYAZMADENEMESI(x,deger) \
	try\
	{\
	x.setDeger(deger); \
	QTest::qFail("yazmada hata vermesi gerekiyordu...", __FILE__, __LINE__);\
	return false;\
	} \
	catch (EException& e) \
	{ \
	} \
	catch (...) \
	{ \
	QTest::qFail("Baska bir exception atti...", __FILE__, __LINE__);\
	return false;\
	}

static bool hataliYazmayiDene(EAyar &iAyar)
{
	EAyar yeniAyar;

	yeniAyar = EAyar(
		iAyar.getSinif(),
		iAyar.getAd(),
		iAyar.getTip(),
		"bilmemne",
		!iAyar.isDegistirilebilir(),
		iAyar.isGenel()
		);
	HATALIYAZMADENEMESI(yeniAyar,"serdar");

	yeniAyar = EAyar(
		iAyar.getSinif(),
		iAyar.getAd(),
		iAyar.getTip(),
		"bilmemne",
		iAyar.isDegistirilebilir(),
		!iAyar.isGenel()
		);
	HATALIYAZMADENEMESI(yeniAyar,"serdar");

	yeniAyar = EAyar(
		iAyar.getSinif(),
		iAyar.getAd(),
		iAyar.getTip(),
		"bilmemne",
		!iAyar.isDegistirilebilir(),
		!iAyar.isGenel()
		);
	HATALIYAZMADENEMESI(yeniAyar,"serdar");

	return true;
}

void EAyarlarTest::ayarTestleri()
{
	EAyar lAyar;
	try
	{
		lAyar = EAyarlar().ayarAl(AYAR_SNF_EPOSTA,AYAR_EPOSTA_HIZMETE_OZEL_SIMSIFALG);		
		QString lAciklama = lAyar.getAciklama();
	}
	catch (EException& exc)
	{
		QFAIL(qPrintable(exc.printStackTrace()));
	}

	//veriyi gir....
	QSqlDatabase yervt = QSqlDatabase::database("eayarlardb");
	QSqlDatabase genvt = QSqlDatabase::database("egenelayarlardb");

	//sinif1
	_ayarEkle(&yervt,&genvt,1,"sinif1","GendevarYerdevarTip1Deg",1,"d1",true,false);
	_ayarEkle(&yervt,NULL,1,"sinif1","GendeyokYerdevarTip1Deg",1,"d2",true,false);
	_ayarEkle(NULL,&genvt,1,"sinif1","GendevarYerdeyokTip1Deg",1,"d3",true,false);
	_ayarEkle(NULL,&genvt,1,"sinif1","GendevarYerdeyokTip2Deg",2,"15",true,false);
	_ayarEkle(NULL,&genvt,1,"sinif1","GendevarYerdeyokTip3Deg",3,"true",true,false);

	_ayarEkle(&yervt,&genvt,1,"sinif1","GendevarYerdevarTip1",1,"d4",false,false);
	_ayarEkle(&yervt,NULL,1,"sinif1","GendeyokYerdevarTip1",1,"d5",false,false);
	_ayarEkle(NULL,&genvt,1,"sinif1","GendevarYerdeyokTip1",1,"d6",false,false);

	//sinif2
	_ayarEkle(&yervt,&genvt,1,"sinif2","GendevarYerdevarTip1Deg",1,"d7",true,false);
	_ayarEkle(&yervt,NULL,1,"sinif2","GendeyokYerdevarTip1Deg",1,"d8",true,false);
	_ayarEkle(NULL,&genvt,1,"sinif2","GendevarYerdeyokTip1Deg",1,"d9",true,false);
	_ayarEkle(NULL,&genvt,1,"sinif2","GendevarYerdeyokTip2Deg",2,"25",true,false);
	_ayarEkle(NULL,&genvt,1,"sinif2","GendevarYerdeyokTip3Deg",3,"false",true,false);

	_ayarEkle(&yervt,&genvt,1,"sinif2","GendevarYerdevarTip1",1,"d10",false,false);
	_ayarEkle(&yervt,NULL,1,"sinif2","GendeyokYerdevarTip1",1,"d11",false,false);
	_ayarEkle(NULL,&genvt,1,"sinif2","GendevarYerdeyokTip1",1,"d12",false,false);


	EAyarlar ayarlar;
	EAyar ayar;

	//Birkac okuma testi
	ayar = ayarlar.ayarAl("sinif1","GendevarYerdevarTip1Deg");
	QCOMPARE(ayar.getStringDeger(),QString("d1"));
	QCOMPARE(ayarlar.ayarAl("sinif1","GendeyokYerdevarTip1Deg").getStringDeger(),QString("d2"));
	QCOMPARE(ayarlar.ayarAl("sinif1","GendevarYerdeyokTip1Deg").getStringDeger(),QString("d3"));
	QCOMPARE(ayarlar.ayarAl("sinif1","GendevarYerdeyokTip2Deg").getIntDeger(),15);
	QCOMPARE(ayarlar.ayarAl("sinif1","GendevarYerdeyokTip3Deg").getBoolDeger(),true);

	ayar = ayarlar.ayarAl("sinif2","GendevarYerdevarTip1Deg");
	QCOMPARE(ayar.getStringDeger(),QString("d7"));
	QCOMPARE(ayarlar.ayarAl("sinif2","GendeyokYerdevarTip1Deg").getStringDeger(),QString("d8"));
	QCOMPARE(EAyarlar().ayarAl("sinif2","GendevarYerdeyokTip1Deg").getStringDeger(),QString("d9"));
	QCOMPARE(ayarlar.ayarAl("sinif2","GendevarYerdeyokTip2Deg").getIntDeger(),25);
	QCOMPARE(ayarlar.ayarAl("sinif2","GendevarYerdeyokTip3Deg").getBoolDeger(),false);


	//yazma testleri
	ayar = ayarlar.ayarAl("sinif1","GendevarYerdevarTip1Deg");
	ayar.setDeger("yenideger");
	QCOMPARE(ayarlar.ayarAl("sinif1","GendevarYerdevarTip1Deg").getStringDeger(),QString("yenideger"));

	ayar = ayarlar.ayarAl("sinif1","GendeyokYerdevarTip1Deg");
	ayar.setDeger("yenideger");
	QCOMPARE(ayarlar.ayarAl("sinif1","GendeyokYerdevarTip1Deg").getStringDeger(),QString("yenideger"));

	ayar = ayarlar.ayarAl("sinif1","GendevarYerdeyokTip1Deg");
	ayar.setDeger("yenideger");
	QString aa = ayarlar.ayarAl("sinif1","GendevarYerdeyokTip1Deg").getStringDeger();
	QCOMPARE(ayarlar.ayarAl("sinif1","GendevarYerdeyokTip1Deg").getStringDeger(),QString("yenideger"));


	ayar = ayarlar.ayarAl("sinif1","GendevarYerdeyokTip2Deg");
	ayar.setDeger(5);
	QCOMPARE(ayarlar.ayarAl("sinif1","GendevarYerdeyokTip2Deg").getIntDeger(),5);
	ayar = ayarlar.ayarAl("sinif1","GendevarYerdeyokTip3Deg");
	ayar.setDeger(false);
	QCOMPARE(ayarlar.ayarAl("sinif1","GendevarYerdeyokTip3Deg").getBoolDeger(),false);

	//yeni ayar yazalim..
	ayar = EAyar(
		"yeniayarsinif",
		"yeniayarad1",
		EAyar::AyarStringTipi,
		"",
		true,
		false
		);
	ayar.setDeger("yeniayar1");
	QCOMPARE(ayarlar.ayarAl("yeniayarsinif","yeniayarad1").getStringDeger(),QString("yeniayar1"));



	//hatali okuma
#define HATALIOKU(sinif,ad) \
	try \
	{ \
		ayar = ayarlar.ayarAl(sinif,ad); \
		QFAIL("Okumada hata vermesi gerekiyordu..."); \
	} \
	catch (EException& e) \
	{ \
	} \
	catch (...) \
	{ \
		QFAIL("Baska bir exception atti..."); \
	}

	
	HATALIOKU("olmayansinif","GendevarYerdevarTip1Deg");
	HATALIOKU("sinif1","olmayanad");

	//hatali yazma
	ayar = ayarlar.ayarAl("sinif2","GendevarYerdevarTip1Deg");
	QVERIFY(hataliYazmayiDene(ayar));
	ayar = ayarlar.ayarAl("sinif2","GendeyokYerdevarTip1Deg");
	QVERIFY(hataliYazmayiDene(ayar));
	ayar = ayarlar.ayarAl("sinif2","GendevarYerdeyokTip1Deg");
	QVERIFY(hataliYazmayiDene(ayar));
	ayar = EAyar(
		"olmayansinif",
		"olmayanad1",
		EAyar::AyarStringTipi,
		"bilmemne",
		true,
		false
		);
	QVERIFY(hataliYazmayiDene(ayar));
	ayar = EAyar(
		"olmayansinif",
		"olmayanad2",
		EAyar::AyarStringTipi,
		"",
		true,
		false
		);
	QVERIFY(hataliYazmayiDene(ayar));
}



void EAyarlarTest::gdmTestleri()
{

	_gdmVerisiGir
		;


	//ozneden ozneleri al...
	ozneler = ayar.gdmOzneleriAlOzneden(g1);
	QCOMPARE(ozneler.size(),3);
	QCOMPARE(ozneler[0],k5g1);
	QCOMPARE(ozneler[1],k6g1);
	QCOMPARE(ozneler[2],g3g1);

	ozneler = ayar.gdmOzneleriAlOzneden(g3g1);
	QCOMPARE(ozneler.size(),2);
	QCOMPARE(ozneler[0],k7g3g1);
	QCOMPARE(ozneler[1],k8g3g1);

	//dizinden ozneleri al
	ozneler = ayar.gdmOzneleriAlDizinden(d1);
	QCOMPARE(ozneler.size(),2);
	QCOMPARE(ozneler[0],k9d1);
	QCOMPARE(ozneler[1],g4d1);

	//tipe gore ozneleri al
	//tek tip verelim...
	QList<EAyarGDMOzneBilgisi::AyarOzneTipi> tipler;
	tipler << EAyarGDMOzneBilgisi::OTYerelGrup;
	ozneler = ayar.gdmOzneleriAlOzneTipineGore(tipler);
	QCOMPARE(ozneler.size(),4);
	QCOMPARE(ozneler[0],g1);
	QCOMPARE(ozneler[1],g2);
	QCOMPARE(ozneler[2],g3g1);
	QCOMPARE(ozneler[3],g4d1);
	//iki tip verelim
	tipler.clear();
	tipler << EAyarGDMOzneBilgisi::OTLdapGrup << EAyarGDMOzneBilgisi::OTYerelKullanici;
	ozneler = ayar.gdmOzneleriAlOzneTipineGore(tipler);
	QCOMPARE(ozneler.size(),10);
	QCOMPARE(ozneler[0],k1);
	QCOMPARE(ozneler[1],k2);
	QCOMPARE(ozneler[2],k3);
	QCOMPARE(ozneler[3],k4);
	QCOMPARE(ozneler[4],k5g1);
	QCOMPARE(ozneler[5],k6g1);
	QCOMPARE(ozneler[6],k7g3g1);
	QCOMPARE(ozneler[7],k8g3g1);
	QCOMPARE(ozneler[8],k9d1);
	QCOMPARE(ozneler[9],dldap);

	//grup olanlari al
	ozneler = ayar.gdmOzneleriAlGrupOlanlari();
	QCOMPARE(ozneler.size(),5);
	QCOMPARE(ozneler[0],g1);
	QCOMPARE(ozneler[1],g2);
	QCOMPARE(ozneler[2],g3g1);
	QCOMPARE(ozneler[3],g4d1);
	QCOMPARE(ozneler[4],dldap);


	//ozneleri adina gore oku...
	ozne = ayar.gdmOzneAlAdinaGore("g1");
	QCOMPARE(ozne,g1);
	ozne = ayar.gdmOzneAlAdinaGore("g2");
	QCOMPARE(ozne,g2);
	ozne = ayar.gdmOzneAlAdinaGore("dldap");
	QCOMPARE(ozne,dldap);
	ozne = ayar.gdmOzneAlAdinaGore("g3g1");
	QCOMPARE(ozne,g3g1);
	ozne = ayar.gdmOzneAlAdinaGore("k7g3g1");
	QCOMPARE(ozne,k7g3g1);

	//ayni isimde ozne yazmaya calis
	try{
		ayar.ozneYazYerelGrup("g1");
		QFAIL("Olan ozne yazilamaz.");
	} catch(...){}

	//olmayan ozneyi adiyla okumaya calis
	try{
		ayar.gdmOzneAlAdinaGore("hoho");
		QFAIL("Olmayan ozne okunamaz.");
	} catch(...){}


}

void EAyarlarTest::gdmOzneSilmeDegistirmeTestleri()
{

	_gdmVerisiGir
	;

	//dizine silinemez bir ozne ekleyelim
	ayar.ozneEkleDizine(d1,k4,false);

	//dizinden silinemez ozne cikar
	ayar.ozneCikarDizinden(d1,k4);
	ozneler = ayar.gdmOzneleriAlDizinden(d1);
	QCOMPARE(ozneler.size(),3);
	QCOMPARE(ozneler[0],k4);
	QCOMPARE(ozneler[1],k9d1);
	QCOMPARE(ozneler[2],g4d1);

	//dizinden silinebilir ozne cikar
	ayar.ozneCikarDizinden(d1,k9d1);
	ozneler = ayar.gdmOzneleriAlDizinden(d1);
	QCOMPARE(ozneler.size(),2);
	QCOMPARE(ozneler[0],k4);
	QCOMPARE(ozneler[1],g4d1);

	//gruptan ozne cikar
	ayar.ozneCikarGruptan(g1,k5g1);
	ozneler = ayar.gdmOzneleriAlOzneden(g1);
	QCOMPARE(ozneler.size(),2);
	QCOMPARE(ozneler[0],k6g1);
	QCOMPARE(ozneler[1],g3g1);

	//ozne sil
	try{
		ayar.ozneSil(k4);
		QFAIL("Dizindeki ozne silinemez.");
	} catch(...){}

	try{
		ayar.ozneSil(k6g1);
		QFAIL("Gruptaki ozne silinemez.");
	} catch(...){}

	ayar.ozneSil(k1);
	try{
		ayar.gdmOzneAlAdinaGore("k1");
		QFAIL("Silinen ozne okunamaz.");
	} catch(...){}



}

void EAyarlarTest::ldapTestleri()
{
	//bir kac ldap ekleyelim...

	//genel vtyi ac
	QSqlDatabase genvt;
	if(!vtAc(genvt,"egenelayarlardb",QDir::homePath()+"/"+TESTGENELDBDOSYAADI))
		QFAIL("genel veritabani acilamadi...");

	//ldap ekleyelim.
	QString query = QString("insert into TBL_LDAP (IP,Port,BaglantiTipi,SizeLimit,TimeLimit,SearchBase,LdapTipi,Varsayilan) "
		"values ('1.1.1.1',111,1,1,1,'cn=1',1,'false')");
	_sorguYap(genvt,query);
	query = QString("insert into TBL_LDAP (IP,Port,BaglantiTipi,SizeLimit,TimeLimit,SearchBase,LdapTipi,Varsayilan) "
		"values ('2.2.2.2',222,2,2,2,'cn=2',2,'true')");
	_sorguYap(genvt,query);
	query = QString("insert into TBL_LDAP (IP,Port,BaglantiTipi,SizeLimit,TimeLimit,SearchBase,LdapTipi,Varsayilan) "
		"values ('3.3.3.3',333,3,3,3,'cn=3',3,'false')");
	_sorguYap(genvt,query);
	query = QString("insert into TBL_LDAP (IP,Port,BaglantiTipi,SizeLimit,TimeLimit,SearchBase,LdapTipi,Varsayilan) "
		"values ('4.4.4.4',444,4,4,4,'cn=4',4,'false')");
	_sorguYap(genvt,query);



	EAyarlar ayar;
	EAyarLdap ld = ayar.varsayilanLdapAl();

#define LDAPKARSILASTIR(ll,no) \
	QCOMPARE(ll.getIP(),QString(#no"."#no"."#no"."#no)); \
	QCOMPARE(ll.getPort(),100*no+10*no+no);\
	QCOMPARE(ll.getBaglantiTipi(),no); \
	QCOMPARE(ll.getSizeLimit(),no); \
	QCOMPARE(ll.getTimeLimit(),no); \
	QCOMPARE(ll.getSearchBase(),QString("cn="#no)); \
//	QCOMPARE(ll.getLdapTipi(),no);


	//birkac okuma
	ld=ayar.varsayilanLdapAl();
	LDAPKARSILASTIR(ld,2);

	QList<EAyarLdap> ldaplar = ayar.digerLdaplariAl();
	QCOMPARE(ldaplar.size(),3);
	LDAPKARSILASTIR(ldaplar[0],1);
	LDAPKARSILASTIR(ldaplar[1],3);
	LDAPKARSILASTIR(ldaplar[2],4);
	
	//varsayilan degistirme testi
	ayar.varsayilanLdapYap(ldaplar[0]);
	ld=ayar.varsayilanLdapAl();
	LDAPKARSILASTIR(ld,1);

	//hatali bir seyi varsayilan yapalim.
	EAyarLdap hataliLdap(10,"ip",1234,1,1,1,"cn=hatali",EAyarLdap::LDAP_TIPI_AD);
	try
	{
		ayar.varsayilanLdapYap(hataliLdap);
		QFAIL("Hatali ldap ekleyememeliyim...");
	}
	catch (EException& e)
	{
	}

	ld=ayar.varsayilanLdapAl();
	LDAPKARSILASTIR(ld,1);
}

void EAyarlarTest::sadeceGeneldenAlmaTesti()
{
	EAyar lAyar;
	try
	{
		lAyar = EGenelAyarManager::getInstance()->getAyar(AYAR_SNF_ISLEMOZELLIKLERI,AYAR_ISLEMOZELLIKLERI_PROGRAM_DILI);		
		QString lAciklama = lAyar.getAciklama();
	}
	catch (EException& exc)
	{
		QFAIL(qPrintable(exc.printStackTrace()));
	}
}

void EAyarlarTest::sadeceGeneldekiAyarAlmaTesti()
{
	EAyar bulunanAyar;
	try
	{
		EAyarAlici ayarAlici(AYAR_SNF_PAROLA,AYAR_PAROLA_DEGISIMZORUNLU);
		bulunanAyar = ayarAlici.ayarBul();
		bool deger = bulunanAyar.getBoolDeger();		
	}
	catch (EException& exc)
	{
		QFAIL(qPrintable(exc.printStackTrace()));
	}
}

void EAyarlarTest::sadeceYereldekiAyarAlmaTesti()
{
	EAyar bulunanAyar;
	try
	{
		EAyarAlici ayarAlici(AYAR_SNF_PAROLA,AYAR_PAROLA_SONDEGISTIRMEZAMANI);
		bulunanAyar = ayarAlici.ayarBul();
		QString deger = bulunanAyar.getStringDeger();		
	}	
	catch (EAyarException& exc)
	{
		QFAIL(qPrintable(exc.printStackTrace()));
	}
	catch (EException& exc)
	{
		QFAIL(qPrintable(exc.printStackTrace()));
	}

}


void EAyarlarTest::hemYereldekiHemYereldekiAlmaTesti()
{
	EAyar bulunanAyar;
	try
	{
		EAyarAlici ayarAlici(AYAR_SNF_LOG,AYAR_ADI_LOG_MAXIMUM_LOG_DOSYA_BOYUTU);
		bulunanAyar = ayarAlici.ayarBul();
		int deger = bulunanAyar.getIntDeger();		
	}
	catch (EException& exc)
	{
		QFAIL(qPrintable(exc.printStackTrace()));
	}
}


void EAyarlarTest::testAktifKullaniciAl()
{	
	try
	{
		EAyarKullanici aktifKullanici = EAyarKullaniciManager::getInstance()->varsayilanKullaniciGetir();
	}
	catch (EException & exc)
	{

	}		
}

void EAyarlarTest::testAktifKullaniciYap()
{
	try
	{
		EAyarKullaniciManager::getInstance()->varsayilanYap("fatihm@ug.uekae");
	}
	catch (EException & exc)
	{
		
	}	
}

void EAyarlarTest::testPasifKullanicilariAl()
{
	try
	{
		EAyarKullaniciManager::getInstance()->varsayilanOlmayanKullanicilariGetir();
	}
	catch (EException &exc)
	{		
	}	
}

void EAyarlarTest::testTumKullanicilariAl()
{
	try
	{
		EAyarKullaniciManager::getInstance()->tumKullanicilariGetir();
	}
	catch (EException &exc)
	{		
	}		
}

void EAyarlarTest::testYereleOlmayanEkleme()
{
	EAyar yeniAyar("YeniSinif","YeniAd",EAyar::AyarStringTipi,"YeniDeger",true,false,"ACiklamasi","Basligi",false);
	EYerelAyarManager::getInstance()->ayarEkle(yeniAyar);
}

void EAyarlarTest::testYereleOlanEklemeyeCalisma()
{
}

void EAyarlarTest::testGeneleEkleme()
{
}

void EAyarlarTest::testGeneleOlaniEklemeyeCalisma()
{
}

void EAyarlarTest::testYerelAyarGuncellemeOlan()
{
	/*
	EAyar yeniAyar("YeniSinif","YeniAd",EAyar::AyarStringTipi,"YeniDeger",true,false,"ACiklamasi","Basligi",false);
	yeniAyar.setDegerValue(QVariant::fromValue(QString("Guncellenen deger")));
	EYerelAyarManager::getInstance()->ayarGuncelle(yeniAyar);
	*/


	EAyarAlici ayarAlici("IslemOzellikleri","SonucDialogGoster");
	EAyar guncellenecekAyar = ayarAlici.ayarBul();
	guncellenecekAyar.setDegerValue(QVariant::fromValue(true));
	EYerelAyarManager::getInstance()->ayarGuncelle(guncellenecekAyar);


}

void EAyarlarTest::testYerelAyarGuncellemeOlmayan()
{
}

void EAyarlarTest::testGenelAyarGuncellemeOlan()
{
}

void EAyarlarTest::testGenelAyarGuncellemeOlmayan()
{
}

void EAyarlarTest::testTumAyarlariAl()
{
	QList<EAyar> tumAyarList = EAyarAlici::tumAyarlariAl();
}
