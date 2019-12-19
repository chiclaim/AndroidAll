import 'dart:io';

main() {
  //test();
//  test2();
  test3();
//  test4();
}

void test() {
  print("processDir start");

  Future<int> result = processDir();

  result.then((result) {
    print("result:$result");
  });

  print('programing end');
}

// 如果想要等待两个 await 执行完，在返回，可以使用 await processDir();
void test2() async {
  print("processDir start");

  var result = await processDir();

  print("result:$result");

  print('programing end');
}

void test3() async {
  print('programing start');
  await asyncOne();
  print('programing end');
}

void test4() async {
  print('programing start');
  await asyncOne2();
  print('programing end');
}

Future<int> processDir() async {
  await scanValidVersion();
  print("start second await");
  await deleteFileInDir();
  print("processDir finished-------------");
  return 0;
}

/*

Although an async function might perform time-consuming operations,
it doesn’t wait for those operations. Instead,
the async function executes only until it encounters its first await expression (details).
Then it returns a Future object, resuming execution only after the await expression completes.


processDir 执行了两个 await 函数，processDir不会等到两个函数都执行完毕返回

而是执行完第一个 wait 函数，processDir 就会返回



 */

scanValidVersion() async {
  print("scanValidVersion start-------------");
  sleep(const Duration(seconds: 3));
  print("scanValidVersion end-------------");
}

deleteFileInDir() async {
  print("delete start-------------");
  sleep(const Duration(seconds: 3));
  print("delete end-------------");
  return 0;
}

//------------------------

asyncOne() async {
  print("asyncOne start");
  for (num number in [1, 2, 3]) await asyncTwo(number);
  print("asyncOne end");
}

asyncTwo(num) async {
  print("asyncTwo #${num}");
}

asyncOne2() async {
  print("asyncOne start");
  await Future.forEach([1, 2, 3], (num) async {
    await asyncTwo(num);
  });
  print("asyncOne end");
}

/*asyncOne3() async {
  print("asyncOne start");
  await for (int number in [1, 2, 3]) {
    await asyncTwo(number);
  }
  print("asyncOne end");
}*/
