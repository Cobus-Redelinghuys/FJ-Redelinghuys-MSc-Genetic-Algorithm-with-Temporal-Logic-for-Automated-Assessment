#include "IsGreater.h"

int IsGreater::numAliveObjects = 0;

IsGreater::IsGreater(int value) : ValueDependantTester(value) {
    numAliveObjects++;
}

IsGreater::~IsGreater() {
    numAliveObjects--;
}

bool IsGreater::evaluate(int val) {
    return val > value;
}

NumberTester *IsGreater::clone() {
    return new IsGreater(value);
}

int IsGreater::getNumAliveObjects() {
    return numAliveObjects;
}