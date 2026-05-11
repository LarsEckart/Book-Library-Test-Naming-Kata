package org.sammancoaching.library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberTest {

    @Test
    void testMemberNoId() {
        Executable createMemberWithoutId = () -> new Member(" ", "Alice");

        assertThrows(IllegalArgumentException.class, createMemberWithoutId);
    }

    @Test
    void testMemberNoName() {
        Executable createMemberWithoutName = () -> new Member("MEMBER-001", " ");

        assertThrows(IllegalArgumentException.class, createMemberWithoutName);
    }
}
