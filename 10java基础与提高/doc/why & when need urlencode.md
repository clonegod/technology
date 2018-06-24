# why urlencode?
    https://en.wikipedia.org/wiki/Percent-encoding
    https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
    https://stackoverflow.com/questions/18381770/does-ajax-post-data-need-to-be-uri-encoded
    
    

# get need urlencode? 
    urlencode 是为了将请求参数中的特殊进行转义。
    = & 在作为请求参数的分隔符，服务器端会根据这两个符号来解析客户端请求的参数。
    如果参数值中包含这两个字符，则会导致参数解析错误。
    因此，解决办法就是，将参数值进行urlencode，确保参数值中的特殊字符被转义。
    
    
# post need urlencde?
    It all depends on the content type.
    
    传统的POST方式提交表单，参数需要urlencode(当Content-Type: application/x-www-form-urlencoded时，浏览器/http框架会自动对参数进行urlencode处理)
    Normally when a <form> uses the HTTP method POST then the form values are URL Encoded and placed in the body of the reqeust. The content type header looks like this:
        Content-Type: application/x-www-form-urlencoded
    
    Ajax提交JSON数据，不需要urlencode，因为Content-Type不是表单类型，服务器端也就不需要进行参数的提取，而是用户自己处理（spring将json数据自动绑定到POJO上 @RequestBody）
    Most AJAX libraries will do this by default since it is universally accepted among web servers. 
    However, there is nothing preventing you from simply seralizing the data as JSON or XML and then sending it with a different content type.
        Content-Type: application/json

    客户端可以提交各种类型的数据，关键要正确设置所提交数据Content-Type。否则服务器端无法正确解析所提交的数据!
    It doesn't matter if you send Plain Text, JSON, XML, or even raw binary data so long as you tell the server how to interpret those bits.
    
    在AJAX出现之前，url编码完全是<form>表单将参数正确提交到服务器的历史方案。
    The url encoding is purely a historical artifact of how <form> elements posted their data to the server before AJAX was around.
    
    
# base64 need urlencde?
    base64编码后的字符串中可能会包含=，+
    由于这两个字符在参数值传递的时候具有特殊用途，因此必须对base64字符串进行urlencode后，再提交给服务器
    
    如果base64字符串以传统的get/post方式提交
        = 会被解析为名值对的分隔符
        + 会被解析为空格字符
    
    

    
    
    
    