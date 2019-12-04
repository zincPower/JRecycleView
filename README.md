# JRecycleView——简单的让RecycleView更有趣

>目录<br/>
一、功能介绍<br/>
----1、上拉加载，下拉刷新（可自定义视图）<br/>
----2、侧滑<br/>
----3、item显示动画<br/>
----4、item粘性<br/>
----5、混合使用<br/>
----6、DIY使用<br/>
二、如何导入<br/>
三、如何使用<br/>
四、高级设置<br/>
五、作者简介<br/>

## 一、简介
封装工作中通常需要使用的列表功能(基于RecycleView)，在作者的实际项目中已使用有一年多。主要是为了让使用者更加专注于业务逻辑，同时又不失去界面交互性，达到JRecycleView的核心：**简单的让RecycleView更有趣**。

## 二、关于 AndroidX

- 主分支（即master）已经全面升级为AndroidX
- release_android_v7 分支为旧版本，为 V7 包。

> 后续新功能会在 master 叠加，release_android_v7分支只进行 bug 修复。

## 三、功能介绍：
### 1、上拉加载，下拉刷新（可自定义视图）

#### (1) 框架自带默认下拉刷新和上拉加载更多效果图

![默认视图](https://github.com/zincPower/JRecycleView/blob/master/img/pull_and_load.gif)

#### (2) 自定义下拉刷新效果图

![自定义刷新视图](https://github.com/zincPower/JRecycleView/blob/master/img/diy_pull_and_load.gif)

### 2、侧滑

侧滑效果图（可自行定义菜单）

![侧滑](https://github.com/zincPower/JRecycleView/blob/master/img/swipe.gif)

### 3、item显示动画

#### (1) 给item添加动画效果图（可自行定义，也可使用框架带的效果）

![item动画](https://github.com/zincPower/JRecycleView/blob/master/img/anim_item.gif)

### 4、item粘性

#### (1) 粘性头部效果

![粘性头部效果](https://github.com/zincPower/JRecycleView/blob/master/img/stick_head.gif)

#### (2) 粘性内容效果

![粘性内容效果](https://github.com/zincPower/JRecycleView/blob/master/img/stick_content.gif)

### 5、混合使用

![混合使用](https://github.com/zincPower/JRecycleView/blob/master/img/multi_use.gif)

### 6、DIY使用
![diy](https://github.com/zincPower/JRecycleView/blob/master/img/diy.gif)

## 二、如何导入

### 1、在项目的根gradle中加入 jitpack 仓库：
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

### 2、在library或app的gradle中添加如下代码：
```
dependencies {
    // v7 包的导入
    implementation 'com.github.zincPower:JRecycleview:1.0.0'

    // AndroidX 的导入
    implementation 'com.github.zincPower:JRecycleview:2.0.0'
}
```

## 三、如何使用

### 1、使用 **下拉刷新** 和 **上拉加载更多** 效果的两个步骤：

#### (1) 包装您的Adapter

> 此过程您的原先使用的Adapter无需任何改动，不会与业务逻辑耦合

```
// 只需将你所编写的 Adapter 替换此处的 "YourAdapter" 传入 JRefreshAndLoadMoreAdapter
JRefreshAndLoadMoreAdapter mAdapter = new JRefreshAndLoadMoreAdapter(this, YourAdapter);

// 设置下拉刷新监听
mAdapter.setOnRefreshListener(new JRefreshAndLoadMoreAdapter.OnRefreshListener() {
    @Override
    public void onRefreshing() {
        //do something for refresh data
    }
});

// 设置加载更多监听
mAdapter.setOnLoadMoreListener(new JRefreshAndLoadMoreAdapter.OnLoadMoreListener() {
    @Override
    public void onLoading() {
       //do something for load more data
    }
});

mJRecycleView.setLayoutManager(new LinearLayoutManager(this));
mJRecycleView.setAdapter(mAdapter);
```

#### (2) 布局中的 RecycleView 需使用JRecycleView

```
<com.zinc.jrecycleview.JRecycleView
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

至此便可使用 **下拉刷新** 和 **上拉加载更多** 效果😄

#### (3) 按需关闭其中一个功能，可通过如下代码

> 默认为开启 "下拉刷新" 和 "上拉加载更多" 功能

```
//关闭加载更多
this.mAdapter.setIsOpenLoadMore(false);
//关闭下拉刷新
this.mAdapter.setIsOpenRefresh(false);
```

#### (4) "下拉刷新" 状态更新

```
// 将 "下拉刷新" 状态置为刷新完成 (下拉刷新结束，即数据刷新完时，调用如下代码结束刷新)
mAdapter.setRefreshComplete(true);
```

#### (5) "上拉加载更多" 状态更新

```
// 加载完毕，但还未加载全部数据
mAdapter.setLoadComplete();

// 加载出错
mAdapter.setLoadError();

// 没有更多数据
mAdapter.setNoMore();
```

### 2、使用侧滑效果

#### (1) Adapter 中需要使用侧滑的 ViewHolder 继承 JSwipeViewHolder，按需实现以下几个方法
```
// 让你的 ViewHolder 继承 JSwipeViewHolder
class MyContentViewHolder extends JSwipeViewHolder {

    MyContentViewHolder(View itemView) {
        super(itemView);
    }

    // 传递左菜单的布局，如果不需要左菜单，则重写该方法即可
    @Override
    public int getLeftMenuLayout() {
        return R.layout.swipe_left_menu;
    }

    // 传递右菜单的布局，如果不需要右菜单，则重写该方法即可
    @Override
    public int getRightMenuLayout() {
        return R.layout.swipe_right_menu;
    }

    // 传递你的内容布局
    @Override
    public int getContentLayout() {
        return R.layout.swipe_content;
    }
    
    // 初始化左菜单视图的控件，
    @Override
    public void initLeftMenuItem(FrameLayout flLeftMenu) {
        tvLeftMenu = flLeftMenu.findViewById(R.id.tv_left_menu);
        tvLeftMenuTwo = flLeftMenu.findViewById(R.id.tv_left_menu_two);
    }


    @Override
    public void initRightMenuItem(FrameLayout flRightMenu) {
        tvRightMenu = flRightMenu.findViewById(R.id.tv_right_menu);
        tvRightMenuTwo = flRightMenu.findViewById(R.id.tv_right_menu_two);
    }

    @Override
    public void initContentItem(FrameLayout flContent) {
        tvContent = flContent.findViewById(R.id.tv_content);
    }

    /**
     * 初始化你的视图控件（包括左右菜单和内容）
     *
     * 也可以选择重写以下方法，在各自的方法中初始化各自负责的控件：
     * 1、initLeftMenuItem：初始化左菜单视图的控件
     * 2、initRightMenuItem：初始化右菜单视图的控件
     * 3、initContentItem：初始化内容视图的控件
     **/
    @Override
    public void initItem(FrameLayout frameLayout) {

    }
}
```

#### (2) 在 onCreateViewHolder 中，使用的 layout 需为 JRecycleConfig.SWIPE_LAYOUT
```
@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
        case 你自己定义的对应的type:
        	//侧滑的view，此处必须要传JRecycleConfig.SWIPE_LAYOUT
            return new MyContentViewHolder(
                    mLayoutInflater.inflate(JRecycleConfig.SWIPE_LAYOUT, parent, false)
            );

        ······ more ······
    }
}
```

#### (3) 布局中的 RecycleView 需使用 JRecycleView
```
<com.zinc.jrecycleview.JRecycleView
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

至此便可使用 **侧滑** 效果

#### (4) 将侧滑出来的菜单关闭
```
myContentViewHolder.getSwipeItemLayout().close();
```

#### (5) 禁止侧滑

有时因某些业务需求，需要禁止原本有侧滑效果的 item 能侧滑，则使用下面代码

```
myContentViewHolder.getSwipeItemLayout().setSwipeEnable(false);
```

但需要重新开启侧滑效果时，只需要重新将其设置为true，即如下

```
myContentViewHolder.getSwipeItemLayout().setSwipeEnable(true);
```

### 3、动画效果

#### (1) 将您的 Adapter 继承 **JBaseRecycleAdapter**
```
public class YourAdapter extends JBaseRecycleAdapter<RecyclerView.ViewHolder> {
	······
}
```

#### (2) 开启动画
```
// 开启动画，默认效果 AnimFactory.SLIDE_BOTTOM（从底部进入）
adapter.setOpenAnim(true);
```

#### (3) 布局中的RecycleView需使用JRecycleView
```
<com.zinc.jrecycleview.JRecycleView
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

至此便可使用 **动画效果**

#### (4) 设置动画效果
```
// 设置动画（设置动画会默认开启动画）
adapter.setAnimations(AnimFactory.getAnimSet(AnimFactory.SLIDE_BOTTOM));
```

### 4、item粘性

**任何一个item** 都能支持粘性

> tip：如果只是需要粘性功能，不需要使用"改动"或"装饰"原来的Adapter

#### (1) 让 ViewHolder 实现 IStick 接口
```
class StickHolder extends RecyclerView.ViewHolder implements IStick {
    StickHolder(View itemView) {
        super(itemView);
    }
}
```

#### (2) 布局中的RecycleView需使用JRecycleView
```
<com.zinc.jrecycleview.JRecycleView
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

至此便可使用 **item粘性效果**

## 四、高级设置

### 1、自定义 "刷新视图" 和 "加载更多视图"

#### (1) 自定义 "刷新视图"
**第一步**：继承 IBaseRefreshLoadView

**第二步**：重写以下方法
```
/**
 * 初始化 刷新 的视图
 */
protected View initView(Context context) {
    return mLoadView;
}

/**
 * 获取 刷新 的视图
 */
protected View getLoadView() {
    return mLoadView;
}

/**
 * 等待上拉 或 等待下拉的状态 视图表现
 */
@Override
protected void onPullToAction() {}

/**
 * 释放执行（释放刷新 或 释放加载更多）视图表现
 */
@Override
protected void onReleaseToAction() {}

/**
 * 执行中 视图表现
 */
@Override
protected void onExecuting() {}

/**
 * 执行完视图表现
 */
@Override
protected void onDone() {}

/**
 * 初始化视图，用于加载自己的视图
 * 拉动过程中的回调，可以更加细微的处理动画（可替换 onPullToAction 和 onReleaseToAction ）
 */
@Override
protected void onMoving(MoveInfo moveInfo) {}
```

**第三步**：通过以下代码进行设置自定义的下拉刷新效果

**全局生效**
```
// MyRefreshView 即为你定义的类
JRecycleViewManager.getInstance().setRefreshLoadView(new MyRefreshView(getBaseContext()));
```

**单页面生效**
```
// MyRefreshView 即为你定义的类
mAdapter.setRefreshLoadView(new MyRefreshView(getBaseContext()));
```

#### (2) 自定义 "上拉加载更多"
**第一步**：继承 IBaseLoadMoreView

**第二步**：重写以下方法
```
/**
 * 获取 加载更多 的视图
 */
@Override
protected View getLoadView() {
    return ....;
}

/**
 * 初始化 加载更多 的视图
 */
@Override
protected View initView(Context context) {
    return ....;
}

/**
 * 上拉加载（上拉超过视图高度前）
 */
@Override
protected void onPullToAction() {}

/**
 * 释放刷新（上拉超过视图高度后）
 */
@Override
protected void onReleaseToAction() {}

/**
 * 执行中
 */
@Override
protected void onExecuting() {}

/**
 * 执行完
 */
@Override
protected void onDone() {}

/**
 * 加载出错
 */
@Override
protected void onError() {}

/**
 * 没有更多数据
 */
@Override
protected void onNoMore() {}
  
```

**第三步**：通过以下代码进行设置自定义的上拉加载更多效果

**全局生效**
> 可在任何地方设置，包括Application，但是只有运行该代码后才有效果，否则运行该代码前使用的还是默认视图
```
JRecycleViewManager.getInstance().setLoadMoreView(LoadMoreView);
```

**单页面生效**
```
mAdapter.setLoadMoreView(LoadMoreView);
```

### 2、自定义动画

#### (1) 编写动画
**第一步**：继承 IBaseAnimation

**第二步**：重写下以下方法，编写自己的动画逻辑
```
@Override
protected void init(View view) {}
```

**第三步**：通过成员对象 mAnimSet 进行设置动画

#### (2) 设置动画

**全局设置**

**第一步**：使用 IBaseAnimation[]{} 将上面的类包装

> 可以设置多个，item会按照顺序使用动画
```
// SlideInTopAnimation 为你编写的类
IBaseAnimation[] set = new IBaseAnimation[]{new SlideInTopAnimation()};
```
**第二步**：使用动画

```
JRecycleViewManager.getInstance().setItemAnimations(set);
```

**单页面设置**
```
// SlideInTopAnimation 为你编写的类
mAdapter.setAnimations(new SlideInTopAnimation());
```

## 五、作者简介
### 1、个人博客
掘金：[https://juejin.im/user/5c3033ef51882524ec3a88ba/posts](https://juejin.im/user/5c3033ef51882524ec3a88ba/posts)

csdn：[https://blog.csdn.net/weixin_37625173](https://blog.csdn.net/weixin_37625173)

github：[https://github.com/zincPower](https://github.com/zincPower)

### 2、赞赏

如果觉得该框架对您有所帮助，那就赞赏一下吧😄

![](https://github.com/zincPower/JRecycleView/blob/master/img/zincPay.jpg)
