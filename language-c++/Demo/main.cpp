//
//  main.cpp
//  Demo
//
//  Created by 余志强 on 2020/8/2.
//  Copyright © 2020 demo. All rights reserved.
//

// 采用尖括号是引用系统提供的引用文件，编译系统会到C++语言系统设定的目录去查找，找不到j就会到指定的目录去找
// 采用双引号则是引用自定义的引用文件
// #include "xxx.h"
#include <iostream>

using namespace std;

#define BUFFER_SIZE = 10 //宏定义，编译期间会将所有 BUFFER_SIZE 替换成 10

int main(int argc, const char * argv[]) {
    // 使用 new 动态分配内存
//    const int SIZE = 3; // 定义常量
//    double *p = new double[SIZE];
//    for(int i=0;i<SIZE;i++){
//        cout << "请输入第" << i+1 << "数：";
//        cin >> *(p+i);
//    }
//    for(int i=0;i<SIZE;i++){
//        cout<< *(p+i) << " ";
//    }
//    delete[] p;


    int x = 56;
    // 声明引用，所谓引用就是将一个新标识和经存在的内存区域相关联。只是变量的别名
    int &a = x;
    int &b = a;

    cout<< "a = " << &a << "\n" << "b = "<< &b <<"\n" << "x = "<< &x <<"\n";

    cout<< "a = " << a << "\n" << "b = "<< b <<"\n" << "x = "<< x << "\n";

    cout<< "change a = 10\n" ;
    a = 10;

    cout<< "a = " << &a << "\n" << "b = "<< &b <<"\n" << "x = "<< &x <<"\n";
    cout<< "a = " << a << "\n" << "b = "<< b <<"\n" << "x = "<< x << "\n";
    // 引用实际上就是变量的别名
    // 引用和指针有相似之处，它可以对内存地址上的存在的变量进行修改，但是它不占用新的地址，从而节省开销

    // 1,指针是低级的直接操作内存地址的机制，指针功能强大，但容易出错。在C++中指针可以有整数数强制类型转换得到，处理不当可能会对系统造成破坏。
    // 2,引用则是较高级的封装了指针的特性，它不能直接操作内存地址，不可以由强制类型转换而得。

    // 不能直接定义对数组的引用，但是可以通过间接的方式：



}
