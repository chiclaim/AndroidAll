main() async {
  Future deleteLotsOfFiles() async {
    print("deleteLotsOfFiles");
    return Future.delayed(Duration(seconds: 5), () {
      return "deleteLotsOfFiles";
    });
  }

  Future copyLotsOfFiles() async {
    print("copyLotsOfFiles");
    return ("copyLotsOfFiles");
  }

  Future checksumLotsOfOtherFiles() async {
    print("checksumLotsOfOtherFiles");
    return ("checksumLotsOfOtherFiles");
  }

  // 虽然是并发执行，但是结果的顺序会是 wait 参数集合的顺序
  var result = await Future.wait([
    deleteLotsOfFiles(),
    copyLotsOfFiles(),
    checksumLotsOfOtherFiles(),
  ]);

  print(result);
  print('Done with all the long steps!');
}
