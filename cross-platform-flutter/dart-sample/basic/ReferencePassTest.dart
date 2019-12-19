


int age = 0;

change(int value){
  value = 10;
  print("change to 10");
}


main(){
  print("before change function, age = ${age}");
  change(age);
  print("after change function, age = ${age}");
}

//测试 Dart 为值传递