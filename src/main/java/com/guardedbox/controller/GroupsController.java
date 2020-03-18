package com.guardedbox.controller;

import static com.guardedbox.constants.Api.API_BASE_PATH;
import static com.guardedbox.constants.Constraints.EMAIL_MAX_LENGTH;
import static com.guardedbox.constants.Constraints.EMAIL_MIN_LENGTH;
import static com.guardedbox.constants.Constraints.EMAIL_PATTERN;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guardedbox.dto.AccountDto;
import com.guardedbox.dto.AddParticipantToGroupDto;
import com.guardedbox.dto.AddSecretToGroupDto;
import com.guardedbox.dto.CreateGroupDto;
import com.guardedbox.dto.GroupDto;
import com.guardedbox.dto.SecretDto;
import com.guardedbox.dto.SuccessDto;
import com.guardedbox.service.SessionAccountService;
import com.guardedbox.service.transactional.GroupsService;

import lombok.RequiredArgsConstructor;

/**
 * Controller: Groups.
 *
 * @author s3curitybug@gmail.com
 *
 */
@RestController
@RequestMapping(API_BASE_PATH + "groups")
@Validated
@RequiredArgsConstructor
public class GroupsController {

    /** GroupsService. */
    private final GroupsService groupsService;

    /** SessionAccountService. */
    private final SessionAccountService sessionAccount;

    /**
     * @return All the groups belonging to the current session account.
     */
    @GetMapping("/owned")
    public List<GroupDto> getOwnedGroups() {

        return groupsService.getGroupsByOwnerAccountId(sessionAccount.getAccountId());

    }

    /**
     * @return All the groups in which the current session account is participant.
     */
    @GetMapping("/invited")
    public List<GroupDto> getInvitedGroups() {

        return groupsService.getGroupsByInvitedAccountId(sessionAccount.getAccountId());

    }

    /**
     * @param groupId An ID representing a group.
     * @return The participants of the group corresponding to the introduced ID.
     */
    @GetMapping("/{group-id}/participants")
    public List<AccountDto> getGroupParticipants(
            @PathVariable(name = "group-id", required = true) @NotNull UUID groupId) {

        return groupsService.getGroupParticipants(sessionAccount.getAccountId(), groupId);

    }

    /**
     * @param groupId An ID representing a group.
     * @return The secrets of the group corresponding to the introduced ID.
     */
    @GetMapping("/{group-id}/secrets")
    public List<SecretDto> getGroupSecrets(
            @PathVariable(name = "group-id", required = true) @NotNull UUID groupId) {

        return groupsService.getGroupSecrets(sessionAccount.getAccountId(), groupId);

    }

    /**
     * Creates Group, belonging to the current session account.
     *
     * @param createGroupDto Object with the necessary data to create a Group.
     * @return Object with the stored group data.
     */
    @PostMapping()
    public GroupDto createGroup(
            @RequestBody(required = true) @Valid CreateGroupDto createGroupDto) {

        return groupsService.createGroup(sessionAccount.getAccountId(), createGroupDto);

    }

    /**
     * Adds a participant to a group belonging to the current session account.
     *
     * @param groupId The group ID.
     * @param addParticipantToGroupDto Object with the necessary data to add a participant to a group.
     * @return Object indicating if the execution was successful.
     */
    @PostMapping("/{group-id}/participants")
    public SuccessDto addParticipantToGroup(
            @PathVariable(name = "group-id", required = true) @NotNull UUID groupId,
            @RequestBody(required = true) AddParticipantToGroupDto addParticipantToGroupDto) {

        addParticipantToGroupDto.setGroupId(groupId);
        groupsService.addParticipantToGroup(sessionAccount.getAccountId(), addParticipantToGroupDto);
        return new SuccessDto(true);

    }

    /**
     * Adds a secret to a group belonging to the current session account.
     *
     * @param groupId The group ID.
     * @param addSecretToGroupDto Object with the necessary data to add a secret to a group.
     * @return Object with the stored secret data.
     */
    @PostMapping("/{group-id}/secrets")
    public SecretDto addSecretToGroup(
            @PathVariable(name = "group-id", required = true) @NotNull UUID groupId,
            @RequestBody(required = true) AddSecretToGroupDto addSecretToGroupDto) {

        addSecretToGroupDto.setGroupId(groupId);
        return groupsService.addSecretToGroup(sessionAccount.getAccountId(), addSecretToGroupDto);

    }

    /**
     * Deletes a group belonging to the current session account.
     *
     * @param groupId The group ID.
     * @return Object indicating if the execution was successful.
     */
    @DeleteMapping("/{group-id}")
    public SuccessDto deleteGroup(
            @PathVariable(name = "group-id", required = true) @NotNull UUID groupId) {

        groupsService.deleteGroup(sessionAccount.getAccountId(), groupId);
        return new SuccessDto(true);

    }

    /**
     * Removes a participant from a group belonging to the current session account.
     *
     * @param groupId The group ID.
     * @param email Email of the participant to be removed from the group.
     * @return Object indicating if the execution was successful.
     */
    @DeleteMapping("/{group-id}/participants")
    public SuccessDto removeParticipantFromGroup(
            @PathVariable(name = "group-id", required = true) @NotNull UUID groupId,
            @RequestParam(name = "email", required = true) @NotBlank @Email(regexp = EMAIL_PATTERN) @Size(min = EMAIL_MIN_LENGTH, max = EMAIL_MAX_LENGTH) String email) {

        groupsService.removeParticipantFromGroup(sessionAccount.getAccountId(), groupId, email);
        return new SuccessDto(true);

    }

    /**
     * Deletes a secret from a group belonging to the current session account.
     *
     * @param groupId The group ID.
     * @param secretId The secret ID.
     * @return Object indicating if the execution was successful.
     */
    @DeleteMapping("/{group-id}/secrets/{secret-id}")
    public SuccessDto deleteSecretFromGroup(
            @PathVariable(name = "group-id", required = true) @NotNull UUID groupId,
            @PathVariable(name = "secret-id", required = true) @NotNull UUID secretId) {

        groupsService.deleteSecretFromGroup(sessionAccount.getAccountId(), groupId, secretId);
        return new SuccessDto(true);

    }

}
