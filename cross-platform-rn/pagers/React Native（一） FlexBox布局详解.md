

在 React Native 中主要使用 FlexBox 来布局。

安装过React Native的开发环境的就知道，在index.android.js/index.ios.js的文件内容如下：

```
export default class TemplateDemo extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit index.ios.js
        </Text>
        <Text style={styles.instructions}>
          Press Cmd+R to reload,{'\n'}
          Cmd+D or shake for dev menu
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

```

上面的:
```
<View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit index.ios.js
        </Text>
        <Text style={styles.instructions}>
          Press Cmd+R to reload,{'\n'}
          Cmd+D or shake for dev menu
</View>
```
就像Android里面的布局一样，其中style是对view如何显示进行定义。 下面我们来看看flexbox的一些属性。



## 一、容器属性
### 1、flexDirection ：
容器内的元素的排列方式[主轴方向]，可取值`row | row-reverse | column | column-reverse`。
`column`: 从上至下，纵向排列[默认]. 效果如下图所示：

<img src="http://img.blog.csdn.net/20161120144201865"  width="360px"/>


<p>
`column-reverse`: 从下至上，纵向排列. 效果如下图所示：
</p>
<img src="http://img.blog.csdn.net/20161120144403381"  width="360px"/>

`row`: 从左至右，水平排列， 效果如下图所示：

<img src="http://img.blog.csdn.net/20161120144445406"  width="360px"/>

`row-reverse`: 从右至左，水平排列，效果如下图所示：

<img src="http://img.blog.csdn.net/20161120144505766"  width="360px"/>


### 2、justifyContent 属性
justifyContent 属性 指定容器内的元素在主轴线的对齐方式，可取值`flex-start | flex-end | center | space-between | space-around`
`flex-start`(默认值)：伸缩项目向一行的起始位置靠齐 ，效果如下图所示：

<img src="http://img.blog.csdn.net/20161120150522374"  width="360px"/>

`flex-end`：元素向一行的结束位置靠齐 ，效果如下图所示：

<img src="http://img.blog.csdn.net/20161120150532936"  width="360px"/>

`center`：元素向一行的中间位置靠齐 ，效果如下图所示：

<img src="http://img.blog.csdn.net/20161120150551249"  width="360px"/>

`space-between`：两端对齐，元素之间的间隔都相等，效果如下图所示：

<img src="http://img.blog.csdn.net/20161120150610013"  width="360px"/>

`space-around`：元素会平均地分布在行里，两端保留一半(中间间隔的一半)的空间，效果如下图所示：

<img src="http://img.blog.csdn.net/20161120150621685"  width="360px"/>

### 3、alignItems属性
alignItems: 侧轴（垂直于主轴）的“对齐方式”。 
flex-start：交叉轴的起点对齐。 效果如下所示：

<img src="http://img.blog.csdn.net/20161120153242605"  width="360px"/>

flex-end：交叉轴的终点对齐 。  效果如下所示：

<img src="http://img.blog.csdn.net/20161120153358230"  width="360px"/>


center：交叉轴的中点对齐。  效果如下所示：

<img src="http://img.blog.csdn.net/20161120153459153"  width="360px"/>

stretch（默认值）：如果项目未设置高度或设为auto，将占满整个容器的高度。我们先把第一个View的height属性注释掉，然后把alignItems设置为stretch，效果如下：

<img src="http://img.blog.csdn.net/20161120153802119"  width="360px"/>

### 4、flexWrap属性
flexWrap: 默认情况下，项目都排在一条线（又称”轴线”）上。flex-wrap属性定义，如果一条轴线排不下，如何换行。 
nowrap(默认值)：不换行。 效果如下：

<img src="http://img.blog.csdn.net/20161120152050064"  width="360px"/>

wrap: 换行，第一行在上方。 效果如下：

<img src="http://img.blog.csdn.net/20161120152112299"  width="360px"/>

wrap-reverse：换行，第一行在下方。【react Native不支持此属性值】




## 二、元素属性 
### 1、flex属性 
flex属性是“flex-grow”、“flex-shrink”和“flex-basis”三个属性的缩写， 
其中第二个和第三个参数（flex-shrink、flex-basis）是可选参数。 
默认值为“0 1 auto”。 
`宽度 ＝ 弹性宽度 ( flexGrow / sum( flexGrow ) ) `
如果该元素设置了宽高，那么他的宽度 = 原来的宽度 + 一行中空白的宽度 (flexGrow / sum(flexGrow))
如下如的三个控件，它们的flex属性分别设置为1，2，3。
据此，第一个控件占屏幕宽度的 **1/(1+2+3)**，第二个控件占 **2(1+2+3)**，第三个控件占屏幕的 **3/(1+2+3)** 如下所示：

<img src="http://img.blog.csdn.net/20161120152607821"  width="360px"/>


