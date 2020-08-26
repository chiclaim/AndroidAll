

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

### 友元函数

#### 类本身的友元函数

```
class Point {


private:
	int x, y;

public:
	Point(int a, int b) {
		x = a;
		y = b;
	}
	friend void show(Point&); // 友元函数
};

void show(Point& p) {
	// 可以访问 point 的私有成员
	cout << "x = "<< p.x << " y = " << p.y << endl;
}

int main()
{
	Point p(1, 1);
	show(p);
}

```

`show` 函数被声明为 Point 类的友元，由于友元不是 Point 类的成员，所有没有 this 指针，在访问该类的对象的成员时，必须使用对象名。

由此可见，友元函数就是一个一般的函数，只不过它是在类中说明，可以访问该类所有对象的私有成员。

#### 将成员函数用做友元

```
class Two;
class One{
    
    private :
        int x;
    
    public:
        One(int a){
            x = a;
        }
        void func(Two&);
        void display(){
            cout<<"one x = "<< x<< endl;
        }
};

class Two{
    private :
        int y;
    public:
        Two(int a){
            y = a;
        }
    
        // 将One的func函数声明为Two的友元
        friend void One::func(Two&);
    
        void display(){
            cout<<"two y = "<< y<< endl;
        }
};

void One::func(Two & two){
    // 可以访问 two 的私有属性了
    two.y = x;
}


int main(int argc, const char * argv[]) {

    One one(1);
    Two two(2);
    one.display();
    two.display();
    one.func(two);
    two.display();
}
```

#### 将一个类说明为另一个类的友元

将一个类说明为另一个类的友元，整个类的成员函数都具有友元的特点。

```
class Two;

class One{
    
    private :
        int x;
    
    public:
        // 将 Two 声明为 One 的友元
        friend class Two;
};

class Two{
    private :
        int y;
    public:
        Two(One& a){
            // 可以访问 One 的所有私有成员
            y = a.x;
        }
};
```

总结：可见友元函数可以是一个类或函数。不管是上面哪种方式声明友元，都是为了能方便访问私有成员，谁想把自己的私有成员分享出去，就在它的类中去定义友元。例如 One 想把它的私有成员可以让 Two 访问，那么就在 One 类中定义友元。私有成员给谁访问是自己控制的，也就是说其他人没有你的允许是不能访问你的私有成员的，除非你将其声明为你的友元（friend）


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


## 对象成员的初始化

在一个类中定义了某个类的类型成员，这些成员称之为对象成员，例如：

```
class Square{
	Point p1,   // 对象成员
	Point p2,   // 对象成员
	Point p3,   // 对象成员
	Point p4    // 对象成员
}
```

我们可以在初始化列表里对其初始化：

```
Square(int i,int j,int k,int v):p1(i),p2(j),p3(k),p4(v)
```
初始化列表有4个初始化参数，初始化的顺序不是有列表的顺序决定的，而是由在类中对象成员的声明顺序决定的。

我们将初始化列表的顺序调整下：

```
class Square {
private :
	Point p1;
	Point p2;
	Point p3;
	Point p4;

public:
	Square() {
		cout << "Default constructor Container" << endl;
	}

	//初始化列表的顺序调整，将 p2 放在第一个
	Square(int i,int j,int k,int v):p2(j), p1(i),p3(k),p4(v) { 
		cout << "Constructor Square " << endl;
	}
};

int main()
{
	Square s2(1,2,3,4);
}
```

输出的顺序依然是：1,2,3,4


## 静态成员

如果类的数据成员被 static 关键字修饰，这样的成员称之为静态成员。

静态成员不依赖对象，在对象没有创建前，静态成员就已经存在。

通常将静态变量定义为 private，然后通过静态函数方位该静态变量。

如果在类中仅对静态成员进行声明，必须在文件作用域的某个地方进行定义。

```
class Point{
    private :
        int x;
        int y;
        static int count; // 声明静态变量
    
    public:
        static int getCount(){ // 定义静态函数
            return count;
        }
};

int Point::count = 10;

int main(int argc, const char * argv[]) {
    cout<< Point::getCount()<<endl;
}
```

