#ifndef ISEVENODD_H
#define ISEVENODD_H

#include "ValueIndependantTester.h"

class IsEvenOdd: public ValueIndependantTester{
    private:
        static int numAliveObjects;
    public:
        IsEvenOdd();
        ~IsEvenOdd();
        bool evaluate(int val);
        static int getNumAliveObjects();
        NumberTester* clone();
};

#endif