main() async {

  // 如果加了 await 关键字
  // 是顺序执行，只有等当前任务执行完毕，才能执行下一行代码
  // 不加 await，也会立马执行任务，结果缓存在 Future 里


  // 3 个任务并发执行
  var t1 = task1();
  var t2 = task2();
  var t3 = task3();


  // 下面根据需要获取相应的结果的值
  var r = await t1;
  print(r);

  var r3 = await t3;
  print(r3);

  var r2 = await t2;
  print(r2);
}

Future<String> task1() async {
  print("start task1...");
  return "Task1 Result";
}

Future<String> task2() async {
  print("start task2...");
  return "Task1 Result2";
}

Future<String> task3() async {
  print("start task3...");
  return "Task1 Result3";
}
