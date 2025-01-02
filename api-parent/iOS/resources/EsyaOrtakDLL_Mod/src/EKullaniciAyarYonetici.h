#ifndef _E_KULLANICI_AYAR_YONETICI_H_
#define _E_KULLANICI_AYAR_YONETICI_H_
#include "EsyaOrtak_Ortak.h"
#include "EAyarLdap.h"
/**
 * \ingroup EsyaOrtakDLL
 *
 * Aktif kullanýcýnýn ayarlarýný almak icin kullanýlan sýnýf 
 *
 * \version 1.0
 * first version
 *
 * \date 06-05-2009
 *
 * \author ramazang
 * 
 * 
 * \todo 
 *
 * \bug 
 *
 */
NAMESPACE_BEGIN(esya)
class Q_DECL_EXPORT EKullaniciAyarYonetici
{	
	QString mKullaniciEMail;
public:	
	EKullaniciAyarYonetici(const QString & iKullaniciEMail = "");
	~EKullaniciAyarYonetici(void);
	
	bool	getWorkingAsPortable()const;

	bool	getSertifikaDogrulama_CevrimDisiCalis();
	void    setSertifikaDogrulama_CevrimDisiCalis(bool iCevrimDisiCalis);
	QString getSertifikaDogrulama_CevrimDisiDogrulamaParametresi();
	QString getSertifikaDogrulama_CevrimIciDogrulamaParametresi();
	QString getSertifikaDogrulamaPolitikasi();

	//Sadece belirli bir tip LDAP ile çalýþmayý desteklemek için 
	EAyarLdap::LDAP_TIPI getCalisilacakLDAPTipi();
	void				 setCalisilacakLDAPTipi(EAyarLdap::LDAP_TIPI iCalisilacakLDAPTipi);

};

NAMESPACE_END
#endif
