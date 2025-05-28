<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
<h1>This is a simple template display</h1>
<h4>Hello ${name} </h4>
<hr/>
<h3><b>display info :</b></h3>
<hr/>
<#list showData?keys as key>
    <b>${key}:</b>${showData[key]!}<br/>
</#list>
<hr/>
</body>
</html>
