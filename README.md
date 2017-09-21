# pxclawer

通过黛莉 MITM 实现 HTTP/HTTPS 抓包

## 使用
1. 启动程序

2. 找到客户端在局域网中的 ip

3. 客户端设置代理到 ip:8888
![trust.jpg](https://raw.githubusercontent.com/zh-h/pxclawer/docs/images/trust.jpg)

4. 访问 http://ip:8888/trust 安装证书，并且设置证书授信

5. 客户端访问任意网站，日志输出所有请求响应

## 日志信息

```bash
8:50:20,785    INFO javax.annotation.Resource:76 - Request: GET http://applehater.cn/
8:50:20,786    INFO javax.annotation.Resource:77 - {"Accept":"text/html,application/xhtml+xml,application/xml;q\u003d0.9,*/*;q\u003d0.8","Cache-Control":"no-cache","Upgrade-Insecure-Requests":"1","User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0","Connection":"keep-alive","Host":"applehater.cn","Accept-Language":"zh-CN,zh;q\u003d0.8,en-US;q\u003d0.5,en;q\u003d0.3","Accept-Encoding":"gzip, deflate","Pragma":"no-cache"}
8:50:20,807    INFO javax.annotation.Resource:76 - Request: GET http://applehater.cn/css/fonts/fontawesome/fontawesome-webfont.woff?v=4.7
8:50:20,808    INFO javax.annotation.Resource:77 - {"Accept":"application/font-woff2;q\u003d1.0,application/font-woff;q\u003d0.9,*/*;q\u003d0.8","Cache-Control":"no-cache","User-Agent":"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0","Referer":"http://applehater.cn/css/style.css","Connection":"keep-alive","Host":"applehater.cn","Accept-Language":"zh-CN,zh;q\u003d0.8,en-US;q\u003d0.5,en;q\u003d0.3","Accept-Encoding":"identity","Pragma":"no-cache"}
8:50:20,975    INFO javax.annotation.Resource:85 - Response
8:50:20,975    INFO javax.annotation.Resource:94 - Content: text/html; charset=utf-8 gzip
8:50:20,975    INFO javax.annotation.Resource:98 - {"X-Cache":"HIT","Server":"GitHub.com","Access-Control-Allow-Origin":"*","Connection":"keep-alive","Last-Modified":"Tue, 19 Sep 2017 12:37:07 GMT","Date":"Thu, 21 Sep 2017 05:51:46 GMT","Via":"1.1 varnish","X-Timer":"S1505973107.857604,VS0,VE0","Accept-Ranges":"bytes","Cache-Control":"max-age\u003d600","X-Served-By":"cache-nrt6130-NRT","Content-Encoding":"gzip","Vary":"Accept-Encoding","Expires":"Thu, 21 Sep 2017 04:27:29 GMT","Content-Length":"6821","X-Cache-Hits":"1","Age":"108","X-Fastly-Request-ID":"8a7894d4a5170b0ad3303ead46cd9089e731753e","Content-Type":"text/html; charset\u003dutf-8","X-GitHub-Request-Id":"30A8:2076C:70BC9F:7B408B:59C33D59"}
8:50:20,976    INFO javax.annotation.Resource:85 - Response
S
����
```