package com.example.eightyage.domain.auth.service;

import com.example.eightyage.domain.auth.dto.request.AuthSigninRequestDto;
import com.example.eightyage.domain.auth.dto.request.AuthSignupRequestDto;
import com.example.eightyage.domain.auth.dto.response.AuthTokensResponseDto;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.userrole.UserRole;
import com.example.eightyage.domain.user.service.UserService;
import com.example.eightyage.global.exception.BadRequestException;
import com.example.eightyage.global.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static com.example.eightyage.global.exception.ErrorMessage.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private TokenService tokenService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private AuthSignupRequestDto successSignupDto;
    private AuthSignupRequestDto passwordCheckErrorSignupDto;
    private AuthSigninRequestDto successSigninDto;
    private User user;

    @BeforeEach
    public void setUp() {
        passwordCheckErrorSignupDto = AuthSignupRequestDto.builder()
                .email("email@email.com")
                .nickname("nickname")
                .password("password1234")
                .passwordCheck("password1234!")
                .userRole("USER_ROLE")
                .build();

        successSignupDto = AuthSignupRequestDto.builder()
                .email("email@email.com")
                .nickname("nickname")
                .password("password1234")
                .passwordCheck("password1234")
                .userRole("USER_ROLE")
                .build();

        successSigninDto = AuthSigninRequestDto.builder()
                .email("email@email.com")
                .password("password1234")
                .build();

        user = User.builder()
                .email(successSignupDto.getEmail())
                .nickname(successSignupDto.getNickname())
                .userRole(UserRole.ROLE_USER)
                .build();

    }

    @Test
    void 회원가입_비밀번호_확인_불일치_실패() {
        // given

        // when & then
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> authService.signup(passwordCheckErrorSignupDto));
        assertEquals(badRequestException.getMessage(), PASSWORD_CONFIRMATION_MISMATCH.getMessage());
    }

    @Test
    void 회원가입_성공() {
        // given
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        given(userService.saveUser(any(String.class), any(String.class), any(String.class), any(String.class))).willReturn(user);
        given(tokenService.createAccessToken(any(User.class))).willReturn(accessToken);
        given(tokenService.createRefreshToken(any(User.class))).willReturn(refreshToken);

        // when
        AuthTokensResponseDto result = authService.signup(successSignupDto);

        // then
        assertEquals(accessToken, result.getAccessToken());
        assertEquals(refreshToken, result.getRefreshToken());
    }

    @Test
    void 로그인_삭제된_유저의_이메일일_경우_실패() {
        // given
        ReflectionTestUtils.setField(user, "deletedAt", LocalDateTime.now());

        given(userService.findUserByEmailOrElseThrow(any(String.class))).willReturn(user);

        // when & then
        UnauthorizedException unauthorizedException = assertThrows(UnauthorizedException.class,
                () -> authService.signin(successSigninDto));
        assertEquals(unauthorizedException.getMessage(), DEACTIVATED_USER_EMAIL.getMessage());
    }

    @Test
    void 로그인_비밀번호가_일치하지_않을_경우_실패() {
        // given
        ReflectionTestUtils.setField(user, "deletedAt", null);

        given(userService.findUserByEmailOrElseThrow(any(String.class))).willReturn(user);
        given(passwordEncoder.matches(successSigninDto.getPassword(), user.getPassword())).willReturn(false);

        // when & then
        UnauthorizedException unauthorizedException = assertThrows(UnauthorizedException.class,
                () -> authService.signin(successSigninDto));
        assertEquals(unauthorizedException.getMessage(), INVALID_PASSWORD.getMessage());
    }

    @Test
    void 로그인_성공() {
        // given
        ReflectionTestUtils.setField(user, "deletedAt", null);

        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        given(userService.findUserByEmailOrElseThrow(any(String.class))).willReturn(user);
        given(passwordEncoder.matches(successSigninDto.getPassword(), user.getPassword())).willReturn(true);
        given(tokenService.createAccessToken(any(User.class))).willReturn(accessToken);
        given(tokenService.createRefreshToken(any(User.class))).willReturn(refreshToken);

        // when
        AuthTokensResponseDto result = authService.signin(successSigninDto);

        // then
        assertEquals(accessToken, result.getAccessToken());
        assertEquals(refreshToken, result.getRefreshToken());
    }

    @Test
    void 토큰_재발급_성공() {
        // given
        String refreshToken = "refreshToken";

        String reissuedAccessToken = "reissued-accessToken";
        String reissuedRefreshToken = "reissued-refreshToken";

        given(tokenService.reissueToken(refreshToken)).willReturn(user);
        given(tokenService.createAccessToken(any(User.class))).willReturn(reissuedAccessToken);
        given(tokenService.createRefreshToken(any(User.class))).willReturn(reissuedRefreshToken);

        // when
        AuthTokensResponseDto result = authService.reissueAccessToken(refreshToken);

        // then
        assertEquals(reissuedAccessToken, result.getAccessToken());
        assertEquals(reissuedRefreshToken, result.getRefreshToken());
    }
}
