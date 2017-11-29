# simple-socket-client
一个简单的socket通信框架，适用场景：一请求一个响应，顺序执行的通信场景中
```

内部封装了，初始化、发送接受数据、以及请求队列和回调的处理
发送请求只需要一句调用即可，例如：
socketApi.sendOpCmd("1", TestProtocol.ACTION_QUERY, "", callback);//TestProtocol.ACTION_QUERY需自己实现
