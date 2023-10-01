#ifndef ISPRIMENUMBER_H
#define ISPRIMENUMBER_H

#include "ValueIndependantTester.h"

class IsPrimeNumber: public ValueIndependantTester{
    private:
        static int numAliveObjects;
    public:
        IsPrimeNumber();
        ~IsPrimeNumber();
        bool evaluate(int val);
        static int getNumAliveObjects();
        NumberTester* clone();
};

#endif