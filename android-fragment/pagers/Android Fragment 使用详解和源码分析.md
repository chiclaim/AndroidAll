
## 一、Fragment 生命周期

要想很好的掌握 Fragment 的使用，首先要掌握 Fragment 在各种情况下的生命周期表现。

**不考虑 Activity，Fragment的生命周期：**

```
//显示
Fragment onAttach
Fragment onCreate
Fragment onCreateView
Fragment onViewCreated
Fragment onActivityCreated
Fragment onStart
Fragment onResume

//关闭
Fragment onPause
Activity onPause
Fragment onStop
Activity onStop
Fragment onDestroyView
Fragment onDestroy
Fragment onDetach

```


**一个Fragment从创建到显示在Activity中的生命周期：**

```
Activity onCreate, savedInstanceState=null
Fragment onAttach
Activity onAttachFragment
Fragment onCreate
Fragment onCreateView
Fragment onViewCreated
Fragment onActivityCreated
Fragment onStart
Activity onStart
Activity onResume
Fragment onResume

```

**锁屏或按Home键回到桌面的生命周期：**

```
Fragment onPause
Activity onPause
Fragment onSaveInstanceState
Activity onSaveInstanceState
Fragment onStop
Activity onStop

```

**解锁或重新回到该界面的生命周期：**

```
Activity onRestart
Fragment onStart
Activity onStart
Activity onResume
Fragment onResume

```

**当前界面已经存在一个Fragment了然后再添加新Fragment的生命周期：**

```
New Fragment onAttach
Activity onAttachFragment
New Fragment onCreate
New Fragment onCreateView
New Fragment onViewCreated
New Fragment onActivityCreated
New Fragment onStart
New Fragment onResume

```
需要注意的是，如果当前显示的Activity中已经存在了一个Fragment，然后再添加一个新Fragment，其中老的Fragment的生命周期方法是不会调用的。这个有区别于Activity界面之间的切换。

**弹出刚刚新添加的Fragment的生命周期：**

```
New Fragment onPause
New Fragment onStop
New Fragment onDestroyView
New Fragment onDestroy
New Fragment onDetach

```

同理，弹出（Pop）刚刚添加的Fragment和添加的时候一样，只会调用它自己的生命周期方法。

**Activity只存在一个Fragment，关闭整个界面的生命周期：**

```
Fragment onPause
Activity onPause
Fragment onStop
Activity onStop
Fragment onDestroyView
Fragment onDestroy
Fragment onDetach
Activity onDestroy

```

**旋转屏幕（Activity包含一个Fragment）**

```
//Part1

Fragment onPause
Activity onPause
Fragment onSaveInstanceState outState=Bundle[{}]
Activity onSaveInstanceState
Fragment onStop
Activity onStop
Fragment onDestroyView
Fragment onDestroy
Fragment onDetach
Activity onDestroy

//Part2

Fragment onAttach
Activity onAttachFragment
Fragment onCreate Bundle=Bundle[{...}]
Activity onCreate, savedInstanceState=Bundle[{...}]
Fragment onCreateView Bundle=Bundle[{...}]
Fragment onViewCreated
Fragment onActivityCreated
Fragment onAttach
Activity onAttachFragment
Fragment onCreate Bundle=null
Fragment onCreateView Bundle=null
Fragment onViewCreated
Fragment onActivityCreated
Fragment onStart
Fragment onStart
Activity onStart
Activity onRestoreInstanceState Bundle=Bundle[{...}]
Activity onResume
Fragment onResume
Fragment onResume

```

从上面这个（旋转屏幕）生命周期执行可以看出：

`Part1`生命周期：把竖屏的Fragment和Activity销毁掉，执行了它俩的销毁的相关方法。

`Part2`生命周期（创建横屏的界面）：Activity的onCreate()方法执行了一次，Fragment的onCreate()方法执行了两次，也就是说创建了两次。

Fragment生命周期为什么输出了两次呢？ 下面我们另起一部分从源码的角度单独来分析具体原因。


## 二、从源码角度分析Activity中的存在多个Fragment实例的原因

Part1的生命周期方法就不说了，销毁竖屏的Activity、Fragment界面，注意它执行了Activity和Fragment的onSaveInstanceState(Bundle)方法，意思就是如果需要保存一些状态可以把数据放在Bundle里面。

