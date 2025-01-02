using System;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.exceptions
{
    public class DatabaseException : Exception
    {
        /**
     * Constructs a new exception with <code>null</code> as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
        public DatabaseException()
            : base()
        {
        }

        /**
         * Constructs a new exception with the specified detail message.  The
         * cause is not initialized, and may subsequently be initialized by
         * a call to {@link #initCause}.
         * @param aMessage the detail message. The detail message is saved for
         *                later retrieval by the {@link #getMessage()} method.
         */
        public DatabaseException(String aMessage)
            : base(aMessage)
        {
        }

        /**
         * Constructs a new exception with the specified cause and a detail
         * message of <tt>(cause==null ? null : cause.toString())</tt> (which
         * typically contains the class and detail message of <tt>cause</tt>).
         * This constructor is useful for exceptions that are little more than
         * wrappers for other throwables (for example, {@link
         * java.security.PrivilegedActionException}).
         * @param aCause the cause (which is saved for later retrieval by the
         *              {@link #getCause()} method).  (A <tt>null</tt> value is
         *              permitted, and indicates that the cause is nonexistent or
         *              unknown.)
         * @since 1.4
         */
        public DatabaseException(Exception aCause)
            : base(aCause.Message, aCause)
        {
        }

        /**
         * Constructs a new exception with the specified detail message and
         * cause.  <p>Note that the detail message associated with
         * <code>cause</code> is <i>not</i> automatically incorporated in
         * this exception's detail message.
         * @param aMessage the detail message (which is saved for later retrieval
         *                by the {@link #getMessage()} method).
         * @param aCause   the cause (which is saved for later retrieval by the
         *                {@link #getCause()} method).  (A <tt>null</tt> value is
         *                permitted, and indicates that the cause is nonexistent or
         *                unknown.)
         * @since 1.4
         */
        public DatabaseException(String aMessage, Exception aCause)
            : base(aMessage, aCause)
        {
        }
    }
}
