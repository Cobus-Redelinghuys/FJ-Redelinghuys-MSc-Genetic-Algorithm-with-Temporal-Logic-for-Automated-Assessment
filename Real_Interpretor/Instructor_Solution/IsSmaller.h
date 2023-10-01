#ifndef ISSMALLER_H
#define ISSMALLER_H

#include "ValueDependantTester.h"

class IsSmaller: public ValueDependantTester{
    private:
        static int numAliveObjects;
    public:
        IsSmaller(int value);
        ~IsSmaller();
        bool evaluate(int val);
        static int getNumAliveObjects();
        NumberTester* clone();
};

#endif