using System;
using System.IO;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace tr.gov.tubitak.uekae.esya.api.asn
{
    [Serializable]
    public class BaseASNWrapper<T> where T : Asn1Type
    {
        protected static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected T mObject;

        private readonly object encodeLock = new object();

        public BaseASNWrapper(T aObject)
        {            
                mObject = aObject;
        }

        public BaseASNWrapper(byte[] aBytes, T aObject)
            : this(aObject)
        {

            try
            {
                AsnIO.arraydenOku(mObject, aBytes);
            }
            catch (Exception x)
            {
                throw new ESYAException(x);

            }
        }

        public BaseASNWrapper(String aBase64Encoded, T aObject)
            : this(aObject)
        {
            try
            {
                //AsnIO.arraydenOku(mObject, Base64.decode(aBase64Encoded));
                AsnIO.arraydenOku(mObject, Convert.FromBase64String(aBase64Encoded));
            }
            catch (Exception x)
            {
                throw new ESYAException(x);

            }
        }

        public BaseASNWrapper(Stream aStream, T aObject)
            : this(aObject)
        {
            try
            {
                using (aStream)
                {
                    mObject = aObject;
                    Asn1DerDecodeBuffer b = new Asn1DerDecodeBuffer(aStream);
                    (mObject).Decode(b);
                }
            }
            catch (Exception x)
            {
                throw new ESYAException(x);
            }

        }




        /**
         * @return AsnObject as der encoded byte[].
         */
        public byte [] getEncoded()
        {
            return getBytes();
        }


        /**
         * get AsnObject as byte[].
         */
        public byte[] getBytes()
        {
            lock (encodeLock)
            {
                Asn1DerEncodeBuffer enc = new Asn1DerEncodeBuffer();
                try
                {
                    (mObject).Encode(enc);
                }
                catch (Exception aEx)
                {
                    logger.Error("Encode edilirken hata olustu", aEx);
                    throw new ESYARuntimeException("Cannot encode " + mObject, aEx);
                }
                return enc.MsgCopy;
            }
        }


        public T getObject()
        {
            return mObject;
        }


        public override bool Equals(Object obj)
        {
            var wrapper = obj as BaseASNWrapper<T>;
            if (wrapper != null)
            {
                return UtilEsitlikler.esitMi(mObject, wrapper.getObject());
            }
            return base.Equals(obj);
        }

        public override int GetHashCode()
        {
            return base.GetHashCode();
        }
        // util methods       
        protected static V[] extendArray<V>(V[] aArray, V aNewMember)
        {
            int oldSize = aArray == null ? 0 : aArray.Length;
            Type type = (aArray == null) ? aNewMember.GetType() : aArray.GetType().GetElementType();
            // create new array
            V[] newArray = (V[])Array.CreateInstance(type, oldSize + 1);
            // copy old members to new one
            if (aArray != null)
                Array.Copy(aArray, newArray, oldSize);
            //set the new member
            newArray[oldSize] = aNewMember;
            return newArray;
        }

        protected static V[] extendArray<V>(V[] aArray, V[] aNewMembers)
        {
            if (aNewMembers == null)
                throw new ESYARuntimeException("Extend array expected array, but got null ");

            int oldSize = (aArray == null) ? 0 : aArray.Length;
            Type type = (aArray == null) ? aNewMembers.GetType() : aArray.GetType().GetElementType();

            // create new array
            V[] newArray = (V[])Array.CreateInstance(type, oldSize + aNewMembers.Length);

            // copy old members to new one
            if (aArray != null)
                Array.Copy(aArray, 0, newArray, 0, oldSize);

            //set the new member
            Array.Copy(aNewMembers, 0, newArray, oldSize, aNewMembers.Length);

            return newArray;
        }

        protected static V[] removeFromArray<V>(V[] aArray, V aObject)
        {
            if (aArray == null || aArray.Length == 0)
                throw new ESYAException("Array is null or empty");

            Type clazz = aArray.GetType().GetElementType();
            int newSize = aArray.Length - 1;
            V[] newArray = (V[])Array.CreateInstance(clazz, newSize);//(V[])Array.newInstance(clazz, newSize);

            int i = 0;
            foreach (V v in aArray)
            {
                if (!v.Equals(aObject))
                {
                    if (i >= newSize)
                        throw new ESYAException("Object can not find in the array");
                    newArray[i] = v;
                    i++;
                }
            }
            return newArray;
        }


        //@SuppressWarnings("unchecked")
        protected static /*<U extends Asn1Type, W extends BaseASNWrapper<U>>*/ U[] unwrapArray<U, W>(W[] aArray)
            where U : Asn1Type
            where W : BaseASNWrapper<U>
        {
            if (aArray == null)
                return null;
            

            //Class clazz = aArray.getClass().getTypeParameters()[0].getClass();
            //Type genericBaseAsnWrapper = aArray.GetType().GetElementType().BaseType;
            //Type clazz = genericBaseAsnWrapper.GetGenericTypeDefinition();
            //Class clazz = (Class) genericBaseAsnWrapper.getActualTypeArguments()[0];  // Arrays has only 1 argument and its safe because W extends BaseASNWrapper<U>
            //U[] newArray = (U[])Array.CreateInstance(clazz, aArray.Length);
           
            U[] newArray = new U[aArray.Length];

            for (int i = 0; i < aArray.Length; i++)
            {
                newArray[i] = (aArray[i] as BaseASNWrapper<U>).getObject();
            }
            return newArray;
        }

        //@SuppressWarnings("unchecked")
        protected static /*<U extends Asn1Type, W extends BaseASNWrapper<U>> */W[] wrapArray<W, U>(U[] aArray, Type/*Class<W>*/ aClazz)
            where U : Asn1Type
            where W : BaseASNWrapper<U>
        {
            if (aArray == null)
                return null;

            W[] newArray = (W[])Array.CreateInstance(aClazz, aArray.Length);
            try
            {
                ConstructorInfo c = aClazz.GetConstructor(new Type[] { aArray.GetType().GetElementType() }/*.getClass().getComponentType()*/);
                for (int i = 0; i < aArray.Length; i++)
                {
                    newArray[i] = (W)c.Invoke(new Object[] { aArray[i] });// new W(U);
                }
            }
            catch (Exception x)
            {
                throw new ESYARuntimeException("Wrap error!", x);
            }

            return newArray;
        }

    }

}
