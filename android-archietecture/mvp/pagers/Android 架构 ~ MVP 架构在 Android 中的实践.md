

# 为什么要重视程序的架构设计

对程序进行架构设计的原因，归根结底是为了`提高生产力`。架构设计是程序模块化，做到模块内部的`高聚合`和模块之间的`低耦合`(如依赖注入就是低耦合的集中体现)。

这样做的好处是使得程序开发过程中，开发人员主需要专注于一点，提高程序开发的效率，并且更容易进行后续的测试以及定位问题。

但是，设计不能违背目的，对于不同量级的工程，具体的架构实现方式必然不同，不要为了设计而设计，为了架构而架构。比如一个android app如果只有几个Java文件，那只需要做点模块和层次的划分就可以了。引入框架或者架构增加了工作量，降低了生产力。

**所以在开发的时候需要考虑：**

1）当前这个项目是否需要以最快速度上线。比如有些创业公司，争取的就是时间，公司老板是要拿着这个app是去找投资的。

2）如果这个项目开发周期还可以，第一个版本就可以把app架构做好。因为一个App肯定是朝着慢慢做大的方向去的，如果等业务到了一定程度了，再去重构的话，成本就有点大。


# 什么是MVP？

MVP架构由MVC发展而来。在MVP中，M代表Model，V代表View，P代表Presenter。

`Model` 负责获取数据，数据的来源可以是网络或本地数据库等；

`View` 负责界面数据的展示，与用户进行交互；

`Presenter` 是Model与View之间的通信的桥梁，将Model与View分离开来。