先来看看Activity的onCreate()方法：

```
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    log("Activity onCreate, savedInstanceState=" + savedInstanceState);
    setContentView(R.layout.activity_fragment_container);
    fragmentLifecycle = new FragmentLifecycle();

    showFragment(fragmentLifecycle);
}

public void showFragment(@NonNull Fragment fragment) {
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    if (fragment.isAdded()) {
        ft.show(fragment);
    } else {
        ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.add(R.id.container, fragment);
    }
    ft.commitAllowingStateLoss();
}

```

上面的代码很简单，就是在Activity onCreate方法创建一个Fragment 然后添加到 FragmentTransaction，showFragment() 方法判断 fragment 是否已经添加过了， 如果添加过了就显示，  否则执行add方法。就是这样简单的代码，为什么旋转屏幕后会创建两次Fragment呢？

我们先来看看 FragmentTransaction.add()方法源代码。

### FragmentTransaction.add()源码分析

我们发现 FragmentTransaction 是一个抽象类，它的子类为 BackStackRecord

```
@Override
public FragmentTransaction add(int containerViewId, Fragment fragment) {
    doAddOp(containerViewId, fragment, null, OP_ADD);
    return this;
}


private void doAddOp(int containerViewId, Fragment fragment, String tag, int opcmd) {
    final Class fragmentClass = fragment.getClass();
    final int modifiers = fragmentClass.getModifiers();

    fragment.mFragmentManager = mManager;

    //设置fragment tag
    if (tag != null) {
        fragment.mTag = tag;
    }
    
    //把容器布局Id作为fragmentId
    if (containerViewId != 0) {
        fragment.mContainerId = fragment.mFragmentId = containerViewId;
    }

    把fragment赋值给Op属性，然后把Op放进List里面
    Op op = new Op();
    op.cmd = opcmd;
    op.fragment = fragment;
    addOp(op);
}

void addOp(Op op) {
    mOps.add(op);
    op.enterAnim = mEnterAnim;
    op.exitAnim = mExitAnim;
    op.popEnterAnim = mPopEnterAnim;
    op.popExitAnim = mPopExitAnim;
}

```

上面的代码总结一句话，就是把Fragment放进一个ArrayList里面去了。

## 分析FragmentTransaction.commitAllowingStateLoss()方法

该方法的调用链很长，差不多9个调用，所以就不贴方法，太长了。下面是它的调用链：

```
BackStateRecord.commitAllowingStateLoss() --> BackStateRecord.commitInternal() -->
FragmentManager.enqueueAction()--> FragmentManager.scheduleCommit() --> mExecCommith回调 -->
FragmentManager.execPendingActions() --> FragmentManager.optimizeAndExecuteOps -->
FragmentManager.executeOpsTogether --> FragmentManager.executeOps ---> record.executePopOps();
```
看看最终调用的方法record.executePopOps()：

```
void executeOps() {
    final int numOps = mOps.size();
    for (int opNum = 0; opNum < numOps; opNum++) {
        final Op op = mOps.get(opNum);
        final Fragment f = op.fragment;
        f.setNextTransition(mTransition, mTransitionStyle);
        switch (op.cmd) {
            case OP_ADD:
                f.setNextAnim(op.enterAnim);
                mManager.addFragment(f, false);
                break;
            case OP_REMOVE:
                f.setNextAnim(op.exitAnim);
                mManager.removeFragment(f);
                break;
            case OP_HIDE:
                f.setNextAnim(op.exitAnim);
                mManager.hideFragment(f);
                break;
            case OP_SHOW:
                f.setNextAnim(op.enterAnim);
                mManager.showFragment(f);
                break;
            case OP_DETACH:
                f.setNextAnim(op.exitAnim);
                mManager.detachFragment(f);
                break;
            case OP_ATTACH:
                f.setNextAnim(op.enterAnim);
                mManager.attachFragment(f);
                break;
            default:
                throw new IllegalArgumentException("Unknown cmd: " + op.cmd);
        }
        if (!mAllowOptimization && op.cmd != OP_ADD) {
            mManager.moveFragmentToExpectedState(f);
        }
    }
    if (!mAllowOptimization) {
        // Added fragments are added at the end to comply with prior behavior.
        mManager.moveToState(mManager.mCurState, true);
    }
}
```

