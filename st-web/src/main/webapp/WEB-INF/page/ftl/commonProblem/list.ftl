<#include "/layout.ftl">
<#--<#include "/WEB-INF/page/ftl/layout.ftl">-->
<#-- head写在这里 -->
<@head>
<title>模板页面</title>
</@head>
<#-- body里的script写在这里 -->
<@script>
<script type="text/javascript">
    //menu页选中
    $('#menu-xxx1').toggleClass('active', true);


    var listParamMapping = {
        "@@primary@@id": "编号",
        "@!date!@createTime": "创建时间",
        "createOperator": "创建人",
        "problemTitle": "问题",
        "problemContent": "内容",
        "@!enum@#1:首页问题;2:详情页问题!@problemType": "问题类型",
        "@!enum@#1:A型;2:O型;3:C型!@problemClass": "问题类别"
    };


    function loadData(pageNum, pageSize) {
        var loadedData = null;
        var condition = $('#condition_form').serializeObject();
        condition.pageNum = pageNum;
        condition.pageSize = pageSize;
        console.log(condition);

        $.ajax({
            type: "POST",
            url: "/template/listLoad",
            data: condition,
            dataType: "json",
            async: false,
            success: function (data) {
                data.targetTable = 'pageTable';
                data.targetDiv = 'pagerBar';
                //data.targetPageSizeDiv = 'pageSizeDiv';
                loadedData = data;
            },
            error: function (data) {
                console.log(data);

                alert(data);
                return null;
            }
        });
        return loadedData;

    }

    _loadPage(1, 10, listParamMapping, loadData);

</script>
</@script>
<#-- body内容写在这里 -->
<@body>
<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
    <h1 class="page-header">页面Test</h1>

    <div class="row placeholders">
        <form class="form-horizontal" id="condition_form">
            <div class="form-group">
                <label for="problemType" class="col-sm-1 control-label">编号</label>
                <div class="col-sm-3">
                    <input type="text" class="form-control" id="problemType" name="problemType" placeholder="问题类型" value="2">
                </div>
                <label for="exampleInputPassword1" class="col-sm-1  control-label">Password</label>
                <div class="col-sm-3">
                    <input type="text" class="form-control" id="exampleInputPassword1" placeholder="Password">
                </div>
                <label for="exampleInputFile" class="col-sm-1  control-label">File input</label>
                <div class="col-sm-3">
                    <input type="file" class="form-control" id="exampleInputFile">
                    <p class="help-block">Example block-level help text here.</p>
                </div>
            </div>

        <#--<label for="exampleInputEmail1" class="col-sm-1 col-xs-6">Email address</label>
        <div class="form-group col-xs-6 col-sm-3 placeholder">
            <input type="email" class="form-control" id="exampleInputEmail1" placeholder="Enter email">
        </div>
        <label for="exampleInputPassword1" class="col-sm-1 col-xs-6">Password</label>
        <div class="form-group col-xs-6 col-sm-3 placeholder">
            <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password">
        </div>
        <label for="exampleInputFile" class="col-sm-1 col-xs-6">File input</label>
        <div class="form-group col-xs-6 col-sm-3 placeholder">
            <input type="file" id="exampleInputFile">
            <p class="help-block">Example block-level help text here.</p>
        </div>-->

        </form>
    <#--<div class="col-xs-6 col-sm-3 placeholder">
        <img data-src="holder.js/200x200/auto/sky" class="img-responsive">
        <h4>Label</h4>
        <span class="text-muted">Something else</span>
    </div>
    <div class="col-xs-6 col-sm-3 placeholder">
        <img data-src="holder.js/200x200/auto/vine" class="img-responsive">
        <h4>Label</h4>
        <span class="text-muted">Something else</span>
    </div>
    <div class="col-xs-6 col-sm-3 placeholder">
        <img data-src="holder.js/200x200/auto/sky" class="img-responsive">
        <h4>Label</h4>
        <span class="text-muted">Something else</span>
    </div>
    <div class="col-xs-6 col-sm-3 placeholder">
        <img data-src="holder.js/200x200/auto/vine" class="img-responsive" alt="Generic placeholder thumbnail">
        <h4>Label</h4>
        <span class="text-muted">Something else</span>
    </div>-->
    </div>

    <h2 class="sub-header">查询结果</h2>
    <div id="pageTable" class="table-responsive">

    </div>
    <div id="pagerBar">

    </div>
</div>
</@body>

