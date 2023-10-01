#include "TesterInterface.h"
#include "IsDivisible.h"

#include <iostream>

//#define NULL 0

TesterInterface::TesterInterface(int maxNumTests)
{
    this->currNumTesters = 0;
    this->maxNumTesters = maxNumTests;
    if(maxNumTesters >= 1)
        this->testers = new NumberTester*[maxNumTesters];
    else {
        this->testers = new NumberTester*[0];
        this->maxNumTesters = 0;
    }
    for(int i=0; i < maxNumTesters; i++){
        testers[i] = NULL;
    }
}

TesterInterface::TesterInterface(TesterInterface* other)
{
    if(other == NULL){
        currNumTesters = 0;
        maxNumTesters = 0;
        testers = new NumberTester*[0];
    } else {
        currNumTesters = other->currNumTesters;
        maxNumTesters = other->maxNumTesters;
        testers = new NumberTester*[maxNumTesters];
        for(int i=0; i < maxNumTesters; i++){
            if(other->testers[i] != NULL){
                NumberTester* t = other->testers[i];
                testers[i] = other->testers[i]->clone();
            } else {
                testers[i] = NULL;
            }
        }
    }
}

TesterInterface::TesterInterface(TesterInterface& other)
{
    currNumTesters = other.currNumTesters;
    maxNumTesters = other.maxNumTesters;
    testers = new NumberTester*[maxNumTesters];
    for(int i=0; i < maxNumTesters; i++){
        if(other.testers[i] != NULL){
            testers[i] = other.testers[i]->clone();
        } else {
                testers[i] = NULL;
        }
    }
}

TesterInterface::~TesterInterface()
{
    for(int i=0; i < maxNumTesters; i++){
        if(testers[i] != NULL){
            delete testers[i];
            testers[i] = NULL;
        }
    }
    delete [] testers;
    testers = NULL;
}

int TesterInterface::addTester(NumberTester *tester)
{
    if(currNumTesters >= maxNumTesters || tester == NULL)
        return -1;
    for(int i=0; i < maxNumTesters; i++){
        if(testers[i] == NULL){
            currNumTesters++;
            testers[i] = tester->clone();
            return i;
        }
    }
    return -1;
}

bool TesterInterface::removeTester(int pos)
{
    if(pos >= maxNumTesters || pos < 0){
        return false;
    }

    if(testers[pos] != NULL){
        delete testers[pos];
        testers[pos] = NULL;
        currNumTesters--;
        return true;
    }
    return false;
}

bool TesterInterface::evaluate(int num)
{
    for(int i=0; i < maxNumTesters; i++){
        if(testers[i] != NULL && !testers[i]->evaluate(num))
            return false;
    }
    return true;
}

int* TesterInterface::failedTests(int num)
{
    int* res = new int[numberOfFailedTests(num)];
    int pos = 0;
    for(int i=0; i < maxNumTesters; i++){
        if(testers[i] != NULL && !testers[i]->evaluate(num))
        {
            res[pos] = i;
            pos++;
        }
    }
    return res;
}

int TesterInterface::numberOfFailedTests(int num)
{
    int count = 0;
    for(int i=0; i < maxNumTesters; i++){
        if(testers[i] != NULL && !testers[i]->evaluate(num))
            count++;
    }
    return count;
}

int TesterInterface::getCurrNumTesters() const
{
    return currNumTesters;
}

int TesterInterface::getMaxNumTesters() const
{
    return maxNumTesters;
}

NumberTester* TesterInterface::operator[](int pos)
{
    if(pos < 0 || pos >= maxNumTesters)
        return NULL;

    return testers[pos];
}