最终也就是对FragmentManager里管理的Fragments进行状态切换，或者把需要添加的Fragment放进FragmentManager的集合（如mAdd），或者是把需要删除的从集合里删除掉。

OK，分析完FragmentTransaction.add()和commit()方法，我们继续分析从旋转屏幕的生命周期输出：

```
//Part2

Fragment onAttach
Activity onAttachFragment
Fragment onCreate Bundle=Bundle[{...}]
Activity onCreate, savedInstanceState=Bundle[{...}]
...

```

可以看出重建的时候 Fragment的生命周期先执行，如 Fragment.onCreate比Activity.onCreate先执行，
说明父Activity是不是做了Fragment初始化的操作，所以我们要去FragmentActivity.onCreate()源码里去看看。


### FragmentActivity.onCreate() 源码分析

```
protected void onCreate(@Nullable Bundle savedInstanceState) {
    mFragments.attachHost(null /*parent*/);

    super.onCreate(savedInstanceState);

    NonConfigurationInstances nc =
            (NonConfigurationInstances) getLastNonConfigurationInstance();
    if (nc != null) {
        mFragments.restoreLoaderNonConfig(nc.loaders);
    }
    if (savedInstanceState != null) {
        Parcelable p = savedInstanceState.getParcelable(FRAGMENTS_TAG);
        
        //恢复Fragments实例
        mFragments.restoreAllState(p, nc != null ? nc.fragments : null);
    }
    
    //为了简洁，把以前其他的代码去掉了

    //调用mFragments的onCreate生命周期方法
    mFragments.dispatchCreate();
}

```

上面最重要的有两处代码：

```
//恢复Fragments实例
mFragments.restoreAllState(p, nc != null ? nc.fragments : null);
    
//调用mFragments的onCreate生命周期方法
mFragments.dispatchCreate();

```

**mFragments.restoreAllState**方法里的代码比较多，为了精简把有些代码去掉了：

```
void restoreAllState(Parcelable state, FragmentManagerNonConfig nonConfig) {
    // If there is no saved state at all, then there can not be
    // any nonConfig fragments either, so that is that.
    if (state == null) return;
    FragmentManagerState fms = (FragmentManagerState)state;
    if (fms.mActive == null) return;

    List<FragmentManagerNonConfig> childNonConfigs = null;

    // Build the full list of active fragments, instantiating them from
    // their saved state.
    //这个集合用于保存激活状态的Fragment
    mActive = new ArrayList<>(fms.mActive.length);
    if (mAvailIndices != null) {
        mAvailIndices.clear();
    }
    for (int i=0; i<fms.mActive.length; i++) {
        FragmentState fs = fms.mActive[i];
        if (fs != null) {
            FragmentManagerNonConfig childNonConfig = null;
            if (childNonConfigs != null && i < childNonConfigs.size()) {
                childNonConfig = childNonConfigs.get(i);
            }
            //通过反射创建Fragment对象
            Fragment f = fs.instantiate(mHost, mParent, childNonConfig);
            if (DEBUG) Log.v(TAG, "restoreAllState: active #" + i + ": " + f);
            //把Fragment放进List中
            mActive.add(f);
            // Now that the fragment is instantiated (or came from being
            // retained above), clear mInstance in case we end up re-restoring
            // from this FragmentState again.
            fs.mInstance = null;
        } else {
            mActive.add(null);
            if (mAvailIndices == null) {
                mAvailIndices = new ArrayList<Integer>();
            }
            if (DEBUG) Log.v(TAG, "restoreAllState: avail #" + i);
            mAvailIndices.add(i);
        }
    }

    // Build the list of currently added fragments.
    //把已经添加过的Fragment放进mAdded List。通过add方法添加的Fragment都是放在该List里面的
    if (fms.mAdded != null) {
        mAdded = new ArrayList<Fragment>(fms.mAdded.length);
        for (int i=0; i<fms.mAdded.length; i++) {
            Fragment f = mActive.get(fms.mAdded[i]);
            if (f == null) {
                throwException(new IllegalStateException(
                        "No instantiated fragment for index #" + fms.mAdded[i]));
            }
            f.mAdded = true;
            if (DEBUG) Log.v(TAG, "restoreAllState: added #" + i + ": " + f);
            if (mAdded.contains(f)) {
                throw new IllegalStateException("Already added!");
            }
            mAdded.add(f);
        }
    } else {
        mAdded = null;
    }

    //如果设置了 BackStack，构建mBackStack List，调用了FragmentTransaction.addToBackStack就会产生BackStack
    // Build the back stack.
    if (fms.mBackStack != null) {
        mBackStack = new ArrayList<BackStackRecord>(fms.mBackStack.length);
        for (int i=0; i<fms.mBackStack.length; i++) {
            BackStackRecord bse = fms.mBackStack[i].instantiate(this);
            if (DEBUG) {
                Log.v(TAG, "restoreAllState: back stack #" + i
                    + " (index " + bse.mIndex + "): " + bse);
                LogWriter logw = new LogWriter(TAG);
                PrintWriter pw = new PrintWriter(logw);
                bse.dump("  ", pw, false);
                pw.close();
            }
            mBackStack.add(bse);
            if (bse.mIndex >= 0) {
                setBackStackIndex(bse.mIndex, bse);
            }
        }
    } else {
        mBackStack = null;
    }
}
```