### 2、alignSelf属性
alignSelf取值范围：“auto | flex-start | flex-end | center | baseline | stretch” 
align-self属性允许单个项目有与其他项目不一样的对齐方式，可覆盖align-items属性。 
默认值为auto，表示继承父元素的`alignItems`属性，如果没有父元素，则等同于`stretch`。 
注意：react Native不支持此属性值: baseline
现在我们把第二个View设置为alignSelf:'flex-start',效果如下：

<img src="http://img.blog.csdn.net/20161120155042410"  width="360px"/>

设置为 **alignSelf:'flex-end'** 效果如下：

<img src="http://img.blog.csdn.net/20161120155142699"  width="360px"/>

设置为alignSelf:'center' 效果如下：

<img src="http://img.blog.csdn.net/20161120155243685"  width="360px"/>

设置为alignSelf:'stretch' 且不设置高度，效果如下：

<img src="http://img.blog.csdn.net/20161120155437881"  width="360px"/>


## 三、几个简单的示例
1,绝对定位和相对定位： 
与css定位不同，在React Native中定位不需要再父组件中设置position属性。

通常情况下设置position:'relative'，和不设置position属性，定位的效果是一样的
但是如果父组件设置了内边距，position会做出相应的定位改变，而absolute则不会。

<img src="http://img.blog.csdn.net/20161120161705937"  width="360px"/>

如果View设置了position: 'absolute'，那么该View或父View设置padding对该View都无效：

<img src="http://img.blog.csdn.net/20161120161203637"  width="360px"/>


2,获取宽高分辨率 
```
var width = require(‘Dimensions’).get(‘window’).width; 
var height = require(‘Dimensions’).get(‘window’).height; 
var scale = require(‘Dimensions’).get(‘window’).scale; 
```

还有一种更简洁的方式： 
```
var {width,height,scale} = require(‘Dimensions’).get(‘window’);
```

<img src="http://img.blog.csdn.net/20161120162006611"  width="360px"/>

3,默认宽度问题 
View不设置宽度默认占一行
把上面显示屏幕分辨率的View设置一个背景，就会发现View不设置宽度默认是占一行的：

<img src="http://img.blog.csdn.net/20161120162231800"  width="360px"/>



## 四、本博客所有代码展示：
```

import React, {Component} from 'react';
import {
    AppRegistry,
    StyleSheet,
    Text,
    View
} from 'react-native';
//330 375  =  45   15+30
var {width, height,scale} = require('Dimensions').get('window');
export default class FlexBoxDemo extends Component {
    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.text_style1}>{width}</Text>
                <Text style={styles.text_style2}/>
                <Text style={styles.text_style3}/>
                <Text style={styles.text_style4}/>
                <Text style={styles.text_style5}/>
                <Text style={styles.text_style6}/>
            </View>
        );
    }
}

class PositionDemo extends Component {
    render() {
        return (
            <View style={{backgroundColor: '#F5FCFF', height: 200, paddingTop: 30,paddingBottom:100,flexDirection:'column'}}>
                <Text style={styles.positionStyle}/>

                <Text style={{backgroundColor:'gray'}}>width x height={width} x {height}; scale:{scale}</Text>
                {/*默认占一行
                <Text style={{backgroundColor:'blue'}}/>*/}
            </View>
        );
    }
}


const styles = StyleSheet.create({
    container: {
        //flex: 1,
        backgroundColor: '#F5FCFF',
        flexDirection: 'row',
        marginTop: 30,
        height: 200,
        justifyContent: 'flex-start',
        alignItems: 'stretch',
        flexWrap: 'wrap',
    },
    text_style1: {
        width: 40,
        height: 50,
        backgroundColor: '#9900ff',
        //flex: 1,

        //textAlign: 'center',//文字仅仅水平居中
    },

    text_style2: {
        width: 40,
        height: 60,
        backgroundColor: '#99ee00',
        //flex: 2,
        // alignSelf: 'stretch'
    },

    text_style3: {
        width: 80,
        height: 30,
        backgroundColor: '#ff9900',
        //flex: 3,

    },

    text_style4: {
        width: 50,
        height: 70,
        backgroundColor: '#99ff00',
        //flex: 3,
    },

    text_style5: {
        width: 90,
        height: 70,
        backgroundColor: '#99ee00',
        //flex: 3,
    },
    text_style6: {
        width: 80,
        height: 70,
        backgroundColor: '#ff9900',
    },

    positionStyle: {
        //flex:1,
        height: 50,
        width: 100,
        backgroundColor: 'black',
        position: 'absolute',//absolute
        bottom: 20,
        left: 120,
        //paddingBottom: 40, //如果position:'absolute'，则paddingBottom:40无效
        //通常情况下设置position和absolute，定位的效果是一样的，
        //但是如果父组件设置了内边距，position会做出相应的定位改变，而absolute则不会。
    }

});

AppRegistry.registerComponent('FlexBoxDemo', () => PositionDemo);

```