需要的注意的是，不要混淆了静态成员和静态对象。静态对象是指使用 static 关键字声明的类的对象，需要注意静态对象的构造函数和析构函数的调用特点。

例如我们在 for 循环中创建一个静态对象和普通对象：

```
class Point{
    
    private :
        int x;
        int y;
    
    public:
        Point(int a,int b,string message = ""):x(a),y(b){
            cout<< "Construct Point x = " << x << ",y = " << y << message << endl;
        }
         void move(int a,int b){
            x += a;
            y += b;
        }
    
        void show(string message = ""){
            cout<< "Point x = " << x << ",y = " << y << message << endl;
        }
    
        ~ Point(){
            cout<<"Destructor Point x = " << x << ",y = " << y << endl;
        }
    
};



int main(int argc, const char * argv[]) {
    for(int i=0;i<3;i++){
        static Point p1(1,1,", by static");
        Point p2(1,1);
        p1.move(2, 2);
        p2.move(2, 2);
        p1.show(", static obj");
        p2.show();
    }
}

// 输出结果
Construct Point x = 1,y = 1, by static
Construct Point x = 1,y = 1
Point x = 3,y = 3, static obj
Point x = 3,y = 3
Destructor Point x = 3,y = 3
Construct Point x = 1,y = 1
Point x = 5,y = 5, static obj
Point x = 3,y = 3
Destructor Point x = 3,y = 3
Construct Point x = 1,y = 1
Point x = 7,y = 7, static obj
Point x = 3,y = 3
Destructor Point x = 3,y = 3
Main exit
Destructor Point x = 7,y = 7
```

可以看出尽管循环了 3 次，但是静态对象只初始化了一次。在 main 函数结束的时候才会调用静态对象的析构函数。静态对象一旦初始化它的生命周期伴随着整个程序的生命周期。



## const 对象

- 常量数据成员
    常量数据成员只能在初始化列表中设置值
- 常量引用
    只能在初始化列表中设置值，不能修改引用的值。
- 静态常量成员
    静态常量数据成员只能在类外初始化
- 常量函数
    在常量函数中不能修改对象的成员数据
- 常量对象
    常量对象不能修改成员属性，只能访问常量函数，避免修改成员数据

```
class Two;
class One{
    
    private :
        int x;
    
        // 常量数据成员
        const int a = 22;
        // 常量引用
        const int & r;
        // 静态常量数据成员
        static const int b = 11;
    
        // 常量对象 必须定义时初始化
        Two const two();
    
    
    public:
        One(int i):x(i),a(i),r(a){
            
        }
        void setA(int _a){
            // 常量数据成员只能在初始化列表中设置值
            //a = _a;
            // 静态常量数据成员只能在类外初始化
            //b = 11;
            // 只能在初始化列表中设置值
            //r = &_a;
        }
    
        void show() const{
            // 在常量函数中不能修改对象的成员数据
            //x = 111;
            cout << "in const function : x = " << x << endl;
        }
        void show(){
            // 在常量函数中不能修改对象的成员数据
            //x = 111;
            cout << "x = " << x << endl;
        }
};

class Two{
    
    
};



int main(int argc, const char * argv[]) {
    One one(1);
    one.show();
    
    // 常量对象不能修改成员属性
    One const one2(2);
    // 会调用const show
    one2.show();
    // 常量对象只能访问常量函数，避免修改成员数据
    //one2.setA(22);
}

```

## 函数指针

每一个函数都占用一段内存单元，它们有一个起始地址，指向函数入口地址的指针称为函数指针。

定义方式：`type(class::*ptrName)(params)` 或者 `type(*ptrName)(params)`

