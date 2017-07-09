/**
 * Created by yangqihua on 2017/7/7.
 */

const env = process.env.NODE_ENV;
const settings = {
    baseUrl: (env!=='development'?'部署服务器IP':'http://localhost:8080/'),
};


export default settings