**restoreAllState** 主要是恢复Fragment实例(包括开发者保留的数据)，然后这些实例放进相关的List（mAdded、mActive）中，该List里面有多少个framgent就会恢复多少个，也就是说如果按照我们上面的代码来操作Fragment，在Activity的onCreate方法里直接new一个Fragment然后直接add，屏幕旋转的次数越多，List里面的的Fragment就越多，所以这样的方式使用Fragment是不行的。

下面看看onCreate里的**mFragments.dispatchCreate();**方法：

```
public void dispatchCreate() {
    mHost.mFragmentManager.dispatchCreate();
}


public void dispatchCreate() {
    mStateSaved = false;
    mExecutingActions = true;
    moveToState(Fragment.CREATED, false);
    mExecutingActions = false;
}
```

就是把Fragment的状态设置为CREATED，并且执行相关生命周期方法。

我们在旋转屏幕后，系统试图恢复竖屏界面显示的数据和状态，我们知道操作Fragment我们通过FragmentManager，FragmentTransaction 两个类来操作的，FragmentTransaction 是抽象类，我们操作的其实是它的子类 BackStackRecord，add fragment的时候把 Fragment放进了Bac  kStackRecord的一个容器里面（`ArrayList<Op> mOps`）。

所以说呢，在FragmentManager中用几个集合来保存不同状态的Fragments，如下所示：

```
    ArrayList<Fragment> mActive; 处于激活状态的Fragments
    ArrayList<Fragment> mAdded;  被添加的Fragments
    ArrayList<BackStackRecord> mBackStack; 保存设置了回退栈的Fragments
```


从上面的界面旋转屏幕所执行的生命周期方法，我们发现Fragment的生命周期输出了两遍，是因为在屏幕旋转前Activity就已经存在了一个Fragment了（当前显示的），然后旋转屏幕，销毁重建当前的Activity，通过上面分析的Activity onCreate方法，在创建该Activity的时候，回去检测是否有fragment需要恢复，发现有一个Fragment需要恢复，然后执行Fragment相关的生命周期方法，最后我们在覆写的Activity onCreate方法又直接new了一个fragment，添加到Activity中，所以控制台输出了两遍fragment的生命周期方法。


从上面我们得知，在执行FragmentActivity的onCreate的时候，系统会试图恢复之前的framgent，我们都知道要恢复数据 ，肯定要有一个地方保存数据（fragment），自然而然想到**onSaveInstanceState(Bundle outState)**方法，下面我们就来看下，系统在杀死当前进程的时候是怎么保存当前显示的framgent的：

```
FragmentActivity.java

@Override
protected void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);
	//其实就是调用FragmentManager的saveAllState()方法，获取需要保存的fragments的相关信息
	Parcelable p = mFragments.saveAllState();
	//如果存在则保存在outState中，供下次恢复的Activity的时候使用
	if (p != null) {
		outState.putParcelable(FRAGMENTS_TAG, p);
	}
	//省掉其他代码
}
```
**FragmentManager.saveAllState()**

