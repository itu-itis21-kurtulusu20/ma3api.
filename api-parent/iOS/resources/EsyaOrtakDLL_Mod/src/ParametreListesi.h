#ifndef __PARAMETRELISTESI__
#define __PARAMETRELISTESI__

#include <QString>
#include <QVariant>
#include <QHash>

namespace esya
{
	/**
	* <p>Politika olarak tanýmlanan classlarýn kullanacaðý argüman listesinin tutulduðu yapý</p>
	* <p>{@link PolitikaSinifi} classýnda class politika parametresi olarak
	* kullanýlan bu yapý String-Object ikililerinden oluþan bir liste içermektedir.</p>
	* 
	* @author IH
	*
	*/
	class Q_DECL_EXPORT ParametreListesi
	{
		QHash<QString,QVariant> mParametreListesi;	
	public:
		ParametreListesi(void);
		ParametreListesi(const QHash<QString, QVariant> & aParametreListesi);
		ParametreListesi(const ParametreListesi& );
		
		ParametreListesi& operator=(const ParametreListesi& iPL);
		void parametreEkle(const QString & aParamIsmi, const QVariant & aParamDeger);
		
		const QVariant parametreDegeriAl(const QString aParamIsmi) const ;


		const	QHash<QString,QVariant> & parametreListesiAl()const;	
				QHash<QString,QVariant> & parametreListesiAl();	

		QString	parametreDegeriStringAl	(const QString &aParamIsmi) const;
		int		parametreDegeriIntegerAl(const QString &aParamIsmi) const;
		long	parametreDegeriLongAl	(const QString &aParamIsmi) const;
		bool	parametreDegeriBooleanAl(const QString &aParamIsmi) const;

		QString	parametreDegeriStringAl	(const QString &aParamIsmi, const QString& iDefaultValue) const;
		int		parametreDegeriIntegerAl(const QString &aParamIsmi, int iDefaultValue) const;
		long	parametreDegeriLongAl	(const QString &aParamIsmi, long iDefaultValue) const;
		bool	parametreDegeriBooleanAl(const QString &aParamIsmi, bool iDefaultValue) const;
		
	public:
		virtual ~ParametreListesi(void);
	};
}


#endif

