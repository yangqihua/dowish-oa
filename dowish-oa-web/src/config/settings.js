/**
 * Created by yangqihua on 2017/7/7.
 */

const env = process.env.NODE_ENV;
const settings = {
    baseUrl: (env!=='development'?'部署服务器IP':'http://localhost:7777/'),
    // rootMenuId:0,//因为这个值是自增的，所以不能保证数据库里边根菜单的id为1.
};


export default settings
