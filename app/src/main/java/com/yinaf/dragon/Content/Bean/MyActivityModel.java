package com.yinaf.dragon.Content.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * 我的活动
 *
 */
public class MyActivityModel implements Serializable {
    /**
     * code : 2000
     * msg : 系统操作成功
     * pager : {"currentPage":1,"endRow":20,"firstPage":false,"getCount":false,"lastPage":false,"pageSize":20,"startRow":0,"totalPages":1,"totalRows":1}
     * rows : [{"createTime":"2018-04-11","actId":1,"activityTitle":"活动啊","activityNum":"","activityImg":"http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/2918/1803291811459786.jpg","maId":8,"actIntegral":5}]
     * success : true
     */

    private String code;
    private String msg;
    private PagerBean pager;
    private boolean success;
    private List<RowsBean> rows;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PagerBean getPager() {
        return pager;
    }

    public void setPager(PagerBean pager) {
        this.pager = pager;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class PagerBean {
        /**
         * currentPage : 1
         * endRow : 20
         * firstPage : false
         * getCount : false
         * lastPage : false
         * pageSize : 20
         * startRow : 0
         * totalPages : 1
         * totalRows : 1
         */

        private int currentPage;
        private int endRow;
        private boolean firstPage;
        private boolean getCount;
        private boolean lastPage;
        private int pageSize;
        private int startRow;
        private int totalPages;
        private int totalRows;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public boolean isFirstPage() {
            return firstPage;
        }

        public void setFirstPage(boolean firstPage) {
            this.firstPage = firstPage;
        }

        public boolean isGetCount() {
            return getCount;
        }

        public void setGetCount(boolean getCount) {
            this.getCount = getCount;
        }

        public boolean isLastPage() {
            return lastPage;
        }

        public void setLastPage(boolean lastPage) {
            this.lastPage = lastPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getTotalRows() {
            return totalRows;
        }

        public void setTotalRows(int totalRows) {
            this.totalRows = totalRows;
        }
    }

    public static class RowsBean {
        /**
         * createTime : 2018-04-11
         * actId : 1
         * activityTitle : 活动啊
         * activityNum :
         * activityImg : http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/2918/1803291811459786.jpg
         * maId : 8
         * actIntegral : 5
         */

        private String createTime;
        private String actId;
        private String activityTitle;
        private String activityNum;
        private String activityImg;
        private String maId;
        private String actIntegral;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getActId() {
            return actId;
        }

        public void setActId(String actId) {
            this.actId = actId;
        }

        public String getActivityTitle() {
            return activityTitle;
        }

        public void setActivityTitle(String activityTitle) {
            this.activityTitle = activityTitle;
        }

        public String getActivityNum() {
            return activityNum;
        }

        public void setActivityNum(String activityNum) {
            this.activityNum = activityNum;
        }

        public String getActivityImg() {
            return activityImg;
        }

        public void setActivityImg(String activityImg) {
            this.activityImg = activityImg;
        }

        public String getMaId() {
            return maId;
        }

        public void setMaId(String maId) {
            this.maId = maId;
        }

        public String getActIntegral() {
            return actIntegral;
        }

        public void setActIntegral(String actIntegral) {
            this.actIntegral = actIntegral;
        }
    }
}
