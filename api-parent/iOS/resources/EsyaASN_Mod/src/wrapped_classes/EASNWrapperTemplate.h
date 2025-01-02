#ifndef __EASNWRAPPERTEMPLATE__
#define __EASNWRAPPERTEMPLATE__


#include <QByteArray>
#include <QString>
#include <QFile>
#include "EException.h"
#include "asn1CppTypes.h"
#include "esyaOrtak.h"
#include "asn1compat.h"
#include "ESeqOfList.h"
#include "algorithms.h"
#include "esyaOrtak.h"
//#include "ELogger.h"

#define ST_ASN_OBJECT "ASN Wrapped Object"
#define FAILURE -1
#define SUCCESS  0

namespace esya
{
	
	/**
	* \ingroup EsyaASN
	* 
	* ASN1 wrapper ata sýnýfý. 
	*
	*
	* \author dindaro
	*
	*/
	template <class T,class C>
	class Q_DECL_EXPORT EASNWrapperTemplate
	{
	public:
		EASNWrapperTemplate(void){};

	public:
		
		virtual void freeASNObject(T&) const = 0;
		virtual int copyToASNObject(T&) const = 0;
		virtual int copyFromASNObject(const T&) = 0;

		QByteArray getEncodedBytes() const
		{
			ASN1BEREncodeBuffer encBuf;
			T * pT = getASNCopy();
			C c(*pT);

			int stat = c.EncodeTo(encBuf);
			if (stat < ASN_OK)
			{
				throw EException(QString("ASNObject Encode edilemedi Hata : %1").arg(stat));
			}

			freeASNObjectPtr(pT);
			return QByteArray((char*) encBuf.getMsgPtr(),stat);
		}

		int constructObject(const QByteArray& iEncodedBytes)
		{
            ASN1BERDecodeBuffer decBuf((OSOCTET*)iEncodedBytes.data(),iEncodedBytes.size());
			T t;
			C c(t);

            int stat = c.DecodeFrom(decBuf);
			if (stat != ASN_OK)
			{
				throw EException(QString("ASNObject Decode edilemedi. Hata : %1").arg(stat),__FILE__, __LINE__ );
			}

			int res = copyFromASNObject(t);

			return res;
		}


		T * getASNCopy()const
		{
			return getASNCopyOf(*this);
		}


		T * getASNCopyOf(const EASNWrapperTemplate & iObject )const
		{
			T * newT= new T();
			iObject.copyToASNObject(*newT);
			return newT;
		}

		void freeASNObjectPtr(T* pT) const 
		{
			if (pT)
				freeASNObject(*pT);
			DELETE_MEMORY(pT);
		};

		void freeASNObjects(ASN1TPDUSeqOfList & oObjects)const
		{
			if (oObjects.count == 0)
				return;

			OSRTDListNode * nodePtr =  oObjects.head;
			while(nodePtr)
			{
				T * pT =(T *) nodePtr->data;
				freeASNObjectPtr(pT);
				nodePtr = nodePtr->next;
			}

			ESeqOfList::free(oObjects);
		}


		template <class W>
		int copyASNObjects(const ASN1TPDUSeqOfList & iASNObjects ,QList<W>& oList)const
		{
			if (iASNObjects.count == 0)
				return SUCCESS;
			
			OSRTDListNode * nodePtr =  iASNObjects.head;
			while(nodePtr)
			{
				T * pT =(T *) nodePtr->data;
				oList.append(W(*pT));
				nodePtr = nodePtr->next;
			}


// 			for (int i = 0 ; i<iASNObjects.count ; i++	)
// 			{
// 				T* ext = (T*)ESeqOfList::get(iASNObjects,i);
// 				oList.append(W(*ext));
// 			}
			return SUCCESS;
		}

		template <class W>
		int copyASNObjects(const QList<W> iList ,ASN1TPDUSeqOfList& oASNObjects)const
		{
			oASNObjects.count = 0;

			for (int i = 0 ; i<iList.size() ; i++	)
			{
				T* pT = iList[i].getASNCopy();
				ESeqOfList::append(oASNObjects,pT);
			}
			return SUCCESS;
		}

		template <class ST, class SC ,class W>
		int	copyASNObjects(const QByteArray & iASNBytes, QList<W>& oList)const
		{
			ASN1BERDecodeBuffer decBuf;
			decBuf.setBuffer((OSOCTET*)iASNBytes.data(), iASNBytes.size()) ;

			ST objects;
			SC cObjects(objects);

			if ( cObjects.DecodeFrom(decBuf) != ASN_OK )
			{
				throw EException("Seq Of ASNObject okunamadý",__FILE__,__LINE__);
			}

			copyASNObjects<W>(objects,oList);
			return SUCCESS;
		}


		template <class ST, class SC ,class W>
		int	copyASNObjects(const QList<W>& iList,QByteArray & oASNBytes)const
		{
			ST objects;
			copyASNObjects<W>(iList,objects);
			SC cObjects(objects);
			ASN1BEREncodeBuffer encBuf;

			int len = cObjects.EncodeTo(encBuf);

			if (len <= 0 )
			{
				throw EException("Seq Of ASNObject encode edilemedi",__FILE__,__LINE__);
			}

			oASNBytes.append(QByteArray((const char*)encBuf.getMsgPtr(),len));
			freeASNObjects(objects);
			return SUCCESS;
		}


		virtual QString toString() const
		{
			return QString(ST_ASN_OBJECT);
		};


		/**
		* \brief
		* Nesneyi ASN1-encoded þekilde dosyaya yazar
		*
		* \param 		const QString & iFileName
		* Dosya Yolu
		*
		*/
		int write2File(const QString &iFileName) const
		{
			QByteArray data = getEncodedBytes();
			if ( data.size() == 0 )
			{
				return FAILURE;
			}
			QFile file(iFileName);
			if ( !file.open(QIODevice::WriteOnly))
				throw EException(" write2File() : Dosya açýlamadý",__FILE__,__LINE__);
			file.write(data);
			file.close();
			return SUCCESS;
		}

		/**
		* \brief
		* Nesneyi ASN1-encoded dosyadan yükler
		*
		* \param 		const QString & iFileName
		* Dosya Yoku
		*
		* \return   	int
		*/
		int  loadFromFile(const QString & iFileName)
		{
			QByteArray bytes;
			QFile file(iFileName);
			if (!file.open(QIODevice::ReadOnly))
			{		
				throw EException(" loadFromFile() : Dosya bulunamadý.",__FILE__,__LINE__);
			}
			bytes = file.readAll();
			file.close();

			try
			{
				return constructObject(bytes);
			}
			catch (EException&  exc)
			{
				bytes = bytes.trimmed();	
				if (bytes.left(2) == "--" )
					bytes.remove(0,bytes.indexOf("\n"));

				bytes = QByteArray::fromBase64(bytes);
				try
				{
					return constructObject(bytes);
				}
				catch (EException exc2)
				{
					throw exc.append(exc2.printStackTrace());		
				}
			}
		}


		virtual ~EASNWrapperTemplate(void){};


	};

}

#endif 

