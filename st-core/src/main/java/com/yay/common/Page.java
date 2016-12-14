package com.yay.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 * @param <T>
 */
public class Page<T> {
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    public Integer pageNum;    //页码
    public Integer pageSize;   //每页条数
    public Integer count;      //总条数
    public Integer total;      //总页数
    public List<T> data;    //每页对象列表
    public String msg;      //显示信息

    public Page() {
        this(1, DEFAULT_PAGE_SIZE, 0, null);
    }

    public Page(String msg) {
        this(1, DEFAULT_PAGE_SIZE, 0, null, msg);
    }

    /*public Page(int pageNum, int pageSize, Long count, List<T> data) {
        this((long)pageNum, (long)pageSize, count, data, null);
    }*/


    public Page(Integer pageNum, Integer pageSize, Integer count, List<T> data) {
        this(pageNum, pageSize, count, data, null);
    }

    public Page(Integer pageNum, Integer pageSize, Integer count, List<T> data, String msg) {
        if (pageNum == null) {
            this.pageNum = 1;
        } else {
            this.pageNum = pageNum;
        }
        if (pageSize == null) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        } else {
            this.pageSize = pageSize;
        }
        if (count == null) {
            this.count = 0;
        } else {
            this.count = count;
        }
        this.data = data;
        this.msg = msg;
        computeAttrs();
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        computeAttrs();
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        computeAttrs();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
        computeAttrs();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCountPage() {
        if (count != null && count > 0) {
            return count / pageSize + (count % pageSize > 0 ? 1 : 0);
        }
        return 1;
    }

    public Integer getPrevPage() {

        if (pageNum != null && pageNum != 1) {
            return pageNum - 1;
        } else {
            return pageNum;
        }
    }

    public Integer getNextPage() {
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
     * 显示的页列表 设置显示位数为7位  1 2 3 4 5 6 7
     * 或 1...5 6 7...22
     */
    public List<PagerBtn> getPageNumList() {
        List<PagerBtn> pageNumList = new ArrayList<PagerBtn>();

        pageNumList.add(new PagerBtn("前一页", getPrevPage(), disabledFirstPage(), false));
        //KeyValue keyValue = new DefaultKeyValue("", true);
        if (this.total <= 7) {
            for (int i = 0; i < this.total; i++) {
                pageNumList.add(new PagerBtn(String.valueOf(i + 1), i + 1, pageNum == (i + 1), i + 1 == this.pageNum));
            }
        } else if (pageNum <= 4) {
            for (int i = 0; i < 5; i++) {
                pageNumList.add(new PagerBtn(String.valueOf(i + 1), i + 1, pageNum == (i + 1), i + 1 == this.pageNum));
            }
            pageNumList.add(new PagerBtn("...", 1, true, false));
            pageNumList.add(new PagerBtn(this.total + "", this.total, false, false));
        } else if (pageNum >= this.total - 3) {
            pageNumList.add(new PagerBtn("1", 1, false, false));
            pageNumList.add(new PagerBtn("...", 1, true, false));
            for (int l = this.total - 5; l < this.total; l++) {
                pageNumList.add(new PagerBtn(String.valueOf(l + 1), l + 1, pageNum == (l + 1), pageNum == (l + 1)));
            }
        } else {
            pageNumList.add(new PagerBtn("1", 1, false, false));
            pageNumList.add(new PagerBtn("...", 1, true, false));
            for (int l = pageNum - 2; l < pageNum + 1; l++) {
                pageNumList.add(new PagerBtn(String.valueOf(l + 1), l + 1, pageNum == (l + 1), pageNum == (l + 1)));
            }
            pageNumList.add(new PagerBtn("...", 1, true, false));
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
        private Integer num;
        /** 是否是disabled */
        private boolean disabled;
        /** 是否是当前页 */
        private boolean current;

        public PagerBtn() {

        }

        public PagerBtn(String name, Integer num, boolean disabled, boolean current) {
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

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
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
        System.out.println(new Page(1, 10, 10, null).print());
        System.out.println(new Page(2, 10, 13, null).print());
        System.out.println(new Page(3, 10, 25, null).print());
        System.out.println(new Page(4, 10, 40, null).print());
        System.out.println(new Page(5, 10, 44, null).print());
        System.out.println(new Page(2, 10, 50, null).print());
        System.out.println(new Page(2, 10, 50, null).print());
        System.out.println(new Page(2, 10, 70, null).print());
        System.out.println(new Page(5, 10, 83, null).print());
        System.out.println(new Page(20, 10, 222, null).print());
        System.out.println(new Page(18, 10, 222, null).print());
        System.out.println(new Page(12, 10, 222, null).print());
        System.out.println(new Page(22, 10, 222, null).print());
        System.out.println(new Page(23, 10, 222, null).print());

    }


}

