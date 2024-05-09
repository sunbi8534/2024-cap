package cap.capServer.Controller;

import cap.capServer.Dto.LoginResultDto;
import cap.capServer.Dto.UserEnrollDto;
import cap.capServer.Service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Login-controller", description = "로그인 및 회원가입 관련 처리를 위한 컨트롤러 입니다.") //클래스에 대한 설명을 할 수 있는 어노테이션이다.
public class LoginController {
    LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공하면 닉네임 반환, 실패하면 false 문자열 반환")
    })
    @Operation(summary = "로그인 API", description = "id와 pw를 받아 로그인을 시도합니다.")
    @GetMapping("/user/login")
    public LoginResultDto login(
            @Parameter(description = "사용자 id", required = true, example = "sunbi8534")
            @RequestParam String id,
            @Parameter(description = "사용자 비밀번호", required = true, example = "8534")
            @RequestParam String pw) {
        System.out.println(id + pw);
        return loginService.checkUser(id, pw);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복이 없으면 ok 문자열 반환, 실패하면 false 문자열 반환")
    })
    @Operation(summary = "중복 닉네임 체크 API", description = "nickname을 받아서 기존 닉네임 중에 있는 닉네임인지 확인합니다.")
    @GetMapping("/user/dupNickname")
    public String checkDupNickname(
            @Parameter(description = "사용자 nickname", required = true, example = "sunbi")
            @RequestParam String nickname) {
        return loginService.checkDupNickname(nickname);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복이 없으면 ok 문자열 반환, 실패하면 false 문자열 반환")
    })
    @Operation(summary = "중복 ID 체크 API", description = "id를 받아서 기존 id 중에 있는 id인지 확인합니다.")
    @GetMapping("/user/dupId")
    public String checkDupId(
            @Parameter(description = "사용자 id", required = true, example = "sunbi8534")
            @RequestParam String id) {
        return loginService.checkDupId(id);
    }

    @Operation(summary = "회원가입 유저 등록 API", description = "id,pw,nickname 정보를 가지고 유저를 등록합니다.")
    @PostMapping("/user/enroll")
    public void enrollUser(
            @Parameter(description = "사용자 정보 객체", required = true)
            @RequestBody UserEnrollDto user) {
        loginService.enrollUser(user);
    }
}

