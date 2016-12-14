<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>模板样例</title>
</head>
<body>
Hello, ${name} <br>
金额: ${amount?string("#0.00")} | ${amount} | ${amount?string(",##0.0#")}<br>
时间: ${myDate?date} | ${myDate?time} | ${myDate?datetime}
金额格式: ${amount1} | ${amount2} | ${amount3} | ${amount4} |
</body>
</html>