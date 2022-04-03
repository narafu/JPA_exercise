package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 데이터 변경 X, 조회 최적화
@RequiredArgsConstructor // final field 생성자를 만들어준다. - lombok
public class MemberService {

    // 변경할 일이 없기 때문에 final 선언하기를 추천
    private final MemberRepository memberRepository;

    // 생성자가 하나만 있는 경우에는 스프링이 자동으로 bean을 injection 해주므로, @Autowird 생략 가능
    // field injection을 많이 사용하는데, field를 바꿀 수 없는 단점
    // setter injection은 그 값을 바꿀 수 있는 여지가 잇음.
    // 생성자 inejection이 좋다. 생성할 때 연관관계도 알 수 있고.
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
// --> 결론: fianl field + @RequiredArgsConstructor

    // 회원가입
    @Transactional(readOnly = false) // 클래스 전체 transcational 보다 우선순위 높다.
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 단건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
