main() {
  int a = null;
  a ??= 3;
  print(a);



  int b = 2;
  b ??= 3; // 如果b是null把3赋值给b，否则什么都不做
  print(b);
}
