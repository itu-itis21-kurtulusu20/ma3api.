#ifndef _TEST_E_AYAR_LDAP_H_
#define _TEST_E_AYAR_LDAP_H_

#include <QObject>
class TestEAyarLDAP:public QObject
{
	Q_OBJECT
public:
	TestEAyarLDAP(void);
	~TestEAyarLDAP(void);
private slots:
    void testTumLDAPlariAl();
};

#endif