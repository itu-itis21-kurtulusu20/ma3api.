#ifndef __EAYARLARTEST_H__
#define __EAYARLARTEST_H__

#include <QObject>
#include "EAyarlar.h"
#include <QtTest>

using namespace esya;

class EAyarlarTest: public QObject
{
	Q_OBJECT
public:
	EAyarlarTest(void);
	~EAyarlarTest(void);

private slots:

	void testTumAyarlariAl();

	void testYerelAyarGuncellemeOlan();
	void testYerelAyarGuncellemeOlmayan();
	void testGenelAyarGuncellemeOlan();
	void testGenelAyarGuncellemeOlmayan();

	void testYereleOlmayanEkleme();
	void testYereleOlanEklemeyeCalisma();
	void testGeneleEkleme();
	void testGeneleOlaniEklemeyeCalisma();
	
	void testAktifKullaniciAl();
	void testAktifKullaniciYap();
	void testPasifKullanicilariAl();
	void testTumKullanicilariAl();

	void sadeceGeneldekiAyarAlmaTesti();
	void sadeceYereldekiAyarAlmaTesti();
	void hemYereldekiHemYereldekiAlmaTesti();
	void sadeceGeneldenAlmaTesti();

	
	void ayarTestleri();
	void gdmOnTanimliDizinlerTesti();
	void testKullaniciSil();
	void grubaAyniElemanEklenmesiTesti();
	void tumAyarlariAlmaTesti();
	void ozneAdiDegistirme();
	void ozneliGrupSilmeTesti();
	void ozneSilTamamenTesti();
	void ayarAlDefaultValueluTest();
	void kartTestleri();
	void vtVarkenCalismasi();
	void kullaniciTestleri();
	void gdmTestleri();
	void gdmOzneSilmeDegistirmeTestleri();
	void ldapTestleri();
};


#endif
