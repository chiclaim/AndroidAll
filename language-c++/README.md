# C++

## 指针与引用

`指针` 是一个变量，它本身是占用内存空间的，它的值是另一个变量的地址。例如：


```
int x = 1;
int *p = &x;
```
上面的变量 p 就是一个指针，它存放的是变量 x 的地址。

所谓 `引用` 就是将一个新标识和已经存在的内存区域相关联。只是变量的别名。例如：

```
int x = 56;
int &a = x;
int &b = a;
```
其中 a、b、x 表示的是一个东西，a、b 都是 x 的别名。

小结：
- 引用实际上就是变量的别名
- 引用和指针有相似之处，它可以对内存地址上的存在的变量进行修改，但是它不占用新的地址，从而节省开销
- 指针是低级的直接操作内存地址的机制，指针功能强大，但容易出错。在C++中指针可以有整数数强制类型转换得到，处理不当可能会对系统造成破坏。
- 引用则是较高级的封装了指针的特性，它不能直接操作内存地址，不可以由强制类型转换而得。

## 指针与常量

- 指向常量的指针

在指针类型前面加上 const 关键字，表示指针 p 指向一个常量，也就是指针指向的内容不可以被修改。例如：

```
int x = 5;
const int *p = &x; // 无法通过指针 p 来修改 x 的值的
// *p = 3 非法
x = 3;
cout << *p; //3
```

- 常量指针

在指针的名字前面加上 const 关键字，表示这个指针是常量，也就是指针初始化后，不能重新赋值了。例如：

```
int * const p = &x; // 指针 p 指向 x
// p = &x; 非法
*p = 4;
```

- 指向常量的常量指针

它是指向常量的指针和指针常量的结合体。如：

```
const int * const p = &x;
```
也就是 `*p = ` 和 `p = ` 都是非法的。


## 指针与数组

数组和指针是两个不同类型的东西。在 32 为系统中，指针占用 4 字节，在 64 位系统中占用 8 字节。

指针是用来存放地址的；数组用来存储一个固定大小的相同类型元素的顺序集合。

指针和数组可以组合使用，比如将数组赋值给一个指针：

```
float score[] = {1,2,3,4};
float *p = score;
```

通过指针也可以来访问数组：

```

// 访问数组
std::cout<< p[1] <<std::endl;  // 2
std::cout<< score[1] <<std::endl; // 2
std::cout << *(score+1) << std::endl; // 2
std::cout << *(p+1) << std::endl;    // 2

// 修改数组
p[1] = 11;
*(score+2) = 12;

std::cout<< p[1] <<std::endl;  // 11
std::cout<< p[2] <<std::endl;  // 12
```

当数组名在表达式中使用时，编译器会把数组名转换为一个指针常量：

```
float score[] = {1,2,3,4};
float *p = score; // 在这里 score 会转换成数组第一个元素的地址


// 下面输出的都是同一个地址：
std::cout<< score <<std::endl;
std::cout<< &score <<std::endl;
std::cout<< &score[0] <<std::endl;
std::cout<< &p[0] <<std::endl;
```

通过 `sizeof` 来输出指针和数组的大小：

```
std::cout<<sizeof(p)<<std::endl;     // 64 位系统指针占 8 字节
std::cout<<sizeof(score)<<std::endl; // 4*4 = 16
```

## 类和对象

C++ 中的类和对象与 Java 非常类似。但也有些区别：

只不过在声明成员的时候，是按照访问修饰符分组，并不是像 Java 那种每个成员一个访问修饰符。在 C++ 中如果在成员没有使用访问修饰符，默认为 `private`：

```
class Point{
    
    private :
        int x;
        int y;
    
    public:
        void setXY(int x,int y){
            this->x = x;
            this->y = y;
        }
    
        int getX(){
            return x;
        }
    
        int getY(){
            return y;
        }
};
```

新建对象的时候，默认会对对象初始化，如：

```
// 创建 Point 对象
Point p;
```

在 Java 中上面的 p 会等于 null ，在 C++ 中会调用默认的构造方法进行初始化。


需要注意的是，如果没有成员变量 x、y 进行初始化，通过默认构造函数创建对象后，x、y 可能是随机值（也有可能是0），例如：

