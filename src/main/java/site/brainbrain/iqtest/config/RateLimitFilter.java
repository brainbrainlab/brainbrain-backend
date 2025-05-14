package site.brainbrain.iqtest.config;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final IpRateLimiter rateLimiter;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        if (isRateLimitedPath(request) && rateLimiter.isRateLimited(request.getRemoteAddr())) {
            rejectRequest(response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isRateLimitedPath(final HttpServletRequest request) {
        return request.getRequestURI().startsWith("/coupons");
    }

    private void rejectRequest(final HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("text/plain; charset=utf-8");
        response.getWriter().write("요청 수를 초과했습니다. 잠시 후 다시 시도해주세요.");
    }
}
