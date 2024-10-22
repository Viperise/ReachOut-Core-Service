package com.reachout.ReachoutSystem.users.resources;

import com.reachout.ReachoutSystem.users.dto.UserListResponseDTO;
import com.reachout.ReachoutSystem.users.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserListConverter {

    public static UserListResponseDTO userToUserListResponseConverter(User user) {
        var response = new UserListResponseDTO();

        response.setUid(user.getUid());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setStatus(String.valueOf(user.getStatus()));
        response.setRole(String.valueOf(user.getRole()));
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }

}

