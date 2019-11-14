package com.example.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//用于解析多张人脸返回的json数据
public class MultiFace {
    /**
     * error_code : 0
     * error_msg : SUCCESS
     * log_id : 1555653535899
     * timestamp : 1573310059
     * cached : 0
     * result : {"face_num":2,"face_list":[{"face_token":"216fbd22dc0d396c4582994582fef520","location":{"left":273.68,"top":98.5,"width":50,"height":52,"rotation":0},"user_list":[{"group_id":"test4","user_id":"huangbo","user_info":"huangbo","score":83.464210510254}]},{"face_token":"8993abdfe41e2841cc0335e5f314d275","location":{"left":173.52,"top":77.19,"width":53,"height":49,"rotation":11},"user_list":[{"group_id":"test4","user_id":"xuzheng","user_info":"xuzheng","score":93.112098693848}]}]}
     */

    private int error_code;
    private String error_msg;
    private long log_id;
    private int timestamp;
    private int cached;
    private ResultBean result;

    public MultiFace (String json) throws JSONException {
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
            MultiFace.ResultBean resultBean = new MultiFace.ResultBean();
            this.setResult(resultBean);

            //第二层解析
            JSONArray face_list = result.optJSONArray("face_list");
            int face_num = result.optInt("face_num");

            //javabean
            resultBean.setFace_num(face_num);
            List<MultiFace.ResultBean.FaceListBean> faceListBean = new ArrayList<>();
            resultBean.setFace_list(faceListBean);

            //第三层解析
            for(int i = 0;i < face_list.length();i++){
                JSONObject jsonObject1 = face_list.optJSONObject(i);
                if(jsonObject1 != null){
                    String face_token = jsonObject1.optString("face_token");
                    JSONObject location = jsonObject1.optJSONObject("location");
                    JSONArray user_list = jsonObject1.optJSONArray("user_list");

                    //javabean
                    MultiFace.ResultBean.FaceListBean faceListBean1 = new MultiFace.ResultBean.FaceListBean();
                    faceListBean1.setFace_token(face_token);
                    MultiFace.ResultBean.FaceListBean.LocationBean locationBean = new MultiFace.ResultBean.FaceListBean.LocationBean();
                    faceListBean1.setLocation(locationBean);
                    List<MultiFace.ResultBean.FaceListBean.UserListBean> userListBean = new ArrayList<>();
                    faceListBean1.setUser_list(userListBean);
                    faceListBean.add(faceListBean1);

                    //第四层解析location
                    double left = location.optDouble("left");
                    double top = location.optDouble("top");
                    int width = location.optInt("width");
                    int height = location.optInt("height");
                    int rotation = location.optInt("rotation");

                    //javabean
                    locationBean.setLeft(left);
                    locationBean.setTop(top);
                    locationBean.setWidth(width);
                    locationBean.setHeight(height);
                    locationBean.setRotation(rotation);


                    //第四层解析user_list
                    for(int j = 0;j < user_list.length();j++){
                        JSONObject jsonObject2 = user_list.optJSONObject(j);
                        if(jsonObject2 != null){
                            String user_id = jsonObject2.optString("user_id");
                            String user_info = jsonObject2.optString("user_info");

                            //javabean
                            MultiFace.ResultBean.FaceListBean.UserListBean bean = new ResultBean.FaceListBean.UserListBean();
                            bean.setUser_id(user_id);
                            bean.setUser_info(user_info);
                            userListBean.add(bean);
                        }
                    }
                }
            }
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
         * face_num : 2
         * face_list : [{"face_token":"216fbd22dc0d396c4582994582fef520","location":{"left":273.68,"top":98.5,"width":50,"height":52,"rotation":0},"user_list":[{"group_id":"test4","user_id":"huangbo","user_info":"huangbo","score":83.464210510254}]},{"face_token":"8993abdfe41e2841cc0335e5f314d275","location":{"left":173.52,"top":77.19,"width":53,"height":49,"rotation":11},"user_list":[{"group_id":"test4","user_id":"xuzheng","user_info":"xuzheng","score":93.112098693848}]}]
         */

        private int face_num;
        private List<FaceListBean> face_list;


        public int getFace_num() {
            return face_num;
        }

        public void setFace_num(int face_num) {
            this.face_num = face_num;
        }

        public List<FaceListBean> getFace_list() {
            return face_list;
        }

        public void setFace_list(List<FaceListBean> face_list) {
            this.face_list = face_list;
        }

        public static class FaceListBean {
            /**
             * face_token : 216fbd22dc0d396c4582994582fef520
             * location : {"left":273.68,"top":98.5,"width":50,"height":52,"rotation":0}
             * user_list : [{"group_id":"test4","user_id":"huangbo","user_info":"huangbo","score":83.464210510254}]
             */

            private String face_token;
            private LocationBean location;
            private List<UserListBean> user_list;

            public String getFace_token() {
                return face_token;
            }

            public void setFace_token(String face_token) {
                this.face_token = face_token;
            }

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public List<UserListBean> getUser_list() {
                return user_list;
            }

            public void setUser_list(List<UserListBean> user_list) {
                this.user_list = user_list;
            }

            public static class LocationBean {
                /**
                 * left : 273.68
                 * top : 98.5
                 * width : 50
                 * height : 52
                 * rotation : 0
                 */

                private double left;
                private double top;
                private int width;
                private int height;
                private int rotation;

                public double getLeft() {
                    return left;
                }

                public void setLeft(double left) {
                    this.left = left;
                }

                public double getTop() {
                    return top;
                }

                public void setTop(double top) {
                    this.top = top;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getRotation() {
                    return rotation;
                }

                public void setRotation(int rotation) {
                    this.rotation = rotation;
                }
            }

            public static class UserListBean {
                /**
                 * group_id : test4
                 * user_id : huangbo
                 * user_info : huangbo
                 * score : 83.464210510254
                 */

                private String group_id;
                private String user_id;
                private String user_info;
                private double score;

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

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }
            }
        }
    }
}