```
class One{
    
    private :
        int x;
    
    public:
        One(int a){
            x = a;
        }
        void show(){
            cout << "x = " << x << endl;
        }
    
        static void show2(){
            cout << "call static function" << endl;
        }
};

void show(){
    cout << "hello world" << endl;
}

int main(int argc, const char * argv[]) {
    
    // type(class::*ptrName)(params)
    
    //指向 One 的成员函数（无返回值、无参）
    void(One::*pfun)(void);
    
    One one(1);
    pfun = &One::show; // 类的非静态函数不能缺少 & 符号
    // 通过函数指针调用函数
    (one.*pfun)();
    
    One *p =  &one;
    // 通过函数指针调用函数
    (p->*pfun)();
    
    
    // 指向全局函数（无返回值、无参）
    void(*pfun2)(void);
    pfun2 = show;
    pfun2();
    (*pfun2)();
    
    // 指向静态函数（无返回值、无参）
    pfun2 = One::show2; // 可以没有 & 符号
    (*pfun2)();
}

```


## 继承和派生

通过特殊化已有的类来建立新类的过程，叫做“类的派生”，原有的类叫做“基类”，新建的类叫做“派生类”。

从类的成员角度看，**派生类自动将基类的所有成员作为自己的成员**，这叫做“继承”。

基类和派生类又可以分别叫做“父类”和“子类”。

当从现有类中派生出新类时，派生类可以有如下几种变化：
- 增加新的成员（数据成员或成员函数）
- 重新定义已有的成员函数
- 改变基类成员的访问权限（公有继承、私有继承等）

### 单继承

#### 单继承的语法：

```
class 派生类 : 访问控制符 基类名{

}
```

#### 派生类的构造函数和析构函数

如果我们是会用了继承，构造函数和析构函数调用时机是怎么样的呢？

```
class Point {
private :
	int x, y;
public :

	Point(int a, int b) {
		x = a;
		y = b;
		cout << "Construciton Point" << endl;
	}

	~Point() {
		cout << "Destruciton Point" << endl;
	}

	void display() {
		cout << "x = "<<x<<", y = "<<y << endl;
	}
};

class Rectangle : public Point {
private:
	int width, height;
public :
	
	// 构造函数
	Rectangle(int x,int y,int width,int height):Point(x,y) {
		this->width = width;
		this->height = height;
		cout << "Construciton Rectangle" << endl;
	}
	// 析构函数
	~Rectangle() {
		cout << "Destruciton Rectangle" << endl;
	}

};


int main()
{
	Rectangle rectangle(10,10,2,2);
}
```

输出结果：

```
Construciton Point
Construciton Rectangle
x = 10, y = 10
Destruciton Rectangle
Destruciton Point
```

可见，会先构造基类 Point 然后再构造子类 Rectangle。销毁的时候顺序刚好相反。

在 Java 中创建一个对象， 可能会这样写：

```
// 先创建一个子类对象，然后赋值给父类
Rectangle rectangle(10, 10, 2, 2);
Point p = rectangle;
```

如果在 C++ 这样写会发生什么情况呢？

```
Construciton Point
Construciton Rectangle
x = 10, y = 10
Destruciton Point
Destruciton Rectangle
Destruciton Point
```

发现 `Destruciton Point` 输出 2 次，但是 `Construciton Point` 只输出了 1 次。

这是因为赋值给 p 的时候调用了 `复制构造函数`，在 Point 类中加上 `复制构造函数`，然后输出：

```
Construciton Point
Construciton Rectangle
Point Copy Construct
x = 10, y = 10
Destruciton Point
Destruciton Rectangle
Destruciton Point
```

创建了两次 Point，所以会调用两次析构函数。


问题：

如果将测试程序改成如下形式：

```
int main()
{
	Point p = Rectangle(10, 10, 2, 2);
	p.display();
}
```

VS 提示 ‘不存在用户定义的从 Rectangle 到 Point 的适当转换’。

如果将 Point 类中的复制构造函数的参数改成 Rectangle 引用：

```
Point(Rectangle& p) {	
	cout << "Point Copy Construct" << endl;
}
```
则可以通过编译，但是运行没有调用该复制构造函数：

```
Construciton Point
Construciton Rectangle
Destruciton Rectangle
Destruciton Point
x = 10, y = 10
Destruciton Point
```

