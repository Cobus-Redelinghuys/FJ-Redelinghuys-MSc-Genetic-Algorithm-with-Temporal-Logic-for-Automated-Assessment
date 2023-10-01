#ifndef ISGREATER_H
#define ISGREATER_H

#include "ValueDependantTester.h"

class IsGreater: public ValueDependantTester{
    private:
        static int numAliveObjects;
    public:
        IsGreater(int value);
        ~IsGreater();
        bool evaluate(int val);
        static int getNumAliveObjects();
        NumberTester* clone();
};

#endif