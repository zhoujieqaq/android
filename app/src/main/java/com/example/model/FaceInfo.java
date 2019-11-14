package com.example.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FaceInfo {

    /**
     * error_code : 0
     * error_msg : SUCCESS
     * log_id : 1019955101001
     * timestamp : 1573293175
     * cached : 0
     * result : {"face_token":"629a3c291bde2629ea250de6dcd1a202","user_list":[{"group_id":"test2","user_id":"test","user_info":"daoqi2","score":100}]}
     */

    private int error_code;
    private String error_msg;
    private long log_id;
    private int timestamp;
    private int cached;
    private ResultBean result;


    //用于单张人脸返回的json解析
    public FaceInfo(String json) {
        try {
            //第一层解析
            JSONObject jsonObject = new JSONObject(json);
            JSONObject result = jsonObject.optJSONObject("result");
            int error_code = jsonObject.optInt("error_code");
            String error_msg = jsonObject.optString("error_msg");
            //Long Log_id = jsonObject.optLong("log_id");
            this.setError_code(error_code);
            this.setError_msg(error_msg);


            //如果为SUCCESS，则进行第二层解析
            if (error_msg.equals("SUCCESS")) {
                //javabean

                FaceInfo.ResultBean resultBean = new FaceInfo.ResultBean();
                this.setResult(resultBean);
                //第二层解析
                String face_token = result.optString("face_token");
                JSONArray user_list = result.optJSONArray("user_list");

                //javabean
                resultBean.setFace_token(face_token);
                List<FaceInfo.ResultBean.UserListBean> userListBeansBean = new ArrayList<>();
                resultBean.setUser_list(userListBeansBean);

                //第三层解析
                if (user_list != null) {
                    for (int i = 0; i < user_list.length(); i++) {
                        JSONObject jsonObject1 = user_list.optJSONObject(i);
                        if (jsonObject1 != null) {
                            String user_id = jsonObject1.optString("user_id");
                            String user_info = jsonObject1.optString("user_info");

                            //javabean
                            FaceInfo.ResultBean.UserListBean bean = new FaceInfo.ResultBean.UserListBean();
                            bean.setUser_id(user_id);
                            bean.setUser_info(user_info);
                            userListBeansBean.add(bean);
                        }
                    }
                }
            }
            } catch(JSONException e){
                e.printStackTrace();
            }

    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getCached() {
        return cached;
    }

    public void setCached(int cached) {
        this.cached = cached;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * face_token : 629a3c291bde2629ea250de6dcd1a202
         * user_list : [{"group_id":"test2","user_id":"test","user_info":"daoqi2","score":100}]
         */

        private String face_token;
        private List<UserListBean> user_list;

        public String getFace_token() {
            return face_token;
        }

        public void setFace_token(String face_token) {
            this.face_token = face_token;
        }

        public List<UserListBean> getUser_list() {
            return user_list;
        }

        public void setUser_list(List<UserListBean> user_list) {
            this.user_list = user_list;
        }

        public static class UserListBean {
            /**
             * group_id : test2
             * user_id : test
             * user_info : daoqi2
             * score : 100
             */

            private String group_id;
            private String user_id;
            private String user_info;
            private int score;

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_info() {
                return user_info;
            }

            public void setUser_info(String user_info) {
                this.user_info = user_info;
            }

            public int getScore() {
                return score;
            }

            @Override
            public String toString() {
                return "UserListBean{" +
                        "group_id='" + group_id + '\'' +
                        ", user_id='" + user_id + '\'' +
                        ", user_info='" + user_info + '\'' +
                        ", score=" + score +
                        '}';
            }


            public void setScore(int score) {
                this.score = score;
            }



        }
    }
}
