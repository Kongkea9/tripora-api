package tripora.api.Util.security;

import org.springframework.security.core.context.SecurityContextHolder;
import tripora.api.security.UserPrincipal;

public class SecurityUtil {

    public static UserPrincipal getCurrentUser() {
        return (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public static Integer getCurrentUserId() {
        return getCurrentUser().id();
    }
}