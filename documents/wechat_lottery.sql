CREATE DATABASE IF NOT EXISTS wechat_lottery DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

use wechat_lottery;

DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user(
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '编号',
  access_token VARCHAR(500) COMMENT '用于微信网页端登录',
  access_token1 VARCHAR(500) COMMENT '普通access_token',
  openid VARCHAR(200) COMMENT 'openid',
  unionid VARCHAR(200) COMMENT '联合id',
  wechat_nickname VARCHAR(100) COMMENT '微信昵称',
  wechat_no VARCHAR(100) COMMENT '微信号',
  phone VARCHAR(11) COMMENT '手机号',
  gender VARCHAR(2) COMMENT '性别',
  payed_fee DOUBLE COMMENT '支付金额',
  payed_time DATETIME COMMENT '支付时间',
  payed_order int COMMENT '支付顺序',
  prized int COMMENT '是否中奖'
) ENGINE = InnoDB DEFAULT CHARSET = utf8;