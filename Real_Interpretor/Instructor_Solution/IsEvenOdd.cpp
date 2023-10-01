#include "IsEvenOdd.h"

int IsEvenOdd::numAliveObjects = 0;

IsEvenOdd::IsEvenOdd()
{
    numAliveObjects++;
}

IsEvenOdd::~IsEvenOdd()
{
    numAliveObjects--;
}

bool IsEvenOdd::evaluate(int val)
{
    if(val%2 == 0)
        return true;
    return false;
}

int IsEvenOdd::getNumAliveObjects()
{
    return numAliveObjects;
}

NumberTester* IsEvenOdd::clone()
{
    return new IsEvenOdd();
}
