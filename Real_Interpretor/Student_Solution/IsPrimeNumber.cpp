#include "IsPrimeNumber.h"

int IsPrimeNumber::numAliveObjects = 0;

IsPrimeNumber::IsPrimeNumber() {
    numAliveObjects++;
}

IsPrimeNumber::~IsPrimeNumber() {
    numAliveObjects--;
}

bool IsPrimeNumber::evaluate(int val) {
    for (int i = 2; i < val; i++) {
        if (val % i == 0) {
            return false;
        }
    }
    return true;
}

NumberTester *IsPrimeNumber::clone() {
    return new IsPrimeNumber();
}

int IsPrimeNumber::getNumAliveObjects() {
    return numAliveObjects;
}