#ifndef ISDIVISIBLE_H
#define ISDIVISIBLE_H

#include "ValueDependantTester.h"

class IsDivisible: public ValueDependantTester{
    private:
        static int numAliveObjects;
    public:
        IsDivisible(int value);
        ~IsDivisible();
        bool evaluate(int val);
        static int getNumAliveObjects();
        NumberTester* clone();
};

#endif