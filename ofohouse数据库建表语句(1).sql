--用户表
create table Users(
userID int identity(1,1) primary key,                     --用户ID
phonenumber varchar(11) not null,                         --手机号
username varchar(20)  ,                                   --昵称
password varchar(16) not null,                            --密码
realname varchar(20) ,                                    --姓名
sex varchar(2)check(sex in ('男','女')) default '男',     --性别，默认男
birthday varchar(9),                                      --生日
email varchar(50),                                        --邮箱
IDcardnum varchar(18) ,                                   --身份证号
permission int check(permission in(0,1)) default 0,       --实名认证1表示认证完成0表示未认证，默认未完成验证
zhifubao varchar(50),                                     --支付宝
touxiang varchar(50),                                     --头像
);

--登录表
create table ULogin(
  ID int identity(1,1) primary key,   
  phoneID varchar(50)   ,               
  phonenumber varchar(11) not null  ,
  password varchar(16),
  useraddress varchar(16)   ,                                      
  lastdatetime varchar(19) ,                                     
  login_status int check(login_status in(0,1)) default 0,   
             
) 
----创建房屋信息表
create table Houses(
  house_ID varchar(20) not null  primary key, 
  house_name varchar(30) not null,
  house_owner varchar(30) not null,
  house_introduce varchar(100),      
  house_addr varchar(50) not null,
  house_position varchar(50) not null,
  house_price int not null,
  house_phonenum varchar(20) not null,
  house_img varchar(20) not null,
  house_state int check(house_state in(0,1,2,3)) default 0,
  house_img2 varchar(20) ,
  house_img3 varchar(20) , 
  house_img4 varchar(20) ,
  count_ID varchar(20) , 
  foreign key(count_ID) references Counts( count_ID)
  
)
----创建折扣信息表
create table Counts(
  count_ID varchar(20) not null  primary key, 
  count_name varchar(20) not null  , 
  count_value varchar(100) not null,
  count_time varchar(20),
  count_endtime varchar(20)
 
)

create table Code(
code_ID  int identity(1,1) primary key,
house_ID varchar(20)  ,                  --房间ID
code varchar(4),
insert_time varchar(20),

)

create table SystemManager(
manager_ID int identity(1,1) primary key,
manager_phone varchar(11) not null,
manager_pwd varchar(10) not null,
manager_logintime varchar(19),
)


update Houses set house_state='0'where house_ID='00000002' and house_state='1'
drop table Houses
drop table Users
drop table ULogin

 drop table Code
 drop table SystemManager
 
 
 
 
 ----创建订单表
create table orders(
  order_ID int identity(1,1) primary key,      ---订单编号
  prepaid_amount varchar(10),
  order_money int,
  begin_time varchar(20),
  checkin_time varchar(20),
  checkout_time varchar(20),
  end_time varchar(20),
  order_state int, 
  rent_time varchar(20) ,                              --订单时间
  rent_money varchar(20) , 
  process_time varchar(20),
  house_ID varchar(20),               --房间ID
  userID int,                                      --用户ID
  order_time varchar(30),
  foreign key(userID) references Users( userID),
  foreign key(house_ID) references Houses( house_ID),
  )
  
  
   ----创建预定表
   create table subscribs(
  subscrib_ID int identity(1,1) primary key,
  userID int,       ---订单编号
  subscrib_money int,
  begin_date varchar(20),
  order_state int,
  order_ID varchar(20),
  begin_time varchar(20),
  end_date varchar(20),
  end_time varchar(20),
  house_phonenum varchar(20),
  foreign key(userID) references Users( userID),
 
  )