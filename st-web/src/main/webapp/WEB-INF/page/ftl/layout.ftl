<#macro head>
    <#assign layout_head>
        <#nested />
    </#assign>
</#macro>
<#macro script>
    <#assign layout_script>
        <#nested />
    </#assign>
</#macro>
<#macro body>
    <#assign base=request.contextPath />
<!DOCTYPE html>
<html lang="zh_cn">
<head>
    <base id="base" href="${base}">
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="${base}/resources/bootstrap/icon/favicon.ico">
    <link href="${base}/resources/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="${base}/resources/css/common.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="${base}/resources/bootstrap/js/html5shiv.min.js"></script>
    <script src="${base}/resources/bootstrap/js/respond.min.js"></script>
    <![endif]-->

${layout_head}
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
        <#--<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>-->
            <a class="navbar-brand" href="#">后台管理</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
        <#--<ul class="nav navbar-nav navbar-right">
                <li><a href="#">Dashboard</a></li>
                <li><a href="#">Settings</a></li>
                <li><a href="#">Profile</a></li>
                <li><a href="#">Help</a></li>
            </ul>
            <form class="navbar-form navbar-right">
                <input type="text" class="form-control" placeholder="Search...">
            </form>-->
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <ul class="nav nav-sidebar">
                <li id="menu-xxx1"><a href="#">页面1</a></li>
                <li id="menu-xxx2"><a href="#">页面2</a></li>
                <li id="menu-xxx3"><a href="#">页面3</a></li>
                <li id="menu-xxx4"><a href="#">页面4</a></li>
            </ul>
            <ul class="nav nav-sidebar">
                <li id="menu-xxx5"><a href="">页面5</a></li>
                <li id="menu-xxx6"><a href="">页面6</a></li>
                <li id="menu-xxx7"><a href="">页面7</a></li>
                <li id="menu-xxx8"><a href="">页面8</a></li>
                <li id="menu-xxx9"><a href="">页面9</a></li>
            </ul>
            <ul class="nav nav-sidebar">
                <li id="menu-xxx10"><a href="">页面10</a></li>
                <li id="menu-xxx11"><a href="">页面11</a></li>
                <li id="menu-xxx12"><a href="">页面12</a></li>
            </ul>
        </div>
        <#nested />
    </div>
</div>
<script type="text/javascript">
    window.jQuery || document.write("<script src='${base}/resources/jquery/js/jquery-1.11.1.js'>" + "<" + "/script>");
</script>
<script src="${base}/resources/bootstrap/js/bootstrap.js"></script>
<script src="${base}/resources/js/common.js"></script>
<script src="${base}/resources/js/pager.js"></script>
${layout_script}
</body>
</html>
</#macro>

