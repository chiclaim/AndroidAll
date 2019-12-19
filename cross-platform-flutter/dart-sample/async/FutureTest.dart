import 'dart:async';
import 'dart:io';

main() {
//  test1();
//  test2();
//  test3();
  test4();
}

test1() async {
  var a = await async01();
  var b = await async02(a);
  var result = async03(b);
  print("result=$result");
}

// 通过 Future 的链式调用代替 test1
test2() {
  async01().then(async02).then(async03).then((result) {
    print("result=$result");
  });
}

// 捕获来自Future的异步异常
test3() async {
  var a = await async01();
  try {
    var b = await async002(a);
    var result = async03(b);
    print("result=$result");
  } catch (e) {}
}

// 捕获链式调用的异常
test4() {
  async01().then(async002).then(async03).then((result) {
    print("result=$result");
  }).catchError((e) {
    print(e);
  }).then(async003); // throw a exception
  //catchError 只会捕获它之前的异常
}

Future<int> async01() async {
  sleep(Duration(seconds: 1));
  print("async01");
  return 1;
}

Future<int> async02(int a) async {
  sleep(Duration(seconds: 1));
  print("async02");
  return a + 2;
}

// throw a exception
Future<int> async002(int a) async {
  sleep(Duration(seconds: 1));
  print("async02");
  throw Exception("test exception");
}

//using for throw a exception after catchError
FutureOr<dynamic> async003(dynamic a) async {
  sleep(Duration(seconds: 1));
  print("async02");
  throw Exception("test exception");
}

async03(int a) {
  sleep(Duration(seconds: 1));
  print("async03");
  return 3 + a;
}
