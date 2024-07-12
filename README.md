# 💡마이 보드

---

### 프로젝트 목적
* 회원들과 의견을 주고 받을 수 있는 커뮤니티 서비스를 제작했습니다.
* 단순히 기능 구현 뿐만 아니라 서비스의 규모가 확장되었을 때를 고려하여 발생할 수 있는 문제들에 대해 생각했습니다.
* 가독성이 좋은 코드와 유지 보수가 용이한 코드를 작성하기 위해 리팩토링했습니다.
* 버그를 줄여 소프트웨어의 품질을 향상하고 개발 과정을 효율적으로 만들기 위해 테스트 코드를 작성했습니다.
---

## 서버 구조

<img width="497" alt="Untitled (1)" src="https://github.com/user-attachments/assets/8de79c09-b5a8-4b15-af19-15a5027d5484">

---

### 트러블 슈팅
* 다량의 데이터로 인해 조회 속도가 느려졌고, 이로 인해 많은 부하가 발생했을 때 DB Connection을 가져오는 것을 실패하는 현상 발생
  
* 사용자 요청 건수를 예측하고 JMeter를 통해 서비스 성능을 테스트
  *  https://velog.io/@wjdals05/%EC%84%B1%EB%8A%A5%ED%85%8C%EC%8A%A4%ED%8A%B8-JMeter%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%84%B1%EB%8A%A5%ED%85%8C%EC%8A%A4%ED%8A%B8    

* Batch Size 설정과 DTO 조회 방식을 비교한 후, 적용하여 쿼리를 최적화하고 N + 1 현상을 해결
  *   https://velog.io/@wjdals05/%EC%84%B1%EB%8A%A5%EA%B0%9C%EC%84%A0-%EC%BF%BC%EB%A6%AC-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0%EA%B8%B0

* 인덱스를 적용하고 EXPLAIN 명령어를 통해 인덱스가 효율적으로 사용되었는지 확인
  * https://velog.io/@wjdals05/%EC%84%B1%EB%8A%A5%EA%B0%9C%EC%84%A0-%EC%9D%B8%EB%8D%B1%EC%8A%A4-%EC%A0%81%EC%9A%A9
 
* 빠른 데이터 접근과 데이터베이스 부하 감소하기 위해 인메모리 기반 Redis 캐시 도입
  * https://velog.io/@wjdals05/Redis-%EC%A0%81%EC%9A%A9

* 쿼리 최적화, 인덱스 적용, Redis 캐시를 적용하여 오류를 해결하고 조회 성능을 개선

* Redis 분산 락을 통해 동시성 문제를 해결
  * https://velog.io/@wjdals05/%EB%8F%99%EC%8B%9C%EC%84%B1-%EC%9D%B4%EC%8A%88%EB%A5%BC-%ED%95%B4%EA%B2%B0%ED%95%98%EC%9E%90

* 하나의 서비스에 부하가 몰려 장애가 발생하는 것을 해결하기 위해 서버를 Scale-out하고 Nginx를 통해 로드 밸런싱하여 Scale-out된 서버에 요청 분산
  * https://velog.io/@wjdals05/Scale-out%EA%B3%BC-%EB%A1%9C%EB%93%9C%EB%B0%B8%EB%9F%B0%EC%8B%B1

* 데이터 베이스의 읽기 성능을 향상하고 데이터를 안전하게 보관하기 위해 DB Replication 적용
  *  https://velog.io/@wjdals05/%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4-%EB%A6%AC%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98
 
* 다중 서버 환경에 적합한 인증/인가 방식인 JWT 적용
  * https://velog.io/@wjdals05/%EB%8B%A4%EC%A4%91-%EC%84%9C%EB%B2%84-%ED%99%98%EA%B2%BD%EC%9D%84-%EA%B3%A0%EB%A0%A4%ED%95%9C-%EC%9D%B8%EC%A6%9D%EC%9D%B8%EA%B0%80

---

## 사용 기술
![stack drawio](https://github.com/user-attachments/assets/b01be63f-e277-4879-b7e7-673857d07d2f)

