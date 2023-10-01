#ifndef TESTERINTERFACE_H
#define TESTERINTERFACE_H

#include "NumberTester.h"

class TesterInterface{
    private:
        NumberTester** testers;
        int maxNumTesters;
        int currNumTesters;

    public:
        TesterInterface(int maxNumTests);
        TesterInterface(TesterInterface* other);
        TesterInterface(TesterInterface& other);
        ~TesterInterface();
        int addTester(NumberTester *tester);
        bool removeTester(int pos);
        bool evaluate(int num);
        int* failedTests(int num);
        int numberOfFailedTests(int num);
        NumberTester* operator[](int pos);
        int getCurrNumTesters() const;
        int getMaxNumTesters() const;

};;

#endif