```
Parcelable saveAllState() {
   
   //把所有激活的Fragments需要保存的数据放在FragmentState[]中
	int N = mActive.size();
	FragmentState[] active = new FragmentState[N];
	boolean haveFragments = false;
	for (int i=0; i<N; i++) {
		Fragment f = mActive.get(i);
		if (f != null) {
		
			haveFragments = true;

			FragmentState fs = new FragmentState(f);
			active[i] = fs;

			if (f.mState > Fragment.INITIALIZING && fs.mSavedFragmentState == null) {
				fs.mSavedFragmentState = saveFragmentBasicState(f);

				if (f.mTarget != null) {
					//ignore some code...
					if (fs.mSavedFragmentState == null) {
						fs.mSavedFragmentState = new Bundle();
					}
					
					//保存该fragment的index
					putFragment(fs.mSavedFragmentState,
							FragmentManagerImpl.TARGET_STATE_TAG, f.mTarget);
							
					//保存fragment相关联的requestCode
					if (f.mTargetRequestCode != 0) {
						fs.mSavedFragmentState.putInt(
								FragmentManagerImpl.TARGET_REQUEST_CODE_STATE_TAG,
								f.mTargetRequestCode);
					}
				}

			} else {
				fs.mSavedFragmentState = f.mSavedFragmentState;
			}
		}
	}

	//把所以添加过的Fragment的索引放在added数组中
	int[] added = null;
	BackStackState[] backStack = null;

	// Build list of currently added fragments.
	if (mAdded != null) {
		N = mAdded.size();
		if (N > 0) {
			added = new int[N];
			for (int i=0; i<N; i++) {
				added[i] = mAdded.get(i).mIndex;
				//ignore some code...
			}
		}
	}

	//把fragment栈相关的信息保存在backStack数组中
	if (mBackStack != null) {
		N = mBackStack.size();
		if (N > 0) {
			backStack = new BackStackState[N];
			for (int i=0; i<N; i++) {
				backStack[i] = new BackStackState(mBackStack.get(i));
				//ignore some code...
			}
		}
	}
	
	//最后把上面的信息封装在FragmentManagerState，然后返回，供上层保存
	FragmentManagerState fms = new FragmentManagerState();
	fms.mActive = active;
	fms.mAdded = added;
	fms.mBackStack = backStack;
	return fms;
}


```

Activity就是这样恢复fragments的，在Activity.onSaveInstanceState方法中调用**FragmentActivity.restoreAllState**方法，在Activity.onCreate方法中调用**FragmentActivity.saveAllState**方法。


## 上面的例子中，如何避免创建两个Fragment

既然系统在销毁的时候，把Activity中的Fragment保存起来了，我们就从里面取该fragment好了，不用新new一个fragment了，这样就可以避免创建新的Fragment了。通过FragmentManager.findFragmentByXX系列方法：

```
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Log.e("Kill", "MainActivity onCreate : " + " state: " + savedInstanceState);

    //如果有保存的数据，则尝试去获取fragment
    if (savedInstanceState != null) {
        myFragment = getSupportFragmentManager().findFragmentById(R.id.container);
    }

    //如果没有，重新创建Fragment
    if (myFragment == null) {
        myFragment = new MyFragment();
    }

    showFragment(getSupportFragmentManager(), myFragment, R.id.container);

}
```


**FragmentManager.findFragmentById()分析**

```
@Override
public Fragment findFragmentById(int id) {
    if (mAdded != null) {
        // First look through added fragments.
        for (int i=mAdded.size()-1; i>=0; i--) {
            Fragment f = mAdded.get(i);
            if (f != null && f.mFragmentId == id) {
                return f;
            }
        }
    }
    if (mActive != null) {
        // Now for any known fragment.
        for (int i=mActive.size()-1; i>=0; i--) {
            Fragment f = mActive.get(i);
            if (f != null && f.mFragmentId == id) {
                return f;
            }
        }
    }
    return null;
}

```

上面代码很简单，一开始从被添加的集合mAdded中查找，如果没有再从激活的集合mActive中查找。但是需要注意的是，这个时候的FragmentManager的相关集合（mAdded、mActive）里的fragment都是在FragmentActivity.onCreate()方法里恢复的，看看上面分析的FragmentActivity.onCreate()分析就知道了。所以如果有fragment被恢复了，肯定会被放在FragmentManager的相关集合中来进行管理。


## App进程被系统杀死后用户重新进入APP的问题

通过上面的分析，一个Fragment是被包含在一个Activity中的，并且一个Fragment实例只能被包含一次，如果多次调用add(fragment)方法，程序就会闪退，报**java.lang.IllegalStateException: Fragment already added:......** 异常。

