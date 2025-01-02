#ifndef _GUID_GENERATOR_H_
#define _GUID_GENERATOR_H_

#include <QString>

class GUIDGenerator
{
public:
	GUIDGenerator(void);
	static QString generate();
	virtual ~GUIDGenerator(void);
};
#endif