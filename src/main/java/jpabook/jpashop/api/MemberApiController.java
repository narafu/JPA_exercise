package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        // 앤티티는 자주 변경된다. 그런데 그 변경으로 인해 api 스펙이 바뀌면 안 된다.
        // api를 만들 때 앤티티를 클라이언트와 주고 받는 파라미터로 받지 말 것!
        // 앤티티를 외부에 노출하지 말 것!
        /** Member 앤티티를 변경하면(name -> userName) api 스펙이 바뀐다. */
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        // 엔티티를 바로 호출하지 말고 별도의 DTO를 만들어서 파라미터로 받아야 한다.
        // 또한, 앤티티를 파라미터로 하면, api마다 @Valid가 다를 경우 힘들어진다. api에 맞는 DTO를 만들면 그에 맞게 맞춰주면 된다.
        /** Member 앤티티를 변경해도(name -> userName) api 스펙이 바뀌지 않는다. */
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id); // 커맨드와 쿼리를 구분하기 위해, 다시 조회
        return new UpateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        // 앤티티를 반환하면, 앤티티 정보가 전부 노출된다.
        // @JsonIgnore를 사용하면 되지만, api마다 요구가 다를 경우 힘들어진다. 또한 화면 관련 소스가 포함되는 것이므로 좋지 않다.
        // 리스트를 바로 반환하면 유연성이 떨어진다.
        /** Member 앤티티를 변경하면(name -> userName) api 스펙이 바뀐다. */
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result membersV2() {
        // json을 한번 감쌌기 때문에, 유지보수성이 좋다.
        /** Member 앤티티를 변경해도(name -> userName) api 스펙이 바뀌지 않는다. */
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream().map(m -> new MemberDto(m.getName())).collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
}