我们知道，当系统内存紧张的时候，我们的app进程退到后台，有可能会被系统kill掉。那么我们如何来模拟app进程被系统杀死呢？有两种方法可以来模拟：

1. 在开发者选项中，有个`后台进程限制`的选项，一般默认为`标准限制`，
你可以把它设置为`不允许后台进程`，但是这个选项在有的机器上不好使，
如果不起作用的话，可以设置为`不得超过一个进程`，按home键把我们的app置为后台进程，
然后启动一个其他的app，按home键，回到桌面启动我们的app，这样就可以达到类似的效果。

2. 把我们的app置为后台进程后，打开设置，动态修改app权限状态，
比如一开始app有相机权限，通过设置把该权限禁用掉，然后再次点击我们的app，这样也可以达到类似的效果。

当app被杀死后 ，我们再次进入的时候系统就会试图恢复推到后台是app正在显示的界面，产生的行为大致和上面分析Fragment旋转屏幕生命周期的时候类似。

所以如果处理不好也会出现一个fragment界面，多个fragment实例。所以一定要记得使用findFragmentByXX方法：

```
if (savedInstanceState != null) {
    myFragment = getSupportFragmentManager().findFragmentById(R.id.container);
}

if (myFragment == null) {
    myFragment = new MyFragment();
}
```

当然如果一个Activity包含多种不行界面的Fragment，我们可以通过**findFragmentByTag()**方法来获取。

显示fragment的时候也要注意，如果该fragment已经被add了就不再在执行add方法了，如：

```
FragmentTransaction ft = fragmentManager.beginTransaction();
if (fragment.isAdded()) {
    ft.show(fragment);
} else {
    //如果同一个fragment实例被add都次会闪退:Fragment already added
    ft.add(frameId, fragment);
}
ft.commitAllowingStateLoss();
```

## 总结

1. activity包含fragment的生命周期执行顺序，由内到外。先执行Fragment然后再执行Activity的，如先执行Fragment onPause再执行Activity onPause。例外的是在显示的时候，是先执行Activity onResume再执行Fragment onResume，很好理解，只有Activity显示了，Fragment才能显示。宿主关系。

2. 如果仅仅是把 Activity 中的Fragment 弹出（调用popBackStack()方法弹出）只会执行Fragment的生命周期方法，不会影响Activity的生命周期方法

3. 如果想要保存Fragment一些数据，可以在`onSaveInstanceState(Bundle outState)`方法里可以把数据放在Bundle里，然后系统会把Bundle通过`onCreate()，onCreateView()，onActivityCreated()`方法参数传递给我们。

4. 当需要在Activity中传递参数给Fragment的时候，不要通过在Fragment定义方法，然后在Activity中调用Fragment方法把数据传递过去，这样的方式容易在Activity销毁重建的时候丢失该参数，因为重建了，fragment实例就没有Activity传递的参数了，因为整个Fragment对象重置了，通过全局变量保存的参数就丢失了，可以通过如下方式传递参数：

```
Bundle bundle = new Bundle();
bundle.putString(key,value);
myFragment.setArguments(bundle);

```

如果你不想用fragment.setArguments(bundle)这样的方式，也可以利用Fragment本身的onSaveInstanceState(Bundle outState)方法来保存Activity传递过来的参数，然后在fragment onCreate方法里恢复保存的值就可以了。

5. 如果当前显示的Activity中已经存在了一个Fragment，然后再添加一个新Fragment，其中老的Fragment的生命周期方法是不会调用的


6. 如果需要把fragment加入到栈中，可以使用addToBackStack方法，如果一个Activity需要展示多个fragment，按返回键（需要弹出栈）就可以回到上一个fragment了，这样从一定程度上减少Activity的使用，也更加轻量级，启动界面也更快。
```
FragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
FragmentTransaction.add(R.id.container, fragment);
```

处理Activity的返回键

```
@Override
public void onBackPressed() {
    //如果fragment stack = 1 的时候则关闭activity， 也就是说activity中只剩下一个fragment
    if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
        finish();
    } else {
        //需要在add fragment的时候需要调用transaction.addToBackStack(name);
        getSupportFragmentManager().popBackStack();
    }
}
```


