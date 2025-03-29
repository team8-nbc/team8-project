package com.example.eightyage.domain.auth.service;

import com.example.eightyage.domain.auth.entity.RefreshToken;
import com.example.eightyage.domain.auth.repository.RefreshTokenRepository;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.service.UserService;
import com.example.eightyage.global.util.JwtUtil;
import com.example.eightyage.global.exception.NotFoundException;
import com.example.eightyage.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.eightyage.domain.auth.tokenstate.TokenState.INVALIDATED;
import static com.example.eightyage.global.exception.ErrorMessage.EXPIRED_REFRESH_TOKEN;
import static com.example.eightyage.global.exception.ErrorMessage.REFRESH_TOKEN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /* Access Token 생성 */
    public String createAccessToken(User user) {
        return jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getNickname(), user.getUserRole());
    }

    /* Refresh Token 생성 */
    public String createRefreshToken(User user) {
        RefreshToken refreshToken = refreshTokenRepository.save(new RefreshToken(user.getId()));
        return refreshToken.getToken();
    }

    /* Refresh Token 유효성 검사 */
    public User reissueToken(String token) {

        RefreshToken refreshToken = findByTokenOrElseThrow(token);

        if (refreshToken.getTokenState() == INVALIDATED) {
            throw new UnauthorizedException(EXPIRED_REFRESH_TOKEN.getMessage());
        }
        refreshToken.updateTokenStatus(INVALIDATED);

        return userService.findUserByIdOrElseThrow(refreshToken.getUserId());
    }

    private RefreshToken findByTokenOrElseThrow(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new NotFoundException(REFRESH_TOKEN_NOT_FOUND.getMessage()));
    }
}