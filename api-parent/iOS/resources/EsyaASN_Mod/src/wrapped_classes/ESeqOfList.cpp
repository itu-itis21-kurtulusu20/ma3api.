
#include "ESeqOfList.h"

using namespace esya;




 const void* ESeqOfList::get(const ASN1TPDUSeqOfList & list, int index)
{
	if ( index >= list.count ) return NULL;

	OSRTDListNode * nodePtr =  list.head;
	for (int i = 0; i < list.count ; i++)
	{
		if ( i == index )	return (ASN1TPDU*)nodePtr->data;
		nodePtr = nodePtr->next;
	}
	return (ASN1TPDU*)nodePtr->data;
}

void ESeqOfList::append(ASN1TPDUSeqOfList & list, const void * node )
 {
	OSRTDListNode * nodePtr =  list.head;	

	if (list.count == 0 )
	{
		list.head = list.tail = nodePtr = new OSRTDListNode();
		nodePtr->prev = nodePtr->next = NULL;
	}
	else 
	{
		nodePtr = new OSRTDListNode();
		nodePtr->next = NULL;
		nodePtr->prev = list.tail;
		list.tail->next = nodePtr;
		list.tail = nodePtr;
	}
	list.tail->data = (void*)node;
	list.count++;
	return;
 }

void ESeqOfList::free(ASN1TPDUSeqOfList & list)
{
	OSRTDListNode *tmp,* nodePtr  =  list.head;	

	if (list.count == 1)
	{
		delete nodePtr;
		nodePtr = NULL;
	}
	while (nodePtr&& nodePtr != list.tail )
	{
		tmp = nodePtr; 
		nodePtr = nodePtr->next;
		delete tmp;
	}
	if (nodePtr) 
		delete nodePtr;
}
 
 ESeqOfList::~ESeqOfList(void)
{
}
