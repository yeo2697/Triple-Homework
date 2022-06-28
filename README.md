# 트리플여행자 클럽 마일리지 서비스
트리플 사용자들이 장소에 리뷰를 작성할 때 포인트를 부여하고, 전체/개인에 대한 포인트 부여 히스토리와 개인별 누적 포인트를 관리하고자 합니다.

---

## 목차
- [개발 환경](#개발-환경)
- [SPECIFICATIONS](#SPECIFICATIONS)
- [REQUIREMENTS](#REQUIREMENTS)
- [REMARKS](#REMARKS)
- [요구사항에 따른 상세 기술 구현 사항](#요구사항에-따른-상세-기술-구현-사항)
- [API 테스트](#API-테스트)

---

## 개발 환경
- 기본 환경
  - IDE : IntelliJ IDEA Community (Settings - Build - Gradle - <Build and run using>, <Run tests using> IntelliJ 설정)
  - OS : Windows 10
  - Git
- 서버
  - Java 11
  - Spring Boot 2.7.1
  - JPA/Hibernate
  - MySQL 8.x
  - Gradle
- 실행 방법

  1. 다운로드
  2. 압축 해제
  3. IntelliJ homework 폴더 열기
  4. Gradle Build(대기)
  5. 우측 상단 Gradle Tab 클릭
  6. Tasks
  7. build
  8. bootJar 더블 클릭
  9. build/libs/homework-0.0.1-SNAPSHOT.jar 확인
  10. cmd java -jar homework-0.0.1-SNAPSHOT.jar (port : 9011)
  
  jar 파일 요청시 압축하여 회신가능합니다 :D

---

## SPECIFICATIONS

리뷰 작성이 이뤄질때마다 리뷰 작성 이벤트가 발생하고, 아래 API로 이벤트를 전달합니다.

```json
POST /events
{
"type": "REVIEW",
"action": "ADD", /* "MOD", "DELETE" */
"reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
"content": "좋아요!",
"attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-
851d-4a50-bb07-9cc15cbdc332"],
"userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
"placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
```
- type: 미리 정의된 string 값을 가지고 있습니다. 리뷰 이벤트의 경우 "REVIEW" 로 옵니다.
- action: 리뷰 생성 이벤트의 경우 "ADD" , 수정 이벤트는 "MOD" , 삭제 이벤트는 "DELETE" 값을 가지고 있습니다.
- reviewId: UUID 포맷의 review id입니다. 어떤 리뷰에 대한 이벤트인지 가리키는 값입니다.
- content: 리뷰의 내용입니다.
- attachedPhotoIds: 리뷰에 첨부된 이미지들의 id 배열입니다.
- userId: 리뷰의 작성자 id입니다.
- placeId: 리뷰가 작성된 장소의 id입니다.

한 사용자는 장소마다 리뷰를 1개만 작성할 수 있고, 리뷰는 수정 또는 삭제할 수 있습니다. 리뷰 작성 보상 점수는
아래와 같습니다.

- 내용 점수
  - 1자 이상 텍스트 작성: 1점
  - 1장 이상 사진 첨부: 1점
- 보너스 점수
  - 특정 장소에 첫 리뷰 작성: 1점

---

## REQUIREMENTS

- 이 서비스를 위한 SQL(MySQL >= 5.7) 스키마를 설계해 주세요. 테이블과 인덱스에 대한 DDL이 필요합니다.
- 아래 API를 제공하는 서버 애플리케이션을 작성해 주세요.
  - POST /events 로 호출하는 포인트 적립 API
  - 포인트 조회 API
- 상세 요구사항은 아래와 같습니다.
  - REST API를 제공하는 서버 애플리케이션을 구현해 주세요.
  - 프로그래밍 언어는 Java, Kotlin, Python, JavaScript(혹은 TypeScript) 중에서 선택해주세요.
  - Framework, Library는 자유롭게 사용할 수 있어요. 추가의 Data Storage가 필요하다면 여러 종류를 사용해도 돼요.
  - 필수는 아니지만 테스트 케이스를 작성하면 더욱 좋아요!
  - 애플리케이션 실행 방법을 README 파일에 작성해 주세요.
  - 작업한 결과물을 GitHub에 올리고 URL을 알려주세요.

---

## REMARKS

- 포인트 증감이 있을 때마다 이력이 남아야 합니다.
- 사용자마다 현재 시점의 포인트 총점을 조회하거나 계산할 수 있어야 합니다.
- 포인트 부여 API 구현에 필요한 SQL 수행 시, 전체 테이블 스캔이 일어나지 않는 인덱스가 필요합니다.
- 리뷰를 작성했다가 삭제하면 해당 리뷰로 부여한 내용 점수와 보너스 점수는 회수합니다.
- 리뷰를 수정하면 수정한 내용에 맞는 내용 점수를 계산하여 점수를 부여하거나 회수합니다.
  - 글만 작성한 리뷰에 사진을 추가하면 1점을 부여합니다.
  - 글과 사진이 있는 리뷰에서 사진을 모두 삭제하면 1점을 회수합니다.
- 사용자 입장에서 본 '첫 리뷰'일 때 보너스 점수를 부여합니다.
  - 어떤 장소에 사용자 A가 리뷰를 남겼다가 삭제하고, 삭제된 이후 사용자 B가 리뷰를 남기면 사용자 B에게 보너스 점수를 부여합니다.
  - 어떤 장소에 사용자 A가 리뷰를 남겼다가 삭제하는데, 삭제되기 이전 사용자 B가 리뷰를 남기면 사용자 B에게 보너스 점수를 부여하지 않습니다.

---

## 요구사항에 따른 상세 기술 구현 사항

### DataBase Table 구성

#### 스키마
스키마명은 `triple` 입니다.

#### tbl_review (필수)

```sql
CREATE TABLE `tbl_review` (
	`idx` BIGINT(19) NOT NULL AUTO_INCREMENT,
	`review_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`user_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`place_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`review_content` TEXT NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`review_photos` TEXT NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`del_yn` CHAR(1) NULL DEFAULT 'N' COLLATE 'utf8_general_ci',
	`create_by` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`create_dt` BIGINT(19) NULL DEFAULT NULL,
	`update_by` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`update_dt` BIGINT(19) NULL DEFAULT NULL,
	PRIMARY KEY (`idx`) USING BTREE,
	INDEX `review_id` (`review_id`) USING BTREE,
	INDEX `user_id` (`user_id`) USING BTREE,
	INDEX `place_id` (`place_id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
```

- review 정보를 저장하는 테이블입니다.
- del_yn 컬럼으로 삭제여부를 확인합니다.
- review_id 컬럼으로 idx 와 함께 고유 무결성을 만족시킵니다. 단, del_yn 이 'Y'(삭제) 가 되면 review_id는 중복이 될 수 있습니다.
- user_id 와 place_id 로 place 당 하나의 user review를 만족시킵니다. -> SPECIFICATIONS 조건

#### tbl_mileage (필수)

```sql
CREATE TABLE `tbl_mileage` (
	`idx` BIGINT(19) NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`review_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`mileage_log` VARCHAR(200) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`mileage_value` BIGINT(19) NULL DEFAULT NULL,
	`del_yn` CHAR(1) NULL DEFAULT 'N' COLLATE 'utf8_general_ci',
	`create_by` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`create_dt` BIGINT(19) NULL DEFAULT NULL,
	`update_by` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`update_dt` BIGINT(19) NULL DEFAULT NULL,
	PRIMARY KEY (`idx`) USING BTREE,
	INDEX `user_id` (`user_id`) USING BTREE,
	INDEX `review_id` (`review_id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
```

- mileage 정보를 저장하는 테이블입니다.
- del_yn 컬럼으로 회수 및 여부를 확인합니다.
- review_id 로 review 삭제시 해당 review 에서 발생한 모든 마일리지를 회수합니다.
- user_id 로 user 의 마일리지 조회를 합니다.
- mileage_log 로 특정 보상에 대한 회수를 합니다. -> 텍스트 및 사진 업로드, 첫 리뷰 

#### tbl_user

```sql
CREATE TABLE `tbl_user` (
	`idx` BIGINT(19) NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`user_name` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`user_tel` VARCHAR(12) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`del_yn` CHAR(1) NULL DEFAULT 'N' COLLATE 'utf8_general_ci',
	`create_by` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`create_dt` BIGINT(19) NULL DEFAULT NULL,
	`update_by` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`update_dt` BIGINT(19) NULL DEFAULT NULL,
	PRIMARY KEY (`idx`) USING BTREE,
	INDEX `user_id` (`user_id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=3
;
```

- user 정보를 저장하는 테이블입니다.
- 구성만하였을뿐 사용하지는 않습니다.

#### tbl_place

```sql
CREATE TABLE `tbl_place` (
	`idx` BIGINT(19) NOT NULL AUTO_INCREMENT,
	`place_id` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`place_name` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`del_yn` CHAR(1) NULL DEFAULT 'N' COLLATE 'utf8_general_ci',
	`create_by` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`create_dt` BIGINT(19) NULL DEFAULT NULL,
	`update_by` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8_general_ci',
	`update_dt` BIGINT(19) NULL DEFAULT NULL,
	PRIMARY KEY (`idx`) USING BTREE,
	INDEX `place_id` (`place_id`) USING BTREE
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=2
;
```

- place 정보를 저장하는 테이블입니다.
- 구성만하였을뿐 사용하지는 않습니다.

### 문제 해결 전략
#### ADD
- 리뷰 작성시 고려할 사항은 다음과 같다.
	- 1자 이상 텍스트 작성시 + 1
	- 1장 이상 사진 첨부시 + 1
	- 특정 장소에 첫 리뷰 작성시 + 1
- ADD 요청 시 다음을 확인하고 마일리지를 적립한다.
	- 텍스트 길이 > 0 일 경우 마일리지 추가
	- 사진 개수 > 0 일 경우 마일리지 추가
	- place_id 와 del_yn 으로 review를 조회하여 첫 리뷰인지 확인 후, 부합하면 마일리지 부여
		- REMARKS 조건 클리어
- 상세 설명은 코드상 주석처리하였습니다 :D
	
#### MOD
- 리뷰 수정시 고려할 사항은 다음과 같다.
	- 기존 텍스트 길이가 0 이고, 수정된 텍스트 길이가 0 초과일때 + 1
	- 기존 텍스트 길이가 0 초과이고, 수정된 텍스트 길이가 0 일때 1 회수
	- 기존 사진 개수가 0 개 이고, 수정된 사진 개수가 0 개 초과일때 + 1
	- 기존 사진 개수가 0 개 초과이고, 수정된 사진 개수가 0 개 일때 1 회수
- review_id 와 mileage_log 로 특정 마일리지 회수
- 상세 설명은 코드상 주석처리하였습니다 :D
	
#### DEL
- 리뷰 삭제시 고려할 사항은 다음과 같다.
	- review_id 와 del_yn = 'N' 인 레코드 모두 'Y' (회수) 처리
- 상세 설명은 코드상 주석처리하였습니다 :D
	
#### 마일리지 조회
- user_id 와 del_yn = 'N' 인 조건을 사용하여 마일리지 총합 계산

```java
@Query(value = "select SUM(mileage_value) from tbl_mileage where user_id = :id and del_yn = 'N' group by user_id", nativeQuery = true)
Long getTotalMileages(@Param(value = "id") String userId);
```

### API

1. `POST /events` 로 호출되는 포인트 적립 API
#### REQUEST
- METHOD : POST
- URL : http://{IP}:{PORT}/events
- BODY
```json
{
"type": "REVIEW",
"action": "ADD",
"reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
"content": "좋아요!",
"attachedPhotoIds": [],
"userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
"placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
or
{
"type": "REVIEW",
"action": "MOD",
"reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
"content": "좋아요!",
"attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
"userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
"placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
or
{
"type": "REVIEW",
"action": "DEL",
"reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
"content": "좋아요!",
"attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
"userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
"placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}
```

#### RESPONSE
- HTTP STATUS : 201

`CREATED`

2. 포인트 조회 API
#### REQUEST
- METHOD : GET
- URI : http://{IP}:{PORT}/mileages?id={USER_ID}

#### RESPONSE
- HTTP STATUS : 200

`2`

---

## API 테스트

1. `POST /events` 로 호출되는 포인트 적립 API 테스트
2. 포인트 조회 API 테스트

- 실제 DataBase Table에 Commit 하도록 설정
- ADD / MOD / DEL 수행 및 예상되는 마일리지 조회로 구성
- 상세 설명은 주석처리하였습니다 :D

---
