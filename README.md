# simpleTable
一种管理后台简单表格展示组件


## 一. 概述
该插件的主要目的是将前页列表页的显示代码大大简化，只需要配置和写一个加载page的ajax方法。仅需要配置即可生成列表页，但同时，样式就不可修改，想要自定义表格样式是不支持的。

本插件基于Bootstrap和jquery，必须要引入相关的库。

主要接入步骤如下:

    通用js部分,将js代码加入到公用js文件中。
    
    后台必须以ajax形式返回我们指定的Page对象。
    
    列表页在指定位置建立两个div，一个用来显示表格，另一个用来显示分页条。
    
    做表列名与对象属性映射的配置。
    
    前台js写一个加载数据的方法，要求以页码和每页条数为参数，返回page对象。
    
    调用_loadPage(页码, 每页条数, 映射配置对象, 加载数据方法)，大功告成。
    
后面会按照步骤，依次说明。

## 二.样式
本表格支持的样式有限，如果要展示比较复杂的表格，建议使用其他插件。

表格采用纯bootstrap风格，支持行单选。
    
分页条，该部分样式是写死的，如果要修改样式，必须修改Page类的代码和前端的js代码。 该分页条首页和最后一页一定是显示的，因此有首页，尾页，上一页和下一页的功能。

![pagerBar](https://github.com/myyay/simpleTable/blob/master/resources/img/pagerBar.png)

## 三. 通用js代码部分
接入第一步，将以下代码加入到公用js代码中。
代码在附件中:common.js 。
 
使用说明:

_loadPage(页码, 每页条数, 映射配置对象,加载数据方法)方法用来加载页面。

_getSelPrimaryKey()方法用于获取先中行的主键（后面配置部分会说明什么是主键）。
_getCheckedPrimaryArray(配置的class)方法用于获取checkbox选中的行的主键列表。

 
注意事项:
其中增加的以“_”开头的方法，注意在自己代码中不要用到。
定义了公共变量_selectedKey，注意不要使用到。

 
## 四.Page类
接入第二步，加载数据的部分必须返回Page对象。

Page对象如下: Page.java

Spring MVC的方法:
  
![Spring MVC写法](https://github.com/myyay/simpleTable/tree/master/resources/img/SpringMVC.png)


## 五.配置说明
接入第三步，略。

接入第四步，配置部分。

该部分是插件的核心部分。配置其实就是要我们定义一个对象，这个对象做一个列表对象与表列名的映射关系。

比如:我们返回的对象为User:
<pre><code>class User {
          String name;
          Integer age;
          set()...
          get()...
    }
</code></pre>
js 对象:
<pre><code>
    var mapping = {
        "name" : "姓名",
        "age" : "年龄" 
    }
</code></pre>
这样一个简单的配置就完成了。
 
但是仅仅这样肯定是不够的，能用配置解决的就不要写代码。

先定义几个标签：

### 属性标签

以@@······@@包围起来是属性标签，属性标签标识该数据的意义，要求写在最前面。

| 标签 | 含义  |
| ----------- | -------------- |
| @@primary@@ |	主键，表示该字段为本表的主键，后续一些操作的方法会以该标签标识的数据作为参数传入。一般标识一列数据，也可以标识多列，标识多列时会返回一个数组。不能和@@dataArray@@同时使用。|
| @@dataArray@@ |数据列，在回调方法中会将此列以方法参数传入。可标识多个字段，从上到下从0开始。不能和@@primary@@同时使用。|
| @@hide@@ | 表示该列隐藏不显示。用这个标签和上面两个标签同时使用可以仅提供数据，不显示内容。|

注意：使用主键的值必须先定义，后面才能用。如果遇到第一列为checkbox的场景，可以先定义一列主键隐藏掉，后面checkbox列就可以用了。

### 类型标签

以@!······!@包围起来的是类型标签，用于标识该列的数据类型。

| 标签 | 含义 |
| ----------- | -------------- |
| @!date!@	|标识该列数据返回的是java.util.Date类型，会转换显示样式为:yyyy-MM-dd HH:mm:ss。注意使用该标签必须定义附录1的方法。|
| @!day!@	|标识该列数据返回的是java.util.Date类型，会转换显示样式为:yyyy-MM-dd。注意使用该标签必须定义附录1的方法。|
| @!enum!@	|枚举类型，数据返回的是String或Integer类型。根据样式进行显示内容的转换。 例如: @!enum@#1:PC;2:IOS;3:安卓!@orderSource @#后面跟的是枚举内容，比如如果返回值为1，则显示PC；如果返回值 为3，则显示安卓。|
| @!optCheckbox!@ |<p>	表示该列为checkbox列，后面不再跟对象名，而是跟指定的样式类型，该样式仅供获取check列表时使用，可以不定义真实样式。 使用方法例如:</p><p>"@!optCheckbox!@choose" : {</p> <p>//<-----choose为css样式，可以不指定，用于选择数据时使用</p><p>"columnName" : "选择", //<-----此行没用，忽略</p><p>"getClassFunc" : getCheckClass</p> <p>//<-----渲染方法，此方法用于获取该checkbox的样式，需要返回一个class。方法参数：第一个参数为主键或主键列表(多个主键时)，第二个参数为dataArray数组。</p><p>//"clickFuncName" : "cancelOrder" //<-----无用</p><p>}</p>|
| @!optBtn!@|<p>表示该列为操作按钮。后面不再跟对象名，而是跟按钮的名称。例如：<p>"@!optBtn!@取消订单" : { //<-----取消订单为按钮名称</p<p>"columnName" : "操作", //<-----操作为该列的名称 表格头</p><p>"getClassFunc" : getCancelClass,</p> <p>//<-----渲染方法（同上），需要返回一个class样式，参数同上。比如想要主键为xxxx时隐藏该按钮，就可判断第一个参数是否为xxx，如果是则返回"hidden"（bootstrap隐藏class），否则返回""。</p>
<p>"clickFuncName" : "cancelOrder"</p> //<-----触发方法名称，参数和渲染一样。注意这里传的是字符串方法名。你需要定义同名方法，来执行点击事件。</p><p>}</p>|
 	 
参数名还支持a.b的方式获取对象a属性b。支持a[0]的方式获取a列表的第一个值。这两种方式可以依次类推。
比如:    var obj = {a:[[{c:[{e:[{f:{g:'tt'}}]}]}]]}; 可以通过配置a[0][0].c[0].e[0].f.g的方式获取，得到的值为"tt"。
举个配置的例子:
  
![listMapping](https://github.com/myyay/simpleTable/tree/master/resources/img/listMapping.png)
  
![方法说明](https://github.com/myyay/simpleTable/tree/master/resources/img/方法说明.png)
       
## 六.加载数据方法
接入第五步，加载数据方法。
该方法即将返回的Page对象转换为js对象即可。
在成功获取数据后，需要设置两个数据的值。
targetTable和targetDiv，将这两个值设置到page对象中。分别为展示table的div的id属性和展示分页条div的id属性。
如下:
  
![loadData方法.png](https://github.com/myyay/simpleTable/tree/master/resources/img/loadData方法.png)

## 七.可使用的方法
接入第六步，调用加载方法。
第一个就是加载列表的方法，在你的搜索或查询按钮的事件中调用
_loadPage(1, 10, listParamMapping, loadData);   // <-------加载第1页，每页10条，配置对象，加载page的方法
就可以啦。
_getSelPrimaryKey()  和 _getCheckedPrimaryArray(checkBoxClass)方法见使用说明部分。


## 附录1: javascript Date对象扩展方法
 
//日期格式化
<pre><code>
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(), //day
        "h+": this.getHours(), //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
        "S": this.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}
</code></pre>

## 附录2: javascript form转json方法
 
<pre><code>
$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if ($.trim(this.value) == '') {
            return;
        }
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value);
        } else {
            o[this.name] = this.value;
        }
    });
    return o;
}
</pre></code>
 ## 附录3: common.js中的内容
 <pre><code>
//=================================== 分页列表功能 ===================================
// pagerBar显示
// pager 对象
function _pagerBar(pager) {
    var nav = $('<nav style="text-align: center"></nav>');
 var bar = $('<ul class="pagination"></ul>');
 $(pager.pageNumList).each(function (index, elem) {
        var li = $('<li></li>');

 if (elem.disabled) {
            if (elem.current) {
                li.addClass('active');
 } else {
                li.addClass('disabled');
 }
        }
        li.append('<a href="javascript:void(0);">' + elem.name + '</a>');
 li.children('a').click(function () {
            var me = $(this).parent();
 if (me.hasClass('active') || me.hasClass('disabled')) {
                return;
 }
            _loadPage(elem.num, pager.pageSize, pager.mapping, pager.loadFunc);
 });
 bar.append(li);
 });
 nav.append(bar);
 return nav;
}

//渲染分页bar
function _renderPagerBar(pager) {
    var t = $('#' + pager.targetDiv);
 t.empty();
 t.append(_pagerBar(pager));

}
//选中行的主键 如果有多个主键返回为Array ,否则返回主键串
var _selectedKey = null;
//var _selectedTr = null;
function _checkboxCheckAll(checkbox, cls) {
    var selectAll = $(checkbox);
 if (selectAll.is(':checked')) {
        $('.' + cls).prop('checked', true);
 } else {
        $('.' + cls).prop('checked', false);
 }
}

//渲染table
function _renderTable(pager) {
    var d = $('#' + pager.targetTable);

 d.empty();
 var t = $('<table class="table table-striped table-bordered table-hover table-condensed table-responsive"></table>');
 var head = $('<thead></thead>');
 var headTr = $('<tr></tr>');
 for (var attr in pager.mapping) {
        if (attr.indexOf('@@hide@@') == -1) {
            if (typeof pager.mapping[attr] == "string") {
                headTr.append($('<th>' + pager.mapping[attr] + '</th>'));
 }
            if (typeof pager.mapping[attr] == "object") {
                if (attr.indexOf('@!optCheckbox!@') != -1) {
                    headTr.append($('<th><label><input type="checkbox" onclick="_checkboxCheckAll(this, \'' + _getRealFieldName(attr) + '\')"/>全选</label></th>'));
 } else {
                    headTr.append($('<th>' + pager.mapping[attr].columnName + '</th>'));
 }
            }
        }
    }
    head.append(headTr);
 t.append(head);

 var body = $('<tbody></tbody>');

 $(pager.data).each(function (index, elem) {
        var btr = $('<tr></tr>');
 for (var atr in pager.mapping) {
            if (atr.indexOf('@@hide@@') == -1) {
                btr.append($('<td>' + _formatTdContent(atr, _getFieldValue(_getRealFieldName(atr), elem), pager.mapping[atr], btr.primaryKey, btr.dataArray) + '</td>'));
 }
            atr = atr.replace('@@hide@@','');
 if (atr.startsWith('@@primary@@')) {
                if (btr.primaryKey == undefined || btr.primaryKey == null) {
                    btr.primaryKey = [];
 }
                btr.primaryKey.push(_getFieldValue(_getRealFieldName(atr), elem));
 }
            if (atr.startsWith('@@dataArray@@')) {
                if (btr.dataArray == undefined || btr.dataArray == null) {
                    btr.dataArray = [];
 }
                btr.dataArray.push(_getFieldValue(_getRealFieldName(atr), elem));
 }
        }
        btr.click(function () {
            //单行选择
 if (btr.hasClass('info')) {
                btr.removeClass('info');
 _selectedKey = null;
 }
            else {
                btr.siblings().removeClass('info');
 $(this).addClass('info');
 if (btr.primaryKey != undefined && btr.primaryKey != null) {
                    if (btr.primaryKey.length == 1) {
                        _selectedKey = btr.primaryKey[0];
 } else {
                        _selectedKey = btr.primaryKey;
 }
                }
            }
        });
 body.append(btr);
 });
 t.append(body);

 d.append(t);

}

function _replaceTab(fieldName) {
    if (null == fieldName) return null;
 return fieldName.replace('@@primary@@', '').replace('@@dataArray@@', '').replace('@@hide@@','');
}


//filedName定义 @!~!@中间为命令，后面为真实的域名称 例如:@!date!@createTime,date为命令，createTime为真实域名称
//获取真实域名称
function _getRealFieldName(fieldName) {
    var fName = _replaceTab(fieldName);
 if (fName.startsWith('@!')) {
        return fName.substring(fName.indexOf('!@') + 2, fName.length);
 }
    return fName;
}
//获取域命令
function _getFieldNameCmd(fieldName) {
    var fName = _replaceTab(fieldName);
 if (fName.startsWith('@!')) {
        return fName.substring(fName.indexOf('@!') + 2, fName.indexOf('!@'));
 }
    return null;
}

//根据命令执行相应的格式化操作
function _formatTdContent(fieldName, fieldValue, mappingObj, primaryKey, dataArray) {
    //opt操作可以允许value为空
 if (!fieldName.startsWith('@!opt')) {
        if (null == fieldValue || undefined == fieldValue) {
            return '--';
 }
    }

    if ('null' == fieldValue) {
        return fieldValue;
 }

    var fName = _replaceTab(fieldName);

 if (fName.startsWith('@!')) {
        var cmd = _getFieldNameCmd(fName);
 //date命令格式化
 if (cmd == 'date') {
            return new Date(fieldValue).format('yyyy-MM-dd hh:mm:ss');
 }
        //day命令格式化
 if (cmd == 'day') {
            return new Date(fieldValue).format('yyyy-MM-dd');
 }

        //enum命令格式化 要求枚举中不能出现@#字符 enum命令格式: @!enum@#1:自动到账;2:红利再投;3:现金分红!@redemptionType
 if (cmd.startsWith('enum')) {
            var arr = cmd.split('@#');
 if (arr.length < 2) return fieldValue;
 var enums = arr[1].split(';');
 var retValue = fieldValue;
 enums.forEach(function (e) {
                if ($.trim(e) == '') {
                    return;
 }
                var nameVal = e.split(':');
 if (fieldValue == $.trim(nameVal[0])) {
                    retValue = $.trim(nameVal[1]);
 }
            });
 return retValue;
 }
        //如果是操作按钮
 if (cmd.startsWith('optBtn')) {
            var optBtnClasses = "";
 if (undefined != mappingObj.getClassFunc && null != mappingObj.getClassFunc) {
                optBtnClasses = " " + mappingObj.getClassFunc(primaryKey, dataArray);
 }
            var optBtnClickFunc = "";
 if (undefined != mappingObj.clickFuncName && null != mappingObj.clickFuncName) {
                optBtnClickFunc = mappingObj.clickFuncName + '(' + primaryKey + ', ' + dataArray + ');';
 }
            return '<button type="button" class="btn btn-primary btn-sm btn-block ' + optBtnClasses + '" onclick="' + optBtnClickFunc + '">' + _getRealFieldName(fName) + '</button>';
 }

        //如果是checkbox列
 if (cmd.startsWith('optCheckbox')) {
            var optCheckboxClasses = "";
 if (undefined != mappingObj.getClassFunc && null != mappingObj.getClassFunc) {
                optCheckboxClasses = " " + mappingObj.getClassFunc(primaryKey, dataArray);
 }
            var optCheckboxClickFunc = "";
 if (undefined != mappingObj.clickFuncName && null != mappingObj.clickFuncName) {
                optCheckboxClickFunc = 'onchange="' + mappingObj.clickFuncName + '(' + primaryKey + ', ' + dataArray + ');' + '" ';
 }
            return '<label><input type="checkbox" class="' + _getRealFieldName(fName) + ' ' + optCheckboxClasses + '"' + optCheckboxClickFunc + '" value="' + primaryKey + '"/>' + '</label>';
 }


    }
    return fieldValue;
}


//用于获取value的值,支持.和[]的方式获取
function _getFieldValue(fieldName, elem) {
    if (elem == null || elem == undefined) {
        return null;
 }
    var array = fieldName.split('.');
 return _dotValue(array, elem, array.length);
}


function _dotValue(array, obj, len) {
    if (len <= 0) return _splitList(null, obj);
 return _dotValue(array, _splitList(array[array.length - len], obj), len - 1);
}
function _listValue(array, obj, len) {
    if (len <= 0) return obj;
 return _listValue(array, obj[+(array[array.length - len].replace(']', ''))], len - 1);
}
function _splitList(fieldName, elem) {
    if (null == fieldName || null == elem || undefined == elem) {
        return elem;
 }
    var array = fieldName.split('[');
 return _listValue(array, elem[array[0]], array.length - 1);
}


function _loadPage(pageNum, pageSize, mapping, loadFunc) {

    var pager = loadFunc(pageNum, pageSize);
 pager.loadFunc = loadFunc;
 pager.mapping = mapping;
 _renderTable(pager);
 _renderPagerBar(pager);

}

//获取选中行的主键
function _getSelPrimaryKey() {
    return _selectedKey;
}

//获取checked行的主键列表
function _getCheckedPrimaryArray(checkBoxClass) {
    var array = [];
 //console.log('.' + checkBoxClass + ":checked");
 $('.' + checkBoxClass + ":checked").each(function (index, elem) {
        array.push(elem.value);
 });
 return array;
}
</code></pre>

//=================================== 分页列表功能 ↑ ===================================

## 附录4: Page类
<pre><code>
package com.jd.scrt.common.utils.web;

import java.util.ArrayList;
import java.util.List;

/**
 * ---------------------------------------------------------------
 * @copyright Copyright 2014-2016 JR.JD.COM All Rights Reserved
 * -----------------------------------------------------------------
 * 项目名称: scrt-supper
 * <p/>
 * 类名称: com.jd.scrt.common.utils.web.Page<T>
 * 功 能: 分页对象
 * -----------------------------------------------------------------
 * 创建人： wangjunlei1
 * 创建时间： 2016/4/3 20:55
 * 版本号： 1.0
 * <p/>
 * 修改人： wangjunlei1
 * 修改时间： 2016/4/3 20:55
 * 版本号： 1.0
 * 修改原因：
 * <p/>
 * 复审人：
 * 复审时间：
 * -------------------------------------------------------------
 */
public class Page<T> {
  public Long pageNum; //页码
 public Long pageSize; //每页条数
 public Long count; //总条数
 public Long total; //总页数
 public List<T> data; //每页对象列表
 public String msg; //显示信息

 public Page() {
        this(1L, PageInfoConsts.DEFAULT_PAGESIZE, 0L, null);
 }

    public Page(String msg) {
        this(1L, PageInfoConsts.DEFAULT_PAGESIZE, 0L, null, msg);
 }

    /*public Page(int pageNum, int pageSize, Long count, List<T> data) {
 this((long)pageNum, (long)pageSize, count, data, null);
 }*/


 public Page(Long pageNum, Long pageSize, Long count, List<T> data) {
        this(pageNum, pageSize, count, data, null);
 }

    public Page(Long pageNum, Long pageSize, Long count, List<T> data, String msg) {
        if (pageNum == null) {
            this.pageNum = 1L;
 } else {
            this.pageNum = pageNum;
 }
        if (pageSize == null) {
            this.pageSize = PageInfoConsts.DEFAULT_PAGESIZE;
 } else {
            this.pageSize = pageSize;
 }
        if (count == null) {
            this.count = 0L;
 } else {
            this.count = count;
 }
        this.data = data;
 this.msg = msg;
 computeAttrs();
 }

    public Long getPageNum() {
        return pageNum;
 }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
 computeAttrs();
 }

    public Long getPageSize() {
        return pageSize;
 }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
 computeAttrs();
 }

    public Long getCount() {
        return count;
 }

    public void setCount(Long count) {
        this.count = count;
 computeAttrs();
 }

    public String getMsg() {
        return msg;
 }

    public void setMsg(String msg) {
        this.msg = msg;
 }

    public Long getCountPage() {
        if (count != null && count > 0) {
            return count / pageSize + (count % pageSize > 0 ? 1 : 0);
 }
        return 1L;
 }

    public Long getPrevPage() {

        if (pageNum != null && pageNum != 1) {
            return pageNum - 1;
 } else {
            return pageNum;
 }
    }

    public Long getNextPage() {
        if (pageNum != null && pageNum != getCountPage()) {
            return pageNum + 1;
 } else {
            return pageNum;
 }
    }


    private void computeAttrs() {
        // 每页条数
 this.pageSize = this.pageSize <= 0 ? 10 : this.pageSize;

 // 数据总个数
 this.count = this.count < 0 ? 0 : this.count;

 this.total = getCountPage();

 // 当前页号
 this.pageNum = this.pageNum <= 0 ? 1 : (this.pageNum > this.total ? this.total : this.pageNum);
 }


    /**
 * 是否禁用“首页”
 * @return boolean
 */
 public boolean disabledFirstPage() {
        return this.getCountPage() == 0L || this.getCountPage() == 1L || this.pageNum == 0 || this.pageNum == 1;
 }

    /**
 * 是否禁用“上一页”
 * @return boolean
 */
 public boolean disabledPreviousPage() {
        return this.getCountPage() == 0L || this.getCountPage() == 1L || this.pageNum == 0 || this.pageNum == 1;
 }

    /**
 * 是否禁用“下一页”
 * @return boolean
 */
 public boolean disabledNextPage() {
        return this.getCountPage() == 0L || this.getCountPage() == 1L || this.pageNum.equals(this.getCountPage());
 }

    /**
 * 是否禁用“末页”
 * @return boolean
 */
 public boolean disabledLastPage() {
        return this.getCountPage() == 0L || this.getCountPage() == 1L || this.pageNum.equals(this.getCountPage());
 }

    /**
 * 是否禁用“GO”
 * @return boolean
 */
 public boolean disabledTurnTo() {
        return this.getCountPage() == 0L || this.getCountPage() == 1L;
 }

    /**
 * 显示的页列表 设置显示位数为7位 1 2 3 4 5 6 7
 * 或 1...5 6 7...22
 */
 public List<PagerBtn> getPageNumList() {
        List<PagerBtn> pageNumList = new ArrayList<PagerBtn>();

 pageNumList.add(new PagerBtn("前一页", getPrevPage(), disabledFirstPage(), false));
 //KeyValue keyValue = new DefaultKeyValue("", true);
 if (this.total <= 7) {
            for (int i = 0; i < this.total; i++) {
                pageNumList.add(new PagerBtn(String.valueOf(i + 1), i + 1L, pageNum == (i + 1), i + 1 == this.pageNum));
 }
        } else if (pageNum <= 4) {
            for (int i = 0; i < 5; i++) {
                pageNumList.add(new PagerBtn(String.valueOf(i + 1), i + 1L, pageNum == (i + 1), i + 1 == this.pageNum));
 }
            pageNumList.add(new PagerBtn("...", 1L, true, false));
 pageNumList.add(new PagerBtn(this.total + "", this.total, false, false));
 } else if (pageNum >= this.total - 3) {
            pageNumList.add(new PagerBtn("1", 1L, false, false));
 pageNumList.add(new PagerBtn("...", 1L, true, false));
 for (long l = this.total - 5; l < this.total; l++) {
                pageNumList.add(new PagerBtn(String.valueOf(l + 1), l + 1, pageNum == (l + 1), pageNum == (l + 1)));
 }
        } else {
            pageNumList.add(new PagerBtn("1", 1L, false, false));
 pageNumList.add(new PagerBtn("...", 1L, true, false));
 for (long l = pageNum - 2; l < pageNum + 1; l++) {
                pageNumList.add(new PagerBtn(String.valueOf(l + 1), l + 1, pageNum == (l + 1), pageNum == (l + 1)));
 }
            pageNumList.add(new PagerBtn("...", 1L, true, false));
 pageNumList.add(new PagerBtn(this.total + "", this.total, false, false));
 }


        pageNumList.add(new PagerBtn("后一页", getNextPage(), disabledNextPage(), false));

 return pageNumList;
 }

    public String print() {
        List<PagerBtn> pageNumList = getPageNumList();
 StringBuilder builder = new StringBuilder();
 for (PagerBtn keyValue : pageNumList) {
            builder.append("[").append(keyValue.getName()).append("(").append(keyValue.isDisabled()).append(")] ");
 }
        return builder.toString();
 }


    class PagerBtn {
        /** 按钮名称 */
 private String name;
 /** 按钮跳转页 */
 private Long num;
 /** 是否是disabled */
 private boolean disabled;
 /** 是否是当前页 */
 private boolean current;

 public PagerBtn() {

        }

        public PagerBtn(String name, Long num, boolean disabled, boolean current) {
            this.name = name;
 this.num = num;
 this.disabled = disabled;
 this.current = current;
 }

        public String getName() {
            return name;
 }

        public void setName(String name) {
            this.name = name;
 }

        public Long getNum() {
            return num;
 }

        public void setNum(Long num) {
            this.num = num;
 }

        public boolean isDisabled() {
            return disabled;
 }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
 }

        public boolean isCurrent() {
            return current;
 }

        public void setCurrent(boolean current) {
            this.current = current;
 }
    }


    public static void main(String[] args) {
        System.out.println(new Page(1L, 10L, 10L, null).print());
 System.out.println(new Page(2L, 10L, 13L, null).print());
 System.out.println(new Page(3L, 10L, 25L, null).print());
 System.out.println(new Page(4L, 10L, 40L, null).print());
 System.out.println(new Page(5L, 10L, 44L, null).print());
 System.out.println(new Page(2L, 10L, 50L, null).print());
 System.out.println(new Page(2L, 10L, 50L, null).print());
 System.out.println(new Page(2L, 10L, 70L, null).print());
 System.out.println(new Page(5L, 10L, 83L, null).print());
 System.out.println(new Page(20L, 10L, 222L, null).print());
 System.out.println(new Page(18L, 10L, 222L, null).print());
 System.out.println(new Page(12L, 10L, 222L, null).print());
 System.out.println(new Page(22L, 10L, 222L, null).print());
 System.out.println(new Page(23L, 10L, 222L, null).print());

 }


}
</code></pre>
