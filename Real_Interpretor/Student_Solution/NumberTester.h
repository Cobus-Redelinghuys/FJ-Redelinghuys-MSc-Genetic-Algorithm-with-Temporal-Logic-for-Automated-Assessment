#ifndef NUMBERTESTER_H
#define NUMBERTESTER_H

class NumberTester{
    private:
        static int numAliveObjects;
    public:
        NumberTester();
        virtual ~NumberTester() =0;
        virtual bool evaluate(int val) = 0;
        virtual NumberTester* clone() = 0;
        static int getNumAliveObjects();
};

#endif