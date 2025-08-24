use studentm;
create table Student
(
    Id         int auto_increment
        primary key,
    First_Name varchar(255) not null,
    Last_name  varchar(255) null,
    Age        int          not null,
    Email      varchar(255) null,
    constraint Student_pk_2
        unique (Email)
);

