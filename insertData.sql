insert into `tb_role` (`id`,`role_name`,`create_time`,`update_time`,`version`,`deleted`) values (null,"游客",now() ,now(),1,0),
(null,"教师",now() ,now(),1,0),(null,"学生",now() ,now(),1,0),(null,"管理员",now() ,now(),1,0),(null,"超级管理员",now() ,now(),1,0);

insert into `role_permissions` (`id`,`role_id`,`permissions_id`,`create_time`,`update_time`,`version`,`deleted`) values (null,4,1,now() ,now(),1,0),
(null,4,2,now() ,now(),1,0);
insert into `permissions` (`id`,`permissions_name`,`create_time`,`update_time`,`version`,`deleted`) values (1,"resetPassById",now() ,now(),1,0),
(2,"getUserByCondition",now() ,now(),1,0);