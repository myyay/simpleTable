
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
    if (undefined == pager.targetDiv || null == pager.targetDiv) {
        return;
    }
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
        $('.' + cls + ':visible').prop('checked', true);
    } else {
        $('.' + cls + ':visible').prop('checked', false);
    }
}

function _renderPageSizeDiv(pager) {
    if (undefined == pager.targetPageSizeDiv || null == pager.targetPageSizeDiv ) {
        return;
    }
    var t = $('#' + pager.targetPageSizeDiv);
    if ($.trim(t.html()) == '') {
        t.append('<h4 style="margin-left: 15px;">每页显示 <select>' +
            '<option value="10">10</option>' +
            '<option value="20">20</option>' +
            '<option value="30">30</option>' +
            '<option value="50">50</option>' +
            '<option value="100">100</option>' +
            '</select>条 </h4>');
        t.find('select').on('change', function () {
            //console.log(this);
            _loadPage(1, this.value, pager.mapping, pager.loadFunc);
        });
    }
}

function _changePageSize(sel) {
    var pageSize = sel.value;
    _loadPage(1, pageSize, pager.mapping, pager.loadFunc);
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
        //money命令格式化
        if (cmd == 'money') {
            return _numberFormat(fieldValue,2);
        }
        //rateFormat命令格式化
        if (cmd == 'rate') {
            return _numberFormat(fieldValue,2) + '%';
        }
        //sellerChannel命令格式化
        if (cmd == 'sellerChannel') {
            var sellerChannelShow = [0,1,2,3,4,5,6,7,8];
            var sellerChannelAll = [255,4,2,2,1,1,8,16,32];
            var sellerChannelArr = [];
            for (var i = 0; i < sellerChannelAll.length; i++) {
                if ((sellerChannelAll[i] & fieldValue) != 0) {
                    sellerChannelArr.push(sellerChannelShow[i]);
                }
            }
            return sellerChannelArr.join('、');
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

function _numberFormat(sourceObj, digits) {
    var f = Math.floor(sourceObj*Math.pow(10,digits))/Math.pow(10,digits);
    var s = f.toString();
    var rs = s.indexOf('.');
    if (rs < 0) {
        rs = s.length;
        s += '.';
    }
    while (s.length <= rs + digits) {
        s += '0';
    }
    return s;
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
    _renderPageSizeDiv(pager);

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


//=================================== 分页列表功能 ↑ ===================================