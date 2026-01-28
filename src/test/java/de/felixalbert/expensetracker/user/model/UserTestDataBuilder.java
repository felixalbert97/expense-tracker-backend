package de.felixalbert.expensetracker.user.model;

public class UserTestDataBuilder {

    // ---------- Default values ----------
    private Long id;
    private String email = "test@test.com";
    private String password = "hashed-password";

    private UserTestDataBuilder() {
        // force factory method
    }

    // ---------- Factory methods ----------
    public static UserTestDataBuilder aUser() {
        return new UserTestDataBuilder();
    }

    public static UserTestDataBuilder anotherUser() {
        return aUser()
            .withEmail("other@test.com");
    }

    // ---------- Fluent modifiers ----------
    public UserTestDataBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserTestDataBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserTestDataBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    // ---------- Build ----------
    public User build() {
        User user = new User(id, email, password);
        return user;
    }

    // ---------- Convenience ----------
    public static User defaultUser() {
        return aUser().withId(1L).build();
    }
}