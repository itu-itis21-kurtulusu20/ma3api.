#ifndef EFILTER_H
#define EFILTER_H

#include <QString>
#include <QList>
#include <QVariant>
#include "esyaOrtak.h"

namespace esya
{
	class CompositeFilter;
	class NotFilter;

	class Q_DECL_EXPORT EFilter  
	{
		friend class CompositeFilter;
		friend class NotFilter;

	protected:
		virtual QString	_toSQL(int &Index) = 0;

	public:

		virtual QList<QVariant> getParams()= 0;

		QString toSQL()
		{
			int index = 1;
			return _toSQL(index);
		}

		EFilter(){};
		virtual ~EFilter(){};

	private:		
	};

	class Q_DECL_EXPORT JoinFieldMatchFilter : public EFilter 
	{
	protected:
		QString		mFirstTableColumName;
		QString		mSecondTableColumName;

		QString mFirstTableName;
		QString mSecondTableName;

		QList<QVariant> getParams(){ return QList<QVariant>();};

		virtual QString _toSQL(int &iCurrentIndex)
		{
			QString  sql = QString("( %1.%2 = %3.%4 )").arg(mFirstTableName).arg(mFirstTableColumName).arg(mSecondTableName).arg(mSecondTableColumName);
			return sql;
		}

	public:	

		JoinFieldMatchFilter(const QString & iFirstTableName,const QString & iFirstTableColumName,const QString & iSecondTableName,const QString & iSecondTableColumName)
			:EFilter(),mFirstTableName(iFirstTableName),mFirstTableColumName(iFirstTableColumName),mSecondTableName(iSecondTableName),mSecondTableColumName(iSecondTableColumName)
		{
		}
	};

	class Q_DECL_EXPORT CompositeFilter : public EFilter
	{
	protected:
		QList<EFilter*> mFilters;

		virtual QString _compositionOperator() = 0;

		virtual QString _toSQL(int &iCurrentIndex)
		{
			QString sql = "( ";
			for (int i= 0 ; i<mFilters.size() ; i++)
			{
				QString rSQL = mFilters[i]->_toSQL(iCurrentIndex);
				if (i)
					sql+= _compositionOperator();
				sql+= rSQL;
			}
			sql+= QString(" )");
			
			return sql;
		};

	public:

		virtual QList<QVariant> getParams()
		{
			QList<QVariant> params;
			for ( int i= 0 ; i<mFilters.size() ; i++ )
			{
				params += mFilters[i]->getParams();
			}
			return params;
		};

		CompositeFilter(EFilter* iRFilter,EFilter* iLFilter )
		{ 
			mFilters.append(iRFilter);
			mFilters.append(iLFilter);
		};
		CompositeFilter(QList<EFilter*> iFilters )
			:mFilters(iFilters) 
		{ 
		};

		void appendFilter(EFilter * iFilter )
		{
			mFilters.append(iFilter);
		}

		virtual ~CompositeFilter()
		{ 
			qDeleteAll(mFilters);
		};
	};

	class Q_DECL_EXPORT ANDFilter : public CompositeFilter 
	{
	protected:
		virtual QString _compositionOperator()
		{
			return "AND";
		};
	public:
		ANDFilter(EFilter* iRFilter, EFilter* iLFilter)
			: CompositeFilter(iRFilter,iLFilter){};
		ANDFilter(QList<EFilter*> iFilters)
			: CompositeFilter(iFilters){};
	};

	class Q_DECL_EXPORT ORFilter : public CompositeFilter 
	{
	protected:
		virtual QString _compositionOperator()
		{
			return "OR";
		};

	public:
		ORFilter(EFilter* iRFilter, EFilter* iLFilter)
			: CompositeFilter(iRFilter,iLFilter){};
		ORFilter(QList<EFilter*> iFilters)
			: CompositeFilter(iFilters){};

	};

	class Q_DECL_EXPORT MatchFilter : public EFilter 
	{
	protected:
		QString		mTableName;
		QString		mColumName;
		QVariant	mValue;

		virtual QString _toSQL(int &iCurrentIndex)
		{
			QString retSQL;
			if (mTableName.isEmpty())
			{
				retSQL = QString("( %1 = :%2 )").arg(mColumName).arg(iCurrentIndex++);
			}
			else
			{
				retSQL = QString("( %1.%2 = :%3 )").arg(mTableName).arg(mColumName).arg(iCurrentIndex++);
			}			
			return retSQL;
		}

