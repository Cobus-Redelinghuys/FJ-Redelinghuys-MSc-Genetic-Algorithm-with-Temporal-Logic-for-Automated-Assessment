#include "IsGreater.h"

int IsGreater::numAliveObjects = 0;

IsGreater::IsGreater(int value): ValueDependantTester(value)
{
    numAliveObjects++;
}

IsGreater::~IsGreater()
{
    numAliveObjects--;
}

bool IsGreater::evaluate(int val)
{
    if(val > value){
        return true;
    }
    return false;
}

int IsGreater::getNumAliveObjects()
{
    return numAliveObjects;
}

NumberTester* IsGreater::clone()
{
    return new IsGreater(value);
}
