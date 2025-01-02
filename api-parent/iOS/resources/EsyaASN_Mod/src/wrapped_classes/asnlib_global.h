#ifndef ASNLIB_GLOBAL_H
#define ASNLIB_GLOBAL_H

#include <Qt/qglobal.h>

#ifdef ASNLIB_LIB
# define ASNLIB_EXPORT Q_DECL_EXPORT
#else
# define ASNLIB_EXPORT Q_DECL_IMPORT
#endif

#endif // ASNLIB_GLOBAL_H