	public:

		virtual QList<QVariant> getParams()
		{
			QList<QVariant> params;
			params.append(mValue);
			return params;
		};

		MatchFilter(const QString & iColumnName, const QVariant & iValue)
			:mColumName(iColumnName),mValue(iValue),mTableName(""){};
		MatchFilter(const QString & iTableName,const QString & iColumnName, const QVariant & iValue)
			:mColumName(iColumnName),mValue(iValue),mTableName(iTableName){};

	};

	class Q_DECL_EXPORT LikeFilter : public EFilter 
	{
	protected:
		QString		mColumName;
		QVariant	mValue;

		virtual QString _toSQL(int &iCurrentIndex)
		{
			QString  sql = QString("( %1 LIKE :%2 )").arg(mColumName).arg(iCurrentIndex++);
			return sql;
		}

	public:

		virtual QList<QVariant> getParams()
		{
			QList<QVariant> params;
			params.append(mValue);
			return params;
		};

		LikeFilter(const QString & iColumnName, const QVariant & iValue)
			:mColumName(iColumnName),mValue(iValue)	{};

	};

	class Q_DECL_EXPORT RangeFilter : public EFilter 
	{
	protected:

		QString		mColumName;
		QVariant	mValueMin;
		QVariant	mValueMax;

		virtual QString _toSQL(int &iCurrentIndex)
		{
			QString minSQL = " (1=1) ", maxSQL= " (1=1) ";

			if (!mValueMin.isNull())
				minSQL = QString("( %1 > :%2 )").arg(mColumName).arg(iCurrentIndex++);
			if (!mValueMax.isNull())
				maxSQL = QString("( %1 < :%2 )").arg(mColumName).arg(iCurrentIndex++);
			
			QString sql = QString("(%1 AND %2)").arg(minSQL).arg(maxSQL);
			return sql;
		}

	public:

		virtual QList<QVariant> getParams()
		{
			QList<QVariant> params;
			if (!mValueMin.isNull())
				params.append(mValueMin);
			if (!mValueMax.isNull())
				params.append(mValueMax);
			return params;
		};

		RangeFilter(const QString & iColumnName, const QVariant & iValueMin , const QVariant & iValuMax)
			:mColumName(iColumnName),mValueMin(iValueMin), mValueMax(iValuMax)	{};

	};

	class Q_DECL_EXPORT InFilter : public EFilter 
	{
	protected:
		QString				mColumName;
		QList<QVariant>		mValueSet;

		virtual QString _toSQL(int &iCurrentIndex)
		{
			QString  sql = QString("( %1 IN ( ").arg(mColumName);

			Q_FOREACH(QVariant item,mValueSet)
			{
				sql += QString(":%1,").arg(iCurrentIndex++);
			}
			sql = QString("%1 ))").arg(sql.left(sql.size()-1));
			return sql;
		}

	public:

		virtual QList<QVariant> getParams()
		{
			QList<QVariant> params;
			Q_FOREACH(QVariant item,mValueSet)
			{
				params.append(item);	
			}
			return params;
		};

		InFilter(const QString & iColumnName, const QList<QVariant> & iValueSet)
			:mColumName(iColumnName),mValueSet(iValueSet)	{};

	};

	class Q_DECL_EXPORT NotFilter : public EFilter 
	{
	protected:
		EFilter*	mFilter;

		virtual QString _toSQL(int &iCurrentIndex)
		{
			QString  sql = QString("( NOT %1 )").arg(mFilter->_toSQL(iCurrentIndex));
			return sql;
		}

	public:

		virtual QList<QVariant> getParams()
		{
			return mFilter->getParams();
		};

		NotFilter(EFilter* iFilter)
			:mFilter(iFilter)	{};

		virtual ~NotFilter()
		{
			DELETE_MEMORY(mFilter);
		}

	};
	class Q_DECL_EXPORT IsNotNULLFilter : public EFilter 
	{
		QString mColumnName;
	protected:		

		virtual QString _toSQL(int &iCurrentIndex)
		{
			QString  sql = QString("(%1 IS NOT NULL )").arg(mColumnName);
			return sql;
		}

	public:

		virtual QList<QVariant> getParams()
		{
			return QList<QVariant>();			
		};

		IsNotNULLFilter(const QString & iColumnName)
			:mColumnName(iColumnName)	{};
	};

}

#endif // EFILTER_H
