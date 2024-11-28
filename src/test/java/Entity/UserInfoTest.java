package Entity;

import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Entity.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


import static org.junit.jupiter.api.Assertions.*;

public class UserInfoTest {
    private UserInfo userInfo;

    private UserInfo userInfo1;

    @BeforeEach
    public void setUp() {
        User user;
        User user1;
        user = new User();
        user1 = new User();
        user.setUsername("Nguyen Giang Minh");
        user1.setUsername("Louis Trieu");
        user.setPassword("namnamnam");
        user1.setPassword("tantantan");
        user.setRole(User.UserRole.ROLE_ADMIN);
        user1.setRole(User.UserRole.ROLE_USER);
        userInfo = new UserInfo(user);
        userInfo1 = new UserInfo(user1);
    }

    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userInfo.getAuthorities();
        assertNotNull(authorities);
        //Xac nhan la co 1 quyen
        assertEquals(1, authorities.size());
        //Xac nhan la quyen Admin
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    public void testGetAuthorities1() {
        Collection<? extends GrantedAuthority> authorities = userInfo1.getAuthorities();
        assertNotNull(authorities);
        //Xac nhan la co 1 quyen
        assertEquals(1, authorities.size());
        //Xac nhan la quyen Admin
        assertEquals("ROLE_USER", authorities.iterator().next().getAuthority());
    }

    @Test
    public void testGetPassword() {
        assertEquals("namnamnam", userInfo.getPassword());
    }

    @Test
    public void testGetPassword1() {
        assertEquals("tantantan", userInfo1.getPassword());
    }

    @Test
    public void testGetUsername() {
        assertEquals("Nguyen Giang Minh", userInfo.getUsername());
    }

    @Test
    public void testGetUsername1() {
        assertEquals("Louis Trieu", userInfo1.getUsername());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(userInfo.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonExpired1() {
        assertTrue(userInfo1.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(userInfo.isAccountNonLocked());
    }

    @Test
    public void testIsAccountNonLocked1() {
        assertTrue(userInfo1.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(userInfo.isCredentialsNonExpired());
    }

    @Test
    public void testIsCredentialsNonExpired1() {
        assertTrue(userInfo1.isCredentialsNonExpired());
    }

    @Test
    public void testIsCredentialsNonExpired2() {
        assertTrue(userInfo1.isCredentialsNonExpired());
    }
}
