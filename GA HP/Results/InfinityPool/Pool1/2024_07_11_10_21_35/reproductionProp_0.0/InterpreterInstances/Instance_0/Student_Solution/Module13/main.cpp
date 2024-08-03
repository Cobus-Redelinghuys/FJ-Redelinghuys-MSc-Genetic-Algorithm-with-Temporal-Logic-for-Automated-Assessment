#include <string>
#include <iostream>

struct Object{
    bool leq;
    Object* other;
};

void less(Object& obj){
    obj.other = new Object;
    obj.other->leq = true;
}

void greater(Object obj){
    obj.other = new Object;
    obj.other->leq = true;
}

int main(int argc, char const *argv[])
{
    int v = std::stoi(argv[1]);
    Object obj;
    obj.other = NULL;
    if(v > 5){
        greater(obj);
    } else {
        less(obj);
    }
    std::cout << (obj.other->leq ? "less" : "greater") << std::endl;
    return 0;
}
