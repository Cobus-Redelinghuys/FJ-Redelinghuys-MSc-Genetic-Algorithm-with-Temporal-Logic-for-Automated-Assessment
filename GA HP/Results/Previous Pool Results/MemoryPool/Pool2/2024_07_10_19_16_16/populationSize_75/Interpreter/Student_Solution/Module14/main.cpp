#include <string>
#include <iostream>

int main(int argc, char const *argv[])
{
    int i = std::stoi(argv[1]);
    if(i-5 < 0){
        for(int i=0; i < 5; i++){
            i++;
        }
    }

    if(i < 5){
        throw "Exception: Loop Failed";
    }
    return 0;
}
