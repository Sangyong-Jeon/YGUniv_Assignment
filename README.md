# 익명 게시판 만들기
영진전문대학교 객체지향프로그래밍(III), 웹프로그래밍(II) 과제

2022.06.02 ~ 2022.06.10


### 앱
- Anroid Studio Bumblebee 2021.1.1 patch2
- Compile SDK version : Android API 32
- Gradle Version 7.2
- Java : JDK 1.8

[안드로이드스튜디오_실행화면.pdf](https://github.com/Sangyong-Jeon/YGUniv_Assignment/files/8986026/_.pdf)

### 백엔드
- IntelliJ IDEA 2021.3.2 (Ultimate Edition)
- Java : OpenJDK 1.8
- Spring Boot 2.7.0
- JPA
- Spring Data JPA
- Lombok <br>

[스프링부트_API명세서.pdf](https://github.com/Sangyong-Jeon/YGUniv_Assignment/files/8986029/_API.pdf)

### DB
- MySQL

---

# 기능
- 전체 게시글 조회
  - 각 리스트 항목은 글번호, 제목, 등록일시, 조회수
  - 기본은 최신순 조회이고, 조회순으로도 조회가능하게 해야함
  - 페이지 단위 조회는 구현 X
- 게시글 검색
  - 사용자가 입력한 키워드를 제목에 포함한 게시글 리스트 조회
- 게시글 상세 조회
  - 리스트에서 선택한 글 확인
- 게시글 등록
  - 제목, 본문, 비밀번호
- 게시글 수정
  - 비밀번호가 맞으면 제목과 본문 수정
  - 게시글이 수정되어도 조회수는 변하지 않음
- 게시글 삭제
  - 비밀번호가 맞으면 게시글 삭제
- 서버 주소 변경할 수 있는 기능

---

# 안드로이드 스튜디오 실행화면

<img width="1348" alt="image" src="https://user-images.githubusercontent.com/80039556/175800409-c4ee1653-1958-46ef-9732-ab09e09e7088.png">
<img width="1341" alt="image" src="https://user-images.githubusercontent.com/80039556/175800424-d7b917ea-86d9-4e5c-9c16-399785d0d571.png">

- 요구사항으로 서버를 스마트폰 또는 컴퓨터 사용하기에 서버 url을 변경할 수 있는 기능을 추가

<img width="1329" alt="image" src="https://user-images.githubusercontent.com/80039556/175800450-c2f63a8a-43aa-4170-837a-38cb717dd967.png">
<img width="1326" alt="image" src="https://user-images.githubusercontent.com/80039556/175800452-b578e695-190f-4bda-98a0-7698a9a20351.png">
<img width="1336" alt="image" src="https://user-images.githubusercontent.com/80039556/175800455-4747e5eb-27db-453e-bd0c-f36c9b81dbc8.png">
<img width="1331" alt="image" src="https://user-images.githubusercontent.com/80039556/175800459-c331067e-2722-4663-b38a-1a00e0b6bead.png">
<img width="1327" alt="image" src="https://user-images.githubusercontent.com/80039556/175800462-d236393d-a344-4e9f-b77b-0b4c480dea5c.png">
<img width="1348" alt="image" src="https://user-images.githubusercontent.com/80039556/175800514-fd4021a4-9bf0-4748-a94f-ef2ad66f9557.png">
<img width="1333" alt="image" src="https://user-images.githubusercontent.com/80039556/175800466-f0d6b6a1-a717-4ac3-8ba7-42680f51acda.png">

