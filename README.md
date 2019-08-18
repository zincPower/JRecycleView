# JRecycleView
封装RecycleView中常用的一些控件

>目录
1、目前已实现功能和效果图
2、如何导入
3、如何使用
4、高级设置

## 目前已实现功能：
#### 1、上拉加载，下拉刷新（可自定义视图）

框架自带默认下拉刷新和上拉加载更多效果图

![默认视图](https://github.com/zincPower/JRecycleView/blob/master/pull_and_load.gif)

定义下拉刷新效果图

![自定义刷新视图](https://github.com/zincPower/JRecycleView/blob/master/diy_pull.gif)

#### 2、侧滑

侧滑效果图（可自行设置需要做有菜单）

![侧滑](https://github.com/zincPower/JRecycleView/blob/master/swipe_multi.gif)

#### 3、item显示动画

给item添加动画效果图（可自行定义，也可使用框架带的效果）

![item动画](https://github.com/zincPower/JRecycleView/blob/master/anim_item.gif)

## 如何导入
#### 1、在项目的根gradle中加入如下代码：
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
#### 2、在library或app的gradle中添加如下代码：
```
dependencies {
    compile 'com.github.zincPower:JRecycleview:0.1.2'
}
```

## 如何使用
#### 1、使用**下拉刷**和**上拉加载更多**效果的两个步骤：

（1）包装您的adapter，此过程您的adapter中的逻辑无需任何改动
```
//只需将你所编写的adapter传入JRefreshAndLoadMoreAdapter
JRefreshAndLoadMoreAdapter mAdapter = new JRefreshAndLoadMoreAdapter(this, yourAdapter);

//设置下拉刷新监听
mAdapter.setOnRefreshListener(new JRefreshAndLoadMoreAdapter.OnRefreshListener() {
    @Override
    public void onRefreshing() {
        //do something for refresh data
    }
});

//设置加载更多监听
mAdapter.setOnLoadMoreListener(new JRefreshAndLoadMoreAdapter.OnLoadMoreListener() {
    @Override
    public void onLoading() {
       //do something for load more data
    }
});

mJRecycleView.setLayoutManager(new LinearLayoutManager(this));
mJRecycleView.setAdapter(mAdapter);
```
(2)布局中的RecycleView需使用JRecycleView
```
<com.zinc.jrecycleview.JRecycleView
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</com.zinc.jrecycleview.JRecycleView>
```
如此便可使用**下拉刷新**和**上拉加载更多**效果，如果需要关闭其中一个功能，可通过如下代码
```
//关闭加载更多
this.mAdapter.setIsOpenLoadMore(false);
//关闭下拉刷新
this.mAdapter.setIsOpenRefresh(false);
```

(3)更多操作
下拉刷新结束，即数据刷新完时，调用如下代码结束刷新
```
//如果有上拉加载更多，调用此句重置上拉加载状态
mAdapter.resetLoadMore();

mAdapter.setRefreshComplete(true);
```

上拉加载更多完成后的操作
```
//加载完毕，但还未加载全部数据
mAdapter.setLoadComplete();

//加载出错
mAdapter.setLoadError();

//没有更多数据
mAdapter.setNoMore();
```


#### 2、使用侧滑效果

（1）adapter中需要使用侧滑的ViewHolder继承JSwipeViewHolder，需要实现以下几个方法
```
class MyContentViewHolder extends JSwipeViewHolder {

    public MyContentViewHolder(View itemView) {
        super(itemView);
    }

    //传递左菜单的布局，如果该ViewHolder不需要左菜单则传递 NONE(该值在父类中已定义) 
    @Override
    public int getLeftMenuLayout() {
        return R.layout.swipe_left_menu;
    }

	  //传递右菜单的布局，如果该ViewHolder不需要右菜单则传递 NONE(该值在父类中已定义) 
    @Override
    public int getRightMenuLayout() {
        return R.layout.swipe_right_menu;
    }

	  //传递内容视图
    @Override
    public int getContentLayout() {
        return R.layout.swipe_content;
    }

    //初始化左菜单视图的控件
    @Override
    public void initLeftMenuItem(FrameLayout flLeftMenu) {
    }

	  //初始化右菜单视图的控件
    @Override
    public void initRightMenuItem(FrameLayout flRightMenu) {
        
    }

	  //初始化内容视图的控件
    @Override
    public void initContentMenuItem(FrameLayout flContent) {
        
    }
}
```
（2）在onCreateViewHolder中，使用的layout需为JRecycleConfig.SWIPE_LAYOUT
```
@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
        case 你自己定义的对应的type:
        	//侧滑的view，此处必须要传JRecycleConfig.SWIPE_LAYOUT
            return new MyContentViewHolder(mLayoutInflater.inflate(JRecycleConfig.SWIPE_LAYOUT, parent, false));

        ······ more ······
    }
}
```
(3)布局中的RecycleView需使用JRecycleView
```
<com.zinc.jrecycleview.JRecycleView
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</com.zinc.jrecycleview.JRecycleView>
```
如此便可使用**侧滑**效果

#### 3、动画效果

(1)将您的 adapter 继承 **JBaseRecycleAdapter**
```
public class YourAdapter extends JBaseRecycleAdapter<RecyclerView.ViewHolder> {
	······
}
```
(2)开启动画
```
//设置动画（设置动画会默认开启动画）
adapter.setAnimations(AnimFactory.getAnimSet(AnimFactory.SLIDE_BOTTOM));
//开启动画（如果不设置动画，便使用默认效果 AnimFactory.SLIDE_BOTTOM ）
adapter.setOpenAnim(true);

```
(3)布局中的RecycleView需使用JRecycleView
```
<com.zinc.jrecycleview.JRecycleView
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</com.zinc.jrecycleview.JRecycleView>
```
如此便可使用**动画效果**

## 高级设置
#### 1、自定义 刷新视图 和 加载更多 视图
（1）继承 IBaseRefreshLoadView（下拉刷新）或 IBaseLoadMoreView（上拉加载）类

IBaseRefreshLoadView（下拉刷新）需重写以下方法
```
//获取 刷新 的视图
View getLoadView();

//初始化 刷新 的视图
void initView();

//下拉刷新（下拉超过视图高度前）
void onPullToAction();

//释放刷新（下拉超过视图高度后）
void onReleaseToAction();

//执行刷新
void onExecuting();

//执行完
void onDone();

//这个方法是下拉过程中（手指未释放）回调，
//是为了更加细致的控制视图中的动画效果（可替换onPullToAction和onReleaseToAction）
void onMoving(MoveInfo moveInfo);
```

IBaseLoadMoreView（上拉加载）需重写以下方法
```
//获取 加载更多 的视图
View getLoadView();

//初始化 加载更多 的视图
void initView();

//上拉加载（上拉超过视图高度前）
void onPullToAction();

//上拉刷新（上拉超过视图高度后）
void onReleaseToAction();

//执行中
void onExecuting();

//执行完
void onDone();

//这个方法是下拉过程中（手指未释放）回调，
//是为了更加细致的控制视图中的动画效果（可替换onPullToAction和onReleaseToAction）
void onMoving(MoveInfo moveInfo);

//加载出错
void onError();

//没有更多数据
void onNoMore();
```

(2)将自定义的视图设置（全局使用和部分视图）
全局设置，可在任何地方设置，包括Application，但是只有运行该代码后才有效果，否则运行该代码前使用的还是默认视图
```
//下拉刷新
JRecycleViewManager.getInstance().setBaseRefreshLoadView(new MyRefreshView(getBaseContext()));
//上拉加载
JRecycleViewManager.getInstance().setBaseLoadMoreView(LoadMoreView);
```
针对某个View做自定义视图
```
//下拉刷新
this.mAdapter.setRefreshLoadView(new MyRefreshView(this));
//上拉加载
this.mAdapter.setLoadMoreView(LoadMoreView);
```

#### 2、自定义动画
(1)继承IBaseAnimation，重写init(View view)方法，编写自己的动画逻辑
```
public class SlideInTopAnimation extends IBaseAnimation {
    @Override
    protected void init(View view) {
        //定义你的动画，使用addAnimTogether方法，将动画添加进动画组
    }
}
```
(2)使用IBaseAnimation[]{}将第一步的类包装，可以设置多个，item会按照顺序使用动画
```
IBaseAnimation[] set = new IBaseAnimation[]{new SlideInTopAnimation()};
```
(3)使用动画
```
//执行了这句后，全部的默认动画遍使用了该动画
JRecycleViewManager.getInstance().setItemAnimations(YOUR ANIM);

//针对某个视图使用特定动画
adapter.setAnimations(AnimFactory.getAnimSet(AnimFactory.SLIDE_BOTTOM));
```









