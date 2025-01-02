#include "GUIDGenerator.h"
#include <QUuid>

GUIDGenerator::GUIDGenerator(void)
{
}

GUIDGenerator::~GUIDGenerator(void)
{
}

QString GUIDGenerator::generate()
{
	QUuid  id = QUuid::createUuid();
	return id.toString().toUpper();
}
