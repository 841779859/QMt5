--�û���
create table Users(
userID int identity(1,1) primary key,                     --�û�ID
phonenumber varchar(11) not null,                         --�ֻ���
username varchar(20)  ,                                   --�ǳ�
password varchar(16) not null,                            --����
realname varchar(20) ,                                    --����
sex varchar(2)check(sex in ('��','Ů')) default '��',     --�Ա�Ĭ����
birthday varchar(9),                                      --����
email varchar(50),                                        --����
IDcardnum varchar(18) ,                                   --���֤��
permission int check(permission in(0,1)) default 0,       --ʵ����֤1��ʾ��֤���0��ʾδ��֤��Ĭ��δ�����֤
zhifubao varchar(50),                                     --֧����
touxiang varchar(50),                                     --ͷ��
);

--��¼��
create table ULogin(
  ID int identity(1,1) primary key,   
  phoneID varchar(50)   ,               
  phonenumber varchar(11) not null  ,
  password varchar(16),
  useraddress varchar(16)   ,                                      
  lastdatetime varchar(19) ,                                     
  login_status int check(login_status in(0,1)) default 0,   
             
) 
----����������Ϣ��
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
----�����ۿ���Ϣ��
create table Counts(
  count_ID varchar(20) not null  primary key, 
  count_name varchar(20) not null  , 
  count_value varchar(100) not null,
  count_time varchar(20),
  count_endtime varchar(20)
 
)

create table Code(
code_ID  int identity(1,1) primary key,
house_ID varchar(20)  ,                  --����ID
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
 
 
 
 
 ----����������
create table orders(
  order_ID int identity(1,1) primary key,      ---�������
  prepaid_amount varchar(10),
  order_money int,
  begin_time varchar(20),
  checkin_time varchar(20),
  checkout_time varchar(20),
  end_time varchar(20),
  order_state int, 
  rent_time varchar(20) ,                              --����ʱ��
  rent_money varchar(20) , 
  process_time varchar(20),
  house_ID varchar(20),               --����ID
  userID int,                                      --�û�ID
  order_time varchar(30),
  foreign key(userID) references Users( userID),
  foreign key(house_ID) references Houses( house_ID),
  )
  
  
   ----����Ԥ����
   create table subscribs(
  subscrib_ID int identity(1,1) primary key,
  userID int,       ---�������
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