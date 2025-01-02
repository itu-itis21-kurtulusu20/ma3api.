#ifndef __TARGET__
#define __TARGET__


#include "TargetCert.h"



namespace esya
{

	class Q_DECL_EXPORT Target : public EASNWrapperTemplate<ASN1T_ATTRCERT_Target,ASN1C_ATTRCERT_Target>
	{
	public:
		enum TargetType 
		{
			TT_TargetName = T_ATTRCERT_Target_targetName  ,
			TT_TargetGroup = T_ATTRCERT_Target_targetGroup , 
			TT_TargetCert = T_ATTRCERT_Target_targetCert   
		};

	protected:
		
		TargetType mType;

		GeneralName mTargetName;
		GeneralName mTargetGroup;
		TargetCert	mTargetCert;

	public:
		Target(void);
		Target(const QByteArray & iTarget);
		Target(const ASN1T_ATTRCERT_Target & iTarget );
		Target(const Target& iTarget);

		Target& operator=(const Target& iTarget);
		friend bool operator==( const Target& iRHS, const Target& iLHS);
		friend bool operator!=( const Target& iRHS, const Target& iLHS);

		int copyFromASNObject(const ASN1T_ATTRCERT_Target & iTargetr);
		int copyToASNObject(ASN1T_ATTRCERT_Target &oTarget)const;
		void freeASNObject(ASN1T_ATTRCERT_Target& oTarget)const;

		int copyTargets(const ASN1T_ATTRCERT_Targets & iTargets, QList<Target>& oList);
		int copyTargets(const QList<Target> iList ,ASN1T_ATTRCERT_Targets & oTargets);	
		int copyTargets(const QByteArray & iASNBytes, QList<Target>& oList);

		virtual ~Target(void);

		// GETTERS AND SETTERS

		const TargetType&  getType()const;
		const GeneralName& getTargetName()const;
		const GeneralName& getTargetGroup()const;
		const TargetCert&  getTargetCert()const;

		void setType(const TargetType&  iType);
		void setTargetName(const GeneralName& iTargetName);
		void setTargetGroup(const GeneralName& iTargetGroup);
		void setTargetCert(const TargetCert&  iTargetCert);
	};

}

#endif

