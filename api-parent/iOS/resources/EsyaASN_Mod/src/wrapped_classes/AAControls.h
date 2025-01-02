#ifndef __AACONTROLS__
#define __AACONTROLS__


#include "AttrSpec.h"


namespace esya
{
	class Q_DECL_EXPORT AAControls : public EASNWrapperTemplate<ASN1T_ATTRCERT_AAControls,ASN1C_ATTRCERT_AAControls>
	{
		bool mPathLenConstraintPresent ;
		bool mPermittedAttrsPresent ;
		bool mExcludedAttrsPresent ;

		int		 mPathLenConstraint;
		AttrSpec mPermittedAttrs;
		AttrSpec mExcludedAttrs;
		bool	 mPermitUnSpecified;

	public:
		AAControls(void);
		AAControls(const QByteArray & iAAControls);
		AAControls(const ASN1T_ATTRCERT_AAControls & iAAControls );
		AAControls(const AAControls& iAAControls);

		AAControls& operator=(const AAControls& iAAControls);
		friend bool operator==( const AAControls& iRHS, const AAControls& iLHS);
		friend bool operator!=( const AAControls& iRHS, const AAControls& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_AAControls & iAAControls);
		int copyToASNObject(ASN1T_ATTRCERT_AAControls &oAAControls)const;
		void freeASNObject(ASN1T_ATTRCERT_AAControls& oAAControls);

		virtual ~AAControls(void);


		// GETTERS AND SETTERS

		bool isPathLenConstraintPresent()const ;
		bool isPermittedAttrsPresent()const ;
		bool isExcludedAttrsPresent()const ;

		int	getPathLenConstraint()const;
		const AttrSpec & getPermittedAttrs()const;
		const AttrSpec & getExcludedAttrs()const;
		bool getPermitUnSpecified()const;

		void setPathLenConstraintPresent(bool ) ;
		void setPermittedAttrsPresent(bool );
		void setExcludedAttrsPresent(bool );

		void setPathLenConstraint(int);
		void setPermittedAttrs(const AttrSpec & iPA);
		void setExcludedAttrs(const AttrSpec & iEA );
		void setPermitUnSpecified(bool iPU);


	};

}

#endif

