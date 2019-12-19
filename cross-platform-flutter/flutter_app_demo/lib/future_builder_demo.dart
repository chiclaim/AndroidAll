import 'package:flutter/material.dart';
import 'dart:math';

class FutureBuilderDemo extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return FutureBuilderState();
  }
}

class FutureBuilderState extends State<FutureBuilderDemo> {
  Widget divider = Divider(color: Colors.green);

  Future _future;

  @override
  void initState() {
    _future = _loadList();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    print("========FutureBuilderState build");
    return Scaffold(
        appBar: AppBar(title: Text("FutureBuilder")),
        body: FutureBuilder<int>(
            future: _future,
            builder: (context, snapshot) {
              print("-------------> ${snapshot.connectionState.toString()}");
              switch (snapshot.connectionState) {
                case ConnectionState.none:
                case ConnectionState.waiting:
                case ConnectionState.active:
                  return createLoadingWidget();
                case ConnectionState.done:
                  if (snapshot.hasError) {
                    return createErrorWidget(snapshot.error.toString());
                  }
                  return ListView.separated(
                    itemCount: snapshot.data,
                    itemBuilder: (BuildContext context, int index) {
                      return ListTile(title: Text(index.toString()));
                    },
                    separatorBuilder: (BuildContext context, int index) {
                      return divider;
                    },
                  );
                default:
                  return Text("unknown state");
              }
            }));
  }

  Future<int> _loadList() async {
    await Future.delayed(const Duration(seconds: 1));
    if (Random().nextInt(4) % 2 == 0) {
      throw Exception("网络请求失败");
    } else {
      return 100;
    }
  }

  Widget createErrorWidget(String error) {
    return Center(child: Text(error));
  }

  Widget createLoadingWidget() {
    return Center(
      child: SizedBox(
        child: CircularProgressIndicator(
          strokeWidth: 2,
        ),
        height: 30.0,
        width: 30.0,
      ),
    );
  }
}
