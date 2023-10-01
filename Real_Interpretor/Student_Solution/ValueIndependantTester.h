#ifndef VALUEINDEPENDANTTESTER_H
#define VALUEINDEPENDANTTESTER_H

#include "NumberTester.h"

class ValueIndependantTester: public NumberTester{
    private:
        static int numAliveObjects;
    public:
        ValueIndependantTester();
        virtual ~ValueIndependantTester() = 0;
        virtual bool evaluate(int val) = 0;
        virtual NumberTester* clone() = 0;
        static int getNumAliveObjects();
};

#endif