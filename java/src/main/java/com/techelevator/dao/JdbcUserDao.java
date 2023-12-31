package com.techelevator.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.techelevator.model.Book;
import com.techelevator.security.exception.DaoException;
import com.techelevator.security.model.RegisterUserDto;
import com.techelevator.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;

@Component
public class JdbcUserDao implements UserDao {

    private List<Book> bookList;
    private final JdbcTemplate jdbcTemplate;

 //   public JdbcUserDao(DataSource dataSource) {  // originally this was (JdbcTemplate jdbcTemplate)
    //       jdbcTemplate = new JdbcTemplate(dataSource);

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT user_id, family_id, username, first_name, last_name, password_hash, role, activated, is_child, avatar_url FROM users WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                user = mapRowToUser(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return user;
    }

    public User getUserByFamilyId(int familyId) {
        User user = null;
        String sql = "SELECT user_id, family_id, username, first_name, last_name, password_hash, role, activated, is_child, avatar_url FROM users WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, familyId);
            if (results.next()) {
                user = mapRowToUser(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return user;
    }


    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, family_id, username, first_name, last_name, password_hash, role, activated, is_child, avatar_url FROM users";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                User user = mapRowToUser(results);
                users.add(user);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return users;
    }

   @Override
    public User getUserByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("Username cannot be null");
        User user = null;
        String sql = "SELECT user_id, family_id, username, first_name, last_name, password_hash, role, activated, is_child, avatar_url FROM users WHERE username = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
            if (rowSet.next()) {
                user = mapRowToUser(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return user;
    }

    @Override
    public User createUser(RegisterUserDto user, int familyId) {
        return createUserWithRole(user, familyId, "ADMIN");
    }

    @Override
    public User createChildUser(RegisterUserDto childUser, int familyId) {
        return createUserWithRole(childUser, familyId, "CHILD");
    }

    private User createUserWithRole(RegisterUserDto newUser, int familyId, String role) {
        User createdUser = null;
//        String insertUserSql = "INSERT INTO users (family_id, username, first_name, last_name, password_hash, role, activated, is_child, avatar_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING user_id";
        String insertUserSql = "INSERT INTO users (family_id, username, first_name, last_name, password_hash, role, activated, is_child) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING user_id";

        String passwordHash = new BCryptPasswordEncoder().encode(newUser.getPassword());
        String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;

        try {
            int newUserId = jdbcTemplate.queryForObject(insertUserSql, int.class,
                    familyId, newUser.getUsername(), newUser.getFirstName(), newUser.getLastName(),
                  passwordHash, formattedRole, true, false/*, newUser.getAvatarUrl()*/);

            createdUser = getUserById(newUserId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return createdUser;
    }




    @Override
    public boolean deactivateFamilyMember(int id) {
        String sql = "UPDATE users SET activated = false WHERE user_id = ?;";
        try {
            int numRowsUpdated = jdbcTemplate.update(sql, id);
            return numRowsUpdated > 0;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
}

//    @Override
//    public boolean updateAvatar(int id, MultipartFile image) {
//        String sql = "UPDATE users SET avatar_url = ? WHERE user_id = ?;";
//        try {
//            int numRowsUpdated = jdbcTemplate.update(sql, image, id);
//            return numRowsUpdated > 0;
//        } catch (CannotGetJdbcConnectionException e) {
//            throw new DaoException("Unable to connect to server or database", e);
//        } catch (DataIntegrityViolationException e) {
//            throw new DaoException("Data integrity violation", e);
//        }
//    }

    public boolean deactivateFamily(int familyId) {
        String sql = "UPDATE users SET activated = false WHERE family_id = ?";
        try {
            int numRowsUpdated = jdbcTemplate.update(sql, familyId);
            return numRowsUpdated > 0;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }


    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setFamilyId(rs.getInt("family_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setChild(rs.getBoolean("is_child"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setAuthorities(Objects.requireNonNull(rs.getString("role")));
        user.setActivated(rs.getBoolean("activated"));
        user.setAvatarUrl(rs.getString("avatar_url"));
        return user;
    }
}
