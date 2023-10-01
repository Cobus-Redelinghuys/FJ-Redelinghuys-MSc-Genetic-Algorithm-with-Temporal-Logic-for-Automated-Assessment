#include "IsSmaller.h"

int IsSmaller::numAliveObjects = 0;

IsSmaller::IsSmaller(int value): ValueDependantTester(value)
{
    numAliveObjects++;
}

IsSmaller::~IsSmaller()
{
    numAliveObjects--;
}

bool IsSmaller::evaluate(int val)
{
    if(val < value)
        return true;
    return false;
}

int IsSmaller::getNumAliveObjects()
{
    return numAliveObjects;
}

NumberTester* IsSmaller::clone()
{
    return new IsSmaller(value);
}
