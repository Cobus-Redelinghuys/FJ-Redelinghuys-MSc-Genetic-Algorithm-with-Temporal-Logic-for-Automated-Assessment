#include "IsDivisible.h"

int IsDivisible::numAliveObjects = 0;

IsDivisible::IsDivisible(int value): ValueDependantTester(value)
{
    numAliveObjects++;
}

IsDivisible::~IsDivisible()
{
    numAliveObjects--;
}

bool IsDivisible::evaluate(int val)
{
    if(val % value == 0){
        return true;
    }
    return false;
}

int IsDivisible::getNumAliveObjects()
{
    return numAliveObjects;
}

NumberTester* IsDivisible::clone()
{
    return new IsDivisible(value);
}
