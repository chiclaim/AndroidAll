
在实际开发中我们常常需要inflate要给布局然后添加到某个布局容器里面去, 要把xml布局文件转成一个View对象 需要使用LayoutInflater.inflate方法. 在开发中常常使用如下几种方式:


```
inflater.inflate(layoutId, null);
inflater.inflate(layoutId, root,false);
inflater.inflate(layoutId, root,true);

```

特别是初学者搞不灵清这种方式有什么区别, 下面就一一来看看他们到底有什么不同.



## inflater.inflate(layoutId, null)

现在我有一个MainActivity, 我想往这个布局里add一个布局, 该布局如下所示:

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginLeft="20dp"
    android:background="#50000000"
    android:orientation="vertical">

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="inflate01"
        android:textAllCaps="false"
        android:textSize="20sp" />

</LinearLayout>
```

该布局非常简单, 外层是一个LinearLayout, 背景颜色为`#50000000`, 大小都是`wrap_content`, 距左20dp.

然后在MainActivity执行添加操作:

```
LayoutInflater inflater = LayoutInflater.from(this);
View view = inflater.inflate(R.layout.inflater01, null);
root.addView(view);
```

添加View后, 发现并不是我们想要的, 如下图所示:

![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcwMzEyMTQ1MDU1NDU4?x-oss-process=image/format,png)

从上图可以看出, 被添加的View, 它的宽度并不是`wrap_content`而是占满了屏幕, 还有设置的margin_left也没有效果. 也就是受我们设置的这些都失效了.

那只有看看源码一探究竟了.

**inflater.inflate(R.layout.inflater01, null)** 方法里调用了**inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot)**, 在该方法又调用了 **inflate(XmlPullParser parser, @Nullable ViewGroup root, boolean attachToRoot)** 这个方法是最核心的方法: 

```
public View inflate(XmlPullParser parser, @Nullable ViewGroup root, boolean attachToRoot) {
    synchronized (mConstructorArgs) {

        final Context inflaterContext = mContext;
        final AttributeSet attrs = Xml.asAttributeSet(parser);
        Context lastContext = (Context) mConstructorArgs[0];
        mConstructorArgs[0] = inflaterContext;
        View result = root;

        try {
            // Look for the root node.
            int type;
            while ((type = parser.next()) != XmlPullParser.START_TAG &&
                    type != XmlPullParser.END_DOCUMENT) {
                // Empty
            }

            final String name = parser.getName();
            
            if (TAG_MERGE.equals(name)) {
                if (root == null || !attachToRoot) {
                    throw new InflateException("<merge /> can be used only with a valid "
                            + "ViewGroup root and attachToRoot=true");
                }

                rInflate(parser, root, inflaterContext, attrs, false);
            } else {
                // Temp is the root view that was found in the xml
                //把xml里的相关属性转成View对象
                final View temp = createViewFromTag(root, name, inflaterContext, attrs);

                ViewGroup.LayoutParams params = null;

                //如果参数root不为空
                if (root != null) {
                    // Create layout params that match root, if supplied
                    //根据在XML文件中的设置,生成LayoutParamsView的LayoutParameter用来适应root布局
                    //root布局的根节点是哪个容器(如LinearLayout)那这个params就是哪个容器布局的params(LinearLayout.LayoutParams)
                    params = root.generateLayoutParams(attrs);
                    //如果不添加到跟布局, 则设置刚刚生成的params
                    if (!attachToRoot) {
                        // Set the layout params for temp if we are not
                        // attaching. (If we are, we use addView, below)
                        temp.setLayoutParams(params);
                    }
                }

                // Inflate all children under temp against its context.
                //初始化View所有的子控件
                rInflateChildren(parser, temp, attrs, true);

                // We are supposed to attach all the views we found (int temp)
                // to root. Do that now.
                if (root != null && attachToRoot) {
                    //把View添加到root布局中
                    root.addView(temp, params);
                }

                // Decide whether to return the root that was passed in or the
                // top view found in xml.
                if (root == null || !attachToRoot) {
                    result = temp;
                }
            }

        } catch (XmlPullParserException e) {
            //igore code...
        } catch (Exception e) {
            //igore code...
        } finally {
            //igore code...
        }

        return result;
    }
}

```

上面的代码比较好理解, 主要的地方都加了注释. 

从上面的代码来看 **inflater.inflate(layoutId, null)** 方法, 因为root我们传了null,所以 attachToRoot=false.

根据源码 **inflater.inflate(layoutId, null)** 方法执行主要流程如下:

1, 把xml里的相关属性转成View对象

