package com.yay.domain;

import java.util.Date;

public class CommonProblem {
    /**
     * 问题id
     */
    private Long id;

    /**
     * 问题类型：1/首页 2/产品详情
     */
    private Integer problemType;

    /**
     * 问题标题
     */
    private String problemTitle;

    /**
     * 问题内容
     */
    private String problemContent;

    /**
     * 0无效  1有效
     */
    private Integer yn;

    /**
     * 版本号
     */
    private Long version;

    /**
     * 更新人ID
     */
    private String updaterId;

    /**
     * 更新人
     */
    private String updateOperator;

    /**
     * 创建人ID
     */
    private String creatorId;

    /**
     * 创建人
     */
    private String createOperator;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 常见问题分类：1 A型、2 B型、3 C型
     */
    private Integer problemClass;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProblemType() {
        return problemType;
    }

    public void setProblemType(Integer problemType) {
        this.problemType = problemType;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }

    public String getProblemContent() {
        return problemContent;
    }

    public void setProblemContent(String problemContent) {
        this.problemContent = problemContent;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public String getUpdateOperator() {
        return updateOperator;
    }

    public void setUpdateOperator(String updateOperator) {
        this.updateOperator = updateOperator;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreateOperator() {
        return createOperator;
    }

    public void setCreateOperator(String createOperator) {
        this.createOperator = createOperator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getProblemClass() {
        return problemClass;
    }

    public void setProblemClass(Integer problemClass) {
        this.problemClass = problemClass;
    }

    @Override
    public String toString() {
        return "CommonProblem{" +
                "id=" + id +
                ", problemType=" + problemType +
                ", problemTitle='" + problemTitle + '\'' +
                ", problemContent='" + problemContent + '\'' +
                ", yn=" + yn +
                ", version=" + version +
                ", updaterId='" + updaterId + '\'' +
                ", updateOperator='" + updateOperator + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", createOperator='" + createOperator + '\'' +
                ", updateTime=" + updateTime +
                ", createTime=" + createTime +
                ", remark='" + remark + '\'' +
                ", problemClass=" + problemClass +
                '}';
    }
}