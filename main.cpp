#include <iostream>
#include "TesterInterface.h"
#include "IsDivisible.h"
#include "IsSmaller.h"
#include "IsGreater.h"
#include "IsEvenOdd.h"
#include "IsPrimeNumber.h"
void printAliveObjects(){
std::cout << "AliveObjects: ";
std::cout << IsEvenOdd::getNumAliveObjects() << ",";
std::cout << IsDivisible::getNumAliveObjects() << ",";
std::cout << IsGreater::getNumAliveObjects() << ",";
std::cout << IsPrimeNumber::getNumAliveObjects() << ",";
std::cout << IsSmaller::getNumAliveObjects() << ",";
std::cout << ValueDependantTester::getNumAliveObjects() << ",";
std::cout << ValueIndependantTester::getNumAliveObjects() << ",";
std::cout << NumberTester::getNumAliveObjects() << std::endl;
}
int main(){
TesterInterface* t1 = new TesterInterface(7);
std::cout << "Finished setup" << std::endl;
printAliveObjects();NumberTester* n1 = NULL;
std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
n1 = new IsSmaller(7);std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n1) << std::endl;
if( n1 != NULL)
{
delete n1;
n1 = NULL;
};if((*t1)[7] != NULL){
std::cout << "Result of test 7: " << (*t1)[7]->evaluate(7) << std::endl;
} else {
std::cout << "Result of test 7: " << "NULL" << std::endl;
}printAliveObjects();printAliveObjects();NumberTester* n2 = NULL;
std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
n2 = new IsSmaller(7);std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n2) << std::endl;
if( n2 != NULL)
{
delete n2;
n2 = NULL;
};if((*t1)[7] != NULL){
std::cout << "Result of test 7: " << (*t1)[7]->evaluate(7) << std::endl;
} else {
std::cout << "Result of test 7: " << "NULL" << std::endl;
}printAliveObjects();printAliveObjects();NumberTester* n3 = NULL;
std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
n3 = new IsSmaller(7);std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n3) << std::endl;
if( n3 != NULL)
{
delete n3;
n3 = NULL;
};if((*t1)[7] != NULL){
std::cout << "Result of test 7: " << (*t1)[7]->evaluate(7) << std::endl;
} else {
std::cout << "Result of test 7: " << "NULL" << std::endl;
}printAliveObjects();printAliveObjects();NumberTester* n4 = NULL;
std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
n4 = new IsSmaller(7);std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n4) << std::endl;
if( n4 != NULL)
{
delete n4;
n4 = NULL;
};if((*t1)[7] != NULL){
std::cout << "Result of test 7: " << (*t1)[7]->evaluate(7) << std::endl;
} else {
std::cout << "Result of test 7: " << "NULL" << std::endl;
}printAliveObjects();printAliveObjects();NumberTester* n5 = NULL;
std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
n5 = new IsSmaller(7);std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n5) << std::endl;
if( n5 != NULL)
{
delete n5;
n5 = NULL;
};if((*t1)[7] != NULL){
std::cout << "Result of test 7: " << (*t1)[7]->evaluate(7) << std::endl;
} else {
std::cout << "Result of test 7: " << "NULL" << std::endl;
}printAliveObjects();printAliveObjects();NumberTester* n6 = NULL;
std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
n6 = new IsSmaller(7);std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n6) << std::endl;
if( n6 != NULL)
{
delete n6;
n6 = NULL;
};if((*t1)[7] != NULL){
std::cout << "Result of test 7: " << (*t1)[7]->evaluate(7) << std::endl;
} else {
std::cout << "Result of test 7: " << "NULL" << std::endl;
}printAliveObjects();printAliveObjects();NumberTester* n7 = NULL;
std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
n7 = new IsSmaller(7);std::cout << "IsSmaller objects: " << IsSmaller::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n7) << std::endl;
if( n7 != NULL)
{
delete n7;
n7 = NULL;
};if((*t1)[7] != NULL){
std::cout << "Result of test 7: " << (*t1)[7]->evaluate(7) << std::endl;
} else {
std::cout << "Result of test 7: " << "NULL" << std::endl;
}printAliveObjects();
std::cout << "Evaluate tester: " << (t1->evaluate(11)  ? "True" : "False") << std::endl;
printAliveObjects();if((*t1)[1] != NULL){
std::cout << "Result of test 1: " << (*t1)[1]->evaluate(1) << std::endl;
} else {
std::cout << "Result of test 1: " << "NULL" << std::endl;
}std::cout << "Remove tester: " << (t1->removeTester(1)  ? "True" : "False") << std::endl;
if((*t1)[1] != NULL){
std::cout << "Result of test 1: " << (*t1)[1]->evaluate(1) << std::endl;
} else {
std::cout << "Result of test 1: " << "NULL" << std::endl;
}printAliveObjects();
printAliveObjects();NumberTester* n8 = NULL;
std::cout << "IsDivisible objects: " << IsDivisible::getNumAliveObjects() << std::endl;
if(0 != 0){
n8 = new IsDivisible(0);
}
std::cout << "IsDivisible objects: " << IsDivisible::getNumAliveObjects() << std::endl;
std::cout << "Add tester: " << t1->addTester(n8) << std::endl;
if( n8 != NULL)
{
delete n8;
n8 = NULL;
};if((*t1)[0] != NULL){
std::cout << "Result of test 0: " << (*t1)[0]->evaluate(0) << std::endl;
} else {
std::cout << "Result of test 0: " << "NULL" << std::endl;
}printAliveObjects();
if( t1 != NULL)
{
delete t1;
t1 = NULL;
};
return 0;
}
/*Instructor output:
rm *.o main -f
g++ -g -std=c++98 *.cpp -o main -w
./main
Finished setup
AliveObjects: 0,0,0,0,0,0,0,0
IsSmaller objects: 0
IsSmaller objects: 1
Add tester: 0
Result of test 7: NULL
AliveObjects: 0,0,0,0,1,1,0,1
AliveObjects: 0,0,0,0,1,1,0,1
IsSmaller objects: 1
IsSmaller objects: 2
Add tester: 1
Result of test 7: NULL
AliveObjects: 0,0,0,0,2,2,0,2
AliveObjects: 0,0,0,0,2,2,0,2
IsSmaller objects: 2
IsSmaller objects: 3
Add tester: 2
Result of test 7: NULL
AliveObjects: 0,0,0,0,3,3,0,3
AliveObjects: 0,0,0,0,3,3,0,3
IsSmaller objects: 3
IsSmaller objects: 4
Add tester: 3
Result of test 7: NULL
AliveObjects: 0,0,0,0,4,4,0,4
AliveObjects: 0,0,0,0,4,4,0,4
IsSmaller objects: 4
IsSmaller objects: 5
Add tester: 4
Result of test 7: NULL
AliveObjects: 0,0,0,0,5,5,0,5
AliveObjects: 0,0,0,0,5,5,0,5
IsSmaller objects: 5
IsSmaller objects: 6
Add tester: 5
Result of test 7: NULL
AliveObjects: 0,0,0,0,6,6,0,6
AliveObjects: 0,0,0,0,6,6,0,6
IsSmaller objects: 6
IsSmaller objects: 7
Add tester: 6
Result of test 7: NULL
AliveObjects: 0,0,0,0,7,7,0,7
Evaluate tester: False
AliveObjects: 0,0,0,0,7,7,0,7
Result of test 1: 1
Remove tester: True
Result of test 1: NULL
AliveObjects: 0,0,0,0,6,6,0,6
AliveObjects: 0,0,0,0,6,6,0,6
IsDivisible objects: 0
IsDivisible objects: 0
Add tester: -1
Result of test 0: 1
AliveObjects: 0,0,0,0,6,6,0,6

Student output:
rm *.o main -f
g++ -g -std=c++98 *.cpp -o main -w
./main
Finished setup
AliveObjects: 0,0,0,0,0,0,0,0
IsSmaller objects: 0
IsSmaller objects: 1
Add tester: 0
*/