#include <iostream>
#include "TesterInterface.h"
#include "IsDivisible.h"
#include "IsSmaller.h"
#include "IsGreater.h"
#include "IsEvenOdd.h"
#include "IsPrimeNumber.h"
int main(){
TesterInterface* t1 = new TesterInterface(9);
std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;NumberTester* n1 = NULL;
std::cout << "IsPrimeNumber objects: " << IsPrimeNumber::getNumAliveObjects() << std::endl;
n1 = new IsPrimeNumber();std::cout << "IsPrimeNumber objects: " << IsPrimeNumber::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n1) << std::endl;
if( n1 != NULL)
{
delete n1;
n1 = NULL;
};std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;
std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;NumberTester* n2 = NULL;
std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
n2 = new IsSmaller(2);std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n2) << std::endl;
if( n2 != NULL)
{
delete n2;
n2 = NULL;
};std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;
std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;NumberTester* n3 = NULL;
std::cout << "IsGreater objects: " << IsGreater::getNumAliveObjects() << std::endl;
n3 = new IsGreater(6);std::cout << "IsGreater objects: " << IsGreater::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n3) << std::endl;
if( n3 != NULL)
{
delete n3;
n3 = NULL;
};std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;
std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;NumberTester* n4 = NULL;
std::cout << "IsEvenOdd objects: " << IsEvenOdd::getNumAliveObjects() << std::endl;
n4 = new IsEvenOdd();std::cout << "IsEvenOdd objects: " << IsEvenOdd::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n4) << std::endl;
if( n4 != NULL)
{
delete n4;
n4 = NULL;
};std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;
std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;NumberTester* n5 = NULL;
std::cout << "IsGreater objects: " << IsGreater::getNumAliveObjects() << std::endl;
n5 = new IsGreater(1);std::cout << "IsGreater objects: " << IsGreater::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n5) << std::endl;
if( n5 != NULL)
{
delete n5;
n5 = NULL;
};std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;
std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;NumberTester* n6 = NULL;
std::cout << "IsDivisible objects: " << IsDivisible::getNumAliveObjects() << std::endl;
n6 = new IsDivisible(5);
std::cout << "IsDivisible objects: " << IsDivisible::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n6) << std::endl;
if( n6 != NULL)
{
delete n6;
n6 = NULL;
};std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;
std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;NumberTester* n7 = NULL;
std::cout << "IsEvenOdd objects: " << IsEvenOdd::getNumAliveObjects() << std::endl;
n7 = new IsEvenOdd();std::cout << "IsEvenOdd objects: " << IsEvenOdd::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n7) << std::endl;
if( n7 != NULL)
{
delete n7;
n7 = NULL;
};std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;
std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;NumberTester* n8 = NULL;
std::cout << "IsDivisible objects: " << IsDivisible::getNumAliveObjects() << std::endl;
n8 = new IsDivisible(0);
std::cout << "IsDivisible objects: " << IsDivisible::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n8) << std::endl;
if( n8 != NULL)
{
delete n8;
n8 = NULL;
};std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;
std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;NumberTester* n9 = NULL;
std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
n9 = new IsSmaller(7);std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n9) << std::endl;
if( n9 != NULL)
{
delete n9;
n9 = NULL;
};std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;
std::cout << "Finished setup" << std::endl;
std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;std::cout << "Remove tester: " << (t1->removeTester(2)  ? "True" : "False") << std::endl;
std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;
std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;NumberTester* n10 = NULL;
std::cout << "IsGreater objects: " << IsGreater::getNumAliveObjects() << std::endl;
n10 = new IsGreater(1);std::cout << "IsGreater objects: " << IsGreater::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n10) << std::endl;
if( n10 != NULL)
{
delete n10;
n10 = NULL;
};std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;
TesterInterface* t2 = new TesterInterface(t1);
if( t1 != NULL)
{
delete t1;
t1 = NULL;
};
std::cout << "Failed tests: "
;for(int i=0; i < t2->numberOfFailedTests(4); i++)
{
std::cout << t2->failedTests(4)[i] << "|";}
std::cout << std::endl;
if((*t2)[6] != NULL){
std::cout << "Result of test 6: " << (*t2)[6] << std::endl;
} else {
std::cout << "Result of test 6: " << "NULL" << std::endl;
}
std::cout << "Number of failed tests: " << t2->numberOfFailedTests(5) << std::endl;
std::cout << "NumberTester objects: " << NumberTester::getNumAliveObjects() << std::endl;
if( t2 != NULL)
{
delete t2;
t2 = NULL;
};
return 0;
}
