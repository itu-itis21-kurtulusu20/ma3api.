#ifndef __CLASSLIST__
#define __CLASSLIST__


#include "ortak.h"
#include "attrcert.h"


namespace esya
{
	class Q_DECL_EXPORT ClassList : public EASNWrapperTemplate<ASN1T_ATTRCERT_ClassList,ASN1C_ATTRCERT_ClassList>
	{
		int				mNumBits;
		unsigned char	mData;
	
	public:
		ClassList(void);
		ClassList(const QByteArray & iClassList);
		ClassList(const ASN1T_ATTRCERT_ClassList & iClassList );
		ClassList(const ClassList& iClassList);

		ClassList& operator=(const ClassList& iClassList);
		friend bool operator==( const ClassList& iRHS, const ClassList& iLHS);
		friend bool operator!=( const ClassList& iRHS, const ClassList& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_ClassList & iClassList);
		int copyToASNObject(ASN1T_ATTRCERT_ClassList &oClassList)const;
		void freeASNObject(ASN1T_ATTRCERT_ClassList& oClassList)const;

		virtual ~ClassList(void);

		// GETTERS AND SETTERS

		int	getNumBits()const;
		unsigned char getData()const;

		void setNumBits(int iNumBits);
		void setData(unsigned char iData);

	};

}

#endif

