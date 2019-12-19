import 'package:flutter/material.dart';
import 'package:flutter_app_demo/future_builder_demo.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

Widget divider = Divider(color: Colors.green);

class _MyHomePageState extends State<MyHomePage> {
  var list = ["FutureBuilder", "StreamBuilder"];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text(widget.title),
        ),
        body: ListView.separated(
          itemCount: list.length,
          itemBuilder: (BuildContext context, int index) {
            return ListTile(
                title: Text(list[index]), onTap: () => handleClick(index));
          },
          separatorBuilder: (BuildContext context, int index) {
            return divider;
          },
        ));
  }

  void handleClick(int index) {
    switch (index) {
      case 0:
        Navigator.push(
          context,
          new MaterialPageRoute(builder: (context) => FutureBuilderDemo()),
        );
        break;
    }
  }
}
