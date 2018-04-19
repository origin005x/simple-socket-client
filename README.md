# simple-socket-client
项目中有一个局域网Android设备与其他硬件设备之间通信的场景，写了个小框架，这里共享出来。
一个简单的局域网socket通信框架，适用场景：一请求一个响应，顺序执行的通信场景中
大概的框架图如下：
![Alt text](https://github.com/fantao005x/simple-socket-client/raw/master/pic/框架说明.png)

```

内部封装了，初始化、发送接受数据、以及请求队列和回调的处理
发送请求只需要一句调用即可，例如：
socketApi.sendOpCmd("1", TestProtocol.ACTION_QUERY, "", callback);//TestProtocol.ACTION_QUERY需自己实现
