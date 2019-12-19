
以前我写过一篇关于 MVP 架构的文章[《Android架构—MVP架构在Android中的实践》](https://blog.csdn.net/johnny901114/article/details/54783106)。

随着业务的复杂化，我们会发现传统的 MVP 架构依然会有很多问题。

下面我将和大家一起探讨下在使用 MVP 架构过程中遇到的比较大的问题以及解决方案。

随着业务逻辑复杂化，我们可能会遇到下面几个比较大的问题：

1. Presenter 中充斥着非常多的业务回调方法，Presenter 非常臃肿
2. 顶层业务逻辑无法重用

## Presenter 臃肿的问题

Prenseter 臃肿的表现形式有两种：

- 第一种：正如我们上面说的 由于 Presenter 有非常多的 **业务回调方法**，比如某个业务需要网络请求，那么成功后怎么处理，对应一个方法，失败了怎么处理，对应一个方法，这样的话基本上一个网络请求至少对应两个方法。如果某个界面业务比较复杂，请求的接口比较多的话，这样的业务回调方法也就比较多

- 第二种：除了业务的回调方法，可能还存在一些业务回调方法的 **辅助方法** 。何谓 **辅助方法**？ 就是为了实现业务回调方法而衍生出的一些方法。比如，某个接口请求成功后，逻辑比较多，可能我们会把某段内聚强的逻辑单独拿出来放在一个新方法里供业务回调方法调用。

所以 Presenter 会有很多 `业务回调方法` 和它衍生的 `辅助方法`。

我一般将业务回调方法命名为：**XXXSuccess()** 和 **XXXFailed()**，**XXXSuccess()** 对应业务请求成功对应的方法， **XXXFailed()** 对应业务请求失败的方法。

这样命名做有两个好处：

- 一是 后期维护的时候我们只需要查询 **Success** 和 **Failed** 相关的方法即可，便于后期修改维护。

- 二是 业务回调方法 和 辅助方法 从名字上就可以区分。 [《Android架构—MVP架构在Android中的实践》](https://blog.csdn.net/johnny901114/article/details/54783106) 也有关于命名这方面的叙述，需要的可以去看下。


Presenter 臃肿的问题，导致 **Presenter** 维护成本变高，可读性变差。因为充斥各种业务回调方法，和一些衍生的辅助方法 。

如果用普通的 MVP  架构来实现，代码 "糟糕" 地自己都不愿意维护了

## 业务逻辑无法重用问题

这个问题不太好描述。为了更好的描述这个问题，我们先来看下我对业务的划分：

- 简单业务：简单业务只由一个 "操作" 组成。比如网络请求、数据库操作等

- 复杂业务 ：一个复杂业务由多个简单业务组成，它像一个业务链。比如一个复杂业务需要多个网络请求然后再把数据呈现给用户。

不管是 **简单业务** 还是 **复杂业务** 我们都是放到 **Presenter** 中。

对于 **复杂业务**，尽管可能调用了多个接口，我们可以使用 **RxJava** 将这些请求通过链式的方式进行组装， 避免 **Callback Hell**

举一个 **复杂业务** 的例子：

```
// 业务接口一：根据用户 id 获取用户的基本信息
userApi.fetchUserInfo("userId")
	.flatMap(new Func1<User, Observable<User>>() {
		@Override
		public Observable<User> call(User user) {
			// 业务接口二：获取用户的好友列表
			return fetchFriendsInfo(user);
		}
	})
	.subscribeOn(Schedulers.io())
	.observeOn(AndroidSchedulers.mainThread())
	.subscribe(new Action1<User>() {
		@Override
		public void call(User user) {
			// 在界面展示 用户的基本信息 和 用户的好友列表
			mView.loadUserSuccess(user);
		}
	}, new Action1<Throwable>() {
		@Override
		public void call(Throwable throwable) {
			throwable.printStackTrace();
			// 在界面提示 对应的错误提示
			mView.loadUserFailed();
		}
	});
```

上面的 **复杂业务逻辑** 的例子主要逻辑为：根据用户 id 获取用户 `基本信息`，成功后获取用户的 `好友列表` ，最后将这些信息展示在界面上。为了实现这个业务逻辑，请求了两个网络接口。

但是，上面的业务逻辑如果外在 Presenter 中是无法复用的。因为 **MVP** 中的 **View** 和 **Presenter** 是一一对应的关系

假设 **A** 界面对应的 **Presenter** 中实现了一个复杂的业务链， 此时 **B** 页面也需要这个 复杂业务链，

**B** 的 **Presenter** 又无法直接使用 **A** 界面的 **Presenter**， 这就出现业务无法重用的问题，**B** 界面的 **Presenter** 还得要把业务链重新写一遍，然后对成功失败的回调进行处理。


## 实际开发需要业务重用的案例

### 案例一

需求描述：扫二维码、条形码，把商品直接接入购物车

在手机上实现扫一扫二维码、条形码，直接把商品加入购物车，这个功能已经实现。 

但是并不是所有的 Android 设备上都会有摄像头，比如一些定制的硬件上可能就没有 ，不过会有外接设备(扫码枪) 来支持扫一扫

所以需要为有扫码枪的系统上支持 **扫二维码、条形码，将商品加入购物车** 的功能

此时也会出现需要重用业务逻辑的情况。业务流程以及业务重用的情况，如下图所示：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190828212652870.png)

一般来说我们都会将手机摄像头的扫一扫功能，封装到一个 Activity 中，比如：**BaseScanActivity**。 

假设手机设备上实现的这个业务逻辑的类名为 **GoodsScanActivity** 该类继承了 **BaseScanActivity** 

现在需要针对扫码枪的设备也实现相同的功能， 但是该业务逻辑 在 **GoodsScanActivity** 对应的 **Presenter** 中， 该业务逻辑很难重用


### 案例二

需求描述：我们的 App 是 to B 的，用户如果有多个店铺会用到 `切换店铺` 的功能：进入 `店铺列表界` 面，点击某个店铺，然后调用 `切店接口`，成功后调用 `初始化接口`

这个功能已经在用户 ｀我的｀ 模块中实现了：我的店铺列表 --> 切店


最近需要开发一个 `开店功能`，这个功能以前是在其他 App 中的，开店成功后也需要 `切换店铺`

这个时候也会出现需要重用业务逻辑的情况。业务流程以及业务重用的情况，如下图所示：

![案例二](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8yNjM2MTM4LTU3ZGYwYTU4ODJkODUzNDMuanBn?x-oss-process=image/format,png)


### 案例三

比如某些硬件内置 Android 系统， 但是弱化屏幕展示功能，或者根本就没有屏幕。这个时候我们就不能直接使用以前的 Module 了

对于 复杂的业务链，我们也无法重用。 这个时候出现业务需要重用的情况会更多



## 解决方案

通过上面案例的分析，我们发现随着业务不断的复杂化，对复杂业务的重用性变得更加紧迫

为了能够将复杂业务重用，我们将其抽取到新的一层中：**Engine** 层，**Presenter** 不直接和 **Model** 交互，改成和 **Engine** 层交互， 再由 **Engine** 层和 **Model** 层进行交互

下面是常规的 MVP 和我们基于MVP改造后的架构对比图：

![MVP架构对比](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8yNjM2MTM4LWE4Yjk0NDZiNjhmNzgyMTEuanBn?x-oss-process=image/format,png)


### 使用基于MVP改造的架构来优化上面的案例一

以第一个业务逻辑重用的案例，我们来实现下：

#### 1) Engine 层，省略其实现类：

```
interface IMenuScanGunEngine : IEngine {
    //二维码
    fun getMenuByUrl(param: MenuScanGunEngine.Param, logic: IMenuByUrlLogic?)  
    //条形码
    fun getMenuByCode(param: MenuScanGunEngine.Param, logic: IMenuByCodeLogic?)
}
```
getMenuByUrl() 与之对应的逻辑回调：

```
interface IMenuByUrlLogic {
    fun scanFailed(errorCode: String?, errorMessage: String?)
    fun gotoComboMenuDetail(menuId: String?, baseMenuVo: BaseMenuVo?)
    fun gotoNormalMenuDetail(baseMenuVo: BaseMenuVo?)
    fun menuTookOff()
    fun menuSoldOut()
    fun addCartSuccess(menuName: String?, dinningTableVo: DinningTableVo?)
}
```

getMenuByCode() 与之对应的逻辑回调

```
interface IMenuByCodeLogic : IMenuByUrlLogic {
    fun showMenuList(list: ArrayList<BoMenu>)
}
```

#### 2) View 层:

**在 View 层实现所有的业务回调**

```
//View 继承了上面两个业务回调接口
interface View : BaseView<Presenter>, IMenuByCodeLogic, IMenuByUrlLogic{
    
}

```


Activity/Fragment 实现业务回调方法，也就是 View 层的实现类，省略具体的实现逻辑：

```
class MenuScanGunActivity:MenuScanGunContract.View{
    //扫码失败
    fun scanFailed(errorCode: String?, errorMessage: String?){
        //ignore...
    }
    //进入套餐详情
    fun gotoComboMenuDetail(menuId: String?, baseMenuVo: BaseMenuVo?){
        //ignore...
    }
    //进入普通商品详情
    fun gotoNormalMenuDetail(baseMenuVo: BaseMenuVo?){
        //ignore...
    }
    //商品下架
    fun menuTookOff(){
        //ignore...
    }
    //商品售罄
    fun menuSoldOut(){
        //ignore...
    }
    //加入购物车成功
    fun addCartSuccess(menuName: String?, dinningTableVo: DinningTableVo?){
        //ignore...
    }
    //一个码对应多个商品，展示一个列表让用户选择
    fun showMenuList(list: ArrayList<BoMenu>){
        //ignore...
    }
}
```

#### 3) Presenter 层：

```
interface Presenter : BasePresenter{
    fun processResultCode(resultCode: String?)
    fun processMenuDetail(menuId: String)
}

class MenuScanGunPresenter(private var mOrderId: String?,
                           private var mSeatCode: String?,
                           private var mView: MenuScanGunContract.View?) : MenuScanGunContract.Presenter {

    private val mEngine = MenuScanGunEngine()

    override fun processResultCode(resultCode: String?) {
        if (mEngine.isURL(resultCode)) {
            mEngine.getMenuByUrl(createParam(resultCode), mView)
        } else {
            mEngine.getMenuByCode(createParam(resultCode), mView)
        }
    }

    override fun processMenuDetail(menuId: String) {
        mEngine.handleMenuDetail(menuId, mView, createParam(menuId = menuId))
    }

    private fun createParam(readCode: String? = null, menuId: String? = null): MenuScanGunEngine.Param {
        return MenuScanGunEngine.Param().apply {
            this.readCode = readCode
            this.menuId = menuId
            this.orderId = mOrderId
            this.seatCode = mSeatCode
        }
    }

    override fun subscribe() {
    }

    override fun unsubscribe() {
        mView = null
        mEngine.destroy()
    }
}
```

通过这个例子我们知道，如果要复用业务逻辑只需要在 `Presenter` 中使用需要的 `Engine` 即可。



## 简单业务是否需要 Engine 层

上面列举的三个案例，都是 `复杂业务` (复杂业务可能是接口请求、数据库操作的组合)，但是在项目中同样会存在很多的 `简单业务` (一个网络请求或者数据库操作)

在这种情况下，我们是否还需要 Engine 层呢？如果再加上 Engine 是否复杂了一点呢？

笔者觉得还是有加上 Engine 层的必要的：

1. 在业务不断迭代的过程中，都是由简单变得复杂
2. Engine 层封装 `简单业务` ，可以更灵活的处理由 简单业务 产生的业务分支

下面我们再举一个实际的案例：

![案例4](https://imgconvert.csdnimg.cn/aHR0cHM6Ly91cGxvYWQtaW1hZ2VzLmppYW5zaHUuaW8vdXBsb2FkX2ltYWdlcy8yNjM2MTM4LTI5ZjAwOWUxYzNiODgwYTUuanBn?x-oss-process=image/format,png)

上面的简单的业务：查询桌位状态，成功后根据不同的状态处理不同的逻辑

上面这个业务逻辑在 `桌位列表` 页用到了，在 `订单搜索` 页也用到了，我们需要在两个不同的地方进行 **status** 判断，然后走不同的逻辑分支

如果我们在 Engine 中在封装一层，就不需要在多个地方进行 **if** 判断了，这些逻辑判断都可以写在 Engine 中，然后对外暴露几个需要关心的业务接口方法即可


## Engine 层 和 Repository 的区别

Google 在 `android-architecture` 中的 MVP 架构中，会把 Model 中的 DataSource 在抽象一层 Repository ，然后 Presenter 调用 Repository ，如下所示：

**View -> Presenter -> Repository -> RemoteDataSource/LocalDataSource**

读者可能会问，你这个 Engine 和这个 Repository 不差不多吗？

其实不一样！ `Repository` 更多的是组合多个 `DataSource`，比如是操作本地数据源，还是调用远程接口，充当的是一个 `底层数据` 提供者的角色

而我们这个 `Engine` 层主要是对顶层业务的封装，而不是对数据的封装

另外，在实际的开发过程中，个人觉得 `Repository` 的作用并不是很大。 当然每个 App 的性质不一样，有些 App 可能对本地数据操作比较多，对 `Model` 层的依赖比较大

如果本地数据操作比较多，其实都可以放到 Engine 层在处理，根据业务逻辑的不同，对本地 Dao 层 和 远程数据层进行组合即可

如果不需要 `Repository` 层的话，那么我们最终的流程是这样的：

**View -> Presenter -> Engine -> RemoteDataSource/LocalDataSource**

下面是我的公众号，干货文章不错过，有需要的可以关注下，有任何问题可以联系我：


## 总结

基于 MVP 架构基础上，我们在 Presenter 和 Model 之间加了一个 Engine 层，使得业务逻辑变得可重用，避免模板代码和逻辑的不一致性问题

同时也解决 Presenter 层代码过于臃肿的问题

View 层的业务回调方法也更加清晰，不同的业务回调，放在不同接口里，也保证了业务回调方法命名的统一

当然，Engine 层只是笔者取的名字，也可以叫做 Business 层等

不管任何架构，在业务不断发展的过程中，可能都需要在某个架构基础上，根据我们的实际业务情况，来做相应的改造和优化。

