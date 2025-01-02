#ifndef _ATOMIC_WORK_H_
#define _ATOMIC_WORK_H_

class AtomicWork
{
protected:
	bool mIsFinished;
	bool mIsUndone;
public:
	AtomicWork(void);
	virtual ~AtomicWork(void);

	virtual void finish()=0;
	virtual bool undo()=0;
	virtual void start()=0;

	bool isActive();
	bool isFinished(){return mIsFinished;};	
	bool isUndone(){return mIsUndone;};


};
#endif
