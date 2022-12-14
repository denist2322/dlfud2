# DB 생성
DROP DATABASE IF EXISTS sbb;
CREATE DATABASE sbb;
USE sbb;

# 회원 테이블 생성
CREATE TABLE `user` (
    id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    reg_date DATETIME NOT NULL,
    update_date DATETIME NOT NULL,
    email VARCHAR(100) NOT NULL,
    `password` VARCHAR(150) NOT NULL,
    `name` CHAR(50) NOT NULL
);


# 회원 데이터 생성
INSERT INTO `user`
SET reg_date = NOW(),
update_date = NOW(),
email = 'use1@test.com',
`password` = '1234',
`name` = '유저1';

INSERT INTO `user`
SET reg_date = NOW(),
update_date = NOW(),
email = 'use2@test.com',
`password` = '1234',
`name` = '유저2';

INSERT INTO `user`
SET reg_date = NOW(),
update_date = NOW(),
email = 'use3@test.com',
`password` = '1234',
`name` = '유저3';

# 게시물 테이블 생성
CREATE TABLE `article` (
    id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    reg_date DATETIME NOT NULL,
    update_date DATETIME NOT NULL,
    title VARCHAR(100) NOT NULL,
    `body` TEXT NOT NULL,
    `user_id` BIGINT UNSIGNED NOT NULL
);

# 게시물 데이터 생성
INSERT INTO `article`
SET reg_date = NOW(),
update_date = NOW(),
title = '제목 1',
`body` = '내용 1',
`user_id` = 1;

INSERT INTO `article`
SET reg_date = NOW(),
update_date = NOW(),
title = '제목 2',
`body` = '내용 2',
`user_id` = 2;

INSERT INTO `article`
SET reg_date = NOW(),
update_date = NOW(),
title = '제목 3',
`body` = '내용 3',
`user_id` = 3;

INSERT INTO `article`
SET reg_date = NOW(),
update_date = NOW(),
title = '제목 4',
`body` = '이건 정말정말 긴 길이의 텍스트입니다. 이것을 만든 이유는 몰라도 됩니다. 미안합니다 사실 테스트를 위해서 만든것입니다. 과연 잘 나올지 궁금해죠?',
`user_id` = 3;

CREATE TABLE files(
    id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `filename` TEXT,
    article_id BIGINT UNSIGNED NOT NULL
);

SELECT * FROM article;
SELECT * FROM `user`;
SELECT * FROM `files`;
