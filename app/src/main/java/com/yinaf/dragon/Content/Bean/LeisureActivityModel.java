package com.yinaf.dragon.Content.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * 休闲活动
 */
public class LeisureActivityModel implements Serializable {

    /**
     * code : 2000
     * msg : 系统操作成功
     * pager : {"currentPage":1,"endRow":20,"firstPage":false,"getCount":false,"lastPage":false,"pageSize":20,"startRow":0,"totalPages":1,"totalRows":1}
     * rows : [{"startTime":"2018-04-06","actId":1,"activityTitle":"活动啊","actPlaces":50,"activityImg":"http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/2918/1803291811459786.jpg","usePlaces":20,"endTime":"2018-04-27"}]
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
         * startTime : 2018-04-06
         * actId : 1
         * activityTitle : 活动啊
         * actPlaces : 50
         * activityImg : http://yinafimg.oss-cn-hangzhou.aliyuncs.com/201803/2918/1803291811459786.jpg
         * usePlaces : 20
         * endTime : 2018-04-27
         */

        private String startTime;
        private String actId;
        private String activityTitle;
        private String actPlaces;
        private String activityImg;
        private String usePlaces;
        private String endTime;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
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

        public String getActPlaces() {
            return actPlaces;
        }

        public void setActPlaces(String actPlaces) {
            this.actPlaces = actPlaces;
        }

        public String getActivityImg() {
            return activityImg;
        }

        public void setActivityImg(String activityImg) {
            this.activityImg = activityImg;
        }

        public String getUsePlaces() {
            return usePlaces;
        }

        public void setUsePlaces(String usePlaces) {
            this.usePlaces = usePlaces;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