```
int main(int argc, const char * argv[]) {
    Point p;
    std::cout<< "p's address :" << &p <<std::endl;
    std::cout<< p.getX() <<std::endl;
    std::cout<< "p's address :" << &p <<std::endl;
    std::cout<< p.getX() <<std::endl;
    
}

输出结果：
p's address :0x7ffeefbff4e8
-272632472
p's address :0x7ffeefbff4e8
-272632472
```
我们稍微改动下：

```
int main(int argc, const char * argv[]) {
    Point p;
    std::cout<< p.getX() <<std::endl;
    std::cout<< "p's address :" << &p <<std::endl;
    std::cout<< p.getX() <<std::endl;
}
输出结果：
0
p's address :0x7ffeefbff4f8
0
```
可见，如果我们输变量 x 出前，使用了变量 x 的地址，那么 x 的值是一个随机值。

如果直接输出 x 变量，那么可能是 0。


在调用对象的成员方面，C++可以使用指针的方式：

```
Point p;
p.setXY(10, 20); //使用对象调用函数
cout << p.getX() << endl;

Point *ptr = &p;
ptr -> setXY(20,30);// 通过对象指针的方式访问函数
cout << ptr -> getX() << endl;
```

在对象的创建上和 Java 也有所差异，C++ 有两种方式创建对象，一个在栈上分配，一个在堆上分配：

```
Point p; // 在栈上创建对象，方法执行完，自动销毁
cout<<p.getX()<<endl;

Point *ptr = new Point; // 在堆上创建对象，需要使用 delete 进行释放

ptr->setXY(20, 30);
cout<<ptr->getX()<<endl;

delete ptr;
```


## 函数

C++ 和 Java 的函数语法非常类似。如：

```
int max(int a, int b) {
    return a > b ? a : b;
}
```

### 函数默认参数

C++ 还支持参数默认值，也就是在调用的时候不传递该参数，例如：

```
string append(string a,string b,string c = " ",string d = " "){
    return a+b+c+d;
}

int main(int argc, const char * argv[]) {

    cout << append("hello", "world") << endl;
    cout << append("hello1", "world1","c++") << endl;
   
}
```

需要注意的是，默认参数必须放在参数序列的后面。

### 内联函数

内联函数就是在函数前面加上 `inline` 关键字。

内联函数就是编译器会在编译时将调用的地方替换成函数体。

如果函数的函数体较大使用 inline 会增加软件体积。

### 函数模板

例如获取最大值函数 max：

```
int max(int a, int b) {
    return a > b ? a : b;
}
```

如果我们获取 double 类型的怎么办？需要创建一个类似的 max 函数，唯一不同的就是类型不一样。这个时候就可以使用模板函数了：

```
template <class T>
T max(T a,T b){
    return a>b?a:b;
}

int main(int argc, const char * argv[]) {
    std::cout << max(10,2) << std::endl;
    std::cout << max(10.0,12.0) << std::endl;
}
```

### 函数参数的传递方式

C++ 中函数的参数有两种传递方式：传值、传引用（或者值传递、引用传递）。传引用实际上就是传递对象的地址。


不要将传值和传地址混淆。传值是传递这块内存存放的值，传地址传的是内存的地址。

函数参数可以是对象、对象指针、对象引用。需要注意的是，在 C++ 中像 int、double、char、bool 等简单类型的变量也是对象。

- 函数参数是对象（值传递）
对象作为参数传递给函数时，实际上是将对象的值拷贝给函数的形参。也就是说函数的形参对象和外面的实参对象是两个东西。
在函数中对形参对象做任何改变都不会影响外面实参的对象。
- 函数参数是指针（值传递）
函数的参数是指针的话，也就是将实参对象的地址赋值给形参。所以函数的形参指针修改对象的时候，外面的实参对象也会受到改变。
- 函数参数是引用（引用传递）
函数的参数是引用的话，也就是形参是实参的别名，它们两是同一个东西。所以改变形参对象的值就是改变形参对象的值。

下面通过一个简单的例子来说明三种表形式的差别：