MVP架构图：
![MVP](https://img-blog.csdnimg.cn/20190828213550436.png)

**所以MVP的架构有如下好处：**

1）降低了View和Model的耦合，通过Presenter层来通信；

2）把视图层抽象到View接口，逻辑层抽象到Presenter接口，提高了代码的可读性、可维护性；

3）Activity和Fragment功能变得更加单一，只需要处理View相关的逻辑；

4）Presenter抽象成接口，就可以有多种实现，方便单元测试。


下面就来实际的体验一下MVP在项目中的使用吧！(`用户注册`和`文章详情`两个例子)


# 用户注册

功能界面如下图所示（注册、登录）：

<img src="http://img.blog.csdn.net/20170130125437322" width="400"/>

下面是完整的代码，主要实现了验证码注册、登录的功能，可能代码比较多：

**Model部分**

```
public class UserEngine extends BaseEngine {

    public static final int ID_LOGIN = 1;
    public static final int ID_REGISTER = 2;

    private UserApi userApi;

    private UserEngine(Callback callback, int... ids) {
        super(callback, ids);
        userApi = ApiFactory.createService(UserApi.class, true);
    }

    public static UserEngine getInstance(Callback callback, int... ids) {
        return new UserEngine(callback, ids);
    }

    /**
     * 用户登录
     *
     * @param phone    手机号码
     * @param password 密码
     */
    public void login(String phone, String password) {
        //请求服务器
        //成功失败，通过回调通知
    }

    /**
     * 用户注册
     */
    public void register(String phone, String password) {
        //请求服务器
        //成功失败，通过回调通知
    }
}

```

**View**



```

public interface BaseView {

    void showToast(String message);

    void showLoading();

    void hideLoading();

}

public interface ILoginView extends BaseView {

    /**
     * 切换 登录/注册 界面
     */
    void switchUiByActionType(int actionType);

    /**
     * 用户名错误
     *
     * @param errorMsg 错误消息
     */
    void setUsernameError(String errorMsg);

    /**
     * 验证码错误
     */
    void setCodeError(String errorMsg);

    /**
     * 密码错误
     *
     * @param errorMsg 错误消息
     */
    void setPasswordError(String errorMsg);

    /**
     * 登录成功
     */
    void loginSuccess();

    /**
     * 校验验证码成功
     *
     * @param data 成功数据
     */
    void verifySmsCodeSuccess(Object data);

    /**
     * 发送验证码成功
     *
     * @param data 成功数据
     */
    void sendSmsCodeSuccess(Object data);

    /**
     * 获取手机号支持的国家
     *
     * @param data 成功数据
     */
    void getSupportCountrySuccess(Object data);

}

//Activity 实现IView接口
public class UserLoginActivity extends BaseActivity<UserLoginBinding> implements ILoginView
        , MyCountDownTimer.CountDownCallback {


    private static final int ACTION_TYPE_LOGIN = 1;
    private static final int ACTION_TYPE_REGISTER = 2;


    private LoginPresenterImpl loginPresenter;

    private MyCountDownTimer countDownTimer;

    private int actionType;

    public static void launchForLogin(Context context) {
        Intent intent = new Intent(context, UserLoginActivity.class);
        intent.putExtra("actionType", ACTION_TYPE_LOGIN);
        context.startActivity(intent);
    }


    public static void launchForRegister(Context context) {
        Intent intent = new Intent(context, UserLoginActivity.class);
        intent.putExtra("actionType", ACTION_TYPE_REGISTER);
        context.startActivity(intent);
    }

    @Override
    protected void initParams() {
        super.initParams();
        //默认为登录界面
        actionType = getIntent().getIntExtra("actionType", ACTION_TYPE_LOGIN);
        loginPresenter = new LoginPresenterImpl(this);
        countDownTimer = new MyCountDownTimer(60000, 1000, this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        setTitle("登录");

        binding.btnLoginRegister.setOnClickListener(this);
        binding.tvGetCode.setOnClickListener(this);
        binding.tvToggleUi.setOnClickListener(this);

        loginPresenter.initSMSSDK(this);
        loginPresenter.switchUiByActionType(actionType);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_login_layout;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_get_code:
                loginPresenter.sendVerificationCode(binding.etUsername.getText().toString());
                break;
            case R.id.btn_login_register:
                if (actionType == ACTION_TYPE_LOGIN) {
                    loginPresenter.login(xxx);
                } else {
                    loginPresenter.submitVerificationCode(xxx);
                }
                break;
            case R.id.tv_toggle_ui:
                if (VersionUtils.hasKITKAT()) {
                    TransitionManager.beginDelayedTransition(binding.llContainer);
                }
                loginPresenter.switchUiByActionType(
                        actionType == ACTION_TYPE_LOGIN ? ACTION_TYPE_REGISTER : ACTION_TYPE_LOGIN);
                break;
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.showShortToast(this, message);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        loginPresenter.destroy();
    }

    @Override
    public void switchUiByActionType(int actionType) {
        this.actionType = actionType;
        if (actionType == ACTION_TYPE_REGISTER) {
            setTitle("注册");
            binding.tvToggleUi.setText(R.string.flag_login);
            binding.rlPhoneCode.setVisibility(View.VISIBLE);
            binding.btnLoginRegister.setText(R.string.btn_register);
        } else {
            setTitle("登录");
            binding.tvToggleUi.setText(R.string.flag_register);
            binding.rlPhoneCode.setVisibility(View.GONE);
            binding.btnLoginRegister.setText(R.string.btn_login);
        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @Override
    public void setUsernameError(String errorMsg) {
        binding.etUsername.requestFocus();
        binding.tilUsername.setError(errorMsg);

    }

    @Override
    public void setCodeError(String errorMsg) {
        binding.etCode.requestFocus();
        binding.tilPhoneCode.setError(errorMsg);
    }

    @Override
    public void setPasswordError(String errorMsg) {
        binding.etPassword.requestFocus();
        binding.tilPassword.setError(errorMsg);
    }

    @Override
    public void loginSuccess() {
        finish();
    }


    @Override
    public void verifySmsCodeSuccess(Object data) {
        loginPresenter.sendVerificationCode(binding.etCode.getText().toString());
    }

    @Override
    public void sendSmsCodeSuccess(Object data) {
        binding.tvGetCode.setClickable(false);
        countDownTimer.start();
    }

    @Override
    public void getSupportCountrySuccess(Object data) {
        //返回支持发送验证码的国家列表
        Log.d("SMS", data.toString());
        ArrayList<HashMap<String, String>> cs = (ArrayList) data;
        for (HashMap<String, String> map : cs) {
            Log.d("SMS", map.toString());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "-" + entry.getValue());
            }
        }
    }

    @Override
    public void onTimerTick(long millisUntilFinished) {
        binding.tvGetCode.setText(String.format(getString(R.string.count_down_timer)
                , millisUntilFinished / 1000L));
    }

    @Override
    public void onTimerFinish() {
        binding.tvGetCode.setClickable(true);
        binding.tvGetCode.setText(R.string.get_varification_code);
    }

}


```

**Presenter**

```
public interface BasePresenter {
    void destroy();
}

public interface ILoginPresenter extends BasePresenter{

    void switchUiByActionType(int actionType);

    void onUsernameError(String errorMsg);

    void onPasswordError(String errorMsg);

    void onCodeError(String errorMsg);

    void submitVerificationCode(String phone, String code, String password);

    void sendVerificationCode(String phone);

    void login(String username, String password);

    void register(String username, String password, String code);

    void loginSuccess();

    void registerSuccess();

    void loginFailed(String errorMsg);

    void registerFailed(String errorMsg);

    void verifySmsCodeSuccess(Object data);
    void sendSmsCodeSuccess(Object data);
    void getSupportCountrySuccess(Object data);

    void smsFailed(int event, Object data);

    void initSMSSDK(Context context);
}

public class LoginPresenterImpl implements ILoginPresenter,
        BaseEngine.Callback, SMSCallback {

    private UserEngine userEngine;//相当于Model

    private ILoginView loginView;

    private SMSEventHandler smsEventHandler;

    public LoginPresenterImpl(ILoginView loginView) {
        this.loginView = loginView;
        userEngine = UserEngine.getInstance(this, UserEngine.ID_LOGIN, UserEngine.ID_REGISTER);
    }

    @Override
    public void initSMSSDK(Context context) {
        SMSSDK.initSDK(context, "15da6511b04f5", "ec275402ed1402d13d37132c55ae90c0");
        smsEventHandler = new SMSEventHandler(this);
        //注册短信回调
        SMSSDK.registerEventHandler(smsEventHandler);
    }

    public void switchUiByActionType(int actionType) {
        if (loginView != null) {
            loginView.switchUiByActionType(actionType);
        }
    }

    @Override
    public void onUsernameError(String errorMsg) {
        if (loginView != null) {
            loginView.setUsernameError(errorMsg);
        }
    }

    @Override
    public void onCodeError(String errorMsg) {
        if (loginView != null) {
            loginView.setCodeError(errorMsg);
        }
    }

    @Override
    public void onPasswordError(String errorMsg) {
        if (loginView != null) {
            loginView.setPasswordError(errorMsg);
        }
    }

    @Override
    public void sendVerificationCode(String phone) {
        String error;
        if ((error = checkPhone(phone)) != null) {
            onUsernameError(error);
            return;
        }

        if (loginView != null) {
            loginView.showLoading();
        }
        SMSSDK.getVerificationCode("86", phone.trim());
    }

    @Override
    public void submitVerificationCode(String phone, String code, String password) {
        String error;
        if ((error = checkPhone(phone)) != null) {
            onUsernameError(error);
            return;
        } else if ((error = checkCode(code)) != null) {
            onCodeError(error);
            return;
        } else if ((error = checkPassword(password)) != null) {
            onPasswordError(error);
            return;
        }

        SMSSDK.submitVerificationCode("86", phone.trim(), code.trim());
    }

    @Override
    public void register(String phone, String password, String code) {
        String error;
        if ((error = checkPhone(phone)) != null) {
            onUsernameError(error);
            return;
        } else if ((error = checkCode(code)) != null) {
            onCodeError(error);
            return;
        } else if ((error = checkPassword(password)) != null) {
            onPasswordError(error);
            return;
        }
        if (loginView != null) {
            loginView.showLoading();
        }
        if (userEngine != null) {
            userEngine.register(phone, password);
        }
    }


    @Override
    public void login(String phone, String password) {

        String error;
        if ((error = checkPhone(phone)) != null) {
            onUsernameError(error);
            return;
        }
        if ((error = checkPassword(password)) != null) {
            onPasswordError(error);
            return;
        }

        if (loginView != null) {
            loginView.showLoading();
        }
        if (userEngine != null) {
            userEngine.login(phone, password);
        }
    }


    @Override
    public void loginSuccess() {
        if (loginView != null) {
            loginView.hideLoading();
            loginView.showToast("登录成功");
            loginView.loginSuccess();
        }
    }

    @Override
    public void registerSuccess() {
        if (loginView != null) {
            loginView.hideLoading();
            loginView.showToast("登录成功");
            loginView.loginSuccess();
        }
    }

    public void loginFailed(String errorMsg) {
        if (loginView != null) {
            loginView.hideLoading();
            loginView.showToast(errorMsg);
        }
    }


    public void registerFailed(String errorMsg) {
        if (loginView != null) {
            loginView.hideLoading();
            loginView.showToast(errorMsg);
        }
    }

    @Override
    public void onSuccess(int id, Object data) {
        switch (id) {
            case UserEngine.ID_LOGIN:
                loginSuccess();
                break;

            case UserEngine.ID_REGISTER:
                registerSuccess();
                break;
        }
    }

    @Override
    public void onError(int id, int code, String msg) {
        switch (id) {
            case UserEngine.ID_LOGIN:
                loginFailed(msg);
                break;

            case UserEngine.ID_REGISTER:
                registerFailed(msg);
                break;
        }
    }


    /**
     * 验证码校验成功
     */
    @Override
    public void verifySmsCodeSuccess(Object data) {
        if (loginView != null) {
            loginView.hideLoading();
            loginView.verifySmsCodeSuccess(data);
        }
    }

    /**
     * 获取验证码成功
     */
    @Override
    public void sendSmsCodeSuccess(Object data) {
        if (loginView != null) {
            loginView.hideLoading();
            loginView.showToast("验证码发送成功,注意查收");
            loginView.sendSmsCodeSuccess(data);
        }
    }

    /**
     * 返回支持发送验证码的国家列表
     */
    @Override
    public void getSupportCountrySuccess(Object data) {
        if (loginView != null) {
            loginView.hideLoading();
            loginView.getSupportCountrySuccess(data);
        }
    }

    public void smsFailed(int event, Object data) {
        if (loginView != null) {
            loginView.hideLoading();
            String error = ((Throwable) data).getMessage();
            try {
                SmsError smsError = new Gson().fromJson(error, SmsError.class);
                error = smsError.getDetail();
            } catch (Exception e) {
                e.printStackTrace();
            }
            loginView.showToast(error);
        }
    }


    @Override
    public void smsSuccess(int event, Object data) {
        //回调完成
        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
            verifySmsCodeSuccess(data);
        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
            sendSmsCodeSuccess(data);
        } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
            getSupportCountrySuccess(data);
        }
    }

    @Override
    public void smsError(int event, Object data) {
        if (data != null && (data instanceof Throwable)) {
            smsFailed(event, data);
        }
    }


    @Override
    public void destroy() {
        loginView = null;
        if (smsEventHandler != null) {
            SMSSDK.unregisterEventHandler(smsEventHandler);
        }
    }

    private String checkCode(String code) {
        if (TextUtils.isEmpty(code)) {
            return ("请输入验证码");
        }
        return null;
    }

    private String checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return ("请输入手机号");
        }

        if (!StringUtils.isMobilePhone(phone.trim())) {
            return ("手机号格式有误");
        }
        return null;
    }

    private String checkPassword(String password) {
        if (TextUtils.isEmpty(password.trim())) {
            return "密码不能为空";
        } else if (password.length() < 6) {
            return "密码必须大于6位";
        }
        return null;
    }


    private static class SMSEventHandler extends EventHandler {
        SMSCallback callback;

        public SMSEventHandler(SMSCallback callback) {
            this.callback = callback;
        }

        @Override
        public void afterEvent(final int event, int result, final Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.smsSuccess(event, data);
                    }
                });
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.smsError(event, data);
                    }
                });
                ((Throwable) data).printStackTrace();
            }
        }
    }
}

```

上面的代码有详细的注释，方法名定义的也比较形象，一看就知道干什么的。

如果明白了MVP架构的流程图，上面的代码虽然有点长，但是非常很好理解：

Activity 实现了ILoginView接口，所以Activity就是View 层；

Model层呢，在这里就是请求网络。

我们并没有让Activity和Model直接交互，而是通过Presenter层来作为沟通桥梁的。

所以Activity里`组合`了Presenter：

```
public class UserLoginActivity extends BaseActivity<UserLoginBinding> implements ILoginView
        , MyCountDownTimer.CountDownCallback {


    private static final int ACTION_TYPE_LOGIN = 1;
    private static final int ACTION_TYPE_REGISTER = 2;

    private ILoginPresenter loginPresenter; //Presenter

    //省略其他代码...

```

在Presenter中`组合`了ILoginView

```
public class LoginPresenterImpl implements ILoginPresenter,
        BaseEngine.Callback, SMSCallback {

    private UserEngine userEngine;//相当于Model

    private ILoginView loginView;
    
    //省略其他代码...

}
```

这样Presenter和View之间包含了彼此，就可以彼此通信了。这样就把Activity(View) 和 Model之间实现了低耦合。


# 使用MVP架构需要注意的地方

**1）释放资源(在用户离开界面的时候，记得释放Presenter的资源)**

```
@Override
public void destroy() {

    loginView = null;
    
    userEngine.destroy();
    
    if (smsEventHandler != null) {
        SMSSDK.unregisterEventHandler(smsEventHandler);
    }
}
```

**2）方法命名/有些方法应该放在IView接口里还是接口的实现者Activity里的问题** 

我也看了其他一些MVP的例子，很多也是基于登录的例子，比如在登录成功后，跳到主界面，在IView接口定了 `goMain()`或`goMainActivity()` 我觉得这样是不妥的。

假设用户登录成功后的逻辑变了，不跳到主界面了而是其他的逻辑了，那在`ILoginView顶层接口`里定义`goMain()`或`goMainActivity()`就不合适了，因为用不到了，总不能在goMain方法里不写跳到主界面而是写其他代码的逻辑吧，这样方法名和里面的代码就不匹配了。如果更改方法名，那所以实现该接口的类都需要改方法名了，耦合又变大了。

我的建议是，既然是登录成功的逻辑，那就在ILoginView接口里定义`loginSuccess()`方法，不管以后登录成功做什么逻辑都可以在`loginSuccess()`里实现。而不应该`直接把业务逻辑和方法名绑定死了`，并且还把绑定死的方法名放在`ILoginView顶层接口`里，因为业务逻辑是很容易发生变化的。所以应该定义一个通用的方法名`loginSuccess()`。

有人说`goMain()`或`goMainActivity()`，你看多清晰，一看我就知道是干什么的。但是`loginSuccess()`不知道做什么逻辑，还得详细看`loginSuccess()`里的代码。 我觉得考虑的也有一定的道理，但是这不能作为把业务逻辑和方法名绑定死了放到顶层接口里的理由。
如果你出于这种考虑也很好解决的，直接在实现`ILoginView`的Activity里定义 `goMain()` 或 `goMainActivity()` 方法，然后在`loginSuccess()`里调用`goMain()`或`goMainActivity()`。


**3）有些代码可以放在Activity又可以放到Presenter，该怎么抉择？**

我们在使用验证码注册的时候，当用户点击获取验证码的时候，开发者可能就下意识的就在在Activity中调用SMSSDK的API来获取验证码，然后在Activity中处理获取成功和失败的逻辑。

乍一看功能实现没问题，我们可以在Activity中调用SMSSDK的API来获取验证码，也可以在Presenter里调用SMSSDK的API来获取验证码。在使用MVP架构的时候我们时常遇到这样的抉择，在哪里写都可以实现功能，我们该怎么抉择呢？


其实这个时候SMSSDK（只不过SMSSDK是第三方提供的API而已）就是充当着Model的角色，因为SMSSDK就是去请求网络，然后发送验证码的，如果我们直接在Activity(View)中调用了，那么Model和View又耦合了，所以应该放在Presenter中来做。假设有一天发送验证码的SDK我们替换成了其他第三方的SDK了，在View层我们不需要修改一行代码，只需要修改Presenter就可以了。


---


通过上面一个登录的例子，有些读者看到接口中这么多方法，瞬间感觉这个MVP不仅没有帮我们解决问题，反而是问题的制造者。不是这样的！假设我们使用熟悉的MVC开发的时候，我们可能是这样的实现的：

```
//由于篇幅的原因，省略了大量的代码，保留主要逻辑代码
public class UserLoginActivity extends BaseActivity<UserLoginBinding> {

    private UserEngine userEngine;

	//请求登录接口
    private void requestLogin() {
        showLoadingDialog();
        userEngine.login(xxx);
    }

	//请求注册接口
    private void requestRegister() {
        showLoadingDialog();
        userEngine.register(xxx);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_user_login:
                requestLogin();
                break;
            case R.id.tv_register:
                requestRegister();
                break;
        }
    }

    @Override
    public void onSuccess(int id, Object content) {
        super.onSuccess(id, content);
        switch (id) {
            case userEngine.ID_LOGIN:
                //处理登录成功后的逻辑
                break;
            case userEngine.ID_REGISTER:
                //处理注册成功后的逻辑
                break;
        }
    }

    @Override
    public void onError(int id, String msg) {
        super.onError(id, msg);
    }

}
```

上面的代码直接Activity中操作Model，耦合变得比较大了。而且随着业务的增多Activity的代码会越来越臃肿。

其实MVP也就是在上面的代码之上做出一些改良而已，上面我们在View中直接操作Model，现在呢？MVP只不过是View和Model不直接通信了，通过Presenter来通信，Activity(View)操作Presenter，Presenter来操作Model，这样View和Model实现了间接的通信了。


聪明的读者可能又有问题了，分层（View、Presenter）可以啊，但是也不至于每层都设计一个接口吧？那么以接口的形式给我们带来什么好处了呢？

**1）代码更加清晰** 

这样在一定程度上避免了开发者在一个方法里有太多的代码，因MVP强制要求把各个业务都封装在方法里然后调用。

**2）做好`业务`的顶层设计**

在开发的时候，当`产品原型`出来通过了评审后，因为业务都出来了嘛，开发这块就可以把View、Presenter接口的各个函数名全部设计好，做好顶层设计，这也倒逼开发者对这个需求这块一定要有着全面的理解。如果对业务不了解是无法设计出接口的。到时候往方法里填代码就可以了。

**3）解耦** 

我想大家应该注意到了，在View一层我们使用Presenter的时候都是通过IPresenter接口来定义的，而不是该接口的实现着。这样做方便后面替换，这也是`面向接口编程`的好处。到时候业务逻辑变了，我们只要重新实现IPresenter接口，然后在View中替换下IPresenter的实现者即可，而View层不需要修改代码。


# MVP模式实现文章详情

如果对上面的注册的例子理解了，实现这个功能就非常简单了。由于篇幅的原因，具体的代码细节就不帖出来了。把代码的接口设计出来。


该界面主要有`加载文章、评论列表、评论、分享`等功能.

```
public interface IArticleDetailView extends BaseView{
    //加载文章失败
    void onLoadArticleFailed();
    //加载文章成功
    void onLoadArticleSuccess(Article article);
    //加载评论列表
    void onLoadCommentSuccess(Object data);
    //加载评论列表失败
    void onLoadCommentFailed(String msg);
    //添加评论成功
    void addCommentSuccess(Comment comment);
    //添加评论失败
    void addCommentFailed(String errorMsg);
    //分享
    void shareArticle();
}

//在Activity中实现IArticleDetailView接口细节我不贴出来了，根据要求实现即可。


//下面是Presenter接口

public interface IArticleDetailPresenter extends BasePresenter{
    //加载文章
    void loadArticle(Long id);
    //文章加载成功
    void loadArticleSuccess(Article article);
    //文章加载失败
    void loadArticleFailed(String error);
    //加载文章评论列表
    void loadComments(int articleId);
    //加载文章评论列表成功
    void loadCommentSuccess(Object data);
    //加载文章评论列表失败
    void loadCommentFailed(String msg);
    //添加评论
    void addComment(Comment comment);
    //添加评论成功
    void addCommentSuccess(Comment comment);
    //添加评论失败
    void addCommentFailed(String errorMsg);
}

//至于Presenter调用Model的细节也不贴出来了。Model写好了直接调用调用就可以了。

```


是不是很简单，只要把顶层的接口设计出来，接下来就是在方法里填代码就可以了。

# Google android architecture 例子的参考

上面的View和Presenter接口都是新开新建的, [Google android-architecture-todo-mvp-dagger sample](https://github.com/googlesamples/android-architecture/tree/todo-mvp-dagger/) 是把View和Presenter放在一个Contract类中, 如下所示 : 

**AddEditTaskContract.java** 包含了View和Presenter. 这样组装也是不错的选择. 可读性更好, 也非常容易找.

```
/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditTaskContract {

    interface View extends BaseView<Presenter> {

        void showEmptyTaskError();

        void showTasksList();

        void setTitle(String title);

        void setDescription(String description);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveTask(String title, String description);

        void populateTask();
    }
}
```
