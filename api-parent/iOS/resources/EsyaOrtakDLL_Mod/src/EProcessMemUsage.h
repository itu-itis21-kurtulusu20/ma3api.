#ifndef _E_PROCESS_MEM_USAGE_H
#define _E_PROCESS_MEM_USAGE_H

#include <QString>
#define SIZE_OF_K 1024

class Q_DECL_EXPORT EProcessMemUsage
{
	double mMemoryUsageK;	
public:	
	EProcessMemUsage(void);
	~EProcessMemUsage(void);	
	void reset();
	int getStartMemoryUsage() const{return mMemoryUsageK;};
	int getCurrentMemoryUsage();
	int getMemoryUsageDiff();
	QString getMemUsageDiffStr();

};
#endif
