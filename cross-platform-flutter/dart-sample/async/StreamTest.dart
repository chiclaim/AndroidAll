import 'dart:convert';
import 'dart:io';

/// Stream 初步使用
main() {
//  readFile("StreamTest.dart");
//  readFile2("StreamTest.dart");
  readFile2("StreamTest.dart");
}

readFile(String path) async {
  var config = File(path);
  Stream<List<int>> stream = config.openRead();
  var lines = stream.transform(Utf8Decoder()).transform(LineSplitter());

  try {
    await for (var line in lines) {
      print(line);
    }
  } catch (e) {
    print(e);
  }
}

readFile2(String path) {
  var config = File(path);
  Stream<List<int>> stream = config.openRead();
  stream.transform(Utf8Decoder()).transform(LineSplitter()).listen((line) {
    print((line));
  }, onDone: () {
    print("-----read done-----");
  }, onError: (e) {
    print(e);
  });
}
