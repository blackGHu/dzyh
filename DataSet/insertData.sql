insert into `tb_role` (`id`,`role_name`,`create_time`,`update_time`,`version`,`deleted`) values (null,"游客",now() ,now(),1,0),
(null,"教师",now() ,now(),1,0),(null,"学生",now() ,now(),1,0),(null,"管理员",now() ,now(),1,0),(null,"超级管理员",now() ,now(),1,0);

INSERT INTO `tb_college` VALUES (null, '计算机学院、软件学院、网络空间安全学院', now(), now(), 1, 0),
(null, '自动化学院、人工智能学院', now(), now(), 1, 0),
(null, '社会与人口学院', now(), now(), 1, 0),
(null, '管理学院', now(), now(), 1, 0),
(null, '通信学院', now(), now(), 1, 0),
(null, '理学院', now(), now(), 1, 0);

INSERT INTO `tb_major` VALUES (null, '软件工程', now(), now(), 1, 0),
(null, '信息安全', now(), now(), 1, 0),
(null, '计算机科学与技术', now(), now(), 1, 0),
(null, '通信工程', now(), now(), 1, 0),
(null, '电子信息', now(), now(), 1, 0);

INSERT INTO `tb_user` VALUES (null, '10000001', '10000001', 'admin', '00000000001', 4, 1, now(), now(), 1, 0),
(null, '10000000', '10000000', 'root', '00000000000', 5, 1, now(), now(), 1, 0);

INSERT INTO `user_role` VALUES (null, '10000001', 4, now(), now(), 1, 0),
(null, '10000000', 5, now(), now(), 1, 0);