```

void change1(Point p){
    p.setXY(1, 1);
    std::cout << "change1 p's address :" << &p << std::endl;
}

void change2(Point *p){
    p->setXY(2, 2);
    std::cout << "change2 p's address :" << p << std::endl;
}

void change3(Point &p){
    p.setXY(3, 3);
    std::cout << "change3 p's address :" << &p << std::endl;
}


int main(int argc, const char * argv[]) {
    Point p;
    p.setXY(0, 0);
    std::cout<< "initial p's address :" << &p <<std::endl;
    change1(p);
    std::cout << p.getX() << std::endl; // 0
    change2(&p);
    std::cout << p.getX() << std::endl; // 2
    change3(p);
    std::cout << p.getX() << std::endl; // 3
}
```

输出结果：

```
initial p's address :0x7ffeefbff4e8
change1 p's address :0x7ffeefbff498
0
change2 p's address :0x7ffeefbff4e8
2
change3 p's address :0x7ffeefbff4e8
3
```

`change1` 函数参数是对象，在函数里输出形参的地址，和外面的实参地址并不一样，所以形参和实参是两个对象。`change2、change3` 形参和实参的地址都是一样的。


### 构造函数

构造函数是用来初始化对象的成员属性的。对象创建后，对象的成员的值是不确定的。程序员可以在构造函数中对其进行初始化。

如果你没有定义构造函数，C++编译器会为类创建一个默认的构造函数，只不过默认的构造函数体是空的。例如 Point 类：

```
Point::Point(){
    
}
```

也可以根据需要自定义构造函数：

```
Point::Point(int x,int y){
    this->x = x;
    this->y = y;
}
```

也可以为构造函数定义默认参数：

```
class Point{
    
    private:
        int x,y;
    
    public:
        Point(int = 0, int 0);
};

Point::Point(int a, int b) : x(a), y(b) {
    // 赋值操作也可以放在方法体内，也可以放在初始化列表中。
}

```

如果程序员自定义了构造函数，那么 C++ 编译器就不会添加默认的构造函数了。

## 复制构造函数

复制构造函数是使用已存在的对象创建出一个新的对象。

如果没有自定义复制构造函数，编译器会创建一个默认的复制构造函数，默认的复制构造函数采用浅拷贝的方式来创建新的对象。

复制构造函数的原型为：

```
T::T(T&)
```

和构造函数的非常类似，只不过复制构造函数只有一个参数，且该参数是一个引用。使用引用主要是为了提高程序执行效率。

为了不改变原有对象（也就是实参），通常使用 `const` 来修饰：

```
T::T(const T&)
```

程序调用复制构造函数的情况有一下几种：

- 手动调用复制构造函数
- 对象赋值
- 函数参数是一个对象

下面我们以 Point 类为例：

```
class Point{
    
    private :
        int x;
        int y;
    
    public:

        Point(int x,int y){
            this->x = x;
            this->y = y;
        }
    
        int getX(){
            return x;
        }
    
        int getY(){
            return y;
        }
        int *p;
        Point(const Point&);
};

Point::Point(const Point& p){
    x = p.x;
    y = p.y;
    std::cout<<"复制构造函数被调用"<<std::endl;
}

int main(int argc, const char * argv[]) {
    Point p(10,2);
    Point p2 = p;// 调用复制构造函数
    Point p3(p2);// 调用复制构造函数
    display(p3); // 调用复制构造函数
}
```


### 析构函数

析构函数当对象被回收时被调用。当我们在构造函数中动态分配了内存 ，可以在析构函数中进行释放。

析构函数就是在构造函数前面加上 `~` 号：

```
Point :: ~ Point(){
    cout << "析构函数被调用" << endl;
}
```

## this 指针

类的成员函数在编译时，编译器会为其添加一个隐藏参数，名为 `this 指针`

例如下面的一段代码：

```
class Point {
private :
	int x, y;

public :
	void setXY(int a,int b) {
		x = a;
		y = b;
	}
};
```
成员函数 `setXY`，它包含一个隐形参数：

```
void setXY(int a,int b, Point* this) {
	x = a;
	y = b;
}
```

因为对象和代码是分开存储的，如果没有 this 指针，在代码中修改了对象的成员，会出现不知道修改了哪个对象成员的问题。

上面的代码相当于：

```
void setXY(int a,int b, Point* this) {
	this->x = a;
	this->y = b;
}
```

哪个对象调用该函数，那么 `this` 就指向哪个对象。

Java 也是类似的，Java 的成员函数也会包含一个隐藏参数，这个参数就是调用该函数的对象。


