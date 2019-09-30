package class_bytecode.instruction.load;

public class Client {

    public static void main(String[] args) {
        int a = 10;
        int b = 15;
        int sum = a + b;

        float f1 = 1.1f;
        float f2 = 1.2f;
        float f3 = 1.3f;
        float fSum = f1 + f2 + f3;


        double d1 = 3.14;
    }
    /*
         // 对应的指令：

   stack=2, locals=10, args_size=1
         0: bipush        10                    局部变量表:[args]                                  操作数栈:[10]
         2: istore_1                            局部变量表:[args,10]                               操作数栈:[]
         3: bipush        15                    局部变量表:[args,10]                               操作数栈:[15]
         5: istore_2                            局部变量表:[args,10,15]                            操作数栈:[]
         6: iload_1                             局部变量表:[args,10,15]                            操作数栈:[10]
         7: iload_2                             局部变量表:[args,10,15]                            操作数栈:[10,15]
         8: iadd                                局部变量表:[args,10,15]                            操作数栈:[10+15=25]
         9: istore_3                            局部变量表:[args,10,15,25]                         操作数栈:[]
        10: ldc           #2    // float 1.1f   局部变量表:[args,10,15,25]                         操作数栈:[1.1f]
        12: fstore        4                     局部变量表:[args,10,15,25,1.1f]                    操作数栈:[]
        14: ldc           #3    // float 1.2f   局部变量表:[args,10,15,25,1.1f]                    操作数栈:[1.2f]
        16: fstore        5                     局部变量表:[args,10,15,25,1.1f,1.2f]               操作数栈:[]
        18: ldc           #4    // float 1.3f   局部变量表:[args,10,15,25,1.1f,1.2f]               操作数栈:[1.3f]
        20: fstore        6                     局部变量表:[args,10,15,25,1.1f,1.2f,1.3f]          操作数栈:[]
        22: fload         4                     局部变量表:[args,10,15,25,1.1f,1.2f,1.3f]          操作数栈:[1.1f]
        24: fload         5                     局部变量表:[args,10,15,25,1.1f,1.2f,1.3f]          操作数栈:[1.1f,1.2f]
        26: fadd                                局部变量表:[args,10,15,25,1.1f,1.2f,1.3f]          操作数栈:[2.3f]
        27: fload         6                     局部变量表:[args,10,15,25,1.1f,1.2f,1.3f]          操作数栈:[2.3f,1.3f]
        29: fadd                                局部变量表:[args,10,15,25,1.1f,1.2f,1.3f]          操作数栈:[3.6f]
        30: fstore        7                     局部变量表:[args,10,15,25,1.1f,1.2f,1.3f,3.6]      操作数栈:[3.6f]
        32: ldc2_w        #5    // double 3.14d 局部变量表:[args,10,15,25,1.1f,1.2f,1.3f,3.6]      操作数栈:[3.14]
        35: dstore        8                     局部变量表:[args,10,15,25,1.1f,1.2f,1.3f,3.6,3.14] 操作数栈:[]
        37: return
     */
}
