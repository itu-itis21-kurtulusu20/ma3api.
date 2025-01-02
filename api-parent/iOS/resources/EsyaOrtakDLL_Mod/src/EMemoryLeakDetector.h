#ifndef _E_MEMORY_LEAK_DETECTOR_H_
#define _E_MEMORY_LEAK_DETECTOR_H_

#include <QtGlobal>
typedef void (*FUNC)(); 

class Q_DECL_EXPORT EMemoryLeakDetector
{
public:
	EMemoryLeakDetector(void);
	virtual ~EMemoryLeakDetector(void);
	static bool hasMemoryLeak(FUNC iFuncPtrs);
};
#endif