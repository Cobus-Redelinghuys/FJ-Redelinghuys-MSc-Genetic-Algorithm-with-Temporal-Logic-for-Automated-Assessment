#include "IsDivisible.h"

int IsDivisible::numAliveObjects = 0;

IsDivisible::IsDivisible(int value) : ValueDependantTester(value) {
    numAliveObjects++;
}

IsDivisible::~IsDivisible() {
    numAliveObjects--;
}

bool IsDivisible::evaluate(int val) {
    return val % value == 0;
}

NumberTester *IsDivisible::clone() {
    return new IsDivisible(value);
}

int IsDivisible::getNumAliveObjects() {
    return numAliveObjects;
}