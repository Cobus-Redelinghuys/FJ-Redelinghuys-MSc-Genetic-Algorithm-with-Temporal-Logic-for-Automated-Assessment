#include "TesterInterface.h"

#include <cstddef>
TesterInterface::TesterInterface(int maxNumTests) {
    currNumTesters = 0;
    this->maxNumTesters = maxNumTests;
    if (maxNumTests < 1) {
        maxNumTesters = 0;
        testers = new NumberTester *[0];
        return;
    }
    testers = new NumberTester *[maxNumTesters];
    for (int i = 0; i < maxNumTesters; i++) {
        testers[i] = NULL;
    }
}

TesterInterface::TesterInterface(TesterInterface *other) {
    if (other == NULL) {
        currNumTesters = 0;
        maxNumTesters = 0;
        testers = new NumberTester *[0];
        return;
    }

    maxNumTesters = other->maxNumTesters;
    currNumTesters = other->currNumTesters;
    testers = new NumberTester *[maxNumTesters];

    for (int k = 0; k < maxNumTesters; k++) {
        if (other->testers[k]) {
            testers[k] = other->testers[k]->clone();
        } else {
            testers[k] = NULL;
        }
    }
}

TesterInterface::TesterInterface(TesterInterface &other) {
    maxNumTesters = other.maxNumTesters;
    currNumTesters = other.currNumTesters;
    testers = new NumberTester *[maxNumTesters];

    for (int k = 0; k < maxNumTesters; k++) {
        if (other.testers[k]) {
            testers[k] = other.testers[k]->clone();
        } else {
            testers[k] = NULL;
        }
    }
}

TesterInterface::~TesterInterface() {
    for (int k = 0; k < maxNumTesters; k++) {
        if (testers[k]) {
            delete testers[k];
        }
    }
    delete[] testers;
}

int TesterInterface::addTester(NumberTester *tester) {
    if (!tester) {
        return -1;
    }
    for (int k = 0; k < maxNumTesters; k++) {
        if (!testers[k]) {
            testers[k] = tester->clone();
            return k;
        }
    }
    return -1;
}

bool TesterInterface::removeTester(int pos) {
    if (pos < 0 || pos >= maxNumTesters || !testers[pos]) {
        return false;
    }
    delete testers[pos];
    testers[pos] = NULL;
    return true;
}

bool TesterInterface::evaluate(int num) {
    for (int k = 0; k < maxNumTesters; k++) {
        if (testers[k]) {
            if (!testers[k]->evaluate(num)) {
                return false;
            }
        }
    }
    return true;
}

int *TesterInterface::failedTests(int num) {
    int *res = new int[numberOfFailedTests(num)];

    for (int i = 0, c = 0; i < maxNumTesters; i++) {
        if (testers[i]) {
            if (!testers[i]->evaluate(num)) {
                res[c++] = i;
            }
        }
    }

    return res;
}

int TesterInterface::numberOfFailedTests(int num) {
    int res = 0;
    for (int i = 0; i < maxNumTesters; i++) {
        if (testers[i]) {
            if (!testers[i]->evaluate(num)) {
                res++;
            }
        }
    }
    return res;
}

NumberTester *TesterInterface::operator[](int pos) {
    if (pos < 0 || pos >= maxNumTesters) {
        return NULL;
    }
    return testers[pos];
}

int TesterInterface::getCurrNumTesters() const {
    return NumberTester::getNumAliveObjects();
}

int TesterInterface::getMaxNumTesters() const {
    return maxNumTesters;
}