```
final View temp = createViewFromTag(root, name, inflaterContext, attrs);
```

2, 最后把 View temp返回了.

所以这个View是没有 **LayoutParams** 对象的. 

不信的话可以打印一下就知道了: 

```
View view = inflater.inflate(R.layout.inflater01, null);
ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
if (layoutParams == null) {
    Log.e("MainActivity", "layoutParams is null");
} else {
    Log.e("MainActivity", "layoutParams width:height" + layoutParams.width + ":" + layoutParams.height);
}
```

毫无疑问输出null.

一个View没有 **LayoutParams** 是没办法展示到屏幕上的.

现在只有看看 root.addView(view) 方法了:

```
public void addView(View child) {
    addView(child, -1);
}

public void addView(View child, int index) {
    if (child == null) {
        throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
    }
    LayoutParams params = child.getLayoutParams();
    if (params == null) {
        params = generateDefaultLayoutParams();
        if (params == null) {
            throw new IllegalArgumentException("generateDefaultLayoutParams() cannot return null");
        }
    }
    addView(child, index, params);
}
```

从上面可以看出, 如果 child 的 LayoutParams为null, root会生成一个默认的 LayoutParams 


所以看看 **generateDefaultLayoutParams()** 方法是什么样的? 因为我们这个root是**LinearLayout**, 所以要到 LinearLayout 的 generateDefaultLayoutParams() 里去看,而不是ViewGroup : 

```
@Override
protected LayoutParams generateDefaultLayoutParams() {
    if (mOrientation == HORIZONTAL) {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    } else if (mOrientation == VERTICAL) {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }
    return null;
}

```

我们的root是LinearLayout方向为 VERTICAL 所以 **LayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);** 这就解释了我们是上面运行的效果了: **明明设置的宽和高都是 WRAP_CONTENT, 最后变成了宽度为 MATCH_PARENT, 高度为WRAP_CONTENT**.

```
viewGroup.addView(view, LayoutParams); 
相当于如下两行代码 :
view.setLayoutParams(LayoutParams);
viewGroup.addView(view)
```

有人可能会问 下面三个属性失效了,那 `android:background="#50000000"`又起作用呢?

```
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:layout_marginLeft="20dp"
```

因为**layout_width,layout_height,layout_marginLeft,layout_marginLeft**都属于LayoutParams, 但是**background**属性不属于此范围, 同时所有的标签属性都会存在**AttributeSet**对象里.


## inflater.inflate(layoutId, root,false);

在MainActivity的添加View的方法改成如下形式 :

```
/**
 * inflater.inflate(layoutId, root, false);
 */
private void add02() {
    View view = inflater.inflate(R.layout.inflater01, root, false);
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (layoutParams == null) {
        Log.e("MainActivity", "layoutParams is null");
    } else {
        Log.e("MainActivity", "layoutParams width:height" + layoutParams.width + ":" + layoutParams.height);
    }
    root.addView(view);
}
```

布局文件还是那个布局文件, 不同的只是把inflate方法的参数该了下:

```
inflater.inflate(R.layout.inflater01, root, false);
```

效果如下图所示 : 

![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcwMzEyMTcxODUyMTYz?x-oss-process=image/format,png)

这个才是我们要的效果, 宽高和左边距都是我们要的效果.

根据源码 `inflater.inflate(R.layout.inflater01, root, false);`方法执行流程流程如下:

1, 把xml里的相关属性转成View对象

```
final View temp = createViewFromTag(root, name, inflaterContext, attrs);
```

2, 根据在XML文件中的设置,生成LayoutParamsView的LayoutParameter用来适应root布局, 并且设置给View

```
params = root.generateLayoutParams(attrs);
if (!attachToRoot) {
    // Set the layout params for temp if we are not
    // attaching. (If we are, we use addView, below)
    temp.setLayoutParams(params);
}
```

2, 最后把 View temp 返回了.

所以, 最终呈现出来的是符合我们的预期的.


## inflater.inflate(layoutId, root,true);

**inflater.inflate(layoutId, root,true);** 和 **inflater.inflate(layoutId, root,false); ** 唯一的区别就是 **inflater.inflate(layoutId, root,true);** 会自动把View添加到root里, 不用我们来add进去.

```
/**
 * inflater.inflate(layoutId, root, true);
 */
private void add03() {
    View view = inflater.inflate(R.layout.inflater01, root, true);
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (layoutParams == null) {
        Log.e("MainActivity", "layoutParams is null");
    } else {
        Log.e("MainActivity", "layoutParams width:height" + layoutParams.width + ":" + layoutParams.height);
    }
}
```