问题：这个和上面的有什么区别呢？

#### 赋值兼容规则

通过上面的例子我们知道，派生的对象可以赋值给基类的对象，会调用复制构造函数。

如果不想要让程序调用复制构造函数，则可以用派生类的对象初始化基类的引用或指针：

```
// 基类的引用
Point &p = rectangle;

// 基类的指针
Point *p = &rectangle;
```

#### 类的 protected 的成员

在上面的例子例子中，如果想要在子类中访问父类 Point 的x、y，因为他是 private 的，肯定访问不到。

如果想让子类访问的到，其他的类不能访问，则可以将 private 改成 protected


#### 公有继承

继承的语法为：`class 派生类 : 访问控制符 基类名`

其中访问控制符可以是 public、protected、private

如果访问控制符是 public，则是公有继承。在公有继承的情况下，基类的访问权限在派生类中保持不变。这就意味着：

- 基类的公有成员在派生类中仍然是公有的
- 基类的保护成员在派生类中仍然是保护的
- 基类的`不可访问`的和私有的成员在派生中仍然是不可访问的

前两条好理解，最后一条是中的不可访问的是什么意思呢，不可访问和私有不是一个意思吗？

在根类（不是从别的派生类出来的类）中，没有成员是`不可访问`的。

在根类中存在 private、protected、public 访问级别。

在派生类中则可能存在第 4 种访问级别：不可访问(inaccessible)

如果基类的成员是 private 的，那么派生类继承过来后，这个成员就是不可访问的，不可访问成员总是从基类继承来的。


基类成员 | 派生类的成员函数对基类的访问 | 基类、派生类对象 
---|---|---
private 成员 | 不可访问| 不可访问
protected 成员 | protected| 不可访问
public 成员 | public| 可访问

#### 私有继承

通过私有派生，基类的 `private、inaccessable` 成员在派生类中是不可访问的，而 `public、protected` 成员就成了派生类的私有成员，派生类的对象不能访问继承的基类成员。

虽然派生类的成员函数可以通过自定义函数访问基类的成员，但将该派生类作为基类继续派生，即使使用公有派生，原基类公有成员在新的派生类中也是不可访问的。

```
class A {

    private:
    	int a;
    
    public :
    	void showA() {}
    };
    
// 私有继承 A
class B : private A{
    	
    	//从基类A继承过来的私有变量a，变成了inaccessible
    	//从基类A继承过来的公有方法showA，变成了private
    
    private:
    	int b;
    
    public:
    	void showB() {
    		// 可以访问私有方法 showA
    		showA();
    	}
};

// 公有继承 B
class C: public B {
    	//从基类B继承过来的inaccessible变量a，变成了inaccessible
    	//从基类B继承过来的private方法showA，变成了inaccessible
    
    	//从基类B继承过来的私有变量b，变成了inaccessible
    	//从基类B继承过来的公有方法showB，变成了private
    private:
    	int c;
    
    public:
    	void showC() {
    		// 不能访问 showA 了
    	}
};
```

#### 保护继承

保护派生使原来的权限都降了一级：private 变成 inaccessible；protected 变成 private；public 变成 protected

在上面的例子中，如果 `class B : protected A` 的话， class C 中就可以访问 class A 中的 showA 函数了。

```
class A {

	private:
		int a;

	public :
		void showA() {}
};

// 保护继承 A
class B : protected A{
	
	//从基类A继承过来的私有变量a，变成了inaccessible
	//从基类A继承过来的公有方法showA，变成了protected

	private:
		int b;

	public:
		void showB() {
			// 可以访问 protected 方法 showA
			showA();
		}
};

// 公有继承 B
class C: public B {
	//从基类B继承过来的inaccessible变量a，变成了inaccessible
	//从基类B继承过来的protected方法showA，变成了protected

	//从基类B继承过来的私有变量b，还是private
	//从基类B继承过来的公有方法showB，还是public
	private:
		int c;

	public:
		void showC() {
			showA();
			showB();
		}
};
```


待续...




